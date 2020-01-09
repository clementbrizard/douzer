package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;
import datamodel.User;

import java.net.InetAddress;

/**
 * Simple payload that sends an answer to the sender. Process
 * is overload in order to have the sender IP address.
 * 
 * @author Antoine
 *
 */
public class KeepalivePayload extends Payload {

  private boolean received = false;
  private User receiver;
  private InetAddress senderIP;
  
  public KeepalivePayload(LocalUser sender) {
    super(sender);
  }

  public void setReceiver(User r) {
    this.receiver = r;
  }

  @Override
  public void process(Datacore dc, InetAddress senderIP) {
    this.senderIP = senderIP;
    this.run(dc);
  }

  @Override
  public void run(Datacore dc) {
    if (!received) {
      received = true;
      dc.net.sendToUser(this, this.senderIP);
    } else {
      dc.getKeepAlive().notifyAlive(this.receiver);
    }
  }

}
