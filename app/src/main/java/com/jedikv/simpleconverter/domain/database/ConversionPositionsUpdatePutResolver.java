package com.jedikv.simpleconverter.domain.database;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.*;

/**
 * Created by Kurian on 21/11/2016.
 */

public class ConversionPositionsUpdatePutResolver extends PutResolver<ContentValues> {

    final int oldPos;
    final String sourceIsoCode;

    public static ConversionPositionsUpdatePutResolver create(String sourceIsoCode, int oldPos) {
        return new ConversionPositionsUpdatePutResolver(sourceIsoCode, oldPos);
    }

    private ConversionPositionsUpdatePutResolver(String sourceIsoCode, int oldPos) {
        this.sourceIsoCode = sourceIsoCode;
        this.oldPos = oldPos;
    }

    @NonNull
    @Override
    public PutResult performPut(@NonNull StorIOSQLite storIOSQLite,
                                @NonNull ContentValues contentValues) {

        StorIOSQLite.LowLevel lowLevel = storIOSQLite.lowLevel();
        lowLevel.beginTransaction();

        final long id = contentValues.getAsLong(COLUMN_ID);

        final UpdateQuery query = UpdateQuery
                .builder()
                .table(TABLE)
                .where(COLUMN_ID + " = ?")
                .whereArgs(id)
                .build();

        int updateCount = 0;
        try {
            updateCount = lowLevel.update(query, contentValues);

            //Determine if it's a position swap or an item removal
            if(contentValues.containsKey(COLUMN_LIST_POSITION)) {
                final int position = contentValues
                        .getAsInteger(COLUMN_LIST_POSITION);
                updateCount += lowLevel
                        .update(swapConversionPosition(sourceIsoCode, position), contentValues);
            } else {
                lowLevel.executeSQL(updateRemainingConversionPositions(sourceIsoCode, oldPos));
            }

            lowLevel.setTransactionSuccessful();
        } finally {
            lowLevel.endTransaction();
        }
        return PutResult.newUpdateResult(updateCount, ConversionPairTable.TABLE);
    }

    private UpdateQuery swapConversionPosition(String sourceCurrency,
                                            int newPos) {

        return UpdateQuery.builder()
                .table(TABLE)
                .where(COLUMN_SOURCE_CURRENCY_CODE + " = ? AND "
                + COLUMN_LIST_POSITION + " = ?")
                .whereArgs(sourceCurrency, newPos)
                .build();
    }

    /**
     * Update the positions of every conversion entry below the deleted entry
     * @param sourceCurrency the source currency to filter by
     * @param deletedPosition the deleted position to compare to
     * @return Constructed raw query object
     */
    private RawQuery updateRemainingConversionPositions(String sourceCurrency, int deletedPosition) {

        return RawQuery.builder()
                .query("UPDATE " + TABLE + " SET " + COLUMN_LIST_POSITION
                        + " = " + COLUMN_LIST_POSITION + " - 1"
                        + " WHERE "
                        + COLUMN_SOURCE_CURRENCY_CODE + " = ? AND "
                        + COLUMN_LIST_POSITION + " > ?")
                .affectsTables(TABLE)
                .args(sourceCurrency, deletedPosition)
                .build();
    }
}

