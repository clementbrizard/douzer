package controllers;

import datamodel.User;

import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContactListController implements Controller {

  private static final Logger contactListLogger = LogManager.getLogger();

  private MainController mainController;

  @FXML
  private ListView<User> lvContacts;

  // The list view updates itself when this observable list changes
  private ObservableList<User> contactsList;

  @Override
  public void initialize() {
    // Initialize the observable list
    contactsList = FXCollections.observableArrayList();

    // Set it as the data model of the ListView
    lvContacts.setItems(contactsList);
    lvContacts.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
      @Override
      public ListCell<User> call(ListView<User> listView) {
        return new ListCell<User>() {
          @Override
          public void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
            if (user == null) {
              setText(null);
            } else {

              if (user.isConnected()) {
                setText(user.getUsername() + " (connecté)");
              } else {
                setText(user.getUsername());
              }
            }
          }
        };
      }
    });

    lvContacts.setOnMouseClicked(click -> {
      if (click.getClickCount() == 2) {
        User clickedOnlineUser = lvContacts.getSelectionModel().getSelectedItem();
        mainController.getCentralFrameController()
                .setCentralContentDistantUser();
        mainController.getCentralFrameController()
                .getDistantUserController().setDistantUser(clickedOnlineUser);
        mainController.getCentralFrameController()
            .getDistantUserController().refreshFriendshipStatus();
      }
    });
  }

  public void init() {
    try {
      displayContacts();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Fills the view with the username of contacts.
   **/

  public void displayContacts() {

    ObservableList<User> contacts = this.mainController
            .getApplication()
            .getIhmCore()
            .getDataForIhm()
            .getCurrentUser()
            .getFriends()
            .stream()
            .collect(Collectors.toCollection(FXCollections::observableArrayList));

    contactListLogger.info("Retrieved {} contacts from Data", contacts.size());

    Platform.runLater(new Runnable() {
      @Override
      public void run() {
        contactsList.setAll(contacts);
      }
    });
  }

  public MainController getMainController() {
    return mainController;
  }

  public void setMainController(MainController mainController) {
    this.mainController = mainController;
  }
}
