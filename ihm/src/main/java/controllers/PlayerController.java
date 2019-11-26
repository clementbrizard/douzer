package controllers;

import datamodel.LocalMusic;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * PlayerMusic Controller.
 */
public class PlayerController implements Controller {

  @FXML
  private Label songInfo;

  @FXML
  private Label lblTime;

  @FXML
  private Label fullTime;

  @FXML
  private ImageView ivPlay;

  @FXML
  private ImageView ivPause;

  @FXML
  private ImageView ivNext;

  @FXML
  private ImageView ivBefore;

  @FXML
  private ProgressBar pgMusicProgress;

  private MainController mainController;

  private List<MediaPlayer> players;
  private MediaPlayer mediaPlayer;
  private ArrayList<LocalMusic> arrayMusic;

  private int musicID = 0;

  @Override
  public void initialize() {
    players = new ArrayList<MediaPlayer>();

    ivPlay.setOnMouseClicked(e -> {
      playPauseSong();
    });
    
    playerOnMusic("/");
  }

  /**
   * Function creating PlayerList using musicPath.
   * @param url : MusicPath
   * @return
   */
  private MediaPlayer createPlayer(String url) {
    final Media media = new Media(new File(url).toURI().toString());
    final MediaPlayer player = new MediaPlayer(media);
    System.out.println("[PlayerController]+++++ " + url);
    return player;
  }

  private void playerOnMusic(String url) {
    players.clear();
    players.add(createPlayer(url));
    musicID = 0;
  }
  
  /**
   * Function to interact with musicPlayer : Play, Pause, Next, Previous.
   */
  private void playPauseSong() {

    if (players.size() != 0) {

      updateValues();

      mediaPlayer = players.get(musicID);
      mediaPlayer.setVolume(100);
      mediaPlayer.play();

      showSongInfo(arrayMusic.get(musicID));
      playIcon();

      ivPause.setOnMouseClicked(e -> {
        mediaPlayer.pause();
        pauseIcon();
      });

      ivNext.setOnMouseClicked(e -> {
        if (musicID + 1 < players.size()) {
          musicID++;
        } else {
          musicID = 0;
        }

        pgMusicProgress.setProgress(0.0);
        mediaPlayer.stop();
        playPauseSong();

      });

      ivBefore.setOnMouseClicked(e -> {
        if (musicID - 1 >= 0) {
          musicID--;
        } else {
          musicID = players.size() - 1;
        }

        pgMusicProgress.setProgress(0.0);
        mediaPlayer.stop();

        playPauseSong();

      });
    } else {
      System.out.println("appuie sur start");
      Stream<LocalMusic> streamMusic = mainController
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .getLocalMusics();
      arrayMusic = streamMusic.collect(Collectors.toCollection(ArrayList::new));
      arrayMusic.forEach(musicPath -> players.add(createPlayer(musicPath.getMp3Path())));
    }
  }

  /**
   * Update GUI values.
   */
  private void updateValues() {
    Thread thread = new Thread(() -> {
      do {
        Platform.runLater(() -> {
          if ((mediaPlayer.getStatus() != MediaPlayer.Status.PAUSED)
              && (mediaPlayer.getStatus() != MediaPlayer.Status.STOPPED)
              && (mediaPlayer.getStatus() != MediaPlayer.Status.READY)) {
            lblTime.setText(secToMin((long) mediaPlayer.getCurrentTime().toSeconds()));
            double totalDuration = 180000.0;
            double timer = (mediaPlayer.getCurrentTime().toMillis() / totalDuration);
            pgMusicProgress.setProgress(timer);

          }
        });
        try {
          Thread.sleep(100);
        } catch (InterruptedException ex) {
          break;
        }
      } while (!players.isEmpty());
    });
    thread.start();
  }

  /**
   *  Function to show musicInfo.
   * @param song : LocalMusic object
   */
  private void showSongInfo(LocalMusic song) {
    if (song != null) {
      songInfo.setText(song.getMetadata().getArtist() + " - " + song.getMetadata().getTitle());
      lblTime.setText("0:00");
      fullTime.setText(song.getMetadata().getDuration().toString());
    } else {
      songInfo.setText("-");
      lblTime.setText("0:00");
      fullTime.setText("-");
    }
  }

  /**
   * Update playButton apperance.
   */
  private void playIcon() {
    ivPlay.setVisible(false);
    ivPlay.setDisable(true);
    ivPause.setVisible(true);
    ivPause.setDisable(false);
  }

  /**
   * Update pauseButton apperance.
   */
  private void pauseIcon() {
    ivPause.setVisible(false);
    ivPause.setDisable(true);
    ivPlay.setVisible(true);
    ivPlay.setDisable(false);
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
