package com.jedikv.simpleconverter.presenters;

import com.jedikv.simpleconverter.ui.views.IConversionView;

import java.util.List;

/**
 * Created by KV_87 on 20/09/2015.
 */
public interface IConversionPresenter {

    void attachView(IConversionView view);

    void detachView();

    void onResume();

    void onPause();

    void convertValue(String value);

    void addCurrency(long sourceCurrency, long targetCurrencyCode);

    void updateFromSourceCurrency(long sourceCurrencyCode);
}
