package implementation;

import java.io.Serializable;
import java.net.InetAddress;
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
  
  /**
   * Sends a message containing the string payload to the given ip.
   * 
   * @param payload content of the message
   * @param ipDest ip address of the receiver
   */
  public void sendToUser(Serializable payload, InetAddress ipDest) {
    this.netProvider.createSendToUserThread(payload, ipDest);
  }
  
  /**
   * Sends a message containing the string payload to the given ipS.
   * 
   * @param payload content of the message
   * @param ipsDest ip addresses of the receivers
   */
  public void sendToUsers(Serializable payload, Stream<InetAddress> ipsDest) {
    this.netProvider.createSendToUsersThread(payload, ipsDest);
  }
  
  /**
   * TODO.
   * 
   * @param sourcesIPs ips where the music can be downloaded
   * @param musicHash hash of the music to download
   */
  public void requestDownload(Stream<InetAddress> sourcesIPs, String musicHash) {
    return;
  }
  
  /**
   * Connect the user to the network. Create a server thread to listen network and
   * sends a payload to the known network.
   * 
   * @param payload data to transmit to the network
   * @param knownIPs known nodes of the network
   */
  public void connect(Serializable payload, Stream<InetAddress> knownIPs) {
    this.netProvider.createServer();
    sendToUsers(payload, knownIPs);
  }
  
  /**
   * Stop all interactions between user and network and notifies the network that
   * the user has been disconnected.
   * 
   * @param payload data to inform the network we are disconnected
   * @param knownIPs known ips in the network
   */
  public void disconnect(Serializable payload, Stream<InetAddress> knownIPs) {
    //Kill server thread (we don't want to receive any messages)
    this.netProvider.createServer().kill();
    
    //Wait for the network to be fully notified
    this.netProvider.createSendToUsersThread(payload, knownIPs).join();
    
    //Then kill every running threads
    for (ThreadExtend thread : this.netProvider.getThreads()) {
      if (thread.isAlive()) {
        thread.kill();
      }
    }
    
    //Clears the list of threads
    this.netProvider.getThreads().clear();
  }
}
