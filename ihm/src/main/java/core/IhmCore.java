package core;

import interfaces.DataForIhm;

/**
 * the IhmCore will start the HCI of the Application, manages controllers and stage changes.
 */
public class IhmCore {
  
  
  
  
  private DataForIhm dataForIhm;
  private IhmForData ihmForData;
  
  private static IhmCore ihmCore;
  
  public static IhmCore init() {
    if(ihmCore == null) {
      ihmCore = new IhmCore();
      
      ihmCore.ihmForData = new IhmForData(ihmCore);
    }
    return ihmCore;
  }
  
  private IhmCore() {
    
  }
  
  public IhmForData getIhmForData () {
    return ihmForData;
  }
  
  public void setIhmForData (IhmForData ihmForData) {
    this.ihmForData = ihmForData;
  }
  
  public DataForIhm getDataForIhm () {
    return dataForIhm;
  }
  
  public void setDataForIhm (DataForIhm dataForIhm) {
    this.dataForIhm = dataForIhm;
  }
  
}
