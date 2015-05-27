package module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jedikv.simpleconverter.App;

import javax.inject.Singleton;

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
    SharedPreferences provideSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(mApp);
    }
}
