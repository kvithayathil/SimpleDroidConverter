package com.jedikv.simpleconverter.domain.table;

/**
 * Created by Kurian on 25/07/2017.
 */

public class CurrencyTable implements BaseTable {

  public static final String TABLE = "currency";
  public static final String COLUMN_NUM_CODE = COLUMN_ID;
  public static final String COLUMN_SYMBOL = "symbol";
  public static final String COLUMN_NAME = "name";
  public static final String COLUMN_COUNTRY_NAME = "country_name";
  public static final String COLUMN_ISO_CODE = "iso_code";
  public static final String COLUMN_SHOW_AT_END = "show_at_end";
  public static final String COLUMN_DECIMAL_MARKER = "decimal_marker";
  public static final String COLUMN_THOUSANDS_SEPARATOR = "thousands_separator";

  private CurrencyTable() {
    throw new IllegalStateException("Do not instantiate");
  }

  public static String createTable() {
    return new StringBuilder()
        .append("CREATE TABLE IF NOT EXISTS " + TABLE + "(")
        .append(COLUMN_NUM_CODE + " INTEGER PRIMARY KEY, ")
        .append(COLUMN_ISO_CODE + " TEXT UNIQUE, ")
        .append(COLUMN_SYMBOL + " TEXT, ")
        .append(COLUMN_NAME + " TEXT, ")
        .append(COLUMN_COUNTRY_NAME + " TEXT, ")
        .append(COLUMN_DECIMAL_MARKER + " TEXT, ")
        .append(COLUMN_THOUSANDS_SEPARATOR + " TEXT, ")
        .append(COLUMN_SHOW_AT_END + " INTEGER DEFAULT 0")
        .append(");")
        .toString();
  }
}
