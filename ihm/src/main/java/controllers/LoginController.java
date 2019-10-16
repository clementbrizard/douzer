package controllers;

import core.IHMCore;

//replace by javadocs
//login view
public class LoginController implements Controller {

	private IHMCore ihmcore;
	private ImportController importController;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public IHMCore getIhmcore() {
		return ihmcore;
	}

	public void setIhmcore(IHMCore ihmcore) {
		this.ihmcore = ihmcore;
	}

	public ImportController getImportController() {
		return importController;
	}

	public void setImportController(ImportController importController) {
		this.importController = importController;
	}
	
	

}
