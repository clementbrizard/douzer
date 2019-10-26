package controllers;

import core.IhmCore;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


/**
 * Controller used for the login view.
 */
public class LoginController implements Controller {

  private IhmCore ihmcore;

  @FXML
  private TextField textFieldPseudo;
  @FXML
  private TextField textFieldPassword;

  @Override
  public void initialize() {

  }

  @FXML
  private void actionLogin() {



    // TODO try with the real view, connect to data
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();
    System.out.println("Pseudo:" + userName + ", pass: " + password);
    /*
    try {
      ihmcore.getDataForIhm().login(userName, password);
      //Go to Main view

    } catch (LoginException le) {

      Notifications.create()
              .title("Connection failed")
              .text("It seems you entered a wrong username/password. Try again.")
              .darkStyle()
              .showWarning();
    }
   */
  }

  @FXML
  private void actionSignup() {
    ihmcore.showSignupScene();

  }

  @FXML
  private void actionForgottenPassword() {
    ihmcore.showForgottenPasswordScene();

  }

  public void setIhmcore(IhmCore ihmcore) {
    this.ihmcore = ihmcore;
  }

}
