package features;

import core.Datacore;
import core.LocalUsersFileHandler;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import exceptions.data.DataException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ImportUser {

  /**
   * Import a previously exported LocalUser.
   * @param pathToBackup the path to the backup directory.
   * @param newSavePath the new savePath for the imported LocalUser.
   * @param dc the datacore.
   * @throws DataException if the username already exists.
   */
  public static void run(Path pathToBackup, Path newSavePath, Datacore dc)
      throws DataException, IOException {
    //Searching for the .ser file.
    List list = Arrays.stream(Objects.requireNonNull(new File(pathToBackup.toUri()).listFiles()))
        .filter(f -> f.getName().matches(".*\\.ser"))
        .map(File::getName)
        .collect(Collectors.toList());

    if (list.isEmpty()) {
      throw new DataException("Unable to find the user backup file.");
    } else {
      String username = list.get(0).toString().split("\\.")[0];

      LocalUsersFileHandler fileHandler = new LocalUsersFileHandler(
          pathToBackup.resolve(username + ".ser").toString());
      LocalUser localUser = fileHandler.getUser(username);

      if (!dc.getLocalUsersFileHandler().contains(localUser)) {
        localUser.setSavePath(newSavePath);
        Files.copy(pathToBackup.resolve((localUser.getUsername() + "-config.properties")),
            newSavePath.resolve((localUser.getUsername() + "-config.properties")));

        //Copying the mp3 files to the new savePath.
        for (LocalMusic m : localUser.getLocalMusics()) {
          String fileName = new File(m.getMp3Path()).getName();
          Files.copy(pathToBackup.resolve(fileName), newSavePath.resolve(fileName));
          m.setMp3Path(newSavePath.resolve(fileName).toString());
        }

        dc.getLocalUsersFileHandler().add(localUser);
      } else {
        throw new DataException("The user already exists.");
      }
    }
  }
}
