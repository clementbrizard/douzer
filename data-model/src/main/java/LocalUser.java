import java.util.HashSet;
import java.util.List;

public class LocalUser implements java.io.Serializable {
  private String pwdHash;
  private HashSet<Contact> contacts;
  private transient HashSet<LocalMusic> musics;
  private transient List<LocalMusic> playlists;

  public String getPwdHash() {
    return pwdHash;
  }

  public void setPwdHash(String pwdHash) {
    this.pwdHash = pwdHash;
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

  public List<LocalMusic> getPlaylists() {
    return playlists;
  }

  public void setPlaylists(List<LocalMusic> playlists) {
    this.playlists = playlists;
  }
}
