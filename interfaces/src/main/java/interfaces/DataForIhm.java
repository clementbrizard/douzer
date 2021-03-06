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
import exceptions.data.DataException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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

  /**
   * Backup the current LocalUser, its musics and its avatar.
   * @param path path to the directory that will contain the backup.
   * @throws IOException if there is a problem creating or deleting any file.
   */
  void exportProfile(Path path) throws IOException;

  /**
   * Import a previously exported LocalUser.
   * @param pathToBackup the path to the backup directory.
   * @param newSavePath the new savePath for the imported LocalUser.
   * @throws IOException if it is not possible to read a file.
   * @throws DataException if the username already exists.
   */
  void importProfile(Path pathToBackup, Path newSavePath)
      throws IOException, DataException;

  void login(String username, String password) throws IOException, LoginException;

  void notifyUserUpdate(LocalUser user) throws IOException;

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

  ConcurrentHashMap<UUID, User> getOnlineUsersSet();

  Stream<Music> getAvailableMusics();

  Stream<LocalMusic> getLocalMusics();

  Collection<Playlist> getPlaylist();

  Playlist getPlaylistByName(String name) throws IllegalArgumentException;

  void setPlaylistMusicList(Playlist playlist, ArrayList<LocalMusic> musicList);

  LocalUser getCurrentUser();

  Stream<Music> getMusics();

  Stream<Music> searchMusics(SearchQuery searchQuery);
}
