import core.IhmCore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main {
  /**
   * start application main.
   */
  public static void main(String[] args) {
    //start data
    //start network
    IhmCore ihmCore = new IhmCore();
    ihmCore.run(args);
  }

}
