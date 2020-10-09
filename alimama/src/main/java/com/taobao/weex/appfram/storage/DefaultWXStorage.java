package com.taobao.weex.appfram.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.alipay.sdk.util.e;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultWXStorage implements IWXStorageAdapter {
    /* access modifiers changed from: private */
    public WXSQLiteOpenHelper mDatabaseSupplier;
    private ExecutorService mExecutorService;

    private void execute(@Nullable Runnable runnable) {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newSingleThreadExecutor();
        }
        if (runnable != null && !this.mExecutorService.isShutdown() && !this.mExecutorService.isTerminated()) {
            this.mExecutorService.execute(WXThread.secure(runnable));
        }
    }

    public DefaultWXStorage(Context context) {
        this.mDatabaseSupplier = new WXSQLiteOpenHelper(context);
    }

    public void setItem(final String str, final String str2, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> itemResult = StorageResultHandler.setItemResult(DefaultWXStorage.this.performSetItem(str, str2, false, true));
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(itemResult);
                }
            }
        });
    }

    public void getItem(final String str, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> itemResult = StorageResultHandler.getItemResult(DefaultWXStorage.this.performGetItem(str));
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(itemResult);
                }
            }
        });
    }

    public void removeItem(final String str, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> removeItemResult = StorageResultHandler.removeItemResult(DefaultWXStorage.this.performRemoveItem(str));
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(removeItemResult);
                }
            }
        });
    }

    public void length(final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> lengthResult = StorageResultHandler.getLengthResult(DefaultWXStorage.this.performGetLength());
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(lengthResult);
                }
            }
        });
    }

    public void getAllKeys(final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> allkeysResult = StorageResultHandler.getAllkeysResult(DefaultWXStorage.this.performGetAllKeys());
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(allkeysResult);
                }
            }
        });
    }

    public void setItemPersistent(final String str, final String str2, final IWXStorageAdapter.OnResultReceivedListener onResultReceivedListener) {
        execute(new Runnable() {
            public void run() {
                Map<String, Object> itemResult = StorageResultHandler.setItemResult(DefaultWXStorage.this.performSetItem(str, str2, true, true));
                if (onResultReceivedListener != null) {
                    onResultReceivedListener.onReceived(itemResult);
                }
            }
        });
    }

    public void close() {
        final ExecutorService executorService = this.mExecutorService;
        execute(new Runnable() {
            public void run() {
                try {
                    DefaultWXStorage.this.mDatabaseSupplier.closeDatabase();
                    if (executorService != null) {
                        executorService.shutdown();
                    }
                } catch (Exception e) {
                    WXLogUtils.e("weex_storage", e.getMessage());
                }
            }
        });
        this.mExecutorService = null;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00d0  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean performSetItem(java.lang.String r7, java.lang.String r8, boolean r9, boolean r10) {
        /*
            r6 = this;
            com.taobao.weex.appfram.storage.WXSQLiteOpenHelper r0 = r6.mDatabaseSupplier
            android.database.sqlite.SQLiteDatabase r0 = r0.getDatabase()
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            java.lang.String r2 = "weex_storage"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "set k-v to storage(key:"
            r3.append(r4)
            r3.append(r7)
            java.lang.String r4 = ",value:"
            r3.append(r4)
            r3.append(r8)
            java.lang.String r4 = ",isPersistent:"
            r3.append(r4)
            r3.append(r9)
            java.lang.String r4 = ",allowRetry:"
            r3.append(r4)
            r3.append(r10)
            java.lang.String r4 = ")"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r2, (java.lang.String) r3)
            java.lang.String r2 = "INSERT OR REPLACE INTO default_wx_storage VALUES (?,?,?,?);"
            r3 = 0
            java.text.SimpleDateFormat r4 = com.taobao.weex.appfram.storage.WXSQLiteOpenHelper.sDateFormatter
            java.util.Date r5 = new java.util.Date
            r5.<init>()
            java.lang.String r4 = r4.format(r5)
            android.database.sqlite.SQLiteStatement r0 = r0.compileStatement(r2)     // Catch:{ Exception -> 0x007a }
            r0.clearBindings()     // Catch:{ Exception -> 0x0075, all -> 0x0072 }
            r2 = 1
            r0.bindString(r2, r7)     // Catch:{ Exception -> 0x0075, all -> 0x0072 }
            r3 = 2
            r0.bindString(r3, r8)     // Catch:{ Exception -> 0x0075, all -> 0x0072 }
            r3 = 3
            r0.bindString(r3, r4)     // Catch:{ Exception -> 0x0075, all -> 0x0072 }
            r3 = 4
            if (r9 == 0) goto L_0x0064
            r4 = 1
            goto L_0x0066
        L_0x0064:
            r4 = 0
        L_0x0066:
            r0.bindLong(r3, r4)     // Catch:{ Exception -> 0x0075, all -> 0x0072 }
            r0.execute()     // Catch:{ Exception -> 0x0075, all -> 0x0072 }
            if (r0 == 0) goto L_0x0071
            r0.close()
        L_0x0071:
            return r2
        L_0x0072:
            r7 = move-exception
            r3 = r0
            goto L_0x00d4
        L_0x0075:
            r2 = move-exception
            r3 = r0
            goto L_0x007b
        L_0x0078:
            r7 = move-exception
            goto L_0x00d4
        L_0x007a:
            r2 = move-exception
        L_0x007b:
            java.lang.String r0 = "weex_storage"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0078 }
            r4.<init>()     // Catch:{ all -> 0x0078 }
            java.lang.String r5 = "DefaultWXStorage occurred an exception when execute setItem :"
            r4.append(r5)     // Catch:{ all -> 0x0078 }
            java.lang.String r5 = r2.getMessage()     // Catch:{ all -> 0x0078 }
            r4.append(r5)     // Catch:{ all -> 0x0078 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0078 }
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r4)     // Catch:{ all -> 0x0078 }
            boolean r0 = r2 instanceof android.database.sqlite.SQLiteFullException     // Catch:{ all -> 0x0078 }
            if (r0 == 0) goto L_0x00ce
            if (r10 == 0) goto L_0x00ce
            boolean r10 = r6.trimToSize()     // Catch:{ all -> 0x0078 }
            if (r10 == 0) goto L_0x00ce
            java.lang.String r10 = "weex_storage"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0078 }
            r0.<init>()     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "retry set k-v to storage(key:"
            r0.append(r2)     // Catch:{ all -> 0x0078 }
            r0.append(r7)     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = ",value:"
            r0.append(r2)     // Catch:{ all -> 0x0078 }
            r0.append(r8)     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = ")"
            r0.append(r2)     // Catch:{ all -> 0x0078 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0078 }
            com.taobao.weex.utils.WXLogUtils.d((java.lang.String) r10, (java.lang.String) r0)     // Catch:{ all -> 0x0078 }
            boolean r7 = r6.performSetItem(r7, r8, r9, r1)     // Catch:{ all -> 0x0078 }
            if (r3 == 0) goto L_0x00cd
            r3.close()
        L_0x00cd:
            return r7
        L_0x00ce:
            if (r3 == 0) goto L_0x00d3
            r3.close()
        L_0x00d3:
            return r1
        L_0x00d4:
            if (r3 == 0) goto L_0x00d9
            r3.close()
        L_0x00d9:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.DefaultWXStorage.performSetItem(java.lang.String, java.lang.String, boolean, boolean):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x007f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0080  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean trimToSize() {
        /*
            r11 = this;
            com.taobao.weex.appfram.storage.WXSQLiteOpenHelper r0 = r11.mDatabaseSupplier
            android.database.sqlite.SQLiteDatabase r1 = r0.getDatabase()
            r0 = 0
            if (r1 != 0) goto L_0x000a
            return r0
        L_0x000a:
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.lang.String r2 = "default_wx_storage"
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]
            java.lang.String r4 = "key"
            r3[r0] = r4
            java.lang.String r4 = "persistent"
            r10 = 1
            r3[r10] = r4
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            java.lang.String r8 = "timestamp ASC"
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6, r7, r8)
            int r2 = r1.getCount()     // Catch:{ Exception -> 0x0060 }
            int r2 = r2 / 10
            r3 = 0
        L_0x002e:
            boolean r4 = r1.moveToNext()     // Catch:{ Exception -> 0x005c }
            if (r4 == 0) goto L_0x0058
            java.lang.String r4 = "key"
            int r4 = r1.getColumnIndex(r4)     // Catch:{ Exception -> 0x005c }
            java.lang.String r4 = r1.getString(r4)     // Catch:{ Exception -> 0x005c }
            java.lang.String r5 = "persistent"
            int r5 = r1.getColumnIndex(r5)     // Catch:{ Exception -> 0x005c }
            int r5 = r1.getInt(r5)     // Catch:{ Exception -> 0x005c }
            if (r5 != r10) goto L_0x004c
            r5 = 1
            goto L_0x004d
        L_0x004c:
            r5 = 0
        L_0x004d:
            if (r5 != 0) goto L_0x002e
            if (r4 == 0) goto L_0x002e
            int r3 = r3 + 1
            r9.add(r4)     // Catch:{ Exception -> 0x005c }
            if (r3 != r2) goto L_0x002e
        L_0x0058:
            r1.close()
            goto L_0x007d
        L_0x005c:
            r2 = move-exception
            goto L_0x0062
        L_0x005e:
            r0 = move-exception
            goto L_0x00b0
        L_0x0060:
            r2 = move-exception
            r3 = 0
        L_0x0062:
            java.lang.String r4 = "weex_storage"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x005e }
            r5.<init>()     // Catch:{ all -> 0x005e }
            java.lang.String r6 = "DefaultWXStorage occurred an exception when execute trimToSize:"
            r5.append(r6)     // Catch:{ all -> 0x005e }
            java.lang.String r2 = r2.getMessage()     // Catch:{ all -> 0x005e }
            r5.append(r2)     // Catch:{ all -> 0x005e }
            java.lang.String r2 = r5.toString()     // Catch:{ all -> 0x005e }
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r4, (java.lang.String) r2)     // Catch:{ all -> 0x005e }
            goto L_0x0058
        L_0x007d:
            if (r3 > 0) goto L_0x0080
            return r0
        L_0x0080:
            java.util.Iterator r0 = r9.iterator()
        L_0x0084:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0094
            java.lang.Object r1 = r0.next()
            java.lang.String r1 = (java.lang.String) r1
            r11.performRemoveItem(r1)
            goto L_0x0084
        L_0x0094:
            java.lang.String r0 = "weex_storage"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "remove "
            r1.append(r2)
            r1.append(r3)
            java.lang.String r2 = " items by lru"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r1)
            return r10
        L_0x00b0:
            r1.close()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.DefaultWXStorage.trimToSize():boolean");
    }

    /* access modifiers changed from: private */
    public String performGetItem(String str) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }
        Cursor query = database.query("default_wx_storage", new String[]{"value"}, "key=?", new String[]{str}, (String) null, (String) null, (String) null);
        try {
            if (query.moveToNext()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("timestamp", WXSQLiteOpenHelper.sDateFormatter.format(new Date()));
                int update = this.mDatabaseSupplier.getDatabase().update("default_wx_storage", contentValues, "key= ?", new String[]{str});
                StringBuilder sb = new StringBuilder();
                sb.append("update timestamp ");
                sb.append(update == 1 ? "success" : e.a);
                sb.append(" for operation [getItem(key = ");
                sb.append(str);
                sb.append(")]");
                WXLogUtils.d("weex_storage", sb.toString());
                return query.getString(query.getColumnIndex("value"));
            }
            query.close();
            return null;
        } catch (Exception e) {
            WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute getItem:" + e.getMessage());
            return null;
        } finally {
            query.close();
        }
    }

    /* access modifiers changed from: private */
    public boolean performRemoveItem(String str) {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return false;
        }
        try {
            if (database.delete("default_wx_storage", "key=?", new String[]{str}) == 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute removeItem:" + e.getMessage());
            return false;
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0047  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long performGetLength() {
        /*
            r7 = this;
            com.taobao.weex.appfram.storage.WXSQLiteOpenHelper r0 = r7.mDatabaseSupplier
            android.database.sqlite.SQLiteDatabase r0 = r0.getDatabase()
            r1 = 0
            if (r0 != 0) goto L_0x000b
            return r1
        L_0x000b:
            java.lang.String r3 = "SELECT count(key) FROM default_wx_storage"
            r4 = 0
            android.database.sqlite.SQLiteStatement r0 = r0.compileStatement(r3)     // Catch:{ Exception -> 0x0024 }
            long r3 = r0.simpleQueryForLong()     // Catch:{ Exception -> 0x001f, all -> 0x001c }
            if (r0 == 0) goto L_0x001b
            r0.close()
        L_0x001b:
            return r3
        L_0x001c:
            r1 = move-exception
            r4 = r0
            goto L_0x0045
        L_0x001f:
            r3 = move-exception
            r4 = r0
            goto L_0x0025
        L_0x0022:
            r1 = move-exception
            goto L_0x0045
        L_0x0024:
            r3 = move-exception
        L_0x0025:
            java.lang.String r0 = "weex_storage"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0022 }
            r5.<init>()     // Catch:{ all -> 0x0022 }
            java.lang.String r6 = "DefaultWXStorage occurred an exception when execute getLength:"
            r5.append(r6)     // Catch:{ all -> 0x0022 }
            java.lang.String r3 = r3.getMessage()     // Catch:{ all -> 0x0022 }
            r5.append(r3)     // Catch:{ all -> 0x0022 }
            java.lang.String r3 = r5.toString()     // Catch:{ all -> 0x0022 }
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r0, (java.lang.String) r3)     // Catch:{ all -> 0x0022 }
            if (r4 == 0) goto L_0x0044
            r4.close()
        L_0x0044:
            return r1
        L_0x0045:
            if (r4 == 0) goto L_0x004a
            r4.close()
        L_0x004a:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.appfram.storage.DefaultWXStorage.performGetLength():long");
    }

    /* access modifiers changed from: private */
    public List<String> performGetAllKeys() {
        SQLiteDatabase database = this.mDatabaseSupplier.getDatabase();
        if (database == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Cursor query = database.query("default_wx_storage", new String[]{"key"}, (String) null, (String[]) null, (String) null, (String) null, (String) null);
        while (query.moveToNext()) {
            try {
                arrayList.add(query.getString(query.getColumnIndex("key")));
            } catch (Exception e) {
                WXLogUtils.e("weex_storage", "DefaultWXStorage occurred an exception when execute getAllKeys:" + e.getMessage());
                return arrayList;
            } finally {
                query.close();
            }
        }
        return arrayList;
    }
}
