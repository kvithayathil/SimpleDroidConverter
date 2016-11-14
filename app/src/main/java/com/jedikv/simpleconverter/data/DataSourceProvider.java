package com.jedikv.simpleconverter.data;

import com.jedikv.simpleconverter.domain.ConversionItem;

import rx.Observable;

/**
 * Created by Kurian on 07/11/2016.
 */

public class DataSourceProvider {

    private final PersistentDataSource localDataSource;
    private final NetworkDataSource networkDataSource;

    public DataSourceProvider(PersistentDataSource persistentDataSource,
                              NetworkDataSource networkDataSource) {

        this.localDataSource = persistentDataSource;
        this.networkDataSource = networkDataSource;
    }

    public Observable<ConversionItem> getConversionItem
}
