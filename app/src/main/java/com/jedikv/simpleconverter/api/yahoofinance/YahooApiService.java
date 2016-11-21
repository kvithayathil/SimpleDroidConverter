package com.jedikv.simpleconverter.api.yahoofinance;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kurian on 02/05/2015.
 */
public interface YahooApiService {

    String SOURCE = "Yahoo Finance";

    //YQL - select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")

    @GET("public/yql?format=json&diagnostics=true&env=store://datatables.org/alltableswithkeys&callback=")
    Observable<YahooDataContainerResponse> getCurrencyPairs(@Query("q")String query);
}
