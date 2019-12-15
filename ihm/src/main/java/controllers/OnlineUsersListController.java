package controllers;

import datamodel.User;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;

/**
 * The left middle view containing all connected users.
 */
public class OnlineUsersListController implements Controller {
  @FXML
  private ListView<String> lvwOnlineUsers;

  // The list view updates itself when this observable list changes
  private ObservableList<String> onlineUsersList;

  private MainController mainController;

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  // Other methods

  @Override
  public void initialize() {
    // Initialize the observable list
    onlineUsersList = FXCollections.observableArrayList();

    // Set it as the data model of the ListView
    lvwOnlineUsers.setItems(onlineUsersList);
  }

  public void init() {
    try {
      displayOnlineUsers();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }
  }

  public void addNewOnlineUser(User user) {
    if (!onlineUsersList.contains(user.getUsername())) {

      // This is done to avoid a "Not on FX application thread" error
      // Solution found here : https://stackoverflow.com/a/23007018
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          onlineUsersList.add(user.getUsername());
        }
      });

    }
  }

  public void removeOnlineUser(User user) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        onlineUsersList.remove(user.getUsername());
      }
    });
  }

  /**
   * Fills the view with the ips of users.
   **/

  public void displayOnlineUsers() {
    ObservableList<String> onlineUsers = this.mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getOnlineUsers()
        .map(user -> user.getUsername())
        .collect(Collectors.toCollection(FXCollections::observableArrayList));

    onlineUsersList.setAll(onlineUsers);
  }

}
