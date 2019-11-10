package features;

import core.Datacore;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class DeleteUser {
  /**
   * Function to delete a directory.
   * The function first check if the file is a directory
   * Then delete it if the directory is empty
   * If it is not delete, list all the directory content
   * Then delete all the content
   *
   * @param file File
   */
  private static void deleteDirectory(File file) throws IOException {
    if (file.isDirectory()) {

      //directory is empty, then delete it
      if (file.list().length == 0) {
        file.delete();
        System.out.println("Directory is deleted : "
            + file.getAbsolutePath());

      } else {
        //list all the directory contents
        String[] files = file.list();

        for (String temp : files) {
          //construct the file structure
          File fileDelete = new File(file, temp);

          //recursive delete
          deleteDirectory(fileDelete);
        }

        //check the directory again, if empty then delete it
        if (Objects.requireNonNull(file.list()).length == 0) {
          file.delete();
          System.out.println("Directory is deleted : "
              + file.getAbsolutePath());
        }
      }

    } else {
      //if file, then delete it
      file.delete();
      System.out.println("File is deleted : " + file.getAbsolutePath());
    }
  }

  /**
   * Delete an account.
   * Delete the directory link to the user we
   * want to delete
   * Then delete LocalUser object by giving it the value null
   *
   * @param dc Datacore
   */
  public static void run(Datacore dc) {
    File directoryToDelete = new File(dc.getCurrentUser().getSavePath().toString());

    if (!directoryToDelete.exists()) {
      System.out.println("Directory does not exist.");
    } else {
      try {
        deleteDirectory(directoryToDelete);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    dc.setCurrentUser(null);
  }

}



