package threads;

import java.io.Serializable;
import java.net.InetAddress;

public class SendToUserThread extends ThreadExtend {
  private Serializable payload;
  private InetAddress ipDest;

  public SendToUserThread(Serializable p, InetAddress ip) {
    this.payload = p;
    this.ipDest = ip;
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
