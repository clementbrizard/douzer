package drydatamodel;

import core.Datacore;
import datamodel.MusicMetadata;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class DryMusicMetadata implements java.io.Serializable {
  private String hash;
  private String title;
  private String artist;
  private String album;
  private Date releaseDate;
  private Set<String> tags;
  private Map<UUID, Integer> ratings;
  private Set<DryComment> comments;

  public DryMusicMetadata(MusicMetadata metadata) {
    this.hash = metadata.getHash();
    this.title = metadata.getTitle();
    this.artist = metadata.getArtist();
    this.album = metadata.getAlbum();
    this.releaseDate = metadata.getReleaseDate();
    this.tags = metadata.getTags();

    this.ratings = metadata.getRatings().entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey().getUuid(), Map.Entry::getValue));
    this.comments = metadata.getComments().stream()
        .map(DryComment::new).collect(Collectors.toSet());
  }

  public MusicMetadata hydrate(Datacore dc) {
    MusicMetadata metadata = new MusicMetadata();
    metadata.setHash(this.hash);
    metadata.setTitle(this.title);
    metadata.setArtist(this.artist);
    metadata.setAlbum(this.album);
    metadata.setReleaseDate(this.releaseDate);
    metadata.setRatings(null);

    metadata.setComments(this.comments.stream()
        .map(dryComment -> dryComment.hydrate(dc)).collect(Collectors.toList())
    );
    metadata.setRatings(this.ratings.entrySet().stream()
        .collect(Collectors.toMap(e -> dc.getUser(e.getKey()), Map.Entry::getValue)));
    return metadata;
  }
}
