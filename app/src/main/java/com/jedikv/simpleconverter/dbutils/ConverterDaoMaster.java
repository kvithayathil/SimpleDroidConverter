package com.jedikv.simpleconverter.dbutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.R;
import com.jedikv.simpleconverter.utils.AndroidUtils;
import com.jedikv.simpleconverter.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import converter_db.CurrencyEntity;
import converter_db.DaoMaster;
import timber.log.Timber;

/**
 * Created by Kurian on 04/05/2015.
 */
public class ConverterDaoMaster extends DaoMaster.OpenHelper {

    private Context mContext;

    public ConverterDaoMaster(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);

        mContext = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        /*
        String constraint = false? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CURRENCY_ENTITY' (" + //
                "'SYMBOL' TEXT," + // 0: symbol
                "'NAME' TEXT," + // 1: name
                "'COUNTRY_NAME' TEXT," + // 2: countryName
                "'NUMERIC_CODE' INTEGER PRIMARY KEY ," + // 3: numericCode
                "'CODE' TEXT NOT NULL UNIQUE );"); // 4: code

        */
        List<CurrencyEntity> entityList = loadJsonFromAsset(mContext);

        String sql = "INSERT INTO CURRENCY_ENTITY VALUES (?,?,?,?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (CurrencyEntity entity : entityList) {

            //Currently only add currencies with an associated flag
            if(AndroidUtils.getDrawableResIdByCurrencyCode(mContext, entity.getCode()) > 0) {

                statement.clearBindings();
                statement.bindString(1, entity.getSymbol());
                statement.bindString(2, entity.getName());
                statement.bindString(3, entity.getCountryName());
                statement.bindLong(4, entity.getNumericCode());
                statement.bindString(5, entity.getCode());
                statement.execute();
            }
        }

        /*

    private String symbol;
    private String name;
    private String countryName;
    private Long numericCode;
        private String code;
         */

        db.setTransactionSuccessful();
        db.endTransaction();

        //Set the default currency
        setDefaultCurrency(mContext.getString(R.string.default_source_currency));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void setDefaultCurrency(String currencyCode) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().putString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, currencyCode).apply();
    }

    private List<CurrencyEntity> loadJsonFromAsset(Context context) {

        List<CurrencyEntity> currencyEntities;

        try {

            InputStream is = context.getAssets().open("currency_output.json");
            Gson gson = new GsonBuilder().create();
            Reader reader = new InputStreamReader(is);
            currencyEntities = gson.fromJson(reader, new TypeToken<List<CurrencyEntity>>(){}.getType());

        } catch (IOException e) {

            Timber.e(e, e.getMessage());
            return null;
        }

        return currencyEntities;
    }
}
