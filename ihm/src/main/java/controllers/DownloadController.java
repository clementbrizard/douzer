package controllers;

import datamodel.Music;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

//replace by javadocs
//righdown view with progress bar about current music download
public class DownloadController implements Controller {


  private static final Logger downloadLogger = LogManager.getLogger();

  private MainController mainController;

  @FXML
  private Label lblDownload;

  @FXML
  private ProgressBar progressDownload;

  // Getters

  public MainController getMainController() {
    return mainController;
  }

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  // Other Methods

  @Override
  public void initialize() {
    this.lblDownload.setText("Ceci est un test");
    this.progressDownload.setProgress(0);
    this.progressDownload.setVisible(false);
  }

  /**
   * Download a music. It uses the download method given by Data.
   *
   * @param musicToBeDownloaded the music to be downloaded
   */
  public void download(Music musicToBeDownloaded) {
    try {
      this.mainController
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
   * Update download progressBar progress
   * @param downloadedMusic The downloaded music
   * @param downloadProgress The downloaded music download progress
   */
  public void updateDownloadProgressBar(Music downloadedMusic, int downloadProgress) {
    String downloadedMusicArtist = downloadedMusic.getMetadata().getArtist();
    String downloadedMusicTitle = downloadedMusic.getMetadata().getTitle();
    //A negative progress send by data means a download failure
    if (downloadProgress < 0) {
      Notifications.create()
          .title("Download failed")
          .text(downloadedMusicTitle + " - " + downloadedMusicArtist)
          .darkStyle()
          .showWarning();
      // Clear download information label
      this.lblDownload.setText("");
      // Make download progress bar invisible
      this.progressDownload.setVisible(false);
    } else {
      // Make download progress bar visible
      this.progressDownload.setVisible(true);
      this.progressDownload.setProgress(downloadProgress / 100);
      this.lblDownload
          .setText("Downloading : " + downloadedMusicTitle + " - " + downloadedMusicArtist);
      // We update central views when download is done
      if (downloadProgress == 100) {
        Notifications.create()
            .title("Download done")
            .text(downloadedMusicTitle + " - " + downloadedMusicArtist)
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
        this.progressDownload.setProgress(0);
        // Clear download information label
        this.lblDownload.setText("");
        // Make download progress bar invisible
        this.progressDownload.setVisible(false);
      }
    }
  }
}
