package controllers;

import core.IhmAlert;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.ShareStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Central view to show all information about a music.
 */
public class DetailsMusicController implements Controller {
  private static final Logger detailsMusicLogger = LogManager.getLogger();

  @FXML
  private TextField textFieldTitle;

  @FXML
  private TextField textFieldArtist;

  @FXML
  private TextField textFieldAlbum;

  @FXML
  private Spinner<Integer> dateYear;

  @FXML
  private ListView<String> ownersListView;

  @FXML
  private TextField textFieldAddTag;

  @FXML
  private ListView<String> listViewTagsList;

  private ObservableList<String> tags;

  private Music music;

  @FXML
  private Button validateButton;

  @FXML
  private Button buttonAddTag;

  @FXML
  private Button buttonDeleteTag;

  @FXML
  private ImageView starOne;

  @FXML
  private ImageView starTwo;

  @FXML
  private ImageView starThree;

  @FXML
  private ImageView starFour;

  @FXML
  private ImageView starFive;

  @FXML
  private RadioButton radioPublic;

  @FXML
  private RadioButton radioFriends;

  @FXML
  private RadioButton radioPrivate;
  
  @FXML
  private Label noteLabel;

  private MyMusicsController myMusicsController;

  private ToggleGroup shareStatusGroup;

  private int rating = 0;

  // Map of rating stars to access then dynamically.
  private Map<Integer, ImageView> starsMap = new HashMap<Integer, ImageView>();

  public MyMusicsController getMyMusicsController() {
    return this.myMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  public Music getMusic() {
    return this.music;
  }

  @Override
  public void initialize() {
    this.shareStatusGroup = new ToggleGroup();
    this.radioPrivate.setToggleGroup(this.shareStatusGroup);
    this.radioFriends.setToggleGroup(this.shareStatusGroup);
    this.radioPublic.setToggleGroup(this.shareStatusGroup);

    // Fill stars map.
    starsMap.put(1, starOne);
    starsMap.put(2, starTwo);
    starsMap.put(3, starThree);
    starsMap.put(4, starFour);
    starsMap.put(5, starFive);

    // Set event handler for each star.
    starsMap.forEach((k, v) -> {
      v.setOnMousePressed((new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          setStars(k);
          rating = k;
        }
      }));
    });

    buttonAddTag.setOnMousePressed((new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        boolean hasTag = false;
        if (music.getMetadata() != null) {
          if (music.getMetadata().getTags() != null) {

            for (String s : music.getMetadata().getTags()) {
              if (s.trim().equals(textFieldAddTag.getText().trim())) {
                hasTag = true;
              }
            }
          }
        }
        if (!hasTag && !textFieldAddTag.getText().trim().equals("")) {
          tags.add(textFieldAddTag.getText().trim());
        }
        //give new tag or new tag list to data
        textFieldAddTag.clear();
      }
    }));

    buttonDeleteTag.setOnMousePressed((new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        String selectedItem = listViewTagsList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
          Iterator<String> it = music.getMetadata().getTags().iterator();

          while (it.hasNext()) {
            if (it.next() == selectedItem) {
              it.remove();
            }
          }

          it = tags.iterator();

          while (it.hasNext()) {
            if (it.next() == selectedItem) {
              it.remove();
            }
          }
        }

      }
    }));
  }

  /**
   * After the initialisation with initialize() of the controller execute this function with
   * the localMusic where the user has click .
   *
   * @param musicSelected the music clicked
   *                   After the initialisation of the controller, call this method with
   *                   the localMusic on which the user clicked.
   */
  public void initMusic(Music musicSelected) {
    this.music = musicSelected;
    
    if (musicSelected instanceof LocalMusic) {
      LocalMusic localMusic = (LocalMusic) musicSelected;
      
      switch (localMusic.getShareStatus()) {
        case PUBLIC:
          this.radioPublic.setSelected(true);
          break;
        case FRIENDS:
          this.radioFriends.setSelected(true);
          break;
        default:
          this.radioPrivate.setSelected(true);
          break;
      }

      if (localMusic.getMetadata() != null) {
        if (localMusic.getMetadata().getRatings() != null) {
          LocalUser localUser = getMyMusicsController()
              .getCentralFrameController()
              .getMainController()
              .getApplication()
              .getIhmCore()
              .getDataForIhm()
              .getCurrentUser();

          Integer rating;
          if ((rating = localMusic.getMetadata().getRatings().get(localUser)) != null) {
            setStars(rating);
          }
        }
      }
    } else {
      radioPublic.setDisable(true);
      radioFriends.setDisable(true);
      radioPrivate.setDisable(true);

      validateButton.setDisable(true);
      
      this.textFieldAlbum.setEditable(false);
      this.textFieldArtist.setEditable(false);
      this.textFieldTitle.setEditable(false);
      this.dateYear.setEditable(false);
      
      this.textFieldAddTag.setEditable(false);
      this.buttonAddTag.setDisable(true);
      this.starOne.setVisible(false);
      this.starTwo.setVisible(false);
      this.starThree.setVisible(false);
      this.starFour.setVisible(false);
      this.starFive.setVisible(false);
      this.noteLabel.setVisible(false);
    }

    if (musicSelected.getMetadata() != null) {
      if (musicSelected.getMetadata().getTitle() != null) {
        textFieldTitle.setText(musicSelected.getMetadata().getTitle());
      }
    }

    if (musicSelected.getMetadata() != null) {
      if (musicSelected.getMetadata().getArtist() != null) {
        textFieldArtist.setText(musicSelected.getMetadata().getArtist());
      }
    }

    if (musicSelected.getMetadata() != null) {
      if (musicSelected.getMetadata().getAlbum() != null) {
        textFieldAlbum.setText(musicSelected.getMetadata().getAlbum());
      }
    }

    if (musicSelected.getMetadata() != null) {
      if (musicSelected.getMetadata().getReleaseYear() != null) {
        if (music instanceof LocalMusic) {
          dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
              1000, LocalDate.now().getYear(), musicSelected.getMetadata()
              .getReleaseYear().getValue(), 1));
        } else {
          dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
              musicSelected.getMetadata().getReleaseYear().getValue(), 
              musicSelected.getMetadata().getReleaseYear().getValue(), 
              musicSelected.getMetadata().getReleaseYear().getValue(), 1));
        }
      }
    }

    if (music.getOwners() != null) {
      music.getOwners().forEach(owner -> ownersListView.getItems().add(owner.getUsername()));
    }

    tags = FXCollections.observableArrayList();

    if (musicSelected.getMetadata() != null) {
      if (musicSelected.getMetadata().getTags() != null) {
        tags.addAll(musicSelected.getMetadata().getTags());
        listViewTagsList.setItems(tags);
      }
    }
  }

  /**
   * Sets the stars to the value set by user.
   *
   * @param rating the rating of the current user
   */

  public void setStars(int rating) {
    URL fullStarFile = getClass().getResource("/images/FullStarSymbol.png");
    URL emptyStarFile = getClass().getResource("/images/EmptyStarSymbol.png");

    try {
      Image fullStarImage;
      Image emptyStarImage;
      fullStarImage = new Image(fullStarFile.openStream());
      emptyStarImage = new Image(emptyStarFile.openStream());



      for (int i = 1; i <= rating; i++) {
        starsMap.get(i).setImage(fullStarImage);
      }

      for (int i = rating + 1; i <= 5; i++) {
        starsMap.get(i).setImage(emptyStarImage);
      }
    } catch (Exception e) {
      IhmAlert.showAlert("Pictures Load", "Fail : picture load PLAYER", "critical");
    }
  }

  /**
   * Function checks that the fields are not null.
   *
   * @return Boolean, false if there is a nul field.
   */
  public Boolean checkFields() {
    boolean bool = true;

    if (textFieldTitle.getText() == null || textFieldTitle.getText().trim().equals("")) {
      bool = false;
      textFieldTitle.setStyle(" -fx-background-color:red;");
    } else {
      textFieldTitle.setStyle(" -fx-background-color:white;");
    }

    if (textFieldArtist.getText() == null || textFieldArtist.getText().trim().equals("")) {
      bool = false;
      textFieldArtist.setStyle(" -fx-background-color:red;");
    } else {
      textFieldArtist.setStyle(" -fx-background-color:white;");
    }

    if (textFieldAlbum.getText() == null || textFieldAlbum.getText().trim().equals("")) {
      bool = false;
      textFieldAlbum.setStyle(" -fx-background-color:red;");
    } else {
      textFieldAlbum.setStyle(" -fx-background-color:white;");
    }
    
    return bool;
  }

  /**
   * Function to update the value of modified fields.
   *
   * @param action button Validation is clicked.
   */
  public void validation(ActionEvent action) {

    if (!checkFields()) {
      return;
    }
    music.getMetadata().setTitle(textFieldTitle.getText());

    if (music instanceof LocalMusic) {
      LocalMusic localMusic = (LocalMusic) this.music;
      if (this.radioPublic.isSelected()) {
        localMusic.setShareStatus(ShareStatus.PUBLIC);
      } else if (this.radioFriends.isSelected()) {
        localMusic.setShareStatus(ShareStatus.FRIENDS);
      } else {
        localMusic.setShareStatus(ShareStatus.PRIVATE);
      }
  
      localMusic.getMetadata().setTitle(textFieldTitle.getText());
      localMusic.getMetadata().setAlbum(textFieldAlbum.getText());
      localMusic.getMetadata().setReleaseYear(Year.parse("" + dateYear.getValue()));
      localMusic.getMetadata().setArtist(textFieldArtist.getText());
  
      this.getMyMusicsController().displayAvailableMusics();
  
      if (rating > 0) {
        localMusic.getMetadata().setArtist(textFieldArtist.getText());
  
        LocalUser localUser = getMyMusicsController()
            .getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser();
  
        localMusic.getMetadata().addRating(localUser, rating);
      }
  
      localMusic.getMetadata().getTags().addAll(tags);
  
      this.getMyMusicsController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .notifyMusicUpdate(localMusic);
  
      // If the same music is shown in the comment view,
      // update the comment view in order to have the same stars.
      if (localMusic.equals(getMyMusicsController()
          .getCentralFrameController()
          .getMainController()
          .getCurrentMusicInfoController()
          .getCommentCurrentMusicController()
          .getMusic())) {
  
        getMyMusicsController()
            .getCentralFrameController()
            .getMainController()
            .getCurrentMusicInfoController()
            .getCommentCurrentMusicController().init(localMusic);
      }
  
      this.getMyMusicsController().displayAvailableMusics();
      this.getMyMusicsController().getCentralFrameController().getAllMusicsController()
          .displayAvailableMusics();
  
      ((Stage) this.textFieldTitle.getScene().getWindow()).close();
    }
  }
}
