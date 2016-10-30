package com.jedikv.simpleconverter.domain.repository;

import com.jedikv.simpleconverter.api.yahoofinance.YahooApiService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyRateResponse;
import com.jedikv.simpleconverter.api.yahoofinance.YahooDataContainerResponse;
import com.jedikv.simpleconverter.domain.ConversionItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Kurian on 30/10/2016.
 */

public class YahooConversionRepository implements ConversionRepository {

    private final YahooApiService api;

    public YahooConversionRepository(YahooApiService api) {
        this.api = api;
    }

    @Override
    public Observable<List<ConversionItem>> getConversionItems(String source,
                                                               List<String> selectedCurrencies) {

        List<String> currencyPair = createReverseFromPairs(selectedCurrencies, source);
        String yqlStatement = generateYQLCurrencyQuery(currencyPair);

        return api.getCurrencyPairs(yqlStatement)
                .map(new Func1<YahooDataContainerResponse,
                        YahooDataContainerResponse.YahooCurrencyRatesHolder>() {
                    @Override
                    public YahooDataContainerResponse.YahooCurrencyRatesHolder call(YahooDataContainerResponse response) {
                        return response.query.results;
                    }
                })
                .map(new Func1<YahooDataContainerResponse.YahooCurrencyRatesHolder, List<ConversionItem>>() {

                    @Override
                    public List<ConversionItem> call(YahooDataContainerResponse.YahooCurrencyRatesHolder conversion) {
                        List<ConversionItem> conversionItems
                                = new ArrayList<>(conversion.rate.size());

                        for (YahooCurrencyRateResponse rate : conversion.rate) {
                            conversionItems.add(ConversionItem
                                    .builder()
                                    .currencyId(0)
                                    .conversionComboId(rate.id)
                                    .conversionRate(new BigDecimal(rate.rate)
                                            .movePointRight(4)
                                            .intValue())
                                    .build());
                        }
                        return conversionItems;
                    }
                });
    }

    /**
     * Build the YQL statement to pass into the request
     *
     * @param currencyPairs list of currency pairs to pass in e.g. USDGBP, USDCHF...
     * @return the YQL statement to execute the request
     */
    private String generateYQLCurrencyQuery(List<String> currencyPairs) {

        //select * from yahoo.finance.xchange where pair in ("USDMXN", "USDCHF")

        StringBuilder sb = new StringBuilder("select * from yahoo.finance.xchange where pair in ");
        sb.append("(");

        int count = 1;

        for (String currencyPair : currencyPairs) {

            sb.append("\"").append(currencyPair).append("\"");

            //Check if it's not the last entry in the list
            if (count < currencyPairs.size()) {
                sb.append(",");
            }

            count++;
        }

        sb.append(")");

        return sb.toString();
    }


    /**
     * Create the reverse pairs for
     *
     * @param targetCurrencies list of currencies to get the converted values for
     * @param sourceCurrency   the currency at the source of the conversion
     * @return A string list of currency pairs
     */
    private List<String> createReverseFromPairs(List<String> targetCurrencies, String sourceCurrency) {

        List<String> currencyPairs = new ArrayList<>();

        for (String targetCurrency : targetCurrencies) {

            currencyPairs.add(targetCurrency + sourceCurrency);
            currencyPairs.add(sourceCurrency + targetCurrency);
        }

        return currencyPairs;
    }
}
