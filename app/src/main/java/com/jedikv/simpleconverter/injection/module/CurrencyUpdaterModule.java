package com.jedikv.simpleconverter.injection.module;

import com.jedikv.simpleconverter.api.CurrencyDownloadService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyApi;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyRestAdapter;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.presenters.ConversionPresenter;
import com.jedikv.simpleconverter.presenters.CurrencyListPresenter;
import com.jedikv.simpleconverter.presenters.ICurrencyListPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class CurrencyUpdaterModule {

    @Provides
    @Singleton
    public Retrofit provideYahooCurrencyRestAdapter() {
        return new YahooCurrencyRestAdapter().getRestAdapter();
    }


    @Provides
    @Singleton
    YahooCurrencyApi provideYahooCurrencyApi(Retrofit restAdapter) {
        return restAdapter.create(YahooCurrencyApi.class);
    }

    @Provides
    @Singleton
    public CurrencyDownloadService provideCurrencyDownloadService(YahooCurrencyApi api, CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        return new YahooCurrencyDownloadService(api, currencyDbHelper, currencyPairDbHelper);
    }

    @Provides
    public ConversionPresenter provideConversionPresenter(CurrencyDownloadService downloadService, CurrencyPairDbHelper currencyPairDbHelper, CurrencyDbHelper currencyDbHelper) {
        return new ConversionPresenter(downloadService, currencyPairDbHelper, currencyDbHelper);
    }

    @Provides
    public ICurrencyListPresenter provideCurrencyListPresenter(CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        return new CurrencyListPresenter(currencyPairDbHelper, currencyDbHelper);
    }

}
