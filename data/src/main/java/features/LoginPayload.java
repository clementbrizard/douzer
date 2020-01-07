package features;

import core.Datacore;
import datamodel.LocalUser;
import datamodel.ShareStatus;
import datamodel.User;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Connection handshake
 * Payload for "Hey, I connected ! Here is my profile and my shared musics"
 * The "my shared music" part is handled with the ShareMusicsPayload
 */
public class LoginPayload extends UpdateMusicsPayload {
  private User user;
  private Set<InetAddress> ips;
  private boolean isResponse;

  LoginPayload(LocalUser user, Set<InetAddress> addresses) {
    super(user,
        user.getLocalMusics().stream()
            .filter(m -> m.getShareStatus() == ShareStatus.PUBLIC)
            .collect(Collectors.toCollection(HashSet::new))
    );
    // copy constructor instead of cast to not send sensitive info over the network
    this.user = new User(user);
    this.ips = new HashSet<>(addresses);
    this.isResponse = false;
  }

  private LoginPayload(LocalUser user, Set<InetAddress> addresses, boolean isResponse) {
    super(user,
        user.getLocalMusics().stream()
            .filter(m -> m.getShareStatus() == ShareStatus.PUBLIC)
            .collect(Collectors.toCollection(HashSet::new))
    );
    // copy constructor instead of cast to not send sensitive info over the network
    this.user = new User(user);
    this.ips = new HashSet<>(addresses);
    this.isResponse = isResponse;
  }

  @Override
  public void process(Datacore dc, InetAddress senderIp) {
    // Register the new user
    dc.getAllIps().add(senderIp);
    this.user.setIp(senderIp);
    dc.addUser(this.user);
    dc.ihm.notifyUserConnection(this.user);

    // Answer to sender
    if (!this.isResponse) {
      LoginPayload responsePayload = new LoginPayload(
          dc.getCurrentUser(),
          dc.getAllIps().stream()
              .filter(ip -> !ip.equals(senderIp))
              .collect(Collectors.toCollection(HashSet::new)),
          true
      );
      dc.net.sendToUser(responsePayload, senderIp);
    }

    // Connecting to unknown IPs
    for (InetAddress ip : this.ips) {
      // iterate and don't send ip of user A to A
      if (!dc.getAllIps().contains(ip)) {
        HashSet<InetAddress> ipsToShare =
            (HashSet<InetAddress>) ((HashSet<InetAddress>) dc.getAllIps()).clone();
        ipsToShare.remove(ip);
        LoginPayload payload = new LoginPayload(dc.getCurrentUser(), ipsToShare);
        dc.net.sendToUser(payload, ip);
      }
    }

    super.run(dc); // update musics
  }

  @Override
  public String toString() {
    return "LoginPayload{"
        + "user=" + user
        + ", ips=" + ips
        + ", isResponse=" + isResponse
        + '}';
  }
}