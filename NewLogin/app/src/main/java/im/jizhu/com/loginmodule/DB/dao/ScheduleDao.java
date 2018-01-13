package im.jizhu.com.loginmodule.DB.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import im.jizhu.com.loginmodule.DB.entity.ScheduleEntity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table ScheduleInfo.
*/
public class ScheduleDao extends AbstractDao<ScheduleEntity, Long> {

    public static final String TABLENAME = "ScheduleInfo";

    /**
     * Properties of entity ScheduleEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Task_id = new Property(0, long.class, "task_id", true, "TASK_ID");
        public final static Property Dlid = new Property(1, Integer.class, "dlid", false, "DLID");
        public final static Property Created = new Property(2, Integer.class, "created", false, "CREATED");
        public final static Property Received = new Property(3, Integer.class, "received", false, "RECEIVED");
        public final static Property Finished = new Property(4, Integer.class, "finished", false, "FINISHED");
        public final static Property Status = new Property(5, Integer.class, "status", false, "STATUS");
        public final static Property Isstar = new Property(6, Integer.class, "isstar", false, "ISSTAR");
        public final static Property Content = new Property(7, String.class, "content", false, "CONTENT");
    };


    public ScheduleDao(DaoConfig config) {
        super(config);
    }
    
    public ScheduleDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ScheduleInfo' (" + //
                "'TASK_ID' INTEGER PRIMARY KEY NOT NULL ," + // 0: task_id
                "'DLID' INTEGER," + // 1: dlid
                "'CREATED' INTEGER," + // 2: created
                "'RECEIVED' INTEGER," + // 3: received
                "'FINISHED' INTEGER," + // 4: finished
                "'STATUS' INTEGER," + // 5: status
                "'ISSTAR' INTEGER," + // 6: isstar
                "'CONTENT' TEXT);"); // 7: content
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ScheduleInfo'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ScheduleEntity entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getTask_id());
 
        Integer dlid = entity.getDlid();
        if (dlid != null) {
            stmt.bindLong(2, dlid);
        }
 
        Integer created = entity.getCreated();
        if (created != null) {
            stmt.bindLong(3, created);
        }
 
        Integer received = entity.getReceived();
        if (received != null) {
            stmt.bindLong(4, received);
        }
 
        Integer finished = entity.getFinished();
        if (finished != null) {
            stmt.bindLong(5, finished);
        }
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(6, status);
        }
 
        Integer isstar = entity.getIsstar();
        if (isstar != null) {
            stmt.bindLong(7, isstar);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ScheduleEntity readEntity(Cursor cursor, int offset) {
        ScheduleEntity entity = new ScheduleEntity( //
            cursor.getLong(offset + 0), // task_id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // dlid
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // created
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // received
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // finished
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // status
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // isstar
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // content
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ScheduleEntity entity, int offset) {
        entity.setTask_id(cursor.getLong(offset + 0));
        entity.setDlid(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setCreated(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setReceived(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setFinished(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setStatus(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setIsstar(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setContent(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ScheduleEntity entity, long rowId) {
        entity.setTask_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ScheduleEntity entity) {
        if(entity != null) {
            return entity.getTask_id();
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