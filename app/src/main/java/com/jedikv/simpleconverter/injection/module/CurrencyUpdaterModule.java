package com.jedikv.simpleconverter.injection.module;

import com.jedikv.simpleconverter.api.ICurrencyDownloadService;
import com.jedikv.simpleconverter.api.IYahooCurrencyApi;
import com.jedikv.simpleconverter.api.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.api.YahooCurrencyRestAdapter;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.presenters.ConversionPresenter;
import com.jedikv.simpleconverter.presenters.CurrencyListPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class CurrencyUpdaterModule {

    @Provides
    @Singleton
    public RestAdapter provideYahooCurrencyRestAdapter() {
        return new YahooCurrencyRestAdapter().getRestAdapter();
    }


    @Provides
    @Singleton
    IYahooCurrencyApi provideYahooCurrencyApi(RestAdapter restAdapter) {
        return restAdapter.create(IYahooCurrencyApi.class);
    }

    @Provides
    @Singleton
    public ICurrencyDownloadService provideCurrencyDownloadService(IYahooCurrencyApi api, CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        return new YahooCurrencyDownloadService(api, currencyDbHelper, currencyPairDbHelper);
    }

    @Provides
    public ConversionPresenter provideConversionPresenter(ICurrencyDownloadService downloadService, CurrencyPairDbHelper currencyPairDbHelper, CurrencyDbHelper currencyDbHelper) {
        return new ConversionPresenter(downloadService, currencyPairDbHelper, currencyDbHelper);
    }

    @Provides
    public CurrencyListPresenter provideCurrencyListPresenter(CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        return new CurrencyListPresenter(currencyPairDbHelper, currencyDbHelper);
    }

}
