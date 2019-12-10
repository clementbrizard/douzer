package controllers;

import core.Application;
import datamodel.LocalMusic;

import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.stream.Stream;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;

/**
 * Central view show up my musics.
 */
public class MyMusicsController implements Controller {
  private static final org.apache.logging.log4j.Logger myMusicsLogger = LogManager.getLogger();

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
  private DetailsMusicController detailsMusicController;
  private CentralFrameController centralFrameController;

  private Scene addMusicScene;
  private Scene infoMusicScene;
  private Application application;

  private LocalMusic musicInformation;
  private ArrayList<LocalMusic> listMusics;

  private  ContextMenu contextMenu;

  // Getters

  public NewMusicController getNewMusicController() {
    return newMusicController;
  }

  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  public DetailsMusicController getDetailsMusicController(DetailsMusicController controller) {
    return this.detailsMusicController;
  }

  public Application getApplication() {
    return application;
  }

  // Setters

  public void setNewMusicController(NewMusicController newMusicController) {
    this.newMusicController = newMusicController;
  }

  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  public void setDetailsMusicController(DetailsMusicController controller) {
    this.detailsMusicController = controller;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  // Other methods

  @Override
  public void initialize() {
    listMusics = new ArrayList<LocalMusic>();
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



    try {
      this.displayAvailableMusics();
    } catch (UnsupportedOperationException e) {
      myMusicsLogger.error(e);
    }

    // Create ContextMenu for getting informations
    // of music on right click.
    contextMenu = new ContextMenu();

    MenuItem itemInformation = new MenuItem("Informations");
    itemInformation.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showMusicInformation(musicInformation);
      }
    });

    MenuItem itemDelete = new MenuItem("Delete");
    itemDelete.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ArrayList<LocalMusic> musicsDelete = new ArrayList<LocalMusic>();
        ObservableList<MusicMetadata> selectedItems = tvMusics.getSelectionModel().getSelectedItems();
        for (int i = 0;i < selectedItems.size();i++) {
        for (int j = 0;j < listMusics.size();j++) {
          if (selectedItems.get(i).equals(listMusics.get(j).getMetadata())) {
            musicsDelete.add(listMusics.get(j));
            }
          }
        }
        delete(musicsDelete);
      }
    });

    // Add "Informations" menu item to ContextMenu.
    contextMenu.getItems().addAll(itemInformation, itemDelete);
  }

  public void delete(ArrayList<LocalMusic> musicsDelete) {
    for (int i = 0; i < musicsDelete.size(); i++) {
      try {
        this.getApplication().getIhmCore().getDataForIhm().deleteMusic(musicsDelete.get(i), true);
      } catch (NullPointerException e) {

      }
    }
    displayAvailableMusics();
  }


  /**
   * Refresh the table with getLocalMusics() from DataForIhm.
   */
  public void displayAvailableMusics() {
    List<MusicMetadata> listMusic = this.parseMusic();
    tvMusics.getItems().setAll(listMusic);
  }

  /**
   * The button that show the form to add a music.
   */
  @FXML
  public void addMusic() {
    try {
      // Initialize scene and controller.
      FXMLLoader addMusicLoader = new FXMLLoader(getClass().getResource("/fxml/NewMusicView.fxml"));
      Parent addMusicParent = addMusicLoader.load();
      addMusicScene = new Scene(addMusicParent);
      NewMusicController newMusicController = addMusicLoader.getController();
      this.setNewMusicController(newMusicController);
      newMusicController.setMyMusicsController(this);

    } catch (Exception e) {
      myMusicsLogger.error(e);
    }

    Stage musicSharingPopup = new Stage();
    musicSharingPopup.setTitle("Ajout musique");
    musicSharingPopup.setScene(this.addMusicScene);

    // Show add music popup.
    musicSharingPopup.show();
  }

  /**
   * This function implements right click options.
   *
   * @param click mouse event right click.
   */
  @FXML
  public void handleClickTableView(MouseEvent click) {
    MusicMetadata music = tvMusics.getSelectionModel().getSelectedItem();

    if (click.getButton().equals(MouseButton.PRIMARY)) {
      if (music != null) {
        for (int i = 0; i < this.listMusics.size(); i++) {
          if (this.listMusics.get(i).getMetadata().equals(music)) {
            musicInformation = listMusics.get(i);
          }
        }

        this.getCentralFrameController().getMainController()
            .getCurrentMusicInfoController().init(musicInformation);
      }
    }

    if (click.getButton().equals(MouseButton.SECONDARY)) {
      if (music != null) {
        for (int i = 0; i < this.listMusics.size(); i++) {
          if (this.listMusics.get(i).getMetadata().equals(music)) {
            musicInformation = listMusics.get(i);
          }
        }

        contextMenu.show(tvMusics, click.getScreenX(), click.getScreenY());
      }
    }
  }

  @FXML
  public void changeFrameToAllMusics(ActionEvent event) {
    MyMusicsController.this.centralFrameController.setCentralContentAllMusics();
  }

  /**
   * Show labels for advanced research on My Musics view.
   *
   * @param event The click on the button "Recherche avancée".
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

  /**
   * Search music request corresponding to the labels content.
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

    Stream<Music> searchResults = MyMusicsController.this.getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm().searchMusics(query);

    updateMusics(searchResults);
  }

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(x -> x.getMetadata()).collect(Collectors.toList()));
  }

  private List<MusicMetadata> parseMusic() {
    listMusics.clear();
    this.listMusics.addAll(this.getCentralFrameController().getMainController().getApplication()
        .getIhmCore().getDataForIhm().getLocalMusics()
        .collect(Collectors.toCollection(ArrayList::new)));

    List<MusicMetadata> l = new ArrayList<MusicMetadata>();

    for (int i = 0; i < this.listMusics.size(); i++) {
      l.add(listMusics.get(i).getMetadata());
    }
    return l;
  }

  private void showMusicInformation(LocalMusic music) {
    try {
      // Initialize scene and controller.
      FXMLLoader musicDetailsLoader = new FXMLLoader(getClass()
          .getResource("/fxml/MusicDetailsView.fxml"));
      Parent musicDetailsParent = musicDetailsLoader.load();
      infoMusicScene = new Scene(musicDetailsParent);
      DetailsMusicController detailsMusicController = musicDetailsLoader.getController();
      this.setDetailsMusicController(detailsMusicController);
      detailsMusicController.setMyMusicsController(this);
      detailsMusicController.initMusic(music);

    } catch (Exception e) {
      myMusicsLogger.error(e);
    }

    Stage musicDetailsPopup = new Stage();
    musicDetailsPopup.setTitle("Info musique");
    musicDetailsPopup.setScene(this.infoMusicScene);

    // Show music info popup.
    musicDetailsPopup.show();
  }
}