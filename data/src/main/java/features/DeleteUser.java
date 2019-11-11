package features;

import core.Datacore;
import datamodel.LocalUser;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class DeleteUser {
  /**
   * Remove user from the lo23-users.ser file.
   * write every user in a temp file
   * skip the user we want to delete
   * save the new temp file as our old one
   *
   * @param filePath Path
   * @param username String
   */
  private static void deleteUserFromDisk(Path filePath, String username
                                         ) throws Exception {
    FileInputStream file = new FileInputStream(filePath.toFile());
    ObjectInputStream reader = new ObjectInputStream(file);
    File temp = new File("_temp_");
    FileOutputStream tempFile = new FileOutputStream(temp);
    ObjectOutputStream writer = new ObjectOutputStream(tempFile);
    LocalUser user;

    //Write the first part of the file
    try {
      do {
        user = (LocalUser) reader.readObject();
        writer.writeObject(user);
      } while (!(user.getUsername().equals(username)));
    } catch (ClassNotFoundException e) {
      throw new Exception("Local save may be corrupted or outdated");
    }
    //Skip the user we want to delete
    writer.writeObject(null);

    //Write the second part of the file
    try {
      do {
        user = (LocalUser) reader.readObject();
        writer.writeObject(user);
      } while (!(user.getUsername().equals(username)));
    } catch (EOFException e) {
      //Replace old file with the new one
      temp.renameTo(filePath.toFile()); //Not executed ?
    } catch (ClassNotFoundException e) {
      throw new Exception("Local save may be corrupted or outdated");
    }

  }

  /**
   * Delete an account.
   * Delete the properties file linked to the user we want to delete
   * Remove user from the lo23-users.ser file.
   * Then delete LocalUser object by giving it the value null
   *
   * @param dc Datacore
   */
  public static void run(Datacore dc) throws Exception {
    LocalUser user = dc.getCurrentUser();
    File propFileToDelete = new File(user.getSavePath().resolve(user.getUsername()
        + "-config.properties").toString());
    if (!propFileToDelete.exists()) {
      System.out.println("Properties file for this user does not exist. Can't be deleted");
    } else {
      propFileToDelete.delete();
      System.out.println("Properties file deleted");
    }

    Path savePath = Paths.get("").toAbsolutePath();
    deleteUserFromDisk(savePath.resolve(dc.LOCAL_USERS_FILENAME), user.getUsername());

    dc.setCurrentUser(null);
  }

}



