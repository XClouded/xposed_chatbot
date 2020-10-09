package com.alibaba.ut.abtest.internal.database;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class ABDataObject extends DataObject {
    public static final String COLUMN_CREATE_TIME = "create_time";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MODIFIED_TIME = "modified_time";
    public static final String COLUMN_OWNER_ID = "owner_id";
    private static final long serialVersionUID = -2123949727986811135L;
    private long createTime;
    private long id;
    private long modifiedTime;
    private String ownerId;

    public ABDataObject() {
    }

    public ABDataObject(Cursor cursor) {
        this.id = getCursorLong(cursor, "id");
        this.createTime = getCursorLong(cursor, COLUMN_CREATE_TIME);
        this.modifiedTime = getCursorLong(cursor, COLUMN_MODIFIED_TIME);
        this.ownerId = getCursorString(cursor, COLUMN_OWNER_ID);
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Long.valueOf(getId()));
        contentValues.put(COLUMN_CREATE_TIME, Long.valueOf(getCreateTime()));
        contentValues.put(COLUMN_MODIFIED_TIME, Long.valueOf(getModifiedTime()));
        contentValues.put(COLUMN_OWNER_ID, getOwnerId());
        return contentValues;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long j) {
        this.createTime = j;
    }

    public long getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(long j) {
        this.modifiedTime = j;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String str) {
        this.ownerId = str;
    }
}
