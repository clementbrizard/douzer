package controllers;

import core.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller used for the forgotten password form.
 */
public class ForgottenPasswordController implements Controller {

  @FXML
  private TextField textFieldUsername;

  @FXML
  private TextField textFieldSecretAnswer;

  @FXML
  private TextField textFieldNewPassword;

  @FXML
  private TextField textFieldNewPassword2;

  @FXML
  private Label labelSecretQuestion;

  private Application application;

  // Setters

  public void setApplication(Application application) {
    this.application = application;
  }

  // Other methods

  @Override
  public void initialize() {
    textFieldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
      //Check if username exists, if yes show the secret question
      //if(newValue in localUsersList....)
      //  labelSecretQuestion.setText(...);
    });
  }

  /**
   * Called upon clicking the button to confirm password renewal.
   * Checks if secret answer matches secret question for specified user.
   */
  @FXML
  public void actionPasswordChange() {

    //TODO: connect to data + sanity checks
    String userName = textFieldUsername.getText();
    String newPassword = textFieldNewPassword.getText();
    String newPassword2 = textFieldNewPassword2.getText();

    String secretAnswer = textFieldSecretAnswer.getText();

    //if(newPassword.equals(newPassword2) &&
    // secretAnswer.equals(localUsersList.get(userName).getSecretAnswer()))
    //...
    // back to login or straight to main ?
    //else
    //Notifications.create()
    //              .title("Password change failed")
    //              .text("You entered a wrong username or your passwords do not match. Try again.")
    //              .darkStyle()
    //              .showWarning();
  }

  /**
   * Called upon clicking the cancel button from the forgotten password form.
   * Switches back to the login window.
   */
  public void actionCancel() {
    application.showLoginScene();
  }

}
