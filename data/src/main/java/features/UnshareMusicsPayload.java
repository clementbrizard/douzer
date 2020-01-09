package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;
import datamodel.Music;
import java.util.Collection;
import java.util.UUID;

/**
 * Example feature.
 */
public class UnshareMusicsPayload extends Payload {
  private Collection<String> musicHashs;

  public UnshareMusicsPayload(LocalUser user, Collection<String> musicHashs) {
    super(user);
    this.musicHashs = musicHashs;
  }

  /**.
   * To stop sharing a music by deleting the user from the owner list
   */
  @Override
  public void run(Datacore dc) {
    this.musicHashs.forEach(hash -> {
      Music musicToUnshare = dc.getMusic(hash);
      if (musicToUnshare != null) {
        dc.removeOwner(musicToUnshare, dc.getUser(this.senderUuid));
        dc.ihm.updateMusic(musicToUnshare);
      }
    });
  }

  @Override
  public String toString() {
    return "UnshareMusicsPayload{"
        + "musicHashs=" + musicHashs
        + '}';
  }
}
