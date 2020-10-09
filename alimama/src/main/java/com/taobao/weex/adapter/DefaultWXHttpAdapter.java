package com.taobao.weex.adapter;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import anet.channel.request.Request;
import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DefaultWXHttpAdapter implements IWXHttpAdapter {
    private static final IEventReporterDelegate DEFAULT_DELEGATE = new NOPEventReportDelegate();
    private ExecutorService mExecutorService;

    public interface IEventReporterDelegate {
        void httpExchangeFailed(IOException iOException);

        InputStream interpretResponseStream(@Nullable InputStream inputStream);

        void postConnect();

        void preConnect(HttpURLConnection httpURLConnection, @Nullable String str);
    }

    private void execute(Runnable runnable) {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newFixedThreadPool(3);
        }
        this.mExecutorService.execute(runnable);
    }

    public void sendRequest(final WXRequest wXRequest, final IWXHttpAdapter.OnHttpListener onHttpListener) {
        if (onHttpListener != null) {
            onHttpListener.onHttpStart();
        }
        execute(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:33:0x00bf  */
            /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r9 = this;
                    com.taobao.weex.WXSDKManager r0 = com.taobao.weex.WXSDKManager.getInstance()
                    java.util.Map r0 = r0.getAllInstanceMap()
                    com.taobao.weex.common.WXRequest r1 = r2
                    java.lang.String r1 = r1.instanceId
                    java.lang.Object r0 = r0.get(r1)
                    com.taobao.weex.WXSDKInstance r0 = (com.taobao.weex.WXSDKInstance) r0
                    if (r0 == 0) goto L_0x0021
                    boolean r1 = r0.isDestroy()
                    if (r1 != 0) goto L_0x0021
                    com.taobao.weex.performance.WXInstanceApm r1 = r0.getApmForInstance()
                    r1.actionNetRequest()
                L_0x0021:
                    r1 = 1
                    com.taobao.weex.common.WXResponse r2 = new com.taobao.weex.common.WXResponse
                    r2.<init>()
                    com.taobao.weex.adapter.DefaultWXHttpAdapter r3 = com.taobao.weex.adapter.DefaultWXHttpAdapter.this
                    com.taobao.weex.adapter.DefaultWXHttpAdapter$IEventReporterDelegate r3 = r3.getEventReporterDelegate()
                    r4 = 0
                    com.taobao.weex.adapter.DefaultWXHttpAdapter r5 = com.taobao.weex.adapter.DefaultWXHttpAdapter.this     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.common.WXRequest r6 = r2     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r7 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.net.HttpURLConnection r5 = r5.openConnection(r6, r7)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.common.WXRequest r6 = r2     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.lang.String r6 = r6.body     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r3.preConnect(r5, r6)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.util.Map r6 = r5.getHeaderFields()     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    int r7 = r5.getResponseCode()     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r8 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    if (r8 == 0) goto L_0x0050
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r8 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r8.onHeadersReceived(r7, r6)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                L_0x0050:
                    r3.postConnect()     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.lang.String r6 = java.lang.String.valueOf(r7)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r2.statusCode = r6     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r6 = 200(0xc8, float:2.8E-43)
                    if (r7 < r6) goto L_0x0074
                    r6 = 299(0x12b, float:4.19E-43)
                    if (r7 > r6) goto L_0x0074
                    java.io.InputStream r5 = r5.getInputStream()     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.io.InputStream r5 = r3.interpretResponseStream(r5)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.adapter.DefaultWXHttpAdapter r6 = com.taobao.weex.adapter.DefaultWXHttpAdapter.this     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r7 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    byte[] r5 = r6.readInputStreamAsBytes(r5, r7)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r2.originalData = r5     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    goto L_0x0083
                L_0x0074:
                    com.taobao.weex.adapter.DefaultWXHttpAdapter r1 = com.taobao.weex.adapter.DefaultWXHttpAdapter.this     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.io.InputStream r5 = r5.getErrorStream()     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r6 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    java.lang.String r1 = r1.readInputStream(r5, r6)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r2.errorMsg = r1     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r1 = 0
                L_0x0083:
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r5 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    if (r5 == 0) goto L_0x00b7
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r5 = r3     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    r5.onHttpFinish(r2)     // Catch:{ IOException | IllegalArgumentException -> 0x008d }
                    goto L_0x00b7
                L_0x008d:
                    r1 = move-exception
                    r1.printStackTrace()
                    java.lang.String r5 = "-1"
                    r2.statusCode = r5
                    java.lang.String r5 = "-1"
                    r2.errorCode = r5
                    java.lang.String r5 = r1.getMessage()
                    r2.errorMsg = r5
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r5 = r3
                    if (r5 == 0) goto L_0x00a8
                    com.taobao.weex.adapter.IWXHttpAdapter$OnHttpListener r5 = r3
                    r5.onHttpFinish(r2)
                L_0x00a8:
                    boolean r2 = r1 instanceof java.io.IOException
                    if (r2 == 0) goto L_0x00b6
                    java.io.IOException r1 = (java.io.IOException) r1     // Catch:{ Throwable -> 0x00b2 }
                    r3.httpExchangeFailed(r1)     // Catch:{ Throwable -> 0x00b2 }
                    goto L_0x00b6
                L_0x00b2:
                    r1 = move-exception
                    r1.printStackTrace()
                L_0x00b6:
                    r1 = 0
                L_0x00b7:
                    if (r0 == 0) goto L_0x00c7
                    boolean r2 = r0.isDestroy()
                    if (r2 != 0) goto L_0x00c7
                    com.taobao.weex.performance.WXInstanceApm r0 = r0.getApmForInstance()
                    r2 = 0
                    r0.actionNetResult(r1, r2)
                L_0x00c7:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.adapter.DefaultWXHttpAdapter.AnonymousClass1.run():void");
            }
        });
    }

    /* access modifiers changed from: private */
    public HttpURLConnection openConnection(WXRequest wXRequest, IWXHttpAdapter.OnHttpListener onHttpListener) throws IOException {
        HttpURLConnection createConnection = createConnection(new URL(wXRequest.url));
        createConnection.setConnectTimeout(wXRequest.timeoutMs);
        createConnection.setReadTimeout(wXRequest.timeoutMs);
        createConnection.setUseCaches(false);
        createConnection.setDoInput(true);
        if (wXRequest.paramMap != null) {
            for (String next : wXRequest.paramMap.keySet()) {
                createConnection.addRequestProperty(next, wXRequest.paramMap.get(next));
            }
        }
        if ("POST".equals(wXRequest.method) || Request.Method.PUT.equals(wXRequest.method) || "PATCH".equals(wXRequest.method)) {
            createConnection.setRequestMethod(wXRequest.method);
            if (wXRequest.body != null) {
                if (onHttpListener != null) {
                    onHttpListener.onHttpUploadProgress(0);
                }
                createConnection.setDoOutput(true);
                DataOutputStream dataOutputStream = new DataOutputStream(createConnection.getOutputStream());
                dataOutputStream.write(wXRequest.body.getBytes());
                dataOutputStream.close();
                if (onHttpListener != null) {
                    onHttpListener.onHttpUploadProgress(100);
                }
            }
        } else if (!TextUtils.isEmpty(wXRequest.method)) {
            createConnection.setRequestMethod(wXRequest.method);
        } else {
            createConnection.setRequestMethod("GET");
        }
        return createConnection;
    }

    /* access modifiers changed from: private */
    public byte[] readInputStreamAsBytes(InputStream inputStream, IWXHttpAdapter.OnHttpListener onHttpListener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[2048];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr, 0, bArr.length);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
                i += read;
                if (onHttpListener != null) {
                    onHttpListener.onHttpResponseProgress(i);
                }
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    /* access modifiers changed from: private */
    public String readInputStream(InputStream inputStream, IWXHttpAdapter.OnHttpListener onHttpListener) throws IOException {
        if (inputStream == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        char[] cArr = new char[2048];
        while (true) {
            int read = bufferedReader.read(cArr);
            if (read != -1) {
                sb.append(cArr, 0, read);
                if (onHttpListener != null) {
                    onHttpListener.onHttpResponseProgress(sb.length());
                }
            } else {
                bufferedReader.close();
                return sb.toString();
            }
        }
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection createConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    @NonNull
    public IEventReporterDelegate getEventReporterDelegate() {
        return DEFAULT_DELEGATE;
    }

    private static class NOPEventReportDelegate implements IEventReporterDelegate {
        public void httpExchangeFailed(IOException iOException) {
        }

        public InputStream interpretResponseStream(@Nullable InputStream inputStream) {
            return inputStream;
        }

        public void postConnect() {
        }

        public void preConnect(HttpURLConnection httpURLConnection, @Nullable String str) {
        }

        private NOPEventReportDelegate() {
        }
    }
}
