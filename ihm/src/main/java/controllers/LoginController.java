package controllers;

import core.IhmCore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


//replace by javadocs
//login view
public class LoginController implements Controller {

  private IhmCore ihmcore;
  private ImportController importController;

  private Scene signUpScene;

  @FXML
  private TextField textFieldPseudo;
  @FXML
  private TextField textFieldPassword;


  public void setSignUpScene(Scene sceneSignUp) {
    signUpScene = sceneSignUp;
  }

  @Override
  public void initialize() {

  }

  @FXML
  private void actionLogin(ActionEvent event) {



    // TODO try with the real view, connect to data
    String userName = textFieldPseudo.getText();
    String password = textFieldPassword.getText();
    System.out.println("Pseudo:" + userName + ", pass: " + password);
    /*
    try {
      ihmcore.getDataForIhm().login(userName, password);
      //Go to Main view

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
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.setScene(signUpScene);

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
