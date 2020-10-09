package com.taobao.weex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.aliweex.adapter.component.WXIExternalComponentGetter;
import com.alibaba.aliweex.adapter.component.WXIExternalModuleGetter;
import com.taobao.android.nav.Nav;
import com.taobao.weex.adapter.R;
import com.taobao.weex.appbar.AppbarComponent;
import com.taobao.weex.atlas.getter.AtlasExternalComponentGetter;
import com.taobao.weex.atlas.getter.AtlasExternalLoaderComponentHolder;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXException;
import com.taobao.weex.module.WXMessageStatusModule;
import com.taobao.weex.module.WXShopMenuExtendModule;
import com.taobao.weex.module.actionsheet.WXActionSheetModule;
import com.taobao.weex.ui.ExternalLoaderComponentHolder;
import com.taobao.weex.ui.IExternalComponentGetter;
import com.taobao.weex.ui.IExternalModuleGetter;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXTLogImpl;
import com.uc.webview.export.media.CommandID;

public class WXBundleInit {
    private static final String TAG = "WXBundleInit";

    public static void init() {
        try {
            if (!WXEnvironment.isApkDebugable()) {
                WXTLogImpl.initWXLogWatcher();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (WXEnvironment.isApkDebugable()) {
            DevToolReceive devToolReceive = new DevToolReceive();
            WXEnvironment.getApplication().registerReceiver(devToolReceive, new IntentFilter("remoteDebugProxyUrl"));
            WXEnvironment.getApplication().registerReceiver(devToolReceive, new IntentFilter("unRegisterDevTool"));
        }
        try {
            WXSDKEngine.registerComponent(AppbarComponent.TYPE, (Class<? extends WXComponent>) AppbarComponent.class);
            WXSDKEngine.registerModule("messagestatus", WXMessageStatusModule.class);
            WXSDKEngine.registerModule("actionSheet", WXActionSheetModule.class);
            WXSDKEngine.registerModule("shopMenu", WXShopMenuExtendModule.class);
            WXSDKEngine.registerCoreEnv("defaultNavWidth", String.valueOf(WXEnvironment.getApplication().getResources().getDimensionPixelSize(R.dimen.appbar_nav_button_width)));
            WXSDKEngine.registerCoreEnv("defaultOverflowWidth", String.valueOf(WXEnvironment.getApplication().getResources().getDimensionPixelSize(R.dimen.appbar_overflow_button_width)));
            WXSDKEngine.registerCoreEnv("appbar_color", WXEnvironment.getApplication().getResources().getString(R.string.appbar_color));
            WXSDKEngine.registerCoreEnv("appbar_background_color", WXEnvironment.getApplication().getResources().getString(R.string.appbar_background_color));
        } catch (WXException e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
        registerComponentAndModule();
    }

    private static void registerComponentAndModule() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            HandlerThread handlerThread = new HandlerThread("awareness_and_brand_loader_thread");
            handlerThread.start();
            new Handler(handlerThread.getLooper()).post(new Runnable() {
                public void run() {
                    WXBundleInit.regAwareness();
                    WXBundleInit.regBrand();
                    WXBundleInit.regVideo();
                    WXBundleInit.regLiveBubble();
                    WXBundleInit.regArtcModule();
                    WXBundleInit.regArtcComponent();
                }
            });
            return;
        }
        regAwareness();
        regBrand();
        regVideo();
        regLiveBubble();
        regArtcModule();
        regArtcComponent();
    }

    /* access modifiers changed from: private */
    public static void regVideo() {
        try {
            WXSDKEngine.registerComponent((IFComponentHolder) new AtlasExternalLoaderComponentHolder("videoplus", new AtlasExternalComponentGetter()) {
                public String[] getMethods() {
                    return new String[]{Constants.Value.PLAY, "pause", "setCurrentTime", "getCurrentTime", CommandID.setMuted, "getMuted", "getDuration", "getScreenMode", "setInstanceMode"};
                }
            }, true, "videoplus");
        } catch (WXException e) {
            Log.e(TAG, WXLogUtils.getStackTrace(e));
        }
    }

    /* access modifiers changed from: private */
    public static void regAwareness() {
        try {
            WXSDKEngine.registerComponent("officialSubscribe", (IExternalComponentGetter) new WXIExternalComponentGetter(), false);
        } catch (Throwable th) {
            Log.e(TAG, "registerModuleWithFactory[officialSubscribe]", th);
        }
    }

    /* access modifiers changed from: private */
    public static void regBrand() {
        try {
            WXSDKEngine.registerModuleWithFactory("pictureBook2Module", (IExternalModuleGetter) new WXIExternalModuleGetter(), false);
        } catch (Throwable th) {
            Log.e(TAG, "registerModuleWithFactory[pictureBookModule]", th);
        }
    }

    /* access modifiers changed from: private */
    public static void regLiveBubble() {
        try {
            WXSDKEngine.registerComponent((IFComponentHolder) new ExternalLoaderComponentHolder("liveshowbubble", new WXIExternalComponentGetter()) {
                public String[] getMethods() {
                    return new String[]{"setBubbleScale", "setBubbleLoop", "setBubbleSpeed", "setBubbleZip", "bubble", "stop", "start"};
                }
            }, true, "liveshowbubble");
        } catch (Throwable th) {
            Log.e(TAG, "registerComponent[bubble]", th);
        }
    }

    /* access modifiers changed from: private */
    public static void regArtcComponent() {
        try {
            WXSDKEngine.registerComponent("ArtcViewComponent", (IExternalComponentGetter) new WXIExternalComponentGetter(), false);
        } catch (WXException e) {
            Log.e(TAG, WXLogUtils.getStackTrace(e));
        }
    }

    /* access modifiers changed from: private */
    public static void regArtcModule() {
        try {
            WXSDKEngine.registerModuleWithFactory("ArtcEngineModule", (IExternalModuleGetter) new WXIExternalModuleGetter(), false);
        } catch (WXException e) {
            Log.e(TAG, WXLogUtils.getStackTrace(e));
        }
    }

    static class DevToolReceive extends BroadcastReceiver {
        DevToolReceive() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.equals(action, "remoteDebugProxyUrl")) {
                WXEnvironment.sRemoteDebugProxyUrl = Uri.parse(intent.getStringExtra("remoteDebugProxyUrl")).getQueryParameter("_wx_devtool");
                WXSDKEngine.reload(context, true);
                Toast.makeText(context, "已经开启devtool功能!", 0).show();
                Nav.from(context).toUri("https://www.taobao.com");
            } else if (TextUtils.equals(action, "unRegisterDevTool")) {
                context.unregisterReceiver(this);
            }
        }
    }
}
