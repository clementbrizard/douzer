package features;

import core.Datacore;
import datamodel.LocalUser;

import java.io.File;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DeleteUser {
  private static final Logger deleteUserLogger = LogManager.getLogger();

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
    dc.getLocalUsersFileHandler().remove(user);

    File propFileToDelete = user.getSavePath().resolve(user.getUsername()
        + "-config.properties").toFile();
    if (propFileToDelete.exists() && propFileToDelete.delete()) {
      deleteUserLogger.info("Properties file deleted");
    } else {
      deleteUserLogger.info("Properties file for this user does not exist. Can't be deleted");
    }

    dc.wipe();
  }

}



