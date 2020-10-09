package com.taobao.tao.remotebusiness;

import android.content.Context;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.tao.remotebusiness.listener.MtopListenerProxyFactory;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.ApiTypeEnum;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.JsonTypeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import mtopsdk.mtop.intf.MtopPrefetch;
import mtopsdk.mtop.stat.IMtopMonitor;
import mtopsdk.mtop.stat.MtopMonitor;

public class MtopBusiness extends MtopBuilder {
    public static final int MAX_RETRY_TIMES = 3;
    private static final String TAG = "mtopsdk.MtopBusiness";
    private static AtomicInteger seqGen = new AtomicInteger(0);
    private ApiID apiID;
    public String authParam = null;
    public Class<?> clazz;
    public boolean isCached = false;
    private boolean isCancelled = false;
    private boolean isErrorNotifyAfterCache = false;
    public MtopListener listener;
    private MtopResponse mtopResponse = null;
    private boolean needAuth = false;
    public long onBgFinishTime = 0;
    public long reqStartTime = 0;
    @Deprecated
    public Object requestContext = null;
    protected int requestType = 0;
    protected int retryTime = 0;
    public long sendStartTime = 0;
    private final String seqNo = genSeqNo();
    public boolean showAuthUI = true;
    private boolean showLoginUI = true;
    private boolean syncRequestFlag = false;

    protected MtopBusiness(@NonNull Mtop mtop, IMTOPDataObject iMTOPDataObject, String str) {
        super(mtop, iMTOPDataObject, str);
    }

    protected MtopBusiness(@NonNull Mtop mtop, MtopRequest mtopRequest, String str) {
        super(mtop, mtopRequest, str);
    }

    private String genSeqNo() {
        StringBuilder sb = new StringBuilder(16);
        sb.append("MB");
        sb.append(seqGen.incrementAndGet());
        sb.append('.');
        sb.append(this.stat.seqNo);
        return sb.toString();
    }

    public static MtopBusiness build(Mtop mtop, IMTOPDataObject iMTOPDataObject, String str) {
        return new MtopBusiness(mtop, iMTOPDataObject, str);
    }

    public static MtopBusiness build(Mtop mtop, IMTOPDataObject iMTOPDataObject) {
        return build(mtop, iMTOPDataObject, (String) null);
    }

    @Deprecated
    public static MtopBusiness build(IMTOPDataObject iMTOPDataObject, String str) {
        return build(Mtop.instance((Context) null, str), iMTOPDataObject, str);
    }

    @Deprecated
    public static MtopBusiness build(IMTOPDataObject iMTOPDataObject) {
        return build(Mtop.instance((Context) null), iMTOPDataObject);
    }

    public static MtopBusiness build(Mtop mtop, MtopRequest mtopRequest, String str) {
        return new MtopBusiness(mtop, mtopRequest, str);
    }

    public static MtopBusiness build(Mtop mtop, MtopRequest mtopRequest) {
        return build(mtop, mtopRequest, (String) null);
    }

    @Deprecated
    public static MtopBusiness build(MtopRequest mtopRequest, String str) {
        return build(Mtop.instance((Context) null, str), mtopRequest, str);
    }

    @Deprecated
    public static MtopBusiness build(MtopRequest mtopRequest) {
        return build(Mtop.instance((Context) null), mtopRequest, (String) null);
    }

    @Deprecated
    public MtopBusiness registerListener(MtopListener mtopListener) {
        this.listener = mtopListener;
        return this;
    }

    @Deprecated
    public MtopBusiness addListener(MtopListener mtopListener) {
        this.listener = mtopListener;
        return this;
    }

    public MtopBusiness registerListener(IRemoteListener iRemoteListener) {
        this.listener = iRemoteListener;
        return this;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public void startRequest() {
        startRequest(0, (Class<?>) null);
    }

    public void startRequest(Class<?> cls) {
        startRequest(0, cls);
    }

    public void startRequest(int i, Class<?> cls) {
        if (this.request == null) {
            TBSdkLog.e(TAG, this.seqNo, "MtopRequest is null!");
            return;
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            String str = this.seqNo;
            TBSdkLog.i(TAG, str, "startRequest " + this.request);
        }
        this.reqStartTime = System.currentTimeMillis();
        this.isCancelled = false;
        this.isCached = false;
        this.clazz = cls;
        this.requestType = i;
        if (this.requestContext != null) {
            reqContext(this.requestContext);
        }
        if (this.listener != null && !this.isCancelled) {
            super.addListener(MtopListenerProxyFactory.getMtopListenerProxy(this, this.listener));
        }
        mtopCommitStatData(false);
        this.sendStartTime = System.currentTimeMillis();
        this.apiID = super.asyncRequest();
    }

    public MtopResponse syncRequest() {
        String key = this.request != null ? this.request.getKey() : "";
        if (MtopUtils.isMainThread()) {
            TBSdkLog.e(TAG, this.seqNo, "do syncRequest in UI main thread!");
        }
        this.syncRequestFlag = true;
        if (this.listener == null) {
            this.listener = new IRemoteBaseListener() {
                public void onError(int i, MtopResponse mtopResponse, Object obj) {
                }

                public void onSuccess(int i, MtopResponse mtopResponse, BaseOutDo baseOutDo, Object obj) {
                }

                public void onSystemError(int i, MtopResponse mtopResponse, Object obj) {
                }
            };
        }
        startRequest();
        synchronized (this.listener) {
            try {
                if (this.mtopResponse == null) {
                    this.listener.wait(60000);
                }
            } catch (InterruptedException unused) {
                String str = this.seqNo;
                TBSdkLog.e(TAG, str, "syncRequest InterruptedException. apiKey=" + key);
            } catch (Exception unused2) {
                String str2 = this.seqNo;
                TBSdkLog.e(TAG, str2, "syncRequest do wait Exception. apiKey=" + key);
            }
        }
        if (this.mtopResponse == null) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                String str3 = this.seqNo;
                TBSdkLog.w(TAG, str3, "syncRequest timeout. apiKey=" + key);
            }
            cancelRequest();
        }
        return this.mtopResponse != null ? this.mtopResponse : handleAsyncTimeoutException();
    }

    @Deprecated
    public ApiID asyncRequest() {
        startRequest();
        return this.apiID;
    }

    public int getRequestType() {
        return this.requestType;
    }

    public boolean isTaskCanceled() {
        return this.isCancelled;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void cancelRequest() {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, getRequestLogInfo("cancelRequest.", this));
        }
        this.isCancelled = true;
        if (this.apiID != null) {
            try {
                this.apiID.cancelApiCall();
            } catch (Throwable th) {
                TBSdkLog.w(TAG, this.seqNo, getRequestLogInfo("cancelRequest failed.", this), th);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void retryRequest() {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, getRequestLogInfo("retryRequest.", this));
        }
        if (this.retryTime >= 3) {
            this.retryTime = 0;
            doFinish(this.mtopContext.mtopResponse, (BaseOutDo) null);
            return;
        }
        cancelRequest();
        startRequest(this.requestType, this.clazz);
        this.retryTime++;
    }

    public MtopBusiness showLoginUI(boolean z) {
        this.showLoginUI = z;
        return this;
    }

    public boolean isShowLoginUI() {
        return this.showLoginUI;
    }

    public MtopBusiness setNeedAuth(String str, boolean z) {
        this.authParam = str;
        this.showAuthUI = z;
        this.needAuth = true;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("[setNeedAuth] authParam=");
            sb.append(str);
            sb.append(", showAuthUI=");
            sb.append(z);
            sb.append(", needAuth=");
            sb.append(this.needAuth);
            TBSdkLog.d(TAG, this.seqNo, sb.toString());
        }
        return this;
    }

    public MtopBusiness setNeedAuth(@NonNull String str, String str2, boolean z) {
        this.mtopProp.apiType = ApiTypeEnum.ISV_OPEN_API;
        this.mtopProp.isInnerOpen = true;
        if (StringUtils.isNotBlank(str)) {
            this.mtopProp.openAppKey = str;
        }
        this.authParam = str2;
        this.showAuthUI = z;
        this.needAuth = true;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            StringBuilder sb = new StringBuilder(64);
            sb.append("[setNeedAuth] openAppKey=");
            sb.append(str);
            sb.append(", bizParam=");
            sb.append(str2);
            sb.append(", showAuthUI=");
            sb.append(z);
            sb.append(", needAuth=");
            sb.append(this.needAuth);
            sb.append(", isInnerOpen=true");
            TBSdkLog.d(TAG, this.seqNo, sb.toString());
        }
        return this;
    }

    public boolean isNeedAuth() {
        return this.needAuth || this.authParam != null;
    }

    public MtopBusiness addOpenApiParams(String str, String str2) {
        return (MtopBusiness) super.addOpenApiParams(str, str2);
    }

    public MtopBusiness setErrorNotifyAfterCache(boolean z) {
        this.isErrorNotifyAfterCache = z;
        return this;
    }

    @Deprecated
    public void setErrorNotifyNeedAfterCache(boolean z) {
        setErrorNotifyAfterCache(z);
    }

    public void doFinish(MtopResponse mtopResponse2, BaseOutDo baseOutDo) {
        if (this.syncRequestFlag) {
            this.mtopResponse = mtopResponse2;
            synchronized (this.listener) {
                try {
                    this.listener.notify();
                } catch (Exception e) {
                    String str = this.seqNo;
                    StringBuilder sb = new StringBuilder();
                    sb.append("[doFinish]syncRequest do notify Exception. apiKey=");
                    sb.append(mtopResponse2 != null ? mtopResponse2.getFullKey() : "");
                    TBSdkLog.e(TAG, str, sb.toString(), e);
                }
            }
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            StringBuilder sb2 = new StringBuilder(32);
            sb2.append("doFinish request=");
            sb2.append(this.request);
            if (mtopResponse2 != null) {
                sb2.append(", retCode=");
                sb2.append(mtopResponse2.getRetCode());
            }
            TBSdkLog.i(TAG, this.seqNo, sb2.toString());
        }
        if (this.isCancelled) {
            TBSdkLog.w(TAG, this.seqNo, "request is cancelled,don't callback listener.");
        } else if (!(this.listener instanceof IRemoteListener)) {
            String str2 = this.seqNo;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("listener did't implement IRemoteBaseListener.apiKey=");
            sb3.append(mtopResponse2 != null ? mtopResponse2.getFullKey() : "");
            TBSdkLog.e(TAG, str2, sb3.toString());
        } else {
            IRemoteListener iRemoteListener = (IRemoteListener) this.listener;
            if (mtopResponse2 != null && mtopResponse2.isApiSuccess()) {
                long currentTimeMillis = this.stat.currentTimeMillis();
                this.stat.callbackPocTime = currentTimeMillis - this.stat.netSendEndTime;
                this.stat.allTime = currentTimeMillis - this.stat.startTime;
                this.stat.handler = this.mtopContext.property.handler != null;
                try {
                    iRemoteListener.onSuccess(this.requestType, mtopResponse2, baseOutDo, getReqContext());
                    if (MtopMonitor.getInstance() != null) {
                        HashMap hashMap = new HashMap();
                        hashMap.put(IMtopMonitor.DATA_RESPONSE, mtopResponse2.getApi());
                        MtopMonitor.getInstance().onCommit(IMtopMonitor.MtopMonitorType.TYPE_CALLBACK, hashMap);
                    }
                } catch (Throwable th) {
                    TBSdkLog.e(TAG, this.seqNo, "listener onSuccess callback error", th);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, this.seqNo, "listener onSuccess callback.");
                }
            } else if (!this.isCached || this.isErrorNotifyAfterCache) {
                doErrorCallback(mtopResponse2, iRemoteListener);
            } else {
                TBSdkLog.i(TAG, this.seqNo, "listener onCached callback,doNothing in doFinish()");
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a3 A[Catch:{ Throwable -> 0x0092 }] */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00d1  */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doErrorCallback(mtopsdk.mtop.domain.MtopResponse r6, com.taobao.tao.remotebusiness.IRemoteListener r7) {
        /*
            r5 = this;
            r0 = 1
            if (r6 != 0) goto L_0x0026
            mtopsdk.common.util.TBSdkLog$LogEnable r1 = mtopsdk.common.util.TBSdkLog.LogEnable.ErrorEnable
            boolean r1 = mtopsdk.common.util.TBSdkLog.isLogEnable(r1)
            if (r1 == 0) goto L_0x0080
            java.lang.String r1 = "mtopsdk.MtopBusiness"
            java.lang.String r2 = r5.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "mtopResponse is null."
            r3.append(r4)
            mtopsdk.mtop.domain.MtopRequest r4 = r5.request
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)
            goto L_0x0080
        L_0x0026:
            boolean r1 = r6.isSessionInvalid()
            if (r1 == 0) goto L_0x004f
            mtopsdk.common.util.TBSdkLog$LogEnable r1 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r1 = mtopsdk.common.util.TBSdkLog.isLogEnable(r1)
            if (r1 == 0) goto L_0x0080
            java.lang.String r1 = "mtopsdk.MtopBusiness"
            java.lang.String r2 = r5.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "session invalid error."
            r3.append(r4)
            mtopsdk.mtop.domain.MtopRequest r4 = r5.request
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)
            goto L_0x0080
        L_0x004f:
            boolean r1 = r6.isMtopServerError()
            if (r1 != 0) goto L_0x005e
            boolean r1 = r6.isMtopSdkError()
            if (r1 == 0) goto L_0x005c
            goto L_0x005e
        L_0x005c:
            r0 = 0
            goto L_0x0080
        L_0x005e:
            mtopsdk.common.util.TBSdkLog$LogEnable r1 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r1 = mtopsdk.common.util.TBSdkLog.isLogEnable(r1)
            if (r1 == 0) goto L_0x0080
            java.lang.String r1 = "mtopsdk.MtopBusiness"
            java.lang.String r2 = r5.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "mtopServerError or mtopSdkError."
            r3.append(r4)
            mtopsdk.mtop.domain.MtopRequest r4 = r5.request
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r3)
        L_0x0080:
            if (r0 == 0) goto L_0x0094
            boolean r1 = r7 instanceof com.taobao.tao.remotebusiness.IRemoteBaseListener     // Catch:{ Throwable -> 0x0092 }
            if (r1 == 0) goto L_0x0094
            com.taobao.tao.remotebusiness.IRemoteBaseListener r7 = (com.taobao.tao.remotebusiness.IRemoteBaseListener) r7     // Catch:{ Throwable -> 0x0092 }
            int r1 = r5.requestType     // Catch:{ Throwable -> 0x0092 }
            java.lang.Object r2 = r5.getReqContext()     // Catch:{ Throwable -> 0x0092 }
            r7.onSystemError(r1, r6, r2)     // Catch:{ Throwable -> 0x0092 }
            goto L_0x009d
        L_0x0092:
            r6 = move-exception
            goto L_0x00c0
        L_0x0094:
            int r1 = r5.requestType     // Catch:{ Throwable -> 0x0092 }
            java.lang.Object r2 = r5.getReqContext()     // Catch:{ Throwable -> 0x0092 }
            r7.onError(r1, r6, r2)     // Catch:{ Throwable -> 0x0092 }
        L_0x009d:
            mtopsdk.mtop.stat.IMtopMonitor r7 = mtopsdk.mtop.stat.MtopMonitor.getInstance()     // Catch:{ Throwable -> 0x0092 }
            if (r7 == 0) goto L_0x00c9
            java.util.HashMap r7 = new java.util.HashMap     // Catch:{ Throwable -> 0x0092 }
            r7.<init>()     // Catch:{ Throwable -> 0x0092 }
            java.lang.String r1 = "key_data_response"
            if (r6 == 0) goto L_0x00b1
            java.lang.String r6 = r6.getApi()     // Catch:{ Throwable -> 0x0092 }
            goto L_0x00b3
        L_0x00b1:
            java.lang.String r6 = "response null"
        L_0x00b3:
            r7.put(r1, r6)     // Catch:{ Throwable -> 0x0092 }
            mtopsdk.mtop.stat.IMtopMonitor r6 = mtopsdk.mtop.stat.MtopMonitor.getInstance()     // Catch:{ Throwable -> 0x0092 }
            java.lang.String r1 = "TYPE_ERROR_CALLBACK"
            r6.onCommit(r1, r7)     // Catch:{ Throwable -> 0x0092 }
            goto L_0x00c9
        L_0x00c0:
            java.lang.String r7 = "mtopsdk.MtopBusiness"
            java.lang.String r1 = r5.seqNo
            java.lang.String r2 = "listener onError callback error"
            mtopsdk.common.util.TBSdkLog.e(r7, r1, r2, r6)
        L_0x00c9:
            mtopsdk.common.util.TBSdkLog$LogEnable r6 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r6 = mtopsdk.common.util.TBSdkLog.isLogEnable(r6)
            if (r6 == 0) goto L_0x00f0
            java.lang.String r6 = "mtopsdk.MtopBusiness"
            java.lang.String r7 = r5.seqNo
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "listener onError callback, "
            r1.append(r2)
            if (r0 == 0) goto L_0x00e4
            java.lang.String r0 = "sys error"
            goto L_0x00e6
        L_0x00e4:
            java.lang.String r0 = "biz error"
        L_0x00e6:
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r6, (java.lang.String) r7, (java.lang.String) r0)
        L_0x00f0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.remotebusiness.MtopBusiness.doErrorCallback(mtopsdk.mtop.domain.MtopResponse, com.taobao.tao.remotebusiness.IRemoteListener):void");
    }

    @Deprecated
    public MtopBusiness setBizId(int i) {
        return (MtopBusiness) super.setBizId(i);
    }

    public MtopBusiness setBizId(String str) {
        return (MtopBusiness) super.setBizId(str);
    }

    public MtopBusiness ttid(String str) {
        return (MtopBusiness) super.ttid(str);
    }

    public MtopBusiness useCache() {
        return (MtopBusiness) super.useCache();
    }

    public MtopBusiness useWua() {
        return (MtopBusiness) super.useWua();
    }

    @Deprecated
    public MtopBusiness useWua(int i) {
        return (MtopBusiness) super.useWua(i);
    }

    public MtopBusiness addHttpQueryParameter(String str, String str2) {
        return (MtopBusiness) super.addHttpQueryParameter(str, str2);
    }

    public MtopBusiness addCacheKeyParamBlackList(List<String> list) {
        return (MtopBusiness) super.addCacheKeyParamBlackList(list);
    }

    public MtopBusiness addMteeUa(String str) {
        return (MtopBusiness) super.addMteeUa(str);
    }

    public MtopBusiness enableProgressListener() {
        return (MtopBusiness) super.enableProgressListener();
    }

    public MtopBusiness forceRefreshCache() {
        return (MtopBusiness) super.forceRefreshCache();
    }

    public MtopBusiness handler(Handler handler) {
        return (MtopBusiness) super.handler(handler);
    }

    public MtopBusiness headers(Map<String, String> map) {
        return (MtopBusiness) super.headers(map);
    }

    public MtopBusiness protocol(ProtocolEnum protocolEnum) {
        return (MtopBusiness) super.protocol(protocolEnum);
    }

    public MtopBusiness reqContext(Object obj) {
        return (MtopBusiness) super.reqContext(obj);
    }

    public MtopBusiness reqMethod(MethodEnum methodEnum) {
        return (MtopBusiness) super.reqMethod(methodEnum);
    }

    public MtopBusiness retryTime(int i) {
        return (MtopBusiness) super.retryTime(i);
    }

    public MtopBusiness setCacheControlNoCache() {
        return (MtopBusiness) super.setCacheControlNoCache();
    }

    public MtopBusiness setConnectionTimeoutMilliSecond(int i) {
        return (MtopBusiness) super.setConnectionTimeoutMilliSecond(i);
    }

    public MtopBusiness setCustomDomain(String str) {
        return (MtopBusiness) super.setCustomDomain(str);
    }

    public MtopBusiness setCustomDomain(String str, String str2, String str3) {
        return (MtopBusiness) super.setCustomDomain(str, str2, str3);
    }

    public MtopBusiness setUnitStrategy(String str) {
        return (MtopBusiness) super.setUnitStrategy(str);
    }

    public MtopBusiness setJsonType(JsonTypeEnum jsonTypeEnum) {
        return (MtopBusiness) super.setJsonType(jsonTypeEnum);
    }

    public MtopBusiness setNetInfo(int i) {
        return (MtopBusiness) super.setNetInfo(i);
    }

    public MtopBusiness setPageUrl(String str) {
        return (MtopBusiness) super.setPageUrl(str);
    }

    public MtopBusiness setPageName(String str) {
        return (MtopBusiness) super.setPageName(str);
    }

    public MtopBusiness setReqAppKey(String str, String str2) {
        return (MtopBusiness) super.setReqAppKey(str, str2);
    }

    public MtopBusiness setReqBizExt(String str) {
        return (MtopBusiness) super.setReqBizExt(str);
    }

    public MtopBusiness setReqSource(int i) {
        return (MtopBusiness) super.setReqSource(i);
    }

    public MtopBusiness setReqUserId(String str) {
        return (MtopBusiness) super.setReqUserId(str);
    }

    public MtopBusiness setSocketTimeoutMilliSecond(int i) {
        return (MtopBusiness) super.setSocketTimeoutMilliSecond(i);
    }

    public MtopBusiness setPriorityFlag(boolean z) {
        this.mtopProp.priorityFlag = z;
        return this;
    }

    public MtopBusiness setPriorityData(Map<String, String> map) {
        this.mtopProp.priorityData = map;
        return this;
    }

    public MtopBusiness setUserInfo(@Nullable String str) {
        return (MtopBusiness) super.setUserInfo(str);
    }

    private String getRequestLogInfo(String str, MtopBusiness mtopBusiness) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(str);
        sb.append(" [");
        if (mtopBusiness != null) {
            sb.append("apiName=");
            sb.append(mtopBusiness.request.getApiName());
            sb.append(";version=");
            sb.append(mtopBusiness.request.getVersion());
            sb.append(";requestType=");
            sb.append(mtopBusiness.getRequestType());
        }
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }

    public MtopBusiness prefetch(long j, List<String> list, MtopPrefetch.IPrefetchCallback iPrefetchCallback) {
        return (MtopBusiness) super.prefetch(j, list, iPrefetchCallback);
    }

    public MtopBusiness prefetch(long j, MtopPrefetch.IPrefetchCallback iPrefetchCallback) {
        return (MtopBusiness) super.prefetch(j, iPrefetchCallback);
    }

    public MtopBusiness prefetch() {
        return (MtopBusiness) super.prefetch(0, (MtopPrefetch.IPrefetchCallback) null);
    }

    public MtopBusiness prefetchComparator(MtopPrefetch.IPrefetchComparator iPrefetchComparator) {
        return (MtopBusiness) super.prefetchComparator(iPrefetchComparator);
    }
}
