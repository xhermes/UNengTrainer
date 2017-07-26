package me.xeno.unengtrainer.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import me.xeno.unengtrainer.model.entity.FavouriteRecord;
import me.xeno.unengtrainer.model.entity.HistoryRecord;

import me.xeno.unengtrainer.greendao.gen.FavouriteRecordDao;
import me.xeno.unengtrainer.greendao.gen.HistoryRecordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig favouriteRecordDaoConfig;
    private final DaoConfig historyRecordDaoConfig;

    private final FavouriteRecordDao favouriteRecordDao;
    private final HistoryRecordDao historyRecordDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        favouriteRecordDaoConfig = daoConfigMap.get(FavouriteRecordDao.class).clone();
        favouriteRecordDaoConfig.initIdentityScope(type);

        historyRecordDaoConfig = daoConfigMap.get(HistoryRecordDao.class).clone();
        historyRecordDaoConfig.initIdentityScope(type);

        favouriteRecordDao = new FavouriteRecordDao(favouriteRecordDaoConfig, this);
        historyRecordDao = new HistoryRecordDao(historyRecordDaoConfig, this);

        registerDao(FavouriteRecord.class, favouriteRecordDao);
        registerDao(HistoryRecord.class, historyRecordDao);
    }
    
    public void clear() {
        favouriteRecordDaoConfig.clearIdentityScope();
        historyRecordDaoConfig.clearIdentityScope();
    }

    public FavouriteRecordDao getFavouriteRecordDao() {
        return favouriteRecordDao;
    }

    public HistoryRecordDao getHistoryRecordDao() {
        return historyRecordDao;
    }

}
