package drydatamodel;

import core.Datacore;
import datamodel.Music;
import datamodel.User;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DryMusic implements java.io.Serializable {
  private DryMusicMetadata metadata;
  private Set<UUID> owners;

  public DryMusic(Music music) {
    this.metadata = new DryMusicMetadata(music.getMetadata());
    this.owners = music.getOwners().stream().map(User::getUuid).collect(Collectors.toSet());
  }

  public Music hydrate(Datacore dc) {
    return new Music(
        this.metadata.hydrate(dc),
        owners.stream().map(dc::getUser).collect(Collectors.toList())
    );
  }
}
