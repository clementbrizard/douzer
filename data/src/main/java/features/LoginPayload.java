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
public class LoginPayload extends ShareMusicsPayload {
  private User user;
  private Set<InetAddress> ips;
  private boolean isResponse;

  LoginPayload(LocalUser user, Set<InetAddress> addresses) {
    super(user,
        user.getLocalMusics().stream()
            .filter(m -> m.getShareStatus() == ShareStatus.PUBLIC)
            .collect(Collectors.toSet())
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
            .collect(Collectors.toSet())
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
              .filter(ip -> ip != senderIp)
              .collect(Collectors.toSet()),
          true
      );
      dc.net.sendToUser(responsePayload, senderIp);
    }

    // Preparing login payload for unknown IPs
    LoginPayload payload = new LoginPayload(
        dc.getCurrentUser(),
        dc.getAllIps()
    );

    // Connecting to unknown IPs
    dc.net.sendToUsers(
        payload,
        this.ips.stream()
            .filter(ip -> !dc.getAllIps().contains(ip) && ip != dc.getCurrentUser().getIp())
            .peek(ip -> dc.getAllIps().add(ip))
    );
    super.run(dc); // update musics
  }
}