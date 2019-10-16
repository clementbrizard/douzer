package controllers;

//Replace by javadocs
//the left middle list of all User 
public class OnlineUsersListController implements Controller {

  private OnlineUsersListController onlineUsersListController;
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public OnlineUsersListController getOnlineUsersListController() {
    return onlineUsersListController;
  }

  public void setOnlineUsersListController(OnlineUsersListController onlineUsersListController) {
    this.onlineUsersListController = onlineUsersListController;
  }
}
