package datamodel;

import java.awt.Image;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class User implements java.io.Serializable {
  private UUID uuid;
  private String username;
  private Image avatar;
  private String firstName;
  private String lastName;
  private Date dateOfBirth;
  private boolean connected;
  private InetAddress ip;
  private Date timeStamp;

  public User() {
    this.uuid = UUID.randomUUID();
  }

  /**
   * User constructor.
   * @param user model
   */
  public User(User user) {
    this.uuid = user.uuid;
    this.username = user.username;
    this.avatar = user.avatar;
    this.firstName = user.firstName;
    this.lastName = user.lastName;
    this.dateOfBirth = user.dateOfBirth;
    this.connected = user.connected;
    this.ip = user.ip;
    this.timeStamp = new Date();

    DateFormat dateFormat = new SimpleDateFormat("\"yyyy/MM/dd HH:mm:ss\"");
    dateFormat.format(this.timeStamp);
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Image getAvatar() {
    return avatar;
  }

  public void setAvatar(Image avatar) {
    this.avatar = avatar;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  public InetAddress getIp() {
    return ip;
  }

  public void setIp(InetAddress ip) {
    this.ip = ip;
  }

  public Date getTimeStamp() {
    return this.timeStamp;
  }

  public void setTimeStamp(Date timeStamp) {
    this.timeStamp = timeStamp;
  }
}
