package controllers;

import core.IhmAlert;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.controlsfx.control.Notifications;

import utils.FormatImage;
import utils.FormatJavaFxObjects;

/**
 *  Central view that permit the user to edit his profile.
 */
public class ProfileEditController implements Controller {
  private static final Logger logger = LogManager.getLogger();

  @FXML
  private Label lblUserPseudo;

  @FXML
  private TextField textFieldFirstName;

  @FXML
  private TextField textFieldLastName;

  @FXML
  private DatePicker datePickerBirth;

  @FXML
  private ImageView imgAvatar;

  @FXML
  private Pane paneImgAvatar;

  @FXML
  private FileChooser avatarFileChooser = new FileChooser();

  private File avatarFile;
  
  private DirectoryChooser exportProfilDirectory = new DirectoryChooser();
  
  private File exportDirectory;

  private CentralFrameController centralFrameController;
  private ExportProfileController exportProfileController;
  private PasswordEditController passwordEditController;
  private ProfileDeletionController profileDeletionController;

  // Getters

  public ExportProfileController getExportProfileController() {
    return exportProfileController;
  }

  public PasswordEditController getPasswordEditController() {
    return passwordEditController;
  }

  public ProfileDeletionController getProfileDeletionController() {
    return profileDeletionController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  // Setters

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  public void setExportProfileController(ExportProfileController exportProfileController) {
    this.exportProfileController = exportProfileController;
  }

  public void setPasswordEditController(PasswordEditController passwordEditController) {
    this.passwordEditController = passwordEditController;
  }

  public void setProfileDeletionController(ProfileDeletionController profileDeletionController) {
    this.profileDeletionController = profileDeletionController;
  }

  // Other methods

  @Override
  public void initialize() {
  }  
  
  /**
   * Initialize the fields with default data.
   */
  public void init() {
    
    showAvatar();
    
    String pseudo = this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getUsername();

    lblUserPseudo.setText("Profil de " + pseudo);

    String firstName = this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getFirstName();

    textFieldFirstName.setText(firstName);

    String lastName = this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getLastName();

    textFieldLastName.setText(lastName);

    LocalDate birthDate = this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getDateOfBirth();

    FormatJavaFxObjects.restrictDatePicker(datePickerBirth,
        LocalDate.of(1900, 1, 1), LocalDate.now());
    if (!birthDate.isEqual(LocalDate.MIN)) {
      datePickerBirth.setValue(birthDate);
    }
  }

  /**
   * this function show the avatar image.
   */
  private void showAvatar() {
    // get the image
    RenderedImage imgRend = this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getAvatar();

    // Convert and crop the avatar image
    FormatImage.cropAvatar(imgRend, imgAvatar);

    double circleRadius = 45;
    paneImgAvatar.setMinWidth(circleRadius * 2);
    paneImgAvatar.setMinHeight(circleRadius * 2);
    imgAvatar.setFitHeight(circleRadius * 2);
    imgAvatar.setFitWidth(circleRadius * 2);

    WritableImage croppedImage = imgAvatar.snapshot(null, null);

    // the clip to have a circled image
    Circle clip = new Circle(imgAvatar.getFitHeight() / 2,
            imgAvatar.getFitWidth() / 2,
            circleRadius);
    paneImgAvatar.setClip(clip);
  }

  @FXML
  public void changeFrameToMyMusics(ActionEvent event) {
    ProfileEditController.this.centralFrameController.setCentralContentMyMusics();
  }

  @FXML
  public void changeFrameToAllMusics(ActionEvent event) {
    ProfileEditController.this.centralFrameController.setCentralContentAllMusics();
  }

  /**
   * Called upon clicking the button to edit user's avatar.
   * Opens a file dialog to choose avatar file locally.
   *
   * @param event The event inducing the button click
   */
  public void avatarEdition(ActionEvent event) {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

    // Extension filters for the FileChooser
    FileChooser.ExtensionFilter avatarExtensionFilter =
            new FileChooser.ExtensionFilter(
                    "fichier image",
                    "*.jpg", "*.png", "*.gif", "*.jpeg", "*.bmp");
    avatarFileChooser.getExtensionFilters().add(avatarExtensionFilter);

    avatarFile = avatarFileChooser.showOpenDialog(primaryStage);

    try {
      final Path avatarPath = avatarFile.toPath();

      // Get the image from the avatar path
      BufferedImage avatarImg = null;

      avatarImg = ImageIO.read(avatarFile);

      ProfileEditController.this.centralFrameController.getMainController()
              .getApplication()
              .getIhmCore()
              .getDataForIhm()
              .getCurrentUser()
              .setAvatar(avatarImg);

      showAvatar();

      Notifications.create()
              .title("Nouvel avatar")
              .text("Votre avatar a bien été mis à jour.")
              .darkStyle()
              .showInformation();

    } catch (java.io.IOException | java.lang.NullPointerException e) {
      logger.warn(e + ": aucun fichier avatar sélectioné.");
    }
  }

  /**
   * Saving of the updated user profile parameters.
   * And sending of a confirmation notification
   * @param event a click on the save button
   */
  @FXML
  public void save(ActionEvent event) {
    ProfileEditController.this.centralFrameController.getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .setFirstName(textFieldFirstName.getText());

    ProfileEditController.this.centralFrameController.getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .setLastName(textFieldLastName.getText());

    if (datePickerBirth.getValue() != null) {
      if (datePickerBirth.getValue().isBefore(LocalDate.now())
          && datePickerBirth.getValue()
                            .isAfter(LocalDate.of(1899, 12, 31))) {
        ProfileEditController.this.centralFrameController.getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .setDateOfBirth(datePickerBirth.getValue());
      } else {
        Notifications.create()
            .title("Date de naissance non sauvegardée")
            .text("La date que vous avez saisie est erronée.")
            .darkStyle()
            .showError();
        datePickerBirth.setValue(null);
      }
    } else {
      ProfileEditController.this.centralFrameController.getMainController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .getCurrentUser()
          .setDateOfBirth(LocalDate.MIN);
    }

    this.centralFrameController.getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .notifyUserUpdate(
            this.centralFrameController
                .getMainController()
                .getApplication()
                .getIhmCore()
                .getDataForIhm()
                .getCurrentUser()
      );

    Notifications.create()
            .title("Sauvegarde effectuée")
            .text("Vos informations de profil ont bien été mises à jour.")
            .darkStyle()
            .showInformation();
  }
  
  /**
   * The function who call the windows to choose an import directory.
   * @param evt the event clicked
   */
  @FXML
  public void exportClicked(ActionEvent evt) {
    Stage primaryStage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
    exportDirectory = exportProfilDirectory.showDialog(primaryStage);
    try {
      getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .exportProfile(exportDirectory.getAbsolutePath());
    } catch (UnsupportedOperationException e) {
      LogManager.getLogger().error(e.getMessage());
      IhmAlert.showAlert("implementation","pas encore implémenté","critical");
    } catch (java.lang.RuntimeException e) {
      IhmAlert
        .showAlert("Directory","aucun dossier pour exporter le profile selectionné","critical");
    }
    
  }
}
