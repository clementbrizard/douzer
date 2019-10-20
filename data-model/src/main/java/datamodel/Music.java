package datamodel;

import java.util.HashSet;

public class Music implements java.io.Serializable {
  private MusicMetadata metadata;
  private transient HashSet<User> owners;

  public MusicMetadata getMetadata() {
    return metadata;
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
