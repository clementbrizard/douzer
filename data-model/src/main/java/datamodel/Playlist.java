package datamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Playlist is an list of ordered musics.
 * Order should be respected for the display.
 */
public class Playlist implements Serializable {
  private ArrayList<LocalMusic> musicList;
  private String name;

  public Playlist(String name) {
    this.name = name;
    this.musicList = new ArrayList<LocalMusic>();
  }

  /**
   * Adds a new music to the playlist with the index indicated.
   * @param music Music to add as type MusicMetadata
   * @param order Index of the music
   */
  public void addMusic(LocalMusic music, Integer order) {
    if (!this.musicList.contains(music)) {
      this.musicList.add(order, music);
    }
  }

  /**
   * Removes the music from the playlist.
   * @param music the music to remove.
   */
  public void removeMusic(LocalMusic music) {
    this.musicList.remove(music);
  }

  public void setMusicList(ArrayList<LocalMusic> newList) {
    this.musicList = newList;
  }

  /**
   * Changes the index of a music in the playlist.
   * @param music The music to change the index
   * @param newOrder new index for the music
   */
  public void changeOrder(LocalMusic music, Integer newOrder) {
    if (this.musicList.contains(music)) {
      this.musicList.remove(music);
      this.musicList.add(newOrder, music);
    } else {
      throw new UnsupportedOperationException("Music does not exist in the playlist.");
    }
  }

  public ArrayList<LocalMusic> getMusicList() {
    return musicList;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
