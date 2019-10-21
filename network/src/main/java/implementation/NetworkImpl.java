package implementation;

import java.net.InetAddress;
import java.util.stream.Stream;

import provider.NetworkProvider;

/**
 * Implements the Net interface that provides useful mathods to send messages through the
 * network. Most of the times, simply redirect the request to the network provider because
 * every thread-management is processed in provider.
 * 
 * @author Antoine
 *
 */
public class NetworkImpl implements Net {

  private NetworkProvider netProvider;
  
  /**
   * COnstructor that initializes the networkprovider, used to create the threads.
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
  public void sendToUser(String payload, InetAddress ipDest) {
    this.netProvider.createSendToUserThread(payload, ipDest);
  }
  
  /**
   * Sends a message containing the string payload to the given ipS.
   * 
   * @param payload content of the message
   * @param ipsDest ip addresses of the receivers
   */
  public void sendToUsers(String payload, Stream<InetAddress> ipsDest) {
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
  public void connect(String payload, Stream<InetAddress> knownIPs) {
    this.netProvider.createServer();
    sendToUsers(payload, knownIPs);
  }
  
  /**
   * TODO.
   * 
   * @param payload data to inform the network we are disconnected
   * @param knownIPs known ips in the network
   */
  public void disconnect(String payload, Stream<InetAddress> knownIPs) {
    return;
  }
}
