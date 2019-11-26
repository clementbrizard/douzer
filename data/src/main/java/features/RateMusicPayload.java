package features;

import core.Datacore;
import core.Payload;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.User;

public class RateMusicPayload extends Payload {
  private String musicHash;
  private int rating;
  private User rater;

  public RateMusicPayload(Music music, int rating, User rater) {
    this.musicHash = music.getMetadata().getHash();
    this.rating = rating;
  }

  @Override
  public void run(Datacore dc) {
    MusicMetadata musicMetadata = dc.getMusic(this.musicHash).getMetadata();
    musicMetadata.getRatings().put(this.rater, this.rating);
  }
}