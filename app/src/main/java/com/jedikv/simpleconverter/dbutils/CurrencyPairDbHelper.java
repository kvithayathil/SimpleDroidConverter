package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import com.jedikv.simpleconverter.App;

import java.util.List;

import converter_db.CurrencyEntityDao;
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

    public List<CurrencyPairEntity> getCurrencyTargetList(String sourceCurrencyCode) {

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Pair.like(sourceCurrencyCode+"%")).orderAsc(CurrencyPairEntityDao.Properties.Created_date).build().list();
    }

    public CurrencyPairEntity getGetPairByCodes(String sourceCurrency, String targetCurrency) {

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Pair.eq(sourceCurrency.toUpperCase() + "/" + targetCurrency.toUpperCase())).build().unique();
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
