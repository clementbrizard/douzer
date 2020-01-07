package controllers;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import datamodel.MusicMetadata;
import datamodel.ShareStatus;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Year;
import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

/**
 * Pop-up a view when the user want to add a music from a local file.
 */
public class NewMusicController implements Controller {
  private static final Logger newMusicLogger = LogManager.getLogger();

  @FXML
  private TextField textFile;

  /**
   * TextField containing the music title.
   */
  @FXML
  private TextField textTitle;

  /**
   * TextField containing the music artist.
   */
  @FXML
  private TextField textArtist;

  /**
   * TextField containing the music album.
   */
  @FXML
  private TextField textAlbum;

  /**
   * Spinner for the year.
   */
  @FXML
  private Spinner<Integer> dateYear;

  /**
   * RadioButton to select public sharing.
   */
  @FXML
  private RadioButton radioPublic;

  /**
   * RadioButton to select friends sharing.
   */
  @FXML
  private RadioButton radioFriends;

  /**
   * RadioButton to select private sharing (no sharing).
   */
  @FXML
  private RadioButton radioPrivate;

  /**
   * Share radio button group.
   */
  private ToggleGroup shareStatusGroup;

  /**
   * TextField to add a new tag.
   */
  @FXML
  private TextField textNewTag;

  /**
   * ListView of all added tags.
   */
  @FXML
  private ListView<String> listViewTags;

  /**
   * List of tags.
   */
  private ObservableList<String> tags;

  /**
   * Mp3 file.
   */
  private File file;

  /**
   * Boolean to check if a file was chosen by the user.
   */
  private Boolean hasChosenFile;

  /**
   * Reference to parent controller.
   */
  private MyMusicsController myMusicsController;

  /**
   * the metadata of the selected music.
   */
  private MusicMetadata meta;

  // Getters

  /**
   * Get the parent controller.
   *
   * @return Parent controller : MyMusicController
   */
  public MyMusicsController getMyMusicsController() {
    return myMusicsController;
  }

  /**
   * Set the parent controller (MyMusicController).
   * Needed to setup the tree organization.
   *
   * @param myMusicsController Parent controller
   */
  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  // Other methods

  /**
   * Initialize the controller.
   */
  @Override
  public void initialize() {
    this.shareStatusGroup = new ToggleGroup();
    this.radioPrivate.setToggleGroup(this.shareStatusGroup);
    this.radioFriends.setToggleGroup(this.shareStatusGroup);
    this.radioPublic.setToggleGroup(this.shareStatusGroup);

    this.radioPrivate.setUserData(true);
    this.radioPrivate.setSelected(true);

    this.dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
        1000, LocalDate.now().getYear(), LocalDate.now().getYear(), 1));
    this.dateYear.setEditable(true);

    this.tags = FXCollections.observableArrayList();
    this.listViewTags.setItems(tags);

    this.textFile.setEditable(false);
    this.hasChosenFile = false;
  }

  
  /**
   * clear all field in the view.
   */
  private void clear() {

    this.file = null;
    this.hasChosenFile = false;
    this.tags.clear();
    this.textAlbum.setText("");
    this.textArtist.setText("");
    this.textFile.setText("");
    this.textNewTag.setText("");
    this.textTitle.setText("");
  }

  /**
   * Open a window to choose the music file.
   * Action method after click on choose file button.
   *
   * @param event Not used
   */
  @FXML
  private void chooseFile(ActionEvent event) {

    FileChooser fileChooser = new FileChooser();

    // Add extension here if needed
    FileChooser.ExtensionFilter audioExtensionFilter =
            new FileChooser.ExtensionFilter(
                    "Fichier audio",
                    "*.mp3"
            );
    fileChooser.getExtensionFilters().add(audioExtensionFilter);

    fileChooser.setTitle("Ouvrir un fichier de musique");

    Stage stage = (Stage) this.textFile.getScene().getWindow();

    this.file = fileChooser.showOpenDialog(stage);
    if (this.file != null) {
      this.hasChosenFile = true;
      this.textFile.setText(this.file.getAbsolutePath());
      this.textFile.setStyle(null);

      try {
        meta = myMusicsController.getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .parseMusicMetadata(file.getAbsolutePath());

        if (meta.getAlbum() != null) {
          this.textAlbum.setText(meta.getAlbum());
        } else {
          this.textAlbum.setText("");
        }
        if (meta.getArtist() != null) {
          this.textArtist.setText(meta.getArtist());
        } else {
          this.textArtist.setText("");
        }
        if (meta.getTitle() != null) {
          this.textTitle.setText(meta.getTitle());
        } else {
          this.textTitle.setText("");
        }
        if (meta.getReleaseYear() != null) {
          this.dateYear.setValueFactory(new SpinnerValueFactory
              .IntegerSpinnerValueFactory(1000,
              LocalDate.now().getYear(),
              meta.getReleaseYear().getValue()));
        } else {
          this.dateYear.setValueFactory(new SpinnerValueFactory
              .IntegerSpinnerValueFactory(1000,
              LocalDate.now().getYear(),
              LocalDate.now().getYear()));
        }

      } catch (IOException e) {
        newMusicLogger.error(e);
        Notifications.create()
            .title("Ajout de la musique raté")
            .text("le fichier selectionné ne correspond pas au bon format")
            .darkStyle()
            .showWarning();
        return;
      } catch (UnsupportedTagException e) {
        newMusicLogger.error(e);
        Notifications.create()
            .title("Ajout de la musique raté")
            .text("les tags ne correspondent pas au fichier")
            .darkStyle()
            .showWarning();
        return;
      } catch (InvalidDataException e) {
        Notifications.create()
            .title("Ajout de la musique raté")
            .text("des erreurs dans le format de données on été detectées")
            .darkStyle()
            .showWarning();
        newMusicLogger.error(e);
        return;
      } catch (NoSuchAlgorithmException e) {
        newMusicLogger.error(e);
        return;
      }

    } else {
      if (this.file == null) {
        clear();
      }
    }
  }

  /**
   * Update the TextField style if it's empty or not.
   * Action method on key pressed on Title FieldText.
   *
   * @param event Not used
   */
  @FXML
  private void titleEdited(KeyEvent event) {
    if (this.textTitle.getText().isEmpty()) {
      this.textTitle.setStyle("-fx-control-inner-background: red");
    } else {
      this.textTitle.setStyle(null);
    }
  }

  /**
   * Update the TextField style if it's empty or not.
   * Action method on key pressed on Artist FieldText.
   *
   * @param event Not used
   */
  @FXML
  private void artistEdited(KeyEvent event) {
    if (this.textArtist.getText().isEmpty()) {
      this.textArtist.setStyle("-fx-control-inner-background: red");
    } else {
      this.textArtist.setStyle(null);
    }
  }

  /**
   * Update the TextField style if it's empty or not.
   * Action method on key pressed on Album FieldText.
   *
   * @param event Not used
   */
  @FXML
  private void albumEdited(KeyEvent event) {
    if (this.textAlbum.getText().isEmpty()) {
      this.textAlbum.setStyle("-fx-control-inner-background: red");
    } else {
      this.textAlbum.setStyle(null);
    }
  }

  /**
   * Add the entered tag to the tag list.
   * Action method on enter on new tag field.
   * Action method on click on new tag button.
   *
   * @param event Not used
   */
  @FXML
  private void addTag(ActionEvent event) {
    if (!this.textNewTag.getText().isEmpty() && !this.tags.contains(this.textNewTag.getText())) {
      this.tags.add(this.textNewTag.getText().trim());
      this.textNewTag.clear();
    }
  }

  /**
   * Delete the selected tag (if the key pressed is DELETE or BACK_SPACE).
   *
   * @param event KeyEvent corresponding to the key pressed
   */
  @FXML
  private void tagListKeyPressed(KeyEvent event) {
    if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
      String selected = this.listViewTags.getSelectionModel().getSelectedItem();
      this.tags.removeAll(selected);
    }
  }

  /**
   * Check the given information and add the new music through Data.
   * Action method on click on Add music Button.
   *
   * @param event Not used
   */
  @FXML
  private void add(ActionEvent event) {
    // Check the validity of the fields
    // Put in red the background of non valid fields
    Boolean valid = true;
    if (textTitle.getText().isEmpty()) {
      textTitle.setStyle("-fx-control-inner-background: red");
      valid = false;
    }
    if (textArtist.getText().isEmpty()) {
      textArtist.setStyle("-fx-control-inner-background: red");
      valid = false;
    }
    if (textAlbum.getText().isEmpty()) {
      textAlbum.setStyle("-fx-control-inner-background: red");
      valid = false;
    }
    if (!hasChosenFile) {
      textFile.setStyle("-fx-control-inner-background: red");
      valid = false;
    }

    // Add music if valid
    if (valid) {

      meta.setTitle(textTitle.getText());
      meta.setArtist(textArtist.getText());
      meta.setAlbum(textAlbum.getText());
      meta.setReleaseYear(Year.of(dateYear.getValue()));
      meta.getTags().addAll(tags);

      ShareStatus shareStatus = ShareStatus.PRIVATE;
      if (this.radioPublic.isSelected()) {
        shareStatus = ShareStatus.PUBLIC;
      } else if (this.radioFriends.isSelected()) {
        shareStatus = ShareStatus.FRIENDS;
      }

      try {
        myMusicsController.getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .addMusic(meta, file.getAbsolutePath(), shareStatus);

        this.getMyMusicsController().displayAvailableMusics();
        this.getMyMusicsController().getCentralFrameController().getAllMusicsController()
            .displayAvailableMusics();
        this.getMyMusicsController().getCentralFrameController()
            .getMainController().getMyPlaylistsController().resetSelection();
        this.getMyMusicsController().showPlaylist("Mes morceaux");
        Stage stage = (Stage) this.textFile.getScene().getWindow();
        stage.close();
      } catch (java.io.FileNotFoundException e) {
        newMusicLogger.error("File not found : " + file.getAbsolutePath());
        Notifications.create()
            .title("Ajout de la musique raté")
            .text("le fichier selectionné ne correspond pas au bon format")
            .darkStyle()
            .showWarning();
      }
    } else {
      newMusicLogger.error("Entry not valid");
    }
  }

}
