package controllers;

import core.Application;
import core.IhmCore;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.security.auth.login.LoginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

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

    loginLogger.info("User {} logged in", userName);
    try {
      this.application.getIhmCore().getDataForIhm().login(userName, password);
      application.getMainController().init();
      this.application.showMainScene();

    } catch (LoginException le) {

      le.printStackTrace();
      
      Notifications.create()
              .title("Connection failed")
              .text("It seems you entered a wrong username/password. Try again.")
              .darkStyle()
              .showWarning();
      
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @FXML
  private void actionSignup() {
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
