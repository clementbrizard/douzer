package interfaces;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import core.Datacore;
import core.LocalUsersFileHandler;
import datamodel.Comment;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.Playlist;
import datamodel.SearchQuery;
import datamodel.ShareStatus;
import datamodel.User;
import exceptions.data.DataException;
import features.CreateUser;
import features.DeleteMusic;
import features.DeleteUser;
import features.ExportUser;
import features.ImportUser;
import features.Login;
import features.Logout;
import features.ParseMusicMetadata;
import features.Search;
import features.ShareMusics;
import features.UnshareMusics;
import features.UpdateMusicsPayload;
import features.UpdateUserPayload;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
  public void createUser(LocalUser user) throws IOException {
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
    Logout.run(this.dc);
  }

  @Override
  public void download(Music music) {
    ArrayList<InetAddress> ownersIPs = new ArrayList<>();
    String musicHash = music.getMetadata().getHash();
    
    for (User owner : music.getOwners()) {
      ownersIPs.add(owner.getIp());
    }
    
    this.dc.net.requestDownload(ownersIPs.stream(), musicHash);
  }

  @Override
  public void exportProfile(Path path) throws IOException {
    ExportUser.run(dc.getCurrentUser(), path);
  }

  @Override
  public void importProfile(Path pathToBackup, Path newSavePath) throws IOException, DataException {
    ImportUser.run(pathToBackup, newSavePath, this.dc);
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
    return ParseMusicMetadata.run(this.dc, path);
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
  public Collection<Playlist> getPlaylist() {
    return this.getCurrentUser().getPlaylists();
  }

  @Override
  public Playlist getPlaylistByName(String name) throws IllegalArgumentException {
    return this.getCurrentUser().getPlaylistByName(name);
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

  @Override
  public Playlist createPlaylist(String name) {
    return this.getCurrentUser().addPlaylist(name);
  }

  @Override
  public void addMusicToPlaylist(LocalMusic music, Playlist playlist, Integer order) {
    playlist.addMusic(music, order);
  }

  @Override
  public void setPlaylistMusicList(Playlist playlist, ArrayList<LocalMusic> musicList) {
    playlist.setMusicList(musicList);
  }

  @Override
  public void removeMusicFromPlaylist(LocalMusic music, Playlist playlist) {
    playlist.removeMusic(music);
  }

  @Override
  public void deletePlaylist(Playlist playlist) {
    this.getCurrentUser().removePlaylist(playlist);
  }

  @Override
  public void changeMusicOrder(Playlist playlist, LocalMusic music, Integer newOrder) {
    playlist.changeOrder(music, newOrder);
  }
}
