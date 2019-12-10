package core;

import interfaces.DataForIhm;
import org.apache.logging.log4j.LogManager;

/**
 * The IhmCore will start the HCI of the Application, manage controllers and stage changes.
 */
public class IhmCore {
  private static IhmCore ihmCore;

  private DataForIhm dataForIhm;
  private IhmForData ihmForData;

  private IhmCore() {
  }

  public DataForIhm getDataForIhm() {
    return dataForIhm;
  }

  public IhmForData getIhmForData() {
    return ihmForData;
  }

  public void setDataForIhm(DataForIhm dataForIhm) {
    this.dataForIhm = dataForIhm;
  }

  public void setIhmForData(IhmForData ihmForData) {
    this.ihmForData = ihmForData;
  }

  public static IhmCore init() {
    if (ihmCore == null) {
      LogManager.getLogger().info("IhmCore start");
      ihmCore = new IhmCore();

      ihmCore.ihmForData = new IhmForData(ihmCore);
    }
    return ihmCore;
  }

}
