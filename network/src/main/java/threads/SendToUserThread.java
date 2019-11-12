package threads;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import message.Message;
import provider.NetworkProvider;

public class SendToUserThread extends ThreadExtend {
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
    System.out.println("Send message to a user");
    try {
      Socket socket = new Socket(this.ipDest, NetworkProvider.N_PORT);
      OutputStream outputStream = socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
      objectOutputStream.writeObject(new Message(this.payload));
      socket.close();
    } catch (ConnectException e) {
      System.out.println("Unable to send message. Is receiver listening ?");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void kill() {
    System.out.println("Stop Sending payload to a user");
  }
}
