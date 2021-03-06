package cells;

import core.IhmAlert;

import datamodel.Comment;
import datamodel.Music;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommentListViewCell extends ListCell<Comment> {
  private static final Logger commentLogger = LogManager.getLogger();

  @FXML
  Label comment;

  @FXML
  private AnchorPane anchorPane;

  @FXML
  private Label labelOwner;
  
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

  private FXMLLoader mlLoader;
  
  private Music music;
  
  private boolean ifUserNote = false;
  
  @FXML
  public void userClicked() {
    commentLogger.info("click on label user : " + labelOwner.getText());
  }
  
  public void setMusic(Music music) {
    this.music = music;
  }
  
  /**
   * set the amount of stars link to the rating if 0 don't show any stars.
   * @param rating int the rating of music
   */
  public void setStars(int rating) {
    // Fill stars map.
    starsMap.put(1, starOne);
    starsMap.put(2, starTwo);
    starsMap.put(3, starThree);
    starsMap.put(4, starFour);
    starsMap.put(5, starFive);

    if (rating == 0) {
      starsMap.forEach((k, v) -> {
        v.setVisible(true);
      });

    } else {    
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
        commentLogger.error(e);
      } catch (IOException e) {
        commentLogger.error(e);
        IhmAlert.showAlert("Chargement Image", "Erreur : Chargement Des Etoiles", "critical");
      }
    }
  }

  @Override
  protected void updateItem(Comment comment,boolean empty) {
    super.updateItem(comment, empty);

    if (empty || comment == null || comment.getComment().trim().equals("")) {
      setText(null);

      Platform.runLater(() -> {
        setGraphic(null);
      });

    } else {
      if (mlLoader == null) {
        mlLoader = new FXMLLoader(getClass().getResource("/fxml/CommentCustomCell.fxml"));
        mlLoader.setController(this);

        try {
          mlLoader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      Integer rating = music.getMetadata().getRatings().get(comment.getOwner());

      if (rating != null) {
        setStars(rating);
      } else {
        setStars(0);
      }

      this.labelOwner.setText(comment.getOwner().getUsername());

      this.comment.setText(comment.getComment());
      //this.comment.setEditable(false);
      this.comment.resize(getWidth(), getHeight());

      setText(null);
      Platform.runLater(() -> {
        setGraphic(anchorPane);
      });
    }
  }
}
