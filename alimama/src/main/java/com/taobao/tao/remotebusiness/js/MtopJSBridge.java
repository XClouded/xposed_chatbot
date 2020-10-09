package com.taobao.tao.remotebusiness.js;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.IRemoteCacheListener;
import com.taobao.tao.remotebusiness.IRemoteListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopCacheEvent;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.JsonTypeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;
import org.json.JSONArray;
import org.json.JSONObject;

public class MtopJSBridge {
    private static final String AUTO_LOGIN_ONLY = "AutoLoginOnly";
    private static final String AUTO_LOGIN_WITH_MANUAL = "AutoLoginAndManualLogin";
    private static final String DATA_TYPE_JSON = "json";
    private static final String DATA_TYPE_ORIGINAL_JSON = "originaljson";
    static final String TAG = "mtopsdk.MtopJSBridge";
    static volatile ScheduledExecutorService scheduledExecutorService;

    public interface MtopJSParam {
        public static final String API = "api";
        public static final String DATA = "data";
        public static final String DATA_TYPE = "dataType";
        public static final String EXT_HEADERS = "ext_headers";
        public static final String EXT_QUERYS = "ext_querys";
        public static final String METHOD = "method";
        public static final String MP_HOST = "mpHost";
        public static final String NEED_LOGIN = "needLogin";
        public static final String PAGE_URL = "pageUrl";
        public static final String SEC_TYPE = "secType";
        public static final String SESSION_OPTION = "sessionOption";
        public static final String TIMEOUT = "timeout";
        public static final String TTID = "ttid";
        @Deprecated
        public static final String USER_AGENT = "user-agent";
        public static final String V = "v";
        public static final String X_UA = "x-ua";

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    public static void sendMtopRequest(Map<String, Object> map, @NonNull IRemoteBaseListener iRemoteBaseListener) {
        if (iRemoteBaseListener == null) {
            TBSdkLog.e(TAG, "illegal param listener.");
        } else if (map == null || map.isEmpty()) {
            TBSdkLog.e(TAG, "illegal param jsParamMap.");
            iRemoteBaseListener.onSystemError(0, new MtopResponse(ErrorConstant.ERRCODE_ILLEGAL_JSPARAM_ERROR, ErrorConstant.ERRMSG_ILLEGAL_JSPARAM_ERROR), (Object) null);
        } else {
            MtopBusiness buildMtopBusiness = buildMtopBusiness(map);
            if (buildMtopBusiness == null) {
                iRemoteBaseListener.onSystemError(0, new MtopResponse(ErrorConstant.ERRCODE_PARSE_JSPARAM_ERROR, ErrorConstant.ERRMSG_PARSE_JSPARAM_ERROR), (Object) null);
                return;
            }
            int i = 20000;
            try {
                int intValue = ((Integer) map.get("timeout")).intValue();
                if (intValue >= 0) {
                    i = intValue > 60000 ? 60000 : intValue;
                }
            } catch (Exception unused) {
                TBSdkLog.e(TAG, "parse timeout (jsParam field) error.");
            }
            final MtopJSListener mtopJSListener = new MtopJSListener(buildMtopBusiness, iRemoteBaseListener);
            buildMtopBusiness.registerListener((IRemoteListener) mtopJSListener);
            buildMtopBusiness.startRequest();
            getScheduledExecutorService().schedule(new Runnable() {
                public void run() {
                    mtopJSListener.onTimeOut();
                }
            }, (long) i, TimeUnit.MILLISECONDS);
        }
    }

    private static MtopBusiness buildMtopBusiness(Map<String, Object> map) {
        MtopBusiness mtopBusiness;
        HashMap hashMap;
        HashMap hashMap2 = null;
        try {
            JSONObject jSONObject = new JSONObject(map);
            String string = jSONObject.getString("api");
            String optString = jSONObject.optString("v", "*");
            String str = "{}";
            JSONObject optJSONObject = jSONObject.optJSONObject("data");
            if (optJSONObject != null) {
                HashMap hashMap3 = new HashMap();
                Iterator<String> keys = optJSONObject.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    Object obj = optJSONObject.get(next);
                    hashMap3.put(next, obj.toString());
                    if (!(obj instanceof JSONArray) && !(obj instanceof JSONObject)) {
                        optJSONObject.put(next, obj.toString());
                    }
                }
                String jSONObject2 = optJSONObject.toString();
                hashMap = hashMap3;
                str = jSONObject2;
            } else {
                hashMap = null;
            }
            boolean optBoolean = jSONObject.optBoolean("needLogin", false);
            String optString2 = jSONObject.optString(MtopJSParam.SESSION_OPTION, AUTO_LOGIN_WITH_MANUAL);
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName(string);
            mtopRequest.setVersion(optString);
            mtopRequest.setNeedEcode(optBoolean);
            mtopRequest.setData(str);
            mtopRequest.dataParams = hashMap;
            mtopBusiness = MtopBusiness.build(mtopRequest);
            try {
                mtopBusiness.showLoginUI(!AUTO_LOGIN_ONLY.equalsIgnoreCase(optString2));
                if (MethodEnum.POST.getMethod().equalsIgnoreCase(jSONObject.optString("method", "GET"))) {
                    mtopBusiness.reqMethod(MethodEnum.POST);
                }
                String optString3 = jSONObject.optString(MtopJSParam.MP_HOST, "");
                if (StringUtils.isNotBlank(optString3)) {
                    mtopBusiness.setCustomDomain(optString3);
                }
                if (jSONObject.optInt(MtopJSParam.SEC_TYPE, 0) > 0) {
                    mtopBusiness.useWua();
                }
                String optString4 = jSONObject.optString(MtopJSParam.DATA_TYPE, "");
                if (!StringUtils.isBlank(optString4) && (DATA_TYPE_JSON.equals(optString4) || DATA_TYPE_ORIGINAL_JSON.equals(optString4))) {
                    mtopBusiness.setJsonType(JsonTypeEnum.valueOf(optString4.toUpperCase(Locale.US)));
                }
                JSONObject optJSONObject2 = jSONObject.optJSONObject(MtopJSParam.EXT_HEADERS);
                if (optJSONObject2 != null) {
                    hashMap2 = new HashMap();
                    Iterator<String> keys2 = optJSONObject2.keys();
                    while (keys2.hasNext()) {
                        String next2 = keys2.next();
                        String string2 = optJSONObject2.getString(next2);
                        if (!TextUtils.isEmpty(next2)) {
                            if (!TextUtils.isEmpty(string2)) {
                                hashMap2.put(next2, string2);
                            }
                        }
                    }
                }
                String optString5 = jSONObject.optString("x-ua");
                if (!StringUtils.isBlank(optString5)) {
                    if (hashMap2 == null) {
                        hashMap2 = new HashMap();
                    }
                    hashMap2.put("x-ua", URLEncoder.encode(optString5, "utf-8"));
                }
                mtopBusiness.headers((Map<String, String>) hashMap2);
                JSONObject optJSONObject3 = jSONObject.optJSONObject(MtopJSParam.EXT_QUERYS);
                if (optJSONObject3 != null) {
                    Iterator<String> keys3 = optJSONObject3.keys();
                    while (keys3.hasNext()) {
                        String next3 = keys3.next();
                        String string3 = optJSONObject3.getString(next3);
                        if (!TextUtils.isEmpty(next3)) {
                            if (!TextUtils.isEmpty(string3)) {
                                mtopBusiness.addHttpQueryParameter(next3, string3);
                            }
                        }
                    }
                }
                String optString6 = jSONObject.optString("ttid");
                if (!StringUtils.isBlank(optString6)) {
                    mtopBusiness.ttid(optString6);
                }
                String optString7 = jSONObject.optString(MtopJSParam.PAGE_URL);
                if (!StringUtils.isBlank(optString7)) {
                    mtopBusiness.setPageUrl(optString7);
                }
                mtopBusiness.setReqSource(1);
            } catch (Exception e) {
                e = e;
                TBSdkLog.e(TAG, "parse mtop jsParamMap error, jsParamMap=" + map, (Throwable) e);
                return mtopBusiness;
            }
        } catch (Exception e2) {
            e = e2;
            mtopBusiness = null;
            TBSdkLog.e(TAG, "parse mtop jsParamMap error, jsParamMap=" + map, (Throwable) e);
            return mtopBusiness;
        }
        return mtopBusiness;
    }

    static ScheduledExecutorService getScheduledExecutorService() {
        if (scheduledExecutorService == null) {
            synchronized (MtopJSBridge.class) {
                if (scheduledExecutorService == null) {
                    scheduledExecutorService = Executors.newScheduledThreadPool(1);
                }
            }
        }
        return scheduledExecutorService;
    }

    static class MtopJSListener implements IRemoteBaseListener, IRemoteCacheListener {
        private MtopResponse cachedResponse;
        private AtomicBoolean isFinish = new AtomicBoolean(false);
        final IRemoteBaseListener listener;
        private final MtopBusiness mtopBusiness;

        public MtopJSListener(MtopBusiness mtopBusiness2, IRemoteBaseListener iRemoteBaseListener) {
            this.mtopBusiness = mtopBusiness2;
            this.listener = iRemoteBaseListener;
        }

        public void onTimeOut() {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onTimeOut");
                }
                this.mtopBusiness.cancelRequest();
                try {
                    if (this.cachedResponse != null) {
                        this.listener.onSuccess(0, this.cachedResponse, (BaseOutDo) null, (Object) null);
                    } else {
                        this.listener.onSystemError(0, (MtopResponse) null, (Object) null);
                    }
                } catch (Exception e) {
                    TBSdkLog.e(MtopJSBridge.TAG, "do onTimeOut callback error.", (Throwable) e);
                }
            }
        }

        public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onSuccess");
                }
                final int i2 = i;
                final MtopResponse mtopResponse2 = mtopResponse;
                final BaseOutDo baseOutDo2 = baseOutDo;
                final Object obj2 = obj;
                MtopJSBridge.getScheduledExecutorService().submit(new Runnable() {
                    public void run() {
                        try {
                            MtopJSListener.this.listener.onSuccess(i2, mtopResponse2, baseOutDo2, obj2);
                        } catch (Exception e) {
                            TBSdkLog.e(MtopJSBridge.TAG, "do onSuccess callback error.", (Throwable) e);
                        }
                    }
                });
            }
        }

        public void onError(final int i, final MtopResponse mtopResponse, final Object obj) {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onError");
                }
                MtopJSBridge.getScheduledExecutorService().submit(new Runnable() {
                    public void run() {
                        try {
                            MtopJSListener.this.listener.onError(i, mtopResponse, obj);
                        } catch (Exception e) {
                            TBSdkLog.e(MtopJSBridge.TAG, "do onError callback error.", (Throwable) e);
                        }
                    }
                });
            }
        }

        public void onCached(MtopCacheEvent mtopCacheEvent, BaseOutDo baseOutDo, Object obj) {
            if (mtopCacheEvent != null) {
                this.cachedResponse = mtopCacheEvent.getMtopResponse();
            }
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(MtopJSBridge.TAG, "callback onCached");
            }
        }

        public void onSystemError(final int i, final MtopResponse mtopResponse, final Object obj) {
            if (this.isFinish.compareAndSet(false, true)) {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                    TBSdkLog.d(MtopJSBridge.TAG, "callback onSystemError");
                }
                MtopJSBridge.getScheduledExecutorService().submit(new Runnable() {
                    public void run() {
                        try {
                            MtopJSListener.this.listener.onSystemError(i, mtopResponse, obj);
                        } catch (Exception e) {
                            TBSdkLog.e(MtopJSBridge.TAG, "do onSystemError callback error.", (Throwable) e);
                        }
                    }
                });
            }
        }
    }
}
