package features;

import core.Datacore;
import datamodel.LocalUser;
import exceptions.data.LocalUsersFileException;

import java.io.File;
import java.io.IOException;

public abstract class DeleteUser {
  /**
   * Delete an account.
   * Remove user from the lo23-users.ser file.
   * Delete the properties file linked to the user we want to delete.
   * Wipe all useless data and set current user to null
   *
   * @param dc Datacore
   */
  public static void run(Datacore dc) throws IOException {
    LocalUser user = dc.getCurrentUser();
    try {
      dc.getLocalUsersFileHandler().remove(user);
    } catch (LocalUsersFileException e) {
      throw new IOException(e);
    }

    File propFileToDelete = user.getSavePath().resolve(user.getUsername()
        + "-config.properties").toFile();
    if (propFileToDelete.exists() && propFileToDelete.delete()) {
      System.out.println("Properties file deleted");
    } else {
      System.out.println("Properties file for this user does not exist. Can't be deleted");
    }

    dc.wipe();
  }

}



