package com.ta.audid.upload;

public class HttpUtils {
    private static final int MAX_CONNECTION_TIME_OUT = 10000;
    private static final int MAX_READ_CONNECTION_STREAM_TIME_OUT = 10000;
    private static final long TIME_SCOPE = 1800000;
    private static UtdidHostnameVerifier mUtdidHostnameVerifier = null;
    private static UtdidSslSocketFactory mUtdidSslSocketFactory = null;

    static {
        System.setProperty("http.keepAlive", "true");
    }

    /* JADX WARNING: type inference failed for: r7v9, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r7v11, types: [java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r9v3, types: [java.io.DataInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r7v12 */
    /* JADX WARNING: type inference failed for: r9v4 */
    /* JADX WARNING: type inference failed for: r7v14 */
    /* JADX WARNING: type inference failed for: r9v5, types: [java.io.DataInputStream, java.io.InputStream] */
    /* JADX WARNING: type inference failed for: r7v23 */
    /* JADX WARNING: type inference failed for: r7v26 */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x0227, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0228, code lost:
        r4 = r0;
        r0 = "";
        r3 = new java.lang.Object[]{r4};
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x01fa, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x01fb, code lost:
        r1 = r0;
        r7 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x01fe, code lost:
        r0 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x01ff, code lost:
        r7 = r9;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x021f A[Catch:{ Exception -> 0x023e, all -> 0x01fa }, LOOP:1: B:98:0x0219->B:100:0x021f, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x0223 A[EDGE_INSN: B:101:0x0223->B:102:? ?: BREAK  , SYNTHETIC, Splitter:B:101:0x0223] */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0236  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x024d A[SYNTHETIC, Splitter:B:115:0x024d] */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x025f A[SYNTHETIC, Splitter:B:121:0x025f] */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0298 A[SYNTHETIC, Splitter:B:139:0x0298] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01fa A[ExcHandler: all (r0v29 'th' java.lang.Throwable A[CUSTOM_DECLARE]), PHI: r9 
  PHI: (r9v4 ?) = (r9v3 ?), (r9v5 ?) binds: [B:96:0x0217, B:74:0x01de] A[DONT_GENERATE, DONT_INLINE], Splitter:B:74:0x01de] */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ta.audid.upload.HttpResponse sendRequest(java.lang.String r16, java.lang.String r17, boolean r18) {
        /*
            r0 = r17
            com.ta.audid.upload.HttpResponse r1 = new com.ta.audid.upload.HttpResponse
            r1.<init>()
            boolean r2 = android.text.TextUtils.isEmpty(r16)
            if (r2 == 0) goto L_0x000e
            return r1
        L_0x000e:
            r2 = 0
            java.net.URL r3 = new java.net.URL     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            r4 = r16
            r3.<init>(r4)     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            java.lang.String r4 = r3.getHost()     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            boolean r4 = android.text.TextUtils.isEmpty(r4)     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            if (r4 == 0) goto L_0x0021
            return r1
        L_0x0021:
            java.net.URLConnection r4 = r3.openConnection()     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            java.net.HttpURLConnection r4 = (java.net.HttpURLConnection) r4     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            boolean r5 = r4 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            if (r5 == 0) goto L_0x0059
            com.ta.audid.upload.UtdidSslSocketFactory r5 = mUtdidSslSocketFactory     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            if (r5 != 0) goto L_0x003a
            com.ta.audid.upload.UtdidSslSocketFactory r5 = new com.ta.audid.upload.UtdidSslSocketFactory     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            java.lang.String r6 = r3.getHost()     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            r5.<init>(r6)     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            mUtdidSslSocketFactory = r5     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
        L_0x003a:
            com.ta.audid.upload.UtdidHostnameVerifier r5 = mUtdidHostnameVerifier     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            if (r5 != 0) goto L_0x0049
            com.ta.audid.upload.UtdidHostnameVerifier r5 = new com.ta.audid.upload.UtdidHostnameVerifier     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            java.lang.String r3 = r3.getHost()     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            r5.<init>(r3)     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            mUtdidHostnameVerifier = r5     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
        L_0x0049:
            r3 = r4
            javax.net.ssl.HttpsURLConnection r3 = (javax.net.ssl.HttpsURLConnection) r3     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            com.ta.audid.upload.UtdidSslSocketFactory r5 = mUtdidSslSocketFactory     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            r3.setSSLSocketFactory(r5)     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            r3 = r4
            javax.net.ssl.HttpsURLConnection r3 = (javax.net.ssl.HttpsURLConnection) r3     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            com.ta.audid.upload.UtdidHostnameVerifier r5 = mUtdidHostnameVerifier     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
            r3.setHostnameVerifier(r5)     // Catch:{ MalformedURLException -> 0x02c4, IOException -> 0x02bb, Throwable -> 0x02b2 }
        L_0x0059:
            if (r4 == 0) goto L_0x02b1
            r3 = 1
            r4.setDoInput(r3)
            if (r18 == 0) goto L_0x0073
            r4.setDoOutput(r3)
            java.lang.String r5 = "POST"
            r4.setRequestMethod(r5)     // Catch:{ ProtocolException -> 0x006a }
            goto L_0x0078
        L_0x006a:
            r0 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.ta.audid.utils.UtdidLogger.e(r3, r0, r2)
            return r1
        L_0x0073:
            java.lang.String r5 = "GET"
            r4.setRequestMethod(r5)     // Catch:{ ProtocolException -> 0x02a8 }
        L_0x0078:
            r4.setUseCaches(r2)
            r5 = 10000(0x2710, float:1.4013E-41)
            r4.setConnectTimeout(r5)
            r4.setReadTimeout(r5)
            r4.setInstanceFollowRedirects(r3)
            java.lang.String r5 = "Content-Type"
            java.lang.String r6 = "application/x-www-form-urlencoded"
            r4.setRequestProperty(r5, r6)
            java.lang.String r5 = "Charset"
            java.lang.String r6 = "UTF-8"
            r4.setRequestProperty(r5, r6)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            com.ta.audid.Variables r6 = com.ta.audid.Variables.getInstance()
            java.lang.String r6 = r6.getAppkey()
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 != 0) goto L_0x00af
            java.lang.String r7 = "x-audid-appkey"
            r4.setRequestProperty(r7, r6)
            r5.append(r6)
        L_0x00af:
            com.ta.audid.Variables r6 = com.ta.audid.Variables.getInstance()
            android.content.Context r6 = r6.getContext()
            java.lang.String r6 = r6.getPackageName()
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 != 0) goto L_0x00cf
            java.lang.String r7 = "x-audid-appname"
            java.lang.String r8 = "UTF-8"
            java.lang.String r8 = java.net.URLEncoder.encode(r6, r8)     // Catch:{ Exception -> 0x00cf }
            r4.setRequestProperty(r7, r8)     // Catch:{ Exception -> 0x00cf }
            r5.append(r6)     // Catch:{ Exception -> 0x00cf }
        L_0x00cf:
            java.lang.String r6 = "x-audid-sdk"
            java.lang.String r7 = "2.2.3"
            r4.setRequestProperty(r6, r7)
            java.lang.String r6 = "2.2.3"
            r5.append(r6)
            com.ta.audid.Variables r6 = com.ta.audid.Variables.getInstance()
            java.lang.String r6 = r6.getCurrentTimeMillisString()
            java.lang.String r7 = "x-audid-timestamp"
            r4.setRequestProperty(r7, r6)
            java.lang.String r7 = ""
            java.lang.Object[] r8 = new java.lang.Object[r3]
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "timestamp:"
            r9.append(r10)
            r9.append(r6)
            java.lang.String r9 = r9.toString()
            r8[r2] = r9
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r7, (java.lang.Object[]) r8)
            r5.append(r6)
            r5.append(r0)
            java.lang.String r5 = r5.toString()
            java.lang.String r5 = com.ta.audid.utils.MD5Utils.getHmacMd5Hex(r5)
            java.lang.String r6 = "signature"
            byte[] r5 = r5.getBytes()
            r7 = 2
            java.lang.String r5 = com.ta.utdid2.android.utils.Base64.encodeToString(r5, r7)
            r4.setRequestProperty(r6, r5)
            long r5 = java.lang.System.currentTimeMillis()
            r7 = 0
            r4.connect()     // Catch:{ Throwable -> 0x0272 }
            if (r0 == 0) goto L_0x0147
            int r8 = r17.length()     // Catch:{ Throwable -> 0x0272 }
            if (r8 <= 0) goto L_0x0147
            java.io.DataOutputStream r8 = new java.io.DataOutputStream     // Catch:{ Throwable -> 0x0272 }
            java.io.OutputStream r9 = r4.getOutputStream()     // Catch:{ Throwable -> 0x0272 }
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0272 }
            r8.writeBytes(r0)     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            r8.flush()     // Catch:{ Throwable -> 0x0143, all -> 0x013e }
            goto L_0x0148
        L_0x013e:
            r0 = move-exception
            r1 = r0
            r7 = r8
            goto L_0x0296
        L_0x0143:
            r0 = move-exception
            r7 = r8
            goto L_0x0273
        L_0x0147:
            r8 = r7
        L_0x0148:
            if (r8 == 0) goto L_0x0159
            r8.close()     // Catch:{ IOException -> 0x014e }
            goto L_0x0159
        L_0x014e:
            r0 = move-exception
            r8 = r0
            java.lang.String r0 = ""
            java.lang.Object[] r9 = new java.lang.Object[r3]
            r9[r2] = r8
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r9)
        L_0x0159:
            int r0 = r4.getResponseCode()     // Catch:{ Exception -> 0x0168 }
            r1.httpResponseCode = r0     // Catch:{ Exception -> 0x0168 }
            java.lang.String r0 = "signature"
            java.lang.String r0 = r4.getHeaderField(r0)     // Catch:{ Exception -> 0x0168 }
            r1.signature = r0     // Catch:{ Exception -> 0x0168 }
            goto L_0x0172
        L_0x0168:
            r0 = move-exception
            java.lang.String r8 = ""
            java.lang.Object[] r9 = new java.lang.Object[r3]
            r9[r2] = r0
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r8, (java.lang.Object[]) r9)
        L_0x0172:
            java.lang.String r0 = "x-audid-timestamp"
            java.lang.String r0 = r4.getHeaderField(r0)     // Catch:{ Exception -> 0x01c6 }
            long r8 = java.lang.Long.parseLong(r0)     // Catch:{ Exception -> 0x01c6 }
            r1.timestamp = r8     // Catch:{ Exception -> 0x01c6 }
            java.lang.String r0 = ""
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x01c6 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01c6 }
            r9.<init>()     // Catch:{ Exception -> 0x01c6 }
            java.lang.String r10 = "repsonse.timestamp:"
            r9.append(r10)     // Catch:{ Exception -> 0x01c6 }
            long r10 = r1.timestamp     // Catch:{ Exception -> 0x01c6 }
            r9.append(r10)     // Catch:{ Exception -> 0x01c6 }
            java.lang.String r9 = r9.toString()     // Catch:{ Exception -> 0x01c6 }
            r8[r2] = r9     // Catch:{ Exception -> 0x01c6 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r8)     // Catch:{ Exception -> 0x01c6 }
            com.ta.audid.Variables r0 = com.ta.audid.Variables.getInstance()     // Catch:{ Exception -> 0x01c6 }
            long r8 = r0.getCurrentTimeMillis()     // Catch:{ Exception -> 0x01c6 }
            long r10 = r1.timestamp     // Catch:{ Exception -> 0x01c6 }
            r12 = 0
            int r0 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
            if (r0 <= 0) goto L_0x01c6
            long r10 = r1.timestamp     // Catch:{ Exception -> 0x01c6 }
            r12 = 1800000(0x1b7740, double:8.89318E-318)
            long r14 = r8 + r12
            int r0 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
            if (r0 > 0) goto L_0x01bd
            long r10 = r1.timestamp     // Catch:{ Exception -> 0x01c6 }
            r0 = 0
            long r8 = r8 - r12
            int r0 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r0 >= 0) goto L_0x01c6
        L_0x01bd:
            com.ta.audid.Variables r0 = com.ta.audid.Variables.getInstance()     // Catch:{ Exception -> 0x01c6 }
            long r8 = r1.timestamp     // Catch:{ Exception -> 0x01c6 }
            r0.setSystemTime(r8)     // Catch:{ Exception -> 0x01c6 }
        L_0x01c6:
            long r8 = java.lang.System.currentTimeMillis()
            long r8 = r8 - r5
            r1.rt = r8
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream
            r5.<init>()
            r6 = -1
            r8 = 2048(0x800, float:2.87E-42)
            java.io.DataInputStream r9 = new java.io.DataInputStream     // Catch:{ IOException -> 0x0204 }
            java.io.InputStream r0 = r4.getInputStream()     // Catch:{ IOException -> 0x0204 }
            r9.<init>(r0)     // Catch:{ IOException -> 0x0204 }
            byte[] r0 = new byte[r8]     // Catch:{ IOException -> 0x01fe, all -> 0x01fa }
        L_0x01e0:
            int r7 = r9.read(r0, r2, r8)     // Catch:{ IOException -> 0x01fe, all -> 0x01fa }
            if (r7 == r6) goto L_0x01ea
            r5.write(r0, r2, r7)     // Catch:{ IOException -> 0x01fe, all -> 0x01fa }
            goto L_0x01e0
        L_0x01ea:
            r9.close()     // Catch:{ Exception -> 0x01ee }
            goto L_0x0230
        L_0x01ee:
            r0 = move-exception
            r4 = r0
            java.lang.String r0 = ""
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r4
        L_0x01f6:
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r3)
            goto L_0x0230
        L_0x01fa:
            r0 = move-exception
            r1 = r0
            r7 = r9
            goto L_0x025d
        L_0x01fe:
            r0 = move-exception
            r7 = r9
            goto L_0x0205
        L_0x0201:
            r0 = move-exception
            r1 = r0
            goto L_0x025d
        L_0x0204:
            r0 = move-exception
        L_0x0205:
            java.lang.String r9 = ""
            java.lang.Object[] r10 = new java.lang.Object[r3]     // Catch:{ all -> 0x0201 }
            r10[r2] = r0     // Catch:{ all -> 0x0201 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r9, (java.lang.Object[]) r10)     // Catch:{ all -> 0x0201 }
            java.io.DataInputStream r9 = new java.io.DataInputStream     // Catch:{ Exception -> 0x0241 }
            java.io.InputStream r0 = r4.getErrorStream()     // Catch:{ Exception -> 0x0241 }
            r9.<init>(r0)     // Catch:{ Exception -> 0x0241 }
            byte[] r0 = new byte[r8]     // Catch:{ Exception -> 0x023e, all -> 0x01fa }
        L_0x0219:
            int r4 = r9.read(r0, r2, r8)     // Catch:{ Exception -> 0x023e, all -> 0x01fa }
            if (r4 == r6) goto L_0x0223
            r5.write(r0, r2, r4)     // Catch:{ Exception -> 0x023e, all -> 0x01fa }
            goto L_0x0219
        L_0x0223:
            r9.close()     // Catch:{ Exception -> 0x0227 }
            goto L_0x0230
        L_0x0227:
            r0 = move-exception
            r4 = r0
            java.lang.String r0 = ""
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r4
            goto L_0x01f6
        L_0x0230:
            int r0 = r5.size()
            if (r0 <= 0) goto L_0x02b1
            byte[] r0 = r5.toByteArray()
            r1.data = r0
            goto L_0x02b1
        L_0x023e:
            r0 = move-exception
            r7 = r9
            goto L_0x0242
        L_0x0241:
            r0 = move-exception
        L_0x0242:
            java.lang.String r4 = ""
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ all -> 0x0201 }
            r5[r2] = r0     // Catch:{ all -> 0x0201 }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r4, (java.lang.Object[]) r5)     // Catch:{ all -> 0x0201 }
            if (r7 == 0) goto L_0x025c
            r7.close()     // Catch:{ Exception -> 0x0251 }
            goto L_0x025c
        L_0x0251:
            r0 = move-exception
            r4 = r0
            java.lang.String r0 = ""
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r4
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r3)
        L_0x025c:
            return r1
        L_0x025d:
            if (r7 == 0) goto L_0x026e
            r7.close()     // Catch:{ Exception -> 0x0263 }
            goto L_0x026e
        L_0x0263:
            r0 = move-exception
            r4 = r0
            java.lang.Object[] r0 = new java.lang.Object[r3]
            r0[r2] = r4
            java.lang.String r2 = ""
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r2, (java.lang.Object[]) r0)
        L_0x026e:
            throw r1
        L_0x026f:
            r0 = move-exception
            r1 = r0
            goto L_0x0296
        L_0x0272:
            r0 = move-exception
        L_0x0273:
            java.lang.String r4 = ""
            java.lang.Object[] r8 = new java.lang.Object[r3]     // Catch:{ all -> 0x026f }
            r8[r2] = r0     // Catch:{ all -> 0x026f }
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r4, (java.lang.Object[]) r8)     // Catch:{ all -> 0x026f }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x026f }
            r0 = 0
            long r8 = r8 - r5
            r1.rt = r8     // Catch:{ all -> 0x026f }
            if (r7 == 0) goto L_0x0295
            r7.close()     // Catch:{ IOException -> 0x028a }
            goto L_0x0295
        L_0x028a:
            r0 = move-exception
            r4 = r0
            java.lang.String r0 = ""
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r3[r2] = r4
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r0, (java.lang.Object[]) r3)
        L_0x0295:
            return r1
        L_0x0296:
            if (r7 == 0) goto L_0x02a7
            r7.close()     // Catch:{ IOException -> 0x029c }
            goto L_0x02a7
        L_0x029c:
            r0 = move-exception
            r4 = r0
            java.lang.Object[] r0 = new java.lang.Object[r3]
            r0[r2] = r4
            java.lang.String r2 = ""
            com.ta.audid.utils.UtdidLogger.d((java.lang.String) r2, (java.lang.Object[]) r0)
        L_0x02a7:
            throw r1
        L_0x02a8:
            r0 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.ta.audid.utils.UtdidLogger.e(r3, r0, r2)
            return r1
        L_0x02b1:
            return r1
        L_0x02b2:
            r0 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.ta.audid.utils.UtdidLogger.e(r3, r0, r2)
            return r1
        L_0x02bb:
            r0 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.ta.audid.utils.UtdidLogger.e(r3, r0, r2)
            return r1
        L_0x02c4:
            r0 = move-exception
            java.lang.String r3 = ""
            java.lang.Object[] r2 = new java.lang.Object[r2]
            com.ta.audid.utils.UtdidLogger.e(r3, r0, r2)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ta.audid.upload.HttpUtils.sendRequest(java.lang.String, java.lang.String, boolean):com.ta.audid.upload.HttpResponse");
    }
}
