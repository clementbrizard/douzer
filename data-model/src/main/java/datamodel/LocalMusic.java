package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Set;

public class LocalMusic extends Music {
  private static final long serialVersionUID = 1L;
  private ShareStatus shareStatus;
  private String mp3Path;

  public LocalMusic(MusicMetadata metadata, Set<User> owners) {
    super(metadata, owners);
  }

  public LocalMusic(MusicMetadata metadata, String mp3Path) {
    super(metadata);
    this.mp3Path = mp3Path;
    this.shareStatus = ShareStatus.PRIVATE;
  }

  public LocalMusic(MusicMetadata metadata, String mp3Path, ShareStatus shareStatus) {
    super(metadata);
    this.mp3Path = mp3Path;
    this.shareStatus = shareStatus;
  }

  public LocalMusic(LocalMusic that) {
    super(that);
    this.mp3Path = that.mp3Path;
    this.shareStatus = that.shareStatus;
  }

  public ShareStatus getShareStatus() {
    return shareStatus;
  }

  public void setShareStatus(ShareStatus shareStatus) {
    this.shareStatus = shareStatus;
  }

  public String getMp3Path() {
    return mp3Path;
  }

  public void setMp3Path(String mp3Path) {
    this.mp3Path = mp3Path;
  }

  private void writeObject(ObjectOutputStream stream) throws IOException {
    stream.defaultWriteObject();
    // "override" the transient property of the metadata
    stream.writeObject(this.getMetadata());
  }

  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();

    MusicMetadata metadata = (MusicMetadata) stream.readObject();
    this.setMetadata(metadata);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    LocalMusic that = (LocalMusic) o;
    return shareStatus == that.shareStatus
        && Objects.equals(mp3Path, that.mp3Path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }
}
