package interfaces;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import core.Datacore;
import datamodel.Comment;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.ShareStatus;
import datamodel.User;
import exceptions.data.DataException;
import features.CreateUser;
import features.DeleteMusic;
import features.DeleteUser;
import features.Login;
import features.LogoutPayload;
import features.Search;
import features.ShareMusics;
import features.ShareMusicsPayload;
import features.UnshareMusics;
import features.UpdateMusicsPayload;
import features.UpdateUserPayload;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.Year;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;


public class DataForIhmImpl implements DataForIhm {
  private Datacore dc;

  public DataForIhmImpl(Datacore dc) {
    this.dc = dc;
  }

  @Override
  public void addMusic(MusicMetadata music, String path, ShareStatus shareStatus)
      throws FileNotFoundException {
    File f = new File(path);
    if (f.exists() && !f.isDirectory()) {
      LocalMusic newMusic = new LocalMusic(
          music,
          path,
          shareStatus
      );

      newMusic.getOwners().add(dc.getCurrentUser());

      dc.getCurrentUser().getLocalMusics().add(newMusic);
      dc.addMusic(newMusic);
      this.shareMusic(newMusic);
    } else {
      throw new FileNotFoundException("This file doesn't exist");
    }
  }

  @Override
  public void addComment(Music music, String comment) {
    // Add a new Comment created from the String and this current LocalUser
    music.getMetadata().getComments().add(new Comment(comment, this.dc.getCurrentUser()));

    this.dc.net.sendToUsers(
        new UpdateMusicsPayload(this.dc.getCurrentUser(), Collections.singleton(music)),
        this.dc.getOnlineIps()
    );
  }

  @Override
  public void addFriend(User user) {
    dc.getCurrentUser().addFriend(user);
  }

  @Override
  public void removeFriend(User user) {
    dc.getCurrentUser().removeFriend(user);
  }

  @Override
  public void createUser(LocalUser user) throws IOException, LoginException {
    InputStream defaultPropInputStream = getClass().getClassLoader()
        .getResourceAsStream("default-config.properties");
    CreateUser.run(user, this.dc, defaultPropInputStream);
  }

  @Override
  public void deleteAccount() throws IOException {
    DeleteUser.run(this.dc);
  }

  @Override
  public void deleteMusic(LocalMusic music, boolean deleteLocal) {
    DeleteMusic.run(music, deleteLocal, dc);
  }

  @Override
  public void logout() throws IOException {
    // TODO: create feature class
    LocalUser currentUser = this.dc.getCurrentUser();
    currentUser.setConnected(false);
    // Updates the written currentUser in case it has been modified
    this.dc.getLocalUsersFileHandler().update(currentUser);

    LogoutPayload payload = new LogoutPayload(this.dc.getCurrentUser());
    this.dc.net.disconnect(payload, this.dc.getOnlineIps().collect(Collectors.toList()));

    Properties prop = new Properties();
    // TODO: template for filename
    Path userPropFilePath = currentUser.getSavePath()
        .resolve(currentUser.getUsername() + "-config.properties");
    File userConfigFile = userPropFilePath.toFile();

    if (userConfigFile.exists()) {
      prop.load(new FileInputStream(userPropFilePath.toString()));
      String ipsStr = this.dc.getAllIps().stream()
          .map(InetAddress::getHostAddress)
          .collect(Collectors.joining(","));
      prop.setProperty("ips", ipsStr);
      prop.store(new FileOutputStream(userPropFilePath.toString()), null);
    } else {
      throw new FileNotFoundException("Warning: user property file not found in the save path");
    }

    this.dc.wipe();
  }

  @Override
  public void download(Music music) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void exportProfile(String path) throws IOException {
    LocalUser localUser = dc.getCurrentUser();

    FileOutputStream fileOutputStream = new FileOutputStream(path);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    objectOutputStream.writeObject(localUser);

    objectOutputStream.flush();
    objectOutputStream.close();
    fileOutputStream.close();
  }

  @Override
  public void importProfile(String path)
      throws IOException, ClassNotFoundException, DataException {
    FileInputStream fileInputStream = new FileInputStream(path);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    LocalUser localUser = (LocalUser)objectInputStream.readObject();
    objectInputStream.close();
    fileInputStream.close();

    //Check if the username already exists on the local computer.
    //If it doesn't we add the LocalUser.
    boolean userNameAlreadyExists = false;
    try {
      dc.getLocalUsersFileHandler().getUser(localUser.getUsername());
      userNameAlreadyExists = true;
    } catch (NullPointerException e) {
      //If we have a NullPointerException, the user doesn't exists.
      dc.getLocalUsersFileHandler().add(localUser);
    }

    if (userNameAlreadyExists) {
      throw new DataException("The user already exists.");
    }
  }

  @Override
  public void login(String username, String password)
      throws IOException, LoginException {
    Login.run(this.dc, username, password);
  }

  @Override
  public void notifyUserUpdate(LocalUser user) {
    UpdateUserPayload payload = new UpdateUserPayload(user);
    this.dc.net.sendToUsers(payload, this.dc.getOnlineIps());
  }

  @Override
  public MusicMetadata parseMusicMetadata(String path)
      throws IOException, UnsupportedTagException, InvalidDataException, NoSuchAlgorithmException {

    String hash = new String(
        MessageDigest.getInstance("MD5").digest(Files.readAllBytes(Paths.get(path)))
    );

    MusicMetadata metadata = new MusicMetadata(hash);
    Mp3File mp3File = new Mp3File(path);

    metadata.setDuration(Duration.ofSeconds(mp3File.getLengthInSeconds()));

    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();

      metadata.setTitle(id3v1Tag.getTitle());
      metadata.setArtist(id3v1Tag.getArtist());
      metadata.setAlbum(id3v1Tag.getAlbum());
      if (id3v1Tag.getYear().length() > 0) {
        metadata.setReleaseYear(Year.parse(id3v1Tag.getYear()));
      }
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag = mp3File.getId3v2Tag();

      metadata.setTitle(id3v2Tag.getTitle());
      metadata.setArtist(id3v2Tag.getArtist());
      metadata.setAlbum(id3v2Tag.getAlbum());
      if (id3v2Tag.getYear() != null) {
        metadata.setReleaseYear(Year.parse(id3v2Tag.getYear()));
      }
    }

    return metadata;
  }

  @Override
  public void shareMusic(LocalMusic music) {
    this.shareMusics(Collections.singleton(music));
  }

  @Override
  public void shareMusics(Collection<LocalMusic> musics) {
    ShareMusics.run(this.dc, musics);
  }

  @Override
  public void notifyMusicUpdate(LocalMusic music) {
    UpdateMusicsPayload payload = new UpdateMusicsPayload(
        this.dc.getCurrentUser(),
        Collections.singleton(music)
    );
    switch (music.getShareStatus()) {
      case PUBLIC:
        this.dc.net.sendToUsers(payload, this.dc.getOnlineIps());
        break;
      case FRIENDS:
        this.dc.net.sendToUsers(
            payload,
            this.dc.getOnlineFriendsIps()
        );
        break;
      default:
        break;
    }
    this.unshareMusic(music);
  }

  @Override
  public void unshareMusic(LocalMusic music) {
    UnshareMusics.unshareMusic(music, this.dc);
  }


  @Override
  public void unshareMusics(Collection<LocalMusic> musics) {
    UnshareMusics.run(musics, this.dc);
  }

  @Override
  public Stream<User> getOnlineUsers() {
    return this.dc.getOnlineUsers();
  }

  @Override
  public Stream<Music> getAvailableMusics() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Stream<LocalMusic> getLocalMusics() {
    return this.dc.getLocalMusics();
  }

  @Override
  public List<LocalMusic> getPlaylist() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public LocalUser getCurrentUser() {
    return this.dc.getCurrentUser();
  }

  @Override
  public Stream<Music> getMusics() {
    return this.dc.getMusics();
  }

  @Override
  public Stream<Music> searchMusics(SearchQuery searchQuery) {
    return Search.run(this.dc, searchQuery);
  }
}
