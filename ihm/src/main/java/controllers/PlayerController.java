package controllers;

import core.IhmAlert;
import datamodel.LocalMusic;
import datamodel.MusicMetadata;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import utils.FormatDuration;

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

  private Image pauseIcon = null;
  private Image playIcon = null;

  private int currentIndex = -1;
  private boolean isPlaying = false;

  private Duration duration;

  private MediaPlayer player;
  private ArrayList<MediaPlayer> medias;
  private ArrayList<MusicMetadata> arrayMusic;

  @Override
  public void initialize() {

    initPictures();
    medias = new ArrayList<>();
    arrayMusic = new ArrayList<>();
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
   * Function to init Pictures Path.
   */
  public void initPictures() {

    URL pauseIconFile = getClass().getResource("/images/pauseSymbol.png");
    URL playIconFile = getClass().getResource("/images/playSymbol.png");

    try {
      playIcon = new Image(playIconFile.openStream());
      pauseIcon = new Image(pauseIconFile.openStream());

    } catch (Exception e) {
      IhmAlert.showAlert("Pictures Load", "Fail : picture load PLAYER", "critical");
    }

  }

  /**
   * Function playerOneMusic with index item row.
   *
   * @param currentIndexRow : music index
   * @return
   */
  public void playOneMusic(int currentIndexRow) {
    updateArrayMusic();
    currentIndex = currentIndexRow;
    playerOnMusic();
  }

  /**
   * Function Play only one song.
   */
  private void playerOnMusic() {

    stopPlayer();

    System.out.println("medioas size" + medias.size());
    System.out.println("currentIndex" + currentIndex);
    System.out.println("medias" + medias.get(currentIndex).toString());

    player = medias.get(currentIndex);

    /*if (!System.getProperty("os.name").toLowerCase().contains("win")) {
      player.setCycleCount(MediaPlayer.INDEFINITE);
    }*/

    if (player == null) {
      System.out.println("OUPS NULL");
      return;
    }

    showSongInfo(arrayMusic.get(currentIndex));

    player.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable,
                                              Duration oldValue,
                                              Duration newValue) -> {
      duration = player.getMedia().getDuration();
      updateValues();
    });

    player.play();
    isPlaying = true;
    play.setGraphic(new ImageView(pauseIcon));
  }

  /**
   * Function creating PlayerList using musicPath.
   *
   * @param url : MusicPath
   * @return
   */
  private MediaPlayer createPlayer(String url) {
    final Media media = new Media(new File(url).toURI().toString());
    return new MediaPlayer(media);
  }

  /**
   * Function Refresh ArrayMusic.
   *
   * @return
   */
  private void updateArrayMusic() {
    medias.clear();
    arrayMusic.clear();

    ArrayList<LocalMusic> arrayMusicAll;

    arrayMusicAll =
        mainController
            .getCentralFrameController()
            .getMyMusicsController()
            .getLocalMusicInView();

    arrayMusicAll.forEach(musicMetadata ->
        medias.add(createPlayer(musicMetadata.getMp3Path())));
    arrayMusicAll.forEach(musicMetadata ->
        arrayMusic.add(musicMetadata.getMetadata()));
  }

  /**
   * Function playBack : play the music before.
   *
   * @return
   */
  @FXML
  private void playBack(ActionEvent e) {

    if (arrayMusic != null && !arrayMusic.isEmpty() && currentIndex != -1) {

      if (currentIndex - 1 >= 0) {
        currentIndex--;
      } else {
        currentIndex = medias.size() - 1;
      }

      playerOnMusic();
    }
  }

  /**
   * Function playNext: play the next music.
   *
   * @return
   */
  @FXML
  private void playNext(ActionEvent e) {
    if (arrayMusic != null && !arrayMusic.isEmpty() && currentIndex != -1) {

      if (currentIndex + 1 < medias.size()) {
        currentIndex++;
      } else {
        currentIndex = 0;
      }

      playerOnMusic();
    }
  }

  /**
   * Function playPause: Play and Pause button interactions.
   *
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
   * Function stopPlayer: Stop player.
   *
   * @return
   */
  public void stopPlayer() {
    if (isPlaying) {
      player.stop();
    }
  }

  /**
   * Function updateValues: Update GUI (progressBar and timer).
   *
   * @return
   */
  protected void updateValues() {
    if (pgMusicProgress != null && duration != null) {
      Platform.runLater(() -> {

        Duration currentTime = player.getCurrentTime();

        double totalDuration = arrayMusic.get(this.currentIndex)
            .getDuration()
            .toMillis();

        double timer = (currentTime.toMillis() / totalDuration);

        if (timer < 0.99) {
          lblTime.setText(secToMin((long) player.getCurrentTime().toSeconds()));
          pgMusicProgress.setProgress(timer);
        } else {
          play.setGraphic(new ImageView(playIcon));
          isPlaying = false;
          player.stop();
          pgMusicProgress.setProgress(0.0);
          playNext(null);
        }

      });
    }
  }

  /**
   * Function to show musicInfo.
   *
   * @param song : LocalMusic object
   */
  private void showSongInfo(MusicMetadata song) {
    if (song != null) {
      songInfo.setText(song.getArtist() + " - " + song.getTitle());
      lblTime.setText("0:00");
      fullTime.setText(FormatDuration.run(song.getDuration()));
    } else {
      songInfo.setText("-");
      lblTime.setText("0:00");
      fullTime.setText("-");
    }
  }

  /**
   * Converting time.
   *
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