package controllers;

import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * central view show up my musics.
 */
public class MyMusicsController implements Controller {

  @FXML
  private Button btnAdvancedSearchMm;
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
  private TextField tfSearchMm;
  @FXML
  private TextField tfSearchTitleMm;
  @FXML
  private TextField tfSearchArtistMm;
  @FXML
  private TextField tfSearchAlbumMm;
  @FXML
  private TextField tfSearchDurationMm;
  @FXML
  private ImageView ivSearchAllMm;

  private NewMusicController newMusicController;
  private SearchMusicController searchMusicController;

  private CentralFrameController centralFrameController;

  //Getters

  /**
   * getter of newMusicController.
   *
   * @return a NewMusicController
   * @see NewMusicController
   */
  public NewMusicController getNewMusicController() {
    return newMusicController;
  }

  /**
   * getter of searchMusicController.
   *
   * @return a SearchMusicController
   * @see SearchMusicController
   */
  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  /**
   * getter of centralFrameController.
   *
   * @return a CentralFrameController
   * @see CentralFrameController
   */
  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  //Setters

  /**
   * setter of NewMusicController.
   *
   * @param newMusicController the new newMusicController
   * @see NewMusicController
   */
  public void setNewMusicController(NewMusicController newMusicController) {
    this.newMusicController = newMusicController;
  }

  /**
   * setter of searchMusicController.
   *
   * @param searchMusicController the new SearchMusicController
   * @see SearchMusicController
   */
  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  /**
   * setter of centralFrameController.
   *
   * @param centralFrameController the new CentralFrameController
   * @see CentralFrameController
   */
  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  // Other methods

  /**
   * the initialize method of the Controller call by Javafx when we create the view of AllMusics.
   */
  @Override
  public void initialize() {

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

    this.tfSearchTitleMm.setVisible(false);
    this.tfSearchArtistMm.setVisible(false);
    this.tfSearchAlbumMm.setVisible(false);
    this.tfSearchDurationMm.setVisible(false);

    this.btnAdvancedSearchMm.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (tfSearchTitleMm.isVisible()) {
          tfSearchTitleMm.setVisible(false);
        } else {
          tfSearchTitleMm.setVisible(true);
        }

        if (tfSearchArtistMm.isVisible()) {
          tfSearchArtistMm.setVisible(false);
        } else {
          tfSearchArtistMm.setVisible(true);
        }

        if (tfSearchAlbumMm.isVisible()) {
          tfSearchAlbumMm.setVisible(false);
        } else {
          tfSearchAlbumMm.setVisible(true);
        }

        if (tfSearchDurationMm.isVisible()) {
          tfSearchDurationMm.setVisible(false);
        } else {
          tfSearchDurationMm.setVisible(true);
        }

        if (tfSearchMm.isDisable()) {
          tfSearchMm.setDisable(false);
        } else {
          tfSearchMm.setDisable(true);
        }
      }
    });

    this.ivSearchAllMm.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {

        SearchQuery query = new SearchQuery();

        if (tfSearchMm.isVisible()) {
          query.withText(tfSearchMm.getText());
        } else {
          if (tfSearchTitleMm.getText() != null) {
            query.withTitle(tfSearchTitleMm.getText());
          }

          if (tfSearchArtistMm != null) {
            query.withArtist(tfSearchArtistMm.getText());
          }

          if (tfSearchAlbumMm != null) {
            query.withArtist(tfSearchAlbumMm.getText());
          }

          if (tfSearchDurationMm != null) {
            query.withArtist(tfSearchDurationMm.getText());
          }
        }

        Stream<Music> searchResults = MyMusicsController.this.getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm().getMusics(query); //TODO rename

        updateMusics(searchResults);
      }
    });

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

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(x -> x.getMetadata()).collect(Collectors.toList()));
  }

}