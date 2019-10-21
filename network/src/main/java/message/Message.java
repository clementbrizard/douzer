package message;

import java.net.Socket;

public class Message {

  private String payload;

  public Message(String p) {
    this.payload = p;
  }

  public void process(DataInterface data, Socket socket){
    //To be define
  }

  public String getPayload() {
    return payload;
  }
}


