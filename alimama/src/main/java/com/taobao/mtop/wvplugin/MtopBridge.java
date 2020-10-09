package com.taobao.mtop.wvplugin;

import alimama.com.unweventparse.constants.EventConstants;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import com.alibaba.fastjson.JSON;
import com.taobao.android.tlog.protocol.model.joint.point.TimerJointPoint;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.uc.webview.export.extension.UCCore;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.mtop.util.MtopStatistics;
import mtopsdk.network.domain.NetworkStats;
import org.json.JSONArray;
import org.json.JSONObject;

class MtopBridge {
    private static final String CODE = "code";
    private static final String MODULE = "mtopsdk";
    private static final String MONITOR_POINT = "jsStats";
    private static final int NOTIFY_RESULT = 500;
    private static final String RET = "ret";
    private static final String RETCODE = "retCode";
    private static final String TAG = "mtopsdk.MtopBridge";
    private static AtomicBoolean registerFlag = new AtomicBoolean(false);
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            if (message.what == 500 && (message.obj instanceof MtopResult)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopBridge.TAG, "call result, retString: " + ((MtopResult) message.obj).toString());
                }
                MtopWVPlugin mtopWVPlugin = (MtopWVPlugin) MtopBridge.this.wvPluginRef.get();
                if (mtopWVPlugin != null) {
                    try {
                        mtopWVPlugin.wvCallback((MtopResult) message.obj);
                    } catch (Exception e) {
                        TBSdkLog.e(MtopBridge.TAG, "execute  plugin.wvCallback error.", (Throwable) e);
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public WeakReference<MtopWVPlugin> wvPluginRef = null;

    public MtopBridge(MtopWVPlugin mtopWVPlugin) {
        this.wvPluginRef = new WeakReference<>(mtopWVPlugin);
    }

    public void setWvPluginRef(MtopWVPlugin mtopWVPlugin) {
        this.wvPluginRef = new WeakReference<>(mtopWVPlugin);
    }

    /* access modifiers changed from: private */
    public void dispatchToMainThread(MtopResult mtopResult) {
        if (mtopResult != null) {
            this.mHandler.obtainMessage(500, mtopResult).sendToTarget();
        }
    }

    /* access modifiers changed from: package-private */
    public void sendRequest(final WVCallBackContext wVCallBackContext, final String str) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "MtopBridge JSParams: " + str);
        }
        final FrontEndParams frontEndParams = new FrontEndParams(str);
        MtopWVPlugin mtopWVPlugin = (MtopWVPlugin) this.wvPluginRef.get();
        if (mtopWVPlugin != null) {
            frontEndParams.userAgent = mtopWVPlugin.getUserAgent();
            frontEndParams.pageUrl = mtopWVPlugin.getCurrentUrl();
        }
        scheduledExecutorService.submit(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:15:0x007e  */
            /* JADX WARNING: Removed duplicated region for block: B:16:0x0088  */
            /* JADX WARNING: Removed duplicated region for block: B:18:0x008b  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r10 = this;
                    r0 = 0
                    com.taobao.mtop.wvplugin.MtopBridge r1 = com.taobao.mtop.wvplugin.MtopBridge.this     // Catch:{ Exception -> 0x0062 }
                    com.taobao.mtop.wvplugin.FrontEndParams r2 = r0     // Catch:{ Exception -> 0x0062 }
                    java.util.Map r1 = r1.parseJSParams(r2)     // Catch:{ Exception -> 0x0062 }
                    if (r1 != 0) goto L_0x0055
                    java.lang.String r2 = "mtopsdk.MtopBridge"
                    java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0053 }
                    r3.<init>()     // Catch:{ Exception -> 0x0053 }
                    java.lang.String r4 = "MtopBridge parseJSParams failed. params:"
                    r3.append(r4)     // Catch:{ Exception -> 0x0053 }
                    java.lang.String r4 = r5     // Catch:{ Exception -> 0x0053 }
                    r3.append(r4)     // Catch:{ Exception -> 0x0053 }
                    java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0053 }
                    mtopsdk.common.util.TBSdkLog.e(r2, r3)     // Catch:{ Exception -> 0x0053 }
                    com.taobao.mtop.wvplugin.MtopBridge r4 = com.taobao.mtop.wvplugin.MtopBridge.this     // Catch:{ Exception -> 0x0053 }
                    r5 = 0
                    r6 = 0
                    java.lang.String r7 = "MtopBridge parseJSParams failed."
                    java.lang.String r8 = "HY_PARAM_ERR"
                    r9 = 0
                    r4.commitMtopJSStat(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x0053 }
                    com.taobao.mtop.wvplugin.MtopResult r2 = new com.taobao.mtop.wvplugin.MtopResult     // Catch:{ Exception -> 0x0053 }
                    android.taobao.windvane.jsbridge.WVCallBackContext r3 = r4     // Catch:{ Exception -> 0x0053 }
                    r2.<init>(r3)     // Catch:{ Exception -> 0x0053 }
                    java.lang.String r3 = "ret"
                    org.json.JSONArray r4 = new org.json.JSONArray     // Catch:{ Exception -> 0x0053 }
                    r4.<init>()     // Catch:{ Exception -> 0x0053 }
                    java.lang.String r5 = "HY_PARAM_ERR"
                    org.json.JSONArray r4 = r4.put(r5)     // Catch:{ Exception -> 0x0053 }
                    r2.addData((java.lang.String) r3, (org.json.JSONArray) r4)     // Catch:{ Exception -> 0x0053 }
                    java.lang.String r3 = "code"
                    java.lang.String r4 = "MtopBridge parseJSParams failed."
                    r2.addData((java.lang.String) r3, (java.lang.String) r4)     // Catch:{ Exception -> 0x0053 }
                    com.taobao.mtop.wvplugin.MtopBridge r3 = com.taobao.mtop.wvplugin.MtopBridge.this     // Catch:{ Exception -> 0x0053 }
                    r3.dispatchToMainThread(r2)     // Catch:{ Exception -> 0x0053 }
                    return
                L_0x0053:
                    r2 = move-exception
                    goto L_0x0064
                L_0x0055:
                    com.taobao.mtop.wvplugin.MtopBridge$MtopBridgeListener r2 = new com.taobao.mtop.wvplugin.MtopBridge$MtopBridgeListener     // Catch:{ Exception -> 0x0053 }
                    com.taobao.mtop.wvplugin.MtopBridge r3 = com.taobao.mtop.wvplugin.MtopBridge.this     // Catch:{ Exception -> 0x0053 }
                    android.taobao.windvane.jsbridge.WVCallBackContext r4 = r4     // Catch:{ Exception -> 0x0053 }
                    r2.<init>(r4, r1)     // Catch:{ Exception -> 0x0053 }
                    com.taobao.tao.remotebusiness.js.MtopJSBridge.sendMtopRequest(r1, r2)     // Catch:{ Exception -> 0x0053 }
                    goto L_0x00c1
                L_0x0062:
                    r2 = move-exception
                    r1 = r0
                L_0x0064:
                    java.lang.String r3 = "mtopsdk.MtopBridge"
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder
                    r4.<init>()
                    java.lang.String r5 = "MtopJSBridge sendMtopRequest failed.params:"
                    r4.append(r5)
                    java.lang.String r5 = r5
                    r4.append(r5)
                    java.lang.String r4 = r4.toString()
                    mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r4, (java.lang.Throwable) r2)
                    if (r1 == 0) goto L_0x0088
                    java.lang.String r2 = "api"
                    java.lang.Object r2 = r1.get(r2)
                    java.lang.String r2 = (java.lang.String) r2
                    r4 = r2
                    goto L_0x0089
                L_0x0088:
                    r4 = r0
                L_0x0089:
                    if (r1 == 0) goto L_0x0093
                    java.lang.String r0 = "v"
                    java.lang.Object r0 = r1.get(r0)
                    java.lang.String r0 = (java.lang.String) r0
                L_0x0093:
                    r5 = r0
                    com.taobao.mtop.wvplugin.MtopBridge r3 = com.taobao.mtop.wvplugin.MtopBridge.this
                    java.lang.String r6 = "MtopJSBridge sendMtopRequest failed."
                    java.lang.String r7 = "HY_FAILED"
                    r8 = 0
                    r3.commitMtopJSStat(r4, r5, r6, r7, r8)
                    com.taobao.mtop.wvplugin.MtopResult r0 = new com.taobao.mtop.wvplugin.MtopResult
                    android.taobao.windvane.jsbridge.WVCallBackContext r1 = r4
                    r0.<init>(r1)
                    java.lang.String r1 = "ret"
                    org.json.JSONArray r2 = new org.json.JSONArray
                    r2.<init>()
                    java.lang.String r3 = "HY_FAILED"
                    org.json.JSONArray r2 = r2.put(r3)
                    r0.addData((java.lang.String) r1, (org.json.JSONArray) r2)
                    java.lang.String r1 = "code"
                    java.lang.String r2 = "MtopJSBridge sendMtopRequest failed."
                    r0.addData((java.lang.String) r1, (java.lang.String) r2)
                    com.taobao.mtop.wvplugin.MtopBridge r1 = com.taobao.mtop.wvplugin.MtopBridge.this
                    r1.dispatchToMainThread(r0)
                L_0x00c1:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.mtop.wvplugin.MtopBridge.AnonymousClass2.run():void");
            }
        });
    }

    private class MtopBridgeListener implements IRemoteBaseListener {
        private Map<String, Object> jsParamMap;
        private WVCallBackContext wvCallBackContext;

        public MtopBridgeListener(WVCallBackContext wVCallBackContext, Map<String, Object> map) {
            this.wvCallBackContext = wVCallBackContext;
            this.jsParamMap = map;
        }

        public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
            MtopBridge.this.dispatchToMainThread(MtopBridge.this.parseResult(this.wvCallBackContext, mtopResponse, this.jsParamMap));
        }

        public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
            MtopBridge.this.dispatchToMainThread(MtopBridge.this.parseResult(this.wvCallBackContext, mtopResponse, this.jsParamMap));
        }

        public void onError(int i, MtopResponse mtopResponse, Object obj) {
            MtopBridge.this.dispatchToMainThread(MtopBridge.this.parseResult(this.wvCallBackContext, mtopResponse, this.jsParamMap));
        }
    }

    /* access modifiers changed from: private */
    public Map<String, Object> parseJSParams(FrontEndParams frontEndParams) {
        HashMap hashMap;
        Throwable th;
        boolean z;
        String str;
        int i;
        int i2;
        if (frontEndParams == null || StringUtils.isBlank(frontEndParams.jsParam)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(frontEndParams.jsParam);
            hashMap = new HashMap();
            try {
                hashMap.put("api", jSONObject.getString("api"));
                hashMap.put("v", jSONObject.optString("v", "*"));
                hashMap.put("data", jSONObject.optJSONObject("param"));
                if (!jSONObject.isNull("needLogin")) {
                    z = jSONObject.optBoolean("needLogin");
                } else {
                    z = jSONObject.optInt("ecode", 0) != 0;
                }
                String optString = jSONObject.optString(MtopJSBridge.MtopJSParam.SESSION_OPTION);
                hashMap.put("needLogin", Boolean.valueOf(z));
                hashMap.put(MtopJSBridge.MtopJSParam.SESSION_OPTION, optString);
                String str2 = "GET";
                if (!jSONObject.isNull("method")) {
                    str2 = jSONObject.optString("method");
                } else if (jSONObject.optInt("post", 0) != 0) {
                    str2 = "POST";
                }
                hashMap.put("method", str2);
                if (!jSONObject.isNull(MtopJSBridge.MtopJSParam.DATA_TYPE)) {
                    str = jSONObject.optString(MtopJSBridge.MtopJSParam.DATA_TYPE);
                } else {
                    str = jSONObject.optString("type");
                }
                hashMap.put(MtopJSBridge.MtopJSParam.DATA_TYPE, str);
                if (!jSONObject.isNull(MtopJSBridge.MtopJSParam.SEC_TYPE)) {
                    i = jSONObject.optInt(MtopJSBridge.MtopJSParam.SEC_TYPE);
                } else {
                    i = jSONObject.optInt("isSec", 0);
                }
                hashMap.put(MtopJSBridge.MtopJSParam.SEC_TYPE, Integer.valueOf(i));
                if (!jSONObject.isNull("timeout")) {
                    i2 = jSONObject.optInt("timeout", 20000);
                } else {
                    i2 = jSONObject.optInt(TimerJointPoint.TYPE, 20000);
                }
                if (i2 < 0) {
                    i2 = 20000;
                } else if (i2 > 60000) {
                    i2 = 60000;
                }
                hashMap.put("timeout", Integer.valueOf(i2));
                hashMap.put(MtopJSBridge.MtopJSParam.EXT_HEADERS, jSONObject.optJSONObject(MtopJSBridge.MtopJSParam.EXT_HEADERS));
                hashMap.put("user-agent", frontEndParams.userAgent);
                hashMap.put("ttid", jSONObject.optString("ttid"));
                hashMap.put(MtopJSBridge.MtopJSParam.PAGE_URL, frontEndParams.pageUrl);
                hashMap.put(MtopJSBridge.MtopJSParam.EXT_QUERYS, jSONObject.optJSONObject(MtopJSBridge.MtopJSParam.EXT_QUERYS));
                hashMap.put(MtopJSBridge.MtopJSParam.MP_HOST, jSONObject.optString(MtopJSBridge.MtopJSParam.MP_HOST));
                hashMap.put("x-ua", frontEndParams.userAgent);
            } catch (Throwable th2) {
                th = th2;
                TBSdkLog.e(TAG, "parseJSParams error.params =" + frontEndParams.jsParam, th);
                return hashMap;
            }
        } catch (Throwable th3) {
            hashMap = null;
            th = th3;
            TBSdkLog.e(TAG, "parseJSParams error.params =" + frontEndParams.jsParam, th);
            return hashMap;
        }
        return hashMap;
    }

    /* access modifiers changed from: private */
    public MtopResult parseResult(WVCallBackContext wVCallBackContext, MtopResponse mtopResponse, Map<String, Object> map) {
        if (wVCallBackContext == null) {
            TBSdkLog.e(TAG, "[parseResult]WVCallBackContext is null, webview may be destroyed , mtopJsParamsMap:" + map);
            return null;
        }
        long currentTimeMillis = System.currentTimeMillis();
        String str = map != null ? (String) map.get("api") : "";
        String str2 = map != null ? (String) map.get("v") : "";
        MtopResult mtopResult = new MtopResult(wVCallBackContext);
        if (mtopResponse == null) {
            TBSdkLog.e(TAG, "[parseResult]MP_TIME_OUT. mtopJsParamsMap:" + map);
            commitMtopJSStat(str, str2, "-1", MtopWVPlugin.TIME_OUT, (String) null);
            mtopResult.addData("code", "-1");
            mtopResult.addData("ret", new JSONArray().put(MtopWVPlugin.TIME_OUT));
            return mtopResult;
        }
        String valueOf = String.valueOf(mtopResponse.getResponseCode());
        mtopResult.addData("code", valueOf);
        if (mtopResponse.isSessionInvalid()) {
            commitMtopJSStat(str, str2, valueOf, "ERR_SID_INVALID", mtopResponse.getRetCode());
            mtopResult.addData("ret", new JSONArray().put("ERR_SID_INVALID"));
            return mtopResult;
        }
        mtopResult.addData("ret", new JSONArray().put("HY_FAILED"));
        try {
            if (mtopResponse.getBytedata() != null) {
                JSONObject jSONObject = new JSONObject(new String(mtopResponse.getBytedata(), "utf-8"));
                mtopResult.setData(jSONObject);
                jSONObject.put(EventConstants.Mtop.HEADERS, JSON.toJSONString(mtopResponse.getHeaderFields()));
                jSONObject.put("code", valueOf);
                jSONObject.put("isFromCache", mtopResponse.getSource() != MtopResponse.ResponseSource.NETWORK_REQUEST ? "1" : "0");
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject.put(UCCore.EVENT_STAT, jSONObject2);
                    MtopStatistics mtopStat = mtopResponse.getMtopStat();
                    if (mtopStat == null || mtopStat.getNetworkStats() == null) {
                        jSONObject2.put("oneWayTime", 0);
                        jSONObject2.put("recDataSize", 0);
                    } else {
                        NetworkStats networkStats = mtopStat.getNetworkStats();
                        jSONObject2.put("oneWayTime", networkStats.oneWayTime_ANet);
                        jSONObject2.put("recDataSize", networkStats.recvSize);
                    }
                } catch (Exception e) {
                    TBSdkLog.e(TAG, "[parseResult] parse network stats error" + e.toString());
                }
                commitMtopJSStat(str, str2, valueOf, mtopResponse.getRetCode(), mtopResponse.getRetCode());
            } else {
                mtopResult.addData(RETCODE, mtopResponse.getRetCode());
                commitMtopJSStat(str, str2, valueOf, "HY_FAILED", mtopResponse.getRetCode());
            }
            if (mtopResponse.isApiSuccess()) {
                mtopResult.setSuccess(true);
            }
        } catch (Exception unused) {
            if (TBSdkLog.isPrintLog()) {
                TBSdkLog.e(TAG, "[parseResult] mtop response parse fail, content: " + mtopResponse);
            }
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "parseResult cost time(ms):" + (System.currentTimeMillis() - currentTimeMillis));
        }
        return mtopResult;
    }

    public void commitMtopJSStat(String str, String str2, String str3, String str4, String str5) {
        try {
            IUploadStats iUploadStats = Mtop.instance(Mtop.Id.INNER, (Context) null).getMtopConfig().uploadStats;
            if (iUploadStats != null) {
                if (registerFlag.compareAndSet(false, true)) {
                    HashSet hashSet = new HashSet();
                    hashSet.add("api");
                    hashSet.add("v");
                    hashSet.add("ret");
                    hashSet.add("code");
                    hashSet.add(RETCODE);
                    iUploadStats.onRegister("mtopsdk", MONITOR_POINT, hashSet, (Set<String>) null, false);
                }
                HashMap hashMap = new HashMap();
                hashMap.put("api", str);
                hashMap.put("v", str2);
                hashMap.put("ret", str4);
                hashMap.put("code", str3);
                hashMap.put(RETCODE, str5);
                iUploadStats.onCommit("mtopsdk", MONITOR_POINT, hashMap, (Map<String, Double>) null);
            }
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "commitMtopJSStat error.", th);
        }
    }
}
