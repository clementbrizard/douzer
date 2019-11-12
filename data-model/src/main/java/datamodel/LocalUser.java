package datamodel;

import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalUser extends User {
  private MessageDigest messageDigest;
  private String pwdHash;
  private Set<Contact> contacts;
  private Path savePath;
  private Set<LocalMusic> musics;
  private List<LocalMusic> playlist;

  /**
   * Default constructor.
   */
  public LocalUser() {
    this.contacts = new HashSet<>();
    this.musics = new HashSet<>();
    this.playlist = new ArrayList<>();

    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
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

  public Set<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(Set<Contact> contacts) {
    this.contacts = contacts;
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
    this.savePath = savePath;
  }
}
