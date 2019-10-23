package datamodel;

import java.util.Collection;
import java.util.HashSet;
import javafx.util.Pair;

public class Music implements java.io.Serializable {
  private MusicMetadata metadata;
  private transient HashSet<User> owners;

  public Music(MusicMetadata metadata, Collection<User> owners) {
    this.metadata = metadata;
    this.owners = new HashSet<User>(owners);
  }

  public Music() {
  }

  public MusicMetadata getMetadata() {
    return metadata;
  }

  public Pair<MusicMetadata, HashSet<User>> toPair() {
    return new Pair<>(this.getMetadata(), this.getOwners());
  }

  public void setMetadata(MusicMetadata metadata) {
    this.metadata = metadata;
  }

  public HashSet<User> getOwners() {
    return owners;
  }

  public void setOwners(HashSet<User> owners) {
    this.owners = owners;
  }
}
