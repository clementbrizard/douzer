package drydatamodel;

import core.Datacore;
import datamodel.Comment;
import java.util.UUID;

public class DryComment implements java.io.Serializable {
  private String comment;
  private UUID owner;

  public DryComment(Comment comment) {
    this.comment = comment.getComment();
    this.owner = comment.getOwner().getUuid();
  }

  public Comment hydrate(Datacore dc) {
    return new Comment(comment, dc.getUser(owner));
  }
}
