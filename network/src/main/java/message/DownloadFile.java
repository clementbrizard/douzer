package message;

import interfaces.DataForNet;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;

public class DownloadFile extends Message {

  private static final long serialVersionUID = 7477300323985036967L;
  
  private String hashMusic;
  private InetAddress ip;

  public DownloadFile(Serializable p) {
    super(p); //Call constructor for mother class
    // To be define initialisation for hashMusic and ip
  }

  @Override
  public void process(DataForNet data, Socket socket) {
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
