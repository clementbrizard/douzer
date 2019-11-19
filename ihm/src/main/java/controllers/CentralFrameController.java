package controllers;

import javafx.fxml.FXML;

//replace by javadocs
//the central view who will change often between centralview
public class CentralFrameController implements Controller {

  @FXML
  private AllMusicsController allMusicsController;

  private MyMusicsController myMusicsController;
  private DistantUserController distantUserController;
  private ProfileEditController profileEditController;
  private DetailsMusicController detailsMusicController;
  private MainController mainController;

  @Override
  public void initialize() {
  }

  // Getters

  public AllMusicsController getAllMusicsController() {
    return allMusicsController;
  }

  public MyMusicsController getMyMusicsController() {
    return myMusicsController;
  }

  public DistantUserController getDistantUserController() {
    return distantUserController;
  }

  public ProfileEditController getProfileEditController() {
    return profileEditController;
  }

  public DetailsMusicController getDetailsMusicController() {
    return this.detailsMusicController;
  }

  public MainController getMainController() {
    return this.mainController;
  }

  // Setters

  public void setAllMusicsController(AllMusicsController allMusicsController) {
    this.allMusicsController = allMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  public void setDistantUserController(DistantUserController distantUserController) {
    this.distantUserController = distantUserController;
  }

  public void setProfileEditController(ProfileEditController profileEditController) {
    this.profileEditController = profileEditController;
  }

  public void setDetailsMusicController(DetailsMusicController detailsMusicController) {
    this.detailsMusicController = detailsMusicController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  // Other methods

  /**
   * Initialize the controllers inside the central frame.
   */
  public void init() {
    //TODO add controllers when they are linked to XML
    this.allMusicsController.setCentralFrameController(this);
    this.allMusicsController.setApplication(this.mainController.getApplication());
    this.allMusicsController.init();
  }

}
