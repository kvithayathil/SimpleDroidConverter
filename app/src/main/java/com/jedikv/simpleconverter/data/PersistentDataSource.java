package com.jedikv.simpleconverter.data;

import android.content.ContentValues;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.jedikv.simpleconverter.domain.CurrencyItem;
import com.jedikv.simpleconverter.domain.database.ConversionPairTable;
import com.jedikv.simpleconverter.domain.database.ConversionUpdatePutResolver;
import com.jedikv.simpleconverter.domain.database.CurrencyTable;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

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

    public Observable<List<CurrencyModel>> getFilteredCurrencies(List<String> excludedIsoCodes) {

        final Query query;

        if (excludedIsoCodes == null || excludedIsoCodes.isEmpty()) {
            query = CurrencyTable.queryForAll();
        } else {
            query = CurrencyTable.queryNonSelectedCurrencies(excludedIsoCodes);
        }

        Timber.d("Currency query: %1$s", query.toString());

        return storIOSQLite
                .get()
                .listOfObjects(CurrencyItem.class)
                .withQuery(query)
                .prepare()
                .asRxObservable()
                .map(new Func1<List<CurrencyItem>, List<CurrencyModel>>() {
                    @Override
                    public List<CurrencyModel> call(List<CurrencyItem> currencyItems) {

                        List<CurrencyModel> currencyModels = new ArrayList<>();

                        for (CurrencyItem item : currencyItems) {
                            currencyModels.add(CurrencyModel.builder()
                                    .isoCode(item.isoCode())
                                    .location(item.location())
                                    .name(item.name())
                                    .numericCode(item.numCode())
                                    .symbol(item.symbol())
                                    .build());
                        }

                        return currencyModels;
                    }
                });
    }


    public Observable<ConversionItem> addConversionItem(String sourceCurrency, String target) {
        return storIOSQLite
                .get()
                .object(ConversionItem.class)
                .withQuery(ConversionPairTable.queryConversionItem(sourceCurrency, target))
                .prepare()
                .asRxObservable()
                .map(new Func1<ConversionItem, ConversionItem>() {
                    @Override
                    public ConversionItem call(ConversionItem item) {
                        if (item != null) {
                            return item;
                        } else
                            return null;
                    }
                });
    }


    public void removeConversionItem(final long id) {

        final ContentValues cv = new ContentValues(1);
        cv.put(ConversionPairTable.COLUMN_LIST_POSITION, -1);

        storIOSQLite
                .put()
                .contentValues(cv)
                .withPutResolver(ConversionUpdatePutResolver.create(id))
                .prepare()
                .executeAsBlocking();
    }
}
