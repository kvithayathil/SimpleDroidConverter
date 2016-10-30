package com.jedikv.simpleconverter.api.yahoofinance;

import com.squareup.moshi.Json;

import java.util.Date;
import java.util.List;

/**
 * Created by Kurian on 03/05/2015.
 */
public class YahooDataContainerResponse {

    @Json(name = "query")
    public final YahooCurrencyQueryResult query;

    public YahooDataContainerResponse(YahooCurrencyQueryResult query) {
        this.query = query;
    }

    public static class YahooCurrencyRatesHolder {

        @Json(name = "rate")
        public final List<YahooCurrencyRateResponse> rate;

        public YahooCurrencyRatesHolder(List<YahooCurrencyRateResponse> rate) {
            this.rate = rate;
        }
    }

    public static class YahooCurrencyQueryResult {

        @Json(name = "count")
        public final int count;
        @Json(name = "created")
        public final Date created;
        @Json(name = "lang")
        public final String lang;
        @Json(name = "results")
        public final YahooCurrencyRatesHolder results;

        public YahooCurrencyQueryResult(int count, Date created, String lang, YahooCurrencyRatesHolder results) {
            this.count = count;
            this.created = created;
            this.lang = lang;
            this.results = results;
        }
    }
}
