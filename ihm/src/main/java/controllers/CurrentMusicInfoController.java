package controllers;

import datamodel.LocalMusic;
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


//replace by javadoc
//right panel with light info and comment about the music
public class CurrentMusicInfoController implements Controller {

  private static final Logger currentMusicInfoLogger = LogManager.getLogger();

  private ShareController shareController;
  private NewCommentController newCommentController;
  private MainController mainController;

  private Scene shareScene;

  @FXML
  private Button btnPartager;
  @FXML
  private ProgressBar downloadProgress;

  private LocalMusic currentMusic;


  /**
   * Share the current music. Open the music sharing pop-up.
   *
   * @param event nut used
   */
  @FXML
  private void share(ActionEvent event) {
    try {
      //get the loader for SignUpView
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
    // Set position of second window, related to primary window.
    musicSharingPopup.setX(mainController.getIhmCore().getPrimaryStage().getX() + 200);
    musicSharingPopup.setY(mainController.getIhmCore().getPrimaryStage().getY() + 100);

    musicSharingPopup.show();
  }

  /**
   * Initialize the controller.
   */
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public void init() {
    // TODO Initialize currentMusic with the current music
    // TODO link currentMusicInfoController with MainController instead of IhmCore
    this.currentMusic = new LocalMusic(new MusicMetadata(), "pathTest.mp3");
    this.currentMusic.setShared(true);
    this.currentMusic.getMetadata().setTitle("Ceci est un test");
  }

  public ShareController getShareController() {
    return shareController;
  }

  public void setShareController(ShareController shareController) {
    this.shareController = shareController;
  }

  public NewCommentController getNewCommentController() {
    return newCommentController;
  }

  public void setNewCommentController(NewCommentController newCommentController) {
    this.newCommentController = newCommentController;
  }

  public MainController getMainController() {
    return mainController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public Scene getShareScene() {
    return this.shareScene;
  }

  public void setShareScene(Scene shareScene) {
    this.shareScene = shareScene;
  }

  public LocalMusic getCurrentMusic() {
    return this.currentMusic;
  }

  public void setCurrentMusic(LocalMusic currentMusic) {
    this.currentMusic = currentMusic;
  }

}


