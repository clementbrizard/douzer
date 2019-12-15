package features;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import core.Datacore;
import datamodel.MusicMetadata;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Year;

public abstract class ParseMusicMetadata {
  /**
   * Extracts the Metadata from a mp3 file.
   *
   * @param dc   The Datacore
   * @param path Path to the mp3
   * @return The extracted metadata
   */
  public static MusicMetadata run(Datacore dc, String path)
      throws NoSuchAlgorithmException, IOException, InvalidDataException, UnsupportedTagException {
    String hash = new String(
        MessageDigest.getInstance("MD5").digest(Files.readAllBytes(Paths.get(path)))
    );

    MusicMetadata metadata = new MusicMetadata(hash);
    Mp3File mp3File = new Mp3File(path);

    metadata.setDuration(Duration.ofSeconds(mp3File.getLengthInSeconds()));

    if (mp3File.hasId3v1Tag()) {
      ID3v1 id3v1Tag = mp3File.getId3v1Tag();

      metadata.setTitle(id3v1Tag.getTitle());
      metadata.setArtist(id3v1Tag.getArtist());
      metadata.setAlbum(id3v1Tag.getAlbum());
      if (id3v1Tag.getYear().length() > 0) {
        metadata.setReleaseYear(Year.parse(id3v1Tag.getYear()));
      }
    } else if (mp3File.hasId3v2Tag()) {
      ID3v2 id3v2Tag = mp3File.getId3v2Tag();

      metadata.setTitle(id3v2Tag.getTitle());
      metadata.setArtist(id3v2Tag.getArtist());
      metadata.setAlbum(id3v2Tag.getAlbum());
      if (id3v2Tag.getYear() != null) {
        metadata.setReleaseYear(Year.parse(id3v2Tag.getYear()));
      }
    }

    return metadata;
  }
}
