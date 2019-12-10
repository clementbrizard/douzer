package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class LocalUser extends User {
  private static MessageDigest messageDigest;

  static {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  private String pwdHash;
  private Set<User> friends;
  // Path is not serializable, handle serialization with readObject writeObject below.
  private transient Path savePath;
  private Set<LocalMusic> musics;
  private List<LocalMusic> playlist;

  /**
   * Default constructor.
   */
  public LocalUser() {
    this.friends = new HashSet<>();
    this.musics = new HashSet<>();
    this.playlist = new ArrayList<>();
  }

  /**
   * Set a new password.
   *
   * @param pass New password
   */
  public void setPassword(String pass) {
    messageDigest.update(pass.getBytes());
    this.pwdHash = new String(messageDigest.digest());
  }

  /**
   * Verify that the provided password match the stored one.
   *
   * @param pass Password to verify
   * @return Whether the password is correct or not
   */
  public boolean verifyPassword(String pass) {
    messageDigest.update(pass.getBytes());
    return (new String(messageDigest.digest())).equals(this.pwdHash);
  }

  public Set<User> getFriends() {
    return this.friends;
  }

  public void setFriends(Set<User> friends) {
    this.friends = friends;
  }

  public Set<LocalMusic> getMusics() {
    return musics;
  }

  public void setMusics(Set<LocalMusic> musics) {
    this.musics = musics;
  }

  public List<LocalMusic> getPlaylist() {
    return playlist;
  }

  public void setPlaylist(List<LocalMusic> playlist) {
    this.playlist = playlist;
  }

  public Path getSavePath() {
    return savePath;
  }

  public void setSavePath(Path savePath) {
    this.savePath = savePath.toAbsolutePath();
  }

  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    stream.writeUTF(this.savePath.toString());
  }

  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();

    String savePathString = stream.readUTF();
    setSavePath(Paths.get(savePathString).toAbsolutePath());

    this.friends.forEach(u -> u.setConnected(false));

    // Add himself as owner of its musics
    Set<User> owners = new HashSet<>();
    owners.add(this);
    musics.forEach(localMusic -> localMusic.setOwners(owners));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    LocalUser localUser = (LocalUser) o;
    return Objects.equals(pwdHash, localUser.pwdHash)
        && Objects.equals(friends, localUser.friends)
        && Objects.equals(savePath, localUser.savePath)
        && Objects.equals(musics, localUser.musics)
        && Objects.equals(playlist, localUser.playlist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }

  public Stream<InetAddress> getContactsOnlineIps() {
    return this.getContacts().stream()
        .map(Contact::getUser).filter(User::isConnected).map(User::getIp);
  }

  public Stream<InetAddress> getContactsOnlineIps() {
    return this.getContacts().stream()
        .map(Contact::getUser).filter(User::isConnected).map(User::getIp);
  }
}
