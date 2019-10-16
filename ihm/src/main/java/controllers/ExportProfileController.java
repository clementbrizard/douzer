package controllers;

//Replace by javadocs
//popup view with many option to export the user profile
public class ExportProfileController implements Controller {

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
