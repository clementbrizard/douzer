package features;

import core.Datacore;
import core.Payload;
import datamodel.Music;
import datamodel.User;
import java.util.Collection;

/**
 * Example feature.
 */
public class UnshareMusicsPayload extends Payload {
  private Collection<String> musicHashs;
  private User owner;

  public UnshareMusicsPayload(Collection<String> musicHashs, User owner) {
    this.musicHashs = musicHashs;
    this.owner = owner;
  }

  /**.
   * To stop sharing a music by deleting the user from the owner list
   */
  @Override
  public void run(Datacore dc) {
    this.musicHashs.forEach(hash -> {
      Music music = dc.getMusic(hash);
      dc.removeOwner(music, this.owner);
      dc.ihm.updateMusic(music);
    });
  }
}
