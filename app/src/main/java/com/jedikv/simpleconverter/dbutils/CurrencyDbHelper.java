package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import com.jedikv.simpleconverter.App;

import java.util.List;

import converter_db.CurrencyEntity;
import converter_db.CurrencyEntityDao;
import converter_db.CurrencyPairEntity;
import converter_db.CurrencyPairEntityDao;

/**
 * Created by Kurian on 03/05/2015.
 */
public class CurrencyDbHelper extends BaseDbHelper {

    public CurrencyDbHelper(Context context) {
        super(context);
    }

    public long insertOrUpdate(CurrencyEntity entity) {

        return getDaoSession().insertOrReplace(entity);
    }

    public void bulkUpdate(List<CurrencyEntity> entities) {
        getDao().updateInTx(entities);

    }

    public void bulkInsertOrUpdate(List<CurrencyEntity> entities) {
        getDao().insertOrReplaceInTx(entities);

    }


    public void deleteAll() {
        getDao().deleteAll();
    }


    @Override
    public CurrencyEntityDao getDao() {
        return getDaoSession().getCurrencyEntityDao();
    }
}
