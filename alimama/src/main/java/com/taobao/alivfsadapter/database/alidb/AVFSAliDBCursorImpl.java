package com.taobao.alivfsadapter.database.alidb;

import com.taobao.alivfsadapter.AVFSDBCursor;
import com.taobao.android.alivfsdb.AliResultSet;

public class AVFSAliDBCursorImpl extends AVFSDBCursor {
    public AliResultSet resultSet;

    public void close() {
        if (this.resultSet != null) {
            this.resultSet.close();
        }
    }

    public boolean next() {
        if (this.resultSet == null) {
            return false;
        }
        return this.resultSet.next();
    }

    public int getInt(int i) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getInt(i);
    }

    public int getInt(String str) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getInt(str);
    }

    public long getLong(int i) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getLong(i);
    }

    public long getLong(String str) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getLong(str);
    }

    public double getDouble(int i) {
        if (this.resultSet == null) {
            return -1.0d;
        }
        return this.resultSet.getDouble(i);
    }

    public double getDouble(String str) {
        if (this.resultSet == null) {
            return -1.0d;
        }
        return this.resultSet.getDouble(str);
    }

    public String getString(int i) {
        if (this.resultSet == null) {
            return null;
        }
        return this.resultSet.getString(i);
    }

    public String getString(String str) {
        if (this.resultSet == null) {
            return null;
        }
        return this.resultSet.getString(str);
    }

    public byte[] getBytes(int i) {
        if (this.resultSet == null) {
            return null;
        }
        return this.resultSet.getBytes(i);
    }

    public byte[] getBytes(String str) {
        if (this.resultSet == null) {
            return null;
        }
        return this.resultSet.getBytes(str);
    }

    public int getType(int i) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getType(i);
    }

    public int getType(String str) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getType(str);
    }

    public int getColumnIndex(String str) {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getColumnIndex(str);
    }

    public String getColumnName(int i) {
        if (this.resultSet == null) {
            return null;
        }
        return this.resultSet.getColumnName(i);
    }

    public int getColumnCount() {
        if (this.resultSet == null) {
            return -1;
        }
        return this.resultSet.getColumnCount();
    }
}
