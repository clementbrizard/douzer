package core;

import controllers.ForgottenPasswordController;
import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;
import controllers.TestController;

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

  private TestController testController;

  double witdh = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

  public IhmCore() {}

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setTestController(TestController testController) {
    this.testController = testController;
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

  public TestController getTestController() {
    return testController;
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
    Parent loginParent = loginLoader.load();
    Scene loginScene = new Scene(loginParent,witdh,height);


    FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpView.fxml"));
    Parent signupParent = signupLoader.load();
    Scene signupScene = new Scene(signupParent,witdh,height);

    FXMLLoader forgottenPasswordLoader =
        new FXMLLoader(getClass().getResource("/fxml/ForgottenPasswordView.fxml"));
    Parent forgottenPasswordParent = forgottenPasswordLoader.load();
    Scene forgottenPasswordScene = new Scene(forgottenPasswordParent,witdh,height);

    LoginController loginController = loginLoader.getController();
    SignUpController signUpController = signupLoader.getController();
    ForgottenPasswordController forgottenPasswordController =
        forgottenPasswordLoader.getController();

    loginController.setAdjacentScenes(signupScene, forgottenPasswordScene);
    signUpController.setLoginScene(loginScene);
    forgottenPasswordController.setLoginScene(loginScene);

    loginController.setIhmcore(this);
    signUpController.setIhmCore(this);
    forgottenPasswordController.setIhmCore(this);

    //FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
    //Parent root = (Parent) login.load();
    //primaryStage.setScene(new Scene(root,witdh,height));


    primaryStage.setMaximized(true);
    primaryStage.setScene(loginScene);
    primaryStage.show();
    //TestController ctrl = loader.getController();
    //setTestController(ctrl);
    //System.out.println(testController.getLabel().getText());
  }

  public void run(String[] args) {
    launch(args);
  }


}
