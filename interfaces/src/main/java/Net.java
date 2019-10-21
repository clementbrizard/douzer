import java.net.InetAddress;
import java.util.Collection;
import java.util.stream.Stream;

public interface Net {
  void sendToUser(String payload, InetAddress ip);

  void sendToUsers(String payload, Stream<InetAddress> ips);

  void requestDownload(Stream<InetAddress> ownerIPs, String musicHash);

  void connect(String payload, Collection<InetAddress> ips);

  void disconnect(String payload, Collection<InetAddress> ips);
}
