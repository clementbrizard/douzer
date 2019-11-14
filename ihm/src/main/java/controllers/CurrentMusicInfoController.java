package controllers;

import core.IhmCore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

//replace by javadoc
//right panel with light info and comment about the music
public class CurrentMusicInfoController implements Controller {

  private static final Logger currentMusicInfoLogger = LogManager.getLogger();

  private ShareController shareController;
  private NewCommentController newCommentController;
  private MainController mainController;

  private Scene shareScene;

  private IhmCore ihmCore;

  @FXML
  private Button btnPartager;
  @FXML
  private ProgressBar downloadProgress;


  /**
   * Share the current music. Open the music sharing pop-up.
   * @param event
   */
  @FXML
  private void share(ActionEvent event) {
    currentMusicInfoLogger.debug("Clic sur btn Partager");

    try {
      //get the loader for SignUpView
      FXMLLoader shareLoader = new FXMLLoader(getClass().getResource("/fxml/ShareView.fxml"));
      Parent shareParent = shareLoader.load();
      shareScene = new Scene(shareParent);
    } catch (IOException e) {
      currentMusicInfoLogger.trace(e);
    }
    Stage musicSharingPopup = new Stage();
    musicSharingPopup.setTitle("Partage");
    musicSharingPopup.setScene(this.shareScene);
    // Set position of second window, related to primary window.
    musicSharingPopup.setX(ihmCore.getPrimaryStage().getX() + 200);
    musicSharingPopup.setY(ihmCore.getPrimaryStage().getY() + 100);

    musicSharingPopup.show();
  }

  /**
   * Initialize the controller.
   */
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
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

  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }

  public Scene getShareScene() { return this.shareScene; }

  public void setShareScene(Scene shareScene) { this.shareScene = shareScene; }

}


