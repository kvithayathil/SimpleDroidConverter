package com.jedikv.simpleconverter.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.dbutils.AppDbHelper;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kurian on 07/11/2016.
 */
@Module
public class DataModule {

    public static final String DB_NAME = "converter_db";
    public static final int DB_VERSION = 1;

    @Provides
    @Singleton
    @NonNull
    public SQLiteOpenHelper providesSQLiteOpenHelper(@NonNull Context context) {
        return new AppDbHelper(context, DB_NAME, DB_VERSION);
    }

    @Provides
    @Singleton
    @NonNull
    public StorIOSQLite providesStorIOSqlite(@NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        return DefaultStorIOSQLite
                .builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .build();
    }
}
