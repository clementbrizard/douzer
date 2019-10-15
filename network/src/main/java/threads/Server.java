package threads;

import java.net.ServerSocket;

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
  
  public Server(NetworkProvider n) {
    this.netProvider = n;
  }
  
  public void kill() {
    //To be define
  }
  
  @Override
  public void run() {
    // TODO Auto-generated method stub
  }

}
