package controllers;


//replace by javadocs 
//popup view show when user whant to change his password
public class PasswordEditController implements Controller {

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
