package message;

import java.io.Serializable;
import java.net.Socket;

public class Message {

  private Serializable payload;

  public Message(Serializable p) {
    this.payload = p;
  }

  public void process(DataInterface data, Socket socket){
    //To be define
  }

  public Serializable getPayload() {
    return payload;
  }
}


