package com.taobao.phenix.compat;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import anetwork.channel.monitor.Monitor;
import anetwork.channel.monitor.speed.NetworkSpeed;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.Variation;
import com.alibaba.ut.abtest.VariationSet;
import com.taobao.analysis.abtest.ABTestCenter;
import com.taobao.android.speed.TBSpeed;
import com.taobao.android.task.Coordinator;
import com.taobao.application.common.Apm;
import com.taobao.application.common.ApmManager;
import com.taobao.application.common.IApmEventListener;
import com.taobao.orange.OrangeConfigListenerV1;
import com.taobao.orange.OrangeConfigLocal;
import com.taobao.pexode.Pexode;
import com.taobao.pexode.decoder.APngDecoder;
import com.taobao.pexode.decoder.HeifDecoder;
import com.taobao.pexode.decoder.HeifMimeType;
import com.taobao.pexode.decoder.WebPConvert;
import com.taobao.pexode.mimetype.DefaultMimeTypes;
import com.taobao.phenix.cache.disk.DiskCacheKeyValueStore;
import com.taobao.phenix.common.UnitedLog;
import com.taobao.phenix.compat.TBCloudConfigCenter;
import com.taobao.phenix.compat.alivfs.AlivfsDiskKV;
import com.taobao.phenix.compat.alivfs.AlivfsVerifyListener;
import com.taobao.phenix.compat.stat.NavigationLifecycleObserver;
import com.taobao.phenix.compat.stat.TBImageRetrieveABListener;
import com.taobao.phenix.intf.Phenix;
import com.taobao.rxm.common.RxModel4Phenix;
import com.taobao.tao.image.IImageExtendedSupport;
import com.taobao.tao.image.IImageStrategySupport;
import com.taobao.tao.image.ImageInitBusinss;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.tao.image.TTLStrategyConfigListener;
import com.taobao.tcommon.core.VisibleForTesting;
import com.taobao.uikit.extend.feature.view.TUrlImageView;
import java.io.Serializable;
import java.util.HashMap;
import mtopsdk.mtop.global.SDKUtils;

public class PhenixInitializer implements Serializable {
    private static int mABRetryCount = 0;
    private static boolean mABValid = false;
    private static TBCloudConfigCenter mCloudCenter = null;
    /* access modifiers changed from: private */
    public static boolean mEnableTTL = false;
    static TBImageRetrieveABListener mRetrieveABListener = new TBImageRetrieveABListener() {
        public long getExperimentId() {
            if (!PhenixInitializer.mEnableTTL || !PhenixInitializer.mUseTTL) {
                return 0;
            }
            return TBCloudConfigCenter.EXPERIMENT_ID;
        }
    };
    /* access modifiers changed from: private */
    public static boolean mUseTTL = false;
    private static boolean sABInited = false;
    private static HeifDecoder sHeifDecoder = null;
    /* access modifiers changed from: private */
    public static boolean sHeifPngSupported = false;
    /* access modifiers changed from: private */
    public static boolean sHeifSupported = false;
    private static boolean sInited = false;
    private static boolean sNewLaunchValid = true;
    private static boolean sUseDecouple;
    private static boolean sUserNewLaunch;
    /* access modifiers changed from: private */
    public static boolean sWebPSupported;

    public void init(Application application, HashMap<String, Object> hashMap) {
        if (hashMap != null && hashMap.containsKey("isNextLaunch")) {
            sUserNewLaunch = true;
        }
        if (hashMap != null && hashMap.containsKey("ngLaunch")) {
            sUseDecouple = true;
        }
        try {
            Class.forName("com.taobao.analysis.fulltrace.FullTraceAnalysis");
            mABValid = true;
        } catch (Exception unused) {
            mABValid = false;
        }
        initPhenix(application);
        initImageStrategy(application);
        TUrlImageView.registerActivityCallback(application);
        UnitedLog.i("TBCompat4Phenix", "all init complete", new Object[0]);
    }

    public static void initHeif(Context context) {
        if (sNewLaunchValid) {
            TBCloudConfigCenter instance = TBCloudConfigCenter.getInstance(context);
            if (instance.isFeatureEnabled(22)) {
                sHeifDecoder = new HeifDecoder();
                Pexode.installDecoder(sHeifDecoder);
                boolean isFeatureEnabled = instance.isFeatureEnabled(30);
                if (sHeifDecoder != null) {
                    HeifDecoder heifDecoder = sHeifDecoder;
                    HeifDecoder.useHeifBugFix(isFeatureEnabled);
                }
                sHeifSupported = Pexode.canSupport(HeifMimeType.HEIF);
            }
        }
    }

    public static void initApng(Context context) {
        if (sNewLaunchValid && TBCloudConfigCenter.getInstance(context).isFeatureEnabled(20)) {
            Pexode.installDecoder(new APngDecoder());
        }
    }

    public static void initTBScheduler(Context context) {
        TBScheduler4Phenix.setupScheduler(TBCloudConfigCenter.getInstance(context).isFeatureEnabled(12), TBCloudConfigCenter.getInstance(context).isFeatureEnabled(14));
        if (TBSpeed.isSpeedEdition(context, "UIPost")) {
            Phenix.instance().usePostFrontUI(true);
            RxModel4Phenix.setUsePostAtFront(true);
            registerApmForRx();
        }
    }

    public static void registerApmForRx() {
        ApmManager.addAppLaunchListener(new Apm.OnAppLaunchListener() {
            public void onLaunchChanged(int i, int i2) {
                if (i2 == 4) {
                    RxModel4Phenix.setUsePostAtFront(false);
                }
            }
        });
    }

    public static void initBuild(Context context) {
        Phenix.instance().build();
        StatMonitor4Phenix.setupFlowMonitor(context, new TBNetworkAnalyzer(), TBCloudConfigCenter.getInstance(context).getFeatureCoverage(13), TBCloudConfigCenter.getInstance(context).getFeatureCoverage(21), 524288, mRetrieveABListener);
        TBNetwork4Phenix.setupQualityChangedMonitor();
        if (mEnableTTL) {
            registerApmForTTL();
        }
    }

    public static void registerApmForTTL() {
        ApmManager.addAppLaunchListener(new Apm.OnAppLaunchListener() {
            public void onLaunchChanged(int i, int i2) {
                if (i2 == 4) {
                    PhenixInitializer.updateABTest();
                }
            }
        });
        ApmManager.addApmEventListener(new IApmEventListener() {
            public void onEvent(int i) {
                if (i == 2) {
                    PhenixInitializer.updateABTest();
                }
            }
        });
    }

    static synchronized boolean initPhenix(Context context) {
        synchronized (PhenixInitializer.class) {
            if (sInited) {
                return true;
            }
            UnitedLog.setFormatLog(new TFormatLog());
            Phenix.instance().with(context);
            Phenix.instance().setModuleStrategySupplier(new TBModuleStrategySupplier());
            initTTL(context);
            TBNetwork4Phenix.setupHttpLoader(context);
            Alivfs4Phenix.setupDiskCache();
            if (!sUseDecouple) {
                TBScheduler4Phenix.setupScheduler(TBCloudConfigCenter.getInstance(context).isFeatureEnabled(12), TBCloudConfigCenter.getInstance(context).isFeatureEnabled(14));
            }
            if (mABValid) {
                String uTABTestValue = ABTestCenter.getUTABTestValue("PHENIX", "DISK");
                String uTABTestValue2 = ABTestCenter.getUTABTestValue("PHENIX", "OSVER");
                int i = 23;
                if (!TextUtils.isEmpty(uTABTestValue2)) {
                    i = Integer.valueOf(uTABTestValue2).intValue();
                }
                if (Build.VERSION.SDK_INT >= i && !TextUtils.isEmpty(uTABTestValue)) {
                    Phenix.instance().diskCacheBuilder().maxSize(17, Integer.valueOf(uTABTestValue).intValue() * 1048576);
                }
                UnitedLog.e("TBCompat4Phenix", "DiskCache=%s", uTABTestValue);
            }
            boolean z = TBSpeed.isSpeedEdition(context, "ImgThread") && TBCloudConfigCenter.getInstance(context).isFeatureEnabled(24);
            Phenix.instance().useNewThreadModel(z);
            StatMonitor4Phenix.mIsUseNewThreadModel = z;
            RxModel4Phenix.setUseNewThread(z);
            UnitedLog.e("TBCompat4Phenix", "new-thread=%d", Integer.valueOf(z ? 1 : 0));
            RxModel4Phenix.setUseRecycle(TBCloudConfigCenter.getInstance(context).isFeatureEnabled(25));
            sNewLaunchValid = TBCloudConfigCenter.getInstance(context).isFeatureEnabled(27);
            UnitedLog.e("TBCompat4Phenix", "use-new-launch=%d", Integer.valueOf(sNewLaunchValid ? 1 : 0));
            TBCloudConfigCenter instance = TBCloudConfigCenter.getInstance(context);
            if (!instance.isFeatureEnabled(18)) {
                Phenix.instance().bitmapPoolBuilder().maxSize(0);
            }
            if (!sUseDecouple) {
                Phenix.instance().build();
            }
            try {
                setupPexodeAbility(instance, true);
                Pexode.setBytesPool(Phenix.instance().bytesPoolBuilder().build());
                Pexode.prepare(context);
                instance.addConfigChangeListener(new TBCloudConfigCenter.CloudConfigChangeListener() {
                    public void onConfigUpdated(TBCloudConfigCenter tBCloudConfigCenter) {
                        PhenixInitializer.setupPexodeAbility(tBCloudConfigCenter, false);
                    }
                });
            } catch (Throwable th) {
                UnitedLog.e("TBCompat4Phenix", "init pexode error=%s", th);
            }
            if (!sUseDecouple) {
                StatMonitor4Phenix.setupFlowMonitor(context, new TBNetworkAnalyzer(), TBCloudConfigCenter.getInstance(context).getFeatureCoverage(13), TBCloudConfigCenter.getInstance(context).getFeatureCoverage(21), 524288, mRetrieveABListener);
                TBNetwork4Phenix.setupQualityChangedMonitor();
            }
            UnitedLog.i("TBCompat4Phenix", "phenix init complete", new Object[0]);
            sInited = true;
            return false;
        }
    }

    private static void initTTL(Context context) {
        mCloudCenter = TBCloudConfigCenter.getInstance(context);
        mEnableTTL = mCloudCenter.isFeatureEnabled(38);
        AlivfsDiskKV alivfsDiskKV = new AlivfsDiskKV("");
        alivfsDiskKV.setAlivfsVerifyListener(new AlivfsVerifyListener() {
            public boolean isTTLDomain(String str) {
                return PhenixInitializer.mEnableTTL && PhenixInitializer.mUseTTL && !TextUtils.isEmpty(str) && str.contains(TBCloudConfigCenter.TTL_DOMAIN);
            }

            public boolean isExpectedTime(long j) {
                return PhenixInitializer.mEnableTTL && PhenixInitializer.mUseTTL && j < TBCloudConfigCenter.TTL_MAX_VALUE;
            }

            public long getCurrentTime() {
                return SDKUtils.getCorrectionTime();
            }
        });
        ImageInitBusinss.setTTLNotifyListener(new TTLStrategyConfigListener() {
            public void notifyTTLConfigUpdate(String str, String str2) {
                if (!TextUtils.isEmpty(str) && !str.equals(TBCloudConfigCenter.TTL_DOMAIN)) {
                    TBCloudConfigCenter.TTL_DOMAIN = str;
                }
                if (!TextUtils.isEmpty(str2)) {
                    TBCloudConfigCenter.TTL_MAX_VALUE = Long.valueOf(str2).longValue();
                }
            }
        });
        Phenix.instance().diskCacheKVBuilder().with((DiskCacheKeyValueStore) alivfsDiskKV);
        updateTTL();
    }

    private static void updateTTL() {
        try {
            String cacheString = mCloudCenter.getCacheString(TBCloudConfigCenter.OPEN_TTL);
            String cacheString2 = mCloudCenter.getCacheString(TBCloudConfigCenter.EXPERIMENT_AB_ID);
            if (!TextUtils.isEmpty(cacheString) && "1".equals(cacheString)) {
                mUseTTL = true;
            }
            if (!TextUtils.isEmpty(cacheString2)) {
                TBCloudConfigCenter.EXPERIMENT_ID = Long.valueOf(cacheString2).longValue();
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: private */
    public static void updateABTest() {
        if (!sABInited && mEnableTTL && mABRetryCount < 4) {
            mABRetryCount++;
            VariationSet activate = UTABTest.activate(TBCloudConfigCenter.EXPERIMENT_NAMESPACE, TBCloudConfigCenter.IMAGE_MODULE);
            Variation variation = activate.getVariation(TBCloudConfigCenter.OPEN_TTL);
            if (variation != null) {
                String valueAsString = variation.getValueAsString("0");
                if ("0".equals(valueAsString)) {
                    mUseTTL = false;
                } else if ("1".equals(valueAsString)) {
                    mUseTTL = true;
                }
                sABInited = true;
                TBCloudConfigCenter.EXPERIMENT_ID = activate.getExperimentBucketId();
                if (mCloudCenter != null) {
                    mCloudCenter.setCacheValue(TBCloudConfigCenter.OPEN_TTL, valueAsString);
                    mCloudCenter.setCacheValue(TBCloudConfigCenter.EXPERIMENT_AB_ID, String.valueOf(TBCloudConfigCenter.EXPERIMENT_ID));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static synchronized void setupPexodeAbility(TBCloudConfigCenter tBCloudConfigCenter, boolean z) {
        synchronized (PhenixInitializer.class) {
            boolean isFeatureEnabled = tBCloudConfigCenter.isFeatureEnabled(15);
            boolean isFeatureEnabled2 = tBCloudConfigCenter.isFeatureEnabled(16);
            boolean isFeatureEnabled3 = tBCloudConfigCenter.isFeatureEnabled(19);
            boolean isFeatureEnabled4 = tBCloudConfigCenter.isFeatureEnabled(29);
            Pexode.useAndroidPDecode = isFeatureEnabled4;
            UnitedLog.e("TBCompat4Phenix", "use-AndroidP=%d", Integer.valueOf(isFeatureEnabled4 ? 1 : 0));
            ImageStrategyConfig.isWebpDegrade = !tBCloudConfigCenter.isFeatureEnabled(31);
            UnitedLog.e("TBCompat4Phenix", "use-Degrade-webp=%d", Integer.valueOf(ImageStrategyConfig.isWebpDegrade ? 1 : 0));
            boolean isFeatureEnabled5 = tBCloudConfigCenter.isFeatureEnabled(33);
            Pexode.useWebpConvert = isFeatureEnabled5;
            UnitedLog.e("TBCompat4Phenix", "use-webp-convert=%d", Integer.valueOf(isFeatureEnabled5 ? 1 : 0));
            boolean isFeatureEnabled6 = tBCloudConfigCenter.isFeatureEnabled(32);
            Phenix.NO_USE_WEBP_FORMAT = isFeatureEnabled6;
            UnitedLog.e("TBCompat4Phenix", "use-no-reuse-webp=%d", Integer.valueOf(isFeatureEnabled6 ? 1 : 0));
            boolean isFeatureEnabled7 = tBCloudConfigCenter.isFeatureEnabled(37);
            ImageStrategyConfig.isUseSpecialDomain = isFeatureEnabled7;
            UnitedLog.e("TBCompat4Phenix", "use-special-domain=%d", Integer.valueOf(isFeatureEnabled7 ? 1 : 0));
            boolean isFeatureEnabled8 = tBCloudConfigCenter.isFeatureEnabled(35);
            if (Build.VERSION.SDK_INT == 28 && WebPConvert.sIsSoInstalled) {
                WebPConvert.nativeUseBugFix(isFeatureEnabled8);
            }
            UnitedLog.e("TBCompat4Phenix", "use-webp-bugfix=%d", Integer.valueOf(isFeatureEnabled8 ? 1 : 0));
            int i = (Build.VERSION.SDK_INT < 29 || !tBCloudConfigCenter.isFeatureEnabled(36)) ? 0 : 1;
            if (i != 0) {
                Phenix.instance().useAndroidQThumb(true);
            }
            UnitedLog.e("TBCompat4Phenix", "use-new-thumb=%d", Integer.valueOf(i));
            if (z && (!sNewLaunchValid || !sUserNewLaunch)) {
                if (tBCloudConfigCenter.isFeatureEnabled(20)) {
                    Pexode.installDecoder(new APngDecoder());
                }
                if (tBCloudConfigCenter.isFeatureEnabled(22)) {
                    sHeifDecoder = new HeifDecoder();
                    Pexode.installDecoder(sHeifDecoder);
                }
            }
            boolean isFeatureEnabled9 = tBCloudConfigCenter.isFeatureEnabled(30);
            if (sHeifDecoder != null) {
                HeifDecoder heifDecoder = sHeifDecoder;
                HeifDecoder.useHeifBugFix(isFeatureEnabled9);
            }
            UnitedLog.e("TBCompat4Phenix", "use-HeifBugFix=%d", Integer.valueOf(isFeatureEnabled9 ? 1 : 0));
            Pexode.forceDegrade2System(!isFeatureEnabled);
            Pexode.enableCancellability(isFeatureEnabled2);
            Pexode.forceDegrade2NoAshmem(!isFeatureEnabled3);
            sWebPSupported = Pexode.canSupport(DefaultMimeTypes.WEBP) && Pexode.canSupport(DefaultMimeTypes.WEBP_A);
            if (!sNewLaunchValid || !sUserNewLaunch) {
                sHeifSupported = Pexode.canSupport(HeifMimeType.HEIF);
            }
            mEnableTTL = tBCloudConfigCenter.isFeatureEnabled(38);
            UnitedLog.e("TBCompat4Phenix", "mEnableTTL=%d", Integer.valueOf(mEnableTTL ? 1 : 0));
            sHeifPngSupported = tBCloudConfigCenter.isFeatureEnabled(23);
            UnitedLog.e("TBCompat4Phenix", "sHeifSupported=%b, sHeifPngSupported=%b", Boolean.valueOf(sHeifSupported), Boolean.valueOf(sHeifPngSupported));
            UnitedLog.i("TBCompat4Phenix", "setup pexode ability with heif=%b, webp=%b, external_prior=%b, cancellable=%b, ashmem=%b, initializing=%b", Boolean.valueOf(sHeifSupported), Boolean.valueOf(sWebPSupported), Boolean.valueOf(isFeatureEnabled), Boolean.valueOf(isFeatureEnabled2), Boolean.valueOf(isFeatureEnabled3), Boolean.valueOf(z));
        }
    }

    private void initImageStrategy(Application application) {
        ImageInitBusinss.newInstance(application, new IImageStrategySupport() {
            public String getConfigString(String str, String str2, String str3) {
                return OrangeConfigLocal.getInstance().getConfig(str, str2, str3);
            }

            public boolean isSupportWebP() {
                return PhenixInitializer.sWebPSupported;
            }

            public boolean isNetworkSlow() {
                return Monitor.getNetworkSpeed() == NetworkSpeed.Slow;
            }
        });
        ImageInitBusinss.getInstance().setImageExtendedSupport(new IImageExtendedSupport() {
            public boolean isHEIFSupported() {
                return PhenixInitializer.sHeifSupported;
            }

            public boolean isHEIFPngSupported() {
                return PhenixInitializer.sHeifPngSupported;
            }
        });
        OrangeConfigLocal.getInstance().registerListener(new String[]{ImageInitBusinss.IMAGE_CONFIG}, (OrangeConfigListenerV1) new OrangeConfigListenerV1() {
            public void onConfigUpdate(String str, boolean z) {
                if (ImageInitBusinss.IMAGE_CONFIG.equals(str)) {
                    ImageInitBusinss.getInstance().notifyConfigsChange();
                }
            }
        });
        Coordinator.postTask(new Coordinator.TaggedRunnable("initImageConfig") {
            public void run() {
                OrangeConfigLocal.getInstance().getConfigs(ImageInitBusinss.IMAGE_CONFIG);
            }
        });
        UnitedLog.i("TBCompat4Phenix", "image_strategy init complete", new Object[0]);
    }

    @VisibleForTesting
    static void reset() {
        if (!(Phenix.instance() == null || Phenix.instance().applicationContext() == null)) {
            ((Application) Phenix.instance().applicationContext()).unregisterActivityLifecycleCallbacks(NavigationLifecycleObserver.getInstance());
        }
        sInited = false;
    }
}
