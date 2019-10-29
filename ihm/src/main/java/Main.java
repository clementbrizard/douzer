import core.IhmCore;

public class Main {
  /**
   * Starts the whole application : first Data, then Network, then Ihm.
   * @param args the eventual arguments to start the application
  */
  public static void main(String[] args) {
    //start data
    //start network
    IhmCore ihmCore = new IhmCore();
    ihmCore.run(args);
  }

}