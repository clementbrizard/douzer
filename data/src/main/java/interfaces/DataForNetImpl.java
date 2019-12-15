package interfaces;

import core.Datacore;
import core.Payload;
import exceptions.data.DataException;

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
      Datacore.getLogger().debug("Received and processing payload " + payload.getClass() +
          " from " + sourceIp);
      receivedPayload.process(this.dc, sourceIp);
    } catch (ClassCastException e) {
      Datacore.getLogger().error("Received object is not a payload.");
    } catch (DataException e) {
      Datacore.getLogger().error(e);
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
