import core.DataProvider;
import core.IhmCore;
import exceptions.DataException;
import provider.NetworkProvider;

public class Main {
  /**
   * Initialize all the modules and starts the application.
   */
  public static void main(String[] args) {
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
