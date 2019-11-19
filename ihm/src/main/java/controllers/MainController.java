package controllers;

import core.IhmCore;
import org.omg.CORBA.Current;

//replace by javadocs
//the view who will own each sub-views
public class MainController implements Controller {
  
  private IhmCore ihmCore;
  private UserInfoController userInfoController;
  private CurrentMusicInfoController currentMusicInfoController;
  private DownloadController downloadController;
  private PlayerController playerController;
  private ContactListController contactListcontroller;
  private OnlineUsersListController onlineUsersListController;
  private CentralFrameController centralFrameController;

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
    return contactListcontroller;
  }
  
  public void setContactListcontroller(ContactListController contactListcontroller) {
    this.contactListcontroller = contactListcontroller;
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
    this.centralFrameController = new CentralFrameController();
    this.centralFrameController.setMainController(this);
    this.centralFrameController.init();

    this.contactListcontroller = new ContactListController();
    this.contactListcontroller.setMainController(this);

    this.onlineUsersListController = new OnlineUsersListController();
    this.onlineUsersListController.setMainController(this);
    //this.onlineUsersListController.init(); // Pas implémenté côté Data

    this.userInfoController = new UserInfoController();
    this.userInfoController.setMainController(this);
    this.userInfoController.init(); // Crash ICI : impossible d'accéder aux champs FXML

    this.currentMusicInfoController = new CurrentMusicInfoController();
    this.currentMusicInfoController.setMainController(this);

    this.downloadController = new DownloadController();
    this.downloadController.setMainController(this);

    this.playerController = new PlayerController();
    this.playerController.setMainController(this);

  }
}
