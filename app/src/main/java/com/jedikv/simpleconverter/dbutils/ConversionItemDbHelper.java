package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import java.util.List;

import converter_db.ConversionItem;
import converter_db.ConversionItemDao;

/**
 * Created by Kurian on 03/05/2015.
 */
public class ConversionItemDbHelper extends BaseDbHelper {

    public ConversionItemDbHelper(Context context) {
        super(context);
    }

    public long insertOrUpdate(ConversionItem entity) {

        return getDaoSession().insertOrReplace(entity);
    }

    public void bulkUpdate(List<ConversionItem> entities) {
        getDao().updateInTx(entities);

    }

    public void bulkInsertOrUpdate(List<ConversionItem> entities) {
        getDao().insertOrReplaceInTx(entities);

    }

    public void update(ConversionItem entity) {

        getDaoSession().update(entity);
    }

    public List<ConversionItem> getAll() {
        return  getDao().queryBuilder().list();
    }


    public void deleteAll() {
        getDao().deleteAll();
    }

    public void deleteItem(ConversionItem entity) {
        getDao().delete(entity);
    }

    @Override
    public ConversionItemDao getDao() {
        return getDaoSession().getConversionItemDao();
    }
}
