package features;

import core.Datacore;
import datamodel.LocalUser;
import exceptions.LocalUsersFileException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;

public abstract class Login {
  private static InetAddress getIpFromString(String ip) {
    try {
      return InetAddress.getByName(ip);
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static Collection<InetAddress> getInitialIpsFromConfig(Path filePath)
      throws IOException {
    Properties prop = new Properties();
    InputStream is = new FileInputStream(filePath.toFile());
    prop.load(is);
    is.close();
    String ipsString = prop.getProperty("ips");
    return Arrays.stream(ipsString.split(","))
        .map(String::trim)
        .map(Login::getIpFromString)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  /**
   * Load the requested user from the "users.ser" file and send login handshakes to every ip
   * in the "config.properties" file.
   *
   * @param username Username of the requested user
   * @param password Password of the requested user
   * @throws IOException When the users file or config file can't be read
   * @throws LoginException When the user can't be found
   */
  public static void run(Datacore dc, String username, String password)
      throws LoginException, IOException {
    try {
      LocalUser user = dc.getLocalUsersFileHandler().getUser(username);

      if (!user.verifyPassword(password)) {
        throw new LocalUsersFileException("Wrong user password");
      }

      run(dc, user);
    } catch (LocalUsersFileException e) {
      throw new LoginException("No such user found");
    }
  }

  /**
   * Log the given user in and send login handshakes to every ip in the "config.properties" file.
   * This method should  be called after an account creation.
   *
   * @param user The local user that is logging in
   * @throws IOException When the config file can't be read
   */
  public static void run(Datacore dc, LocalUser user)
      throws IOException {
    dc.setCurrentUser(user);
    dc.addUser(user);
    user.getMusics().forEach(dc::addMusic);

    LoginPayload payload = new LoginPayload(user);
    Path configPath = user.getSavePath().resolve("config.properties");
    dc.net.connect(payload, getInitialIpsFromConfig(configPath));
  }
}
