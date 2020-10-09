package com.taobao.weex.analyzer.core.debug;

import androidx.annotation.NonNull;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Method;

public class DebugTool {
    private static final String TAG = "DebugTool";

    private DebugTool() {
    }

    public static void startRemoteDebug(@NonNull String str) {
        try {
            WXEnvironment.sRemoteDebugProxyUrl = str;
            WXEnvironment.sRemoteDebugMode = true;
            WXSDKEngine.reload();
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
        }
    }

    public static boolean stopRemoteDebug() {
        try {
            WXBridgeManager instance = WXBridgeManager.getInstance();
            Method declaredMethod = instance.getClass().getDeclaredMethod("stopRemoteDebug", new Class[0]);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(instance, new Object[0]);
            return true;
        } catch (Exception e) {
            WXLogUtils.e(TAG, e.getMessage());
            return false;
        }
    }
}
