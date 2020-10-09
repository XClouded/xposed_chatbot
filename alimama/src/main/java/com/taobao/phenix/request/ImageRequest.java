package com.taobao.phenix.request;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.pexode.PexodeOptions;
import com.taobao.phenix.bitmap.BitmapProcessor;
import com.taobao.phenix.cache.CacheKeyInspector;
import com.taobao.phenix.chain.PhenixProduceListener;
import com.taobao.phenix.common.Constant;
import com.taobao.phenix.intf.PhenixTicket;
import com.taobao.rxm.request.RequestContext;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class ImageRequest extends RequestContext {
    private static final int ONLY_CACHE_FLAG = 2;
    private static final int ONLY_MEMORY_FLAG = 4;
    private static final int SKIP_CACHE_FLAG = 1;
    private int mAllowedSizeLevel;
    private BitmapProcessor[] mBitmapProcessors;
    private Future<?> mBlockingFuture;
    private int mDiskCachePriority;
    private boolean mForcedAnimationStatic;
    private ImageUriInfo mImageUriInfo;
    private boolean mIsRetrying;
    private Map<String, String> mLoaderExtras;
    private int mMaxViewHeight;
    private int mMaxViewWidth;
    private int mMemoryCachePriority;
    private String mModuleName;
    private String mMultiplexKey;
    private String mMultiplexKeySuffix;
    private PexodeOptions mPexodeOptions;
    private final PhenixTicket mPhenixTicket;
    private int mProgressUpdateStep;
    private boolean mReleasableDrawableSpecified;
    private long mRequestStartTime;
    private ImageUriInfo mSecondaryUriInfo;
    private ImageStatistics mStatistics;
    private int mSwitchFlags;
    private long mWorkThreadEndTime;

    public static boolean isAllowedSizeLevel(int i, int i2) {
        return (i & i2) > 0;
    }

    public ImageRequest(String str, CacheKeyInspector cacheKeyInspector) {
        this(str, cacheKeyInspector, true);
    }

    public ImageRequest(String str, CacheKeyInspector cacheKeyInspector, boolean z) {
        super(z);
        this.mDiskCachePriority = 17;
        this.mMemoryCachePriority = 17;
        this.mSwitchFlags = 0;
        this.mImageUriInfo = new ImageUriInfo(str, cacheKeyInspector);
        this.mStatistics = new ImageStatistics(this.mImageUriInfo);
        this.mPhenixTicket = new PhenixTicket(this);
        this.mRequestStartTime = System.currentTimeMillis();
        this.mAllowedSizeLevel = 1;
        this.mStatistics.setRequestStartTime(this.mRequestStartTime);
        this.mStatistics.setDiskCachePriority(this.mDiskCachePriority);
    }

    public String getModuleName() {
        return this.mModuleName;
    }

    public void setModuleName(String str) {
        this.mModuleName = str;
    }

    public synchronized ImageStatistics getStatistics() {
        return this.mStatistics;
    }

    public int getAllowedSizeLevel() {
        return this.mAllowedSizeLevel;
    }

    public boolean isAllowedSizeLevel(int i) {
        return (i & this.mAllowedSizeLevel) > 0;
    }

    public void allowSizeLevel(boolean z, int i) {
        if (z) {
            this.mAllowedSizeLevel |= i;
        } else {
            this.mAllowedSizeLevel &= i ^ -1;
        }
        rebuildKeyIfChanged();
    }

    private synchronized void addMultiplexKeySuffix(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mMultiplexKeySuffix == null) {
                this.mMultiplexKeySuffix = str;
            } else {
                this.mMultiplexKeySuffix += str;
            }
            rebuildKeyIfChanged();
        }
    }

    public void memoryOnly(boolean z) {
        if (z) {
            this.mSwitchFlags |= 4;
        } else {
            this.mSwitchFlags &= -5;
        }
        rebuildKeyIfChanged();
    }

    public void onlyCache(boolean z) {
        if (z) {
            this.mSwitchFlags |= 2;
        } else {
            this.mSwitchFlags &= -3;
        }
        rebuildKeyIfChanged();
    }

    public void skipCache() {
        this.mSwitchFlags |= 1;
        rebuildKeyIfChanged();
    }

    public boolean isReleasableDrawableSpecified() {
        return this.mReleasableDrawableSpecified;
    }

    public void releasableDrawableSpecified(boolean z) {
        this.mReleasableDrawableSpecified = z;
    }

    public boolean isMemoryOnly() {
        return (this.mSwitchFlags & 4) > 0;
    }

    public boolean isOnlyCache() {
        return (this.mSwitchFlags & 2) > 0;
    }

    public boolean isSkipCache() {
        return (this.mSwitchFlags & 1) > 0;
    }

    public void asThumbnail(int i, boolean z) {
        SchemeInfo schemeInfo = getImageUriInfo().getSchemeInfo();
        schemeInfo.thumbnailType = i;
        schemeInfo.useOriginIfThumbNotExist = z;
        StringBuilder sb = new StringBuilder();
        sb.append("#THUMBNAIL$");
        if (z) {
            i *= 10000;
        }
        sb.append(i);
        String sb2 = sb.toString();
        getImageUriInfo().addMemoryCacheKeySuffix(sb2);
        addMultiplexKeySuffix(sb2);
    }

    public void setBitmapProcessors(@NonNull BitmapProcessor[] bitmapProcessorArr) {
        String str = "";
        for (BitmapProcessor bitmapProcessor : bitmapProcessorArr) {
            str = str + "#PROCESSOR_" + bitmapProcessor.getClass().hashCode();
            String id = bitmapProcessor.getId();
            if (!TextUtils.isEmpty(id)) {
                str = str + "$" + id;
            }
        }
        this.mBitmapProcessors = bitmapProcessorArr;
        getImageUriInfo().addMemoryCacheKeySuffix(str);
        addMultiplexKeySuffix(str);
    }

    public void setMaxViewWidth(int i) {
        if (this.mMaxViewWidth != i) {
            this.mMaxViewWidth = i;
            this.mImageUriInfo.setMaxViewSize(this.mMaxViewWidth, this.mMaxViewHeight);
            rebuildKeyIfChanged();
        }
    }

    public void setMaxViewHeight(int i) {
        if (this.mMaxViewHeight != i) {
            this.mMaxViewHeight = i;
            this.mImageUriInfo.setMaxViewSize(this.mMaxViewWidth, this.mMaxViewHeight);
            rebuildKeyIfChanged();
        }
    }

    public int getProgressUpdateStep() {
        return this.mProgressUpdateStep;
    }

    public void setProgressUpdateStep(int i) {
        this.mProgressUpdateStep = i;
    }

    public int getMemoryCachePriority() {
        return this.mMemoryCachePriority;
    }

    public void setMemoryCachePriority(int i) {
        this.mMemoryCachePriority = i;
    }

    public void setDiskCachePriority(int i) {
        if (this.mDiskCachePriority != i) {
            this.mDiskCachePriority = i;
            this.mStatistics.setDiskCachePriority(this.mDiskCachePriority);
            rebuildKeyIfChanged();
        }
    }

    public int getDiskCachePriority() {
        return this.mDiskCachePriority;
    }

    public synchronized PhenixTicket getPhenixTicket() {
        return this.mPhenixTicket;
    }

    public BitmapProcessor[] getBitmapProcessors() {
        return this.mBitmapProcessors;
    }

    private synchronized void rebuildKeyIfChanged() {
        if (this.mMultiplexKey != null) {
            this.mMultiplexKey = null;
        }
    }

    public synchronized String getMultiplexKey() {
        if (this.mMultiplexKey == null) {
            String diskCacheKey = this.mImageUriInfo.getDiskCacheKey();
            StringBuilder sb = new StringBuilder(diskCacheKey.length() + 30);
            sb.append("#SLEVEL$");
            sb.append(this.mAllowedSizeLevel);
            sb.append("#FLAGS$");
            sb.append(this.mSwitchFlags);
            sb.append("#MAXW$");
            sb.append(this.mMaxViewWidth);
            sb.append("#MAXH$");
            sb.append(this.mMaxViewHeight);
            sb.append("#SPRIOR$");
            sb.append(getSchedulePriority());
            sb.append("#DPRIOR$");
            sb.append(this.mDiskCachePriority);
            sb.append("#CATALOG$");
            sb.append(diskCacheKey);
            sb.append(this.mImageUriInfo.getDiskCacheCatalog());
            if (this.mSecondaryUriInfo != null) {
                sb.append("#SECOND$");
                sb.append(this.mSecondaryUriInfo.getDiskCacheKey());
                sb.append('$');
                sb.append(this.mSecondaryUriInfo.getDiskCacheCatalog());
            }
            if (this.mMultiplexKeySuffix != null) {
                sb.append(this.mMultiplexKeySuffix);
            }
            this.mMultiplexKey = sb.substring(0);
        }
        return this.mMultiplexKey;
    }

    public String getPath() {
        return this.mImageUriInfo.getPath();
    }

    public int getMaxViewWidth() {
        return this.mMaxViewWidth;
    }

    public int getMaxViewHeight() {
        return this.mMaxViewHeight;
    }

    public long getRequestStartTime() {
        return this.mRequestStartTime;
    }

    public long getWorkThreadEndTime() {
        return this.mWorkThreadEndTime;
    }

    public void setWorkThreadEndTime(long j) {
        this.mWorkThreadEndTime = j;
    }

    public String getMemoryCacheKey() {
        return this.mImageUriInfo.getMemoryCacheKey();
    }

    public String getDiskCacheKey() {
        return this.mImageUriInfo.getDiskCacheKey();
    }

    public int getDiskCacheCatalog() {
        return this.mImageUriInfo.getDiskCacheCatalog();
    }

    public ImageUriInfo getImageUriInfo() {
        return this.mImageUriInfo;
    }

    public void disableSecondary() {
        this.mSecondaryUriInfo = null;
    }

    public ImageUriInfo getSecondaryUriInfo() {
        return this.mSecondaryUriInfo;
    }

    public void setSecondaryPath(String str) {
        this.mSecondaryUriInfo = new ImageUriInfo(str, this.mImageUriInfo.getCacheKeyInspector());
    }

    public Map<String, String> getLoaderExtras() {
        return this.mLoaderExtras;
    }

    public synchronized void addLoaderExtra(String str, String str2) {
        if (this.mLoaderExtras == null) {
            this.mLoaderExtras = new HashMap();
            this.mStatistics.setExtras(this.mLoaderExtras);
        }
        this.mLoaderExtras.put(str, str2);
    }

    public Future<?> getBlockingFuture() {
        return this.mBlockingFuture;
    }

    public void setBlockingFuture(Future<?> future) {
        this.mBlockingFuture = future;
    }

    public PexodeOptions getPexodeOptions() {
        return this.mPexodeOptions;
    }

    public void setPexodeOptions(PexodeOptions pexodeOptions) {
        this.mPexodeOptions = pexodeOptions;
    }

    public Map<String, Long> getProduceTimeline() {
        if (getProducerListener() == null) {
            return new HashMap();
        }
        return ((PhenixProduceListener) getProducerListener()).getProduceTimeline();
    }

    public void syncFrom(RequestContext requestContext) {
        ImageRequest imageRequest = (ImageRequest) requestContext;
        ImageStatistics statistics = imageRequest.getStatistics();
        this.mStatistics.duplicate(true);
        this.mStatistics.fromType(statistics.getFromType());
        this.mStatistics.setCompressFormat(statistics.getFormat());
        this.mStatistics.setSize(statistics.getSize());
        Map<String, Long> produceTimeline = getProduceTimeline();
        for (Map.Entry next : imageRequest.getProduceTimeline().entrySet()) {
            if (!produceTimeline.containsKey(next.getKey())) {
                produceTimeline.put(next.getKey(), next.getValue());
            }
        }
    }

    public boolean isRetrying() {
        return this.mIsRetrying;
    }

    public synchronized void resetBeforeRetry(String str) {
        super.reset();
        this.mIsRetrying = true;
        this.mRequestStartTime = System.currentTimeMillis();
        this.mSecondaryUriInfo = null;
        this.mBlockingFuture = null;
        if (!str.equals(this.mImageUriInfo.getPath())) {
            this.mImageUriInfo = new ImageUriInfo(str, this.mImageUriInfo.getCacheKeyInspector());
            this.mMultiplexKey = null;
        }
        String str2 = this.mStatistics != null ? this.mStatistics.mFullTraceId : "";
        this.mStatistics = new ImageStatistics(this.mImageUriInfo, true);
        if (!TextUtils.isEmpty(str2)) {
            this.mStatistics.mFullTraceId = str2;
        }
        this.mStatistics.setRequestStartTime(this.mRequestStartTime);
        if (this.mLoaderExtras != null) {
            this.mLoaderExtras.remove(Constant.INNER_EXTRA_IS_ASYNC_HTTP);
            this.mStatistics.setExtras(this.mLoaderExtras);
        }
        this.mStatistics.setDiskCachePriority(this.mDiskCachePriority);
    }

    public void forceAnimationStatic(boolean z) {
        this.mForcedAnimationStatic = z;
        if (this.mForcedAnimationStatic) {
            getImageUriInfo().addMemoryCacheKeySuffix("#FSTATIC");
            addMultiplexKeySuffix("#FSTATIC");
        }
    }

    public boolean isForcedAnimationStatic() {
        return this.mForcedAnimationStatic;
    }
}
