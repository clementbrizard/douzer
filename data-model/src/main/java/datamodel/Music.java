package datamodel;

import java.util.Set;

public class Music implements java.io.Serializable {
  private transient MusicMetadata metadata;
  private transient Set<User> owners;

  public Music(MusicMetadata metadata) {
    this.metadata = metadata;
  }

  public Music(MusicMetadata metadata, Set<User> owners) {
    this.metadata = metadata;
    this.owners = owners;
  }

  public MusicMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(MusicMetadata metadata) {
    this.metadata = metadata;
  }

  public Set<User> getOwners() {
    return owners;
  }

  public void setOwners(Set<User> owners) {
    this.owners = owners;
  }
}
