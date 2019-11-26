package core;

import controllers.AllMusicsController;
import controllers.ForgottenPasswordController;
import controllers.LoginController;
import controllers.MainController;
import controllers.SignUpController;

import java.awt.Toolkit;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;

import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application extends javafx.application.Application {
  private static final Logger applicationLogger = LogManager.getLogger();

  // Scenes
  private Scene loginScene;
  private Scene signUpScene;
  private Scene forgottenPasswordScene;
  private Scene allMusicsCenterScene;
  private Scene mainScene;

  // The general stage
  private Stage primaryStage;

  // Controllers
  private MainController mainController;
  private LoginController loginController;
  private SignUpController signUpController;
  private ForgottenPasswordController forgottenPasswordController;
  private AllMusicsController allMusicsController;

  // Window size properties
  private double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2;
  private double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2;

  // Reference to ihmCore
  private IhmCore ihmCore;

  // Constructor
  public Application() {
    applicationLogger.info("IhmCore start");
  }

  // Getters for scenes
  public Scene getLoginScene() {
    return loginScene;
  }

  public Scene getSignUpScene() {
    return signUpScene;
  }

  public Scene getForgottenPasswordScene() {
    return forgottenPasswordScene;
  }

  public Scene getAllMusicsCenterScene() {
    return allMusicsCenterScene;
  }

  public Scene getMainScene() {
    return mainScene;
  }

  // Getter for general stage
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  // Getters for controllers
  public MainController getMainController() {
    return this.mainController;
  }

  public LoginController getLoginController() {
    return this.loginController;
  }

  public SignUpController getSignUpController() {
    return this.signUpController;
  }

  public ForgottenPasswordController getForgottenPasswordController() {
    return this.forgottenPasswordController;
  }

  public AllMusicsController getAllMusicsController() {
    return this.allMusicsController;
  }

  // Getter for reference to ihmCore
  public IhmCore getIhmCore() {
    return this.ihmCore;
  }

  // Setters for scenes
  public void setLoginScene(Scene loginScene) {
    this.loginScene = loginScene;
  }

  public void setSignUpScene(Scene signUpScene) {
    this.signUpScene = signUpScene;
  }

  public void setForgottenPasswordScene(Scene forgottenPasswordScene) {
    this.forgottenPasswordScene = forgottenPasswordScene;
  }

  public void setAllMusicsCenterScene(Scene allMusicsCenterScene) {
    this.allMusicsCenterScene = allMusicsCenterScene;
  }

  public void setMainScene(Scene mainScene) {
    this.mainScene = mainScene;
  }

  // Setter for general stage
  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  // Setters for controllers
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
  }

  public void setSignUpController(SignUpController signUpController) {
    this.signUpController = signUpController;
  }

  public void setForgottenPasswordController(ForgottenPasswordController forgottenPwdController) {
    this.forgottenPasswordController = forgottenPwdController;
  }

  public void setAllMusicsController(AllMusicsController allMusicsController) {
    this.allMusicsController = allMusicsController;
  }

  // Other methods

  /**
   * Show the application and init the controllers.
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
   * Hide the application.
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

  /**
   * Change the mainScene into loginScene.
   */
  public void showLoginScene() {
    primaryStage.setScene(loginScene);

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    primaryStage.setTitle("Connexion");
  }

  /**
   * Change the mainScene into signUpScene.
   */
  public void showSignUpScene() {
    primaryStage.setScene(signUpScene);

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    primaryStage.setTitle("Inscription");
  }

  /**
   * Change the mainScene into forgottenPasswordScene.
   */
  public void showForgottenPasswordScene() {
    primaryStage.setScene(forgottenPasswordScene);

    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
    primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);

    primaryStage.setTitle("Mot de passe oublié");
  }

  /**
   * Show main scene.
   */
  public void showMainScene() {
    primaryStage.setScene(mainScene);
    primaryStage.setTitle("Vue principale");
  }

  /**
   * Start method which initialize and load the first views and manage theirs controllers.
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    this.ihmCore = IhmCore.init();

    //  Get the loader for LoginView
    FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
    Parent loginParent = loginLoader.load();
    loginScene = new Scene(loginParent);

    //  Get the loader for SignUpView
    FXMLLoader signupLoader = new FXMLLoader(getClass().getResource("/fxml/SignUpView.fxml"));
    Parent signupParent = signupLoader.load();
    signUpScene = new Scene(signupParent);

    // Get the loader for ForgottenPasswordView
    FXMLLoader forgottenPasswordLoader =
        new FXMLLoader(getClass().getResource("/fxml/ForgottenPasswordView.fxml"));
    Parent forgottenPasswordParent = forgottenPasswordLoader.load();
    forgottenPasswordScene = new Scene(forgottenPasswordParent);

    // Get the loader for AllMusicsCenterView
    FXMLLoader allMusicsCenterLoader = new FXMLLoader(getClass()
        .getResource("/fxml/AllMusicsCenterView.fxml"));
    Parent allMusicsCenterParent = allMusicsCenterLoader.load();
    allMusicsCenterScene = new Scene(allMusicsCenterParent);

    // Get the loader for MainView
    FXMLLoader mainLoader = new FXMLLoader(getClass()
        .getResource("/fxml/MainView.fxml"));
    Parent mainParent = mainLoader.load();
    mainScene = new Scene(mainParent);

    // Get the Controllers from loader
    LoginController loginController = loginLoader.getController();
    SignUpController signUpController = signupLoader.getController();
    ForgottenPasswordController forgottenPasswordController =
        forgottenPasswordLoader.getController();
    AllMusicsController allMusicsController = allMusicsCenterLoader.getController();
    MainController mainController = mainLoader.getController();

    // Set the Controllers link to access from the controllers
    this.setLoginController(loginController);
    this.setSignUpController(signUpController);
    this.setForgottenPasswordController(forgottenPasswordController);
    this.setAllMusicsController(allMusicsController);
    this.setMainController(mainController);

    // Set the link to ihmCore for controllers
    loginController.setApplication(this);
    signUpController.setApplication(this);
    forgottenPasswordController.setApplication(this);
    allMusicsController.setApplication(this);
    mainController.setApplication(this);

    // Initialize the first view
    this.primaryStage = primaryStage;

    //primaryStage.initStyle(StageStyle.UNDECORATED);

    // Add the root scene (login)
    primaryStage.setScene(loginScene);
    primaryStage.setResizable(false);
    //primaryStage.show();

    // Handle window closing
    primaryStage.setOnCloseRequest(event -> {
      applicationLogger.info("Stage is closing");
      if (this.ihmCore.getDataForIhm() != null) {
        try {
          this.ihmCore.getDataForIhm().logout();
        } catch (UnsupportedOperationException | IOException ex) {
          ex.printStackTrace();
        }
      }
    });

    primaryStage.show();
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
   * Method which is called from Main to start the HCI.
   *
   * @param args the arguments of the Application from Main method
   */
  public static void run(String[] args) {
    launch(Application.class);
  }

}
