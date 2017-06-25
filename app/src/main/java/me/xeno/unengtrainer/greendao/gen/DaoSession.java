package me.xeno.unengtrainer.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import me.xeno.unengtrainer.model.entity.FavoiriteRecord;

import me.xeno.unengtrainer.greendao.gen.FavoiriteRecordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig favoiriteRecordDaoConfig;

    private final FavoiriteRecordDao favoiriteRecordDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        favoiriteRecordDaoConfig = daoConfigMap.get(FavoiriteRecordDao.class).clone();
        favoiriteRecordDaoConfig.initIdentityScope(type);

        favoiriteRecordDao = new FavoiriteRecordDao(favoiriteRecordDaoConfig, this);

        registerDao(FavoiriteRecord.class, favoiriteRecordDao);
    }
    
    public void clear() {
        favoiriteRecordDaoConfig.clearIdentityScope();
    }

    public FavoiriteRecordDao getFavoiriteRecordDao() {
        return favoiriteRecordDao;
    }

}
