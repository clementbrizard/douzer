package message;

import interfaces.DataForNet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Message sent to owners of a music one want to download. This message contains
 * informations about the music (hash) and sends the file to the sender of the
 * message in the same socket.
 * 
 * @author Antoine
 *
 */
public class DownloadFile extends Message {

  private static final long serialVersionUID = 7477300323985036967L;
  
  private static final Logger logger = LogManager.getLogger();
  
  private String hashMusic;

  public DownloadFile(Serializable p) {
    super(p);
  }

  @Override
  public void process(DataForNet data, Socket socket) {
    logger.info("Received a download request.");
    
    try {      
      ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
      
      //Send a message to warn distant user that it'll receive a file
      SendFile message = new SendFile(null);
      message.setHashMusic(hashMusic);
      stream.writeObject(message);
      
      
      File file = data.getLocalMusic(hashMusic);
      FileInputStream fis = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(fis); 
            
      //Get socket's output stream
      OutputStream os = socket.getOutputStream();
                  
      //Read File Contents into contents array 
      byte[] contents;
      long fileLength = file.length(); 
      long current = 0;
          
      while (current != fileLength) { 
        int size = 10000;
        if (fileLength - current >= size) {
          current += size;    
        } else { 
          size = (int)(fileLength - current); 
          current = fileLength;
        } 
        contents = new byte[size]; 
        bis.read(contents, 0, size); 
        os.write(contents);
        logger.info("Sending file ... " + (current * 100) / fileLength + "% complete!\n");
      }   
          
      os.flush(); 
      bis.close();
      
      //File transfer done. Close the socket connection!
      socket.close();
          
      logger.info("File sent succesfully!");
    } catch (Exception e) {
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
