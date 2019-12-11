package controllers;

import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Central view show up all music in the network.
 */
public class AllMusicsController implements Controller {
  private static final Logger allMusicsLogger = LogManager.getLogger();

  @FXML
  private TableView<MusicMetadata> tvMusics;
  @FXML
  private TableColumn<MusicMetadata, String> artistCol;
  @FXML
  private TableColumn<MusicMetadata, String> titleCol;
  @FXML
  private TableColumn<MusicMetadata, String> albumCol;
  @FXML
  private TableColumn<MusicMetadata, String> durationCol;
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

  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  // Getters

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

  // Setters

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

    // Duration MusicMetaData attribute has type Duration
    // so we need to convert it to a string
    this.durationCol
        .setCellValueFactory(
            new Callback<TableColumn.CellDataFeatures<MusicMetadata, String>,
                ObservableValue<String>>() {
              public ObservableValue<String> call(
                  TableColumn.CellDataFeatures<MusicMetadata, String> metadata) {

                // Duration toString() returns ISO 8601 duration. We get it and
                // extract hour, minute and second using a Regex

                // Need to do it because regex is too long
                String regex = String.join(
                    "",
                    "PT((?<hour>\\d{0,2})H)?",
                    "((?<minute>\\d{0,2})M)?",
                    "((?<second>\\d{0,2})S?)"
                );

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(metadata.getValue().getDuration().toString());
                String duration = "";

                while (matcher.find()) {
                  try {
                    if (matcher.group("hour") != null) {
                      duration += matcher.group("hour");
                      duration += ":";
                    }

                    if (matcher.group("minute") != null) {
                      duration += matcher.group("minute");
                      duration += ":";
                    }

                    if (matcher.group("second") != null) {
                      duration += matcher.group("second");
                    }
                  } catch (IllegalStateException e) {
                    allMusicsLogger.error("Error while getting music {} duration", e);
                  }
                }

                return new SimpleStringProperty(duration);
              }
            });

    try {
      this.displayAvailableMusics();
    } catch (UnsupportedOperationException e) {
      allMusicsLogger.error(e);
    }

    tfSearchTitle.setVisible(false);
    tfSearchArtist.setVisible(false);
    tfSearchAlbum.setVisible(false);
    tfSearchDuration.setVisible(false);
    ChangeListener<String> textListener = new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
              String oldValue, String newValue) {
        searchMusics();
      }
    };
    
    tfSearchTitle.textProperty().addListener(textListener);
    tfSearchArtist.textProperty().addListener(textListener);
    tfSearchAlbum.textProperty().addListener(textListener);
    tfSearchDuration.textProperty().addListener(textListener);
    
    //event when the user edit the textField
    tfSearch.textProperty().addListener(textListener);
  }

  public void displayAvailableMusics() {
    tvMusics.getItems().setAll(this.parseMusic());
  }

  private List<MusicMetadata> parseMusic() {
    return this.getCentralFrameController().getMainController().getApplication().getIhmCore()
        .getDataForIhm().getLocalMusics()
        .map(x -> x.getMetadata())
        .collect(Collectors.toList());
  }

  /**
   * Show labels for advanced search for All musics view.
   * @param event the clic ont the button "Recherche avancée".
   */
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
  public void changeFrameToMyMusics(ActionEvent event) {
    AllMusicsController.this.centralFrameController.setCentralContentMyMusics();
  }

  /**
   * Search all musics that correspond to the labels content.
   */
  @FXML
  public void searchMusics() {

    SearchQuery query = new SearchQuery();
    if (!tfSearch.isDisabled()) {
      query.withText(tfSearch.getText());
    } else {
      if (tfSearchTitle.getText() != null) {
        query.withTitle(tfSearchTitle.getText());
      }

      if (tfSearchArtist != null) {
        query.withArtist(tfSearchArtist.getText());
      }

      if (tfSearchAlbum != null) {
        query.withAlbum(tfSearchAlbum.getText());
      }

      /*if (tfSearchDuration != null) {
        query.withArtist(tfSearchDuration.getText());
      }*/
    }

    Stream<Music> searchResults = AllMusicsController.this.getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm().searchMusics(query); 

    updateMusics(searchResults);
  }

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(x -> x.getMetadata()).collect(Collectors.toList()));
  }
}