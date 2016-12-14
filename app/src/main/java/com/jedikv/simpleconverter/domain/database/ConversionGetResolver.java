package com.jedikv.simpleconverter.domain.database;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_CURRENCY_CODES_COMBO;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_ID;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_LAST_UPDATED;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_LIST_POSITION;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_RATE_AS_INTEGER;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_SOURCE_CURRENCY_CODE;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_TARGET_CURRENCY_CODE;
import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.COLUMN_UPDATE_SOURCE;

/**
 * Created by Kurian on 14/11/2016.
 */

public class ConversionGetResolver extends DefaultGetResolver<ConversionItem> {

    @NonNull
    @Override
    public ConversionItem mapFromCursor(@NonNull Cursor cursor) {
        return ConversionItem.builder()
                .id(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)))
                .conversionComboId(cursor
                        .getString(cursor.getColumnIndex(COLUMN_CURRENCY_CODES_COMBO)))
                .conversionRate(cursor.getInt(cursor.getColumnIndex(COLUMN_RATE_AS_INTEGER)))
                .source(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE_CURRENCY_CODE)))
                .pairToCode(cursor.getString(cursor.getColumnIndex(COLUMN_TARGET_CURRENCY_CODE)))
                .lastUpdatedDate(cursor.getLong(cursor.getColumnIndex(COLUMN_LAST_UPDATED)))
                .position(cursor.getInt(cursor.getColumnIndex(COLUMN_LIST_POSITION)))
                .source(cursor.getString(cursor.getColumnIndex(COLUMN_UPDATE_SOURCE)))
                .build();
    }
}
