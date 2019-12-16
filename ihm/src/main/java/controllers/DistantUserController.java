package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;

public class DistantUserController implements Controller {
  /* Logger */
  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

  @FXML
  private ImageView imgAvatar;

  @FXML
  private Pane paneImgAvatar;

  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  /* Getters */

  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  /* Setters */

  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  /* Initialisation methods */

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  /**
   * Initialize the fields with default data.
   */
  public void init() {

    // Show avatar
    // TODO Get the real avatar instead of showing the default avatar image
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

  /* FXML methods (to handle events from user) */

  @FXML
  public  void manageFriendship(ActionEvent event) {
    logger.debug("click sur le bouton");
  }

  @FXML
  public void changeFrameToMyMusics(ActionEvent event) {
    this.centralFrameController.setCentralContentMyMusics();
  }

  @FXML
  public void changeFrameToAllMusics(ActionEvent event) {
    this.centralFrameController.setCentralContentAllMusics();
  }

  /* Logic methods */

}
