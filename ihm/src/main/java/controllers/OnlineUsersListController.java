package controllers;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


/**
 * The left middle view containing all connected users.
 */
public class OnlineUsersListController implements Controller {

  private OnlineUsersListController onlineUsersListController;

  @FXML
  private ListView<String> lvwOnlineUsers;

  @Override
  public void initialize() {
    /* //Uncomment for test
    List<String> dummyUsers = new ArrayList<String>();
    for(int i = 1; i < 21; i++){
      dummyUsers.add("Test" + i);
    }
    updateOnlineUsers(dummyUsers);
     */
  }

  /**
   * Fills the view with the given list of users.
   * @param users list of connected users to display (pseudos)
   */
  public void displayOnlineUsers(List<String> users) {
    ObservableList<String> items = FXCollections.observableArrayList(users);
    lvwOnlineUsers.setItems(items);
  }

  public OnlineUsersListController getOnlineUsersListController() {
    return onlineUsersListController;
  }

  public void setOnlineUsersListController(OnlineUsersListController onlineUsersListController) {
    this.onlineUsersListController = onlineUsersListController;
  }
}
