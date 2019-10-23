package drydatamodel;

import core.Datacore;
import datamodel.Comment;
import java.util.UUID;

public class DryComment implements java.io.Serializable {
  private Comment comment;
  private UUID owner;

  public DryComment(Comment comment) {
    this.comment = comment;
    this.owner = comment.getOwner().getUuid();
  }

  public Comment hydrate(Datacore dc) {
    this.comment.setOwner(dc.getUser(this.owner));
    return this.comment;
  }
}
