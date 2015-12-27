package com.jedikv.simpleconverter.dbutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import com.jedikv.simpleconverter.BuildConfig;
import com.jedikv.simpleconverter.utils.DateUtils;

import java.util.Date;
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

        if(BuildConfig.DEBUG) {
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }

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

    public CurrencyPairEntity queryById(long id) {

        return getDao().loadDeep(id);
    }


    /**
     * Try and update a currencyPair by
     * @param entity currency pair entity to update
     * @return
     */
    public long updateCurrencyPair(CurrencyPairEntity entity) {

        SQLiteDatabase db = getDaoSession().getDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CurrencyPairEntityDao.Properties.Last_updated.columnName, System.currentTimeMillis());
        contentValues.put(CurrencyPairEntityDao.Properties.Rate.columnName, entity.getRate());
        String where = CurrencyPairEntityDao.Properties.Source_currency.columnName + " = ? AND " + CurrencyPairEntityDao.Properties.Target_currency.columnName + " = ?";
        return db.update(CurrencyPairEntityDao.TABLENAME, contentValues, where, new String[]{String.valueOf(entity.getSource_currency()), String.valueOf(entity.getTarget_currency())});
    }

    public CurrencyPairEntity getCurrencyPairFromIdPair(long sourceId, long targetId) {

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Source_currency.eq(sourceId), CurrencyPairEntityDao.Properties.Target_currency.eq(targetId)).build().unique();

    }

    public List<CurrencyPairEntity> getPairsToBeUpdated(long sourceId) {

        return getDao().queryBuilder().where(CurrencyPairEntityDao.Properties.Source_currency.eq(sourceId), CurrencyPairEntityDao.Properties.Last_updated.le(DateUtils.getPrevious24HoursTimestamp())).build().list();
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
