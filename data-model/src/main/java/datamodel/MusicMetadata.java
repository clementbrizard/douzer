package datamodel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MusicMetadata implements java.io.Serializable {
  private String hash;
  private String title;
  private String artist;
  private String album;
  private Duration duration;
  private Year releaseYear;
  private Set<String> tags;
  private transient Map<User, Integer> ratings;
  private transient List<Comment> comments;

  public MusicMetadata() {
    this.tags = new HashSet<>();
    this.ratings = new HashMap<>();
    this.comments = new ArrayList<>();
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public Year getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(Year releaseYear) {
    this.releaseYear = releaseYear;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  public Map<User, Integer> getRatings() {
    return ratings;
  }

  public void setRatings(Map<User, Integer> ratings) {
    this.ratings = ratings;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
    stream.defaultReadObject();
    this.setRatings(new HashMap<>());
    this.setComments(new ArrayList<>());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MusicMetadata that = (MusicMetadata) o;
    return Objects.equals(hash, that.hash) &&
        Objects.equals(title, that.title) &&
        Objects.equals(artist, that.artist) &&
        Objects.equals(album, that.album) &&
        Objects.equals(duration, that.duration) &&
        Objects.equals(releaseYear, that.releaseYear) &&
        Objects.equals(tags, that.tags) &&
        Objects.equals(ratings, that.ratings) &&
        Objects.equals(comments, that.comments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hash, title, artist, album, duration, releaseYear, tags, ratings, comments);
  }
}
