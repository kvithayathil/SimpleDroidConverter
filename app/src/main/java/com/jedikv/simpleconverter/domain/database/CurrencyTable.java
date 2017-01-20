package com.jedikv.simpleconverter.domain.database;

import com.jedikv.simpleconverter.utils.SqlUtils;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

/**
 * Created by Kurian on 01/11/2016.
 */

public class CurrencyTable implements Table {
    public static final String TABLE = "currency";

    public static final String COLUMN_ID = _ID;
    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_ISO_CODE = "iso_code";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "country_name";
    public static final String COLUMN_DECIMAL_MARK = "decimal_mark";
    public static final String COLUMN_THOUSANDS_SEPARATOR = "thousand_separator";
    public static final String COLUMN_SYMBOL_AT_START = "symbol_at_start";


    private CurrencyTable() {
        throw new IllegalStateException("No instances please");
    }


    public static String createTable() {
        return new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS " + TABLE + "(")
                .append(COLUMN_ID + " INTEGER PRIMARY KEY, ")
                .append(COLUMN_ISO_CODE + " TEXT UNIQUE, ")
                .append(COLUMN_SYMBOL + " TEXT, ")
                .append(COLUMN_NAME + " TEXT, ")
                .append(COLUMN_LOCATION + " TEXT, ")
                .append(COLUMN_DECIMAL_MARK + " TEXT, ")
                .append(COLUMN_THOUSANDS_SEPARATOR + " TEXT, ")
                .append(COLUMN_SYMBOL_AT_START + " TEXT")
                .append(");")
                .toString();
    }

    public static Query queryForAll() {
        return Query.builder()
                .table(TABLE)
                .orderBy(COLUMN_ISO_CODE + " ASC")
                .build();
    }

    public static Query queryByIsoCode(String isoCode) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_ISO_CODE + " = ?")
                .whereArgs(isoCode)
                .orderBy(COLUMN_ISO_CODE + " ASC")
                .build();
    }

    public static Query queryByNumericCode(long numericCode) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_ID + " = ?")
                .whereArgs(numericCode)
                .orderBy(COLUMN_ISO_CODE + " ASC")
                .build();
    }


    public static Query queryNonSelectedCurrencies(List<String> isoCodes) {
        return Query.builder()
                .table(TABLE)
                .where(COLUMN_ISO_CODE + " NOT IN ("
                        + SqlUtils.generateListPlaceholder(isoCodes.size())
                        + ")")
                .whereArgs(isoCodes)
                .orderBy(COLUMN_ISO_CODE + " ASC")
                .build();
    }
}
