package com.jedikv.simpleconverter.injection.module;

import android.content.Context;
import android.text.TextUtils;

import com.jedikv.simpleconverter.api.IYahooCurrencyApi;
import com.jedikv.simpleconverter.api.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.api.YahooCurrencyRestAdapter;
import com.jedikv.simpleconverter.dbutils.ConversionItemDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.presenters.ConversionInteractorImpl;
import com.jedikv.simpleconverter.presenters.IPresenterBase;

import javax.inject.Singleton;

import converter_db.DaoSession;
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
    public YahooCurrencyDownloadService provideYahooCurrencyDownloadService(IYahooCurrencyApi api, CurrencyDbHelper currencyDbHelper, CurrencyPairDbHelper currencyPairDbHelper) {
        return new YahooCurrencyDownloadService(api, currencyDbHelper, currencyPairDbHelper);
    }

    @Provides
    public ConversionInteractorImpl provideConversionInteractor(YahooCurrencyDownloadService downloadService) {
        return new ConversionInteractorImpl(downloadService);
    }

}
