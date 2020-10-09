package com.taobao.windvane.plugins;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.taobao.windvane.jsbridge.IJsBridgeService;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.text.TextUtils;
import androidx.annotation.Nullable;

public class DynamicJsBridge extends Service implements IJsBridgeService {
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Class<? extends WVApiPlugin> getBridgeClass(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.equals("GCanvas")) {
            return GCanvasPlugin.class;
        }
        if (str.equals("GAudio")) {
            return GCanvasAudioPlugin.class;
        }
        if (str.equals("GUtil")) {
            return GUtilPlugin.class;
        }
        return null;
    }
}
