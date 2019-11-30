package cells;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class CommentListViewCell extends ListCell<String> {
  
  @FXML
  TextArea comment;
  
  @FXML
  private AnchorPane anchorPane;
  
  private FXMLLoader mLLoader;
  
  @Override
  protected void updateItem(String comment,boolean empty) {
    super.updateItem(comment, empty);
    
    if(empty || comment == null || comment.trim().equals("")) {

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

      this.comment.setText(comment);
      this.comment.setEditable(false);

      setText(null);
      setGraphic(anchorPane);
    }
  }
}
