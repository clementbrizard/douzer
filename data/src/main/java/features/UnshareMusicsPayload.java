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
  private Collection<String> hashMusics;

  public UnshareMusicsPayload(Collection<String> hashMusics) {
    this.hashMusics = hashMusics;
  }

  /**
   * Add the carried musics to the available musics.
   */
  @Override
  public void run(Datacore dc) {
    this.hashMusics.forEach(hash -> {
      Music music = dc.getMusic(hash);
      User user = dc.getCurrentUser();
      dc.removeOwner(music, user);
    });
  }
}
