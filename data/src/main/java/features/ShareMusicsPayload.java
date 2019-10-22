package features;

import core.Datacore;
import core.Payload;
import drydatamodel.DryMusic;
import java.util.Collection;

/**
 * Example feature.
 */
public class ShareMusicsPayload extends Payload {
  private Collection<DryMusic> musics;

  public ShareMusicsPayload(Collection<DryMusic> musics) {
    this.musics = musics;
  }

  @Override
  public void run(Datacore dc) {
    this.musics.forEach(dryMusic -> dc.addMusic(dryMusic.hydrate(dc)));
  }
}
