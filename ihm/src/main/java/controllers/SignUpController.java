package controllers;

import core.Application;
import datamodel.LocalUser;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

/**
 * Controller used for the sign up form.
 */
public class SignUpController implements Controller {
  private static final Logger signUpLogger = LogManager.getLogger();

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

  @FXML
  private TextField avatarFilePath;

  @FXML
  private DirectoryChooser saveProfileDirectoryChooser = new DirectoryChooser();

  @FXML
  private TextField profileFilePath;

  private File avatarFile = null;
  private File directoryChosenForSavingProfile = null;
  private Application application;

  // Setters

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public void initialize() {}

  // Other methods

  /**
   * Called upon clicking the button to confirm sign up.
   * Retrieves relevant data from the sign up form fields, creates a LocalUser with
   * them and forwards it to data for further process
   */
  @FXML
  public void actionSignup() {

    //TODO: sanity checks
    final String userName = textFieldUsername.getText();
    final String password = textFieldPassword.getText();
    final String firstName = textFieldFirstName.getText();
    final String lastName = textFieldLastName.getText();

    final LocalDate dateOfBirth = datePickerBirth.getValue();
    final Path avatarPath = avatarFile.toPath();
    // Get the image from the avatar path
    BufferedImage avatarImg = null;
    try {
      avatarImg = ImageIO.read(avatarFile);
    } catch (java.io.IOException ex) {
      // Image could not be loaded
      // log it ?
    }

    final String secretQuestion = textFieldSecretQuestion.getText();
    final String secretAnswer = textFieldSecretAnswer.getText();

    signUpLogger.info("Signing up as user {}.", userName);

    final Path profileSavePath = directoryChosenForSavingProfile.toPath();

    LocalUser user = new LocalUser();

    user.setPassword(password);

    user.setUsername(userName);
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setDateOfBirth(dateOfBirth);
    user.setSavePath(profileSavePath);
    user.setAvatar(avatarImg);

    try {
      application.getIhmCore().getDataForIhm().createUser(user);
      application.showMainScene();
      application.getMainController().init();


    } catch (IOException | LoginException se) {

      se.printStackTrace();

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
    application.showLoginScene();
  }

  /**
   * Called upon clicking the button to choose user's avatar.
   * Opens a file dialog to choose avatar file locally.
   *
   * @param event The event inducing the button click
   */
  public void actionAvatarChoice(ActionEvent event) {
    //TODO: add extension filter
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    avatarFile = avatarFileChooser.showOpenDialog(primaryStage);
    avatarFilePath.setText(avatarFile.getAbsolutePath());

  }

  public void actionSaveProfileDirChoose(ActionEvent event) {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    directoryChosenForSavingProfile = saveProfileDirectoryChooser.showDialog(primaryStage);
    profileFilePath.setText(directoryChosenForSavingProfile.getAbsolutePath());
  }

}
