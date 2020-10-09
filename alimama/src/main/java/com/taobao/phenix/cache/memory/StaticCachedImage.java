package com.taobao.phenix.cache.memory;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import com.taobao.phenix.common.SizeUtil;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.weex.el.parse.Operators;
import java.util.Map;
import java.util.WeakHashMap;

public class StaticCachedImage extends CachedRootImage {
    private static final Map<Bitmap, Map<StaticCachedImage, Boolean>> bitmapGlobalMaps = new WeakHashMap(300);
    final Bitmap bitmap;
    final Rect bitmapPadding;
    private StaticImageRecycleListener mRecycleListener;

    public interface StaticImageRecycleListener {
        void recycle(StaticCachedImage staticCachedImage);
    }

    public StaticCachedImage(Bitmap bitmap2, Rect rect, String str, String str2, int i, int i2) {
        super(str, str2, i, i2);
        this.bitmap = bitmap2;
        this.bitmapPadding = rect;
        addBitmapGlobalReference();
        UnitedLog.d(ImageCacheAndPool.TAG_RECYCLE, "new image=%s", this);
    }

    private void addBitmapGlobalReference() {
        synchronized (bitmapGlobalMaps) {
            Map map = bitmapGlobalMaps.get(this.bitmap);
            if (map == null) {
                map = new WeakHashMap(1);
                bitmapGlobalMaps.put(this.bitmap, map);
            }
            map.put(this, Boolean.TRUE);
        }
    }

    /* access modifiers changed from: protected */
    public PassableBitmapDrawable newBitmapDrawable(String str, String str2, int i, int i2, boolean z, Resources resources) {
        if (z) {
            return new ReleasableBitmapDrawable(resources, this.bitmap, this.bitmapPadding, str, str2, i, i2);
        }
        return new PassableBitmapDrawable(resources, this.bitmap, this.bitmapPadding, str, str2, i, i2);
    }

    public int getSize() {
        return SizeUtil.getBitmapSize(this.bitmap);
    }

    /* access modifiers changed from: protected */
    public void onChange2NotRecycled() {
        UnitedLog.d(ImageCacheAndPool.TAG_RECYCLE, "image change to not recycled, image=%s", this);
        addBitmapGlobalReference();
    }

    /* access modifiers changed from: protected */
    public void onCanBeRecycled() {
        boolean z;
        synchronized (bitmapGlobalMaps) {
            Map map = bitmapGlobalMaps.get(this.bitmap);
            z = false;
            if (map != null) {
                map.remove(this);
                int size = map.size();
                if (size == 0) {
                    bitmapGlobalMaps.remove(this.bitmap);
                    UnitedLog.d(ImageCacheAndPool.TAG_RECYCLE, "bitmap in the image can be recycled now, image=%s", this);
                    z = true;
                } else {
                    UnitedLog.w(ImageCacheAndPool.TAG_RECYCLE, "cannot recycled the image(bitmap referenced by %d image still), image=%s", Integer.valueOf(size), this);
                }
            } else {
                UnitedLog.w(ImageCacheAndPool.TAG_RECYCLE, "cannot recycled the image(bitmap has been recycled ever), image=%s", this);
            }
        }
        if (z && this.mRecycleListener != null) {
            this.mRecycleListener.recycle(this);
        }
    }

    public String toString() {
        return "StaticCachedImage(" + Integer.toHexString(hashCode()) + ", bmp@" + this.bitmap + ", key@" + getMemoryCacheKey() + Operators.BRACKET_END_STR;
    }

    public StaticCachedImage setStaticImageRecycleListener(StaticImageRecycleListener staticImageRecycleListener) {
        this.mRecycleListener = staticImageRecycleListener;
        return this;
    }
}
