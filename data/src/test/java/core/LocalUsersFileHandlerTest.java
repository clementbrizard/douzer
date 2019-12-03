package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import datamodel.Contact;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.MusicMetadata;
import datamodel.User;
import exceptions.data.LocalUsersFileException;
import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalUsersFileHandlerTest {
  private final String path = "lo23-users.ser";
  private LocalUsersFileHandler handler;
  private LocalUser testUser;

  @BeforeEach
  void setUp() {
    handler = new LocalUsersFileHandler(path);
    User friendUser = new User();
    friendUser.setUsername("mark");

    testUser = new LocalUser();
    testUser.setSavePath(Paths.get("testDir1/testDir2/../testDir3"));
    testUser.setUsername("john_oliver");
    testUser.setPassword("hunter2");
    testUser.setFirstName("John");
    testUser.setLastName("Oliver");
    testUser.getContacts().add(new Contact(friendUser));
    LocalMusic music = new LocalMusic(new MusicMetadata("JesuisUnHashLOL"), "testDir1");
    music.getMetadata().setArtist("Vulfpeck");
    music.getOwners().add(testUser);
    testUser.getMusics().add(music);
  }

  @AfterEach
  void tearDown() {
    File file = Paths.get("").resolve(path).toFile();
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  void contains() throws LocalUsersFileException {
    assertFalse(handler.contains(testUser),
        "User not in the file so contains should be false");
    handler.add(testUser);
    assertTrue(handler.contains(testUser),
        "User in the file so contains should be true");
  }

  @Test
  void addAndGetUserTest() throws LocalUsersFileException {
    handler.add(testUser);

    LocalUser loadedFromObject = handler.getUser(testUser);
    LocalUser loadedFromUsername = handler.getUser(testUser.getUsername());

    assertEquals(loadedFromObject, testUser);
    assertEquals(loadedFromUsername, testUser);
  }

  @Test
  void update() throws LocalUsersFileException {
    handler.add(testUser);
    testUser.setFirstName("Bob");
    handler.update(testUser);
    LocalUser loaded = handler.getUser(testUser);

    assertEquals(loaded, testUser,
        "User should be updated in the file");
  }

  @Test
  void remove() throws LocalUsersFileException {
    handler.add(testUser);
    handler.remove(testUser);
    assertFalse(handler.contains(testUser),
        "User should be removed from the file");
  }
}