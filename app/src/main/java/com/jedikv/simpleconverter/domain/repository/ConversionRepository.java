package com.jedikv.simpleconverter.domain.repository;

import com.jedikv.simpleconverter.api.ConversionItemDTO;

import java.util.List;

import rx.Observable;

/**
 * Created by Kurian on 30/10/2016.
 */

public interface ConversionRepository {

    Observable<List<ConversionItemDTO>> getConversionItems(String source,
                                                           List<String> selectedCurrencies);
}
