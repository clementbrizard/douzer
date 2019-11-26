package controllers;

import core.Application;
import datamodel.Music;
import datamodel.MusicMetadata;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.text.html.ImageView;


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


    MusicMetadata mtest = new MusicMetadata();
    mtest.setTitle("aaa");
    mtest.setArtist("bbb");
    mtest.setAlbum("ccc");

    MusicMetadata mtest2 = new MusicMetadata();
    mtest2.setTitle("zzz");
    mtest2.setArtist("yyy");
    mtest2.setAlbum("aaa");

    ArrayList<MusicMetadata> l = new ArrayList<MusicMetadata>();
    l.add(mtest);
    l.add(mtest2);

    tvMusics.getItems().setAll(l);

    //this.displayAvailableMusics();
  }

  private MusicMetadata getCurrentlySelectedItem() {
    return tvMusics.getSelectionModel().getSelectedItem();
  }

  public void searchClicked() {
    // Filter music list according to search
    //tvMusics.getItems().setAll();
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

  /**
   * Called when the user clicks on the info image.
   * Gets the selected music and shows its information.
   */
  public void showMusicInfo() {
    MusicMetadata metadataToShow = getCurrentlySelectedItem();
    if (metadataToShow != null) {
      System.out.println("SHOWING -> " + metadataToShow.getArtist());
    }
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