package controllers;

import core.IhmCore;

//replace by javadocs
//login view
public class LoginController implements Controller {

  private IhmCore ihmcore;
  private ImportController importController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public IhmCore getIhmcore() {
    return ihmcore;
  }

  public void setIhmcore(IhmCore ihmcore) {
    this.ihmcore = ihmcore;
  }

  public ImportController getImportController() {
    return importController;
  }

  public void setImportController(ImportController importController) {
    this.importController = importController;
  }
}
