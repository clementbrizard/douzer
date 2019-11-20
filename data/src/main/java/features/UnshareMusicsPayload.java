package features;

import core.Datacore;
import core.Payload;
import datamodel.Music;
import datamodel.User;
import java.util.Collection;
import java.util.UUID;

/**
 * Example feature.
 */
public class UnshareMusicsPayload extends Payload {
  private Collection<String> musicHashs;
  private UUID uuid;

  public UnshareMusicsPayload(Collection<String> musicHashs, UUID uuid) {
    this.musicHashs = musicHashs;
    this.uuid = uuid;
  }

  /**.
   * To stop sharing a music by deleting the user from the owner list
   */
  @Override
  public void run(Datacore dc) {
    this.musicHashs.forEach(hash -> {
      Music music = dc.getMusic(hash);
      dc.removeOwner(music, dc.getUser(this.uuid));
      dc.ihm.updateMusic(music);
    });
  }
}
