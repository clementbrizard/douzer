package features;

import core.Datacore;
import core.Payload;
import datamodel.LocalUser;
import datamodel.Music;
import drydatamodel.DryMusic;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Add or update the carried musics.
 */
public class UpdateMusicsPayload extends Payload {
  private Collection<DryMusic> musics;

  public UpdateMusicsPayload(LocalUser user, Collection<? extends Music> musics) {
    super(user);
    this.musics = musics.stream()
        .map(DryMusic::new)
        .collect(Collectors.toList());
  }

  @Override
  public void run(Datacore dc) {
    this.musics.forEach(dryMusic -> {
      Music music = dryMusic.hydrate(dc);
      dc.addMusic(music);
      // The reference might have changed
      music = dc.getMusic(music.getHash());
      dc.ihm.updateMusic(music);
    });
  }

  @Override
  public String toString() {
    return "UpdateMusicsPayload{"
        + "musics=" + musics
        + '}';
  }
}
