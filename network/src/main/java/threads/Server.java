package threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import message.Message;
import provider.NetworkProvider;

/**
 * Server class running in a different thread. It listens to the default
 * port and will ask network provider to process messages in another thread when
 * a message is received.
 * 
 * @author Antoine
 */
public class Server extends Thread {

  private ServerSocket listeningSocket;
  private NetworkProvider netProvider;
  private boolean serverRunning = true;
  
  /**
   * Constructor that initiates the network provider with the current instance.
   * 
   * @param n current instance of the NetworkProvider
   */
  public Server(NetworkProvider n) {
    this.netProvider = n;
  }
  
  /**
   * Should end 'properly' the thread execution.
   */
  public void kill() {
    this.serverRunning = false;
    try {
      this.listeningSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Infinite loop that wait for messages to be received and request the network
   * provider to process the message in another thread.
   */
  @Override
  public void run() {
    try {
      this.listeningSocket = new ServerSocket(6549);
      while (this.serverRunning) {
        System.out.println("Waiting for connection...");
        
        Socket socket = this.listeningSocket.accept();
        
        ObjectInputStream receivedObject = new ObjectInputStream(socket.getInputStream());
        
        Message receivedMessage = (Message) receivedObject.readObject();
        this.netProvider.createMessageProcess(receivedMessage, socket);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
