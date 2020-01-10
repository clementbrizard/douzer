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
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
   * Function to createMedia.
   * GUI update.
   */
  private void createMedia(LocalMusic music) {
    if (music != null) {
      File file = new File(music.getMp3Path());
      Media pick = new Media(file.toURI().toString());
      player = new MediaPlayer(pick);

      pgMusicProgress.setMax(music.getMetadata().getDuration().getSeconds());

      player.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable,
                                                Duration oldValue,
                                                Duration newValue) -> {

        if (pgMusicProgress.getMax() == pgMusicProgress.getValue()) {
          pgMusicProgress.setValue(0.0);
          playNext(null);
        }

        Platform.runLater(() -> {
          lblTime.setText(secToMin((long) newValue.toSeconds()));
          pgMusicProgress.setValue(newValue.toSeconds());
        });

      });

      pgMusicProgress.setOnMouseClicked((MouseEvent mouseEvent) -> {
        if(player != null && player.getStatus() == MediaPlayer.Status.PLAYING ) {
          player.seek(Duration.seconds(pgMusicProgress.getValue()));
        } else {
          pgMusicProgress.setValue(0.0);
        }
      });

    }
  }

  /**
   * Function playerOneMusic with index item row.
   *
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
      createMedia(item);

      showSongInfo(item.getMetadata());

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
      if ( !playList.isEmpty() ){
        play.setGraphic(new ImageView(pauseIcon));
        player.play();
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
   * Function selectOneMusic with selected LocalMusic.
   *
   * @param music : LocalMusic
   * @return
   */
  public void selectOneMusic(LocalMusic music) {
    if (music != null && player != null && player.getStatus() == MediaPlayer.Status.STOPPED) {
      showSongInfo(music.getMetadata());
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
    createMedia(item);

    showSongInfo(item.getMetadata());

    player.play();
    play.setGraphic(new ImageView(pauseIcon));
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
   * Function stopPlayer: Stop player.
   *
   * @return
   */
  public void stopPlayer() {
    if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {

      play.setGraphic(new ImageView(playIcon));
      player.stop();

      if(!playList.isEmpty()) {
        playList.clear();
        player.dispose();
      }

      fullTime.setText("0:00");
      lblTime.setText("0:00");

      songInfo.setText(" - ");
      pgMusicProgress.setValue(0.0);
    }
  }

  /**
   * Function to show musicInfo.
   *
   * @param song : LocalMusic object
   */
  public void showSongInfo(MusicMetadata song) {
    if (song != null) {
      if (song.getArtist() != null) {
        songInfo.setText(song.getArtist() + " - " + song.getTitle());
      } else {
        songInfo.setText(song.getTitle());
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
  public LocalMusic getCurrentMusic() {
    if (!playList.isEmpty() && currentIndex != -1) {
      return playList.get(currentIndex);
    }
    return null;
  }

}