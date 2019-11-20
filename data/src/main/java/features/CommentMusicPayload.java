package features;

import core.Datacore;
import core.Payload;
import datamodel.Comment;
import datamodel.Music;
import datamodel.MusicMetadata;

public class CommentMusicPayload extends Payload {
  private String musicHash;
  private Comment comment;
  private Datacore dc;

  public CommentMusicPayload(Music music, String commentText, Datacore dc) {
    this.musicHash = music.getMetadata().getHash();
    this.comment = new Comment(commentText, dc.getCurrentUser());
  }

  @Override
  public void run (Datacore dc) {
    MusicMetadata musicMetadata = dc.getMusic(this.musicHash).getMetadata();
    musicMetadata.getComments().add(this.comment);
  }
}
