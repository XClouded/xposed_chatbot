package com.taobao.weex;

import android.os.AsyncTask;
import android.util.Log;
import com.ali.adapt.api.AliAdaptServiceManager;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.IConfigGeneratorAdapter;
import com.alibaba.aliweex.adapter.IConfigModuleAdapter;
import com.alibaba.aliweex.adapter.component.WXIExternalComponentGetter;
import com.alibaba.aliweex.adapter.module.WXConfigModule;
import com.alibaba.aliweex.hc.HC;
import com.alibaba.aliweex.hc.adapter.HCWXHttpAdapter;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.android.WeexEnhance;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.triver.triver_weex.WXAriverComponent;
import com.ta.utdid2.device.UTDevice;
import com.taobao.aiimage.sdk.weex.WXAIImageComponent;
import com.taobao.aiimage.sdk.weex.WXAIImageModule;
import com.taobao.alimama.AlimamaAdWeexSupportModule;
import com.taobao.android.capsule.CapsuleBundle;
import com.taobao.android.eagle.EagleLauncher;
import com.taobao.android.festival.jsbridge.TBSkinThemeWXModule;
import com.taobao.android.interactive_common.InteractiveWX;
import com.taobao.android.msoa.MSOAWeexModule;
import com.taobao.android.qking.QkingLauncher;
import com.taobao.android.shop.features.calendar.ShopWXCalendarManager;
import com.taobao.android.shop.weex.ShopDataModule;
import com.taobao.android.shop.weex.ShopParallelRenderModule;
import com.taobao.android.shop.weex.ShopReportModule;
import com.taobao.fashionai.common.FashionAI;
import com.taobao.ma.api.ITBInsideService;
import com.taobao.reward.api.ITBRewardService;
import com.taobao.search.weex.SearchWeexBundle;
import com.taobao.tao.Globals;
import com.taobao.tao.content.basic.utils.CBInit;
import com.taobao.tao.util.TaoHelper;
import com.taobao.taolivehome.weex.TBLiveBundle;
import com.taobao.weex.adapter.IWXJSExceptionAdapter;
import com.taobao.weex.adapter.StageEyeAdapter;
import com.taobao.weex.adapter.TBConfigAdapter;
import com.taobao.weex.adapter.TBConfigModuleAdapter;
import com.taobao.weex.adapter.TBNavigatorAdapter;
import com.taobao.weex.atlas.getter.AtlasExternalComponentGetter;
import com.taobao.weex.atlas.getter.AtlasExternalLoaderComponentHolder;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXConfig;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXJSExceptionInfo;
import com.taobao.weex.compontent.WXErrorViewComponent;
import com.taobao.weex.module.TBLiveComponentGetter;
import com.taobao.weex.module.WXEventModule;
import com.taobao.weex.module.WXFestivalModule;
import com.taobao.weex.module.WXPageInfoModule;
import com.taobao.weex.module.WXPageModule;
import com.taobao.weex.module.WXShareModule;
import com.taobao.weex.module.WXTBModalUIModule;
import com.taobao.weex.module.WXTBUtils;
import com.taobao.weex.module.WXUserModule;
import com.taobao.weex.module.live.TBLiveModuleGetter;
import com.taobao.weex.module.market.WXMarketModule;
import com.taobao.weex.module.monitor.AliAPMAdaptorModule;
import com.taobao.weex.module.monitor.ContainerMonitorModule;
import com.taobao.weex.ui.ExternalLoaderComponentHolder;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.IExternalModuleGetter;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.wopc.WopcSdkGateway;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.media.CommandID;

public class TBWXSDKEngine extends WXSDKEngine {
    private static volatile boolean m_fromLauncher = true;
    private static volatile boolean m_hasBeenInit = false;

    public static void initSDKEngine(boolean z) {
        if (!m_hasBeenInit) {
            m_hasBeenInit = true;
            m_fromLauncher = z;
            initSDKEngine();
        }
    }

    public static void initSDKEngine() {
        WXLogUtils.d("[TBWXSDKEngine] initSDKEngine");
        WXEnvironment.addCustomOptions("appName", "TB");
        WXEnvironment.addCustomOptions(WXConfig.appGroup, "AliApp");
        WXEnvironment.addCustomOptions("utdid", UTDevice.getUtdid(Globals.getApplication()));
        WXEnvironment.addCustomOptions("ttid", TaoHelper.getTTID());
        AliWeex.Config.Builder builder = new AliWeex.Config.Builder();
        EagleLauncher.initPlugin();
        QkingLauncher.initPlugin();
        builder.setEventModuleAdapter(new WXEventModule()).setPageInfoModuleAdapter(new WXPageInfoModule()).setShareModuleAdapter(new WXShareModule()).setUserModuleAdapter(new WXUserModule()).setConfigAdapter(new TBConfigAdapter()).setConfigGeneratorAdapter(new ConfigGeneratorAdapter()).setHttpAdapter(new HCWXHttpAdapter()).setFestivalModuleAdapter(new WXFestivalModule()).setGodEyeStageAdapter(new StageEyeAdapter());
        AliWeex.getInstance().initWithConfig(Globals.getApplication(), builder.build());
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                AliWXSDKEngine.initSDKEngine();
                WXSDKEngine.setNavigator(new TBNavigatorAdapter());
                HC.getInstance().init(new WXMarketModule());
                TBWXSDKEngine.registerModulesAndComponents();
                WXBundleInit.init();
            }
        };
        boolean parseBoolean = Boolean.parseBoolean(WXInitConfigManager.getInstance().getFromConfigKV(WXInitConfigManager.getInstance().c_enable_lazy_init));
        boolean parseBoolean2 = Boolean.parseBoolean(WXInitConfigManager.getInstance().getFromConfigKV(WXInitConfigManager.getInstance().c_enable_init_async));
        String str = "init weex from Launcher";
        if (m_fromLauncher) {
            if (parseBoolean2) {
                str = str + " async";
                WXBridgeManager.getInstance().post(r0);
            } else {
                str = str + " sync";
                r0.run();
            }
        } else if (parseBoolean) {
            str = "init weex from lazy async";
            WXBridgeManager.getInstance().post(r0);
        }
        WXLogUtils.e(str);
    }

    /* access modifiers changed from: private */
    public static void registerModulesAndComponents() {
        try {
            WeexEnhance.prepare();
            registerModule("modal", WXTBModalUIModule.class, false);
            registerModule("orange", WXConfigModule.class, false);
            registerModule(UMLLCons.FEATURE_TYPE_MSOA, MSOAWeexModule.class);
            registerModule("skin", TBSkinThemeWXModule.class);
            registerModule("weexContainerMonitor", ContainerMonitorModule.class);
            registerModule("pageState", WXPageModule.class);
            WXSDKEngine.registerModule("aiimage", WXAIImageModule.class);
            WXSDKEngine.registerComponent("aiimageview", (Class<? extends WXComponent>) WXAIImageComponent.class);
            try {
                WXLogUtils.e("Ariver", "Register ariver component");
                WXSDKEngine.registerComponent("ariver", (Class<? extends WXComponent>) WXAriverComponent.class);
            } catch (Throwable th) {
                WXLogUtils.e("Ariver", "Register ariver component failed");
                WXLogUtils.e("Ariver", th);
                th.printStackTrace();
            }
            WXSDKEngine.registerModule("shopData", ShopDataModule.class);
            WXSDKEngine.registerModule("shopReport", ShopReportModule.class);
            WXSDKEngine.registerModule("shopParallenRender", ShopParallelRenderModule.class);
            WXSDKEngine.registerModule("shopParallelRender", ShopParallelRenderModule.class);
            WXSDKEngine.registerModule("shopWXCalendar", ShopWXCalendarManager.class);
            WXSDKEngine.registerModuleWithFactory("tbMiniLiveRoom", (IExternalModuleGetter) new TBLiveModuleGetter(), true);
            WXSDKEngine.registerComponent((IFComponentHolder) new ExternalLoaderComponentHolder("video", new TBLiveComponentGetter()) {
                public String[] getMethods() {
                    return new String[]{Constants.Value.PLAY, "pause", "setCurrentTime", "getCurrentTime", CommandID.setVolume, "getVolume", CommandID.setMuted, "getMuted"};
                }
            }, true, "video");
            WXSDKEngine.registerComponent((IFComponentHolder) new ExternalLoaderComponentHolder("tbliveMiniRoom", new TBLiveComponentGetter()) {
                public String[] getMethods() {
                    return new String[]{Constants.Value.PLAY, "pause", CommandID.setMuted};
                }
            }, true, "tbliveMiniRoom");
            WXSDKEngine.registerComponent("alilivephoto", (IExternalComponentGetter) new WXIExternalComponentGetter(), true);
            WXSDKEngine.registerComponent((IFComponentHolder) new AtlasExternalLoaderComponentHolder("videoplus", new AtlasExternalComponentGetter()) {
                public String[] getMethods() {
                    return new String[]{Constants.Value.PLAY, "pause", "setCurrentTime", "getCurrentTime", CommandID.setMuted, "getMuted", "getDuration", "getScreenMode", "setInstanceMode"};
                }
            }, true, "videoplus");
            WXSDKEngine.registerComponent("followsdkbutton", (IExternalComponentGetter) new WXIExternalComponentGetter(), true);
            WXSDKEngine.registerModule("tbutils", WXTBUtils.class);
            WXSDKEngine.registerComponent("tberrorview", (Class<? extends WXComponent>) WXErrorViewComponent.class);
            WXSDKEngine.registerModule("AlimamaAdModule", AlimamaAdWeexSupportModule.class);
            WXSDKEngine.registerModule("aliHAFeedback", AliAPMAdaptorModule.class);
            SearchWeexBundle.register();
            TBLiveBundle.register();
            CapsuleBundle.initBundle(Globals.getApplication());
            InteractiveWX.register();
            CBInit.init();
            registerRewardPlugin();
            registerInsidePlugin();
            WopcSdkGateway.initWeexModule();
            FashionAI.initWeexModule(Globals.getApplication());
        } catch (WXException e) {
            WXLogUtils.e("[TBWXSDKEngine] registerModulesAndComponents:" + e.getCause());
        }
    }

    private static void registerInsidePlugin() {
        new AsyncTask<Void, Void, ITBInsideService>() {
            /* access modifiers changed from: protected */
            public ITBInsideService doInBackground(Void... voidArr) {
                try {
                    return (ITBInsideService) AliAdaptServiceManager.getInstance().findAliAdaptService(ITBInsideService.class);
                } catch (Exception e) {
                    Log.e("TBWXSDKEngine", "find ITBInsideService error", e);
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(ITBInsideService iTBInsideService) {
                if (iTBInsideService != null) {
                    try {
                        iTBInsideService.registerModule();
                        Log.d("TBWXSDKEngine", "find ITBInsideService success");
                    } catch (Exception e) {
                        Log.e("TBWXSDKEngine", "register inside WXModule error", e);
                    }
                } else {
                    Log.d("TBWXSDKEngine", "find ITBInsideService null");
                }
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    private static void registerRewardPlugin() {
        new AsyncTask<Void, Void, ITBRewardService>() {
            /* access modifiers changed from: protected */
            public ITBRewardService doInBackground(Void... voidArr) {
                try {
                    return (ITBRewardService) AliAdaptServiceManager.getInstance().findAliAdaptService(ITBRewardService.class);
                } catch (Exception e) {
                    Log.e("TBWXSDKEngine", "find ITBRewardService error", e);
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(ITBRewardService iTBRewardService) {
                if (iTBRewardService != null) {
                    try {
                        iTBRewardService.registerWXModule();
                        Log.d("TBWXSDKEngine", "find ITBRewardService success");
                    } catch (Exception e) {
                        Log.e("TBWXSDKEngine", "register rs WXModule error", e);
                    }
                } else {
                    Log.d("TBWXSDKEngine", "find ITBRewardService null");
                }
            }
        }.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    private static class JSExceptionGlobalEventReporter implements IWXJSExceptionAdapter {
        IWXJSExceptionAdapter mOriginal;

        JSExceptionGlobalEventReporter(IWXJSExceptionAdapter iWXJSExceptionAdapter) {
            this.mOriginal = iWXJSExceptionAdapter;
        }

        public void onJSException(WXJSExceptionInfo wXJSExceptionInfo) {
            if (wXJSExceptionInfo != null) {
                WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(wXJSExceptionInfo.getInstanceId());
                if (sDKInstance != null) {
                    JSONObject jSONObject = null;
                    try {
                        jSONObject = JSON.parseObject(JSON.toJSONString(wXJSExceptionInfo));
                    } catch (Exception unused) {
                        jSONObject.put("bundleUrl", wXJSExceptionInfo.getBundleUrl());
                        jSONObject.put("errorCode", wXJSExceptionInfo.getErrCode());
                        jSONObject.put(UCCore.EVENT_EXCEPTION, wXJSExceptionInfo.getException());
                        jSONObject.put("extParams", wXJSExceptionInfo.getExtParams());
                        jSONObject.put("function", wXJSExceptionInfo.getFunction());
                        jSONObject.put(BindingXConstants.KEY_INSTANCE_ID, wXJSExceptionInfo.getInstanceId());
                        jSONObject.put("jsFrameworkVersion", wXJSExceptionInfo.getJsFrameworkVersion());
                        jSONObject.put("weexVersion", wXJSExceptionInfo.getWeexVersion());
                    }
                    sDKInstance.fireGlobalEventCallback(UCCore.EVENT_EXCEPTION, jSONObject);
                }
                if (this.mOriginal != null) {
                    this.mOriginal.onJSException(wXJSExceptionInfo);
                }
            }
        }
    }

    public static class ConfigGeneratorAdapter implements IConfigGeneratorAdapter {
        public IConfigModuleAdapter generateConfigInstance(String str) {
            return new TBConfigModuleAdapter();
        }
    }
}
