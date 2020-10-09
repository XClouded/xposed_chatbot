package com.alibaba.android.prefetchx;

import android.content.Context;
import android.os.SystemClock;
import android.taobao.windvane.service.WVEventService;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.adapter.AssetAdapter;
import com.alibaba.android.prefetchx.adapter.AssetAdapterImpl;
import com.alibaba.android.prefetchx.adapter.DefaultThreadExecutorImpl;
import com.alibaba.android.prefetchx.adapter.HttpAdapter;
import com.alibaba.android.prefetchx.adapter.HttpAdapterImpl;
import com.alibaba.android.prefetchx.adapter.IThreadExecutor;
import com.alibaba.android.prefetchx.adapter.LoginAdapter;
import com.alibaba.android.prefetchx.config.GlobalOnlineConfigManager;
import com.alibaba.android.prefetchx.core.data.PFMtop;
import com.alibaba.android.prefetchx.core.data.StorageMemory;
import com.alibaba.android.prefetchx.core.data.StorageWeex;
import com.alibaba.android.prefetchx.core.data.SupportH5;
import com.alibaba.android.prefetchx.core.data.SupportWeex;
import com.alibaba.android.prefetchx.core.data.SupportWindmill;
import com.alibaba.android.prefetchx.core.data.adapter.PFDataCallbackImpl;
import com.alibaba.android.prefetchx.core.data.adapter.PFDataUrlKeysAdapterImpl;
import com.alibaba.android.prefetchx.core.file.NetworkPrefetchInterceptor;
import com.alibaba.android.prefetchx.core.file.WXFilePrefetchModule;
import com.alibaba.android.prefetchx.core.image.PFImage;
import com.alibaba.android.prefetchx.core.jsmodule.PFJSModule;
import com.taobao.weaver.prefetch.WMLPrefetch;
import com.taobao.weex.WXSDKEngine;

public class PrefetchX {
    public static final int FEATURE_ALL = 62;
    public static final int FEATURE_DATA = 2;
    public static final int FEATURE_FILE = 4;
    public static final int FEATURE_IMAGE = 32;
    public static final int FEATURE_JSMODULE = 16;
    public static final int FEATURE_RESOUCE = 8;
    public static final long SUPPORT_ALL = 50334782;
    public static final long SUPPORT_DATA_NATIVE_PLUGIN = 64;
    public static final long SUPPORT_DATA_STORAGE_MEMORY = 4;
    public static final long SUPPORT_DATA_STORAGE_WEEX = 2;
    public static final long SUPPORT_DATA_TAOBAO = 62;
    public static final long SUPPORT_DATA_WEEX_MODULE = 8;
    public static final long SUPPORT_DATA_WINDMILL_PLUGIN = 32;
    public static final long SUPPORT_DATA_WINDVANE_PLUGIN = 16;
    public static final long SUPPORT_FILE_TAOBAO = 3072;
    public static final long SUPPORT_FILE_WEEX_MODULE = 1024;
    public static final long SUPPORT_FILE_WINDMILL_MODULE = 2048;
    public static final long SUPPORT_FILE_WINDVANE_PLUGIN = 2048;
    public static final long SUPPORT_IMAGE_TAOBAO = 50331648;
    public static final long SUPPORT_IMAGE_WEEX_MODULE = 16777216;
    public static final long SUPPORT_IMAGE_WINDVANE_PLUGIN = 33554432;
    public static final long SUPPORT_JSMODULE_TAOBAO = 1048576;
    public static final long SUPPORT_JSMODULE_WEEX_MODULE = 1048576;
    private static Boolean hasWeexDependency;
    private static volatile PrefetchX instance;
    public static volatile Context sContext;
    private volatile boolean allowWarmUp = true;
    private boolean isDataFeatureOn = false;
    private boolean isFileFeatureOn = false;
    private boolean isImageFeatureOn = false;
    private boolean isInited = false;
    private boolean isJSModuleFeatureOn = false;
    private boolean isResouceFeatureOn = false;
    private AssetAdapter mAssetAdapter = null;
    private GlobalOnlineConfigManager mGlobalOnlineConfigManager;
    private HttpAdapter mHttpAdapter = null;
    private LoginAdapter mLoginAdapter = null;
    private boolean mReady = false;
    private IThreadExecutor mThreadExecutor = null;

    private PrefetchX() {
    }

    public static PrefetchX getInstance() throws PFException {
        if (instance == null) {
            synchronized (PrefetchX.class) {
                if (instance == null) {
                    instance = new PrefetchX();
                }
            }
        }
        return instance;
    }

    public void init(@NonNull Context context) {
        init(context, (PFInitConfig) null);
    }

    public synchronized void init(@NonNull Context context, @Nullable PFInitConfig pFInitConfig) {
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.isInited) {
            PFLog.w("PrefetchX", "PrefetchX has been inited. Nothing will be done this time.", new Throwable[0]);
            return;
        }
        if (context != null) {
            sContext = context.getApplicationContext();
        }
        if (pFInitConfig == null) {
            this.mAssetAdapter = new AssetAdapterImpl();
            this.mHttpAdapter = new HttpAdapterImpl();
            this.mGlobalOnlineConfigManager = new GlobalOnlineConfigManager();
            this.mGlobalOnlineConfigManager.initDefault();
            this.mThreadExecutor = ThreadExecutorProxy.wrap(new DefaultThreadExecutorImpl());
            this.allowWarmUp = true;
        } else {
            this.mAssetAdapter = pFInitConfig.getAssetAdapter() == null ? new AssetAdapterImpl() : pFInitConfig.getAssetAdapter();
            this.mHttpAdapter = pFInitConfig.getHttpAdapter() == null ? new HttpAdapterImpl() : pFInitConfig.getHttpAdapter();
            this.mGlobalOnlineConfigManager = pFInitConfig.getOnlineConfigManager() == null ? GlobalOnlineConfigManager.createdAll() : pFInitConfig.getOnlineConfigManager();
            this.mThreadExecutor = ThreadExecutorProxy.wrap(pFInitConfig.getThreadExecutor() == null ? new DefaultThreadExecutorImpl() : pFInitConfig.getThreadExecutor());
            this.allowWarmUp = pFInitConfig.allowWarmUp();
        }
        this.isInited = true;
        PFLog.w("PrefetchX", "PrefetchX inited sync by " + Thread.currentThread().getName() + ". " + BuildConfig.PREFETCHX_VERSION + " cost " + (SystemClock.uptimeMillis() - uptimeMillis) + "ms", new Throwable[0]);
    }

    public void prepare() {
        prepare(62, SUPPORT_ALL);
    }

    @Deprecated
    public void prepare(int i, long j) {
        asyncPrepare(i, j);
    }

    public void asyncPrepare(final int i, final long j) {
        getThreadExecutor().executeImmediately(new Runnable() {
            public void run() {
                PrefetchX.this.doPrepare(i, j);
            }
        });
    }

    public void syncPrepare() {
        syncPrepare(62, SUPPORT_ALL);
    }

    public void syncPrepare(int i, long j) {
        doPrepare(i, j);
    }

    /* access modifiers changed from: private */
    public void doPrepare(int i, long j) {
        detectIfWeexExists();
        boolean z = false;
        if (instance == null || !instance.isInited) {
            PFLog.w("PrefetchX", "PrefetchX has NOT been inited. call init() before prepare()", new Throwable[0]);
            throw new PFException("not inited");
        }
        PFLog.d("PrefetchX", "start to prepare PrefetchX. feature:", Integer.valueOf(i), " , support:", Long.valueOf(j));
        if ((i & 2) != 0 && !this.isDataFeatureOn) {
            PFMtop instance2 = PFMtop.getInstance();
            instance2.dataCallback = new PFDataCallbackImpl();
            instance2.dataUrlKeysAdapter = new PFDataUrlKeysAdapterImpl();
            if (this.allowWarmUp) {
                instance2.createMtopConfigCacheAndConfigMapUrls();
            }
            long j2 = 2 & j;
            if (j2 != 0) {
                instance2.weexStorage = StorageWeex.getInstance();
            }
            if ((4 & j) != 0) {
                instance2.memoryStorage = StorageMemory.getInstance();
            }
            if (instance2.weexStorage == null && instance2.memoryStorage == null) {
                PFLog.Data.w("NO date storage is enabled. Nowhere will save the result!", new Throwable[0]);
            }
            if (!hasWeex() && j2 != 0) {
                PFLog.Data.w("ignore SUPPORT_DATA_STORAGE_WEEX, because there is No weex", new Throwable[0]);
            }
            if ((8 & j) != 0 && hasWeex()) {
                SupportWeex.register();
            }
            if ((32 & j) != 0) {
                WMLPrefetch.getInstance().registerHandler(new SupportWindmill());
            }
            if ((16 & j) != 0) {
                WVEventService.getInstance().addEventListener(new SupportH5());
            }
            this.isDataFeatureOn = true;
        }
        if ((i & 4) != 0 && !this.isFileFeatureOn) {
            try {
                NetworkPrefetchInterceptor.registerSelf();
                if ((1024 & j) != 0) {
                    WXSDKEngine.registerModule(WXFilePrefetchModule.PREFETCH_MODULE_NAME, WXFilePrefetchModule.class);
                }
            } catch (Exception unused) {
            }
            this.isFileFeatureOn = true;
        }
        if ((i & 16) != 0 && !this.isJSModuleFeatureOn) {
            if (!PFDevice.isLowEndDevices() || this.mGlobalOnlineConfigManager.getJSModuleConfig().runOnLowDevices()) {
                z = true;
            } else {
                PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_NOT_RUN_ON_LOW_DEVICES, "not run on low devices", PFDevice.getOutlineInfo());
            }
            if (z) {
                PFJSModule.getInstance();
                if ((1048576 & j) != 0 && hasWeex()) {
                    com.alibaba.android.prefetchx.core.jsmodule.SupportWeex.register();
                }
                this.isJSModuleFeatureOn = true;
            }
        }
        if ((i & 32) != 0 && !this.isImageFeatureOn) {
            PFImage.getInstance();
            if ((SUPPORT_IMAGE_WEEX_MODULE & j) != 0 && hasWeex()) {
                com.alibaba.android.prefetchx.core.image.SupportWeex.register();
            }
            if ((j & SUPPORT_IMAGE_WINDVANE_PLUGIN) != 0) {
                com.alibaba.android.prefetchx.core.image.SupportH5.register();
            }
            this.isImageFeatureOn = true;
        }
        this.mReady = true;
    }

    public boolean hasWeex() {
        if (hasWeexDependency != null) {
            return hasWeexDependency.booleanValue();
        }
        return false;
    }

    private void detectIfWeexExists() {
        try {
            Class.forName("com.taobao.weex.WXSDKManager");
            hasWeexDependency = true;
            PFLog.d("PrefetchX", "weex in this app.");
        } catch (ClassNotFoundException unused) {
            hasWeexDependency = false;
            PFLog.d("PrefetchX", "weex NOT in this app.");
        } catch (NoClassDefFoundError unused2) {
            hasWeexDependency = false;
            PFLog.d("PrefetchX", "weex NOT in this app. by NoClassDefFoundError");
        } catch (Throwable unused3) {
            hasWeexDependency = false;
            PFLog.d("PrefetchX", "weex NOT in this app. by Throwable");
        }
    }

    public GlobalOnlineConfigManager getGlobalOnlineConfigManager() {
        return this.mGlobalOnlineConfigManager;
    }

    public AssetAdapter getAssetAdapter() {
        return this.mAssetAdapter;
    }

    public HttpAdapter getHttpAdapter() {
        return this.mHttpAdapter;
    }

    public boolean isReady() {
        return this.mReady;
    }

    public IThreadExecutor getThreadExecutor() {
        return this.mThreadExecutor;
    }
}
