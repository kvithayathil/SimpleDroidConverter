package com.jedikv.simpleconverter.injection.module;

import com.jedikv.simpleconverter.api.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.api.YahooCurrencyRestAdapter;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;

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
    public YahooCurrencyDownloadService provideYahooCurrencyDownloadService(RestAdapter restAdapter, CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        return new YahooCurrencyDownloadService(restAdapter, currencyDbHelper, currencyPairDbHelper);
    }
}
