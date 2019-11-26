package features;

import core.Datacore;
import core.Payload;
import datamodel.Comment;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.User;

public class CommentMusicPayload extends Payload {
  private String musicHash;
  private Comment comment;

  public CommentMusicPayload(Music music, String commentText, User commentator) {
    this.musicHash = music.getMetadata().getHash();
    this.comment = new Comment(commentText, commentator);
  }

  @Override
  public void run(Datacore dc) {
    MusicMetadata musicMetadata = dc.getMusic(this.musicHash).getMetadata();
    musicMetadata.getComments().add(this.comment);
  }
}
