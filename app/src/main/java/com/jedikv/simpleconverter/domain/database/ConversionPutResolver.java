package com.jedikv.simpleconverter.domain.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_CURRENCY_CODES_COMBO;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_ID;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_LAST_UPDATED;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_LIST_POSITION;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_RATE_AS_INTEGER;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_SOURCE_CURRENCY_CODE;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_TARGET_CURRENCY_CODE;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_UPDATE_SOURCE;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.TABLE;


/**
 * Created by Kurian on 14/11/2016.
 */

public class ConversionPutResolver extends PutResolver<ConversionItem> {

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite,
                                @NonNull ConversionItem object) {

        final StorIOSQLite.LowLevel lowLevel = storIOSQLite.lowLevel();
        final UpdateQuery updateQuery = createUpdateQuery(object);

        lowLevel.beginTransaction();

        try {
            final PutResult putResult;
            final Cursor cursor = lowLevel
                    .query(findExistingEntry(object.source(), object.pairToCode()));

            if (cursor.getCount() == 0) {

                Cursor sourceCurrencyCursor = lowLevel
                        .query(getCurrencyCode(object.currencyCode()));
                Cursor targetCurrencyCursor = lowLevel.query(getCurrencyCode(object.pairToCode()));
                ContentValues cv = generateInsertContentValues(object);

                String codeComboString = String.format("%s%s",
                        sourceCurrencyCursor.getLong(0),
                        targetCurrencyCursor.getLong(0));

                cv.put(COLUMN_ID, Long.parseLong(codeComboString));

                final long id = lowLevel.insert(InsertQuery.builder()
                        .table(TABLE)
                        .build(), cv);
                putResult = PutResult.newInsertResult(id, TABLE);

            } else {
                final int count = lowLevel.update(updateQuery, generateUpdateContentValues(object));
                putResult = PutResult.newUpdateResult(count, TABLE);
            }

            lowLevel.setTransactionSuccessful();
            return putResult;
        } finally {
            lowLevel.endTransaction();
        }
    }


    private Query getCurrencyCode(String isoCode) {
        return Query
                .builder()
                .table(CurrencyTable.TABLE)
                .columns(CurrencyTable.COLUMN_ID)
                .where(CurrencyTable.COLUMN_ISO_CODE + " = ?")
                .whereArgs(isoCode)
                .build();
    }


    private ContentValues generateInsertContentValues(ConversionItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CURRENCY_CODES_COMBO, item.conversionComboId());
        cv.put(COLUMN_RATE_AS_INTEGER, item.conversionRate());
        cv.put(COLUMN_SOURCE_CURRENCY_CODE, item.currencyCode());
        cv.put(COLUMN_TARGET_CURRENCY_CODE, item.pairToCode());
        cv.put(COLUMN_LAST_UPDATED, System.currentTimeMillis());
        cv.put(COLUMN_LIST_POSITION, item.position());
        cv.put(COLUMN_UPDATE_SOURCE, item.source());
        return cv;
    }


    private ContentValues generateUpdateContentValues(ConversionItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RATE_AS_INTEGER, item.conversionRate());
        cv.put(COLUMN_LAST_UPDATED, System.currentTimeMillis());
        cv.put(COLUMN_UPDATE_SOURCE, item.source());
        return cv;
    }

    private Query findExistingEntry(String sourceIsoCode, String targetIsoCode) {
        return Query
                .builder()
                .table(TABLE)
                .columns(COLUMN_ID)
                .where(COLUMN_SOURCE_CURRENCY_CODE + " = ?" +
                        " AND " + COLUMN_TARGET_CURRENCY_CODE + " = ?")
                .whereArgs(sourceIsoCode, targetIsoCode)
                .build();
    }


    private UpdateQuery createUpdateQuery(ConversionItem item) {

        return UpdateQuery
                .builder()
                .table(TABLE)
                .where(COLUMN_TARGET_CURRENCY_CODE + " = ?"
                        + " AND " + COLUMN_SOURCE_CURRENCY_CODE + " = ?")
                .whereArgs(item.pairToCode(), item.source())
                .build();

    }
}
