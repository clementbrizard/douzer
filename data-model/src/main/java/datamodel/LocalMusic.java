package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.Set;

public class LocalMusic extends Music {
  private boolean sharedToAll;
  private boolean sharedToFriends;
  private String mp3Path;

  public LocalMusic(MusicMetadata metadata, Set<User> owners) {
    super(metadata, owners);
  }

  public LocalMusic(MusicMetadata metadata, String mp3Path) {
    super(metadata);
    this.mp3Path = mp3Path;
  }

  public boolean isSharedToAll() {
    return sharedToAll;
  }

  public void setSharedToAll(boolean sharedToAll) {
    this.sharedToAll = sharedToAll;
  }

  public boolean isSharedToFriends() {
    return sharedToFriends;
  }

  public void setSharedToFriends(boolean sharedToFriends) {
    this.sharedToFriends = sharedToFriends;
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
    return sharedToAll == that.sharedToAll
        && sharedToFriends == that.sharedToFriends
        && Objects.equals(mp3Path, that.mp3Path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }
}
