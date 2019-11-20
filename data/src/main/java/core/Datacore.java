package core;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.User;
import interfaces.Ihm;
import interfaces.Net;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

public class Datacore {
  public Net net;
  public Ihm ihm;
  private static final String LOCAL_USERS_FILENAME = "lo23-users.ser";
  private final LocalUsersFileHandler localUsersFileHandler;
  private volatile HashMap<UUID, User> users;
  private volatile HashMap<String, Music> musics;
  private volatile LocalUser currentUser;

  Datacore(Net net, Ihm ihm) {
    this.net = net;
    this.ihm = ihm;
    this.users = new HashMap<>();
    this.musics = new HashMap<>();
    this.localUsersFileHandler = new LocalUsersFileHandler(LOCAL_USERS_FILENAME);
  }

  /**
   * Add the music by either creating a new entry if it doesn't exist already
   * or by merging it into the existing music.
   */
  public void addMusic(Music music) {
    Music original = this.musics.get(music.getMetadata().getHash());
    if (original != null) {
      this.mergeMusics(original, music);
    } else {
      this.musics.put(music.getMetadata().getHash(), music);
    }
  }

  /**
   * Add the user by either creating a new entry if it doesn't exist already
   * or by merging it into the existing user.
   */
  public void addUser(User user) {
    User original = this.users.get(user.getUuid());
    if (original != null) {
      this.mergeUsers(original, user);
    } else {
      this.users.put(user.getUuid(), user);
    }
  }

  public HashMap<UUID, User> getUsers() {
    return users;
  }

  /**
   * Get the connected known users (current user excluded).
   */
  public Stream<User> getOnlineUsers() {
    return users.values().stream()
        .filter(User::isConnected)
        .filter(u -> !(u instanceof LocalUser));
  }

  public HashMap<String, Music> getMusics() {
    return musics;
  }

  public User getUser(UUID uuid) {
    return users.get(uuid);
  }

  public Music getMusic(String hash) {
    return musics.get(hash);
  }

  public LocalUser getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(LocalUser user) {
    this.currentUser = user;
  }

  public LocalUsersFileHandler getLocalUsersFileHandler() {
    return localUsersFileHandler;
  }

  public LocalMusic getLocalMusic(String hash) {
    Music m = this.musics.get(hash);
    return m instanceof LocalMusic ? (LocalMusic) m : null;
  }

  public Stream<LocalMusic> getLocalMusics() {
    return this.musics.values().stream()
        .filter(m -> !(m instanceof LocalMusic)).map(m -> (LocalMusic) m);
  }

  /**
   * Remove the user from all the owners sets
   * and remove the music if it has no more owners.
   */
  public void removeOwner(User user) {
    this.musics.values().forEach(m -> {
      m.getOwners().remove(user);
      if (m.getOwners().isEmpty()) {
        this.musics.remove(m.getMetadata().getHash());
      }
    });
  }

  /**
   * Remove the user from the music's owner set
   * and remove the music if it has no more owners.
   */
  public void removeOwner(Music music, User user) {
    music.getOwners().remove(user);
    if (music.getOwners().isEmpty()) {
      this.musics.remove(music.getMetadata().getHash());
    }
  }

  /**
   * Merge music2 into music1.
   *
   * @param music1 the reference that will be updated.
   * @param music2 the reference that will not be updated.
   */
  private void mergeMusics(Music music1, Music music2) {
    //Local User must be owner of the music
    music1.getOwners().add(this.currentUser);

    // extends the lists of Tags, comments and ratings
    music1.getMetadata().getTags().addAll(music2.getMetadata().getTags());
    music1.getMetadata().getComments().addAll(music2.getMetadata().getComments());
    music1.getMetadata().getRatings().putAll(music2.getMetadata().getRatings());
    //music2's was created first
    if (music1.getMetadata().getTimeStamp().compareTo(music2.getMetadata().getTimeStamp()) < 0) {
      music1.getMetadata().updateMusicMetadata(music2.getMetadata());
    }
    // else, music1 is already the template
  }

  /**
   * Merge user2 into user1.
   *
   * @param user1 the reference that will be updated.
   * @param user2 the reference that will not be updated.
   */
  private void mergeUsers(User user1, User user2) {
    // user2 was created first
    if (user1.getTimeStamp().compareTo(user2.getTimeStamp()) < 0) {
      user1.updateUser(user2);
    }
    // No else, the user1 is the template
  }

  /**
   * Clear all volatiles variables.
   */
  public void wipe() {
    this.users.clear();
    this.musics.clear();
    this.currentUser = null;
  }

  public Stream<InetAddress> getIps() {
    return this.users.values().stream()
        .map(User::getIp).filter(ip -> ip != this.currentUser.getIp());
  }
}
