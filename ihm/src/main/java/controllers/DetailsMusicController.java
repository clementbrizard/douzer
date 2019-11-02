package controllers;

import datamodel.LocalMusic;
import datamodel.User;

import java.util.Iterator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

//replace by javadocs
//central view to show all information about on music
public class DetailsMusicController implements Controller {

  private MyMusicsController myMusicsController;

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
      if (localMusic.getMetadata().getReleaseDate() != null) {
        textFieldAnnee.setText("" + localMusic.getMetadata().getReleaseDate().getYear());
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
        if (localMusic.getMetadata().getRatings().get(new User()) != null) { 
          System.out.println("recherche de la note de l'utilisateur courrant");
          //rating = localMusic.getMetadata().getRatings().get(new User());
        }
      }
    }
  }

  @FXML
  private TextField textFieldTitre;

  @FXML
  private TextField textFieldArtiste;

  @FXML
  private TextField textFieldAlbum;

  @FXML
  private TextField textFieldAnnee;

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

  @Override
  public void initialize() {
    
    imageEtoile1.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 1");
        imageEtoile1.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile2.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
        imageEtoile3.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
        imageEtoile4.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
        imageEtoile5.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
      } 
    }));
    
    imageEtoile2.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 2");
        imageEtoile1.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile2.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile3.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
        imageEtoile4.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
        imageEtoile5.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
      } 
    }));
    
    imageEtoile3.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 3");
        imageEtoile1.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile2.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile3.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile4.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
        imageEtoile5.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
      } 
    }));
    
    imageEtoile4.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 4");
        imageEtoile1.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile2.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile3.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile4.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile5.setImage(new Image("main/resources/images/EmptyStarSymbol.png"));
      } 
    }));
    
    imageEtoile5.setOnMousePressed((new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        System.out.println("clique sur l'�toile 5");
        imageEtoile1.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile2.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile3.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile4.setImage(new Image("main/resources/images/FullStarSymbol.png"));
        imageEtoile5.setImage(new Image("main/resources/images/FullStarSymbol.png"));
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


}
