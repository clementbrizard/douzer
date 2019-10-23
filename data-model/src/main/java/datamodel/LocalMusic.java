package datamodel;

import java.util.Collection;

public class LocalMusic extends Music {
  private boolean shared;
  private String mp3Path;

  public LocalMusic(MusicMetadata metadata, Collection<User> owners) {
    super(metadata, owners);
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
