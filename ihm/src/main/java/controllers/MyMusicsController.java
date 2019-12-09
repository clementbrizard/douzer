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
  private DetailsMusicController detailsMusicController;

  private CentralFrameController centralFrameController;
  private Scene addMusicScene;
  private Scene infoMusicScene;
  private Application application;

  private LocalMusic musicInformation;
  private ArrayList<LocalMusic> listMusics;

  private  ContextMenu contextMenu;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    //logger = LogManager.getLogger();
    listMusics = new ArrayList<LocalMusic>();

  }

  public NewMusicController getNewMusicController() {
    return newMusicController;
  }

  public void setNewMusicController(NewMusicController newMusicController) {
    this.newMusicController = newMusicController;
  }


  public void setDetailsMusicController(DetailsMusicController controller) {
    this.detailsMusicController = controller;
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

  public DetailsMusicController getDetailsMusicController(DetailsMusicController controller) {
    return this.detailsMusicController;
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

    tfSearchTitle.setVisible(false);
    tfSearchArtist.setVisible(false);
    tfSearchAlbum.setVisible(false);
    tfSearchDuration.setVisible(false);

    try {
      this.displayAvailableMusics();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }


    // Create ContextMenu
    contextMenu = new ContextMenu();

    MenuItem itemInformation = new MenuItem("Informations");
    itemInformation.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        showMusicInformation(musicInformation);
        System.out.println("click on first element");
      }
    });

    // Add MenuItem to ContextMenu
    contextMenu.getItems().addAll(itemInformation);
  }


  /**
   * Refreshes the table with getLocalMusics() from DataForIhm.
   */
  public void displayAvailableMusics() {
    List<MusicMetadata> listMusic = this.parseMusic();

    tvMusics.getItems().setAll(listMusic);
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

  /**
   * the Button who will show the windows to add music.
   */
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

  private void showMusicInformation(LocalMusic music) {
    try {
      // Initialize shareScene and shareController
      FXMLLoader musicDetailsLoader = new FXMLLoader(getClass()
          .getResource("/fxml/MusicDetailsView.fxml"));
      Parent musicDetailsParent = musicDetailsLoader.load();
      infoMusicScene = new Scene(musicDetailsParent);
      DetailsMusicController detailsMusicController = musicDetailsLoader.getController();
      this.setDetailsMusicController(detailsMusicController);
      detailsMusicController.setMyMusicsController(this);
      detailsMusicController.initMusic(music);

    } catch (Exception e) {
      e.printStackTrace();
    }

    Stage musicDetailsPopup = new Stage();
    musicDetailsPopup.setTitle("Info musique");
    musicDetailsPopup.setScene(this.infoMusicScene);

    // Set position of second window, relatively to primary window.
    //musicSharingPopup.setX(application.getPrimaryStage().getX() + 200);
    //musicSharingPopup.setY(application.getPrimaryStage().getY() + 100);

    // Show sharing popup.
    musicDetailsPopup.show();
  }

  /**
   * This function implements right click options.
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
   * Shox labels for advanced research on My Musics view.
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
   * @param event The click on the search button.
   */
  @FXML
  public void searchMusics(MouseEvent event) {

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
        .getDataForIhm().searchMusics(query); //TODO rename

    updateMusics(searchResults);
  }

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(x -> x.getMetadata()).collect(Collectors.toList()));
  }

}