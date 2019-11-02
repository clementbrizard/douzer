package core;

import controllers.ForgottenPasswordController;
import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;

import interfaces.DataForIhm;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * the IhmCore will start the HCI of the Application, manages controllers and stage changes.
 */
public class IhmCore extends Application {

  private Scene loginScene;
  private Scene signupScene;
  private Scene forgottenPasswordScene;

  /**
   * the general scene.
   */
  private Stage primaryStage;


  private MainController mainController;
  private LoginController loginController;
  private SignUpController signUpController;
  private ForgottenPasswordController forgottenPasswordController;


  private IhmForData ihmForData;
  
  //TODO
  //just in order to avoid the null value error
  //the start function create a new instance of IhmCore
  //then we need to get the DataForIhm in start function
  //for that I think it's better to create an instance of DataForIhm in ihmProvider
  private static DataForIhm dataForIhm;

  private double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  private double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

  public IhmCore() {
    this.ihmForData = new IhmForData(this);
  }


  /**
   * Setter for @see IhmForData.
   *
   * @param ihmForData the integration of ihm interface
   */
  public void setIhmForData(IhmForData ihmForData) {
    this.ihmForData = ihmForData;
  }

  /**
   * Setter of @see MainController.
   *
   * @param mainController the main Controller
   */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  /**
   * Setter of @see LoginController.
   *
   * @param loginController the login Controller
   */
  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
  }

  public Scene getLoginScene() {
    return loginScene;
  }


  public void setLoginScene(Scene loginScene) {
    this.loginScene = loginScene;
  }


  public Scene getSignupScene() {
    return signupScene;
  }


  public void setSignupScene(Scene signupScene) {
    this.signupScene = signupScene;
  }


  public Scene getForgottenPasswordScene() {
    return forgottenPasswordScene;
  }


  public void setForgottenPasswordScene(Scene forgottenPasswordScene) {
    this.forgottenPasswordScene = forgottenPasswordScene;
  }


  public Stage getPrimaryStage() {
    return primaryStage;
  }


  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }
  
  public void setDataForIhm(DataForIhm dataForIhm) {
    this.dataForIhm = dataForIhm;
  }
  
  public DataForIhm getDataForIhm() {
    return this.dataForIhm;
  }


  /**
   * Setter of @see SignUpController.
   *
   * @param signUpController the signup Controller
   */
  public void setSignUpController(SignUpController signUpController) {
    this.signUpController = signUpController;
  }

  /**
   * Setter of @see ForgottenPasswordController.
   *
   * @param forgottenPwdController the forgotten password Controller
   */
  public void setForgottenPasswordController(ForgottenPasswordController forgottenPwdController) {
    this.forgottenPasswordController = forgottenPwdController;
  }

  /**
   * Getter of @see IhmForData.
   *
   * @return @see IhmForData
   */
  public IhmForData getIhmForData() {
    return ihmForData;
  }

  /**
   * Getter of @see MainController.
   *
   * @return @see MainController
   */
  public MainController getMainController() {
    return this.mainController;
  }

  /**
   * Getter of @see LoginController.
   *
   * @return @see LoginController
   */
  public LoginController getLoginController() {
    return this.loginController;
  }

  /**
   * Getter of @see SignUpController.
   *
   * @return @see SignUpController
   */
  public SignUpController getSignUpController() {
    return this.signUpController;
  }

  /**
   * Getter of @see ForgottenPasswordController.
   *
   * @return @see ForgottenPasswordController
   */
  public ForgottenPasswordController getForgottenPasswordController() {
    return this.forgottenPasswordController;
  }


  /**
   * change the mainScene into LoginScene.
   */
  public void showLoginScene() {
    primaryStage.setScene(loginScene);

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    primaryStage.setTitle("Connexion");
  }

  /**
   * change the mainScene into SignupScene.
   */
  public void showSignupScene() {
    primaryStage.setScene(signupScene);

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    primaryStage.setTitle("Inscription");
  }

  /**
   * change the mainScene into ForgottenPasswordScene.
   */
  public void showForgottenPasswordScene() {
    primaryStage.setScene(forgottenPasswordScene);

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    primaryStage.setTitle("Mot de passe oubli�");
  }

  /**
   * start method who initialize and load the first views and manage theirs controllers.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    //get the loader for LoginView
    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
    Parent loginParent = loginLoader.load();
    loginScene = new Scene(loginParent);

    //get the loader for SignUpView
    FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpView.fxml"));
    Parent signupParent = signupLoader.load();
    signupScene = new Scene(signupParent);

    //get the loader for ForgottenPasswordView
    FXMLLoader forgottenPasswordLoader =
        new FXMLLoader(getClass().getResource("/fxml/ForgottenPasswordView.fxml"));
    Parent forgottenPasswordParent = forgottenPasswordLoader.load();
    forgottenPasswordScene = new Scene(forgottenPasswordParent);

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
    loginController.setIhmCore(this);
    signUpController.setIhmCore(this);
    forgottenPasswordController.setIhmCore(this);

    //initialize the first View
    this.primaryStage = primaryStage;

    //primaryStage.initStyle(StageStyle.UNDECORATED);

    //add the root scene (login)    
    primaryStage.setScene(loginScene);
    primaryStage.setResizable(false);
    primaryStage.show();

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
  }

  /**
   * function who is called from Main to start the HCI.
   *
   * @param args the arguments of the Application from Main method
   */
  public void run(String[] args) {
    /**
     * launch function will call the function {@link #start(Stage) void}.
     */
    launch(args);
  }  
}
