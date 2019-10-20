import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;

public class LocalUser implements java.io.Serializable {
  private MessageDigest messageDigest;
  private String pwdHash;
  private HashSet<Contact> contacts;
  private transient HashSet<LocalMusic> musics;
  private transient List<LocalMusic> playlist;

  /**
   * Default constructor.
   */
  public LocalUser() {
    try {
      messageDigest = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  /**
   * Set a new password.
   * @param pass New password
   */
  void setPassword(String pass) {
    messageDigest.update(pass.getBytes());
    this.pwdHash = new String(messageDigest.digest());
  }

  /**
   * Verify that the provided password match the stored one.
   * @param pass Password to verify
   * @return Whether the password is correct or not
   */
  boolean verifyPassword(String pass) {
    messageDigest.update(pass.getBytes());
    return (new String(messageDigest.digest())).equals(this.pwdHash);
  }

  public HashSet<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(HashSet<Contact> contacts) {
    this.contacts = contacts;
  }

  public HashSet<LocalMusic> getMusics() {
    return musics;
  }

  public void setMusics(HashSet<LocalMusic> musics) {
    this.musics = musics;
  }

  public List<LocalMusic> getPlaylist() {
    return playlist;
  }

  public void setPlaylist(List<LocalMusic> playlist) {
    this.playlist = playlist;
  }
}
