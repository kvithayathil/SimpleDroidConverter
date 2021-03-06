package converter_db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import converter_db.CurrencyEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CURRENCY_ENTITY".
*/
public class CurrencyEntityDao extends AbstractDao<CurrencyEntity, Long> {

    public static final String TABLENAME = "CURRENCY_ENTITY";

    /**
     * Properties of entity CurrencyEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Symbol = new Property(0, String.class, "symbol", false, "SYMBOL");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property CountryName = new Property(2, String.class, "countryName", false, "COUNTRY_NAME");
        public final static Property NumericCode = new Property(3, Long.class, "numericCode", true, "NUMERIC_CODE");
        public final static Property Code = new Property(4, String.class, "code", false, "CODE");
    };


    public CurrencyEntityDao(DaoConfig config) {
        super(config);
    }
    
    public CurrencyEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CURRENCY_ENTITY\" (" + //
                "\"SYMBOL\" TEXT," + // 0: symbol
                "\"NAME\" TEXT," + // 1: name
                "\"COUNTRY_NAME\" TEXT," + // 2: countryName
                "\"NUMERIC_CODE\" INTEGER PRIMARY KEY ," + // 3: numericCode
                "\"CODE\" TEXT NOT NULL UNIQUE );"); // 4: code
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CURRENCY_ENTITY\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CurrencyEntity entity) {
        stmt.clearBindings();
 
        String symbol = entity.getSymbol();
        if (symbol != null) {
            stmt.bindString(1, symbol);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String countryName = entity.getCountryName();
        if (countryName != null) {
            stmt.bindString(3, countryName);
        }
 
        Long numericCode = entity.getNumericCode();
        if (numericCode != null) {
            stmt.bindLong(4, numericCode);
        }
        stmt.bindString(5, entity.getCode());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3);
    }    

    /** @inheritdoc */
    @Override
    public CurrencyEntity readEntity(Cursor cursor, int offset) {
        CurrencyEntity entity = new CurrencyEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // symbol
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // countryName
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // numericCode
            cursor.getString(offset + 4) // code
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CurrencyEntity entity, int offset) {
        entity.setSymbol(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCountryName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNumericCode(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setCode(cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CurrencyEntity entity, long rowId) {
        entity.setNumericCode(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CurrencyEntity entity) {
        if(entity != null) {
            return entity.getNumericCode();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
