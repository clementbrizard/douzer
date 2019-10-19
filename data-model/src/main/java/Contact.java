import java.util.HashSet;

public class Contact implements java.io.Serializable {
  private User user;
  private String group;
  private transient HashSet<LocalMusic> sharedMusics;

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
}
