package controllers;

//replace by javadocs 
//the central view who will change often between centralview
public class CentralFrameController implements Controller {
  
  private AllMusicsController allMusicsController;
  private MyMusicsController myMusicsController;
  private DistantUserController distantUserController;
  private ProfileEditController profilEditcontroller;
  private DetailsMusicController detailMusicController;
  
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
  
  public ProfileEditController getProfilEditcontroller() {
    return profilEditcontroller;
  }

  public void setProfilEditcontroller(ProfileEditController profilEditcontroller) {
    this.profilEditcontroller = profilEditcontroller;
  }
  
  public DetailsMusicController getDetailMusicController() {
    return detailMusicController;
  }
  
  public void setDetailMusicController(DetailsMusicController detailMusicController) {
    this.detailMusicController = detailMusicController;
  }
  
  public MainController getMainController() {
    return mainController;
  }
  
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }
}
