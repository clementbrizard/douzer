package controllers;

import datamodel.User;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


/**
 * The left middle view containing all connected users.
 */
public class OnlineUsersListController implements Controller {

  private MainController mainController;
  private OnlineUsersListController onlineUsersListController;

  @FXML
  private ListView<String> lvwOnlineUsers;

  @Override
  public void initialize() {
  }

  /**
   * Fills the view with the ips of users.
   * @param users Stream of connected users
   **/

  public void displayOnlineUsers(Stream<User> users) {
    ObservableList<String> items = FXCollections.observableArrayList();

    User[] usersList = users.toArray(User[]::new);
    for (User u : usersList) {
      items.add(u.getIp().toString());
    }
    lvwOnlineUsers.setItems(items);
  }

  public OnlineUsersListController getOnlineUsersListController() {
    return onlineUsersListController;
  }

  public void setOnlineUsersListController(OnlineUsersListController onlineUsersListController) {
    this.onlineUsersListController = onlineUsersListController;
  }

  public void init() {
    displayOnlineUsers(mainController.getIhmCore().getDataForIhm().getOnlineUsers());
  }
}
