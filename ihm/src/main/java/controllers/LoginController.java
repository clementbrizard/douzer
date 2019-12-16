package controllers;

import core.Application;
import core.IhmAlert;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.security.auth.login.LoginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

/**
 * Controller used for the login view.
 */
public class LoginController implements Controller {

  private static final Logger loginLogger = LogManager.getLogger();

  @FXML
  private TextField textFieldPseudo;

  @FXML
  private PasswordField textFieldPassword;

  private Application application;
  
  private FileChooser importProfilFile = new FileChooser();
  
  private File importFile;

  // Other methods

  @Override
  public void initialize() {
  }

  @FXML
  private void actionLogin() {
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();

    try {
      this.application.getIhmCore().getDataForIhm().login(userName, password);
      application.getMainController().init();
      this.application.showMainScene();

    } catch (LoginException e) {

      loginLogger.error(e);

      Notifications.create()
          .title("Connection failed")
          .text("It seems you entered a wrong username/password. Try again.")
          .darkStyle()
          .showWarning();

    } catch (IOException e) {
      loginLogger.error(e);
    }
  }

  @FXML
  private void actionSignup() {
    application.showSignUpScene();
  }

  @FXML
  private void actionForgottenPassword() {
    application.showForgottenPasswordScene();
  }

  public void setApplication(Application application) {
    this.application = application;
  }
  
  /**
   * The function who call the windows to choose a export directory. 
   * @param evt the clicked event
   */
  @FXML
  public void importClicked(ActionEvent evt) {
    Stage primaryStage = (Stage) ((Node) evt.getSource()).getScene().getWindow();
    importFile = importProfilFile.showOpenDialog(primaryStage);
    try {
      this.application
        .getIhmCore()
        .getDataForIhm()
        .importProfile(importFile.getAbsolutePath());
    } catch (UnsupportedOperationException e) {
      LogManager.getLogger().error(e.getMessage());
      IhmAlert.showAlert("implementation","pas encore implémenté","critical");
    } catch (java.lang.RuntimeException e) {
      IhmAlert
        .showAlert("Directory","aucun dossier pour exporter le profil selectionné","critical");
    }
  }
}
