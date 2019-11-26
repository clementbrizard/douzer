package controllers;

import datamodel.User;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * The left middle view containing all connected users.
 */
public class OnlineUsersListController implements Controller {
  @FXML
  private ListView<String> lvwOnlineUsers;

  private MainController mainController;

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }

  // Other methods

  @Override
  public void initialize() {}

  public void init() {
    // TODO uncomment to get IPs when Data method is implemented
    displayOnlineUsers(mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getOnlineUsers());
  }

  /**
   * Fill the view with the ips of users.
   * @param users Stream of connected users
   **/

  public void displayOnlineUsers(Stream<User> users) {
    ObservableList<String> items =
        users.map(user -> user.getIp().toString())
             .collect(Collectors.toCollection(FXCollections::observableArrayList));

    lvwOnlineUsers.setItems(items);
  }

}
