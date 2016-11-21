package com.jedikv.simpleconverter.domain.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.preference.PreferenceManager;

import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.utils.AndroidUtils;
import com.jedikv.simpleconverter.utils.Constants;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import okio.BufferedSource;
import okio.Okio;
import timber.log.Timber;

/**
 * Created by Kurian on 01/11/2016.
 */

public class AppDbHelper extends SQLiteOpenHelper {

    private final Context context;

    public AppDbHelper(Context context, String name, int version) {
        super(context, name, null, version);
        this.context = context.getApplicationContext();
        Timber.tag(AppDbHelper.class.getCanonicalName());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(CurrencyTable.createTable());
            db.execSQL(ConversionPairTable.createTable());
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + ConversionPairTable.TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CurrencyTable.TABLE);
            onCreate(db);
            preLoadCurrenciesInDatabase(context, db);
        }
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Enable constraints and cascades
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            if (!db.isReadOnly()) {
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //Enable constraints and cascades
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            db.setForeignKeyConstraintsEnabled(true);
        }
    }

    private void preLoadCurrenciesInDatabase(Context context, SQLiteDatabase db) {
        List<CurrencyPreLoadContainer> currencies = loadJsonFromAsset(context);
        String sql = "INSERT INTO CURRENCY_ENTITY VALUES (?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (CurrencyPreLoadContainer currency : currencies) {
            if (AndroidUtils.getDrawableResIdByCurrencyCode(context, currency.isoCode) > 0) {
                statement.clearBindings();
                statement.bindLong(1, currency.numCode);
                statement.bindString(2, currency.isoCode);
                statement.bindString(3, currency.symbol);
                statement.bindString(4, currency.name);
                statement.bindString(5, currency.locationName);
                statement.bindString(6, currency.decimalMark);
                statement.bindString(7, currency.separator);
                //Setting the boolean values
                if (currency.symbolAtStart) {
                    statement.bindLong(8, 1);
                } else {
                    statement.bindLong(8, 0);
                }
                statement.execute();
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        //Set the default currency
        setDefaultCurrency(context);
    }

    private void setDefaultCurrency(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        sharedPreferences.edit()
                .putString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY,
                        context.getString(R.string.default_source_currency))
                .apply();
    }

    private List<CurrencyPreLoadContainer> loadJsonFromAsset(Context context) {
        try {

            InputStream is = context.getAssets().open("currency_output.json");
            Moshi moshi = new Moshi.Builder()
                    .build();

            Type currencyList = Types.newParameterizedType(List.class, CurrencyPreLoadContainer.class);
            JsonAdapter<List<CurrencyPreLoadContainer>> adapter = moshi.adapter(currencyList);
            BufferedSource jsonSource = Okio.buffer(Okio.source(is));
            return adapter.fromJson(jsonSource);

        } catch (IOException e) {
            Timber.e(e, e.getMessage());
            return null;
        }
    }


    private class CurrencyPreLoadContainer {
        @Json(name = "numericCode")
        public final long numCode;
        @Json(name = "symbol")
        public final String symbol;
        @Json(name = "code")
        public final String isoCode;
        @Json(name = "name")
        public final String name;
        @Json(name = "countryName")
        public final String locationName;
        @Json(name = "showAtEnd")
        public final boolean symbolAtStart;
        @Json(name = "decimalmark")
        public final String decimalMark;
        @Json(name = "thousandsSeparator")
        public final String separator;

        public CurrencyPreLoadContainer(long numCode, String symbol, String isoCode, String name,
                                        String locationName, boolean symbolAtStart,
                                        String decimalMark, String separator) {
            this.numCode = numCode;
            this.symbol = symbol;
            this.isoCode = isoCode;
            this.name = name;
            this.locationName = locationName;
            this.symbolAtStart = symbolAtStart;
            this.decimalMark = decimalMark;
            this.separator = separator;
        }
    }
}
