package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import datamodel.Contact;
import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.MusicMetadata;
import datamodel.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalUsersFileHandlerTest {
  private final String path = "lo23-users.ser";
  private LocalUsersFileHandler handler;
  private LocalUser testUser;

  @BeforeEach
  void setUp() {
    File file = Paths.get("").resolve(path).toFile();
    try {
      file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

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
    LocalMusic music = new LocalMusic(new MusicMetadata(), "testDir1");
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
  void contains() throws IOException {
    assertFalse(handler.contains(testUser),
        "User not in the file so contains should be false");

    handler.add(testUser);
    assertTrue(handler.contains(testUser), "User in the file so contains should be true");

    assertFalse(handler.contains(null));
  }

  @Test
  void getUserTest() throws IOException {
    handler.add(testUser);

    LocalUser loadedFromObject = handler.getUser(testUser);
    LocalUser loadedFromUsername = handler.getUser(testUser.getUsername());

    assertEquals(loadedFromObject, testUser);
    assertEquals(loadedFromUsername, testUser);

    LocalUser testUser2 = new LocalUser();
    testUser2.setUsername("Billy");
    testUser2.setSavePath(Paths.get("testDir1/testDir2/../testDir3"));

    handler.add(testUser2);
    assertEquals(handler.getUser(testUser), testUser);
    assertEquals(handler.getUser(testUser2), testUser2);
    assertEquals(handler.getUser("Billy"), testUser2);
  }

  @Test
  void update() throws IOException {
    handler.add(testUser);
    testUser.setFirstName("NewName");
    handler.update(testUser);

    assertEquals(testUser, handler.getUser(testUser));
  }

  @Test
  void remove() throws IOException {
    handler.add(testUser);
    handler.remove(testUser);
    assertFalse(handler.contains(testUser),
        "User should be removed from the file");

    handler.remove(testUser);
    assertFalse(handler.contains(testUser),
        "User should be removed from the file");
  }
}