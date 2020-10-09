package com.alibaba.analytics.core.sync;

import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;

public class UrlWrapper {
    private static final int ENVIRONMENT_BETA = 1;
    private static final int ENVIRONMENT_DAILY = 3;
    private static final int ENVIRONMENT_ONLINE = 0;
    private static final int ENVIRONMENT_PRE = 2;
    private static final int HTTP_ENVIRONMENT = 0;
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    private static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    public static int mErrorCode = 0;
    public static final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private static UtHostnameVerifier mUtHostnameVerifier = null;
    private static UtSslSocketFactory mUtSslSocketFactory = null;

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: Removed duplicated region for block: B:105:0x0221 A[SYNTHETIC, Splitter:B:105:0x0221] */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x0280 A[SYNTHETIC, Splitter:B:124:0x0280] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01d1 A[SYNTHETIC, Splitter:B:84:0x01d1] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01e5  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x01fc A[SYNTHETIC, Splitter:B:94:0x01fc] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.analytics.core.sync.BizResponse sendRequest(byte[] r10) {
        /*
            com.alibaba.analytics.utils.Logger.d()
            com.alibaba.analytics.core.sync.BizResponse r0 = new com.alibaba.analytics.core.sync.BizResponse
            r0.<init>()
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.core.sync.HttpsHostPortMgr r3 = com.alibaba.analytics.core.sync.HttpsHostPortMgr.getInstance()     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.lang.String r3 = r3.getHttpsUrl()     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            r2.<init>(r3)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.net.URLConnection r3 = r2.openConnection()     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.net.HttpURLConnection r3 = (java.net.HttpURLConnection) r3     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            boolean r4 = r3 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            r5 = 1
            if (r4 == 0) goto L_0x0080
            java.lang.String r2 = r2.getHost()     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            if (r4 == 0) goto L_0x002c
            return r0
        L_0x002c:
            com.alibaba.analytics.core.sync.UtSslSocketFactory r4 = mUtSslSocketFactory     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            if (r4 == 0) goto L_0x003c
            com.alibaba.analytics.core.sync.UtSslSocketFactory r4 = mUtSslSocketFactory     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.lang.String r4 = r4.getHost()     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            boolean r4 = r2.equals(r4)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            if (r4 != 0) goto L_0x004e
        L_0x003c:
            java.lang.String r4 = "UrlWrapper"
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.lang.String r7 = "new SslSocketFactory"
            r6[r1] = r7     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r4, (java.lang.Object[]) r6)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.core.sync.UtSslSocketFactory r4 = new com.alibaba.analytics.core.sync.UtSslSocketFactory     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            r4.<init>(r2)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            mUtSslSocketFactory = r4     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
        L_0x004e:
            r4 = r3
            javax.net.ssl.HttpsURLConnection r4 = (javax.net.ssl.HttpsURLConnection) r4     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.core.sync.UtSslSocketFactory r6 = mUtSslSocketFactory     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            r4.setSSLSocketFactory(r6)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.core.sync.UtHostnameVerifier r4 = mUtHostnameVerifier     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            if (r4 == 0) goto L_0x0066
            com.alibaba.analytics.core.sync.UtHostnameVerifier r4 = mUtHostnameVerifier     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.lang.String r4 = r4.getHost()     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            boolean r4 = r2.equals(r4)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            if (r4 != 0) goto L_0x0078
        L_0x0066:
            java.lang.String r4 = "UrlWrapper"
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            java.lang.String r7 = "new HostnameVerifier"
            r6[r1] = r7     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r4, (java.lang.Object[]) r6)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.core.sync.UtHostnameVerifier r4 = new com.alibaba.analytics.core.sync.UtHostnameVerifier     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            r4.<init>(r2)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            mUtHostnameVerifier = r4     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
        L_0x0078:
            r2 = r3
            javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            com.alibaba.analytics.core.sync.UtHostnameVerifier r4 = mUtHostnameVerifier     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
            r2.setHostnameVerifier(r4)     // Catch:{ MalformedURLException -> 0x02a2, IOException -> 0x0299 }
        L_0x0080:
            if (r3 == 0) goto L_0x0298
            r3.setDoOutput(r5)
            r3.setDoInput(r5)
            java.lang.String r2 = "POST"
            r3.setRequestMethod(r2)     // Catch:{ ProtocolException -> 0x028f }
            r3.setUseCaches(r1)
            r2 = 10000(0x2710, float:1.4013E-41)
            r3.setConnectTimeout(r2)
            r2 = 60000(0xea60, float:8.4078E-41)
            r3.setReadTimeout(r2)
            r3.setInstanceFollowRedirects(r5)
            java.lang.String r2 = "Content-Type"
            java.lang.String r4 = "application/x-www-form-urlencoded"
            r3.setRequestProperty(r2, r4)
            java.lang.String r2 = "Charset"
            java.lang.String r4 = "UTF-8"
            r3.setRequestProperty(r2, r4)
            com.alibaba.analytics.core.Variables r2 = com.alibaba.analytics.core.Variables.getInstance()
            java.lang.String r2 = r2.getAppkey()
            boolean r4 = android.text.TextUtils.isEmpty(r2)
            if (r4 != 0) goto L_0x00c0
            java.lang.String r4 = "x-k"
            r3.setRequestProperty(r4, r2)
        L_0x00c0:
            com.alibaba.analytics.core.Variables r2 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ Throwable -> 0x014c }
            com.ut.mini.core.sign.IUTRequestAuthentication r2 = r2.getRequestAuthenticationInstance()     // Catch:{ Throwable -> 0x014c }
            if (r2 == 0) goto L_0x0154
            java.lang.String r4 = com.alibaba.analytics.utils.MD5Utils.getMd5Hex(r10)     // Catch:{ Throwable -> 0x014c }
            java.lang.String r4 = r2.getSign(r4)     // Catch:{ Throwable -> 0x014c }
            java.lang.String r6 = ""
            r7 = 2
            java.lang.Object[] r8 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x014c }
            java.lang.String r9 = "signValue"
            r8[r1] = r9     // Catch:{ Throwable -> 0x014c }
            r8[r5] = r4     // Catch:{ Throwable -> 0x014c }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r6, (java.lang.Object[]) r8)     // Catch:{ Throwable -> 0x014c }
            java.lang.String r6 = "x-s"
            r3.setRequestProperty(r6, r4)     // Catch:{ Throwable -> 0x014c }
            boolean r4 = r2 instanceof com.ut.mini.core.sign.UTBaseRequestAuthentication     // Catch:{ Throwable -> 0x014c }
            if (r4 == 0) goto L_0x0129
            com.ut.mini.core.sign.UTBaseRequestAuthentication r2 = (com.ut.mini.core.sign.UTBaseRequestAuthentication) r2     // Catch:{ Throwable -> 0x014c }
            boolean r2 = r2.isEncode()     // Catch:{ Throwable -> 0x014c }
            if (r2 == 0) goto L_0x010d
            java.lang.String r2 = "x-t"
            java.lang.String r4 = "2"
            r3.setRequestProperty(r2, r4)     // Catch:{ Throwable -> 0x014c }
            java.lang.String r2 = ""
            java.lang.Object[] r4 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x014c }
            java.lang.String r6 = "x-t"
            r4[r1] = r6     // Catch:{ Throwable -> 0x014c }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r7)     // Catch:{ Throwable -> 0x014c }
            r4[r5] = r6     // Catch:{ Throwable -> 0x014c }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x014c }
            goto L_0x0154
        L_0x010d:
            java.lang.String r2 = "x-t"
            java.lang.String r4 = "3"
            r3.setRequestProperty(r2, r4)     // Catch:{ Throwable -> 0x014c }
            java.lang.String r2 = ""
            java.lang.Object[] r4 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x014c }
            java.lang.String r6 = "x-t"
            r4[r1] = r6     // Catch:{ Throwable -> 0x014c }
            r6 = 3
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x014c }
            r4[r5] = r6     // Catch:{ Throwable -> 0x014c }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x014c }
            goto L_0x0154
        L_0x0129:
            boolean r4 = r2 instanceof com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication     // Catch:{ Throwable -> 0x014c }
            if (r4 != 0) goto L_0x0131
            boolean r2 = r2 instanceof com.ut.mini.core.sign.UTSecurityThridRequestAuthentication     // Catch:{ Throwable -> 0x014c }
            if (r2 == 0) goto L_0x0154
        L_0x0131:
            java.lang.String r2 = "x-t"
            java.lang.String r4 = "1"
            r3.setRequestProperty(r2, r4)     // Catch:{ Throwable -> 0x014c }
            java.lang.String r2 = ""
            java.lang.Object[] r4 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x014c }
            java.lang.String r6 = "x-t"
            r4[r1] = r6     // Catch:{ Throwable -> 0x014c }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x014c }
            r4[r5] = r6     // Catch:{ Throwable -> 0x014c }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x014c }
            goto L_0x0154
        L_0x014c:
            r2 = move-exception
            java.lang.String r4 = ""
            java.lang.Object[] r6 = new java.lang.Object[r1]
            com.alibaba.analytics.utils.Logger.e(r4, r2, r6)
        L_0x0154:
            long r6 = java.lang.System.currentTimeMillis()
            r2 = 0
            r3.connect()     // Catch:{ SSLHandshakeException -> 0x0230, Exception -> 0x020d }
            if (r10 == 0) goto L_0x017d
            int r4 = r10.length     // Catch:{ SSLHandshakeException -> 0x0230, Exception -> 0x020d }
            if (r4 <= 0) goto L_0x017d
            java.io.DataOutputStream r4 = new java.io.DataOutputStream     // Catch:{ SSLHandshakeException -> 0x0230, Exception -> 0x020d }
            java.io.OutputStream r8 = r3.getOutputStream()     // Catch:{ SSLHandshakeException -> 0x0230, Exception -> 0x020d }
            r4.<init>(r8)     // Catch:{ SSLHandshakeException -> 0x0230, Exception -> 0x020d }
            r4.write(r10)     // Catch:{ SSLHandshakeException -> 0x0179, Exception -> 0x0175, all -> 0x0171 }
            r4.flush()     // Catch:{ SSLHandshakeException -> 0x0179, Exception -> 0x0175, all -> 0x0171 }
            goto L_0x017e
        L_0x0171:
            r10 = move-exception
            r2 = r4
            goto L_0x027e
        L_0x0175:
            r10 = move-exception
            r2 = r4
            goto L_0x020e
        L_0x0179:
            r10 = move-exception
            r2 = r4
            goto L_0x0231
        L_0x017d:
            r4 = r2
        L_0x017e:
            if (r4 == 0) goto L_0x018e
            r4.close()     // Catch:{ IOException -> 0x0184 }
            goto L_0x018e
        L_0x0184:
            r10 = move-exception
            java.lang.String r4 = ""
            java.lang.Object[] r8 = new java.lang.Object[r5]
            r8[r1] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r4, (java.lang.Object[]) r8)
        L_0x018e:
            long r8 = java.lang.System.currentTimeMillis()
            long r8 = r8 - r6
            r0.rt = r8
            java.io.ByteArrayOutputStream r10 = new java.io.ByteArrayOutputStream
            r10.<init>()
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch:{ IOException -> 0x01c3, all -> 0x01c0 }
            java.io.InputStream r3 = r3.getInputStream()     // Catch:{ IOException -> 0x01c3, all -> 0x01c0 }
            r4.<init>(r3)     // Catch:{ IOException -> 0x01c3, all -> 0x01c0 }
            r2 = 2048(0x800, float:2.87E-42)
            byte[] r3 = new byte[r2]     // Catch:{ IOException -> 0x01be }
        L_0x01a7:
            int r6 = r4.read(r3, r1, r2)     // Catch:{ IOException -> 0x01be }
            r7 = -1
            if (r6 == r7) goto L_0x01b2
            r10.write(r3, r1, r6)     // Catch:{ IOException -> 0x01be }
            goto L_0x01a7
        L_0x01b2:
            r4.close()     // Catch:{ Exception -> 0x01b6 }
            goto L_0x01df
        L_0x01b6:
            r2 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r4 = new java.lang.Object[r5]
            r4[r1] = r2
            goto L_0x01dc
        L_0x01be:
            r2 = move-exception
            goto L_0x01c6
        L_0x01c0:
            r10 = move-exception
            r4 = r2
            goto L_0x01fa
        L_0x01c3:
            r3 = move-exception
            r4 = r2
            r2 = r3
        L_0x01c6:
            java.lang.String r3 = ""
            java.lang.Object[] r6 = new java.lang.Object[r5]     // Catch:{ all -> 0x01f9 }
            r6[r1] = r2     // Catch:{ all -> 0x01f9 }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r3, (java.lang.Object[]) r6)     // Catch:{ all -> 0x01f9 }
            if (r4 == 0) goto L_0x01df
            r4.close()     // Catch:{ Exception -> 0x01d5 }
            goto L_0x01df
        L_0x01d5:
            r2 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r4 = new java.lang.Object[r5]
            r4[r1] = r2
        L_0x01dc:
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r3, (java.lang.Object[]) r4)
        L_0x01df:
            int r1 = r10.size()
            if (r1 <= 0) goto L_0x0298
            byte[] r10 = r10.toByteArray()
            int r10 = com.alibaba.analytics.core.sync.BizRequest.parseResult(r10)
            mErrorCode = r10
            int r10 = mErrorCode
            r0.errCode = r10
            java.lang.String r10 = com.alibaba.analytics.core.sync.BizRequest.mResponseAdditionalData
            r0.data = r10
            goto L_0x0298
        L_0x01f9:
            r10 = move-exception
        L_0x01fa:
            if (r4 == 0) goto L_0x020a
            r4.close()     // Catch:{ Exception -> 0x0200 }
            goto L_0x020a
        L_0x0200:
            r0 = move-exception
            java.lang.Object[] r2 = new java.lang.Object[r5]
            r2[r1] = r0
            java.lang.String r0 = ""
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r2)
        L_0x020a:
            throw r10
        L_0x020b:
            r10 = move-exception
            goto L_0x027e
        L_0x020d:
            r10 = move-exception
        L_0x020e:
            java.lang.String r3 = ""
            java.lang.Object[] r4 = new java.lang.Object[r5]     // Catch:{ all -> 0x020b }
            r4[r1] = r10     // Catch:{ all -> 0x020b }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ all -> 0x020b }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x020b }
            r10 = 0
            long r3 = r3 - r6
            r0.rt = r3     // Catch:{ all -> 0x020b }
            if (r2 == 0) goto L_0x022f
            r2.close()     // Catch:{ IOException -> 0x0225 }
            goto L_0x022f
        L_0x0225:
            r10 = move-exception
            java.lang.String r2 = ""
            java.lang.Object[] r3 = new java.lang.Object[r5]
            r3[r1] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r3)
        L_0x022f:
            return r0
        L_0x0230:
            r10 = move-exception
        L_0x0231:
            java.lang.String r3 = ""
            java.lang.Object[] r4 = new java.lang.Object[r5]     // Catch:{ all -> 0x020b }
            r4[r1] = r10     // Catch:{ all -> 0x020b }
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r3, (java.lang.Object[]) r4)     // Catch:{ all -> 0x020b }
            com.alibaba.analytics.core.Variables r10 = com.alibaba.analytics.core.Variables.getInstance()     // Catch:{ all -> 0x020b }
            boolean r10 = r10.isSelfMonitorTurnOn()     // Catch:{ all -> 0x020b }
            if (r10 == 0) goto L_0x0265
            java.util.HashMap r10 = new java.util.HashMap     // Catch:{ all -> 0x020b }
            r10.<init>()     // Catch:{ all -> 0x020b }
            java.lang.String r3 = "type"
            java.lang.String r4 = "3"
            r10.put(r3, r4)     // Catch:{ all -> 0x020b }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather r3 = mMonitor     // Catch:{ all -> 0x020b }
            int r4 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.UPLOAD_FAILED     // Catch:{ all -> 0x020b }
            java.lang.String r10 = com.alibaba.fastjson.JSON.toJSONString(r10)     // Catch:{ all -> 0x020b }
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            java.lang.Double r8 = java.lang.Double.valueOf(r8)     // Catch:{ all -> 0x020b }
            com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent r10 = com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent.buildCountEvent(r4, r10, r8)     // Catch:{ all -> 0x020b }
            r3.onEvent(r10)     // Catch:{ all -> 0x020b }
        L_0x0265:
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x020b }
            r10 = 0
            long r3 = r3 - r6
            r0.rt = r3     // Catch:{ all -> 0x020b }
            if (r2 == 0) goto L_0x027d
            r2.close()     // Catch:{ IOException -> 0x0273 }
            goto L_0x027d
        L_0x0273:
            r10 = move-exception
            java.lang.String r2 = ""
            java.lang.Object[] r3 = new java.lang.Object[r5]
            r3[r1] = r10
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r2, (java.lang.Object[]) r3)
        L_0x027d:
            return r0
        L_0x027e:
            if (r2 == 0) goto L_0x028e
            r2.close()     // Catch:{ IOException -> 0x0284 }
            goto L_0x028e
        L_0x0284:
            r0 = move-exception
            java.lang.Object[] r2 = new java.lang.Object[r5]
            r2[r1] = r0
            java.lang.String r0 = ""
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r0, (java.lang.Object[]) r2)
        L_0x028e:
            throw r10
        L_0x028f:
            r10 = move-exception
            java.lang.String r2 = ""
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.alibaba.analytics.utils.Logger.e(r2, r10, r1)
            return r0
        L_0x0298:
            return r0
        L_0x0299:
            r10 = move-exception
            java.lang.String r2 = ""
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.alibaba.analytics.utils.Logger.e(r2, r10, r1)
            return r0
        L_0x02a2:
            r10 = move-exception
            java.lang.String r2 = ""
            java.lang.Object[] r1 = new java.lang.Object[r1]
            com.alibaba.analytics.utils.Logger.e(r2, r10, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.analytics.core.sync.UrlWrapper.sendRequest(byte[]):com.alibaba.analytics.core.sync.BizResponse");
    }
}
