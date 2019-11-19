package controllers;

import core.IhmCore;
import javafx.fxml.FXML;


//replace by javadocs
//the view who will own each sub-views
public class MainController implements Controller {
  
  private IhmCore ihmCore;
  // 1. Left controllers
  @FXML private UserInfoController userInfoController;
  @FXML private ContactListController contactListController;
  @FXML private OnlineUsersListController onlineUsersListController;

  // 2. Central controllers
  @FXML private CentralFrameController centralFrameController;
  @FXML private PlayerController playerController;

  // 3. Controllers not linked to FXML yet
  @FXML private DownloadController downloadController;
  @FXML private CurrentMusicInfoController currentMusicInfoController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public IhmCore getIhmCore() {
    return ihmCore;
  }

  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }

  public UserInfoController getUserInfoController() {
    return userInfoController;
  }

  public void setUserInfoController(UserInfoController userInfoController) {
    this.userInfoController = userInfoController;
  }
  
  public CurrentMusicInfoController getCurrentMusicInfoController() {
    return currentMusicInfoController;
  }
  
  public void setCurrentMusicInfoController(CurrentMusicInfoController currentMusicInfoController) {
    this.currentMusicInfoController = currentMusicInfoController;
  }
  
  public DownloadController getDownloadController() {
    return downloadController;
  }

  public void setDownloadController(DownloadController downloadController) {
    this.downloadController = downloadController;
  }
  
  public PlayerController getPlayerController() {
    return playerController;
  }

  public void setPlayerController(PlayerController playerController) {
    this.playerController = playerController;
  }

  public ContactListController getContactListcontroller() {
    return contactListController;
  }
  
  public void setContactListcontroller(ContactListController contactListController) {
    this.contactListController = contactListController;
  }
  
  public OnlineUsersListController getOnlineUsersListController() {
    return onlineUsersListController;
  }
  
  public void setOnlineUsersListController(OnlineUsersListController onlineUsersListController) {
    this.onlineUsersListController = onlineUsersListController;
  }
  
  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }
  
  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }


  /**
   * Initializes all controllers inside of the main controller.
   */
  public void init() {
    //TODO add controllers when we link them to the FXML
    userInfoController.setMainController(this);
    userInfoController.init();

    this.playerController.setMainController(this);
    this.playerController.setPlayerText("Test artist", "Test music");

    this.centralFrameController.setMainController(this);
    this.centralFrameController.init();

    this.contactListController.setMainController(this);

    this.onlineUsersListController.setMainController(this);
    this.onlineUsersListController.init();

    this.currentMusicInfoController.setMainController(this);
    this.currentMusicInfoController.init();


  }
}
