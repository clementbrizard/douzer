package cells;

import java.io.IOException;

import datamodel.Comment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class CommentListViewCell extends ListCell<Comment> {
  
  @FXML
  TextArea comment;
  
  @FXML
  private AnchorPane anchorPane;
  
  @FXML
  private Label labelOwner;
  
  private FXMLLoader mLLoader;
  
  @FXML
  public void userClicked() {
    System.out.println("click on label user : " + labelOwner.getText());
    
  }
  
  
  @Override
  protected void updateItem(Comment comment,boolean empty) {
    super.updateItem(comment, empty);
    
    if(empty || comment == null || comment.getComment().trim().equals("")) {

      setText(null);
      setGraphic(null);

    } else {
      if (mLLoader == null) {
          mLLoader = new FXMLLoader(getClass().getResource("/fxml/CommentCustomCell.fxml"));
          mLLoader.setController(this);

          try {
              mLLoader.load();
          } catch (IOException e) {
              e.printStackTrace();
          }

      }
      this.labelOwner.setText(comment.getOwner().getUsername());
      this.comment.setText(comment.getComment());
      this.comment.setEditable(false);
      this.comment.resize(getWidth(), getHeight());
      
      setText(null);
      setGraphic(anchorPane);
    }
  }
}
