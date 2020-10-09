package com.ta.audid.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ta.audid.utils.UtdidLogger;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private DelayCloseDbTask mCloseDbTask = new DelayCloseDbTask();
    /* access modifiers changed from: private */
    public AtomicInteger mWritableCounter = new AtomicInteger();
    /* access modifiers changed from: private */
    public SQLiteDatabase mWritableDatabase;
    private Future<?> mcloseFuture;

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SqliteHelper(Context context, String str) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, 2);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [java.lang.String[], android.database.Cursor] */
    public void onOpen(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor = 0;
        try {
            cursor = sQLiteDatabase.rawQuery("PRAGMA journal_mode=DELETE", cursor);
        } catch (Throwable unused) {
        } finally {
            closeCursor(cursor);
        }
        super.onOpen(sQLiteDatabase);
    }

    public synchronized SQLiteDatabase getWritableDatabase() {
        try {
            if (this.mWritableDatabase == null) {
                this.mWritableDatabase = super.getWritableDatabase();
            }
            this.mWritableCounter.incrementAndGet();
        } catch (Throwable th) {
            UtdidLogger.w("TAG", "e", th);
        }
        return this.mWritableDatabase;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void closeWritableDatabase(android.database.sqlite.SQLiteDatabase r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            if (r5 != 0) goto L_0x0005
            monitor-exit(r4)
            return
        L_0x0005:
            java.util.concurrent.atomic.AtomicInteger r5 = r4.mWritableCounter     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            int r5 = r5.decrementAndGet()     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            if (r5 != 0) goto L_0x002a
            java.util.concurrent.Future<?> r5 = r4.mcloseFuture     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            if (r5 == 0) goto L_0x0017
            java.util.concurrent.Future<?> r5 = r4.mcloseFuture     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            r0 = 0
            r5.cancel(r0)     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
        L_0x0017:
            com.ta.audid.utils.TaskExecutor r5 = com.ta.audid.utils.TaskExecutor.getInstance()     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            r0 = 0
            com.ta.audid.db.SqliteHelper$DelayCloseDbTask r1 = r4.mCloseDbTask     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            r2 = 30000(0x7530, double:1.4822E-319)
            java.util.concurrent.ScheduledFuture r5 = r5.schedule(r0, r1, r2)     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            r4.mcloseFuture = r5     // Catch:{ Throwable -> 0x002a, all -> 0x0027 }
            goto L_0x002a
        L_0x0027:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        L_0x002a:
            monitor-exit(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.db.SqliteHelper.closeWritableDatabase(android.database.sqlite.SQLiteDatabase):void");
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable unused) {
            }
        }
    }

    class DelayCloseDbTask implements Runnable {
        DelayCloseDbTask() {
        }

        public void run() {
            synchronized (SqliteHelper.this) {
                if (SqliteHelper.this.mWritableCounter.get() == 0 && SqliteHelper.this.mWritableDatabase != null) {
                    SqliteHelper.this.mWritableDatabase.close();
                    SQLiteDatabase unused = SqliteHelper.this.mWritableDatabase = null;
                }
            }
        }
    }
}
