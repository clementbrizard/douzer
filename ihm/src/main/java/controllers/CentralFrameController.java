package controllers;

import javax.xml.soap.Detail;

//replace by javadocs
//the central view who will change often between centralview
public class CentralFrameController implements Controller {

  private AllMusicsController allMusicsController;
  private MyMusicsController myMusicsController;
  private DistantUserController distantUserController;
  private ProfileEditController profileEditController;
  private DetailsMusicController detailsMusicController;
  
  private MainController mainController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public AllMusicsController getAllMusicsController() {
    return allMusicsController;
  }

  public void setAllMusicsController(AllMusicsController allMusicsController) {
    this.allMusicsController = allMusicsController;
  }
  
  public MyMusicsController getMyMusicsController() {
    return myMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  public DistantUserController getDistantUserController() {
    return distantUserController;
  }

  public void setDistantUserController(DistantUserController distantUserController) {
    this.distantUserController = distantUserController;
  }
  
  public ProfileEditController getprofileEditController() {
    return profileEditController;
  }

  public void setprofileEditController(ProfileEditController profileEditController) {
    this.profileEditController = profileEditController;
  }
  
  public DetailsMusicController getDetailsMusicController() {
    return this.detailsMusicController;
  }
  
  public void setdetailsMusicController(DetailsMusicController detailsMusicController) {
    this.detailsMusicController = detailsMusicController;
  }
  
  public MainController getMainController() {
    return this.mainController;
  }
  
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  /**
   * Initializes the controllers inside of the central frame.
   */
  public void init() {
    this.allMusicsController = new AllMusicsController();
    this.allMusicsController.setCentralFrameController(this);

    this.detailsMusicController = new DetailsMusicController();
    this.detailsMusicController.setCentralFrameController(this);

    this.distantUserController = new DistantUserController();
    this.distantUserController.setCentralFrameController(this);

    this.myMusicsController = new MyMusicsController();
    this.myMusicsController.setCentralFrameController(this);

    this.profileEditController = new ProfileEditController();
    this.profileEditController.setCentralFrameController(this);
  }
}
