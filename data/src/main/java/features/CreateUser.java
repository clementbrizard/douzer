package features;

import core.Datacore;
import datamodel.LocalUser;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import javax.security.auth.login.LoginException;

public abstract class CreateUser {
  /**
   * Check if there is a config.properties file in the user's save path.
   * If there is no config file
   * yet, create a config.properties file by copy of default-config.properties
   * default-config.properties is located in resources/
   * Then the function is calling Login.run function by giving
   * the Datacore object and the LocalUser
   *
   * @param user LocalUser given by IHM
   */
  public static void run(LocalUser user, Datacore dc, InputStream defaultPropInputStream)
      throws IOException, LoginException {
    //Create file config.properties
    Properties defaultProp = new Properties();
    // TODO: template for filename
    Path userPropFilePath = user.getSavePath().resolve(user.getUsername() + "-config.properties");
    File userConfigFile = new File(userPropFilePath.toString());

    //If there is no config file for our user in the his path
    if (!userConfigFile.exists()) {
      if (defaultPropInputStream != null) {
        defaultProp.load(defaultPropInputStream);
      } else {
        throw new FileNotFoundException(
            "Warning: default property file not found in the resources path");
      }
      defaultProp.store(new FileOutputStream(userPropFilePath.toString()), null);
    }

    File localUsersFile = Paths.get(Datacore.LOCAL_USERS_FILENAME).toFile();
    if (!localUsersFile.exists()) {
      // not yet created, so we assume it is the first user
      Login.run(dc, user);
    } else {
      FileInputStream file = new FileInputStream(localUsersFile);
      ObjectInputStream reader = new ObjectInputStream(file);
      LocalUser otherUser;
      try {
        do {
          otherUser = (LocalUser) reader.readObject();
        } while (!(user.getUsername().equals(otherUser.getUsername())));
      } catch (EOFException e) {
        //Log user immediately after creation
        Login.run(dc, user);
        return;
      } catch (ClassNotFoundException e) {
        throw new LoginException("Local save may be corrupted or outdated");
      }
      throw new LoginException(("Username already exists"));
    }
  }
}


