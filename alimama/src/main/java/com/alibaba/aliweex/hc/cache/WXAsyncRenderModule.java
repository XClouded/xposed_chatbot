package com.alibaba.aliweex.hc.cache;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;
import java.util.Map;

public class WXAsyncRenderModule extends WXModule {
    private HashMap<String, JSONObject> mAsyncData = new HashMap<>();
    private HashMap<String, String> mCallbackQueue = new HashMap<>();
    private boolean mFailFlag = false;

    @JSMethod(uiThread = false)
    public boolean isExist() {
        return true;
    }

    @JSMethod
    public void addEventListener(String str, String str2, Map<String, Object> map) {
        super.addEventListener(str, str2, map);
        if (this.mWXSDKInstance != null && !TextUtils.isEmpty(this.mWXSDKInstance.getInstanceId())) {
            this.mCallbackQueue.put(this.mWXSDKInstance.getInstanceId(), str2);
            if (!fireFirstScreenAsyncData(this.mWXSDKInstance.getInstanceId())) {
                fireFailEvent(this.mWXSDKInstance.getInstanceId());
            }
        }
    }

    @JSMethod
    public void addMsg(String str) {
        WeexCacheMsgPanel.d(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void logPerf(java.lang.String r5, java.lang.String r6) {
        /*
            r4 = this;
            int r0 = r5.hashCode()
            r1 = -1518823526(0xffffffffa578979a, float:-2.1561936E-16)
            if (r0 == r1) goto L_0x0037
            r1 = -934592106(0xffffffffc84b4196, float:-208134.34)
            if (r0 == r1) goto L_0x002d
            r1 = 100571(0x188db, float:1.4093E-40)
            if (r0 == r1) goto L_0x0023
            r1 = 1879906466(0x700d18a2, float:1.7466866E29)
            if (r0 == r1) goto L_0x0019
            goto L_0x0041
        L_0x0019:
            java.lang.String r0 = "module_execute"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = 0
            goto L_0x0042
        L_0x0023:
            java.lang.String r0 = "end"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = 3
            goto L_0x0042
        L_0x002d:
            java.lang.String r0 = "render"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = 2
            goto L_0x0042
        L_0x0037:
            java.lang.String r0 = "data_manage"
            boolean r5 = r5.equals(r0)
            if (r5 == 0) goto L_0x0041
            r5 = 1
            goto L_0x0042
        L_0x0041:
            r5 = -1
        L_0x0042:
            switch(r5) {
                case 0: goto L_0x009f;
                case 1: goto L_0x0094;
                case 2: goto L_0x0089;
                case 3: goto L_0x0046;
                default: goto L_0x0045;
            }
        L_0x0045:
            goto L_0x00a9
        L_0x0046:
            com.alibaba.aliweex.hc.cache.CachePerf r5 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = java.lang.System.currentTimeMillis()
            com.alibaba.aliweex.hc.cache.CachePerf r6 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r2 = r6.mFSStartTime
            long r0 = r0 - r2
            r5.mFSAllTime = r0
            com.taobao.weex.WXSDKInstance r5 = r4.mWXSDKInstance
            if (r5 == 0) goto L_0x0081
            com.taobao.weex.WXSDKInstance r5 = r4.mWXSDKInstance
            com.taobao.weex.common.WXPerformance r5 = r5.getWXPerformance()
            if (r5 == 0) goto L_0x0081
            com.taobao.weex.WXSDKInstance r5 = r4.mWXSDKInstance
            com.taobao.weex.common.WXPerformance r5 = r5.getWXPerformance()
            com.alibaba.aliweex.hc.cache.CachePerf r6 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = r5.networkTime
            r6.mFSBundleNetworkTime = r0
            com.alibaba.aliweex.hc.cache.CachePerf r6 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = r5.screenRenderTime
            r6.mFSBundleRenderTime = r0
            com.alibaba.aliweex.hc.cache.CachePerf r6 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = r5.firstScreenJSFExecuteTime
            r6.mFSBundleExecTime = r0
        L_0x0081:
            com.alibaba.aliweex.hc.cache.CachePerf r5 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            r5.commitStatWeexCache()
            goto L_0x00a9
        L_0x0089:
            com.alibaba.aliweex.hc.cache.CachePerf r5 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = java.lang.Long.parseLong(r6)
            r5.mFSRenderTime = r0
            goto L_0x00a9
        L_0x0094:
            com.alibaba.aliweex.hc.cache.CachePerf r5 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = java.lang.Long.parseLong(r6)
            r5.mFSModuleDataManageTime = r0
            goto L_0x00a9
        L_0x009f:
            com.alibaba.aliweex.hc.cache.CachePerf r5 = com.alibaba.aliweex.hc.cache.CachePerf.getInstance()
            long r0 = java.lang.Long.parseLong(r6)
            r5.mFSModuleExecuteTime = r0
        L_0x00a9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.hc.cache.WXAsyncRenderModule.logPerf(java.lang.String, java.lang.String):void");
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        this.mFailFlag = false;
        this.mAsyncData.clear();
        this.mCallbackQueue.clear();
    }

    /* access modifiers changed from: package-private */
    public void addAsyncData(String str, JSONObject jSONObject) {
        this.mAsyncData.put(str, jSONObject);
        fireFirstScreenAsyncData(str);
    }

    /* access modifiers changed from: package-private */
    public void setFailFlag(String str) {
        this.mFailFlag = true;
        fireFailEvent(str);
    }

    private synchronized void fireFailEvent(String str) {
        if (this.mWXSDKInstance != null) {
            boolean checkModuleEventRegistered = this.mWXSDKInstance.checkModuleEventRegistered("onFirstScreen", this);
            String callback = getCallback(str);
            if (this.mFailFlag && checkModuleEventRegistered && !TextUtils.isEmpty(callback)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("result", (Object) Constants.Event.FAIL);
                this.mWXSDKInstance.fireModuleEvent("onFirstScreen", this, jSONObject);
                removeCallback(str);
            }
        }
    }

    private synchronized boolean fireFirstScreenAsyncData(String str) {
        if (this.mWXSDKInstance != null && !TextUtils.isEmpty(str)) {
            boolean checkModuleEventRegistered = this.mWXSDKInstance.checkModuleEventRegistered("onFirstScreen", this);
            JSONObject asyncData = getAsyncData(str);
            String callback = getCallback(str);
            if (!this.mFailFlag && asyncData != null && checkModuleEventRegistered && !TextUtils.isEmpty(callback)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("result", (Object) "success");
                jSONObject.put("data", (Object) asyncData);
                this.mWXSDKInstance.fireModuleEvent("onFirstScreen", this, jSONObject);
                removeCallback(str);
                return true;
            }
        }
        return false;
    }

    private JSONObject getAsyncData(String str) {
        return this.mAsyncData.get(str);
    }

    private String getCallback(String str) {
        return this.mCallbackQueue.get(str);
    }

    private void removeCallback(String str) {
        this.mCallbackQueue.remove(str);
    }
}
