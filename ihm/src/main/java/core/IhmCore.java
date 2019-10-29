package core;

import controllers.ForgottenPasswordController;
import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;

import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *  the IhmCore will start the HCI of the Application, manages controllers and stage changes
 */
public class IhmCore extends Application {

  private Scene loginScene;
  private Scene signupScene;
  private Scene forgottenPasswordScene;
  
  private Stage primaryStage;

  
  private MainController mainController;
  private LoginController loginController;
  private SignUpController signUpController;
  private ForgottenPasswordController forgottenPasswordController;
  
  //private DataInterface dataInterface

  private double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  private double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;
  
  public IhmCore() {}

  /**
   *  Setter of @see MainController.
   *  @param mainController the main Controller
  */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
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
   * Setter of @see ForgottenPasswordController.
   * @param forgottenPasswordController the forgotten password Controller
   */
  public void setForgottenPasswordController(ForgottenPasswordController forgottenPasswordController) {
    this.forgottenPasswordController = forgottenPasswordController;
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
  
  /**
   * Getter of @see ForgottenPasswordController.
   * @return @see ForgottenPasswordController
   */
  public ForgottenPasswordController getForgottenPasswordController() {
    return this.forgottenPasswordController;
  }

  
  /**
   * change the mainScene into LoginScene
   */
  public void showLoginScene() {
    primaryStage.setScene(loginScene);
    
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    
    primaryStage.setTitle("Connexion");
  }

  /**
   * change the mainScene into SignupScene
   */
  public void showSignupScene() {
    primaryStage.setScene(signupScene);
    
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    
    primaryStage.setTitle("Inscription");
  }

  /**
   * change the mainScene into ForgottenPasswordScene
   */
  public void showForgottenPasswordScene() {
    primaryStage.setScene(forgottenPasswordScene);
    
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    
    primaryStage.setTitle("Mot de passe oublié");
  }

  /**
   * start method who initialize and load the first views and manage theirs controllers.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    
    //get the loader for LoginView
    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
    Parent loginParent = loginLoader.load();
    loginScene = new Scene(loginParent,width,height);

    //get the loader for SignUpView
    FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpView.fxml"));
    Parent signupParent = signupLoader.load();
    signupScene = new Scene(signupParent,width,height);

    //get the loader for ForgottenPasswordView
    FXMLLoader forgottenPasswordLoader =
        new FXMLLoader(getClass().getResource("/fxml/ForgottenPasswordView.fxml"));
    Parent forgottenPasswordParent = forgottenPasswordLoader.load();
    forgottenPasswordScene = new Scene(forgottenPasswordParent,width,height);

    //get Controllers from loader
    LoginController loginController = loginLoader.getController();
    SignUpController signUpController = signupLoader.getController();
    ForgottenPasswordController forgottenPasswordController =
        forgottenPasswordLoader.getController();
    
    //set the Controllers link to acces from the controllers
    
    setLoginController(loginController);
    setSignUpController(signUpController);
    setForgottenPasswordController(forgottenPasswordController);
    
    //set the IgmCore link into Controllers
    loginController.setIhmcore(this);
    signUpController.setIhmCore(this);
    forgottenPasswordController.setIhmCore(this);

    //initialize the first View
    this.primaryStage = primaryStage;
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    primaryStage.setScene(loginScene);
    primaryStage.show();
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
