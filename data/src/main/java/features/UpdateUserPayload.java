package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;
import datamodel.User;

/**
 * Payload to share in order to update User.
 */
public class UpdateUserPayload extends Payload {
  private User user;

  public UpdateUserPayload(LocalUser u) {
    this.user = new User(u);
  }

  /**
   * Give the updated User to ihm.
   */
  @Override
  public void run(Datacore dc) {
    dc.ihm.updateUser(this.user);
  }
}