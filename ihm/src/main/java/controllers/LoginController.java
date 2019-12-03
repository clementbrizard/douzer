package controllers;

import core.IhmCore;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.security.auth.login.LoginException;

import org.controlsfx.control.Notifications;


/**
 * Controller used for the login view.
 */
public class LoginController implements Controller {

  private IhmCore ihmcore;

  @FXML
  private TextField textFieldPseudo;
  @FXML
  private PasswordField textFieldPassword;

  @Override
  public void initialize() {

  }

  @FXML
  private void actionLogin() {



    // TODO try with the real view, connect to data
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();
    System.out.println("Pseudo:" + userName + ", pass: " + password);
    boolean login = true;

    
    try {
      ihmcore.getDataForIhm().login(userName, password);
      //Go to Main view

    } catch (LoginException le) {

      le.printStackTrace();
      
      login = false;
      
      Notifications.create()
              .title("Connection failed")
              .text("It seems you entered a wrong username/password. Try again.")
              .darkStyle()
              .showWarning();
      
    } catch (IOException ioe) {
      
      login = false;
      ioe.printStackTrace();
    }
    
    if (login) {
      //change for the main view
      System.out.println("login ok");
    }
  }

  @FXML
  private void actionSignup() {
    ihmcore.showSignupScene();
  }

  @FXML
  private void actionForgottenPassword() {
    ihmcore.showForgottenPasswordScene();

  }

  public void setIhmCore(IhmCore ihmcore) {
    this.ihmcore = ihmcore;
  }

}
