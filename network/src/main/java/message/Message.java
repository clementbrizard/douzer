package message;

import interfaces.DataForNet;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Represents the objects that cross the network. It contains payload
 * due to Data. Default beahaviour is : transmit payload to Data. Can
 * be subclassed to overload default behaviour.
 * 
 * @author Antoine
 *
 */
public class Message implements Serializable {

  private static final long serialVersionUID = -6557831202306199363L;
  
  private Serializable payload;

  public Message(Serializable p) {
    this.payload = p;
  }

  public void process(DataForNet data, Socket socket) {
    try {
      data.process(payload, 
          InetAddress.getByName(socket.getRemoteSocketAddress()
          .toString().split(":")[0].split("/")[1]));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  public Serializable getPayload() {
    return payload;
  }
}


