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
  void notifyUserConnection(User user);

  /**
   * Notify IHM that a User disconnected. This is just a change update, there is no user list
   * exchange.
   *
   * @param user User recently disconnected.
   */
  void notifyUserDisconnection(User user);

  /**
   * Update the IHM progress bar by giving the percentage of the download progression.
   *
   * @param music   Music considered.
   * @param integer Percentage progression, between 0 and 100.
   */
  void notifyDownloadProgress(Music music, int integer);

  /**
   * Trigger the update of all the occurrences of the modified music.
   *
   * @param music Music to update.
   */
  void updateMusic(Music music);

  /**
   * Notify that a music has been deleted.
   *
   * @param music Deleted music.
   */
  void notifyMusicDeletion(Music music);

  /**
   * Trigger the update of all the occurrences of the modified user.
   *
   * @param user User to update
   */
  void updateUser(User user);

  /**
   * Notify that a User has been deleted.
   *
   * @param user Deleted user.
   */
  void notifyUserDeletion(User user);
}


