package features;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.User;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public abstract class UnshareMusics {

  /**
   * unshare a music, first delete the local user from owner.
   * Then send payloads to all the user and delete the user from other users' music's owner list
   * @param musics musics to unshare
   * @param dc datacore
   */
  public static void run(Collection<LocalMusic> musics, Datacore dc) {
    User u = dc.getCurrentUser();
    musics.forEach(m -> dc.removeOwner(m, u));
    Collection<String> musicHashs = musics.stream()
        .map(m -> m.getMetadata().getHash()).collect(Collectors.toList());
    UnshareMusicsPayload payload = new UnshareMusicsPayload(musicHashs, u.getUuid());
    dc.net.sendToUsers(payload, dc.getOnlineIps());
  }

  public static void unshareMusic(LocalMusic music, Datacore dc) {
    run(Collections.singleton(music), dc);
  }
}
