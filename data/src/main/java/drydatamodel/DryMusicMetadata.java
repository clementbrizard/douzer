package drydatamodel;

import core.Datacore;
import datamodel.MusicMetadata;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DryMusicMetadata implements java.io.Serializable {
  private MusicMetadata musicMetadata;
  private Map<UUID, Integer> ratings;
  private List<DryComment> comments;

  public DryMusicMetadata(MusicMetadata metadata) {
    this.musicMetadata = metadata;

    this.ratings = metadata.getRatings().entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey().getUuid(), Map.Entry::getValue));
    this.comments = metadata.getComments().stream()
        .map(DryComment::new).collect(Collectors.toList());
  }

  public MusicMetadata hydrate(Datacore dc) {
    this.musicMetadata.setComments(this.comments.stream()
        .map(dryComment -> dryComment.hydrate(dc)).collect(Collectors.toList())
    );
    this.musicMetadata.setRatings(this.ratings.entrySet().stream()
        .collect(Collectors.toMap(e -> dc.getUser(e.getKey()), Map.Entry::getValue)));
    return this.musicMetadata;
  }
}
