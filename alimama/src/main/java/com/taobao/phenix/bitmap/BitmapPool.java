package com.taobao.phenix.bitmap;

import android.graphics.Bitmap;
import com.taobao.phenix.cache.memory.CachedRootImage;

public interface BitmapPool {
    int available();

    void clear();

    Bitmap getFromPool(int i, int i2, Bitmap.Config config);

    void maxPoolSize(int i);

    boolean putIntoPool(CachedRootImage cachedRootImage);

    void trimPool(int i);
}
