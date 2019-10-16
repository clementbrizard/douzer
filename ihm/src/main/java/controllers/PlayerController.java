package controllers;

//replace by javadocs
//the view of the music player down in the middle
public class PlayerController implements Controller {
  
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
