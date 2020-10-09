package anetwork.channel.unified;

import alimama.com.unweventparse.constants.EventConstants;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import anet.channel.Config;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.NoAvailStrategyException;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.SessionGetCallback;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ENV;
import anet.channel.entity.SessionType;
import anet.channel.flow.FlowStat;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.monitor.BandWidthSampler;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.session.HttpSession;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.cache.CacheHelper;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.cookie.CookieManager;
import anetwork.channel.http.NetworkSdkSetting;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.util.RequestConstant;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class NetworkTask implements IUnifiedTask {
    public static final int MAX_RSP_BUFFER_LENGTH = 131072;
    public static final String TAG = "anet.NetworkTask";
    Cache cache = null;
    ByteArrayOutputStream cacheBuffer = null;
    volatile Cancelable cancelable = null;
    int contentLength = 0;
    int dataChunkIndex = 0;
    Cache.Entry entry = null;
    String f_refer = "other";
    volatile boolean isCanceled = false;
    boolean isDataChuckCallback = false;
    volatile AtomicBoolean isDone = null;
    boolean isHeaderCallback = false;
    RequestContext rc;
    ResponseBuffer responseBuffer = null;

    NetworkTask(RequestContext requestContext, Cache cache2, Cache.Entry entry2) {
        this.rc = requestContext;
        this.isDone = requestContext.isDone;
        this.cache = cache2;
        this.entry = entry2;
        this.f_refer = requestContext.config.getHeaders().get("f-refer");
    }

    public void cancel() {
        this.isCanceled = true;
        if (this.cancelable != null) {
            this.cancelable.cancel();
        }
    }

    public void run() {
        if (!this.isCanceled) {
            RequestStatistic requestStatistic = this.rc.config.rs;
            requestStatistic.f_refer = this.f_refer;
            if (!NetworkStatusHelper.isConnected()) {
                if (!NetworkConfigCenter.isRequestDelayRetryForNoNetwork() || requestStatistic.statusCode == -200) {
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "network unavailable", this.rc.seqNum, "NetworkStatus", NetworkStatusHelper.getStatus());
                    }
                    this.isDone.set(true);
                    this.rc.cancelTimeoutTask();
                    requestStatistic.isDone.set(true);
                    requestStatistic.statusCode = ErrorConstant.ERROR_NO_NETWORK;
                    requestStatistic.msg = ErrorConstant.getErrMsg(ErrorConstant.ERROR_NO_NETWORK);
                    requestStatistic.rspEnd = System.currentTimeMillis();
                    this.rc.callback.onFinish(new DefaultFinishEvent((int) ErrorConstant.ERROR_NO_NETWORK, (String) null, this.rc.config.getAwcnRequest()));
                    return;
                }
                requestStatistic.statusCode = ErrorConstant.ERROR_NO_NETWORK;
                ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                    public void run() {
                        ThreadPoolExecutorFactory.submitPriorityTask(NetworkTask.this, ThreadPoolExecutorFactory.Priority.HIGH);
                    }
                }, 1000, TimeUnit.MILLISECONDS);
            } else if (!NetworkConfigCenter.isBgRequestForbidden() || !GlobalAppRuntimeInfo.isAppBackground() || AppLifecycle.lastEnterBackgroundTime <= 0 || System.currentTimeMillis() - AppLifecycle.lastEnterBackgroundTime <= ((long) NetworkConfigCenter.getBgForbidRequestThreshold()) || NetworkConfigCenter.isUrlInWhiteList(this.rc.config.getHttpUrl()) || NetworkConfigCenter.isBizInWhiteList(this.rc.config.getAwcnRequest().getBizId())) {
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "exec request", this.rc.seqNum, "retryTimes", Integer.valueOf(this.rc.config.currentRetryTimes));
                }
                if (NetworkConfigCenter.isGetSessionAsyncEnable()) {
                    executeRequest();
                    return;
                }
                try {
                    Session tryGetSession = tryGetSession();
                    if (tryGetSession != null) {
                        sendRequest(tryGetSession, this.rc.config.getAwcnRequest());
                    }
                } catch (Exception e) {
                    ALog.e(TAG, "send request failed.", this.rc.seqNum, e, new Object[0]);
                }
            } else {
                this.isDone.set(true);
                this.rc.cancelTimeoutTask();
                if (ALog.isPrintLog(2)) {
                    ALog.i(TAG, "request forbidden in background", this.rc.seqNum, "url", this.rc.config.getHttpUrl());
                }
                requestStatistic.isDone.set(true);
                requestStatistic.statusCode = ErrorConstant.ERROR_REQUEST_FORBIDDEN_IN_BG;
                requestStatistic.msg = ErrorConstant.getErrMsg(ErrorConstant.ERROR_REQUEST_FORBIDDEN_IN_BG);
                requestStatistic.rspEnd = System.currentTimeMillis();
                this.rc.callback.onFinish(new DefaultFinishEvent((int) ErrorConstant.ERROR_REQUEST_FORBIDDEN_IN_BG, (String) null, this.rc.config.getAwcnRequest()));
                ExceptionStatistic exceptionStatistic = new ExceptionStatistic(ErrorConstant.ERROR_REQUEST_FORBIDDEN_IN_BG, (String) null, "rt");
                exceptionStatistic.host = this.rc.config.getHttpUrl().host();
                exceptionStatistic.url = this.rc.config.getUrlString();
                AppMonitor.getInstance().commitStat(exceptionStatistic);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0017, code lost:
        r0 = anet.channel.util.HttpUrl.parse(r4.urlString().replaceFirst(r4.host(), r0));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private anet.channel.util.HttpUrl checkCName(anet.channel.util.HttpUrl r4) {
        /*
            r3 = this;
            anetwork.channel.unified.RequestContext r0 = r3.rc
            anetwork.channel.entity.RequestConfig r0 = r0.config
            java.util.Map r0 = r0.getHeaders()
            java.lang.String r1 = "x-host-cname"
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x002a
            java.lang.String r1 = r4.urlString()
            java.lang.String r2 = r4.host()
            java.lang.String r0 = r1.replaceFirst(r2, r0)
            anet.channel.util.HttpUrl r0 = anet.channel.util.HttpUrl.parse(r0)
            if (r0 == 0) goto L_0x002a
            r4 = r0
        L_0x002a:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.unified.NetworkTask.checkCName(anet.channel.util.HttpUrl):anet.channel.util.HttpUrl");
    }

    private SessionCenter getSessionCenter() {
        String requestProperty = this.rc.config.getRequestProperty(RequestConstant.APPKEY);
        if (TextUtils.isEmpty(requestProperty)) {
            return SessionCenter.getInstance();
        }
        ENV env = ENV.ONLINE;
        String requestProperty2 = this.rc.config.getRequestProperty(RequestConstant.ENVIRONMENT);
        if (RequestConstant.ENV_PRE.equalsIgnoreCase(requestProperty2)) {
            env = ENV.PREPARE;
        } else if ("test".equalsIgnoreCase(requestProperty2)) {
            env = ENV.TEST;
        }
        if (env != NetworkSdkSetting.CURRENT_ENV) {
            NetworkSdkSetting.CURRENT_ENV = env;
            SessionCenter.switchEnvironment(env);
        }
        Config config = Config.getConfig(requestProperty, env);
        if (config == null) {
            config = new Config.Builder().setAppkey(requestProperty).setEnv(env).setAuthCode(this.rc.config.getRequestProperty(RequestConstant.AUTH_CODE)).build();
        }
        return SessionCenter.getInstance(config);
    }

    private Session tryGetSession() {
        Session session;
        final SessionCenter sessionCenter = getSessionCenter();
        final HttpUrl httpUrl = this.rc.config.getHttpUrl();
        final boolean containsNonDefaultPort = httpUrl.containsNonDefaultPort();
        final RequestStatistic requestStatistic = this.rc.config.rs;
        if (this.rc.config.requestType != 1 || !NetworkConfigCenter.isSpdyEnabled() || this.rc.config.currentRetryTimes != 0 || containsNonDefaultPort) {
            return tryGetHttpSession((Session) null, sessionCenter, httpUrl, containsNonDefaultPort);
        }
        final HttpUrl checkCName = checkCName(httpUrl);
        try {
            session = sessionCenter.getThrowsException(checkCName, SessionType.LONG_LINK, 0);
        } catch (NoAvailStrategyException unused) {
            return tryGetHttpSession((Session) null, sessionCenter, httpUrl, containsNonDefaultPort);
        } catch (Exception unused2) {
            session = null;
        }
        if (session == null) {
            ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                public void run() {
                    long currentTimeMillis = System.currentTimeMillis();
                    Session session = sessionCenter.get(checkCName, SessionType.LONG_LINK, (long) TBToast.Duration.MEDIUM);
                    requestStatistic.connWaitTime = System.currentTimeMillis() - currentTimeMillis;
                    requestStatistic.spdyRequestSend = session != null;
                    NetworkTask.this.sendRequest(NetworkTask.this.tryGetHttpSession(session, sessionCenter, httpUrl, containsNonDefaultPort), NetworkTask.this.rc.config.getAwcnRequest());
                }
            }, ThreadPoolExecutorFactory.Priority.NORMAL);
            return null;
        }
        ALog.i(TAG, "tryGetSession", this.rc.seqNum, "Session", session);
        requestStatistic.spdyRequestSend = true;
        return session;
    }

    private void executeRequest() {
        SessionCenter sessionCenter = getSessionCenter();
        final HttpUrl httpUrl = this.rc.config.getHttpUrl();
        final boolean containsNonDefaultPort = httpUrl.containsNonDefaultPort();
        final RequestStatistic requestStatistic = this.rc.config.rs;
        final Request awcnRequest = this.rc.config.getAwcnRequest();
        if (this.rc.config.requestType != 1 || !NetworkConfigCenter.isSpdyEnabled() || this.rc.config.currentRetryTimes != 0 || containsNonDefaultPort) {
            sendRequest(tryGetHttpSession((Session) null, sessionCenter, httpUrl, containsNonDefaultPort), awcnRequest);
            return;
        }
        HttpUrl checkCName = checkCName(httpUrl);
        final long currentTimeMillis = System.currentTimeMillis();
        final SessionCenter sessionCenter2 = sessionCenter;
        sessionCenter.asyncGet(checkCName, SessionType.LONG_LINK, TBToast.Duration.MEDIUM, new SessionGetCallback() {
            public void onSessionGetSuccess(Session session) {
                ALog.i(NetworkTask.TAG, "onSessionGetSuccess", NetworkTask.this.rc.seqNum, "Session", session);
                requestStatistic.connWaitTime = System.currentTimeMillis() - currentTimeMillis;
                requestStatistic.spdyRequestSend = true;
                NetworkTask.this.sendRequest(session, awcnRequest);
            }

            public void onSessionGetFail() {
                ALog.e(NetworkTask.TAG, "onSessionGetFail", NetworkTask.this.rc.seqNum, "url", requestStatistic.url);
                requestStatistic.connWaitTime = System.currentTimeMillis() - currentTimeMillis;
                NetworkTask.this.sendRequest(NetworkTask.this.tryGetHttpSession((Session) null, sessionCenter2, httpUrl, containsNonDefaultPort), awcnRequest);
            }
        });
    }

    /* access modifiers changed from: private */
    public Session tryGetHttpSession(HttpSession httpSession, SessionCenter sessionCenter, HttpUrl httpUrl, boolean z) {
        RequestStatistic requestStatistic = this.rc.config.rs;
        if (httpSession == null && this.rc.config.isHttpSessionEnable() && !z && !NetworkStatusHelper.isProxy()) {
            httpSession = sessionCenter.get(httpUrl, SessionType.SHORT_LINK, 0);
        }
        if (httpSession == null) {
            ALog.i(TAG, "create HttpSession with local DNS", this.rc.seqNum, new Object[0]);
            httpSession = new HttpSession(GlobalAppRuntimeInfo.getContext(), new ConnInfo(StringUtils.concatString(httpUrl.scheme(), HttpConstant.SCHEME_SPLIT, httpUrl.host()), this.rc.seqNum, (IConnStrategy) null));
        }
        if (requestStatistic.spdyRequestSend) {
            requestStatistic.degraded = 1;
        }
        ALog.i(TAG, "tryGetHttpSession", this.rc.seqNum, "Session", httpSession);
        return httpSession;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x008f  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private anet.channel.request.Request preProcessRequest(anet.channel.request.Request r7) {
        /*
            r6 = this;
            anetwork.channel.unified.RequestContext r0 = r6.rc
            anetwork.channel.entity.RequestConfig r0 = r0.config
            boolean r0 = r0.isRequestCookieEnabled()
            if (r0 == 0) goto L_0x003e
            anetwork.channel.unified.RequestContext r0 = r6.rc
            anetwork.channel.entity.RequestConfig r0 = r0.config
            java.lang.String r0 = r0.getUrlString()
            java.lang.String r0 = anetwork.channel.cookie.CookieManager.getCookie(r0)
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 != 0) goto L_0x003e
            anet.channel.request.Request$Builder r1 = r7.newBuilder()
            java.util.Map r2 = r7.getHeaders()
            java.lang.String r3 = "Cookie"
            java.lang.Object r2 = r2.get(r3)
            java.lang.String r2 = (java.lang.String) r2
            boolean r3 = android.text.TextUtils.isEmpty(r2)
            if (r3 != 0) goto L_0x0038
            java.lang.String r3 = "; "
            java.lang.String r0 = anet.channel.util.StringUtils.concatString(r2, r3, r0)
        L_0x0038:
            java.lang.String r2 = "Cookie"
            r1.addHeader(r2, r0)
            goto L_0x003f
        L_0x003e:
            r1 = 0
        L_0x003f:
            anetwork.channel.cache.Cache$Entry r0 = r6.entry
            if (r0 == 0) goto L_0x006f
            if (r1 != 0) goto L_0x0049
            anet.channel.request.Request$Builder r1 = r7.newBuilder()
        L_0x0049:
            anetwork.channel.cache.Cache$Entry r0 = r6.entry
            java.lang.String r0 = r0.etag
            if (r0 == 0) goto L_0x0058
            java.lang.String r0 = "If-None-Match"
            anetwork.channel.cache.Cache$Entry r2 = r6.entry
            java.lang.String r2 = r2.etag
            r1.addHeader(r0, r2)
        L_0x0058:
            anetwork.channel.cache.Cache$Entry r0 = r6.entry
            long r2 = r0.lastModified
            r4 = 0
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x006f
            java.lang.String r0 = "If-Modified-Since"
            anetwork.channel.cache.Cache$Entry r2 = r6.entry
            long r2 = r2.lastModified
            java.lang.String r2 = anetwork.channel.cache.CacheHelper.toGMTDate(r2)
            r1.addHeader(r0, r2)
        L_0x006f:
            anetwork.channel.unified.RequestContext r0 = r6.rc
            anetwork.channel.entity.RequestConfig r0 = r0.config
            int r0 = r0.currentRetryTimes
            if (r0 != 0) goto L_0x008c
            java.lang.String r0 = "weex"
            java.lang.String r2 = r6.f_refer
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 == 0) goto L_0x008c
            if (r1 != 0) goto L_0x0087
            anet.channel.request.Request$Builder r1 = r7.newBuilder()
        L_0x0087:
            r0 = 3000(0xbb8, float:4.204E-42)
            r1.setReadTimeout(r0)
        L_0x008c:
            if (r1 != 0) goto L_0x008f
            goto L_0x0093
        L_0x008f:
            anet.channel.request.Request r7 = r1.build()
        L_0x0093:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: anetwork.channel.unified.NetworkTask.preProcessRequest(anet.channel.request.Request):anet.channel.request.Request");
    }

    /* access modifiers changed from: private */
    public void sendRequest(Session session, Request request) {
        if (session != null && !this.isCanceled) {
            final Request preProcessRequest = preProcessRequest(request);
            final RequestStatistic requestStatistic = this.rc.config.rs;
            requestStatistic.reqStart = System.currentTimeMillis();
            this.cancelable = session.request(preProcessRequest, new RequestCb() {
                public void onResponseCode(int i, Map<String, List<String>> map) {
                    String singleHeaderFieldByKey;
                    if (!NetworkTask.this.isDone.get()) {
                        if (ALog.isPrintLog(2)) {
                            ALog.i(NetworkTask.TAG, "onResponseCode", preProcessRequest.getSeq(), "code", Integer.valueOf(i));
                            ALog.i(NetworkTask.TAG, "onResponseCode", preProcessRequest.getSeq(), EventConstants.Mtop.HEADERS, map);
                        }
                        if (HttpHelper.checkRedirect(preProcessRequest, i) && (singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map, HttpConstant.LOCATION)) != null) {
                            HttpUrl parse = HttpUrl.parse(singleHeaderFieldByKey);
                            if (parse == null) {
                                ALog.e(NetworkTask.TAG, "redirect url is invalid!", preProcessRequest.getSeq(), "redirect url", singleHeaderFieldByKey);
                            } else if (NetworkTask.this.isDone.compareAndSet(false, true)) {
                                parse.lockScheme();
                                NetworkTask.this.rc.config.redirectToUrl(parse);
                                NetworkTask.this.rc.isDone = new AtomicBoolean();
                                NetworkTask.this.rc.runningTask = new NetworkTask(NetworkTask.this.rc, (Cache) null, (Cache.Entry) null);
                                requestStatistic.recordRedirect(i, parse.simpleUrlString());
                                ThreadPoolExecutorFactory.submitPriorityTask(NetworkTask.this.rc.runningTask, ThreadPoolExecutorFactory.Priority.HIGH);
                                return;
                            } else {
                                return;
                            }
                        }
                        try {
                            NetworkTask.this.rc.cancelTimeoutTask();
                            CookieManager.setCookie(NetworkTask.this.rc.config.getUrlString(), map);
                            NetworkTask.this.contentLength = HttpHelper.parseContentLength(map);
                            String urlString = NetworkTask.this.rc.config.getUrlString();
                            if (NetworkTask.this.entry == null || i != 304) {
                                if (NetworkTask.this.cache != null) {
                                    if ("no-store".equals(HttpHelper.getSingleHeaderFieldByKey(map, HttpConstant.CACHE_CONTROL))) {
                                        NetworkTask.this.cache.remove(urlString);
                                    } else {
                                        NetworkTask networkTask = NetworkTask.this;
                                        Cache.Entry parseCacheHeaders = CacheHelper.parseCacheHeaders(map);
                                        networkTask.entry = parseCacheHeaders;
                                        if (parseCacheHeaders != null) {
                                            HttpHelper.removeHeaderFiledByKey(map, HttpConstant.CACHE_CONTROL);
                                            map.put(HttpConstant.CACHE_CONTROL, Arrays.asList(new String[]{"no-store"}));
                                            NetworkTask.this.cacheBuffer = new ByteArrayOutputStream(NetworkTask.this.contentLength != 0 ? NetworkTask.this.contentLength : 5120);
                                        }
                                    }
                                }
                                map.put(HttpConstant.X_PROTOCOL, Arrays.asList(new String[]{requestStatistic.protocolType}));
                                if (!NetworkConfigCenter.isResponseBufferEnable() || NetworkTask.this.contentLength > 131072) {
                                    NetworkTask.this.rc.callback.onResponseCode(i, map);
                                    NetworkTask.this.isHeaderCallback = true;
                                    return;
                                }
                                NetworkTask.this.responseBuffer = new ResponseBuffer(i, map);
                                return;
                            }
                            NetworkTask.this.entry.responseHeaders.putAll(map);
                            Cache.Entry parseCacheHeaders2 = CacheHelper.parseCacheHeaders(map);
                            if (parseCacheHeaders2 != null && parseCacheHeaders2.ttl > NetworkTask.this.entry.ttl) {
                                NetworkTask.this.entry.ttl = parseCacheHeaders2.ttl;
                            }
                            NetworkTask.this.rc.callback.onResponseCode(200, NetworkTask.this.entry.responseHeaders);
                            NetworkTask.this.rc.callback.onDataReceiveSize(1, NetworkTask.this.entry.data.length, ByteArray.wrap(NetworkTask.this.entry.data));
                            long currentTimeMillis = System.currentTimeMillis();
                            NetworkTask.this.cache.put(urlString, NetworkTask.this.entry);
                            ALog.i(NetworkTask.TAG, "update cache", NetworkTask.this.rc.seqNum, "cost", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), "key", urlString);
                        } catch (Exception e) {
                            ALog.w(NetworkTask.TAG, "[onResponseCode] error.", NetworkTask.this.rc.seqNum, e, new Object[0]);
                        }
                    }
                }

                public void onDataReceive(ByteArray byteArray, boolean z) {
                    if (!NetworkTask.this.isDone.get()) {
                        if (NetworkTask.this.dataChunkIndex == 0) {
                            ALog.i(NetworkTask.TAG, "[onDataReceive] receive first data chunk!", NetworkTask.this.rc.seqNum, new Object[0]);
                        }
                        if (z) {
                            ALog.i(NetworkTask.TAG, "[onDataReceive] receive last data chunk!", NetworkTask.this.rc.seqNum, new Object[0]);
                        }
                        NetworkTask.this.dataChunkIndex++;
                        try {
                            if (NetworkTask.this.responseBuffer != null) {
                                NetworkTask.this.responseBuffer.bodyBufferList.add(byteArray);
                                if (requestStatistic.recDataSize > PlaybackStateCompat.ACTION_PREPARE_FROM_URI || z) {
                                    NetworkTask.this.dataChunkIndex = NetworkTask.this.responseBuffer.callback(NetworkTask.this.rc.callback, NetworkTask.this.contentLength);
                                    NetworkTask.this.isHeaderCallback = true;
                                    NetworkTask.this.isDataChuckCallback = NetworkTask.this.dataChunkIndex > 1;
                                    NetworkTask.this.responseBuffer = null;
                                }
                            } else {
                                NetworkTask.this.rc.callback.onDataReceiveSize(NetworkTask.this.dataChunkIndex, NetworkTask.this.contentLength, byteArray);
                                NetworkTask.this.isDataChuckCallback = true;
                            }
                            if (NetworkTask.this.cacheBuffer != null) {
                                NetworkTask.this.cacheBuffer.write(byteArray.getBuffer(), 0, byteArray.getDataLength());
                                if (z) {
                                    String urlString = NetworkTask.this.rc.config.getUrlString();
                                    NetworkTask.this.entry.data = NetworkTask.this.cacheBuffer.toByteArray();
                                    long currentTimeMillis = System.currentTimeMillis();
                                    NetworkTask.this.cache.put(urlString, NetworkTask.this.entry);
                                    ALog.i(NetworkTask.TAG, "write cache", NetworkTask.this.rc.seqNum, "cost", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), "size", Integer.valueOf(NetworkTask.this.entry.data.length), "key", urlString);
                                }
                            }
                        } catch (Exception e) {
                            ALog.w(NetworkTask.TAG, "[onDataReceive] error.", NetworkTask.this.rc.seqNum, e, new Object[0]);
                        }
                    }
                }

                public void onFinish(int i, String str, RequestStatistic requestStatistic) {
                    DefaultFinishEvent defaultFinishEvent;
                    String str2;
                    if (!NetworkTask.this.isDone.getAndSet(true)) {
                        int i2 = 3;
                        if (ALog.isPrintLog(2)) {
                            ALog.i(NetworkTask.TAG, "[onFinish]", NetworkTask.this.rc.seqNum, "code", Integer.valueOf(i), "msg", str);
                        }
                        if (i < 0) {
                            try {
                                if (NetworkTask.this.rc.config.isAllowRetry()) {
                                    if (NetworkTask.this.isHeaderCallback || NetworkTask.this.isDataChuckCallback) {
                                        requestStatistic.msg += ":回调后触发重试";
                                        if (NetworkTask.this.isDataChuckCallback) {
                                            requestStatistic.roaming = 2;
                                        } else if (NetworkTask.this.isHeaderCallback) {
                                            requestStatistic.roaming = 1;
                                        }
                                        ALog.e(NetworkTask.TAG, "Cannot retry request after onHeader/onDataReceived callback!", NetworkTask.this.rc.seqNum, new Object[0]);
                                    } else {
                                        ALog.e(NetworkTask.TAG, "clear response buffer and retry", NetworkTask.this.rc.seqNum, new Object[0]);
                                        if (NetworkTask.this.responseBuffer != null) {
                                            if (!NetworkTask.this.responseBuffer.bodyBufferList.isEmpty()) {
                                                i2 = 4;
                                            }
                                            requestStatistic.roaming = i2;
                                            NetworkTask.this.responseBuffer.release();
                                            NetworkTask.this.responseBuffer = null;
                                        }
                                        NetworkTask.this.rc.config.retryRequest();
                                        NetworkTask.this.rc.isDone = new AtomicBoolean();
                                        NetworkTask.this.rc.runningTask = new NetworkTask(NetworkTask.this.rc, NetworkTask.this.cache, NetworkTask.this.entry);
                                        if (requestStatistic.tnetErrorCode != 0) {
                                            str2 = i + "|" + requestStatistic.tnetErrorCode;
                                            requestStatistic.tnetErrorCode = 0;
                                        } else {
                                            str2 = String.valueOf(i);
                                        }
                                        requestStatistic.appendErrorTrace(str2);
                                        long currentTimeMillis = System.currentTimeMillis();
                                        requestStatistic.retryCostTime += currentTimeMillis - requestStatistic.start;
                                        requestStatistic.start = currentTimeMillis;
                                        ThreadPoolExecutorFactory.submitPriorityTask(NetworkTask.this.rc.runningTask, ThreadPoolExecutorFactory.Priority.HIGH);
                                        return;
                                    }
                                }
                            } catch (Exception unused) {
                                return;
                            }
                        }
                        if (NetworkTask.this.responseBuffer != null) {
                            NetworkTask.this.responseBuffer.callback(NetworkTask.this.rc.callback, NetworkTask.this.contentLength);
                        }
                        NetworkTask.this.rc.cancelTimeoutTask();
                        requestStatistic.isDone.set(true);
                        if (!(!NetworkTask.this.rc.config.shouldCheckContentLength() || requestStatistic.contentLength == 0 || requestStatistic.contentLength == requestStatistic.rspBodyDeflateSize)) {
                            requestStatistic.ret = 0;
                            requestStatistic.statusCode = ErrorConstant.ERROR_DATA_LENGTH_NOT_MATCH;
                            str = ErrorConstant.getErrMsg(ErrorConstant.ERROR_DATA_LENGTH_NOT_MATCH);
                            requestStatistic.msg = str;
                            ALog.e(NetworkTask.TAG, "received data length not match with content-length", NetworkTask.this.rc.seqNum, "content-length", Integer.valueOf(NetworkTask.this.contentLength), "recDataLength", Long.valueOf(requestStatistic.rspBodyDeflateSize));
                            ExceptionStatistic exceptionStatistic = new ExceptionStatistic(ErrorConstant.ERROR_DATA_LENGTH_NOT_MATCH, str, "rt");
                            exceptionStatistic.url = NetworkTask.this.rc.config.getUrlString();
                            AppMonitor.getInstance().commitStat(exceptionStatistic);
                            i = ErrorConstant.ERROR_DATA_LENGTH_NOT_MATCH;
                        }
                        if (i != 304 || NetworkTask.this.entry == null) {
                            defaultFinishEvent = new DefaultFinishEvent(i, str, preProcessRequest);
                        } else {
                            requestStatistic.protocolType = "cache";
                            defaultFinishEvent = new DefaultFinishEvent(200, str, preProcessRequest);
                        }
                        NetworkTask.this.rc.callback.onFinish(defaultFinishEvent);
                        if (i >= 0) {
                            BandWidthSampler.getInstance().onDataReceived(requestStatistic.sendStart, requestStatistic.rspEnd, requestStatistic.rspHeadDeflateSize + requestStatistic.rspBodyDeflateSize);
                        } else {
                            requestStatistic.netType = NetworkStatusHelper.getNetworkSubType();
                        }
                        NetworkAnalysis.getInstance().commitFlow(new FlowStat(NetworkTask.this.f_refer, requestStatistic));
                    }
                }
            });
        }
    }

    private static class ResponseBuffer {
        List<ByteArray> bodyBufferList = new ArrayList();
        int code;
        Map<String, List<String>> header;

        ResponseBuffer(int i, Map<String, List<String>> map) {
            this.code = i;
            this.header = map;
        }

        /* access modifiers changed from: package-private */
        public void release() {
            for (ByteArray recycle : this.bodyBufferList) {
                recycle.recycle();
            }
        }

        /* access modifiers changed from: package-private */
        public int callback(Callback callback, int i) {
            callback.onResponseCode(this.code, this.header);
            int i2 = 1;
            for (ByteArray onDataReceiveSize : this.bodyBufferList) {
                callback.onDataReceiveSize(i2, i, onDataReceiveSize);
                i2++;
            }
            return i2;
        }
    }
}
