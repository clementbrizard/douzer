package features;

import core.Datacore;
import datamodel.LocalUser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;

public abstract class Login {
  private static InetAddress getIpFromString(String ip) {
    if (ip.isEmpty()) {
      return null;
    }

    try {
      return InetAddress.getByName(ip);
    } catch (UnknownHostException e) {
      e.printStackTrace();
      return null;
    }
  }

  private static Set<InetAddress> getInitialIpsFromConfig(Path filePath)
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
        .collect(Collectors.toSet());
  }

  /**
   * Load the requested user from the "users.ser" file and send login handshakes to every ip
   * in the "config.properties" file.
   *
   * @param username Username of the requested user
   * @param password Password of the requested user
   * @throws IOException    When the users file or config file can't be read
   * @throws LoginException When the user can't be found
   */
  public static void run(Datacore dc, String username, String password)
      throws LoginException, IOException {
    try {
      LocalUser user = dc.getLocalUsersFileHandler().getUser(username);

      if (!user.verifyPassword(password)) {
        throw new LoginException("Wrong user password");
      }

      run(dc, user);
    } catch (NullPointerException e) {
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
    user.setConnected(true);
    dc.setCurrentUser(user);
    dc.addUser(user);
    user.getLocalMusics().forEach(dc::addMusic);
    user.getFriends().forEach(dc::addUser);

    // TODO: template for filename
    Path configPath = user.getSavePath().resolve(user.getUsername() + "-config.properties");
    Set<InetAddress> ips = getInitialIpsFromConfig(configPath);
    dc.setAllIps((HashSet<InetAddress>) ips);

    dc.net.createServer();
    for (InetAddress ip : ips) {
      // iterate and don't send ip of user A to A
      HashSet<InetAddress> ipsToShare = (HashSet<InetAddress>) ((HashSet<InetAddress>) ips).clone();
      ipsToShare.remove(ip);
      LoginPayload payload = new LoginPayload(user, ipsToShare);
      dc.net.sendToUser(payload, ip);
    }
  }
}
