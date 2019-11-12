package core;

import datamodel.LocalUser;
import exceptions.LocalUsersFileException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class allows to access a LocalUsersFile.
 */
public class LocalUsersFileHandler {
  private Path filePath;

  LocalUsersFileHandler(String filePath) {
    Path savePath = Paths.get("").toAbsolutePath();
    this.filePath = savePath.resolve(filePath);
  }

  /**
   * Returns if the LocalUsersFile contains the specified LocalUser.
   *
   * @param localUser Checked LocalUser.
   * @return True if the related LocalUsers exists in the LocalUsersFile.
   */
  public boolean contains(LocalUser localUser) {
    try {
      return getUser(localUser) != null;
    } catch (LocalUsersFileException e) {
      return false;
    }
  }

  /**
   * Add a LocalUser to the LocalUsersFile.
   * If it already exist, this is ignored.
   *
   * @param localUser Added LocalUser.
   * @throws LocalUsersFileException if the file is not accessible.
   */
  public void add(LocalUser localUser) throws LocalUsersFileException {
    if (contains(localUser)) {
      return;
    }

    try {
      FileOutputStream file = new FileOutputStream(this.filePath.toFile());
      ObjectOutputStream writer = new ObjectOutputStream(file);
      writer.writeObject(localUser);
    } catch (IOException e) {
      throw new LocalUsersFileException("Local save may be corrupted or outdated");
    }
  }

  /**
   * Update a LocalUser on the LocalUsersFile.
   *
   * @param localUser Updated LocalUser.
   * @throws LocalUsersFileException if the file is not accessible.
   */
  public void update(LocalUser localUser) throws LocalUsersFileException {
    remove(localUser);
    add(localUser);
  }

  /**
   * Remove a LocalUser on the LocalUsersFile.
   *
   * @param localUser Removed LocalUser.
   * @throws LocalUsersFileException if the file is not accessible.
   */
  public void remove(LocalUser localUser) throws LocalUsersFileException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public void removeAll() throws LocalUsersFileException {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  /**
   * Returns a LocalUser if it is in the LocalUsersFile.
   *
   * @param username Wanted LocalUser's username.
   * @return the LocalUser or null if it does not exist.
   * @throws LocalUsersFileException if the file is not accessible.
   */
  public LocalUser getUser(String username) throws LocalUsersFileException {
    LocalUser user;

    try {
      FileInputStream file = new FileInputStream(filePath.toFile());
      ObjectInputStream reader = new ObjectInputStream(file);
      do {
        user = (LocalUser) reader.readObject();
      } while (!(user.getUsername().equals(username)));
    } catch (EOFException e) {
      throw new LocalUsersFileException("No such user found");
    } catch (ClassNotFoundException | IOException e) {
      throw new LocalUsersFileException("Local save may be corrupted or outdated");
    }

    return user;
  }

  /**
   * Returns a LocalUser if it is in the LocalUsersFile.
   *
   * @param localUser Updated LocalUser.
   * @return the LocalUser or null if it does not exist.
   * @throws LocalUsersFileException if the file is not accessible.
   */
  public LocalUser getUser(LocalUser localUser) throws LocalUsersFileException {
    return getUser(localUser.getUsername());
  }
}