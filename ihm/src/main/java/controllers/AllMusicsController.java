package controllers;

import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.Music;
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

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * central view show up all music in the network.
 */
public class AllMusicsController implements Controller {

  @FXML
  private Button btnAdvancedSearch;
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
  @FXML
  private ImageView ivSearchAll;

  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  // Getters

  /**
   * getter of searchMusicController.
   * @return a SearchMusicController
   * @see SearchMusicController
   */
  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  /**
   * getter of centralFrameController.
   * @return a CentralFrameController
   * @see CentralFrameController
   */
  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  // Setters

  /**
   * setter of searchMusicController.
   * @param searchMusicController the new SearchMusicController
   * @see SearchMusicController
   */
  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  /**
   * setter of centralFrameController.
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

    this.tfSearchTitle.setVisible(false);
    this.tfSearchArtist.setVisible(false);
    this.tfSearchAlbum.setVisible(false);
    this.tfSearchDuration.setVisible(false);

    this.btnAdvancedSearch.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
          if (tfSearchTitle.isVisible()) {
            tfSearchTitle.setVisible(false);
          } else {
            tfSearchTitle.setVisible(true);
          }

        if (tfSearchArtist.isVisible()) {
          tfSearchArtist.setVisible(false);
        } else {
          tfSearchArtist.setVisible(true);
        }

        if (tfSearchAlbum.isVisible()) {
          tfSearchAlbum.setVisible(false);
        } else {
          tfSearchAlbum.setVisible(true);
        }

        if (tfSearchDuration.isVisible()) {
          tfSearchDuration.setVisible(false);
        } else {
          tfSearchDuration.setVisible(true);
        }

        if (tfSearch.isVisible()) {
          tfSearch.setVisible(false);
        } else {
          tfSearch.setVisible(true);
        }
      }
    });

    this.ivSearchAll.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {

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

        Stream<Music> searchResults = AllMusicsController.this.getCentralFrameController()
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

  public void displayAvailableMusics() {
    tvMusics.getItems().setAll(this.parseMusic());
  }

  private List<MusicMetadata> parseMusic() {
    return this.getCentralFrameController().getMainController().getApplication()
        .getIhmCore().getDataForIhm().getAvailableMusics()
        .map(x -> x.getMetadata())
        .collect(Collectors.toList());
  }

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(x -> x.getMetadata()).collect(Collectors.toList()));
  }

}