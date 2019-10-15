package threads;

import java.net.InetAddress;
import java.util.stream.Stream;

public class SendToUsersThread extends ThreadExtend {

  private Payload payload;
  private Stream<InetAddress> usersIps;

  public SendToUsersThread(Payload p, Stream<InetAddress> s) {
    this.payload = p;
    this.usersIps = s;
  }

  @Override
  public void run() {
    //To be define
  }

  @Override
  public void kill() {
    //To be define
  }
}
