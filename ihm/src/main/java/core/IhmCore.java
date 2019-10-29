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

/**
 *  the IhmCore will start the HCI of the Application and get the main Controllers.
 */
public class IhmCore extends Application {

  private MainController mainController;
  private LoginController loginController;
  private SignUpController signUpController;

  private TestController testController;
  
  //private DataInterface dataInterface

  double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

  public IhmCore() {}

  /**
   *  Setter of @see MainController.
   *  @param mainController the main Controller
   */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }
  
  public void setTestController(TestController testController) {
    this.testController = testController;
  }

  /**
   * Setter of @see LoginController.
   * @param loginController the login Controller
   */
  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
  }

  /**
   * Setter of @see SignUpController.
   * @param signUpController the signup Controller
   */
  public void setSignUpController(SignUpController signUpController) {
    this.signUpController = signUpController;
  }

  /**
   * Getter of @see MainController.
   * @return @see MainController
   */
  public MainController getMainController() {
    return this.mainController;
  }

  /**
   * Getter of @see LoginController.
   * @return @see LoginController
   */
  public LoginController getLoginController() {
    return this.loginController;
  }

  /**
   * Getter of @see SignUpController.
   * @return @see SignUpController
   */
  public SignUpController getSignUpController() {
    return this.signUpController;
  }

  public TestController getTestController() {
    return testController;
  }


  /**
   * start method who load the first views and get their Controllers .
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    
    //load the view from fxml  
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TestView.fxml"));
    //FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
    //FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/views/SignUpView.fxml"));
    //FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
    
    //get the first view (login)
    Parent root = (Parent) loader.load();
    
    //set the Title
    primaryStage.setTitle("first test");
    //primaryStage.setTitle("login");
    
    //add the root scene (login)
    primaryStage.setScene(new Scene(root,witdh,height));
    primaryStage.setMaximized(true);
    
    primaryStage.show();
    
    //get the different Controllers
    //LoginController ctrl = loader.getController();
    //LoginController signUpCtrl = loaderSignUp.getController();
    //LoginController mainCtrl = loaderMain.getController();
    TestController ctrl = loader.getController();
    
    //set Controllers
    //setLoginController(ctrl);
    //setLoginController(signUpCtrl);
    //setLoginController(mainCtrl);
    setTestController(ctrl);
    
    //check into the console if we can get the Label from the testController
    System.out.println(testController.getLabel().getText());
  }

  
  /**
   * function who is called from Main to start the HCI.
   * @param args the arguments of the Application from Main method
   */
  public void run(String[] args) {
    /**
     * launch function will call the function {@link #start(Stage) void}.
     */
    launch(args);
  }


}
