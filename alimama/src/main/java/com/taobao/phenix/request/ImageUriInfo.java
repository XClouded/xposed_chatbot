package com.taobao.phenix.request;

import android.text.TextUtils;
import com.taobao.phenix.cache.CacheKeyInspector;
import com.taobao.phenix.common.SizeUtil;

public class ImageUriInfo {
    private static final int[] LEVEL_MODEL_SIZES = {10, 30, 60, 100, 200, 300, 500, 800, 1100, 1500};
    private int mBaseCacheCatalog;
    private final CacheKeyInspector mCacheKeyInspector;
    private String mDiskCacheKey;
    private int mMaxViewHeight;
    private int mMaxViewWidth;
    private String mMemoryCacheKey;
    private String mMemoryCacheKeySuffix;
    private String mRequestPath;
    private final SchemeInfo mSchemeInfo;

    public ImageUriInfo(String str, CacheKeyInspector cacheKeyInspector) {
        this.mCacheKeyInspector = cacheKeyInspector;
        this.mRequestPath = str;
        if (str == null) {
            this.mSchemeInfo = new SchemeInfo(1);
            return;
        }
        this.mSchemeInfo = SchemeInfo.parse(str);
        if (this.mSchemeInfo.isNetworkUri() && this.mSchemeInfo.isCdnSize) {
            this.mBaseCacheCatalog = SizeUtil.mergeWH(this.mSchemeInfo.width, this.mSchemeInfo.height);
        }
    }

    /* access modifiers changed from: package-private */
    public void setMaxViewSize(int i, int i2) {
        this.mMaxViewWidth = i;
        this.mMaxViewHeight = i2;
    }

    private int findBestLevel(int i) {
        int length = LEVEL_MODEL_SIZES.length;
        int i2 = length / 2;
        char c = 65535;
        while (i2 >= 0 && i2 < length) {
            int i3 = LEVEL_MODEL_SIZES[i2];
            if (i > i3) {
                if (c >= 0) {
                    if (c == 2) {
                        break;
                    }
                } else {
                    c = 1;
                }
                i2++;
            } else if (i >= i3) {
                break;
            } else {
                if (c >= 0) {
                    if (c == 1) {
                        break;
                    }
                } else {
                    c = 2;
                }
                i2--;
            }
        }
        if (i2 < 0) {
            i2 = 0;
        } else if (i2 >= length) {
            i2 = length - 1;
        } else if (c == 1 && i <= (LEVEL_MODEL_SIZES[i2 - 1] + LEVEL_MODEL_SIZES[i2]) / 2) {
            i2--;
        } else if (c == 2) {
            int i4 = i2 + 1;
            if (i > (LEVEL_MODEL_SIZES[i2] + LEVEL_MODEL_SIZES[i4]) / 2) {
                i2 = i4;
            }
        }
        return LEVEL_MODEL_SIZES[i2];
    }

    public void addMemoryCacheKeySuffix(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mMemoryCacheKeySuffix == null) {
                this.mMemoryCacheKeySuffix = str;
                return;
            }
            this.mMemoryCacheKeySuffix += str;
        }
    }

    public String getMemoryCacheKey() {
        StringBuilder sb;
        if (this.mMemoryCacheKey == null) {
            if (this.mSchemeInfo.baseName != null) {
                sb = new StringBuilder(this.mSchemeInfo.baseName);
            } else {
                sb = new StringBuilder();
            }
            if (this.mBaseCacheCatalog != 0 || (this.mMaxViewWidth == 0 && this.mMaxViewHeight == 0)) {
                sb.append(this.mBaseCacheCatalog);
            } else {
                sb.append(SizeUtil.mergeWH(findBestLevel(this.mMaxViewWidth), findBestLevel(this.mMaxViewHeight)));
            }
            this.mMemoryCacheKey = sb.toString();
            if (this.mCacheKeyInspector != null) {
                this.mMemoryCacheKey = this.mCacheKeyInspector.inspectMemoryCacheKey(this.mRequestPath, this.mMemoryCacheKey);
            }
            if (!(this.mMemoryCacheKey == null || this.mMemoryCacheKeySuffix == null)) {
                this.mMemoryCacheKey += this.mMemoryCacheKeySuffix;
            }
        }
        return this.mMemoryCacheKey;
    }

    public String getDiskCacheKey() {
        StringBuilder sb;
        if (this.mDiskCacheKey == null) {
            if (this.mSchemeInfo.baseName != null) {
                sb = new StringBuilder(this.mSchemeInfo.baseName);
            } else {
                sb = new StringBuilder();
            }
            sb.append(this.mSchemeInfo.extension);
            this.mDiskCacheKey = sb.toString();
            if (this.mCacheKeyInspector != null) {
                this.mDiskCacheKey = this.mCacheKeyInspector.inspectDiskCacheKey(this.mRequestPath, this.mDiskCacheKey);
            }
        }
        return this.mDiskCacheKey;
    }

    public int getDiskCacheCatalog() {
        if (this.mCacheKeyInspector != null) {
            return this.mCacheKeyInspector.inspectDiskCacheCatalog(this.mRequestPath, this.mBaseCacheCatalog);
        }
        return this.mBaseCacheCatalog;
    }

    public CacheKeyInspector getCacheKeyInspector() {
        return this.mCacheKeyInspector;
    }

    public boolean containsCdnSize() {
        return this.mSchemeInfo.isCdnSize;
    }

    public String getPath() {
        return this.mRequestPath;
    }

    public boolean isLocalUri() {
        return this.mSchemeInfo.isLocalUri();
    }

    public SchemeInfo getSchemeInfo() {
        return this.mSchemeInfo;
    }

    public int getWidth() {
        return this.mSchemeInfo.width;
    }

    public int getHeight() {
        return this.mSchemeInfo.height;
    }

    public String getImageExtension() {
        return this.mSchemeInfo.extension;
    }

    public int getQuality() {
        return this.mSchemeInfo.quality;
    }

    public String toString() {
        return "path: " + this.mRequestPath + "\nscheme info: " + this.mSchemeInfo + "\nbase cache catalog: " + getDiskCacheCatalog() + "\nmemory cache key: " + getMemoryCacheKey() + "\ndisk cache key: " + getDiskCacheKey() + "\ndisk cache catalog: " + getDiskCacheCatalog();
    }
}
