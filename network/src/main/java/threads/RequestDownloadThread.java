package threads;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import message.DownloadFile;
import message.Message;
import message.SendFile;
import provider.NetworkProvider;

public class RequestDownloadThread extends ThreadExtend {

  private static final Logger logger = LogManager.getLogger();
  
  private List<InetAddress> ownersIps;
  private String musicHash;
  private NetworkProvider netProvider;
  private boolean threadRunning;

  public RequestDownloadThread(NetworkProvider np, Stream<InetAddress> ownersIps, String musicHash) {
    this.ownersIps = ownersIps.collect(Collectors.toList());
    this.musicHash = musicHash;
    this.netProvider = np;
    this.threadRunning = true;
  }

  @Override
  public void run() {
    logger.info("Sending download request to several users");
    
    DownloadFile messageToSend = new DownloadFile(null);
    messageToSend.setHashMusic(this.musicHash);
    
    for (InetAddress ipDest : ownersIps) {
      try {
        if (!this.threadRunning) return;
        
        Socket socket = new Socket(ipDest, NetworkProvider.N_PORT);
        
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(messageToSend);
        
        ObjectInputStream receivedObject = new ObjectInputStream(socket.getInputStream());
        SendFile receivedMessage = (SendFile) receivedObject.readObject();
        
        receivedMessage.process(this.netProvider.getDataImpl(), socket);
        
        socket.close();
        
        break;
      } catch (ConnectException e) {
        logger.error("Unable to send message. Is receiver listening ?");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void kill() {
    this.threadRunning = false;
  }
}
