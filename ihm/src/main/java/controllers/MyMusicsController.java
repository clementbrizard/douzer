package controllers;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import core.Application;
import datamodel.LocalMusic;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.Playlist;
import datamodel.SearchQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import utils.FormatDuration;

/**
 * Central view show up my musics.
 */
public class MyMusicsController implements Controller {
  private static final org.apache.logging.log4j.Logger myMusicsLogger = LogManager.getLogger();

  @FXML
  private Label lblTitle;

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
  private TextField tfSearchTags;
  @FXML
  private Button btnAddMusic;

  private NewMusicController newMusicController;
  private SearchMusicController searchMusicController;
  private DetailsMusicController detailsMusicController;
  private CentralFrameController centralFrameController;
  private Scene addMusicScene;
  private Scene infoMusicScene;
  private Application application;
  private ContextMenu contextMenu;

  // Clicked local music to trigger actions
  private LocalMusic currentLocalMusic;

  // Local musics hashmap to access them instantly
  private LinkedHashMap<String, LocalMusic> localMusics;
  private List<MusicMetadata> musicsToDisplay;


  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    //logger = LogManager.getLogger();
    localMusics = new LinkedHashMap<String, LocalMusic>();
    musicsToDisplay = new ArrayList<>();
  }

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

  /**
   * getLocalMusicInView.
   */
  public ArrayList<LocalMusic> getLocalMusicInView() {
    ArrayList<LocalMusic> h = new ArrayList<>();
    this.tvMusics.getItems().forEach(item -> {
      h.add(localMusics.get(item.getHash()));
    });

    return h;
  }

  public Application getApplication() {
    return application;
  }

  /* Setters */

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
                return new SimpleStringProperty(
                    FormatDuration.run(metadata.getValue().getDuration())
                );
              }
            });

    // Hide advanced search fields
    tfSearchTitle.setVisible(false);
    tfSearchArtist.setVisible(false);
    tfSearchAlbum.setVisible(false);
    tfSearchTags.setVisible(false);

    ChangeListener<String> textListener = new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
                          String oldValue, String newValue) {
        searchMusics();
      }
    };

    // Bind listener to simple and advanced search fields for instant search
    tfSearch.textProperty().addListener(textListener);
    tfSearchTitle.textProperty().addListener(textListener);
    tfSearchArtist.textProperty().addListener(textListener);
    tfSearchAlbum.textProperty().addListener(textListener);
    tfSearchTags.textProperty().addListener(textListener);

    //event when the user edit the textField
    tfSearch.textProperty().addListener(textListener);


    tvMusics.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    try {
      this.displayLocalMusics();
    } catch (UnsupportedOperationException e) {
      myMusicsLogger.error(e);
    }

    // Create ContextMenu for getting information
    // about music on right click.
    contextMenu = new ContextMenu();

    // Create information item for context menu
    MenuItem itemInformation = new MenuItem("Informations");
    itemInformation.setOnAction(event -> showMusicInformation(currentLocalMusic));

    MenuItem playMusic = new MenuItem("Play");
    playMusic.setOnAction(event -> {
      ArrayList<LocalMusic> listMusicClicked = new ArrayList<LocalMusic>();

      ObservableList<MusicMetadata> selectedItems = tvMusics
          .getSelectionModel()
          .getSelectedItems();

      selectedItems.forEach(item -> {
        listMusicClicked.add(localMusics.get(item.getHash()));
      });

      //add to the list with right click play to the list
      if (listMusicClicked.isEmpty()) {
        listMusicClicked.add(currentLocalMusic);
      } else {
        if (!listMusicClicked.contains(currentLocalMusic)) {
          listMusicClicked.add(currentLocalMusic);
        }
      }

      getCentralFrameController()
          .getMainController()
          .getPlayerController()
          .playOneMusic(tvMusics.getSelectionModel().getFocusedIndex());
    });

    // Add delete item in context menu
    MenuItem itemDelete = new MenuItem("Supprimer");
    itemDelete.setOnAction(event -> {
      ArrayList<LocalMusic> musicsDelete = new ArrayList<>();
      ObservableList<MusicMetadata> selectedItems = tvMusics
          .getSelectionModel()
          .getSelectedItems();

      selectedItems.forEach(item -> {
        musicsDelete.add(localMusics.get(item.getHash()));
      });
      deleteMusics(musicsDelete);
    });

    Menu menuAddToPlaylist = new Menu("Ajouter à la la playlist...");
    menuAddToPlaylist.setOnAction(event -> {
      menuAddToPlaylist.getItems().clear();
      List<String> playlistNames = MyMusicsController.this.getApplication()
          .getIhmCore().getDataForIhm()
          .getCurrentUser().getPlaylists()
          .stream().map(p -> p.getName())
          .collect(Collectors.toList());
      playlistNames.forEach(name -> {
        MenuItem menuPlaylist = new MenuItem(name);
        menuPlaylist.setOnAction(ev -> {
          Playlist playlist = MyMusicsController.this.getApplication()
              .getIhmCore().getDataForIhm()
              .getPlaylistByName(name);
          ObservableList<MusicMetadata> selectedItems = tvMusics
              .getSelectionModel()
              .getSelectedItems();

          selectedItems.forEach(item -> {
            MyMusicsController.this.getApplication()
              .getIhmCore().getDataForIhm()
              .addMusicToPlaylist(localMusics.get(item.getHash()), playlist, 0);
          });
        });
        menuAddToPlaylist.getItems().add(menuPlaylist);
      });
    });

    // Add MenuItem to ContextMenu
    contextMenu.getItems().addAll(playMusic, itemInformation, itemDelete, menuAddToPlaylist);


    // Header click event
    tvMusics.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
      if (event.getTarget() instanceof TableColumnHeader) {
        event.consume();
        this.getCentralFrameController()
            .getMainController()
            .getPlayerController()
            .updateArrayMusic();
      }

    });

  }

  /* FXML methods (to handle events from user) */

  /**
   * Handle mouse events on music table.
   *
   * @param click mouse event
   */
  @FXML
  public void handleClickTableView(MouseEvent click) {
    MusicMetadata music = tvMusics.getSelectionModel().getSelectedItem();

    // If left click, show current music info at right of the screen
    if (click.getButton().equals(MouseButton.PRIMARY)) {
      if (music != null) {
        currentLocalMusic = localMusics.get(music.getHash());

        this.getCentralFrameController().getMainController()
            .getCurrentMusicInfoController().init(currentLocalMusic);

        this.getCentralFrameController().getMainController()
            .getPlayerController()
            .selectOneMusic(tvMusics.getSelectionModel()
                .getFocusedIndex());
      }
    }
    // If double left click, play music

    if (click.getClickCount() == 2 && !click.isConsumed()) {
      click.consume();
      this.getCentralFrameController().getMainController()
          .getPlayerController()
          .playOneMusic(tvMusics.getSelectionModel()
              .getFocusedIndex());
    }

    // If right click, show context menu
    if (click.getButton().equals(MouseButton.SECONDARY)) {
      if (music != null) {
        currentLocalMusic = localMusics.get(music.getHash());
        contextMenu.show(tvMusics, click.getScreenX(), click.getScreenY());
      }
    }
  }

  /*
  private List<MusicMetadata> parseMusic() {
    this.listMusics.addAll(this.getCentralFrameController().getMainController().getApplication()
            .getIhmCore().getDataForIhm().getLocalMusics().collect(Collectors.toList()));

    return this.getCentralFrameController().getMainController().getApplication()
            .getIhmCore().getDataForIhm().getLocalMusics()
            .map(x -> x.getMetadata()).collect(Collectors.toList());
  }*/

  public void displayLocalMusics() {
    musicsToDisplay = this.retrieveLocalMusics();
    this.updateTableMusics();
  }

  private void updateTableMusics() {
    tvMusics.getItems().setAll(musicsToDisplay);
  }

  /**
   * Handle click on button to add a new music.
   */
  @FXML
  public void addMusic() {
    try {
      // Initialize scene and controller of new music pop-up
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
    musicSharingPopup.initModality(Modality.APPLICATION_MODAL);
    musicSharingPopup.showAndWait();
  }

  /**
   * Handle click on advanced search button.
   *
   * @param event The click on the advanced search button
   */
  @FXML
  public void showAdvancedSearch(ActionEvent event) {
    if (tfSearch.isDisabled()) {
      tfSearch.setDisable(false);
      tfSearchTitle.setVisible(false);
      tfSearchArtist.setVisible(false);
      tfSearchAlbum.setVisible(false);
      tfSearchTags.setVisible(false);

    } else {
      tfSearch.setDisable(true);
      tfSearchTitle.setVisible(true);
      tfSearchArtist.setVisible(true);
      tfSearchAlbum.setVisible(true);
      tfSearchTags.setVisible(true);

    }
  }

  /**
   * Handle changes in simple or advanced search fields.
   */
  @FXML
  public void searchMusics() {

    SearchQuery query = new SearchQuery();

    if (!tfSearch.isDisabled()) {
      query.withText(tfSearch.getText());
    } else {
      // TextField default constructor sets the initial text to "" (empty string)
      // so instead of checking if text is not null (which will be false), we must
      // check if trimmed text (with leading and trailing whitespaces removed) is not empty.
      if (!tfSearchTitle.getText().trim().isEmpty()) {
        query.withTitle(tfSearchTitle.getText());
      }

      if (!tfSearchArtist.getText().trim().isEmpty()) {
        query.withArtist(tfSearchArtist.getText());
      }

      if (!tfSearchAlbum.getText().trim().isEmpty()) {
        query.withAlbum(tfSearchAlbum.getText());
      }

      if (!tfSearchTags.getText().trim().isEmpty()) {
        //on remplace les espaces avant et après la virgule,
        //mais pas ceux contenus dans les tags
        // trim() supprime les espaces après chaque tag
        //ce qui permet de supprimer les espaces après le dernier tags et
        // de ne faire la recherche qu'avec les caractères utiles
        Collection<String> tags = Arrays.asList(tfSearchTags
            .getText()
            .trim()
            .replaceAll("\\s*,\\s*", ",")
            .split(",")
        );

        query.withTags(tags);
      }
    }

    Stream<Music> searchResults = MyMusicsController.this.getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm().searchMusics(query);

    updateMusicsOnSearch(searchResults);
  }

  /**
   * Handle click on all musics button.
   *
   * @param event the click on all musics button
   */
  @FXML
  public void changeFrameToAllMusics(ActionEvent event) {
    MyMusicsController.this.centralFrameController.setCentralContentAllMusics();
    this.getCentralFrameController().getMainController().getMyPlaylistsController()
        .resetSelection();
  }

  /**
   * Handle click on loopPlayer musics button.
   *
   * @return
   */
  @FXML
  public void loopPlayer() {
    this.getCentralFrameController().getMainController().getPlayerController().loopPlayer();
  }

  /**
   * Handle click on randomPlayer musics button.
   *
   * @return
   */
  @FXML
  public void randomPlayer() {
    this.getCentralFrameController().getMainController().getPlayerController().randomPlayer();
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
    musicDetailsPopup.initModality(Modality.APPLICATION_MODAL);
    musicDetailsPopup.showAndWait();
  }

  /**
   * Refresh the table by retrieving local musics from Data.
   */
  public void displayAvailableMusics() {
    List<MusicMetadata> listMusic = this.retrieveLocalMusics();
    tvMusics.getItems().setAll(listMusic);
  }

  /**
   * Update musics table content with search result.
   *
   * @param newMusics the search result
   */
  private void updateMusicsOnSearch(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(Music::getMetadata).collect(Collectors.toList()));
    this.getCentralFrameController().getMainController().getPlayerController().updateArrayMusic();
  }

  /**
   * Delete a list of one or more musics.
   *
   * @param musicsToDelete the musics to delete
   */
  public void deleteMusics(ArrayList<LocalMusic> musicsToDelete) {

    final String deleteOnApp = "Seulement sur l'application";
    final String deleteOnAppAndDisk = "Sur l'application et le disque";
    final String deleteOnPlaylist = "Supprimer de la playlist uniquement";

    List<String> deleteOptions = new ArrayList<String>();

    if (this.getCentralFrameController().getMainController().getMyPlaylistsController()
        .getSelectedIndex() != 0) {
      // We are viewing a playlist
      deleteOptions.add(deleteOnPlaylist);
    }

    deleteOptions.add(deleteOnApp);
    deleteOptions.add(deleteOnAppAndDisk);


    ChoiceDialog<String> deleteMusicChoiceDialog = new ChoiceDialog<>(
        deleteOptions.get(0),
        deleteOptions);

    deleteMusicChoiceDialog.setHeaderText("Choisissez votre mode de suppression :");
    deleteMusicChoiceDialog.setTitle("Suppression musique");
    deleteMusicChoiceDialog.setContentText("Supprimer :");

    Optional<String> deleteChoice = deleteMusicChoiceDialog.showAndWait();

    deleteChoice.ifPresent(choice -> {
      if (choice.equals(deleteOnPlaylist)) {
        String playlistName = this.getCentralFrameController()
            .getMainController()
            .getMyPlaylistsController()
            .getSelectedPlaylist();
        Playlist playlist = this.getApplication()
            .getIhmCore().getDataForIhm()
            .getPlaylistByName(playlistName);
        musicsToDelete.forEach(music -> {
          this.getApplication().getIhmCore().getDataForIhm()
              .removeMusicFromPlaylist(music, playlist);
        });
        this.showPlaylist(playlistName);
      } else {
        boolean deleteLocal = choice.equals(deleteOnAppAndDisk);
        musicsToDelete.forEach(music -> {
          try {
            this.getApplication()
                .getIhmCore()
                .getDataForIhm()
                .deleteMusic(music, deleteLocal);
            this.displayLocalMusics();
          } catch (NullPointerException e) {
            myMusicsLogger.error("Erreur lors d'une suppression de musique", e);
          }
        });
        this.displayLocalMusics();
      }

    });
  }

  /**
   * Sets the view to show the current playlist only.
   * @param playlistName name of the playlist
   */
  public void showPlaylist(String playlistName) {
    this.lblTitle.setText(playlistName);
    this.musicsToDisplay.clear();

    // Get musics in playlist
    this.getApplication().getIhmCore().getDataForIhm().getPlaylistByName(playlistName);
    if (this.getApplication().getIhmCore().getDataForIhm()
        .getPlaylistByName(playlistName) !=  null) {
      ArrayList<LocalMusic> musicsInPlaylist = this.getApplication().getIhmCore()
          .getDataForIhm().getPlaylistByName(playlistName).getMusicList();
      musicsInPlaylist.forEach(m -> musicsToDisplay.add(m.getMetadata()));
    }

    this.updateTableMusics();
  }

  /**
   * Retrieve user's local musics from Data.
   *
   * @return list of metadata of user's local musics
   */
  private List<MusicMetadata> retrieveLocalMusics() {
    localMusics.clear();
    Set<LocalMusic> localMusicsStream = this
        .getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getCurrentUser()
        .getLocalMusics();

    List<MusicMetadata> localMusicsMetadata = new ArrayList<>();
    for (LocalMusic localMusic : localMusicsStream) {
      //if the current localMusic isn't already in our localMusics we add it.
      if (localMusics.get(localMusic.getMetadata().getHash()) == null) {
        localMusics.put(localMusic.getMetadata().getHash(), localMusic);
        localMusicsMetadata.add(localMusic.getMetadata());
      }
    }

    return localMusicsMetadata;
  }


  public void reset() {
    this.lblTitle.setText("Mes morceaux");
    this.displayLocalMusics();
  }
}