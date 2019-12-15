package features;

import core.Datacore;
import datamodel.LocalMusic;
import java.util.Collection;
import java.util.HashSet;

public abstract class ShareMusics {
  /**
   * Shares the musics according to their settings.
   */
  public static void run(Datacore dc, Collection<LocalMusic> musics) {
    HashSet<LocalMusic> musicsForAll = new HashSet<>();
    HashSet<LocalMusic> musicsForFriends = new HashSet<>();
    musics.forEach(m -> {
          switch (m.getShareStatus()) {
            case PUBLIC:
              musicsForAll.add(m);
              break;
            case FRIENDS:
              musicsForFriends.add(m);
              break;
            default:
              break;
          }
        }
    );

    ShareMusicsPayload payloadForAll = new ShareMusicsPayload(
        dc.getCurrentUser(),
        musicsForAll
    );
    dc.net.sendToUsers(payloadForAll, dc.getOnlineIps());

    ShareMusicsPayload payloadForFriends = new ShareMusicsPayload(
        dc.getCurrentUser(),
        musicsForFriends
    );
    dc.net.sendToUsers(payloadForFriends, dc.getOnlineFriendsIps());
  }
}
