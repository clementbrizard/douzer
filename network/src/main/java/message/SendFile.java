package message;

import interfaces.DataForNet;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendFile extends Message {

  private static final long serialVersionUID = -6590037734212272745L;
  
  private static final Logger logger = LogManager.getLogger();

  private String hashMusic;

  public SendFile(Serializable p) {
    super(p);
  }

  @Override
  public void process(DataForNet data, Socket socket) {
    logger.info("Going to receive file");
    try {
      data.saveMp3(socket.getInputStream(), hashMusic);
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getHashMusic() {
    return hashMusic;
  }

  public void setHashMusic(String hashMusic) {
    this.hashMusic = hashMusic;
  }
}
