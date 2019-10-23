package controllers;

import core.IhmCore;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

//remplace by javadocs when implementation
public class SignUpController implements Controller {
  
  private IhmCore ihmCore;

  @FXML
  private Label labelSignup;

  @FXML
  private Button buttonCreateAccount;
  @FXML
  private Button buttonCancel;
  
  @Override
  public void initialize()  {
    labelSignup.setText("Time to sign up");
  }

  @FXML
  private void actionSignup(ActionEvent event) {

    /*
    // TODO try with the real view, connect to data
    String userName = userTextField.getText();
    String password = passwordTextField.getText();
    String name = nameTextField.getText();
    String surname = surnameTextField.getText();
    //Date birthdate = birthdate???.getDate();
    //Avatar ???
    String secretQuestion = secretQuestionTextField.getText();
    String secretAnswer = secretAnswerTextField.getText();

    //LocalUser user = new LocalUser(...);

    try {
      getIhmcore().getDataForIhm().createUser(user);
      Stage mainControllerStage = getIhmcore().getMainController().getScene().getWindow();
      mainControllerStage.setScene(new Scene(new Pane()));

    } catch (SignupException se) {

      Notifications.create()
              .title("Signup failed")
              .text("It seems you entered something wrong. Try again.")
              .darkStyle()
              .showWarning();
    }

   */

  }
  
  public IhmCore getIhmCore() {
    return ihmCore;
  }
  
  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
}
