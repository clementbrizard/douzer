package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


//replace by javadocs
//view in the top left of mainView
public class UserInfoController implements Controller {

  private MainController mainController;

  @FXML
  private Label lblUserPseudo;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    System.out.println("Initialize");
  }
  
  public MainController getMainController() {
    return mainController;
  }
  
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void init() {
    System.out.println("Init");
    lblUserPseudo.setText(
        this.mainController.getIhmCore().getDataForIhm().getCurrentUser().getUsername());
  }

}
