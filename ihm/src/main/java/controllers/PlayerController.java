package controllers;

import static utils.Converter.convertFrom;

import core.IhmAlert;

import datamodel.LocalMusic;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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

  @FXML
  private Slider pgMusicProgress;

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

  private MediaPlayer player;

  // check and play music
  private ArrayList<LocalMusic> playList;
  private LocalMusic previewMusic;

  @Override
  public void initialize() {
    initPictures();
    playList = new ArrayList<>();
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
   * Function to createMediaLinux
   */
  private void createMediaLinux(LocalMusic music) {
    if (music != null) {
      File file = new File(music.getMp3Path());
      Media pick = new Media(file.toURI().toString());
      player = new MediaPlayer(pick);

      if (System.getProperty("os.name").toLowerCase().contains("nux")
          || System.getProperty("os.name").toLowerCase().contains("nix")
          || System.getProperty("os.name").toLowerCase().contains("mac")) {

        String filename = file.getName().substring(0, file.getName().indexOf('.'));

        File newFile = new File("/tmp/" + filename + ".wav");

        pick = new Media(newFile.toURI().toString());

        player = new MediaPlayer(pick);
      }
    }
  }

  /**
   * Function to createMedia.
   * GUI update.
   */
  private void createMedia(LocalMusic music) {
    if (music != null) {

      File file = new File(music.getMp3Path());
      Media pick = new Media(file.toURI().toString());
      player = new MediaPlayer(pick);

      player.setOnEndOfMedia(() -> playNext(null));

      player.setOnReady(() -> pgMusicProgress.setMax(player.getMedia().getDuration().toSeconds()));

      player.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable,
                                                Duration oldValue,
                                                Duration newValue) -> {

        Platform.runLater(() -> {
          lblTime.setText(secToMin((long) newValue.toSeconds()));
          pgMusicProgress.setValue(newValue.toSeconds());
        });

      });
      
      pgMusicProgress.setOnMousePressed(event -> {
        player.pause();
      });

      pgMusicProgress.setOnMouseReleased(event -> {
        if (player != null) {
          player.seek(Duration.seconds(pgMusicProgress.getValue()));
          player.play();
        } else {
          pgMusicProgress.setValue(0.0);
        }
      });

    }
  }

  /**
   * Function playerOneMusic with index item row.
   */
  public void playOneMusic(ArrayList<LocalMusic> musics, int index) {
    if (musics != null) {

      if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
        player.stop();
      }

      if (!playList.isEmpty()) {
        playList.clear();
      }

      playList.addAll(musics);
      currentIndex = index;

      LocalMusic item = playList.get(index);
      showSongInfo(item);

      createMedia(item);

      player.play();
      play.setGraphic(new ImageView(pauseIcon));

    }
  }

  /**
   * Function playPause: Play and Pause button interactions.
   *
   * @return
   */
  @FXML
  private void playPause(ActionEvent e) {
    if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {

      play.setGraphic(new ImageView(playIcon));
      player.pause();

    } else {
      if (!songInfo.getText().equals("-")) {
        play.setGraphic(new ImageView(pauseIcon));

        if (playList.isEmpty() && previewMusic != null) {
          ArrayList<LocalMusic> tmp = new ArrayList<LocalMusic>();
          tmp.add(previewMusic);
          playOneMusic(tmp,0);
          previewMusic = null;
        } else {
          player.play();
        }

      }
    }
  }

  /**
   * Function playNext: play the next music.
   */
  @FXML
  private void playNext(ActionEvent e) {
    if (playList != null && !playList.isEmpty() && currentIndex != -1) {

      if (currentIndex + 1 < playList.size()) {
        currentIndex++;
      } else {
        currentIndex = 0;
      }

      playerOnMusic();
    }

  }

  /**
   * Function playBack : play the music before.
   *
   * @return
   */
  @FXML
  private void playBack(ActionEvent e) {

    if (playList != null && !playList.isEmpty() && currentIndex != -1) {

      if (currentIndex - 1 >= 0) {
        currentIndex--;
      } else {
        currentIndex = playList.size() - 1;
      }

      playerOnMusic();
    }
  }

  /**
   * Function Play only one song.
   */
  private void playerOnMusic() {
    if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
      player.stop();
    }

    LocalMusic item = playList.get(currentIndex);
    showSongInfo(item);

    createMedia(item);

    player.play();
    play.setGraphic(new ImageView(pauseIcon));
  }

  /**
   * Function stopPlayer: Stop player.
   *
   * @return
   */
  public void stopPlayer() {
    if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {

      play.setGraphic(new ImageView(playIcon));
      player.stop();

      if (!playList.isEmpty()) {
        playList.clear();
        player.dispose();
      }

      fullTime.setText("0:00");
      lblTime.setText("0:00");

      songInfo.setText("-");
      pgMusicProgress.setValue(0.0);
    }
  }

  /**
   * Function to call.
   *
   * @param song : LocalMusic object
   */
  private void specialCall(LocalMusic song) {
    if (System.getProperty("os.name").toLowerCase().contains("nux")
        || System.getProperty("os.name").toLowerCase().contains("nix")
        || System.getProperty("os.name").toLowerCase().contains("mac")) {

      File file = new File(song.getMp3Path());

      Platform.runLater(() -> {
        try {
          convertMp3ToWav(file.getPath());
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

    }
  }

  /**
   * Function to show musicInfo.
   *
   * @param song : LocalMusic object
   */
  public void showSongInfo(LocalMusic song) {
    if (song != null ) {
        previewMusic = song; // preview song

        // linux, mac convert mp3
        specialCall(song);

        if (song.getMetadata().getArtist() != null) {
          songInfo.setText(song.getMetadata().getArtist() + " - " + song.getMetadata().getTitle());
        } else {
          songInfo.setText(song.getMetadata().getTitle());
        }
        lblTime.setText("0:00");
        fullTime.setText(FormatDuration.run(song.getMetadata().getDuration()));
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
  public LocalMusic getCurrentMusic() {
    if (!playList.isEmpty() && currentIndex != -1) {
      return playList.get(currentIndex);
    }
    return null;
  }

  public MediaPlayer getPlayer() {
    return player;
  }
}