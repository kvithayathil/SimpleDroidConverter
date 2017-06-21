package com.jedikv.simpleconverter.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.AppModule;
import com.jedikv.simpleconverter.ConversionAppScope;
import com.jedikv.simpleconverter.api.yahoofinance.YahooApiService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyRestAdapter;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

/**
 * Created by Kurian on 31/10/2016.
 */
@Module(includes = {AppModule.class})
public class ApiModule {

    public static final String CACHE_NAME = "okhttp3_cache";

    @NonNull
    @Provides
    @OkhttpCacheFile
    @ConversionAppScope
    File provideHttpCacheFile(Context context) {
        return new File(context.getCacheDir(), CACHE_NAME);
    }

    @Provides
    @NonNull
    @ConversionAppScope
    Cache provideHttpCache(@OkhttpCacheFile File file) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(file, cacheSize);
    }

    @Provides
    @NonNull
    @ConversionAppScope
    YahooApiService providesYahooApiService(Cache cache) {
        return new YahooCurrencyRestAdapter(cache)
                .getRestAdapter()
                .create(YahooApiService.class);
    }
}
