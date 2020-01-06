package controllers;

import core.Application;

import core.IhmAlert;
import datamodel.LocalUser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
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
import org.controlsfx.control.Notifications;

/**
 * Controller used for the sign up form.
 */
public class SignUpController implements Controller {

  @FXML
  private TextField textFieldFirstName;

  @FXML
  private TextField textFieldUsername;

  @FXML
  private TextField textFieldLastName;

  @FXML
  private PasswordField textFieldPassword;

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

  // Extension filters for the FileChooser
  private FileChooser.ExtensionFilter avatarExtensionFilter =
      new FileChooser.ExtensionFilter(
          "fichier image",
          "*.jpg", "*.png", "*.gif");
  private File avatarFile = null;
  private File directoryChosenForSavingProfile = null;
  private Application application;

  // Setters

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public void initialize() {
  }

  // Other methods

  /**
   * Called upon clicking the button to confirm sign up.
   * Retrieves relevant data from the sign up form fields, creates a LocalUser with
   * them and forwards it to data for further process
   */
  @FXML
  public void actionSignup() throws InterruptedException {

    final String userName = textFieldUsername.getText();
    final String password = textFieldPassword.getText();
    final String firstName = textFieldFirstName.getText();
    final String lastName = textFieldLastName.getText();
    final LocalDate dateOfBirth = datePickerBirth.getValue();

    BufferedImage avatarImg = null;

    if (textFieldLastName.getText() == null || textFieldLastName.getText().trim().isEmpty()) {
      IhmAlert.showAlert("Nom","Le champ nom ne doit pas être vide","warning");
    }

    if (textFieldFirstName.getText() == null || textFieldFirstName.getText().trim().isEmpty()) {
      IhmAlert.showAlert("Prenom","Le champ prenom ne doit pas être vide","warning");
    }

    if (textFieldUsername.getText() == null || textFieldUsername.getText().trim().isEmpty()) {
      IhmAlert.showAlert("Pseudo","Le champ pseudo ne doit pas être vide","warning");
    }

    if (textFieldPassword.getText().isEmpty()) {
      IhmAlert.showAlert("Mot de passe","Le champ mot de passe ne doit pas être vide","warning");
    }

    if (datePickerBirth.getValue() == null) {
      IhmAlert.showAlert("Date de naissance",
          "Le champ date de naissance ne doit pas être vide",
          "warning");
    }

    if (avatarFile == null) {
      IhmAlert.showAlert("Avatar","Vous devez chosir un avatar","warning");
    }

    if (directoryChosenForSavingProfile == null) {
      IhmAlert.showAlert("Répertoire de sauvegarde du profil",
          "Vous devez choisir un répertoire pour sauvegarder votre profil",
          "warning");
    }

    if (textFieldUsername.getText() != null
        && !textFieldPassword.getText().isEmpty()
        && textFieldFirstName.getText() != null
        && textFieldLastName.getText() != null
        && datePickerBirth.getValue() != null
        && avatarFile != null
    ) {

      final Path avatarPath = avatarFile.toPath();

      try {
        avatarImg = ImageIO.read(avatarFile);
      } catch (IOException e) {
        IhmAlert.showAlert("avatarFile","Avatar bug","critical");
      }

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
      }

    } else {
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
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    avatarFileChooser.getExtensionFilters().add(avatarExtensionFilter);
    avatarFile = avatarFileChooser.showOpenDialog(primaryStage);
    try {
      avatarFilePath.setText(avatarFile.getAbsolutePath());
    } catch (java.lang.RuntimeException e) {
      IhmAlert.showAlert("Avatar","aucun fichier avatar sélectioné","critical");
    }
  }

  /**
   * Called upon clicking the button to choose user's pathDirectory.
   *
   * @param event The event inducing the button click
   */
  public void actionSaveProfileDirChoose(ActionEvent event) {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    directoryChosenForSavingProfile = saveProfileDirectoryChooser.showDialog(primaryStage);
    try {
      profileFilePath.setText(directoryChosenForSavingProfile.getAbsolutePath());
    } catch (java.lang.RuntimeException e) {
      IhmAlert.showAlert("Dir","aucun emplacement de stockage sélectioné","critical");
    }
  }

}
