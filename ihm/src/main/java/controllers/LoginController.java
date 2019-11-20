package controllers;

import core.Application;
import core.IhmCore;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller used for the login view.
 */
public class LoginController implements Controller {

  private static final Logger loginLogger = LogManager.getLogger();

  @FXML
  private TextField textFieldPseudo;

  @FXML
  private PasswordField textFieldPassword;

  private Application application;

  // Other methods

  @Override
  public void initialize() {}

  @FXML
  private void actionLogin() {
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();
    boolean login = true;
    application.showMainScene();
    application.getMainController().init();

    loginLogger.info("User {} logged in", userName);

    //TODO uncomment when Data team fix login method
    /*
    try {
      ihmcore.getDataForIhm().login(userName, password);
      ihmcore.showMainScene();

    } catch (LoginException le) {

      le.printStackTrace();
      
      Notifications.create()
              .title("Connection failed")
              .text("It seems you entered a wrong username/password. Try again.")
              .darkStyle()
              .showWarning();
      
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }*/
  }

  @FXML
  private void actionSignUp() {
    application.showSignUpScene();
  }

  @FXML
  private void actionForgottenPassword() {
    application.showForgottenPasswordScene();
  }

  public void setApplication(Application application) {
    this.application = application;
  }

}
