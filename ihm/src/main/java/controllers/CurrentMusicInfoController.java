package controllers;

import core.Application;
import datamodel.LocalMusic;
import datamodel.Music;
import datamodel.MusicMetadata;
import java.io.IOException;
import java.util.stream.Stream;
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
import org.controlsfx.control.Notifications;


/*
 * Right panel controller. Contains information about the current music, sharing button,
 * download progress bar.
 */
public class CurrentMusicInfoController implements Controller {

  private static final Logger currentMusicInfoLogger = LogManager.getLogger();

  @FXML
  private Button btnShare;

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

  public Button getBtnShare() {
    return this.btnShare;
  }

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
    //The share button is invisible unless the selected music is a localMusic (see in Init method)
    btnShare.setVisible(false);

  }

  /**
   * Initialize the current music info view with a selected music.
   *
   * @param music selected music
   */
  public void init(Music music) {
    this.currentMusic = music;

    // The user can only see his/her own musics (that are localMusics)
    if (music instanceof LocalMusic) {
      btnShare.setVisible(true);
    }

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
    if (this.currentMusic == null || !(this.currentMusic instanceof LocalMusic)) {
      // TODO: do not display the button
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
      shareController.initializeCurrentMusicInfo((LocalMusic) this.currentMusic);

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

  /**
   * Update the download progress bar value.
   *
   * @param progress dowload progress, between 0 and 100
   * @param music    The music that is downloaded
   */
  public void updateDownloadProgressBar(Music music, int progress) {
    downloadProgress.setProgress(progress / 100);
    // We update central views when download is done
    if (progress == 100) {
      Notifications.create()
          .title("Download done")
          .text(music.getMetadata().getTitle() + " - " + music.getMetadata().getArtist())
          .darkStyle()
          .showWarning();
      this.application
          .getMainController()
          .getCentralFrameController()
          .getMyMusicsController()
          .init();
      this.application
          .getMainController()
          .getCentralFrameController()
          .getAllMusicsController()
          .init();
      // Reinitialize progress bar with 0
      downloadProgress.setProgress(0);
    }
  }

}


