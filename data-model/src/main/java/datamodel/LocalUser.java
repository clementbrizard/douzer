package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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

    //Same behaviour as DryComment
    Map<LocalMusic, List<AbstractMap.SimpleEntry<UUID, String>>> commentsMap = new HashMap<>();
    Map<LocalMusic, Map<UUID, Integer>> ratingsMap = new HashMap<>();
    //Save comments and ratings for localMusics
    this.getLocalMusics().forEach(music -> {
      List<AbstractMap.SimpleEntry<UUID, String>> comments = new ArrayList<>();
      music.getMetadata().getComments().forEach(c -> {
        if (c.getOwner() == this || this.getFriends().contains(c.getOwner())) {
          comments.add(new AbstractMap.SimpleEntry<>(this.getUuid(), c.getComment()));
        }
      });
      commentsMap.put(music, comments);

      Map<UUID, Integer> ratings = new HashMap<>();
      music.getMetadata().getRatings().forEach((u, r) -> {
        if (u == this || this.getFriends().contains(u)) {
          ratings.put(u.getUuid(), r);
        }
      });
      ratingsMap.put(music, ratings);
    });
    stream.writeObject(commentsMap);
    stream.writeObject(ratingsMap);
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

    //Bind the comments to the existing user reference corresponding to their serialized uuid
    Map<LocalMusic, List<AbstractMap.SimpleEntry<UUID, String>>> commentsMap =
        (Map<LocalMusic, List<AbstractMap.SimpleEntry<UUID, String>>>) stream.readObject();
    commentsMap.forEach((music, mapList) -> {
      List<Comment> comments = new ArrayList<>();
      mapList.forEach(entry -> {
        if (entry.getKey() == this.getUuid()) { //UUID is from localUser
          comments.add(new Comment(entry.getValue(), this));
        } else { //UUID is from a friend
          this.getFriends().stream().filter(u -> entry.getKey() == u.getUuid())
              .findFirst().ifPresent(user -> comments.add(new Comment(entry.getValue(), user)));
        }
      });
      music.getMetadata().setComments(comments);
    });

    //Bind the ratings to the existing user reference corresponding to their serialized uuid
    Map<LocalMusic, Map<UUID, Integer>> ratingsMap =
        (Map<LocalMusic, Map<UUID, Integer>>) stream.readObject();
    ratingsMap.forEach((music, mapRate) -> {
      Map<User, Integer> ratings = new HashMap<>();
      mapRate.forEach((uuid, rate) -> {
        if (rate != null) {
          if (uuid == this.getUuid()) { //UUID is from localUser
            ratings.put(this, rate);
          } else { //UUID is from a friend
            this.getFriends().stream().filter(u -> uuid == u.getUuid())
                .findFirst().ifPresent(user -> ratings.put(user, rate));
          }
        }
      });
      music.getMetadata().setRatings(ratings);
    });
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
