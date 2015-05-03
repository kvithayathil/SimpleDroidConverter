package com.jedikv.simpleconverter;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jedikv.simpleconverter.component.AppComponent;
import com.jedikv.simpleconverter.component.DaggerAppComponent;

import converter_db.DaoMaster;
import converter_db.DaoSession;
import module.AppModule;
import timber.log.Timber;

/**
 * Created by Kurian on 03/05/2015.
 */
public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private AppComponent mAppComponent;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.tag(TAG);
        Timber.plant(new Timber.DebugTree());

        setUpGraph();
        setUpDatabase();
    }

    private void setUpGraph() {
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

        mAppComponent.inject(this);
    }

    public AppComponent component() {
        return mAppComponent;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }


    public DaoSession daoSession() {

        return mDaoSession;
    }

    private void setUpDatabase() {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "converter_db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }
}
