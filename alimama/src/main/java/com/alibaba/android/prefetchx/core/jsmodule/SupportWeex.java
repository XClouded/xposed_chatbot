package com.alibaba.android.prefetchx.core.jsmodule;

import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFUtil;
import com.alibaba.android.prefetchx.core.jsmodule.JSModulePojo;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class SupportWeex extends WXModule {
    public static final String MODULE_NAME = "PrefetchXJSModule";

    public static void register() {
        try {
            WXSDKEngine.registerModule(MODULE_NAME, SupportWeex.class, true);
        } catch (Exception e) {
            PFLog.Data.w("error in register weex module of data. e.getMessage() is " + e.getMessage(), new Throwable[0]);
        }
    }

    @JSMethod(uiThread = false)
    public void loadJSService(String str, String str2, String str3, final JSCallback jSCallback) {
        if (isForbidden(this.mWXSDKInstance.getBundleUrl())) {
            jSCallback.invoke("invalid url host. only works in debug TB.");
            return;
        }
        String bundleUrl = this.mWXSDKInstance.getBundleUrl();
        JSModulePojo jSModulePojo = new JSModulePojo();
        jSModulePojo.url = bundleUrl;
        jSModulePojo.name = str;
        jSModulePojo.version = str2;
        jSModulePojo.jsModule = str3;
        jSModulePojo.callback = new WeakReference<>(new JSModulePojo.Callback() {
            public void done(Object obj) {
                jSCallback.invoke(obj);
            }
        });
        PFJSModule.getInstance().loadJSService(jSModulePojo);
    }

    @JSMethod(uiThread = false)
    public void loadJSServiceByUrl(String str, String str2, String str3, final JSCallback jSCallback) {
        if (isForbidden(this.mWXSDKInstance.getBundleUrl())) {
            jSCallback.invoke("invalid url host. only works in debug TB.");
            return;
        }
        String bundleUrl = this.mWXSDKInstance.getBundleUrl();
        JSModulePojo jSModulePojo = new JSModulePojo();
        jSModulePojo.url = bundleUrl;
        jSModulePojo.name = str;
        jSModulePojo.version = str2;
        jSModulePojo.jsModuleUrl = str3;
        jSModulePojo.callback = new WeakReference<>(new JSModulePojo.Callback() {
            public void done(Object obj) {
                jSCallback.invoke(obj);
            }
        });
        PFJSModule.getInstance().loadJSServiceByUrl(jSModulePojo);
    }

    @JSMethod(uiThread = false)
    public void unloadJSService(String str, String str2, final JSCallback jSCallback) {
        if (isForbidden(this.mWXSDKInstance.getBundleUrl())) {
            jSCallback.invoke("invalid url host. only works in debug TB.");
            return;
        }
        String bundleUrl = this.mWXSDKInstance.getBundleUrl();
        JSModulePojo jSModulePojo = new JSModulePojo();
        jSModulePojo.url = bundleUrl;
        jSModulePojo.name = str;
        jSModulePojo.version = str2;
        jSModulePojo.callback = new WeakReference<>(new JSModulePojo.Callback() {
            public void done(Object obj) {
                jSCallback.invoke(obj);
            }
        });
        PFJSModule.getInstance().unloadJSService(jSModulePojo);
    }

    @JSMethod
    public void refresh(JSCallback jSCallback) {
        if (isForbidden(this.mWXSDKInstance.getBundleUrl())) {
            jSCallback.invoke("invalid url host. only works in debug TB.");
        } else {
            PFJSModule.getInstance().refresh();
        }
    }

    @JSMethod
    public void removeJSFile(JSCallback jSCallback) {
        if (isForbidden(this.mWXSDKInstance.getBundleUrl())) {
            jSCallback.invoke("invalid url host. only works in debug TB.");
        } else {
            PFJSModule.getInstance().removeAllJSModuleCache();
        }
    }

    private boolean isForbidden(String str) {
        return !PFUtil.isDebug();
    }

    @JSMethod
    public void isReady(JSCallback jSCallback) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("isReady", Boolean.valueOf(PFJSModule.getInstance().isReady()));
        hashMap.put("todoKeys", PFJSModule.getInstance().todoKeys);
        hashMap.put("loadedKeys", PFJSModule.getInstance().loadedKeys);
        hashMap.put("loadedKeysSize", Integer.valueOf(PFJSModule.getInstance().loadedKeys.size()));
        jSCallback.invoke(hashMap);
    }

    @JSMethod
    public void getStatistics(JSCallback jSCallback) {
        HashMap hashMap = new HashMap(4);
        hashMap.put("allJSModule", JSServiceSizeManager.getInstance().getAllJSModules());
        hashMap.put("sizeByHostPath", JSServiceSizeManager.getInstance().sizeByHostPath);
        hashMap.put("sizeByJSModule", JSServiceSizeManager.getInstance().sizeByJSModule);
        hashMap.put("sizeEachByHostPath", JSServiceSizeManager.getInstance().sizeEachByHostPath);
        jSCallback.invoke(hashMap);
    }

    @JSMethod
    public void getAllJSModule(JSCallback jSCallback) {
        jSCallback.invoke(PFJSModule.getInstance().jsServiceSizeManager.getAllJSModules());
    }
}
