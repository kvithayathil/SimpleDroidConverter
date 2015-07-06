package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import java.util.List;
import java.util.TimeZone;

import converter_db.CurrencyPairEntity;
import converter_db.CurrencyPairEntityDao;
import de.greenrobot.dao.query.QueryBuilder;
import hirondelle.date4j.DateTime;
import timber.log.Timber;

/**
 * Created by Kurian on 03/05/2015.
 */
public class CurrencyPairDbHelper extends BaseDbHelper {

    public CurrencyPairDbHelper(Context context) {
        super(context);
        Timber.tag(CurrencyPairDbHelper.class.getSimpleName());
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

    public CurrencyPairEntity getCurrencyPair(long sourceId, long targetId) {

        Timber.d("Source Id: " + sourceId + " TargetId: " + targetId);

        QueryBuilder<CurrencyPairEntity> queryBuilder = getDao().queryBuilder();
        queryBuilder.where(CurrencyPairEntityDao.Properties.Source_currency.eq(sourceId), CurrencyPairEntityDao.Properties.Target_currency.eq(targetId));
        return queryBuilder.build().unique();
    }

    public List<CurrencyPairEntity> getCurrencyPairBySource(long sourceId) {

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Source_currency.eq(sourceId)).list();

    }

    public List<CurrencyPairEntity> getPairsToBeUpdated(long sourceId) {

        TimeZone utc = TimeZone.getTimeZone("UTC");

        DateTime today = DateTime.now(utc);
        DateTime yesterday = today.minusDays(1);

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Source_currency.eq(sourceId), CurrencyPairEntityDao.Properties.Last_updated.le((yesterday.getMilliseconds(utc)))).build().list();

    }

    public List<CurrencyPairEntity> getCurrencyPairByTarget(long sourceId) {

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Target_currency.eq(sourceId)).list();
    }

    public void deleteAll() {
        getDao().deleteAll();
    }


    @Override
    public CurrencyPairEntityDao getDao() {
        return getDaoSession().getCurrencyPairEntityDao();
    }
}
