package core;
import controllers.*;

//replace by javadocs
//start the FX application
public class IHMCore {

	private MainController mainController;
	private LoginController loginController;
	private SignUpController signUpController;
	
	public IHMCore() {
		
	}
	
	public void setMainController(MainController mainController) {
		this.mainController = mainController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public void setSignUpController(SignUpController signUpController) {
		this.signUpController = signUpController;
	}
	
	public MainController getMainController(){
		return this.mainController;
	}
	
	public LoginController getLoginController(){
		return this.loginController;
	}
	
	public SignUpController getSignUpController(){
		return this.signUpController;
	}
	
}
