package com.alibaba.ut.abtest.internal.debug;

import android.content.ContentValues;
import android.database.Cursor;
import com.alibaba.ut.abtest.internal.database.ABDataObject;

public class DebugDO extends ABDataObject {
    public static final String COLUMN_DEBUG_KEY = "debug_key";
    public static final String COLUMN_EXPIRED_TIME = "expired_time";
    private static final long serialVersionUID = -8717474586033072944L;
    private String debugKey;
    private long expiredTime;

    public DebugDO() {
    }

    public DebugDO(Cursor cursor) {
        super(cursor);
        this.debugKey = getCursorString(cursor, "debug_key");
        this.expiredTime = getCursorLong(cursor, COLUMN_EXPIRED_TIME);
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = super.toContentValues();
        contentValues.put("debug_key", getDebugKey());
        contentValues.put(COLUMN_EXPIRED_TIME, Long.valueOf(getExpiredTime()));
        return contentValues;
    }

    public String getDebugKey() {
        return this.debugKey;
    }

    public void setDebugKey(String str) {
        this.debugKey = str;
    }

    public long getExpiredTime() {
        return this.expiredTime;
    }

    public void setExpiredTime(long j) {
        this.expiredTime = j;
    }
}
