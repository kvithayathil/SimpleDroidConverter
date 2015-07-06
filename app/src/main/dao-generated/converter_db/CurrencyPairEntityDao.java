package converter_db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import converter_db.CurrencyPairEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CURRENCY_PAIR_ENTITY.
*/
public class CurrencyPairEntityDao extends AbstractDao<CurrencyPairEntity, Long> {

    public static final String TABLENAME = "CURRENCY_PAIR_ENTITY";

    /**
     * Properties of entity CurrencyPairEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Created_date = new Property(1, java.util.Date.class, "created_date", false, "CREATED_DATE");
        public final static Property Last_updated = new Property(2, java.util.Date.class, "last_updated", false, "LAST_UPDATED");
        public final static Property Source_currency = new Property(3, long.class, "source_currency", false, "SOURCE_CURRENCY");
        public final static Property Target_currency = new Property(4, long.class, "target_currency", false, "TARGET_CURRENCY");
        public final static Property Rate = new Property(5, Integer.class, "rate", false, "RATE");
    };

    private DaoSession daoSession;


    public CurrencyPairEntityDao(DaoConfig config) {
        super(config);
    }
    
    public CurrencyPairEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CURRENCY_PAIR_ENTITY' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CREATED_DATE' INTEGER," + // 1: created_date
                "'LAST_UPDATED' INTEGER," + // 2: last_updated
                "'SOURCE_CURRENCY' INTEGER NOT NULL ," + // 3: source_currency
                "'TARGET_CURRENCY' INTEGER NOT NULL ," + // 4: target_currency
                "'RATE' INTEGER);"); // 5: rate
        // Add Indexes
        db.execSQL("CREATE UNIQUE INDEX " + constraint + "IDX_CURRENCY_PAIR_ENTITY_SOURCE_CURRENCY_TARGET_CURRENCY ON CURRENCY_PAIR_ENTITY" +
                " (SOURCE_CURRENCY,TARGET_CURRENCY);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CURRENCY_PAIR_ENTITY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CurrencyPairEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date created_date = entity.getCreated_date();
        if (created_date != null) {
            stmt.bindLong(2, created_date.getTime());
        }
 
        java.util.Date last_updated = entity.getLast_updated();
        if (last_updated != null) {
            stmt.bindLong(3, last_updated.getTime());
        }
        stmt.bindLong(4, entity.getSource_currency());
        stmt.bindLong(5, entity.getTarget_currency());
 
        Integer rate = entity.getRate();
        if (rate != null) {
            stmt.bindLong(6, rate);
        }
    }

    @Override
    protected void attachEntity(CurrencyPairEntity entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CurrencyPairEntity readEntity(Cursor cursor, int offset) {
        CurrencyPairEntity entity = new CurrencyPairEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // created_date
            cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)), // last_updated
            cursor.getLong(offset + 3), // source_currency
            cursor.getLong(offset + 4), // target_currency
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5) // rate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CurrencyPairEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCreated_date(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setLast_updated(cursor.isNull(offset + 2) ? null : new java.util.Date(cursor.getLong(offset + 2)));
        entity.setSource_currency(cursor.getLong(offset + 3));
        entity.setTarget_currency(cursor.getLong(offset + 4));
        entity.setRate(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CurrencyPairEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CurrencyPairEntity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCurrencyEntityDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getCurrencyEntityDao().getAllColumns());
            builder.append(" FROM CURRENCY_PAIR_ENTITY T");
            builder.append(" LEFT JOIN CURRENCY_ENTITY T0 ON T.'SOURCE_CURRENCY'=T0.'NUMERIC_CODE'");
            builder.append(" LEFT JOIN CURRENCY_ENTITY T1 ON T.'TARGET_CURRENCY'=T1.'NUMERIC_CODE'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected CurrencyPairEntity loadCurrentDeep(Cursor cursor, boolean lock) {
        CurrencyPairEntity entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        CurrencyEntity source_id = loadCurrentOther(daoSession.getCurrencyEntityDao(), cursor, offset);
         if(source_id != null) {
            entity.setSource_id(source_id);
        }
        offset += daoSession.getCurrencyEntityDao().getAllColumns().length;

        CurrencyEntity target_id = loadCurrentOther(daoSession.getCurrencyEntityDao(), cursor, offset);
         if(target_id != null) {
            entity.setTarget_id(target_id);
        }

        return entity;    
    }

    public CurrencyPairEntity loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<CurrencyPairEntity> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<CurrencyPairEntity> list = new ArrayList<CurrencyPairEntity>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<CurrencyPairEntity> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<CurrencyPairEntity> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
