package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;
import datamodel.User;
import java.util.UUID;

public class LogoutPayload extends Payload {
  private UUID uuid;

  public LogoutPayload(LocalUser user, UUID uuid) {
    super(user);
    this.uuid = uuid;
  }

  /**
   * Notify IHM that a certain User is disconnected. Also remove its musics ownerships.
   */
  @Override
  public void run(Datacore dc) {
    User disconnectedUser = dc.getUser(uuid);
    // this line is not useless if the user is referenced is a friend
    disconnectedUser.setConnected(false);
    dc.ihm.notifyUserDisconnection(disconnectedUser);
    dc.removeOwner(disconnectedUser);
    dc.removeUser(disconnectedUser);
  }
}
