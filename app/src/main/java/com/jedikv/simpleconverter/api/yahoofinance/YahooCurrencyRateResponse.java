package com.jedikv.simpleconverter.api.yahoofinance;

import com.squareup.moshi.Json;

/**
 * Created by Kurian on 03/05/2015.
 */
public class YahooCurrencyRateResponse {

    @Json(name = "id")
    public final String id;
    @Json(name = "Name")
    public final String name;
    @Json(name = "Rate")
    public final String rate;
    @Json(name = "Date")
    public final String date;
    @Json(name = "Time")
    public final String time;
    @Json(name = "Ask")
    public final String ask;
    @Json(name = "Bid")
    public final String bid;

    public YahooCurrencyRateResponse(String id, String name, String rate, String date, String time,
        String ask, String bid) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.date = date;
        this.time = time;
        this.ask = ask;
        this.bid = bid;
    }
}
