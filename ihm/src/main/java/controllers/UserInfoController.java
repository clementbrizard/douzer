package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


//replace by javadocs
//view in the top left of mainView
public class UserInfoController implements Controller {
  @FXML
  private Label lblUserPseudo;

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
  }

}
