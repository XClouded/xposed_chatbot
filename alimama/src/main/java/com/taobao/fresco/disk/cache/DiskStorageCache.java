package com.taobao.fresco.disk.cache;

import android.util.Base64;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.taobao.fresco.disk.cache.CacheEventListener;
import com.taobao.fresco.disk.common.BinaryResource;
import com.taobao.fresco.disk.common.Clock;
import com.taobao.fresco.disk.common.SystemClock;
import com.taobao.fresco.disk.common.WriterCallback;
import com.taobao.fresco.disk.fs.FileBinaryResource;
import com.taobao.fresco.disk.fs.StatFsHelper;
import com.taobao.fresco.disk.storage.DiskStorage;
import com.taobao.phenix.compat.SimpleDiskCache;
import com.taobao.tcommon.core.VisibleForTesting;
import com.taobao.tcommon.log.FLog;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

public class DiskStorageCache implements FileCache {
    private static final long FILECACHE_SIZE_UPDATE_PERIOD_MS = TimeUnit.MINUTES.toMillis(30);
    private static final long FUTURE_TIMESTAMP_THRESHOLD_MS = TimeUnit.HOURS.toMillis(2);
    public static final int START_OF_VERSIONING = 1;
    private static final double TRIMMING_LOWER_BOUND = 0.02d;
    private static final long UNINITIALIZED = -1;
    private final CacheEventListener mCacheEventListener;
    private long mCacheSizeLastUpdateTime;
    private long mCacheSizeLimit;
    private final long mCacheSizeLimitMinimum;
    private final CacheStats mCacheStats;
    private final Clock mClock;
    private final long mDefaultCacheSizeLimit;
    private final Object mLock = new Object();
    private final long mLowDiskSpaceCacheSizeLimit;
    private final StatFsHelper mStatFsHelper;
    private final DiskStorage mStorage;

    public DiskStorageCache(DiskStorage diskStorage, Params params, CacheEventListener cacheEventListener) {
        this.mLowDiskSpaceCacheSizeLimit = params.mLowDiskSpaceCacheSizeLimit;
        this.mDefaultCacheSizeLimit = params.mDefaultCacheSizeLimit;
        this.mCacheSizeLimit = params.mDefaultCacheSizeLimit;
        this.mStatFsHelper = StatFsHelper.getInstance();
        this.mStorage = diskStorage;
        this.mCacheSizeLastUpdateTime = -1;
        this.mCacheEventListener = cacheEventListener;
        this.mCacheSizeLimitMinimum = params.mCacheSizeLimitMinimum;
        this.mCacheStats = new CacheStats();
        this.mClock = SystemClock.get();
    }

    public static String makeSHA1HashBase64(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_1);
            instance.update(bArr, 0, bArr.length);
            return Base64.encodeToString(instance.digest(), 11);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public DiskStorage.DiskDumpInfo getDumpInfo() throws IOException {
        return this.mStorage.getDumpInfo();
    }

    public boolean isEnabled() {
        return this.mStorage.isEnabled();
    }

    public BinaryResource getResource(CacheKey cacheKey) {
        BinaryResource resource;
        try {
            synchronized (this.mLock) {
                resource = this.mStorage.getResource(getResourceId(cacheKey), cacheKey);
                if (resource == null) {
                    this.mCacheEventListener.onMiss();
                } else {
                    this.mCacheEventListener.onHit();
                }
            }
            return resource;
        } catch (IOException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: GENERIC_IO, getResource:" + e.getMessage(), new Object[0]);
            this.mCacheEventListener.onReadException();
            return null;
        }
    }

    public boolean probe(CacheKey cacheKey) {
        boolean z;
        try {
            synchronized (this.mLock) {
                z = this.mStorage.touch(getResourceId(cacheKey), cacheKey);
            }
            return z;
        } catch (IOException unused) {
            this.mCacheEventListener.onReadException();
            return false;
        }
    }

    private BinaryResource createTemporaryResource(String str, CacheKey cacheKey) throws IOException {
        maybeEvictFilesInCacheDir();
        return this.mStorage.createTemporary(str, cacheKey);
    }

    private void deleteTemporaryResource(BinaryResource binaryResource) {
        if (binaryResource instanceof FileBinaryResource) {
            File file = ((FileBinaryResource) binaryResource).getFile();
            if (file.exists()) {
                FLog.e(SimpleDiskCache.TAG, "Temp file still on disk: %s ", file);
                if (!file.delete()) {
                    FLog.e(SimpleDiskCache.TAG, "Failed to delete temp file: %s", file);
                }
            }
        }
    }

    private BinaryResource commitResource(String str, CacheKey cacheKey, BinaryResource binaryResource) throws IOException {
        BinaryResource commit;
        synchronized (this.mLock) {
            commit = this.mStorage.commit(str, binaryResource, cacheKey);
            this.mCacheStats.increment(commit.size(), 1);
        }
        return commit;
    }

    public BinaryResource insert(CacheKey cacheKey, WriterCallback writerCallback) throws IOException {
        BinaryResource createTemporaryResource;
        this.mCacheEventListener.onWriteAttempt();
        String resourceId = getResourceId(cacheKey);
        try {
            createTemporaryResource = createTemporaryResource(resourceId, cacheKey);
            this.mStorage.updateResource(resourceId, createTemporaryResource, writerCallback, cacheKey);
            BinaryResource commitResource = commitResource(resourceId, cacheKey, createTemporaryResource);
            deleteTemporaryResource(createTemporaryResource);
            return commitResource;
        } catch (IOException e) {
            this.mCacheEventListener.onWriteException();
            FLog.e(SimpleDiskCache.TAG, "Failed inserting a file into the cache: %s", e);
            throw e;
        } catch (Throwable th) {
            deleteTemporaryResource(createTemporaryResource);
            throw th;
        }
    }

    public void remove(CacheKey cacheKey) {
        synchronized (this.mLock) {
            try {
                this.mStorage.remove(getResourceId(cacheKey));
            } catch (IOException e) {
                FLog.e(SimpleDiskCache.TAG, "CacheError: DELETE_FILE, delete: " + e.getMessage(), new Object[0]);
            }
        }
    }

    public long clearOldEntries(long j) {
        long j2;
        synchronized (this.mLock) {
            try {
                long now = this.mClock.now();
                int i = 0;
                long j3 = 0;
                j2 = 0;
                for (DiskStorage.Entry next : this.mStorage.getEntries()) {
                    try {
                        long max = Math.max(1, Math.abs(now - next.getTimestamp()));
                        if (max >= j) {
                            long remove = this.mStorage.remove(next);
                            if (remove > 0) {
                                i++;
                                j3 += remove;
                            }
                        } else {
                            j2 = Math.max(j2, max);
                        }
                    } catch (IOException e) {
                        e = e;
                        FLog.e(SimpleDiskCache.TAG, "CacheError: EVICTION, clearOldEntries: " + e.getMessage(), new Object[0]);
                        return j2;
                    }
                }
                this.mStorage.purgeUnexpectedResources();
                if (i > 0) {
                    maybeUpdateFileCacheSize();
                    this.mCacheStats.increment(-j3, (long) (-i));
                    reportEviction(CacheEventListener.EvictionReason.CONTENT_STALE, i, j3);
                }
            } catch (IOException e2) {
                e = e2;
                j2 = 0;
                FLog.e(SimpleDiskCache.TAG, "CacheError: EVICTION, clearOldEntries: " + e.getMessage(), new Object[0]);
                return j2;
            }
        }
        return j2;
    }

    private void reportEviction(CacheEventListener.EvictionReason evictionReason, int i, long j) {
        this.mCacheEventListener.onEviction(evictionReason, i, j);
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
                evictAboveSize((this.mCacheSizeLimit * 9) / 10, CacheEventListener.EvictionReason.CACHE_FULL);
            }
        }
    }

    private void evictAboveSize(long j, CacheEventListener.EvictionReason evictionReason) throws IOException {
        try {
            Collection<DiskStorage.Entry> sortedEntries = getSortedEntries(this.mStorage.getEntries());
            long size = this.mCacheStats.getSize() - j;
            long j2 = 0;
            int i = 0;
            for (DiskStorage.Entry next : sortedEntries) {
                if (j2 > size) {
                    break;
                }
                long remove = this.mStorage.remove(next);
                if (remove > 0) {
                    i++;
                    j2 += remove;
                }
            }
            this.mCacheStats.increment(-j2, (long) (-i));
            this.mStorage.purgeUnexpectedResources();
            reportEviction(evictionReason, i, j2);
        } catch (IOException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: EVICTION, evictAboveSize: " + e.getMessage(), new Object[0]);
            throw e;
        }
    }

    private Collection<DiskStorage.Entry> getSortedEntries(Collection<DiskStorage.Entry> collection) {
        ArrayList arrayList = new ArrayList(collection);
        Collections.sort(arrayList, new TimestampComparator(this.mClock.now() + FUTURE_TIMESTAMP_THRESHOLD_MS));
        return arrayList;
    }

    private void updateFileCacheSizeLimit() {
        if (this.mStatFsHelper.testLowDiskSpace(StatFsHelper.StorageType.INTERNAL, this.mDefaultCacheSizeLimit - this.mCacheStats.getSize())) {
            this.mCacheSizeLimit = this.mLowDiskSpaceCacheSizeLimit;
        } else {
            this.mCacheSizeLimit = this.mDefaultCacheSizeLimit;
        }
    }

    public long getSize() {
        return this.mCacheStats.getSize();
    }

    public void clearAll() {
        synchronized (this.mLock) {
            try {
                this.mStorage.clearAll();
            } catch (IOException e) {
                FLog.e(SimpleDiskCache.TAG, "CacheError: EVICTION, clearAll: " + e.getMessage(), new Object[0]);
            }
            this.mCacheStats.reset();
        }
    }

    public boolean hasKey(CacheKey cacheKey) {
        try {
            return this.mStorage.contains(getResourceId(cacheKey), cacheKey);
        } catch (IOException unused) {
            return false;
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
            com.taobao.fresco.disk.cache.DiskStorageCache$CacheStats r1 = r8.mCacheStats     // Catch:{ all -> 0x003d }
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
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.fresco.disk.cache.DiskStorageCache.trimToMinimum():void");
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
                FLog.e(SimpleDiskCache.TAG, "CacheError: EVICTION, trimBy: " + e.getMessage(), new Object[0]);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void trim2LimitNow() {
        long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
        calcFileCacheSize();
        this.mCacheSizeLastUpdateTime = elapsedRealtime;
        try {
            evictAboveSize(this.mCacheSizeLimit, CacheEventListener.EvictionReason.CACHE_MANAGER_TRIMMED);
        } catch (IOException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: EVICTION, trimBy: " + e.getMessage(), new Object[0]);
        }
    }

    private boolean maybeUpdateFileCacheSize() {
        long elapsedRealtime = android.os.SystemClock.elapsedRealtime();
        if (this.mCacheStats.isInitialized() && this.mCacheSizeLastUpdateTime != -1 && elapsedRealtime - this.mCacheSizeLastUpdateTime <= FILECACHE_SIZE_UPDATE_PERIOD_MS) {
            return false;
        }
        calcFileCacheSize();
        this.mCacheSizeLastUpdateTime = elapsedRealtime;
        return true;
    }

    private void calcFileCacheSize() {
        long j;
        long now = this.mClock.now();
        long j2 = FUTURE_TIMESTAMP_THRESHOLD_MS + now;
        try {
            long j3 = -1;
            int i = 0;
            int i2 = 0;
            long j4 = 0;
            boolean z = false;
            int i3 = 0;
            for (DiskStorage.Entry next : this.mStorage.getEntries()) {
                i3++;
                j4 += next.getSize();
                if (next.getTimestamp() > j2) {
                    i++;
                    j = j2;
                    z = true;
                    j3 = Math.max(next.getTimestamp() - now, j3);
                    i2 = (int) (((long) i2) + next.getSize());
                } else {
                    j = j2;
                }
                j2 = j;
            }
            if (z) {
                FLog.e(SimpleDiskCache.TAG, "CacheError: READ_INVALID_ENTRY, Future timestamp found in " + i + " files , with a total size of " + i2 + " bytes, and a maximum time delta of " + j3 + "ms", new Object[0]);
            }
            this.mCacheStats.set(j4, (long) i3);
        } catch (IOException e) {
            FLog.e(SimpleDiskCache.TAG, "CacheError: GENERIC_IO, calcFileCacheSize: " + e.getMessage(), new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public String getResourceId(CacheKey cacheKey) {
        try {
            return makeSHA1HashBase64(cacheKey.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

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

    private static class TimestampComparator implements Comparator<DiskStorage.Entry> {
        private final long threshold;

        public TimestampComparator(long j) {
            this.threshold = j;
        }

        public int compare(DiskStorage.Entry entry, DiskStorage.Entry entry2) {
            long j = 0;
            long timestamp = entry.getTimestamp() <= this.threshold ? entry.getTimestamp() : 0;
            if (entry2.getTimestamp() <= this.threshold) {
                j = entry2.getTimestamp();
            }
            if (timestamp < j) {
                return -1;
            }
            return j > timestamp ? 1 : 0;
        }
    }
}
