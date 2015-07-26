package com.jedikv.simpleconverter.utils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Kurian on 13/06/2015.
 */
public class YahooApiUtils {

    private static final String TAG = YahooApiUtils.class.getSimpleName();

    private YahooApiUtils() {
        Timber.tag(TAG);
    }

    /**
     * Build the YQL statement to pass into the request
     * @param currencyPairs list of currency pairs to pass in e.g. USDGBP, USDCHF...
     * @return the YQL statement to execute the request
     */
    public static String generateYQLCurrencyQuery(List<String> currencyPairs) {

        //select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")

        StringBuilder sb = new StringBuilder("select * from yahoo.finance.xchange where pair in ");
        sb.append("(");

        int count = 1;

        for (String currencyPair : currencyPairs) {

            sb.append("\"").append(currencyPair).append("\"");

            //Check if it's not the last entry in the list
            if(count < currencyPairs.size()) {
                sb.append(",");
            }

            count++;
        }

        sb.append(")");

        return sb.toString();
    }


    /**
     * Create the reverse pairs for
     * @param targetCurrencies list of currencies to get the converted values for
     * @param sourceCurrency the currency at the source of the conversion
     * @return A string list of currency pairs
     */
    public static List<String> createReverseFromPairs(List<String> targetCurrencies, String sourceCurrency) {

        List<String> currencyPairs = new ArrayList<>();

        for(String targetCurrency : targetCurrencies) {

            currencyPairs.add(targetCurrency + sourceCurrency);
            currencyPairs.add(sourceCurrency + targetCurrency);
        }

        return currencyPairs;
    }
}
