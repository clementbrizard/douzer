package controllers;

import datamodel.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import utils.FormatImage;

public class DistantUserController implements Controller {
  /* Logger */
  private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();
  
  @FXML
  private ImageView imgAvatar;

  @FXML
  private Pane paneImgAvatar;

  @FXML
  private Label pseudo;

  @FXML
  private Label nameAndSurname;

  @FXML
  private Label dateOfBirth;

  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  private User distantUser;

  /* Getters */

  public SearchMusicController getSearchMusicController() {
    return searchMusicController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  public User getDistantUser() {
    return distantUser;
  }

  /* Setters */

  private void setPseudo(String pseudo) {
    this.pseudo.setText(pseudo);
  }

  private void setNameAndSurname(String name, String surname) {
    this.nameAndSurname.setText(String.format("%s %s", name, surname));
  }

  private void setDateOfBirth(LocalDate dateOfBirth) {  
    DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
    if (dateOfBirth.isEqual(LocalDate.MIN)) {
      this.dateOfBirth.setVisible(false);
    } else {
      this.dateOfBirth.setVisible(true);
      this.dateOfBirth.setText(String.format("Né(e) le %s", dateOfBirth.format(formatter)));
    }
  }

  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  public void setDistantUser(User user) {
    this.distantUser = user;
    FormatImage.cropAvatar(distantUser.getAvatar(), imgAvatar);
    this.setPseudo(distantUser.getUsername());
    this.setNameAndSurname(distantUser.getFirstName(), distantUser.getLastName());
    this.setDateOfBirth(distantUser.getDateOfBirth());
  }

  /* Initialisation methods */

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  /**
   * Initialize the fields with default data.
   */
  public void init() {

    // Show avatar by default
    double circleRadius = 45;
    paneImgAvatar.setMinWidth(circleRadius * 2);
    paneImgAvatar.setMinHeight(circleRadius * 2);
    imgAvatar.setFitHeight(circleRadius * 2);
    imgAvatar.setFitWidth(circleRadius * 2);

    WritableImage croppedImage = imgAvatar.snapshot(null, null);

    // Add a clip to have a circled avatar image
    Circle clip = new Circle(imgAvatar.getFitHeight() / 2,
        imgAvatar.getFitWidth() / 2,
        circleRadius);
    paneImgAvatar.setClip(clip);
  }

  /* FXML methods (to handle events from user) */

  @FXML
  public  void manageFriendship(ActionEvent event) {
    // TODO
  }

  @FXML
  public void changeFrameToMyMusics(ActionEvent event) {
    this.centralFrameController.setCentralContentMyMusics();
  }

  @FXML
  public void changeFrameToAllMusics(ActionEvent event) {
    this.centralFrameController.setCentralContentAllMusics();
  }

  /* Logic methods */
}
