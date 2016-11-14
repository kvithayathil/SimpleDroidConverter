package com.jedikv.simpleconverter.domain.repository;

import com.jedikv.simpleconverter.domain.ConversionItem;

import java.util.List;

import rx.Observable;

/**
 * Created by Kurian on 30/10/2016.
 */

public interface ConversionRepository {

    Observable<List<ConversionItem>> getConversionItems(String source,
                                                        List<String> selectedCurrencies);
}
