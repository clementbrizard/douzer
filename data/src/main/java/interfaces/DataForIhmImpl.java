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
import features.CreateUser;
import features.Login;
import features.ShareMusicsPayload;
import features.UnshareMusicsPayload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Year;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DataForIhmImpl implements DataForIhm {
  private Datacore dc;
  private static final Logger startLogger = LogManager.getLogger();

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
  public void deleteAccount() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * .to delete a music in the library, eventually delete locally, and unshare the music
   * @param music the music
   * @param deleteLocal whether or not to delete locally
   */
  @Override
  public void deleteMusic(LocalMusic music, boolean deleteLocal) {
    Set musics = this.dc.getCurrentUser().getMusics();
    if (musics.contains(music)) {
      unshareMusic(music);
      musics.remove(music);
      if (deleteLocal) {
        File file = new File(music.getMp3Path());
        if (file.delete()) {
          startLogger.info(music.getMetadata().getTitle() + "is deleted locally");
        } else {
          startLogger.error("Delete operation is failed.");
        }
      }
    }
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
    this.unshareMusics(Collections.singleton(music));
  }

  @Override
  public void unshareMusics(Collection<LocalMusic> musics) {
    Collection<String> musicHashs = musics.stream()
        .map(x -> x.getMetadata().getHash()).collect(Collectors.toList());
    UnshareMusicsPayload payload = new UnshareMusicsPayload(musicHashs, dc.getCurrentUser());
    this.dc.net.sendToUsers(payload, this.dc.getIps());
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
  public LocalUser getCurrentUser() {
    return this.dc.getCurrentUser();
  }

  @Override
  public Stream<Music> getMusics(SearchQuery query) {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
