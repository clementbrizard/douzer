package controllers;

import core.Application;

import datamodel.LocalUser;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.filechooser.FileSystemView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;
import utils.FormatJavaFxObjects;

/**
 * Controller used for the sign up form.
 */
public class SignUpController implements Controller {
  /* logger */
  private static final Logger logger = LogManager.getLogger();

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

  @FXML
  private Button signUpBtn;

  // Extension filters for the FileChooser
  private FileChooser.ExtensionFilter avatarExtensionFilter =
      new FileChooser.ExtensionFilter(
          "fichier image",
          "*.jpg", "*.png", "*.gif","*.jpeg", "*.bmp");
  private File avatarFile = null;
  private File directoryChosenForSavingProfile = null;
  private Application application;

  private Path defaultProfileFilePath = FileSystemView
      .getFileSystemView()
      .getHomeDirectory()
      .toPath();

  /* Getters */

  public Button getSignUpButton() {
    return this.signUpBtn;
  }

  /* Setters */

  public void setApplication(Application application) {
    this.application = application;
  }

  @Override
  public void initialize() {
    FormatJavaFxObjects.restrictDatePicker(datePickerBirth, LocalDate.of(1900, 1, 1), LocalDate.now());
  }

  /* Other methods */

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

    LocalDate dateOfBirth = datePickerBirth.getValue();
    BufferedImage avatarImg = null;
    Path profileSavePath;

    // Gestion de la date par défaut
    if (datePickerBirth.getValue() == null) {
      logger.warn("Date de naissance non renseignée.");
      dateOfBirth = LocalDate.MIN;
    }

    // Gestion de l'avatar par défaut
    if (avatarFile == null) {
      try {
        avatarImg = ImageIO.read(this.getClass().getResource("/images/defaultAvatar.png"));
        logger.warn("Utilisation de l'avatar par défaut.");
      } catch (IOException se) {
        logger.fatal("Avatar par défaut compromis.");
      }
    } else {
      final Path avatarPath = avatarFile.toPath();

      try {
        avatarImg = ImageIO.read(avatarFile);
      } catch (IOException e) {
        logger.warn(e + ": erreur de lecture du fichier avatar sélectioné.");
        logger.warn("Utilisation de l'avatar par défaut.");
      }
    }

    // Gestion de l'emplacement de profil par défaut
    if (directoryChosenForSavingProfile == null) {
      profileSavePath = defaultProfileFilePath;
      logger.warn(
          "Utilisation de l'emplacement de profil par défaut : {}",
          profileSavePath.toAbsolutePath().toString());
    } else {
      profileSavePath = directoryChosenForSavingProfile.toPath();
    }

    if (textFieldUsername.getText() != null && !textFieldPassword.getText().isEmpty()) {

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
      if (textFieldUsername.getText() == null || textFieldUsername.getText().trim().isEmpty()) {
        Notifications.create()
            .title("Inscription échouée")
            .text("Aucun pseudo n'a été renseigné.")
            .darkStyle()
            .showWarning();
      }

      if (textFieldPassword.getText().isEmpty()) {
        Notifications.create()
            .title("Inscription échouée")
            .text("Aucun mot de passe n'a été renseigné.")
            .darkStyle()
            .showWarning();
      }
    }
  }

  /**
   * Called upon clicking the cancel button from the sign up form.
   * Switches back to the login window.
   */
  public void actionSignIn() {
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
      logger.warn(e + ": aucun fichier avatar sélectioné.");
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
      logger.warn(e + ": aucun emplacement de sauvegarde sélectioné.");
      profileFilePath.setText(defaultProfileFilePath.toAbsolutePath().toString());
    }
  }


}
