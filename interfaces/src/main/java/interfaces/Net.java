package interfaces;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.stream.Stream;

public interface Net {
  
  /**
   * Sends a message containing the string payload to the given ip.
   * If the current user is not connected then it conects it.
   * 
   * @param payload content of the message
   * @param ipDest ip address of the receiver
   */
  void sendToUser(Serializable payload, InetAddress ipDest);

  /**
   * Sends a message containing the string payload to the given ipS.
   * If the current user is not connected then it conects it.
   * 
   * @param payload content of the message
   * @param ipsDest ip addresses of the receivers
   */
  void sendToUsers(Serializable payload, Stream<InetAddress> ipsDest);

  /**
   * Sends a request to download the distant file identified by the given
   * hash from differents IPs (list of users who have the mp3). The list is
   * used to manage errors
   * 
   * @param ownerIps ips where the music can be downloaded
   * @param musicHash hash of the music to download
   */
  void requestDownload(Stream<InetAddress> ownerIps, String musicHash);

  /**
   * Stop all interactions between user and network and notifies the network that
   * the user has been disconnected.
   * 
   * @param payload data to inform the network we are disconnected
   * @param knownIPs known ips in the network
   */
  void disconnect(Serializable payload, Collection<InetAddress> knownIPs);
}
