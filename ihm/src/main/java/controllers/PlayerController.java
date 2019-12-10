package controllers;

import datamodel.LocalMusic;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * PlayerMusic Controller.
 */
public class PlayerController implements Controller {

  @FXML
  private ProgressBar pgMusicProgress;

  @FXML
  private Button play;

  @FXML
  private Label songInfo;

  @FXML
  private Label lblTime;

  @FXML
  private Label fullTime;

  private MainController mainController;

  private final Image pauseIcon = new Image(
      getClass().getResourceAsStream("../images/pauseSymbol.png")
  );

  private final Image playIcon = new Image(
      getClass().getResourceAsStream("../images/playSymbol.png")
  );

  private int currentIndex = -1;
  private double progressSave = 0;
  private boolean isPlaying = false;
  private Duration duration;

  private MediaPlayer player;
  private ArrayList<MediaPlayer> medias;
  private ArrayList<LocalMusic> arrayMusic;

  public void setArrayMusic(ArrayList<LocalMusic> arrayMusic) {
    this.arrayMusic = arrayMusic;
  }
  
  @Override
  public void initialize() {
    medias = new ArrayList<MediaPlayer>();
    isPlaying = false;
    
    // Click on progressBar
    pgMusicProgress.setOnMouseClicked(e -> {
      double dx = e.getX();
      double dwidth = pgMusicProgress.getWidth();
      double progression = (dx / dwidth);
      double milliseconds = (progression * player.getTotalDuration().toMillis());
      Duration duration = new Duration(milliseconds);
      player.seek(duration);
    });
  }

  /**
   * Function playerOnMusic without path.
   *
   */
  public void playerOnMusic() {
    if (this.arrayMusic.isEmpty()) {
      player.stop();
    } else {
      playerOnMusic(arrayMusic.get(0).getMp3Path());
    }
  }
  
  /**
   * Function Play only one song.
   * @param url : MusicPath
   * @return
   */
  private void playerOnMusic(String url) {

    if (isPlaying) {
      player.stop();
    }

    updateArrayMusic();
    
    for (int i = 0; i < arrayMusic.size(); i++) {
      if (url.equals(arrayMusic.get(i).getMp3Path())) {
        currentIndex = i;
      }
    }

    player = medias.get(currentIndex);
    showSongInfo(arrayMusic.get(currentIndex));

    isPlaying = true;

    player.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable,
                                              Duration oldValue,
                                              Duration newValue) -> {
      duration = player.getMedia().getDuration();
      updateValues();
    });

    player.play();
    play.setGraphic(new ImageView(pauseIcon));

  }

  /**
   * Function creating PlayerList using musicPath.
   * @param url : MusicPath
   * @return
   */
  private MediaPlayer createPlayer(String url) {
    final Media media = new Media(new File(url).toURI().toString());
    return new MediaPlayer(media);
  }

  /**
   * Function Refresh ArrayMusic.
   * @return
   */
  private void updateArrayMusic() {
    medias.clear();

    Stream<LocalMusic> streamMusic = this.arrayMusic.stream();
    /*Stream<LocalMusic> streamMusic = mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getLocalMusics();*/
    arrayMusic = streamMusic.collect(Collectors.toCollection(ArrayList::new));
    arrayMusic.forEach(musicPath -> medias.add(createPlayer(musicPath.getMp3Path())));
  }

  /**
   * Function playBack : play the music before.
   * @return
   */
  @FXML
  private void playBack(ActionEvent e) {
    if (arrayMusic.isEmpty() && arrayMusic.size() == 1) {
      return;
    }
    if (currentIndex != -1) {
      if (currentIndex - 1 >= 0) {
        currentIndex--;
      } else {
        currentIndex = medias.size() - 1;
      }

      playerOnMusic(arrayMusic.get(currentIndex).getMp3Path());
    }
  }

  /**
   * Function playNext: play the next music.
   * @return
   */
  @FXML
  private void playNext(ActionEvent e) {
    if (arrayMusic.isEmpty() && arrayMusic.size() == 1) {
      return;
    }
    if (currentIndex != -1) {
      if (currentIndex + 1 < medias.size()) {
        currentIndex++;
      } else {
        currentIndex = 0;
      }
      playerOnMusic(arrayMusic.get(currentIndex).getMp3Path());
    }
  }

  /**
   * Function playPause: Play and Pause button interactions.
   * @return
   */
  @FXML
  private void playPause(ActionEvent e) {
    if (currentIndex != -1) {
      if (isPlaying) {
        play.setGraphic(new ImageView(playIcon));
        isPlaying = false;
        player.pause();
      } else {
        play.setGraphic(new ImageView(pauseIcon));
        isPlaying = true;
        player.play();
      }
    }
  }

  /**
   * Function updateValues: Update GUI (progressBar and timer).
   * @return
   */
  protected void updateValues() {
    if (pgMusicProgress != null && duration != null) {
      Platform.runLater(() -> {
        Duration currentTime = player.getCurrentTime();

        double totalDuration = arrayMusic.get(this.currentIndex)
            .getMetadata()
            .getDuration()
            .toMillis();
        
        double timer = (currentTime.toMillis() / totalDuration);

        if (timer < 1) {
          lblTime.setText(secToMin((long) player.getCurrentTime().toSeconds()));
          pgMusicProgress.setProgress(timer);
        } else {
          play.setGraphic(new ImageView(playIcon));
          isPlaying = false;
          player.stop();
          pgMusicProgress.setProgress(0.0);
        }

      });
    }
  }

  /**
   *  Function to show musicInfo.
   * @param song : LocalMusic object
   */
  private void showSongInfo(LocalMusic song) {
    if (song != null) {
      songInfo.setText(song.getMetadata().getArtist() + " - " + song.getMetadata().getTitle());
      lblTime.setText("0:00");
      fullTime.setText(secToMin(song.getMetadata().getDuration().getSeconds()));
    } else {
      songInfo.setText("-");
      lblTime.setText("0:00");
      fullTime.setText("-");
    }
  }

  /**
   * Converting time.
   * @param sec : time
   * @return string format
   */
  private String secToMin(long sec) {
    String time = null;
    if ((sec % 60) < 10) {
      time = sec / 60 + ":0" + sec % 60;
    } else {
      time = sec / 60 + ":" + sec % 60;
    }

    return time;
  }

  public MainController getMainController() {
    return mainController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }


}
