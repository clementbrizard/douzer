package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalMusic;
import drydatamodel.DryMusic;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Example feature.
 */
public class ShareMusicsPayload extends Payload {
  private Collection<DryMusic> musics;

  public ShareMusicsPayload(Collection<LocalMusic> musics) {
    this.musics = musics.stream()
        .map(DryMusic::new)
        .collect(Collectors.toList());
  }

  @Override
  public void run(Datacore dc) {
    this.musics.forEach(dryMusic -> {
      dc.addMusic(dryMusic.hydrate(dc));
    });
  }
}
