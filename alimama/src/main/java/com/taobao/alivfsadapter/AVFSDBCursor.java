package com.taobao.alivfsadapter;

public abstract class AVFSDBCursor {
    public abstract void close();

    public abstract byte[] getBytes(int i);

    public abstract byte[] getBytes(String str);

    public abstract int getColumnCount();

    public abstract int getColumnIndex(String str);

    public abstract String getColumnName(int i);

    public abstract double getDouble(int i);

    public abstract double getDouble(String str);

    public abstract int getInt(int i);

    public abstract int getInt(String str);

    public abstract long getLong(int i);

    public abstract long getLong(String str);

    public abstract String getString(int i);

    public abstract String getString(String str);

    public abstract int getType(int i);

    public abstract int getType(String str);

    public abstract boolean next();
}
