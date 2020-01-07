package controllers;

import cells.CommentListViewCell;
import datamodel.Comment;
import datamodel.LocalMusic;
import datamodel.Music;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handle display of selected musics' comments
 * and pop up of comment form for selected music.
 */
public class CommentsController implements Controller {
  private static final Logger commentsLogger = LogManager.getLogger();

  @FXML
  private Button commentButton;

  @FXML
  private Label titleMusic;

  @FXML
  private ListView<Comment> listComment;

  private Scene newCommentLoader;

  private Music music;
  private ObservableList<Comment> commentObservableList;

  private CurrentMusicInfoController currentMusicInfoController;
  private NewCommentController newCommentController;

  public CurrentMusicInfoController getCurrentMusicInfoController() {
    return currentMusicInfoController;
  }

  public NewCommentController getNewCommentController() {
    return newCommentController;
  }

  public void setCurrentMusicController(CurrentMusicInfoController currentMusicController) {
    this.currentMusicInfoController = currentMusicController;
  }

  public void setNewCommentController(NewCommentController newCommentController) {
    this.newCommentController = newCommentController;
  }
  
  public Music getMusic() {
    return music;
  }

  /**
   * This function displays the music's comments.
   * @param music Object Music.
   */
  public void init(Music music) {
    listComment.setVisible(true);
    this.music = music;
    if (music == null) {
      return;
    }
    titleMusic.setText(music.getMetadata().getTitle());
    if (commentObservableList == null) {
      commentObservableList = FXCollections.observableArrayList();
    } else {
      commentObservableList.clear();
    }

    commentObservableList.addAll(music.getMetadata().getComments());
    listComment.setItems(commentObservableList);
    
    listComment.setCellFactory(new Callback<ListView<Comment>, ListCell<Comment>>() {
      @Override
      public ListCell<Comment> call(ListView<Comment> param) {
        CommentListViewCell c = new CommentListViewCell();
        c.setMusic(music);

        //controller for each comment
        return c;
      }
    });
    
    if (!(music instanceof LocalMusic)) {
      commentButton.setDisable(true);
    }
  }

  /**
   * We show the CommentPopupView for the Current LocalMusic.
   */
  @FXML
  public void commentClick(ActionEvent event) {
    if (this.music == null) {
      return;
    }

    try {
      //Initialize scene and controller
      FXMLLoader newComment = new FXMLLoader(getClass().getResource("/fxml/CommentPopupView.fxml"));
      Parent newCommentParent = newComment.load();
      newCommentLoader = new Scene(newCommentParent);

      NewCommentController newCommentController = newComment.getController();
      this.setNewCommentController(newCommentController);
      newCommentController.setCommentsController(this);
      newCommentController.init(this.music);

    } catch (Exception e) {
      commentsLogger.debug(e);
    }

    Stage newCommentPopup = new Stage();
    newCommentPopup.setTitle("Commentaire");
    newCommentPopup.setScene(this.newCommentLoader);

    // Show popup.
    newCommentPopup.initModality(Modality.APPLICATION_MODAL);
    newCommentPopup.showAndWait();
  }

  @Override
  public void initialize() {
    if (this.music == null) {
      listComment.setVisible(false);
    }
  }

}
