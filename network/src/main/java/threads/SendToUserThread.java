package threads;

import java.net.InetAddress;

public class SendToUserThread extends ThreadExtend {
  private String payload;
  private InetAddress ipDest;

  public SendToUserThread(String p, InetAddress ip) {
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
