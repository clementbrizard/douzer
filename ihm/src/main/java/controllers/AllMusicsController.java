package controllers;

import core.IhmCore;
import datamodel.Music;
import datamodel.MusicMetadata;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

//replace by javadocs
//central view show up all music in the network
public class AllMusicsController implements Controller {

  @FXML private TableView<MusicMetadata> tvMusics;
  @FXML private TableColumn<MusicMetadata, String> artistCol;
  @FXML private TableColumn<MusicMetadata, String> titleCol;
  @FXML private TableColumn<MusicMetadata, String> albumCol;
  @FXML private TableColumn<MusicMetadata, Duration> durationCol;

  private IhmCore ihmCore;
  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
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

  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }


  /**
   * Setup the table columns to receive data.
   */
  public void init() {

    // "artist", "title", "album", "duration" refer to MusicMetaData attributes
    this.artistCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("artist"));
    this.titleCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("title"));
    this.albumCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("album"));
    this.durationCol.setCellValueFactory(
        new PropertyValueFactory<MusicMetadata, Duration>("duration"));

    this.displayAvailableMusics();
  }

  public void displayAvailableMusics() {
    tvMusics.getItems().setAll(this.parseMusic());
  }

  private List<MusicMetadata> parseMusic() {
    return this.ihmCore.getDataForIhm().getAvailableMusics()
        .map(x -> x.getMetadata())
        .collect(Collectors.toList());
  }
}