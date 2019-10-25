package controllers;

import core.IhmCore;
import java.io.File;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;

//remplace by javadocs when implementation
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
  private TextField textFieldPassword;

  @FXML
  private TextField textFieldSecretQuestion;

  @FXML
  private DatePicker datePickerBirth;

  @FXML
  private FileChooser avatarFileChooser = new FileChooser();
  private File avatarFile = null;


  private Scene loginScene;


  public void setLoginScene(Scene sceneLogin) {
    loginScene = sceneLogin;
  }
  
  @Override
  public void initialize()  {

  }

  /**
   * Called upon clicking the button to confirm sign up.
   * Retrieves relevant data from the sign up form fields, creates a LocalUser with
   * them and forwards it to data for further process
   * @param event The event inducing the button click
   */
  @FXML
  public void actionSignup(ActionEvent event) {

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

    //LocalUser user = new LocalUser(...);
    /*
    try {
      ihmcore.getDataForIhm().createUser(user);
      //Go to main view

    } catch (SignupException se) {

      Notifications.create()
              .title("Signup failed")
              .text("It seems you entered something wrong. Try again.")
              .darkStyle()
              .showWarning();
    }

   */

  }

  /**
   * Called upon clicking the cancel button from the sign up form.
   * Switches back to the login window.
   * @param event The event inducing the button click
   */
  public void actionCancel(ActionEvent event) {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.setScene(loginScene);
  }

  /**
   * Called upon clicking the button to choose user's avatar.
   * Opens a file dialog to choose avatar file locally.
   * @param event The event inducing the button click
   */
  public void actionAvatarChoice(ActionEvent event) {
    //TODO: add extension filter
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    File chosenFile = avatarFileChooser.showOpenDialog(primaryStage);
    avatarFile = chosenFile;
  }
  
  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
}
