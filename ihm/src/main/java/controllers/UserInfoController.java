package controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;


//replace by javadocs
//view in the top left of mainView
public class UserInfoController implements Controller {

  private MainController mainController;

  @FXML
  private Label lblUserPseudo;

  @Override
  public void initialize() { }

  public MainController getMainController() {
    return mainController;
  }
  
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void init() {
    String name = "";
    if (this.mainController.getApplication().getIhmCore().getDataForIhm().getCurrentUser() == null) {
      name = "Booba";
    } else {
      name = this.mainController.getApplication().getIhmCore().getDataForIhm().getCurrentUser().getUsername();
    }
    lblUserPseudo.setText(
        name
    );
  }

}
