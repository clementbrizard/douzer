package controllers;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NewCommentController implements Controller {
  private static final Logger newCommentLogger = LogManager.getLogger();

  private Music music;

  private CommentsController commentsController;

  @FXML
  private Button validation;

  @FXML
  private TextArea textAreaComment;

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

  // Map of rating stars to access them dynamically.
  private Map<Integer, ImageView> starsMap = new HashMap<Integer, ImageView>();

  private int rate = 0;


  public CommentsController getCommentsController() {
    return commentsController;
  }

  public void setCommentsController(CommentsController commentsController) {
    this.commentsController = commentsController;
  }

  /**
   * init the new comment view with a music.
   * @param music the music whose we want to add our comment
   */
  public void init(Music music) {
    this.music = music;
    this.music.getMetadata().getRatings().forEach((user,note) -> {
      if (user.equals(getCommentsController()
          .getCurrentMusicInfoController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .getCurrentUser())) {
        setStars(note);
      }
    });
  }

  /**
   * Sets the stars to the value set by user.
   * @param rating the rating of the current user
   */
  public void setStars(int rating) {
    File fullStarFile = new File(getClass().getResource("/images/FullStarSymbol.png").getFile());
    File emptyStarFile = new File(getClass().getResource("/images/EmptyStarSymbol.png").getFile());

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
      newCommentLogger.error(e);
    }
  }

  /**
   * Function changes the rating and the stars display when star is clicked.
   */
  @Override
  public void initialize() {
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
          rate = k;
        }
      }));
    });

  }

  private boolean check() {
    boolean valide = true;
    if (textAreaComment == null
        || textAreaComment.getText() == null
        || textAreaComment.getText().trim().equals("")) {
      valide = false;
    }

    return valide;
  }

  @FXML
  private void comment(ActionEvent event) {
    if (!check()) {
      return;
    }
    
    //add comment
    this.getCommentsController()
    .getCurrentMusicInfoController()
    .getApplication()
    .getIhmCore()
    .getDataForIhm().addComment(music, textAreaComment.getText());
    
    if (rate > 0) {
      LocalUser userlocal = getCommentsController()
          .getCurrentMusicInfoController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .getCurrentUser();
      
      if (music.getMetadata().getRatings().get(userlocal) == null) {
        music.getMetadata().getRatings().put(userlocal, rate);
      } else {
        music.getMetadata().getRatings().replace(userlocal, rate);
      }
    }
    
    //reload the view of comment
    this.getCommentsController().init(music);
    
    //close the popup
    ((Stage) this.validation.getScene().getWindow()).close();
  }



}
