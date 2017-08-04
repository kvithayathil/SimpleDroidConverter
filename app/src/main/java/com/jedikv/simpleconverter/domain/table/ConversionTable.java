/***************************************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2017 Kurian
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Last modified 26/07/17 14:20
 **************************************************************************************************/

package com.jedikv.simpleconverter.domain.table;

/**
 * Created by Kurian on 26/07/2017.
 */

public class ConversionTable implements BaseTable {

  public static final String TABLE = "conversion";
  public static final String SOURCE_CURRENCY_ID = "source_currency_id";
  public static final String TARGET_CURRENCY_ID = "target_currency_id";
  public static final String SOURCE_CURRENCY_CODE = "source_currency_code";
  public static final String TARGET_CURRENCY_CODE = "target_currency_code";
  public static final String RATE_AS_INT = "rate_as_int";
  public static final String SOURCE = "source";
  public static final String CREATED_TIMESTAMP = "created_at";
  public static final String UPDATED_TIMESTAMP = "last_updated";

  private ConversionTable() {
    throw new IllegalStateException("Do not instantiate");
  }

  public static String createTable() {
    return new StringBuilder()
        .append("CREATE TABLE IF NOT EXISTS " + TABLE + "(")
        .append(COLUMN_ID + " INTEGER PRIMARY KEY, ")
        .append(SOURCE_CURRENCY_ID + " INTEGER, ")
        .append(TARGET_CURRENCY_ID + " INTEGER, ")
        .append(SOURCE_CURRENCY_CODE + " TEXT, ")
        .append(SOURCE_CURRENCY_CODE + " TEXT, ")
        .append(TARGET_CURRENCY_CODE + " TEXT, ")
        .append(RATE_AS_INT + " INTEGER DEFAULT 0, ")
        .append(SOURCE + " TEXT, ")
        .append(CREATED_TIMESTAMP + " INTEGER")
        .append(UPDATED_TIMESTAMP + " INTEGER")
        .append(");")
        .toString();
  }

}
