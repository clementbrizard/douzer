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
  private volatile HashMap<UUID, User> users;
  private volatile HashMap<String, Music> musics;
  private volatile LocalUser currentUser;

  public Net net;
  public Ihm ihm;

  Datacore(Net net, Ihm ihm) {
    this.net = net;
    this.ihm = ihm;
    this.users = new HashMap<>();
    this.musics = new HashMap<>();
  }

  public void addMusic(Music music) {
    Music original = this.musics.get(music.getMetadata().getHash());
    if (original != null) {
      this.mergeMusics(original, music);
    } else {
      this.musics.put(music.getMetadata().getHash(), music);
    }
  }

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

  public LocalMusic getLocalMusic(String hash) {
    Music m = this.musics.get(hash);
    return m instanceof LocalMusic ? (LocalMusic) m : null;
  }

  public Stream<LocalMusic> getLocalMusics() {
    return this.musics.values().stream()
        .filter(m -> !(m instanceof LocalMusic)).map(m -> (LocalMusic) m);
  }

  public void removeOwner(User user) {
    this.musics.values().forEach(m -> {
      m.getOwners().remove(user);
      if (m.getOwners().isEmpty()) {
        this.musics.remove(m.getMetadata().getHash());
      }
    });
  }

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
    // TODO: do a proper merge
    throw new UnsupportedOperationException("Merge with between musics is not implemented yet");
  }

  /**
   * Merge user2 into user1.
   *
   * @param user1 the reference that will be updated.
   * @param user2 the reference that will not be updated.
   */
  private void mergeUsers(User user1, User user2) {
    // TODO: do a proper merge
    throw new UnsupportedOperationException("Merge with between users is not implemented yet");
  }

  public Stream<InetAddress> getIps() {
    return this.users.values().stream()
        .map(User::getIp).filter(ip -> ip != this.currentUser.getIp());
  }

}
