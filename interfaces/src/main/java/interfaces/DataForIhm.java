package interfaces;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.Playlist;
import datamodel.SearchQuery;
import datamodel.ShareStatus;
import datamodel.User;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;

public interface DataForIhm {
  /**
   * Add new local music to the current owner and to the local list and shares it.
   *
   * @param music New music metadata
   * @param path  Path of local mp3
   * @throws FileNotFoundException Throws if MP3 file doesn't exist
   */
  void addMusic(MusicMetadata music, String path, ShareStatus shareStatus)
      throws FileNotFoundException;

  /**
   * Add a Comment to the specified Music.
   *
   * @param music   Updated Music
   * @param comment Added comment as a String
   */
  void addComment(Music music, String comment);

  void addFriend(User user);

  void removeFriend(User user);

  /**
   * Check if there is a config.properties file in the user's save path. If there is no config file
   * yet, create a config.properties file by copy of default-config.properties
   * default-config.properties is located in resources/
   * Then the function is calling Login.run function by giving the Datacore object and the LocalUser
   *
   * @param user LocalUser given by IHM
   */
  void createUser(LocalUser user) throws IOException, LoginException;

  void deleteAccount() throws IOException;

  void deleteMusic(LocalMusic music, boolean deleteLocal);

  void logout() throws IOException;

  void download(Music music);

  void exportProfile(String path);

  void importProfile(String path);

  void login(String username, String password) throws IOException, LoginException;

  void notifyUserUpdate(LocalUser user);

  /**
   * Extract metadata from mp3 file.
   *
   * @param path : path of the mp3 file
   * @return MusicMetaData object containing the extracted metadata
   */
  MusicMetadata parseMusicMetadata(String path)
      throws IOException, UnsupportedTagException, InvalidDataException, NoSuchAlgorithmException;

  void shareMusic(LocalMusic music);

  void shareMusics(Collection<LocalMusic> musics);

  void notifyMusicUpdate(LocalMusic music);

  void unshareMusic(LocalMusic music);

  void unshareMusics(Collection<LocalMusic> musics);

  Playlist createPlaylist(String name);

  void addMusicToPlaylist(LocalMusic music, Playlist playlist, Integer order);

  void removeMusicFromPlaylist(LocalMusic music, Playlist playlist);

  void deletePlaylist(Playlist playlist);

  void changeMusicOrder(Playlist playlist, LocalMusic music, Integer order);

  Stream<User> getOnlineUsers();

  Stream<Music> getAvailableMusics();

  Stream<LocalMusic> getLocalMusics();

  Collection<Playlist> getPlaylist();

  Playlist getPlaylistByName(String name) throws IllegalArgumentException;

  void setPlaylistMusicList(Playlist playlist, ArrayList<LocalMusic> musicList);

  LocalUser getCurrentUser();

  Stream<Music> getMusics();

  Stream<Music> searchMusics(SearchQuery searchQuery);
}
