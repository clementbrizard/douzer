package message;

import interfaces.DataForNet;

import java.io.Serializable;
import java.net.Socket;

public class SendFile extends Message {

  private static final long serialVersionUID = -6590037734212272745L;
  
  private String hashMusic;

  public SendFile(Serializable p) {
    super(p);
    // To be define
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
}
