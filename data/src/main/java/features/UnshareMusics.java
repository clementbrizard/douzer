package features;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.User;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public abstract class UnshareMusics {

  /**
   * unshare a music, first delete the local user from owner.
   * Then send payloads to all the user and delete the user from other users' music's owner list
   * @param musics musics to unshare
   * @param dc datacore
   */
  public static void run(Collection<LocalMusic> musics, Datacore dc) {
    HashSet<String> musicHashsForAll = new HashSet<>();
    HashSet<String> musicHashsForNonFriends = new HashSet<>();

    musics.forEach(m -> {
          switch (m.getShareStatus()) {
            case PRIVATE:
              musicHashsForAll.add(m.getMetadata().getHash());
              break;
            case FRIENDS:
              musicHashsForNonFriends.add(m.getMetadata().getHash());
              break;
            default:
              break;
          }
        }
    );

    UnshareMusicsPayload payloadForFriends = new UnshareMusicsPayload(
        dc.getCurrentUser(),
        musicHashsForNonFriends
    );
    UnshareMusicsPayload payloadForAll = new UnshareMusicsPayload(
        dc.getCurrentUser(),
        musicHashsForAll
    );

    dc.net.sendToUsers(payloadForAll, dc.getOnlineIps());
    dc.net.sendToUsers(payloadForFriends, dc.getOnlineNonFriendsIps());
  }

  public static void unshareMusic(LocalMusic music, Datacore dc) {
    run(Collections.singleton(music), dc);
  }
}
