package controllers;

import datamodel.User;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The left middle view containing all connected users.
 */
public class OnlineUsersListController implements Controller {
  private static final Logger onlineUsersListLogger = LogManager.getLogger();

  @FXML
  private ListView<User> lvwOnlineUsers;

  // The list view updates itself when this observable list changes
  private ObservableList<User> onlineUsersList;

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
    lvwOnlineUsers.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
      @Override
      public ListCell<User> call(ListView<User> listView) {
        return new ListCell<User>() {
          @Override
          public void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
            if (user == null) {
              setText(null);
            } else {
              setText(user.getUsername());
            }
          }
        };
      }
    });

    lvwOnlineUsers.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent click) {
        if (click.getClickCount() == 2) {
          User clickedOnlineUser = lvwOnlineUsers.getSelectionModel().getSelectedItem();
          onlineUsersListLogger.debug(clickedOnlineUser.getUsername());
          OnlineUsersListController.this.mainController.getCentralFrameController()
            .setCentralContentDistantUser();
          OnlineUsersListController.this.mainController.getCentralFrameController()
            .getDistantUserController().setDistantUser(clickedOnlineUser);
        }
      }
    });
  }

  public void init() {
    try {
      displayOnlineUsers();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add new online user in online users list
   * which will update the list view.
   * @param user the user to add
   *
   */
  public void addNewOnlineUser(User user) {
    if (!onlineUsersList.contains(user)) {

      // This is done to avoid a "Not on FX application thread" error
      // Solution found here : https://stackoverflow.com/a/23007018
      Platform.runLater(new Runnable() {
        @Override
        public void run() {
          onlineUsersList.add(user);
          // Update ContactList :
          mainController.getContactListController().displayContacts();
        }
      });

    }
  }

  /**
   * Update the online user in online users list and distant user controller.
   * which will update its infos
   * @param user the user to update
   *
   */
  public void updateOnlineUser(User user) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        User userToUpdate = onlineUsersList.stream()
            .filter(currentUser -> currentUser.getUuid().equals(user.getUuid()))
            .findAny()
            .orElse(null);
        onlineUsersList.set(onlineUsersList.indexOf(userToUpdate), user);

        if (OnlineUsersListController.this.mainController
            .getCentralFrameController()
            .getDistantUserController()
            .getDistantUser()
            .equals(userToUpdate)
        ) {
          OnlineUsersListController.this.mainController
              .getCentralFrameController()
              .getDistantUserController()
              .setDistantUser(user);
        }
      }
    });
  }

  /**
   * Remove online user in online users list
   * which will update the list view.
   * @param user the user to add
   *
   */
  public void removeOnlineUser(User user) {
    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        onlineUsersList.remove(user);
        // Update ContactList :
        mainController.getContactListController().displayContacts();
      }
    });
  }

  /**
   * Fills the view with the username of already connected users.
   **/

  public void displayOnlineUsers() {
    ObservableList<User> onlineUsers = this.mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getOnlineUsers()
        .collect(Collectors.toCollection(FXCollections::observableArrayList));

    onlineUsersListLogger.info("Retrieved {} online users from Data", onlineUsers.size());
    onlineUsersList.setAll(onlineUsers);
  }

}
