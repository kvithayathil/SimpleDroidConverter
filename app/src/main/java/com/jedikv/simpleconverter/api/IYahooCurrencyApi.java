package com.jedikv.simpleconverter.api;

import com.jedikv.simpleconverter.api.responses.YahooDataContainer;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Kurian on 02/05/2015.
 */
public interface IYahooCurrencyApi {


    @GET("/v1/public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    public Observable<YahooDataContainer> getCurrencyPairs(@Query("q")String query);

    @GET("/v1/public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    public void getCurrencyPairs(@Query("q")String query, Callback<YahooDataContainer> callback);

    @GET("/v1/public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    public void getCurrencyPairs(@Query("q")String query, Observable<YahooDataContainer> observable);

    //select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")
}
