package controllers;

import datamodel.Music;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

//replace by javadocs
//righdown view with progress bar about current music download
public class DownloadController implements Controller {


  private static final Logger downloadLogger = LogManager.getLogger();

  private MainController mainController;

  private AllMusicsController allMusicsController;

  private CurrentMusicInfoController currentMusicInfoController;

  // Getters

  public MainController getMainController() {
    return mainController;
  }

  public AllMusicsController getAllMusicsController() {
    return allMusicsController;
  }

  public CurrentMusicInfoController getCurrentMusicInfoController() {
    return currentMusicInfoController;
  }

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  public void setAllMusicsController(AllMusicsController allMusicsController) {
    this.allMusicsController = allMusicsController;
  }

  public void setCurrentMusicInfoController(CurrentMusicInfoController currentMusicInfoController) {
    this.currentMusicInfoController = currentMusicInfoController;
  }

  // Other Methods

  @Override
  public void initialize() {
    this.allMusicsController = mainController.getCentralFrameController().getAllMusicsController();
    this.currentMusicInfoController = mainController.getCurrentMusicInfoController();
  }

  /**
   * Download a music. It uses the download method given by Data.
   *
   * @param musicToBeDownloaded the music to be downloaded
   */
  public void download(Music musicToBeDownloaded) {
    try {
      this.allMusicsController
          .getCentralFrameController()
          .getMainController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .download(musicToBeDownloaded);
    } catch (Exception e) {

      downloadLogger.error(e);

      Notifications.create()
          .title("Download failed")
          .text(musicToBeDownloaded.getMetadata()
              .getTitle() + " - " + musicToBeDownloaded.getMetadata()
              .getArtist())
          .darkStyle()
          .showWarning();

    }
  }

  /**
   * Update download promvn gressBar progress into CurrenMusicIndoController.
   * @param downloadedMusic The downloaded music
   * @param downloadProgress The downloaded music download progress
   */
  public void updateDownloadProgressBar(Music downloadedMusic, int downloadProgress) {
    //A negative progress send by data means a download failure
    if (downloadProgress < 0) {
      Notifications.create()
          .title("Download failed")
          .text(downloadedMusic
              .getMetadata()
              .getTitle() + " - " + downloadedMusic
              .getMetadata()
              .getArtist())
          .darkStyle()
          .showWarning();
    } else {
      this.currentMusicInfoController.getDownloadProgress().setProgress(downloadProgress / 100);
      // We update central views when download is done
      if (downloadProgress == 100) {
        Notifications.create()
            .title("Download done")
            .text(downloadedMusic
                .getMetadata()
                .getTitle() + " - " + downloadedMusic
                .getMetadata()
                .getArtist())
            .darkStyle()
            .showInformation();
        this.mainController
            .getCentralFrameController()
            .getMyMusicsController()
            .init();
        this.mainController
            .getCentralFrameController()
            .getAllMusicsController()
            .init();
        // Reinitialize progress bar with 0
        this.currentMusicInfoController.getDownloadProgress().setProgress(0);
      }
    }
  }
}
