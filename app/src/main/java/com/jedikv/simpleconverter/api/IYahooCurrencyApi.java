package com.jedikv.simpleconverter.api;

import com.jedikv.simpleconverter.api.responses.YahooDataContainer;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kurian on 02/05/2015.
 */
public interface IYahooCurrencyApi {

    //YQL - select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")

    @GET("public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    Observable<YahooDataContainer> getCurrencyPairs(@Query("q")String query);
}
