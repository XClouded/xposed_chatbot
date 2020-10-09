package com.taobao.alivfssdk.cache;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.taobao.alivfsadapter.AVFSDBCursor;
import com.taobao.alivfsadapter.AVFSDataBase;
import com.taobao.alivfssdk.fresco.cache.common.CacheKey;
import com.taobao.alivfssdk.fresco.cache.common.PairCacheKey;
import com.taobao.alivfssdk.utils.AVFSCacheLog;
import java.io.IOException;
import java.util.Arrays;

public class AVFSSQLiteCacheItem {
    private static final String AVFS_SQLITE_CACHE_ITEM__TABLE = "AVFS_KV_TABLE";
    private static final String CREATE_AVFS_SQLITE_CACHE_ITEM__TABLE = "CREATE TABLE IF NOT EXISTS AVFS_KV_TABLE(key TEXT, key2 TEXT, value BLOB, size INTEGER, time INTEGER, PRIMARY KEY(key, key2));";
    private static final String DELETE_ALL_STATEMENT = "DELETE FROM AVFS_KV_TABLE";
    private static final String DELETE_STATEMENT = "DELETE FROM AVFS_KV_TABLE WHERE key = ? AND key2 = ?";
    private static final String INSERT_STATEMENT = "INSERT INTO AVFS_KV_TABLE VALUES(?,?,?,?,?)";
    private static final String KEY_ID = "_id";
    private static final String KEY_KEY = "key";
    private static final String KEY_KEY2 = "key2";
    private static final String KEY_SIZE = "size";
    private static final String KEY_TIME = "time";
    private static final String KEY_VALUE = "value";
    private static final String QUERY_ALL_EXTEND_KEY_STATEMENT = "SELECT key2 FROM AVFS_KV_TABLE WHERE key = ?";
    private static final String QUERY_ALL_STATEMENT = "SELECT * FROM AVFS_KV_TABLE";
    private static final String QUERY_SINGLE_STATEMENT = "SELECT  * FROM AVFS_KV_TABLE WHERE key = ? AND key2 = ? LIMIT 1";
    private static final String REPLACE_STATEMENT = "REPLACE INTO AVFS_KV_TABLE VALUES(?,?,?,?,?)";
    private static final String TAG = "AVFSSQLiteCacheItem";
    private static final String UPDATE_TIME_STATEMENT = "UPDATE AVFS_KV_TABLE SET time = ? WHERE key = ? AND key2 = ?";
    public String key;
    public String key2;
    public long size;
    public long time;
    public byte[] value;

    public AVFSSQLiteCacheItem(String str, CacheKey cacheKey) {
        this.key = str;
        if (cacheKey instanceof PairCacheKey) {
            PairCacheKey pairCacheKey = (PairCacheKey) cacheKey;
            if (!TextUtils.isEmpty(pairCacheKey.mKey2)) {
                this.key2 = pairCacheKey.mKey2;
                return;
            }
        }
        this.key2 = this.key2 == null ? "" : this.key2;
    }

    public AVFSSQLiteCacheItem() {
    }

    public AVFSSQLiteCacheItem(String str, String str2) {
        this.key = str;
        this.key2 = str2 == null ? "" : str2;
    }

    public static void createTable(AVFSDataBase aVFSDataBase) throws IOException {
        try {
            aVFSDataBase.execUpdate(CREATE_AVFS_SQLITE_CACHE_ITEM__TABLE);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public void save(AVFSDataBase aVFSDataBase) throws IOException {
        if (this.key == null) {
            throw new IllegalArgumentException("key cannot be null");
        } else if (this.value != null) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                aVFSDataBase.execUpdate(REPLACE_STATEMENT, getObjects());
                long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                Log.i(TAG, "save: " + currentTimeMillis2 + "ms");
            } catch (Exception e) {
                throw new IOException(e);
            }
        } else {
            throw new IllegalArgumentException("value cannot be null");
        }
    }

    public boolean updateReadTime(AVFSDataBase aVFSDataBase, long j) throws IOException {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            boolean execUpdate = aVFSDataBase.execUpdate(UPDATE_TIME_STATEMENT, new Object[]{Long.valueOf(j), this.key, this.key2});
            if (execUpdate) {
                this.time = j;
            }
            Log.i(TAG, "updateReadTime:" + (System.currentTimeMillis() - currentTimeMillis) + " ms ");
            return execUpdate;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0066  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem[] getItems(com.taobao.alivfsadapter.AVFSDataBase r5) throws java.io.IOException {
        /*
            long r0 = java.lang.System.currentTimeMillis()
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r3 = 0
            java.lang.String r4 = "SELECT * FROM AVFS_KV_TABLE"
            com.taobao.alivfsadapter.AVFSDBCursor r5 = r5.execQuery(r4)     // Catch:{ Exception -> 0x005d }
            if (r5 == 0) goto L_0x002c
            boolean r3 = r5.next()     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            if (r3 == 0) goto L_0x002c
        L_0x0018:
            com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem r3 = loadFromCursor(r5)     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            r2.add(r3)     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            boolean r3 = r5.next()     // Catch:{ Exception -> 0x0029, all -> 0x0026 }
            if (r3 != 0) goto L_0x0018
            goto L_0x002c
        L_0x0026:
            r0 = move-exception
            r3 = r5
            goto L_0x0064
        L_0x0029:
            r0 = move-exception
            r3 = r5
            goto L_0x005e
        L_0x002c:
            if (r5 == 0) goto L_0x0031
            r5.close()
        L_0x0031:
            int r5 = r2.size()
            com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem[] r5 = new com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem[r5]
            r2.toArray(r5)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r0
            java.lang.String r0 = "AVFSSQLiteCacheItem"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r4 = "getItems: "
            r1.append(r4)
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.i(r0, r1)
            return r5
        L_0x005b:
            r0 = move-exception
            goto L_0x0064
        L_0x005d:
            r0 = move-exception
        L_0x005e:
            java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x005b }
            r5.<init>(r0)     // Catch:{ all -> 0x005b }
            throw r5     // Catch:{ all -> 0x005b }
        L_0x0064:
            if (r3 == 0) goto L_0x0069
            r3.close()
        L_0x0069:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem.getItems(com.taobao.alivfsadapter.AVFSDataBase):com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem[]");
    }

    public static AVFSSQLiteCacheItem get(AVFSDataBase aVFSDataBase, String str, CacheKey cacheKey) throws IOException {
        String str2;
        if (cacheKey instanceof PairCacheKey) {
            PairCacheKey pairCacheKey = (PairCacheKey) cacheKey;
            if (!TextUtils.isEmpty(pairCacheKey.mKey2)) {
                str2 = pairCacheKey.mKey2;
                return get(aVFSDataBase, str, str2);
            }
        }
        str2 = "";
        return get(aVFSDataBase, str, str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0083  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem get(com.taobao.alivfsadapter.AVFSDataBase r7, java.lang.String r8, java.lang.String r9) throws java.io.IOException {
        /*
            r0 = 0
            r1 = 0
            r2 = 1
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0060 }
            java.lang.String r5 = "SELECT  * FROM AVFS_KV_TABLE WHERE key = ? AND key2 = ? LIMIT 1"
            r6 = 2
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ Exception -> 0x0060 }
            r6[r1] = r8     // Catch:{ Exception -> 0x0060 }
            r6[r2] = r9     // Catch:{ Exception -> 0x0060 }
            com.taobao.alivfsadapter.AVFSDBCursor r7 = r7.execQuery(r5, r6)     // Catch:{ Exception -> 0x0060 }
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r9 = 0
            long r5 = r5 - r3
            java.lang.String r9 = "AVFSSQLiteCacheItem"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r3.<init>()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r4 = "get: "
            r3.append(r4)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            r3.append(r5)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r4 = "ms"
            r3.append(r4)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            android.util.Log.i(r9, r3)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            if (r7 == 0) goto L_0x0047
            boolean r9 = r7.next()     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            if (r9 == 0) goto L_0x0047
            com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem r9 = loadFromCursor(r7)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            if (r7 == 0) goto L_0x0046
            r7.close()
        L_0x0046:
            return r9
        L_0x0047:
            java.lang.String r9 = "AVFSSQLiteCacheItem"
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            java.lang.String r4 = "No item found to select."
            r3[r1] = r4     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            com.taobao.alivfssdk.utils.AVFSCacheLog.w((java.lang.String) r9, (java.lang.Object[]) r3)     // Catch:{ Exception -> 0x005b, all -> 0x0058 }
            if (r7 == 0) goto L_0x0057
            r7.close()
        L_0x0057:
            return r0
        L_0x0058:
            r8 = move-exception
            r0 = r7
            goto L_0x0081
        L_0x005b:
            r9 = move-exception
            r0 = r7
            goto L_0x0061
        L_0x005e:
            r8 = move-exception
            goto L_0x0081
        L_0x0060:
            r9 = move-exception
        L_0x0061:
            java.lang.String r7 = "AVFSSQLiteCacheItem"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x005e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x005e }
            r3.<init>()     // Catch:{ all -> 0x005e }
            java.lang.String r4 = "Error encountered on selecting the key="
            r3.append(r4)     // Catch:{ all -> 0x005e }
            r3.append(r8)     // Catch:{ all -> 0x005e }
            java.lang.String r8 = r3.toString()     // Catch:{ all -> 0x005e }
            r2[r1] = r8     // Catch:{ all -> 0x005e }
            com.taobao.alivfssdk.utils.AVFSCacheLog.e(r7, r9, r2)     // Catch:{ all -> 0x005e }
            java.io.IOException r7 = new java.io.IOException     // Catch:{ all -> 0x005e }
            r7.<init>(r9)     // Catch:{ all -> 0x005e }
            throw r7     // Catch:{ all -> 0x005e }
        L_0x0081:
            if (r0 == 0) goto L_0x0086
            r0.close()
        L_0x0086:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem.get(com.taobao.alivfsadapter.AVFSDataBase, java.lang.String, java.lang.String):com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem");
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0064  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<java.lang.String> extendsKeysForKey(com.taobao.alivfsadapter.AVFSDataBase r6, java.lang.String r7) throws java.io.IOException {
        /*
            java.lang.System.currentTimeMillis()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 1
            r2 = 0
            r3 = 0
            java.lang.String r4 = "SELECT key2 FROM AVFS_KV_TABLE WHERE key = ?"
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x0041 }
            r5[r2] = r7     // Catch:{ Exception -> 0x0041 }
            com.taobao.alivfsadapter.AVFSDBCursor r6 = r6.execQuery(r4, r5)     // Catch:{ Exception -> 0x0041 }
            java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x003c, all -> 0x0039 }
            if (r6 == 0) goto L_0x0033
            boolean r3 = r6.next()     // Catch:{ Exception -> 0x003c, all -> 0x0039 }
            if (r3 == 0) goto L_0x0033
        L_0x0020:
            java.lang.String r3 = r6.getString((int) r2)     // Catch:{ Exception -> 0x003c, all -> 0x0039 }
            boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x003c, all -> 0x0039 }
            if (r4 != 0) goto L_0x002d
            r0.add(r3)     // Catch:{ Exception -> 0x003c, all -> 0x0039 }
        L_0x002d:
            boolean r3 = r6.next()     // Catch:{ Exception -> 0x003c, all -> 0x0039 }
            if (r3 != 0) goto L_0x0020
        L_0x0033:
            if (r6 == 0) goto L_0x0038
            r6.close()
        L_0x0038:
            return r0
        L_0x0039:
            r7 = move-exception
            r3 = r6
            goto L_0x0062
        L_0x003c:
            r0 = move-exception
            r3 = r6
            goto L_0x0042
        L_0x003f:
            r7 = move-exception
            goto L_0x0062
        L_0x0041:
            r0 = move-exception
        L_0x0042:
            java.lang.String r6 = "AVFSSQLiteCacheItem"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x003f }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x003f }
            r4.<init>()     // Catch:{ all -> 0x003f }
            java.lang.String r5 = "Error encountered on extendsKeysForKey the key="
            r4.append(r5)     // Catch:{ all -> 0x003f }
            r4.append(r7)     // Catch:{ all -> 0x003f }
            java.lang.String r7 = r4.toString()     // Catch:{ all -> 0x003f }
            r1[r2] = r7     // Catch:{ all -> 0x003f }
            com.taobao.alivfssdk.utils.AVFSCacheLog.e(r6, r0, r1)     // Catch:{ all -> 0x003f }
            java.io.IOException r6 = new java.io.IOException     // Catch:{ all -> 0x003f }
            r6.<init>(r0)     // Catch:{ all -> 0x003f }
            throw r6     // Catch:{ all -> 0x003f }
        L_0x0062:
            if (r3 == 0) goto L_0x0067
            r3.close()
        L_0x0067:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.AVFSSQLiteCacheItem.extendsKeysForKey(com.taobao.alivfsadapter.AVFSDataBase, java.lang.String):java.util.List");
    }

    public static boolean delete(AVFSDataBase aVFSDataBase, String str, String str2) throws IOException {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            boolean execUpdate = aVFSDataBase.execUpdate(DELETE_STATEMENT, new Object[]{str, str2});
            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
            Log.i(TAG, "delete: " + currentTimeMillis2 + "ms");
            return execUpdate;
        } catch (Exception e) {
            AVFSCacheLog.e(TAG, e, "Error encountered on selecting the key=" + str);
            throw new IOException(e);
        }
    }

    public static boolean deleteAll(AVFSDataBase aVFSDataBase) throws IOException {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            boolean execUpdate = aVFSDataBase.execUpdate(DELETE_ALL_STATEMENT);
            Log.i(TAG, "delete: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            return execUpdate;
        } catch (Exception e) {
            AVFSCacheLog.e(TAG, e, "Error encountered on deleteAll the TABLE=AVFS_KV_TABLE");
            throw new IOException(e);
        }
    }

    public boolean delete(AVFSDataBase aVFSDataBase) throws IOException {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            boolean execUpdate = aVFSDataBase.execUpdate(DELETE_STATEMENT, new Object[]{this.key, this.key2});
            Log.i(TAG, "delete: " + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            return execUpdate;
        } catch (Exception e) {
            AVFSCacheLog.e(TAG, e, "Error encountered on selecting the key=" + this.key);
            throw new IOException(e);
        }
    }

    private static AVFSSQLiteCacheItem loadFromCursor(AVFSDBCursor aVFSDBCursor) {
        AVFSSQLiteCacheItem aVFSSQLiteCacheItem = new AVFSSQLiteCacheItem();
        aVFSSQLiteCacheItem.key = aVFSDBCursor.getString(0);
        aVFSSQLiteCacheItem.key2 = aVFSDBCursor.getString(1);
        aVFSSQLiteCacheItem.value = aVFSDBCursor.getBytes(2);
        aVFSSQLiteCacheItem.size = aVFSDBCursor.getLong(3);
        aVFSSQLiteCacheItem.time = aVFSDBCursor.getLong(4);
        return aVFSSQLiteCacheItem;
    }

    @NonNull
    private Object[] getObjects() {
        return new Object[]{this.key, this.key2, this.value, Long.valueOf(this.size), Long.valueOf(this.time)};
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AVFSSQLiteCacheItem aVFSSQLiteCacheItem = (AVFSSQLiteCacheItem) obj;
        if (this.size != aVFSSQLiteCacheItem.size || this.time != aVFSSQLiteCacheItem.time) {
            return false;
        }
        if (this.key == null ? aVFSSQLiteCacheItem.key == null : this.key.equals(aVFSSQLiteCacheItem.key)) {
            return Arrays.equals(this.value, aVFSSQLiteCacheItem.value);
        }
        return false;
    }

    public int hashCode() {
        return ((((((this.key != null ? this.key.hashCode() : 0) * 31) + Arrays.hashCode(this.value)) * 31) + ((int) (this.size ^ (this.size >>> 32)))) * 31) + ((int) (this.time ^ (this.time >>> 32)));
    }

    public class ByteArrayOutputStream extends java.io.ByteArrayOutputStream {
        public ByteArrayOutputStream() {
        }

        public ByteArrayOutputStream(int i) {
            super(i);
        }

        public void flush() throws IOException {
            super.flush();
            AVFSSQLiteCacheItem.this.value = toByteArray();
            AVFSSQLiteCacheItem.this.size = (long) size();
        }
    }
}
