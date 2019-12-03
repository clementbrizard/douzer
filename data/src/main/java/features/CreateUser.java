package features;

import core.Datacore;
import datamodel.LocalUser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

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
      throws IOException {
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

    dc.getLocalUsersFileHandler().add(user);
    Login.run(dc, user);
  }
}


