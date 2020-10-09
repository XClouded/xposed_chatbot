package mtopsdk.network.impl;

import anetwork.channel.NetworkCallBack;
import anetwork.channel.NetworkEvent;
import anetwork.channel.aidl.ParcelableInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.network.Call;
import mtopsdk.network.NetworkCallback;
import mtopsdk.network.domain.Response;
import mtopsdk.network.domain.ResponseBody;
import mtopsdk.network.util.ANetworkConverter;
import mtopsdk.network.util.NetworkUtils;

public class NetworkListenerAdapter implements NetworkCallBack.ResponseCodeListener, NetworkCallBack.InputStreamListener, NetworkCallBack.FinishListener {
    private static final String TAG = "mtopsdk.NetworkListenerAdapter";
    ByteArrayOutputStream bos = null;
    Call call;
    NetworkEvent.FinishEvent finishEvent = null;
    Map<String, List<String>> headers;
    boolean isNeedCallFinish = false;
    private volatile boolean isStreamReceived = false;
    NetworkCallback networkCallback;
    int resLength = 0;
    final String seqNo;
    int statusCode;

    public NetworkListenerAdapter(Call call2, NetworkCallback networkCallback2, String str) {
        this.call = call2;
        this.networkCallback = networkCallback2;
        this.seqNo = str;
    }

    public boolean onResponseCode(int i, Map<String, List<String>> map, Object obj) {
        this.statusCode = i;
        this.headers = map;
        try {
            String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(this.headers, "content-length");
            if (StringUtils.isBlank(singleHeaderFieldByKey)) {
                singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(this.headers, HttpHeaderConstant.X_BIN_LENGTH);
            }
            if (!StringUtils.isNotBlank(singleHeaderFieldByKey)) {
                return false;
            }
            this.resLength = Integer.parseInt(singleHeaderFieldByKey);
            return false;
        } catch (Exception unused) {
            TBSdkLog.e(TAG, this.seqNo, "[onResponseCode]parse Response HeaderField ContentLength error ");
            return false;
        }
    }

    public void onFinished(NetworkEvent.FinishEvent finishEvent2, Object obj) {
        synchronized (this) {
            this.finishEvent = finishEvent2;
            if (this.isNeedCallFinish || !this.isStreamReceived) {
                callFinish(finishEvent2, obj);
            }
        }
    }

    public void onInputStreamGet(final ParcelableInputStream parcelableInputStream, final Object obj) {
        this.isStreamReceived = true;
        MtopSDKThreadPoolExecutorFactory.submitRequestTask(new Runnable() {
            public void run() {
                ParcelableInputStream parcelableInputStream;
                try {
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        TBSdkLog.d(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[onInputStreamGet]start to read input stream");
                    }
                    NetworkListenerAdapter.this.bos = new ByteArrayOutputStream(parcelableInputStream.length() > 0 ? parcelableInputStream.length() : NetworkListenerAdapter.this.resLength);
                    byte[] bArr = new byte[4096];
                    while (true) {
                        int read = parcelableInputStream.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                            String str = NetworkListenerAdapter.this.seqNo;
                            TBSdkLog.d(NetworkListenerAdapter.TAG, str, "[onInputStreamGet]data chunk content: " + new String(bArr, 0, read));
                        }
                        NetworkListenerAdapter.this.bos.write(bArr, 0, read);
                    }
                    if (parcelableInputStream != null) {
                        try {
                            parcelableInputStream = parcelableInputStream;
                            parcelableInputStream.close();
                        } catch (Exception unused) {
                        }
                    }
                } catch (Exception e) {
                    TBSdkLog.e(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[onInputStreamGet]Read data from inputstream failed.", e);
                    NetworkListenerAdapter.this.bos = null;
                    if (parcelableInputStream != null) {
                        parcelableInputStream = parcelableInputStream;
                    }
                } catch (Throwable th) {
                    if (parcelableInputStream != null) {
                        try {
                            parcelableInputStream.close();
                        } catch (Exception unused2) {
                        }
                    }
                    NetworkUtils.closeQuietly(NetworkListenerAdapter.this.bos);
                    throw th;
                }
                NetworkUtils.closeQuietly(NetworkListenerAdapter.this.bos);
                synchronized (NetworkListenerAdapter.this) {
                    if (NetworkListenerAdapter.this.finishEvent != null) {
                        NetworkListenerAdapter.this.callFinish(NetworkListenerAdapter.this.finishEvent, obj);
                    } else {
                        NetworkListenerAdapter.this.isNeedCallFinish = true;
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void callFinish(final NetworkEvent.FinishEvent finishEvent2, final Object obj) {
        MtopSDKThreadPoolExecutorFactory.submitCallbackTask(this.seqNo != null ? this.seqNo.hashCode() : hashCode(), new Runnable() {
            public void run() {
                try {
                    long currentTimeMillis = System.currentTimeMillis();
                    NetworkListenerAdapter.this.onFinishTask(finishEvent2, obj);
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                        String str = NetworkListenerAdapter.this.seqNo;
                        TBSdkLog.d(NetworkListenerAdapter.TAG, str, "[callFinish] execute onFinishTask time[ms] " + (System.currentTimeMillis() - currentTimeMillis));
                    }
                } catch (Exception e) {
                    TBSdkLog.e(NetworkListenerAdapter.TAG, NetworkListenerAdapter.this.seqNo, "[callFinish]execute onFinishTask error.", e);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onFinishTask(NetworkEvent.FinishEvent finishEvent2, Object obj) {
        if (this.networkCallback == null) {
            TBSdkLog.e(TAG, this.seqNo, "[onFinishTask]networkCallback is null");
            return;
        }
        final byte[] bArr = null;
        if (this.bos != null) {
            bArr = this.bos.toByteArray();
        }
        AnonymousClass3 r0 = new ResponseBody() {
            public InputStream byteStream() {
                return null;
            }

            public String contentType() {
                return HeaderHandlerUtil.getSingleHeaderFieldByKey(NetworkListenerAdapter.this.headers, "Content-Type");
            }

            public long contentLength() throws IOException {
                if (bArr != null) {
                    return (long) bArr.length;
                }
                return 0;
            }

            public byte[] getBytes() throws IOException {
                return bArr;
            }
        };
        this.networkCallback.onResponse(this.call, new Response.Builder().request(this.call.request()).code(finishEvent2.getHttpCode()).message(finishEvent2.getDesc()).headers(this.headers).body(r0).stat(ANetworkConverter.convertNetworkStats(finishEvent2.getStatisticData())).build());
    }
}
