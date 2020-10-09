package com.taobao.weex.devtools.debug;

import android.text.TextUtils;
import android.util.Log;
import anet.channel.util.HttpConstant;
import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.bridge.ResultCallback;
import com.taobao.weex.bridge.WXBridge;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.bridge.WXDebugJsBridge;
import com.taobao.weex.bridge.WXJSObject;
import com.taobao.weex.bridge.WXParams;
import com.taobao.weex.common.IWXBridge;
import com.taobao.weex.devtools.common.LogUtil;
import com.taobao.weex.devtools.websocket.SimpleSession;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.layout.ContentBoxMeasurement;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXWsonJSONSwitch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WXDebugBridge implements IWXBridge {
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "weex-devtool";
    private static volatile WXDebugBridge sInstance;
    private final OkHttpClient client = new OkHttpClient();
    private final Object mLock = new Object();
    private IWXBridge mOriginBridge = new WXBridge();
    private volatile SimpleSession mSession;
    private WXDebugJsBridge mWXDebugJsBridge;
    private String syncCallJSURL = "";

    public void removeInstanceRenderType(String str) {
    }

    public void setDeviceDisplay(String str, float f, float f2, float f3) {
    }

    public void setInstanceRenderType(String str, String str2) {
    }

    public void setPageArgument(String str, String str2, String str3) {
    }

    public void updateInitFrameworkParams(String str, String str2, String str3) {
    }

    private WXDebugBridge() {
    }

    public static WXDebugBridge getInstance() {
        if (sInstance == null) {
            synchronized (WXDebugBridge.class) {
                if (sInstance == null) {
                    sInstance = new WXDebugBridge();
                }
            }
        }
        return sInstance;
    }

    public int initFramework(String str, WXParams wXParams) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> initFramework");
        while (true) {
            if (this.mSession == null || (this.mSession != null && !this.mSession.isOpen())) {
                synchronized (this.mLock) {
                    try {
                        Log.v(TAG, "waiting for session now");
                        this.mLock.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sendMessage(getInitFrameworkMessage(str, wXParams));
    }

    public int initFrameworkEnv(String str, WXParams wXParams, String str2, boolean z) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> initFrameworkEnv");
        return initFramework(str, wXParams);
    }

    public void refreshInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> refreshInstance, instanceId is " + str);
        this.mOriginBridge.refreshInstance(str, str2, str3, wXJSObjectArr);
    }

    public int execJS(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        int i;
        WXLogUtils.e(TAG, "WXDebugBridge >>>> execJS, instanceId is " + str + ", func is " + str3 + ", args is " + wXJSObjectArr);
        ArrayList arrayList = new ArrayList();
        if (wXJSObjectArr == null) {
            i = 0;
        } else {
            i = wXJSObjectArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (wXJSObjectArr[i2] != null) {
                if (wXJSObjectArr[i2].type != 2) {
                    arrayList.add(WXWsonJSONSwitch.convertWXJSObjectDataToJSON(wXJSObjectArr[i2]));
                } else {
                    arrayList.add(wXJSObjectArr[i2].data);
                }
            }
        }
        HashMap hashMap = new HashMap();
        if (TextUtils.equals(str3, WXBridgeManager.METHOD_REGISTER_COMPONENTS) || TextUtils.equals(str3, WXBridgeManager.METHOD_REGISTER_MODULES) || TextUtils.equals(str3, WXBridgeManager.METHOD_DESTROY_INSTANCE)) {
            hashMap.put("method", str3);
        } else if (TextUtils.equals(str3, WXBridgeManager.METHOD_CREATE_INSTANCE)) {
            hashMap.put("method", WXBridgeManager.METHOD_CREATE_INSTANCE);
        } else {
            hashMap.put("method", WXDebugConstants.WEEX_CALL_JAVASCRIPT);
        }
        hashMap.put("args", arrayList);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("method", WXDebugConstants.METHOD_CALL_JS);
        hashMap2.put("params", hashMap);
        return sendMessage(JSON.toJSONString(hashMap2));
    }

    public void execJSWithCallback(String str, String str2, String str3, WXJSObject[] wXJSObjectArr, ResultCallback resultCallback) {
        int i;
        WXLogUtils.e(TAG, "WXDebugBridge >>>> execJSWithCallback, instanceId is " + str + ", func is " + str3 + ", args is " + wXJSObjectArr);
        ArrayList arrayList = new ArrayList();
        if (wXJSObjectArr == null) {
            i = 0;
        } else {
            i = wXJSObjectArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (wXJSObjectArr[i2] != null) {
                if (wXJSObjectArr[i2].type != 2) {
                    arrayList.add(WXWsonJSONSwitch.convertWXJSObjectDataToJSON(wXJSObjectArr[i2]));
                } else {
                    arrayList.add(wXJSObjectArr[i2].data);
                }
            }
        }
        HashMap hashMap = new HashMap();
        if (TextUtils.equals(str3, WXBridgeManager.METHOD_REGISTER_COMPONENTS) || TextUtils.equals(str3, WXBridgeManager.METHOD_REGISTER_MODULES) || TextUtils.equals(str3, WXBridgeManager.METHOD_DESTROY_INSTANCE)) {
            hashMap.put("method", str3);
        } else {
            hashMap.put("method", WXDebugConstants.WEEX_CALL_JAVASCRIPT);
        }
        hashMap.put("args", arrayList);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("method", WXDebugConstants.METHOD_CALL_JS);
        hashMap2.put("params", hashMap);
        try {
            Response execute = this.client.newCall(new Request.Builder().url(this.syncCallJSURL).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, JSON.toJSONString(hashMap2))).build()).execute();
            if (execute.isSuccessful()) {
                execute.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int execJSService(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("source", str);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("method", WXDebugConstants.METHOD_IMPORT_JS);
        hashMap2.put("params", hashMap);
        return sendMessage(JSON.toJSONString(hashMap2));
    }

    public void takeHeapSnapshot(String str) {
        LogUtil.log("warning", "Ignore invoke takeSnapshot: " + str);
    }

    public int createInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> createInstanceContext, instanceId is " + str + ", func is " + str3 + ", args is " + wXJSObjectArr);
        WXJSObject wXJSObject = wXJSObjectArr[0];
        WXJSObject wXJSObject2 = wXJSObjectArr[1];
        WXJSObject wXJSObject3 = wXJSObjectArr[2];
        doCreateInstanceContext(str, str2, WXBridgeManager.METHOD_CREATE_INSTANCE_CONTEXT, new WXJSObject[]{wXJSObject, wXJSObject3, wXJSObjectArr[3], wXJSObjectArr[4]});
        return doImportScript(str, str2, "importScript", new WXJSObject[]{wXJSObject, wXJSObject2, wXJSObject3});
    }

    private int doCreateInstanceContext(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        int i;
        ArrayList arrayList = new ArrayList();
        if (wXJSObjectArr == null) {
            i = 0;
        } else {
            i = wXJSObjectArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (wXJSObjectArr[i2].type != 2) {
                arrayList.add(WXWsonJSONSwitch.convertWXJSObjectDataToJSON(wXJSObjectArr[i2]));
            } else {
                arrayList.add(wXJSObjectArr[i2].data);
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("method", WXDebugConstants.METHOD_CALL_JS);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("method", str3);
        hashMap2.put("args", arrayList);
        hashMap.put("params", hashMap2);
        return sendMessage(JSON.toJSONString(hashMap));
    }

    private int doImportScript(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        int i;
        ArrayList arrayList = new ArrayList();
        if (wXJSObjectArr == null) {
            i = 0;
        } else {
            i = wXJSObjectArr.length;
        }
        for (int i2 = 0; i2 < i; i2++) {
            if (wXJSObjectArr[i2].type != 2) {
                arrayList.add(WXWsonJSONSwitch.convertWXJSObjectDataToJSON(wXJSObjectArr[i2]));
            } else {
                arrayList.add(wXJSObjectArr[i2].data);
            }
        }
        HashMap hashMap = new HashMap();
        hashMap.put("method", str3);
        hashMap.put("args", arrayList);
        HashMap hashMap2 = new HashMap();
        hashMap2.put("method", WXDebugConstants.METHOD_CALL_JS);
        hashMap2.put("params", hashMap);
        return sendMessage(JSON.toJSONString(hashMap2));
    }

    public int destoryInstance(String str, String str2, String str3, WXJSObject[] wXJSObjectArr) {
        return execJS(str, str2, str3, wXJSObjectArr);
    }

    public String execJSOnInstance(String str, String str2, int i) {
        return this.mOriginBridge.execJSOnInstance(str, str2, i);
    }

    public int callNative(String str, byte[] bArr, String str2) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> callNative, instanceId is " + str + ", tasks is " + new String(bArr));
        return callNative(str, new String(bArr), str2);
    }

    public int callNative(String str, String str2, String str3) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> callNative, instanceId is " + str + ", tasks is " + str2);
        return this.mOriginBridge.callNative(str, str2, str3);
    }

    public void reportJSException(String str, String str2, String str3) {
        this.mOriginBridge.reportJSException(str, str2, str3);
    }

    public Object callNativeModule(String str, String str2, String str3, byte[] bArr, byte[] bArr2) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> callNativeModule, instanceId is " + str + ", module is " + str2 + ", method is " + str3 + ", arguments is " + new String(bArr));
        return this.mOriginBridge.callNativeModule(str, str2, str3, bArr, bArr2);
    }

    public void callNativeComponent(String str, String str2, String str3, byte[] bArr, byte[] bArr2) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> callNativeComponent, instanceId is " + str + ", componentRef is " + str2 + ", method is " + str3 + ", arguments is " + new String(bArr));
        this.mOriginBridge.callNativeComponent(str, str2, str3, bArr, bArr2);
    }

    public int callUpdateFinish(String str, byte[] bArr, String str2) {
        return this.mOriginBridge.callUpdateFinish(str, bArr, str2);
    }

    public int callRefreshFinish(String str, byte[] bArr, String str2) {
        return this.mOriginBridge.callRefreshFinish(str, bArr, str2);
    }

    public int callAddEvent(String str, String str2, String str3) {
        return this.mOriginBridge.callAddEvent(str, str2, str3);
    }

    public int callRemoveEvent(String str, String str2, String str3) {
        return this.mOriginBridge.callRemoveEvent(str, str2, str3);
    }

    public int callUpdateStyle(String str, String str2, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2, HashMap<String, String> hashMap3, HashMap<String, String> hashMap4) {
        return this.mOriginBridge.callUpdateStyle(str, str2, hashMap, hashMap2, hashMap3, hashMap4);
    }

    public int callUpdateAttrs(String str, String str2, HashMap<String, String> hashMap) {
        return this.mOriginBridge.callUpdateAttrs(str, str2, hashMap);
    }

    public int callLayout(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6, boolean z, int i7) {
        StringBuilder sb = new StringBuilder();
        sb.append("callLayout ");
        String str3 = str;
        sb.append(str);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        String str4 = str2;
        sb.append(str4);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        int i8 = i;
        sb.append(i8);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        int i9 = i2;
        sb.append(i9);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        int i10 = i3;
        sb.append(i10);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        int i11 = i4;
        sb.append(i11);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        int i12 = i5;
        sb.append(i12);
        sb.append(",");
        int i13 = i6;
        sb.append(i13);
        WXLogUtils.e("WXDebugBridge layout", sb.toString());
        return this.mOriginBridge.callLayout(str, str4, i8, i9, i10, i11, i12, i13, z, i7);
    }

    public int callCreateFinish(String str) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> callCreateFinish");
        return this.mOriginBridge.callCreateFinish(str);
    }

    public int callRenderSuccess(String str) {
        WXLogUtils.e(TAG, "WXDebugBridge >>>> callRenderSuccess");
        return this.mOriginBridge.callRenderSuccess(str);
    }

    public int callAppendTreeCreateFinish(String str, String str2) {
        return this.mOriginBridge.callAppendTreeCreateFinish(str, str2);
    }

    public void reportServerCrash(String str, String str2) {
        LogUtil.e("ServerCrash: instanceId: " + str + ", crashFile: " + str2);
    }

    public int callCreateBody(String str, String str2, String str3, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3) {
        StringBuilder sb = new StringBuilder();
        sb.append("WXDebugBridge >>>> callCreateBody >>>> pageId:");
        String str4 = str;
        sb.append(str);
        sb.append(", componentType:");
        sb.append(str2);
        sb.append(", ref:");
        sb.append(str3);
        sb.append(", styles:");
        HashMap<String, String> hashMap3 = hashMap;
        sb.append(hashMap3);
        sb.append(", attributes:");
        HashMap<String, String> hashMap4 = hashMap2;
        sb.append(hashMap4);
        sb.append(", events:");
        HashSet<String> hashSet2 = hashSet;
        sb.append(hashSet2);
        WXLogUtils.e(TAG, sb.toString());
        return this.mOriginBridge.callCreateBody(str, str2, str3, hashMap3, hashMap4, hashSet2, fArr, fArr2, fArr3);
    }

    public int callAddElement(String str, String str2, String str3, int i, String str4, HashMap<String, String> hashMap, HashMap<String, String> hashMap2, HashSet<String> hashSet, float[] fArr, float[] fArr2, float[] fArr3, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("WXDebugBridge >>>> callAddElement >>>> pageId:");
        sb.append(str);
        sb.append(", componentType:");
        String str5 = str2;
        sb.append(str5);
        sb.append(", ref:");
        String str6 = str3;
        sb.append(str6);
        sb.append(", index:");
        int i2 = i;
        sb.append(i2);
        sb.append(", parentRef:");
        String str7 = str4;
        sb.append(str7);
        sb.append(", styles:");
        HashMap<String, String> hashMap3 = hashMap;
        sb.append(hashMap3);
        sb.append(", attributes:");
        HashMap<String, String> hashMap4 = hashMap2;
        sb.append(hashMap4);
        sb.append(", events:");
        HashSet<String> hashSet2 = hashSet;
        sb.append(hashSet2);
        WXLogUtils.e(TAG, sb.toString());
        return this.mOriginBridge.callAddElement(str, str5, str6, i2, str7, hashMap3, hashMap4, hashSet2, fArr, fArr2, fArr3, z);
    }

    public int callRemoveElement(String str, String str2) {
        return this.mOriginBridge.callRemoveElement(str, str2);
    }

    public int callMoveElement(String str, String str2, String str3, int i) {
        return this.mOriginBridge.callMoveElement(str, str2, str3, i);
    }

    public int callHasTransitionPros(String str, String str2, HashMap<String, String> hashMap) {
        return this.mOriginBridge.callHasTransitionPros(str, str2, hashMap);
    }

    public ContentBoxMeasurement getMeasurementFunc(String str, long j) {
        return this.mOriginBridge.getMeasurementFunc(str, j);
    }

    public void bindMeasurementToRenderObject(long j) {
        this.mOriginBridge.bindMeasurementToRenderObject(j);
    }

    public void setRenderContainerWrapContent(boolean z, String str) {
        this.mOriginBridge.setRenderContainerWrapContent(z, str);
    }

    public long[] getFirstScreenRenderTime(String str) {
        return this.mOriginBridge.getFirstScreenRenderTime(str);
    }

    public long[] getRenderFinishTime(String str) {
        return this.mOriginBridge.getRenderFinishTime(str);
    }

    public void setDefaultHeightAndWidthIntoRootDom(String str, float f, float f2, boolean z, boolean z2) {
        this.mOriginBridge.setDefaultHeightAndWidthIntoRootDom(str, f, f2, z, z2);
    }

    public void onInstanceClose(String str) {
        this.mOriginBridge.onInstanceClose(str);
    }

    public void forceLayout(String str) {
        this.mOriginBridge.forceLayout(str);
    }

    public boolean notifyLayout(String str) {
        return this.mOriginBridge.notifyLayout(str);
    }

    public void setStyleWidth(String str, String str2, float f) {
        this.mOriginBridge.setStyleWidth(str, str2, f);
    }

    public void setStyleHeight(String str, String str2, float f) {
        this.mOriginBridge.setStyleHeight(str, str2, f);
    }

    public void setMargin(String str, String str2, CSSShorthand.EDGE edge, float f) {
        this.mOriginBridge.setMargin(str, str2, edge, f);
    }

    public void setPadding(String str, String str2, CSSShorthand.EDGE edge, float f) {
        this.mOriginBridge.setPadding(str, str2, edge, f);
    }

    public void setPosition(String str, String str2, CSSShorthand.EDGE edge, float f) {
        this.mOriginBridge.setPosition(str, str2, edge, f);
    }

    public void markDirty(String str, String str2, boolean z) {
        this.mOriginBridge.markDirty(str, str2, z);
    }

    public void registerCoreEnv(String str, String str2) {
        this.mOriginBridge.registerCoreEnv(str, str2);
    }

    public void reportNativeInitStatus(String str, String str2) {
        this.mOriginBridge.reportNativeInitStatus(str, str2);
    }

    public void setTimeoutNative(String str, String str2) {
        this.mOriginBridge.setTimeoutNative(str, str2);
    }

    public void setJSFrmVersion(String str) {
        this.mOriginBridge.setJSFrmVersion(str);
    }

    public void resetWXBridge(boolean z) {
        this.mWXDebugJsBridge.resetWXBridge(this, getClass().getName().replace('.', DXTemplateNamePathUtil.DIR));
    }

    public void setSession(SimpleSession simpleSession) {
        this.mSession = simpleSession;
        if (this.mSession instanceof SocketClient) {
            String[] split = ((SocketClient) this.mSession).getUrl().split("debugProxy/native");
            if (split.length >= 2) {
                this.syncCallJSURL = split[0] + "syncCallJS" + split[1];
                StringBuilder sb = new StringBuilder();
                sb.append("http://");
                sb.append(this.syncCallJSURL.split(HttpConstant.SCHEME_SPLIT)[1]);
                this.syncCallJSURL = sb.toString();
            }
        }
    }

    public void sendToRemote(String str) {
        if (this.mSession != null && this.mSession.isOpen()) {
            this.mSession.sendText(str);
        }
    }

    public void post(Runnable runnable) {
        if (this.mSession != null && this.mSession.isOpen()) {
            this.mSession.post(runnable);
        }
    }

    public boolean isSessionActive() {
        return this.mSession != null && this.mSession.isOpen();
    }

    public void onConnected() {
        Log.v(TAG, "connect to debug server success");
        synchronized (this.mLock) {
            this.mLock.notify();
        }
    }

    public void onDisConnected() {
        Log.w(TAG, "WebSocket disconnected");
        synchronized (this.mLock) {
            this.mSession = null;
            this.mLock.notify();
        }
    }

    private String getInitFrameworkMessage(String str, WXParams wXParams) {
        Map<String, Object> environmentMap;
        HashMap hashMap = new HashMap();
        hashMap.put("source", str);
        hashMap.put(WXDebugConstants.PARAM_LAYOUT_SANDBOX, "true");
        if (!(wXParams == null || (environmentMap = getEnvironmentMap(wXParams)) == null || environmentMap.size() <= 0)) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(WXDebugConstants.ENV_WX_ENVIRONMENT, environmentMap);
            hashMap.put("env", hashMap2);
        }
        HashMap hashMap3 = new HashMap();
        hashMap3.put("method", WXDebugConstants.METHOD_INIT_RUNTIME);
        hashMap3.put("params", hashMap);
        return JSON.toJSONString(hashMap3);
    }

    private Map<String, Object> getEnvironmentMap(WXParams wXParams) {
        HashMap hashMap = new HashMap();
        hashMap.put("appName", wXParams.getAppName());
        hashMap.put("appVersion", wXParams.getAppVersion());
        hashMap.put("platform", wXParams.getPlatform());
        hashMap.put(WXDebugConstants.ENV_OS_VERSION, wXParams.getOsVersion());
        hashMap.put("logLevel", wXParams.getLogLevel());
        hashMap.put("weexVersion", wXParams.getWeexVersion());
        hashMap.put(WXDebugConstants.ENV_DEVICE_MODEL, wXParams.getDeviceModel());
        hashMap.put(WXDebugConstants.ENV_INFO_COLLECT, wXParams.getShouldInfoCollect());
        hashMap.put("deviceWidth", wXParams.getDeviceWidth());
        hashMap.put("deviceHeight", wXParams.getDeviceHeight());
        hashMap.put("runtime", "devtools");
        hashMap.putAll(WXEnvironment.getCustomOptions());
        return hashMap;
    }

    private int sendMessage(String str) {
        if (this.mSession == null || !this.mSession.isOpen()) {
            WXBridgeManager.getInstance().stopRemoteDebug();
            return 0;
        }
        this.mSession.sendText(str);
        return 1;
    }

    public void setWXDebugJsBridge(WXDebugJsBridge wXDebugJsBridge) {
        this.mWXDebugJsBridge = wXDebugJsBridge;
    }

    public WXDebugJsBridge getWXDebugJsBridge() {
        return this.mWXDebugJsBridge;
    }
}
