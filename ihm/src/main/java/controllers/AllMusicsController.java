package controllers;

import core.IhmCore;

//replace by javadocs
//central view show up all music in the network
public class AllMusicsController implements Controller {

  private IhmCore ihmCore;
  
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

  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
}
