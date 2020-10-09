package com.taobao.phenix.builder;

import com.taobao.phenix.cache.disk.DiskCacheKeyValueStore;

public class DiskCacheKVBuilder implements Builder<DiskCacheKeyValueStore> {
    private DiskCacheKeyValueStore mDiskCacheKV;
    private boolean mHaveBuilt;

    public Builder<DiskCacheKeyValueStore> with(DiskCacheKeyValueStore diskCacheKeyValueStore) {
        this.mDiskCacheKV = diskCacheKeyValueStore;
        return this;
    }

    public DiskCacheKeyValueStore build() {
        if (this.mHaveBuilt) {
            return this.mDiskCacheKV;
        }
        this.mHaveBuilt = true;
        if (this.mDiskCacheKV != null) {
            this.mDiskCacheKV.init();
        }
        return this.mDiskCacheKV;
    }
}
