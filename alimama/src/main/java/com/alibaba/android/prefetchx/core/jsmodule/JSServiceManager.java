package com.alibaba.android.prefetchx.core.jsmodule;

import com.alibaba.android.prefetchx.PFLog;
import com.taobao.weex.WXSDKEngine;
import java.util.HashMap;

public class JSServiceManager {
    private static volatile JSServiceManager instance;

    private JSServiceManager() {
    }

    public static JSServiceManager getInstance() {
        if (instance == null) {
            synchronized (JSServiceManager.class) {
                if (instance == null) {
                    instance = new JSServiceManager();
                }
            }
        }
        return instance;
    }

    public boolean registerJSService(JSModulePojo jSModulePojo) {
        String str = jSModulePojo.version;
        String str2 = jSModulePojo.jsModule;
        String key = jSModulePojo.getKey();
        HashMap hashMap = new HashMap();
        hashMap.put("version", str);
        boolean registerService = WXSDKEngine.registerService(key, str2, hashMap);
        if (!registerService) {
            PFLog.JSModule.w("registerJSService failed, " + key, new Throwable[0]);
        }
        return registerService;
    }

    public boolean unRegisterJSService(JSModulePojo jSModulePojo) {
        boolean unRegisterService = WXSDKEngine.unRegisterService(jSModulePojo.getKey());
        if (!unRegisterService) {
            PFLog.JSModule.w("unRegisterJSService failed, " + jSModulePojo.getKey(), new Throwable[0]);
        }
        return unRegisterService;
    }
}
