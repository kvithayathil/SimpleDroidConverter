package com.jedikv.simpleconverter.api.yahoofinance;

/**
 * Created by Kurian on 03/05/2015.
 */
public class YahooCurrencyRateResponse {

    private String id;
    private String Name;
    private String Rate;
    private String Date;
    private String Time;
    private String Ask;
    private String Bid;

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getRate() {
        return Rate;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getAsk() {
        return Ask;
    }

    public String getBid() {
        return Bid;
    }
}
