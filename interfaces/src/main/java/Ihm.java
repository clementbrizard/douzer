// java doc
public interface Ihm {
  void notifyUserConnection(User user);
  
  void notifyUserDisconnection(User user);
  
  void notifyDownloadProgress(Music music, int integer);
  
  void updateMusic(Music music);
  
  void notifyMusicDeletion(Music music);
  
  void updateUser(User user);
  
  void notifyUserDeletion(User user);
}


