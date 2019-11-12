import core.DataProvider;
import core.IhmCore;
import exceptions.DataException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import provider.NetworkProvider;

public class Main {

  private static final Logger startLogger = LogManager.getLogger();

  /**
   * Initialize all the modules and starts the application.
   */
  public static void main(String[] args) {

    startLogger.info("Application start");

    IhmCore ihmCore = new IhmCore();
    NetworkProvider networkProvider = new NetworkProvider();
    DataProvider dataProvider = new DataProvider();

    try {
      dataProvider.initData(networkProvider.getNetwork(), ihmCore.getIhmForData());
    } catch (DataException e) {
      e.printStackTrace();
    }
    ihmCore.setDataForIhm(dataProvider.getDataForIhm());
    networkProvider.setDataImpl(dataProvider.getDataForNet());

    ihmCore.run(args);
  }
}
