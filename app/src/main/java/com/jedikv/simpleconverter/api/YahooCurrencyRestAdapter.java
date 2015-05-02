package com.jedikv.simpleconverter.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedikv.simpleconverter.deserializer.CurrencyPairResponseDeserializer;
import com.jedikv.simpleconverter.response.ExchangePairResponse;
import com.jedikv.simpleconverter.utils.Constants;

import retrofit.RestAdapter;
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
        Gson gson = new GsonBuilder().registerTypeAdapter(ExchangePairResponse.class, new CurrencyPairResponseDeserializer()).create();

        if(mInstance == null) {
            mInstance = new RestAdapter.Builder()
                    .setEndpoint(Constants.YAHOO_CURRENCY_URL)
                    .setConverter(new GsonConverter(gson))
                    .build();
        }

        return mInstance;
    }
}
