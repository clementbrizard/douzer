package core;

//replace by javadocs
//start IhmForData class
public class IhmForData implements Ihm {

  private IhmCore ihmCore;
    
  public IhmForData(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
    
  public IhmCore getIhmCore() {
    return this.ihmCore;
  }
    
  @Override
  void notifyUserConnection(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
    
  @Override
   void notifyUserDisconnection(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
    
  @Override
  void notifyDownloadProgress(Music music, int integer) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
    
  @Override
  void updateMusic(Music music) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
    
  @Override
  void notifyMusicDeletion(Music music) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
    
  @Override
  void updateUser(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
    
  @Overrides
  void notifyUserDeletion(User user) {
    throw new Exception("La fonction n'est pas encore implémentée");
  }
}