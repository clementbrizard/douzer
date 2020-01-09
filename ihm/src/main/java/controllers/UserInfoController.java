package controllers;

import java.awt.image.RenderedImage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FormatImage;


/**
 * Controller used for managing the top left view
 * with the nickname and the buttons to modify profile and disconnect.
 */
public class UserInfoController implements Controller {
  private static final Logger logger = LogManager.getLogger();

  @FXML
  private Label userPseudoSideLbl;
  @FXML
  private Pane userProfilePane;
  @FXML
  private Button btnLogout;
  @FXML
  private Button btnParameters;
  @FXML
  private ImageView imgAvatar;
  @FXML
  private Pane paneImgAvatar;

  private MainController mainController;

  // Getters

  public MainController getMainController() {
    return mainController;
  }

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;

  }

  // Other methods

  @Override
  public void initialize() {
  }

  /**
   * Initialize the field with default data.
   */
  public void init() {
    String name = this.mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getCurrentUser()
        .getUsername();

    userPseudoSideLbl.setText(name);

    RenderedImage avatar = this.mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getCurrentUser()
        .getAvatar();

    // Show avatar
    FormatImage.cropAvatar(avatar, imgAvatar);

    double circleRadius = 45;
    paneImgAvatar.setMinWidth(circleRadius * 2);
    paneImgAvatar.setMinHeight(circleRadius * 2);
    imgAvatar.setFitHeight(circleRadius * 2);
    imgAvatar.setFitWidth(circleRadius * 2);

    WritableImage croppedImage = imgAvatar.snapshot(null, null);

    // Add a clip to have a circled avatar image
    Circle clip = new Circle(imgAvatar.getFitHeight() / 2,
        imgAvatar.getFitWidth() / 2,
        circleRadius);
    paneImgAvatar.setClip(clip);

    this.showParameters();
  }

  @FXML
  private void logout(ActionEvent event) {
    try {
      // Refresh views before logout
      this.refreshViews();
      this.mainController.getApplication().getIhmCore().getDataForIhm().logout();
      this.mainController.getApplication().showLoginScene();
      this.mainController.getPlayerController().stopPlayer();
    } catch (IOException e) {
      logger.error(e);
    }
  }

  @FXML
  private void showParameterView(ActionEvent event) {
    UserInfoController.this.mainController.getCentralFrameController()
        .setCentralContentProfileEdit();
  }

  /**
   * Open the ProfileEditView at click on the StackPane.
   */
  private void showParameters() {
    this.userProfilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        UserInfoController.this.mainController.getCentralFrameController()
            .setCentralContentProfileEdit();
      }
    });
  }

  /**
   * Refresh all necessaries views.
   */
  private void refreshViews() {
    // Refresh login view
    this.getMainController().getApplication().getLoginController().refresh();
    // Refresh sign up view
    this.getMainController().getApplication().getSignUpController().refresh();
    // Refresh music info view (it refresh comment music info view)
    this.getMainController().getCurrentMusicInfoController().refresh();
  }
}


