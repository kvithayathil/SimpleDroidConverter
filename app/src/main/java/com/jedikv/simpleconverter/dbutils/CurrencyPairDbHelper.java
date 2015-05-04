package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import com.jedikv.simpleconverter.App;

import java.util.List;

import converter_db.CurrencyPairEntity;
import converter_db.CurrencyPairEntityDao;
import de.greenrobot.dao.AbstractDao;

/**
 * Created by Kurian on 03/05/2015.
 */
public class CurrencyPairDbHelper extends BaseDbHelper {

    public CurrencyPairDbHelper(Context context) {
        super(context);
    }

    public long insertOrUpdate(CurrencyPairEntity entity) {

        return getDaoSession().insertOrReplace(entity);
    }

    public void bulkUpdate(List<CurrencyPairEntity> entities) {
        getDao().updateInTx(entities);

    }

    public void bulkInsertOrUpdate(List<CurrencyPairEntity> entities) {
        getDao().insertOrReplaceInTx(entities);

    }


    public void deleteAll() {
        getDao().deleteAll();
    }

    public void deleteByPair(String code) {

    }

    @Override
    public CurrencyPairEntityDao getDao() {
        return getDaoSession().getCurrencyPairEntityDao();
    }
}
