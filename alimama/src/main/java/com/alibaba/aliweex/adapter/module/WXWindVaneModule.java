package com.alibaba.aliweex.adapter.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.jsbridge.WVCallMethodContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVPluginEntryManager;
import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.service.WVEventService;
import android.text.TextUtils;
import com.alibaba.aliweex.interceptor.mtop.MtopTracker;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.mtop.wvplugin.MtopWVPlugin;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import java.util.Map;

public class WXWindVaneModule extends WXModule implements Destroyable {
    private static Map<String, MtopTracker> sMtopRequests = new HashMap();
    private WVPluginEntryManager mEntryManager = null;
    private WXWVEventListener mEventListener = new WXWVEventListener();
    private WXWindVaneWebView mIWVWebView = null;

    public WXWindVaneModule() {
        WVEventService.getInstance().addEventListener(this.mEventListener);
    }

    @JSMethod
    public void call(String str, String str2) {
        boolean z;
        if (this.mWXSDKInstance != null && this.mWXSDKInstance.getContext() != null && !TextUtils.isEmpty(str)) {
            if (this.mEntryManager == null) {
                this.mIWVWebView = new WXWindVaneWebView(this.mWXSDKInstance);
                this.mEntryManager = new WVPluginEntryManager(this.mWXSDKInstance.getContext(), this.mIWVWebView);
            }
            if (this.mEventListener != null) {
                this.mEventListener.setWXSDKInstance(this.mWXSDKInstance);
            }
            WVCallMethodContext wVCallMethodContext = new WVCallMethodContext();
            JSONObject parseObject = JSON.parseObject(str);
            filterMtopRequest(parseObject, str2);
            if (parseObject != null) {
                wVCallMethodContext.webview = this.mIWVWebView;
                wVCallMethodContext.objectName = parseObject.getString("class");
                wVCallMethodContext.methodName = parseObject.getString("method");
                wVCallMethodContext.params = parseObject.getString("data");
                if (MtopWVPlugin.API_SERVER_NAME.equalsIgnoreCase(wVCallMethodContext.objectName) && "send".equalsIgnoreCase(wVCallMethodContext.methodName)) {
                    WXStateRecord instance = WXStateRecord.getInstance();
                    String instanceId = this.mWXSDKInstance.getInstanceId();
                    instance.recordAction(instanceId, "windvineMtopCall forCallBack:" + str2);
                    z = true;
                    WVJsBridge.getInstance().exCallMethod(this.mEntryManager, wVCallMethodContext, new WXWindVaneCallBack(this.mWXSDKInstance.getInstanceId(), str2, false, z), new WXWindVaneCallBack(this.mWXSDKInstance.getInstanceId(), str2, false, z));
                }
            }
            z = false;
            WVJsBridge.getInstance().exCallMethod(this.mEntryManager, wVCallMethodContext, new WXWindVaneCallBack(this.mWXSDKInstance.getInstanceId(), str2, false, z), new WXWindVaneCallBack(this.mWXSDKInstance.getInstanceId(), str2, false, z));
        }
    }

    @JSMethod
    public void call2(String str, String str2, String str3, String str4) {
        if (this.mWXSDKInstance != null && this.mWXSDKInstance.getContext() != null && !TextUtils.isEmpty(str2)) {
            if (this.mEntryManager == null) {
                this.mIWVWebView = new WXWindVaneWebView(this.mWXSDKInstance);
                this.mEntryManager = new WVPluginEntryManager(this.mWXSDKInstance.getContext(), this.mIWVWebView);
            }
            if (this.mEventListener != null) {
                this.mEventListener.setWXSDKInstance(this.mWXSDKInstance);
            }
            WVCallMethodContext wVCallMethodContext = new WVCallMethodContext();
            try {
                filterMtopRequest(JSON.parseObject(str2), str3);
                if (TextUtils.isEmpty(str)) {
                    WXSDKManager.getInstance().callback(this.mWXSDKInstance.getInstanceId(), str4, (Map<String, Object>) null);
                } else if (str.indexOf(".") == -1) {
                    WXSDKManager.getInstance().callback(this.mWXSDKInstance.getInstanceId(), str4, (Map<String, Object>) null);
                } else {
                    wVCallMethodContext.webview = this.mIWVWebView;
                    wVCallMethodContext.objectName = str.substring(0, str.indexOf("."));
                    wVCallMethodContext.methodName = str.substring(str.indexOf(".") + 1);
                    wVCallMethodContext.params = str2;
                    WVJsBridge.getInstance().exCallMethod(this.mEntryManager, wVCallMethodContext, new WXWindVaneCallBack(this.mWXSDKInstance.getInstanceId(), str4, true, false), new WXWindVaneCallBack(this.mWXSDKInstance.getInstanceId(), str3, true, false));
                }
            } catch (Throwable th) {
                WXLogUtils.w("Invalid param", th);
                WXSDKManager.getInstance().callback(this.mWXSDKInstance.getInstanceId(), str4, (Map<String, Object>) null);
            }
        }
    }

    public void destroy() {
        if (this.mEventListener != null) {
            this.mEventListener.destroy();
            WVEventService.getInstance().removeEventListener(this.mEventListener);
        }
        if (this.mIWVWebView != null) {
            this.mIWVWebView.destroy();
        }
        if (this.mEntryManager != null) {
            this.mEntryManager.onDestroy();
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mEntryManager != null) {
            this.mEntryManager.onActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    private void filterMtopRequest(JSONObject jSONObject, String str) {
        if (WXEnvironment.isApkDebugable() && jSONObject != null && MtopWVPlugin.API_SERVER_NAME.equals(jSONObject.getString("class"))) {
            MtopTracker newInstance = MtopTracker.newInstance();
            sMtopRequests.put(str, newInstance);
            newInstance.preRequest(jSONObject.getJSONObject("data"));
        }
    }

    static MtopTracker popMtopTracker(String str) {
        return sMtopRequests.remove(str);
    }

    static class ActivityResultReceive extends BroadcastReceiver {
        private WVPluginEntryManager mWVPluginEntryManager;

        ActivityResultReceive() {
        }

        public void onReceive(Context context, Intent intent) {
            if (this.mWVPluginEntryManager != null) {
                this.mWVPluginEntryManager.onActivityResult(intent.getIntExtra("requestCode", -1), intent.getIntExtra("resultCode", -1), intent);
            }
        }

        public void setWVPluginEntryManager(WVPluginEntryManager wVPluginEntryManager) {
            this.mWVPluginEntryManager = wVPluginEntryManager;
        }

        public void destroy() {
            this.mWVPluginEntryManager = null;
        }
    }

    static class WXWVEventListener implements WVEventListener {
        private WXSDKInstance mWXSDKInstance;

        WXWVEventListener() {
        }

        public WVEventResult onEvent(int i, WVEventContext wVEventContext, Object... objArr) {
            if (i != 3013 || objArr == null) {
                return null;
            }
            try {
                if (this.mWXSDKInstance == null) {
                    return null;
                }
                HashMap hashMap = new HashMap();
                if (objArr.length >= 3) {
                    JSONObject parseObject = JSON.parseObject(objArr[2].toString());
                    for (String next : parseObject.keySet()) {
                        hashMap.put(next, parseObject.get(next));
                    }
                }
                this.mWXSDKInstance.fireGlobalEventCallback(objArr[1] == null ? "" : objArr[1].toString(), hashMap);
                return null;
            } catch (Exception unused) {
                return null;
            }
        }

        public void setWXSDKInstance(WXSDKInstance wXSDKInstance) {
            this.mWXSDKInstance = wXSDKInstance;
        }

        public void destroy() {
            this.mWXSDKInstance = null;
        }
    }
}
