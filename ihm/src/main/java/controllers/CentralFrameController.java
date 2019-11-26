package controllers;

import javafx.fxml.FXML;


/**
 * the central view who will change often with each subView and their Controllers.
 * @see MyMusicsController
 * @see DistantUserController
 * @see ProfileEditController
 * @see DetailsMusicController
 * @see AllMusicsController
 */
public class CentralFrameController implements Controller {

  @FXML
  private AllMusicsController allMusicsController;

  private MyMusicsController myMusicsController;
  private DistantUserController distantUserController;
  private ProfileEditController profileEditController;
  private DetailsMusicController detailsMusicController;
  private MainController mainController;


  // Getters

  /**
   * getter of allMusicsController the controller of the sub-view AllMusicsCenterView.
   * @return AllMusicsController
   * @see AllMusicsController
   */
  public AllMusicsController getAllMusicsController() {
    return allMusicsController;
  }

  /**
   * getter of myMusicsController the controller of the sub-view MyMusicsCenterView.
   * @return MyMusicsController
   * @see MyMusicsController
   */
  public MyMusicsController getMyMusicsController() {
    return myMusicsController;
  }

  /**
   * getter of distantUserController the controller of the sub-view OnlineUsersListView.
   * @return DistantUserController
   * @see DistantUserController
   */
  public DistantUserController getDistantUserController() {
    return distantUserController;
  }

  /**
   * getter of profileEditController the controller of the sub-view ProfileEditView.
   * @return ProfileEditController
   * @see ProfileEditController
   */
  public ProfileEditController getProfileEditController() {
    return profileEditController;
  }

  /**
   * getter of detailsMusicController the controller of the sub-view DetailsMusicView.
   * @return DetailsMusicController
   * @see DetailsMusicController
   */
  public DetailsMusicController getDetailsMusicController() {
    return detailsMusicController;
  }

  /**
   * getter of mainController the controller of the super-view MainView.
   * @return MainController
   * @see MainController
   */
  public MainController getMainController() {
    return mainController;
  }

  // Setters

  /**
   * setter of allMusicsController the controller of the sub-view AllMusicsCenterView.
   * @param allMusicsController the new AllMusicsController
   * @see AllMusicsController
   */
  public void setAllMusicsController(AllMusicsController allMusicsController) {
    this.allMusicsController = allMusicsController;
  }

  /**
   * setter of myMusicsController the controller of the sub-view MyMusicsCenterView.
   * @param myMusicsController the new MyMusicsController
   * @see MyMusicsController
   */
  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  /**
   * setter of distantUserController the controller of the sub-view OnlineUsersListView.
   * @param distantUserController the new DistantUserController
   * @see DistantUserController
   */
  public void setDistantUserController(DistantUserController distantUserController) {
    this.distantUserController = distantUserController;
  }

  /**
   * setter of allMusicsController the controller of the sub-view AllMusicsCenterView.
   * @param profileEditController the new ProfileEditController
   * @see ProfileEditController
   */
  public void setProfileEditController(ProfileEditController profileEditController) {
    this.profileEditController = profileEditController;
  }

  /**
   * setter of detailsMusicController the controller of the sub-view DetailsMusicView.
   * @param detailsMusicController the new DetailsMusicController
   * @see DetailsMusicController
   */
  public void setDetailsMusicController(DetailsMusicController detailsMusicController) {
    this.detailsMusicController = detailsMusicController;
  }

  /**
   * setter of mainController the controller of the super-view MainView.
   * @param mainController the new MainController
   * @see MainController
   */
  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  // Other methods
  
  @Override
  public void initialize() {
  }
  
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
