package com.jedikv.simpleconverter.domain.database;

import com.jedikv.simpleconverter.utils.SqlUtils;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

/**
 * Created by Kurian on 01/11/2016.
 */

public class ConversionPairTable implements Table {
    public static final String TABLE = "conversion_pair";

    public static final String COLUMN_ID = _ID;
    public static final String COLUMN_CURRENCY_CODES_COMBO = "currency_code_combo";
    public static final String COLUMN_RATE_AS_INTEGER = "rate_as_integer";
    public static final String COLUMN_SOURCE_CURRENCY_CODE = "source_currency_code";
    public static final String COLUMN_TARGET_CURRENCY_CODE = "target_currency_code";
    public static final String COLUMN_LAST_UPDATED = "last_updated";
    public static final String COLUMN_LIST_POSITION = "list_position";
    public static final String COLUMN_UPDATE_SOURCE = "update_source";

    private ConversionPairTable() {
        throw new IllegalStateException("No instances please");
    }


    public static final String createTable() {
        return new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS " + TABLE + "(")
                .append(COLUMN_ID + " INTEGER PRIMARY KEY, ")
                .append(COLUMN_CURRENCY_CODES_COMBO + " TEXT UNIQUE, ")
                .append(COLUMN_RATE_AS_INTEGER + " INTEGER, ")
                .append(COLUMN_SOURCE_CURRENCY_CODE + " TEXT, ")
                .append(COLUMN_TARGET_CURRENCY_CODE + " TEXT, ")
                .append(COLUMN_LAST_UPDATED + " TIMESTAMP, ")
                .append(COLUMN_LIST_POSITION + " INTEGER, ")
                .append(COLUMN_UPDATE_SOURCE + " TEXT")
                .append(");")
                .toString();
    }

    public static final Query queryBy(String currencyCode) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_TARGET_CURRENCY_CODE + " = ? AND " + COLUMN_LIST_POSITION + " >= 0")
                .whereArgs(currencyCode)
                .orderBy(COLUMN_LIST_POSITION + " ASC")
                .build();
    }

    public static final Query querySelectedConversionItems(String sourceIsoCode,
                                                         List<String> isoCodes) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_SOURCE_CURRENCY_CODE
                        + " = ?"
                        + " AND "
                        + COLUMN_TARGET_CURRENCY_CODE + " IN ("
                        + SqlUtils.generateListPlaceholder(isoCodes.size())
                        + ")")
                .whereArgs(sourceIsoCode, isoCodes)
                .orderBy(COLUMN_LIST_POSITION + " ASC")
                .build();
    }
}
