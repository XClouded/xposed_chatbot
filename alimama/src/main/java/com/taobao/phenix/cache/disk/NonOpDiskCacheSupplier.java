package com.taobao.phenix.cache.disk;

import android.annotation.SuppressLint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NonOpDiskCacheSupplier implements DiskCacheSupplier {
    private final int[] SUPPORT_PRIORITIES = {17};
    @SuppressLint({"UseSparseArrays"})
    private Map<Integer, DiskCache> mPriorityMap = new HashMap();

    public synchronized DiskCache get(int i) {
        for (int i2 : this.SUPPORT_PRIORITIES) {
            if (i2 == i) {
                return ensureDiskCache(i);
            }
        }
        return null;
    }

    private synchronized DiskCache ensureDiskCache(int i) {
        DiskCache diskCache;
        diskCache = this.mPriorityMap.get(Integer.valueOf(i));
        if (diskCache == null) {
            diskCache = new NonOpDiskCache(i);
            this.mPriorityMap.put(Integer.valueOf(i), diskCache);
        }
        return diskCache;
    }

    public synchronized Collection<DiskCache> getAll() {
        for (int ensureDiskCache : this.SUPPORT_PRIORITIES) {
            ensureDiskCache(ensureDiskCache);
        }
        return this.mPriorityMap.values();
    }
}
