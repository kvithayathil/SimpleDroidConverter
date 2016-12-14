package com.jedikv.simpleconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.dbutils.ConversionItemDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;
import com.jedikv.simpleconverter.domain.database.AppDbHelper;

import java.io.File;

import converter_db.DaoSession;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class AppModule {

    private final App application;

    public AppModule(App app) {
        this.application = app;
    }

    @Provides
    @NonNull
    @ConversionAppScope
    public App providesApplication() {
        return this.application;
    }


    @Provides
    @NonNull
    @ConversionAppScope
    public Context providesContext() {
        return this.application;
    }

    @Provides
    @NonNull
    @ConversionAppScope
    SharedPreferences provideSharedPrefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @NonNull
    @ConversionAppScope
    Resources providesAndroidResources(@NonNull Context context) {
        return context.getResources();
    }

    @Provides
    @NonNull
    @ConversionAppScope
    public File providesLocalCacheDir() {
        return application.getCacheDir();
    }

    @Provides
    @NonNull
    @ConversionAppScope
    public AppDbHelper providesDbHelper(@NonNull Context context) {
        return new AppDbHelper(context, "converter_db", 1);
    }
}
