package features;

import core.Datacore;
import datamodel.LocalUser;
import exceptions.LocalUsersFileException;

import java.io.File;
import java.io.IOException;

public abstract class DeleteUser {
  /**
   * Delete an account.
   * Delete the properties file linked to the user we want to delete
   * Remove user from the lo23-users.ser file.
   * Wipe all useless data and set current user to null
   *
   * @param dc Datacore
   */
  public static void run(Datacore dc) throws IOException {
    LocalUser user = dc.getCurrentUser();
    File propFileToDelete = new File(user.getSavePath().resolve(user.getUsername()
        + "-config.properties").toString());
    if (propFileToDelete.exists() && propFileToDelete.delete()) {
      System.out.println("Properties file deleted");
    } else {
      System.out.println("Properties file for this user does not exist. Can't be deleted");
    }

    try {
      dc.getLocalUsersFileHandler().remove(user);
    } catch (LocalUsersFileException e) {
      throw new IOException(e);
    }
    dc.wipe();
  }

}



