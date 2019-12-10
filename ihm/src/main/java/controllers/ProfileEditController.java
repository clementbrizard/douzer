package controllers;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.time.LocalDate;
import java.util.Hashtable;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

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
  private Label lblSaved;

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
    
    //get the image
    RenderedImage imgRend = this.centralFrameController
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getCurrentUser()
        .getAvatar();

    //thanks to SwingFXUtils we can convert a bufferedImage into an Image
    Image img = SwingFXUtils.toFXImage(convertRenderedImage(imgRend),null); 
    imgAvatar.setImage(img);
    
    //the clip to have a circled image
    Circle clip = new Circle(imgAvatar.getFitHeight() / 2,imgAvatar.getFitWidth() / 2,35);
    imgAvatar.setClip(clip);
    
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

    datePickerBirth.setPromptText(birthDate.toString());
    datePickerBirth.setValue(birthDate);
  }
  
  /**
   * this function permit to convert a RenderedImage into a BufferedImage
   * I took this function here http://www.jguru.com/faq/view.jsp?EID=114602.
   * @param img a RenderedImage
   * @return a BufferedImage
   */
  private BufferedImage convertRenderedImage(RenderedImage img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage)img;  
    }   
    ColorModel cm = img.getColorModel();
    int width = img.getWidth();
    int height = img.getHeight();
    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    Hashtable properties = new Hashtable();
    String[] keys = img.getPropertyNames();
    if (keys != null) {
      for (int i = 0; i < keys.length; i++) {
        properties.put(keys[i], img.getProperty(keys[i]));
      }
    }
    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
    img.copyData(raster);
    return result;
  }

  @FXML
  public void changeFrameToMyMusics(ActionEvent event) {
    ProfileEditController.this.centralFrameController.setCentralContentMyMusics();
  }

  @FXML
  public void avatarEdition(ActionEvent event) {

  }

  @FXML
  public void avatarDeletion(ActionEvent event) {
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

    ProfileEditController.this.centralFrameController.getMainController()
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
            .getDateOfBirth()
            .toString());

    Notifications.create()
            .title("Sauvegarde effectuée")
            .text("Vos informations de profil ont bien été mises à jour.")
            .darkStyle()
            .showInformation();
  }
}
