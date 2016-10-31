package com.jedikv.simpleconverter.domain.repository;

import com.jedikv.simpleconverter.api.ConversionItemDTO;
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
    private final YqlStringHelper yqlStringHelper;

    public YahooConversionRepository(YahooApiService api) {
        this.yqlStringHelper = new YqlStringHelper();
        this.api = api;
    }

    @Override
    public Observable<List<ConversionItemDTO>> getConversionItems(String source,
                                                                  List<String> selectedCurrencies) {

        String yqlStatement = yqlStringHelper.generateYQLCurrencyQuery(selectedCurrencies, source);

        return api.getCurrencyPairs(yqlStatement)
                .map(new Func1<YahooDataContainerResponse,
                        YahooDataContainerResponse.YahooCurrencyRatesHolder>() {
                    @Override
                    public YahooDataContainerResponse.YahooCurrencyRatesHolder call(YahooDataContainerResponse response) {
                        return response.query.results;
                    }
                })
                .map(new Func1<YahooDataContainerResponse.YahooCurrencyRatesHolder, List<ConversionItemDTO>>() {

                    @Override
                    public List<ConversionItemDTO> call(YahooDataContainerResponse.YahooCurrencyRatesHolder conversion) {
                        List<ConversionItemDTO> conversionItems
                                = new ArrayList<>(conversion.rate.size());

                        for (YahooCurrencyRateResponse rate : conversion.rate) {

                            //Get the individual currencies
                            //[0] is the source and [1] is a target
                            String[] currencyId = rate.name.split("/");
                            conversionItems.add(ConversionItemDTO
                                    .builder()
                                    .currencyId(rate.name)
                                    .conversionRateAsInteger(convertRateToInteger(rate.rate))
                                    .currencyId(currencyId[1])
                                    .pairTo(currencyId[0])
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
