package com.jedikv.simpleconverter.injection.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jedikv.simpleconverter.App;
import com.jedikv.simpleconverter.dbutils.ConversionItemDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyDbHelper;
import com.jedikv.simpleconverter.dbutils.CurrencyPairDbHelper;

import javax.inject.Singleton;

import converter_db.DaoSession;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 03/05/2015.
 */
@Module
public class AppModule {

    private App mApp;

    public AppModule(App app) {
        this.mApp = app;
    }

    @Provides
    @Singleton
    public App providesApplication() {
        return this.mApp;
    }


    @Provides
    @Singleton
    public Context providesContext() {
        return this.mApp;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    ConversionItemDbHelper provideConversionItemDbHelper(Context context) {
        return new ConversionItemDbHelper(context);
    }

    @Provides
    @Singleton
    CurrencyPairDbHelper provideCurrencyPairDbHelper(Context context) {
        return new CurrencyPairDbHelper(context);
    }

    @Provides
    @Singleton
    CurrencyDbHelper provideCurrencyDbHelper(Context context) {
        return new CurrencyDbHelper(context);
    }

    @Provides
    @Singleton
    public DaoSession provideDaoSession() {
        return mApp.daoSession();
    }

}
