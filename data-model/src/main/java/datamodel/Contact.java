package datamodel;

import java.util.HashSet;
import java.util.Objects;

public class Contact implements java.io.Serializable {
  private User user;
  private String group;
  private HashSet<LocalMusic> sharedMusics;

  public Contact(User user) {
    this.user = user;
    this.sharedMusics = new HashSet<>();
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public HashSet<LocalMusic> getSharedMusics() {
    return sharedMusics;
  }

  public void setSharedMusics(HashSet<LocalMusic> sharedMusics) {
    this.sharedMusics = sharedMusics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Contact contact = (Contact) o;
    return Objects.equals(user, contact.user) &&
        Objects.equals(group, contact.group) &&
        Objects.equals(sharedMusics, contact.sharedMusics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, group, sharedMusics);
  }
}
