package com.taobao.alivfssdk.fresco.cache.disk;

import android.content.Context;
import androidx.annotation.Nullable;
import com.taobao.alivfssdk.fresco.binaryresource.BinaryResource;
import com.taobao.alivfssdk.fresco.cache.common.CacheErrorLogger;
import com.taobao.alivfssdk.fresco.cache.common.CacheEventListener;
import com.taobao.alivfssdk.fresco.cache.common.CacheKey;
import com.taobao.alivfssdk.fresco.cache.common.MultiCacheKey;
import com.taobao.alivfssdk.fresco.cache.common.NoEncryptionKey;
import com.taobao.alivfssdk.fresco.cache.common.WriterCallback;
import com.taobao.alivfssdk.fresco.cache.disk.DiskStorage;
import com.taobao.alivfssdk.fresco.common.disk.DiskTrimmable;
import com.taobao.alivfssdk.fresco.common.disk.DiskTrimmableRegistry;
import com.taobao.alivfssdk.fresco.common.internal.VisibleForTesting;
import com.taobao.alivfssdk.fresco.common.statfs.StatFsHelper;
import com.taobao.alivfssdk.fresco.common.util.SecureHashUtil;
import com.taobao.alivfssdk.utils.AVFSCacheLog;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class DiskStorageCache implements FileCache, DiskTrimmable {
    private static final long FILECACHE_SIZE_UPDATE_PERIOD_MS = TimeUnit.MINUTES.toMillis(30);
    private static final long FUTURE_TIMESTAMP_THRESHOLD_MS = TimeUnit.HOURS.toMillis(2);
    private static final String SHARED_PREFS_FILENAME_PREFIX = "disk_entries_list";
    public static final int START_OF_VERSIONING = 1;
    private static final String TAG = "DiskStorageCache";
    private static final double TRIMMING_LOWER_BOUND = 0.02d;
    private static final long UNINITIALIZED = -1;
    static Pattern fileNameFilter = Pattern.compile("[^a-zA-Z0-9\\.\\-]");
    private final CacheErrorLogger mCacheErrorLogger;
    private final CacheEventListener mCacheEventListener;
    @GuardedBy("mLock")
    private long mCacheSizeLastUpdateTime;
    private long mCacheSizeLimit;
    private final long mCacheSizeLimitMinimum;
    private final CacheStats mCacheStats;
    /* access modifiers changed from: private */
    public final CountDownLatch mCountDownLatch = new CountDownLatch(1);
    private long mDefaultCacheSizeLimit;
    private final EntryEvictionComparatorSupplier mEntryEvictionComparatorSupplier;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    private final long mLowDiskSpaceCacheSizeLimit;
    @GuardedBy("mLock")
    @VisibleForTesting
    final Set<String> mResourceIndex;
    private final StatFsHelper mStatFsHelper;
    /* access modifiers changed from: private */
    public final DiskStorage mStorage;

    @VisibleForTesting
    static class CacheStats {
        private long mCount = -1;
        private boolean mInitialized = false;
        private long mSize = -1;

        CacheStats() {
        }

        public synchronized boolean isInitialized() {
            return this.mInitialized;
        }

        public synchronized void reset() {
            this.mInitialized = false;
            this.mCount = -1;
            this.mSize = -1;
        }

        public synchronized void set(long j, long j2) {
            this.mCount = j2;
            this.mSize = j;
            this.mInitialized = true;
        }

        public synchronized void increment(long j, long j2) {
            if (this.mInitialized) {
                this.mSize += j;
                this.mCount += j2;
            }
        }

        public synchronized long getSize() {
            return this.mSize;
        }

        public synchronized long getCount() {
            return this.mCount;
        }
    }

    public static class Params {
        public final long mCacheSizeLimitMinimum;
        public final long mDefaultCacheSizeLimit;
        public final long mLowDiskSpaceCacheSizeLimit;

        public Params(long j, long j2, long j3) {
            this.mCacheSizeLimitMinimum = j;
            this.mLowDiskSpaceCacheSizeLimit = j2;
            this.mDefaultCacheSizeLimit = j3;
        }
    }

    public DiskStorageCache(DiskStorage diskStorage, EntryEvictionComparatorSupplier entryEvictionComparatorSupplier, Params params, CacheEventListener cacheEventListener, CacheErrorLogger cacheErrorLogger, @Nullable DiskTrimmableRegistry diskTrimmableRegistry, final Context context, ExecutorService executorService) {
        this.mLowDiskSpaceCacheSizeLimit = params.mLowDiskSpaceCacheSizeLimit;
        this.mDefaultCacheSizeLimit = params.mDefaultCacheSizeLimit;
        this.mCacheSizeLimit = params.mDefaultCacheSizeLimit;
        this.mStatFsHelper = StatFsHelper.getInstance();
        this.mStorage = diskStorage;
        this.mEntryEvictionComparatorSupplier = entryEvictionComparatorSupplier;
        this.mCacheSizeLastUpdateTime = -1;
        this.mCacheEventListener = cacheEventListener;
        this.mCacheSizeLimitMinimum = params.mCacheSizeLimitMinimum;
        this.mCacheErrorLogger = cacheErrorLogger;
        this.mCacheStats = new CacheStats();
        if (diskTrimmableRegistry != null) {
            diskTrimmableRegistry.registerDiskTrimmable(this);
        }
        this.mResourceIndex = new HashSet();
        executorService.execute(new Runnable() {
            public void run() {
                synchronized (DiskStorageCache.this.mLock) {
                    boolean unused = DiskStorageCache.this.maybeUpdateFileCacheSize();
                    DiskStorageCache.maybeDeleteSharedPreferencesFile(context, DiskStorageCache.this.mStorage.getStorageName());
                }
                DiskStorageCache.this.mCountDownLatch.countDown();
            }
        });
    }

    public DiskStorage.DiskDumpInfo getDumpInfo() throws IOException {
        return this.mStorage.getDumpInfo();
    }

    public Collection<DiskStorage.Entry> getEntries() throws IOException {
        return this.mStorage.getEntries();
    }

    public boolean isEnabled() {
        return this.mStorage.isEnabled();
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public void awaitIndex() {
        try {
            this.mCountDownLatch.await();
        } catch (InterruptedException unused) {
            AVFSCacheLog.e(TAG, "Memory Index is not ready yet. ");
        }
    }

    public BinaryResource getResource(CacheKey cacheKey) {
        BinaryResource binaryResource;
        int i = 0;
        AVFSCacheLog.d(TAG, "- getResource: key=" + cacheKey + ", thread=" + Thread.currentThread());
        SettableCacheEvent cacheKey2 = new SettableCacheEvent().setCacheKey(cacheKey);
        try {
            synchronized (this.mLock) {
                List<String> resourceIds = getResourceIds(cacheKey);
                String str = null;
                binaryResource = null;
                while (true) {
                    if (i >= resourceIds.size()) {
                        break;
                    }
                    str = resourceIds.get(i);
                    cacheKey2.setResourceId(str);
                    binaryResource = this.mStorage.getResource(str, cacheKey, cacheKey);
                    if (binaryResource != null) {
                        break;
                    }
                    i++;
                }
                if (binaryResource == null) {
                    if (this.mCacheEventListener != null) {
                        this.mCacheEventListener.onMiss(cacheKey2);
                    }
                    this.mResourceIndex.remove(str);
                } else {
                    if (this.mCacheEventListener != null) {
                        this.mCacheEventListener.onHit(cacheKey2);
                    }
                    this.mResourceIndex.add(str);
                }
            }
            return binaryResource;
        } catch (IOException e) {
            this.mCacheErrorLogger.logError(CacheErrorLogger.CacheErrorCategory.GENERIC_IO, TAG, "getResource", e);
            cacheKey2.setException(e);
            if (this.mCacheEventListener != null) {
                this.mCacheEventListener.onReadException(cacheKey2);
            }
            return null;
        }
    }

    public boolean probe(CacheKey cacheKey) {
        String str;
        String str2 = null;
        try {
            synchronized (this.mLock) {
                try {
                    List<String> resourceIds = getResourceIds(cacheKey);
                    String str3 = null;
                    int i = 0;
                    while (i < resourceIds.size()) {
                        try {
                            String str4 = resourceIds.get(i);
                            try {
                                if (this.mStorage.touch(str4, cacheKey, cacheKey)) {
                                    this.mResourceIndex.add(str4);
                                    return true;
                                }
                                i++;
                                str3 = str4;
                            } catch (Throwable th) {
                                th = th;
                                try {
                                    throw th;
                                } catch (IOException e) {
                                    e = e;
                                    str2 = str;
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            str = str3;
                            throw th;
                        }
                    }
                    return false;
                } catch (Throwable th3) {
                    str = null;
                    th = th3;
                    throw th;
                }
            }
        } catch (IOException e2) {
            e = e2;
            if (this.mCacheEventListener != null) {
                this.mCacheEventListener.onReadException(new SettableCacheEvent().setCacheKey(cacheKey).setResourceId(str2).setException(e));
            }
            return false;
        }
    }

    private DiskStorage.Inserter startInsert(String str, CacheKey cacheKey) throws IOException {
        maybeEvictFilesInCacheDir();
        return this.mStorage.insert(str, cacheKey, cacheKey);
    }

    private BinaryResource endInsert(DiskStorage.Inserter inserter, CacheKey cacheKey, String str) throws IOException {
        BinaryResource commit;
        synchronized (this.mLock) {
            commit = inserter.commit(cacheKey, cacheKey);
            this.mResourceIndex.add(str);
            this.mCacheStats.increment(commit.size(), 1);
        }
        return commit;
    }

    public BinaryResource insert(CacheKey cacheKey, WriterCallback writerCallback) throws IOException {
        String firstResourceId;
        DiskStorage.Inserter startInsert;
        long currentTimeMillis = System.currentTimeMillis();
        SettableCacheEvent cacheKey2 = new SettableCacheEvent().setCacheKey(cacheKey);
        if (this.mCacheEventListener != null) {
            this.mCacheEventListener.onWriteAttempt(cacheKey2);
        }
        synchronized (this.mLock) {
            firstResourceId = getFirstResourceId(cacheKey);
        }
        cacheKey2.setResourceId(firstResourceId);
        try {
            startInsert = startInsert(firstResourceId, cacheKey);
            startInsert.writeData(writerCallback, cacheKey, cacheKey);
            BinaryResource endInsert = endInsert(startInsert, cacheKey, firstResourceId);
            cacheKey2.setItemSize(endInsert.size()).setCacheSize(this.mCacheStats.getSize()).setElapsed(System.currentTimeMillis() - currentTimeMillis);
            if (this.mCacheEventListener != null) {
                this.mCacheEventListener.onWriteSuccess(cacheKey2);
            }
            if (!startInsert.cleanUp()) {
                AVFSCacheLog.e(TAG, "Failed to delete temp file");
            }
            return endInsert;
        } catch (IOException e) {
            cacheKey2.setException(e);
            if (this.mCacheEventListener != null) {
                this.mCacheEventListener.onWriteException(cacheKey2);
            }
            AVFSCacheLog.e(TAG, "Failed inserting a file into the cache", e);
            throw e;
        } catch (Throwable th) {
            if (!startInsert.cleanUp()) {
                AVFSCacheLog.e(TAG, "Failed to delete temp file");
            }
            throw th;
        }
    }

    public boolean remove(CacheKey cacheKey) {
        synchronized (this.mLock) {
            boolean z = false;
            try {
                List<String> resourceIds = getResourceIds(cacheKey);
                if (resourceIds.size() > 0) {
                    String str = resourceIds.get(0);
                    SettableCacheEvent cacheKey2 = new SettableCacheEvent().setCacheKey(cacheKey);
                    cacheKey2.setResourceId(str);
                    long remove = this.mStorage.remove(str, cacheKey);
                    this.mResourceIndex.remove(str);
                    cacheKey2.setItemSize(remove).setCacheSize(this.mCacheStats.getSize());
                    if (this.mCacheEventListener != null) {
                        this.mCacheEventListener.onRemoveSuccess(cacheKey2);
                    }
                    if (remove >= 0) {
                        z = true;
                    }
                    return z;
                }
            } catch (IOException e) {
                CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
                CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.DELETE_FILE;
                cacheErrorLogger.logError(cacheErrorCategory, TAG, "delete: " + e.getMessage(), e);
            } catch (Throwable th) {
                throw th;
            }
        }
        return false;
    }

    public long clearOldEntries(long j) {
        long j2;
        Iterator<DiskStorage.Entry> it;
        synchronized (this.mLock) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                Collection<DiskStorage.Entry> entries = this.mStorage.getEntries();
                long size = this.mCacheStats.getSize();
                int i = 0;
                Iterator<DiskStorage.Entry> it2 = entries.iterator();
                long j3 = 0;
                j2 = 0;
                while (it2.hasNext()) {
                    try {
                        DiskStorage.Entry next = it2.next();
                        long j4 = currentTimeMillis;
                        long max = Math.max(1, Math.abs(currentTimeMillis - next.getTimestamp()));
                        if (max >= j) {
                            long remove = this.mStorage.remove(next);
                            it = it2;
                            this.mResourceIndex.remove(next.getId());
                            if (remove > 0) {
                                i++;
                                j3 += remove;
                                if (this.mCacheEventListener != null) {
                                    this.mCacheEventListener.onEviction(new SettableCacheEvent().setResourceId(next.getId()).setEvictionReason(CacheEventListener.EvictionReason.CONTENT_STALE).setItemSize(remove).setCacheSize(size - j3));
                                }
                            }
                        } else {
                            it = it2;
                            j2 = Math.max(j2, max);
                        }
                        currentTimeMillis = j4;
                        it2 = it;
                    } catch (IOException e) {
                        e = e;
                        CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
                        CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.EVICTION;
                        cacheErrorLogger.logError(cacheErrorCategory, TAG, "clearOldEntries: " + e.getMessage(), e);
                        return j2;
                    }
                }
                this.mStorage.purgeUnexpectedResources();
                if (i > 0) {
                    maybeUpdateFileCacheSize();
                    this.mCacheStats.increment(-j3, (long) (-i));
                }
            } catch (IOException e2) {
                e = e2;
                j2 = 0;
                CacheErrorLogger cacheErrorLogger2 = this.mCacheErrorLogger;
                CacheErrorLogger.CacheErrorCategory cacheErrorCategory2 = CacheErrorLogger.CacheErrorCategory.EVICTION;
                cacheErrorLogger2.logError(cacheErrorCategory2, TAG, "clearOldEntries: " + e.getMessage(), e);
                return j2;
            }
        }
        return j2;
    }

    private void maybeEvictFilesInCacheDir() throws IOException {
        synchronized (this.mLock) {
            boolean maybeUpdateFileCacheSize = maybeUpdateFileCacheSize();
            updateFileCacheSizeLimit();
            long size = this.mCacheStats.getSize();
            if (size > this.mCacheSizeLimit && !maybeUpdateFileCacheSize) {
                this.mCacheStats.reset();
                maybeUpdateFileCacheSize();
            }
            if (size > this.mCacheSizeLimit) {
                long currentTimeMillis = System.currentTimeMillis();
                long j = (this.mCacheSizeLimit * 9) / 10;
                evictAboveSize(j, CacheEventListener.EvictionReason.CACHE_FULL);
                AVFSCacheLog.d(TAG, "- evictAboveSize: desiredSize=" + j + ", elapsed=" + (System.currentTimeMillis() - currentTimeMillis) + "ms");
            }
        }
    }

    @GuardedBy("mLock")
    private void evictAboveSize(long j, CacheEventListener.EvictionReason evictionReason) throws IOException {
        long j2 = j;
        try {
            Collection<DiskStorage.Entry> sortedEntries = getSortedEntries(this.mStorage.getEntries());
            long size = this.mCacheStats.getSize();
            long j3 = size - j2;
            int i = 0;
            long j4 = 0;
            for (DiskStorage.Entry next : sortedEntries) {
                if (j4 > j3) {
                    break;
                }
                long remove = this.mStorage.remove(next);
                this.mResourceIndex.remove(next.getId());
                if (remove > 0) {
                    i++;
                    j4 += remove;
                    if (this.mCacheEventListener != null) {
                        boolean onEviction = this.mCacheEventListener.onEviction(new SettableCacheEvent().setResourceId(next.getId()).setEvictionReason(evictionReason).setItemSize(remove).setCacheSize(size - j4).setCacheLimit(j2));
                    }
                }
                CacheEventListener.EvictionReason evictionReason2 = evictionReason;
            }
            this.mCacheStats.increment(-j4, (long) (-i));
            this.mStorage.purgeUnexpectedResources();
        } catch (IOException e) {
            CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
            CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.EVICTION;
            cacheErrorLogger.logError(cacheErrorCategory, TAG, "evictAboveSize: " + e.getMessage(), e);
            throw e;
        }
    }

    private Collection<DiskStorage.Entry> getSortedEntries(Collection<DiskStorage.Entry> collection) {
        if (this.mEntryEvictionComparatorSupplier == null) {
            return collection;
        }
        long currentTimeMillis = System.currentTimeMillis() + FUTURE_TIMESTAMP_THRESHOLD_MS;
        ArrayList arrayList = new ArrayList(collection.size());
        ArrayList arrayList2 = new ArrayList(collection.size());
        for (DiskStorage.Entry next : collection) {
            if (next.getTimestamp() > currentTimeMillis) {
                arrayList.add(next);
            } else {
                arrayList2.add(next);
            }
        }
        Collections.sort(arrayList2, this.mEntryEvictionComparatorSupplier.get());
        arrayList.addAll(arrayList2);
        return arrayList;
    }

    @GuardedBy("mLock")
    private void updateFileCacheSizeLimit() {
        if (this.mStatFsHelper.testLowDiskSpace(this.mStorage.isExternal() ? StatFsHelper.StorageType.EXTERNAL : StatFsHelper.StorageType.INTERNAL, this.mDefaultCacheSizeLimit - this.mCacheStats.getSize())) {
            this.mCacheSizeLimit = this.mLowDiskSpaceCacheSizeLimit;
        } else {
            this.mCacheSizeLimit = this.mDefaultCacheSizeLimit;
        }
    }

    public long getSize() {
        return this.mCacheStats.getSize();
    }

    public long getCount() {
        return this.mCacheStats.getCount();
    }

    public void clearAll() {
        synchronized (this.mLock) {
            try {
                this.mStorage.clearAll();
                this.mResourceIndex.clear();
            } catch (IOException e) {
                CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
                CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.EVICTION;
                cacheErrorLogger.logError(cacheErrorCategory, TAG, "clearAll: " + e.getMessage(), e);
            }
            this.mCacheStats.reset();
        }
    }

    public boolean hasKeySync(CacheKey cacheKey) {
        synchronized (this.mLock) {
            List<String> resourceIds = getResourceIds(cacheKey);
            for (int i = 0; i < resourceIds.size(); i++) {
                if (this.mResourceIndex.contains(resourceIds.get(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean hasKey(CacheKey cacheKey) {
        synchronized (this.mLock) {
            if (hasKeySync(cacheKey)) {
                return true;
            }
            try {
                List<String> resourceIds = getResourceIds(cacheKey);
                for (int i = 0; i < resourceIds.size(); i++) {
                    String str = resourceIds.get(i);
                    if (this.mStorage.contains(str, cacheKey, cacheKey)) {
                        this.mResourceIndex.add(str);
                        return true;
                    }
                }
                return false;
            } catch (IOException unused) {
                return false;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void trimToMinimum() {
        /*
            r8 = this;
            java.lang.Object r0 = r8.mLock
            monitor-enter(r0)
            r8.maybeUpdateFileCacheSize()     // Catch:{ all -> 0x003d }
            com.taobao.alivfssdk.fresco.cache.disk.DiskStorageCache$CacheStats r1 = r8.mCacheStats     // Catch:{ all -> 0x003d }
            long r1 = r1.getSize()     // Catch:{ all -> 0x003d }
            long r3 = r8.mCacheSizeLimitMinimum     // Catch:{ all -> 0x003d }
            r5 = 0
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 <= 0) goto L_0x003b
            int r3 = (r1 > r5 ? 1 : (r1 == r5 ? 0 : -1))
            if (r3 <= 0) goto L_0x003b
            long r3 = r8.mCacheSizeLimitMinimum     // Catch:{ all -> 0x003d }
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 >= 0) goto L_0x001f
            goto L_0x003b
        L_0x001f:
            r3 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            long r5 = r8.mCacheSizeLimitMinimum     // Catch:{ all -> 0x003d }
            double r5 = (double) r5
            double r1 = (double) r1
            java.lang.Double.isNaN(r5)
            java.lang.Double.isNaN(r1)
            double r5 = r5 / r1
            double r3 = r3 - r5
            r1 = 4581421828931458171(0x3f947ae147ae147b, double:0.02)
            int r5 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r5 <= 0) goto L_0x0039
            r8.trimBy(r3)     // Catch:{ all -> 0x003d }
        L_0x0039:
            monitor-exit(r0)     // Catch:{ all -> 0x003d }
            return
        L_0x003b:
            monitor-exit(r0)     // Catch:{ all -> 0x003d }
            return
        L_0x003d:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x003d }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.alivfssdk.fresco.cache.disk.DiskStorageCache.trimToMinimum():void");
    }

    public List<String> getCatalogs(CacheKey cacheKey) {
        synchronized (this.mLock) {
            List<String> resourceIds = getResourceIds(cacheKey);
            if (resourceIds.size() <= 0) {
                return null;
            }
            List<String> catalogs = this.mStorage.getCatalogs(resourceIds.get(0));
            return catalogs;
        }
    }

    public void trimToNothing() {
        clearAll();
    }

    private void trimBy(double d) {
        synchronized (this.mLock) {
            try {
                this.mCacheStats.reset();
                maybeUpdateFileCacheSize();
                long size = this.mCacheStats.getSize();
                double d2 = (double) size;
                Double.isNaN(d2);
                evictAboveSize(size - ((long) (d * d2)), CacheEventListener.EvictionReason.CACHE_MANAGER_TRIMMED);
            } catch (IOException e) {
                CacheErrorLogger cacheErrorLogger = this.mCacheErrorLogger;
                CacheErrorLogger.CacheErrorCategory cacheErrorCategory = CacheErrorLogger.CacheErrorCategory.EVICTION;
                cacheErrorLogger.logError(cacheErrorCategory, TAG, "trimBy: " + e.getMessage(), e);
            }
        }
    }

    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public boolean maybeUpdateFileCacheSize() {
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mCacheStats.isInitialized() && this.mCacheSizeLastUpdateTime != -1 && currentTimeMillis - this.mCacheSizeLastUpdateTime <= FILECACHE_SIZE_UPDATE_PERIOD_MS) {
            return false;
        }
        long currentTimeMillis2 = System.currentTimeMillis();
        maybeUpdateFileCacheSizeAndIndex();
        long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis2;
        AVFSCacheLog.d(TAG, "- maybeUpdateFileCacheSizeAndIndex: now=" + currentTimeMillis + ", elapsed=" + currentTimeMillis3 + "ms" + ", thread=" + Thread.currentThread());
        this.mCacheSizeLastUpdateTime = currentTimeMillis;
        return true;
    }

    @GuardedBy("mLock")
    private void maybeUpdateFileCacheSizeAndIndex() {
        Set<String> set;
        long j;
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = FUTURE_TIMESTAMP_THRESHOLD_MS + currentTimeMillis;
        if (this.mResourceIndex.isEmpty()) {
            set = this.mResourceIndex;
        } else {
            set = new HashSet<>();
        }
        try {
            boolean z = false;
            long j3 = -1;
            int i = 0;
            int i2 = 0;
            long j4 = 0;
            int i3 = 0;
            for (DiskStorage.Entry next : this.mStorage.getEntries()) {
                i3++;
                j4 += next.getSize();
                if (next.getTimestamp() > j2) {
                    i++;
                    j = j2;
                    int size = (int) (((long) i2) + next.getSize());
                    z = true;
                    j3 = Math.max(next.getTimestamp() - currentTimeMillis, j3);
                    i2 = size;
                } else {
                    j = j2;
                    set.add(next.getId());
                }
                j2 = j;
            }
            if (z) {
                this.mCacheErrorLogger.logError(CacheErrorLogger.CacheErrorCategory.READ_INVALID_ENTRY, TAG, "Future timestamp found in " + i + " files , with a total size of " + i2 + " bytes, and a maximum time delta of " + j3 + "ms", (Throwable) null);
            }
            long j5 = (long) i3;
            if (this.mCacheStats.getCount() != j5 || this.mCacheStats.getSize() != j4) {
                if (this.mResourceIndex != set) {
                    this.mResourceIndex.clear();
                    this.mResourceIndex.addAll(set);
                }
                this.mCacheStats.set(j4, j5);
            }
        } catch (IOException e) {
            this.mCacheErrorLogger.logError(CacheErrorLogger.CacheErrorCategory.GENERIC_IO, TAG, "calcFileCacheSize: " + e.getMessage(), e);
        }
    }

    private static List<String> getResourceIds(CacheKey cacheKey) {
        try {
            ArrayList arrayList = new ArrayList();
            if (cacheKey instanceof MultiCacheKey) {
                List<CacheKey> cacheKeys = ((MultiCacheKey) cacheKey).getCacheKeys();
                for (int i = 0; i < cacheKeys.size(); i++) {
                    arrayList.add(secureHashKey(cacheKeys.get(i)));
                }
            } else if (cacheKey instanceof NoEncryptionKey) {
                arrayList.add(escapeString(cacheKey.toString()));
            } else {
                arrayList.add(secureHashKey(cacheKey));
            }
            return arrayList;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @VisibleForTesting
    public static String getFirstResourceId(CacheKey cacheKey) {
        try {
            if (cacheKey instanceof MultiCacheKey) {
                return secureHashKey(((MultiCacheKey) cacheKey).getCacheKeys().get(0));
            }
            if (cacheKey instanceof NoEncryptionKey) {
                return cacheKey.toString();
            }
            return secureHashKey(cacheKey);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String secureHashKey(CacheKey cacheKey) throws UnsupportedEncodingException {
        return SecureHashUtil.makeSHA1HashBase64(cacheKey.toString().getBytes("UTF-8"));
    }

    private static String escapeString(String str) {
        return fileNameFilter.matcher(str).replaceAll("_");
    }

    /* access modifiers changed from: private */
    public static void maybeDeleteSharedPreferencesFile(Context context, String str) {
        Context applicationContext = context.getApplicationContext();
        File file = new File((applicationContext.getFilesDir().getParent() + File.separator + "shared_prefs" + File.separator + SHARED_PREFS_FILENAME_PREFIX + str) + ".xml");
        try {
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception unused) {
            AVFSCacheLog.e(TAG, "Fail to delete SharedPreference from file system. ");
        }
    }

    public void setDefaultCacheSizeLimit(long j) {
        synchronized (this.mLock) {
            this.mDefaultCacheSizeLimit = j;
        }
    }

    public void close() throws IOException {
        this.mStorage.close();
    }
}
