package com.alibaba.android.umbrella.weex;

import android.content.Context;
import com.taobao.tao.log.TLog;
import com.taobao.weex.WXSDKEngine;

public final class UmbrellaWeexLauncher {
    private static final String KEY_MODULE_UMBRELLA_WEEX = "umbrella-weex";

    public static void launchUmbrella(Context context) {
        try {
            WXSDKEngine.registerModule(KEY_MODULE_UMBRELLA_WEEX, UmbrellaWeexModule.class);
        } catch (Throwable th) {
            TLog.loge("UmbrellaWeexLauncher", "launchUmbrella", th);
        }
    }
}
