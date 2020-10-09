package android.taobao.windvane.extra.uc;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import com.uc.webview.export.internal.interfaces.EventHandler;
import com.uc.webview.export.internal.interfaces.INetwork;
import com.uc.webview.export.internal.interfaces.IRequest;

import org.apache.http.ParseException;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import anet.channel.entity.ConnType;
import anetwork.channel.IBodyHandler;
import anetwork.channel.Network;
import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.Request;
import anetwork.channel.Response;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.http.HttpNetwork;

public class AliNetworkAdapter implements INetwork {
    public static final int NETWORK_WORKING_MODE_ASYNC = 1;
    public static final int NETWORK_WORKING_MODE_SYNC = 0;
    /* access modifiers changed from: private */
    public int BUFFER_SIZE;
    String LOGTAG;
    private String bizCode;
    public boolean isReload;
    public boolean isStop;
    private boolean isUseWebpImg;
    private Network mAliNetwork;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mNetworkType;
    private int mWorkingMode;
    public HashSet<EventHandler> mainRequest;

    public void cancelPrefetchLoad() {
    }

    public void clearUserSslPrefTable() {
    }

    public int getNetworkType() {
        return 1;
    }

    public String getVersion() {
        return "1.0";
    }

    public AliNetworkAdapter(Context context) {
        this(context, "windvane");
    }

    public AliNetworkAdapter(Context context, String str) {
        this(context, 2, str);
    }

    public AliNetworkAdapter(Context context, int i) {
        this(context, i, "windvane");
    }

    public AliNetworkAdapter(Context context, int i, String str) {
        this.LOGTAG = "AliNetwork";
        this.mNetworkType = -1;
        this.mWorkingMode = 1;
        this.BUFFER_SIZE = 1024;
        this.isUseWebpImg = true;
        this.bizCode = "";
        this.isReload = false;
        this.isStop = false;
        this.mainRequest = new HashSet<>();
        this.mContext = context;
        this.mNetworkType = i;
        this.bizCode = str;
        if (WVCommonConfig.commonConfig.ucsdk_image_strategy_rate > Math.random()) {
            this.isUseWebpImg = true;
        } else {
            this.isUseWebpImg = false;
        }
        switch (this.mNetworkType) {
            case 0:
                this.mAliNetwork = new HttpNetwork(context);
                return;
            case 1:
            case 2:
                this.mAliNetwork = new DegradableNetwork(context);
                return;
            default:
                return;
        }
    }

    public boolean requestURL(EventHandler eventHandler, String str, String str2, boolean z, Map<String, String> map, Map<String, String> map2, Map<String, String> map3, Map<String, byte[]> map4, long j, int i, int i2) {
        String str3 = this.LOGTAG;
        TaoLog.d(str3, "requestURL:" + str + " isUCProxyReq:" + z + " requestType:" + i);
        AliRequestAdapter aliRequestAdapter = new AliRequestAdapter(eventHandler, Escape.tryDecodeUrl(str), str2, z, map, map2, map3, map4, j, i, i2, this.isUseWebpImg, this.bizCode);
        if (this.isReload) {
            aliRequestAdapter.cancelPhase = "reload";
        }
        setRequestBodyHandler(aliRequestAdapter.getAliRequest(), aliRequestAdapter);
        eventHandler.setRequest(aliRequestAdapter);
        return sendRequestInternal(aliRequestAdapter);
    }

    public static boolean willLog(EventHandler eventHandler) {
        int resourceType = eventHandler.getResourceType();
        return resourceType == 0 || resourceType == 14 || eventHandler.isSynchronous();
    }

    private boolean sendRequestInternal(AliRequestAdapter aliRequestAdapter) {
        Request aliRequest = aliRequestAdapter.getAliRequest();
        EventHandler eventHandler = aliRequestAdapter.getEventHandler();
        if (TaoLog.getLogStatus()) {
            String str = this.LOGTAG;
            TaoLog.d(str, "requestURL eventId=" + aliRequestAdapter.getEventHandler().hashCode() + ", url=" + aliRequestAdapter.getUrl() + ",isSync=" + eventHandler.isSynchronous());
        }
        if (this.mWorkingMode == 0) {
            Response syncSend = this.mAliNetwork.syncSend(aliRequest, (Object) null);
            Throwable error = syncSend.getError();
            if (error != null) {
                eventHandler.error(getErrorFromException(error), error.toString());
            } else {
                int statusCode = syncSend.getStatusCode();
                String str2 = syncSend.getStatisticData().connectionType;
                if (TextUtils.isEmpty(str2) || !str2.startsWith(ConnType.HTTP2)) {
                    eventHandler.status(0, 0, statusCode, "");
                } else {
                    eventHandler.status(2, 0, statusCode, "");
                }
                if (TaoLog.getLogStatus()) {
                    String str3 = this.LOGTAG;
                    TaoLog.d(str3, "status code=" + statusCode);
                }
                eventHandler.headers(syncSend.getConnHeadFields());
                byte[] bytedata = syncSend.getBytedata();
                if (bytedata != null) {
                    eventHandler.data(bytedata, bytedata.length);
                }
                aliRequestAdapter.cancelPhase = AliRequestAdapter.PHASE_ENDDATA;
                eventHandler.endData();
            }
        } else if (this.mWorkingMode == 1) {
            AliNetCallback aliNetCallback = new AliNetCallback();
            aliNetCallback.setEventHandler(eventHandler);
            aliNetCallback.setURL(aliRequestAdapter.getUrl());
            aliNetCallback.setRequest(aliRequestAdapter);
            aliRequestAdapter.setFutureResponse(this.mAliNetwork.asyncSend(aliRequest, (Object) null, (Handler) null, aliNetCallback));
        }
        return true;
    }

    protected class AliNetCallback implements NetworkCallBack.FinishListener, NetworkCallBack.ResponseCodeListener, NetworkCallBack.ProgressListener {
        EventHandler mEventHandler;
        IRequest mReq;
        String mUrl;
        int size = 0;
        int total = 0;

        protected AliNetCallback() {
        }

        public void setEventHandler(EventHandler eventHandler) {
            this.mEventHandler = eventHandler;
        }

        public void setURL(String str) {
            this.mUrl = str;
        }

        public void setRequest(IRequest iRequest) {
            this.mReq = iRequest;
        }

        public void onDataReceived(NetworkEvent.ProgressEvent progressEvent, Object obj) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "[onDataReceived] event:" + progressEvent + "event.getSize():" + progressEvent.getSize() + ", data:" + progressEvent.getBytedata().length + " bytes");
            }
            this.mEventHandler.data(progressEvent.getBytedata(), progressEvent.getSize());
            this.size += progressEvent.getSize();
            if (this.total == 0) {
                this.total = progressEvent.getTotal();
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:51:0x0113  */
        /* JADX WARNING: Removed duplicated region for block: B:57:0x0141  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onResponseCode(int r7, java.util.Map<java.lang.String, java.util.List<java.lang.String>> r8, java.lang.Object r9) {
            /*
                r6 = this;
                r9 = 0
                java.lang.String r0 = r6.mUrl     // Catch:{ Throwable -> 0x00df }
                java.lang.String r0 = android.taobao.windvane.util.WVUrlUtil.removeQueryParam(r0)     // Catch:{ Throwable -> 0x00df }
                java.lang.String r0 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r0)     // Catch:{ Throwable -> 0x00df }
                com.uc.webview.export.internal.interfaces.IRequest r1 = r6.mReq     // Catch:{ Throwable -> 0x00df }
                java.util.Map r1 = r1.getHeaders()     // Catch:{ Throwable -> 0x00df }
                java.lang.String r2 = ""
                if (r1 == 0) goto L_0x002c
                java.lang.String r2 = "Referer"
                java.lang.Object r2 = r1.get(r2)     // Catch:{ Throwable -> 0x00df }
                java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x00df }
                boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x00df }
                if (r3 == 0) goto L_0x002c
                java.lang.String r2 = "referer"
                java.lang.Object r1 = r1.get(r2)     // Catch:{ Throwable -> 0x00df }
                r2 = r1
                java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x00df }
            L_0x002c:
                java.lang.String r1 = "0"
                if (r0 == 0) goto L_0x004a
                java.lang.String r3 = ".htm"
                boolean r3 = r0.endsWith(r3)     // Catch:{ Throwable -> 0x00df }
                if (r3 != 0) goto L_0x0048
                java.lang.String r3 = ".html"
                boolean r3 = r0.endsWith(r3)     // Catch:{ Throwable -> 0x00df }
                if (r3 != 0) goto L_0x0048
                java.lang.String r3 = "/"
                boolean r3 = r0.endsWith(r3)     // Catch:{ Throwable -> 0x00df }
                if (r3 == 0) goto L_0x004a
            L_0x0048:
                java.lang.String r1 = "1"
            L_0x004a:
                r3 = 200(0xc8, float:2.8E-43)
                if (r7 < r3) goto L_0x0052
                r3 = 304(0x130, float:4.26E-43)
                if (r7 <= r3) goto L_0x0056
            L_0x0052:
                r3 = 307(0x133, float:4.3E-43)
                if (r7 != r3) goto L_0x00cd
            L_0x0056:
                r3 = 302(0x12e, float:4.23E-43)
                if (r7 != r3) goto L_0x00fc
                java.lang.String r3 = ""
                if (r8 == 0) goto L_0x0086
                java.lang.String r4 = "Location"
                java.lang.Object r4 = r8.get(r4)     // Catch:{ Throwable -> 0x00df }
                java.util.List r4 = (java.util.List) r4     // Catch:{ Throwable -> 0x00df }
                if (r4 != 0) goto L_0x0070
                java.lang.String r4 = "location"
                java.lang.Object r4 = r8.get(r4)     // Catch:{ Throwable -> 0x00df }
                java.util.List r4 = (java.util.List) r4     // Catch:{ Throwable -> 0x00df }
            L_0x0070:
                if (r4 == 0) goto L_0x0086
                java.lang.Object r3 = r4.get(r9)     // Catch:{ Throwable -> 0x00df }
                java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x00df }
                boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x00df }
                if (r4 != 0) goto L_0x0086
                java.lang.String r3 = android.taobao.windvane.util.WVUrlUtil.removeQueryParam(r3)     // Catch:{ Throwable -> 0x00df }
                java.lang.String r3 = android.taobao.windvane.util.WVUrlUtil.removeScheme(r3)     // Catch:{ Throwable -> 0x00df }
            L_0x0086:
                boolean r4 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x00df }
                if (r4 != 0) goto L_0x00fc
                java.lang.String r4 = "//err.tmall.com/error1.html"
                boolean r4 = r3.equals(r4)     // Catch:{ Throwable -> 0x00df }
                if (r4 != 0) goto L_0x00b9
                java.lang.String r4 = "//err.taobao.com/error1.html"
                boolean r4 = r3.equals(r4)     // Catch:{ Throwable -> 0x00df }
                if (r4 == 0) goto L_0x009d
                goto L_0x00b9
            L_0x009d:
                java.lang.String r4 = "//err.tmall.com/error2.html"
                boolean r3 = r3.equals(r4)     // Catch:{ Throwable -> 0x00df }
                if (r3 == 0) goto L_0x00fc
                java.lang.String r3 = r6.mUrl     // Catch:{ Throwable -> 0x00df }
                r4 = 500(0x1f4, float:7.0E-43)
                java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Throwable -> 0x00df }
                android.taobao.windvane.extra.uc.UCNetworkDelegate r5 = android.taobao.windvane.extra.uc.UCNetworkDelegate.getInstance()     // Catch:{ Throwable -> 0x00df }
                java.lang.String r0 = r5.getBizCodeByUrl(r0)     // Catch:{ Throwable -> 0x00df }
                android.taobao.windvane.monitor.AppMonitorUtil.commitStatusCode(r3, r2, r4, r1, r0)     // Catch:{ Throwable -> 0x00df }
                goto L_0x00fc
            L_0x00b9:
                java.lang.String r3 = r6.mUrl     // Catch:{ Throwable -> 0x00df }
                r4 = 404(0x194, float:5.66E-43)
                java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Throwable -> 0x00df }
                android.taobao.windvane.extra.uc.UCNetworkDelegate r5 = android.taobao.windvane.extra.uc.UCNetworkDelegate.getInstance()     // Catch:{ Throwable -> 0x00df }
                java.lang.String r0 = r5.getBizCodeByUrl(r0)     // Catch:{ Throwable -> 0x00df }
                android.taobao.windvane.monitor.AppMonitorUtil.commitStatusCode(r3, r2, r4, r1, r0)     // Catch:{ Throwable -> 0x00df }
                goto L_0x00fc
            L_0x00cd:
                java.lang.String r3 = r6.mUrl     // Catch:{ Throwable -> 0x00df }
                java.lang.String r4 = java.lang.String.valueOf(r7)     // Catch:{ Throwable -> 0x00df }
                android.taobao.windvane.extra.uc.UCNetworkDelegate r5 = android.taobao.windvane.extra.uc.UCNetworkDelegate.getInstance()     // Catch:{ Throwable -> 0x00df }
                java.lang.String r0 = r5.getBizCodeByUrl(r0)     // Catch:{ Throwable -> 0x00df }
                android.taobao.windvane.monitor.AppMonitorUtil.commitStatusCode(r3, r2, r4, r1, r0)     // Catch:{ Throwable -> 0x00df }
                goto L_0x00fc
            L_0x00df:
                r0 = move-exception
                android.taobao.windvane.extra.uc.AliNetworkAdapter r1 = android.taobao.windvane.extra.uc.AliNetworkAdapter.this
                java.lang.String r1 = r1.LOGTAG
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "AppMonitorUtil.commitStatusCode error : "
                r2.append(r3)
                java.lang.String r0 = r0.getMessage()
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                android.taobao.windvane.util.TaoLog.e(r1, r0)
            L_0x00fc:
                java.lang.String r0 = "x-protocol"
                boolean r0 = r8.containsKey(r0)
                r1 = 2
                if (r0 == 0) goto L_0x0141
                java.lang.String r0 = "x-protocol"
                java.lang.Object r0 = r8.get(r0)
                java.util.List r0 = (java.util.List) r0
                int r0 = r0.size()
                if (r0 == 0) goto L_0x0141
                java.lang.String r0 = "x-protocol"
                java.lang.Object r0 = r8.get(r0)
                java.util.List r0 = (java.util.List) r0
                java.lang.Object r0 = r0.get(r9)
                java.lang.String r0 = (java.lang.String) r0
                java.lang.String r2 = "http"
                boolean r2 = r0.equals(r2)
                if (r2 != 0) goto L_0x0139
                java.lang.String r2 = "https"
                boolean r0 = r0.equals(r2)
                if (r0 != 0) goto L_0x0139
                com.uc.webview.export.internal.interfaces.EventHandler r0 = r6.mEventHandler
                java.lang.String r2 = ""
                r0.status(r1, r9, r7, r2)
                goto L_0x0158
            L_0x0139:
                com.uc.webview.export.internal.interfaces.EventHandler r0 = r6.mEventHandler
                java.lang.String r1 = ""
                r0.status(r9, r9, r7, r1)
                goto L_0x0158
            L_0x0141:
                java.lang.String r0 = ":status"
                boolean r0 = r8.containsKey(r0)
                if (r0 == 0) goto L_0x0151
                com.uc.webview.export.internal.interfaces.EventHandler r0 = r6.mEventHandler
                java.lang.String r2 = ""
                r0.status(r1, r9, r7, r2)
                goto L_0x0158
            L_0x0151:
                com.uc.webview.export.internal.interfaces.EventHandler r0 = r6.mEventHandler
                java.lang.String r1 = ""
                r0.status(r9, r9, r7, r1)
            L_0x0158:
                com.uc.webview.export.internal.interfaces.EventHandler r7 = r6.mEventHandler
                r7.headers((java.util.Map<java.lang.String, java.util.List<java.lang.String>>) r8)
                return r9
            */
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.AliNetworkAdapter.AliNetCallback.onResponseCode(int, java.util.Map, java.lang.Object):boolean");
        }

        public void onFinished(NetworkEvent.FinishEvent finishEvent, Object obj) {
            if (TaoLog.getLogStatus()) {
                String str = AliNetworkAdapter.this.LOGTAG;
                TaoLog.d(str, "[onFinished] event:" + finishEvent);
                String str2 = AliNetworkAdapter.this.LOGTAG;
                TaoLog.e(str2, "onFinished code = " + finishEvent.getHttpCode() + ", url = " + this.mUrl + ", rev_size = " + this.size + ", total_size = " + this.total);
            }
            if (WVMonitorService.getPerformanceMonitor() != null) {
                try {
                    String str3 = finishEvent.getStatisticData().connectionType;
                    long j = finishEvent.getStatisticData().tcpConnTime;
                    WVMonitorService.getPerformanceMonitor().didResourceFinishLoadAtTime(this.mUrl, System.currentTimeMillis(), str3, 0);
                } catch (Throwable th) {
                    WVMonitorService.getPerformanceMonitor().didResourceFinishLoadAtTime(this.mUrl, System.currentTimeMillis(), "unknown", 0);
                    String str4 = AliNetworkAdapter.this.LOGTAG;
                    TaoLog.e(str4, "didResourceFinishLoadAtTime failed : " + th.getMessage());
                }
            }
            AliRequestAdapter aliRequestAdapter = (AliRequestAdapter) this.mReq;
            int httpCode = finishEvent.getHttpCode();
            UCNetworkDelegate.getInstance().onFinish(httpCode, this.mUrl);
            if (httpCode < 0) {
                if (TaoLog.getLogStatus()) {
                    String str5 = AliNetworkAdapter.this.LOGTAG;
                    TaoLog.e(str5, "error code=" + httpCode + ",desc=" + finishEvent.getDesc() + ",url=" + this.mUrl);
                }
                this.mEventHandler.error(httpCode, finishEvent.getDesc());
                aliRequestAdapter.complete();
                return;
            }
            if (TaoLog.getLogStatus()) {
                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "endData");
            }
            aliRequestAdapter.cancelPhase = AliRequestAdapter.PHASE_ENDDATA;
            this.mEventHandler.endData();
            aliRequestAdapter.complete();
        }
    }

    private void setRequestBodyHandler(Request request, AliRequestAdapter aliRequestAdapter) {
        if (aliRequestAdapter.getUploadFileTotalLen() != 0) {
            final Map<String, String> uploadFileMap = aliRequestAdapter.getUploadFileMap();
            final Map<String, byte[]> uploadDataMap = aliRequestAdapter.getUploadDataMap();
            final int size = uploadFileMap.size() + uploadDataMap.size();
            request.setBodyHandler(new IBodyHandler() {
                byte[] buffer = new byte[AliNetworkAdapter.this.BUFFER_SIZE];
                int curFilenum = 0;
                byte[] dataValue = null;
                String fileNameValue = null;
                boolean hasInitilized = false;
                List<InputStream> instream = new ArrayList(size);
                boolean isCompleted = false;
                int postedLen = 0;

                public void initStream() {
                    try {
                        this.curFilenum = 0;
                        while (this.curFilenum < size) {
                            this.fileNameValue = (String) uploadFileMap.get(String.valueOf(this.curFilenum));
                            this.dataValue = (byte[]) uploadDataMap.get(String.valueOf(this.curFilenum));
                            if (TaoLog.getLogStatus() && this.dataValue != null) {
                                TaoLog.d(AliNetworkAdapter.this.LOGTAG, "len =" + this.dataValue.length + ",datavalue=" + new String(this.dataValue, 0, this.dataValue.length));
                            }
                            if (this.fileNameValue == null) {
                                this.instream.add(this.curFilenum, new ByteArrayInputStream(this.dataValue));
                            } else if (this.fileNameValue.toLowerCase().startsWith("content://")) {
                                this.instream.add(this.curFilenum, AliNetworkAdapter.this.mContext.getContentResolver().openInputStream(Uri.parse(this.fileNameValue)));
                            } else {
                                this.instream.add(this.curFilenum, new FileInputStream(this.fileNameValue));
                            }
                            this.curFilenum++;
                        }
                    } catch (Exception unused) {
                    }
                }

                public int read(byte[] bArr) {
                    if (!this.hasInitilized) {
                        initStream();
                        if (this.instream == null || this.instream.size() == 0) {
                            this.isCompleted = true;
                            return 0;
                        }
                        this.hasInitilized = true;
                    }
                    for (InputStream read : this.instream) {
                        try {
                            int read2 = read.read(this.buffer, 0, AliNetworkAdapter.this.BUFFER_SIZE > bArr.length ? bArr.length : AliNetworkAdapter.this.BUFFER_SIZE);
                            if (read2 != -1) {
                                System.arraycopy(this.buffer, 0, bArr, 0, read2);
                                this.postedLen += read2;
                                TaoLog.i(AliNetworkAdapter.this.LOGTAG, "read len=" + read2);
                                return read2;
                            }
                        } catch (Exception e) {
                            TaoLog.i(AliNetworkAdapter.this.LOGTAG, "read exception" + e.getMessage());
                            this.isCompleted = true;
                            return 0;
                        }
                    }
                    TaoLog.i(AliNetworkAdapter.this.LOGTAG, "read finish");
                    this.isCompleted = true;
                    return 0;
                }

                public boolean isCompleted() {
                    if (!this.isCompleted) {
                        return this.isCompleted;
                    }
                    this.isCompleted = false;
                    this.hasInitilized = false;
                    try {
                        for (InputStream close : this.instream) {
                            close.close();
                        }
                        this.instream.clear();
                        return true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            });
        }
    }

    public int getErrorFromException(Throwable th) {
        if (th instanceof ParseException) {
            return -43;
        }
        if (th instanceof SocketTimeoutException) {
            return -46;
        }
        if (th instanceof SocketException) {
            return -47;
        }
        if (th instanceof IOException) {
            return -44;
        }
        if (th instanceof IllegalStateException) {
            return -45;
        }
        return th instanceof UnknownHostException ? -2 : -99;
    }

    public IRequest formatRequest(EventHandler eventHandler, String str, String str2, boolean z, Map<String, String> map, Map<String, String> map2, Map<String, String> map3, Map<String, byte[]> map4, long j, int i, int i2) {
        AliRequestAdapter aliRequestAdapter;
        EventHandler eventHandler2 = eventHandler;
        String tryDecodeUrl = Escape.tryDecodeUrl(str);
        boolean z2 = this.isUseWebpImg;
        boolean z3 = z2;
        AliRequestAdapter aliRequestAdapter2 = r1;
        AliRequestAdapter aliRequestAdapter3 = new AliRequestAdapter(eventHandler, tryDecodeUrl, str2, z, map, map2, map3, map4, j, i, i2, z3, this.bizCode);
        if (this.isReload) {
            aliRequestAdapter = aliRequestAdapter2;
            aliRequestAdapter.cancelPhase = "reload";
        } else {
            aliRequestAdapter = aliRequestAdapter2;
        }
        setRequestBodyHandler(aliRequestAdapter.getAliRequest(), aliRequestAdapter);
        EventHandler eventHandler3 = eventHandler;
        eventHandler3.setRequest(aliRequestAdapter);
        eventHandler3.setResourceType(i);
        return aliRequestAdapter;
    }

    public boolean sendRequest(IRequest iRequest) {
        return sendRequestInternal((AliRequestAdapter) iRequest);
    }
}
