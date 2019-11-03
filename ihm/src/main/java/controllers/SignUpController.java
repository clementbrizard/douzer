package controllers;

import core.IhmCore;
import datamodel.LocalUser;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.security.auth.login.LoginException;
import org.controlsfx.control.Notifications;

/**
 * Controller used for the sign up form.
 */
public class SignUpController implements Controller {
  
  private IhmCore ihmCore;

  @FXML
  private TextField textFieldFirstName;

  @FXML
  private TextField textFieldUsername;

  @FXML
  private TextField textFieldLastName;

  @FXML
  private TextField textFieldSecretAnswer;

  @FXML
  private PasswordField textFieldPassword;

  @FXML
  private TextField textFieldSecretQuestion;

  @FXML
  private DatePicker datePickerBirth;

  @FXML
  private FileChooser avatarFileChooser = new FileChooser();
  private File avatarFile = null;
  
  @Override
  public void initialize()  {

  }

  /**
   * Called upon clicking the button to confirm sign up.
   * Retrieves relevant data from the sign up form fields, creates a LocalUser with
   * them and forwards it to data for further process
   */
  @FXML
  public void actionSignup() {

    //TODO: connect to data + sanity checks
    String userName = textFieldUsername.getText();
    String password = textFieldPassword.getText();
    String name = textFieldFirstName.getText();
    String surname = textFieldLastName.getText();

    java.sql.Date birthdateAsJavaSqlDate = java.sql.Date.valueOf(datePickerBirth.getValue());
    //Avatar ???
    String secretQuestion = textFieldSecretQuestion.getText();
    String secretAnswer = textFieldSecretAnswer.getText();

    System.out.println("Signing up as user " + userName);

    LocalUser user = new LocalUser();
    user.setPassword(password);

    try {
      System.out.println(ihmCore.getDataForIhm());
      ihmCore.getDataForIhm().createUser(user);
      //Go to main view

    } catch (IOException | LoginException se) {

      Notifications.create()
              .title("Signup failed")
              .text("It seems you entered something wrong. Try again.")
              .darkStyle()
              .showWarning();
    }

  }

  /**
   * Called upon clicking the cancel button from the sign up form.
   * Switches back to the login window.
   */
  public void actionCancel() {
    ihmCore.showLoginScene();
  }

  /**
   * Called upon clicking the button to choose user's avatar.
   * Opens a file dialog to choose avatar file locally.
   * @param event The event inducing the button click
   */
  public void actionAvatarChoice(ActionEvent event) {
    //TODO: add extension filter
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    avatarFile = avatarFileChooser.showOpenDialog(primaryStage);
  }
  
  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
}
