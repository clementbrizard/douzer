package controllers;

//replace by javadocs
//view in the top left of mainView
public class UserInfoController implements Controller {

  private MainController mainController;
  
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
}
