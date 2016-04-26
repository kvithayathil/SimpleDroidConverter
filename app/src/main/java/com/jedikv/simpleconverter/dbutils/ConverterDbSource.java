package com.jedikv.simpleconverter.dbutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jedikv.simpleconverter.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import converter_db.CurrencyEntity;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.meta.EntityModel;
import timber.log.Timber;

/**
 * Created by KV_87 on 25/04/2016.
 */
public class ConverterDbSource extends DatabaseSource {

    private final SharedPreferences sharedPreferences;
    private final Resources resources;
    private final AssetManager assetManager;

    public ConverterDbSource(Context context, EntityModel model, int version) {
        super(context, model, version);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.resources = context.getResources();
        this.assetManager = context.getAssets();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        List<CurrencyEntity> entityList = loadJsonFromAsset();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }



    private void setDefaultCurrency(String currencyCode) {
        sharedPreferences.edit().putString(Constants.PREFS_CURRENTLY_SELECTED_CURRENCY, currencyCode).apply();
    }

    private List<CurrencyEntity> loadJsonFromAsset() {

        List<CurrencyEntity> currencyEntities;

        try {

            InputStream is = this.assetManager.open("currency_output.json");
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
