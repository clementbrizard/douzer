package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import datamodel.LocalMusic;
import datamodel.LocalUser;
import datamodel.MusicMetadata;
import datamodel.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalUsersFileHandlerTest {
  private final String path = "lo23-users.ser";
  private LocalUsersFileHandler handler;
  private LocalUser testUser1, testUser2;

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

    testUser1 = new LocalUser();
    testUser1.setSavePath(Paths.get("testDir1/testDir2/../testDir3"));
    testUser1.setUsername("john_oliver");
    testUser1.setPassword("hunter2");
    testUser1.setFirstName("John");
    testUser1.setLastName("Oliver");
    testUser1.getFriends().add(friendUser);
    LocalMusic music = new LocalMusic(new MusicMetadata("JesuisUnHashLOL"), "testDir1");
    music.getMetadata().setArtist("Vulfpeck");
    music.getOwners().add(testUser1);
    testUser1.getMusics().add(music);

    testUser2 = new LocalUser();
    testUser2.setUsername("Billy");
    testUser2.setSavePath(Paths.get("testDir1/testDir2/../testDir3"));
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
    assertFalse(handler.contains(testUser1),
        "User not in the file so contains should be false");

    handler.add(testUser1);
    assertTrue(handler.contains(testUser1), "User in the file so contains should be true");

    assertFalse(handler.contains(null));
  }

  @Test
  void getUserTest() throws IOException {
    handler.add(testUser1);

    LocalUser loadedFromObject = handler.getUser(testUser1);
    LocalUser loadedFromUsername = handler.getUser(testUser1.getUsername());

    assertEquals(loadedFromObject, testUser1);
    assertEquals(loadedFromUsername, testUser1);

    handler.add(testUser2);
    assertEquals(handler.getUser(testUser1), testUser1);
    assertEquals(handler.getUser(testUser2), testUser2);
    assertEquals(handler.getUser("Billy"), testUser2);
  }

  @Test
  void update() throws IOException {
    handler.add(testUser1);
    testUser1.setFirstName("NewName");
    handler.update(testUser1);

    assertEquals(testUser1, handler.getUser(testUser1));
  }

  @Test
  void remove() throws IOException {
    handler.add(testUser1);
    handler.remove(testUser1);
    assertFalse(handler.contains(testUser1),
        "User should be removed from the file");

    handler.remove(testUser1);
    assertFalse(handler.contains(testUser1),
        "User should be removed from the file");
  }
}