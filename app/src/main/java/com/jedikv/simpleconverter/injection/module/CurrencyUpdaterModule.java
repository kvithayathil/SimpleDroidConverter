package com.jedikv.simpleconverter.injection.module;

import com.jedikv.simpleconverter.api.CurrencyDownloadService;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.presenters.ConversionPresenter;
import com.jedikv.simpleconverter.presenters.CurrencyListPresenter;
import com.jedikv.simpleconverter.presenters.ICurrencyListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class CurrencyUpdaterModule {

    @Provides
    public ConversionPresenter provideConversionPresenter(CurrencyDownloadService downloadService,
                                                          CurrencyPairDbHelper currencyPairDbHelper,
                                                          CurrencyDbHelper currencyDbHelper) {
        return new ConversionPresenter(downloadService, currencyPairDbHelper, currencyDbHelper);
    }

    @Provides
    public ICurrencyListPresenter provideCurrencyListPresenter(CurrencyDbHelper currencyDbHelper,
                                                               CurrencyPairDbHelper currencyPairDbHelper) {
        return new CurrencyListPresenter(currencyPairDbHelper, currencyDbHelper);
    }

}
