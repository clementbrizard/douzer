package controllers;

//replace by javadocs
//popup view when click on share Button on shareController
public class ShareController implements Controller {
  
  private ShareController shareController;
  
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
}
