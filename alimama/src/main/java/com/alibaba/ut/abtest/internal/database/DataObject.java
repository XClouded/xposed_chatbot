package com.alibaba.ut.abtest.internal.database;

import android.content.ContentValues;
import android.database.Cursor;
import java.io.Serializable;

public abstract class DataObject implements Serializable {
    public abstract ContentValues toContentValues();

    /* access modifiers changed from: protected */
    public long getCursorLong(Cursor cursor, String str) {
        return getCursorLong(cursor, str, 0);
    }

    /* access modifiers changed from: protected */
    public long getCursorLong(Cursor cursor, String str, long j) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex >= 0 ? cursor.getLong(columnIndex) : j;
    }

    /* access modifiers changed from: protected */
    public int getCursorInt(Cursor cursor, String str) {
        return getCursorInt(cursor, str, 0);
    }

    /* access modifiers changed from: protected */
    public int getCursorInt(Cursor cursor, String str, int i) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex >= 0 ? cursor.getInt(columnIndex) : i;
    }

    /* access modifiers changed from: protected */
    public String getCursorString(Cursor cursor, String str) {
        return getCursorString(cursor, str, (String) null);
    }

    /* access modifiers changed from: protected */
    public String getCursorString(Cursor cursor, String str, String str2) {
        int columnIndex = cursor.getColumnIndex(str);
        return columnIndex >= 0 ? cursor.getString(columnIndex) : str2;
    }
}
