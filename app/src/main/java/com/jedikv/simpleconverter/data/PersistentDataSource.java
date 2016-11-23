package com.jedikv.simpleconverter.data;

import android.content.ContentValues;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.jedikv.simpleconverter.domain.CurrencyItem;
import com.jedikv.simpleconverter.domain.database.ConversionPairTable;
import com.jedikv.simpleconverter.domain.database.ConversionPositionsUpdatePutResolver;
import com.jedikv.simpleconverter.domain.database.CurrencyTable;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
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

    public Observable<List<CurrencyItem>> getFilteredCurrencies(List<String> excludedIsoCodes) {

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
                .asRxObservable();
    }


    public Observable<ConversionItem> addConversionItem(final String sourceCurrency,
                                                        final String target,
                                                        final int targetPos) {

        return storIOSQLite
                .get()
                .object(ConversionItem.class)
                .withQuery(ConversionPairTable.queryConversionItem(sourceCurrency, target))
                .prepare()
                .asRxObservable()
                .map(new Func1<ConversionItem, ConversionItem>() {
                    @Override
                    public ConversionItem call(ConversionItem item) {
                        if (item == null) {
                            //Create placeholder entry if it doesn't exist already
                            return item.toBuilder()
                                    .source(sourceCurrency)
                                    .pairToCode(target)
                                    .conversionRate(1)
                                    .position(targetPos)
                                    .build();
                        } else {
                            return item.updatePosition(targetPos);
                        }
                    }
                })
                .doOnNext(new Action1<ConversionItem>() {
                    @Override
                    public void call(ConversionItem item) {
                        //Update the new position
                        storIOSQLite.put()
                                .object(item.updatePosition(targetPos))
                                .prepare()
                                .executeAsBlocking();
                    }
                });
    }


    public Observable<Void> removeConversionItemFromList(final String sourceCurrencyIso,
                                                         final Integer oldPos) {

        return updateConversionItemPositions(sourceCurrencyIso, oldPos, -1);
    }

    public Observable<Void> updateConversionItemPositions(final String sourceCurrencyIso,
                                                          final int oldPos,
                                                          final int newPos) {

        final ContentValues cv = new ContentValues();
        cv.put(ConversionPairTable.COLUMN_SOURCE_CURRENCY_CODE, sourceCurrencyIso);
        if (newPos >= 0) {
            cv.put(ConversionPairTable.COLUMN_LIST_POSITION, newPos);
        }

        return storIOSQLite
                .put()
                .contentValues(cv)
                .withPutResolver(ConversionPositionsUpdatePutResolver
                        .create(sourceCurrencyIso, oldPos))
                .prepare()
                .asRxObservable()
                .ignoreElements().cast(Void.TYPE);
    }
}
