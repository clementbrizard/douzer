package threads;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.stream.Stream;

public class SendToUsersThread extends ThreadExtend {

  private Serializable payload;
  private Stream<InetAddress> usersIps;

  public SendToUsersThread(Serializable p, Stream<InetAddress> s) {
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
