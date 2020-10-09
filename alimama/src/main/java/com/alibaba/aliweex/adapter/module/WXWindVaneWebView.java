package com.alibaba.aliweex.adapter.module;

import android.content.Context;
import android.taobao.windvane.webview.IWVWebView;
import android.view.View;
import android.webkit.ValueCallback;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.http.WXHttpUtil;
import java.util.HashMap;

public class WXWindVaneWebView implements IWVWebView {
    private static final String TAG = "WXWindVaneWebView";
    private String mDataOnActive = null;
    private WXSDKInstance mWXSDKInstance;

    public boolean _post(Runnable runnable) {
        return false;
    }

    public void addJsObject(String str, Object obj) {
    }

    public boolean back() {
        return false;
    }

    public void clearCache() {
    }

    public void evaluateJavascript(String str) {
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
    }

    public Object getJsObject(String str) {
        return null;
    }

    public void hideLoadingView() {
    }

    public void loadUrl(String str) {
    }

    public void refresh() {
    }

    public void setUserAgentString(String str) {
    }

    public void showLoadingView() {
    }

    public WXWindVaneWebView(WXSDKInstance wXSDKInstance) {
        this.mWXSDKInstance = wXSDKInstance;
    }

    public String getUrl() {
        return this.mWXSDKInstance != null ? this.mWXSDKInstance.getBundleUrl() : TAG;
    }

    public void fireEvent(String str, String str2) {
        HashMap hashMap = new HashMap();
        JSONObject jSONObject = (JSONObject) JSONObject.parse(str2);
        for (String next : jSONObject.keySet()) {
            hashMap.put(next, jSONObject.getString(next));
        }
        this.mWXSDKInstance.fireGlobalEventCallback(str, hashMap);
    }

    public String getDataOnActive() {
        return this.mDataOnActive;
    }

    public void setDataOnActive(String str) {
        this.mDataOnActive = str;
    }

    public View getView() {
        return this.mWXSDKInstance.getContainerView();
    }

    public Context getContext() {
        return this.mWXSDKInstance == null ? WXEnvironment.getApplication() : this.mWXSDKInstance.getContext();
    }

    public String getUserAgentString() {
        return WXHttpUtil.assembleUserAgent(this.mWXSDKInstance.getContext(), WXEnvironment.getConfig());
    }

    public void destroy() {
        this.mWXSDKInstance = null;
    }
}
