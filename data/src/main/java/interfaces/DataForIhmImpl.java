package interfaces;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.User;
import exceptions.LocalUsersFileException;
import features.CreateUser;
import features.Login;
import features.LogoutPayload;
import features.ShareMusicsPayload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;

public class DataForIhmImpl implements DataForIhm {
  private Datacore dc;

  @Override
  public void addMusic(MusicMetadata music, String path) throws FileNotFoundException {
    File f = new File(path);
    if (f.exists() && !f.isDirectory()) {
      LocalMusic newMusic = new LocalMusic(
          music,
          path
      );

      newMusic.getOwners().add(dc.getCurrentUser());

      dc.getCurrentUser().getMusics().add(newMusic);
      dc.addMusic(newMusic);
    } else {
      throw new FileNotFoundException("This file doesn't exist");
    }
  }

  @Override
  public void addComment(Music music, String comment) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void createUser(LocalUser user) throws IOException, LoginException {
    InputStream defaultPropInputStream = getClass().getClassLoader()
        .getResourceAsStream("default-config.properties");
    CreateUser.run(user, this.dc, defaultPropInputStream);
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
    LocalUser currentUser = this.dc.getCurrentUser();

    try {
      // Updates the written currentUser in case it has been modified
      this.dc.getLocalUsersFileHandler().update(currentUser);
    } catch (LocalUsersFileException e) {
      e.printStackTrace();
    }

    LogoutPayload payload = new LogoutPayload(currentUser.getUuid());
    this.dc.net.disconnect(payload, this.dc.getIps().collect(Collectors.toList()));

    this.dc.wipe();
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
