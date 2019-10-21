package interfaces;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;

public interface DataForNet {
  void process(String payload, InetAddress sourceIp);

  void saveMp3(InputStream stream, String musicHash);

  File getLocalMusic(String musicHash);
}
