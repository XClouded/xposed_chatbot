package com.taobao.phenix.cache.disk;

import android.text.TextUtils;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.DecodedImage;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.rxm.consume.Consumer;
import com.taobao.tcommon.log.FLog;
import java.util.Map;

public class DiskCacheWriter extends BaseDiskCacheProducer<DecodedImage, DecodedImage> {
    private DiskCacheKeyValueStore mDiskKV;

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<DecodedImage, ImageRequest> consumer) {
        return false;
    }

    public DiskCacheWriter(DiskCacheSupplier diskCacheSupplier) {
        super(0, 2, diskCacheSupplier);
    }

    public DiskCacheWriter(DiskCacheSupplier diskCacheSupplier, DiskCacheKeyValueStore diskCacheKeyValueStore) {
        super(0, 2, diskCacheSupplier);
        this.mDiskKV = diskCacheKeyValueStore;
    }

    public void consumeNewResult(Consumer<DecodedImage, ImageRequest> consumer, boolean z, DecodedImage decodedImage) {
        UnitedLog.e("Phenix", "DiskCache Writer Started.", consumer.getContext());
        consumer.onNewResult(decodedImage, z);
        writeImage(consumer.getContext(), decodedImage.getEncodedImage(), true);
        if (isTTLValid(consumer.getContext())) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                String str = consumer.getContext().getLoaderExtras().get("max-age");
                if (!TextUtils.isEmpty(str) && TextUtils.isDigitsOnly(str)) {
                    String str2 = consumer.getContext().getDiskCacheKey() + consumer.getContext().getDiskCacheCatalog();
                    long longValue = Long.valueOf(str).longValue();
                    consumer.getContext().getStatistics().mReportTTLException = !(this.mDiskKV.isExpectedTime(longValue) ? this.mDiskKV.put(str2, longValue) : false);
                    consumer.getContext().getStatistics().mTTLPutTime = System.currentTimeMillis() - currentTimeMillis;
                }
                UnitedLog.e("Phenix", "DiskCache Writer Put TTL Time", consumer.getContext());
            } catch (Exception e) {
                FLog.e("TTL", "ttl put error=%s", e);
            }
        }
        UnitedLog.e("Phenix", "DiskCache Writer Ended.", consumer.getContext());
    }

    private boolean isTTLValid(ImageRequest imageRequest) {
        Map<String, String> loaderExtras = imageRequest.getLoaderExtras();
        return this.mDiskKV != null && loaderExtras != null && !TextUtils.isEmpty(loaderExtras.get("max-age")) && this.mDiskKV.isTTLDomain(imageRequest.getPath());
    }
}
