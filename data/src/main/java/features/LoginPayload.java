package features;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.User;
import java.net.InetAddress;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * Connection handshake
 * Payload for "Hey, I connected ! Here is my profile and my shared musics"
 * The "my shared music" part is handled with the ShareMusicsPayload
 */
public class LoginPayload extends ShareMusicsPayload {
  private User user;

  LoginPayload(LocalUser user) {
    super(
        user.getMusics().stream()
            .filter(LocalMusic::isShared)
            .collect(Collectors.toSet())
    );
    // copy constructor instead of cast to not send sensitive info over the network
    this.user = new User(user);
  }

  @Override
  public void process(Datacore dc, InetAddress senderIp) {
    LoginResponsePayload response = new LoginResponsePayload(
        dc.getCurrentUser(),
        dc.getOnlineUsers().map(User::getIp).collect(Collectors.toSet())
    );
    dc.net.sendToUser(response, senderIp);
    this.updateData(dc, senderIp);
  }

  void updateData(Datacore dc, InetAddress senderIp) {
    this.user.setIp(senderIp);
    dc.addUser(this.user);
    dc.ihm.notifyUserConnection(this.user);
    super.run(dc); // update musics
  }

  /*
   * Connection handshake
   * Payload for "Hey, I see you just connected ! Here is me, the ips I know and my shared musics"
   * Extends LoginPayload because the "me and my shared music" bit is the same
   */
  static class LoginResponsePayload extends LoginPayload {
    private Set<InetAddress> otherIps;

    LoginResponsePayload(LocalUser user, Set<InetAddress> otherIps) {
      super(user);
      this.otherIps = otherIps;
    }

    @Override
    public void process(Datacore dc, InetAddress senderIp) {
      this.updateData(dc, senderIp);

      Set<InetAddress> knownIps = dc.getOnlineUsers()
          .map(User::getIp)
          .collect(Collectors.toSet());

      // keep only the IPs that we didn't know about (to not send infinite login payloads)
      this.otherIps.removeAll(knownIps);
      LoginPayload payload = new LoginPayload(dc.getCurrentUser());
      // send new handshake to these new ips
      dc.net.connect(payload, this.otherIps);
    }
  }
}
