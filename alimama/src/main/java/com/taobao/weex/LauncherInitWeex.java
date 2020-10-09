package com.taobao.weex;

import android.app.Application;
import android.util.Log;
import com.taobao.weex.common.WXConfig;
import java.io.Serializable;
import java.util.HashMap;

public class LauncherInitWeex implements Serializable {
    public void init(Application application, HashMap<String, Object> hashMap) {
        long currentTimeMillis = System.currentTimeMillis();
        if (hashMap != null) {
            Object obj = hashMap.get("isDebuggable");
            if (obj instanceof Boolean) {
                WXEnvironment.addCustomOptions(WXConfig.debugMode, String.valueOf(obj));
            }
        }
        TBWXSDKEngine.initSDKEngine(true);
        Log.e("weex", "init weex cost " + (System.currentTimeMillis() - currentTimeMillis));
    }
}
