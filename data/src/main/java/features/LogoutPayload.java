package features;

import core.Datacore;
import core.Payload;
import datamodel.User;

public class LogoutPayload extends Payload {
  private User disconnectedUser;

  public LogoutPayload(User disconnectedUser) {
    this.disconnectedUser = disconnectedUser;
  }

  /**
   * Notify IHM that a certain User is disconnected. Also remove its musics ownerships.
   */
  @Override
  public void run(Datacore dc) {
    dc.ihm.notifyUserDisconnection(this.disconnectedUser);
    dc.removeOwner(this.disconnectedUser);
  }
}
