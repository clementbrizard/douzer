package features;

import core.Datacore;
import datamodel.LocalUser;
import datamodel.Music;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;

public abstract class Login {
  private static LocalUser loadUserFromDisk(Path filePath, String username, String password)
      throws IOException, LoginException {
    FileInputStream file = new FileInputStream(filePath.toFile());
    ObjectInputStream reader = new ObjectInputStream(file);
    LocalUser user;
    try {
      do {
        user = (LocalUser) reader.readObject();
      } while (!(user.getUsername().equals(username) && user.verifyPassword(password)));
    } catch (EOFException e) {
      throw new LoginException("No such user found");
    } catch (ClassNotFoundException e) {
      throw new LoginException("Local save may be corrupted or outdated");
    }

    return user;
  }

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
   * @throws IOException    When the users file or config file can't be read
   * @throws LoginException When the user can't be found
   */
  public static void run(Datacore dc, String username, String password)
      throws IOException, LoginException {
    Path savePath = Paths.get("").toAbsolutePath();
    LocalUser user = loadUserFromDisk(savePath.resolve(dc.LOCAL_USERS_FILENAME),
        username, password);
    run(dc, user);
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
    // TODO: template for filename
    Path configPath = user.getSavePath().resolve(user.getUsername() + "-config.properties");
    dc.net.connect(payload, getInitialIpsFromConfig(configPath));
  }
}