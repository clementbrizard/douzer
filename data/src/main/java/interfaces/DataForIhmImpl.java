package interfaces;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.User;
import features.Login;
import features.ShareMusicsPayload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;

public class DataForIhmImpl implements DataForIhm {
  private Datacore dc;

  @Override
  public void addMusic(MusicMetadata music, String path) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void addComment(Music music, String comment) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Check if there is a config.properties file in the user's save path. If there is no config file
   * yet, create a config.properties file by copy of default-config.properties
   * default-config.properties is located in resources/
   * Then the function is calling Login.run function by giving the Datacore object and the LocalUser
   *
   * @param  user LocalUser given by IHM
   */
  @Override
  public void createUser(LocalUser user) throws IOException {
    //Create file config.properties
    Properties defaultProp = new Properties();
    Path defaultPropFilePath = Paths.get("resources/default-config.properties");
    Path userPropFilePath = user.getSavePath().resolve("config.properties");
    File userConfigFile = new File(userPropFilePath.toString());

    //If there is no config file for our user in the his path
    if (!userConfigFile.exists()) {
      InputStream defaultPropInputStream = new FileInputStream(defaultPropFilePath.toFile());

      if (defaultPropInputStream != null) {
        defaultProp.load(defaultPropInputStream);
      } else {
        throw new FileNotFoundException(
                "Warning: default property file not found in the resources path");
      }
      defaultProp.store(new FileOutputStream(userPropFilePath.toString()), null);
    }

    //Log user immediately after creation
    Login.run(this.dc, user);
  }

  @Override
  public void deleteAccount() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void deleteMusic(Music music, boolean deleteLocal) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void logout() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void download(Music music) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void exportProfile(String path) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void importProfile(String path) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void login(String username, String password)
      throws IOException, LoginException {
    Login.run(this.dc, username, password);
  }

  @Override
  public void modifyUser(LocalUser user) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void rateMusic(Music music, int rating) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void shareMusic(LocalMusic music) {
    this.shareMusics(Collections.singleton(music));
  }

  @Override
  public void shareMusics(Collection<LocalMusic> musics) {
    ShareMusicsPayload payload = new ShareMusicsPayload(musics);
    this.dc.net.sendToUsers(payload, this.dc.getIps());
  }

  @Override
  public void unshareMusic(LocalMusic music) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Stream<User> getOnlineUsers() {
    throw new UnsupportedOperationException("Not implemented yet");
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
  public LocalUser getLocalUser() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Stream<Music> getMusics(SearchQuery query) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
