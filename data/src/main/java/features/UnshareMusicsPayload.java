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
  private UUID ownerToRemove;

  public UnshareMusicsPayload(LocalUser user, Collection<String> musicHashs, UUID ownerToRemove) {
    super(user);
    this.musicHashs = musicHashs;
    this.ownerToRemove = ownerToRemove;
  }

  /**.
   * To stop sharing a music by deleting the user from the owner list
   */
  @Override
  public void run(Datacore dc) {
    this.musicHashs.forEach(hash -> {
      Music musicToUnshare = dc.getMusic(hash);
      dc.removeOwner(musicToUnshare, dc.getUser(this.ownerToRemove));
      dc.ihm.updateMusic(musicToUnshare);
    });
  }
}
