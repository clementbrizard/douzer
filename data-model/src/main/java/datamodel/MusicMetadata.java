package datamodel;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class MusicMetadata implements java.io.Serializable {
  private String hash;
  private String title;
  private String artist;
  private String album;
  private Date releaseDate;
  private HashSet<String> tags;
  private transient HashMap<User, Integer> ratings;
  private HashSet<Comment> comments;

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

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public HashSet<String> getTags() {
    return tags;
  }

  public void setTags(HashSet<String> tags) {
    this.tags = tags;
  }

  public HashMap<User, Integer> getRatings() {
    return ratings;
  }

  public void setRatings(HashMap<User, Integer> ratings) {
    this.ratings = ratings;
  }

  public HashSet<Comment> getComments() {
    return comments;
  }

  public void setComments(HashSet<Comment> comments) {
    this.comments = comments;
  }
}
