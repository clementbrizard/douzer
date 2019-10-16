import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  
  double witdh = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/views/TestView.fxml"));
    primaryStage.setTitle("first test");
    primaryStage.setScene(new Scene(root,witdh,height));
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  public static void main(String[] args) {
    System.out.println("start application");
    launch(args);
  }
}