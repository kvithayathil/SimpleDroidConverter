package com.jedikv.simpleconverter.presenters;

import java.util.List;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionInteractor {

    void downloadCurrency(List<String> currencyList);

    void convertValue(String value);

    void onResume();

    void onPause();
}
