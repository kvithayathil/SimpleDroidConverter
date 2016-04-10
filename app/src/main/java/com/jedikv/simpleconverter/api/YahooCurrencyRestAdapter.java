package com.jedikv.simpleconverter.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kurian on 02/05/2015.
 */
public class YahooCurrencyRestAdapter implements IRestAdapter {

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


        instance = new Retrofit.Builder()
                .baseUrl(Constants.YAHOO_CURRENCY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Override
    public Retrofit getRestAdapter() {

        return instance;
    }
}
