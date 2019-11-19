package core;

import controllers.AllMusicsController;
import controllers.ForgottenPasswordController;
import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;

import interfaces.DataForIhm;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * the IhmCore will start the HCI of the Application, manages controllers and stage changes.
 */
public class IhmCore extends Application {

  private Scene loginScene;
  private Scene signupScene;
  private Scene forgottenPasswordScene;
  private Scene allMusicsCenterScene;
  private Scene mainScene;

  /**
   * the general scene.
   */
  private Stage primaryStage;

  private MainController mainController;
  private LoginController loginController;
  private SignUpController signUpController;
  private ForgottenPasswordController forgottenPasswordController;
  private AllMusicsController allMusicsController;

  private IhmForData ihmForData;
  
  //TODO
  //just in order to avoid the null value error
  //the start function create a new instance of IhmCore
  //then we need to get the DataForIhm in start function
  //for that I think it's better to create an instance of DataForIhm in ihmProvider
  private static DataForIhm dataForIhm;

  private double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  private double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;


  private static final Logger ihmLogger = LogManager.getLogger();
  
  private static IhmCore ihmCore;

  public IhmCore() {
    ihmLogger.info("IhmCore start");
    ihmCore = this;
    this.ihmForData = new IhmForData(this);
  }

  public static IhmCore getIhmCore() {
    return ihmCore;
  }
  
  /**
   * show the application.
   */
  public void showApplication() {
    if (primaryStage != null) {
      Platform.runLater(new Runnable() {

        @Override
        public void run() {

          primaryStage.show();
          Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
          primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
          primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        }
      });
    }
  }
  
  /**
   * hide the application.
   */
  public void hideApplication() {
    if (primaryStage != null) {
      Platform.runLater(new Runnable() {

        @Override
        public void run() {
          primaryStage.hide();
        }
      });
    }
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

  public Scene getAllMusicsCenterScene() {
    return allMusicsCenterScene;
  }

  public void setAllMusicsCenterScene(Scene allMusicsCenterScene) {
    this.allMusicsCenterScene = allMusicsCenterScene;
  }

  public Scene getMainScene() {
    return mainScene;
  }

  public void setMainScene(Scene mainScene) {
    this.mainScene = mainScene;
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
   * Getter of @see MainController.
   *
   * @return @see MainController
   */
  public MainController getMainController() {
    return this.mainController;
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
   * Getter of @see LoginController.
   *
   * @return @see LoginController
   */
  public LoginController getLoginController() {
    return this.loginController;
  }

  /**
   * Setter of @see LoginController.
   *
   * @param loginController the login Controller
   */
  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
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
   * Setter of @see SignUpController.
   *
   * @param signUpController the signup Controller
   */
  public void setSignUpController(SignUpController signUpController) {
    this.signUpController = signUpController;
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
   * Setter of @see ForgottenPasswordController.
   *
   * @param forgottenPwdController the forgotten password Controller
   */
  public void setForgottenPasswordController(ForgottenPasswordController forgottenPwdController) {
    this.forgottenPasswordController = forgottenPwdController;
  }

  public AllMusicsController getAllMusicsController() {
    return this.allMusicsController;
  }

  public void setAllMusicsController(AllMusicsController allMusicsController) {
    this.allMusicsController = allMusicsController;
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
   * Setter for @see IhmForData.
   *
   * @param ihmForData the integration of ihm interface
   */
  public void setIhmForData(IhmForData ihmForData) {
    this.ihmForData = ihmForData;
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

  public void showMainScene() {
    primaryStage.setScene(mainScene);
    primaryStage.setTitle("Vue principale");
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

    // get the loader for AllMusicsCenterView
    FXMLLoader allMusicsCenterLoader = new FXMLLoader(getClass()
        .getResource("/fxml/AllMusicsCenterView.fxml"));
    Parent allMusicsCenterParent = allMusicsCenterLoader.load();
    allMusicsCenterScene = new Scene(allMusicsCenterParent);

    // get the loader for mainView
    FXMLLoader mainLoader = new FXMLLoader(getClass()
            .getResource("/fxml/MainView.fxml"));
    Parent mainParent = mainLoader.load();
    mainScene = new Scene(mainParent);

    //get the Controllers from loader
    LoginController loginController = loginLoader.getController();
    SignUpController signUpController = signupLoader.getController();
    ForgottenPasswordController forgottenPasswordController =
        forgottenPasswordLoader.getController();
    AllMusicsController allMusicsController = allMusicsCenterLoader.getController();
    MainController mainController = mainLoader.getController();

    //set the Controllers link to acces from the controllers
    this.setLoginController(loginController);
    this.setSignUpController(signUpController);
    this.setForgottenPasswordController(forgottenPasswordController);
    this.setAllMusicsController(allMusicsController);
    this.setMainController(mainController);
    
    //set the IgmCore link into Controllers
    loginController.setIhmCore(this);
    signUpController.setIhmCore(this);
    forgottenPasswordController.setIhmCore(this);
    allMusicsController.setIhmCore(this);
    mainController.setIhmCore(this);

    //initialize the first View
    this.primaryStage = primaryStage;

    //primaryStage.initStyle(StageStyle.UNDECORATED);

    //add the root scene (login)    
    primaryStage.setScene(loginScene);
    primaryStage.setResizable(false);
    //primaryStage.show();
    
    // handler close window
    primaryStage.setOnCloseRequest(event -> {
      System.out.println("Stage is closing");
      if (dataForIhm != null) {
        try {
          dataForIhm.logout();
        } catch (UnsupportedOperationException ex) {
          ex.printStackTrace();
        }
      }
    });
  }
  
  /**
   * Override function called when the user exits the application.
   */
  @Override
  public void stop() {
    // Tell data to exit
    //getDataForIhm().logout();
  }

  /**
   * function who is called from Main to start the HCI.
   *
   * @param args the arguments of the Application from Main method
   */
  public static void run(String[] args) {
    (new Thread() { 
      
      @Override
      public void run() {
        launch(IhmCore.class,args);
      }
    }).start();
  }  
}
