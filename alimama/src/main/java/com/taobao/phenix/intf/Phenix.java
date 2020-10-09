package com.taobao.phenix.intf;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import com.taobao.phenix.builder.BitmapPoolBuilder;
import com.taobao.phenix.builder.BytesPoolBuilder;
import com.taobao.phenix.builder.ChainBuilders;
import com.taobao.phenix.builder.DiskCacheBuilder;
import com.taobao.phenix.builder.DiskCacheKVBuilder;
import com.taobao.phenix.builder.FileLoaderBuilder;
import com.taobao.phenix.builder.HttpLoaderBuilder;
import com.taobao.phenix.builder.MemCacheBuilder;
import com.taobao.phenix.builder.SchedulerBuilder;
import com.taobao.phenix.cache.CacheKeyInspector;
import com.taobao.phenix.cache.disk.DiskCache;
import com.taobao.phenix.cache.memory.MemoryCacheProducer;
import com.taobao.phenix.chain.DefaultSchedulerSupplier;
import com.taobao.phenix.chain.ImageDecodingListener;
import com.taobao.phenix.chain.NormalChainProducerSupplier;
import com.taobao.phenix.chain.PrefetchChainProducerSupplier;
import com.taobao.phenix.common.SizeUtil;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.decode.EncodedDataInspector;
import com.taobao.phenix.entity.ResponseData;
import com.taobao.phenix.loader.LocalSchemeHandler;
import com.taobao.phenix.loader.file.DefaultFileLoader;
import com.taobao.phenix.request.ImageFlowMonitor;
import com.taobao.phenix.request.ImageRequest;
import com.taobao.phenix.request.ImageUriInfo;
import com.taobao.phenix.strategy.ModuleStrategy;
import com.taobao.phenix.strategy.ModuleStrategySupplier;
import com.taobao.rxm.schedule.SchedulerSupplier;
import com.taobao.tcommon.core.Preconditions;
import com.taobao.tcommon.core.RuntimeUtil;
import com.taobao.tcommon.core.VisibleForTesting;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Phenix implements ChainBuilders {
    public static boolean NO_USE_WEBP_FORMAT = false;
    private static Phenix sPhenix;
    private final BitmapPoolBuilder mBitmapPoolBuilder = new BitmapPoolBuilder();
    private final BytesPoolBuilder mBytesPoolBuilder = new BytesPoolBuilder();
    private CacheKeyInspector mCacheKeyInspector;
    private Context mContext;
    private final DiskCacheBuilder mDiskCacheBuilder = new DiskCacheBuilder();
    private final DiskCacheKVBuilder mDiskCacheKVBuilder = new DiskCacheKVBuilder();
    private boolean mEnableGenericTypeCheck;
    private EncodedDataInspector mEncodedDataInspector;
    private List<LocalSchemeHandler> mExtendedSchemeHandlers;
    private final FileLoaderBuilder mFileLoaderBuilder = new FileLoaderBuilder();
    private boolean mHasBuilt;
    private final HttpLoaderBuilder mHttpLoaderBuilder = new HttpLoaderBuilder();
    private ImageDecodingListener mImageDecodingListener;
    private ImageFlowMonitor mImageFlowMonitor;
    private final MemCacheBuilder mMemCacheBuilder = new MemCacheBuilder();
    private ModuleStrategySupplier mModuleStrategySupplier;
    private PrefetchChainProducerSupplier mPrefetchProducerSupplier;
    private boolean mPreloadWithLowImage = true;
    private final NormalChainProducerSupplier mProducerSupplier = new NormalChainProducerSupplier(this);
    private boolean mScaleWithLargeImage = true;
    private final SchedulerBuilder mSchedulerBuilder = new SchedulerBuilder();

    @Deprecated
    public void shutdown() {
    }

    public static synchronized Phenix instance() {
        Phenix phenix;
        synchronized (Phenix.class) {
            if (sPhenix == null) {
                sPhenix = new Phenix();
            }
            phenix = sPhenix;
        }
        return phenix;
    }

    @VisibleForTesting
    static void reset() {
        sPhenix = null;
    }

    private Phenix() {
    }

    /* access modifiers changed from: package-private */
    public NormalChainProducerSupplier getProducerSupplier() {
        return this.mProducerSupplier;
    }

    public SchedulerSupplier getSchedulerSupplierUsedInProducer() {
        return this.mProducerSupplier.getSchedulerSupplierUsedInProducer();
    }

    /* access modifiers changed from: package-private */
    public synchronized PrefetchChainProducerSupplier getPrefetchProducerSupplier() {
        if (this.mPrefetchProducerSupplier == null) {
            this.mPrefetchProducerSupplier = new PrefetchChainProducerSupplier(this);
        }
        if (this.mHasBuilt) {
            this.mPrefetchProducerSupplier.buildChain();
        }
        return this.mPrefetchProducerSupplier;
    }

    /* access modifiers changed from: package-private */
    public ModuleStrategySupplier getModuleStrategySupplier() {
        return this.mModuleStrategySupplier;
    }

    /* access modifiers changed from: package-private */
    public CacheKeyInspector getCacheKeyInspector() {
        return this.mCacheKeyInspector;
    }

    public ImageFlowMonitor getImageFlowMonitor() {
        return this.mImageFlowMonitor;
    }

    public boolean isGenericTypeCheckEnabled() {
        return this.mEnableGenericTypeCheck;
    }

    public void skipGenericTypeCheck(boolean z) {
        this.mEnableGenericTypeCheck = !z;
    }

    public Context applicationContext() {
        return this.mContext;
    }

    public EncodedDataInspector getEncodedDataInspector() {
        return this.mEncodedDataInspector;
    }

    public void setCacheKeyInspector(CacheKeyInspector cacheKeyInspector) {
        this.mCacheKeyInspector = cacheKeyInspector;
    }

    public void setImageFlowMonitor(ImageFlowMonitor imageFlowMonitor) {
        this.mImageFlowMonitor = imageFlowMonitor;
        UnitedLog.i("Initialize", "setup image flow monitor=%s", imageFlowMonitor);
    }

    public void setModuleStrategySupplier(ModuleStrategySupplier moduleStrategySupplier) {
        this.mModuleStrategySupplier = moduleStrategySupplier;
    }

    public void setEncodedDataInspector(EncodedDataInspector encodedDataInspector) {
        this.mEncodedDataInspector = encodedDataInspector;
    }

    public boolean registerLocalSchemeHandler(LocalSchemeHandler localSchemeHandler) {
        synchronized (this) {
            if (this.mExtendedSchemeHandlers == null) {
                this.mExtendedSchemeHandlers = new CopyOnWriteArrayList();
            }
        }
        return this.mExtendedSchemeHandlers.add(localSchemeHandler);
    }

    public boolean unregisterLocalSchemeHandler(LocalSchemeHandler localSchemeHandler) {
        boolean z = false;
        if (this.mExtendedSchemeHandlers != null) {
            while (this.mExtendedSchemeHandlers.remove(localSchemeHandler)) {
                z = true;
            }
        }
        return z;
    }

    public List<LocalSchemeHandler> getExtendedSchemeHandlers() {
        return this.mExtendedSchemeHandlers;
    }

    public MemCacheBuilder memCacheBuilder() {
        return this.mMemCacheBuilder;
    }

    public DiskCacheBuilder diskCacheBuilder() {
        return this.mDiskCacheBuilder;
    }

    public FileLoaderBuilder fileLoaderBuilder() {
        return this.mFileLoaderBuilder;
    }

    public SchedulerBuilder schedulerBuilder() {
        return this.mSchedulerBuilder;
    }

    public DiskCacheKVBuilder diskCacheKVBuilder() {
        return this.mDiskCacheKVBuilder;
    }

    public HttpLoaderBuilder httpLoaderBuilder() {
        return this.mHttpLoaderBuilder;
    }

    public BytesPoolBuilder bytesPoolBuilder() {
        return this.mBytesPoolBuilder;
    }

    public BitmapPoolBuilder bitmapPoolBuilder() {
        return this.mBitmapPoolBuilder;
    }

    public synchronized Phenix with(Context context) {
        Preconditions.checkNotNull(context, "Phenix with context must not be null.");
        if (this.mContext == null) {
            this.mContext = context.getApplicationContext();
        }
        return this;
    }

    public synchronized void build() {
        Preconditions.checkNotNull(this.mContext, "Phenix.with(Context) hasn't been called before chain producer building");
        this.mProducerSupplier.buildChain();
        this.mHasBuilt = true;
        UnitedLog.i("Initialize", "Phenix chain producer build complete", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public boolean isScaleWithLargeImage() {
        return this.mScaleWithLargeImage;
    }

    /* access modifiers changed from: package-private */
    public boolean isPreloadWithLowImage() {
        return this.mPreloadWithLowImage;
    }

    public Phenix scaleWithLargeImage(boolean z) {
        this.mScaleWithLargeImage = z;
        return this;
    }

    public Phenix preloadWithLowImage(boolean z) {
        this.mPreloadWithLowImage = z;
        return this;
    }

    private ModuleStrategy getModuleStrategy(String str) {
        if (this.mModuleStrategySupplier != null) {
            return this.mModuleStrategySupplier.get(str);
        }
        return null;
    }

    private ModuleStrategy getPreloadStrategy(String str) {
        if (this.mModuleStrategySupplier == null) {
            return new ModuleStrategy("common", 2, 17, 17, false, true);
        }
        ModuleStrategy moduleStrategy = this.mModuleStrategySupplier.get(str);
        if (moduleStrategy != null) {
            return moduleStrategy;
        }
        throw new RuntimeException("preload module[" + str + "] strategy hasn't been registered, please contact zhaomi.zm@alibaba-inc.com");
    }

    public PhenixCreator load(String str, String str2, CacheKeyInspector cacheKeyInspector) {
        return new PhenixCreator(getModuleStrategy(str), str2, cacheKeyInspector);
    }

    public PhenixCreator load(String str, CacheKeyInspector cacheKeyInspector) {
        return load((String) null, str, cacheKeyInspector);
    }

    public PhenixCreator load(String str, String str2) {
        return load(str, str2, instance().getCacheKeyInspector());
    }

    public PhenixCreator load(String str) {
        return load((String) null, str, instance().getCacheKeyInspector());
    }

    public PrefetchCreator preload(String str, List<String> list) {
        return new PrefetchCreator(getPreloadStrategy(str), list);
    }

    public boolean clearMemory(String str, boolean z) {
        if (!this.mHasBuilt) {
            return false;
        }
        boolean z2 = !z ? this.mMemCacheBuilder.build().remove(new ImageRequest(str, this.mCacheKeyInspector, this.mEnableGenericTypeCheck).getMemoryCacheKey()) != null : this.mMemCacheBuilder.build().remove(str) != null;
        UnitedLog.d("UserAction", "clear image memory with(urlOrKey=%s isKey=%b), result=%B", str, Boolean.valueOf(z), Boolean.valueOf(z2));
        return z2;
    }

    public boolean clearCache(String str, String str2) {
        if (!this.mHasBuilt) {
            return false;
        }
        ImageRequest imageRequest = new ImageRequest(str2, this.mCacheKeyInspector, this.mEnableGenericTypeCheck);
        this.mMemCacheBuilder.build().remove(imageRequest.getMemoryCacheKey());
        ModuleStrategy moduleStrategy = getModuleStrategy(str);
        boolean z = moduleStrategy != null && this.mDiskCacheBuilder.build().get(moduleStrategy.diskCachePriority).remove(imageRequest.getDiskCacheKey(), imageRequest.getDiskCacheCatalog());
        UnitedLog.dp("UserAction", str2, "clear cache with module=%s, result=%B", str, Boolean.valueOf(z));
        return z;
    }

    @Deprecated
    public void clearCache(String str) {
        if (this.mHasBuilt) {
            ImageRequest imageRequest = new ImageRequest(str, this.mCacheKeyInspector, this.mEnableGenericTypeCheck);
            this.mMemCacheBuilder.build().remove(imageRequest.getMemoryCacheKey());
            Iterator<DiskCache> it = this.mDiskCacheBuilder.build().getAll().iterator();
            while (true) {
                boolean z = false;
                while (true) {
                    if (!it.hasNext()) {
                        UnitedLog.dp("UserAction", str, "clear cache with key, result=%B", Boolean.valueOf(z));
                        return;
                    } else if (it.next().remove(imageRequest.getDiskCacheKey(), imageRequest.getDiskCacheCatalog()) || z) {
                        z = true;
                    }
                }
            }
        }
    }

    public void clearAll() {
        if (this.mHasBuilt) {
            this.mMemCacheBuilder.build().clear();
            for (DiskCache next : this.mDiskCacheBuilder.build().getAll()) {
                if (next.open(this.mContext)) {
                    next.clear();
                }
            }
            UnitedLog.w("UserAction", "clear all phenix cache", new Object[0]);
        }
    }

    public void useNewThreadModel(boolean z) {
        if (this.mProducerSupplier != null) {
            this.mProducerSupplier.useNewThreadModel(z);
        }
    }

    public void useAndroidQThumb(boolean z) {
        DefaultFileLoader.setUseNewThumb(z);
    }

    public void usePostFrontUI(boolean z) {
        DefaultSchedulerSupplier.setUsePostFrontUI(z);
    }

    @Deprecated
    public void cancel(PhenixTicket phenixTicket) {
        if (phenixTicket != null) {
            phenixTicket.cancel();
        }
    }

    @Deprecated
    public BitmapDrawable fetchMemCache(String str) {
        if (!this.mHasBuilt) {
            return null;
        }
        return MemoryCacheProducer.getFilteredCache(memCacheBuilder().build(), new ImageRequest(str, this.mCacheKeyInspector, this.mEnableGenericTypeCheck).getMemoryCacheKey(), false);
    }

    public ResponseData fetchDiskCache(String str, String str2, int i, boolean z) {
        String str3;
        int i2;
        boolean z2 = true;
        Preconditions.checkArgument(!RuntimeUtil.isMainThread(), "fetchDiskCache must be called in non-main thread");
        ResponseData responseData = null;
        if (!this.mHasBuilt) {
            return null;
        }
        if (z) {
            str3 = str2;
            i2 = i;
        } else {
            ImageRequest imageRequest = new ImageRequest(str2, this.mCacheKeyInspector, this.mEnableGenericTypeCheck);
            if (imageRequest.getImageUriInfo().isLocalUri()) {
                return null;
            }
            str3 = imageRequest.getDiskCacheKey();
            i2 = imageRequest.getDiskCacheCatalog();
        }
        int i3 = 17;
        ModuleStrategy moduleStrategy = getModuleStrategy(str);
        if (moduleStrategy != null) {
            i3 = moduleStrategy.diskCachePriority;
        }
        DiskCache diskCache = diskCacheBuilder().build().get(i3);
        if (diskCache != null && diskCache.open(this.mContext)) {
            responseData = diskCache.get(str3, i2);
        }
        Object[] objArr = new Object[4];
        objArr[0] = str;
        objArr[1] = Integer.valueOf(i);
        objArr[2] = Boolean.valueOf(z);
        if (responseData == null) {
            z2 = false;
        }
        objArr[3] = Boolean.valueOf(z2);
        UnitedLog.dp("UserAction", str2, "fetch disk cache, module=%s, catalog=%d, direct=%b, result=%B", objArr);
        return responseData;
    }

    @Deprecated
    public List<ImageInfo> hasCategorys(String str) {
        int[] catalogs;
        ArrayList arrayList = new ArrayList();
        if (!this.mHasBuilt) {
            return arrayList;
        }
        ImageUriInfo imageUriInfo = new ImageUriInfo(str, this.mCacheKeyInspector);
        DiskCache diskCache = diskCacheBuilder().build().get(17);
        if (diskCache.open(this.mContext) && (catalogs = diskCache.getCatalogs(imageUriInfo.getDiskCacheKey())) != null) {
            for (int i : catalogs) {
                arrayList.add(new ImageInfo(SizeUtil.getSplitWidth(i), SizeUtil.getSplitHeight(i)));
            }
        }
        UnitedLog.ip("UserAction", str, "find cache categories, size=%d", Integer.valueOf(arrayList.size()));
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public ImageDecodingListener getImageDecodingListener() {
        return this.mImageDecodingListener;
    }

    public void setImageDecodingListener(ImageDecodingListener imageDecodingListener) {
        this.mImageDecodingListener = imageDecodingListener;
    }
}
