package android.taobao.windvane.extra.uc;

import android.taobao.windvane.util.TaoLog;

import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.internal.interfaces.EventHandler;
import com.uc.webview.export.internal.interfaces.IRequest;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import anetwork.channel.Request;
import anetwork.channel.Response;

public class AliRequestAdapter implements IRequest {
    public static final String PHASE_ENDDATA = "enddata";
    private static final String PHASE_NORMAL = "normal";
    public static final String PHASE_RELOAD = "reload";
    public static final String PHASE_STOP = "stop";
    public static int connectTimeout = 10000;
    public static int readTimeout = 10000;
    public static int retryTimes = 1;
    String TAG = "alinetwork";
    private String bizCode;
    public String cancelPhase = "normal";
    Request mAliRequest;
    private final Object mClientResource = new Object();
    private EventHandler mEventHandler;
    Future<Response> mFutureResponse;
    private Map<String, String> mHeaders;
    private boolean mIsUCProxy;
    private boolean mIsUseWebP;
    private int mLoadType;
    private String mMethod = "GET";
    private int mRequestType;
    private Map<String, String> mUCHeaders;
    private Map<String, byte[]> mUploadDataMap;
    private Map<String, String> mUploadFileMap;
    private long mUploadFileTotalLen;
    private String mUrl;

    public void handleSslErrorResponse(boolean z) {
    }

    AliRequestAdapter(Request request, EventHandler eventHandler) {
        this.mEventHandler = eventHandler;
    }

    AliRequestAdapter(EventHandler eventHandler, String str, String str2, boolean z, Map<String, String> map, Map<String, String> map2, Map<String, String> map3, Map<String, byte[]> map4, long j, int i, int i2, boolean z2, String str3) {
        this.mIsUseWebP = z2;
        this.mEventHandler = eventHandler;
        this.mUrl = str;
        this.mMethod = str2;
        this.mIsUCProxy = z;
        this.mHeaders = map;
        this.mUCHeaders = map2;
        this.mUploadFileMap = map3;
        this.mUploadDataMap = map4;
        this.mUploadFileTotalLen = j;
        this.mRequestType = i;
        this.mLoadType = i2;
        this.bizCode = str3;
        this.mAliRequest = formatAliRequest();
    }

    private Request formatAliRequest() {
        return formatAliRequest(this.mUrl, this.mMethod, this.mIsUCProxy, this.mHeaders, this.mUCHeaders, this.mUploadFileMap, this.mUploadDataMap, this.mUploadFileTotalLen, this.mRequestType, this.mLoadType, this.mIsUseWebP);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0032 */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x005b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private anetwork.channel.Request formatAliRequest(java.lang.String r1, java.lang.String r2, boolean r3, java.util.Map<java.lang.String, java.lang.String> r4, java.util.Map<java.lang.String, java.lang.String> r5, java.util.Map<java.lang.String, java.lang.String> r6, java.util.Map<java.lang.String, byte[]> r7, long r8, int r10, int r11, boolean r12) {
        /*
            r0 = this;
            if (r12 == 0) goto L_0x0032
            boolean r3 = android.taobao.windvane.util.CommonUtils.isPicture(r1)     // Catch:{ Throwable -> 0x0032 }
            if (r3 == 0) goto L_0x0032
            java.lang.String r3 = com.taobao.tao.util.ImageStrategyDecider.justConvergeAndWebP(r1)     // Catch:{ Throwable -> 0x0032 }
            boolean r5 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x0032 }
            if (r5 != 0) goto L_0x0032
            boolean r5 = r3.equals(r1)     // Catch:{ Throwable -> 0x0032 }
            if (r5 != 0) goto L_0x0032
            java.lang.String r5 = r0.TAG     // Catch:{ Throwable -> 0x0032 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0032 }
            r6.<init>()     // Catch:{ Throwable -> 0x0032 }
            r6.append(r1)     // Catch:{ Throwable -> 0x0032 }
            java.lang.String r7 = " decideUrl to : "
            r6.append(r7)     // Catch:{ Throwable -> 0x0032 }
            r6.append(r3)     // Catch:{ Throwable -> 0x0032 }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0032 }
            android.taobao.windvane.util.TaoLog.i(r5, r6)     // Catch:{ Throwable -> 0x0032 }
            r1 = r3
        L_0x0032:
            anetwork.channel.entity.RequestImpl r3 = new anetwork.channel.entity.RequestImpl     // Catch:{ Exception -> 0x00c5 }
            r3.<init>((java.lang.String) r1)     // Catch:{ Exception -> 0x00c5 }
            r5 = 0
            r3.setFollowRedirects(r5)     // Catch:{ Exception -> 0x00c5 }
            java.lang.String r5 = r0.bizCode     // Catch:{ Exception -> 0x00c5 }
            r3.setBizId((java.lang.String) r5)     // Catch:{ Exception -> 0x00c5 }
            int r5 = retryTimes     // Catch:{ Exception -> 0x00c5 }
            r3.setRetryTime(r5)     // Catch:{ Exception -> 0x00c5 }
            int r5 = connectTimeout     // Catch:{ Exception -> 0x00c5 }
            r3.setConnectTimeout(r5)     // Catch:{ Exception -> 0x00c5 }
            int r5 = readTimeout     // Catch:{ Exception -> 0x00c5 }
            r3.setReadTimeout(r5)     // Catch:{ Exception -> 0x00c5 }
            boolean r5 = android.taobao.windvane.extra.uc.WVUCWebView.isNeedCookie(r1)     // Catch:{ Exception -> 0x00c5 }
            r3.setCookieEnabled(r5)     // Catch:{ Exception -> 0x00c5 }
            r3.setMethod(r2)     // Catch:{ Exception -> 0x00c5 }
            if (r4 == 0) goto L_0x00b1
            java.lang.String r2 = "f-refer"
            java.lang.String r5 = "wv_h5"
            r3.addHeader(r2, r5)
            android.taobao.windvane.extra.uc.UCNetworkDelegate r2 = android.taobao.windvane.extra.uc.UCNetworkDelegate.getInstance()
            r2.onSendRequest(r4, r1)
            java.util.Set r1 = r4.entrySet()
            java.util.Iterator r1 = r1.iterator()
        L_0x0071:
            boolean r2 = r1.hasNext()
            if (r2 == 0) goto L_0x00b1
            java.lang.Object r2 = r1.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r4 = r2.getKey()
            java.lang.String r4 = (java.lang.String) r4
            java.lang.Object r2 = r2.getValue()
            java.lang.String r2 = (java.lang.String) r2
            boolean r5 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r5 == 0) goto L_0x00ad
            java.lang.String r5 = r0.TAG
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "AliRequestAdapter from uc header key="
            r6.append(r7)
            r6.append(r4)
            java.lang.String r7 = ",value="
            r6.append(r7)
            r6.append(r2)
            java.lang.String r6 = r6.toString()
            android.taobao.windvane.util.TaoLog.d(r5, r6)
        L_0x00ad:
            r3.addHeader(r4, r2)
            goto L_0x0071
        L_0x00b1:
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            if (r1 == 0) goto L_0x00c4
            android.taobao.windvane.monitor.WVPerformanceMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPerformanceMonitor()
            java.lang.String r2 = r0.mUrl
            long r4 = java.lang.System.currentTimeMillis()
            r1.didResourceStartLoadAtTime(r2, r4)
        L_0x00c4:
            return r3
        L_0x00c5:
            r1 = move-exception
            java.lang.String r2 = r0.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = " AliRequestAdapter formatAliRequest Exception"
            r3.append(r4)
            java.lang.String r1 = r1.getMessage()
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            android.taobao.windvane.util.TaoLog.e(r2, r1)
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.AliRequestAdapter.formatAliRequest(java.lang.String, java.lang.String, boolean, java.util.Map, java.util.Map, java.util.Map, java.util.Map, long, int, int, boolean):anetwork.channel.Request");
    }

    public void setFutureResponse(Future<Response> future) {
        this.mFutureResponse = future;
    }

    /* access modifiers changed from: package-private */
    public void complete() {
        WVUCWebView.isStop = false;
        if (this.mEventHandler.isSynchronous()) {
            synchronized (this.mClientResource) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(this.TAG, "AliRequestAdapter complete will notify");
                }
                this.mClientResource.notifyAll();
            }
        }
    }

    public void cancel() {
        if (WVUCWebView.isStop) {
            this.cancelPhase = "stop";
        }
        String str = this.TAG;
        TaoLog.e(str, "cancel id= " + this.mEventHandler.hashCode() + ", phase:[" + this.cancelPhase + Operators.ARRAY_END_STR);
        try {
            if (!(!TaoLog.getLogStatus() || this.mFutureResponse == null || this.mFutureResponse.get() == null)) {
                String str2 = this.TAG;
                TaoLog.d(str2, "AliRequestAdapter cancel desc url=" + this.mFutureResponse.get().getDesc());
            }
            complete();
        } catch (InterruptedException e) {
            e.printStackTrace();
            String str3 = this.TAG;
            TaoLog.d(str3, "AliRequestAdapter cancel =" + e.getMessage());
        } catch (ExecutionException e2) {
            e2.printStackTrace();
            String str4 = this.TAG;
            TaoLog.d(str4, "AliRequestAdapter cancel =" + e2.getMessage());
        }
        if (this.mFutureResponse != null) {
            this.mFutureResponse.cancel(true);
        }
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.mEventHandler = eventHandler;
    }

    public EventHandler getEventHandler() {
        return this.mEventHandler;
    }

    public int getRequestType() {
        return this.mRequestType;
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public Map<String, String> getUCHeaders() {
        return this.mUCHeaders;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x003a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void waitUntilComplete(int r5) {
        /*
            r4 = this;
            com.uc.webview.export.internal.interfaces.EventHandler r0 = r4.mEventHandler
            boolean r0 = r0.isSynchronous()
            if (r0 == 0) goto L_0x003e
            java.lang.Object r0 = r4.mClientResource
            monitor-enter(r0)
            boolean r1 = android.taobao.windvane.util.TaoLog.getLogStatus()     // Catch:{ InterruptedException -> 0x003a }
            if (r1 == 0) goto L_0x0031
            java.lang.String r1 = r4.TAG     // Catch:{ InterruptedException -> 0x003a }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x003a }
            r2.<init>()     // Catch:{ InterruptedException -> 0x003a }
            java.lang.String r3 = "AliRequestAdapter waitUntilComplete timeout="
            r2.append(r3)     // Catch:{ InterruptedException -> 0x003a }
            r2.append(r5)     // Catch:{ InterruptedException -> 0x003a }
            java.lang.String r3 = ",url="
            r2.append(r3)     // Catch:{ InterruptedException -> 0x003a }
            java.lang.String r3 = r4.mUrl     // Catch:{ InterruptedException -> 0x003a }
            r2.append(r3)     // Catch:{ InterruptedException -> 0x003a }
            java.lang.String r2 = r2.toString()     // Catch:{ InterruptedException -> 0x003a }
            android.taobao.windvane.util.TaoLog.d(r1, r2)     // Catch:{ InterruptedException -> 0x003a }
        L_0x0031:
            java.lang.Object r1 = r4.mClientResource     // Catch:{ InterruptedException -> 0x003a }
            long r2 = (long) r5     // Catch:{ InterruptedException -> 0x003a }
            r1.wait(r2)     // Catch:{ InterruptedException -> 0x003a }
            goto L_0x003a
        L_0x0038:
            r5 = move-exception
            goto L_0x003c
        L_0x003a:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            goto L_0x003e
        L_0x003c:
            monitor-exit(r0)     // Catch:{ all -> 0x0038 }
            throw r5
        L_0x003e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.AliRequestAdapter.waitUntilComplete(int):void");
    }

    public Request getAliRequest() {
        return this.mAliRequest;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public String getMethod() {
        return this.mMethod;
    }

    public boolean getIsUCProxy() {
        return this.mIsUCProxy;
    }

    public Map<String, String> getUploadFileMap() {
        return this.mUploadFileMap;
    }

    public Map<String, byte[]> getUploadDataMap() {
        return this.mUploadDataMap;
    }

    public long getUploadFileTotalLen() {
        return this.mUploadFileTotalLen;
    }

    public int getLoadtype() {
        return this.mLoadType;
    }
}
