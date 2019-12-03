package controllers;

import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;

//replace by javadocs
//central view to show all information about on music
public class DetailsMusicController implements Controller {

  @FXML
  private TextField textFieldTitre;

  @FXML
  private TextField textFieldArtiste;

  @FXML
  private TextField textFieldAlbum;

  @FXML
  private Spinner<Integer> dateYear;

  @FXML
  private TextField textFieldLastUploader;

  @FXML
  private TextField textFieldAddTag;

  @FXML
  private ListView<String> listViewTagsList;

  private ObservableList<String> tags;

  private LocalMusic localMusic;

  @FXML
  private Button buttonValider;

  @FXML
  private Button buttonAddTag;

  @FXML
  private ImageView imageEtoile1;

  @FXML
  private ImageView imageEtoile2;

  @FXML
  private ImageView imageEtoile3;

  @FXML
  private ImageView imageEtoile4;

  @FXML
  private ImageView imageEtoile5;

  private MyMusicsController myMusicsController;

  private int note = 0;

  public MyMusicsController getMyMusicsController() {
    return this.myMusicsController;
  }

  public void setMyMusicsController(MyMusicsController myMusicsController) {
    this.myMusicsController = myMusicsController;
  }

  /**
   * After the initialisation with initialize() of the controller execute this function with
   * the localMusic where the user has click .
   * @param localMusic the music clicked
   */
  public void initMusic(LocalMusic localMusic) {
    this.localMusic = localMusic;

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getTitle() != null) {
        textFieldTitre.setText(localMusic.getMetadata().getTitle());
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getArtist() != null) {
        textFieldArtiste.setText(localMusic.getMetadata().getArtist());
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getAlbum() != null) {
        textFieldAlbum.setText(localMusic.getMetadata().getAlbum());
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getReleaseYear() != null) {
        dateYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
            1000, LocalDate.now().getYear(), localMusic.getMetadata()
            .getReleaseYear().getValue(), 1));
      }
    }

    if (localMusic.getOwners() != null) {
      Iterator<User> itOwners = localMusic.getOwners().iterator();
      if (itOwners.hasNext()) {
        textFieldLastUploader.setText(itOwners.next().getUsername());
      }
    }

    tags = FXCollections.observableArrayList();

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getTags() != null) {
        Iterator<String> itMusicTag = localMusic.getMetadata().getTags().iterator();
        while (itMusicTag.hasNext()) {
          tags.add(itMusicTag.next());
        }

        listViewTagsList.setItems(tags);
      }
    }

    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getRatings() != null) {
        LocalUser userlocal = new LocalUser();


        if (localMusic.getMetadata().getRatings().get(userlocal) != null) {
          System.out.println("recherche de la note de l'utilisateur courrant");
          Integer rating = localMusic.getMetadata().getRatings().get(new User());
          setStars(rating.intValue());
        }
      }
    }
  }

  /**
   * Sets the stars to the value set by user.
   * @param rating the rating of the current user
   */

  public void setStars(int rating) {
    if (rating == 1) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 2) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 3) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 4) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/EmptyStarSymbol.png").toURI().toString()));
    }
    if (rating == 5) {
      imageEtoile1.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile2.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile3.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile4.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
      imageEtoile5.setImage(new Image(new File(
          "../ihm/src/main/resources/images/FullStarSymbol.png").toURI().toString()));
    }
  }

  /**
   * Function changes the rating and the stars display when star is clicked.
   */
  @Override
  public void initialize() {

    imageEtoile1.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 1");
        setStars(1);
        note = 1;
      }
    }));

    imageEtoile2.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 2");
        setStars(2);
        note = 2;
      }
    }));

    imageEtoile3.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 3");
        setStars(3);
        note = 3;
      }
    }));

    imageEtoile4.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 4");
        setStars(4);
        note = 4;
      }
    }));

    imageEtoile5.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 5");
        setStars(5);
        note = 5;
      }
    }));

    buttonAddTag.setOnMousePressed((new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
          System.out.println("clique sur add tag");
          boolean tagexist = false;
          if (localMusic.getMetadata() != null) {
            if (localMusic.getMetadata().getTags() != null) {

              Iterator<String> itMusicTag = localMusic.getMetadata().getTags().iterator();
              while (itMusicTag.hasNext()) {
                if (itMusicTag.next().equals(textFieldAddTag.getText())) {
                  tagexist = true;
                }
              }
            }
          }
          if (!tagexist) {
            tags.add(textFieldAddTag.getText());
          }
          //give new tag or new tag list to data
        }
    }));

  }

  /**
   * Function checks that the fields are not null.
   * @return Boolean, false if there is a nul field.
   */
  public Boolean checkFields() {
    Boolean bool = true;
    if (textFieldTitre.getText() == null || textFieldTitre.getText().trim().equals("")) {
      bool = false;
      textFieldTitre.setStyle(" -fx-background-color:red;");
    } else {
      textFieldTitre.setStyle(" -fx-background-color:white;");
    }
    if (textFieldArtiste.getText() == null || textFieldArtiste.getText().trim().equals("")) {
      bool = false;
      textFieldArtiste.setStyle(" -fx-background-color:red;");
    } else {
      textFieldArtiste.setStyle(" -fx-background-color:white;");
    }
    if (textFieldAlbum.getText() == null || textFieldAlbum.getText().trim().equals("")) {
      bool = false;
      textFieldAlbum.setStyle(" -fx-background-color:red;");
    } else {
      textFieldAlbum.setStyle(" -fx-background-color:white;");
    }
    //if (textFieldAnnee.getText() == null || textFieldAnnee.getText().trim().equals("")) {
    //  bool = false;
    //  textFieldAnnee.setStyle(" -fx-background-color:red;");
    //}
    if (textFieldLastUploader.getText() == null
        || textFieldLastUploader.getText().trim().equals("")) {
      bool = false;
      textFieldLastUploader.setStyle(" -fx-background-color:red;");
    } else {
      textFieldLastUploader.setStyle(" -fx-background-color:white;");
    }


    return bool;
  }

  /**
   * Function updates the value of modified fields.
   * @param action button Validation is clicked.
   */
  public void validation(ActionEvent action) {

    if (!checkFields()) {
      return;
    }
    if (localMusic.getMetadata().getTitle() != textFieldTitre.getText()
        || localMusic.getMetadata().getAlbum() != textFieldAlbum.getText()
        || localMusic.getMetadata().getArtist() != textFieldArtiste.getText()) {

      this.getMyMusicsController()
          .getApplication()
          .getIhmCore()
          .getDataForIhm()
          .deleteMusic(localMusic, false);

      localMusic.getMetadata().setTitle(textFieldTitre.getText());
      localMusic.getMetadata().setAlbum(textFieldAlbum.getText());
      localMusic.getMetadata().setArtist(textFieldArtiste.getText());

      try {
        this.getMyMusicsController()
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .addMusic(localMusic.getMetadata(), localMusic.getMp3Path());
      } catch (FileNotFoundException e) {
        LogManager.getLogger()
            .error("File not found for music : {}".format(localMusic.getMp3Path()));
      }
      this.getMyMusicsController().displayAvailableMusics();
    }

    if (note > 0) {
      this.getMyMusicsController().getApplication()
      .getIhmCore().getDataForIhm().rateMusic(localMusic, note);
    }

    LogManager.getLogger().info("Change Field TODO with Data function if exist");

    ((Stage) this.textFieldTitre.getScene().getWindow()).close();
  }

}
