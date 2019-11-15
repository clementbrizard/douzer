package threads;

import java.net.Socket;

import message.Message;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import provider.NetworkProvider;

/**
 * Class that will execute the appropriate behaviour depending on the given message
 * given in constructor. It is just supposed to execute message.process(...) in a 
 * separate thread in order not to block the application.
 * 
 * @author Antoine
 *
 */
public class MessageProcess extends ThreadExtend {
  
  private static final Logger logger = LogManager.getLogger();

  private Message message;
  private Socket socket;
  private NetworkProvider netProvider;
  

  /**
   * Simple constructor.
   * 
   * @param m : message received, will call m.process
   * @param s : socket through which the message has been received
   * @param np : current instance of the networkProvider
   */
  public MessageProcess(Message m, Socket s, NetworkProvider np) {
    this.message = m;
    this.socket = s;
    this.netProvider = np;
  }
  
  @Override
  public void run() {
    logger.info("Message received");
    this.message.process(this.netProvider.getDataImpl(), socket);
  }

  @Override
  public void kill() {
    logger.info("Killing MessageProcess thread");
  }

}
