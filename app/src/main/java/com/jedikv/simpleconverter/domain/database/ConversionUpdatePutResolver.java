package com.jedikv.simpleconverter.domain.database;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

/**
 * Created by Kurian on 21/11/2016.
 */

public class ConversionUpdatePutResolver extends PutResolver<ContentValues> {

    final long id;

    public static ConversionUpdatePutResolver create(long id) {
        return new ConversionUpdatePutResolver(id);
    }

    private ConversionUpdatePutResolver(long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite,
                                @NonNull ContentValues contentValues) {

        StorIOSQLite.LowLevel lowLevel = storIOSQLite.lowLevel();
        lowLevel.beginTransaction();

        final UpdateQuery query = UpdateQuery
                .builder()
                .table(ConversionPairTable.TABLE)
                .where(ConversionPairTable.COLUMN_ID + " = ?")
                .whereArgs(id)
                .build();

        int updateCount = 0;

        try {
            updateCount = lowLevel.update(query, contentValues);
            lowLevel.setTransactionSuccessful();
        } finally {
            lowLevel.endTransaction();
        }

        return PutResult.newUpdateResult(updateCount, ConversionPairTable.TABLE);
    }
}

