package com.jedikv.simpleconverter.domain.database;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.ui.model.ConversionItemModel;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import java.math.BigDecimal;

/**
 * Created by Kurian on 02/12/2016.
 */

public class ConversionModelGetResolver extends DefaultGetResolver<ConversionItemModel> {

    private static final int DECIMAL_POINT = 4;

    @NonNull
    @Override
    public ConversionItemModel mapFromCursor(@NonNull Cursor cursor) {

        final BigDecimal rate = new BigDecimal(cursor.getInt(cursor
                .getColumnIndex(ConversionPairTable.COLUMN_RATE_AS_INTEGER)))
                .movePointLeft(DECIMAL_POINT);

        return ConversionItemModel.builder()
                .conversionId(cursor.getLong(cursor.getColumnIndex(ConversionPairTable.COLUMN_ID)))
                .rate(rate)
                .source(cursor.getString(cursor
                        .getColumnIndex(ConversionPairTable.COLUMN_SOURCE_CURRENCY_CODE)))
                .isoCode(cursor.getString(cursor
                        .getColumnIndex(ConversionPairTable.COLUMN_TARGET_CURRENCY_CODE)))
                .timestamp(cursor.getLong(cursor
                        .getColumnIndex(ConversionPairTable.COLUMN_LAST_UPDATED)))
                .source(cursor.getString(cursor
                        .getColumnIndex(ConversionPairTable.COLUMN_UPDATE_SOURCE)))


                .decimalMarker(cursor.getString(cursor
                        .getColumnIndex(CurrencyTable.COLUMN_DECIMAL_MARK)))
                .symbol(cursor.getString(cursor
                        .getColumnIndex(CurrencyTable.COLUMN_SYMBOL)))
                .separator(cursor.getString(cursor
                        .getColumnIndex(CurrencyTable.COLUMN_THOUSANDS_SEPARATOR)))
                .symbolAtStart(cursor.getInt(cursor
                        .getColumnIndex(CurrencyTable.COLUMN_SYMBOL_AT_START)) == 1)
                .location(cursor.getString(cursor
                        .getColumnIndex(CurrencyTable.COLUMN_LOCATION)))
                .build();
    }
}
