package com.alibaba.aliweex.adapter.module.mtop;

import alimama.com.unwrouter.UNWRouter;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import anetwork.channel.statist.StatisticData;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.module.mtop.WXMtopModule;
import com.alibaba.aliweex.interceptor.mtop.MtopTracker;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.RemoteBusiness;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.http.WXHttpUtil;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.utils.WXLogUtils;
import com.uc.webview.export.extension.UCCore;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.JsonTypeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.global.SDKConfig;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONArray;

public class WXMtopRequest {
    private static final String AUTO_LOGIN_ONLY = "AutoLoginOnly";
    private static final String AUTO_LOGIN_WITH_MANUAL = "AutoLoginAndManualLogin";
    public static final String MSG_FAILED = "WX_FAILED";
    public static final String MSG_PARAM_ERR = "MSG_PARAM_ERR";
    public static final String MSG_SUCCESS = "WX_SUCCESS";
    private static final int NOTIFY_RESULT = 500;
    private static final String TAG = "WXMtopRequest";
    /* access modifiers changed from: private */
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public String instanceId;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            JSCallback jSCallback;
            if (message.what == 500 && (message.obj instanceof MtopResult)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(WXMtopRequest.TAG, "call result, retString: " + ((MtopResult) message.obj).toString());
                }
                try {
                    MtopResult mtopResult = (MtopResult) message.obj;
                    if (mtopResult.getCallback() != null && mtopResult.getResult() != null) {
                        JSONObject jSONObject = new JSONObject();
                        if (WXMtopRequest.this.version == WXMtopModule.MTOP_VERSION.V1) {
                            jSONObject.put("result", (Object) mtopResult.isSuccess() ? "WX_SUCCESS" : "WX_FAILED");
                            jSONObject.put("data", (Object) JSON.parseObject(mtopResult.toString()));
                            jSCallback = mtopResult.getCallback();
                        } else {
                            jSONObject = JSON.parseObject(mtopResult.toString());
                            if (mtopResult.isSuccess()) {
                                jSCallback = mtopResult.getCallback();
                            } else {
                                if (!jSONObject.containsKey("result")) {
                                    jSONObject.put("result", (Object) mtopResult.getRetCode());
                                }
                                jSCallback = mtopResult.getFailureCallback();
                            }
                        }
                        JSONObject jSONObject2 = jSONObject;
                        JSCallback jSCallback2 = jSCallback;
                        WXMtopRequest.this.recordMtopState("weex-mtop-end", (String) null, (String) null, (String) null, mtopResult);
                        if (jSCallback2 != null) {
                            jSCallback2.invoke(jSONObject2);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public MtopTracker mtopTracker;
    /* access modifiers changed from: private */
    public WXMtopModule.MTOP_VERSION version;

    public WXMtopRequest(WXMtopModule.MTOP_VERSION mtop_version) {
        if (WXEnvironment.isApkDebugable()) {
            this.mtopTracker = MtopTracker.newInstance();
        }
        this.version = mtop_version;
    }

    public WXMtopRequest setInstanceId(String str) {
        this.instanceId = str;
        return this;
    }

    /* access modifiers changed from: private */
    public void recordMtopState(String str, String str2, String str3, String str4, MtopResult mtopResult) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if ((configAdapter == null || Boolean.valueOf(configAdapter.getConfig(WXInitConfigManager.WXAPM_CONFIG_GROUP, "recordMtopState", "true")).booleanValue()) && AliWeex.getInstance().getGodEyeStageAdapter() != null) {
            HashMap hashMap = new HashMap();
            if (str3 != null) {
                hashMap.put("url", str3);
                WXStateRecord instance = WXStateRecord.getInstance();
                instance.recordAction("", "sendMtop:" + str3);
            }
            if (str2 != null) {
                hashMap.put(UNWRouter.PAGE_NAME, str2);
            }
            if (str4 != null) {
                hashMap.put("msg", str4);
            }
            if (mtopResult != null) {
                String str5 = mtopResult.callApi == null ? "" : mtopResult.callApi;
                hashMap.put("callApi", str5);
                hashMap.put("success", Boolean.valueOf(mtopResult.isSuccess()));
                hashMap.put("retCode", mtopResult.getRetCode());
                if (!mtopResult.isSuccess()) {
                    hashMap.put("result", mtopResult.getResult().toString());
                }
                WXStateRecord instance2 = WXStateRecord.getInstance();
                instance2.recordAction("", "receiveMtop:" + str5 + ",result" + mtopResult.getResult().toString());
            }
        }
    }

    public void send(Context context, String str, JSCallback jSCallback, JSCallback jSCallback2) {
        if (WXEnvironment.isApkDebugable()) {
            WXLogUtils.d("mtop send >>> " + str);
        }
        WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.instanceId);
        if (sDKInstance != null) {
            sDKInstance.getApmForInstance().actionNetRequest();
        }
        final String str2 = str;
        final JSCallback jSCallback3 = jSCallback;
        final JSCallback jSCallback4 = jSCallback2;
        final Context context2 = context;
        scheduledExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    org.json.JSONObject jSONObject = new org.json.JSONObject(str2);
                    MtopServerParams access$200 = WXMtopRequest.this.parseParams(jSONObject);
                    if (access$200 == null) {
                        MtopResult mtopResult = new MtopResult(jSCallback3, jSCallback4);
                        mtopResult.addData("ret", new JSONArray().put("HY_PARAM_ERR"));
                        WXMtopRequest.this.dispatchToMainThread(mtopResult);
                        return;
                    }
                    WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(WXMtopRequest.this.instanceId);
                    if (sDKInstance != null) {
                        try {
                            XState.setValue(XStateConstants.KEY_CURRENT_PAGE_NAME, sDKInstance.getApmForInstance().reportPageName);
                            XState.setValue(XStateConstants.KEY_CURRENT_PAGE_URL, sDKInstance.getBundleUrl());
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                    MtopRequest access$400 = WXMtopRequest.this.buildRequest(access$200);
                    WXMtopRequest.this.recordMtopState("weex-send-mtop", sDKInstance == null ? "" : sDKInstance.getWXPerformance().pageName, access$400.getApiName(), access$400.getVersion(), (MtopResult) null);
                    String optString = jSONObject.optString("userAgent");
                    if (TextUtils.isEmpty(optString)) {
                        optString = WXHttpUtil.assembleUserAgent(context2, WXEnvironment.getConfig());
                    }
                    RemoteBusiness access$500 = WXMtopRequest.this.buildRemoteBusiness(access$400, access$200, optString);
                    if (WXMtopRequest.this.mtopTracker != null) {
                        WXMtopRequest.this.mtopTracker.preRequest(access$500);
                    }
                    RbListener rbListener = new RbListener(WXMtopRequest.this.mtopTracker, jSCallback3, jSCallback4, access$500, access$200.timer);
                    rbListener.instanceId = WXMtopRequest.this.instanceId;
                    rbListener.requestAi = access$400.getApiName();
                    access$500.registeListener((IRemoteListener) rbListener);
                    access$500.startRequest();
                } catch (Exception e) {
                    TBSdkLog.e(WXMtopRequest.TAG, "send Request failed" + e);
                    MtopResult mtopResult2 = new MtopResult(jSCallback3, jSCallback4);
                    mtopResult2.addData("ret", new JSONArray().put("HY_FAILED"));
                    WXMtopRequest.this.dispatchToMainThread(mtopResult2);
                }
            }
        });
    }

    public void request(Context context, JSONObject jSONObject, JSCallback jSCallback, JSCallback jSCallback2) {
        send(context, jSONObject.toString(), jSCallback, jSCallback2);
    }

    /* access modifiers changed from: private */
    public void dispatchToMainThread(MtopResult mtopResult) {
        this.mHandler.obtainMessage(500, mtopResult).sendToTarget();
    }

    /* access modifiers changed from: private */
    public MtopRequest buildRequest(MtopServerParams mtopServerParams) {
        MtopRequest mtopRequest = new MtopRequest();
        mtopRequest.setApiName(mtopServerParams.api);
        mtopRequest.setVersion(mtopServerParams.v);
        mtopRequest.setNeedEcode(mtopServerParams.ecode);
        mtopRequest.setNeedSession(true);
        if (StringUtils.isNotBlank(mtopServerParams.dataString)) {
            mtopRequest.setData(mtopServerParams.dataString);
        }
        mtopRequest.dataParams = mtopServerParams.getDataMap();
        return mtopRequest;
    }

    /* access modifiers changed from: private */
    public RemoteBusiness buildRemoteBusiness(MtopRequest mtopRequest, MtopServerParams mtopServerParams, String str) {
        RemoteBusiness build = RemoteBusiness.build(mtopRequest, StringUtils.isBlank(mtopServerParams.ttid) ? SDKConfig.getInstance().getGlobalTtid() : mtopServerParams.ttid);
        build.showLoginUI(!mtopServerParams.sessionOption.equals(AUTO_LOGIN_ONLY));
        build.protocol(ProtocolEnum.HTTP);
        if ("true".equals(WXInitConfigManager.getInstance().getConfigKVFirstValue(WXInitConfigManager.getInstance().c_enable_mtop_cache))) {
            build.useCache();
        }
        if (mtopServerParams.wuaFlag > 0) {
            build.useWua();
        }
        build.reqMethod(mtopServerParams.post ? MethodEnum.POST : MethodEnum.GET);
        if (mtopServerParams.getHeaders() != null) {
            build.headers(mtopServerParams.getHeaders());
        }
        if (StringUtils.isNotBlank(str)) {
            HashMap hashMap = new HashMap();
            hashMap.put("x-ua", str);
            build.headers((Map<String, String>) hashMap);
        }
        if (!StringUtils.isBlank(mtopServerParams.type) && ("json".equals(mtopServerParams.type) || "originaljson".equals(mtopServerParams.type))) {
            build.setJsonType(JsonTypeEnum.valueOf(mtopServerParams.type.toUpperCase()));
        }
        return build;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0073 A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x007a A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009e A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a5 A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00ba A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00c4 A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00e1 A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00e8 A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00ed A[Catch:{ JSONException -> 0x014c }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0126 A[Catch:{ JSONException -> 0x014c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.alibaba.aliweex.adapter.module.mtop.MtopServerParams parseParams(org.json.JSONObject r7) {
        /*
            r6 = this;
            com.alibaba.aliweex.adapter.module.mtop.MtopServerParams r0 = new com.alibaba.aliweex.adapter.module.mtop.MtopServerParams     // Catch:{ JSONException -> 0x014c }
            r0.<init>()     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "api"
            java.lang.String r1 = r7.getString(r1)     // Catch:{ JSONException -> 0x014c }
            r0.api = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "v"
            java.lang.String r2 = "*"
            java.lang.String r1 = r7.optString(r1, r2)     // Catch:{ JSONException -> 0x014c }
            r0.v = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "type"
            java.lang.String r1 = r7.optString(r1)     // Catch:{ JSONException -> 0x014c }
            java.lang.String r2 = "GET"
            boolean r2 = r2.equalsIgnoreCase(r1)     // Catch:{ JSONException -> 0x014c }
            r3 = 1
            r4 = 0
            if (r2 != 0) goto L_0x0051
            java.lang.String r2 = "POST"
            boolean r1 = r2.equalsIgnoreCase(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x0030
            goto L_0x0051
        L_0x0030:
            java.lang.String r1 = "post"
            java.lang.Object r1 = r7.opt(r1)     // Catch:{ JSONException -> 0x014c }
            boolean r2 = r1 instanceof java.lang.Boolean     // Catch:{ JSONException -> 0x014c }
            if (r2 == 0) goto L_0x0043
            java.lang.Boolean r1 = (java.lang.Boolean) r1     // Catch:{ JSONException -> 0x014c }
            boolean r1 = r1.booleanValue()     // Catch:{ JSONException -> 0x014c }
            r0.post = r1     // Catch:{ JSONException -> 0x014c }
            goto L_0x0061
        L_0x0043:
            java.lang.String r1 = "post"
            int r1 = r7.optInt(r1, r4)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x004d
            r1 = 1
            goto L_0x004e
        L_0x004d:
            r1 = 0
        L_0x004e:
            r0.post = r1     // Catch:{ JSONException -> 0x014c }
            goto L_0x0061
        L_0x0051:
            java.lang.String r1 = "POST"
            java.lang.String r2 = "type"
            java.lang.String r5 = "GET"
            java.lang.String r2 = r7.optString(r2, r5)     // Catch:{ JSONException -> 0x014c }
            boolean r1 = r1.equalsIgnoreCase(r2)     // Catch:{ JSONException -> 0x014c }
            r0.post = r1     // Catch:{ JSONException -> 0x014c }
        L_0x0061:
            java.lang.String r1 = "dataType"
            java.lang.String r2 = "originaljson"
            java.lang.String r1 = r7.optString(r1, r2)     // Catch:{ JSONException -> 0x014c }
            r0.type = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "needLogin"
            boolean r1 = r7.has(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x007a
            java.lang.String r1 = "needLogin"
            boolean r1 = r7.optBoolean(r1, r4)     // Catch:{ JSONException -> 0x014c }
            goto L_0x0094
        L_0x007a:
            java.lang.String r1 = "loginRequest"
            boolean r1 = r7.has(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x0089
            java.lang.String r1 = "loginRequest"
            boolean r1 = r7.optBoolean(r1, r4)     // Catch:{ JSONException -> 0x014c }
            goto L_0x0094
        L_0x0089:
            java.lang.String r1 = "ecode"
            int r1 = r7.optInt(r1, r4)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x0093
            r1 = 1
            goto L_0x0094
        L_0x0093:
            r1 = 0
        L_0x0094:
            r0.ecode = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "secType"
            boolean r1 = r7.has(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 != 0) goto L_0x00a5
            java.lang.String r1 = "isSec"
        L_0x00a0:
            int r1 = r7.optInt(r1, r4)     // Catch:{ JSONException -> 0x014c }
            goto L_0x00a8
        L_0x00a5:
            java.lang.String r1 = "secType"
            goto L_0x00a0
        L_0x00a8:
            r0.wuaFlag = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "ttid"
            java.lang.String r1 = r7.optString(r1)     // Catch:{ JSONException -> 0x014c }
            r0.ttid = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "timeout"
            boolean r1 = r7.has(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 != 0) goto L_0x00c4
            java.lang.String r1 = "timer"
            r2 = 500(0x1f4, float:7.0E-43)
            int r1 = r7.optInt(r1, r2)     // Catch:{ JSONException -> 0x014c }
        L_0x00c2:
            long r1 = (long) r1     // Catch:{ JSONException -> 0x014c }
            goto L_0x00cd
        L_0x00c4:
            java.lang.String r1 = "timeout"
            r2 = 20000(0x4e20, float:2.8026E-41)
            int r1 = r7.optInt(r1, r2)     // Catch:{ JSONException -> 0x014c }
            goto L_0x00c2
        L_0x00cd:
            r0.timer = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "sessionOption"
            java.lang.String r2 = "AutoLoginAndManualLogin"
            java.lang.String r1 = r7.optString(r1, r2)     // Catch:{ JSONException -> 0x014c }
            r0.sessionOption = r1     // Catch:{ JSONException -> 0x014c }
            java.lang.String r1 = "data"
            org.json.JSONObject r1 = r7.optJSONObject(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x00e8
            java.lang.String r1 = "data"
        L_0x00e3:
            org.json.JSONObject r1 = r7.optJSONObject(r1)     // Catch:{ JSONException -> 0x014c }
            goto L_0x00eb
        L_0x00e8:
            java.lang.String r1 = "param"
            goto L_0x00e3
        L_0x00eb:
            if (r1 == 0) goto L_0x011e
            java.util.Iterator r2 = r1.keys()     // Catch:{ JSONException -> 0x014c }
        L_0x00f1:
            boolean r3 = r2.hasNext()     // Catch:{ JSONException -> 0x014c }
            if (r3 == 0) goto L_0x0118
            java.lang.Object r3 = r2.next()     // Catch:{ JSONException -> 0x014c }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ JSONException -> 0x014c }
            java.lang.Object r4 = r1.get(r3)     // Catch:{ JSONException -> 0x014c }
            java.lang.String r5 = r4.toString()     // Catch:{ JSONException -> 0x014c }
            r0.addData(r3, r5)     // Catch:{ JSONException -> 0x014c }
            boolean r5 = r4 instanceof org.json.JSONArray     // Catch:{ JSONException -> 0x014c }
            if (r5 != 0) goto L_0x00f1
            boolean r5 = r4 instanceof org.json.JSONObject     // Catch:{ JSONException -> 0x014c }
            if (r5 != 0) goto L_0x00f1
            java.lang.String r4 = r4.toString()     // Catch:{ JSONException -> 0x014c }
            r1.put(r3, r4)     // Catch:{ JSONException -> 0x014c }
            goto L_0x00f1
        L_0x0118:
            java.lang.String r1 = r1.toString()     // Catch:{ JSONException -> 0x014c }
            r0.dataString = r1     // Catch:{ JSONException -> 0x014c }
        L_0x011e:
            java.lang.String r1 = "ext_headers"
            org.json.JSONObject r1 = r7.optJSONObject(r1)     // Catch:{ JSONException -> 0x014c }
            if (r1 == 0) goto L_0x014b
            java.util.Iterator r2 = r1.keys()     // Catch:{ JSONException -> 0x014c }
        L_0x012a:
            boolean r3 = r2.hasNext()     // Catch:{ JSONException -> 0x014c }
            if (r3 == 0) goto L_0x014b
            java.lang.Object r3 = r2.next()     // Catch:{ JSONException -> 0x014c }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ JSONException -> 0x014c }
            java.lang.String r4 = r1.getString(r3)     // Catch:{ JSONException -> 0x014c }
            boolean r5 = android.text.TextUtils.isEmpty(r3)     // Catch:{ JSONException -> 0x014c }
            if (r5 != 0) goto L_0x012a
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ JSONException -> 0x014c }
            if (r5 == 0) goto L_0x0147
            goto L_0x012a
        L_0x0147:
            r0.addHeader(r3, r4)     // Catch:{ JSONException -> 0x014c }
            goto L_0x012a
        L_0x014b:
            return r0
        L_0x014c:
            java.lang.String r0 = "WXMtopRequest"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "parseParams error, param="
            r1.append(r2)
            java.lang.String r7 = r7.toString()
            r1.append(r7)
            java.lang.String r7 = r1.toString()
            mtopsdk.common.util.TBSdkLog.e(r0, r7)
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest.parseParams(org.json.JSONObject):com.alibaba.aliweex.adapter.module.mtop.MtopServerParams");
    }

    /* access modifiers changed from: private */
    public MtopResult parseResult(JSCallback jSCallback, JSCallback jSCallback2, MtopResponse mtopResponse) {
        long currentTimeMillis = System.currentTimeMillis();
        MtopResult mtopResult = new MtopResult(jSCallback, jSCallback2);
        if (mtopResponse != null) {
            mtopResult.callApi = mtopResponse.getApi();
        }
        mtopResult.addData("ret", new JSONArray().put("HY_FAILED"));
        if (mtopResponse == null) {
            mtopResult.addData("code", "-1");
            TBSdkLog.d(TAG, "parseResult: time out");
            return mtopResult;
        }
        mtopResult.addData("code", String.valueOf(mtopResponse.getResponseCode()));
        if (mtopResponse.isSessionInvalid()) {
            mtopResult.addData("ret", new JSONArray().put("ERR_SID_INVALID"));
            return mtopResult;
        }
        try {
            if (mtopResponse.getBytedata() != null) {
                org.json.JSONObject jSONObject = new org.json.JSONObject(new String(mtopResponse.getBytedata(), "utf-8"));
                jSONObject.put("code", String.valueOf(mtopResponse.getResponseCode()));
                org.json.JSONObject jSONObject2 = new org.json.JSONObject();
                if (mtopResponse.getMtopStat() == null || mtopResponse.getMtopStat().getNetStat() == null) {
                    jSONObject2.put("oneWayTime", 0);
                    jSONObject2.put("recDataSize", 0);
                } else {
                    StatisticData netStat = mtopResponse.getMtopStat().getNetStat();
                    jSONObject2.put("oneWayTime", netStat.oneWayTime_AEngine);
                    jSONObject2.put("recDataSize", netStat.totalSize);
                }
                jSONObject.put(UCCore.EVENT_STAT, jSONObject2);
                mtopResult.setData(jSONObject);
            }
            if (mtopResponse.isApiSuccess()) {
                mtopResult.setSuccess(true);
            } else {
                mtopResult.setRetCode(mtopResponse.getRetCode());
            }
        } catch (Exception unused) {
            if (TBSdkLog.isPrintLog()) {
                TBSdkLog.e(TAG, "parseResult mtop response parse fail, content: " + mtopResponse.toString());
            }
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "parseResult cost time(ms):" + (System.currentTimeMillis() - currentTimeMillis));
        }
        return mtopResult;
    }

    private class RbListener implements IRemoteListener, IRemoteCacheListener {
        private MtopResponse cachedResponse;
        /* access modifiers changed from: private */
        public JSCallback callback;
        /* access modifiers changed from: private */
        public JSCallback failure;
        public String instanceId;
        private boolean isFinish = false;
        private boolean isTimeout = false;
        /* access modifiers changed from: private */
        public MtopTracker mtopTracker;
        private WeakReference<RemoteBusiness> rbWeakRef;
        public String requestAi;
        private long timer;

        public RbListener(MtopTracker mtopTracker2, JSCallback jSCallback, JSCallback jSCallback2, RemoteBusiness remoteBusiness, long j) {
            this.mtopTracker = mtopTracker2;
            this.callback = jSCallback;
            this.failure = jSCallback2;
            this.timer = j;
            this.rbWeakRef = new WeakReference<>(remoteBusiness);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0059, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public synchronized void onTimeOut() {
            /*
                r5 = this;
                monitor-enter(r5)
                boolean r0 = r5.isFinish     // Catch:{ all -> 0x005a }
                if (r0 == 0) goto L_0x0007
                monitor-exit(r5)
                return
            L_0x0007:
                mtopsdk.common.util.TBSdkLog$LogEnable r0 = mtopsdk.common.util.TBSdkLog.LogEnable.DebugEnable     // Catch:{ all -> 0x005a }
                boolean r0 = mtopsdk.common.util.TBSdkLog.isLogEnable(r0)     // Catch:{ all -> 0x005a }
                if (r0 == 0) goto L_0x0016
                java.lang.String r0 = "WXMtopRequest"
                java.lang.String r1 = "callback onTimeOut"
                mtopsdk.common.util.TBSdkLog.d(r0, r1)     // Catch:{ all -> 0x005a }
            L_0x0016:
                r0 = 1
                r5.isTimeout = r0     // Catch:{ all -> 0x005a }
                java.lang.ref.WeakReference<com.taobao.tao.remotebusiness.RemoteBusiness> r0 = r5.rbWeakRef     // Catch:{ all -> 0x005a }
                java.lang.Object r0 = r0.get()     // Catch:{ all -> 0x005a }
                com.taobao.tao.remotebusiness.RemoteBusiness r0 = (com.taobao.tao.remotebusiness.RemoteBusiness) r0     // Catch:{ all -> 0x005a }
                if (r0 == 0) goto L_0x0026
                r0.cancelRequest()     // Catch:{ all -> 0x005a }
            L_0x0026:
                com.alibaba.aliweex.interceptor.mtop.MtopTracker r0 = r5.mtopTracker     // Catch:{ all -> 0x005a }
                if (r0 == 0) goto L_0x0031
                com.alibaba.aliweex.interceptor.mtop.MtopTracker r0 = r5.mtopTracker     // Catch:{ all -> 0x005a }
                mtopsdk.mtop.domain.MtopResponse r1 = r5.cachedResponse     // Catch:{ all -> 0x005a }
                r0.onResponse((mtopsdk.mtop.domain.MtopResponse) r1)     // Catch:{ all -> 0x005a }
            L_0x0031:
                com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest r0 = com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest.this     // Catch:{ all -> 0x005a }
                com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest r1 = com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest.this     // Catch:{ all -> 0x005a }
                com.taobao.weex.bridge.JSCallback r2 = r5.callback     // Catch:{ all -> 0x005a }
                com.taobao.weex.bridge.JSCallback r3 = r5.failure     // Catch:{ all -> 0x005a }
                mtopsdk.mtop.domain.MtopResponse r4 = r5.cachedResponse     // Catch:{ all -> 0x005a }
                com.alibaba.aliweex.adapter.module.mtop.MtopResult r1 = r1.parseResult(r2, r3, r4)     // Catch:{ all -> 0x005a }
                r0.dispatchToMainThread(r1)     // Catch:{ all -> 0x005a }
                com.taobao.weex.WXSDKManager r0 = com.taobao.weex.WXSDKManager.getInstance()     // Catch:{ all -> 0x005a }
                java.lang.String r1 = r5.instanceId     // Catch:{ all -> 0x005a }
                com.taobao.weex.WXSDKInstance r0 = r0.getSDKInstance(r1)     // Catch:{ all -> 0x005a }
                if (r0 == 0) goto L_0x0058
                com.taobao.weex.performance.WXInstanceApm r0 = r0.getApmForInstance()     // Catch:{ all -> 0x005a }
                r1 = 0
                java.lang.String r2 = "onTimeOut"
                r0.actionNetResult(r1, r2)     // Catch:{ all -> 0x005a }
            L_0x0058:
                monitor-exit(r5)
                return
            L_0x005a:
                r0 = move-exception
                monitor-exit(r5)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.module.mtop.WXMtopRequest.RbListener.onTimeOut():void");
        }

        public synchronized void onSuccess(int i, final MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
            if (mtopResponse != null) {
                if (!this.isTimeout) {
                    WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.instanceId);
                    if (sDKInstance != null) {
                        sDKInstance.getApmForInstance().actionNetResult(true, (String) null);
                    }
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(WXMtopRequest.TAG, "RemoteBusiness callback onSuccess");
                    }
                    this.isFinish = true;
                    WXMtopRequest.scheduledExecutorService.submit(new Runnable() {
                        public void run() {
                            if (RbListener.this.mtopTracker != null) {
                                RbListener.this.mtopTracker.onResponse(mtopResponse);
                            }
                            WXMtopRequest.this.dispatchToMainThread(WXMtopRequest.this.parseResult(RbListener.this.callback, RbListener.this.failure, mtopResponse));
                        }
                    });
                }
            }
        }

        public synchronized void onError(int i, final MtopResponse mtopResponse, Object obj) {
            if (mtopResponse != null) {
                if (!this.isTimeout) {
                    WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(this.instanceId);
                    if (sDKInstance != null) {
                        sDKInstance.getApmForInstance().actionNetResult(false, (String) null);
                    }
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(WXMtopRequest.TAG, "RemoteBusiness callback onError");
                    }
                    this.isFinish = true;
                    WXMtopRequest.scheduledExecutorService.submit(new Runnable() {
                        public void run() {
                            MtopResult access$700 = WXMtopRequest.this.parseResult(RbListener.this.callback, RbListener.this.failure, mtopResponse);
                            if (RbListener.this.mtopTracker != null) {
                                RbListener.this.mtopTracker.onFailed(mtopResponse.getApi(), access$700.toString());
                            }
                            WXMtopRequest.this.dispatchToMainThread(access$700);
                        }
                    });
                }
            }
        }

        public synchronized void onCached(MtopCacheEvent mtopCacheEvent, BaseOutDo baseOutDo, Object obj) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(WXMtopRequest.TAG, "RemoteBusiness callback onCached");
            }
            if (mtopCacheEvent != null) {
                this.cachedResponse = mtopCacheEvent.getMtopResponse();
                WXMtopRequest.scheduledExecutorService.schedule(new Runnable() {
                    public void run() {
                        RbListener.this.onTimeOut();
                    }
                }, this.timer, TimeUnit.MILLISECONDS);
            }
        }
    }
}
