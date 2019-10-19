import java.util.HashSet;
import java.util.UUID;

public class Music implements java.io.Serializable {
  MusicMetadata metadata;
  HashSet<UUID> owners;
}
