package features;

import core.Datacore;
import core.Payload;
import datamodel.Music;
import datamodel.MusicMetadata;

public class RateMusicPayload extends Payload {
  private String musicHash;
  private int rating;
  private Datacore dc;

  public RateMusicPayload(Music music, int rating, Datacore dc) {
    this.musicHash = music.getMetadata().getHash();
    this.rating = rating;
    this.dc = dc;
  }

  @Override
  public void run(Datacore dc) {
    MusicMetadata musicMetadata = dc.getMusic(this.musicHash).getMetadata();
    musicMetadata.getRatings().put(this.dc.getCurrentUser(), this.rating);
  }
}