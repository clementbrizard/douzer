package interfaces;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.User;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;

public interface DataForIhm {
  void addMusic(MusicMetadata music, String path);

  void addComment(Music music, String comment);

  /**
   * Check if there is a config.properties file in the user's save path. If there is no config file
   * yet, create a config.properties file by copy of default-config.properties
   * default-config.properties is located in resources/
   * Then the function is calling Login.run function by giving the Datacore object and the LocalUser
   *
   * @param  user LocalUser given by IHM
   */
  void createUser(LocalUser user) throws IOException;

  void deleteAccount();

  void deleteMusic(Music music, boolean deleteLocal);

  void logout();

  void download(Music music);

  void exportProfile(String path);

  void importProfile(String path);

  void login(String username, String password) throws IOException, LoginException;

  void modifyUser(LocalUser user);

  void rateMusic(Music music, int rating);

  void shareMusic(LocalMusic music);

  void shareMusics(Collection<LocalMusic> musics);

  void unshareMusic(LocalMusic music);

  Stream<User> getOnlineUsers();

  Stream<Music> getAvailableMusics();

  Stream<LocalMusic> getLocalMusics();

  List<LocalMusic> getPlaylist();

  LocalUser getLocalUser();

  Stream<Music> getMusics(SearchQuery query);
}
