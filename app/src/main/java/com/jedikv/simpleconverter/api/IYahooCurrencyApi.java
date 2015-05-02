package com.jedikv.simpleconverter.api;

import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.mime.TypedString;

/**
 * Created by Kurian on 02/05/2015.
 */
public interface IYahooCurrencyApi {

    @GET("v1/public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    public void getCurrencyPairs(@Query("q")TypedString query);
}
