package core;

import controllers.ForgottenPasswordController;
import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * IHM Core, manages controllers and stage changes.
 */
public class IhmCore extends Application {

  private Scene loginScene;
  private Scene signupScene;
  private Scene forgottenPasswordScene;
  private Stage primaryStage;

  private double witdh = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  private double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

  public IhmCore() {}

  public void showLoginScene() {
    primaryStage.setScene(loginScene);
  }

  public void showSignupScene() {
    primaryStage.setScene(signupScene);
  }

  public void showForgottenPasswordScene() {
    primaryStage.setScene(forgottenPasswordScene);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
    Parent loginParent = loginLoader.load();
    loginScene = new Scene(loginParent,witdh,height);


    FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpView.fxml"));
    Parent signupParent = signupLoader.load();
    signupScene = new Scene(signupParent,witdh,height);

    FXMLLoader forgottenPasswordLoader =
        new FXMLLoader(getClass().getResource("/fxml/ForgottenPasswordView.fxml"));
    Parent forgottenPasswordParent = forgottenPasswordLoader.load();
    forgottenPasswordScene = new Scene(forgottenPasswordParent,witdh,height);

    LoginController loginController = loginLoader.getController();
    SignUpController signUpController = signupLoader.getController();
    ForgottenPasswordController forgottenPasswordController =
        forgottenPasswordLoader.getController();

    loginController.setIhmcore(this);
    signUpController.setIhmCore(this);
    forgottenPasswordController.setIhmCore(this);

    this.primaryStage = primaryStage;
    primaryStage.setMaximized(true);
    primaryStage.setScene(loginScene);
    primaryStage.show();
  }

  public void run(String[] args) {
    launch(args);
  }


}
