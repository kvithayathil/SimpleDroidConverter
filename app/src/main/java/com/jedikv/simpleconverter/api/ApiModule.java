package com.jedikv.simpleconverter.api;

import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.api.yahoofinance.YahooApiService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyDownloadService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyRestAdapter;
import com.jedikv.simpleconverter.domain.repository.ConversionRepository;
import com.jedikv.simpleconverter.domain.repository.YahooConversionRepository;
import com.jedikv.simpleconverter.domain.repository.YqlStringHelper;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

/**
 * Created by Kurian on 31/10/2016.
 */
@Module
public class ApiModule {

    public ApiModule() {
    }

    @Provides @NonNull @Singleton
    Cache provideHttpCache(File file) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(file, cacheSize);
    }

    @Provides @NonNull @Singleton
    YahooApiService providesYahooApiService(Cache cache) {
        return new YahooCurrencyRestAdapter(cache)
                .getRestAdapter()
                .create(YahooApiService.class);
    }

    @Provides @NonNull @Singleton @Named("YahooRepository")
    ConversionRepository provideYahooConversionRepository(YahooApiService api) {
        return new YahooConversionRepository(api);
    }
}
