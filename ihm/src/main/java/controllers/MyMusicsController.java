package controllers;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import core.Application;
import datamodel.LocalMusic;
import datamodel.Music;
import datamodel.MusicMetadata;
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

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import utils.FormatDuration;

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

  /* Getters */

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

  /* Initialisation methods */

  @Override
  public void initialize() {
    localMusics = new HashMap<>();
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
            metadata -> new SimpleStringProperty(
                FormatDuration.run(metadata.getValue().getDuration())
            ));

    // Hide advanced search fields
    tfSearchTitle.setVisible(false);
    tfSearchArtist.setVisible(false);
    tfSearchAlbum.setVisible(false);
    tfSearchTags.setVisible(false);

    ChangeListener<String> textListener = (observable, oldValue, newValue) -> searchMusics();

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
      this.displayAvailableMusics();
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
      if (listMusicClicked.size() == 1) {
        ArrayList<LocalMusic> musics = new ArrayList<LocalMusic>();
        int index = 0;
        for (int i = 0; i < tvMusics.getItems().size(); i++) {
          MusicMetadata m = tvMusics.getItems().get(i);
          if (currentLocalMusic.getMetadata().getHash().equals(m.getHash())) {
            index = i;
          }
          
          musics.add(localMusics.get(m.getHash()));
        }
        getCentralFrameController()
          .getMainController()
          .getPlayerController()
          .playOneMusic(musics,index);
      } else {
        int index = 0;
        for (int i = 0; i < listMusicClicked.size(); i++) {
          if (currentLocalMusic.getMetadata().getHash().equals(listMusicClicked.get(i).getHash())) {
            index = i;
          }
        }
        getCentralFrameController()
          .getMainController()
          .getPlayerController()
          .playOneMusic(listMusicClicked,index);
      }
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
    contextMenu.getItems().addAll(playMusic, itemInformation, itemDelete);
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
    ArrayList<LocalMusic> musicsList = new ArrayList<LocalMusic>();
    int index = -1;

    if (music != null) {
      currentLocalMusic = localMusics.get(music.getHash());
      index = 0;
      for (int i = 0; i < tvMusics.getItems().size(); i++) {
        MusicMetadata m = tvMusics.getItems().get(i);
        if (currentLocalMusic.getMetadata().getHash().equals(m.getHash())) {
          index = i;
        }
        musicsList.add(localMusics.get(m.getHash()));
      }
    }

    // If left click, show current music info at right of the screen
    if (click.getButton().equals(MouseButton.PRIMARY)) {
      if (music != null) {

        this.getCentralFrameController().getMainController()
            .getCurrentMusicInfoController().init(currentLocalMusic);

        // refresh player view music information
        this.getCentralFrameController().getMainController()
            .getPlayerController()
            .showSongInfo(currentLocalMusic.getMetadata());
      }
    }

    // If double left click, play music

    if (click.getClickCount() == 2 && !click.isConsumed()) {
      click.consume();
      if (music != null && !musicsList.isEmpty()) {

        getCentralFrameController()
            .getMainController()
            .getPlayerController()
            .playOneMusic(musicsList,index);
      }
    }

    // If right click, show context menu
    if (click.getButton().equals(MouseButton.SECONDARY)) {
      if (music != null) {
        currentLocalMusic = localMusics.get(music.getHash());
        contextMenu.show(tvMusics, click.getScreenX(), click.getScreenY());
      }
    }
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
        .getDataForIhm().searchMusics(query)
        .filter(music -> music.getOwners().contains(this
            .getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
        ));

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
  }

  /* Logic methods */

  /**
   * Handle click on information item in context menu.
   *
   * @param music the music on which the user right clicked
   */
  public void showMusicInformation(Music music) {
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
  }

  /**
   * Delete a list of one or more musics.
   *
   * @param musicsToDelete the musics to delete
   */
  public void deleteMusics(ArrayList<LocalMusic> musicsToDelete) {

    final String deleteOnApp = "Seulement sur l'application";
    final String deleteOnAppAndDisk = "Sur l'application et le disque";

    List<String> deleteOptions = new ArrayList<String>();

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
      boolean deleteLocal = choice.equals(deleteOnAppAndDisk);
      musicsToDelete.forEach(music -> {
        try {
          this.getApplication()
              .getIhmCore()
              .getDataForIhm()
              .deleteMusic(music, deleteLocal);

          if (music.equals(this
              .getCentralFrameController()
              .getMainController()
              .getPlayerController()
              .getCurrentMusic())
          ) {
            this
                .getCentralFrameController()
                .getMainController()
                .getPlayerController()
                .stopPlayer();
          }

          this.displayAvailableMusics();
        } catch (NullPointerException e) {
          myMusicsLogger.error("Erreur lors d'une suppression de musique", e);
        }
      });
    });
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