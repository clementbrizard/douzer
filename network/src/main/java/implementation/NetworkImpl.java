package implementation;

import interfaces.Net;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import provider.NetworkProvider;
import threads.ThreadExtend;

/**
 * Implements the Net interface that provides useful methods to send messages through the
 * network. Most of the times, simply redirect the request to the network provider because
 * every thread-management is processed in provider.
 * 
 * @author Antoine
 *
 */
public class NetworkImpl implements Net {

  private NetworkProvider netProvider;
  
  /**
   * Constructor that initializes the networkprovider, used to create the threads.
   * 
   * @param np current instance of the network provider
   */
  public NetworkImpl(NetworkProvider np) {
    this.netProvider = np;
  }
  
  @Override
  public void sendToUser(Serializable payload, InetAddress ipDest) {
    this.netProvider.createServer();
    this.netProvider.createSendToUserThread(payload, ipDest);
  }
  
  @Override
  public void sendToUsers(Serializable payload, Stream<InetAddress> ipsDest) {
    this.netProvider.createServer();
    ipsDest.forEach((ip) -> netProvider.createSendToUserThread(payload, ip));
  }
  
  @Override
  public void requestDownload(Stream<InetAddress> sourcesIPs, String musicHash) {
    this.netProvider.createServer();
    this.netProvider.createRequestDownloadThread(sourcesIPs, musicHash);
  }
  
  
  @Override
  public void disconnect(Serializable payload, Collection<InetAddress> knownIPs) {
    //Kill server thread (we don't want to receive any messages)
    this.netProvider.createServer().kill();
    
    //Wait for the network to be fully notified
    this.netProvider.getNetwork().sendToUsers(payload, knownIPs.stream());
    
    //Then kill every running threads
    for (ThreadExtend thread : this.netProvider.getThreads()) {
      if (thread.isAlive()) {
        thread.kill();
      }
    }
    
    //Clears the list of threads
    this.netProvider.getThreads().clear();
  }

  @Override
  public void createServer() {
    netProvider.createServer();
  }
}
