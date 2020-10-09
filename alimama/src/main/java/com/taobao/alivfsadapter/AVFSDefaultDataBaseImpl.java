package com.taobao.alivfsadapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.taobao.alivfsadapter.AVFSDataBase;
import com.taobao.alivfsadapter.utils.AVFSApplicationUtils;

public class AVFSDefaultDataBaseImpl extends AVFSDataBase {
    private final SQLiteDatabase mDB;

    public AVFSDefaultDataBaseImpl(String str, int i) {
        this(str, (String) null, i);
    }

    public AVFSDefaultDataBaseImpl(String str, String str2, int i) {
        super(str, str2, i);
        this.mDB = new MySQLiteOpenHelper(AVFSApplicationUtils.getApplication(), str, (SQLiteDatabase.CursorFactory) null, i).getWritableDatabase();
    }

    public AVFSDBCursor execQuery(String str) throws Exception {
        Cursor rawQuery = this.mDB.rawQuery(str, (String[]) null);
        AVFSDefaultDBCursorImpl aVFSDefaultDBCursorImpl = new AVFSDefaultDBCursorImpl();
        aVFSDefaultDBCursorImpl.mCursor = rawQuery;
        return aVFSDefaultDBCursorImpl;
    }

    public AVFSDBCursor execQuery(String str, Object[] objArr) throws Exception {
        Cursor rawQuery = this.mDB.rawQuery(str, toStringArray(objArr));
        AVFSDefaultDBCursorImpl aVFSDefaultDBCursorImpl = new AVFSDefaultDBCursorImpl();
        aVFSDefaultDBCursorImpl.mCursor = rawQuery;
        return aVFSDefaultDBCursorImpl;
    }

    public boolean execUpdate(String str) throws Exception {
        this.mDB.execSQL(str);
        return true;
    }

    public boolean execUpdate(String str, Object[] objArr) throws Exception {
        this.mDB.execSQL(str, objArr);
        return true;
    }

    public void execUpdate(String str, AVFSDataBase.IExecCallback iExecCallback) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void execUpdate(String str, Object[] objArr, AVFSDataBase.IExecCallback iExecCallback) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    private class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public void onCreate(SQLiteDatabase sQLiteDatabase) {
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }

        public MySQLiteOpenHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
            super(context, str, cursorFactory, i);
        }

        public MySQLiteOpenHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i, DatabaseErrorHandler databaseErrorHandler) {
            super(context, str, cursorFactory, i, databaseErrorHandler);
        }
    }

    private String[] toStringArray(Object[] objArr) {
        String[] strArr = new String[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            strArr[i] = objArr[i].toString();
        }
        return strArr;
    }

    public int close() {
        this.mDB.close();
        return 0;
    }
}
