package message;

import java.net.InetAddress;
import java.net.Socket;

public class DownloadFile extends Message {

  private String hashMusic;
  private InetAddress ip;

  public DownloadFile(String p) {
    super(p); //Call constructor for mother class
    // To be define initialisation for hashMusic and ip
  }

  @Override
  public void process(DataInterface data, Socket socket) {
    //To be define
  }

  public String getHashMusic() {
    return hashMusic;
  }

  public void setHashMusic(String hashMusic) {
    this.hashMusic = hashMusic;
  }

  public InetAddress getIp() {
    return ip;
  }

  public void setIp(InetAddress ip) {
    this.ip = ip;
  }
}
