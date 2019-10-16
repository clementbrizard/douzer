package controllers;

//replace by javadoc
//right panel with light info and comment about the music
public class CurrentMusicInfoController implements Controller {

  private ShareController shareController;
  private NewCommentController newCommentController;
  private MainController mainController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public ShareController getShareController() {
    return shareController;
  }
  
  public void setShareController(ShareController shareController) {
    this.shareController = shareController;
  }
  
  public NewCommentController getNewCommentController() {
    return newCommentController;
  }

  public void setNewCommentController(NewCommentController newCommentController) {
    this.newCommentController = newCommentController;
  }

  public MainController getMainController() {
    return mainController;
  }
  
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }
}
