import java.util.HashSet;
import java.util.List;

public class LocalUser {
    String pwdHash;
    HashSet<Contact> contacts;
    HashSet<LocalMusic> musics;
    List<LocalMusic> playlists;
}
