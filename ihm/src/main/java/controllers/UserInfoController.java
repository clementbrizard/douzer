package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


/**
 * Controller used for managing the top left view 
 * with the nickname and the buttons to modify profile and disconnect.
 */
public class UserInfoController implements Controller {
  @FXML
  private Label lblUserPseudo;
  @FXML
  private Button btnModifyProfile;
  private MainController mainController;

  // Getters

  public MainController getMainController() {
    return mainController;
  }

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;

  }

  // Other methods

  @Override
  public void initialize() { }

  /**
   * Initialize the field with default data.
   */
  public void init() {
    String name = "";
    if (this.mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getCurrentUser() == null) {
      name = "Booba";
    } else {
      name = this.mainController
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .getCurrentUser()
          .getUsername();
    }
    lblUserPseudo.setText(
        name
    );

    this.btnModifyProfile.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        UserInfoController.this.mainController.getCentralFrameController()
            .setCentralContentProfileEdit();
      }
    });
  }

}
