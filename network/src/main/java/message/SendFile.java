package message;

import interfaces.DataForNet;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Message sent to a user who has previously asked to download a file. This
 * message warn the user that it'll receive a file in the socket.
 * 
 * @author Antoine
 *
 */
public class SendFile extends Message {

  private static final long serialVersionUID = -6590037734212272745L;
  
  private static final Logger logger = LogManager.getLogger();

  private String hashMusic;
  private int musicSize;

  public SendFile(Serializable p) {
    super(p);
  }

  @Override
  public void process(DataForNet data, Socket socket) {
    logger.info("Going to receive file");
    try {
      data.saveMp3(socket.getInputStream(), hashMusic, musicSize);
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
  
  public void setMusicSize(int s) {
    this.musicSize = s;
  }
  
  public int getMusicSize() {
    return this.musicSize;
  }
}
