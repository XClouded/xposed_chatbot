package com.facebook.imagepipeline.producers;

import bolts.Continuation;
import bolts.Task;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.imagepipeline.cache.BufferedDiskCache;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MediaVariationsIndex;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.MediaVariations;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MediaVariationsFallbackProducer implements Producer<EncodedImage> {
    public static final String EXTRA_CACHED_VALUE_FOUND = "cached_value_found";
    public static final String EXTRA_CACHED_VALUE_USED_AS_LAST = "cached_value_used_as_last";
    public static final String EXTRA_VARIANTS_COUNT = "variants_count";
    public static final String EXTRA_VARIANTS_SOURCE = "variants_source";
    public static final String PRODUCER_NAME = "MediaVariationsFallbackProducer";
    /* access modifiers changed from: private */
    public final CacheKeyFactory mCacheKeyFactory;
    private final BufferedDiskCache mDefaultBufferedDiskCache;
    private final Producer<EncodedImage> mInputProducer;
    /* access modifiers changed from: private */
    public final MediaVariationsIndex mMediaVariationsIndex;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;

    public MediaVariationsFallbackProducer(BufferedDiskCache bufferedDiskCache, BufferedDiskCache bufferedDiskCache2, CacheKeyFactory cacheKeyFactory, MediaVariationsIndex mediaVariationsIndex, Producer<EncodedImage> producer) {
        this.mDefaultBufferedDiskCache = bufferedDiskCache;
        this.mSmallImageBufferedDiskCache = bufferedDiskCache2;
        this.mCacheKeyFactory = cacheKeyFactory;
        this.mMediaVariationsIndex = mediaVariationsIndex;
        this.mInputProducer = producer;
    }

    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        final ImageRequest imageRequest = producerContext.getImageRequest();
        final ResizeOptions resizeOptions = imageRequest.getResizeOptions();
        final MediaVariations mediaVariations = imageRequest.getMediaVariations();
        if (!imageRequest.isDiskCacheEnabled() || resizeOptions == null || resizeOptions.height <= 0 || resizeOptions.width <= 0 || imageRequest.getBytesRange() != null) {
            startInputProducerWithExistingConsumer(consumer, producerContext);
        } else if (mediaVariations == null) {
            startInputProducerWithExistingConsumer(consumer, producerContext);
        } else {
            producerContext.getListener().onProducerStart(producerContext.getId(), PRODUCER_NAME);
            AtomicBoolean atomicBoolean = new AtomicBoolean(false);
            if (mediaVariations.getVariantsCount() > 0) {
                chooseFromVariants(consumer, producerContext, imageRequest, mediaVariations, resizeOptions, atomicBoolean);
            } else {
                final Consumer<EncodedImage> consumer2 = consumer;
                final ProducerContext producerContext2 = producerContext;
                final AtomicBoolean atomicBoolean2 = atomicBoolean;
                this.mMediaVariationsIndex.getCachedVariants(mediaVariations.getMediaId(), MediaVariations.newBuilderForMediaId(mediaVariations.getMediaId()).setForceRequestForSpecifiedUri(mediaVariations.shouldForceRequestForSpecifiedUri()).setSource(MediaVariations.SOURCE_INDEX_DB)).continueWith(new Continuation<MediaVariations, Object>() {
                    public Object then(Task<MediaVariations> task) throws Exception {
                        if (task.isCancelled() || task.isFaulted()) {
                            return task;
                        }
                        try {
                            if (task.getResult() != null) {
                                return MediaVariationsFallbackProducer.this.chooseFromVariants(consumer2, producerContext2, imageRequest, task.getResult(), resizeOptions, atomicBoolean2);
                            }
                            MediaVariationsFallbackProducer.this.startInputProducerWithWrappedConsumer(consumer2, producerContext2, mediaVariations.getMediaId());
                            return null;
                        } catch (Exception unused) {
                            return null;
                        }
                    }
                });
            }
            subscribeTaskForRequestCancellation(atomicBoolean, producerContext);
        }
    }

    /* access modifiers changed from: private */
    public Task chooseFromVariants(Consumer<EncodedImage> consumer, ProducerContext producerContext, ImageRequest imageRequest, MediaVariations mediaVariations, ResizeOptions resizeOptions, AtomicBoolean atomicBoolean) {
        if (mediaVariations.getVariantsCount() == 0) {
            return Task.forResult(null).continueWith(onFinishDiskReads(consumer, producerContext, imageRequest, mediaVariations, Collections.emptyList(), 0, atomicBoolean));
        }
        return attemptCacheReadForVariant(consumer, producerContext, imageRequest, mediaVariations, mediaVariations.getSortedVariants(new VariantComparator(resizeOptions)), 0, atomicBoolean);
    }

    /* access modifiers changed from: private */
    public Task attemptCacheReadForVariant(Consumer<EncodedImage> consumer, ProducerContext producerContext, ImageRequest imageRequest, MediaVariations mediaVariations, List<MediaVariations.Variant> list, int i, AtomicBoolean atomicBoolean) {
        ImageRequest.CacheChoice cacheChoice;
        MediaVariations.Variant variant = list.get(i);
        CacheKey encodedCacheKey = this.mCacheKeyFactory.getEncodedCacheKey(imageRequest, variant.getUri(), producerContext.getCallerContext());
        if (variant.getCacheChoice() == null) {
            cacheChoice = imageRequest.getCacheChoice();
        } else {
            cacheChoice = variant.getCacheChoice();
        }
        return (cacheChoice == ImageRequest.CacheChoice.SMALL ? this.mSmallImageBufferedDiskCache : this.mDefaultBufferedDiskCache).get(encodedCacheKey, atomicBoolean).continueWith(onFinishDiskReads(consumer, producerContext, imageRequest, mediaVariations, list, i, atomicBoolean));
    }

    /* access modifiers changed from: private */
    public static boolean isBigEnoughForRequestedSize(MediaVariations.Variant variant, ResizeOptions resizeOptions) {
        return variant.getWidth() >= resizeOptions.width && variant.getHeight() >= resizeOptions.height;
    }

    private Continuation<EncodedImage, Void> onFinishDiskReads(Consumer<EncodedImage> consumer, ProducerContext producerContext, ImageRequest imageRequest, MediaVariations mediaVariations, List<MediaVariations.Variant> list, int i, AtomicBoolean atomicBoolean) {
        final String id = producerContext.getId();
        final ProducerListener listener = producerContext.getListener();
        final Consumer<EncodedImage> consumer2 = consumer;
        final ProducerContext producerContext2 = producerContext;
        final MediaVariations mediaVariations2 = mediaVariations;
        final List<MediaVariations.Variant> list2 = list;
        final int i2 = i;
        final ImageRequest imageRequest2 = imageRequest;
        final AtomicBoolean atomicBoolean2 = atomicBoolean;
        return new Continuation<EncodedImage, Void>() {
            /* JADX WARNING: Removed duplicated region for block: B:28:0x00fc  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Void then(bolts.Task<com.facebook.imagepipeline.image.EncodedImage> r14) throws java.lang.Exception {
                /*
                    r13 = this;
                    boolean r0 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.isTaskCancelled(r14)
                    r1 = 0
                    r2 = 0
                    r3 = 1
                    if (r0 == 0) goto L_0x001b
                    com.facebook.imagepipeline.producers.ProducerListener r14 = r2
                    java.lang.String r0 = r3
                    java.lang.String r3 = "MediaVariationsFallbackProducer"
                    r14.onProducerFinishWithCancellation(r0, r3, r1)
                    com.facebook.imagepipeline.producers.Consumer r14 = r4
                    r14.onCancellation()
                L_0x0017:
                    r14 = 0
                    r3 = 0
                    goto L_0x00fa
                L_0x001b:
                    boolean r0 = r14.isFaulted()
                    if (r0 == 0) goto L_0x0040
                    com.facebook.imagepipeline.producers.ProducerListener r0 = r2
                    java.lang.String r4 = r3
                    java.lang.String r5 = "MediaVariationsFallbackProducer"
                    java.lang.Exception r14 = r14.getError()
                    r0.onProducerFinishWithFailure(r4, r5, r14, r1)
                    com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer r14 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.this
                    com.facebook.imagepipeline.producers.Consumer r0 = r4
                    com.facebook.imagepipeline.producers.ProducerContext r4 = r5
                    com.facebook.imagepipeline.request.MediaVariations r5 = r6
                    java.lang.String r5 = r5.getMediaId()
                    r14.startInputProducerWithWrappedConsumer(r0, r4, r5)
                L_0x003d:
                    r14 = 1
                    goto L_0x00fa
                L_0x0040:
                    java.lang.Object r14 = r14.getResult()
                    com.facebook.imagepipeline.image.EncodedImage r14 = (com.facebook.imagepipeline.image.EncodedImage) r14
                    if (r14 == 0) goto L_0x00b7
                    com.facebook.imagepipeline.request.MediaVariations r0 = r6
                    boolean r0 = r0.shouldForceRequestForSpecifiedUri()
                    if (r0 != 0) goto L_0x0068
                    java.util.List r0 = r7
                    int r4 = r8
                    java.lang.Object r0 = r0.get(r4)
                    com.facebook.imagepipeline.request.MediaVariations$Variant r0 = (com.facebook.imagepipeline.request.MediaVariations.Variant) r0
                    com.facebook.imagepipeline.request.ImageRequest r4 = r9
                    com.facebook.imagepipeline.common.ResizeOptions r4 = r4.getResizeOptions()
                    boolean r0 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.isBigEnoughForRequestedSize(r0, r4)
                    if (r0 == 0) goto L_0x0068
                    r0 = 1
                    goto L_0x0069
                L_0x0068:
                    r0 = 0
                L_0x0069:
                    com.facebook.imagepipeline.producers.ProducerListener r10 = r2
                    java.lang.String r11 = r3
                    java.lang.String r12 = "MediaVariationsFallbackProducer"
                    com.facebook.imagepipeline.producers.ProducerListener r4 = r2
                    java.lang.String r5 = r3
                    r6 = 1
                    java.util.List r7 = r7
                    int r7 = r7.size()
                    com.facebook.imagepipeline.request.MediaVariations r8 = r6
                    java.lang.String r8 = r8.getSource()
                    r9 = r0
                    java.util.Map r4 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.getExtraMap(r4, r5, r6, r7, r8, r9)
                    r10.onProducerFinishWithSuccess(r11, r12, r4)
                    if (r0 == 0) goto L_0x009a
                    com.facebook.imagepipeline.producers.ProducerListener r4 = r2
                    java.lang.String r5 = r3
                    java.lang.String r6 = "MediaVariationsFallbackProducer"
                    r4.onUltimateProducerReached(r5, r6, r3)
                    com.facebook.imagepipeline.producers.Consumer r4 = r4
                    r5 = 1065353216(0x3f800000, float:1.0)
                    r4.onProgressUpdate(r5)
                L_0x009a:
                    int r4 = com.facebook.imagepipeline.producers.BaseConsumer.simpleStatusForIsLast(r0)
                    r5 = 2
                    int r4 = com.facebook.imagepipeline.producers.BaseConsumer.turnOnStatusFlag(r4, r5)
                    if (r0 != 0) goto L_0x00aa
                    r5 = 4
                    int r4 = com.facebook.imagepipeline.producers.BaseConsumer.turnOnStatusFlag(r4, r5)
                L_0x00aa:
                    com.facebook.imagepipeline.producers.Consumer r5 = r4
                    r5.onNewResult(r14, r4)
                    r14.close()
                    r14 = r0 ^ 1
                    r3 = r14
                    r14 = 0
                    goto L_0x00fa
                L_0x00b7:
                    int r14 = r8
                    java.util.List r0 = r7
                    int r0 = r0.size()
                    int r0 = r0 - r3
                    if (r14 >= r0) goto L_0x00d9
                    com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer r4 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.this
                    com.facebook.imagepipeline.producers.Consumer r5 = r4
                    com.facebook.imagepipeline.producers.ProducerContext r6 = r5
                    com.facebook.imagepipeline.request.ImageRequest r7 = r9
                    com.facebook.imagepipeline.request.MediaVariations r8 = r6
                    java.util.List r9 = r7
                    int r14 = r8
                    int r10 = r14 + 1
                    java.util.concurrent.atomic.AtomicBoolean r11 = r10
                    bolts.Task unused = r4.attemptCacheReadForVariant(r5, r6, r7, r8, r9, r10, r11)
                    goto L_0x0017
                L_0x00d9:
                    com.facebook.imagepipeline.producers.ProducerListener r14 = r2
                    java.lang.String r0 = r3
                    java.lang.String r4 = "MediaVariationsFallbackProducer"
                    com.facebook.imagepipeline.producers.ProducerListener r5 = r2
                    java.lang.String r6 = r3
                    r7 = 0
                    java.util.List r8 = r7
                    int r8 = r8.size()
                    com.facebook.imagepipeline.request.MediaVariations r9 = r6
                    java.lang.String r9 = r9.getSource()
                    r10 = 0
                    java.util.Map r5 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.getExtraMap(r5, r6, r7, r8, r9, r10)
                    r14.onProducerFinishWithSuccess(r0, r4, r5)
                    goto L_0x003d
                L_0x00fa:
                    if (r3 == 0) goto L_0x0120
                    com.facebook.imagepipeline.producers.ProducerContext r0 = r5
                    boolean r0 = r0.isIntermediateResultExpected()
                    if (r0 == 0) goto L_0x0111
                    if (r14 != 0) goto L_0x0111
                    com.facebook.imagepipeline.producers.SettableProducerContext r14 = new com.facebook.imagepipeline.producers.SettableProducerContext
                    com.facebook.imagepipeline.producers.ProducerContext r0 = r5
                    r14.<init>(r0)
                    r14.setIsIntermediateResultExpected(r2)
                    goto L_0x0113
                L_0x0111:
                    com.facebook.imagepipeline.producers.ProducerContext r14 = r5
                L_0x0113:
                    com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer r0 = com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.this
                    com.facebook.imagepipeline.producers.Consumer r2 = r4
                    com.facebook.imagepipeline.request.MediaVariations r3 = r6
                    java.lang.String r3 = r3.getMediaId()
                    r0.startInputProducerWithWrappedConsumer(r2, r14, r3)
                L_0x0120:
                    return r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.MediaVariationsFallbackProducer.AnonymousClass2.then(bolts.Task):java.lang.Void");
            }
        };
    }

    private void startInputProducerWithExistingConsumer(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        this.mInputProducer.produceResults(consumer, producerContext);
    }

    /* access modifiers changed from: private */
    public void startInputProducerWithWrappedConsumer(Consumer<EncodedImage> consumer, ProducerContext producerContext, String str) {
        this.mInputProducer.produceResults(new MediaVariationsConsumer(consumer, producerContext, str), producerContext);
    }

    /* access modifiers changed from: private */
    public static boolean isTaskCancelled(Task<?> task) {
        return task.isCancelled() || (task.isFaulted() && (task.getError() instanceof CancellationException));
    }

    @VisibleForTesting
    static Map<String, String> getExtraMap(ProducerListener producerListener, String str, boolean z, int i, String str2, boolean z2) {
        if (!producerListener.requiresExtraMap(str)) {
            return null;
        }
        if (z) {
            return ImmutableMap.of("cached_value_found", String.valueOf(true), EXTRA_CACHED_VALUE_USED_AS_LAST, String.valueOf(z2), EXTRA_VARIANTS_COUNT, String.valueOf(i), EXTRA_VARIANTS_SOURCE, str2);
        }
        return ImmutableMap.of("cached_value_found", String.valueOf(false), EXTRA_VARIANTS_COUNT, String.valueOf(i), EXTRA_VARIANTS_SOURCE, str2);
    }

    private void subscribeTaskForRequestCancellation(final AtomicBoolean atomicBoolean, ProducerContext producerContext) {
        producerContext.addCallbacks(new BaseProducerContextCallbacks() {
            public void onCancellationRequested() {
                atomicBoolean.set(true);
            }
        });
    }

    @VisibleForTesting
    class MediaVariationsConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        private final String mMediaId;
        private final ProducerContext mProducerContext;

        public MediaVariationsConsumer(Consumer<EncodedImage> consumer, ProducerContext producerContext, String str) {
            super(consumer);
            this.mProducerContext = producerContext;
            this.mMediaId = str;
        }

        /* access modifiers changed from: protected */
        public void onNewResultImpl(EncodedImage encodedImage, int i) {
            if (isLast(i) && encodedImage != null && !statusHasFlag(i, 8)) {
                storeResultInDatabase(encodedImage);
            }
            getConsumer().onNewResult(encodedImage, i);
        }

        private void storeResultInDatabase(EncodedImage encodedImage) {
            ImageRequest.CacheChoice cacheChoice;
            ImageRequest imageRequest = this.mProducerContext.getImageRequest();
            if (imageRequest.isDiskCacheEnabled() && this.mMediaId != null) {
                if (imageRequest.getCacheChoice() == null) {
                    cacheChoice = ImageRequest.CacheChoice.DEFAULT;
                } else {
                    cacheChoice = imageRequest.getCacheChoice();
                }
                MediaVariationsFallbackProducer.this.mMediaVariationsIndex.saveCachedVariant(this.mMediaId, cacheChoice, MediaVariationsFallbackProducer.this.mCacheKeyFactory.getEncodedCacheKey(imageRequest, this.mProducerContext.getCallerContext()), encodedImage);
            }
        }
    }

    @VisibleForTesting
    static class VariantComparator implements Comparator<MediaVariations.Variant> {
        private final ResizeOptions mResizeOptions;

        VariantComparator(ResizeOptions resizeOptions) {
            this.mResizeOptions = resizeOptions;
        }

        public int compare(MediaVariations.Variant variant, MediaVariations.Variant variant2) {
            boolean access$300 = MediaVariationsFallbackProducer.isBigEnoughForRequestedSize(variant, this.mResizeOptions);
            boolean access$3002 = MediaVariationsFallbackProducer.isBigEnoughForRequestedSize(variant2, this.mResizeOptions);
            if (access$300 && access$3002) {
                return variant.getWidth() - variant2.getWidth();
            }
            if (access$300) {
                return -1;
            }
            if (access$3002) {
                return 1;
            }
            return variant2.getWidth() - variant.getWidth();
        }
    }
}
