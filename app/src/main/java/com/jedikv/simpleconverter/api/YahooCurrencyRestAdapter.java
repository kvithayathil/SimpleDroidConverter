package com.jedikv.simpleconverter.api;

import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.utils.Constants;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Kurian on 02/05/2015.
 */
public class YahooCurrencyRestAdapter implements IRestAdapter {

    private RestAdapter instance;

    public YahooCurrencyRestAdapter() {

        final OkHttpClient client = new OkHttpClient();

        //Show http logs only in debug builds
        RestAdapter.LogLevel logLevel;
        if(BuildConfig.LOG_HTTP_REQUESTS) {
            logLevel = RestAdapter.LogLevel.FULL;
        } else {
            logLevel = RestAdapter.LogLevel.NONE;
        }

        instance = new RestAdapter.Builder()
                .setEndpoint(Constants.YAHOO_CURRENCY_URL)
                .setLogLevel(logLevel)
                .setClient(new OkClient(client))
                .build();
    }

    @Override
    public RestAdapter getRestAdapter() {

        return instance;
    }
}
