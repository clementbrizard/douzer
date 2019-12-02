package controllers;

import com.sun.javafx.logging.Logger;

import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

/**
 * central view show up my musics.
 */
public class MyMusicsController implements Controller {

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
  @FXML
  private TextField tfSearch;
  @FXML
  private TextField tfSearchTitle;
  @FXML
  private TextField tfSearchArtist;
  @FXML
  private TextField tfSearchAlbum;
  @FXML
  private TextField tfSearchDuration;


  private NewMusicController newMusicController;
  private SearchMusicController searchMusicController;

  private CentralFrameController centralFrameController;
  private Scene addMusicScene;

  private Logger logger;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    //logger = LogManager.getLogger();
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

    tfSearchTitle.setVisible(false);
    tfSearchArtist.setVisible(false);
    tfSearchAlbum.setVisible(false);
    tfSearchDuration.setVisible(false);

    try {
      this.displayAvailableMusics();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }
  }


  /**
   * Refreshes the table with getLocalMusics() from DataForIhm.
   */
  public void displayAvailableMusics() {
    tvMusics.getItems().setAll(this.parseMusic());
  }


  private List<MusicMetadata> parseMusic() {
    return this.getCentralFrameController().getMainController().getApplication()
        .getIhmCore().getDataForIhm().getLocalMusics()
        .map(x -> x.getMetadata())
        .collect(Collectors.toList());
  }

  /**
   * the Button who will show the windows to add music.
   */
  @FXML
  public void addMusic() {
    System.out.println("buton");
    try {
      // Initialize shareScene and shareController
      FXMLLoader addMusicLoader = new FXMLLoader(getClass().getResource("/fxml/NewMusicView.fxml"));
      Parent addMusicParent = addMusicLoader.load();
      addMusicScene = new Scene(addMusicParent);
      NewMusicController newMusicController = addMusicLoader.getController();
      this.setNewMusicController(newMusicController);
      newMusicController.setMyMusicsController(this);

    } catch (Exception e) {
      e.printStackTrace();
    }

    Stage musicSharingPopup = new Stage();
    musicSharingPopup.setTitle("Ajout musique");
    musicSharingPopup.setScene(this.addMusicScene);

    // Set position of second window, relatively to primary window.
    //musicSharingPopup.setX(application.getPrimaryStage().getX() + 200);
    //musicSharingPopup.setY(application.getPrimaryStage().getY() + 100);

    // Show sharing popup.
    musicSharingPopup.show();

  }

  @FXML
  public void changeFrameToAllMusics(ActionEvent event) {
    MyMusicsController.this.centralFrameController.setCentralContentAllMusics();
  }


  @FXML
  public void showAdvancedSearch(ActionEvent event) {
    if (tfSearch.isDisabled()) {
      tfSearch.setDisable(false);
      tfSearchTitle.setVisible(false);
      tfSearchArtist.setVisible(false);
      tfSearchAlbum.setVisible(false);
      tfSearchDuration.setVisible(false);

    } else {
      tfSearch.setDisable(true);
      tfSearchTitle.setVisible(true);
      tfSearchArtist.setVisible(true);
      tfSearchAlbum.setVisible(true);
      tfSearchDuration.setVisible(true);

    }
  }


  @FXML
  public void searchMusics(MouseEvent event) {

    SearchQuery query = new SearchQuery();

    if (tfSearch.isVisible()) {
      query.withText(tfSearch.getText());
    } else {
      if (tfSearchTitle.getText() != null) {
        query.withTitle(tfSearchTitle.getText());
      }

      if (tfSearchArtist != null) {
        query.withArtist(tfSearchArtist.getText());
      }

      if (tfSearchAlbum != null) {
        query.withArtist(tfSearchAlbum.getText());
      }

      if (tfSearchDuration != null) {
        query.withArtist(tfSearchDuration.getText());
      }
    }

    Stream<Music> searchResults = MyMusicsController.this.getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm().getMusics(query); //TODO rename

    updateMusics(searchResults);
  }

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(x -> x.getMetadata()).collect(Collectors.toList()));
  }

}