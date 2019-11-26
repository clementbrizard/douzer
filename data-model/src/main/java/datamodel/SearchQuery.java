package datamodel;

import java.util.Collection;

public class SearchQuery {

  private String text;
  private String title;
  private String artist;
  private String album;
  private Collection<String> tags;

  public SearchQuery() {

  }

  /**
   * Look for this text in any field.
   *
   */
  public SearchQuery withText(String text) {
    this.text = text;
    return this;
  }

  public SearchQuery withTitle(String title) {
    this.title = title;
    return this;

  }

  public SearchQuery withArtist(String artist) {
    this.artist = artist;
    return this;
  }

  public SearchQuery withAlbum(String album) {
    this.album = album;
    return this;
  }

  public SearchQuery withTags(Collection<String> tags) {
    this.tags = tags;
    return this;
  }

  public String getText() {
    return text;
  }

  public String getTitle() {
    return title;
  }

  public String getArtist() {
    return artist;
  }

  public String getAlbum() {
    return album;
  }

  public Collection<String> getTags() {
    return this.tags;
  }
}
