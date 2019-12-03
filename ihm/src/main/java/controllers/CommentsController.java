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

//replace by javadocs
//popup view when click on Comment Button on CurrentMusicInfoController
public class CommentsController implements Controller {

  private Scene newCommentLoader;

  private CurrentMusicInfoController currentMusicInfoController;
  private NewCommentController newCommentController;

  private Music music;

  @FXML
  private Button commentButton;

  @FXML
  private Label titleMusic;

  @FXML
  private ListView<Comment> listCommentaire;

  private ObservableList<Comment> commentObservableList;

  public CurrentMusicInfoController getCurrentMusicController() {
    return currentMusicInfoController;
  }

  public void setCurrentMusicController(CurrentMusicInfoController currentMusicController) {
    this.currentMusicInfoController = currentMusicController;
  }

  public NewCommentController getNewCommentController() {
    return newCommentController;
  }

  public void setNewCommentController(NewCommentController newCommentController) {
    this.newCommentController = newCommentController;
  }


  /**
   * This function displays the music's comments.
   * @param music Object Music.
   */
  public void init(Music music) {
    this.music = music;
    titleMusic.setText(music.getMetadata().getTitle());
    commentObservableList = FXCollections.observableArrayList();
    commentObservableList.addAll(music.getMetadata().getComments());

    commentObservableList.addAll(
        new Comment("test1",this.getCurrentMusicController().getApplication().getIhmCore()
            .getDataForIhm().getCurrentUser()),new Comment("2",this.getCurrentMusicController()
            .getApplication().getIhmCore().getDataForIhm().getCurrentUser()));

    listCommentaire.setItems(commentObservableList);
    listCommentaire.setCellFactory(new Callback<ListView<Comment>, ListCell<Comment>>() {

      @Override
      public ListCell<Comment> call(ListView<Comment> param) {
        // TODO Auto-generated method stub
        return new CommentListViewCell();
      }

    });

  }

  /**
   * we show the CommentPopupView for the Current LocalMusic.
   */
  @FXML
  public void commentClick(ActionEvent event) {
    if (this.music == null) {
      return;
    }
    try {
      // Initialize shareScene and shareController
      FXMLLoader newComment = new FXMLLoader(getClass().getResource("/fxml/CommentPopupView.fxml"));
      Parent newCommentParent = newComment.load();
      newCommentLoader = new Scene(newCommentParent);

      NewCommentController newCommentController = newComment.getController();
      this.setNewCommentController(newCommentController);
      newCommentController.setCommentsController(this);
      newCommentController.init(this.music);

    } catch (Exception e) {
      e.printStackTrace();
    }

    Stage newCommentPopup = new Stage();
    newCommentPopup.setTitle("Commentaire");
    newCommentPopup.setScene(this.newCommentLoader);

    // Set position of second window, relatively to primary window.
    //musicSharingPopup.setX(application.getPrimaryStage().getX() + 200);
    //musicSharingPopup.setY(application.getPrimaryStage().getY() + 100);


    // Show sharing popup.
    newCommentPopup.initModality(Modality.APPLICATION_MODAL);
    newCommentPopup.showAndWait();
    //newCommentPopup.show();
  }

  @Override
  public void initialize() {

  }


}