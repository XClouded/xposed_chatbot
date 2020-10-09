package com.taobao.android.eagle;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXEaglePlugin;
import com.taobao.weex.bridge.WXJSObject;
import com.taobao.weex.ui.IFComponentHolder;
import java.util.Map;

public class EagleVuePlugin implements WXEaglePlugin {
    private static final String LOG_TAG = "EagleVuePlugin";

    public static native void nativeFireEvent(String str, String str2, String str3, String str4, String str5);

    public boolean callEagleTaskFromWeex(String str, JSONObject jSONObject) {
        return false;
    }

    public String getPluginName() {
        return "EagleVue";
    }

    public String getSoLibName() {
        return EagleLauncher.soName;
    }

    public void invokeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
    }

    public void invokeJSCallback(String str, String str2, String str3, boolean z) {
    }

    public boolean isLazyCompAndModuleSupport() {
        return false;
    }

    public boolean isSkipFrameworkInit(String str) {
        return false;
    }

    public int isSupportFireEvent(String str) {
        return 1;
    }

    public int isSupportInvokeExecJS(String str) {
        return 2;
    }

    public int isSupportJSCallback(String str) {
        return 2;
    }

    public String isSupportedUrl(String str) {
        return null;
    }

    public void registerComponent(String str, IFComponentHolder iFComponentHolder, Map<String, Object> map) {
    }

    public void registerModules(String str, ModuleFactory moduleFactory, boolean z) {
    }

    public void fireEvent(String str, String str2, String str3, String str4, String str5) {
        try {
            nativeFireEvent(str, str2, str3, str4, str5);
        } catch (Throwable th) {
            Log.e(LOG_TAG, WXBridgeManager.METHOD_FIRE_EVENT, th);
        }
    }
}
