package com.jedikv.simpleconverter.presenters;

import java.util.List;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionPresenter extends IPresenterBase {

    void downloadCurrency(List<String> currencyList);

    void convertValue(String value);

    void addCurrency(long currencyCode);

    void updateSourceCurrency(List<String> currencyList);
}
