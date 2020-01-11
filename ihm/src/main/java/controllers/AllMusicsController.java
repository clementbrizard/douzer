package controllers;

import datamodel.LocalMusic;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.FormatDuration;

/**
 * Central view show up all music in the network.
 */
public class AllMusicsController implements Controller {
  private static final Logger allMusicsLogger = LogManager.getLogger();

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

  private SearchMusicController searchMusicController;

  private CentralFrameController centralFrameController;

  // All musics hashmap to access them instantly
  private HashMap<String, Music> availableMusics;
  
  private ContextMenu contextMenu;
  
  private Music musicSelected;
  
  private boolean isOnlyLocalMusicSelected = true;

  @FXML
  private Button btnDownload;

  // Getters

  /**
   * getter of searchMusicController.
   *
   * @return a SearchMusicController
   * @see SearchMusicController
   */
  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  /**
   * getter of centralFrameController.
   *
   * @return a CentralFrameController
   * @see CentralFrameController
   */
  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  // Setters

  /**
   * setter of searchMusicController.
   *
   * @param searchMusicController the new SearchMusicController
   * @see SearchMusicController
   */
  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  /**
   * setter of centralFrameController.
   *
   * @param centralFrameController the new CentralFrameController
   * @see CentralFrameController
   */
  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  // Other methods

  /**
   * the initialize method of the Controller call by Javafx when we create the view of AllMusics.
   */
  @Override
  public void initialize() {
    availableMusics = new HashMap<String, Music>();
  }

  /**
   * Setup the table columns to receive data,
   * this method has to be called right after the creation of the view.
   */
  public void init() {
    // Download button is availabe only a music that is not local is selected
    btnDownload.setDisable(true);

    // "artist", "title", "album", "duration" refer to MusicMetaData attributes
    this.artistCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("artist"));
    this.titleCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("title"));
    this.albumCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("album"));
    this.tagsCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, Set<String>>("tags"));

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

    try {
      this.displayAvailableMusics();
    } catch (UnsupportedOperationException e) {
      allMusicsLogger.error(e);
    }

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
  }
  
  /**
   * construct the contextMenu (the windows who appear when user right click on a music)
   * depend on which music the user selected.
   */
  private void constructContextMenu() {
    // Create ContextMenu for getting information
    // about music on right click.
    contextMenu = new ContextMenu();
    // Create information item for context menu
    MenuItem itemInformation = new MenuItem("Informations");
    itemInformation.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        getCentralFrameController()
          .getMyMusicsController()
          .showMusicInformation(musicSelected);
      }
    });
    
    //check the selected item
    tvMusics
        .getSelectionModel()
        .getSelectedItems()
        .forEach(item -> {
          if (!(availableMusics.get(item.getHash()) instanceof LocalMusic)) {
            isOnlyLocalMusicSelected = false;
          }
        });    
    if (musicSelected instanceof LocalMusic && isOnlyLocalMusicSelected) {
      MenuItem playMusic = new MenuItem("Jouer");
      playMusic.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          if (musicSelected instanceof LocalMusic) {
            ArrayList<LocalMusic> listMusicClicked = new ArrayList<LocalMusic>();
    
            ObservableList<MusicMetadata> selectedItems = tvMusics
                .getSelectionModel()
                .getSelectedItems();
    
            selectedItems.forEach(item -> {
              listMusicClicked.add((LocalMusic) availableMusics.get(item.getHash()));
            });
    
            //add to the list with right click play to the list
            if (listMusicClicked.isEmpty()) {
              listMusicClicked.add((LocalMusic) musicSelected);
            } else {
              if (!listMusicClicked.contains(musicSelected)) {
                listMusicClicked.add((LocalMusic) musicSelected);
              }
            }

            if (listMusicClicked.size() == 1) {
              ArrayList<LocalMusic> musics = new ArrayList<LocalMusic>();
              int index = 0;
              int y = 0;
              for (int i = 0; i < tvMusics.getItems().size(); i++) {
                MusicMetadata m = tvMusics.getItems().get(i);
                if (availableMusics.get(m.getHash()) instanceof LocalMusic) {
                  LocalMusic localMusic = (LocalMusic) availableMusics.get(m.getHash());
                  if (localMusic.getHash().equals(musicSelected.getHash())) {
                    index = y;
                  }
                  musics.add(localMusic);
                  y += 1;
                }
              }
              getCentralFrameController()
                .getMainController()
                .getPlayerController()
                .playOneMusic(musics,index);
              
            } else {
            
              // Get the first local music selected index (do not count the distant musics)
              int readIndex = 0;
              ArrayList<LocalMusic> arrayMusicAll =
                  getCentralFrameController()
                      .getMyMusicsController()
                      .getLocalMusicInView();
  
              for (LocalMusic m : arrayMusicAll) {
                if (listMusicClicked.get(0).getHash().equals(m.getHash())) {
                  break;
                }
                readIndex++;
              }
  
              getCentralFrameController()
                .getMainController()
                .getPlayerController()
                .playOneMusic(listMusicClicked,readIndex);
            }
          }
        }
      });
  
      // Add delete item in context menu
      MenuItem itemDelete = new MenuItem("Supprimer");
      itemDelete.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          ArrayList<LocalMusic> musicsDelete = new ArrayList<LocalMusic>();
          ObservableList<MusicMetadata> selectedItems = tvMusics
              .getSelectionModel()
              .getSelectedItems();
  
          selectedItems.forEach(item -> {
            musicsDelete.add((LocalMusic) availableMusics.get(item.getHash()));
          });
          getCentralFrameController()
            .getMyMusicsController()
            .deleteMusics(musicsDelete);
        }
      });

      // Add MenuItem to ContextMenu
      contextMenu.getItems().addAll(playMusic, itemInformation, itemDelete);
    } else {
      MenuItem download = new MenuItem("Telechargement");
      download.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          centralFrameController
            .getMainController()
            .getDownloadController()
            .download(musicSelected);
        }
      });
      
      
      
      contextMenu.getItems().addAll(itemInformation,download);
    }
  }
  
  /**
   * display All Music Available in the center table.
   */
  public void displayAvailableMusics() {
    tvMusics.getItems().clear();
    tvMusics.getItems().setAll(this.retrieveAvailableMusics());

    //change the size of Tags column
    ArrayList<Double> d = new ArrayList<Double>();
    d.add(0.0);
    tvMusics.getItems().forEach(metadata -> {
      double numberOfChar = 0;
      d.set(0,7.0);
      for (String tag : metadata.getTags()) {
        numberOfChar += tag.length();
      }
      if (numberOfChar > d.get(0)) {
        d.set(0,numberOfChar);
      }
    });
    this.tagsCol.setPrefWidth(d.get(0) * 9);
  }

  private List<MusicMetadata> retrieveAvailableMusics() {
    availableMusics.clear();
    // Retrieve all musics from current user (no matter the shared status)
    // and musics from other connected users that are public or shared to him
    // and apply a filter to get only public musics
    List<Music> availableMusicsStream = this
        .getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getMusics()
        .collect(Collectors.toList());

    List<MusicMetadata> availableMusicsMetaData = new ArrayList<>();
    for (Music music : availableMusicsStream) {
      availableMusics.put(music.getHash(), music);
      availableMusicsMetaData.add(music.getMetadata());
    }

    return availableMusicsMetaData;
  }

  /**
   * Show labels for advanced search for All musics view.
   *
   * @param event the clic ont the button "Recherche avancée".
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

  @FXML
  public void changeFrameToMyMusics(ActionEvent event) {
    AllMusicsController.this.centralFrameController.setCentralContentMyMusics();
  }

  /**
   * Downwload the selected musics.
   *
   * @param event not used
   */
  @FXML
  public void download(ActionEvent event) {
    // Creation of the music metadata from the tableView
    MusicMetadata selectedMusicMetadata = this.tvMusics
        .getSelectionModel()
        .getSelectedItem();
    Music selectedMusic = availableMusics.get(selectedMusicMetadata.getHash());
    this.centralFrameController
        .getMainController()
        .getDownloadController()
        .download(selectedMusic);
  }

  /**
   * handle the click on the TableView.
   * @param click the MouseEvent
   */
  @FXML
  public void handleClickTableView(MouseEvent click) {
    MusicMetadata music = tvMusics.getSelectionModel().getSelectedItem();

    // If left click, show current music info at right of the screen
    if (click.getButton().equals(MouseButton.PRIMARY)) {
      if (music != null) {
        musicSelected = availableMusics.get(music.getHash());

        this.getCentralFrameController().getMainController()
            .getCurrentMusicInfoController().init(musicSelected);
        if (musicSelected instanceof LocalMusic) {
          btnDownload.setDisable(true);
        } else {
          btnDownload.setDisable(false);
        }
      }
    }

    // If right click, show context menu
    if (click.getButton().equals(MouseButton.SECONDARY)) {
      if (music != null) {
        isOnlyLocalMusicSelected = true;
        musicSelected = availableMusics.get(music.getHash());
        constructContextMenu();
        contextMenu.show(tvMusics.getScene().getWindow(), click.getScreenX(), click.getScreenY());
      }
    }
  }

  /**
   * Search all musics that correspond to the labels content.
   *
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
        //on supprime les espaces avant et après la virgule,
        // mais pas ceux contenus dans les tags
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

    Stream<Music> searchResults = AllMusicsController.this.getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm().searchMusics(query);

    updateMusics(searchResults);
  }

  private void updateMusics(Stream<Music> newMusics) {
    tvMusics.getItems().setAll(newMusics.map(Music::getMetadata).collect(Collectors.toList()));
  }
}