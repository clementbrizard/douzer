package features;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.User;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;

public abstract class DeleteMusic {
  /**
   * to delete a music in the library, eventually delete locally, and unshare the music.
   * @param music music to delete
   * @param deleteLocal boolean to indicate whether or not delete the music locally
   * @param dc datacore
   */
  public static void deleteMusic(LocalMusic music, boolean deleteLocal,
                                 Datacore dc, Logger startLogger) {
    Set musics = dc.getCurrentUser().getMusics();
    if (musics.contains(music)) {
      unshareMusic(music, dc);
      musics.remove(music);
      if (deleteLocal) {
        File file = new File(music.getMp3Path());
        if (file.delete()) {
          startLogger.info(music.getMetadata().getTitle() + "is deleted locally");
        } else {
          startLogger.error("Delete operation for the local music has failed.");
        }
      }
    }
  }

  /**
   * unshare a music, first delete the local user from owner.
   * Then send payloads to all the user and delete the user from other users' music's owner list
   * @param musics musics to unshare
   * @param dc datacore
   */
  public static void unshareMusics(Collection<LocalMusic> musics, Datacore dc) {
    User u = dc.getCurrentUser();
    musics.forEach(m -> dc.removeOwner(m, u));
    Collection<String> musicHashs = musics.stream()
        .map(m -> m.getMetadata().getHash()).collect(Collectors.toList());
    UnshareMusicsPayload payload = new UnshareMusicsPayload(musicHashs, u.getUuid());
    dc.net.sendToUsers(payload, dc.getIps());
  }

  public static void unshareMusic(LocalMusic music, Datacore dc) {
    unshareMusics(Collections.singleton(music), dc);
  }


}
