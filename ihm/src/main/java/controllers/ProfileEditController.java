package controllers;

//replace by javadocs
//central view who permit the user to edit his profile
public class ProfileEditController implements Controller {

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
	
	

}
