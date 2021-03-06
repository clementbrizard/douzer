package core;

import controllers.AllMusicsController;
import controllers.ContactListController;
import controllers.CurrentMusicInfoController;
import controllers.DistantUserController;
import controllers.DownloadController;
import controllers.OnlineUsersListController;
import datamodel.Music;
import datamodel.User;
import interfaces.Ihm;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

/**
 * integration for Ihm interface.
 */
public class IhmForData implements Ihm {
  private static final Logger ihmForDataLogger = LogManager.getLogger();

  private IhmCore ihmCore;

  public IhmForData(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }

  public IhmCore getIhmCore() {
    return this.ihmCore;
  }

  /**
   * Notify IHM that a new User is connected. This is just a change update, there is no user list
   * exchange.
   *
   * @param user User recently connected.
   */
  @Override
  public void notifyUserConnection(User user) {
    OnlineUsersListController onlineUsersListController;
    ContactListController contactListController;
    AllMusicsController allMusicsController;

    try {
      ihmForDataLogger.info("{} was notified of {} connection",
          this.ihmCore.getDataForIhm().getCurrentUser().getUsername(),
          user.getUsername()
      );

      onlineUsersListController = this.ihmCore
          .getApplication().getMainController().getOnlineUsersListController();
      contactListController = this.ihmCore
          .getApplication().getMainController().getContactListController();
      allMusicsController = this.ihmCore.getApplication()
          .getMainController().getCentralFrameController().getAllMusicsController();

    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    onlineUsersListController.addNewOnlineUser(user);
    contactListController.displayContacts();

    //Refresh music now that new user has been added
    allMusicsController.displayAvailableMusics();

  }

  /**
   * Notify IHM that a User disconnected. This is just a change update, there is no user list
   * exchange.
   *
   * @param user User recently disconnected.
   */
  @Override
  public void notifyUserDisconnection(User user) {
    OnlineUsersListController onlineUsersListController;
    DistantUserController controllerDistantUser;
    ContactListController contactListController;
    AllMusicsController allMusicsController;

    try {
      onlineUsersListController = this.ihmCore
          .getApplication().getMainController().getOnlineUsersListController();
      controllerDistantUser = this.ihmCore.getApplication()
          .getMainController().getCentralFrameController().getDistantUserController();
      contactListController = this.ihmCore
          .getApplication().getMainController().getContactListController();
      allMusicsController = this.ihmCore.getApplication()
          .getMainController().getCentralFrameController().getAllMusicsController();

    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    onlineUsersListController.removeOnlineUser(user);
    ihmForDataLogger.info("{} was notified of {} disconnection",
        this.ihmCore.getDataForIhm().getCurrentUser().getUsername(),
        user.getUsername());

    contactListController.displayContacts();

    //Refresh music now that user isn't available anymore
    allMusicsController.displayAvailableMusics();

    if (controllerDistantUser.getDistantUser() != null) {
      if (controllerDistantUser.getDistantUser().equals(user)) {
        Platform.runLater(new Runnable() {
          @Override
          public void run() {
            controllerDistantUser.getCentralFrameController().setCentralContentMyMusics();
            Notifications.create()
                .title(user.getUsername() + " vient de se déconnecter")
                .text("Son profil n'est plus accessible. Redirection sur Mes Musiques.")
                .darkStyle()
                .showInformation();
          }
        });
      }
    }
  }

  /**
   * Update the IHM progress bar by giving the percentage of the download progression.
   *
   * @param downloadedMusic  Music considered.
   * @param downloadProgress Percentage progression, between 0 and 100.
   */
  @Override
  public void notifyDownloadProgress(Music downloadedMusic, int downloadProgress) {
    DownloadController downloadController = null;
    try {
      downloadController = this.ihmCore.getApplication()
          .getMainController()
          .getDownloadController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
    }
    downloadController.updateDownloadProgressBar(downloadedMusic, downloadProgress);

    LogManager.getLogger()
        .warn("La fonction d'affichage du téléchargement n'est pas encore liée");
  }

  /**
   * Trigger the update of all the occurrences of the modified music.
   *
   * @param music Music to update.
   */
  @Override
  public void updateMusic(Music music) {
    AllMusicsController controllerAllMusic;
    try {
      controllerAllMusic = this.ihmCore.getApplication()
          .getMainController().getCentralFrameController().getAllMusicsController();
    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    ihmForDataLogger.info("{} was notified of music \"{}\" update",
        this.ihmCore.getDataForIhm().getCurrentUser().getUsername(),
        music.getMetadata().getTitle());
    controllerAllMusic.displayAvailableMusics();

    CurrentMusicInfoController controllerCurrentMusic;
    try {
      controllerCurrentMusic = this.ihmCore.getApplication()
          .getMainController().getCurrentMusicInfoController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    if (controllerCurrentMusic.getCurrentMusic() != null
        && controllerCurrentMusic.getCurrentMusic().getMetadata().getHash()
        .equals(music.getMetadata().getHash())) {
      controllerCurrentMusic.init(music);
    }

    // Update musics in the distantUserController if necessary
    DistantUserController controllerDistantUser;
    try {
      controllerDistantUser = this.ihmCore.getApplication()
          .getMainController()
          .getCentralFrameController()
          .getDistantUserController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    controllerDistantUser.displayDistantUserMusics();
  }

  /**
   * Notify that a music has been deleted.
   *
   * @param music Deleted music.
   */
  @Override
  public void notifyMusicDeletion(Music music) {
    AllMusicsController controllerAllMusic;
    try {
      controllerAllMusic = this.ihmCore.getApplication()
          .getMainController()
          .getCentralFrameController()
          .getAllMusicsController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    ihmForDataLogger.info("{} was notified of music \"{}\" deletion",
        this.ihmCore.getDataForIhm().getCurrentUser().getUsername(),
        music.getMetadata().getTitle()
    );

    controllerAllMusic.displayAvailableMusics();

    CurrentMusicInfoController controllerCurrentMusic;
    try {
      controllerCurrentMusic = this.ihmCore.getApplication()
          .getMainController()
          .getCurrentMusicInfoController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    if (controllerCurrentMusic.getCurrentMusic() != null
        && controllerCurrentMusic.getCurrentMusic().getHash()
        .equals(music.getHash())) {
      controllerCurrentMusic.init(null);
    }

    // Update musics in the distantUserController if necessary
    DistantUserController controllerDistantUser;
    try {
      controllerDistantUser = this.ihmCore.getApplication()
          .getMainController()
          .getCentralFrameController()
          .getDistantUserController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    controllerDistantUser.displayDistantUserMusics();
  }

  /**
   * Trigger the update of all the occurrences of the modified user.
   *
   * @param user User to update
   */
  @Override
  public void updateUser(User user) {
    OnlineUsersListController onlineUsersListController;
    DistantUserController distantUserController;
    ContactListController contactListController;

    try {
      onlineUsersListController = this.ihmCore
          .getApplication().getMainController().getOnlineUsersListController();
      distantUserController = this.ihmCore.getApplication()
          .getMainController().getCentralFrameController().getDistantUserController();
      contactListController = this.ihmCore
          .getApplication().getMainController().getContactListController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }

    // This is done to avoid a "Not on FX application thread" error
    // Solution found here : https://stackoverflow.com/a/23007018
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        // Refresh online user list
        onlineUsersListController.updateOnlineUser(user);
        distantUserController.updateDistantUser(user);
        contactListController.displayContacts();
      }
    });
  }

  /**
   * Notify that a User has been deleted.
   *
   * @param user Deleted user.
   */
  @Override
  public void notifyUserDeletion(User user) {
    DistantUserController controllerDistantUser;
    try {
      controllerDistantUser = this.ihmCore.getApplication()
          .getMainController()
          .getCentralFrameController()
          .getDistantUserController();
    } catch (NullPointerException e) {
      ihmForDataLogger.error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }
    ihmForDataLogger.warn("L'affichage des utilisateurs distants n'est pas encore implémenté");
  }
}