package message;

import java.net.Socket;

public class Message {

    private Payload payload;

    public Message(Payload p) {
        this.payload = p;
    }

    public void process(DataInterface data, Socket socket){
        //To be define
    }

    public Payload getPayload() {
        return payload;
    }
}


