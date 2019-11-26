package controllers;

//Replace by javadocs
//view with search bar and other search option
//Let's see if we can create an interface who regroup 
//  all the view that will be needed to do a research 
//  (in our case, there are several parents, which can be weird)
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
