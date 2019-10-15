package message;

public class SendFile extends Message {
    private String hashMusic;

    public SendFile(Payload p) {
        // To be define
    }

    @Override
    public void process(Datacore datacore, Socket socket) {
        //To be define
    }

    public String getHashMusic() {
        return hashMusic;
    }

    public void setHashMusic(String hashMusic) {
        this.hashMusic = hashMusic;
    }
}
