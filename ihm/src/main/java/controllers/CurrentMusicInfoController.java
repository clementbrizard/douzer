package controllers;

import core.Application;
import datamodel.LocalMusic;
import datamodel.Music;
import datamodel.MusicMetadata;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/*
 * Right panel controller. Contains informations about the current music, sharing button,
 * download progress bar
 */
public class CurrentMusicInfoController implements Controller {

  private static final Logger currentMusicInfoLogger = LogManager.getLogger();

  @FXML
  private Button buttonShare;

  @FXML
  private ProgressBar downloadProgress;
  
  @FXML
  private CommentsController commentCurrentMusicController;

  private ShareController shareController;
  private NewCommentController newCommentController;
  private MainController mainController;

  private Scene shareScene;

  private Application application;
  private Music currentMusic;
  
  // Getters
  public ShareController getShareController() {
    return shareController;
  }

  public CommentsController getCommentCurrentMusicController() {
    return commentCurrentMusicController;
  }
  
  public NewCommentController getNewCommentController() {
    return newCommentController;
  }

  public MainController getMainController() {
    return mainController;
  }

  public Application getApplication() {
    return application;
  }

  /**
   * Initialize the controller.
   */
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public void init(Music music) {
    // TODO link currentMusicInfoController with MainController instead of IhmCore
    this.currentMusic = music;

    commentCurrentMusicController.setCurrentMusicController(this);
    commentCurrentMusicController.init(music);
    
  }

  public Music getCurrentMusic() {
    return this.currentMusic;
  }

  // Setters
  public void setShareController(ShareController shareController) {
    this.shareController = shareController;
  }

  public void setNewCommentController(NewCommentController newCommentController) {
    this.newCommentController = newCommentController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public Scene getShareScene() {
    return this.shareScene;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public void setShareScene(Scene shareScene) {
    this.shareScene = shareScene;
  }

  public void setCurrentMusic(LocalMusic currentMusic) {
    this.currentMusic = currentMusic;
  }

  // Other methods

  /**
   * Share the current music. Open the music sharing pop-up.
   *
   * @param event nut used
   */
  @FXML
  private void share(ActionEvent event) {
    if (this.currentMusic == null) {
      return;
    }
    try {
      // Initialize shareScene and shareController
      FXMLLoader shareLoader = new FXMLLoader(getClass().getResource("/fxml/ShareView.fxml"));
      Parent shareParent = shareLoader.load();
      shareScene = new Scene(shareParent);
      ShareController shareController = shareLoader.getController();
      this.setShareController(shareController);
      shareController.setCurrentMusicInfoController(this);
      shareController.initializeCurrentMusicInfo(this.currentMusic);

    } catch (IOException e) {
      currentMusicInfoLogger.error(e);
    }

    Stage musicSharingPopup = new Stage();
    musicSharingPopup.setTitle("Partage");
    musicSharingPopup.setScene(this.shareScene);

    // Set position of second window, relatively to primary window.
    musicSharingPopup.setX(application.getPrimaryStage().getX() + 200);
    musicSharingPopup.setY(application.getPrimaryStage().getY() + 100);

    // Show sharing popup.
    musicSharingPopup.show();
  }

}


