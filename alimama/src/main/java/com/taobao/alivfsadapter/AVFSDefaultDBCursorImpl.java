package com.taobao.alivfsadapter;

import android.database.Cursor;

public class AVFSDefaultDBCursorImpl extends AVFSDBCursor {
    public Cursor mCursor;

    public void close() {
        this.mCursor.close();
    }

    public boolean next() {
        return this.mCursor.moveToNext();
    }

    public int getInt(int i) {
        return this.mCursor.getInt(i);
    }

    public int getInt(String str) {
        return this.mCursor.getInt(this.mCursor.getColumnIndex(str));
    }

    public long getLong(int i) {
        return this.mCursor.getLong(i);
    }

    public long getLong(String str) {
        return this.mCursor.getLong(this.mCursor.getColumnIndex(str));
    }

    public double getDouble(int i) {
        return this.mCursor.getDouble(i);
    }

    public double getDouble(String str) {
        return this.mCursor.getDouble(this.mCursor.getColumnIndex(str));
    }

    public String getString(int i) {
        return this.mCursor.getString(i);
    }

    public String getString(String str) {
        return this.mCursor.getString(this.mCursor.getColumnIndex(str));
    }

    public byte[] getBytes(int i) {
        return this.mCursor.getBlob(i);
    }

    public byte[] getBytes(String str) {
        return this.mCursor.getBlob(this.mCursor.getColumnIndex(str));
    }

    public int getType(int i) {
        return this.mCursor.getType(i);
    }

    public int getType(String str) {
        return this.mCursor.getType(this.mCursor.getColumnIndex(str));
    }

    public int getColumnIndex(String str) {
        return this.mCursor.getColumnIndex(str);
    }

    public String getColumnName(int i) {
        return this.mCursor.getColumnName(i);
    }

    public int getColumnCount() {
        return this.mCursor.getColumnCount();
    }
}
