package datamodel;

import java.awt.Image;
import java.net.InetAddress;
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
    updateTimeStamp();
  }

  public UUID getUuid() {
    return uuid;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    updateTimeStamp();
    this.username = username;
  }

  public Image getAvatar() {
    return avatar;
  }

  public void setAvatar(Image avatar) {
    updateTimeStamp();
    this.avatar = avatar;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    updateTimeStamp();
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    updateTimeStamp();
    this.lastName = lastName;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    updateTimeStamp();
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
    updateTimeStamp();
    this.ip = ip;
  }

  public Date getTimeStamp() {
    return this.timeStamp;
  }

  private void updateTimeStamp() {
    this.timeStamp = new Date();
  }

  /**
   * Update of a user attributes based on an other user.
   * @param newUser other user
   */
  public void updateUser(User newUser) {
    this.username = newUser.username;
    this.firstName = newUser.firstName;
    this.lastName = newUser.lastName;
    this.avatar = newUser.avatar;
    this.connected = newUser.connected;
    this.dateOfBirth = newUser.dateOfBirth;
    this.ip = newUser.ip;

    updateTimeStamp();
  }
}
