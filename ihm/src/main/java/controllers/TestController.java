package controllers;

import java.util.Random;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.controlsfx.control.Notifications;

public class TestController implements Controller {
  static int count = 0;
  
  @FXML
  private Label label;
  
  @FXML
  private Button buttonClick;
  
  @Override
  public void initialize() {
    String javaVersion = System.getProperty("java.version");
    String javafxVersion = System.getProperty("javafx.version");
    
    label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
  }

  public Label getLabel() {
    return label;
  }
  
  @FXML
  private void test(ActionEvent event) {
    count++;
    launchThread(count);
    buttonClick.setText("click " + count + " time");
    label.setText("appuy�");        
  }

  private void launchThread(int countCopy) {
    Random rand = new Random();
    int[] n = {rand.nextInt(5)};
    n[0]++;
    
    //start a service (like a thread) 
    Service<Void> service = new Service<Void>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            System.out.println("dans le thread");
            Thread.sleep(n[0] * 1000);
            System.out.println("apr�s " + n[0] + " secondes");
            
            //function runLater who permit to run a task in thread
            Platform.runLater(() -> {
              //one notification
              Notifications.create()
                           .title("Title Text")
                           .text("pop up n� " + countCopy + " apr�s  : " + n[0] +  " secondes")
                           .darkStyle()
                           .position(Pos.TOP_LEFT)
                           .showWarning();
            });
            count++;
            System.out.println("apr�s la notification");
            return null;
            }
          };
        }
    };
    service.start();
  }
}
