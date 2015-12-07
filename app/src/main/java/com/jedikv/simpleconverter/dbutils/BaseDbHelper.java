package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import com.jedikv.simpleconverter.App;

import converter_db.DaoSession;
import de.greenrobot.dao.AbstractDao;

/**
 * Created by Kurian on 03/05/2015.
 */
public abstract class BaseDbHelper {

    protected DaoSession daoSession;

    public BaseDbHelper(Context context) {

         daoSession = App.get(context).daoSession();
    }

    protected DaoSession getDaoSession() {
        return daoSession;
    }

    public void clearCache() {
        getDaoSession().clear();
    }

    public abstract <T extends AbstractDao<T, Long>> T getDao();
}
