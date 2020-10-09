package com.alibaba.motu.tbrest.request;

import com.alibaba.motu.tbrest.SendService;

public class UrlWrapper {
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    private static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 60000;
    public static int mErrorCode;
    private static RestSslSocketFactory mRestSslSocketFactory;

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: Removed duplicated region for block: B:108:0x0203 A[SYNTHETIC, Splitter:B:108:0x0203] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x018c A[SYNTHETIC, Splitter:B:74:0x018c] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x019e  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x01b4 A[SYNTHETIC, Splitter:B:83:0x01b4] */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x01d5 A[SYNTHETIC, Splitter:B:93:0x01d5] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:99:0x01e3=Splitter:B:99:0x01e3, B:90:0x01c4=Splitter:B:90:0x01c4} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alibaba.motu.tbrest.request.BizResponse sendRequest(java.lang.String r7, java.lang.String r8, byte[] r9) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "sendRequest use adashx, bytes length : "
            r0.append(r1)
            if (r9 != 0) goto L_0x000f
            java.lang.String r1 = "0"
            goto L_0x0014
        L_0x000f:
            int r1 = r9.length
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
        L_0x0014:
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.d(r0)
            com.alibaba.motu.tbrest.request.BizResponse r0 = new com.alibaba.motu.tbrest.request.BizResponse
            r0.<init>()
            com.alibaba.motu.tbrest.SendService r1 = com.alibaba.motu.tbrest.SendService.getInstance()     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            java.lang.Boolean r1 = r1.openHttp     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            boolean r1 = r1.booleanValue()     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            if (r1 == 0) goto L_0x0035
            java.net.URL r1 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            r1.<init>(r8)     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            goto L_0x003a
        L_0x0035:
            java.net.URL r1 = new java.net.URL     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            r1.<init>(r8)     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
        L_0x003a:
            java.net.URLConnection r8 = r1.openConnection()     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            boolean r2 = r8 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            if (r2 == 0) goto L_0x0065
            com.alibaba.motu.tbrest.request.RestSslSocketFactory r2 = mRestSslSocketFactory     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            if (r2 != 0) goto L_0x005d
            java.lang.String r2 = r1.getHost()     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            if (r2 != 0) goto L_0x005d
            com.alibaba.motu.tbrest.request.RestSslSocketFactory r2 = new com.alibaba.motu.tbrest.request.RestSslSocketFactory     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            java.lang.String r1 = r1.getHost()     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            r2.<init>(r1)     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            mRestSslSocketFactory = r2     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
        L_0x005d:
            r1 = r8
            javax.net.ssl.HttpsURLConnection r1 = (javax.net.ssl.HttpsURLConnection) r1     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            com.alibaba.motu.tbrest.request.RestSslSocketFactory r2 = mRestSslSocketFactory     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
            r1.setSSLSocketFactory(r2)     // Catch:{ MalformedURLException -> 0x0213, IOException -> 0x0212 }
        L_0x0065:
            if (r8 == 0) goto L_0x0211
            r1 = 1
            r8.setDoOutput(r1)
            r8.setDoInput(r1)
            java.lang.String r2 = "POST"
            r8.setRequestMethod(r2)     // Catch:{ ProtocolException -> 0x0210 }
            r2 = 0
            r8.setUseCaches(r2)
            r3 = 10000(0x2710, float:1.4013E-41)
            r8.setConnectTimeout(r3)
            r3 = 60000(0xea60, float:8.4078E-41)
            r8.setReadTimeout(r3)
            r8.setInstanceFollowRedirects(r1)
            java.lang.String r3 = "Content-Type"
            java.lang.String r4 = "application/x-www-form-urlencoded"
            r8.setRequestProperty(r3, r4)
            java.lang.String r3 = "Charset"
            java.lang.String r4 = "UTF-8"
            r8.setRequestProperty(r3, r4)
            boolean r3 = android.text.TextUtils.isEmpty(r7)
            if (r3 != 0) goto L_0x009f
            java.lang.String r3 = "x-k"
            r8.setRequestProperty(r3, r7)
        L_0x009f:
            com.alibaba.motu.tbrest.SendService r3 = com.alibaba.motu.tbrest.SendService.getInstance()     // Catch:{ Throwable -> 0x010f }
            java.lang.String r3 = r3.appSecret     // Catch:{ Throwable -> 0x010f }
            if (r3 == 0) goto L_0x00dd
            int r4 = r3.length()     // Catch:{ Throwable -> 0x010f }
            if (r4 <= 0) goto L_0x00dd
            com.alibaba.motu.tbrest.sign.RestBaseRequestAuthentication r4 = new com.alibaba.motu.tbrest.sign.RestBaseRequestAuthentication     // Catch:{ Throwable -> 0x010f }
            r4.<init>(r7, r3, r1)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r7 = com.alibaba.motu.tbrest.utils.MD5Utils.getMd5Hex(r9)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r7 = r4.getSign(r7)     // Catch:{ Throwable -> 0x010f }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x010f }
            r1.<init>()     // Catch:{ Throwable -> 0x010f }
            java.lang.String r3 = "signValue:"
            r1.append(r3)     // Catch:{ Throwable -> 0x010f }
            r1.append(r7)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x010f }
            com.alibaba.motu.tbrest.utils.LogUtil.d(r1)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r1 = "x-s"
            r8.setRequestProperty(r1, r7)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r7 = "x-t"
            java.lang.String r1 = "3"
            r8.setRequestProperty(r7, r1)     // Catch:{ Throwable -> 0x010f }
            goto L_0x0117
        L_0x00dd:
            java.lang.String r1 = ""
            com.alibaba.motu.tbrest.sign.RestBaseRequestAuthentication r3 = new com.alibaba.motu.tbrest.sign.RestBaseRequestAuthentication     // Catch:{ Throwable -> 0x010f }
            r3.<init>(r7, r1, r2)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r7 = com.alibaba.motu.tbrest.utils.MD5Utils.getMd5Hex(r9)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r7 = r3.getSign(r7)     // Catch:{ Throwable -> 0x010f }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x010f }
            r1.<init>()     // Catch:{ Throwable -> 0x010f }
            java.lang.String r3 = "signValue:"
            r1.append(r3)     // Catch:{ Throwable -> 0x010f }
            r1.append(r7)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x010f }
            com.alibaba.motu.tbrest.utils.LogUtil.d(r1)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r1 = "x-s"
            r8.setRequestProperty(r1, r7)     // Catch:{ Throwable -> 0x010f }
            java.lang.String r7 = "x-t"
            java.lang.String r1 = "3"
            r8.setRequestProperty(r7, r1)     // Catch:{ Throwable -> 0x010f }
            goto L_0x0117
        L_0x010f:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r7)
        L_0x0117:
            long r3 = java.lang.System.currentTimeMillis()
            r7 = 0
            r8.connect()     // Catch:{ SSLHandshakeException -> 0x01e2, Exception -> 0x01c3 }
            if (r9 == 0) goto L_0x0143
            int r1 = r9.length     // Catch:{ SSLHandshakeException -> 0x01e2, Exception -> 0x01c3 }
            if (r1 <= 0) goto L_0x0143
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ SSLHandshakeException -> 0x01e2, Exception -> 0x01c3 }
            java.io.OutputStream r5 = r8.getOutputStream()     // Catch:{ SSLHandshakeException -> 0x01e2, Exception -> 0x01c3 }
            r1.<init>(r5)     // Catch:{ SSLHandshakeException -> 0x01e2, Exception -> 0x01c3 }
            r1.write(r9)     // Catch:{ SSLHandshakeException -> 0x013e, Exception -> 0x0139, all -> 0x0134 }
            r1.flush()     // Catch:{ SSLHandshakeException -> 0x013e, Exception -> 0x0139, all -> 0x0134 }
            goto L_0x0144
        L_0x0134:
            r7 = move-exception
            r8 = r7
            r7 = r1
            goto L_0x0201
        L_0x0139:
            r7 = move-exception
            r8 = r7
            r7 = r1
            goto L_0x01c4
        L_0x013e:
            r7 = move-exception
            r8 = r7
            r7 = r1
            goto L_0x01e3
        L_0x0143:
            r1 = r7
        L_0x0144:
            if (r1 == 0) goto L_0x0152
            r1.close()     // Catch:{ IOException -> 0x014a }
            goto L_0x0152
        L_0x014a:
            r9 = move-exception
            java.lang.String r9 = r9.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r9)
        L_0x0152:
            long r5 = java.lang.System.currentTimeMillis()
            long r5 = r5 - r3
            r0.rt = r5
            java.io.ByteArrayOutputStream r9 = new java.io.ByteArrayOutputStream
            r9.<init>()
            java.io.DataInputStream r1 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0180, all -> 0x017c }
            java.io.InputStream r8 = r8.getInputStream()     // Catch:{ IOException -> 0x0180, all -> 0x017c }
            r1.<init>(r8)     // Catch:{ IOException -> 0x0180, all -> 0x017c }
            r7 = 2048(0x800, float:2.87E-42)
            byte[] r8 = new byte[r7]     // Catch:{ IOException -> 0x017a }
        L_0x016b:
            int r3 = r1.read(r8, r2, r7)     // Catch:{ IOException -> 0x017a }
            r4 = -1
            if (r3 == r4) goto L_0x0176
            r9.write(r8, r2, r3)     // Catch:{ IOException -> 0x017a }
            goto L_0x016b
        L_0x0176:
            r1.close()     // Catch:{ Exception -> 0x0190 }
            goto L_0x0198
        L_0x017a:
            r7 = move-exception
            goto L_0x0183
        L_0x017c:
            r8 = move-exception
            r1 = r7
            r7 = r8
            goto L_0x01b2
        L_0x0180:
            r8 = move-exception
            r1 = r7
            r7 = r8
        L_0x0183:
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x01b1 }
            com.alibaba.motu.tbrest.utils.LogUtil.e(r7)     // Catch:{ all -> 0x01b1 }
            if (r1 == 0) goto L_0x0198
            r1.close()     // Catch:{ Exception -> 0x0190 }
            goto L_0x0198
        L_0x0190:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r7)
        L_0x0198:
            int r7 = r9.size()
            if (r7 <= 0) goto L_0x0211
            byte[] r7 = r9.toByteArray()
            int r7 = com.alibaba.motu.tbrest.request.BizRequest.parseResult(r7)
            mErrorCode = r7
            int r7 = mErrorCode
            r0.errCode = r7
            java.lang.String r7 = com.alibaba.motu.tbrest.request.BizRequest.mResponseAdditionalData
            r0.data = r7
            goto L_0x0211
        L_0x01b1:
            r7 = move-exception
        L_0x01b2:
            if (r1 == 0) goto L_0x01c0
            r1.close()     // Catch:{ Exception -> 0x01b8 }
            goto L_0x01c0
        L_0x01b8:
            r8 = move-exception
            java.lang.String r8 = r8.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r8)
        L_0x01c0:
            throw r7
        L_0x01c1:
            r8 = move-exception
            goto L_0x0201
        L_0x01c3:
            r8 = move-exception
        L_0x01c4:
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x01c1 }
            com.alibaba.motu.tbrest.utils.LogUtil.e(r8)     // Catch:{ all -> 0x01c1 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01c1 }
            r1 = 0
            long r8 = r8 - r3
            r0.rt = r8     // Catch:{ all -> 0x01c1 }
            if (r7 == 0) goto L_0x01e1
            r7.close()     // Catch:{ IOException -> 0x01d9 }
            goto L_0x01e1
        L_0x01d9:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r7)
        L_0x01e1:
            return r0
        L_0x01e2:
            r8 = move-exception
        L_0x01e3:
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x01c1 }
            com.alibaba.motu.tbrest.utils.LogUtil.e(r8)     // Catch:{ all -> 0x01c1 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x01c1 }
            r1 = 0
            long r8 = r8 - r3
            r0.rt = r8     // Catch:{ all -> 0x01c1 }
            if (r7 == 0) goto L_0x0200
            r7.close()     // Catch:{ IOException -> 0x01f8 }
            goto L_0x0200
        L_0x01f8:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r7)
        L_0x0200:
            return r0
        L_0x0201:
            if (r7 == 0) goto L_0x020f
            r7.close()     // Catch:{ IOException -> 0x0207 }
            goto L_0x020f
        L_0x0207:
            r7 = move-exception
            java.lang.String r7 = r7.toString()
            com.alibaba.motu.tbrest.utils.LogUtil.e(r7)
        L_0x020f:
            throw r8
        L_0x0210:
            return r0
        L_0x0211:
            return r0
        L_0x0212:
            return r0
        L_0x0213:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.request.UrlWrapper.sendRequest(java.lang.String, java.lang.String, byte[]):com.alibaba.motu.tbrest.request.BizResponse");
    }

    public static BizResponse sendRequest(String str, byte[] bArr) {
        String str2;
        String str3 = SendService.getInstance().appKey;
        if (SendService.getInstance().openHttp.booleanValue()) {
            str2 = "http://" + str + "/upload";
        } else {
            str2 = "https://" + str + "/upload";
        }
        return sendRequest(str3, str2, bArr);
    }

    public static BizResponse sendRequestByUrl(String str, byte[] bArr) {
        return sendRequest(SendService.getInstance().appKey, str, bArr);
    }
}
