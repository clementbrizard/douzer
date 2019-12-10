package core;

import datamodel.LocalUser;
import exceptions.data.DataException;
import java.net.InetAddress;
import java.util.UUID;

public abstract class Payload implements java.io.Serializable {
  private UUID senderUuid;

  public Payload(LocalUser user){
    senderUuid = user.getUuid();
  }
  /**
   * Will be overridden for login only in order to access the IP.
   *
   * @param dc       the datacore of the remote application.
   * @param senderIp the IP of the sender passed by network.
   */
  public void process(Datacore dc, InetAddress senderIp) throws DataException {
    if (dc.getUsers().containsKey(this.senderUuid)) {
      this.run(dc);
    } else {
      throw new DataException("Received a message from an unknown user");
    }
  }

  public abstract void run(Datacore dc);
}
