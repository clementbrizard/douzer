package controllers;

import core.Application;

//replace by javadocs
//popupview when user whant to delete his profile
public class ProfileDeletionController implements Controller {

  private ProfileEditController profileEditController;
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public ProfileEditController getProfileEditController() {
    return profileEditController;
  }

  public void setProfileEditController(ProfileEditController profileEditController) {
    this.profileEditController = profileEditController;
  }
}
