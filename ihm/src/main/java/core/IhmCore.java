package core;

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
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TestView.fxml"));
    //FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
    //FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/views/SignUpView.fxml"));
    //FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
    Parent root = (Parent) loader.load();
    primaryStage.setTitle("first test");
    //primaryStage.setTitle("login");
    primaryStage.setScene(new Scene(root,witdh,height));
    primaryStage.setMaximized(true);
    primaryStage.show();
    //LoginController ctrl = loader.getController();
    //LoginController signUpCtrl = loaderSignUp.getController();
    //LoginController mainCtrl = loaderMain.getController();
    TestController ctrl = loader.getController();
    //setLoginController(ctrl);
    //setLoginController(signUpCtrl);
    //setLoginController(mainCtrl);
    setTestController(ctrl);
    System.out.println(testController.getLabel().getText());
  }

  public void run(String[] args) {
    launch(args);
  }


}
