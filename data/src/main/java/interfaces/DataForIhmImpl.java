package interfaces;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import core.Datacore;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.User;
import exceptions.LocalUsersFileException;
import features.CreateUser;
import features.DeleteMusic;
import features.DeleteUser;
import features.Login;
import features.LogoutPayload;
import features.Search;
import features.ShareMusicsPayload;
import features.UnshareMusics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Year;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;


public class DataForIhmImpl implements DataForIhm {
  private Datacore dc;

  public DataForIhmImpl(Datacore dc) {
    this.dc = dc;
  }

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
  public void deleteAccount() throws IOException {
    DeleteUser.run(this.dc);
  }

  @Override
  public void deleteMusic(LocalMusic music, boolean deleteLocal) {
    DeleteMusic.run(music, deleteLocal, dc);
  }

  @Override
  public void logout() throws IOException {
    LocalUser currentUser = this.dc.getCurrentUser();
    currentUser.setConnected(false);
    try {
      // Updates the written currentUser in case it has been modified
      this.dc.getLocalUsersFileHandler().update(currentUser);
    } catch (LocalUsersFileException e) {
      throw new IOException(e);
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
  public MusicMetadata parseMusicMetadata(String path)
      throws IOException, UnsupportedTagException, InvalidDataException {
    MusicMetadata metadata = new MusicMetadata();
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
    UnshareMusics.unshareMusic(music, dc);
  }


  @Override
  public void unshareMusics(Collection<LocalMusic> musics) {
    UnshareMusics.run(musics, dc);
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
