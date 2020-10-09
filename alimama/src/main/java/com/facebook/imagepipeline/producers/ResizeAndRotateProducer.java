package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.memory.PooledByteBufferFactory;
import com.facebook.common.util.TriState;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.JobScheduler;
import com.facebook.imagepipeline.request.ImageRequest;
import com.taobao.weex.common.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class ResizeAndRotateProducer implements Producer<EncodedImage> {
    @VisibleForTesting
    static final int DEFAULT_JPEG_QUALITY = 85;
    private static final String DOWNSAMPLE_ENUMERATOR_KEY = "downsampleEnumerator";
    private static final String FRACTION_KEY = "Fraction";
    private static final int FULL_ROUND = 360;
    /* access modifiers changed from: private */
    public static final ImmutableList<Integer> INVERTED_EXIF_ORIENTATIONS = ImmutableList.of(2, 7, 4, 5);
    @VisibleForTesting
    static final int MAX_JPEG_SCALE_NUMERATOR = 8;
    @VisibleForTesting
    static final int MIN_TRANSFORM_INTERVAL_MS = 100;
    private static final String ORIGINAL_SIZE_KEY = "Original size";
    public static final String PRODUCER_NAME = "ResizeAndRotateProducer";
    private static final String REQUESTED_SIZE_KEY = "Requested size";
    private static final String ROTATION_ANGLE_KEY = "rotationAngle";
    private static final String SOFTWARE_ENUMERATOR_KEY = "softwareEnumerator";
    /* access modifiers changed from: private */
    public final Executor mExecutor;
    private final Producer<EncodedImage> mInputProducer;
    /* access modifiers changed from: private */
    public final PooledByteBufferFactory mPooledByteBufferFactory;
    /* access modifiers changed from: private */
    public final boolean mResizingEnabled;
    /* access modifiers changed from: private */
    public final boolean mUseDownsamplingRatio;

    @VisibleForTesting
    static int roundNumerator(float f, float f2) {
        return (int) (f2 + (f * 8.0f));
    }

    private static boolean shouldResize(int i) {
        return i < 8;
    }

    /* JADX WARNING: type inference failed for: r4v0, types: [com.facebook.imagepipeline.producers.Producer<com.facebook.imagepipeline.image.EncodedImage>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ResizeAndRotateProducer(java.util.concurrent.Executor r1, com.facebook.common.memory.PooledByteBufferFactory r2, boolean r3, com.facebook.imagepipeline.producers.Producer<com.facebook.imagepipeline.image.EncodedImage> r4, boolean r5) {
        /*
            r0 = this;
            r0.<init>()
            java.lang.Object r1 = com.facebook.common.internal.Preconditions.checkNotNull(r1)
            java.util.concurrent.Executor r1 = (java.util.concurrent.Executor) r1
            r0.mExecutor = r1
            java.lang.Object r1 = com.facebook.common.internal.Preconditions.checkNotNull(r2)
            com.facebook.common.memory.PooledByteBufferFactory r1 = (com.facebook.common.memory.PooledByteBufferFactory) r1
            r0.mPooledByteBufferFactory = r1
            r0.mResizingEnabled = r3
            java.lang.Object r1 = com.facebook.common.internal.Preconditions.checkNotNull(r4)
            com.facebook.imagepipeline.producers.Producer r1 = (com.facebook.imagepipeline.producers.Producer) r1
            r0.mInputProducer = r1
            r0.mUseDownsamplingRatio = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.<init>(java.util.concurrent.Executor, com.facebook.common.memory.PooledByteBufferFactory, boolean, com.facebook.imagepipeline.producers.Producer, boolean):void");
    }

    public void produceResults(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        this.mInputProducer.produceResults(new TransformingConsumer(consumer, producerContext), producerContext);
    }

    private class TransformingConsumer extends DelegatingConsumer<EncodedImage, EncodedImage> {
        /* access modifiers changed from: private */
        public boolean mIsCancelled = false;
        /* access modifiers changed from: private */
        public final JobScheduler mJobScheduler;
        /* access modifiers changed from: private */
        public final ProducerContext mProducerContext;

        public TransformingConsumer(final Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            super(consumer);
            this.mProducerContext = producerContext;
            this.mJobScheduler = new JobScheduler(ResizeAndRotateProducer.this.mExecutor, new JobScheduler.JobRunnable(ResizeAndRotateProducer.this) {
                public void run(EncodedImage encodedImage, int i) {
                    TransformingConsumer.this.doTransform(encodedImage, i);
                }
            }, 100);
            this.mProducerContext.addCallbacks(new BaseProducerContextCallbacks(ResizeAndRotateProducer.this) {
                public void onIsIntermediateResultExpectedChanged() {
                    if (TransformingConsumer.this.mProducerContext.isIntermediateResultExpected()) {
                        TransformingConsumer.this.mJobScheduler.scheduleJob();
                    }
                }

                public void onCancellationRequested() {
                    TransformingConsumer.this.mJobScheduler.clearJob();
                    boolean unused = TransformingConsumer.this.mIsCancelled = true;
                    consumer.onCancellation();
                }
            });
        }

        /* access modifiers changed from: protected */
        public void onNewResultImpl(@Nullable EncodedImage encodedImage, int i) {
            if (!this.mIsCancelled) {
                boolean isLast = isLast(i);
                if (encodedImage != null) {
                    TriState access$600 = ResizeAndRotateProducer.shouldTransform(this.mProducerContext.getImageRequest(), encodedImage, ResizeAndRotateProducer.this.mResizingEnabled);
                    if (!isLast && access$600 == TriState.UNSET) {
                        return;
                    }
                    if (access$600 != TriState.YES) {
                        if (!(this.mProducerContext.getImageRequest().getRotationOptions().canDeferUntilRendered() || encodedImage.getRotationAngle() == 0 || encodedImage.getRotationAngle() == -1)) {
                            encodedImage = moveImage(encodedImage);
                            encodedImage.setRotationAngle(0);
                        }
                        getConsumer().onNewResult(encodedImage, i);
                    } else if (this.mJobScheduler.updateJob(encodedImage, i)) {
                        if (isLast || this.mProducerContext.isIntermediateResultExpected()) {
                            this.mJobScheduler.scheduleJob();
                        }
                    }
                } else if (isLast) {
                    getConsumer().onNewResult(null, 1);
                }
            }
        }

        private EncodedImage moveImage(EncodedImage encodedImage) {
            EncodedImage cloneOrNull = EncodedImage.cloneOrNull(encodedImage);
            encodedImage.close();
            return cloneOrNull;
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:60:0x0106 A[Catch:{ all -> 0x0114 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void doTransform(com.facebook.imagepipeline.image.EncodedImage r17, int r18) {
            /*
                r16 = this;
                r8 = r16
                r0 = r17
                com.facebook.imagepipeline.producers.ProducerContext r1 = r8.mProducerContext
                com.facebook.imagepipeline.producers.ProducerListener r1 = r1.getListener()
                com.facebook.imagepipeline.producers.ProducerContext r2 = r8.mProducerContext
                java.lang.String r2 = r2.getId()
                java.lang.String r3 = "ResizeAndRotateProducer"
                r1.onProducerStart(r2, r3)
                com.facebook.imagepipeline.producers.ProducerContext r1 = r8.mProducerContext
                com.facebook.imagepipeline.request.ImageRequest r3 = r1.getImageRequest()
                com.facebook.imagepipeline.producers.ResizeAndRotateProducer r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.this
                com.facebook.common.memory.PooledByteBufferFactory r1 = r1.mPooledByteBufferFactory
                com.facebook.common.memory.PooledByteBufferOutputStream r9 = r1.newOutputStream()
                r10 = 0
                com.facebook.imagepipeline.producers.ResizeAndRotateProducer r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.this     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                boolean r1 = r1.mResizingEnabled     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                int r6 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.getSoftwareNumerator(r3, r0, r1)     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                int r11 = com.facebook.imagepipeline.producers.DownsampleUtil.determineSampleSize(r3, r0)     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                int r5 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.calculateDownsampleNumerator(r11)     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                com.facebook.imagepipeline.producers.ResizeAndRotateProducer r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.this     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                boolean r1 = r1.mUseDownsamplingRatio     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                if (r1 == 0) goto L_0x0042
                r12 = r5
                goto L_0x0043
            L_0x0042:
                r12 = r6
            L_0x0043:
                java.io.InputStream r13 = r17.getInputStream()     // Catch:{ Exception -> 0x00eb, all -> 0x00e8 }
                com.facebook.common.internal.ImmutableList r1 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.INVERTED_EXIF_ORIENTATIONS     // Catch:{ Exception -> 0x00e4 }
                int r2 = r17.getExifOrientation()     // Catch:{ Exception -> 0x00e4 }
                java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x00e4 }
                boolean r1 = r1.contains(r2)     // Catch:{ Exception -> 0x00e4 }
                r14 = 85
                if (r1 == 0) goto L_0x0077
                com.facebook.imagepipeline.common.RotationOptions r1 = r3.getRotationOptions()     // Catch:{ Exception -> 0x00e4 }
                int r15 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.getForceRotatedInvertedExifOrientation(r1, r0)     // Catch:{ Exception -> 0x00e4 }
                r7 = 0
                r1 = r16
                r2 = r17
                r4 = r12
                java.util.Map r1 = r1.getExtraMap(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x00e4 }
                com.facebook.imagepipeline.nativecode.JpegTranscoder.transcodeJpegWithExifOrientation(r13, r9, r15, r12, r14)     // Catch:{ Exception -> 0x0071 }
                goto L_0x008c
            L_0x0071:
                r0 = move-exception
                r3 = r18
                r10 = r1
                goto L_0x00ef
            L_0x0077:
                com.facebook.imagepipeline.common.RotationOptions r1 = r3.getRotationOptions()     // Catch:{ Exception -> 0x00e4 }
                int r15 = com.facebook.imagepipeline.producers.ResizeAndRotateProducer.getRotationAngle(r1, r0)     // Catch:{ Exception -> 0x00e4 }
                r1 = r16
                r2 = r17
                r4 = r12
                r7 = r15
                java.util.Map r1 = r1.getExtraMap(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x00e4 }
                com.facebook.imagepipeline.nativecode.JpegTranscoder.transcodeJpeg(r13, r9, r15, r12, r14)     // Catch:{ Exception -> 0x0071 }
            L_0x008c:
                r10 = r1
                com.facebook.common.memory.PooledByteBuffer r0 = r9.toByteBuffer()     // Catch:{ Exception -> 0x00e4 }
                com.facebook.common.references.CloseableReference r1 = com.facebook.common.references.CloseableReference.of(r0)     // Catch:{ Exception -> 0x00e4 }
                com.facebook.imagepipeline.image.EncodedImage r2 = new com.facebook.imagepipeline.image.EncodedImage     // Catch:{ all -> 0x00db }
                r2.<init>((com.facebook.common.references.CloseableReference<com.facebook.common.memory.PooledByteBuffer>) r1)     // Catch:{ all -> 0x00db }
                com.facebook.imageformat.ImageFormat r0 = com.facebook.imageformat.DefaultImageFormats.JPEG     // Catch:{ all -> 0x00db }
                r2.setImageFormat(r0)     // Catch:{ all -> 0x00db }
                r2.parseMetaData()     // Catch:{ all -> 0x00d2 }
                com.facebook.imagepipeline.producers.ProducerContext r0 = r8.mProducerContext     // Catch:{ all -> 0x00d2 }
                com.facebook.imagepipeline.producers.ProducerListener r0 = r0.getListener()     // Catch:{ all -> 0x00d2 }
                com.facebook.imagepipeline.producers.ProducerContext r3 = r8.mProducerContext     // Catch:{ all -> 0x00d2 }
                java.lang.String r3 = r3.getId()     // Catch:{ all -> 0x00d2 }
                java.lang.String r4 = "ResizeAndRotateProducer"
                r0.onProducerFinishWithSuccess(r3, r4, r10)     // Catch:{ all -> 0x00d2 }
                r0 = 1
                if (r11 == r0) goto L_0x00ba
                r0 = r18 | 16
                r3 = r0
                goto L_0x00bc
            L_0x00ba:
                r3 = r18
            L_0x00bc:
                com.facebook.imagepipeline.producers.Consumer r0 = r16.getConsumer()     // Catch:{ all -> 0x00d0 }
                r0.onNewResult(r2, r3)     // Catch:{ all -> 0x00d0 }
                com.facebook.imagepipeline.image.EncodedImage.closeSafely(r2)     // Catch:{ all -> 0x00d9 }
                com.facebook.common.references.CloseableReference.closeSafely((com.facebook.common.references.CloseableReference<?>) r1)     // Catch:{ Exception -> 0x00e2 }
                com.facebook.common.internal.Closeables.closeQuietly((java.io.InputStream) r13)
                r9.close()
                return
            L_0x00d0:
                r0 = move-exception
                goto L_0x00d5
            L_0x00d2:
                r0 = move-exception
                r3 = r18
            L_0x00d5:
                com.facebook.imagepipeline.image.EncodedImage.closeSafely(r2)     // Catch:{ all -> 0x00d9 }
                throw r0     // Catch:{ all -> 0x00d9 }
            L_0x00d9:
                r0 = move-exception
                goto L_0x00de
            L_0x00db:
                r0 = move-exception
                r3 = r18
            L_0x00de:
                com.facebook.common.references.CloseableReference.closeSafely((com.facebook.common.references.CloseableReference<?>) r1)     // Catch:{ Exception -> 0x00e2 }
                throw r0     // Catch:{ Exception -> 0x00e2 }
            L_0x00e2:
                r0 = move-exception
                goto L_0x00ef
            L_0x00e4:
                r0 = move-exception
                r3 = r18
                goto L_0x00ef
            L_0x00e8:
                r0 = move-exception
                r13 = r10
                goto L_0x0115
            L_0x00eb:
                r0 = move-exception
                r3 = r18
                r13 = r10
            L_0x00ef:
                com.facebook.imagepipeline.producers.ProducerContext r1 = r8.mProducerContext     // Catch:{ all -> 0x0114 }
                com.facebook.imagepipeline.producers.ProducerListener r1 = r1.getListener()     // Catch:{ all -> 0x0114 }
                com.facebook.imagepipeline.producers.ProducerContext r2 = r8.mProducerContext     // Catch:{ all -> 0x0114 }
                java.lang.String r2 = r2.getId()     // Catch:{ all -> 0x0114 }
                java.lang.String r4 = "ResizeAndRotateProducer"
                r1.onProducerFinishWithFailure(r2, r4, r0, r10)     // Catch:{ all -> 0x0114 }
                boolean r1 = isLast(r3)     // Catch:{ all -> 0x0114 }
                if (r1 == 0) goto L_0x010d
                com.facebook.imagepipeline.producers.Consumer r1 = r16.getConsumer()     // Catch:{ all -> 0x0114 }
                r1.onFailure(r0)     // Catch:{ all -> 0x0114 }
            L_0x010d:
                com.facebook.common.internal.Closeables.closeQuietly((java.io.InputStream) r13)
                r9.close()
                return
            L_0x0114:
                r0 = move-exception
            L_0x0115:
                com.facebook.common.internal.Closeables.closeQuietly((java.io.InputStream) r13)
                r9.close()
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.facebook.imagepipeline.producers.ResizeAndRotateProducer.TransformingConsumer.doTransform(com.facebook.imagepipeline.image.EncodedImage, int):void");
        }

        private Map<String, String> getExtraMap(EncodedImage encodedImage, ImageRequest imageRequest, int i, int i2, int i3, int i4) {
            String str;
            String str2;
            if (!this.mProducerContext.getListener().requiresExtraMap(this.mProducerContext.getId())) {
                return null;
            }
            String str3 = encodedImage.getWidth() + Constants.Name.X + encodedImage.getHeight();
            if (imageRequest.getResizeOptions() != null) {
                str = imageRequest.getResizeOptions().width + Constants.Name.X + imageRequest.getResizeOptions().height;
            } else {
                str = "Unspecified";
            }
            if (i > 0) {
                str2 = i + "/8";
            } else {
                str2 = "";
            }
            HashMap hashMap = new HashMap();
            hashMap.put(ResizeAndRotateProducer.ORIGINAL_SIZE_KEY, str3);
            hashMap.put(ResizeAndRotateProducer.REQUESTED_SIZE_KEY, str);
            hashMap.put(ResizeAndRotateProducer.FRACTION_KEY, str2);
            hashMap.put("queueTime", String.valueOf(this.mJobScheduler.getQueuedTime()));
            hashMap.put(ResizeAndRotateProducer.DOWNSAMPLE_ENUMERATOR_KEY, Integer.toString(i2));
            hashMap.put(ResizeAndRotateProducer.SOFTWARE_ENUMERATOR_KEY, Integer.toString(i3));
            hashMap.put(ResizeAndRotateProducer.ROTATION_ANGLE_KEY, Integer.toString(i4));
            return ImmutableMap.copyOf(hashMap);
        }
    }

    /* access modifiers changed from: private */
    public static TriState shouldTransform(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        if (encodedImage == null || encodedImage.getImageFormat() == ImageFormat.UNKNOWN) {
            return TriState.UNSET;
        }
        if (encodedImage.getImageFormat() != DefaultImageFormats.JPEG) {
            return TriState.NO;
        }
        return TriState.valueOf(shouldRotate(imageRequest.getRotationOptions(), encodedImage) || shouldResize(getSoftwareNumerator(imageRequest, encodedImage, z)));
    }

    @VisibleForTesting
    static float determineResizeRatio(ResizeOptions resizeOptions, int i, int i2) {
        if (resizeOptions == null) {
            return 1.0f;
        }
        float f = (float) i;
        float f2 = (float) i2;
        float max = Math.max(((float) resizeOptions.width) / f, ((float) resizeOptions.height) / f2);
        if (f * max > resizeOptions.maxBitmapSize) {
            max = resizeOptions.maxBitmapSize / f;
        }
        return f2 * max > resizeOptions.maxBitmapSize ? resizeOptions.maxBitmapSize / f2 : max;
    }

    /* access modifiers changed from: private */
    public static int getSoftwareNumerator(ImageRequest imageRequest, EncodedImage encodedImage, boolean z) {
        ResizeOptions resizeOptions;
        int i;
        int i2;
        if (!z || (resizeOptions = imageRequest.getResizeOptions()) == null) {
            return 8;
        }
        int rotationAngle = getRotationAngle(imageRequest.getRotationOptions(), encodedImage);
        boolean z2 = false;
        int forceRotatedInvertedExifOrientation = INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation())) ? getForceRotatedInvertedExifOrientation(imageRequest.getRotationOptions(), encodedImage) : 0;
        if (rotationAngle == 90 || rotationAngle == 270 || forceRotatedInvertedExifOrientation == 5 || forceRotatedInvertedExifOrientation == 7) {
            z2 = true;
        }
        if (z2) {
            i = encodedImage.getHeight();
        } else {
            i = encodedImage.getWidth();
        }
        if (z2) {
            i2 = encodedImage.getWidth();
        } else {
            i2 = encodedImage.getHeight();
        }
        int roundNumerator = roundNumerator(determineResizeRatio(resizeOptions, i, i2), resizeOptions.roundUpFraction);
        if (roundNumerator > 8) {
            return 8;
        }
        if (roundNumerator < 1) {
            return 1;
        }
        return roundNumerator;
    }

    /* access modifiers changed from: private */
    public static int getRotationAngle(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (!rotationOptions.rotationEnabled()) {
            return 0;
        }
        int extractOrientationFromMetadata = extractOrientationFromMetadata(encodedImage);
        if (rotationOptions.useImageMetadata()) {
            return extractOrientationFromMetadata;
        }
        return (extractOrientationFromMetadata + rotationOptions.getForcedAngle()) % FULL_ROUND;
    }

    /* access modifiers changed from: private */
    public static int getForceRotatedInvertedExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        int indexOf = INVERTED_EXIF_ORIENTATIONS.indexOf(Integer.valueOf(encodedImage.getExifOrientation()));
        if (indexOf >= 0) {
            int i = 0;
            if (!rotationOptions.useImageMetadata()) {
                i = rotationOptions.getForcedAngle();
            }
            return ((Integer) INVERTED_EXIF_ORIENTATIONS.get((indexOf + (i / 90)) % INVERTED_EXIF_ORIENTATIONS.size())).intValue();
        }
        throw new IllegalArgumentException("Only accepts inverted exif orientations");
    }

    private static int extractOrientationFromMetadata(EncodedImage encodedImage) {
        int rotationAngle = encodedImage.getRotationAngle();
        if (rotationAngle == 90 || rotationAngle == 180 || rotationAngle == 270) {
            return encodedImage.getRotationAngle();
        }
        return 0;
    }

    private static boolean shouldRotate(RotationOptions rotationOptions, EncodedImage encodedImage) {
        return !rotationOptions.canDeferUntilRendered() && (getRotationAngle(rotationOptions, encodedImage) != 0 || shouldRotateUsingExifOrientation(rotationOptions, encodedImage));
    }

    private static boolean shouldRotateUsingExifOrientation(RotationOptions rotationOptions, EncodedImage encodedImage) {
        if (rotationOptions.rotationEnabled() && !rotationOptions.canDeferUntilRendered()) {
            return INVERTED_EXIF_ORIENTATIONS.contains(Integer.valueOf(encodedImage.getExifOrientation()));
        }
        encodedImage.setExifOrientation(0);
        return false;
    }

    @VisibleForTesting
    static int calculateDownsampleNumerator(int i) {
        return Math.max(1, 8 / i);
    }
}
