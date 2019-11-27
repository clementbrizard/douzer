package core;

import exceptions.data.DataException;
import interfaces.DataForIhm;
import interfaces.DataForIhmImpl;
import interfaces.DataForNet;
import interfaces.DataForNetImpl;
import interfaces.Ihm;
import interfaces.Net;

public class DataProvider {
  private static volatile Datacore dc;

  public void initData(Net net, Ihm ihm) throws DataException {
    if (DataProvider.dc == null) {
      DataProvider.dc = new Datacore(net, ihm);
    } else {
      throw new DataException("Datacore already initialized.");
    }
  }

  public DataForNet getDataForNet() {
    return new DataForNetImpl(DataProvider.dc);
  }

  public DataForIhm getDataForIhm() {
    return new DataForIhmImpl(DataProvider.dc);
  }
}
