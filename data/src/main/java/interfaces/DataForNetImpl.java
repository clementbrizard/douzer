package interfaces;

import core.Datacore;
import core.Payload;

import datamodel.LocalMusic;
import datamodel.Music;

import exceptions.data.DataException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.nio.file.Path;

public class DataForNetImpl implements DataForNet {
  private Datacore dc;

  public DataForNetImpl(Datacore dc) {
    this.dc = dc;
  }

  @Override
  public void process(Serializable payload, InetAddress sourceIp) {
    try {
      Payload receivedPayload = (Payload) payload;
      receivedPayload.process(this.dc, sourceIp);
    } catch (ClassCastException e) {
      Datacore.getLogger().error("Received object is not a payload.");
    } catch (DataException e) {
      Datacore.getLogger().error(e);
    }
  }

  @Override
  public void saveMp3(InputStream stream, String musicHash) {
    Music formerMusic = this.dc.getMusic(musicHash);
    
    //Default name for mp3
    String mp3FileName =
        formerMusic.getMetadata().getTitle()
        + " - "
        + formerMusic.getMetadata().getArtist()
        + ".mp3";
    
    //Path to directory where mp3 are saved
    Path savePath = this.dc.getCurrentUser().getSavePath().resolve(
        this.dc.getCurrentUser().getUsername() + "-musics");

    //Create mp3 directory if it does not exist
    if (!savePath.toFile().exists() || !savePath.toFile().isDirectory()) {
      savePath.toFile().mkdir();
    }
    
    File mp3File = savePath.resolve(mp3FileName).toFile();
    
    //Update the name of the .mp3 if a file already exist
    for (int i = 1; mp3File.exists() && !mp3File.isDirectory(); i++) {
      mp3FileName = 
          formerMusic.getMetadata().getTitle() 
          + " - "
          + formerMusic.getMetadata().getArtist()
          + " (" + i + ").mp3";
      
      mp3File = savePath.resolve(mp3FileName).toFile();
    }
      
      
    //Save file
    //TODO progress bar
    byte[] contents = new byte[20000];
    try {
      FileOutputStream fos = new FileOutputStream(mp3File.getAbsolutePath());
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      
      //No of bytes read in one read() call
      int bytesRead = 0; 
      
      while ((bytesRead = stream.read(contents)) != -1) {
        bos.write(contents, 0, bytesRead);
      }
      
      bos.flush(); 
      bos.close();
    
      Datacore.getLogger().info("File" + mp3FileName + " saved successfully.");
      
      LocalMusic newMusic = this.dc.upgradeMusicToLocal(formerMusic, mp3File.getAbsolutePath());
      
      newMusic.getOwners().add(dc.getCurrentUser());
      dc.getCurrentUser().getLocalMusics().add(newMusic);
    } catch (Exception e) {
      //TODO manage errors
      this.dc.ihm.notifyDownloadProgress(formerMusic, -1);
      e.printStackTrace();
    }
  }

  @Override
  public File getLocalMusic(String musicHash) {
    LocalMusic localMusic = this.dc.getLocalMusic(musicHash);
    return new File(localMusic.getMp3Path());
  }
}
