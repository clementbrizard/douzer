package interfaces;

import core.Datacore;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.SearchQuery;
import datamodel.User;
import features.ShareMusicsPayload;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

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

  @Override
  public void createUser(LocalUser user) {
    throw new UnsupportedOperationException("Not implemented yet");
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
  public void login(String username, String password) {
    throw new UnsupportedOperationException("Not implemented yet");
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
