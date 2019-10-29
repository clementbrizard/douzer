package core;

import interfaces.Ihm;

public class IhmProvider {
  public IhmCore ihmCore;

  IhmProvider() {}

  public Ihm init() {
    ihmCore = new IhmCore();
    return ihmCore.getIhmForData();
  }

  public void setDataInterface(DataForIhm data) {
    // TODO : set data interface
    // ihmCore.setDataInterface(data);
  }

  public void startApplication(String[] args) {
    ihmCore.run(args);
  }

}
