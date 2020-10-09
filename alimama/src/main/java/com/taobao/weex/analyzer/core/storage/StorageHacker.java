package com.taobao.weex.analyzer.core.storage;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.appfram.storage.DefaultWXStorage;
import com.taobao.weex.appfram.storage.IWXStorageAdapter;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class StorageHacker {
    /* access modifiers changed from: private */
    public final boolean isDebug;
    /* access modifiers changed from: private */
    public Context mContext;
    private ExecutorService mExecutor = Executors.newCachedThreadPool(new ThreadFactory() {
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "wx_analyzer_storage_dumper");
        }
    });
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public IWXStorageAdapter mStorageAdapter = WXSDKEngine.getIWXStorageAdapter();

    interface OnLoadListener {
        void onLoad(List<StorageInfo> list);
    }

    interface OnRemoveListener {
        void onRemoved(boolean z);
    }

    StorageHacker(@NonNull Context context, boolean z) {
        this.mContext = !(context instanceof Application) ? context.getApplicationContext() : context;
        this.isDebug = z;
    }

    public void destroy() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
            this.mHandler = null;
        }
        if (this.mExecutor != null) {
            this.mExecutor.shutdown();
            this.mExecutor = null;
        }
    }

    public boolean isDestroy() {
        return this.mHandler == null || this.mExecutor == null || this.mExecutor.isShutdown();
    }

    public void fetch(@Nullable final OnLoadListener onLoadListener) {
        if (onLoadListener != null) {
            if (this.mStorageAdapter == null) {
                onLoadListener.onLoad(Collections.emptyList());
            } else if (!(this.mStorageAdapter instanceof DefaultWXStorage)) {
                onLoadListener.onLoad(Collections.emptyList());
            } else if (isDestroy()) {
                onLoadListener.onLoad(Collections.emptyList());
            } else {
                this.mExecutor.execute(new Runnable() {
                    /* JADX WARNING: Removed duplicated region for block: B:36:0x0108  */
                    /* JADX WARNING: Removed duplicated region for block: B:42:0x0115  */
                    /* JADX WARNING: Removed duplicated region for block: B:44:0x011a  */
                    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public void run() {
                        /*
                            r15 = this;
                            java.util.ArrayList r0 = new java.util.ArrayList
                            r0.<init>()
                            r1 = 0
                            java.lang.Class<com.taobao.weex.appfram.storage.WXSQLiteOpenHelper> r2 = com.taobao.weex.appfram.storage.WXSQLiteOpenHelper.class
                            r3 = 1
                            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            java.lang.Class<android.content.Context> r5 = android.content.Context.class
                            r6 = 0
                            r4[r6] = r5     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            java.lang.reflect.Constructor r2 = r2.getDeclaredConstructor(r4)     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            r2.setAccessible(r3)     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            com.taobao.weex.analyzer.core.storage.StorageHacker r5 = com.taobao.weex.analyzer.core.storage.StorageHacker.this     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            android.content.Context r5 = r5.mContext     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            r4[r6] = r5     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            java.lang.Object r2 = r2.newInstance(r4)     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            com.taobao.weex.appfram.storage.WXSQLiteOpenHelper r2 = (com.taobao.weex.appfram.storage.WXSQLiteOpenHelper) r2     // Catch:{ Exception -> 0x0101, all -> 0x00fd }
                            java.lang.Class<com.taobao.weex.appfram.storage.WXSQLiteOpenHelper> r4 = com.taobao.weex.appfram.storage.WXSQLiteOpenHelper.class
                            java.lang.String r5 = "getDatabase"
                            java.lang.Class[] r7 = new java.lang.Class[r6]     // Catch:{ Exception -> 0x00fb }
                            java.lang.reflect.Method r4 = r4.getDeclaredMethod(r5, r7)     // Catch:{ Exception -> 0x00fb }
                            r4.setAccessible(r3)     // Catch:{ Exception -> 0x00fb }
                            java.lang.Object[] r5 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x00fb }
                            java.lang.Object r4 = r4.invoke(r2, r5)     // Catch:{ Exception -> 0x00fb }
                            r7 = r4
                            android.database.sqlite.SQLiteDatabase r7 = (android.database.sqlite.SQLiteDatabase) r7     // Catch:{ Exception -> 0x00fb }
                            java.lang.String r8 = "default_wx_storage"
                            r4 = 3
                            java.lang.String[] r9 = new java.lang.String[r4]     // Catch:{ Exception -> 0x00fb }
                            java.lang.String r4 = "key"
                            r9[r6] = r4     // Catch:{ Exception -> 0x00fb }
                            java.lang.String r4 = "value"
                            r9[r3] = r4     // Catch:{ Exception -> 0x00fb }
                            r3 = 2
                            java.lang.String r4 = "timestamp"
                            r9[r3] = r4     // Catch:{ Exception -> 0x00fb }
                            r10 = 0
                            r11 = 0
                            r12 = 0
                            r13 = 0
                            r14 = 0
                            android.database.Cursor r3 = r7.query(r8, r9, r10, r11, r12, r13, r14)     // Catch:{ Exception -> 0x00fb }
                            com.taobao.weex.analyzer.core.storage.StorageHacker r1 = com.taobao.weex.analyzer.core.storage.StorageHacker.this     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            boolean r1 = r1.isDebug     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            if (r1 == 0) goto L_0x0067
                            java.lang.String r1 = "weex-analyzer"
                            java.lang.String r4 = "start dump weex storage"
                            android.util.Log.d(r1, r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                        L_0x0067:
                            boolean r1 = r3.moveToNext()     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            if (r1 == 0) goto L_0x00c9
                            com.taobao.weex.analyzer.core.storage.StorageHacker$StorageInfo r1 = new com.taobao.weex.analyzer.core.storage.StorageHacker$StorageInfo     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r1.<init>()     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r4 = "key"
                            int r4 = r3.getColumnIndex(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r4 = r3.getString(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r1.key = r4     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r4 = "value"
                            int r4 = r3.getColumnIndex(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r4 = r3.getString(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r1.value = r4     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r4 = "timestamp"
                            int r4 = r3.getColumnIndex(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r4 = r3.getString(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r1.timestamp = r4     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            com.taobao.weex.analyzer.core.storage.StorageHacker r4 = com.taobao.weex.analyzer.core.storage.StorageHacker.this     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            boolean r4 = r4.isDebug     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            if (r4 == 0) goto L_0x00c5
                            java.lang.String r4 = "weex-analyzer"
                            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r5.<init>()     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r6 = "weex storage["
                            r5.append(r6)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r6 = r1.key     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r5.append(r6)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r6 = " | "
                            r5.append(r6)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r6 = r1.value     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r5.append(r6)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r6 = "]"
                            r5.append(r6)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            android.util.Log.d(r4, r5)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                        L_0x00c5:
                            r0.add(r1)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            goto L_0x0067
                        L_0x00c9:
                            com.taobao.weex.analyzer.core.storage.StorageHacker r1 = com.taobao.weex.analyzer.core.storage.StorageHacker.this     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            boolean r1 = r1.isDebug     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            if (r1 == 0) goto L_0x00d8
                            java.lang.String r1 = "weex-analyzer"
                            java.lang.String r4 = "end dump weex storage"
                            android.util.Log.d(r1, r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                        L_0x00d8:
                            com.taobao.weex.analyzer.core.storage.StorageHacker r1 = com.taobao.weex.analyzer.core.storage.StorageHacker.this     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            android.os.Handler r1 = r1.mHandler     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            if (r1 == 0) goto L_0x00ee
                            com.taobao.weex.analyzer.core.storage.StorageHacker r1 = com.taobao.weex.analyzer.core.storage.StorageHacker.this     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            android.os.Handler r1 = r1.mHandler     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            com.taobao.weex.analyzer.core.storage.StorageHacker$2$1 r4 = new com.taobao.weex.analyzer.core.storage.StorageHacker$2$1     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r4.<init>(r0)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                            r1.post(r4)     // Catch:{ Exception -> 0x00f8, all -> 0x00f6 }
                        L_0x00ee:
                            if (r3 == 0) goto L_0x00f3
                            r3.close()
                        L_0x00f3:
                            if (r2 == 0) goto L_0x0110
                            goto L_0x010d
                        L_0x00f6:
                            r0 = move-exception
                            goto L_0x0113
                        L_0x00f8:
                            r0 = move-exception
                            r1 = r3
                            goto L_0x0103
                        L_0x00fb:
                            r0 = move-exception
                            goto L_0x0103
                        L_0x00fd:
                            r0 = move-exception
                            r2 = r1
                            r3 = r2
                            goto L_0x0113
                        L_0x0101:
                            r0 = move-exception
                            r2 = r1
                        L_0x0103:
                            r0.printStackTrace()     // Catch:{ all -> 0x0111 }
                            if (r1 == 0) goto L_0x010b
                            r1.close()
                        L_0x010b:
                            if (r2 == 0) goto L_0x0110
                        L_0x010d:
                            r2.closeDatabase()
                        L_0x0110:
                            return
                        L_0x0111:
                            r0 = move-exception
                            r3 = r1
                        L_0x0113:
                            if (r3 == 0) goto L_0x0118
                            r3.close()
                        L_0x0118:
                            if (r2 == 0) goto L_0x011d
                            r2.closeDatabase()
                        L_0x011d:
                            throw r0
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.analyzer.core.storage.StorageHacker.AnonymousClass2.run():void");
                    }
                });
            }
        }
    }

    public void remove(@Nullable final String str, @Nullable final OnRemoveListener onRemoveListener) {
        if (onRemoveListener != null && !TextUtils.isEmpty(str)) {
            if (this.mStorageAdapter == null) {
                onRemoveListener.onRemoved(false);
            } else if (!(this.mStorageAdapter instanceof DefaultWXStorage)) {
                onRemoveListener.onRemoved(false);
            } else if (isDestroy()) {
                onRemoveListener.onRemoved(false);
            } else {
                this.mExecutor.execute(new Runnable() {
                    public void run() {
                        try {
                            DefaultWXStorage defaultWXStorage = (DefaultWXStorage) StorageHacker.this.mStorageAdapter;
                            Method declaredMethod = defaultWXStorage.getClass().getDeclaredMethod("performRemoveItem", new Class[]{String.class});
                            if (declaredMethod != null) {
                                declaredMethod.setAccessible(true);
                                final boolean booleanValue = ((Boolean) declaredMethod.invoke(defaultWXStorage, new Object[]{str})).booleanValue();
                                if (StorageHacker.this.mHandler != null) {
                                    StorageHacker.this.mHandler.post(new Runnable() {
                                        public void run() {
                                            onRemoveListener.onRemoved(booleanValue);
                                        }
                                    });
                                    declaredMethod.setAccessible(false);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    static class StorageInfo {
        public String key;
        public String timestamp;
        public String value;

        StorageInfo() {
        }
    }
}
