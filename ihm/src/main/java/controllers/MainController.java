package controllers;

import core.Application;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

  // Left
  @FXML
  private DownloadController downloadController;
  @FXML
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
    Logger logger = LogManager.getLogger();
    try {
      userInfoController.setMainController(this);
      userInfoController.init();
    } catch (UnsupportedOperationException e) {
      logger.warn("User Info Controller calls : %s", e.toString());
    }
    try {
      this.playerController.setMainController(this);
      this.playerController.setPlayerText("Test artist", "Test music");
    } catch (UnsupportedOperationException e) {
      logger.warn("Player Controller calls : " + e.getMessage());
    }

    try {
      this.centralFrameController.setMainController(this);
      this.centralFrameController.init();
    } catch (UnsupportedOperationException e) {
      logger.warn("Central Frame Controller calls : " + e.getMessage());
    }

    try {
      this.contactListController.setMainController(this);
    } catch (UnsupportedOperationException e) {
      logger.warn("Contact List Controller calls : " + e.getMessage());
    }

    try {
      this.onlineUsersListController.setMainController(this);
      this.onlineUsersListController.init();
    } catch (UnsupportedOperationException e) {
      logger.warn("Online Users Controller calls : " + e.getMessage());
    }

    try {
      this.currentMusicInfoController.setMainController(this);
      this.currentMusicInfoController.init();
      this.currentMusicInfoController.setApplication(this.application);
    } catch (UnsupportedOperationException e) {
      logger.warn("Current Music Info Controller calls : " + e.getMessage());
    }
  }

}
