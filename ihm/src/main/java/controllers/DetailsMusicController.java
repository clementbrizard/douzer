package controllers;

import datamodel.LocalMusic;
import javafx.fxml.FXML;
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
   * After the initialisation of the controller execute this function with
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
  
  
  @Override
  public void initialize() {
    
  }

  
}
