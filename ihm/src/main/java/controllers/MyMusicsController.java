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
import java.util.HashMap;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import org.apache.logging.log4j.LogManager;
import org.controlsfx.control.Notifications;
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
  private TableColumn<MusicMetadata, Set<String>> tagsCol;

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
  private HashMap<String, LocalMusic> localMusics;
  // MusicsToDispay is used to filter playlists
  private List<MusicMetadata> musicsToDisplay;


  @Override
  public void initialize() {
    localMusics = new HashMap<String, LocalMusic>();
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
    // Bind columns to corresponding attributes in MusicMetaData
    this.artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
    this.titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    this.albumCol.setCellValueFactory(new PropertyValueFactory<>("album"));
    this.tagsCol.setCellValueFactory(new PropertyValueFactory<>("tags"));


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


    // Add MenuItem to ContextMenu
    // menuAddToPlaylist should ALWAYS be the last item of contextMenu !
    // @see refreshMenuPlaylist() which removes the last item to replace it with playlist menu
    contextMenu.getItems().addAll(playMusic, itemInformation, itemDelete);
    refreshMenuPlaylist(false);

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

    // Remove up / down arrows keybinding (used to move playlists)
    tvMusics.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
      if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN) {
        this.moveMusicInCurrentPlaylist(keyEvent);
        keyEvent.consume();
      }
    });
  }

  /**
   * Adds the "ajouter à la playlist..." menu to the context menu which pops up with a right click.
   *
   * @param removeOldMenu if true, removes the old playlist menu to replace it with a new one.
   *                      false should be used for initialization only, or else the menu
   *                      will be duplicated.
   */
  private void refreshMenuPlaylist(boolean removeOldMenu) {
    if (removeOldMenu) {
      contextMenu.getItems().remove(contextMenu.getItems().size() - 1);
    }
    Menu menuAddToPlaylist = new Menu("Ajouter à la la playlist...");
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
          if (playlist.getMusicList().contains(localMusics.get(item.getHash()))) {
            Notifications.create()
                .title("Ajout non nécessaire")
                .text("Le morceau \"" + item.getTitle() + "\" est déjà présent dans la playlist \""
                    + playlist.getName() + "\" !")
                .showWarning();
          } else {
            MyMusicsController.this.getApplication()
                .getIhmCore().getDataForIhm()
                .addMusicToPlaylist(localMusics.get(item.getHash()), playlist, 0);

            Notifications.create()
                .title("Ajout réussi")
                .text("Morceau \"" + item.getTitle() + "\" ajouté dans la playlist \""
                    + playlist.getName() + "\" avec succès !")
                .showInformation();
          }
        });
      });
      menuAddToPlaylist.getItems().add(menuPlaylist);
    });
    contextMenu.getItems().add(menuAddToPlaylist);
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
        this.refreshMenuPlaylist(true);
        contextMenu.show(tvMusics, click.getScreenX(), click.getScreenY());
      }
    }
  }

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
    Stream<Music> rawResult = MyMusicsController.this.getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm().searchMusics(query);

    Stream<Music> specificResult = rawResult;

    if (this.lblTitle.getText().equals("Mes morceaux")) {
      specificResult = rawResult.filter(music ->
        music.getOwners().contains(this
            .getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser())
      );
    } else {
      Playlist currentPlaylist = this.getApplication().getIhmCore().getDataForIhm()
          .getCurrentUser().getPlaylistByName(this.lblTitle.getText());
      specificResult = rawResult.filter(music -> currentPlaylist.getMusicList()
          .contains(music));
    }

    updateMusicsOnSearch(specificResult);
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

  /* Logic methods */

  /**
   * Handle click on information item in context menu.
   *
   * @param music the music on which the user right clicked
   */
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

    //change the size of Tags column
    ArrayList<Double> d = new ArrayList<Double>();
    d.add(0.0);
    listMusic.forEach(metadata -> {
      double numberOfChar = 0;
      for (String tag : metadata.getTags()) {
        numberOfChar += tag.length();
      }
      if (numberOfChar > d.get(0)) {
        d.set(0,numberOfChar);
      }
    });
    this.tagsCol.setPrefWidth(d.get(0) * 9);
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
    deleteMusicChoiceDialog.initModality(Modality.APPLICATION_MODAL);
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

            if (music.getMetadata()
                .getTitle()
                .equals(
                    this
                        .getCentralFrameController()
                        .getMainController()
                        .getPlayerController()
                        .getCurrentMusicTitle())

            ) {
              this
                  .getCentralFrameController()
                  .getMainController()
                  .getPlayerController()
                  .stopPlayer();
            }

            // Remove music from every playlist
            for (Playlist playlist : this.getApplication().getIhmCore().getDataForIhm()
                .getCurrentUser().getPlaylists()) {
              this.getApplication().getIhmCore().getDataForIhm().removeMusicFromPlaylist(
                  music, playlist
              );
            }
            this.displayAvailableMusics();
          } catch (NullPointerException e) {
            myMusicsLogger.error("Erreur lors d'une suppression de musique", e);
          }
        });
      }
    });
  }

  /**
   * Sets the view to show the current playlist only.
   * @param playlistName name of the playlist
   */
  public void showPlaylist(String playlistName) {
    this.lblTitle.setText(playlistName);

    if (playlistName.equals("Mes morceaux")) {
      this.displayLocalMusics();
    } else {
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
  }

  /**
   * Moves a music in the current playlist.
   *
   * @param keyEvent : up or down arrow key press
   */
  private void moveMusicInCurrentPlaylist(KeyEvent keyEvent) {
    String currentPlaylistName = this.lblTitle.getText();
    int currentIndexRow = tvMusics.getSelectionModel().getSelectedIndex();
    LocalMusic selectedMusic = getLocalMusicInView().get(currentIndexRow);
    int newOrder = 0;

    if (keyEvent.getCode() == KeyCode.DOWN) {
      newOrder = currentIndexRow + 1;
    } else if (keyEvent.getCode() == KeyCode.UP) {
      newOrder = currentIndexRow - 1;
    } else {
      myMusicsLogger.error("Caught wrong key press event !");
    }

    if (newOrder < 0) {
      newOrder = 0;
    }

    if (!currentPlaylistName.equals("Mes morceaux")) {
      Playlist currentPlaylist = this.getApplication().getIhmCore().getDataForIhm()
          .getCurrentUser().getPlaylistByName(currentPlaylistName);
      if (newOrder > currentPlaylist.getMusicList().size() - 1) {
        newOrder = currentPlaylist.getMusicList().size() - 1;
      }
      currentPlaylist.changeOrder(selectedMusic, newOrder);
    } else {
      Notifications.create()
          .title("Impossible de changer l'ordre des musiques")
          .text("L'ordre des morceaux ne peut être modifié que "
              + "dans une playlist que vous avez créée.")
          .darkStyle()
          .showWarning();
    }
    //Update track order
    this.showPlaylist(currentPlaylistName);
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
      if (localMusics.get(localMusic.getHash()) == null) {
        localMusics.put(localMusic.getHash(), localMusic);
        localMusicsMetadata.add(localMusic.getMetadata());
      }
    }

    return localMusicsMetadata;
  }
}