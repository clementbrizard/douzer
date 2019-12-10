package features;

import core.Datacore;
import datamodel.LocalMusic;
import java.util.Collection;
import java.util.HashSet;

public class ShareMusics {
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
            case PRIVATE:
              musicsForFriends.add(m);
              break;
            default:
              break;
          }
        }
    );

    ShareMusicsPayload payloadForAll = new ShareMusicsPayload(musicsForAll);
    dc.net.sendToUsers(payloadForAll, dc.getOnlineIps());

    ShareMusicsPayload payloadForFriends = new ShareMusicsPayload(musicsForFriends);
    dc.net.sendToUsers(payloadForFriends, dc.getOnlineFriendsIps());
  }
}
