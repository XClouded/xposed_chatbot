package com.taobao.phenix.builder;

import android.util.SparseIntArray;
import com.taobao.phenix.cache.disk.DiskCache;
import com.taobao.phenix.cache.disk.DiskCacheSupplier;
import com.taobao.phenix.cache.disk.NonOpDiskCacheSupplier;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.tcommon.core.Preconditions;

public class DiskCacheBuilder implements Builder<DiskCacheSupplier> {
    private DiskCacheSupplier mDiskCacheSupplier;
    private boolean mHaveBuilt;
    private final SparseIntArray mPrioritySizes = new SparseIntArray(4);

    public DiskCacheBuilder() {
        this.mPrioritySizes.put(17, 83886080);
        this.mPrioritySizes.put(34, 10485760);
        this.mPrioritySizes.put(51, 31457280);
        this.mPrioritySizes.put(68, 10485760);
        this.mPrioritySizes.put(85, 20971520);
    }

    public DiskCacheBuilder with(DiskCacheSupplier diskCacheSupplier) {
        Preconditions.checkState(!this.mHaveBuilt, "DiskCacheBuilder has been built, not allow with() now");
        this.mDiskCacheSupplier = diskCacheSupplier;
        return this;
    }

    public DiskCacheBuilder maxSize(int i, int i2) {
        Preconditions.checkState(!this.mHaveBuilt, "DiskCacheBuilder has been built, not allow maxSize() now");
        this.mPrioritySizes.put(i, i2);
        return this;
    }

    public synchronized DiskCacheSupplier build() {
        if (this.mHaveBuilt) {
            return this.mDiskCacheSupplier;
        }
        if (this.mDiskCacheSupplier == null) {
            this.mDiskCacheSupplier = new NonOpDiskCacheSupplier();
            UnitedLog.w("DiskCache", "use default non-operation DiskCacheSupplier, cause not implement a custom DiskCacheSupplier", new Object[0]);
        }
        this.mHaveBuilt = true;
        Preconditions.checkNotNull(this.mDiskCacheSupplier.get(17), "DiskCache for the priority(TOP_USED_1) cannot be null");
        for (DiskCache next : this.mDiskCacheSupplier.getAll()) {
            next.maxSize(this.mPrioritySizes.get(next.getPriority(), 0));
        }
        return this.mDiskCacheSupplier;
    }
}
