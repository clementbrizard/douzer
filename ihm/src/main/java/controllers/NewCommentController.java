package controllers;

import datamodel.Music;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class NewCommentController implements Controller {

  private Music music;
  
  private CommentsController commentsController;
  
  @FXML
  private Button validation; 
  
  @FXML
  private TextArea textAreaComment;
  
  
  public CommentsController getCommentsController() {
    return commentsController;
  }

  public void setCommentsController(CommentsController commentsController) {
    this.commentsController = commentsController;
  }
  
  public void init(Music music) {
    this.music = music;
  }
  
  @Override
  public void initialize() {
  }
  
  private boolean check() {
    boolean valide = true;
    if(textAreaComment == null 
        || textAreaComment.getText() == null 
        || textAreaComment.getText().trim().equals("")) {
      valide = false;
    }
    
    return valide;
  }
  
  @FXML
  private void comment(ActionEvent event) {
    if(!check()) {
      return;
    }
    this.getCommentsController()
    .getCurrentMusicController()
    .getApplication()
    .getIhmCore()
    .getDataForIhm().addComment(music, textAreaComment.getText());
    ((Stage) this.validation.getScene().getWindow()).close();
  }


  
}
