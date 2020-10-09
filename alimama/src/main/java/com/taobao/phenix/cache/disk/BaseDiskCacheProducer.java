package com.taobao.phenix.cache.disk;

import androidx.annotation.NonNull;
import com.taobao.phenix.common.SizeUtil;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.EncodedData;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.loader.StreamResultHandler;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.common.Releasable;
import com.taobao.rxm.produce.BaseChainProducer;

public abstract class BaseDiskCacheProducer<OUT, NEXT_OUT extends Releasable> extends BaseChainProducer<OUT, NEXT_OUT, ImageRequest> {
    protected static final int WR_CACHE_UNAVAILABLE = 3;
    protected static final int WR_CACHE_WRITE_FAILED = 4;
    protected static final int WR_DATA_UNAVAILABLE = 1;
    protected static final int WR_NOT_NEED_CACHE = 2;
    protected static final int WR_SUCCEEDED = 0;
    private final DiskCacheSupplier mDiskCacheSupplier;

    public BaseDiskCacheProducer(int i, int i2, DiskCacheSupplier diskCacheSupplier) {
        super(i, i2);
        this.mDiskCacheSupplier = diskCacheSupplier;
    }

    /* access modifiers changed from: protected */
    public DiskCache getPriorityDiskCache(int i) {
        DiskCache diskCache = this.mDiskCacheSupplier.get(i);
        return diskCache == null ? this.mDiskCacheSupplier.get(17) : diskCache;
    }

    /* access modifiers changed from: protected */
    public int[] getBestLevelAndCatalog(int[] iArr, int i) {
        int[] iArr2 = iArr;
        int splitWidth = SizeUtil.getSplitWidth(i);
        int splitHeight = SizeUtil.getSplitHeight(i);
        int length = iArr2.length;
        int i2 = 0;
        int i3 = -1;
        int i4 = 2;
        int i5 = Integer.MAX_VALUE;
        while (true) {
            if (i2 >= length) {
                break;
            }
            int i6 = iArr2[i2];
            int splitWidth2 = SizeUtil.getSplitWidth(i6) - splitWidth;
            int splitHeight2 = SizeUtil.getSplitHeight(i6) - splitHeight;
            int abs = Math.abs(splitWidth2) + Math.abs(splitHeight2);
            if (abs == 0) {
                i3 = i6;
                i4 = 1;
                break;
            }
            if (i4 == 2 && splitWidth2 > 0 && splitHeight2 > 0) {
                i3 = i6;
                i5 = abs;
                i4 = 4;
            } else if ((i4 != 4 || (splitWidth2 >= 0 && splitHeight2 >= 0)) && abs < i5) {
                i3 = i6;
                i5 = abs;
            }
            i2++;
        }
        return new int[]{i4, i3};
    }

    /* access modifiers changed from: protected */
    public long getCacheLength(int i, String str, int i2) {
        DiskCache priorityDiskCache = getPriorityDiskCache(i);
        if (priorityDiskCache.open(Phenix.instance().applicationContext())) {
            return priorityDiskCache.getLength(str, i2);
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    public EncodedData getCacheResult(@NonNull ImageRequest imageRequest, String str, int i, int[] iArr) {
        ResponseData cacheResponse = getCacheResponse(imageRequest.getDiskCachePriority(), str, i, iArr);
        if (cacheResponse == null) {
            return null;
        }
        try {
            return EncodedData.transformFrom(cacheResponse, (StreamResultHandler) null);
        } catch (Exception e) {
            UnitedLog.e("DiskCache", imageRequest, "transform data from response[type:%d] error=%s", Integer.valueOf(cacheResponse.type), e);
            return null;
        }
    }

    private ResponseData getCacheResponse(int i, String str, int i2, int[] iArr) {
        int i3;
        DiskCache priorityDiskCache = getPriorityDiskCache(i);
        if (!priorityDiskCache.open(Phenix.instance().applicationContext())) {
            UnitedLog.w("DiskCache", "%s open failed in DiskCacheReader", priorityDiskCache);
            return null;
        } else if (!priorityDiskCache.isSupportCatalogs() || (i3 = iArr[0]) == 1) {
            return priorityDiskCache.get(str, i2);
        } else {
            int[] catalogs = priorityDiskCache.getCatalogs(str);
            if (catalogs == null || catalogs.length <= 0) {
                UnitedLog.d("DiskCache", "find catalogs failed, key=%s", str);
                return null;
            }
            int[] bestLevelAndCatalog = getBestLevelAndCatalog(catalogs, i2);
            int i4 = bestLevelAndCatalog[0];
            if (!ImageRequest.isAllowedSizeLevel(i3, i4)) {
                return null;
            }
            iArr[0] = i4;
            UnitedLog.d("DiskCache", "find best size level=%d, actual=%d, target=%d, key=%s", Integer.valueOf(i4), Integer.valueOf(SizeUtil.getSplitWidth(bestLevelAndCatalog[1])), Integer.valueOf(SizeUtil.getSplitWidth(i2)), str);
            return priorityDiskCache.get(str, bestLevelAndCatalog[1]);
        }
    }

    /* access modifiers changed from: protected */
    public int writeImage(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        int i = 3;
        if (!encodedImage.isAvailable()) {
            UnitedLog.d("DiskCache", imageRequest, "write skipped, because encode data not available, key=%s, catalog=%d", imageRequest.getDiskCacheKey(), Integer.valueOf(imageRequest.getDiskCacheCatalog()));
            i = 1;
        } else if (encodedImage.notNeedCache()) {
            UnitedLog.d("DiskCache", imageRequest, "write skipped, because encode data not need to be cached(fromDisk=%b, fromScale=%b), key=%s, catalog=%d", Boolean.valueOf(encodedImage.fromDisk), Boolean.valueOf(encodedImage.fromScale), imageRequest.getDiskCacheKey(), Integer.valueOf(imageRequest.getDiskCacheCatalog()));
            i = 2;
        } else {
            DiskCache priorityDiskCache = getPriorityDiskCache(imageRequest.getDiskCachePriority());
            if (priorityDiskCache.open(Phenix.instance().applicationContext())) {
                boolean put = priorityDiskCache.put(imageRequest.getDiskCacheKey(), imageRequest.getDiskCacheCatalog(), encodedImage.bytes, encodedImage.offset, encodedImage.length);
                int i2 = !put ? 4 : 0;
                UnitedLog.d("DiskCache", imageRequest, "write result=%B，priority=%d, key=%s, catalog=%d", Boolean.valueOf(put), Integer.valueOf(imageRequest.getDiskCachePriority()), imageRequest.getDiskCacheKey(), Integer.valueOf(imageRequest.getDiskCacheCatalog()));
                i = i2;
            } else {
                UnitedLog.w("DiskCache", "%s open failed in DiskCacheWriter", priorityDiskCache);
            }
        }
        if (z) {
            encodedImage.release();
        }
        return i;
    }
}
