package controllers;

import static utils.Converter.convertFrom;

import core.IhmAlert;

import datamodel.LocalMusic;
import datamodel.MusicMetadata;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
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
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import utils.FormatDuration;

/**
 * PlayerMusic Controller.
 */
public class PlayerController implements Controller {

  static final double TIMER = 0.99;

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
  private boolean isLoop = true;
  private boolean isRandom = false;

  private Duration duration;
  private double totalDuration;
  private Random rand = new Random();

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
   * Function to convertMp3ToWav.
   */
  public static void convertMp3ToWav(String url) throws Exception {
    File file = new File(url);
    String filename = file.getName().substring(0, file.getName().indexOf('.'));
    File newFile = new File("/tmp/" + filename + ".wav");

    if (!newFile.exists()) {
      try {
        try (
            InputStream inputStream = new FileInputStream(file);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
        ) {

          AudioInputStream in = (new MpegAudioFileReader()).getAudioInputStream(inputStream);
          AudioFormat baseFormat = in.getFormat();
          AudioFormat newFormat =
              new AudioFormat(
                  baseFormat.getSampleRate(),
                  16,
                  2,
                  true,
                  baseFormat.isBigEndian());

          convertFrom(inputStream).withTargetFormat(newFormat).to(output);

          Files.write(Paths.get(newFile.getPath()), output.toByteArray());
        }
      } catch (IOException | UnsupportedAudioFileException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  /**
   * Function selectOneMusic with index item row.
   *
   * @param currentIndexRow : music index
   * @return
   */
  public void selectOneMusic(int currentIndexRow) {
    if (!isPlaying) {
      currentIndex = currentIndexRow;
      updateArrayMusic();

      showSongInfo(arrayMusic.get(currentIndex));

      player = medias.get(currentIndex);

      showSongInfo(arrayMusic.get(currentIndex));

      player.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable,
                                                Duration oldValue,
                                                Duration newValue) -> {
        duration = player.getMedia().getDuration();
        updateValues();
      });
    }
  }

  /**
   * Function playerOneMusic with index item row.
   *
   * @param currentIndexRow : music index
   * @return
   */
  public void playOneMusic(int currentIndexRow) {
    currentIndex = currentIndexRow;
    updateArrayMusic();
    playerOnMusic();
  }

  /**
   * Function Play only one song.
   */
  private void playerOnMusic() {

    stopPlayer();

    player = medias.get(currentIndex);

    showSongInfo(arrayMusic.get(currentIndex));

    totalDuration = arrayMusic.get(this.currentIndex)
        .getDuration()
        .toMillis();

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
    Media media = null;
    File file = new File(url);
    String filename = file.getName().substring(0, file.getName().indexOf('.'));

    if (System.getProperty("os.name").toLowerCase().contains("nux")
        || System.getProperty("os.name").toLowerCase().contains("nix")
        || System.getProperty("os.name").toLowerCase().contains("mac")) {

      try {
        convertMp3ToWav(file.getPath());
      } catch (Exception e) {
        e.printStackTrace();
      }
      File newFile = new File("/tmp/" + filename + ".wav");
      media = new Media(newFile.toURI().toString());

    } else {
      media = new Media(file.toURI().toString());
    }

    return new MediaPlayer(media);
  }

  /**
   * Function Refresh ArrayMusic.
   *
   * @return
   */
  public void updateArrayMusic() {

    String saveTitle = "NOTHING";

    if (!arrayMusic.isEmpty() && currentIndex != -1 && currentIndex < arrayMusic.size()) {
      saveTitle = arrayMusic.get(currentIndex).getTitle();
    }

    medias.clear();
    arrayMusic.clear();

    ArrayList<LocalMusic> arrayMusicAll =
        mainController
            .getCentralFrameController()
            .getMyMusicsController()
            .getLocalMusicInView();

    int localCurrentMusic = 0;

    for (LocalMusic musicMetadata : arrayMusicAll) {
      medias.add(createPlayer(musicMetadata.getMp3Path()));
      arrayMusic.add(musicMetadata.getMetadata());
      if (!arrayMusic.get(localCurrentMusic).getTitle()
              .equals(saveTitle)) {
        localCurrentMusic++;
      }
    }

    /* Removed because of bug when changing playlist

    // change currentINDEX (header tableView click event)
    if (! saveTitle.equals("NOTHING")) {
      currentIndex = localCurrentMusic;
    }
    */
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
      play.setGraphic(new ImageView(playIcon));
      isPlaying = false;
      songInfo.setText(" - ");
      lblTime.setText("0:00");
      pgMusicProgress.setProgress(0.0);
      fullTime.setText("0:00");
    }
  }

  /**
   * Function loopPlayer: Play all playlist.
   *
   */
  public void loopPlayer() {
    isLoop = !isLoop;
  }

  /**
   * Function randomPlayer : play random music.
   *
   */
  public void randomPlayer() {
    isRandom = !isRandom;
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

        double timer = (currentTime.toMillis() / totalDuration);

        if (timer < TIMER) {
          lblTime.setText(secToMin((long) player.getCurrentTime().toSeconds()));
          pgMusicProgress.setProgress(timer);
        } else {
          play.setGraphic(new ImageView(playIcon));
          isPlaying = false;
          player.stop();
          pgMusicProgress.setProgress(0.0);

          if (isLoop && isRandom) {
            playOneMusic(rand.nextInt(arrayMusic.size()));
          } else {
            if (isLoop && !isRandom) {
              playNext(null);
            }
          }

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
      if (song.getArtist() == null || song.getArtist().trim().equals("")) {
        songInfo.setText(song.getTitle());
      } else {
        songInfo.setText(song.getArtist() + " - " + song.getTitle());
      }
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

  /**
   * getCurrentMusicTitle : get current music title.
   *
   * @return string format
   */
  public String getCurrentMusicTitle() {
    if (!arrayMusic.isEmpty() && currentIndex != -1) {
      return arrayMusic.get(currentIndex).getTitle();
    }
    return "";
  }

}