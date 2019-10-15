package threads;

import java.net.InetAddress;
import java.util.stream.Stream;

public class RequestDownloadThread extends ThreadExtend {

  private Stream<InetAddress> ownersIps;
  private String musicHash;

  public RequestDownloadThread(Stream<InetAddress> ownersIps, String musicHash) {
    this.ownersIps = ownersIps;
    this.musicHash = musicHash;
  }

  @Override
  public void run() {
    
  }

  @Override
  public void kill() {

  }
}
