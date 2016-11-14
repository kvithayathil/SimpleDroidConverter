package com.jedikv.simpleconverter.domain.database;

import android.support.annotation.NonNull;

import com.jedikv.simpleconverter.domain.ConversionItem;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import static com.jedikv.simpleconverter.domain.database.ConversionPairTable.*;

/**
 * Created by Kurian on 14/11/2016.
 */

public class ConversionDeleteResolver extends DefaultDeleteResolver<ConversionItem> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull ConversionItem object) {
        return DeleteQuery.builder()
                .table(TABLE)
                .where(COLUMN_ID + " = ?")
                .whereArgs(object.conversionComboId())
                .build();
    }
}
