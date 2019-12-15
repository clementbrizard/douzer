package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;



/**
 * the central view who will change often with each subView and their Controllers.
 * @see MyMusicsController
 * @see DistantUserController
 * @see ProfileEditController
 * @see DetailsMusicController
 * @see AllMusicsController
 */
public class CentralFrameController implements Controller {

  public static final String allMusicsView = "/fxml/AllMusicsCenterView.fxml";
  public static final String profileEditView = "/fxml/MyAccountView.fxml";
  public static final String myMusicsView = "/fxml/MyMusicsCenterView.fxml";
  public static final String distantUserView = "/fxml/DistantUserView.fxml";

  @FXML
  private AnchorPane content;

  private Parent allMusicsParent;
  private AllMusicsController allMusicsController;

  private Parent myMusicsParent;
  private MyMusicsController myMusicsController;

  private Parent distantUserParent;
  private DistantUserController distantUserController;

  private Parent profileEditParent;
  private ProfileEditController profileEditController;
  //TODO : is detailsMusicController useless ??
  private DetailsMusicController detailsMusicController;

  //TODO new view or make new slots appear for research ?
  private Parent allMusicsAdvancedSearchParent;

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
    try {
      FXMLLoader allMusicsLoader = new FXMLLoader(
          getClass().getResource(CentralFrameController.allMusicsView));
      this.allMusicsParent = allMusicsLoader.load();
      this.allMusicsController = allMusicsLoader.getController();
      this.allMusicsController.setCentralFrameController(this);
      this.allMusicsController.init();

      FXMLLoader profileEditLoader = new FXMLLoader(
          getClass().getResource(CentralFrameController.profileEditView));
      this.profileEditParent = profileEditLoader.load();
      this.profileEditController = profileEditLoader.getController();
      this.profileEditController.setCentralFrameController(this);
      this.profileEditController.init();

      FXMLLoader myMusicsLoader = new FXMLLoader(
          getClass().getResource(CentralFrameController.myMusicsView));
      this.myMusicsParent = myMusicsLoader.load();
      this.myMusicsController = myMusicsLoader.getController();
      this.myMusicsController.setCentralFrameController(this);
      this.myMusicsController.init();
      this.myMusicsController.setApplication(this.mainController.getApplication());

      FXMLLoader distantUserLoader = new FXMLLoader(
          getClass().getResource(CentralFrameController.distantUserView));
      this.distantUserParent = distantUserLoader.load();
      this.distantUserController = distantUserLoader.getController();
      this.distantUserController.setCentralFrameController(this);

      this.setCentralContentMyMusics();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Methods to change the FXML shown in the central view
  // https://stackoverflow.com/questions/18619394/loading-new-fxml-in-the-same-scene
  public void setCentralContentProfileEdit() {
    this.content.getChildren().setAll((Node) this.profileEditParent);
  }

  public void setCentralContentAllMusics() {
    this.content.getChildren().setAll((Node) this.allMusicsParent);
  }

  public void setCentralContentMyMusics() {
    this.content.getChildren().setAll(((Node) this.myMusicsParent));
  }

  public void setCentralContentDistantUser() {
    this.content.getChildren().setAll(((Node) this.distantUserParent));
  }

}
