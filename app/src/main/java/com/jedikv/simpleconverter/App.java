package com.jedikv.simpleconverter;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.jedikv.simpleconverter.injection.component.AppComponent;
import com.jedikv.simpleconverter.injection.component.DaggerAppComponent;
import com.jedikv.simpleconverter.dbutils.ConverterDaoMaster;
import com.jedikv.simpleconverter.utils.OttoBus;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.otto.ThreadEnforcer;

import net.danlew.android.joda.JodaTimeAndroid;

import converter_db.DaoMaster;
import converter_db.DaoSession;
import com.jedikv.simpleconverter.injection.module.AppModule;
import timber.log.Timber;

/**
 * Created by Kurian on 03/05/2015.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private AppComponent mAppComponent;
    private DaoSession mDaoSession;

    private static OttoBus mBus;


    @Override
    public void onCreate() {

        super.onCreate();
        Timber.tag(TAG);
        LeakCanary.install(this);
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());


        setUpGraph();

        JodaTimeAndroid.init(this);

        mBus = new OttoBus(ThreadEnforcer.MAIN);
        Timber.plant(new Timber.DebugTree());

        setUpDatabase();
    }

    private void setUpGraph() {
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        mAppComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }


    public DaoSession daoSession() {

        return mDaoSession;
    }

    public static OttoBus getBusInstance() {
        return mBus;
    }


    private void setUpDatabase() {

        ConverterDaoMaster helper = new ConverterDaoMaster(this, "converter_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
}
