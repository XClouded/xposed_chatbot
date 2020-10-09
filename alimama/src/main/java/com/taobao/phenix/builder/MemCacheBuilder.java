package com.taobao.phenix.builder;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import com.alibaba.android.prefetchx.PrefetchX;
import com.taobao.phenix.cache.LruCache;
import com.taobao.phenix.cache.memory.CachedRootImage;
import com.taobao.phenix.cache.memory.ImageCacheAndPool;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.intf.Phenix;
import com.taobao.tcommon.core.Preconditions;

public class MemCacheBuilder implements Builder<LruCache<String, CachedRootImage>> {
    private static final float DEFAULT_HOT_PERCENT = 0.2f;
    private static final int MAX_MEM_CACHE_SIZE = 36700160;
    private ComponentCallbacks2 mComponentCallbacks;
    private Context mContext;
    private boolean mHaveBuilt;
    private Float mHotPercent;
    private Integer mMaxSize;
    private LruCache<String, CachedRootImage> mMemoryCache;

    public MemCacheBuilder with(LruCache<String, CachedRootImage> lruCache) {
        Preconditions.checkState(!this.mHaveBuilt, "MemCacheBuilder has been built, not allow with() now");
        Preconditions.checkNotNull(lruCache);
        this.mMemoryCache = lruCache;
        return this;
    }

    public MemCacheBuilder maxSize(Integer num) {
        Preconditions.checkState(!this.mHaveBuilt, "MemCacheBuilder has been built, not allow maxSize() now");
        this.mMaxSize = num;
        return this;
    }

    public MemCacheBuilder hotPercent(Float f) {
        Preconditions.checkState(!this.mHaveBuilt, "MemCacheBuilder has been built, not allow hotPercent() now");
        this.mHotPercent = f;
        return this;
    }

    /* access modifiers changed from: package-private */
    public LruCache<String, CachedRootImage> memoryCache() {
        return this.mMemoryCache;
    }

    public synchronized LruCache<String, CachedRootImage> build() {
        if (this.mHaveBuilt) {
            return this.mMemoryCache;
        }
        this.mContext = Phenix.instance().applicationContext();
        Preconditions.checkNotNull(this.mContext, "Phenix.with(Context) hasn't been called before MemCacheBuilder building");
        this.mHaveBuilt = true;
        if (this.mMemoryCache != null) {
            int maxSize = this.mMemoryCache.maxSize();
            float hotPercent = this.mMemoryCache.hotPercent();
            int intValue = this.mMaxSize != null ? this.mMaxSize.intValue() : maxSize;
            float floatValue = this.mHotPercent != null ? this.mHotPercent.floatValue() : hotPercent;
            if (maxSize != intValue || ((double) Math.abs(hotPercent - floatValue)) >= 1.0E-4d) {
                this.mMemoryCache.resize(intValue, floatValue);
            }
            return registerComponentCallbacks(this.mMemoryCache);
        }
        if (this.mMaxSize == null) {
            this.mMaxSize = Integer.valueOf(getMaxAvailableSize(this.mContext));
        }
        if (this.mHotPercent == null) {
            this.mHotPercent = Float.valueOf(0.2f);
        }
        this.mMemoryCache = new ImageCacheAndPool(this.mMaxSize.intValue(), this.mHotPercent.floatValue());
        return registerComponentCallbacks(this.mMemoryCache);
    }

    private LruCache<String, CachedRootImage> registerComponentCallbacks(final LruCache<String, CachedRootImage> lruCache) {
        if (Build.VERSION.SDK_INT >= 14) {
            this.mComponentCallbacks = new ComponentCallbacks2() {
                public void onConfigurationChanged(Configuration configuration) {
                }

                public void onLowMemory() {
                }

                public void onTrimMemory(int i) {
                    UnitedLog.d(ImageCacheAndPool.TAG, "ComponentCallbacks2 onTrimMemory, level=%d", Integer.valueOf(i));
                    if (i >= 60) {
                        lruCache.clear();
                        UnitedLog.w(ImageCacheAndPool.TAG, "clear all at TRIM_MEMORY_MODERATE", new Object[0]);
                    } else if (i >= 40) {
                        int size = lruCache.size() / 2;
                        lruCache.trimTo(size);
                        UnitedLog.w(ImageCacheAndPool.TAG, "trim to size=%d at TRIM_MEMORY_BACKGROUND", Integer.valueOf(size));
                    }
                }
            };
            this.mContext.registerComponentCallbacks(this.mComponentCallbacks);
        }
        return lruCache;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            super.finalize();
            if (this.mComponentCallbacks == null) {
                return;
            }
        } catch (Throwable th) {
            if (this.mComponentCallbacks != null) {
                this.mContext.unregisterComponentCallbacks(this.mComponentCallbacks);
            }
            throw th;
        }
        this.mContext.unregisterComponentCallbacks(this.mComponentCallbacks);
    }

    private static int getMaxAvailableSize(Context context) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        long min = Math.min(maxMemory, activityManager != null ? (long) (activityManager.getMemoryClass() * 1048576) : 0);
        return Math.min(MAX_MEM_CACHE_SIZE, min < PrefetchX.SUPPORT_IMAGE_WINDVANE_PLUGIN ? 6291456 : min < 67108864 ? 10485760 : (int) (min / 5));
    }

    /* access modifiers changed from: package-private */
    public ComponentCallbacks2 getComponentCallbacks() {
        return this.mComponentCallbacks;
    }
}
