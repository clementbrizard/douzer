package controllers;

//replace by javadocs
//central view after click on a user, show info of this user and his music
public class DistantUserController implements Controller {

	private SearchMusicController searchMusicController;
	
	private CentralFrameController centralFrameController;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public SearchMusicController getSearchMusicController() {
		return searchMusicController;
	}

	public void setSearchMusicController(SearchMusicController searchMusicController) {
		this.searchMusicController = searchMusicController;
	}

	public CentralFrameController getCentralFrameController() {
		return centralFrameController;
	}

	public void setCentralFrameController(CentralFrameController centralFrameController) {
		this.centralFrameController = centralFrameController;
	}
	

}
