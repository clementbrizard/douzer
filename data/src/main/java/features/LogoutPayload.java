package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;

public class LogoutPayload extends Payload {
  /**
   * Remove the current user from all its owned musics and notify IHM.
   */
  @Override
  public void run(Datacore dc) {
    LocalUser currentUser = dc.getCurrentUser();
    dc.ihm.notifyUserDisconnection(currentUser);
    dc.removeOwner(currentUser);
    dc.wipe();
  }
}
