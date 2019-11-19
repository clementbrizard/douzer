package controllers;

import datamodel.LocalMusic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//replace by javadocs
//popup view when click on share Button on shareController
public class ShareController implements Controller {

  private static final Logger shareLogger = LogManager.getLogger();

  private ShareController shareController;

  private CurrentMusicInfoController currentMusicInfoController;

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

  private LocalMusic currentMusic;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    // TODO Initialize labelMusic with the current music name
    //  (the current music is given as a parameter at the popup window opening)
    // currentMusic initialization with the current music form the parent window.
    //this.currentMusic = currentMusicInfoController.getCurrentMusic();
    //this.labelMusic.setText(currentMusicInfoController.getCurrentMusic()
    // .getMetadata().getTitle());

    this.shareStatusGroup = new ToggleGroup();
    this.radioPrivate.setToggleGroup(this.shareStatusGroup);
    // Private => not shared => false
    this.radioPrivate.setUserData(false);
    this.radioPublic.setToggleGroup(this.shareStatusGroup);
    // Public => shared => true
    this.radioPublic.setUserData(true);
    //this.radioPublic.setSelected(this.currentMusic.isShared());
  }

  /**
   * Confirm the music sharing or unsharing and close the popup window.
   */
  @FXML
  private void confirm(ActionEvent event) {
    // if radiobutton Public is selected, the music if shared
    try {
      if ((Boolean) shareStatusGroup.getSelectedToggle().getUserData()) {
        currentMusicInfoController.getIhmCore().getDataForIhm().shareMusic(currentMusic);
      } else {
        currentMusicInfoController.getIhmCore().getDataForIhm().unshareMusic(currentMusic);
      }
    } catch (Exception e) {
      shareLogger.error(e);
    }
    // closing window
    Stage stage = (Stage) btnConfirm.getScene().getWindow();
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
   * Initialize diplayed data.
   * Ittialize current music with current music object from
   * parent controller (currentMusicInfoController).
   * Is used in the parent controller (currentMusicController),
   * when the shareController is declared.
   *
   * @param currentMusic the current music
   */
  public void initializeCurrentMusicInfo(LocalMusic currentMusic) {
    this.currentMusic = currentMusicInfoController.getCurrentMusic();
    this.labelMusic.setText(currentMusic.getMetadata().getTitle());
    this.radioPublic.setSelected(this.currentMusic.isShared());

  }

  public ShareController getShareController() {
    return shareController;
  }

  public void setShareController(ShareController shareController) {
    this.shareController = shareController;
  }

  public CurrentMusicInfoController getCurrentMusicInfoController() {
    return currentMusicInfoController;
  }

  public void setCurrentMusicInfoController(CurrentMusicInfoController currentMusicInfoController) {
    this.currentMusicInfoController = currentMusicInfoController;
  }

  public Label getLabelMusic() {
    return this.labelMusic;
  }

  public void setLabelMusic(Label labelMusic) {
    this.labelMusic = labelMusic;
  }
}
