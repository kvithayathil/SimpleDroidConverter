package com.jedikv.simpleconverter.api.yahoofinance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kurian on 30/10/2016.
 */

public class MockYahooCurrencyApiServiceImpl implements YahooCurrencyApi {

    private final YahooDataContainerResponse containerResponse;
    private static Observable dummyYahooResult = null;

    public MockYahooCurrencyApiServiceImpl() {

        //Set up the dummy currency result to send back to the caller
        List<YahooCurrencyRateResponse> rateList = new ArrayList<>(2);
        rateList.add(new YahooCurrencyRateResponse("USDMXN", "USD/MXN", "18.9500", "10/29/2016", "10:53pm", "19.0000", "18.9500"));
        rateList.add(new YahooCurrencyRateResponse("USDCHF", "USD/CHF", "0.9872", "10/28/2016", "9:00pm", "0.9885", "0.9872"));

        YahooDataContainerResponse.YahooCurrencyRatesHolder resultContainer = new YahooDataContainerResponse.YahooCurrencyRatesHolder(rateList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date targetDate;

        try {
             targetDate = sdf.parse("2016-10-30T16:08:09Z");
        } catch (ParseException e) {
            e.printStackTrace();
            targetDate = new Date();
        }

        YahooDataContainerResponse.YahooCurrencyQueryResult result = new YahooDataContainerResponse.YahooCurrencyQueryResult(2, targetDate, "en-US", resultContainer);
        containerResponse = new YahooDataContainerResponse(result);
    }

    public static void setDummyCallResult(Observable result) {
        dummyYahooResult = result;
    }

    @Override
    public Observable<YahooDataContainerResponse> getCurrencyPairs(@Query("q") final String query) {
        if(dummyYahooResult != null) {
            return dummyYahooResult;
        }
        return Observable.just(containerResponse);
    }
}
