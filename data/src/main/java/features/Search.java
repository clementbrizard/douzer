package features;

import core.Datacore;
import datamodel.Music;
import datamodel.SearchQuery;
import java.util.stream.Stream;

public abstract class Search {
  /**
   * Search for musics matching the provided query.
   */
  public static Stream<Music> run(Datacore dc, SearchQuery searchQuery) {
    Stream<Music> res = dc.getMusics();
    if (searchQuery.getText() == null) {
      if (searchQuery.getTitle() != null) {
        res = res.filter(m -> m.getMetadata().getTitle().toLowerCase()
            .contains(searchQuery.getTitle().toLowerCase()));
      }
      if (searchQuery.getAlbum() != null) {
        res = res.filter(m -> m.getMetadata().getAlbum().toLowerCase()
            .contains(searchQuery.getAlbum().toLowerCase()));
      }
      if (searchQuery.getArtist() != null) {
        res = res.filter(m -> m.getMetadata().getArtist().toLowerCase()
            .contains(searchQuery.getArtist().toLowerCase()));
      }
      // Complexity very suboptimal but it negligible
      // Check if each tag of the query matches at least one of the music
      // Doing the tag filtering last is important for performances
      // (as it's the most computationally expensive and will benefit the most from pruned answers)
      if (searchQuery.getTags() != null) {
        res = res.filter(m -> searchQuery.getTags().stream().map(String::toLowerCase).allMatch(
            tq -> m.getMetadata().getTags().stream().map(String::toLowerCase)
                .anyMatch(t -> t.contains(tq)))
        );
      }
    } else {
      String text = searchQuery.getText().toLowerCase();
      res = res.filter(m -> m.getMetadata().getTitle().toLowerCase()
          .contains(text)
          || m.getMetadata().getAlbum().toLowerCase()
          .contains(text)
          || m.getMetadata().getArtist().toLowerCase()
          .contains(text)
          || m.getMetadata().getTags().stream().map(String::toLowerCase)
          .anyMatch(t -> t.contains(text))
      );
    }
    return res;
  }
}
