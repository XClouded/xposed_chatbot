package com.taobao.alivfssdk.cache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.alivfsadapter.AVFSAdapterManager;
import com.taobao.alivfsadapter.AVFSSDKAppMonitor;
import com.taobao.alivfsadapter.MonitorCacheEvent;
import com.taobao.alivfssdk.fresco.binaryresource.BinaryResource;
import com.taobao.alivfssdk.fresco.cache.common.CacheErrorLogger;
import com.taobao.alivfssdk.fresco.cache.common.CacheEvent;
import com.taobao.alivfssdk.fresco.cache.common.CacheEventListener;
import com.taobao.alivfssdk.fresco.cache.common.CacheKey;
import com.taobao.alivfssdk.fresco.cache.common.NoEncryptionKey;
import com.taobao.alivfssdk.fresco.cache.common.PairCacheKey;
import com.taobao.alivfssdk.fresco.cache.common.WriterCallback;
import com.taobao.alivfssdk.fresco.cache.common.WriterCallbacks;
import com.taobao.alivfssdk.fresco.cache.disk.DiskStorage;
import com.taobao.alivfssdk.fresco.cache.disk.DiskStorageCache;
import com.taobao.alivfssdk.fresco.cache.disk.EntryEvictionComparatorSupplier;
import com.taobao.alivfssdk.fresco.cache.disk.FileCache;
import com.taobao.alivfssdk.fresco.common.disk.DiskTrimmableRegistry;
import com.taobao.alivfssdk.utils.AVFSCacheLog;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class AVFSDiskCache extends AVFSBaseCache implements CacheEventListener, CacheErrorLogger {
    private static final String TAG = "AVFSCache";
    /* access modifiers changed from: private */
    public final AVFSCache mCaches;
    private final FileCache mFileCache;
    /* access modifiers changed from: private */
    public LruCache<CacheKey, byte[]> mMemoryCache;
    private final String mType;

    public boolean onEviction(CacheEvent cacheEvent) {
        return false;
    }

    public void onHit(CacheEvent cacheEvent) {
    }

    public void onMiss(CacheEvent cacheEvent) {
    }

    public void onRemoveSuccess(CacheEvent cacheEvent) {
    }

    public void onWriteAttempt(CacheEvent cacheEvent) {
    }

    public AVFSDiskCache(@NonNull AVFSCache aVFSCache, String str, DiskStorage diskStorage, DiskStorageCache.Params params, int i) {
        this.mCaches = aVFSCache;
        this.mType = str;
        this.mFileCache = new DiskStorageCache(diskStorage, (EntryEvictionComparatorSupplier) null, params, this, this, (DiskTrimmableRegistry) null, AVFSCacheManager.getInstance().getContext(), Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "AVFSDiskCache #" + AVFSDiskCache.this.mCaches.getModuleName());
            }
        }));
        if (i > 0) {
            this.mMemoryCache = new HotEndLruCache<CacheKey, byte[]>(i, 0.2f) {
                /* access modifiers changed from: protected */
                public int getSize(byte[] bArr) {
                    return bArr.length;
                }
            };
        }
    }

    public Set<String> keySet() {
        try {
            Collection<DiskStorage.Entry> entries = this.mFileCache.getEntries();
            HashSet hashSet = new HashSet(entries.size());
            for (DiskStorage.Entry id : entries) {
                hashSet.add(id.getId());
            }
            return hashSet;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean containObjectForKey(@NonNull String str, int i) {
        return containObjectForKey(str, (String) null, i);
    }

    public boolean containObjectForKey(@NonNull String str, String str2) {
        return containObjectForKey(str, str2, 0);
    }

    public boolean containObjectForKey(@NonNull String str, String str2, int i) {
        CacheKey cacheKey;
        if (str == null) {
            return false;
        }
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, str2);
        } else {
            cacheKey = new PairCacheKey(str, str2);
        }
        return this.mFileCache.hasKey(cacheKey);
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, String str2) {
        return objectForKey(str, str2, (Class) null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:37:0x008b, code lost:
        r10 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x014c, code lost:
        if (r3 == null) goto L_0x0185;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:?, code lost:
        r3.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x0182, code lost:
        if (r3 == null) goto L_0x0185;
     */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x008b A[ExcHandler: AVFSException (e com.taobao.alivfssdk.cache.AVFSException), PHI: r3 
  PHI: (r3v8 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream) = (r3v11 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream), (r3v11 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream), (r3v11 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream), (r3v11 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream), (r3v12 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream), (r3v12 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream), (r3v19 com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream) binds: [B:44:0x0096, B:46:0x009a, B:58:0x00cd, B:48:0x009e, B:33:0x0076, B:34:?, B:14:0x0036] A[DONT_GENERATE, DONT_INLINE], Splitter:B:14:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x012b A[Catch:{ all -> 0x0186 }] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x015e A[Catch:{ all -> 0x0186 }] */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0189 A[SYNTHETIC, Splitter:B:88:0x0189] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:68:0x0121=Splitter:B:68:0x0121, B:78:0x0154=Splitter:B:78:0x0154} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T objectForKey(@androidx.annotation.NonNull java.lang.String r10, java.lang.String r11, java.lang.Class<T> r12, int r13) {
        /*
            r9 = this;
            r12 = 0
            if (r10 != 0) goto L_0x0004
            return r12
        L_0x0004:
            r0 = 0
            java.lang.System.currentTimeMillis()     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r1 = 268435456(0x10000000, float:2.5243549E-29)
            if (r13 != r1) goto L_0x0012
            com.taobao.alivfssdk.fresco.cache.common.NoEncryptionKey r13 = new com.taobao.alivfssdk.fresco.cache.common.NoEncryptionKey     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r13.<init>(r10, r11)     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            goto L_0x0017
        L_0x0012:
            com.taobao.alivfssdk.fresco.cache.common.PairCacheKey r13 = new com.taobao.alivfssdk.fresco.cache.common.PairCacheKey     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r13.<init>(r10, r11)     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
        L_0x0017:
            com.taobao.alivfssdk.cache.LruCache<com.taobao.alivfssdk.fresco.cache.common.CacheKey, byte[]> r1 = r9.mMemoryCache     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r2 = 1
            if (r1 == 0) goto L_0x003a
            com.taobao.alivfssdk.cache.LruCache<com.taobao.alivfssdk.fresco.cache.common.CacheKey, byte[]> r1 = r9.mMemoryCache     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            java.lang.Object r1 = r1.get(r13)     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            byte[] r1 = (byte[]) r1     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            if (r1 == 0) goto L_0x003a
            com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream r3 = new com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r4.<init>(r1)     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            com.taobao.alivfssdk.cache.AVFSCache r1 = r9.mCaches     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r3.<init>(r4, r1)     // Catch:{ AVFSException -> 0x0152, Exception -> 0x011f, all -> 0x011c }
            r9.onHitMemoryCache(r10, r11, r2)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            goto L_0x003c
        L_0x003a:
            r3 = r12
            r2 = 0
        L_0x003c:
            if (r3 != 0) goto L_0x008e
            com.taobao.alivfssdk.fresco.cache.disk.FileCache r1 = r9.mFileCache     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfssdk.fresco.binaryresource.BinaryResource r1 = r1.getResource(r13)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            if (r1 == 0) goto L_0x008e
            java.io.InputStream r4 = r1.openStream()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfssdk.cache.LruCache<com.taobao.alivfssdk.fresco.cache.common.CacheKey, byte[]> r5 = r9.mMemoryCache     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            if (r5 == 0) goto L_0x0074
            com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream r5 = new com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfssdk.cache.AVFSDiskCache$3 r6 = new com.taobao.alivfssdk.cache.AVFSDiskCache$3     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            long r7 = r1.size()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            int r1 = (int) r7     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r6.<init>(r4, r1, r13)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfssdk.cache.AVFSCache r13 = r9.mCaches     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.ClassLoader r13 = r13.getClassLoader()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r5.<init>(r6, r13)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r9.onHitMemoryCache(r10, r11, r0)     // Catch:{ AVFSException -> 0x0070, Exception -> 0x006c, all -> 0x0068 }
            r3 = r5
            goto L_0x0074
        L_0x0068:
            r10 = move-exception
            r3 = r5
            goto L_0x0187
        L_0x006c:
            r10 = move-exception
            r3 = r5
            goto L_0x0121
        L_0x0070:
            r10 = move-exception
            r3 = r5
            goto L_0x0154
        L_0x0074:
            if (r3 != 0) goto L_0x008e
            java.io.BufferedInputStream r13 = new java.io.BufferedInputStream     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r13.<init>(r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream r1 = new com.taobao.alivfssdk.cache.AVFSDiskCache$ObjectInputStream     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfssdk.cache.AVFSCache r4 = r9.mCaches     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.ClassLoader r4 = r4.getClassLoader()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r1.<init>(r13, r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r3 = r1
            goto L_0x008e
        L_0x0088:
            r10 = move-exception
            goto L_0x0121
        L_0x008b:
            r10 = move-exception
            goto L_0x0154
        L_0x008e:
            if (r3 != 0) goto L_0x0096
            if (r3 == 0) goto L_0x0095
            r3.close()     // Catch:{ IOException -> 0x0095 }
        L_0x0095:
            return r12
        L_0x0096:
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.Object r13 = r3.readObject()     // Catch:{ IOException -> 0x00f4, Exception -> 0x00cc, AVFSException -> 0x008b }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r1 = 0
            long r10 = r10 - r4
            java.lang.System.currentTimeMillis()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfsadapter.AVFSAdapterManager r1 = com.taobao.alivfsadapter.AVFSAdapterManager.getInstance()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfsadapter.AVFSSDKAppMonitor r1 = r1.getCacheMonitor()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            if (r1 == 0) goto L_0x00c6
            java.lang.String r4 = "read"
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r4 = r9.getEvenBuilder(r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r10 = r4.diskTime(r10)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r10 = r10.hitMemory(r2)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            com.taobao.alivfsadapter.MonitorCacheEvent r10 = r10.build()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r1.writeEvent(r10)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
        L_0x00c6:
            if (r3 == 0) goto L_0x00cb
            r3.close()     // Catch:{ IOException -> 0x00cb }
        L_0x00cb:
            return r13
        L_0x00cc:
            r13 = move-exception
            com.taobao.alivfssdk.cache.AVFSException r1 = new com.taobao.alivfssdk.cache.AVFSException     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.<init>()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r4 = r13.getMessage()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.append(r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r4 = ", key1="
            r2.append(r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.append(r10)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r10 = ", key2="
            r2.append(r10)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.append(r11)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r10 = r2.toString()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r11 = -3
            r1.<init>(r10, r13, r11)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            throw r1     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
        L_0x00f4:
            r13 = move-exception
            com.taobao.alivfssdk.cache.AVFSException r1 = new com.taobao.alivfssdk.cache.AVFSException     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.<init>()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r4 = r13.getMessage()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.append(r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r4 = ", key1="
            r2.append(r4)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.append(r10)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r10 = ", key2="
            r2.append(r10)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r2.append(r11)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            java.lang.String r10 = r2.toString()     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            r11 = -2
            r1.<init>(r10, r13, r11)     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
            throw r1     // Catch:{ AVFSException -> 0x008b, Exception -> 0x0088 }
        L_0x011c:
            r10 = move-exception
            r3 = r12
            goto L_0x0187
        L_0x011f:
            r10 = move-exception
            r3 = r12
        L_0x0121:
            com.taobao.alivfsadapter.AVFSAdapterManager r11 = com.taobao.alivfsadapter.AVFSAdapterManager.getInstance()     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.AVFSSDKAppMonitor r11 = r11.getCacheMonitor()     // Catch:{ all -> 0x0186 }
            if (r11 == 0) goto L_0x0145
            java.lang.String r13 = "read"
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r13 = r9.getEvenBuilder(r13)     // Catch:{ all -> 0x0186 }
            r1 = -1
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r13 = r13.errorCode(r1)     // Catch:{ all -> 0x0186 }
            java.lang.String r1 = r10.getMessage()     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r13 = r13.errorMessage(r1)     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.MonitorCacheEvent r13 = r13.build()     // Catch:{ all -> 0x0186 }
            r11.writeEvent(r13)     // Catch:{ all -> 0x0186 }
        L_0x0145:
            java.lang.String r11 = "AVFSCache"
            java.lang.Object[] r13 = new java.lang.Object[r0]     // Catch:{ all -> 0x0186 }
            com.taobao.alivfssdk.utils.AVFSCacheLog.e(r11, r10, r13)     // Catch:{ all -> 0x0186 }
            if (r3 == 0) goto L_0x0185
        L_0x014e:
            r3.close()     // Catch:{ IOException -> 0x0185 }
            goto L_0x0185
        L_0x0152:
            r10 = move-exception
            r3 = r12
        L_0x0154:
            com.taobao.alivfsadapter.AVFSAdapterManager r11 = com.taobao.alivfsadapter.AVFSAdapterManager.getInstance()     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.AVFSSDKAppMonitor r11 = r11.getCacheMonitor()     // Catch:{ all -> 0x0186 }
            if (r11 == 0) goto L_0x017b
            java.lang.String r13 = "read"
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r13 = r9.getEvenBuilder(r13)     // Catch:{ all -> 0x0186 }
            int r1 = r10.getErrorCode()     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r13 = r13.errorCode(r1)     // Catch:{ all -> 0x0186 }
            java.lang.String r1 = r10.getMessage()     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.MonitorCacheEvent$Builder r13 = r13.errorMessage(r1)     // Catch:{ all -> 0x0186 }
            com.taobao.alivfsadapter.MonitorCacheEvent r13 = r13.build()     // Catch:{ all -> 0x0186 }
            r11.writeEvent(r13)     // Catch:{ all -> 0x0186 }
        L_0x017b:
            java.lang.String r11 = "AVFSCache"
            java.lang.Object[] r13 = new java.lang.Object[r0]     // Catch:{ all -> 0x0186 }
            com.taobao.alivfssdk.utils.AVFSCacheLog.e(r11, r10, r13)     // Catch:{ all -> 0x0186 }
            if (r3 == 0) goto L_0x0185
            goto L_0x014e
        L_0x0185:
            return r12
        L_0x0186:
            r10 = move-exception
        L_0x0187:
            if (r3 == 0) goto L_0x018c
            r3.close()     // Catch:{ IOException -> 0x018c }
        L_0x018c:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.cache.AVFSDiskCache.objectForKey(java.lang.String, java.lang.String, java.lang.Class, int):java.lang.Object");
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, String str2, Class<T> cls) {
        return objectForKey(str, str2, cls, 0);
    }

    @Nullable
    public <T> T objectForKey(@NonNull String str, int i) {
        return objectForKey(str, (String) null, (Class) null, i);
    }

    private void onHitMemoryCache(@NonNull String str, String str2, boolean z) {
        AVFSSDKAppMonitor cacheMonitor = AVFSAdapterManager.getInstance().getCacheMonitor();
        if (cacheMonitor != null) {
            cacheMonitor.hitMemoryCacheForModule(this.mCaches.getModuleName(), z);
        }
    }

    public List<String> extendsKeysForKey(@NonNull String str) {
        return extendsKeysForKey(str, 0);
    }

    public List<String> extendsKeysForKey(@NonNull String str, int i) {
        CacheKey cacheKey;
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, (String) null);
        } else {
            cacheKey = new PairCacheKey(str, (String) null);
        }
        System.currentTimeMillis();
        try {
            List<String> catalogs = this.mFileCache.getCatalogs(cacheKey);
            System.currentTimeMillis();
            return catalogs;
        } catch (Exception e) {
            AVFSCacheLog.e(TAG, e, new Object[0]);
            return null;
        }
    }

    public boolean setObjectForKey(@NonNull String str, String str2, final Object obj, int i) {
        final CacheKey cacheKey;
        if (str == null) {
            return false;
        }
        if (obj == null) {
            return removeObjectForKey(str, str2);
        }
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, str2);
        } else {
            cacheKey = new PairCacheKey(str, str2);
        }
        try {
            this.mFileCache.insert(cacheKey, new WriterCallback() {
                public OutputStream write(OutputStream outputStream) throws IOException {
                    ObjectOutputStream objectOutputStream = AVFSDiskCache.this.mMemoryCache != null ? new ObjectOutputStream(new BufferedOutputStream(outputStream) {
                        private final ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();

                        public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
                            this.mByteArrayOutputStream.write(bArr, i, i2);
                            super.write(bArr, i, i2);
                        }

                        public synchronized void write(int i) throws IOException {
                            this.mByteArrayOutputStream.write(i);
                            super.write(i);
                        }

                        public void close() throws IOException {
                            AVFSDiskCache.this.mMemoryCache.put(cacheKey, this.mByteArrayOutputStream.toByteArray());
                            super.close();
                        }
                    }) : new ObjectOutputStream(new BufferedOutputStream(outputStream));
                    objectOutputStream.writeObject(obj);
                    return objectOutputStream;
                }
            });
            return true;
        } catch (Exception e) {
            AVFSCacheLog.e(TAG, e, new Object[0]);
            return false;
        }
    }

    public boolean removeObjectForKey(@NonNull String str, int i) {
        return removeObjectForKey(str, (String) null, i);
    }

    public boolean removeObjectForKey(@NonNull String str, String str2) {
        return removeObjectForKey(str, str2, 0);
    }

    public boolean removeObjectForKey(@NonNull String str, String str2, int i) {
        CacheKey cacheKey;
        if (str == null) {
            return false;
        }
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, str2);
        } else {
            cacheKey = new PairCacheKey(str, str2);
        }
        if (this.mMemoryCache != null) {
            this.mMemoryCache.remove(cacheKey);
        }
        return this.mFileCache.remove(cacheKey);
    }

    public boolean removeAllObject() {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.clear();
        }
        this.mFileCache.clearAll();
        return true;
    }

    public InputStream inputStreamForKey(@NonNull String str, String str2) {
        return inputStreamForKey(str, str2, 0);
    }

    public InputStream inputStreamForKey(@NonNull String str, String str2, int i) {
        CacheKey cacheKey;
        if (str == null) {
            return null;
        }
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, str2);
        } else {
            cacheKey = new PairCacheKey(str, str2);
        }
        System.currentTimeMillis();
        try {
            BinaryResource resource = this.mFileCache.getResource(cacheKey);
            System.currentTimeMillis();
            if (resource != null) {
                AVFSCacheLog.d(TAG, "- inputStreamForKey: moduleName=" + this.mCaches.getModuleName() + ", key1=" + str + ", key2=" + str2);
                return resource.openStream();
            }
        } catch (IOException e) {
            AVFSCacheLog.e(TAG, e, new Object[0]);
        }
        return null;
    }

    public boolean setStreamForKey(@NonNull String str, String str2, @NonNull InputStream inputStream, int i) {
        CacheKey cacheKey;
        if (str == null) {
            return false;
        }
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, str2);
        } else {
            cacheKey = new PairCacheKey(str, str2);
        }
        try {
            System.currentTimeMillis();
            this.mFileCache.insert(cacheKey, WriterCallbacks.from(inputStream));
            System.currentTimeMillis();
            return true;
        } catch (Exception e) {
            AVFSCacheLog.e(TAG, e, new Object[0]);
            return false;
        }
    }

    public long lengthForKey(String str, String str2) {
        BinaryResource resource;
        if (str == null || (resource = this.mFileCache.getResource(new PairCacheKey(str, str2))) == null) {
            return -1;
        }
        return resource.size();
    }

    public long lengthForKey(String str, String str2, int i) {
        CacheKey cacheKey;
        if (str == null) {
            return -1;
        }
        if (i == 268435456) {
            cacheKey = new NoEncryptionKey(str, str2);
        } else {
            cacheKey = new PairCacheKey(str, str2);
        }
        BinaryResource resource = this.mFileCache.getResource(cacheKey);
        if (resource != null) {
            return resource.size();
        }
        return -1;
    }

    public void clearMemCache() {
        if (this.mMemoryCache != null) {
            this.mMemoryCache.clear();
        }
    }

    public void close() throws IOException {
        clearMemCache();
        if (this.mFileCache != null) {
            this.mFileCache.close();
        }
    }

    public void onWriteSuccess(CacheEvent cacheEvent) {
        AVFSSDKAppMonitor cacheMonitor = AVFSAdapterManager.getInstance().getCacheMonitor();
        if (cacheMonitor != null) {
            cacheMonitor.writeEvent(getEvenBuilder(MonitorCacheEvent.OPERATION_WRITE).diskTime(cacheEvent.getElapsed()).build());
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public MonitorCacheEvent.Builder getEvenBuilder(String str) {
        return MonitorCacheEvent.newBuilder(this.mCaches.getModuleName(), this.mType, this.mMemoryCache != null).operation(str);
    }

    public void onReadException(CacheEvent cacheEvent) {
        AVFSSDKAppMonitor cacheMonitor = AVFSAdapterManager.getInstance().getCacheMonitor();
        if (cacheMonitor != null) {
            cacheMonitor.writeEvent(getEvenBuilder(MonitorCacheEvent.OPERATION_READ).errorCode(-2).errorMessage(cacheEvent.getException().getMessage()).build());
        }
    }

    public void onWriteException(CacheEvent cacheEvent) {
        AVFSSDKAppMonitor cacheMonitor = AVFSAdapterManager.getInstance().getCacheMonitor();
        if (cacheMonitor != null) {
            cacheMonitor.writeEvent(getEvenBuilder(MonitorCacheEvent.OPERATION_WRITE).errorCode(-2).errorMessage(cacheEvent.getException().getMessage()).build());
        }
    }

    public void logError(CacheErrorLogger.CacheErrorCategory cacheErrorCategory, String str, String str2, @Nullable Throwable th) {
        AVFSCacheLog.e(TAG, th, new Object[0]);
    }

    private static class ObjectInputStream extends java.io.ObjectInputStream {
        private final ClassLoader mClassLoader;

        public ObjectInputStream(InputStream inputStream, ClassLoader classLoader) throws StreamCorruptedException, IOException {
            super(inputStream);
            this.mClassLoader = classLoader;
        }

        /* access modifiers changed from: protected */
        public Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
            try {
                return super.resolveClass(objectStreamClass);
            } catch (ClassNotFoundException unused) {
                return Class.forName(objectStreamClass.getName(), false, this.mClassLoader);
            }
        }
    }
}
