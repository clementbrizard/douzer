package features;

import core.Datacore;
import core.Payload;
import datamodel.Music;
import datamodel.MusicMetadata;

import java.util.Collection;

/**
 * Example feature.
 */
public class AddMusics {
  void run(Datacore dc, Collection<MusicMetadata> musicMetadatas) {
    Payload payload = new AddMusicsPayload(musicMetadatas);
    dc.net.sendToUsers(payload.toString(), dc.getIps());
  }
}
