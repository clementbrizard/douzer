package threads;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import message.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import provider.NetworkProvider;

public class SendToUserThread extends ThreadExtend {
  
  private static final Logger logger = LogManager.getLogger();
  
  private Serializable payload;
  private InetAddress ipDest;

  /**
   * create a socket to send a payload to a user.
   * @param p payload of message to send
   * @param ip ip address of the destination
   */
  public SendToUserThread(Serializable p, InetAddress ip) {
    this.payload = p;
    this.ipDest = ip;
  }

  @Override
  public void run() {
    logger.info("Send message to a user");
    try {
      Socket socket = new Socket(this.ipDest, NetworkProvider.N_PORT);
      OutputStream outputStream = socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeObject(new Message(this.payload));
      socket.close();
    } catch (ConnectException e) {
      logger.error("Unable to send message. Is receiver listening ?");
    } catch (Exception e) {
      logger.error(e);
      e.printStackTrace();
    }
  }

  @Override
  public void kill() {
    logger.info("Stop Sending payload to a user");
  }
}
