package converter_db;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "CONVERSION_ITEM".
 */
public class ConversionItem {

    private Long id;
    private long pair_id;
    private Integer position;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ConversionItemDao myDao;

    private CurrencyPairEntity currencyPairEntity;
    private Long currencyPairEntity__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ConversionItem() {
    }

    public ConversionItem(Long id) {
        this.id = id;
    }

    public ConversionItem(Long id, long pair_id, Integer position) {
        this.id = id;
        this.pair_id = pair_id;
        this.position = position;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getConversionItemDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPair_id() {
        return pair_id;
    }

    public void setPair_id(long pair_id) {
        this.pair_id = pair_id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    /** To-one relationship, resolved on first access. */
    public CurrencyPairEntity getCurrencyPairEntity() {
        long __key = this.pair_id;
        if (currencyPairEntity__resolvedKey == null || !currencyPairEntity__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CurrencyPairEntityDao targetDao = daoSession.getCurrencyPairEntityDao();
            CurrencyPairEntity currencyPairEntityNew = targetDao.load(__key);
            synchronized (this) {
                currencyPairEntity = currencyPairEntityNew;
            	currencyPairEntity__resolvedKey = __key;
            }
        }
        return currencyPairEntity;
    }

    public void setCurrencyPairEntity(CurrencyPairEntity currencyPairEntity) {
        if (currencyPairEntity == null) {
            throw new DaoException("To-one property 'pair_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.currencyPairEntity = currencyPairEntity;
            pair_id = currencyPairEntity.getId();
            currencyPairEntity__resolvedKey = pair_id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
