package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;
import exceptions.LocalUsersFileException;

public class LogoutPayload extends Payload {
  /**
   * Remove the current user from all its owned musics and notify IHM.
   */
  @Override
  public void run(Datacore dc) {
    LocalUser currentUser = dc.getCurrentUser();

    try {
      dc.getLocalUsersFileHandler().update(currentUser);
    } catch (LocalUsersFileException e) {
      e.printStackTrace();
    }

    dc.ihm.notifyUserDisconnection(currentUser);
    dc.removeOwner(currentUser);
    dc.wipe();
  }
}
