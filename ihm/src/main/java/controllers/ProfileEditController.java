package controllers;

import core.Application;
import java.io.IOException;
import javafx.event.ActionEvent;
import org.controlsfx.control.Notifications;


//replace by javadocs
//central view who permit the user to edit his profile
public class ProfileEditController implements Controller {
  private Application application;

  private ExportProfileController exportProfileController;
  private PasswordEditController passwordEditController;
  private ProfileDeletionController profileDeletionController;

  private CentralFrameController centralFrameController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public ExportProfileController getExportProfileController() {
    return exportProfileController;
  }

  public void setExportProfileController(ExportProfileController exportProfileController) {
    this.exportProfileController = exportProfileController;
  }

  public PasswordEditController getPasswordEditController() {
    return passwordEditController;
  }

  public void setPasswordEditController(PasswordEditController passwordEditController) {
    this.passwordEditController = passwordEditController;
  }

  public ProfileDeletionController getProfileDeletionController() {
    return profileDeletionController;
  }

  public void setProfileDeletionController(ProfileDeletionController profileDeletionController) {
    this.profileDeletionController = profileDeletionController;
  }

  public CentralFrameController getCentralFrameController() {
    return centralFrameController;
  }

  public void setCentralFrameController(CentralFrameController centralFrameController) {
    this.centralFrameController = centralFrameController;
  }

  public void deleteAccount(ActionEvent e) {
    //String password = textFieldPassword.getText();
    try {
      this.centralFrameController.getMainController().getApplication().getIhmCore().getDataForIhm().deleteAccount();
      Notifications.create()
          .title("good Deletion ")
          .text("the deletion has been carried out")
          .darkStyle()
          .showWarning();
      this.getCentralFrameController().getMainController().getUserInfoController().logout(null);

    } catch (IOException ex) {
      ex.printStackTrace();
      System.out.println("IO problems");
    } catch (Exception de) {
      de.printStackTrace();

      Notifications.create()
          .title("Deletion failed")
          .text("an error occurred while deleting")
          .darkStyle()
          .showWarning();

    }

  }
}

