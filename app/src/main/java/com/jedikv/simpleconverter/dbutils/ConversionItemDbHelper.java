package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import java.util.List;

import converter_db.ConversionEntity;
import converter_db.ConversionEntityDao;
import converter_db.CurrencyEntity;
import converter_db.CurrencyEntityDao;

/**
 * Created by Kurian on 03/05/2015.
 */
public class ConversionItemDbHelper extends BaseDbHelper {

    public ConversionItemDbHelper(Context context) {
        super(context);
    }

    public long insertOrUpdate(ConversionEntity entity) {

        return getDaoSession().insertOrReplace(entity);
    }

    public void bulkUpdate(List<ConversionEntity> entities) {
        getDao().updateInTx(entities);

    }

    public void bulkInsertOrUpdate(List<ConversionEntity> entities) {
        getDao().insertOrReplaceInTx(entities);

    }

    public void update(ConversionEntity entity) {

        getDaoSession().update(entity);
    }

    public List<ConversionEntity> getAll() {
        return  getDao().loadAll();
    }

    public void deleteAll() {
        getDao().deleteAll();
    }

    public void deleteItem(ConversionEntity entity) {
        getDao().delete(entity);
    }

    @Override
    public ConversionEntityDao getDao() {
        return getDaoSession().getConversionEntityDao();
    }
}
