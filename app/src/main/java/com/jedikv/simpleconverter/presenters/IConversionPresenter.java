package com.jedikv.simpleconverter.presenters;

import com.jedikv.simpleconverter.ui.conversionscreen.ConversionView;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionPresenter {

    void attachView(ConversionView view);

    void detachView();

    void onResume();

    void onPause();

    void convertValue(String value);

    void addCurrency(long targetCurrencyCode);

    void updateFromSourceCurrency(long sourceCurrencyCode);

    void removeItem(long id);
}
