package controllers;


/**
 * Pop-up a view when the user want to add a music from a local file
 */
public class NewMusicController implements Controller {

  private MyMusicsController myMusicsController;
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public MyMusicsController getMyMusicsController() {
    return myMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }
}
