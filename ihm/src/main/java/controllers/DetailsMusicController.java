package controllers;


//replace by javadocs
//central view to show all information about on music
public class DetailsMusicController implements Controller {

	private CentralFrameController centralFrameController;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public CentralFrameController getCentralFrameController() {
		return centralFrameController;
	}

	public void setCentralFrameController(CentralFrameController centralFrameController) {
		this.centralFrameController = centralFrameController;
	}
	
	

}
