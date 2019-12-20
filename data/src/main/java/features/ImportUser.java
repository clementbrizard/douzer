package features;

import core.Datacore;
import core.LocalUsersFileHandler;
import datamodel.LocalUser;
import exceptions.data.DataException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ImportUser {

  /**
   * Import a previously exported LocalUser.
   * @param path the path to the backup directory.
   * @param dc the datacore.
   * @throws DataException if the username already exists.
   */
  public static void run(Path path, Datacore dc) throws DataException, IOException {
    //Searching for the .ser file.
    List list = Arrays.stream(new File(path.toUri()).listFiles())
        .filter(f -> f.getName().matches(".*\\.ser"))
        .map(File::getName)
        .collect(Collectors.toList());

    if (list.isEmpty()) {
      throw new DataException("Unable to find the user backup file.");
    } else {
      String username = list.get(0).toString().split("\\.")[0];

      LocalUsersFileHandler fileHandler = new LocalUsersFileHandler(
          path.resolve(username + ".ser").toString());
      LocalUser localUser = fileHandler.getUser(username);

      //Check if the localUser already exists on the local computer.
      //If it doesn't we add the LocalUser.
      if (!dc.getLocalUsersFileHandler().contains(localUser)) {
        localUser.setSavePath(path);

        //Changing LocalMusics paths.
        localUser.getLocalMusics().forEach(m -> {
          m.setMp3Path(path.resolve(new File(m.getMp3Path()).getName()).toString());
        });

        dc.getLocalUsersFileHandler().add(localUser);
      } else {
        throw new DataException("The user already exists.");
      }
    }
  }
}
