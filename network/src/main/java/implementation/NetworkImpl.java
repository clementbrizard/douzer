package implementation;

import java.net.InetAddress;
import java.util.Collection;
import java.util.stream.Stream;

import provider.NetworkProvider;

public class NetworkImpl implements Net {

  private NetworkProvider netProvider;
  
  public NetworkImpl(NetworkProvider np) {
    this.netProvider = np;
  }
  
  public void sendToUser(String payload, InetAddress ipDets) {
    
    return;
  }
  
  public void sendToUsers(String payload, Stream<InetAddress> ipsDest) {
    return;
  }
  
  public void requestDownload(Stream<InetAddress> sourcesIPs, String musicHash) {
    return;
  }
  
  public void connect(String payload, Collection<InetAddress> knownIPs) {
    return;
  }
  
  public void disconnect(String payload, Collection<InetAddress> knownIPs) {
    return;
  }
}
