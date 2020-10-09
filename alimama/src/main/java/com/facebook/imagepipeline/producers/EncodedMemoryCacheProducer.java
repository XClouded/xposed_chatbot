package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;

public class EncodedMemoryCacheProducer implements Producer<EncodedImage> {
    public static final String EXTRA_CACHED_VALUE_FOUND = "cached_value_found";
    public static final String PRODUCER_NAME = "EncodedMemoryCacheProducer";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer<EncodedImage> mInputProducer;
    private final MemoryCache<CacheKey, PooledByteBuffer> mMemoryCache;

    public EncodedMemoryCacheProducer(MemoryCache<CacheKey, PooledByteBuffer> memoryCache, CacheKeyFactory cacheKeyFactory, Producer<EncodedImage> producer) {
        this.mMemoryCache = memoryCache;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mInputProducer = producer;
    }

    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        EncodedImage encodedImage;
        String id = producerContext.getId();
        ProducerListener listener = producerContext.getListener();
        listener.onProducerStart(id, PRODUCER_NAME);
        CacheKey encodedCacheKey = this.mCacheKeyFactory.getEncodedCacheKey(producerContext.getImageRequest(), producerContext.getCallerContext());
        CloseableReference<PooledByteBuffer> closeableReference = this.mMemoryCache.get(encodedCacheKey);
        Map map = null;
        if (closeableReference != null) {
            try {
                encodedImage = new EncodedImage(closeableReference);
                if (listener.requiresExtraMap(id)) {
                    map = ImmutableMap.of("cached_value_found", "true");
                }
                listener.onProducerFinishWithSuccess(id, PRODUCER_NAME, map);
                listener.onUltimateProducerReached(id, PRODUCER_NAME, true);
                consumer.onProgressUpdate(1.0f);
                consumer.onNewResult(encodedImage, 1);
                EncodedImage.closeSafely(encodedImage);
                CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
            } catch (Throwable th) {
                CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
                throw th;
            }
        } else if (producerContext.getLowestPermittedRequestLevel().getValue() >= ImageRequest.RequestLevel.ENCODED_MEMORY_CACHE.getValue()) {
            listener.onProducerFinishWithSuccess(id, PRODUCER_NAME, listener.requiresExtraMap(id) ? ImmutableMap.of("cached_value_found", "false") : null);
            listener.onUltimateProducerReached(id, PRODUCER_NAME, false);
            consumer.onNewResult(null, 1);
            CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
        } else {
            EncodedMemoryCacheConsumer encodedMemoryCacheConsumer = new EncodedMemoryCacheConsumer(consumer, this.mMemoryCache, encodedCacheKey);
            if (listener.requiresExtraMap(id)) {
                map = ImmutableMap.of("cached_value_found", "false");
            }
            listener.onProducerFinishWithSuccess(id, PRODUCER_NAME, map);
            this.mInputProducer.produceResults(encodedMemoryCacheConsumer, producerContext);
            CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
        }
    }

    private static class EncodedMemoryCacheConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final MemoryCache<CacheKey, PooledByteBuffer> mMemoryCache;
        private final CacheKey mRequestedCacheKey;

        public EncodedMemoryCacheConsumer(Consumer<EncodedImage> consumer, MemoryCache<CacheKey, PooledByteBuffer> memoryCache, CacheKey cacheKey) {
            super(consumer);
            this.mMemoryCache = memoryCache;
            this.mRequestedCacheKey = cacheKey;
        }

        public void onNewResultImpl(EncodedImage encodedImage, int i) {
            if (isNotLast(i) || encodedImage == null || statusHasAnyFlag(i, 10)) {
                getConsumer().onNewResult(encodedImage, i);
                return;
            }
            CloseableReference<PooledByteBuffer> byteBufferRef = encodedImage.getByteBufferRef();
            if (byteBufferRef != null) {
                try {
                    CloseableReference<PooledByteBuffer> cache = this.mMemoryCache.cache(this.mRequestedCacheKey, byteBufferRef);
                    if (cache != null) {
                        try {
                            EncodedImage encodedImage2 = new EncodedImage(cache);
                            encodedImage2.copyMetaDataFrom(encodedImage);
                            try {
                                getConsumer().onProgressUpdate(1.0f);
                                getConsumer().onNewResult(encodedImage2, i);
                                return;
                            } finally {
                                EncodedImage.closeSafely(encodedImage2);
                            }
                        } finally {
                            CloseableReference.closeSafely((CloseableReference<?>) cache);
                        }
                    }
                } finally {
                    CloseableReference.closeSafely((CloseableReference<?>) byteBufferRef);
                }
            }
            getConsumer().onNewResult(encodedImage, i);
        }
    }
}
