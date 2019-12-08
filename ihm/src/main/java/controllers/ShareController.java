package controllers;

import datamodel.LocalMusic;
import datamodel.Music;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Controller of the sharing/unsharing popup window.
 */
public class ShareController implements Controller {
  private static final Logger shareLogger = LogManager.getLogger();

  @FXML
  private Button btnConfirm;

  @FXML
  private Button btnCancel;

  @FXML
  private RadioButton radioPublic;

  @FXML
  private RadioButton radioPrivate;

  @FXML
  private Label labelMusic;

  private ToggleGroup shareStatusGroup;
  private Music currentMusic;

  private ShareController shareController;
  private CurrentMusicInfoController currentMusicInfoController;

  // Getters

  public Label getLabelMusic() {
    return this.labelMusic;
  }

  public ShareController getShareController() {
    return shareController;
  }

  public CurrentMusicInfoController getCurrentMusicInfoController() {
    return currentMusicInfoController;
  }

  // Setters

  public void setLabelMusic(Label labelMusic) {
    this.labelMusic = labelMusic;
  }

  public void setShareController(ShareController shareController) {
    this.shareController = shareController;
  }

  public void setCurrentMusicInfoController(CurrentMusicInfoController currentMusicInfoController) {
    this.currentMusicInfoController = currentMusicInfoController;
  }

  // Other methods

  /**
   * Initialize the Controller.
   */
  @Override
  public void initialize() {
    this.shareStatusGroup = new ToggleGroup();
    this.radioPrivate.setToggleGroup(this.shareStatusGroup);

    // Private => not shared => false
    this.radioPrivate.setUserData(false);
    this.radioPublic.setToggleGroup(this.shareStatusGroup);

    // Public => shared => true
    this.radioPublic.setUserData(true);
  }

  /**
   * Confirm the music sharing or unsharing and close the popup window.
   */
  @FXML
  private void confirm(ActionEvent event) {

    if (!(currentMusic instanceof LocalMusic)) {
      //TODO popup the music is not Local impossible to share then
      return;
    }
    // if radiobutton Public is selected, the music if shared
    try {
      if ((Boolean) shareStatusGroup.getSelectedToggle().getUserData()) {
        currentMusicInfoController
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .shareMusic((LocalMusic) currentMusic);
        shareLogger.debug("Music is shared");
      } else {
        currentMusicInfoController
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .unshareMusic((LocalMusic) currentMusic);
        shareLogger.debug("Music is unshared");
      }
    } catch (Exception e) {
      shareLogger.error(e);
    }
    // closing window
    Stage stage = (Stage) btnConfirm.getScene().getWindow();
    this.currentMusicInfoController.getApplication().getIhmCore().getDataForIhm().notifyMusicUpdate((LocalMusic) this.currentMusic);
    shareLogger.debug("Notify music update");
    stage.close();
  }

  /**
   * Cancel music sharing and close the popup window.
   */
  @FXML
  private void cancel(ActionEvent event) {
    Stage stage = (Stage) btnCancel.getScene().getWindow();
    stage.close();
  }

  /**
   * Initialize displayed data.
   * Initialize current music with current music object from
   * parent controller (currentMusicInfoController).
   * Is used in the parent controller (currentMusicController),
   * when the shareController is declared.
   *
   * @param currentMusic the current music
   */
  public void initializeCurrentMusicInfo(Music currentMusic) {
    this.currentMusic = currentMusicInfoController.getCurrentMusic();
    shareLogger.debug(((LocalMusic) this.currentMusic).isSharedToAll());
    this.labelMusic.setText(currentMusic.getMetadata().getTitle());
    if (currentMusic instanceof LocalMusic) {
      radioPublic.setSelected(((LocalMusic) this.currentMusic).isSharedToAll());
      radioPrivate.setSelected(!((LocalMusic) this.currentMusic).isSharedToAll());
    }
  }
}

