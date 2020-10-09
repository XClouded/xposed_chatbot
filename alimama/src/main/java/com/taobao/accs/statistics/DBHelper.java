package com.taobao.accs.statistics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DBHelper extends SQLiteOpenHelper {
    private static final int MAX_DB_COUNT = 4000;
    private static final int MAX_SQL_NUM = 5;
    private static final String TAG = "DBHelper";
    private static final Lock lock = new ReentrantLock();
    private static volatile DBHelper sInstance;
    LinkedList<SQLObject> cachedSql = new LinkedList<>();
    public int curLogsCount = 0;
    private Context mContext;

    public SQLiteDatabase getWritableDatabase() {
        if (!AdapterUtilityImpl.checkIsWritable(super.getWritableDatabase().getPath(), 102400)) {
            return null;
        }
        return super.getWritableDatabase();
    }

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DBHelper.class) {
                if (sInstance == null) {
                    sInstance = new DBHelper(context, Constants.DB_NAME, (SQLiteDatabase.CursorFactory) null, 3);
                }
            }
        }
        return sInstance;
    }

    private DBHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
        this.mContext = context;
    }

    public final void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            if (lock.tryLock()) {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS traffic(_id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, host TEXT,serviceid TEXT, bid TEXT, isbackground TEXT, size TEXT)");
            }
        } finally {
            lock.unlock();
        }
    }

    public final void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i < i2) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS service");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS network");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ping");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS msg");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ack");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS election");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS bindApp");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS bindUser");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS traffic");
            onCreate(sQLiteDatabase);
        }
    }

    public void onTraffics(String str, String str2, String str3, boolean z, long j, String str4) {
        if (!checkTrafficsExist(str, str2, str3, z, j, str4)) {
            execSQL("INSERT INTO traffic VALUES(null,?,?,?,?,?,?)", new Object[]{str4, str, str2, str3, String.valueOf(z), Long.valueOf(j)}, true);
            return;
        }
        execSQL("UPDATE traffic SET size=? WHERE date=? AND host=? AND bid=? AND isbackground=?", new Object[]{Long.valueOf(j), str4, str, str3, String.valueOf(z)}, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0063, code lost:
        return true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0080 A[SYNTHETIC, Splitter:B:32:0x0080] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0087 A[SYNTHETIC, Splitter:B:37:0x0087] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized boolean checkTrafficsExist(java.lang.String r15, java.lang.String r16, java.lang.String r17, boolean r18, long r19, java.lang.String r21) {
        /*
            r14 = this;
            monitor-enter(r14)
            r1 = 0
            r2 = 0
            android.database.sqlite.SQLiteDatabase r3 = r14.getWritableDatabase()     // Catch:{ Exception -> 0x0072 }
            if (r3 != 0) goto L_0x000b
            monitor-exit(r14)
            return r2
        L_0x000b:
            java.lang.String r4 = "traffic"
            r0 = 7
            java.lang.String[] r5 = new java.lang.String[r0]     // Catch:{ Exception -> 0x0072 }
            java.lang.String r0 = "_id"
            r5[r2] = r0     // Catch:{ Exception -> 0x0072 }
            java.lang.String r0 = "date"
            r12 = 1
            r5[r12] = r0     // Catch:{ Exception -> 0x0072 }
            java.lang.String r0 = "host"
            r6 = 2
            r5[r6] = r0     // Catch:{ Exception -> 0x0072 }
            java.lang.String r0 = "serviceid"
            r7 = 3
            r5[r7] = r0     // Catch:{ Exception -> 0x0072 }
            java.lang.String r0 = "bid"
            r8 = 4
            r5[r8] = r0     // Catch:{ Exception -> 0x0072 }
            r0 = 5
            java.lang.String r9 = "isbackground"
            r5[r0] = r9     // Catch:{ Exception -> 0x0072 }
            r0 = 6
            java.lang.String r9 = "size"
            r5[r0] = r9     // Catch:{ Exception -> 0x0072 }
            java.lang.String r0 = "date=? AND host=? AND bid=? AND isbackground=?"
            java.lang.String[] r8 = new java.lang.String[r8]     // Catch:{ Exception -> 0x0072 }
            r8[r2] = r21     // Catch:{ Exception -> 0x0072 }
            r8[r12] = r15     // Catch:{ Exception -> 0x0072 }
            r8[r6] = r17     // Catch:{ Exception -> 0x0072 }
            java.lang.String r6 = java.lang.String.valueOf(r18)     // Catch:{ Exception -> 0x0072 }
            r8[r7] = r6     // Catch:{ Exception -> 0x0072 }
            r9 = 0
            r10 = 0
            r11 = 0
            r6 = 100
            java.lang.String r13 = java.lang.String.valueOf(r6)     // Catch:{ Exception -> 0x0072 }
            r6 = r0
            r7 = r8
            r8 = r9
            r9 = r10
            r10 = r11
            r11 = r13
            android.database.Cursor r3 = r3.query(r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x0072 }
            if (r3 == 0) goto L_0x006a
            int r0 = r3.getCount()     // Catch:{ Exception -> 0x0067, all -> 0x0064 }
            if (r0 <= 0) goto L_0x006a
            if (r3 == 0) goto L_0x0062
            r3.close()     // Catch:{ all -> 0x008b }
        L_0x0062:
            monitor-exit(r14)
            return r12
        L_0x0064:
            r0 = move-exception
            r1 = r3
            goto L_0x0085
        L_0x0067:
            r0 = move-exception
            r1 = r3
            goto L_0x0073
        L_0x006a:
            if (r3 == 0) goto L_0x0083
            r3.close()     // Catch:{ all -> 0x008b }
            goto L_0x0083
        L_0x0070:
            r0 = move-exception
            goto L_0x0085
        L_0x0072:
            r0 = move-exception
        L_0x0073:
            java.lang.String r3 = "DBHelper"
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0070 }
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ all -> 0x0070 }
            com.taobao.accs.utl.ALog.w(r3, r0, r4)     // Catch:{ all -> 0x0070 }
            if (r1 == 0) goto L_0x0083
            r1.close()     // Catch:{ all -> 0x008b }
        L_0x0083:
            monitor-exit(r14)
            return r2
        L_0x0085:
            if (r1 == 0) goto L_0x008d
            r1.close()     // Catch:{ all -> 0x008b }
            goto L_0x008d
        L_0x008b:
            r0 = move-exception
            goto L_0x008e
        L_0x008d:
            throw r0     // Catch:{ all -> 0x008b }
        L_0x008e:
            monitor-exit(r14)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.statistics.DBHelper.checkTrafficsExist(java.lang.String, java.lang.String, java.lang.String, boolean, long, java.lang.String):boolean");
    }

    public void clearTraffics() {
        execSQL("DELETE FROM traffic", (Object[]) null, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0069, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006a, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b6, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0108, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0109, code lost:
        r4 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x0126, code lost:
        r4.close();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:13:0x001d, B:21:0x0076] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0069 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:13:0x001d] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x011f A[SYNTHETIC, Splitter:B:57:0x011f] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0126  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<com.taobao.accs.ut.monitor.TrafficsMonitor.TrafficInfo> getTraffics(boolean r20) {
        /*
            r19 = this;
            monitor-enter(r19)
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x012a }
            r1.<init>()     // Catch:{ all -> 0x012a }
            r2 = 0
            r3 = 0
            android.database.sqlite.SQLiteDatabase r4 = r19.getWritableDatabase()     // Catch:{ Exception -> 0x010f }
            if (r4 != 0) goto L_0x0010
            monitor-exit(r19)     // Catch:{ all -> 0x012a }
            return r2
        L_0x0010:
            r0 = 100
            r5 = 7
            r13 = 6
            r14 = 5
            r12 = 4
            r11 = 3
            r10 = 2
            r9 = 1
            if (r20 == 0) goto L_0x0071
            java.lang.String r6 = "traffic"
            java.lang.String[] r7 = new java.lang.String[r5]     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "_id"
            r7[r3] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "date"
            r7[r9] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "host"
            r7[r10] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "serviceid"
            r7[r11] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "bid"
            r7[r12] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "isbackground"
            r7[r14] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r5 = "size"
            r7[r13] = r5     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r8 = "date=?"
            java.lang.String[] r5 = new java.lang.String[r9]     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            long r15 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            java.lang.String r15 = com.taobao.accs.utl.UtilityImpl.formatDay(r15)     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            r5[r3] = r15     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            r15 = 0
            r16 = 0
            r17 = 0
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            r18 = r5
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r18
            r2 = 1
            r9 = r15
            r13 = 2
            r10 = r16
            r14 = 3
            r11 = r17
            r12 = r0
            android.database.Cursor r0 = r4.query(r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x006d, all -> 0x0069 }
            r4 = r0
            r3 = 4
            goto L_0x00ae
        L_0x0069:
            r0 = move-exception
            r4 = 0
            goto L_0x0124
        L_0x006d:
            r0 = move-exception
            r2 = 0
            goto L_0x0111
        L_0x0071:
            r2 = 1
            r13 = 2
            r14 = 3
            java.lang.String r6 = "traffic"
            java.lang.String[] r7 = new java.lang.String[r5]     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "_id"
            r7[r3] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "date"
            r7[r2] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "host"
            r7[r13] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "serviceid"
            r7[r14] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "bid"
            r12 = 4
            r7[r12] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "isbackground"
            r8 = 5
            r7[r8] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            java.lang.String r5 = "size"
            r8 = 6
            r7[r8] = r5     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r15 = 0
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            r5 = r6
            r6 = r7
            r7 = r8
            r8 = r9
            r9 = r10
            r10 = r11
            r11 = r15
            r3 = 4
            r12 = r0
            android.database.Cursor r0 = r4.query(r5, r6, r7, r8, r9, r10, r11, r12)     // Catch:{ Exception -> 0x0108, all -> 0x0069 }
            r4 = r0
        L_0x00ae:
            if (r4 != 0) goto L_0x00b8
            if (r4 == 0) goto L_0x00b5
            r4.close()     // Catch:{ all -> 0x012a }
        L_0x00b5:
            monitor-exit(r19)     // Catch:{ all -> 0x012a }
            r4 = 0
            return r4
        L_0x00b8:
            boolean r0 = r4.moveToFirst()     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            if (r0 == 0) goto L_0x00fe
        L_0x00be:
            java.lang.String r6 = r4.getString(r2)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            java.lang.String r10 = r4.getString(r13)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            java.lang.String r8 = r4.getString(r14)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            java.lang.String r7 = r4.getString(r3)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            r0 = 5
            java.lang.String r5 = r4.getString(r0)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            boolean r9 = r5.booleanValue()     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            r11 = 6
            long r15 = r4.getLong(r11)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            if (r7 == 0) goto L_0x00f6
            r17 = 0
            int r5 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
            if (r5 <= 0) goto L_0x00f6
            com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo r12 = new com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            r5 = r12
            r0 = r12
            r17 = 6
            r11 = r15
            r5.<init>(r6, r7, r8, r9, r10, r11)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            r1.add(r0)     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            goto L_0x00f8
        L_0x00f6:
            r17 = 6
        L_0x00f8:
            boolean r0 = r4.moveToNext()     // Catch:{ Exception -> 0x0106, all -> 0x0104 }
            if (r0 != 0) goto L_0x00be
        L_0x00fe:
            if (r4 == 0) goto L_0x0122
            r4.close()     // Catch:{ all -> 0x012a }
            goto L_0x0122
        L_0x0104:
            r0 = move-exception
            goto L_0x0124
        L_0x0106:
            r0 = move-exception
            goto L_0x010a
        L_0x0108:
            r0 = move-exception
            r4 = 0
        L_0x010a:
            r2 = r4
            goto L_0x0111
        L_0x010c:
            r0 = move-exception
            r4 = r2
            goto L_0x0124
        L_0x010f:
            r0 = move-exception
            r4 = r2
        L_0x0111:
            java.lang.String r3 = "DBHelper"
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x010c }
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x010c }
            com.taobao.accs.utl.ALog.w(r3, r0, r4)     // Catch:{ all -> 0x010c }
            if (r2 == 0) goto L_0x0122
            r2.close()     // Catch:{ all -> 0x012a }
        L_0x0122:
            monitor-exit(r19)     // Catch:{ all -> 0x012a }
            return r1
        L_0x0124:
            if (r4 == 0) goto L_0x0129
            r4.close()     // Catch:{ all -> 0x012a }
        L_0x0129:
            throw r0     // Catch:{ all -> 0x012a }
        L_0x012a:
            r0 = move-exception
            monitor-exit(r19)     // Catch:{ all -> 0x012a }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.statistics.DBHelper.getTraffics(boolean):java.util.List");
    }

    private synchronized void execSQL(String str, Object[] objArr, boolean z) {
        this.cachedSql.add(new SQLObject(str, objArr));
        if (this.cachedSql.size() > 5 || z) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            if (writableDatabase != null) {
                while (true) {
                    try {
                        if (this.cachedSql.size() > 0) {
                            SQLObject removeFirst = this.cachedSql.removeFirst();
                            if (removeFirst.args != null) {
                                writableDatabase.execSQL(removeFirst.sql, removeFirst.args);
                            } else {
                                writableDatabase.execSQL(removeFirst.sql);
                            }
                            if (removeFirst.sql.contains("INSERT")) {
                                this.curLogsCount++;
                                if (this.curLogsCount > 4000) {
                                    ALog.d(TAG, "db is full!", new Object[0]);
                                    onUpgrade(writableDatabase, 0, 1);
                                    this.curLogsCount = 0;
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        ALog.d(TAG, e.toString(), new Object[0]);
                    } catch (Throwable th) {
                        writableDatabase.close();
                        throw th;
                    }
                }
                writableDatabase.close();
            } else {
                return;
            }
        }
        return;
    }

    private class SQLObject {
        Object[] args;
        String sql;

        private SQLObject(String str, Object[] objArr) {
            this.sql = str;
            this.args = objArr;
        }
    }
}
