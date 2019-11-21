package controllers;

import core.Application;
import datamodel.MusicMetadata;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * central view show up all music in the network.
 */
public class AllMusicsController implements Controller {

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

  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  private Application application;

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
  
  /**
   * getter of application.
   * @return a Application
   * @see Application
   */
  public Application getApplication() {
    return application;
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

  
  /**
   * setter of application.
   * @param application the new Application
   * @see Application
   */
  public void setApplication(Application application) {
    this.application = application;
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

    this.displayAvailableMusics();
  }

  
  public void displayAvailableMusics() {
    tvMusics.getItems().setAll(this.parseMusic());
  }

  private List<MusicMetadata> parseMusic() {
    return this.application.getIhmCore().getDataForIhm().getAvailableMusics()
        .map(x -> x.getMetadata())
        .collect(Collectors.toList());
  }

}