package controllers;

//replace by javadocs
//popup view when click on Comment Button on CurrentMusicInfoController
public class NewCommentController implements Controller {

  private CurrentMusicInfoController currentMusicController;
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public CurrentMusicInfoController getCurrentMusicController() {
    return currentMusicController;
  }

  public void setCurrentMusicController(CurrentMusicInfoController currentMusicController) {
    this.currentMusicController = currentMusicController;
  }
}
