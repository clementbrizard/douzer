package controllers;

import datamodel.LocalMusic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
   * the localMusic where the user has click
   * @param localMusic
   */
  public void initMusic(LocalMusic localMusic) {
    
    if(localMusic.getMetadata() != null)
      if(localMusic.getMetadata().getTitle() != null)
          textFieldTitre.setText(localMusic.getMetadata().getTitle());
    
    if(localMusic.getMetadata() != null)
      if(localMusic.getMetadata().getArtist() != null)
          textFieldArtiste.setText(localMusic.getMetadata().getArtist());
    
    if(localMusic.getMetadata() != null)
      if(localMusic.getMetadata().getAlbum() != null)
          textFieldAlbum.setText(localMusic.getMetadata().getAlbum());
    
    if(localMusic.getMetadata() != null)
      if(localMusic.getMetadata().getReleaseDate() != null)
          textFieldAnnee.setText("" + localMusic.getMetadata().getReleaseDate().getYear());
    
    if(localMusic.getOwners() != null)
      if(localMusic.getOwners().iterator().hasNext())
        textFieldLastUploader.setText(localMusic.getOwners().iterator().next().getUsername());
    
    tags = FXCollections.observableArrayList();
    
    if(localMusic.getMetadata() != null)
      if(localMusic.getMetadata().getTags() != null) {
        
        while(localMusic.getMetadata().getTags().iterator().hasNext()) {
          tags.add(localMusic.getMetadata().getTags().iterator().next());
        }
        
        listViewTagsList.setItems(tags);
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
  
  @Override
  public void initialize() {
    
  }

  
}
