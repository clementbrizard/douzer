package features;

import core.LocalUsersFileHandler;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public abstract class ExportUser {

  /**
   * Export a LocalUser and its mp3s to the selected path.
   * Wipes a previous backup if it exists.
   * @param localUser the LocalUser to export.
   * @param path the path where the LocalUser will be stored.
   */
  public static void run(LocalUser localUser, Path path) throws IOException {
    Path basePath = path.resolve(localUser.getUuid().toString());
    File baseDirectory = new File(basePath.toUri());

    //Wiping the previous backup if it exists.
    if (Files.exists(basePath)) {
      Arrays.stream(baseDirectory.listFiles()).forEach(File::delete);
      baseDirectory.delete();
    }

    boolean successfullyCreated = baseDirectory.mkdir();
    if (successfullyCreated) {
      //Moving all the songs.
      for (LocalMusic m : localUser.getLocalMusics()) {
        Files.copy(Paths.get(m.getMp3Path()), Paths.get(
            baseDirectory.getAbsolutePath()).resolve(new File(m.getMp3Path()).getName()));
      }

      //Backing up user properties.
      Path propertiesPath = localUser.getSavePath()
          .resolve(localUser.getUsername() + "-config.properties");
      Files.copy(propertiesPath,
          basePath.resolve(localUser.getUsername() + "-config.properties"));

      //User serialization.
      LocalUsersFileHandler fileHandler = new LocalUsersFileHandler(
          basePath.resolve(localUser.getUsername() + ".ser").toString());
      fileHandler.add(localUser);

    } else {
      throw new IOException("Unable to create the directory.");
    }
  }
}
