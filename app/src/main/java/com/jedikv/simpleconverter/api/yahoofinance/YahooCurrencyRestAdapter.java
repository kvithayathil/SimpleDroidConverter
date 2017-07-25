package com.jedikv.simpleconverter.api.yahoofinance;

import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.api.RestAdapter;
import com.jedikv.simpleconverter.api.jsonadapters.DateAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by Kurian on 02/05/2015.
 */
public class YahooCurrencyRestAdapter implements RestAdapter {

    private final Retrofit instance;

    public YahooCurrencyRestAdapter(final OkHttpClient client) {

        //Show http logs only in debug builds
        final Moshi moshi = new Moshi.Builder()
                .add(new DateAdapter("yyyy-MM-dd'T'HH:mm:ss'Z'"))
                .build();

        instance = new Retrofit.Builder()
                .baseUrl("https://query.yahooapis.com/v1/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Override
    public Retrofit getRestAdapter() {
        return instance;
    }
}
