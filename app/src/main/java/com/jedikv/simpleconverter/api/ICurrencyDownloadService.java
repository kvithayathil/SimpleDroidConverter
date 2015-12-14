package com.jedikv.simpleconverter.api;

import java.util.List;

import rx.Subscription;

/**
 * Created by Kurian on 13/12/2015.
 */
public interface ICurrencyDownloadService {

    Subscription executeRequest(List<String> targetCurrencies, String sourceCurrency, final OnRequestFinished listener);

}
