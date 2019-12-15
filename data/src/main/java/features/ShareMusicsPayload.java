package features;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Add the carried shared musics to the available musics.
 */
public class ShareMusicsPayload extends UpdateMusicsPayload {

  public ShareMusicsPayload(LocalUser user, Collection<LocalMusic> musics) {
    super(user, musics.stream().map(Music::new).collect(Collectors.toList()));
  }

  @Override
  public String toString() {
    return "ShareMusicsPayload{"
        + super.toString()
        + '}';
  }
}
