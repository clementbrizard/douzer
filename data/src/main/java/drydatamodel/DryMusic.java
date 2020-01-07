package drydatamodel;

import core.Datacore;
import datamodel.Music;
import datamodel.User;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DryMusic implements java.io.Serializable {
  private Music music;
  private DryMusicMetadata metadata;
  private Set<UUID> owners;

  public DryMusic(Music music) {
    this.music = new Music(music);
    this.metadata = new DryMusicMetadata(music.getMetadata());
    this.owners = music.getOwners().stream().map(User::getUuid).collect(Collectors.toCollection(HashSet::new));
  }

  public Music hydrate(Datacore dc) {
    this.music.setMetadata(this.metadata.hydrate(dc));
    this.music.setOwners(owners.stream().map(dc::getUser).collect(Collectors.toCollection(HashSet::new)));
    return this.music;
  }
}
