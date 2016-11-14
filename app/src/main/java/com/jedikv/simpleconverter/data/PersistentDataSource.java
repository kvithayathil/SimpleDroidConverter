package com.jedikv.simpleconverter.data;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.jedikv.simpleconverter.domain.database.ConversionPairTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.List;

import rx.Observable;

/**
 * Created by Kurian on 07/11/2016.
 */

public class PersistentDataSource {

    private final StorIOSQLite storIOSQLite;

    public PersistentDataSource(StorIOSQLite storIOSQLite) {
        this.storIOSQLite = storIOSQLite;
    }

    public Observable<List<ConversionItem>> getConversionItems(String sourceCurrency,
                                                               List<String> targetCurrencies) {

        return storIOSQLite
                .get()
                .listOfObjects(ConversionItem.class)
                .withQuery(ConversionPairTable
                        .querySelectedConversionItems(sourceCurrency, targetCurrencies))
                .prepare()
                .asRxObservable();
    }
}
