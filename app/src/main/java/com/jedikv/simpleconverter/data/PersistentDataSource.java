package com.jedikv.simpleconverter.data;

import android.content.ContentValues;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.jedikv.simpleconverter.domain.CurrencyItem;
import com.jedikv.simpleconverter.domain.database.ConversionModelGetResolver;
import com.jedikv.simpleconverter.domain.database.ConversionPairTable;
import com.jedikv.simpleconverter.domain.database.ConversionPositionsUpdatePutResolver;
import com.jedikv.simpleconverter.domain.database.CurrencyTable;
import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.jedikv.simpleconverter.ui.model.CurrencyModel;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
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
    private final ConversionModelGetResolver conversionModelGetResolver;

    public PersistentDataSource(StorIOSQLite storIOSQLite) {

        this.storIOSQLite = storIOSQLite;
        this.conversionModelGetResolver = new ConversionModelGetResolver();
    }

    /**
     * Save/Update conversion items to the database
     * @param items list of conversion items to save/update
     * @return The results of the save operation
     */
    public Observable<PutResults<ConversionItem>> saveConversionItems(List<ConversionItem> items) {

        return storIOSQLite
                .put()
                .objects(items)
                .prepare()
                .asRxObservable();
    }

    /**
     * Retrieve the conversion items from the database
     * @param sourceCurrency the source to filter by
     * @return List of conversion items that match the filter
     */
    public Observable<List<ConversionItemModel>> getConversionItems(String sourceCurrency) {

        return storIOSQLite
                .get()
                .listOfObjects(ConversionItemModel.class)
                .withQuery(ConversionPairTable.queryBySourceCurrency(sourceCurrency))
                .withGetResolver(new ConversionModelGetResolver())
                .prepare()
                .asRxObservable();
    }

    /**
     * Retrieve a list of currency items based upon a exclusion filter
     * @param excludedIsoCodes exclusion filter to apply
     * @return list of currencies
     */
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

    public Observable<CurrencyItem> getCurrency(String currencyIso) {
        return storIOSQLite
                .get()
                .object(CurrencyItem.class)
                .withQuery(CurrencyTable.queryByIsoCode(currencyIso))
                .prepare()
                .asRxObservable();
    }


    /**
     * Save a new, single conversion item to local storage
     * @param sourceCurrency source that the conversion is compared to
     * @param target the target currency
     * @param targetPos position in the list to save (-1) to remove it from the selected list
     * @return The resulting conversion item after it's been saved to the database
     */
    public Observable<ConversionItemModel> addConversionItem(final String sourceCurrency,
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
                })
                .flatMap(new Func1<ConversionItem, Observable<ConversionItemModel>>() {
                    @Override
                    public Observable<ConversionItemModel> call(ConversionItem item) {
                        //Finally return the conversion item
                        return storIOSQLite
                                .get()
                                .object(ConversionItemModel.class)
                                .withQuery(ConversionPairTable.queryBySourceCurrency(target))
                                .withGetResolver(conversionModelGetResolver)
                                .prepare()
                                .asRxObservable();
                    }
                });
    }

    /**
     * Removes the conversion item from the list to be displayed to the the client
     * @param sourceCurrencyIso source currency filter
     * @param oldPos position of the item to be removed
     * @return A one way operation which returns a void observable
     */
    public Observable<Void> removeConversionItemFromList(final String sourceCurrencyIso,
                                                         final Integer oldPos) {

        return updateConversionItemPositions(sourceCurrencyIso, oldPos, -1);
    }

    /**
     * Updates the position of the conversion item in the list
     * @param sourceCurrencyIso source currency filter
     * @param oldPos position of the item
     * @param newPos new position to update to
     * @return A one way operation which returns a void observable
     */
    public Observable<Void> updateConversionItemPositions(final String sourceCurrencyIso,
                                                          final int oldPos,
                                                          final int newPos) {

        final ContentValues cv = new ContentValues();
        cv.put(ConversionPairTable.COLUMN_SOURCE_CURRENCY_CODE, sourceCurrencyIso);
        if (newPos >= 0) {
            cv.put(ConversionPairTable.COLUMN_LIST_POSITION, newPos);
        } else {
            cv.putNull(ConversionPairTable.COLUMN_LIST_POSITION);
        }

        return storIOSQLite
                .put()
                .contentValues(cv)
                .withPutResolver(ConversionPositionsUpdatePutResolver
                        .create(sourceCurrencyIso, oldPos))
                .prepare()
                .asRxObservable()
                .ignoreElements()
                .cast(Void.TYPE);
    }
}
