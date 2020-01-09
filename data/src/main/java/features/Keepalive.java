package features;

import core.Datacore;
import datamodel.User;

import java.util.LinkedList;
import java.util.TimerTask;

/**
 * Class that sends message in background to check if known users are
 * still connected to the application.
 * 
 * @author Antoine
 *
 */
public class Keepalive extends TimerTask {

  private Datacore datacore;
  private LinkedList<User> waitingList = new LinkedList<User>();
  
  public Keepalive(Datacore dc) {
    this.datacore = dc;
  }
  
  /**
   * Notify the application that the sender is still connected.
   * 
   * @param user user that notifies it is still connected.
   */
  public void notifyAlive(User user) {
    waitingList.remove(user);
  }
 
  /**
   * Core method of keepalive process. Add all online user in waiting list.
   * When user answer, it is remove from waiting list. When delay expires,
   * users still in waiting list are considered offline.
   */
  @Override
  public void run() {
    Datacore.getLogger().info("Starting keepalive process...");
    
    //Each user that has not answered in time is considered offline
    while (waitingList.size() > 0) {
      User disconnectedUser = waitingList.pop();
      disconnectedUser.setConnected(false);
      this.datacore.ihm.notifyUserDisconnection(disconnectedUser);
      this.datacore.removeOwner(disconnectedUser);
      this.datacore.removeUser(disconnectedUser);
    }
    
    //Fill new list and sends payload
    this.datacore.getOnlineUsers().forEach((user) -> {
      waitingList.add(user);
      KeepalivePayload payload = new KeepalivePayload(this.datacore.getCurrentUser());
      payload.setReceiver(user);
      this.datacore.net.sendToUser(payload, user.getIp());
    });
  }
}
