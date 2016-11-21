package com.jedikv.simpleconverter.data;

import com.jedikv.simpleconverter.api.yahoofinance.YahooApiService;
import com.jedikv.simpleconverter.api.yahoofinance.YahooCurrencyRateResponse;
import com.jedikv.simpleconverter.api.yahoofinance.YahooDataContainerResponse;
import com.jedikv.simpleconverter.api.yahoofinance.YqlStringHelper;
import com.jedikv.simpleconverter.domain.ConversionItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Kurian on 07/11/2016.
 */

public class NetworkDataSource {

    private final YahooApiService apiService;
    private final YqlStringHelper yqlStringHelper;

    public NetworkDataSource(YahooApiService apiService) {
        this.apiService = apiService;
        this.yqlStringHelper = new YqlStringHelper();
    }

    public Observable<List<ConversionItem>> getConversionItems(String sourceCurrency,
                                                               List<String> targetCurrencies) {
        final String yqlStatement = yqlStringHelper
                .generateYQLCurrencyQuery(targetCurrencies, sourceCurrency);

        return apiService.getCurrencyPairs(yqlStatement)
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

                            //Get the individual currencies
                            //[0] is the source and [1] is a target
                            String[] currencyId = rate.name.split("/");
                            conversionItems.add(ConversionItem
                                    .builder()
                                    .conversionRate(convertRateToInteger(rate.rate))
                                    .currencyCode(currencyId[1])
                                    .pairToCode(currencyId[0])
                                    .source(YahooApiService.SOURCE)
                                    .build());
                        }
                        return conversionItems;
                    }
                });
    }


    private int convertRateToInteger(String rateString) {
        return new BigDecimal(rateString)
                .movePointRight(4)
                .intValue();
    }
}
