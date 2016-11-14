package com.jedikv.simpleconverter.domain.repository;

import com.jedikv.simpleconverter.data.DataSourceProvider;
import com.jedikv.simpleconverter.domain.ConversionItem;

import java.util.List;

import rx.Observable;

/**
 * Created by Kurian on 07/11/2016.
 */

public class ConversionDataRepository implements ConversionRepository {

    private final DataSourceProvider dataSourceProvider;

    public ConversionDataRepository(DataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    @Override
    public Observable<List<ConversionItem>> getConversionItems(String source,
                                                               List<String> selectedCurrencies) {
        return null;
    }
}
