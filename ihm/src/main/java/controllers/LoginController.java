package controllers;

import core.IhmCore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;


//replace by javadocs
//login view
public class LoginController implements Controller {

  private IhmCore ihmcore;
  private ImportController importController;

  @FXML
  private Label labelLogin;

  @FXML
  private Button buttonLogin;
  @FXML
  private Button buttonSignup;
  @FXML
  private Button buttonForgottenPassword;
  @FXML
  private Button buttonImportProfile;




  @Override
  public void initialize() {
    labelLogin.setText("Time to connect");
  }

  @FXML
  private void actionLogin(ActionEvent event) {
    labelLogin.setText("Connecting");

    /*
    // TODO try with the real view, connect to data
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();
    try {
      getIhmcore().getDataForIhm().login(userName, password);
      Stage mainControllerStage = getIhmcore().getMainController().getScene().getWindow();
      mainControllerStage.setScene(new Scene(new Pane()));

    } catch (LoginException le) {

      Notifications.create()
              .title("Connection failed")
              .text("It seems you entered a wrong username/password. Try again.")
              .darkStyle()
              .showWarning();
    }
   */
  }

  @FXML
  private void actionSignup(ActionEvent event) {
    labelLogin.setText("Sign up");

    /*
      Stage signUpStage = getIhmcore().getSignUpController().getScene().getWindow();
      signUpStage.setScene(new Scene(new Pane()));

     */
  }

  public IhmCore getIhmcore() {
    return ihmcore;
  }

  public void setIhmcore(IhmCore ihmcore) {
    this.ihmcore = ihmcore;
  }

  public ImportController getImportController() {
    return importController;
  }

  public void setImportController(ImportController importController) {
    this.importController = importController;
  }
}
