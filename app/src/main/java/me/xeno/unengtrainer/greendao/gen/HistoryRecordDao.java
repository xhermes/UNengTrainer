package me.xeno.unengtrainer.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import me.xeno.unengtrainer.model.entity.HistoryRecord;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HISTORY_RECORD".
*/
public class HistoryRecordDao extends AbstractDao<HistoryRecord, Long> {

    public static final String TABLENAME = "HISTORY_RECORD";

    /**
     * Properties of entity HistoryRecord.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property CreateTime = new Property(1, String.class, "createTime", false, "CREATE_TIME");
        public final static Property SwingAngle = new Property(2, double.class, "swingAngle", false, "SWING_ANGLE");
        public final static Property ElevationAngle = new Property(3, double.class, "elevationAngle", false, "ELEVATION_ANGLE");
        public final static Property LeftMotorSpeed = new Property(4, int.class, "leftMotorSpeed", false, "LEFT_MOTOR_SPEED");
        public final static Property RightMotorSpeed = new Property(5, int.class, "rightMotorSpeed", false, "RIGHT_MOTOR_SPEED");
    }


    public HistoryRecordDao(DaoConfig config) {
        super(config);
    }
    
    public HistoryRecordDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HISTORY_RECORD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"CREATE_TIME\" TEXT," + // 1: createTime
                "\"SWING_ANGLE\" REAL NOT NULL ," + // 2: swingAngle
                "\"ELEVATION_ANGLE\" REAL NOT NULL ," + // 3: elevationAngle
                "\"LEFT_MOTOR_SPEED\" INTEGER NOT NULL ," + // 4: leftMotorSpeed
                "\"RIGHT_MOTOR_SPEED\" INTEGER NOT NULL );"); // 5: rightMotorSpeed
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HISTORY_RECORD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HistoryRecord entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(2, createTime);
        }
        stmt.bindDouble(3, entity.getSwingAngle());
        stmt.bindDouble(4, entity.getElevationAngle());
        stmt.bindLong(5, entity.getLeftMotorSpeed());
        stmt.bindLong(6, entity.getRightMotorSpeed());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HistoryRecord entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(2, createTime);
        }
        stmt.bindDouble(3, entity.getSwingAngle());
        stmt.bindDouble(4, entity.getElevationAngle());
        stmt.bindLong(5, entity.getLeftMotorSpeed());
        stmt.bindLong(6, entity.getRightMotorSpeed());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public HistoryRecord readEntity(Cursor cursor, int offset) {
        HistoryRecord entity = new HistoryRecord( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createTime
            cursor.getDouble(offset + 2), // swingAngle
            cursor.getDouble(offset + 3), // elevationAngle
            cursor.getInt(offset + 4), // leftMotorSpeed
            cursor.getInt(offset + 5) // rightMotorSpeed
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HistoryRecord entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setCreateTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSwingAngle(cursor.getDouble(offset + 2));
        entity.setElevationAngle(cursor.getDouble(offset + 3));
        entity.setLeftMotorSpeed(cursor.getInt(offset + 4));
        entity.setRightMotorSpeed(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HistoryRecord entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HistoryRecord entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HistoryRecord entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}