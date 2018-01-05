package im.jizhu.com.loginmodule.DB.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import im.jizhu.com.loginmodule.DB.entity.AttachEntity;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import im.jizhu.com.loginmodule.DB.entity.AttachEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table Attach.
*/
public class AttachDao extends AbstractDao<AttachEntity, Long> {

    public static final String TABLENAME = "Attach";

    /**
     * Properties of entity AttachEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Attach_id = new Property(0, Long.class, "attach_id", true, "ATTACH_ID");
        public final static Property Voice = new Property(1, String.class, "voice", false, "VOICE");
        public final static Property Photo = new Property(2, String.class, "photo", false, "PHOTO");
        public final static Property File = new Property(3, String.class, "file", false, "FILE");
        public final static Property Url = new Property(4, String.class, "url", false, "URL");
        public final static Property Capacity = new Property(5, String.class, "capacity", false, "CAPACITY");
        public final static Property Attach_text1 = new Property(6, String.class, "attach_text1", false, "ATTACH_TEXT1");
        public final static Property Attach_text2 = new Property(7, String.class, "attach_text2", false, "ATTACH_TEXT2");
        public final static Property Attach_text3 = new Property(8, String.class, "attach_text3", false, "ATTACH_TEXT3");
        public final static Property Status = new Property(9, String.class, "status", false, "STATUS");
    };


    public AttachDao(DaoConfig config) {
        super(config);
    }
    
    public AttachDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'Attach' (" + //
                "'ATTACH_ID' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: attach_id
                "'VOICE' TEXT NOT NULL ," + // 1: voice
                "'PHOTO' TEXT NOT NULL ," + // 2: photo
                "'FILE' TEXT NOT NULL ," + // 3: file
                "'URL' TEXT NOT NULL ," + // 4: url
                "'CAPACITY' TEXT NOT NULL ," + // 5: capacity
                "'ATTACH_TEXT1' TEXT NOT NULL ," + // 6: attach_text1
                "'ATTACH_TEXT2' TEXT NOT NULL ," + // 7: attach_text2
                "'ATTACH_TEXT3' TEXT NOT NULL ," + // 8: attach_text3
                "'STATUS' TEXT NOT NULL );"); // 9: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'Attach'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, AttachEntity entity) {
        stmt.clearBindings();
 
        Long attach_id = entity.getAttach_id();
        if (attach_id != null) {
            stmt.bindLong(1, attach_id);
        }
        stmt.bindString(2, entity.getVoice());
        stmt.bindString(3, entity.getPhoto());
        stmt.bindString(4, entity.getFile());
        stmt.bindString(5, entity.getUrl());
        stmt.bindString(6, entity.getCapacity());
        stmt.bindString(7, entity.getAttach_text1());
        stmt.bindString(8, entity.getAttach_text2());
        stmt.bindString(9, entity.getAttach_text3());
        stmt.bindString(10, entity.getStatus());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public AttachEntity readEntity(Cursor cursor, int offset) {
        AttachEntity entity = new AttachEntity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // attach_id
            cursor.getString(offset + 1), // voice
            cursor.getString(offset + 2), // photo
            cursor.getString(offset + 3), // file
            cursor.getString(offset + 4), // url
            cursor.getString(offset + 5), // capacity
            cursor.getString(offset + 6), // attach_text1
            cursor.getString(offset + 7), // attach_text2
            cursor.getString(offset + 8), // attach_text3
            cursor.getString(offset + 9) // status
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, AttachEntity entity, int offset) {
        entity.setAttach_id(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setVoice(cursor.getString(offset + 1));
        entity.setPhoto(cursor.getString(offset + 2));
        entity.setFile(cursor.getString(offset + 3));
        entity.setUrl(cursor.getString(offset + 4));
        entity.setCapacity(cursor.getString(offset + 5));
        entity.setAttach_text1(cursor.getString(offset + 6));
        entity.setAttach_text2(cursor.getString(offset + 7));
        entity.setAttach_text3(cursor.getString(offset + 8));
        entity.setStatus(cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(AttachEntity entity, long rowId) {
        entity.setAttach_id(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(AttachEntity entity) {
        if(entity != null) {
            return entity.getAttach_id();
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