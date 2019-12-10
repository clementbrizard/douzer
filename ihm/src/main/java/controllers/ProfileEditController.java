package controllers;

import static impl.org.controlsfx.tools.MathTools.min;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Hashtable;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

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
  private Pane paneImgAvatar;

  @FXML
  private FileChooser avatarFileChooser = new FileChooser();

  private File avatarFile = null;

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

    // thanks to SwingFXUtils we can convert a bufferedImage into an Image
    Image img = SwingFXUtils.toFXImage(convertRenderedImage(imgRend),null);
    imgAvatar.setImage(img);

    // crop of the image to make square
    double minSide = min(img.getHeight(), img.getWidth());
    PixelReader imgReader = img.getPixelReader();

    // if the image isn't square, we crop it
    if (img.getHeight() != img.getWidth()) {
      if (img.getHeight() < img.getWidth()) {
        // define crop in image coordinates:
        double x = (img.getWidth() - img.getHeight()) / 2;
        double y = 0;
        // define the square for cropping
        Rectangle2D croppedPortion = new Rectangle2D(x, y, minSide, minSide);
        imgAvatar.setViewport(croppedPortion);
      } else if (img.getHeight() > img.getWidth()) {
        // define crop in image coordinates:
        double x = 0;
        double y = (img.getHeight() - img.getWidth()) / 2;
        // define the square for cropping
        Rectangle2D croppedPortion = new Rectangle2D(x, y, minSide, minSide);
        imgAvatar.setViewport(croppedPortion);
      }
    }

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

  /**
   * Called upon clicking the button to edit user's avatar.
   * Opens a file dialog to choose avatar file locally.
   *
   * @param event The event inducing the button click
   */
  public void avatarEdition(ActionEvent event) {
    //TODO: add extension filter
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    avatarFile = avatarFileChooser.showOpenDialog(primaryStage);

    final Path avatarPath = avatarFile.toPath();
    // Get the image from the avatar path
    BufferedImage avatarImg = null;
    try {
      avatarImg = ImageIO.read(avatarFile);
      Notifications.create()
              .title("Nouvel avatar")
              .text("Votre avatar a bien été mis à jour.")
              .darkStyle()
              .showInformation();
    } catch (java.io.IOException ex) {
      Notifications.create()
              .title("Fichier non lisible")
              .text("Le fichier d'avatar que vous avez téléchargé n'est pas lisible.")
              .darkStyle()
              .showError();
    }

    ProfileEditController.this.centralFrameController.getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .setAvatar(avatarImg);

    showAvatar();
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

    Notifications.create()
            .title("Sauvegarde effectuée")
            .text("Vos informations de profil ont bien été mises à jour.")
            .darkStyle()
            .showInformation();
  }
}
