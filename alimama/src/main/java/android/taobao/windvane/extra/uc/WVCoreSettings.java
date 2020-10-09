package android.taobao.windvane.extra.uc;

import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.webview.CoreEventCallback;

import java.util.ArrayList;
import java.util.List;

public class WVCoreSettings {
    public static final int DOWNLOAD = 1;
    public static final int INNER = 0;
    public static final int U420 = 2;
    public static final int U430 = 3;
    private static WVCoreSettings instance;
    public List<CoreEventCallback> coreEventCallbacks;

    public static WVCoreSettings getInstance() {
        if (instance == null) {
            synchronized (WVCoreSettings.class) {
                if (instance == null) {
                    instance = new WVCoreSettings();
                }
            }
        }
        return instance;
    }

    public static void setCorePolicy(int i) {
        WVCommonConfig.commonConfig.initUCCorePolicy = i;
    }

    public static void setDownloadCore(int i) {
        WVCommonConfig.commonConfig.downloadCoreType = i;
    }

    public static void setInputSupportedDomains(String str) {
        WVCommonConfig.commonConfig.ucParam.u4FocusAutoPopupInputHostList = str;
    }

    public static void setWebMultiPolicy(int i) {
        WVCommonConfig.commonConfig.webMultiPolicy = i;
    }

    public static void setGpuMultiPolicy(int i) {
        WVCommonConfig.commonConfig.gpuMultiPolicy = i;
    }

    public void setCoreEventCallback(CoreEventCallback coreEventCallback) {
        if (this.coreEventCallbacks == null) {
            this.coreEventCallbacks = new ArrayList();
        }
        if (!this.coreEventCallbacks.contains(coreEventCallback)) {
            this.coreEventCallbacks.add(coreEventCallback);
        }
        try {
            Class.forName("android.taobao.windvane.extra.uc.WVUCWebView");
        } catch (Throwable unused) {
        }
    }
}
