package com.jedikv.simpleconverter;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.jedikv.simpleconverter.dbutils.ConverterDaoMaster;
import com.jedikv.simpleconverter.injection.component.AppComponent;
import com.jedikv.simpleconverter.injection.component.DaggerAppComponent;
import com.jedikv.simpleconverter.injection.module.AppModule;
import com.jedikv.simpleconverter.model.Models;
import com.jedikv.simpleconverter.utils.OttoBus;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.otto.ThreadEnforcer;

import converter_db.DaoMaster;
import converter_db.DaoSession;
import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.rx.RxSupport;
import io.requery.rx.SingleEntityStore;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import timber.log.Timber;

/**
 * Created by Kurian on 03/05/2015.
 */
public class App extends Application {

    private static final String TAG = App.class.getCanonicalName();

    private AppComponent mAppComponent;
    private DaoSession mDaoSession;

    private static OttoBus mBus;

    private SingleEntityStore<Persistable> dataStore;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

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

    /**
     * @return {@link EntityDataStore} single instance for the application.
     * <p/>
     * Note if you're using Dagger you can make this part of your application level module returning
     * {@code @Provides @Singleton}.
     */
    SingleEntityStore<Persistable> getData() {
        if (dataStore == null) {
            // override onUpgrade to handle migrating to a new version
            DatabaseSource source = new DatabaseSource(this, Models.DEFAULT, 1) {

            };
            if (BuildConfig.DEBUG) {
                // use this in development mode to drop and recreate the tables on every upgrade
                source.setTableCreationMode(TableCreationMode.DROP_CREATE);
            } else {
                source.setTableCreationMode(TableCreationMode.CREATE_NOT_EXISTS);
            }
            Configuration configuration = source.getConfiguration();
            dataStore = RxSupport.toReactiveStore(
                    new EntityDataStore<Persistable>(configuration));
        }
        return dataStore;
    }
}
