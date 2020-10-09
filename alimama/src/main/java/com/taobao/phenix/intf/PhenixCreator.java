package com.taobao.phenix.intf;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.taobao.phenix.bitmap.BitmapProcessor;
import com.taobao.phenix.cache.CacheKeyInspector;
import com.taobao.phenix.cache.memory.PassableBitmapDrawable;
import com.taobao.phenix.chain.NormalChainProducerSupplier;
import com.taobao.phenix.chain.PhenixLastConsumer;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.intf.event.FailPhenixEvent;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.IRetryHandlerOnFailure;
import com.taobao.phenix.intf.event.MemCacheMissPhenixEvent;
import com.taobao.phenix.intf.event.PhenixEvent;
import com.taobao.phenix.intf.event.ProgressPhenixEvent;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.strategy.ModuleStrategy;
import com.taobao.rxm.produce.Producer;
import com.taobao.rxm.schedule.SchedulerSupplier;
import java.lang.ref.WeakReference;
import java.util.Map;

public class PhenixCreator extends AbsPhenixCreator {
    private static int[] gScreenSize;
    private IPhenixListener<PhenixEvent> mCancelListener;
    /* access modifiers changed from: private */
    public Drawable mErrorDrawable;
    /* access modifiers changed from: private */
    public int mErrorResId;
    private IPhenixListener<FailPhenixEvent> mFailListener;
    private final ImageRequest mImageRequest;
    /* access modifiers changed from: private */
    public WeakReference<ImageView> mIntoImageRef;
    private IPhenixListener<MemCacheMissPhenixEvent> mMemMissListener;
    /* access modifiers changed from: private */
    public Drawable mPlaceholderDrawable;
    /* access modifiers changed from: private */
    public int mPlaceholderResId;
    private IPhenixListener<ProgressPhenixEvent> mProgressListener;
    private IRetryHandlerOnFailure mRetryHandlerOnFailure;
    private IPhenixListener<SuccPhenixEvent> mSuccessListener;

    @Deprecated
    public PhenixCreator notSharedDrawable(boolean z) {
        return this;
    }

    PhenixCreator(ModuleStrategy moduleStrategy, String str, CacheKeyInspector cacheKeyInspector) {
        this.mImageRequest = new ImageRequest(str, cacheKeyInspector, Phenix.instance().isGenericTypeCheckEnabled());
        if (moduleStrategy != null) {
            this.mImageRequest.setModuleName(moduleStrategy.name);
            this.mImageRequest.setSchedulePriority(moduleStrategy.schedulePriority);
            this.mImageRequest.setMemoryCachePriority(moduleStrategy.memoryCachePriority);
            this.mImageRequest.setDiskCachePriority(moduleStrategy.diskCachePriority);
            preloadWithSmall(moduleStrategy.preloadWithSmall);
            scaleFromLarge(moduleStrategy.scaleFromLarge);
            return;
        }
        preloadWithSmall(Phenix.instance().isPreloadWithLowImage());
        scaleFromLarge(Phenix.instance().isScaleWithLargeImage());
    }

    public String url() {
        return this.mImageRequest.getImageUriInfo().getPath();
    }

    public int id() {
        if (this.mImageRequest != null) {
            return this.mImageRequest.getId();
        }
        return -1;
    }

    public PhenixCreator placeholder(int i) {
        if (i == 0) {
            throw new IllegalArgumentException("Placeholder image resource invalid.");
        } else if (this.mPlaceholderDrawable == null) {
            this.mPlaceholderResId = i;
            return this;
        } else {
            throw new IllegalStateException("Placeholder image already set.");
        }
    }

    public PhenixCreator placeholder(Drawable drawable) {
        if (this.mPlaceholderResId == 0) {
            this.mPlaceholderDrawable = drawable;
            return this;
        }
        throw new IllegalStateException("Placeholder image already set.");
    }

    public PhenixCreator error(int i) {
        if (i == 0) {
            throw new IllegalArgumentException("Error image resource invalid.");
        } else if (this.mErrorDrawable == null) {
            this.mErrorResId = i;
            return this;
        } else {
            throw new IllegalStateException("Error image already set.");
        }
    }

    public PhenixCreator error(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Error image may not be null.");
        } else if (this.mErrorResId == 0) {
            this.mErrorDrawable = drawable;
            return this;
        } else {
            throw new IllegalStateException("Error image already set.");
        }
    }

    public PhenixCreator onlyCache() {
        this.mImageRequest.onlyCache(true);
        return this;
    }

    public static int[] getScreenSize(Context context) {
        if (gScreenSize == null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            gScreenSize = new int[]{displayMetrics.widthPixels, displayMetrics.heightPixels};
        }
        return gScreenSize;
    }

    public PhenixCreator memOnly(boolean z) {
        this.mImageRequest.memoryOnly(z);
        return this;
    }

    public PhenixCreator skipCache() {
        this.mImageRequest.skipCache();
        return this;
    }

    public PhenixCreator preloadWithSmall(boolean z) {
        this.mImageRequest.allowSizeLevel(z, 2);
        return this;
    }

    public PhenixCreator scaleFromLarge(boolean z) {
        this.mImageRequest.allowSizeLevel(z, 4);
        return this;
    }

    public PhenixCreator memoryCachePriority(int i) {
        this.mImageRequest.setMemoryCachePriority(i);
        return this;
    }

    public PhenixCreator diskCachePriority(int i) {
        this.mImageRequest.setDiskCachePriority(i);
        return this;
    }

    public PhenixCreator schedulePriority(int i) {
        this.mImageRequest.setSchedulePriority(i);
        return this;
    }

    public PhenixCreator releasableDrawable(boolean z) {
        this.mImageRequest.releasableDrawableSpecified(z);
        return this;
    }

    public PhenixCreator asThumbnail(int i, boolean z) {
        if (i == 1 || i == 3) {
            this.mImageRequest.asThumbnail(i, z);
        }
        return this;
    }

    public PhenixCreator bitmapProcessors(BitmapProcessor... bitmapProcessorArr) {
        if (bitmapProcessorArr != null && bitmapProcessorArr.length > 0) {
            this.mImageRequest.setBitmapProcessors(bitmapProcessorArr);
        }
        return this;
    }

    public PhenixCreator forceAnimationToBeStatic(boolean z) {
        this.mImageRequest.forceAnimationStatic(z);
        return this;
    }

    public PhenixCreator secondary(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mImageRequest.setSecondaryPath(str);
        }
        return this;
    }

    @Deprecated
    public PhenixCreator setCacheKey4PlaceHolder(String str) {
        secondary(str);
        return this;
    }

    public PhenixCreator addLoaderExtra(String str, String str2) {
        this.mImageRequest.addLoaderExtra(str, str2);
        return this;
    }

    public PhenixCreator failListener(IPhenixListener<FailPhenixEvent> iPhenixListener) {
        this.mFailListener = iPhenixListener;
        return this;
    }

    public PhenixCreator succListener(IPhenixListener<SuccPhenixEvent> iPhenixListener) {
        this.mSuccessListener = iPhenixListener;
        return this;
    }

    public PhenixCreator retryHandler(IRetryHandlerOnFailure iRetryHandlerOnFailure) {
        this.mRetryHandlerOnFailure = iRetryHandlerOnFailure;
        return this;
    }

    public PhenixCreator memCacheMissListener(IPhenixListener<MemCacheMissPhenixEvent> iPhenixListener) {
        this.mMemMissListener = iPhenixListener;
        return this;
    }

    public PhenixCreator cancelListener(IPhenixListener<PhenixEvent> iPhenixListener) {
        this.mCancelListener = iPhenixListener;
        return this;
    }

    public PhenixCreator progressListener(int i, IPhenixListener<ProgressPhenixEvent> iPhenixListener) {
        this.mImageRequest.setProgressUpdateStep(i);
        this.mProgressListener = iPhenixListener;
        return this;
    }

    public PhenixTicket into(ImageView imageView) {
        return into(imageView, 1.0f);
    }

    public PhenixTicket into(ImageView imageView, float f) {
        limitSize(imageView);
        if (f > 1.0f) {
            this.mImageRequest.setMaxViewWidth((int) (((float) this.mImageRequest.getMaxViewWidth()) / f));
            this.mImageRequest.setMaxViewHeight((int) (((float) this.mImageRequest.getMaxViewHeight()) / f));
        }
        return fetchInto(imageView);
    }

    public PhenixTicket into(ImageView imageView, int i, int i2) {
        limitSize(imageView, i, i2);
        return fetchInto(imageView);
    }

    public PhenixCreator limitSize(View view) {
        int[] screenSize = getScreenSize(view.getContext());
        return limitSize(view, screenSize[0], screenSize[1]);
    }

    public PhenixCreator limitSize(View view, int i, int i2) {
        ViewGroup.LayoutParams layoutParams;
        if (!(view == null || (layoutParams = view.getLayoutParams()) == null)) {
            if (layoutParams.width > 0) {
                this.mImageRequest.setMaxViewWidth(layoutParams.width);
            } else if (layoutParams.width != -2) {
                this.mImageRequest.setMaxViewWidth(view.getWidth());
            }
            if (layoutParams.height > 0) {
                this.mImageRequest.setMaxViewHeight(layoutParams.height);
            } else if (layoutParams.height != -2) {
                this.mImageRequest.setMaxViewHeight(view.getHeight());
            }
        }
        if (this.mImageRequest.getMaxViewWidth() <= 0) {
            this.mImageRequest.setMaxViewWidth(i);
        }
        if (this.mImageRequest.getMaxViewHeight() <= 0) {
            this.mImageRequest.setMaxViewHeight(i2);
        }
        return this;
    }

    private PhenixTicket fetchInto(ImageView imageView) {
        this.mIntoImageRef = new WeakReference<>(imageView);
        return failListener(new IPhenixListener<FailPhenixEvent>() {
            public boolean onHappen(FailPhenixEvent failPhenixEvent) {
                ImageView imageView;
                if (PhenixCreator.this.mIntoImageRef == null || (imageView = (ImageView) PhenixCreator.this.mIntoImageRef.get()) == null) {
                    return false;
                }
                if (PhenixCreator.this.mErrorResId != 0) {
                    imageView.setImageResource(PhenixCreator.this.mErrorResId);
                    return true;
                } else if (PhenixCreator.this.mErrorDrawable == null) {
                    return true;
                } else {
                    imageView.setImageDrawable(PhenixCreator.this.mErrorDrawable);
                    return true;
                }
            }
        }).memCacheMissListener(new IPhenixListener<MemCacheMissPhenixEvent>() {
            public boolean onHappen(MemCacheMissPhenixEvent memCacheMissPhenixEvent) {
                ImageView imageView;
                if (PhenixCreator.this.mIntoImageRef == null || (imageView = (ImageView) PhenixCreator.this.mIntoImageRef.get()) == null) {
                    return false;
                }
                if (PhenixCreator.this.mPlaceholderResId != 0) {
                    imageView.setImageResource(PhenixCreator.this.mPlaceholderResId);
                    return true;
                } else if (PhenixCreator.this.mPlaceholderDrawable == null) {
                    return true;
                } else {
                    imageView.setImageDrawable(PhenixCreator.this.mPlaceholderDrawable);
                    return true;
                }
            }
        }).succListener(new IPhenixListener<SuccPhenixEvent>() {
            public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
                ImageView imageView;
                if (PhenixCreator.this.mIntoImageRef == null || (imageView = (ImageView) PhenixCreator.this.mIntoImageRef.get()) == null) {
                    return false;
                }
                if (succPhenixEvent.getDrawable() == null) {
                    return true;
                }
                imageView.setImageDrawable(succPhenixEvent.getDrawable());
                return true;
            }
        }).fetch();
    }

    public PhenixTicket fetch() {
        String str;
        PhenixTicket phenixTicket = this.mImageRequest.getPhenixTicket();
        if (TextUtils.isEmpty(this.mImageRequest.getPath())) {
            if (this.mFailListener != null) {
                this.mFailListener.onHappen(new FailPhenixEvent(phenixTicket));
            }
            return phenixTicket;
        }
        Map<String, String> loaderExtras = this.mImageRequest.getLoaderExtras();
        if (!(loaderExtras == null || (str = loaderExtras.get(Constant.BUNDLE_BIZ_CODE)) == null)) {
            this.mImageRequest.getStatistics().mBizId = str;
        }
        NormalChainProducerSupplier producerSupplier = Phenix.instance().getProducerSupplier();
        Producer<PassableBitmapDrawable, ImageRequest> producer = producerSupplier.get();
        SchedulerSupplier schedulerSupplierUsedInProducer = producerSupplier.getSchedulerSupplierUsedInProducer();
        producer.produceResults(new PhenixLastConsumer(this.mImageRequest, this, Phenix.instance().getImageFlowMonitor(), schedulerSupplierUsedInProducer, Phenix.instance().getImageDecodingListener()).consumeOn(schedulerSupplierUsedInProducer.forUiThread()));
        return phenixTicket;
    }

    public IPhenixListener<FailPhenixEvent> getFailureListener() {
        return this.mFailListener;
    }

    public IPhenixListener<SuccPhenixEvent> getSuccessListener() {
        return this.mSuccessListener;
    }

    public IPhenixListener<MemCacheMissPhenixEvent> getMemCacheMissListener() {
        return this.mMemMissListener;
    }

    public IRetryHandlerOnFailure getRetryHandlerOnFailure() {
        return this.mRetryHandlerOnFailure;
    }

    public IPhenixListener<PhenixEvent> getCancelListener() {
        return this.mCancelListener;
    }

    public IPhenixListener<ProgressPhenixEvent> getProgressListener() {
        return this.mProgressListener;
    }

    @Deprecated
    public PhenixCreator setImageStrategyInfo(Object obj) {
        if (obj != null) {
            addLoaderExtra(Constant.BUNDLE_BIZ_CODE, obj.toString());
        }
        return this;
    }
}
