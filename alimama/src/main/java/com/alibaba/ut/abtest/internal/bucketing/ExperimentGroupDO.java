package com.alibaba.ut.abtest.internal.bucketing;

import android.content.ContentValues;
import android.database.Cursor;
import com.alibaba.ut.abtest.internal.database.ABDataObject;

public class ExperimentGroupDO extends ABDataObject {
    public static final String COLUMN_BEGIN_TIME = "begin_time";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_END_TIME = "end_time";
    public static final String COLUMN_HIT_COUNT = "hit_count";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_TYPE = "type";
    private static final long serialVersionUID = 6826871722970166508L;
    private long beginTime;
    private String data;
    private long endTime;
    private int hitCount;
    private String key;
    private int type;

    public ExperimentGroupDO() {
    }

    public ExperimentGroupDO(Cursor cursor) {
        super(cursor);
        this.key = getCursorString(cursor, "key");
        this.type = getCursorInt(cursor, "type");
        this.beginTime = getCursorLong(cursor, COLUMN_BEGIN_TIME);
        this.endTime = getCursorLong(cursor, COLUMN_END_TIME);
        this.data = getCursorString(cursor, "data");
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = super.toContentValues();
        contentValues.put("key", this.key);
        contentValues.put("type", Integer.valueOf(this.type));
        contentValues.put(COLUMN_BEGIN_TIME, Long.valueOf(this.beginTime));
        contentValues.put(COLUMN_END_TIME, Long.valueOf(this.endTime));
        contentValues.put("data", this.data);
        return contentValues;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(long j) {
        this.beginTime = j;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long j) {
        this.endTime = j;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String str) {
        this.data = str;
    }
}
