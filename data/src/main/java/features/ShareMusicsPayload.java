package features;

import datamodel.LocalMusic;
import datamodel.Music;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Add the carried shared musics to the available musics.
 */
public class ShareMusicsPayload extends UpdateMusicsPayload {

  public ShareMusicsPayload(Collection<LocalMusic> musics) {
    super(musics.stream().map(m -> (Music) m).collect(Collectors.toList()));
  }
}
