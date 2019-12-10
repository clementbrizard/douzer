package core;

import controllers.AllMusicsController;
import controllers.CurrentMusicInfoController;
import controllers.DetailsMusicController;
import controllers.OnlineUsersListController;
import datamodel.LocalMusic;
import datamodel.Music;
import datamodel.User;
import interfaces.Ihm;

import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * integration for Ihm interface.
 */
public class IhmForData implements Ihm {

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
    OnlineUsersListController controller;
    try {
      controller = this.ihmCore.getApplication().getMainController().getOnlineUsersListController();
    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }
    controller.addNewOnlineUser(user);
  }

  /**
   * Notify IHM that a User disconnected. This is just a change update, there is no user list
   * exchange.
   *
   * @param user User recently disconnected.
   */
  @Override
  public void notifyUserDisconnection(User user) {
    OnlineUsersListController controller;
    try {
      controller = this.ihmCore.getApplication().getMainController().getOnlineUsersListController();
    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }
    controller.removeOnlineUser(user);
  }

  /**
   * Update the IHM progress bar by giving the percentage of the download progression.
   *
   * @param music   Music considered.
   * @param integer Percentage progression, between 0 and 100.
   */
  @Override
  public void notifyDownloadProgress(Music music, int integer) {
    throw new UnsupportedOperationException("La fonction n'est pas encore implémentée");
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
      controllerAllMusic = this.ihmCore.getApplication().getMainController().getCentralFrameController().getAllMusicsController();
    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }
    controllerAllMusic.searchMusics(null);

    CurrentMusicInfoController controllerCurrentMusic;
    try {
      controllerCurrentMusic = this.ihmCore.getApplication().getMainController().getCurrentMusicInfoController();
    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }
    if (controllerCurrentMusic.getCurrentMusic().equals(music))
      controllerCurrentMusic.init(music);

    DetailsMusicController controllerDetails;
    try {
      controllerDetails = this.ihmCore.getApplication().getMainController().getCentralFrameController().getDetailsMusicController();
    } catch (NullPointerException e) {
      LogManager.getLogger().error("Controller chain not fully initialized : " + e);
      e.printStackTrace();
      return;
    }
    if (controllerDetails.getLocalMusic().equals(music))
      controllerDetails.initMusic((LocalMusic)music);
  }

  /**
   * Notify that a music has been deleted.
   *
   * @param music Deleted music.
   */
  @Override
  public void notifyMusicDeletion(Music music) {
    throw new UnsupportedOperationException("La fonction n'est pas encore implémentée");
  }

  /**
   * Trigger the update of all the occurrences of the modified user.
   *
   * @param user User to update
   */
  @Override
  public void updateUser(User user) {
    throw new UnsupportedOperationException("La fonction n'est pas encore implémentée");
  }

  /**
   * Notify that a User has been deleted.
   *
   * @param user Deleted user.
   */
  @Override
  public void notifyUserDeletion(User user) {
    throw new UnsupportedOperationException("La fonction n'est pas encore implémentée");
  }
}