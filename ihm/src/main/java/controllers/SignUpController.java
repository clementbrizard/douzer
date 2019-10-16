package controllers;

import core.IhmCore;

//remplace by javadocs when implementation
public class SignUpController implements Controller {
  
  private IhmCore ihmCore;
  
  @Override
  public void initialize() {
    // TODO Auto-generated method stub
  }
  
  public IhmCore getIhmCore() {
    return ihmCore;
  }
  
  public void setIhmCore(IhmCore ihmCore) {
    this.ihmCore = ihmCore;
  }
}
