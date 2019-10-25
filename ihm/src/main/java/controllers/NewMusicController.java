package controllers;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.lang.ref.Reference;
import java.time.LocalDate;

/**
 * Pop-up a view when the user want to add a music from a local file
 */
public class NewMusicController implements Controller {

  private MyMusicsController myMusicsController;

  @FXML
  private TextField textTitle;
  @FXML
  private TextField textArtist;
  @FXML
  private TextField textAlbum;
  @FXML
  private TextField textUploader;
  @FXML
  private Spinner<Integer> dateYear;

  @FXML
  private RadioButton radioPublic;
  @FXML
  private RadioButton radioPrivate;
  ToggleGroup shareStatusGroup;

  @FXML
  private Button buttonFileChoice;

  @FXML
  private Button buttonAdd;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
    this.shareStatusGroup = new ToggleGroup();
    this.radioPrivate.setToggleGroup(this.shareStatusGroup);
    // Private => not shared => false
    this.radioPrivate.setUserData(false);
    this.radioPublic.setToggleGroup(this.shareStatusGroup);
    // Public => shared => true
    this.radioPublic.setUserData(true);
    this.radioPublic.setSelected(true);

    this.dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, LocalDate.now().getYear(), LocalDate.now().getYear(), 1));
    this.dateYear.setEditable(true);

  }

  public MyMusicsController getMyMusicsController() {
    return myMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  @FXML
  private void chooseFile(ActionEvent event) {
    System.out.println("Choose file");
  }

  @FXML
  private void titleEdited(KeyEvent event) {
    if (this.textTitle.getText().isEmpty()) {
      this.textTitle.setStyle("-fx-control-inner-background: red");
    } else {
      this.textTitle.setStyle(null);
    }
  }

  @FXML
  private void artistEdited(KeyEvent event) {
    if (this.textArtist.getText().isEmpty()) {
      this.textArtist.setStyle("-fx-control-inner-background: red");
    } else {
      this.textArtist.setStyle(null);
    }

  }

  @FXML
  private void albumEdited(KeyEvent event) {
    if (this.textAlbum.getText().isEmpty()) {
      this.textAlbum.setStyle("-fx-control-inner-background: red");
    } else {
      this.textAlbum.setStyle(null);
    }
  }

  private Boolean verifyFieldNotEmpty(Boolean valid, Reference<TextField> f) {
    if (f.get().getText().isEmpty()) {
      f.get().setStyle("-fx-control-inner-background: red");
      valid = false;
    }
    return valid;
  }

  @FXML
  private void add(ActionEvent event) {
    System.out.println("Add new music :");
    System.out.println("Title : " + textTitle.getText());
    System.out.println("Artist : " + textArtist.getText());
    System.out.println("Album : " + textAlbum.getText());
    System.out.println("Uploader text : " + textUploader.getText());
    System.out.println("Share status : " + ((Boolean)shareStatusGroup.getSelectedToggle().getUserData() ? "Public" : "Private"));
    System.out.println("Date : " + dateYear.getValue());

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

    if (valid){
      System.out.println("Entry valid");

      /*
      TODO : Fill the MusicMetadata and send it to data
      TODO : Exit the window
       */

    } else {
      System.out.println("Entry not valid");
    }

  }

}
