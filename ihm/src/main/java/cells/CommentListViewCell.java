package cells;

import datamodel.Comment;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.User;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CommentListViewCell extends ListCell<Comment> {

  @FXML
  TextArea comment;

  @FXML
  private AnchorPane anchorPane;

  @FXML
  private Label labelOwner;
  
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
  

  private FXMLLoader mlLoader;
  
  private Music music;
  
  private boolean ifUserNote = false;
  
  @FXML
  public void userClicked() {
    LogManager.getLogger().info("click on label user : " + labelOwner.getText());
  }


  @Override
  protected void updateItem(Comment comment,boolean empty) {
    super.updateItem(comment, empty);

    if (empty || comment == null || comment.getComment().trim().equals("")) {

      setText(null);
      setGraphic(null);

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
      
      music.getMetadata().getRatings().forEach( (user,note) -> {
        if (user.equals(comment.getOwner())) {
          ifUserNote = true;
          setStars(note);
        }
      });
      
      if (!ifUserNote) {
        setStars(0);
      }
      
      this.labelOwner.setText(comment.getOwner().getUsername());
      
      this.comment.setText(comment.getComment());
      this.comment.setEditable(false);
      this.comment.resize(getWidth(), getHeight());

      setText(null);
      setGraphic(anchorPane);
    }
  }
  
  public void setMusic(Music music) {
    this.music = music;
  }
  
  public void setStars(int rating) {
    if (rating == 0 ) {
      imageEtoile1.setVisible(false);
      imageEtoile2.setVisible(false);
      imageEtoile3.setVisible(false);
      imageEtoile4.setVisible(false);
      imageEtoile5.setVisible(false);
    }
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
  
}
