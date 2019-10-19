import java.awt.Image;
import java.net.InetAddress;
import java.util.Date;
import java.util.UUID;

public class User implements java.io.Serializable {
  UUID uuid;
  String username;
  Image avatar;
  String firstName;
  String lastName;
  Date dateOfBirth;
  Boolean connected;
  InetAddress ip;
}
