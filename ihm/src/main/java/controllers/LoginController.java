package controllers;

import core.Application;
import core.IhmAlert;

import exceptions.data.DataException;
import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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

  @FXML
  private Button buttonLogin;

  @FXML
  private Label lblImport;

  private Application application;

  private DirectoryChooser importProfilDirectorySource = new DirectoryChooser();

  private File imporDirectorySource;
  
  private DirectoryChooser importProfilDirectory = new DirectoryChooser();
  
  private File directoryFile;

  /* Getters */

  public Button getLoginButton() {
    return this.buttonLogin;
  }

  /* Other methods */

  @Override
  public void initialize() {
    lblImport.setOnMouseClicked(event -> importClicked(event));
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
        .title("Connection refusée")
        .text("Il semblerait que vous ayez fait une erreur\ndans le login ou le mot de passe.")
        .darkStyle()
        .showWarning();

    } catch (IOException e) {
      loginLogger.error(e);

      String errorText = String.join(
          "La sauvegarde de l'application est corrompue.",
          "Supprimez votre fichier de sauvegarde.\n",
          "Créez un nouveau compte si le problème persiste."
      );

      Notifications.create()
        .title("Connexion refusée")
        .text(errorText)
        .darkStyle()
        .showWarning();
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
   * @param event the clicked event
   */
  public void importClicked(MouseEvent event) {
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    imporDirectorySource = importProfilDirectorySource.showDialog(primaryStage);
    if (imporDirectorySource == null) {
      IhmAlert
          .showAlert("File",
                   "aucun Dossier source choisi pour importer le profil selectionné",
                   "critical");
      return;
    }
    directoryFile = importProfilDirectory.showDialog(primaryStage);
    if (directoryFile == null) {
      IhmAlert
          .showAlert("Directory",
                   "aucun Dossier cible choisi pour importer le profil selectionné",
                   "critical");
      return;
    }
    try {
      // TODO: save add savepath
      this.application
        .getIhmCore()
        .getDataForIhm()
        .importProfile(imporDirectorySource.toPath(), directoryFile.toPath());
    } catch (java.io.IOException e) {
      LogManager.getLogger().error(e.getMessage());
      IhmAlert
        .showAlert("Directory","aucun dossier pour importer le profil selectionné","critical");
    } catch (DataException e) {
      LogManager.getLogger().error(e.getMessage());
      IhmAlert
          .showAlert("Data",
                     "données corompues, impossible de réaliser l'import du profil",
                     "critical");
    }
  }
}
