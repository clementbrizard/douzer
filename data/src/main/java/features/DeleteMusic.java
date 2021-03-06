package features;

import static core.Datacore.getLogger;

import core.Datacore;

import datamodel.LocalMusic;
import datamodel.ShareStatus;
import java.io.File;
import java.util.Set;

public abstract class DeleteMusic {

  /**
   * to delete a music in the library, eventually delete locally, and unshare the music.
   * @param music music to delete
   * @param deleteLocal boolean to indicate whether or not delete the music locally
   * @param dc datacore
   */
  public static void run(LocalMusic music, boolean deleteLocal, Datacore dc) {
    Set musics = dc.getCurrentUser().getLocalMusics();
    if (musics.contains(music)) {
      music.setShareStatus(ShareStatus.PRIVATE);
      UnshareMusics.unshareMusic(music, dc);
      dc.removeOwner(music, dc.getCurrentUser());
      musics.remove(music);
      if (deleteLocal) {
        File file = new File(music.getMp3Path());
        if (file.delete()) {
          getLogger().info(music.getMetadata().getTitle() + "is deleted locally");
        } else {
          getLogger().error("Delete operation for the local music has failed.");
        }
      }
    }
  }
}
