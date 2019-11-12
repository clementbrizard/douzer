package controllers;

import javafx.fxml.FXML;

import java.awt.*;

//replace by javadocs
//view in the top left of mainView
public class UserInfoController implements Controller {

  private MainController mainController;
  @FXML
  private Label lblUsername;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public MainController getMainController() {
    return mainController;
  }
  
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void init(){

    lblUsername.setText(mainController.getIhmCore().getDataForIhm().getCurrentUser().getUsername());
    
  }

}
