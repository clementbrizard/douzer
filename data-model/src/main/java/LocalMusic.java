import java.util.HashSet;
import java.util.UUID;

public class LocalMusic implements java.io.Serializable {
  UUID user;
  String group;
  HashSet<LocalMusic> sharedMusics;
}
