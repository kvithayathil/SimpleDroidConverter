package com.jedikv.simpleconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

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
    App providesApplication() {
        return this.application;
    }


    @Provides
    @NonNull
    @ConversionAppScope
    Context providesContext() {
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
}
