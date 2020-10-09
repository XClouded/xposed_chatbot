package com.alibaba.ut.abtest.internal.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.taobao.weex.common.Constants;
import java.io.Closeable;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Database implements Closeable {
    private static final String TAG = "Database";
    private final SQLiteOpenHelper helper;

    public Database(SQLiteOpenHelper sQLiteOpenHelper) {
        this.helper = sQLiteOpenHelper;
    }

    public SQLiteDatabase getReadableDatabase() {
        return this.helper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDatabase() {
        return this.helper.getWritableDatabase();
    }

    public void close() {
        this.helper.close();
    }

    public long[] insertInTx(String str, List<ContentValues> list) {
        return insertInTx(str, 0, list);
    }

    public long[] insertInTx(String str, ContentValues... contentValuesArr) {
        return insertInTx(str, 0, (List<ContentValues>) Arrays.asList(contentValuesArr));
    }

    public long[] insertInTx(String str, int i, List<ContentValues> list) {
        return executeInsertInTx(str, i, list);
    }

    public long[] insertInTx(String str, int i, ContentValues... contentValuesArr) {
        return executeInsertInTx(str, i, Arrays.asList(contentValuesArr));
    }

    /* JADX INFO: finally extract failed */
    private long[] executeInsertInTx(String str, int i, List<ContentValues> list) {
        LogUtils.logD(TAG, "INSERT in transaction. table: " + str + ", values: " + list + ", conflictAlgorithm: " + conflictString(i));
        long[] jArr = new long[list.size()];
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        int i2 = 0;
        while (i2 < list.size()) {
            try {
                jArr[i2] = writableDatabase.insertWithOnConflict(str, (String) null, list.get(i2), i);
                i2++;
            } catch (Throwable th) {
                writableDatabase.endTransaction();
                throw th;
            }
        }
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        LogUtils.logD(TAG, "INSERT ids: " + Arrays.toString(jArr));
        return jArr;
    }

    public long insert(String str, ContentValues contentValues) {
        return insert(str, contentValues, 0);
    }

    public long insert(String str, ContentValues contentValues, int i) {
        return executeInsert(str, contentValues, i);
    }

    private long executeInsert(String str, ContentValues contentValues, int i) {
        long j;
        LogUtils.logD(TAG, "INSERT table: " + str + ", values: " + contentValues + ", conflictAlgorithm: " + conflictString(i));
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase.isDbLockedByCurrentThread()) {
            j = insertInsideTx(writableDatabase, str, contentValues, i);
        } else {
            writableDatabase.beginTransaction();
            try {
                j = insertInsideTx(writableDatabase, str, contentValues, i);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        }
        LogUtils.logD(TAG, "INSERT id: " + j);
        return j;
    }

    private long insertInsideTx(SQLiteDatabase sQLiteDatabase, String str, ContentValues contentValues, int i) {
        return sQLiteDatabase.insertWithOnConflict(str, (String) null, contentValues, i);
    }

    public int delete(String str, String str2, String... strArr) {
        int i;
        LogUtils.logD(TAG, "DELETE table: " + str + " whereClause: " + str2 + "  whereArgs: " + Arrays.toString(strArr));
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase.isDbLockedByCurrentThread()) {
            i = writableDatabase.delete(str, str2, strArr);
        } else {
            writableDatabase.beginTransaction();
            try {
                i = writableDatabase.delete(str, str2, strArr);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE affected ");
        sb.append(i);
        sb.append(i != 1 ? " rows" : " row");
        LogUtils.logD(TAG, sb.toString());
        return i;
    }

    public int[] deleteInTx(String str, List<String> list, List<String[]> list2) {
        LogUtils.logD(TAG, "DELETE in transaction. table: " + str + " whereClauses: " + list + "  whereArgs: " + list2);
        if (list == null || list2.isEmpty()) {
            throw new IllegalArgumentException("The whereClauses can not be empty");
        }
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int[] iArr = new int[list.size()];
        writableDatabase.beginTransaction();
        int i = 0;
        while (i < list.size()) {
            try {
                String str2 = list.get(i);
                String[] strArr = null;
                if (list2 != null) {
                    strArr = list2.get(i);
                }
                iArr[i] = writableDatabase.delete(str, str2, strArr);
                i++;
            } finally {
                writableDatabase.endTransaction();
            }
        }
        writableDatabase.setTransactionSuccessful();
        return iArr;
    }

    public int update(String str, ContentValues contentValues, String str2, String... strArr) {
        return update(str, contentValues, 0, str2, strArr);
    }

    public int update(String str, ContentValues contentValues, int i, String str2, String... strArr) {
        return executeUpdate(str, contentValues, i, str2, strArr);
    }

    private int executeUpdate(String str, ContentValues contentValues, int i, String str2, String... strArr) {
        int i2;
        LogUtils.logD(TAG, "UPDATE table: " + str + " values: " + contentValues + " whereClause: " + str2 + "  whereArgs: " + Arrays.toString(strArr) + "  conflictAlgorithm: " + conflictString(i));
        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (writableDatabase.isDbLockedByCurrentThread()) {
            i2 = writableDatabase.updateWithOnConflict(str, contentValues, str2, strArr, i);
        } else {
            writableDatabase.beginTransaction();
            try {
                i2 = writableDatabase.updateWithOnConflict(str, contentValues, str2, strArr, i);
                writableDatabase.setTransactionSuccessful();
            } finally {
                writableDatabase.endTransaction();
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE affected ");
        sb.append(i2);
        sb.append(i2 != 1 ? " rows" : " row");
        LogUtils.logD(TAG, sb.toString());
        return i2;
    }

    public int[] updateInTx(String str, List<ContentValues> list, List<String> list2, List<String[]> list3) {
        return updateInTx(str, list, 0, list2, list3);
    }

    public int[] updateInTx(String str, List<ContentValues> list, int i, List<String> list2, List<String[]> list3) {
        return executeUpdateInTx(str, list, i, list2, list3);
    }

    private int[] executeUpdateInTx(String str, List<ContentValues> list, int i, List<String> list2, List<String[]> list3) {
        LogUtils.logD(TAG, "UPDATE in transaction table: " + str + " values: " + list + " whereClauses: " + list2 + "  whereArgs: " + list3 + "  conflictAlgorithm: " + conflictString(i));
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int[] iArr = new int[list.size()];
        writableDatabase.beginTransaction();
        int i2 = 0;
        while (i2 < list.size()) {
            try {
                ContentValues contentValues = list.get(i2);
                String str2 = list2.get(i2);
                String[] strArr = null;
                if (list3 != null) {
                    strArr = list3.get(i2);
                }
                iArr[i2] = writableDatabase.updateWithOnConflict(str, contentValues, str2, strArr, i);
                i2++;
            } finally {
                writableDatabase.endTransaction();
            }
        }
        writableDatabase.setTransactionSuccessful();
        return iArr;
    }

    public void execute(String str) {
        LogUtils.logD(TAG, "EXECUTE  sql: " + str);
        getWritableDatabase().execSQL(str);
    }

    public void execute(String str, Object... objArr) {
        LogUtils.logD(TAG, "EXECUTE sql: " + str + " args: " + Arrays.toString(objArr));
        if (objArr == null || objArr.length == 0) {
            getWritableDatabase().execSQL(str);
        } else {
            getWritableDatabase().execSQL(str, objArr);
        }
    }

    public Cursor query(String str, String... strArr) {
        long nanoTime = System.nanoTime();
        Cursor rawQuery = getReadableDatabase().rawQuery(str, strArr);
        long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - nanoTime);
        LogUtils.logD(TAG, "QUERY (" + millis + "ms) sql: " + str + " args: " + Arrays.toString(strArr));
        return rawQuery;
    }

    public Cursor query(String str, String[] strArr, String str2, String str3, String str4, String... strArr2) {
        return query(str, strArr, (String) null, (String) null, str2, str3, str4, strArr2);
    }

    public Cursor query(String str, String[] strArr, String str2, String str3, String str4, String str5, String str6, String... strArr2) {
        long nanoTime = System.nanoTime();
        Cursor query = getReadableDatabase().query(str, strArr, str6, strArr2, str2, str3, str4, str5);
        long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - nanoTime);
        StringBuilder sb = new StringBuilder();
        sb.append("QUERY (");
        sb.append(millis);
        sb.append("ms) table: ");
        String str7 = str;
        sb.append(str);
        sb.append(", columns: ");
        sb.append(Arrays.toString(strArr));
        sb.append(", orderBy: ");
        sb.append(str4);
        sb.append(", limit: ");
        sb.append(str5);
        sb.append(", groupBy: ");
        String str8 = str2;
        sb.append(str2);
        sb.append(", having: ");
        String str9 = str3;
        sb.append(str3);
        sb.append(", whereClause: ");
        sb.append(str6);
        sb.append(", whereArgs: ");
        sb.append(Arrays.toString(strArr2));
        LogUtils.logD(TAG, sb.toString());
        return query;
    }

    private static String conflictString(int i) {
        switch (i) {
            case 0:
                return "none";
            case 1:
                return "rollback";
            case 2:
                return "abort";
            case 3:
                return Constants.Event.FAIL;
            case 4:
                return ABConstants.Operator.NAV_LOOPBACK_VALUE_IGNORE;
            case 5:
                return "replace";
            default:
                return "unknown (" + i + ')';
        }
    }
}
