package com.taobao.phenix.cache.memory;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import com.taobao.weex.el.parse.Operators;

public class PassableBitmapDrawable extends BitmapDrawable {
    private Rect mBitmapPadding;
    private int mDiskCacheCatalog;
    private String mDiskCacheKey;
    private int mDiskPriority;
    private boolean mFromDisk;
    private boolean mFromMemory;
    private boolean mFromSecondary;
    private boolean mIsNinePatch;
    private String mMemoryCacheKey;

    public PassableBitmapDrawable(Resources resources, Bitmap bitmap, Rect rect, String str, String str2, int i, int i2) {
        super(resources, bitmap);
        this.mBitmapPadding = rect;
        this.mIsNinePatch = (bitmap == null || bitmap.getNinePatchChunk() == null || !NinePatch.isNinePatchChunk(bitmap.getNinePatchChunk())) ? false : true;
        init(str, str2, i, i2);
    }

    public PassableBitmapDrawable(String str, String str2, int i, int i2) {
        init(str, str2, i, i2);
    }

    private void init(String str, String str2, int i, int i2) {
        this.mMemoryCacheKey = str;
        this.mDiskCacheKey = str2;
        this.mDiskCacheCatalog = i;
        this.mDiskPriority = i2;
    }

    public NinePatchDrawable convert2NinePatchDrawable() {
        if (!this.mIsNinePatch) {
            return null;
        }
        Bitmap bitmap = getBitmap();
        return new NinePatchDrawable(bitmap, bitmap.getNinePatchChunk(), this.mBitmapPadding != null ? this.mBitmapPadding : new Rect(), (String) null);
    }

    public int getDiskCacheCatalog() {
        return this.mDiskCacheCatalog;
    }

    public String getMemoryCacheKey() {
        return this.mMemoryCacheKey;
    }

    public String getDiskCacheKey() {
        return this.mDiskCacheKey;
    }

    public int getDiskPriority() {
        return this.mDiskPriority;
    }

    public boolean isFromMemory() {
        return this.mFromMemory;
    }

    public boolean isFromDisk() {
        return this.mFromDisk;
    }

    public boolean isFromSecondary() {
        return this.mFromSecondary;
    }

    public void fromSecondary(boolean z) {
        this.mFromSecondary = z;
    }

    public void fromMemory(boolean z) {
        this.mFromMemory = z;
    }

    public void fromDisk(boolean z) {
        this.mFromDisk = z;
    }

    public String toString() {
        return getClass().getSimpleName() + Operators.BRACKET_START_STR + Integer.toHexString(hashCode()) + ", key@" + this.mMemoryCacheKey + Operators.BRACKET_END_STR;
    }
}
