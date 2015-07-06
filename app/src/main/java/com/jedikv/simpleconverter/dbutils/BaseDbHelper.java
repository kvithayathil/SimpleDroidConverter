package com.jedikv.simpleconverter.dbutils;

import android.content.Context;

import com.jedikv.simpleconverter.App;

import converter_db.DaoSession;
import de.greenrobot.dao.AbstractDao;

/**
 * Created by Kurian on 03/05/2015.
 */
public abstract class BaseDbHelper {

    protected Context mContext;

    public BaseDbHelper(Context context) {
        mContext = App.get(context);
    }

    protected DaoSession getDaoSession() {
        return App.get(mContext).daoSession();
    }

    public abstract <T extends AbstractDao<T, Long>> T getDao();
}
