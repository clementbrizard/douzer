package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class LocalUser extends User {
  private static final long serialVersionUID = 1L;
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
  private Set<LocalMusic> localMusics;
  private Collection<Playlist> playlists;

  /**
   * Default constructor.
   */
  public LocalUser() {
    this.friends = new HashSet<>();
    this.localMusics = new HashSet<>();
    this.playlists = new ArrayList<>();
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

  public void addFriend(User user) {
    this.friends.add(user);
  }

  public void removeFriend(User user) {
    this.friends.remove(user);
  }

  public Set<LocalMusic> getLocalMusics() {
    return localMusics;
  }

  public void setLocalMusics(Set<LocalMusic> localMusics) {
    this.localMusics = localMusics;
  }

  public Collection<Playlist> getPlaylists() {
    return playlists;
  }

  public void setPlaylists(List<Playlist> playlists) {
    this.playlists = playlists;
  }

  public Playlist addPlaylist(String name) throws IllegalArgumentException {
    this.playlists.forEach(p -> {
      if (p.getName().equals(name)) {
        throw new IllegalArgumentException("Playlist name already exists");
      }
    }
    );
    Playlist newPlaylist = new Playlist(name);
    this.playlists.add(newPlaylist);
    return newPlaylist;
  }

  public Playlist getPlaylistByName(String name) throws IllegalArgumentException {
    return this.playlists.stream()
            .filter(playlist -> playlist.getName().equals(name))
            .findAny()
            .orElse(null);
  }

  public void removePlaylist(Playlist playlist) {
    this.playlists.remove(playlist);
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
    localMusics.forEach(localMusic -> localMusic.setOwners(owners));
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
        && Objects.equals(localMusics, localUser.localMusics)
        && Objects.equals(playlists, localUser.playlists);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }
}
