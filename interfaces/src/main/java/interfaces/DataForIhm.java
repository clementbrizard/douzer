package interfaces;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.ShareStatus;
import datamodel.User;
import exceptions.data.DataException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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

  /**
   * Backup the current LocalUser, its musics and its avatar.
   * @param path path to the directory that will contain the backup.
   * @throws IOException if there is a problem creating or deleting any file.
   */
  void exportProfile(String path) throws IOException;

  /**
   * Import a previously exported LocalUser.
   * @param path the path to the backup directory.
   * @throws IOException if it is not possible to read a file.
   * @throws DataException if the username already exists.
   */
  void importProfile(String path) throws IOException, ClassNotFoundException, DataException;

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

  Stream<User> getOnlineUsers();

  Stream<Music> getAvailableMusics();

  Stream<LocalMusic> getLocalMusics();

  List<LocalMusic> getPlaylist();

  LocalUser getCurrentUser();

  Stream<Music> getMusics();

  Stream<Music> searchMusics(SearchQuery searchQuery);
}
