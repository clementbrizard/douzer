package provider;

import message.Message;
import threads.MessageProcess;
import threads.RequestDownloadThread;
import threads.SendToUserThread;
import threads.SendToUsersThread;
import threads.Server;
import threads.ThreadExtend;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Collection;
import java.util.stream.Stream;

public class NetworkProvider {

    private DataInterface dataInterface;
    private Server server;
    private NetworkImpl netImpl;
    private Collection<ThreadExtend> threads;

    public NetworkProvider(){

    }

    public NetworkImpl createNetwork(){
        return null;
    }

    public Server createServer(){
        return null;
    }

    public DataInterface getDataImpl(){
        return null;
    }

    public void setDataImpl(DataInterface dataInterface){

    }

    public MessageProcess createMessageProcess(Message m, Socket s){
        return null;
    }

    public SendToUserThread createSendToUserThread(Payload p , InetAddress ip){
        return null;
    }

    public SendToUsersThread createSendToUsersThread(Payload p , Stream<InetAddress> userIps){
        return null;
    }

    public RequestDownloadThread createRequestDownloadThread(Stream<InetAddress> ownersIps, String musicHash){
        return null;
    }
}
