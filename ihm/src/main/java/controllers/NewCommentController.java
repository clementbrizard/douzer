package controllers;

import core.IhmAlert;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
import org.controlsfx.control.Notifications;


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
    } catch (FileNotFoundException e) {
      newCommentLogger.error(e);
    } catch (IOException e) {
      newCommentLogger.error(e);
      IhmAlert.showAlert("Chargement Image", "Erreur : Chargement Des Etoiles", "critical");
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
      Notifications.create()
          .title("Commentaire non sauvegardée")
          .text("Le commentaire que vous avez saisi est vide.")
          .darkStyle()
          .showError();
    }

    if (rate == 0) {
      valide = false;
      Notifications.create()
          .title("Commentaire non sauvegardée")
          .text("La note est vide.")
          .darkStyle()
          .showError();
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
