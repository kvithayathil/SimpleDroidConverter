package com.jedikv.simpleconverter.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.deserializer.CurrencyPairResponseDeserializer;
import com.jedikv.simpleconverter.response.ExchangePairResponse;
import com.jedikv.simpleconverter.utils.Constants;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Kurian on 02/05/2015.
 */
public class YahooCurrencyRestAdapter {

    private static RestAdapter mInstance;

    private YahooCurrencyRestAdapter() {

    }

    public static RestAdapter getInstance() {

        //Custom converter for yahoo currency response
        //Gson gson = new GsonBuilder().registerTypeAdapter(ExchangePairResponse.class, new CurrencyPairResponseDeserializer()).create();

        if(mInstance == null) {

            final OkHttpClient client = new OkHttpClient();

            //Show http logs only in debug builds
            RestAdapter.LogLevel logLevel;
            if(BuildConfig.LOG_HTTP_REQUESTS) {
                logLevel = RestAdapter.LogLevel.FULL;
            } else {
                logLevel = RestAdapter.LogLevel.NONE;
            }

            mInstance = new RestAdapter.Builder()
                    .setEndpoint(Constants.YAHOO_CURRENCY_URL)
                    .setLogLevel(logLevel)
                    .setClient(new OkClient(client))
                    .build();
        }

        return mInstance;
    }
}
