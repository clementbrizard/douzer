package datamodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MusicMetadata implements java.io.Serializable {
  private String hash;
  private String title;
  private String artist;
  private String album;
  private Duration duration;
  private Date releaseDate;
  private Set<String> tags;
  private transient Map<User, Integer> ratings;
  private transient List<Comment> comments;
  private Date timeStamp;

  public MusicMetadata() {
    this.tags = new HashSet<>();
    this.ratings = new HashMap<>();
    this.comments = new ArrayList<>();

    this.timeStamp = new Date();
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    dateFormat.format(this.timeStamp);
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

  public Date getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
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

  public Date getTimeStamp() {
    return this.timeStamp;
  }

  public void setTimeStamp(Date date) {
    this.timeStamp = date;
  }
}
