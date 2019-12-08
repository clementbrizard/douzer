package core;

import datamodel.LocalUser;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to access a LocalUsersFile.
 */
public class LocalUsersFileHandler {
  private Path filePath;

  LocalUsersFileHandler(String filePath) throws IOException {
    Path savePath = Paths.get("").toAbsolutePath();
    this.filePath = savePath.resolve(filePath);

    // Create the file with an empty Map of LocalUsers.
    setLocalUsers(new HashMap<>());
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