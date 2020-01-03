package controllers;

import datamodel.LocalUser;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Controller used for managing the top left view
 * with the nickname and the buttons to modify profile and disconnect.
 */
public class UserInfoController implements Controller {
  private static final Logger logger = LogManager.getLogger();

  @FXML
  private Label lblUserPseudo;
  @FXML
  private Button btnModifyProfile;
  @FXML
  private Button btnDisconnect;

  @FXML
  private Button btnLogout;

  private MainController mainController;

  // Getters

  public MainController getMainController() {
    return mainController;
  }

  // Setters

  public void setMainController(MainController mainController) {
    this.mainController = mainController;

  }

  // Other methods

  @Override
  public void initialize() {
  }

  /**
   * Initialize the field with default data.
   */
  public void init() {
    String name = this.mainController
        .getApplication()
        .getIhmCore()
        .getDataForIhm()
        .getCurrentUser()
        .getUsername();

    lblUserPseudo.setText(name);
    this.connectButtons();
  }

  @FXML
  private void logout(ActionEvent event) {
    try {
      this.mainController.getApplication().getIhmCore().getDataForIhm().logout();
      this.mainController.getApplication().showLoginScene();
      this.mainController.getPlayerController().stopPlayer();
    } catch (IOException e) {
      logger.error(e);
    }
  }
    
  /**
   * Connects the buttons to functions.
   */
  private void connectButtons() {
    this.btnModifyProfile.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        UserInfoController.this.mainController.getCentralFrameController()
            .setCentralContentProfileEdit();
      }
    });
  }
}


