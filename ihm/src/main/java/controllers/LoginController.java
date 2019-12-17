package controllers;

import core.Application;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        .title("Connection refusée")
        .text("Il semblerait que vous ayez fait une erreur dans le login ou mot de passe.")
        .darkStyle()
        .showWarning();

    } catch (IOException e) {
      loginLogger.error(e);
      
      Notifications.create()
        .title("Connexion refusée")
        .text("La sauvegarde de l'application est corrompue. Supprimez votre fichier de sauvegarde. Créez un nouveau compte si le problème persiste.")
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

}
