package controllers;

//replace by javadocs
//view signup after click on signup button
public class ImportController implements Controller {

  private LoginController loginController;

  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public LoginController getLoginController() {
    return loginController;
  }

  public void setLoginController(LoginController loginController) {
    this.loginController = loginController;
  }
}
