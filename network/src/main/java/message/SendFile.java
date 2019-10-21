package message;

import java.net.Socket;

public class SendFile extends Message {
  private String hashMusic;

  public SendFile(String p) {
    super(p);
    // To be define
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
}
