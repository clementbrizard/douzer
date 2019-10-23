package interfaces;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;

public class DataForNetImpl implements DataForNet {
  @Override
  public void process(Serializable payload, InetAddress sourceIp) {
    throw new UnsupportedOperationException("Not implemented yet");
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
