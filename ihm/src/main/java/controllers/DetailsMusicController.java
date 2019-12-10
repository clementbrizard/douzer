package controllers;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
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
  private TextField textFieldTitre;

  @FXML
  private TextField textFieldArtiste;

  @FXML
  private TextField textFieldAlbum;

  @FXML
  private Spinner<Integer> dateYear;

  @FXML
  private TextField textFieldLastUploader;

  @FXML
  private TextField textFieldAddTag;

  @FXML
  private ListView<String> listViewTagsList;

  private ObservableList<String> tags;

  private LocalMusic localMusic;

  @FXML
  private Button buttonValider;

  @FXML
  private Button buttonAddTag;

  @FXML
  private ImageView starOne;

  @FXML
  private ImageView startTwo;

  @FXML
  private ImageView starThree;

  @FXML
  private ImageView startFour;

  @FXML
  private ImageView starFive;

  private MyMusicsController myMusicsController;

  private int rate = 0;

  // Map of rating stars to access then dynamically.
  Map<Integer, ImageView> starsMap = new HashMap<Integer, ImageView>();

  public MyMusicsController getMyMusicsController() {
    return this.myMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  @Override
  public void initialize() {
    // Fill stars map.
    starsMap.put(1, starOne);
    starsMap.put(2, startTwo);
    starsMap.put(3, starThree);
    starsMap.put(4, startFour);
    starsMap.put(5, starFive);

    // Set event handler for each star.
    starsMap.forEach((k, v) -> {
      v.setOnMousePressed((new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
          setStars(k);
          rate = k;
        }
      }));
    });

    buttonAddTag.setOnMousePressed((new EventHandler<MouseEvent>() {

      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur add tag");
        boolean tagexist = false;
        if (localMusic.getMetadata() != null) {
          if (localMusic.getMetadata().getTags() != null) {

            Iterator<String> itMusicTag = localMusic.getMetadata().getTags().iterator();
            while (itMusicTag.hasNext()) {
              if (itMusicTag.next().trim().equals(textFieldAddTag.getText().trim())) {
                tagexist = true;
              }
            }
          }
        }
        if (!tagexist && !textFieldAddTag.getText().trim().equals("")) {
          tags.add(textFieldAddTag.getText().trim());
        }
        //give new tag or new tag list to data
      }
    }));
  }

  /**
   * After the initialisation with initialize() of the controller execute this function with
   * the localMusic where the user has click .
   * @param localMusic the music clicked
   */
  public void initMusic(LocalMusic localMusic) {
    this.localMusic = localMusic;

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getTitle() != null) {
        textFieldTitre.setText(localMusic.getMetadata().getTitle());
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getArtist() != null) {
        textFieldArtiste.setText(localMusic.getMetadata().getArtist());
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getAlbum() != null) {
        textFieldAlbum.setText(localMusic.getMetadata().getAlbum());
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getReleaseYear() != null) {
        dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
            1000, LocalDate.now().getYear(), localMusic.getMetadata()
            .getReleaseYear().getValue(), 1));
      }
    }

    if (localMusic.getOwners() != null) {
      Iterator<User> itOwners = localMusic.getOwners().iterator();
      if (itOwners.hasNext()) {
        textFieldLastUploader.setText(itOwners.next().getUsername());
      }
    }

    tags = FXCollections.observableArrayList();

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getTags() != null) {
        Iterator<String> itMusicTag = localMusic.getMetadata().getTags().iterator();
        while (itMusicTag.hasNext()) {
          tags.add(itMusicTag.next());
        }

        listViewTagsList.setItems(tags);
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getRatings() != null) {
        LocalUser userlocal = getMyMusicsController()
            .getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser();

        Integer rating;
        if ((rating = localMusic.getMetadata().getRatings().get(userlocal)) != null) {
          setStars(rating.intValue());
        }
      }
    }
    LogManager.getLogger().info("Fin d'Initialisation de DetailsMusicController");
  }

  /**
   * Sets the stars to the value set by user.
   * @param rating the rating of the current user
   */

  public void setStars(int rating) {
    File fullStarFile = new File("ihm/src/main/resources/images/FullStarSymbol.png");
    File emptyStarFile = new File("ihm/src/main/resources/images/EmptyStarSymbol.png");

    try {
      InputStream fullStarInputStream = new FileInputStream(fullStarFile.getAbsolutePath());
      InputStream emptyStarInputStream = new FileInputStream(emptyStarFile.getAbsolutePath());
      Image fullStarImage = new Image(fullStarInputStream);
      Image emptyStarImage = new Image(emptyStarInputStream);

      for (int i = 1; i <= rating; i++) {
        starsMap.get(i).setImage(fullStarImage);
      }

      for (int i = rating + 1; i <= 5; i++) {
        starsMap.get(i).setImage(emptyStarImage);
      }
    } catch (FileNotFoundException e) {
      detailsMusicLogger.error(e);
    }
  }

  /**
   * Function checks that the fields are not null.
   * @return Boolean, false if there is a nul field.
   */
  public Boolean checkFields() {
    Boolean bool = true;
    if (textFieldTitre.getText() == null || textFieldTitre.getText().trim().equals("")) {
      bool = false;
      textFieldTitre.setStyle(" -fx-background-color:red;");
    } else {
      textFieldTitre.setStyle(" -fx-background-color:white;");
    }
    if (textFieldArtiste.getText() == null || textFieldArtiste.getText().trim().equals("")) {
      bool = false;
      textFieldArtiste.setStyle(" -fx-background-color:red;");
    } else {
      textFieldArtiste.setStyle(" -fx-background-color:white;");
    }
    if (textFieldAlbum.getText() == null || textFieldAlbum.getText().trim().equals("")) {
      bool = false;
      textFieldAlbum.setStyle(" -fx-background-color:red;");
    } else {
      textFieldAlbum.setStyle(" -fx-background-color:white;");
    }
    //if (textFieldAnnee.getText() == null || textFieldAnnee.getText().trim().equals("")) {
    //  bool = false;
    //  textFieldAnnee.setStyle(" -fx-background-color:red;");
    //}
    if (textFieldLastUploader.getText() == null
        || textFieldLastUploader.getText().trim().equals("")) {
      bool = false;
      textFieldLastUploader.setStyle(" -fx-background-color:red;");
    } else {
      textFieldLastUploader.setStyle(" -fx-background-color:white;");
    }


    return bool;
  }

  /**
   * Function updates the value of modified fields.
   * @param action button Validation is clicked.
   */
  public void validation(ActionEvent action) {

    if (!checkFields()) {
      return;
    }
    localMusic.getMetadata().setTitle(textFieldTitre.getText());
    localMusic.getMetadata().setAlbum(textFieldAlbum.getText());
    localMusic.getMetadata().setArtist(textFieldArtiste.getText());

    this.getMyMusicsController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .notifyMusicUpdate(localMusic);

    this.getMyMusicsController().displayAvailableMusics();

    if (note > 0) {
      try {
        this.getMyMusicsController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .rateMusic(localMusic, note);
      } catch (UnsupportedOperationException e) {
        LogManager.getLogger().error(e.getMessage());
        
        LocalUser userlocal = getMyMusicsController()
            .getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser();
        
        if (localMusic.getMetadata().getRatings().get(userlocal) == null) {
          localMusic.getMetadata().getRatings().put(userlocal, note);
        } else {
          localMusic.getMetadata().getRatings().replace(userlocal, note);
        }
        
      }
    }

    /*if the same music is show in the comment view,
      update the comment view in order to have the same stars*/
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

    localMusic.getMetadata().getTags().addAll(tags);

    LogManager.getLogger().info("Change Field TODO with Data function if exist");

    ((Stage) this.textFieldTitre.getScene().getWindow()).close();
  }

}
