package controllers;

//replace by javadocs
//the down left view in the main view
public class ContactListController implements Controller {

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
