package datamodel;

import java.util.Set;

public class LocalMusic extends Music {
  private boolean shared;
  private String mp3Path;

  public LocalMusic(MusicMetadata metadata, Set<User> owners) {
    super(metadata, owners);
  }

  public LocalMusic(MusicMetadata metadata, String mp3Path) {
    super(metadata);
    this.mp3Path = mp3Path;
  }

  public boolean isShared() {
    return shared;
  }

  public void setShared(boolean shared) {
    this.shared = shared;
  }

  public String getMp3Path() {
    return mp3Path;
  }

  public void setMp3Path(String mp3Path) {
    this.mp3Path = mp3Path;
  }
}
