package message;

import interfaces.DataForNet;

import java.io.Serializable;
import java.net.Socket;

public class Message implements Serializable {

  private static final long serialVersionUID = -6557831202306199363L;
  
  private Serializable payload;

  public Message(Serializable p) {
    this.payload = p;
  }

  public void process(DataForNet data, Socket socket){
    //To be define
  }

  public Serializable getPayload() {
    return payload;
  }
}


