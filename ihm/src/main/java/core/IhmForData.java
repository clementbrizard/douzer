package core;

/**
 * integration for Ihm interface.
 *
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
   * @param user User recently connected.
   */
  @Override
  void notifyUserConnection(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }

  /**
   * Notify IHM that a User disconnected. This is just a change update, there is no user list
   * exchange.
   * @param user User recently disconnected.
   */
  @Override
   void notifyUserDisconnection(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }

  /**
   * Update the IHM progress bar by giving the percentage of the download progression.
   * @param music Music considered.
   * @param integer Percentage progression, between 0 and 100.
   */
  @Override
  void notifyDownloadProgress(Music music, int integer) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }

  /**
   * Trigger the update of all the occurrences of the modified music.
   * @param music Music to update.
   */
  @Override
  void updateMusic(Music music) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }

  /**
   * Notify that a music has been deleted.
   * @param music Deleted music.
   */
  @Override
  void notifyMusicDeletion(Music music) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }

  /**
   * Trigger the update of all the occurrences of the modified user.
   * @param user User to update
   */
  @Override
  void updateUser(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }

  /**
   * Notify that a User has been deleted.
   * @param user Deleted user.
   */
  @Override
  void notifyUserDeletion(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
}