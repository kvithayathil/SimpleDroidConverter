package com.jedikv.simpleconverter.api.yahoofinance;

import java.util.List;

/**
 * Created by Kurian on 03/05/2015.
 */
public class YahooDataContainerResponse {

    private YahooCurrencyQueryResult query;

    public YahooCurrencyQueryResult getQuery() {
        return query;
    }

    public static class YahooCurrencyResultContainer {

        private List<YahooCurrencyRateResponse> rate;

        public List<YahooCurrencyRateResponse> getRate() {
            return rate;
        }
    }

    public static class YahooCurrencyQueryResult {

        private int count;
        private String created;
        private String lang;
        private YahooCurrencyResultContainer results;

        public YahooCurrencyResultContainer getResults() {
            return results;
        }

        public int getCount() {
            return count;
        }

        public String getCreated() {
            return created;
        }

        public String getLang() {
            return lang;
        }

    }
}
