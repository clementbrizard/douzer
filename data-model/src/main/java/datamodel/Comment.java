package datamodel;

public class Comment implements java.io.Serializable {
  private String comment;
  private transient User owner;

  public User getOwner() {
    return owner;
  }

  public void setOwner(User owner) {
    this.owner = owner;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
