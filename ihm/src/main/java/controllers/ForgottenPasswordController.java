package controllers;

import core.IhmCore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//remplace by javadocs when implementation
public class ForgottenPasswordController implements Controller {

  private IhmCore ihmCore;

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


  private Scene loginScene;


  public void setLoginScene(Scene sceneLogin) {
    loginScene = sceneLogin;
  }

  @Override
  public void initialize()  {
    textFieldUsername.textProperty().addListener((observable, oldValue, newValue) -> {
      //Check if username exists, if yes show the secret question
      //if(newValue in localUsersList....)
      //  labelSecretQuestion.setText(...);
    });
  }

  /**
   * Called upon clicking the button to confirm password renewal.
   * Checks if secret answer matches secret question for specified user.
   * @param event The event inducing the button click
   */
  @FXML
  public void actionPasswordChange(ActionEvent event) {

    //TODO: connect to data + sanity checks
    String userName = textFieldUsername.getText();
    String newPassword = textFieldNewPassword.getText();
    String newPassword2 = textFieldNewPassword2.getText();

    String secretAnswer = textFieldSecretAnswer.getText();

    //if(newPassword.equals(newPassword2) &&
    // secretAnswer.equals(localUsersList.get(userName).getSecretAnswer()))
    //...
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
   * @param event The event inducing the button click
   */
  public void actionCancel(ActionEvent event) {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.setScene(loginScene);
  }


  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
}
