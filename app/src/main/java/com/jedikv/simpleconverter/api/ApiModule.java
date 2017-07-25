package com.jedikv.simpleconverter.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.AppModule;
import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.ConversionAppScope;
import com.jedikv.simpleconverter.api.yahoofinance.YahooApiService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooApiServiceImpl;

import java.io.File;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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
    File provideHttpCacheFile(@NonNull Context context) {
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
    YahooApiService providesYahooApiService(@NonNull OkHttpClient client) {
        return new YahooApiServiceImpl(client);
    }

    @Provides
    @NonNull
    @ConversionAppScope
    HttpLoggingInterceptor.Level providesLoggingLevel() {
        if(BuildConfig.DEBUG) {
            return HttpLoggingInterceptor.Level.BODY;
        } else {
            return HttpLoggingInterceptor.Level.NONE;
        }
    }

    @Provides
    @NonNull
    @ConversionAppScope
    HttpLoggingInterceptor providesHttpLoggingInterceptor(
        @NonNull HttpLoggingInterceptor.Level loggingLevel) {
        return new HttpLoggingInterceptor().setLevel(loggingLevel);
    }

    @Provides
    @NonNull
    @ConversionAppScope
    OkHttpClient providesOkhttpClient(@NonNull Cache cache,
        @NonNull HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .build();
    }
}
