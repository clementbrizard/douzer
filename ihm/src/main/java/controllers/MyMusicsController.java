package controllers;

import core.Application;
import datamodel.LocalMusic;

import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import utils.FormatDigit;

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

  private LocalMusic musicInformation;
  private ArrayList<LocalMusic> listMusics;

  private ContextMenu contextMenu;

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
    // Bind columns to corresponding attributes in MusicMetaData
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
                      duration += FormatDigit.run(matcher.group("hour"));
                      duration += ":";
                    }

                    if (matcher.group("minute") != null) {
                      // We format the minutes only if there are
                      // hours in the duration
                      duration += (duration != "")
                          ? FormatDigit.run(matcher.group("minute"))
                          : matcher.group("minute");
                      duration += ":";
                    }

                    if (matcher.group("second") != null) {
                      duration += FormatDigit.run(matcher.group("second"));
                    }
                  } catch (IllegalStateException e) {
                    myMusicsLogger.error("Error while getting music {} duration", e);
                  }
                }

                return new SimpleStringProperty(duration);
              }
            });

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

    MenuItem playMusic = new MenuItem("Play");
    playMusic.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        ArrayList<LocalMusic> listMusicClicked = new ArrayList<LocalMusic>();

        //get the list of music clicked
        ObservableList<MusicMetadata> selectedItems = tvMusics
            .getSelectionModel()
            .getSelectedItems();

        for (int i = 0; i < selectedItems.size(); i++) {
          for (int j = 0; j < listMusics.size(); j++) {
            if (selectedItems.get(i).equals(listMusics.get(j).getMetadata())) {
              listMusicClicked.add(listMusics.get(j));
            }
          }
        }

        //add to the list with right click play to the list
        if (listMusicClicked.isEmpty()) {
          listMusicClicked.add(musicInformation);
        } else {
          if (!listMusicClicked.contains(musicInformation)) {
            listMusicClicked.add(musicInformation);
          }
        }
        getCentralFrameController()
            .getMainController()
            .getPlayerController()
            .setArrayMusic(listMusicClicked);

        getCentralFrameController()
            .getMainController()
            .getPlayerController()
            .playerOnMusic();
      }
    });


    // Add MenuItem to ContextMenu
    contextMenu.getItems().addAll(itemInformation, playMusic);
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
   * Search music request corresponding to the labels content.
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