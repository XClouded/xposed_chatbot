package com.taobao.phenix.builder;

import android.os.Build;
import com.taobao.pexode.Pexode;
import com.taobao.phenix.bitmap.BitmapPool;
import com.taobao.phenix.cache.LruCache;
import com.taobao.phenix.cache.memory.CachedRootImage;
import com.taobao.phenix.intf.Phenix;
import com.taobao.tcommon.core.Preconditions;

public class BitmapPoolBuilder implements Builder<BitmapPool> {
    private static final int POOL_DIVISION_IN_MEMCACHE = 4;
    private BitmapPool mBitmapPool;
    private boolean mHaveBuilt;
    private Integer mMaxSize;

    public BitmapPoolBuilder with(BitmapPool bitmapPool) {
        Preconditions.checkState(!this.mHaveBuilt, "BitmapPoolBuilder has been built, not allow with() now");
        this.mBitmapPool = bitmapPool;
        return this;
    }

    public BitmapPoolBuilder maxSize(Integer num) {
        Preconditions.checkState(!this.mHaveBuilt, "BitmapPoolBuilder has been built, not allow maxSize() now");
        this.mMaxSize = num;
        return this;
    }

    public synchronized BitmapPool build() {
        if (Pexode.isAshmemSupported()) {
            return null;
        }
        if (!this.mHaveBuilt || this.mBitmapPool == null) {
            this.mHaveBuilt = true;
            if (this.mBitmapPool == null) {
                LruCache<String, CachedRootImage> memoryCache = Phenix.instance().memCacheBuilder().memoryCache();
                if (Build.VERSION.SDK_INT >= 19 && (memoryCache instanceof BitmapPool)) {
                    this.mBitmapPool = (BitmapPool) memoryCache;
                    this.mBitmapPool.maxPoolSize(this.mMaxSize != null ? this.mMaxSize.intValue() : memoryCache.maxSize() / 4);
                }
            } else if (this.mMaxSize != null) {
                this.mBitmapPool.maxPoolSize(this.mMaxSize.intValue());
            }
            return this.mBitmapPool;
        }
        return this.mBitmapPool;
    }
}
