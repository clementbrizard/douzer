package core;

import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//replace by javadocs
//start the FX application
public class IhmCore extends Application {

  private MainController mainController;
  private LoginController loginController;
  private SignUpController signUpController;

  double witdh = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

  public IhmCore() {}

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
  }

  public void setSignUpController(SignUpController signUpController) {
    this.signUpController = signUpController;
  }

  public MainController getMainController() {
    return this.mainController;
  }

  public LoginController getLoginController() {
    return this.loginController;
  }

  public SignUpController getSignUpController() {
    return this.signUpController;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/views/TestView.fxml"));
    primaryStage.setTitle("first test");
    primaryStage.setScene(new Scene(root,witdh,height));
    primaryStage.setMaximized(true);
    primaryStage.show();
  }

  public void run(String[] args) {
    launch(args);
  }


}
