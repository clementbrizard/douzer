package features;

import core.Datacore;
import core.Payload;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.User;

import java.util.Collection;
import java.util.HashSet;

/**
 * Example feature.
 */
public class AddMusicsPayload extends Payload {
  Collection<MusicMetadata> musicMetadatas;

  public AddMusicsPayload(Collection<MusicMetadata> musicMetadatas) {
    this.musicMetadatas = musicMetadatas;
  }

  @Override
  public void run(Datacore dc) {
    this.musicMetadatas.forEach(mdata -> {
      HashSet<User> owners = new HashSet<>();
      owners.add(dc.getCurrentUser());

      Music m = new Music();
      m.setMetadata(mdata);
      m.setOwners(owners);
      dc.addMusic(m);
    });
  }
}
