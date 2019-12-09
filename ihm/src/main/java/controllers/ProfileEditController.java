package controllers;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

//replace by javadocs
//central view that permit the user to edit his profile
public class ProfileEditController implements Controller {

  @FXML
  private Label lblUserPseudo;

  @FXML
  private TextField textFieldFirstName;

  @FXML
  private TextField textFieldLastName;

  @FXML
  private DatePicker datePickerBirth;

  /*@FXML
  private ImageView imgAvatar;*/

  @FXML
  private Label lblSaved;

  private CentralFrameController centralFrameController;
  private ExportProfileController exportProfileController;
  private PasswordEditController passwordEditController;
  private ProfileDeletionController profileDeletionController;
  
  private CentralFrameController centralFrameController;
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
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

    String birthDate = this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getDateOfBirth()
            .toString();

    datePickerBirth.setPromptText(birthDate);
    /*datePickerBirth.setValue(datePickerBirth.getConverter().fromString(birthDate));*/
  }

  @FXML
  public void changeFrameToMyMusics(ActionEvent event) {
    ProfileEditController.this.centralFrameController.setCentralContentMyMusics();
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

    /*ProfileEditController.this.centralFrameController.getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .setDateOfBirth(datePickerBirth.getValue());

    logger.debug("Dob to set : " + datePickerBirth.getValue().toString());

    logger.debug("getDob : " +     ProfileEditController.this.centralFrameController
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getDateOfBirth();*/

    Notifications.create()
            .title("Sauvegarde effectuée")
            .text("Vos informations de profil ont bien été mises à jour.")
            .darkStyle()
            .showInformation();
  }
}
