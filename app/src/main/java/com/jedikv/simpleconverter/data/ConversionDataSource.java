package com.jedikv.simpleconverter.data;

import com.jedikv.simpleconverter.api.ConversionItemDTO;

import java.util.List;

import rx.Observable;

/**
 * Created by Kurian on 07/11/2016.
 */

public interface ConversionDataSource {

    Observable<List<ConversionItemDTO>> getConversionItems(String sourceCurrency,
                                                           List<String> targetCurrencies);


}
