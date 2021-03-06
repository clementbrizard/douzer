package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Music implements java.io.Serializable {
  private static final long serialVersionUID = 1L;
  private transient MusicMetadata metadata;
  private transient Set<User> owners;

  public Music(Music other) {
    this.metadata = other.metadata;
    this.owners = other.owners;
  }

  public Music(MusicMetadata metadata) {
    this.metadata = metadata;
    this.owners = new HashSet<>();
  }

  public Music(MusicMetadata metadata, Set<User> owners) {
    this.metadata = metadata;
    this.owners = owners;
  }

  public String getHash() {
    return this.getMetadata().getHash();
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

  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.setOwners(new HashSet<>());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Music music = (Music) o;
    return Objects.equals(this.metadata.getHash(), music.metadata.getHash());
    // no check on owners so no infinite recursion
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadata);
  }
}
