package controllers;

import datamodel.LocalMusic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
      if (localMusic.getOwners().iterator().hasNext()) {
        textFieldLastUploader.setText(localMusic.getOwners().iterator().next().getUsername());
      }
    }
    
    tags = FXCollections.observableArrayList();
    
    if (localMusic.getMetadata() != null) {
      if (localMusic.getMetadata().getTags() != null) {
        
        while (localMusic.getMetadata().getTags().iterator().hasNext()) {
          tags.add(localMusic.getMetadata().getTags().iterator().next());
        }
        
        listViewTagsList.setItems(tags);
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
  private ListView<String> listViewTagsList;
  
  private ObservableList<String> tags;
  
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
        System.out.println("clique sur l'étoile 1");
        imageEtoile1.setImage(new Image("my/resources/images/FullStarSymbol.png") );
      } 
      
    }));
    
    
  }

  
}
