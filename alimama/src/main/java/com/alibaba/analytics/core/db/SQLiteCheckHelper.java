package com.alibaba.analytics.core.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alibaba.analytics.utils.Logger;
import java.io.File;
import java.io.FileFilter;

public class SQLiteCheckHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String TAG = "SQLiteCheckHelper";
    private SQLiteDatabase mWritableDatabase;

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public SQLiteCheckHelper(Context context, String str) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, 2, (DatabaseErrorHandler) null);
    }

    public void checkIntegrity() {
        getWritableDatabase();
        closeWritableDatabase();
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
                boolean isDatabaseIntegrityOk = this.mWritableDatabase.isDatabaseIntegrityOk();
                Logger.d(TAG, "isDatabaseIntegrityOk", Boolean.valueOf(isDatabaseIntegrityOk));
                if (!isDatabaseIntegrityOk) {
                    Logger.e(TAG, "delete Database", Boolean.valueOf(deleteDatabase(new File(this.mWritableDatabase.getPath()))));
                }
                Logger.d(TAG, "WritableDatabase", this.mWritableDatabase);
            }
        } catch (Throwable th) {
            Logger.w("TAG", "e", th);
        }
        return this.mWritableDatabase;
    }

    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x000d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void closeWritableDatabase() {
        /*
            r2 = this;
            monitor-enter(r2)
            android.database.sqlite.SQLiteDatabase r0 = r2.mWritableDatabase     // Catch:{ all -> 0x0016 }
            if (r0 != 0) goto L_0x0007
            monitor-exit(r2)
            return
        L_0x0007:
            r0 = 0
            android.database.sqlite.SQLiteDatabase r1 = r2.mWritableDatabase     // Catch:{ Throwable -> 0x000d, all -> 0x0010 }
            r1.close()     // Catch:{ Throwable -> 0x000d, all -> 0x0010 }
        L_0x000d:
            r2.mWritableDatabase = r0     // Catch:{ all -> 0x0016 }
            goto L_0x0014
        L_0x0010:
            r1 = move-exception
            r2.mWritableDatabase = r0     // Catch:{ all -> 0x0016 }
            throw r1     // Catch:{ all -> 0x0016 }
        L_0x0014:
            monitor-exit(r2)
            return
        L_0x0016:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.db.SQLiteCheckHelper.closeWritableDatabase():void");
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable unused) {
            }
        }
    }

    private static boolean deleteDatabase(File file) {
        if (file == null) {
            return false;
        }
        boolean delete = file.delete() | false | new File(file.getPath() + "-journal").delete() | new File(file.getPath() + "-shm").delete() | new File(file.getPath() + "-wal").delete();
        File parentFile = file.getParentFile();
        if (parentFile != null) {
            final String str = file.getName() + "-mj";
            File[] listFiles = parentFile.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().startsWith(str);
                }
            });
            if (listFiles != null) {
                for (File delete2 : listFiles) {
                    delete |= delete2.delete();
                }
            }
        }
        return delete;
    }
}
