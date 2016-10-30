package com.jedikv.simpleconverter.api.yahoofinance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.api.RestAdapter;
import com.jedikv.simpleconverter.api.jsonadapters.DateAdapter;
import com.jedikv.simpleconverter.utils.Constants;
import com.squareup.moshi.Moshi;

import java.util.Date;

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

    public YahooCurrencyRestAdapter() {

        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        //Show http logs only in debug builds
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        final Moshi moshi = new Moshi.Builder()
                .add(Date.class, new DateAdapter("yyyy-MM-dd'T'HH:mm:ssZ"))
                .build();

        instance = new Retrofit.Builder()
                .baseUrl(Constants.YAHOO_CURRENCY_URL)
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
