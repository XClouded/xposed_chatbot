package com.alibaba.ut.abtest.internal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.bucketing.ExperimentGroupDao;
import com.alibaba.ut.abtest.internal.util.LogUtils;

class ABDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "ABDatabaseHelper";

    protected ABDatabaseHelper(Context context) {
        super(context, ABConstants.Database.DB_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        LogUtils.logD(TAG, "onCreate, db=" + sQLiteDatabase);
        try {
            ExperimentGroupDao.createTable(sQLiteDatabase, true);
        } catch (Exception e) {
            LogUtils.logE(TAG, "Failed to create database tables", e);
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        LogUtils.logD(TAG, "onUpgrade, db=" + sQLiteDatabase + ", oldVersion=" + i + ", newVersion=" + i2);
        try {
            ExperimentGroupDao.dropTable(sQLiteDatabase, true);
            onCreate(sQLiteDatabase);
        } catch (Exception e) {
            LogUtils.logE(TAG, "Failed to change database tables", e);
        }
    }
}
