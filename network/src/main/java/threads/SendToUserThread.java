package threads;

import message.Message;

import java.net.InetAddress;

public class SendToUserThread extends ThreadExtend {
    private Payload payload;
    private InetAddress ipDest;

    public SendToUserThread(Payload p, InetAddress ip){
        this.payload = p;
        this.ipDest = ip;
    }

    @Override
    public void run() {
        //To be define
    }

    @Override
    public void kill() {
        //To be define
    }
}
