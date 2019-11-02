package controllers;

//replace by javadocs
//central view with all user music
public class MyMusicsController implements Controller {

  private NewMusicController newMusicController;
  private SearchMusicController searchMusicController;

  private CentralFrameController centralFrameController;
  private DetailsMusicController detailsMusicController;
  
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }

  public NewMusicController getNewMusicController() {
    return newMusicController;
  }

  public void setNewMusicController(NewMusicController newMusicController) {
    this.newMusicController = newMusicController;
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
  

  public DetailsMusicController getDetailMusicController() {
    return detailsMusicController;
  }
  
  public void setDetailMusicController(DetailsMusicController detailsMusicController) {
    this.detailsMusicController = detailsMusicController;
  }
}
