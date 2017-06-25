package me.xeno.unengtrainer.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import me.xeno.unengtrainer.model.entity.FavoiriteRecord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FAVOIRITE_RECORD".
*/
public class FavoiriteRecordDao extends AbstractDao<FavoiriteRecord, Void> {

    public static final String TABLENAME = "FAVOIRITE_RECORD";

    /**
     * Properties of entity FavoiriteRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property CreateTime = new Property(1, String.class, "createTime", false, "CREATE_TIME");
        public final static Property ModifyTime = new Property(2, String.class, "modifyTime", false, "MODIFY_TIME");
        public final static Property SwingAngle = new Property(3, double.class, "swingAngle", false, "SWING_ANGLE");
        public final static Property ElevationAngle = new Property(4, double.class, "elevationAngle", false, "ELEVATION_ANGLE");
        public final static Property LeftMotorSpeed = new Property(5, int.class, "leftMotorSpeed", false, "LEFT_MOTOR_SPEED");
        public final static Property RightMotorSpeed = new Property(6, int.class, "rightMotorSpeed", false, "RIGHT_MOTOR_SPEED");
    }


    public FavoiriteRecordDao(DaoConfig config) {
        super(config);
    }
    
    public FavoiriteRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FAVOIRITE_RECORD\" (" + //
                "\"NAME\" TEXT," + // 0: name
                "\"CREATE_TIME\" TEXT," + // 1: createTime
                "\"MODIFY_TIME\" TEXT," + // 2: modifyTime
                "\"SWING_ANGLE\" REAL NOT NULL ," + // 3: swingAngle
                "\"ELEVATION_ANGLE\" REAL NOT NULL ," + // 4: elevationAngle
                "\"LEFT_MOTOR_SPEED\" INTEGER NOT NULL ," + // 5: leftMotorSpeed
                "\"RIGHT_MOTOR_SPEED\" INTEGER NOT NULL );"); // 6: rightMotorSpeed
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FAVOIRITE_RECORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FavoiriteRecord entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(2, createTime);
        }
 
        String modifyTime = entity.getModifyTime();
        if (modifyTime != null) {
            stmt.bindString(3, modifyTime);
        }
        stmt.bindDouble(4, entity.getSwingAngle());
        stmt.bindDouble(5, entity.getElevationAngle());
        stmt.bindLong(6, entity.getLeftMotorSpeed());
        stmt.bindLong(7, entity.getRightMotorSpeed());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FavoiriteRecord entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(2, createTime);
        }
 
        String modifyTime = entity.getModifyTime();
        if (modifyTime != null) {
            stmt.bindString(3, modifyTime);
        }
        stmt.bindDouble(4, entity.getSwingAngle());
        stmt.bindDouble(5, entity.getElevationAngle());
        stmt.bindLong(6, entity.getLeftMotorSpeed());
        stmt.bindLong(7, entity.getRightMotorSpeed());
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public FavoiriteRecord readEntity(Cursor cursor, int offset) {
        FavoiriteRecord entity = new FavoiriteRecord( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // modifyTime
            cursor.getDouble(offset + 3), // swingAngle
            cursor.getDouble(offset + 4), // elevationAngle
            cursor.getInt(offset + 5), // leftMotorSpeed
            cursor.getInt(offset + 6) // rightMotorSpeed
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FavoiriteRecord entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCreateTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setModifyTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSwingAngle(cursor.getDouble(offset + 3));
        entity.setElevationAngle(cursor.getDouble(offset + 4));
        entity.setLeftMotorSpeed(cursor.getInt(offset + 5));
        entity.setRightMotorSpeed(cursor.getInt(offset + 6));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(FavoiriteRecord entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(FavoiriteRecord entity) {
        return null;
    }

    @Override
    public boolean hasKey(FavoiriteRecord entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}