package controllers;

import datamodel.LocalUser;
import datamodel.Music;
import datamodel.MusicMetadata;
import datamodel.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import utils.FormatDuration;
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
  @FXML
  private Label title;

  @FXML
  private Button btnManageFriendship;

  @FXML
  private TableView<MusicMetadata> tvMusics;
  @FXML
  private TableColumn<MusicMetadata, String> artistCol;
  @FXML
  private TableColumn<MusicMetadata, String> titleCol;
  @FXML
  private TableColumn<MusicMetadata, String> albumCol;
  @FXML
  private TableColumn<MusicMetadata, String> durationCol;

  private SearchMusicController searchMusicController;
  private CentralFrameController centralFrameController;

  // Distant user musics hashmap to access them instantly
  private HashMap<String, Music> distantUserMusics;

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

  private void setLabelTitle(String pseudo) {
    this.title.setText(String.format("Profil de %s", pseudo));
  }

  public void setSearchMusicController(SearchMusicController searchMusicController) {
    this.searchMusicController = searchMusicController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  public void setDistantUser(User user) {
    this.distantUser = user;
    this.setLabelTitle(distantUser.getUsername());
    FormatImage.cropAvatar(distantUser.getAvatar(), imgAvatar);
    this.setPseudo(distantUser.getUsername());
    this.setNameAndSurname(distantUser.getFirstName(), distantUser.getLastName());
    this.setDateOfBirth(distantUser.getDateOfBirth());
    this.displayDistantUserMusics();
  }

  /* Initialisation methods */

  @Override
  public void initialize() {
    distantUserMusics = new HashMap<String, Music>();
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

    // "artist", "title", "album", "duration" refer to MusicMetaData attributes
    this.artistCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("artist"));
    this.titleCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("title"));
    this.albumCol.setCellValueFactory(new PropertyValueFactory<MusicMetadata, String>("album"));

    // Duration MusicMetaData attribute has type Duration
    // so we need to convert it to a string
    this.durationCol
        .setCellValueFactory(
            new Callback<TableColumn.CellDataFeatures<MusicMetadata, String>,
                ObservableValue<String>>() {
              public ObservableValue<String> call(
                  TableColumn.CellDataFeatures<MusicMetadata, String> metadata) {
                return new SimpleStringProperty(
                    FormatDuration.run(metadata.getValue().getDuration())
                );
              }
            });

    try {
      this.displayDistantUserMusics();
    } catch (UnsupportedOperationException e) {
      logger.error(e);
    }

    refreshFriendshipStatus();
  }

  /* FXML methods (to handle events from user) */

  /**
   * Action executed upon clicking friendship button. Add or deletes friend.
   * @param event Click event
   */
  @FXML
  public void manageFriendship(ActionEvent event) {
    LocalUser currentUser = this.getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser();
    if (currentUser.getFriends().contains(distantUser)) {
      currentUser.removeFriend(distantUser);
      logger.info("Removed "
              + distantUser.getUsername()
              + " from friendlist of "
              + currentUser.getUsername()
      );
    } else {
      currentUser.addFriend(distantUser);
      logger.info("Added "
              + distantUser.getUsername()
              + " to friendlist of "
              + currentUser.getUsername()
      );
    }

    refreshFriendshipStatus();

    this
            .getCentralFrameController()
            .getMainController()
            .getContactListController()
            .displayContacts();
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

  /**
   * Refresh the table by retrieving local musics from Data.
   */
  public void displayDistantUserMusics() {
    List<MusicMetadata> listMusics = this.retrieveDistantUserMusics();
    tvMusics.getItems().setAll(listMusics);
  }

  /**
   * Retrieve the public musics from a given distant user.
   */
  private List<MusicMetadata> retrieveDistantUserMusics() {
    distantUserMusics.clear();
    // Retrieve all musics from current user (no matter the shared status)
    // and musics from other connected users that are public or shared to him
    // and apply a filter to get only public musics
    List<Music> distantUserMusicsList = this
        .getCentralFrameController()
        .getMainController()
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getMusics()
        .filter(music -> music.getOwners().contains(distantUser))
        .collect(Collectors.toList());

    List<MusicMetadata> distantUserMusicsMetaData = new ArrayList<>();
    for (Music music : distantUserMusicsList) {
      distantUserMusics.put(music.getMetadata().getHash(), music);
      distantUserMusicsMetaData.add(music.getMetadata());
    }

    return distantUserMusicsMetaData;
  }

  /**
   * Refreshes friendship status to display correct text.
   * Only used to refresh the button's text on distant user's profile
   */
  private void refreshFriendshipStatus() {
    LocalUser currentUser = this.getCentralFrameController()
            .getMainController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser();

    if (!currentUser.getFriends().contains(distantUser)) {
      btnManageFriendship.setText("Ajouter ce contact");
    } else {
      btnManageFriendship.setText("Supprimer ce contact");
    }

    logger.info(currentUser.getFriends());

  }
}
