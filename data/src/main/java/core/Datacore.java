package core;

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

  public Datacore(Net net, Ihm ihm) {
    this.net = net;
    this.ihm = ihm;
    this.users = new HashMap<>();
    this.musics = new HashMap<>();
  }

  public void addMusic(Music music) {
    String hash = music.getMetadata().getHash();
    if (this.musics.containsKey(hash)) {
      this.musics.put(hash, this.mergeMusics(music, this.musics.get(hash)));
    } else {
      this.musics.put(hash, music);
    }
  }

  public void addUser(User user) {
    UUID uuid = user.getUuid();
    if (this.users.containsKey(uuid)) {
      this.users.put(uuid, this.mergeUsers(user, this.users.get(uuid)));
    } else {
      this.users.put(uuid, user);
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

  public Music mergeMusics(Music music1, Music music2) {
    // TODO: do a proper merge
    throw new UnsupportedOperationException("Merge with between musics is not implemented yet");
  }

  public User mergeUsers(User user1, User user2) {
    // TODO: do a proper merge
    throw new UnsupportedOperationException("Merge with between users is not implemented yet");
  }

  public Stream<InetAddress> getIps() {
    return this.users.values().stream()
        .map(user -> user.getIp()).filter(ip -> ip != this.currentUser.getIp());
  }

}
