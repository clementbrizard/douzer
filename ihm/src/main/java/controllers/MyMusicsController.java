package controllers;

import datamodel.MusicMetadata;
import java.time.Duration;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

//replace by javadocs
//central view with all user music
public class MyMusicsController implements Controller {

  @FXML
  private TextField tfSearch;
  @FXML
  private TableView<MusicMetadata> tvMusics;
  @FXML
  private TableColumn<MusicMetadata, String> artistCol;
  @FXML
  private TableColumn<MusicMetadata, String> titleCol;
  @FXML
  private TableColumn<MusicMetadata, String> albumCol;
  @FXML
  private TableColumn<MusicMetadata, Duration> durationCol;

  private NewMusicController newMusicController;
  private SearchMusicController searchMusicController;

  private CentralFrameController centralFrameController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  /**
   * Setup the table columns to receive data,
   * this method has to be called right after the creation of the view.
   */
  public void init() {

    // "artist", "title", "album", "duration" refer to MusicMetaData attributes
    this.artistCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("artist"));
    this.titleCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("title"));
    this.albumCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("album"));
    this.durationCol.setCellValueFactory(
        new PropertyValueFactory<MusicMetadata, Duration>("duration")
    );

    //this.displayLocalMusics();
  }

  private void displayLocalMusics() {
    // Show all local musics
    //tvMusics.getItems().setAll();
  }

  public void searchMusic() {
    String searchQuery = tfSearch.getText();

    if (!searchQuery.equals("")) {
      //Filter according to the search query...
      System.out.println("Anyways I started blasting");
    }
  }

  public void shareMusic() {
    //getCurrentlySelectedItem().share();
  }

  /**
   * Called when the user clicks on the delete image.
   * Gets the selected music and deletes it.
   */
  public void deleteLocalMusic() {
    MusicMetadata musicToDelete = getCurrentlySelectedItem();
    if (musicToDelete != null) {
      System.out.println("DANNY DELETO: " + musicToDelete.getTitle());
    } else {
      System.out.println("NOTHING TO DELETE");
    }
  }

  public NewMusicController getNewMusicController() {
    return newMusicController;
  }

  public void setNewMusicController(NewMusicController newMusicController) {
    this.newMusicController = newMusicController;
  }

  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  private MusicMetadata getCurrentlySelectedItem() {
    return tvMusics.getSelectionModel().getSelectedItem();
  }
}
