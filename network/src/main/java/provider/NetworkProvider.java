package provider;

import implementation.NetworkImpl;

import interfaces.DataForNet;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import message.Message;

import threads.MessageProcess;
import threads.RequestDownloadThread;
import threads.SendToUserThread;
import threads.Server;
import threads.ThreadExtend;

/**
 * Core class of network module. It stores references to every created threads
 * and provide interface to interact with the module.
 * 
 * @author Antoine
 *
 */
public class NetworkProvider {

  public static final int N_PORT = 1026;
  private DataForNet dataInterface;
  private Server server;
  private NetworkImpl netImpl;
  private Collection<ThreadExtend> threads;

  public NetworkProvider() {
    this.netImpl = new NetworkImpl(this);
    this.threads = new ArrayList<ThreadExtend>();
  }

  /**
   * Simple getter for network implementation.
   * 
   * @return Current instance of the network implementation
   */
  public NetworkImpl getNetwork() {
    return this.netImpl;
  }

  /**
   * Create the server/listener thread and return the instance. If a server thread
   * is already running, does not create a new instance but return the running one.
   * 
   * @return current instance of the server thread
   */
  public Server createServer() {
    if (server == null || !server.isAlive()) {
      this.server = new Server(this);
      this.server.start();
    }
    //No need to recreate server
    return this.server;
  }

  /**
   * Simple getter for data interface implementation.
   * 
   * @return current instance of the implemetation of data interface
   */
  public DataForNet getDataImpl() {
    return this.dataInterface;
  }

  /**
   * Allows users to set the instance bind to the DataInterface interface.
   * 
   * @param dataInterface implementation of DataInterface
   */
  public void setDataImpl(DataForNet dataInterface) {
    this.dataInterface = dataInterface;
  }

  
  /**
   * Create a new thread to process a given message. It adds the thread to the list of the
   * running threads and return the instance.
   * 
   * @param m message to process
   * @param s socket through which the message has been received
   * @return created instance of the new thread
   */
  public MessageProcess createMessageProcess(Message m, Socket s) {
    MessageProcess newThread = new MessageProcess(m, s, this);
    threads.add(newThread);
    newThread.start();
    return newThread;
  }

  /**
   * Create a new dedicated thread to send a message to a given user, and returns the
   * instance of the newly created thread.
   * 
   * @param p payload to be sent in the message
   * @param ip ip address of the receiver
   * @return instance of the newly created thread
   */
  public SendToUserThread createSendToUserThread(Serializable p, InetAddress ip) {
    if (ip != null) {
      SendToUserThread newThread = new SendToUserThread(p, ip);
      threads.add(newThread);
      newThread.start();
      return newThread;
    } else {
      return null;
    }
  }

  /**
   * Create a new dedicated thread to send a download request to a given set of users,
   * and returns the instance of the newly created thread.
   * 
   * @param ownersIps ips where the music can be downloaded
   * @param musicHash hash of the desired music
   * @return instance of the newly created thread
   */
  public RequestDownloadThread createRequestDownloadThread(Stream<InetAddress> ownersIps,
      String musicHash) {
    RequestDownloadThread newThread = new RequestDownloadThread(this, ownersIps, musicHash);
    threads.add(newThread);
    newThread.start();
    return newThread;
  }
  
  /**
   * Simple getter for threads.
   * 
   * @return collection of the current running threads
   */
  public Collection<ThreadExtend> getThreads() {
    return this.threads;
  }
}
