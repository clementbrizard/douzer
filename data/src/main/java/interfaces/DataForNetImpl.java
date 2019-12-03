package interfaces;

import core.Datacore;
import core.Payload;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;

public class DataForNetImpl implements DataForNet {
  private Datacore dc;

  public DataForNetImpl(Datacore dc) {
    this.dc = dc;
  }

  @Override
  public void process(Serializable payload, InetAddress sourceIp) {
    try {
      Payload receivedPayload = (Payload) payload;
      receivedPayload.run(this.dc);
    } catch (ClassCastException e) {
      Datacore.getLogger().error("Received object is not a payload.");
    }
  }

  @Override
  public void saveMp3(InputStream stream, String musicHash) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public File getLocalMusic(String musicHash) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
