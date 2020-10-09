package com.taobao.weex.bridge;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.base.CalledByNative;
import com.taobao.weex.common.IWXBridge;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WXBridge implements IWXBridge {
    public static final boolean MULTIPROCESS = true;
    public static final String TAG = "WXBridge";

    private native void nativeBindMeasurementToRenderObject(long j);

    private native int nativeExecJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    private native int nativeExecJSService(String str);

    private native void nativeForceLayout(String str);

    private native int nativeInitFramework(String str, WXParams wXParams);

    private native int nativeInitFrameworkEnv(String str, WXParams wXParams, String str2, boolean z);

    private native void nativeMarkDirty(String str, String str2, boolean z);

    private native boolean nativeNotifyLayout(String str);

    private native void nativeOnInstanceClose(String str);

    private native void nativeRefreshInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    private native void nativeRegisterCoreEnv(String str, String str2);

    private native void nativeReloadPageLayout(String str);

    private native void nativeRemoveInstanceRenderType(String str);

    private native void nativeResetWXBridge(Object obj, String str);

    private native void nativeSetDefaultHeightAndWidthIntoRootDom(String str, float f, float f2, boolean z, boolean z2);

    private native void nativeSetDeviceDisplay(String str, float f, float f2, float f3);

    private native void nativeSetDeviceDisplayOfPage(String str, float f, float f2);

    private native void nativeSetInstanceRenderType(String str, String str2);

    private native void nativeSetLogType(float f, float f2);

    private native void nativeSetMargin(String str, String str2, int i, float f);

    private native void nativeSetPadding(String str, String str2, int i, float f);

    private native void nativeSetPageArgument(String str, String str2, String str3);

    private native void nativeSetPosition(String str, String str2, int i, float f);

    private native void nativeSetRenderContainerWrapContent(boolean z, String str);

    private native void nativeSetStyleHeight(String str, String str2, float f);

    private native void nativeSetStyleWidth(String str, String str2, float f);

    private native void nativeSetViewPortWidth(String str, float f);

    private native void nativeTakeHeapSnapshot(String str);

    private native void nativeUpdateInitFrameworkParams(String str, String str2, String str3);

    public native int nativeCreateInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    public native int nativeDestoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    public native String nativeDumpIpcPageQueueInfo();

    public native String nativeExecJSOnInstance(String str, String str2, int i);

    public native void nativeExecJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, long j);

    public native byte[] nativeExecJSWithResult(String str, String str2, String str3, WXJSObject[] wXJSObjectArr);

    public native long[] nativeGetFirstScreenRenderTime(String str);

    public native long[] nativeGetRenderFinishTime(String str);

    public native void nativeOnInteractionTimeUpdate(String str);

    public native void nativeUpdateGlobalConfig(String str);

    public void updateInitFrameworkParams(String str, String str2, String str3) {
        WXStateRecord.getInstance().recordAction("", "updateInitFrameworkParams:");
        nativeUpdateInitFrameworkParams(str, str2, str3);
    }

    public void setLogType(float f, boolean z) {
        Log.e("WeexCore", "setLog" + WXEnvironment.sLogLevel.getValue() + "isPerf : " + z);
        nativeSetLogType(f, z ? 1.0f : 0.0f);
    }

    public int initFramework(String str, WXParams wXParams) {
        return nativeInitFramework(str, wXParams);
    }

    public int initFrameworkEnv(String str, WXParams wXParams, String str2, boolean z) {
        WXStateRecord.getInstance().recordAction("", "nativeInitFrameworkEnv:");
        return nativeInitFrameworkEnv(str, wXParams, str2, z);
    }

    public void refreshInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "refreshInstance:" + str2 + "," + str3);
        nativeRefreshInstance(str, str2, str3, wXJSObjectArr);
    }

    public int execJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "execJS:" + str2 + "," + str3);
        return nativeExecJS(str, str2, str3, wXJSObjectArr);
    }

    public void execJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, ResultCallback resultCallback) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "execJSWithCallback:" + str2 + "," + str3);
        if (resultCallback == null) {
            execJS(str, str2, str3, wXJSObjectArr);
        }
        nativeExecJSWithCallback(str, str2, str3, wXJSObjectArr, ResultCallbackManager.generateCallbackId(resultCallback));
    }

    @CalledByNative
    public void onNativePerformanceDataUpdate(String str, Map<String, String> map) {
        WXSDKInstance wXSDKInstance;
        if (!TextUtils.isEmpty(str) && map != null && map.size() >= 1 && (wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(str)) != null && wXSDKInstance.getApmForInstance() != null) {
            wXSDKInstance.getApmForInstance().updateNativePerformanceData(map);
        }
    }

    @CalledByNative
    public void onReceivedResult(long j, byte[] bArr) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction("onReceivedResult", "callbackId" + j);
        ResultCallback removeCallbackById = ResultCallbackManager.removeCallbackById(j);
        if (removeCallbackById != null) {
            removeCallbackById.onReceiveResult(bArr);
        }
    }

    public int execJSService(String str) {
        WXStateRecord.getInstance().recordAction("execJSService", "execJSService:");
        return nativeExecJSService(str);
    }

    public void takeHeapSnapshot(String str) {
        nativeTakeHeapSnapshot(str);
    }

    public int createInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        Log.e(TimeCalculator.TIMELINE_TAG, "createInstance :" + System.currentTimeMillis());
        WXStateRecord.getInstance().recordAction(str, "createInstanceContext:");
        return nativeCreateInstanceContext(str, str2, str3, wXJSObjectArr);
    }

    public int destoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXStateRecord.getInstance().recordAction(str, "destoryInstance:");
        return nativeDestoryInstance(str, str2, str3, wXJSObjectArr);
    }

    public String execJSOnInstance(String str, String str2, int i) {
        WXStateRecord instance = WXStateRecord.getInstance();
        instance.recordAction(str, "execJSOnInstance:" + i);
        return nativeExecJSOnInstance(str, str2, i);
    }

    @CalledByNative
    public int callNative(String str, byte[] bArr, String str2) {
        if (!"HeartBeat".equals(str2)) {
            return callNative(str, JSON.parseArray(new String(bArr)), str2);
        }
        Log.e("HeartBeat instanceId", str);
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance == null) {
            return 1;
        }
        sDKInstance.createInstanceFuncHeartBeat();
        return 1;
    }

    public int callNative(String str, String str2, String str3) {
        try {
            return callNative(str, JSONArray.parseArray(str2), str3);
        } catch (Exception e) {
            WXLogUtils.e(TAG, "callNative throw exception: " + WXLogUtils.getStackTrace(e));
            return 1;
        }
    }

    private int callNative(String str, JSONArray jSONArray, String str2) {
        int i;
        long currentTimeMillis = System.currentTimeMillis();
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            sDKInstance.firstScreenCreateInstanceTime(currentTimeMillis);
        }
        try {
            i = WXBridgeManager.getInstance().callNative(str, jSONArray, str2);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callNative throw exception:" + WXLogUtils.getStackTrace(th));
            i = 1;
        }
        if (WXEnvironment.isApkDebugable() && i == -1) {
            WXLogUtils.w("destroyInstance :" + str + " JSF must stop callNative");
        }
        return i;
    }

    @CalledByNative
    public void reportJSException(String str, String str2, String str3) {
        WXBridgeManager.getInstance().reportJSException(str, str2, str3);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r14 = (com.alibaba.fastjson.JSONArray) com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x010d, code lost:
        return com.taobao.weex.utils.WXWsonJSONSwitch.toWsonOrJsonWXJSObject(r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0113, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0114, code lost:
        com.taobao.weex.utils.WXLogUtils.e(TAG, (java.lang.Throwable) r11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x011e, code lost:
        return new com.taobao.weex.bridge.WXJSObject((java.lang.Object) null);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0050 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0109 */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00bd A[Catch:{ Exception -> 0x0113 }] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ea  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x010e A[Catch:{ Exception -> 0x0113 }] */
    @com.taobao.weex.base.CalledByNative
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object callNativeModule(java.lang.String r11, java.lang.String r12, java.lang.String r13, byte[] r14, byte[] r15) {
        /*
            r10 = this;
            r0 = 0
            com.taobao.weex.performance.WXStateRecord r1 = com.taobao.weex.performance.WXStateRecord.getInstance()     // Catch:{ Exception -> 0x0113 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0113 }
            r2.<init>()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = "callNativeModule:"
            r2.append(r3)     // Catch:{ Exception -> 0x0113 }
            r2.append(r12)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r3 = "."
            r2.append(r3)     // Catch:{ Exception -> 0x0113 }
            r2.append(r13)     // Catch:{ Exception -> 0x0113 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0113 }
            r1.recordAction(r11, r2)     // Catch:{ Exception -> 0x0113 }
            long r1 = com.taobao.weex.utils.WXUtils.getFixUnixTime()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.WXSDKManager r3 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.WXSDKInstance r3 = r3.getSDKInstance(r11)     // Catch:{ Exception -> 0x0113 }
            if (r14 == 0) goto L_0x005f
            if (r3 == 0) goto L_0x0057
            com.taobao.weex.common.WXRenderStrategy r4 = r3.getRenderStrategy()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.common.WXRenderStrategy r5 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Exception -> 0x0113 }
            if (r4 == r5) goto L_0x0041
            com.taobao.weex.common.WXRenderStrategy r4 = r3.getRenderStrategy()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.common.WXRenderStrategy r5 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Exception -> 0x0113 }
            if (r4 != r5) goto L_0x0057
        L_0x0041:
            java.lang.String r4 = new java.lang.String     // Catch:{ Exception -> 0x0050 }
            java.lang.String r5 = "UTF-8"
            r4.<init>(r14, r5)     // Catch:{ Exception -> 0x0050 }
            java.lang.Object r4 = com.alibaba.fastjson.JSON.parse(r4)     // Catch:{ Exception -> 0x0050 }
            com.alibaba.fastjson.JSONArray r4 = (com.alibaba.fastjson.JSONArray) r4     // Catch:{ Exception -> 0x0050 }
            r8 = r4
            goto L_0x0060
        L_0x0050:
            java.lang.Object r14 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r14)     // Catch:{ Exception -> 0x0113 }
            com.alibaba.fastjson.JSONArray r14 = (com.alibaba.fastjson.JSONArray) r14     // Catch:{ Exception -> 0x0113 }
            goto L_0x005d
        L_0x0057:
            java.lang.Object r14 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r14)     // Catch:{ Exception -> 0x0113 }
            com.alibaba.fastjson.JSONArray r14 = (com.alibaba.fastjson.JSONArray) r14     // Catch:{ Exception -> 0x0113 }
        L_0x005d:
            r8 = r14
            goto L_0x0060
        L_0x005f:
            r8 = r0
        L_0x0060:
            if (r15 == 0) goto L_0x006a
            java.lang.Object r14 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r15)     // Catch:{ Exception -> 0x0113 }
            com.alibaba.fastjson.JSONObject r14 = (com.alibaba.fastjson.JSONObject) r14     // Catch:{ Exception -> 0x0113 }
        L_0x0068:
            r9 = r14
            goto L_0x00b0
        L_0x006a:
            if (r8 == 0) goto L_0x00af
            com.taobao.weex.WXSDKManager r14 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.WXSDKInstance r14 = r14.getSDKInstance(r11)     // Catch:{ Exception -> 0x0113 }
            if (r14 == 0) goto L_0x00af
            com.taobao.weex.bridge.WXBridgeManager$BundType r15 = com.taobao.weex.bridge.WXBridgeManager.BundType.Rax     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.bridge.WXBridgeManager$BundType r14 = r14.bundleType     // Catch:{ Exception -> 0x0113 }
            boolean r14 = r15.equals(r14)     // Catch:{ Exception -> 0x0113 }
            if (r14 == 0) goto L_0x00af
            java.util.Iterator r14 = r8.iterator()     // Catch:{ Exception -> 0x0113 }
            r15 = r0
        L_0x0085:
            boolean r4 = r14.hasNext()     // Catch:{ Exception -> 0x0113 }
            if (r4 == 0) goto L_0x00a7
            java.lang.Object r4 = r14.next()     // Catch:{ Exception -> 0x0113 }
            boolean r5 = r4 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0113 }
            if (r5 == 0) goto L_0x0085
            r5 = r4
            com.alibaba.fastjson.JSONObject r5 = (com.alibaba.fastjson.JSONObject) r5     // Catch:{ Exception -> 0x0113 }
            java.lang.String r6 = "__weex_options__"
            boolean r5 = r5.containsKey(r6)     // Catch:{ Exception -> 0x0113 }
            if (r5 == 0) goto L_0x0085
            com.alibaba.fastjson.JSONObject r4 = (com.alibaba.fastjson.JSONObject) r4     // Catch:{ Exception -> 0x0113 }
            java.lang.String r15 = "__weex_options__"
            java.lang.Object r15 = r4.get(r15)     // Catch:{ Exception -> 0x0113 }
            goto L_0x0085
        L_0x00a7:
            boolean r14 = r15 instanceof com.alibaba.fastjson.JSONObject     // Catch:{ Exception -> 0x0113 }
            if (r14 == 0) goto L_0x00af
            r14 = r15
            com.alibaba.fastjson.JSONObject r14 = (com.alibaba.fastjson.JSONObject) r14     // Catch:{ Exception -> 0x0113 }
            goto L_0x0068
        L_0x00af:
            r9 = r0
        L_0x00b0:
            com.taobao.weex.bridge.WXBridgeManager r4 = com.taobao.weex.bridge.WXBridgeManager.getInstance()     // Catch:{ Exception -> 0x0113 }
            r5 = r11
            r6 = r12
            r7 = r13
            java.lang.Object r11 = r4.callNativeModule((java.lang.String) r5, (java.lang.String) r6, (java.lang.String) r7, (com.alibaba.fastjson.JSONArray) r8, (com.alibaba.fastjson.JSONObject) r9)     // Catch:{ Exception -> 0x0113 }
            if (r3 == 0) goto L_0x00d8
            com.taobao.weex.performance.WXInstanceApm r12 = r3.getApmForInstance()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r13 = "wxFSCallNativeTotalNum"
            r14 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r12.updateFSDiffStats(r13, r14)     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.performance.WXInstanceApm r12 = r3.getApmForInstance()     // Catch:{ Exception -> 0x0113 }
            java.lang.String r13 = "wxFSCallNativeTotalTime"
            long r14 = com.taobao.weex.utils.WXUtils.getFixUnixTime()     // Catch:{ Exception -> 0x0113 }
            r4 = 0
            long r14 = r14 - r1
            double r14 = (double) r14     // Catch:{ Exception -> 0x0113 }
            r12.updateFSDiffStats(r13, r14)     // Catch:{ Exception -> 0x0113 }
        L_0x00d8:
            if (r3 == 0) goto L_0x010e
            com.taobao.weex.common.WXRenderStrategy r12 = r3.getRenderStrategy()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.common.WXRenderStrategy r13 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Exception -> 0x0113 }
            if (r12 == r13) goto L_0x00ea
            com.taobao.weex.common.WXRenderStrategy r12 = r3.getRenderStrategy()     // Catch:{ Exception -> 0x0113 }
            com.taobao.weex.common.WXRenderStrategy r13 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Exception -> 0x0113 }
            if (r12 != r13) goto L_0x010e
        L_0x00ea:
            if (r11 != 0) goto L_0x00f2
            com.taobao.weex.bridge.WXJSObject r12 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Exception -> 0x0109 }
            r12.<init>(r0)     // Catch:{ Exception -> 0x0109 }
            return r12
        L_0x00f2:
            java.lang.Class r12 = r11.getClass()     // Catch:{ Exception -> 0x0109 }
            java.lang.Class<com.taobao.weex.bridge.WXJSObject> r13 = com.taobao.weex.bridge.WXJSObject.class
            if (r12 != r13) goto L_0x00fe
            r12 = r11
            com.taobao.weex.bridge.WXJSObject r12 = (com.taobao.weex.bridge.WXJSObject) r12     // Catch:{ Exception -> 0x0109 }
            return r12
        L_0x00fe:
            com.taobao.weex.bridge.WXJSObject r12 = new com.taobao.weex.bridge.WXJSObject     // Catch:{ Exception -> 0x0109 }
            r13 = 3
            java.lang.String r14 = com.taobao.weex.utils.WXJsonUtils.fromObjectToJSONString(r11)     // Catch:{ Exception -> 0x0109 }
            r12.<init>(r13, r14)     // Catch:{ Exception -> 0x0109 }
            return r12
        L_0x0109:
            com.taobao.weex.bridge.WXJSObject r11 = com.taobao.weex.utils.WXWsonJSONSwitch.toWsonOrJsonWXJSObject(r11)     // Catch:{ Exception -> 0x0113 }
            return r11
        L_0x010e:
            com.taobao.weex.bridge.WXJSObject r11 = com.taobao.weex.utils.WXWsonJSONSwitch.toWsonOrJsonWXJSObject(r11)     // Catch:{ Exception -> 0x0113 }
            return r11
        L_0x0113:
            r11 = move-exception
            java.lang.String r12 = "WXBridge"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r12, (java.lang.Throwable) r11)
            com.taobao.weex.bridge.WXJSObject r11 = new com.taobao.weex.bridge.WXJSObject
            r11.<init>(r0)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridge.callNativeModule(java.lang.String, java.lang.String, java.lang.String, byte[], byte[]):java.lang.Object");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v8, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: com.alibaba.fastjson.JSONArray} */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:9|10|11|12) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0044 */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.taobao.weex.base.CalledByNative
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void callNativeComponent(java.lang.String r7, java.lang.String r8, java.lang.String r9, byte[] r10, byte[] r11) {
        /*
            r6 = this;
            com.taobao.weex.performance.WXStateRecord r0 = com.taobao.weex.performance.WXStateRecord.getInstance()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "callNativeComponent:"
            r1.append(r2)
            r1.append(r9)
            java.lang.String r1 = r1.toString()
            r0.recordAction(r7, r1)
            com.taobao.weex.WXSDKManager r0 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.WXSDKInstance r0 = r0.getSDKInstance(r7)     // Catch:{ Exception -> 0x0063 }
            r1 = 0
            if (r10 == 0) goto L_0x0053
            if (r0 == 0) goto L_0x004c
            com.taobao.weex.common.WXRenderStrategy r1 = r0.getRenderStrategy()     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.common.WXRenderStrategy r2 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER     // Catch:{ Exception -> 0x0063 }
            if (r1 == r2) goto L_0x0035
            com.taobao.weex.common.WXRenderStrategy r0 = r0.getRenderStrategy()     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.common.WXRenderStrategy r1 = com.taobao.weex.common.WXRenderStrategy.DATA_RENDER_BINARY     // Catch:{ Exception -> 0x0063 }
            if (r0 != r1) goto L_0x004c
        L_0x0035:
            java.lang.String r0 = new java.lang.String     // Catch:{ Exception -> 0x0044 }
            java.lang.String r1 = "UTF-8"
            r0.<init>(r10, r1)     // Catch:{ Exception -> 0x0044 }
            java.lang.Object r0 = com.alibaba.fastjson.JSON.parse(r0)     // Catch:{ Exception -> 0x0044 }
            r1 = r0
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x0044 }
            goto L_0x0053
        L_0x0044:
            java.lang.Object r10 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r10)     // Catch:{ Exception -> 0x0063 }
            r1 = r10
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x0063 }
            goto L_0x0053
        L_0x004c:
            java.lang.Object r10 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r10)     // Catch:{ Exception -> 0x0063 }
            r1 = r10
            com.alibaba.fastjson.JSONArray r1 = (com.alibaba.fastjson.JSONArray) r1     // Catch:{ Exception -> 0x0063 }
        L_0x0053:
            r4 = r1
            java.lang.Object r5 = com.taobao.weex.utils.WXWsonJSONSwitch.parseWsonOrJSON(r11)     // Catch:{ Exception -> 0x0063 }
            com.taobao.weex.bridge.WXBridgeManager r0 = com.taobao.weex.bridge.WXBridgeManager.getInstance()     // Catch:{ Exception -> 0x0063 }
            r1 = r7
            r2 = r8
            r3 = r9
            r0.callNativeComponent(r1, r2, r3, r4, r5)     // Catch:{ Exception -> 0x0063 }
            goto L_0x0069
        L_0x0063:
            r7 = move-exception
            java.lang.String r8 = "WXBridge"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r8, (java.lang.Throwable) r7)
        L_0x0069:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.bridge.WXBridge.callNativeComponent(java.lang.String, java.lang.String, java.lang.String, byte[], byte[]):void");
    }

    @CalledByNative
    public void setTimeoutNative(String str, String str2) {
        WXBridgeManager.getInstance().setTimeout(str, str2);
    }

    @CalledByNative
    public void setJSFrmVersion(String str) {
        if (str != null) {
            WXEnvironment.JS_LIB_SDK_VERSION = str;
        }
        WXStateRecord.getInstance().onJSFMInit();
    }

    public void resetWXBridge(boolean z) {
        nativeResetWXBridge(this, getClass().getName().replace('.', DXTemplateNamePathUtil.DIR));
    }

    @CalledByNative
    public int callUpdateFinish(String str, byte[] bArr, String str2) {
        try {
            return WXBridgeManager.getInstance().callUpdateFinish(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callCreateBody throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callRefreshFinish(String str, byte[] bArr, String str2) {
        try {
            return WXBridgeManager.getInstance().callRefreshFinish(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public void reportServerCrash(String str, String str2) {
        WXLogUtils.e(TAG, "reportServerCrash instanceId:" + str + " crashFile: " + str2);
        try {
            WXBridgeManager.getInstance().callReportCrashReloadPage(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "reloadPageNative throw exception:" + WXLogUtils.getStackTrace(th));
            }
        }
    }

    @CalledByNative
    public int callCreateBody(String str, String str2, String str3, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3) {
        try {
            return WXBridgeManager.getInstance().callCreateBody(str, str2, str3, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callCreateBody throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callAddElement(String str, String str2, String str3, int i, String str4, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3, boolean z) {
        try {
            return WXBridgeManager.getInstance().callAddElement(str, str2, str3, i, str4, hashMap, hashMap2, hashSet, fArr, fArr2, fArr3, z);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                th.printStackTrace();
                WXLogUtils.e(TAG, "callAddElement throw error:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callRemoveElement(String str, String str2) {
        try {
            return WXBridgeManager.getInstance().callRemoveElement(str, str2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callRemoveElement throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callMoveElement(String str, String str2, String str3, int i) {
        try {
            return WXBridgeManager.getInstance().callMoveElement(str, str2, str3, i);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callMoveElement throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callAddEvent(String str, String str2, String str3) {
        try {
            return WXBridgeManager.getInstance().callAddEvent(str, str2, str3);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callAddEvent throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    @CalledByNative
    public int callRemoveEvent(String str, String str2, String str3) {
        try {
            return WXBridgeManager.getInstance().callRemoveEvent(str, str2, str3);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callRemoveEvent throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callUpdateStyle(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3, HashMap<String, String> hashMap4) {
        try {
            return WXBridgeManager.getInstance().callUpdateStyle(str, str2, hashMap, hashMap2, hashMap3, hashMap4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateStyle throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callUpdateAttrs(String str, String str2, HashMap<String, String> hashMap) {
        try {
            return WXBridgeManager.getInstance().callUpdateAttrs(str, str2, hashMap);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateAttr throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callAddChildToRichtext(String str, String str2, String str3, String str4, String str5, HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        try {
            return WXBridgeManager.getInstance().callAddChildToRichtext(str, str2, str3, str4, str5, hashMap, hashMap2);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callAddChildToRichtext throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callRemoveChildFromRichtext(String str, String str2, String str3, String str4) {
        try {
            return WXBridgeManager.getInstance().callRemoveChildFromRichtext(str, str2, str3, str4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callRemoveChildFromRichtext throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callUpdateRichtextStyle(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        try {
            return WXBridgeManager.getInstance().callUpdateRichtextStyle(str, str2, hashMap, str3, str4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateRichtextStyle throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callUpdateRichtextChildAttr(String str, String str2, HashMap<String, String> hashMap, String str3, String str4) {
        try {
            return WXBridgeManager.getInstance().callUpdateRichtextChildAttr(str, str2, hashMap, str3, str4);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callUpdateRichtextChildAttr throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callLayout(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, boolean z, int i7) {
        try {
            return WXBridgeManager.getInstance().callLayout(str, str2, i, i2, i3, i4, i5, i6, z, i7);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callLayout throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public int callCreateFinish(String str) {
        try {
            return WXBridgeManager.getInstance().callCreateFinish(str);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    @CalledByNative
    public int callRenderSuccess(String str) {
        try {
            return WXBridgeManager.getInstance().callRenderSuccess(str);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    @CalledByNative
    public int callAppendTreeCreateFinish(String str, String str2) {
        try {
            return WXBridgeManager.getInstance().callAppendTreeCreateFinish(str, str2);
        } catch (Throwable th) {
            WXLogUtils.e(TAG, "callAppendTreeCreateFinish throw exception:" + WXLogUtils.getStackTrace(th));
            return 1;
        }
    }

    @CalledByNative
    public int callHasTransitionPros(String str, String str2, HashMap<String, String> hashMap) {
        try {
            return WXBridgeManager.getInstance().callHasTransitionPros(str, str2, hashMap);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "callHasTransitionPros throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return 1;
        }
    }

    @CalledByNative
    public ContentBoxMeasurement getMeasurementFunc(String str, long j) {
        try {
            return WXBridgeManager.getInstance().getMeasurementFunc(str, j);
        } catch (Throwable th) {
            if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "getMeasurementFunc throw exception:" + WXLogUtils.getStackTrace(th));
            }
            return null;
        }
    }

    public void bindMeasurementToRenderObject(long j) {
        nativeBindMeasurementToRenderObject(j);
    }

    public void setRenderContainerWrapContent(boolean z, String str) {
        nativeSetRenderContainerWrapContent(z, str);
    }

    public long[] getFirstScreenRenderTime(String str) {
        return nativeGetFirstScreenRenderTime(str);
    }

    public long[] getRenderFinishTime(String str) {
        return nativeGetRenderFinishTime(str);
    }

    public void setDefaultHeightAndWidthIntoRootDom(String str, float f, float f2, boolean z, boolean z2) {
        nativeSetDefaultHeightAndWidthIntoRootDom(str, f, f2, z, z2);
    }

    public void onInstanceClose(String str) {
        nativeOnInstanceClose(str);
    }

    public void forceLayout(String str) {
        nativeForceLayout(str);
    }

    public boolean notifyLayout(String str) {
        return nativeNotifyLayout(str);
    }

    public void setStyleWidth(String str, String str2, float f) {
        nativeSetStyleWidth(str, str2, f);
    }

    public void setMargin(String str, String str2, CSSShorthand.EDGE edge, float f) {
        nativeSetMargin(str, str2, edge.ordinal(), f);
    }

    public void setPadding(String str, String str2, CSSShorthand.EDGE edge, float f) {
        nativeSetPadding(str, str2, edge.ordinal(), f);
    }

    public void setPosition(String str, String str2, CSSShorthand.EDGE edge, float f) {
        nativeSetPosition(str, str2, edge.ordinal(), f);
    }

    public void markDirty(String str, String str2, boolean z) {
        nativeMarkDirty(str, str2, z);
    }

    public void setDeviceDisplay(String str, float f, float f2, float f3) {
        nativeSetDeviceDisplay(str, f, f2, f3);
    }

    public void setStyleHeight(String str, String str2, float f) {
        nativeSetStyleHeight(str, str2, f);
    }

    public void setInstanceRenderType(String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            nativeSetInstanceRenderType(str, str2);
        }
    }

    public void setViewPortWidth(String str, float f) {
        nativeSetViewPortWidth(str, f);
    }

    public void removeInstanceRenderType(String str) {
        nativeRemoveInstanceRenderType(str);
    }

    public void setPageArgument(String str, String str2, String str3) {
        nativeSetPageArgument(str, str2, str3);
    }

    public void registerCoreEnv(String str, String str2) {
        nativeRegisterCoreEnv(str, str2);
    }

    @CalledByNative
    public void reportNativeInitStatus(String str, String str2) {
        if (WXErrorCode.WX_JS_FRAMEWORK_INIT_SINGLE_PROCESS_SUCCESS.getErrorCode().equals(str) || WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED.getErrorCode().equals(str)) {
            IWXUserTrackAdapter iWXUserTrackAdapter = WXSDKManager.getInstance().getIWXUserTrackAdapter();
            if (iWXUserTrackAdapter != null) {
                HashMap hashMap = new HashMap(3);
                hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_CODE, str);
                hashMap.put("arg", "InitFrameworkNativeError");
                hashMap.put(IWXUserTrackAdapter.MONITOR_ERROR_MSG, str2);
                WXLogUtils.e("reportNativeInitStatus is running and errorCode is " + str + " And errorMsg is " + str2);
                iWXUserTrackAdapter.commit((Context) null, (String) null, IWXUserTrackAdapter.INIT_FRAMEWORK, (WXPerformance) null, hashMap);
            }
        } else if (WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED_PARAMS_NULL.getErrorCode().equals(str)) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED_PARAMS_NULL;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "WeexProxy::initFromParam()", WXErrorCode.WX_JS_FRAMEWORK_INIT_FAILED_PARAMS_NULL.getErrorMsg() + ": " + str2, (Map<String, String>) null);
        } else {
            WXErrorCode[] values = WXErrorCode.values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                WXErrorCode wXErrorCode2 = values[i];
                if (!wXErrorCode2.getErrorType().equals(WXErrorCode.ErrorType.NATIVE_ERROR) || !wXErrorCode2.getErrorCode().equals(str)) {
                    i++;
                } else {
                    WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode2, IWXUserTrackAdapter.INIT_FRAMEWORK, str2, (Map<String, String>) null);
                    return;
                }
            }
        }
    }

    public void reloadPageLayout(String str) {
        nativeReloadPageLayout(str);
    }

    public void setDeviceDisplayOfPage(String str, float f, float f2) {
        nativeSetDeviceDisplayOfPage(str, f, f2);
    }

    @CalledByNative
    public void setPageDirty(String str, boolean z) {
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(str);
        if (sDKInstance != null) {
            sDKInstance.setPageDirty(z);
        }
    }
}
