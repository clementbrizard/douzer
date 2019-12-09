package controllers;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.User;

import java.io.File;
import java.time.LocalDate;
import java.util.Iterator;

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




public class NewCommentController implements Controller {

  private Music music;

  private CommentsController commentsController;

  @FXML
  private Button validation;

  @FXML
  private TextArea textAreaComment;

  @FXML
  private ImageView imageEtoile1;

  @FXML
  private ImageView imageEtoile2;

  @FXML
  private ImageView imageEtoile3;

  @FXML
  private ImageView imageEtoile4;

  @FXML
  private ImageView imageEtoile5;

  private int note = 0;


  public CommentsController getCommentsController() {
    return commentsController;
  }

  public void setCommentsController(CommentsController commentsController) {
    this.commentsController = commentsController;
  }

  public void init(Music music) {
    this.music = music;
    this.music.getMetadata().getRatings().forEach((user,note) -> {
      if(user.equals(getCommentsController()
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
    if (rating == 1) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 2) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 3) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 4) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 5) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
    }
  }

  /**
   * Function changes the rating and the stars display when star is clicked.
   */
  @Override
  public void initialize() {
    imageEtoile1.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 1");
        setStars(1);
        note = 1;
      }
    }));

    imageEtoile2.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 2");
        setStars(2);
        note = 2;
      }
    }));

    imageEtoile3.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 3");
        setStars(3);
        note = 3;
      }
    }));

    imageEtoile4.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 4");
        setStars(4);
        note = 4;
      }
    }));

    imageEtoile5.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 5");
        setStars(5);
        note = 5;
      }
    }));

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
    
    if (note > 0) {
      LocalUser userlocal = getCommentsController()
          .getCurrentMusicInfoController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .getCurrentUser();
      
      if (music.getMetadata().getRatings().get(userlocal) == null) {
        music.getMetadata().getRatings().put(userlocal, note);
      } else {
        music.getMetadata().getRatings().replace(userlocal, note);
      }
    }
    
    //reload the view of comment
    this.getCommentsController().init(music);
    
    //close the popup
    ((Stage) this.validation.getScene().getWindow()).close();
  }



}
