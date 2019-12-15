package features;

import core.Datacore;
import datamodel.LocalUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class Logout {

  /**
   *  Logout the current user and send LogoutPayload to everyone.
   * @param dc The Datacore
   * @throws IOException if the properties file can't be accessed
   */
  public static void run(Datacore dc) throws IOException {
    LocalUser currentUser = dc.getCurrentUser();
    currentUser.setConnected(false);
    // Updates the written currentUser in case it has been modified
    dc.getLocalUsersFileHandler().update(currentUser);

    LogoutPayload payload = new LogoutPayload(dc.getCurrentUser());
    dc.net.disconnect(payload, dc.getOnlineIps().collect(Collectors.toList()));

    Properties prop = new Properties();
    // TODO: template for filename
    Path userPropFilePath = currentUser.getSavePath()
        .resolve(currentUser.getUsername() + "-config.properties");
    File userConfigFile = userPropFilePath.toFile();

    if (userConfigFile.exists()) {
      prop.load(new FileInputStream(userPropFilePath.toString()));
      String ipsStr = dc.getAllIps().stream()
          .map(InetAddress::getHostAddress)
          .collect(Collectors.joining(","));
      prop.setProperty("ips", ipsStr);
      prop.store(new FileOutputStream(userPropFilePath.toString()), null);
    } else {
      throw new FileNotFoundException("Warning: user property file not found in the save path");
    }

    dc.wipe();
  }
}
