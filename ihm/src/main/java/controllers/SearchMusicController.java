package controllers;

//Replace by javadocs
//view whith search bar and other search option
//a voir si on ne cr�er pas un interface qui regroupe toute les vues qui auront besoin de faire une recherche (dans le cas pr�sent il y a plusieurs parents �a peux etre bizarre)
public class SearchMusicController implements Controller {

	
	private MyMusicsController myMusicsController;
	private DistantUserController distantUsercontroller;
	private AllMusicsController allMusicController;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public MyMusicsController getMyMusicsController() {
		return myMusicsController;
	}

	public void setMyMusicsController(MyMusicsController myMusicsController) {
		this.myMusicsController = myMusicsController;
	}

	public DistantUserController getDistantUsercontroller() {
		return distantUsercontroller;
	}

	public void setDistantUsercontroller(DistantUserController distantUsercontroller) {
		this.distantUsercontroller = distantUsercontroller;
	}

	public AllMusicsController getAllMusicController() {
		return allMusicController;
	}

	public void setAllMusicController(AllMusicsController allMusicController) {
		this.allMusicController = allMusicController;
	}

	
}
