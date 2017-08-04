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
 * Last modified 25/07/17 20:34
 **************************************************************************************************/

package com.jedikv.simpleconverter.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.jedikv.simpleconverter.domain.table.ConversionTable;
import com.jedikv.simpleconverter.domain.table.CurrencyTable;
import timber.log.Timber;

/**
 * Created by Kurian on 25/07/2017.
 */

public class DbOpenHelper extends SQLiteOpenHelper {

  private static final String DB_NAME = "converter_db";
  private static final int DB_CURRENT_VERSION = 1;

  public DbOpenHelper(Context context) {
    super(context, DB_NAME, null, DB_CURRENT_VERSION);
    Timber.tag(DbOpenHelper.class.getCanonicalName());
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CurrencyTable.createTable());
    db.execSQL(ConversionTable.createTable());
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    if(oldVersion != newVersion) {
      db.execSQL(String.format("DROP TABLE IF EXISTS %s", CurrencyTable.TABLE));
      db.execSQL(String.format("DROP TABLE IF EXISTS %s", ConversionTable.TABLE));
      onCreate(db);
    }
  }
}
