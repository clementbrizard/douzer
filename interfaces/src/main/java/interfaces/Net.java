package interfaces;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collection;
import java.util.stream.Stream;

public interface Net {
  void sendToUser(Serializable payload, InetAddress ip);

  void sendToUsers(Serializable payload, Stream<InetAddress> ips);

  void requestDownload(Stream<InetAddress> ownerIps, String musicHash);

  void connect(Serializable payload, Collection<InetAddress> ips);

  void disconnect(Serializable payload, Collection<InetAddress> ips);
}
