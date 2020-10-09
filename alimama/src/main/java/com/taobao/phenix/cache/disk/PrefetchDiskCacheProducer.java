package com.taobao.phenix.cache.disk;

import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.entity.PrefetchImage;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.ImageStatistics;
import com.taobao.rxm.consume.Consumer;

public class PrefetchDiskCacheProducer extends BaseDiskCacheProducer<PrefetchImage, EncodedImage> {
    public PrefetchDiskCacheProducer(DiskCacheSupplier diskCacheSupplier) {
        super(1, 1, diskCacheSupplier);
    }

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<PrefetchImage, ImageRequest> consumer) {
        ImageRequest context = consumer.getContext();
        context.getStatistics().mReqProcessStart = System.currentTimeMillis();
        if (Phenix.instance().getImageFlowMonitor() != null) {
            Phenix.instance().getImageFlowMonitor().onStart(consumer.getContext().getStatistics());
        }
        boolean z = false;
        if (context.isSkipCache()) {
            return false;
        }
        onConductStart(consumer);
        long cacheLength = getCacheLength(context.getDiskCachePriority(), context.getDiskCacheKey(), context.getDiskCacheCatalog());
        if (cacheLength > 0) {
            z = true;
        }
        onConductFinish(consumer, z);
        if (z) {
            context.getStatistics().mRspProcessStart = System.currentTimeMillis();
            context.getStatistics().mRspCbDispatch = System.currentTimeMillis();
            context.getStatistics().mRspDeflateSize = cacheLength;
            PrefetchImage prefetchImage = new PrefetchImage();
            prefetchImage.fromDisk = true;
            prefetchImage.length = cacheLength;
            prefetchImage.url = context.getPath();
            consumer.onNewResult(prefetchImage, true);
        }
        if (z || !context.isOnlyCache()) {
            return z;
        }
        consumer.onFailure(new OnlyCacheFailedException("PrefetchDiskCache"));
        return true;
    }

    public void consumeNewResult(Consumer<PrefetchImage, ImageRequest> consumer, boolean z, EncodedImage encodedImage) {
        ImageRequest context = consumer.getContext();
        ImageStatistics statistics = context.getStatistics();
        statistics.setCompressFormat(encodedImage.getMimeType());
        statistics.setSize(encodedImage.length);
        int writeImage = writeImage(context, encodedImage, true);
        if (writeImage == 1 || writeImage == 2 || writeImage == 0) {
            PrefetchImage prefetchImage = new PrefetchImage();
            prefetchImage.fromDisk = encodedImage.fromDisk;
            prefetchImage.length = (long) encodedImage.length;
            prefetchImage.url = encodedImage.path;
            context.getStatistics().mRspCbDispatch = System.currentTimeMillis();
            consumer.onNewResult(prefetchImage, z);
            return;
        }
        DiskCache priorityDiskCache = getPriorityDiskCache(context.getDiskCachePriority());
        String path = context.getPath();
        if (writeImage == 3) {
            consumer.onFailure(new CacheUnavailableException(priorityDiskCache, path));
        } else {
            consumer.onFailure(new CacheWriteFailedException(priorityDiskCache, path));
        }
    }
}
