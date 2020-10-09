package com.facebook.drawee.backends.pipeline;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableList;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Supplier;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawable.base.DrawableWithCaches;
import com.facebook.drawee.components.DeferredReleaser;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.debug.DebugControllerOverlayDrawable;
import com.facebook.drawee.drawable.OrientedDrawable;
import com.facebook.drawee.drawable.ScaleTypeDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.drawable.DrawableFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.ImageInfo;
import java.util.Iterator;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public class PipelineDraweeController extends AbstractDraweeController<CloseableReference<CloseableImage>, ImageInfo> {
    private static final Class<?> TAG = PipelineDraweeController.class;
    /* access modifiers changed from: private */
    public final DrawableFactory mAnimatedDrawableFactory;
    private CacheKey mCacheKey;
    @Nullable
    private ImmutableList<DrawableFactory> mCustomDrawableFactories;
    private Supplier<DataSource<CloseableReference<CloseableImage>>> mDataSourceSupplier;
    private final DrawableFactory mDefaultDrawableFactory;
    private boolean mDrawDebugOverlay;
    @Nullable
    private final ImmutableList<DrawableFactory> mGlobalDrawableFactories;
    @Nullable
    private MemoryCache<CacheKey, CloseableImage> mMemoryCache;
    /* access modifiers changed from: private */
    public final Resources mResources;

    /* access modifiers changed from: private */
    public static boolean hasTransformableRotationAngle(CloseableStaticBitmap closeableStaticBitmap) {
        return (closeableStaticBitmap.getRotationAngle() == 0 || closeableStaticBitmap.getRotationAngle() == -1) ? false : true;
    }

    /* access modifiers changed from: private */
    public static boolean hasTransformableExifOrientation(CloseableStaticBitmap closeableStaticBitmap) {
        if (closeableStaticBitmap.getExifOrientation() == 1 || closeableStaticBitmap.getExifOrientation() == 0) {
            return false;
        }
        return true;
    }

    public PipelineDraweeController(Resources resources, DeferredReleaser deferredReleaser, DrawableFactory drawableFactory, Executor executor, MemoryCache<CacheKey, CloseableImage> memoryCache, Supplier<DataSource<CloseableReference<CloseableImage>>> supplier, String str, CacheKey cacheKey, Object obj) {
        this(resources, deferredReleaser, drawableFactory, executor, memoryCache, supplier, str, cacheKey, obj, (ImmutableList<DrawableFactory>) null);
    }

    public PipelineDraweeController(Resources resources, DeferredReleaser deferredReleaser, DrawableFactory drawableFactory, Executor executor, MemoryCache<CacheKey, CloseableImage> memoryCache, Supplier<DataSource<CloseableReference<CloseableImage>>> supplier, String str, CacheKey cacheKey, Object obj, @Nullable ImmutableList<DrawableFactory> immutableList) {
        super(deferredReleaser, executor, str, obj);
        this.mDefaultDrawableFactory = new DrawableFactory() {
            public boolean supportsImageType(CloseableImage closeableImage) {
                return true;
            }

            public Drawable createDrawable(CloseableImage closeableImage) {
                if (closeableImage instanceof CloseableStaticBitmap) {
                    CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) closeableImage;
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(PipelineDraweeController.this.mResources, closeableStaticBitmap.getUnderlyingBitmap());
                    if (PipelineDraweeController.hasTransformableRotationAngle(closeableStaticBitmap) || PipelineDraweeController.hasTransformableExifOrientation(closeableStaticBitmap)) {
                        return new OrientedDrawable(bitmapDrawable, closeableStaticBitmap.getRotationAngle(), closeableStaticBitmap.getExifOrientation());
                    }
                    return bitmapDrawable;
                } else if (PipelineDraweeController.this.mAnimatedDrawableFactory == null || !PipelineDraweeController.this.mAnimatedDrawableFactory.supportsImageType(closeableImage)) {
                    return null;
                } else {
                    return PipelineDraweeController.this.mAnimatedDrawableFactory.createDrawable(closeableImage);
                }
            }
        };
        this.mResources = resources;
        this.mAnimatedDrawableFactory = drawableFactory;
        this.mMemoryCache = memoryCache;
        this.mCacheKey = cacheKey;
        this.mGlobalDrawableFactories = immutableList;
        init(supplier);
    }

    public void initialize(Supplier<DataSource<CloseableReference<CloseableImage>>> supplier, String str, CacheKey cacheKey, Object obj, @Nullable ImmutableList<DrawableFactory> immutableList) {
        super.initialize(str, obj);
        init(supplier);
        this.mCacheKey = cacheKey;
        setCustomDrawableFactories(immutableList);
    }

    public void setDrawDebugOverlay(boolean z) {
        this.mDrawDebugOverlay = z;
    }

    public void setCustomDrawableFactories(@Nullable ImmutableList<DrawableFactory> immutableList) {
        this.mCustomDrawableFactories = immutableList;
    }

    private void init(Supplier<DataSource<CloseableReference<CloseableImage>>> supplier) {
        this.mDataSourceSupplier = supplier;
        maybeUpdateDebugOverlay((CloseableImage) null);
    }

    /* access modifiers changed from: protected */
    public Resources getResources() {
        return this.mResources;
    }

    /* access modifiers changed from: protected */
    public CacheKey getCacheKey() {
        return this.mCacheKey;
    }

    /* access modifiers changed from: protected */
    public DataSource<CloseableReference<CloseableImage>> getDataSource() {
        if (FLog.isLoggable(2)) {
            FLog.v(TAG, "controller %x: getDataSource", (Object) Integer.valueOf(System.identityHashCode(this)));
        }
        return this.mDataSourceSupplier.get();
    }

    /* access modifiers changed from: protected */
    public Drawable createDrawable(CloseableReference<CloseableImage> closeableReference) {
        Preconditions.checkState(CloseableReference.isValid(closeableReference));
        CloseableImage closeableImage = closeableReference.get();
        maybeUpdateDebugOverlay(closeableImage);
        Drawable maybeCreateDrawableFromFactories = maybeCreateDrawableFromFactories(this.mCustomDrawableFactories, closeableImage);
        if (maybeCreateDrawableFromFactories != null) {
            return maybeCreateDrawableFromFactories;
        }
        Drawable maybeCreateDrawableFromFactories2 = maybeCreateDrawableFromFactories(this.mGlobalDrawableFactories, closeableImage);
        if (maybeCreateDrawableFromFactories2 != null) {
            return maybeCreateDrawableFromFactories2;
        }
        Drawable createDrawable = this.mDefaultDrawableFactory.createDrawable(closeableImage);
        if (createDrawable != null) {
            return createDrawable;
        }
        throw new UnsupportedOperationException("Unrecognized image class: " + closeableImage);
    }

    private Drawable maybeCreateDrawableFromFactories(@Nullable ImmutableList<DrawableFactory> immutableList, CloseableImage closeableImage) {
        Drawable createDrawable;
        if (immutableList == null) {
            return null;
        }
        Iterator it = immutableList.iterator();
        while (it.hasNext()) {
            DrawableFactory drawableFactory = (DrawableFactory) it.next();
            if (drawableFactory.supportsImageType(closeableImage) && (createDrawable = drawableFactory.createDrawable(closeableImage)) != null) {
                return createDrawable;
            }
        }
        return null;
    }

    public void setHierarchy(@Nullable DraweeHierarchy draweeHierarchy) {
        super.setHierarchy(draweeHierarchy);
        maybeUpdateDebugOverlay((CloseableImage) null);
    }

    private void maybeUpdateDebugOverlay(@Nullable CloseableImage closeableImage) {
        ScaleTypeDrawable activeScaleTypeDrawable;
        if (this.mDrawDebugOverlay) {
            Drawable controllerOverlay = getControllerOverlay();
            if (controllerOverlay == null) {
                controllerOverlay = new DebugControllerOverlayDrawable();
                setControllerOverlay(controllerOverlay);
            }
            if (controllerOverlay instanceof DebugControllerOverlayDrawable) {
                DebugControllerOverlayDrawable debugControllerOverlayDrawable = (DebugControllerOverlayDrawable) controllerOverlay;
                debugControllerOverlayDrawable.setControllerId(getId());
                DraweeHierarchy hierarchy = getHierarchy();
                ScalingUtils.ScaleType scaleType = null;
                if (!(hierarchy == null || (activeScaleTypeDrawable = ScalingUtils.getActiveScaleTypeDrawable(hierarchy.getTopLevelDrawable())) == null)) {
                    scaleType = activeScaleTypeDrawable.getScaleType();
                }
                debugControllerOverlayDrawable.setScaleType(scaleType);
                if (closeableImage != null) {
                    debugControllerOverlayDrawable.setDimensions(closeableImage.getWidth(), closeableImage.getHeight());
                    debugControllerOverlayDrawable.setImageSize(closeableImage.getSizeInBytes());
                    return;
                }
                debugControllerOverlayDrawable.reset();
            }
        }
    }

    /* access modifiers changed from: protected */
    public ImageInfo getImageInfo(CloseableReference<CloseableImage> closeableReference) {
        Preconditions.checkState(CloseableReference.isValid(closeableReference));
        return closeableReference.get();
    }

    /* access modifiers changed from: protected */
    public int getImageHash(@Nullable CloseableReference<CloseableImage> closeableReference) {
        if (closeableReference != null) {
            return closeableReference.getValueHash();
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void releaseImage(@Nullable CloseableReference<CloseableImage> closeableReference) {
        CloseableReference.closeSafely((CloseableReference<?>) closeableReference);
    }

    /* access modifiers changed from: protected */
    public void releaseDrawable(@Nullable Drawable drawable) {
        if (drawable instanceof DrawableWithCaches) {
            ((DrawableWithCaches) drawable).dropCaches();
        }
    }

    /* access modifiers changed from: protected */
    public CloseableReference<CloseableImage> getCachedImage() {
        if (this.mMemoryCache == null || this.mCacheKey == null) {
            return null;
        }
        CloseableReference<CloseableImage> closeableReference = this.mMemoryCache.get(this.mCacheKey);
        if (closeableReference == null || closeableReference.get().getQualityInfo().isOfFullQuality()) {
            return closeableReference;
        }
        closeableReference.close();
        return null;
    }

    public String toString() {
        return Objects.toStringHelper((Object) this).add("super", (Object) super.toString()).add("dataSourceSupplier", (Object) this.mDataSourceSupplier).toString();
    }
}
