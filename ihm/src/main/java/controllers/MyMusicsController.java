package controllers;

import core.Application;
import datamodel.MusicMetadata;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//replace by javadocs
//central view with all user music
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
  private Button btnAddMusic;

  private NewMusicController newMusicController;
  private SearchMusicController searchMusicController;

  private CentralFrameController centralFrameController;
  private Scene addMusicScene;
  private Application application;

  private Logger logger;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    logger = LogManager.getLogger();
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


  public Application getApplication() {
    return application;
  }

  public void setApplication(Application application) {
    this.application = application;
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

  @FXML
  public void addMusic() {
    try {
      // Initialize shareScene and shareController
      FXMLLoader addMusicLoader = new FXMLLoader(getClass().getResource("/fxml/NewMusicView.fxml"));
      Parent addMusicParent = addMusicLoader.load();
      addMusicScene = new Scene(addMusicParent);
      NewMusicController newMusicController = addMusicLoader.getController();
      this.setNewMusicController(newMusicController);
      newMusicController.setMyMusicsController(this);

    } catch (IOException e) {
      logger.error(e);
    }

    Stage addMusicPopup = new Stage();
    addMusicPopup.setTitle("Ajout de musique");
    addMusicPopup.setScene(this.addMusicScene);

    if (application == null) {
      logger.error("MyMusicsController application is null, did you call MyMusicsController.setApplication(app) ?");
      return;
    }
    // Set position of second window, relatively to primary window.
    addMusicPopup.setX(application.getPrimaryStage().getX() + 200);
    addMusicPopup.setY(application.getPrimaryStage().getY() + 100);

    // Show sharing popup.
    addMusicPopup.show();
  }

}
