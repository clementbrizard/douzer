package controllers;

import datamodel.MusicMetadata;

import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * Pop-up a view when the user want to add a music from a local file.
 * TODO : Do a popup
 */
public class NewMusicController implements Controller {

  /**
   * Reference to parent controller.
   */
  private MyMusicsController myMusicsController;

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
   * TextField containing the uploader.
   */
  @FXML
  private TextField textUploader;
  /**
   * Spinner for the year.
   */
  @FXML
  private Spinner<Integer> dateYear;

  /**
   * RadioButton to select Public share.
   */
  @FXML
  private RadioButton radioPublic;
  /**
   * RadioButton to select Private share (no share).
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
  private ObservableList tags;
  /**
   * Mp3 file.
   */
  private File file;
  /**
   * Is a file chosen.
   */
  private Boolean fileChose;

  /**
   * Initialize the controller.
   */
  @Override
  public void initialize() {
    this.shareStatusGroup = new ToggleGroup();
    this.radioPrivate.setToggleGroup(this.shareStatusGroup);
    // Private => not shared => false
    this.radioPrivate.setUserData(false);
    this.radioPublic.setToggleGroup(this.shareStatusGroup);
    // Public => shared => true
    this.radioPublic.setUserData(true);
    this.radioPublic.setSelected(true);

    this.dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
        1000, LocalDate.now().getYear(), LocalDate.now().getYear(), 1));
    this.dateYear.setEditable(true);

    this.textUploader.setEditable(false);
    /** TODO : get the username from the current session */
    this.textUploader.setText("Maxime");

    this.tags = FXCollections.observableArrayList();
    this.listViewTags.setItems(tags);

    this.textFile.setEditable(false);
    this.fileChose = false;
  }

  /**
   * Gets the parent controller.
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

  /**
   * Opens a window to choose the music file.
   * Action method after click on choose file button.
   *
   * @param event Not used
   */
  @FXML
  private void chooseFile(ActionEvent event) {
    System.out.println("Choose file");
    Stage stage = new Stage();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Ouvrir un fichier de musique");
    /** TODO Fix extension filter not working (on linux at least) */
    fileChooser.setSelectedExtensionFilter(
        new FileChooser.ExtensionFilter("Fichier musique", "*.mp3"));

    this.file = fileChooser.showOpenDialog(stage);
    if (this.file != null) {
      this.fileChose = true;
      this.textFile.setText(this.file.getAbsolutePath());
      this.textFile.setStyle(null);
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
      this.tags.add(this.textNewTag.getText());
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
    // Print the informations
    System.out.println("Add new music :");
    System.out.println("File : " + (fileChose ? file.getAbsolutePath() : "Not set"));
    System.out.println("Title : " + textTitle.getText());
    System.out.println("Artist : " + textArtist.getText());
    System.out.println("Album : " + textAlbum.getText());
    System.out.println("Uploader : " + textUploader.getText());
    System.out.println("Share status : "
        + ((Boolean) shareStatusGroup.getSelectedToggle().getUserData() ? "Public" : "Private"));
    System.out.println("Date : " + dateYear.getValue());
    System.out.print("Tags : ");
    Boolean first = true;
    for (Object tag : this.tags) {
      if (!first) {
        System.out.print(", ");
      } else {
        first = false;
      }
      System.out.print((String) tag);
    }
    System.out.print("\n");

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
    if (!fileChose) {
      textFile.setStyle("-fx-control-inner-background: red");
      valid = false;
    }

    // Add music if valid
    if (valid) {
      System.out.println("Entry valid");

      MusicMetadata meta = new MusicMetadata();
      meta.setTitle(textTitle.getText());
      meta.setArtist(textArtist.getText());
      meta.setAlbum(textAlbum.getText());
      meta.setReleaseDate(new Date(dateYear.getValue()));

      try {
        myMusicsController.getCentralFrameController().getMainController().getIhmCore()
            .getDataForIhm().addMusic(meta, file.getAbsolutePath());

        /*
         TODO : Exit the window
         */

      } catch (java.io.FileNotFoundException e) {
        System.out.println("File not found : " + file.getAbsolutePath());
      }

    } else {
      System.out.println("Entry not valid");
    }
  }


}