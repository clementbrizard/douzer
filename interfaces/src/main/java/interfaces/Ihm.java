package interfaces;

import datamodel.Music;
import datamodel.User;
import java.util.Collection;

/**
 * Interface for Data. Give the interface to update the IHM for change from network.
 */
public interface Ihm {
  /**
   * Notify IHM that a new User is connected. This is just a change update, there is no user list
   * exchange.
   *
   * @param user User recently connected.
   */
  public void notifyUserConnection(User user) throws Exception;

  /**
   * Notify IHM that a User disconnected. This is just a change update, there is no user list
   * exchange.
   *
   * @param user User recently disconnected.
   */
  public void notifyUserDisconnection(User user) throws Exception;

  /**
   * Update the IHM progress bar by giving the percentage of the download progression.
   *
   * @param music   Music considered.
   * @param integer Percentage progression, between 0 and 100.
   */
  public void notifyDownloadProgress(Music music, int integer) throws Exception;

  /**
   * Trigger the update of all the occurrences of the modified music.
   *
   * @param music Music to update.
   */
  public void updateMusic(Music music) throws Exception;

  /**
   * Notify that a music has been deleted.
   *
   * @param music Deleted music.
   */
  public void notifyMusicDeletion(Music music) throws Exception;

  /**
   * Trigger the update of all the occurrences of the modified user.
   *
   * @param user User to update
   */
  public void updateUser(User user) throws Exception;

  /**
   * Notify that a User has been deleted.
   *
   * @param user Deleted user.
   */
  public void notifyUserDeletion(User user) throws Exception;

}
