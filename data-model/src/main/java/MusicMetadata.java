import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MusicMetadata implements java.io.Serializable {
  String hash;
  String tittle;
  String artist;
  String album;
  Date releaseDate;
  HashSet<String> tags;
  HashMap<UUID, Integer> ratings;
  HashSet<Comment> comments;
}
