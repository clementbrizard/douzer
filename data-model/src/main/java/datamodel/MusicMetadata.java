package datamodel;

import java.time.Duration;
import java.time.Year;
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
  private Year releaseYear;
  private Set<String> tags;
  private transient Map<User, Integer> ratings;
  private transient List<Comment> comments;
  private Date timeStamp;

  /**
   * MusicMetadata constructor.
   */
  public MusicMetadata() {
    this.tags = new HashSet<>();
    this.ratings = new HashMap<>();
    this.comments = new ArrayList<>();

    updateTimeStamp();
  }

  public String getHash() {
    return hash;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    updateTimeStamp();
    this.title = title;
  }

  public String getArtist() {
    return artist;
  }

  public void setArtist(String artist) {
    updateTimeStamp();
    this.artist = artist;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    updateTimeStamp();
    this.album = album;
  }

  public Year getReleaseYear() {
    return releaseYear;
  }

  public void setReleaseYear(Year releaseYear) {
    updateTimeStamp();
    this.releaseYear = releaseYear;
  }

  public Set<String> getTags() {
    return tags;
  }

  public void setTags(Set<String> tags) {
    updateTimeStamp();
    this.tags = tags;
  }

  public Map<User, Integer> getRatings() {
    return ratings;
  }

  public void setRatings(Map<User, Integer> ratings) {
    updateTimeStamp();
    this.ratings = ratings;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    updateTimeStamp();
    this.comments = comments;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    updateTimeStamp();
    this.duration = duration;
  }

  public Date getTimeStamp() {
    return this.timeStamp;
  }

  private void updateTimeStamp() {
    this.timeStamp = new Date();
  }

  /**
   * Update this MusicMetadata with metaData of another music.
   *
   * @param newMusicMetadata the reference that will be updated.
   */
  public void updateMusicMetadata(MusicMetadata newMusicMetadata) {
    // Modify unique values
    this.title = newMusicMetadata.title;
    this.album = newMusicMetadata.album;
    this.artist = newMusicMetadata.artist;
    this.releaseYear = newMusicMetadata.releaseYear;
    //Add set values
    this.tags = newMusicMetadata.tags;
    this.comments = newMusicMetadata.comments;
    this.ratings = newMusicMetadata.ratings;

    updateTimeStamp();
  }
}
