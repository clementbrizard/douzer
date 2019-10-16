package controllers;

//replace by javadocs
//righdown view with progress bar about current music download
public class DownloadController implements Controller {

	private MainController mainController;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public MainController getMainController() {
		return mainController;
	}

	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	
	
}
