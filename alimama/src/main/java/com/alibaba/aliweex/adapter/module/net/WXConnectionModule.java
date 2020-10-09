package com.alibaba.aliweex.adapter.module.net;

import androidx.annotation.VisibleForTesting;
import com.alibaba.aliweex.adapter.module.net.IWXConnection;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.utils.WXLogUtils;
import java.util.Map;

public class WXConnectionModule extends WXSDKEngine.DestroyableModule {
    private static final String EVENT_CONNECTION_CHANGE = "change";
    private static final String TAG = "WXConnectionModule";
    private IWXConnection mWXConnectionImpl;

    private boolean createWXConnectionImpl() {
        if (this.mWXConnectionImpl != null) {
            return true;
        }
        if (this.mWXSDKInstance == null || this.mWXSDKInstance.getContext() == null) {
            return false;
        }
        this.mWXConnectionImpl = WXConnectionFactory.createDefault(this.mWXSDKInstance.getContext());
        if (this.mWXConnectionImpl != null) {
            return true;
        }
        return false;
    }

    @JSMethod
    public void addEventListener(String str, String str2, Map<String, Object> map) {
        super.addEventListener(str, str2, map);
        if (createWXConnectionImpl()) {
            this.mWXConnectionImpl.addNetworkChangeListener(new IWXConnection.OnNetworkChangeListener() {
                public void onNetworkChange() {
                    if (WXConnectionModule.this.mWXSDKInstance != null) {
                        if (WXConnectionModule.this.mWXSDKInstance.checkModuleEventRegistered("change", WXConnectionModule.this)) {
                            WXConnectionModule.this.mWXSDKInstance.fireModuleEvent("change", WXConnectionModule.this, (Map<String, Object>) null);
                            WXLogUtils.d(WXConnectionModule.TAG, "send connection change event success.");
                            return;
                        }
                        WXLogUtils.d(WXConnectionModule.TAG, "no listener found. drop the connection change event.");
                    }
                }
            });
        } else if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.e(TAG, "addListener failed because of context or instance been destroyed.");
        }
    }

    @JSMethod(uiThread = false)
    public String getType() {
        if (createWXConnectionImpl()) {
            return this.mWXConnectionImpl.getType();
        }
        if (!WXEnvironment.isApkDebugable()) {
            return "none";
        }
        WXLogUtils.e(TAG, "getType failed because of context or instance been destroyed.");
        return "none";
    }

    @JSMethod(uiThread = false)
    public String getNetworkType() {
        if (createWXConnectionImpl()) {
            return this.mWXConnectionImpl.getNetworkType();
        }
        if (!WXEnvironment.isApkDebugable()) {
            return "unknown";
        }
        WXLogUtils.e(TAG, "getNetworkType failed because of context or instance been destroyed.");
        return "unknown";
    }

    @JSMethod(uiThread = false)
    public double getDownlinkMax() {
        if (createWXConnectionImpl()) {
            return this.mWXConnectionImpl.getDownlinkMax();
        }
        if (!WXEnvironment.isApkDebugable()) {
            return 0.0d;
        }
        WXLogUtils.e(TAG, "getDownlinkMax failed because of context or instance been destroyed.");
        return 0.0d;
    }

    public void destroy() {
        if (this.mWXConnectionImpl != null) {
            this.mWXConnectionImpl.destroy();
            this.mWXConnectionImpl = null;
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setConnectionImpl(IWXConnection iWXConnection) {
        this.mWXConnectionImpl = iWXConnection;
    }
}
