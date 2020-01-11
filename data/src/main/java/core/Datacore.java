package core;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.User;
import interfaces.Ihm;
import interfaces.Net;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Datacore {
  private static final Logger logger = LogManager.getLogger();
  private static final String LOCAL_USERS_FILENAME = "lo23-users.ser";
  public Net net;
  public Ihm ihm;
  private LocalUsersFileHandler localUsersFileHandler;
  private volatile ConcurrentHashMap<UUID, User> users;
  private volatile ConcurrentHashMap<String, Music> musics;
  private volatile LocalUser currentUser;
  private volatile HashSet<InetAddress> allIps;

  Datacore(Net net, Ihm ihm) {
    this.net = net;
    this.ihm = ihm;
    this.users = new ConcurrentHashMap<>();
    this.musics = new ConcurrentHashMap<>();
    this.allIps = new HashSet<>();
    try {
      this.localUsersFileHandler = new LocalUsersFileHandler(LOCAL_USERS_FILENAME);
    } catch (IOException e) {
      logger.error(e);
    }
  }

  public static Logger getLogger() {
    return logger;
  }

  /**
   * Add the music by either creating a new entry if it doesn't exist already
   * or by merging it into the existing music.
   */
  public void addMusic(Music music) {
    Music original = this.musics.get(music.getHash());
    if (original != null) {
      this.mergeMusics(original, music);
    } else {
      this.musics.put(music.getHash(), music);
    }
  }

  /**
   * Add the user by either creating a new entry if it doesn't exist already
   * or by merging it into the existing user.
   */
  public void addUser(User user) {
    // try to get the original ref
    User original = this.currentUser
            .getFriends()
            .stream()
            .filter(u -> u.equals(user)).findAny().orElse(null);
    if (original == null) {
      original = this.users.get(user.getUuid());
    }

    if (original != null) {
      this.mergeUsers(original, user);
      if (!this.users.containsKey(user.getUuid())) {
        this.users.put(user.getUuid(), user);
      }
    } else {
      this.users.put(user.getUuid(), user);
    }


  }

  /**
   * Remove a user from the map.
   */
  public void removeUser(User user) {
    this.users.remove(user.getUuid());
  }

  ConcurrentHashMap<UUID, User> getUsers() {
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

  public Stream<Music> getMusics() {
    return musics.values().stream();
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

  public Set<InetAddress> getAllIps() {
    return allIps;
  }

  public Stream<InetAddress> getOnlineFriendsIps() {
    return this.getCurrentUser().getFriends().stream().filter(User::isConnected).map(User::getIp);
  }

  public Stream<InetAddress> getOnlineNonFriendsIps() {
    return this.users.values().stream()
        .filter(u -> !this.getCurrentUser().getFriends().contains(u))
        .map(User::getIp);
  }

  public void setAllIps(HashSet<InetAddress> allIps) {
    this.allIps = allIps;
  }

  public Stream<InetAddress> getOnlineIps() {
    return this.getOnlineUsers().map(User::getIp);
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
        .filter(m -> (m instanceof LocalMusic)).map(m -> (LocalMusic) m);
  }

  public HashMap<UUID, User> getOnlineUsersSet() {
    return this.users;
  }

  /**
   * Remove the user from all the owners sets
   * and remove the music if it has no more owners.
   */
  public void removeOwner(User user) {
    for (HashMap.Entry<String, Music> m : musics.entrySet()) {
      this.removeOwner(m.getValue(), user);
    }
  }

  /**
   * Remove the user from the music's owner set
   * and remove the music if it has no more owners.
   */
  public void removeOwner(Music music, User user) {
    music.getOwners().remove(user);
    if (music.getOwners().isEmpty()) {
      this.musics.remove(music.getHash());
      this.ihm.notifyMusicDeletion(music);
    }
  }

  /**
   * Merge music2 into music1.
   *
   * @param music1 the reference that will be updated.
   * @param music2 the reference that will not be updated. It should be discarded.
   */
  private void mergeMusics(Music music1, Music music2) {

    // Always merge the owners
    music1.getOwners().addAll(music2.getOwners());

    //music2's was created first
    if (music1.getMetadata().getTimeStamp().compareTo(music2.getMetadata().getTimeStamp()) < 0) {
      music2.getMetadata().getTags().addAll(music1.getMetadata().getTags());
      music2.getMetadata().getComments().addAll(music1.getMetadata().getComments());
      music2.getMetadata().getRatings().putAll(music1.getMetadata().getRatings());
      music1.getMetadata().updateMusicMetadata(music2.getMetadata());
    } else {
      // else, music1 is the most recent, so we just merge set attributes
      music1.getMetadata().getTags().addAll(music2.getMetadata().getTags());
      music1.getMetadata().getComments().addAll(music2.getMetadata().getComments());
      music1.getMetadata().getRatings().putAll(music2.getMetadata().getRatings());
    }
  }

  /**
   * Merge user2 into user1.
   *
   * @param user1 the reference that will be updated.
   * @param user2 the reference that will not be updated.
   */
  private void mergeUsers(User user1, User user2) {
    // user2 was created first
    if (user1.getTimeStamp().compareTo(user2.getTimeStamp()) <= 0) {
      user1.updateUser(user2);
    }
    // No else, the user1 is the template
  }

  public void upgradeMusicToLocal(Music toUpgrade, String mp3Path) {
    LocalMusic newMusic = new LocalMusic(toUpgrade, mp3Path);
    this.musics.remove(toUpgrade.getHash());
    newMusic.getOwners().add(this.getCurrentUser());

    this.addMusic(newMusic);
    // Get the potentially new ref
    newMusic = this.getLocalMusic(newMusic.getHash());
    this.getCurrentUser().getLocalMusics().add(newMusic);

  }

  /**
   * Clear all volatiles variables.
   */
  public void wipe() {
    this.users.clear();
    this.musics.clear();
    this.allIps.clear();
    this.currentUser = null;
  }
}
