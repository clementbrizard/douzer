package interfaces;

import datamodel.Music;
import datamodel.User;
import java.util.Collection;

public interface Ihm {
  void notifyUserConnection(User user);

  void notifyUserDisconnection(User user);

  void notifyDownloadProgress(Music music, int progress);

  void updateMusic(Music music);

  void notifyMusicDeletion(Music music);

  void updateUser(User user);

  void notifyUserDeletion(User user);

  void notifyUsersChanges(Collection<User> users);
}
