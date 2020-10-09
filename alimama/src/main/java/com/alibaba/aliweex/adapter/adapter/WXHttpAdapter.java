package com.alibaba.aliweex.adapter.adapter;

import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.taobao.windvane.cache.WVMemoryCache;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.zipapp.ZCacheResourceResponse;
import android.taobao.windvane.thread.WVThreadPool;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.RestrictTo;
import anet.channel.bytes.ByteArray;
import anet.channel.request.ByteArrayEntry;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.Request;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import anetwork.channel.interceptor.InterceptorManager;
import anetwork.channel.statist.StatisticData;
import anetwork.channel.util.RequestConstant;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.adapter.IGodEyeStageAdapter;
import com.alibaba.aliweex.adapter.module.net.IWXConnection;
import com.alibaba.aliweex.adapter.module.net.WXConnectionFactory;
import com.alibaba.aliweex.interceptor.network.NetworkTracker;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.aliweex.utils.WXUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.auth.mobile.common.AlipayAuthConstant;
import com.taobao.android.task.Coordinator;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.common.WXPerformance;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Future;
import mtopsdk.common.util.SymbolExpUtil;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class WXHttpAdapter implements IWXHttpAdapter {
    public static final String GROUP_CACHE_SWITCH = "weex_degrade_cache_switch";
    public static final String KEY_CACHE_SWITCH = "cache_switch";
    private static final String TAG = "TBWXHttpAdapter";
    private static final String WX_NETWORK_ORANGE_GROUP = "wx_network_ctl_android";
    private static String WX_NETWORK_SWITCH_CONTENT_LENGTH = "wx_network_ctl_android";
    private final String WX_NETWORK_TIMEOUT_MS = "wx_network_timeout_ms";
    /* access modifiers changed from: private */
    public DebugInterceptor mDebugInterceptor;

    public void sendRequest(WXRequest wXRequest, IWXHttpAdapter.OnHttpListener onHttpListener) {
        if (onHttpListener != null && wXRequest != null) {
            WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(wXRequest.instanceId);
            if (sDKInstance != null) {
                sDKInstance.getApmForInstance().actionNetRequest();
            }
            if (WXUtil.isAirGrey()) {
                wXRequest.paramMap.put("x-air-grey", "true");
            }
            final NetworkTracker newInstance = WXEnvironment.isApkDebugable() ? NetworkTracker.newInstance() : null;
            if (WXEnvironment.isApkDebugable() && this.mDebugInterceptor == null) {
                try {
                    this.mDebugInterceptor = new DebugInterceptor();
                    InterceptorManager.addInterceptor(this.mDebugInterceptor);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            onHttpListener.onHttpStart();
            final WXResponse wXResponse = new WXResponse();
            if (wXResponse.extendParams == null) {
                wXResponse.extendParams = new HashMap();
            }
            Log.w("test->", "sendRequest" + wXRequest.url);
            if (TextUtils.isEmpty(wXRequest.url)) {
                wXResponse.statusCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
                wXResponse.errorMsg = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorMsg() + "request url is empty!";
                onHttpListener.onHttpFinish(wXResponse);
                return;
            }
            recordRequestState("request->zcache", wXRequest.url, wXRequest, (WXResponse) null, (Map<String, List<String>>) null);
            WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(wXRequest.instanceId);
            if (wXSDKInstance == null || !wXSDKInstance.isPreDownLoad() || Looper.myLooper() != Looper.getMainLooper()) {
                long currentTimeMillis = System.currentTimeMillis();
                getResponseByPackageApp(wXRequest, wXResponse);
                wXResponse.extendParams.put("packageSpendTime", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                processResponse(wXRequest, wXResponse, onHttpListener, newInstance);
                return;
            }
            final WXRequest wXRequest2 = wXRequest;
            final IWXHttpAdapter.OnHttpListener onHttpListener2 = onHttpListener;
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    long currentTimeMillis = System.currentTimeMillis();
                    WXHttpAdapter.this.getResponseByPackageApp(wXRequest2, wXResponse);
                    wXResponse.extendParams.put("packageSpendTime", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
                            WXHttpAdapter.this.processResponse(wXRequest2, wXResponse, onHttpListener2, newInstance);
                        }
                    });
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void processResponse(WXRequest wXRequest, WXResponse wXResponse, IWXHttpAdapter.OnHttpListener onHttpListener, NetworkTracker networkTracker) {
        String trim = wXRequest.url.trim();
        Uri parse = Uri.parse(trim);
        if (TextUtils.equals(AlipayAuthConstant.LoginResult.SUCCESS, wXResponse.statusCode)) {
            recordRequestState("request->zcache->end", wXRequest.url, wXRequest, wXResponse, (Map<String, List<String>>) null);
            wXResponse.extendParams.put(WXInstanceApm.KEY_PAGE_PROPERTIES_REQUEST_TYPE, "zcache");
            wXResponse.extendParams.put("requestType", "cache");
            wXResponse.extendParams.put(WXPerformance.CACHE_TYPE, "zcache");
            processZCache(trim, wXResponse, onHttpListener);
            return;
        }
        processHttp(trim, parse, wXRequest, wXResponse, onHttpListener, networkTracker);
    }

    /* access modifiers changed from: protected */
    public void processHttp(String str, Uri uri, WXRequest wXRequest, WXResponse wXResponse, IWXHttpAdapter.OnHttpListener onHttpListener, NetworkTracker networkTracker) {
        if (!AlipayAuthConstant.LoginResult.SUCCESS.equals(wXResponse.statusCode)) {
            recordRequestState("request-> network", wXRequest.url, wXRequest, (WXResponse) null, (Map<String, List<String>>) null);
            sendRequestByHttp(networkTracker, wXRequest, wXResponse, onHttpListener);
            return;
        }
        wXResponse.extendParams.put(WXInstanceApm.KEY_PAGE_PROPERTIES_REQUEST_TYPE, "weex_cache");
        wXResponse.extendParams.put("requestType", "cache");
        wXResponse.extendParams.put(WXPerformance.CACHE_TYPE, "weex_cache");
    }

    /* access modifiers changed from: protected */
    public void processZCache(String str, WXResponse wXResponse, IWXHttpAdapter.OnHttpListener onHttpListener) {
        Log.w("test->", "processZCache");
        onHttpListener.onHttpFinish(wXResponse);
    }

    /* access modifiers changed from: protected */
    public void onHttpFinish(IWXHttpAdapter.OnHttpListener onHttpListener, String str, WXResponse wXResponse, int i, Map<String, List<String>> map) {
        recordRequestState("weex->onHttpFinish", str, (WXRequest) null, wXResponse, map);
        onHttpListener.onHttpFinish(wXResponse);
    }

    private void recordRequestState(String str, String str2, WXRequest wXRequest, WXResponse wXResponse, Map<String, List<String>> map) {
        IGodEyeStageAdapter godEyeStageAdapter;
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if ((configAdapter == null || Boolean.valueOf(configAdapter.getConfig(WXInitConfigManager.WXAPM_CONFIG_GROUP, "recordRequestState", "true")).booleanValue()) && (godEyeStageAdapter = AliWeex.getInstance().getGodEyeStageAdapter()) != null) {
            try {
                HashMap hashMap = new HashMap();
                if (!TextUtils.isEmpty(str2)) {
                    hashMap.put("url", str2);
                }
                if (wXRequest != null) {
                    hashMap.put("timeoutValue", Integer.valueOf(wXRequest.timeoutMs));
                }
                if (wXResponse != null) {
                    hashMap.put("response.statusCode", wXResponse.statusCode);
                    hashMap.put("response.extendParams", wXResponse.extendParams);
                }
                if (map != null) {
                    hashMap.put("response.header", map);
                }
                godEyeStageAdapter.onStage(str, hashMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public WXResponse getResponseByPackageApp(WXRequest wXRequest, WXResponse wXResponse) {
        Log.w("test->", "start getResponseByPackageApp");
        wXResponse.statusCode = "-1";
        String str = "";
        String trim = wXRequest.url.trim();
        try {
            Uri parse = Uri.parse(trim);
            if (parse.getBooleanQueryParameter("wh_weex", false)) {
                String host = parse.getHost();
                trim = trim.replace(host, parse.getHost() + ".local.weex");
            }
            ZCacheResourceResponse zCacheResourceResponse = WVPackageAppRuntime.getZCacheResourceResponse(trim);
            if (zCacheResourceResponse != null) {
                if (zCacheResourceResponse.headers != null) {
                    wXResponse.extendParams.put("zCacheInfo", zCacheResourceResponse.headers.get("X-ZCache-Info"));
                }
                if (zCacheResourceResponse.inputStream != null) {
                    str = readStringAndClose(zCacheResourceResponse.inputStream);
                }
            } else {
                wXResponse.extendParams.put("zCacheInfo", com.taobao.zcache.model.ZCacheResourceResponse.ZCACHE_NO_RESPONSE);
            }
        } catch (Exception e) {
            WXLogUtils.e("getResponseByPackageApp error:" + e.getMessage());
        }
        if (TextUtils.isEmpty(str)) {
            return wXResponse;
        }
        wXResponse.statusCode = AlipayAuthConstant.LoginResult.SUCCESS;
        wXResponse.originalData = str.getBytes();
        wXResponse.extendParams.put("connectionType", "packageApp");
        Log.w("test->", "end getResponseByPackageApp");
        return wXResponse;
    }

    /* JADX INFO: finally extract failed */
    private String readStringAndClose(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    String byteArrayOutputStream2 = byteArrayOutputStream.toString("UTF-8");
                    closeIO(inputStream);
                    return byteArrayOutputStream2;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeIO(inputStream);
            return null;
        } catch (Throwable th) {
            closeIO(inputStream);
            throw th;
        }
    }

    public void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception unused) {
            }
        }
    }

    private void sendRequestByHttp(NetworkTracker networkTracker, WXRequest wXRequest, WXResponse wXResponse, IWXHttpAdapter.OnHttpListener onHttpListener) {
        final WXRequest wXRequest2 = wXRequest;
        final WXResponse wXResponse2 = wXResponse;
        final NetworkTracker networkTracker2 = networkTracker;
        final IWXHttpAdapter.OnHttpListener onHttpListener2 = onHttpListener;
        Coordinator.postTask(new Coordinator.TaggedRunnable(TAG) {
            public void run() {
                WXSDKInstance sDKInstance = WXSDKManager.getInstance().getSDKInstance(wXRequest2.instanceId);
                if (sDKInstance != null) {
                    sDKInstance.getApmForInstance().actionNetRequest();
                }
                WXLogUtils.d(WXHttpAdapter.TAG, "into--[sendRequestByHttp] url:" + wXRequest2.url);
                wXResponse2.statusCode = WXErrorCode.WX_DEGRAD_ERR_NETWORK_BUNDLE_DOWNLOAD_FAILED.getErrorCode();
                wXResponse2.extendParams.put(WXPerformance.CACHE_TYPE, "none");
                String access$100 = WXHttpAdapter.this.getNetWorkType();
                wXResponse2.extendParams.put(WXInstanceApm.KEY_PAGE_PROPERTIES_REQUEST_TYPE, access$100);
                wXResponse2.extendParams.put("requestType", access$100);
                try {
                    if (wXRequest2.timeoutMs == 3000) {
                        String config = WXHttpAdapter.getConfig("wx_network_timeout_ms", "10000");
                        wXRequest2.timeoutMs = Integer.valueOf(config).intValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Request access$200 = WXHttpAdapter.this.assembleRequest(wXRequest2, wXResponse2);
                if (networkTracker2 != null) {
                    networkTracker2.preRequest(access$200);
                }
                new DegradableNetwork(AliWeex.getInstance().getApplication()).asyncSend(access$200, (Object) null, (Handler) null, new NetworkListener(wXRequest2.instanceId, networkTracker2, wXResponse2, onHttpListener2, wXRequest2.url, System.currentTimeMillis(), wXRequest2.paramMap));
            }
        });
    }

    /* access modifiers changed from: private */
    public String getNetWorkType() {
        String str;
        IWXConnection createDefault = WXConnectionFactory.createDefault(WXEnvironment.getApplication());
        if (createDefault == null) {
            str = "unknown";
        } else {
            str = createDefault.getNetworkType();
        }
        return ("wifi".equals(str) || "4g".equals(str) || "3g".equals(str) || "2g".equals(str)) ? str : "other";
    }

    /* access modifiers changed from: private */
    public Request assembleRequest(WXRequest wXRequest, WXResponse wXResponse) {
        RequestImpl requestImpl = new RequestImpl(wXRequest.url);
        requestImpl.setBizId(4102);
        if (wXRequest.paramMap != null) {
            for (String next : wXRequest.paramMap.keySet()) {
                requestImpl.addHeader(next, wXRequest.paramMap.get(next));
            }
        }
        requestImpl.addHeader("f-refer", "weex");
        requestImpl.addHeader("Accept-Language", getLanguageString());
        String str = wXRequest.method;
        if (TextUtils.isEmpty(str)) {
            str = "GET";
        }
        requestImpl.setMethod(str);
        requestImpl.setCharset("UTF-8");
        requestImpl.setRetryTime(2);
        requestImpl.setConnectTimeout(wXRequest.timeoutMs);
        try {
            if (Boolean.valueOf(getConfig(WX_NETWORK_SWITCH_CONTENT_LENGTH, "true")).booleanValue()) {
                requestImpl.setExtProperty(RequestConstant.CHECK_CONTENT_LENGTH, "true");
            }
        } catch (Exception e) {
            WXLogUtils.e(WXLogUtils.getStackTrace(e));
            WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_DEGRAD_ERR_NETWORK_CHECK_CONTENT_LENGTH_FAILED, "assembleRequest", WXLogUtils.getStackTrace(e), (Map<String, String>) null);
        }
        if (!TextUtils.isEmpty(wXRequest.body)) {
            requestImpl.setBodyEntry(new ByteArrayEntry(wXRequest.body.getBytes()));
        }
        if (WXEnvironment.isApkDebugable() && this.mDebugInterceptor != null) {
            this.mDebugInterceptor.record(wXRequest.url);
        }
        return requestImpl;
    }

    private String getLanguageString() {
        Locale locale;
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                locale = LocaleList.getDefault().get(0);
            } else {
                locale = Locale.getDefault();
            }
            String str = locale.getLanguage() + "-" + locale.getCountry();
            if (locale.getLanguage().equals("zh")) {
                return str + ",zh;q=0.8" + ",en-US;q=0.5,en;q=0.3";
            }
            return str + "," + locale.getLanguage() + ";q=0.8" + ",en-US;q=0.5,en;q=0.3";
        } catch (Throwable unused) {
            return "zh-CN,zh;q=0.8" + ",en-US;q=0.5,en;q=0.3";
        }
    }

    static class DebugInterceptor implements Interceptor {
        /* access modifiers changed from: private */
        public Map<String, Map<String, Object>> mStatistics = new HashMap();

        public synchronized void record(String str) {
            this.mStatistics.put(str, new HashMap());
        }

        public synchronized Map<String, Object> getRecord(String str) {
            return this.mStatistics.remove(str);
        }

        public Future intercept(final Interceptor.Chain chain) {
            anet.channel.request.Request request = chain.request();
            Callback callback = chain.callback();
            if ("weex".equals(chain.request().getHeaders().get("f-refer"))) {
                callback = new Callback() {
                    public void onResponseCode(int i, Map<String, List<String>> map) {
                        chain.callback().onResponseCode(i, map);
                        Map map2 = (Map) DebugInterceptor.this.mStatistics.get(chain.request().getUrlString());
                        if (map2 != null) {
                            map2.put("bizId", chain.request().rs.bizId);
                            map2.put("cacheTime", Long.valueOf(chain.request().rs.cacheTime));
                            map2.put("firstDataTime", Long.valueOf(chain.request().rs.firstDataTime));
                            map2.put("host", chain.request().rs.host);
                            map2.put("ip", chain.request().rs.ip);
                            map2.put("isDNS", Boolean.valueOf(chain.request().rs.isDNS));
                            map2.put("isProxy", Boolean.valueOf(chain.request().rs.isProxy));
                            map2.put("isSSL", Boolean.valueOf(chain.request().rs.isSSL));
                            map2.put("msg", chain.request().rs.msg);
                            map2.put("netType", chain.request().rs.netType);
                            map2.put("oneWayTime", Long.valueOf(chain.request().rs.oneWayTime));
                            map2.put("port", Integer.valueOf(chain.request().rs.port));
                            map2.put("protocolType", chain.request().rs.protocolType);
                            map2.put("proxyType", chain.request().rs.proxyType);
                            map2.put("recDataSize", Long.valueOf(chain.request().rs.recDataSize));
                            map2.put("recDataTime", Long.valueOf(chain.request().rs.recDataTime));
                            map2.put("sendBeforeTime", Long.valueOf(chain.request().rs.sendBeforeTime));
                            map2.put("sendDataSize", Long.valueOf(chain.request().rs.sendDataSize));
                            map2.put("sendDataTime", Long.valueOf(chain.request().rs.sendDataTime));
                            map2.put("serverRT", Long.valueOf(chain.request().rs.serverRT));
                            map2.put("statusCode", Integer.valueOf(chain.request().rs.statusCode));
                            map2.put("url", chain.request().rs.url);
                            map2.put("waitingTime", Long.valueOf(chain.request().rs.waitingTime));
                            map2.put("start", Long.valueOf(chain.request().rs.start));
                        }
                    }

                    public void onDataReceiveSize(int i, int i2, ByteArray byteArray) {
                        chain.callback().onDataReceiveSize(i, i2, byteArray);
                    }

                    public void onFinish(DefaultFinishEvent defaultFinishEvent) {
                        chain.callback().onFinish(defaultFinishEvent);
                    }
                };
            }
            return chain.proceed(request, callback);
        }
    }

    class NetworkListener implements NetworkCallBack.FinishListener, NetworkCallBack.ProgressListener, NetworkCallBack.ResponseCodeListener {
        private ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();
        private Map<String, List<String>> mHeader;
        private String mInstanceId;
        private NetworkTracker mNetworkTracker;
        private IWXHttpAdapter.OnHttpListener mOnHttpListener;
        private Map<String, String> mRequestParams;
        private long mStartRequestTime;
        private String mUrl;
        private WXResponse mWXResponse;

        NetworkListener(String str, NetworkTracker networkTracker, WXResponse wXResponse, IWXHttpAdapter.OnHttpListener onHttpListener, String str2, long j, Map<String, String> map) {
            this.mNetworkTracker = networkTracker;
            this.mWXResponse = wXResponse;
            this.mOnHttpListener = onHttpListener;
            this.mUrl = str2;
            this.mStartRequestTime = j;
            this.mInstanceId = str;
            this.mRequestParams = map;
        }

        public void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj) {
            WXLogUtils.d(WXHttpAdapter.TAG, "into--[onFinished]");
            onHttpFinish(finishEvent);
        }

        private void onHttpFinish(NetworkEvent.FinishEvent finishEvent) {
            boolean z;
            if (TextUtils.equals(WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode(), this.mWXResponse.statusCode)) {
                if (this.mNetworkTracker != null) {
                    this.mNetworkTracker.onFailed(this.mWXResponse.errorMsg);
                }
                String str = "false";
                if (AliWeex.getInstance().getConfigAdapter() != null) {
                    str = AliWeex.getInstance().getConfigAdapter().getConfig(WXHttpAdapter.GROUP_CACHE_SWITCH, WXHttpAdapter.KEY_CACHE_SWITCH, "false");
                }
                if ("true".equals(str) && finishEvent.getHttpCode() == 200 && !isMatchErrorUrl(this.mUrl) && this.mByteArrayOutputStream.size() > 0) {
                    try {
                        WVMemoryCache.getInstance().addMemoryCache(this.mUrl, this.mHeader, this.mByteArrayOutputStream.toByteArray());
                    } catch (Exception unused) {
                        Log.e(WXHttpAdapter.TAG, "Please join windvane dependency!");
                    }
                }
                z = false;
            } else {
                StatisticData statisticData = finishEvent.getStatisticData();
                WXEnvironment.isApkDebugable();
                this.mWXResponse.statusCode = String.valueOf(finishEvent.getHttpCode());
                byte[] byteArray = this.mByteArrayOutputStream.toByteArray();
                if (finishEvent.getHttpCode() == 200) {
                    this.mWXResponse.originalData = byteArray;
                    z = true;
                    if (statisticData != null) {
                        this.mWXResponse.extendParams.put("connectionType", statisticData.connectionType);
                        this.mWXResponse.extendParams.put("pureNetworkTime", Long.valueOf(statisticData.oneWayTime_ANet));
                        if ("cache".equals(statisticData.connectionType)) {
                            this.mWXResponse.extendParams.put("requestType", "cache");
                            this.mWXResponse.extendParams.put(WXPerformance.CACHE_TYPE, "netCache");
                        }
                    }
                    this.mWXResponse.extendParams.put("actualNetworkTime", Long.valueOf(System.currentTimeMillis() - this.mStartRequestTime));
                } else {
                    if (finishEvent.getHttpCode() == 404) {
                        this.mWXResponse.errorCode = String.valueOf(finishEvent.getHttpCode());
                        this.mWXResponse.errorMsg = "404 NOT FOUND!";
                    } else {
                        this.mWXResponse.errorCode = String.valueOf(finishEvent.getHttpCode());
                        WXResponse wXResponse = this.mWXResponse;
                        wXResponse.errorMsg = "networkMsg==" + finishEvent.getDesc() + "|networkErrorCode==" + finishEvent.getHttpCode() + "|mWXResponse==" + JSONObject.toJSONString(this.mWXResponse);
                    }
                    z = false;
                }
                if (this.mNetworkTracker != null) {
                    this.mNetworkTracker.onFinished(byteArray);
                }
            }
            try {
                WXSDKInstance wXSDKInstance = WXSDKManager.getInstance().getAllInstanceMap().get(this.mInstanceId);
                if (!(wXSDKInstance == null || wXSDKInstance.getApmForInstance() == null)) {
                    wXSDKInstance.getApmForInstance().actionNetResult(z, this.mWXResponse.errorCode);
                    List list = this.mHeader.get(AliWXSDKEngine.WX_AIR_GERY);
                    List list2 = this.mHeader.get(AliWXSDKEngine.WX_AIR_ENV);
                    if (!(list == null && list2 == null)) {
                        StringBuilder sb = new StringBuilder();
                        if (list != null && !list.isEmpty()) {
                            sb.append(AliWXSDKEngine.WX_AIR_GERY);
                            sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                            sb.append((String) list.get(0));
                        }
                        if (list2 != null && !list2.isEmpty()) {
                            if (sb.length() != 0) {
                                sb.append("&");
                            }
                            sb.append(AliWXSDKEngine.WX_AIR_ENV);
                            sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                            sb.append((String) list2.get(0));
                        }
                        if (sb.length() != 0) {
                            wXSDKInstance.getContainerInfo().put(AliWXSDKEngine.WX_AIR_TAG, sb.toString());
                            wXSDKInstance.getApmForInstance().addProperty(AliWXSDKEngine.WX_AIR_TAG, sb.toString());
                        }
                    }
                }
            } catch (Throwable unused2) {
            }
            WXHttpAdapter.this.onHttpFinish(this.mOnHttpListener, this.mUrl, this.mWXResponse, finishEvent.getHttpCode(), this.mHeader);
            if (this.mByteArrayOutputStream != null) {
                try {
                    this.mByteArrayOutputStream.close();
                    this.mByteArrayOutputStream = null;
                } catch (IOException e) {
                    WXLogUtils.e(WXHttpAdapter.TAG, WXLogUtils.getStackTrace(e));
                    if (this.mNetworkTracker != null) {
                        this.mNetworkTracker.onFailed(e.toString());
                    }
                }
            }
        }

        private boolean isMatchErrorUrl(String str) {
            return str.contains("err.tmall.com/error") || str.contains("err.taobao.com/error");
        }

        public void onDataReceived(NetworkEvent.ProgressEvent progressEvent, Object obj) {
            if (progressEvent != null) {
                if (this.mNetworkTracker != null) {
                    this.mNetworkTracker.onDataReceived(progressEvent);
                }
                this.mByteArrayOutputStream.write(progressEvent.getBytedata(), 0, progressEvent.getSize());
                this.mOnHttpListener.onHttpResponseProgress(this.mByteArrayOutputStream.size());
            }
        }

        public boolean onResponseCode(int i, Map<String, List<String>> map, Object obj) {
            if (map == null) {
                map = new HashMap<>();
            }
            this.mHeader = map;
            this.mOnHttpListener.onHeadersReceived(i, map);
            if (this.mNetworkTracker != null) {
                this.mNetworkTracker.onResponseCode(i, map);
                if (this.mNetworkTracker instanceof NetworkTracker) {
                    HashMap hashMap = new HashMap();
                    if (WXHttpAdapter.this.mDebugInterceptor != null) {
                        hashMap.putAll(WXHttpAdapter.this.mDebugInterceptor.getRecord(this.mUrl));
                    }
                    hashMap.putAll(this.mWXResponse.extendParams);
                    this.mNetworkTracker.onStatisticDataReceived(hashMap);
                }
            }
            if (!this.mUrl.contains(Utils.WH_WEEX_TRUE) || this.mRequestParams == null || !"true".equals(this.mRequestParams.get("isBundleRequest"))) {
                return true;
            }
            List list = map.get(map.containsKey("Content-Type") ? "Content-Type" : "content-type");
            String str = null;
            if (list != null) {
                str = list.toString();
            }
            if (!TextUtils.isEmpty(str) && (TextUtils.isEmpty(str) || str.contains("application/javascript"))) {
                return true;
            }
            this.mWXResponse.statusCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
            this.mWXResponse.errorCode = WXErrorCode.WX_DEGRAD_ERR_BUNDLE_CONTENTTYPE_ERROR.getErrorCode();
            this.mWXResponse.errorMsg = "degradeToH5";
            return true;
        }
    }

    public static String getConfig(String str, String str2) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        return configAdapter != null ? configAdapter.getConfig(WX_NETWORK_ORANGE_GROUP, str, str2) : str2;
    }
}
