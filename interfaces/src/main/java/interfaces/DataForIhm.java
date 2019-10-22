package interfaces;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.User;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface DataForIhm {
  void addMusic(MusicMetadata music, String path);

  void addComment(Music music, String comment);

  void createUser(LocalUser user);

  void deleteAccount();

  void deleteMusic(Music music, boolean deleteLocal);

  void logout();

  void download(Music music);

  void exportProfile(String path);

  void importProfile(String path);

  void login(String username, String password);

  void modifyUser(LocalUser user);

  void rateMusic(Music music, int rating);

  void shareMusic(LocalMusic music);

  void shareMusics(Collection<LocalMusic> musics);

  void unshareMusic(LocalMusic music);

  Stream<User> getOnlineUsers();

  Stream<Music> getAvailableMusics();

  List<LocalMusic> getPlaylist();

  LocalUser getLocalUser();

  Stream<Music> getMusics(SearchQuery query);
}
