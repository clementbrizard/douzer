package message;

public class Message {

    private Payload payload;

    public Message(Payload p) {
       return this.payload = p;
    }

    public void process(Datacore datacore, Socket socket){
        //To be define
    }
}
