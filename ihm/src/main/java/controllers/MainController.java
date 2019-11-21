package controllers;

import core.Application;
import javafx.fxml.FXML;

/**
 * Controller in charge of managing the other (central, left and right).
 */
public class MainController implements Controller {

  // Left controllers
  @FXML
  private UserInfoController userInfoController;
  @FXML
  private ContactListController contactListController;
  @FXML
  private OnlineUsersListController onlineUsersListController;

  // Central controllers
  @FXML
  private CentralFrameController centralFrameController;
  @FXML
  private PlayerController playerController;

  // Controllers not linked to FXML yet
  private DownloadController downloadController;
  private CurrentMusicInfoController currentMusicInfoController;

  private Application application;

  // Getters

  public UserInfoController getUserInfoController() {
    return userInfoController;
  }

  public CurrentMusicInfoController getCurrentMusicInfoController() {
    return currentMusicInfoController;
  }

  public DownloadController getDownloadController() {
    return downloadController;
  }

  public PlayerController getPlayerController() {
    return playerController;
  }

  public ContactListController getContactListController() {
    return contactListController;
  }

  public void setApplication(Application application) {
    this.application = application;
  }

  public OnlineUsersListController getOnlineUsersListController() {
    return onlineUsersListController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  public Application getApplication() {
    return application;
  }

  // Setters

  public void setUserInfoController(UserInfoController userInfoController) {
    this.userInfoController = userInfoController;
  }

  public void setCurrentMusicInfoController(CurrentMusicInfoController currentMusicInfoController) {
    this.currentMusicInfoController = currentMusicInfoController;
  }

  public void setDownloadController(DownloadController downloadController) {
    this.downloadController = downloadController;
  }

  public void setPlayerController(PlayerController playerController) {
    this.playerController = playerController;
  }

  public void setContactListController(ContactListController contactListController) {
    this.contactListController = contactListController;
  }

  public void setOnlineUsersListController(OnlineUsersListController onlineUsersListController) {
    this.onlineUsersListController = onlineUsersListController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  // Other methods

  @Override
  public void initialize() {
  }

  /**
   * Initialize all controllers inside the main controller.
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
  }

}
