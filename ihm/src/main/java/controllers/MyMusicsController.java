package controllers;

import com.sun.javafx.logging.Logger;

import core.Application;
import datamodel.LocalMusic;
import datamodel.MusicMetadata;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

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
  private DetailsMusicController detailsMusicController;

  private CentralFrameController centralFrameController;
  private Scene addMusicScene;
  private Scene infoMusicScene;
  private Application application;

  private LocalMusic musicInformation;
  private ArrayList<LocalMusic> listMusics;

  private Logger logger;

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

    try {
      this.displayAvailableMusics();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }


    // Create ContextMenu
    contextMenu = new ContextMenu();

    MenuItem item1 = new MenuItem("Informations");
    item1.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {

        showMusicInformation(musicInformation);
        System.out.println("click on first element");
      }
    });

    /*MenuItem item2 = new MenuItem("Menu Item 2");
    item2.setOnAction(new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        System.out.println("Click On second Item");
      }
    });*/
    // Add MenuItem to ContextMenu
    contextMenu.getItems().addAll(item1);//, item2);

    // When user right-click on TvMusics


  }


  /**
   * Refreshes the table with getLocalMusics() from DataForIhm.
   */
  public void displayAvailableMusics() {
    tvMusics.getItems().setAll(this.parseMusic());
  }


  private List<MusicMetadata> parseMusic() {
    this.listMusics.addAll(this.getCentralFrameController().getMainController().getApplication()
            .getIhmCore().getDataForIhm().getLocalMusics().collect(Collectors.toList()));

    return this.getCentralFrameController().getMainController().getApplication()
            .getIhmCore().getDataForIhm().getLocalMusics().map(x -> x.getMetadata()).collect(Collectors.toList());
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
	      FXMLLoader musicDetailsLoader = new FXMLLoader(getClass().getResource("/fxml/MusicDetailsView.fxml"));
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

  @FXML
  public void handleClickTableView(MouseEvent click){
    MusicMetadata music = tvMusics.getSelectionModel().getSelectedItem();
    boolean doubleclicked = false;
    if(click.getButton().equals(MouseButton.PRIMARY)){
        if(click.getClickCount() == 2){
            if(music != null) {
              System.out.println("Double clicked on : " + music.getTitle());
              doubleclicked = true;
            }
        }
        if(music != null && !doubleclicked) {
          System.out.println("musique appuyé : " + music.getTitle());
        }
    }

    if(click.getButton().equals(MouseButton.SECONDARY)) {
      if(music != null) {
        for(int i = 0; i<this.listMusics.size(); i++) {
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

}
