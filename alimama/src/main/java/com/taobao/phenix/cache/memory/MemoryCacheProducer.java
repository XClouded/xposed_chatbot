package com.taobao.phenix.cache.memory;

import android.graphics.Bitmap;
import android.os.Build;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.pexode.mimetype.MimeType;
import com.taobao.phenix.bitmap.BitmapPool;
import com.taobao.phenix.cache.LruCache;
import com.taobao.phenix.cache.memory.StaticCachedImage;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.entity.DecodedImage;
import com.taobao.phenix.entity.EncodedImage;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.ImageUriInfo;
import com.taobao.rxm.consume.Consumer;
import com.taobao.rxm.produce.BaseChainProducer;
import com.taobao.tcommon.core.Preconditions;

public class MemoryCacheProducer extends BaseChainProducer<PassableBitmapDrawable, DecodedImage, ImageRequest> {
    private static final StaticCachedImage.StaticImageRecycleListener STATIC_IMAGE_RECYCLE_LISTENER = new StaticCachedImage.StaticImageRecycleListener() {
        public void recycle(StaticCachedImage staticCachedImage) {
            BitmapPool build = Phenix.instance().bitmapPoolBuilder().build();
            if (build != null) {
                build.putIntoPool(staticCachedImage);
            }
        }
    };
    private static final String TAG = "MemoryCache";
    private final LruCache<String, CachedRootImage> mMemoryCache;

    public MemoryCacheProducer(LruCache<String, CachedRootImage> lruCache) {
        super(1, 1);
        Preconditions.checkNotNull(lruCache);
        this.mMemoryCache = lruCache;
    }

    private static PassableBitmapDrawable newDrawableWithRootImage(CachedRootImage cachedRootImage, boolean z) {
        return cachedRootImage.newImageDrawableWith(z, Phenix.instance().applicationContext() != null ? Phenix.instance().applicationContext().getResources() : null);
    }

    public static PassableBitmapDrawable getFilteredCache(LruCache<String, CachedRootImage> lruCache, String str, boolean z) {
        CachedRootImage cachedRootImage = lruCache.get(str);
        if (cachedRootImage == null) {
            return null;
        }
        PassableBitmapDrawable newDrawableWithRootImage = newDrawableWithRootImage(cachedRootImage, z);
        if (newDrawableWithRootImage == null) {
            return newDrawableWithRootImage;
        }
        newDrawableWithRootImage.fromMemory(true);
        Bitmap bitmap = newDrawableWithRootImage.getBitmap();
        if (bitmap == null || !bitmap.isRecycled()) {
            return newDrawableWithRootImage;
        }
        lruCache.remove(str);
        UnitedLog.w(TAG, "remove image(exist cache but bitmap is recycled), key=%s, releasable=%b", str, Boolean.valueOf(z));
        return null;
    }

    private void createFullTrack(Consumer<PassableBitmapDrawable, ImageRequest> consumer) {
        if (Phenix.instance().getImageFlowMonitor() != null) {
            Phenix.instance().getImageFlowMonitor().onStart(consumer.getContext().getStatistics());
        }
    }

    /* access modifiers changed from: protected */
    public boolean conductResult(Consumer<PassableBitmapDrawable, ImageRequest> consumer) {
        ImageRequest context = consumer.getContext();
        context.getStatistics().mReqProcessStart = System.currentTimeMillis();
        if (consumer.getContext().isSkipCache()) {
            createFullTrack(consumer);
            UnitedLog.e("Phenix", "start & end ", context);
            return false;
        }
        UnitedLog.e("Phenix", "start", context);
        onConductStart(consumer);
        String memoryCacheKey = context.getMemoryCacheKey();
        boolean isReleasableDrawableSpecified = context.isReleasableDrawableSpecified();
        PassableBitmapDrawable filteredCache = getFilteredCache(this.mMemoryCache, memoryCacheKey, isReleasableDrawableSpecified);
        boolean z = filteredCache != null;
        UnitedLog.d(TAG, context, "read from memcache, result=%B, key=%s", Boolean.valueOf(z), memoryCacheKey);
        if (!z && context.getSecondaryUriInfo() != null) {
            String memoryCacheKey2 = context.getSecondaryUriInfo().getMemoryCacheKey();
            filteredCache = getFilteredCache(this.mMemoryCache, memoryCacheKey2, isReleasableDrawableSpecified);
            Object[] objArr = new Object[2];
            objArr[0] = Boolean.valueOf(filteredCache != null);
            objArr[1] = memoryCacheKey2;
            UnitedLog.d(TAG, context, "secondary read from memcache, result=%B, key=%s", objArr);
            if (filteredCache != null) {
                filteredCache.fromSecondary(true);
                context.disableSecondary();
            }
        }
        onConductFinish(consumer, z);
        if (filteredCache != null) {
            consumer.onNewResult(filteredCache, z);
            context.getStatistics().onMemCacheLookedUp(true);
        } else {
            context.getStatistics().onMemCacheLookedUp(false);
        }
        if (z || !context.isMemoryOnly()) {
            if (!z) {
                createFullTrack(consumer);
            }
            UnitedLog.e("Phenix", "End", context);
            return z;
        }
        consumer.onFailure(new MemOnlyFailedException());
        return true;
    }

    private static CachedRootImage newCachedRootImage(ImageRequest imageRequest, DecodedImage decodedImage, StaticCachedImage.StaticImageRecycleListener staticImageRecycleListener) {
        ImageUriInfo imageUriInfo = imageRequest.getImageUriInfo();
        if (decodedImage.isStaticBitmap()) {
            return new StaticCachedImage(decodedImage.getBitmap(), decodedImage.getBitmapPadding(), imageUriInfo.getMemoryCacheKey(), imageUriInfo.getDiskCacheKey(), imageUriInfo.getDiskCacheCatalog(), imageRequest.getDiskCachePriority()).setStaticImageRecycleListener(staticImageRecycleListener);
        }
        return new AnimatedCachedImage(decodedImage.getAnimatedImage(), imageUriInfo.getMemoryCacheKey(), imageUriInfo.getDiskCacheKey(), imageUriInfo.getDiskCacheCatalog(), imageRequest.getDiskCachePriority());
    }

    public void consumeNewResult(Consumer<PassableBitmapDrawable, ImageRequest> consumer, boolean z, DecodedImage decodedImage) {
        boolean z2;
        CachedRootImage newCachedRootImage;
        ImageRequest context = consumer.getContext();
        boolean isReleasableDrawableSpecified = context.isReleasableDrawableSpecified();
        String memoryCacheKey = context.getMemoryCacheKey();
        CachedRootImage cachedRootImage = null;
        PassableBitmapDrawable filteredCache = context.isSkipCache() ? null : getFilteredCache(this.mMemoryCache, memoryCacheKey, isReleasableDrawableSpecified);
        boolean z3 = filteredCache == null;
        MimeType mimeType = decodedImage.getEncodedImage() != null ? decodedImage.getEncodedImage().getMimeType() : null;
        boolean z4 = Phenix.NO_USE_WEBP_FORMAT && Build.VERSION.SDK_INT == 28 && mimeType != null && (mimeType.isSame(DefaultMimeTypes.WEBP) || mimeType.isSame(DefaultMimeTypes.WEBP_A));
        if (z3) {
            if (z4) {
                newCachedRootImage = newCachedRootImage(context, decodedImage, (StaticCachedImage.StaticImageRecycleListener) null);
            } else {
                newCachedRootImage = newCachedRootImage(context, decodedImage, STATIC_IMAGE_RECYCLE_LISTENER);
            }
            cachedRootImage = newCachedRootImage;
            filteredCache = newDrawableWithRootImage(cachedRootImage, isReleasableDrawableSpecified);
            z2 = context.isMultiplexPipeline() && z && decodedImage.needCached();
            EncodedImage encodedImage = decodedImage.getEncodedImage();
            if (encodedImage != null) {
                filteredCache.fromDisk(encodedImage.fromDisk);
                filteredCache.fromSecondary(encodedImage.isSecondary);
                if (!z) {
                    encodedImage.release();
                }
            }
        } else {
            if (context.isMultiplexPipeline()) {
                UnitedLog.i(TAG, context, "found existing cache before new CachedRootImage with pipeline consume result, key=%s", memoryCacheKey);
            }
            z2 = false;
        }
        context.setWorkThreadEndTime(System.currentTimeMillis());
        context.getStatistics().mRspCbDispatch = context.getWorkThreadEndTime();
        UnitedLog.e("Phenix", "Dispatch Image to UI Thread.", context);
        consumer.onNewResult(filteredCache, z);
        if (z2) {
            UnitedLog.d(TAG, context, "write into memcache with priority=%d, result=%B, value=%s", Integer.valueOf(context.getMemoryCachePriority()), Boolean.valueOf(this.mMemoryCache.put(context.getMemoryCachePriority(), memoryCacheKey, cachedRootImage)), cachedRootImage);
        } else if (z3 && z && decodedImage.needCached()) {
            UnitedLog.i(TAG, context, "skip to write into memcache cause the request is not pipeline, key=%s", memoryCacheKey);
        }
    }
}
