package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

//replace by javadocs
//the view of the music player down in the middle
public class PlayerController implements Controller {

  @FXML
  Label lblArtistMusicName;

  private MainController mainController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public MainController getMainController() {
    return mainController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setPlayerText(String artistName, String musicName) {
    this.lblArtistMusicName.setText(artistName + " - " + musicName);
  }
}
