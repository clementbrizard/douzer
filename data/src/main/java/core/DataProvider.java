package core;


import exceptions.DataException;
import interfaces.*;

public class DataProvider {
  private static volatile Datacore dc;

  void initData(Net net, Ihm ihm) throws DataException {
        if (DataProvider.dc != null) {
            DataProvider.dc = new Datacore(net, ihm);
        } else {
            throw new DataException("Datacore already initialized.");
        }
    }

    DataForNet getDataForNet() {
        return new DataForNetImpl();
    }

    DataForIhm getDataForIhm() {
        return new DataForIhmImpl();
    }
}
