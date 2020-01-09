package datamodel;

import java.util.Objects;

public class Comment implements java.io.Serializable {
  private String comment;
  private transient User owner;

  public Comment(String comment, User user) {
    this.comment = comment;
    this.owner = user;
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(owner, comment.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(owner);
  }
}
