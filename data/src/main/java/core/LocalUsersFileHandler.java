package core;

import datamodel.LocalMusic;
import datamodel.LocalUser;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;


/**
 * This class allows to access a LocalUsersFile.
 */
public class LocalUsersFileHandler {
  private Path filePath;

  LocalUsersFileHandler(String filePath) throws IOException {
    Path savePath = Paths.get("").toAbsolutePath();
    this.filePath = savePath.resolve(filePath);

    if (!this.filePath.toFile().exists()) {
      // Create the file with an empty Map of LocalUsers.
      setLocalUsers(new HashMap<>());
    }
  }

  private void setLocalUsers(Map<String, LocalUser> localUsers) throws IOException {
    // This also clear the file
    try (FileOutputStream file = new FileOutputStream(this.filePath.toFile());
         ObjectOutputStream writer = new ObjectOutputStream(file)) {
      if (!localUsers.isEmpty()) {
        writer.writeObject(localUsers);
      }
    } catch (IOException e) {
      throw new IOException("Local save may be corrupted or outdated");
    }
  }

  /**
   * Returns if the LocalUsersFile contains the specified LocalUser.
   *
   * @param localUser Checked LocalUser.
   * @return True if the related LocalUsers exists in the LocalUsersFile.
   */
  public boolean contains(LocalUser localUser) throws IOException {
    try {
      return getUser(localUser) != null;
    } catch (NullPointerException e) {
      return false;
    }
  }

  /**
   * Add a LocalUser to the LocalUsersFile.
   * If it already exist, this is ignored.
   *
   * @param localUser Added LocalUser.
   * @throws IOException if the file is not accessible.
   */
  public void add(LocalUser localUser) throws IOException {
    Map<String, LocalUser> localUsers = getAll();
    localUsers.put(localUser.getUsername(), localUser);
    setLocalUsers(localUsers);
  }

  /**
   * Update a LocalUser on the LocalUsersFile.
   *
   * @param localUser Updated LocalUser.
   * @throws IOException if the file is not accessible.
   */
  public void update(LocalUser localUser) throws IOException {
    Map<String, LocalUser> localUsers = getAll();
    localUsers.remove(localUser.getUsername());
    localUsers.put(localUser.getUsername(), localUser);
    setLocalUsers(localUsers);
  }

  /**
   * Remove a LocalUser on the LocalUsersFile.
   * @param localUser Removed LocalUser.
   * @throws IOException if the file is not accessible.
   */
  public void remove(LocalUser localUser) throws IOException {
    Map<String, LocalUser> localUsers = getAll();
    localUsers.remove(localUser.getUsername());
    setLocalUsers(localUsers);
  }

  public void removeAll() throws IOException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Returns a LocalUser if it is in the LocalUsersFile.
   *
   * @param username Wanted LocalUser's username.
   * @return the LocalUser or throws an NullPointerException if it does not exist.
   * @throws NullPointerException if the user is not found.
   * @throws IOException if the file is not accessible.
   */
  public LocalUser getUser(String username) throws IOException {
    LocalUser localUser = getAll().get(username);
    if (localUser != null) {
      return localUser;
    }
    throw new NullPointerException("No such user found");
  }

  /**
   * Returns a LocalUser if it is in the LocalUsersFile.
   *
   * @param localUser Updated LocalUser.
   * @return the LocalUser or throws an NullPointerException if it does not exist.
   * @throws NullPointerException if the user is not found.
   * @throws IOException if the file is not accessible.
   */
  public LocalUser getUser(LocalUser localUser) throws IOException {
    return getUser(localUser.getUsername());
  }

  /**
   * Export a LocalUser and its mp3 files on the hard drive.
   * @param localUser the LocalUser to export.
   * @param path path to the directory that will contain the backup directory.
   * @throws IOException if the file is not accessible.
   */
  public void exportLocalUser(LocalUser localUser, String path) throws IOException {
    LocalUser localUserToExport = new LocalUser(localUser);
    String basePath = path + "/" + localUserToExport.getUuid() + "/";
    File baseDirectory = new File(basePath);

    //Wiping the previous backup if it exists.
    if (Files.exists(Paths.get(basePath))) {
      Arrays.stream(baseDirectory.listFiles()).forEach(File::delete);
      baseDirectory.delete();
    }

    boolean successfullyCreated = baseDirectory.mkdir();
    if (successfullyCreated) {
      //Moving all the songs.
      for (LocalMusic m : localUserToExport.getLocalMusics()) {
        Files.copy(Paths.get(m.getMp3Path()), Paths.get(
                baseDirectory.getAbsolutePath() + "/" + new File(m.getMp3Path()).getName()));
      }

      //Changing LocalMusics paths.
      localUserToExport.getLocalMusics().forEach(m -> {
        m.setMp3Path(baseDirectory.getAbsolutePath() + "/" + new File(m.getMp3Path()).getName());
      });

      //Backing up user properties.
      Path propertiesPath = Paths.get(
              localUserToExport.getSavePath() + "/" + localUserToExport.getUsername() + "-config.properties");
      Files.copy(propertiesPath,
              Paths.get(basePath + localUserToExport.getUsername() + "-config.properties"));
      localUserToExport.setSavePath(Paths.get(basePath));

      //User serialization.
      FileOutputStream fileOutputStream = new FileOutputStream(
              basePath + "user.ser");
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(localUserToExport);
      objectOutputStream.flush();
      objectOutputStream.close();
      fileOutputStream.close();

      //Backing up user avatar.
      ImageIO.write(localUserToExport.getAvatar(), "jpg", new File(basePath + "avatar.jpg"));
    } else {
      throw new IOException("Unable to create the directory.");
    }
  }

  /**
   * Import a previously exported LocalUser.
   * @param path the path to the previously exported LocalUser.
   * @return the imported LocalUSer.
   * @throws IOException if the file is not accessible.
   */
  public LocalUser importLocalUser(String path) throws IOException, ClassNotFoundException {
    FileInputStream fileInputStream = new FileInputStream(path + "/user.ser");
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    LocalUser localUser = (LocalUser)objectInputStream.readObject();

    objectInputStream.close();
    fileInputStream.close();

    localUser.setAvatar(ImageIO.read(new File(path + "/avatar.jpg")));

    return localUser;
  }

  @SuppressWarnings("unchecked")
  private Map<String, LocalUser> getAll() throws IOException {
    try (FileInputStream file = new FileInputStream(this.filePath.toFile());
         ObjectInputStream reader = new ObjectInputStream(file)) {
      return (Map<String, LocalUser>) reader.readObject();
    } catch (EOFException e) {
      return new HashMap<>();
    } catch (IOException | ClassNotFoundException e) {
      throw new IOException("Local save may be corrupted or outdated: " + e);
    }
  }
}