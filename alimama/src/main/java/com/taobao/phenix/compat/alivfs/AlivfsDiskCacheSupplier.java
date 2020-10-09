package com.taobao.phenix.compat.alivfs;

import android.annotation.SuppressLint;
import android.app.Application;
import com.taobao.alivfsadapter.AVFSAdapterManager;
import com.taobao.phenix.cache.disk.DiskCache;
import com.taobao.phenix.cache.disk.DiskCacheSupplier;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.intf.Phenix;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AlivfsDiskCacheSupplier implements DiskCacheSupplier {
    private static final int[] SUPPORT_PRIORITIES = {17, 34, 51, 68, 85};
    private static final String[] SUPPORT_PRIORITY_NAMES = {"top1", "top2", "top3", "top4", "top5"};
    private static final int SUPPORT_TOTAL = SUPPORT_PRIORITIES.length;
    @SuppressLint({"UseSparseArrays"})
    private Map<Integer, DiskCache> mPriorityMap = new HashMap();

    public void ensureInitialized() {
        try {
            if (!AVFSAdapterManager.getInstance().isInitialized()) {
                AVFSAdapterManager.getInstance().ensureInitialized((Application) Phenix.instance().applicationContext());
            }
        } catch (Throwable th) {
            UnitedLog.e("DiskCache", "alivfs inited error=%s", th);
        }
    }

    public synchronized AlivfsDiskCache get(int i) {
        for (int i2 = 0; i2 < SUPPORT_TOTAL; i2++) {
            if (SUPPORT_PRIORITIES[i2] == i) {
                return (AlivfsDiskCache) ensureDiskCache(i, i2);
            }
        }
        return null;
    }

    private synchronized DiskCache ensureDiskCache(int i, int i2) {
        AlivfsDiskCache alivfsDiskCache;
        alivfsDiskCache = (AlivfsDiskCache) this.mPriorityMap.get(Integer.valueOf(i));
        if (alivfsDiskCache == null) {
            alivfsDiskCache = new AlivfsDiskCache(i, SUPPORT_PRIORITY_NAMES[i2]);
            this.mPriorityMap.put(Integer.valueOf(i), alivfsDiskCache);
        }
        return alivfsDiskCache;
    }

    public synchronized Collection<DiskCache> getAll() {
        for (int i = 0; i < SUPPORT_TOTAL; i++) {
            ensureDiskCache(SUPPORT_PRIORITIES[i], i);
        }
        return this.mPriorityMap.values();
    }
}
