package controllers;

import core.Application;
import core.IhmCore;

import java.io.IOException;

import javafx.application.Platform;
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

  private static final Logger logger = LogManager.getLogger();
  private IhmCore ihmcore;

  @FXML
  private TextField textFieldPseudo;
  @FXML
  private PasswordField textFieldPassword;
  private Application application;

  @Override
  public void initialize() {

  }

  @FXML
  private void actionLogin() {
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();
    boolean login = true;
    application.showMainScene();
    application.getMainController().init();

    logger.info("User {} logged in", userName);

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
  private void actionSignup() {
    application.showSignupScene();
  }

  @FXML
  private void actionForgottenPassword() {
    application.showForgottenPasswordScene();

  }

  public void setApplication(Application application) {
    this.application = application;
  }

}
