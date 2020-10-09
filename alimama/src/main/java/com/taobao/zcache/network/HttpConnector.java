package com.taobao.zcache.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.taobao.zcache.log.ZLog;
import java.net.HttpURLConnection;
import java.util.Map;
import org.apache.http.HttpHost;

public class HttpConnector {
    public static final String CACHE_CONTROL = "cache-control";
    public static final String CONTENT_LENGTH = "content-length";
    public static final String CONTENT_TYPE = "content-type";
    public static final String DATE = "date";
    public static final String ETAG = "etag";
    public static final String EXPIRES = "expires";
    public static final String IF_MODIFY_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String LAST_MODIFIED = "last-modified";
    public static final String REDIRECT_LOCATION = "location";
    public static final String RESPONSE_CODE = "response-code";
    public static final String SET_COOKIE = "Set-Cookie";
    private static String TAG = "HttpConnector";
    public static final String URL = "url";
    private HttpConnectListener<HttpResponse> mListener = null;
    private int redirectTime = 0;

    static {
        System.setProperty("http.keepAlive", "false");
    }

    public HttpResponse syncConnect(String str) {
        return syncConnect(new HttpRequest(str), (HttpConnectListener<HttpResponse>) null);
    }

    public HttpResponse syncConnect(HttpRequest httpRequest) {
        return syncConnect(httpRequest, (HttpConnectListener<HttpResponse>) null);
    }

    public HttpResponse syncConnect(HttpRequest httpRequest, HttpConnectListener<HttpResponse> httpConnectListener) {
        if (httpRequest != null) {
            String str = TAG;
            ZLog.w(str, "Request: " + httpRequest.getUri().toString());
            String str2 = null;
            this.mListener = httpConnectListener;
            int i = 0;
            this.redirectTime = 0;
            int retryTime = httpRequest.getRetryTime();
            while (i < retryTime) {
                try {
                    return dataConnect(httpRequest);
                } catch (NetWorkErrorException e) {
                    e.printStackTrace();
                    str2 = e.toString();
                    i++;
                    try {
                        Thread.sleep((long) (i * 2 * 1000));
                    } catch (InterruptedException unused) {
                        ZLog.e(TAG, "HttpConnector retry Sleep has been interrupted, go ahead");
                    }
                } catch (HttpOverFlowException e2) {
                    e2.printStackTrace();
                    str2 = e2.toString();
                } catch (RedirectException e3) {
                    e3.printStackTrace();
                    str2 = e3.toString();
                } catch (HttpsErrorException e4) {
                    e4.printStackTrace();
                    str2 = e4.toString();
                }
            }
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setErrorMsg(str2);
            return httpResponse;
        }
        throw new NullPointerException("Http connect error, request is null");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v3, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v20, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v22, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v23, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v119, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v120, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v121, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v122, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v123, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v124, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v125, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v126, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v127, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v128, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v129, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v130, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v131, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX WARNING: type inference failed for: r5v0 */
    /* JADX WARNING: type inference failed for: r5v8 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: type inference failed for: r5v11 */
    /* JADX WARNING: type inference failed for: r5v12 */
    /* JADX WARNING: type inference failed for: r5v13 */
    /* JADX WARNING: type inference failed for: r5v14 */
    /* JADX WARNING: type inference failed for: r5v15 */
    /* JADX WARNING: type inference failed for: r5v16 */
    /* JADX WARNING: type inference failed for: r5v17 */
    /* JADX WARNING: type inference failed for: r5v18 */
    /* JADX WARNING: type inference failed for: r5v19 */
    /* JADX WARNING: type inference failed for: r5v21 */
    /* JADX WARNING: type inference failed for: r5v24 */
    /* JADX WARNING: type inference failed for: r5v25 */
    /* JADX WARNING: type inference failed for: r5v26 */
    /* JADX WARNING: type inference failed for: r5v27 */
    /* JADX WARNING: type inference failed for: r5v28 */
    /* JADX WARNING: Code restructure failed: missing block: B:171:0x0293, code lost:
        r15 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:172:0x0294, code lost:
        r0 = null;
        r6 = null;
        r2 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:175:0x029c, code lost:
        r15 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:0x029d, code lost:
        r0 = null;
        r6 = null;
        r2 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:177:0x02a1, code lost:
        r15 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:178:0x02a2, code lost:
        r0 = null;
        r6 = null;
        r2 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:179:0x02a6, code lost:
        r15 = null;
        r0 = null;
        r2 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:181:0x02aa, code lost:
        r15 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:182:0x02ab, code lost:
        r0 = null;
        r6 = null;
        r2 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:211:0x0304, code lost:
        if (r2 != null) goto L_0x03c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:219:0x0316, code lost:
        r14.mListener.onError(-5, "out of memory error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:223:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:224:0x032a, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:225:0x032b, code lost:
        r15.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:228:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:229:0x0334, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:230:0x0335, code lost:
        r15.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:234:0x033e, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:235:0x033f, code lost:
        r15.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:240:0x034a, code lost:
        if (r2 != null) goto L_0x03c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:246:0x0356, code lost:
        r14.mListener.onError(-3, "ssl handshake exception");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:250:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:251:0x0367, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:252:0x0368, code lost:
        r15.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:255:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:256:0x0371, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:257:0x0372, code lost:
        r15.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:260:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:261:0x037b, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:262:0x037c, code lost:
        r15.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:267:0x0387, code lost:
        if (r2 != null) goto L_0x03c1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:293:0x03bf, code lost:
        if (r2 == null) goto L_0x0404;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:294:0x03c1, code lost:
        r2.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00df, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00e0, code lost:
        r2 = r2;
        r2 = r2;
        r2 = r2;
        r2 = r2;
        r2 = r2;
        r2 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        r6.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:323:0x0401, code lost:
        if (r2 == null) goto L_0x0404;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:325:0x0409, code lost:
        return new com.taobao.zcache.network.HttpResponse();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:329:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:330:0x0411, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:331:0x0412, code lost:
        r1.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:334:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:335:0x041b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:336:0x041c, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:339:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:340:0x0425, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:341:0x0426, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:347:0x0433, code lost:
        r2.disconnect();
     */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:270:0x038d */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x0293 A[ExcHandler: all (th java.lang.Throwable), PHI: r2 
  PHI: (r2v21 java.net.HttpURLConnection) = (r2v50 java.net.HttpURLConnection), (r2v61 java.net.HttpURLConnection), (r2v83 java.net.HttpURLConnection), (r2v89 java.net.HttpURLConnection), (r2v95 java.net.HttpURLConnection), (r2v101 java.net.HttpURLConnection), (r2v106 java.net.HttpURLConnection), (r2v112 java.net.HttpURLConnection), (r2v117 java.net.HttpURLConnection), (r2v124 java.net.HttpURLConnection), (r2v130 java.net.HttpURLConnection) binds: [B:20:0x0082, B:130:0x0220, B:169:0x0285, B:170:?, B:66:0x016c, B:57:0x0155, B:27:0x00db, B:30:0x00e0, B:28:?, B:16:0x0078, B:17:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:16:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:175:0x029c A[ExcHandler: OutOfMemoryError (e java.lang.OutOfMemoryError), PHI: r2 
  PHI: (r2v19 java.net.HttpURLConnection) = (r2v48 java.net.HttpURLConnection), (r2v59 java.net.HttpURLConnection), (r2v81 java.net.HttpURLConnection), (r2v87 java.net.HttpURLConnection), (r2v93 java.net.HttpURLConnection), (r2v99 java.net.HttpURLConnection), (r2v105 java.net.HttpURLConnection), (r2v110 java.net.HttpURLConnection), (r2v116 java.net.HttpURLConnection), (r2v122 java.net.HttpURLConnection), (r2v128 java.net.HttpURLConnection) binds: [B:20:0x0082, B:130:0x0220, B:169:0x0285, B:170:?, B:66:0x016c, B:57:0x0155, B:27:0x00db, B:30:0x00e0, B:28:?, B:16:0x0078, B:17:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:16:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:177:0x02a1 A[ExcHandler: SSLHandshakeException (e javax.net.ssl.SSLHandshakeException), PHI: r2 
  PHI: (r2v18 java.net.HttpURLConnection) = (r2v47 java.net.HttpURLConnection), (r2v58 java.net.HttpURLConnection), (r2v80 java.net.HttpURLConnection), (r2v86 java.net.HttpURLConnection), (r2v92 java.net.HttpURLConnection), (r2v98 java.net.HttpURLConnection), (r2v104 java.net.HttpURLConnection), (r2v109 java.net.HttpURLConnection), (r2v115 java.net.HttpURLConnection), (r2v121 java.net.HttpURLConnection), (r2v127 java.net.HttpURLConnection) binds: [B:20:0x0082, B:130:0x0220, B:169:0x0285, B:170:?, B:66:0x016c, B:57:0x0155, B:27:0x00db, B:30:0x00e0, B:28:?, B:16:0x0078, B:17:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:16:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:180:? A[ExcHandler: HttpOverFlowException (unused com.taobao.zcache.network.HttpConnector$HttpOverFlowException), PHI: r2 
  PHI: (r2v17 java.net.HttpURLConnection) = (r2v46 java.net.HttpURLConnection), (r2v57 java.net.HttpURLConnection), (r2v79 java.net.HttpURLConnection), (r2v85 java.net.HttpURLConnection), (r2v91 java.net.HttpURLConnection), (r2v97 java.net.HttpURLConnection), (r2v103 java.net.HttpURLConnection), (r2v108 java.net.HttpURLConnection), (r2v114 java.net.HttpURLConnection), (r2v120 java.net.HttpURLConnection), (r2v126 java.net.HttpURLConnection) binds: [B:20:0x0082, B:130:0x0220, B:169:0x0285, B:170:?, B:66:0x016c, B:57:0x0155, B:27:0x00db, B:30:0x00e0, B:28:?, B:16:0x0078, B:17:?] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:16:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:181:0x02aa A[ExcHandler: RedirectException (e com.taobao.zcache.network.HttpConnector$RedirectException), PHI: r2 
  PHI: (r2v16 java.net.HttpURLConnection) = (r2v45 java.net.HttpURLConnection), (r2v56 java.net.HttpURLConnection), (r2v78 java.net.HttpURLConnection), (r2v84 java.net.HttpURLConnection), (r2v90 java.net.HttpURLConnection), (r2v96 java.net.HttpURLConnection), (r2v102 java.net.HttpURLConnection), (r2v107 java.net.HttpURLConnection), (r2v113 java.net.HttpURLConnection), (r2v119 java.net.HttpURLConnection), (r2v125 java.net.HttpURLConnection) binds: [B:20:0x0082, B:130:0x0220, B:169:0x0285, B:170:?, B:66:0x016c, B:57:0x0155, B:27:0x00db, B:30:0x00e0, B:28:?, B:16:0x0078, B:17:?] A[DONT_GENERATE, DONT_INLINE], Splitter:B:16:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x02c1 A[Catch:{ all -> 0x0308 }] */
    /* JADX WARNING: Removed duplicated region for block: B:193:0x02e0 A[SYNTHETIC, Splitter:B:193:0x02e0] */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x02ea A[SYNTHETIC, Splitter:B:198:0x02ea] */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x02f4 A[SYNTHETIC, Splitter:B:203:0x02f4] */
    /* JADX WARNING: Removed duplicated region for block: B:219:0x0316 A[Catch:{ all -> 0x040a }] */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x0326 A[SYNTHETIC, Splitter:B:222:0x0326] */
    /* JADX WARNING: Removed duplicated region for block: B:227:0x0330 A[SYNTHETIC, Splitter:B:227:0x0330] */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x033a A[SYNTHETIC, Splitter:B:232:0x033a] */
    /* JADX WARNING: Removed duplicated region for block: B:246:0x0356 A[Catch:{ all -> 0x040a }] */
    /* JADX WARNING: Removed duplicated region for block: B:249:0x0363 A[SYNTHETIC, Splitter:B:249:0x0363] */
    /* JADX WARNING: Removed duplicated region for block: B:254:0x036d A[SYNTHETIC, Splitter:B:254:0x036d] */
    /* JADX WARNING: Removed duplicated region for block: B:259:0x0377 A[SYNTHETIC, Splitter:B:259:0x0377] */
    /* JADX WARNING: Removed duplicated region for block: B:273:0x0391 A[Catch:{ all -> 0x03c5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:275:0x039b A[SYNTHETIC, Splitter:B:275:0x039b] */
    /* JADX WARNING: Removed duplicated region for block: B:280:0x03a5 A[SYNTHETIC, Splitter:B:280:0x03a5] */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x03af A[SYNTHETIC, Splitter:B:285:0x03af] */
    /* JADX WARNING: Removed duplicated region for block: B:302:0x03d1 A[Catch:{ all -> 0x040a }] */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x03dd A[SYNTHETIC, Splitter:B:305:0x03dd] */
    /* JADX WARNING: Removed duplicated region for block: B:310:0x03e7 A[SYNTHETIC, Splitter:B:310:0x03e7] */
    /* JADX WARNING: Removed duplicated region for block: B:315:0x03f1 A[SYNTHETIC, Splitter:B:315:0x03f1] */
    /* JADX WARNING: Removed duplicated region for block: B:328:0x040d A[SYNTHETIC, Splitter:B:328:0x040d] */
    /* JADX WARNING: Removed duplicated region for block: B:333:0x0417 A[SYNTHETIC, Splitter:B:333:0x0417] */
    /* JADX WARNING: Removed duplicated region for block: B:338:0x0421 A[SYNTHETIC, Splitter:B:338:0x0421] */
    /* JADX WARNING: Removed duplicated region for block: B:347:0x0433  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:216:0x0312=Splitter:B:216:0x0312, B:243:0x0352=Splitter:B:243:0x0352, B:299:0x03cd=Splitter:B:299:0x03cd} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.taobao.zcache.network.HttpResponse dataConnect(com.taobao.zcache.network.HttpRequest r15) throws com.taobao.zcache.network.HttpConnector.NetWorkErrorException, com.taobao.zcache.network.HttpConnector.HttpOverFlowException, com.taobao.zcache.network.HttpConnector.RedirectException, com.taobao.zcache.network.HttpConnector.HttpsErrorException {
        /*
            r14 = this;
            android.net.Uri r0 = r15.getUri()
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener
            if (r1 == 0) goto L_0x000d
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener
            r1.onStart()
        L_0x000d:
            com.taobao.zcache.network.HttpResponse r1 = new com.taobao.zcache.network.HttpResponse
            r1.<init>()
            java.lang.String r2 = "https"
            java.lang.String r3 = r0.getScheme()
            boolean r2 = r2.equalsIgnoreCase(r3)
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream
            r4 = 128(0x80, float:1.794E-43)
            r3.<init>(r4)
            r4 = -1
            r5 = 0
            java.net.URL r6 = new java.net.URL     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r0 = r0.toString()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            r6.<init>(r0)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r0 = r6.getHost()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            if (r2 == 0) goto L_0x007c
            java.lang.String r2 = TAG     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r7 = "proxy or https"
            com.taobao.zcache.log.ZLog.i(r2, r7)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            com.taobao.zcache.global.ZCacheGlobal r2 = com.taobao.zcache.global.ZCacheGlobal.instance()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            android.content.Context r2 = r2.context()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            org.apache.http.HttpHost r2 = getHttpsProxyInfo(r2)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            if (r2 == 0) goto L_0x0059
            com.taobao.zcache.network.SSLTunnelSocketFactory r7 = new com.taobao.zcache.network.SSLTunnelSocketFactory     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r8 = r2.getHostName()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            int r2 = r2.getPort()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r9 = "taobao_hybrid"
            r7.<init>(r8, r2, r5, r9)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            goto L_0x0061
        L_0x0059:
            java.lang.String r2 = TAG     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r7 = "https:proxy: none"
            com.taobao.zcache.log.ZLog.d(r2, r7)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            r7 = r5
        L_0x0061:
            java.net.URLConnection r2 = r6.openConnection()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            if (r7 == 0) goto L_0x006c
            r2.setSSLSocketFactory(r7)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
        L_0x006c:
            org.apache.http.conn.ssl.StrictHostnameVerifier r6 = new org.apache.http.conn.ssl.StrictHostnameVerifier     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            r6.<init>()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            r2.setHostnameVerifier(r6)     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.lang.String r6 = "Connection"
            java.lang.String r7 = "Keep-Alive"
            r2.setRequestProperty(r6, r7)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            goto L_0x0082
        L_0x007c:
            java.net.URLConnection r2 = r6.openConnection()     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ RedirectException -> 0x03c9, HttpOverFlowException -> 0x038a, SSLHandshakeException -> 0x034e, OutOfMemoryError -> 0x030e, Throwable -> 0x02b5, all -> 0x02af }
        L_0x0082:
            r14.setConnectProp(r2, r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r6 = r14.mListener     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r7 = 0
            if (r6 == 0) goto L_0x008f
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r6 = r14.mListener     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r6.onProcess(r7)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
        L_0x008f:
            java.lang.String r6 = "post"
            java.lang.String r8 = r15.getMethod()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            boolean r6 = r6.equalsIgnoreCase(r8)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r8 = 1
            if (r6 == 0) goto L_0x00db
            java.lang.String r6 = TAG     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r9.<init>()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r10 = "post data: "
            r9.append(r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r10 = new java.lang.String     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            byte[] r11 = r15.getPostData()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r10.<init>(r11)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r9.append(r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r9 = r9.toString()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            com.taobao.zcache.log.ZLog.d(r6, r9)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r2.setDoOutput(r8)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r2.setDoInput(r8)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r6 = "POST"
            r2.setRequestMethod(r6)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r2.connect()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.io.OutputStream r6 = r2.getOutputStream()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            byte[] r9 = r15.getPostData()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r6.write(r9)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r6.flush()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r6.close()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            goto L_0x00e3
        L_0x00db:
            r2.connect()     // Catch:{ Throwable -> 0x00df, RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, all -> 0x0293 }
            goto L_0x00e3
        L_0x00df:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
        L_0x00e3:
            int r6 = r2.getResponseCode()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r9 = TAG     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r10.<init>()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r11 = "responeCode:"
            r10.append(r11)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r10.append(r6)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r10 = r10.toString()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            com.taobao.zcache.log.ZLog.d(r9, r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r9 = 300(0x12c, float:4.2E-43)
            if (r6 < r9) goto L_0x0174
            r10 = 400(0x190, float:5.6E-43)
            if (r6 >= r10) goto L_0x0174
            r10 = 304(0x130, float:4.26E-43)
            if (r6 == r10) goto L_0x0174
            boolean r10 = r15.isRedirect()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            if (r10 == 0) goto L_0x0174
            int r10 = r14.redirectTime     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r11 = 5
            if (r10 > r11) goto L_0x016c
            int r10 = r14.redirectTime     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            int r10 = r10 + r8
            r14.redirectTime = r10     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r10 = "location"
            java.lang.String r10 = r2.getHeaderField(r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            if (r10 == 0) goto L_0x0174
            java.lang.String r1 = r10.toLowerCase()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r7 = "http"
            boolean r1 = r1.startsWith(r7)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            if (r1 != 0) goto L_0x0138
            java.net.URL r1 = new java.net.URL     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r7 = "http"
            r1.<init>(r7, r0, r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r10 = r1.toString()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
        L_0x0138:
            r0 = 305(0x131, float:4.27E-43)
            if (r6 == r0) goto L_0x0155
            android.net.Uri r0 = android.net.Uri.parse(r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r15.setUri(r0)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            com.taobao.zcache.network.HttpResponse r15 = r14.dataConnect(r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r3.close()     // Catch:{ Exception -> 0x014b }
            goto L_0x014f
        L_0x014b:
            r0 = move-exception
            r0.printStackTrace()
        L_0x014f:
            if (r2 == 0) goto L_0x0154
            r2.disconnect()
        L_0x0154:
            return r15
        L_0x0155:
            com.taobao.zcache.network.HttpRequest r15 = new com.taobao.zcache.network.HttpRequest     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r15.<init>(r10)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            com.taobao.zcache.network.HttpResponse r15 = r14.dataConnect(r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r3.close()     // Catch:{ Exception -> 0x0162 }
            goto L_0x0166
        L_0x0162:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0166:
            if (r2 == 0) goto L_0x016b
            r2.disconnect()
        L_0x016b:
            return r15
        L_0x016c:
            com.taobao.zcache.network.HttpConnector$RedirectException r15 = new com.taobao.zcache.network.HttpConnector$RedirectException     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r0 = "too many redirect"
            r15.<init>(r0)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            throw r15     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
        L_0x0174:
            r1.setHttpCode(r6)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
        L_0x0177:
            java.lang.String r15 = r2.getHeaderFieldKey(r8)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            if (r15 != 0) goto L_0x0283
            r15 = 200(0xc8, float:2.8E-43)
            if (r6 < r15) goto L_0x0237
            if (r6 >= r9) goto L_0x0237
            int r15 = r2.getContentLength()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r0 = 5242880(0x500000, float:7.34684E-39)
            if (r15 > r0) goto L_0x0220
            java.io.InputStream r0 = r2.getInputStream()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r6 = r2.getContentEncoding()     // Catch:{ RedirectException -> 0x021c, HttpOverFlowException -> 0x0219, SSLHandshakeException -> 0x0215, OutOfMemoryError -> 0x0211, Throwable -> 0x020d, all -> 0x0209 }
            if (r6 == 0) goto L_0x01bc
            java.lang.String r8 = "gzip"
            boolean r6 = r8.equals(r6)     // Catch:{ RedirectException -> 0x021c, HttpOverFlowException -> 0x0219, SSLHandshakeException -> 0x0215, OutOfMemoryError -> 0x0211, Throwable -> 0x020d, all -> 0x0209 }
            if (r6 == 0) goto L_0x01bc
            java.util.zip.GZIPInputStream r6 = new java.util.zip.GZIPInputStream     // Catch:{ RedirectException -> 0x021c, HttpOverFlowException -> 0x0219, SSLHandshakeException -> 0x0215, OutOfMemoryError -> 0x0211, Throwable -> 0x020d, all -> 0x0209 }
            r6.<init>(r0)     // Catch:{ RedirectException -> 0x021c, HttpOverFlowException -> 0x0219, SSLHandshakeException -> 0x0215, OutOfMemoryError -> 0x0211, Throwable -> 0x020d, all -> 0x0209 }
            java.io.DataInputStream r8 = new java.io.DataInputStream     // Catch:{ RedirectException -> 0x01b9, HttpOverFlowException -> 0x01b6, SSLHandshakeException -> 0x01b3, OutOfMemoryError -> 0x01b0, Throwable -> 0x01aa }
            r8.<init>(r6)     // Catch:{ RedirectException -> 0x01b9, HttpOverFlowException -> 0x01b6, SSLHandshakeException -> 0x01b3, OutOfMemoryError -> 0x01b0, Throwable -> 0x01aa }
            r5 = r6
            r6 = r8
            goto L_0x01c1
        L_0x01aa:
            r15 = move-exception
            r13 = r6
            r6 = r5
            r5 = r13
            goto L_0x02b9
        L_0x01b0:
            r15 = move-exception
            goto L_0x0312
        L_0x01b3:
            r15 = move-exception
            goto L_0x0352
        L_0x01b6:
            r15 = r6
            goto L_0x038d
        L_0x01b9:
            r15 = move-exception
            goto L_0x03cd
        L_0x01bc:
            java.io.DataInputStream r6 = new java.io.DataInputStream     // Catch:{ RedirectException -> 0x021c, HttpOverFlowException -> 0x0219, SSLHandshakeException -> 0x0215, OutOfMemoryError -> 0x0211, Throwable -> 0x020d, all -> 0x0209 }
            r6.<init>(r0)     // Catch:{ RedirectException -> 0x021c, HttpOverFlowException -> 0x0219, SSLHandshakeException -> 0x0215, OutOfMemoryError -> 0x0211, Throwable -> 0x020d, all -> 0x0209 }
        L_0x01c1:
            r8 = 2048(0x800, float:2.87E-42)
            byte[] r9 = new byte[r8]     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            r10 = r15
            r15 = 0
        L_0x01c7:
            int r11 = r6.read(r9, r7, r8)     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            if (r11 == r4) goto L_0x01e6
            r3.write(r9, r7, r11)     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r12 = r14.mListener     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            if (r12 == 0) goto L_0x01c7
            int r15 = r15 + r11
            if (r15 <= r10) goto L_0x01d8
            r10 = r15
        L_0x01d8:
            float r11 = (float) r15     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            float r12 = (float) r10     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            float r11 = r11 / r12
            r12 = 1120403456(0x42c80000, float:100.0)
            float r11 = r11 * r12
            int r11 = (int) r11     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r12 = r14.mListener     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            r12.onProcess(r11)     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            goto L_0x01c7
        L_0x01e6:
            byte[] r15 = r3.toByteArray()     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            r1.setData(r15)     // Catch:{ RedirectException -> 0x0203, HttpOverFlowException -> 0x01ff, SSLHandshakeException -> 0x01f9, OutOfMemoryError -> 0x01f3, Throwable -> 0x01f0 }
            r15 = r5
            r5 = r6
            goto L_0x0239
        L_0x01f0:
            r15 = move-exception
            goto L_0x02b9
        L_0x01f3:
            r15 = move-exception
            r13 = r6
            r6 = r5
            r5 = r13
            goto L_0x0312
        L_0x01f9:
            r15 = move-exception
            r13 = r6
            r6 = r5
            r5 = r13
            goto L_0x0352
        L_0x01ff:
            r15 = r5
            r5 = r6
            goto L_0x038d
        L_0x0203:
            r15 = move-exception
            r13 = r6
            r6 = r5
            r5 = r13
            goto L_0x03cd
        L_0x0209:
            r15 = move-exception
            r6 = r5
            goto L_0x040b
        L_0x020d:
            r15 = move-exception
            r6 = r5
            goto L_0x02b9
        L_0x0211:
            r15 = move-exception
            r6 = r5
            goto L_0x0312
        L_0x0215:
            r15 = move-exception
            r6 = r5
            goto L_0x0352
        L_0x0219:
            r15 = r5
            goto L_0x038d
        L_0x021c:
            r15 = move-exception
            r6 = r5
            goto L_0x03cd
        L_0x0220:
            com.taobao.zcache.network.HttpConnector$HttpOverFlowException r0 = new com.taobao.zcache.network.HttpConnector$HttpOverFlowException     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r1.<init>()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r6 = "The Content-Length is too large:"
            r1.append(r6)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r1.append(r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r15 = r1.toString()     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r0.<init>(r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            throw r0     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
        L_0x0237:
            r15 = r5
            r0 = r15
        L_0x0239:
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r6 = r14.mListener     // Catch:{ RedirectException -> 0x027e, HttpOverFlowException -> 0x038d, SSLHandshakeException -> 0x0279, OutOfMemoryError -> 0x0274, Throwable -> 0x026e }
            if (r6 == 0) goto L_0x0242
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r6 = r14.mListener     // Catch:{ RedirectException -> 0x027e, HttpOverFlowException -> 0x038d, SSLHandshakeException -> 0x0279, OutOfMemoryError -> 0x0274, Throwable -> 0x026e }
            r6.onFinish(r1, r7)     // Catch:{ RedirectException -> 0x027e, HttpOverFlowException -> 0x038d, SSLHandshakeException -> 0x0279, OutOfMemoryError -> 0x0274, Throwable -> 0x026e }
        L_0x0242:
            if (r5 == 0) goto L_0x024c
            r5.close()     // Catch:{ Exception -> 0x0248 }
            goto L_0x024c
        L_0x0248:
            r4 = move-exception
            r4.printStackTrace()
        L_0x024c:
            if (r0 == 0) goto L_0x0256
            r0.close()     // Catch:{ Exception -> 0x0252 }
            goto L_0x0256
        L_0x0252:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0256:
            if (r15 == 0) goto L_0x0260
            r15.close()     // Catch:{ Exception -> 0x025c }
            goto L_0x0260
        L_0x025c:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0260:
            r3.close()     // Catch:{ Exception -> 0x0264 }
            goto L_0x0268
        L_0x0264:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0268:
            if (r2 == 0) goto L_0x026d
            r2.disconnect()
        L_0x026d:
            return r1
        L_0x026e:
            r1 = move-exception
            r6 = r5
            r5 = r15
            r15 = r1
            goto L_0x02b9
        L_0x0274:
            r1 = move-exception
            r6 = r15
            r15 = r1
            goto L_0x0312
        L_0x0279:
            r1 = move-exception
            r6 = r15
            r15 = r1
            goto L_0x0352
        L_0x027e:
            r1 = move-exception
            r6 = r15
            r15 = r1
            goto L_0x03cd
        L_0x0283:
            int r8 = r8 + 1
            java.lang.String r0 = r2.getHeaderField(r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            r1.addHeader(r15, r0)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            java.lang.String r0 = "Set-Cookie"
            r0.equals(r15)     // Catch:{ RedirectException -> 0x02aa, HttpOverFlowException -> 0x02a6, SSLHandshakeException -> 0x02a1, OutOfMemoryError -> 0x029c, Throwable -> 0x0298, all -> 0x0293 }
            goto L_0x0177
        L_0x0293:
            r15 = move-exception
            r0 = r5
            r6 = r0
            goto L_0x040b
        L_0x0298:
            r15 = move-exception
            r0 = r5
            r6 = r0
            goto L_0x02b9
        L_0x029c:
            r15 = move-exception
            r0 = r5
            r6 = r0
            goto L_0x0312
        L_0x02a1:
            r15 = move-exception
            r0 = r5
            r6 = r0
            goto L_0x0352
        L_0x02a6:
            r15 = r5
            r0 = r15
            goto L_0x038d
        L_0x02aa:
            r15 = move-exception
            r0 = r5
            r6 = r0
            goto L_0x03cd
        L_0x02af:
            r15 = move-exception
            r0 = r5
            r2 = r0
            r6 = r2
            goto L_0x040b
        L_0x02b5:
            r15 = move-exception
            r0 = r5
            r2 = r0
            r6 = r2
        L_0x02b9:
            java.lang.String r1 = r15.getMessage()     // Catch:{ all -> 0x0308 }
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r4 = r14.mListener     // Catch:{ all -> 0x0308 }
            if (r4 == 0) goto L_0x02d8
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r4 = r14.mListener     // Catch:{ all -> 0x0308 }
            r7 = -4
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0308 }
            r8.<init>()     // Catch:{ all -> 0x0308 }
            java.lang.String r9 = "network exception: "
            r8.append(r9)     // Catch:{ all -> 0x0308 }
            r8.append(r1)     // Catch:{ all -> 0x0308 }
            java.lang.String r1 = r8.toString()     // Catch:{ all -> 0x0308 }
            r4.onError(r7, r1)     // Catch:{ all -> 0x0308 }
        L_0x02d8:
            r3.reset()     // Catch:{ all -> 0x0308 }
            r15.printStackTrace()     // Catch:{ all -> 0x0308 }
            if (r6 == 0) goto L_0x02e8
            r6.close()     // Catch:{ Exception -> 0x02e4 }
            goto L_0x02e8
        L_0x02e4:
            r15 = move-exception
            r15.printStackTrace()
        L_0x02e8:
            if (r0 == 0) goto L_0x02f2
            r0.close()     // Catch:{ Exception -> 0x02ee }
            goto L_0x02f2
        L_0x02ee:
            r15 = move-exception
            r15.printStackTrace()
        L_0x02f2:
            if (r5 == 0) goto L_0x02fc
            r5.close()     // Catch:{ Exception -> 0x02f8 }
            goto L_0x02fc
        L_0x02f8:
            r15 = move-exception
            r15.printStackTrace()
        L_0x02fc:
            r3.close()     // Catch:{ Exception -> 0x0300 }
            goto L_0x0304
        L_0x0300:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0304:
            if (r2 == 0) goto L_0x0404
            goto L_0x03c1
        L_0x0308:
            r15 = move-exception
            r13 = r6
            r6 = r5
            r5 = r13
            goto L_0x040b
        L_0x030e:
            r15 = move-exception
            r0 = r5
            r2 = r0
            r6 = r2
        L_0x0312:
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x040a }
            if (r1 == 0) goto L_0x031e
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x040a }
            r4 = -5
            java.lang.String r7 = "out of memory error"
            r1.onError(r4, r7)     // Catch:{ all -> 0x040a }
        L_0x031e:
            r3.reset()     // Catch:{ all -> 0x040a }
            r15.printStackTrace()     // Catch:{ all -> 0x040a }
            if (r5 == 0) goto L_0x032e
            r5.close()     // Catch:{ Exception -> 0x032a }
            goto L_0x032e
        L_0x032a:
            r15 = move-exception
            r15.printStackTrace()
        L_0x032e:
            if (r0 == 0) goto L_0x0338
            r0.close()     // Catch:{ Exception -> 0x0334 }
            goto L_0x0338
        L_0x0334:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0338:
            if (r6 == 0) goto L_0x0342
            r6.close()     // Catch:{ Exception -> 0x033e }
            goto L_0x0342
        L_0x033e:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0342:
            r3.close()     // Catch:{ Exception -> 0x0346 }
            goto L_0x034a
        L_0x0346:
            r15 = move-exception
            r15.printStackTrace()
        L_0x034a:
            if (r2 == 0) goto L_0x0404
            goto L_0x03c1
        L_0x034e:
            r15 = move-exception
            r0 = r5
            r2 = r0
            r6 = r2
        L_0x0352:
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x040a }
            if (r1 == 0) goto L_0x035e
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x040a }
            r4 = -3
            java.lang.String r7 = "ssl handshake exception"
            r1.onError(r4, r7)     // Catch:{ all -> 0x040a }
        L_0x035e:
            r15.printStackTrace()     // Catch:{ all -> 0x040a }
            if (r5 == 0) goto L_0x036b
            r5.close()     // Catch:{ Exception -> 0x0367 }
            goto L_0x036b
        L_0x0367:
            r15 = move-exception
            r15.printStackTrace()
        L_0x036b:
            if (r0 == 0) goto L_0x0375
            r0.close()     // Catch:{ Exception -> 0x0371 }
            goto L_0x0375
        L_0x0371:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0375:
            if (r6 == 0) goto L_0x037f
            r6.close()     // Catch:{ Exception -> 0x037b }
            goto L_0x037f
        L_0x037b:
            r15 = move-exception
            r15.printStackTrace()
        L_0x037f:
            r3.close()     // Catch:{ Exception -> 0x0383 }
            goto L_0x0387
        L_0x0383:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0387:
            if (r2 == 0) goto L_0x0404
            goto L_0x03c1
        L_0x038a:
            r15 = r5
            r0 = r15
            r2 = r0
        L_0x038d:
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x03c5 }
            if (r1 == 0) goto L_0x0399
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x03c5 }
            r4 = -2
            java.lang.String r6 = "connect file is too large"
            r1.onError(r4, r6)     // Catch:{ all -> 0x03c5 }
        L_0x0399:
            if (r5 == 0) goto L_0x03a3
            r5.close()     // Catch:{ Exception -> 0x039f }
            goto L_0x03a3
        L_0x039f:
            r1 = move-exception
            r1.printStackTrace()
        L_0x03a3:
            if (r0 == 0) goto L_0x03ad
            r0.close()     // Catch:{ Exception -> 0x03a9 }
            goto L_0x03ad
        L_0x03a9:
            r0 = move-exception
            r0.printStackTrace()
        L_0x03ad:
            if (r15 == 0) goto L_0x03b7
            r15.close()     // Catch:{ Exception -> 0x03b3 }
            goto L_0x03b7
        L_0x03b3:
            r15 = move-exception
            r15.printStackTrace()
        L_0x03b7:
            r3.close()     // Catch:{ Exception -> 0x03bb }
            goto L_0x03bf
        L_0x03bb:
            r15 = move-exception
            r15.printStackTrace()
        L_0x03bf:
            if (r2 == 0) goto L_0x0404
        L_0x03c1:
            r2.disconnect()
            goto L_0x0404
        L_0x03c5:
            r1 = move-exception
            r6 = r15
            r15 = r1
            goto L_0x040b
        L_0x03c9:
            r15 = move-exception
            r0 = r5
            r2 = r0
            r6 = r2
        L_0x03cd:
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x040a }
            if (r1 == 0) goto L_0x03d8
            com.taobao.zcache.network.HttpConnectListener<com.taobao.zcache.network.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x040a }
            java.lang.String r7 = "too many redirect"
            r1.onError(r4, r7)     // Catch:{ all -> 0x040a }
        L_0x03d8:
            r15.printStackTrace()     // Catch:{ all -> 0x040a }
            if (r5 == 0) goto L_0x03e5
            r5.close()     // Catch:{ Exception -> 0x03e1 }
            goto L_0x03e5
        L_0x03e1:
            r15 = move-exception
            r15.printStackTrace()
        L_0x03e5:
            if (r0 == 0) goto L_0x03ef
            r0.close()     // Catch:{ Exception -> 0x03eb }
            goto L_0x03ef
        L_0x03eb:
            r15 = move-exception
            r15.printStackTrace()
        L_0x03ef:
            if (r6 == 0) goto L_0x03f9
            r6.close()     // Catch:{ Exception -> 0x03f5 }
            goto L_0x03f9
        L_0x03f5:
            r15 = move-exception
            r15.printStackTrace()
        L_0x03f9:
            r3.close()     // Catch:{ Exception -> 0x03fd }
            goto L_0x0401
        L_0x03fd:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0401:
            if (r2 == 0) goto L_0x0404
            goto L_0x03c1
        L_0x0404:
            com.taobao.zcache.network.HttpResponse r15 = new com.taobao.zcache.network.HttpResponse
            r15.<init>()
            return r15
        L_0x040a:
            r15 = move-exception
        L_0x040b:
            if (r5 == 0) goto L_0x0415
            r5.close()     // Catch:{ Exception -> 0x0411 }
            goto L_0x0415
        L_0x0411:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0415:
            if (r0 == 0) goto L_0x041f
            r0.close()     // Catch:{ Exception -> 0x041b }
            goto L_0x041f
        L_0x041b:
            r0 = move-exception
            r0.printStackTrace()
        L_0x041f:
            if (r6 == 0) goto L_0x0429
            r6.close()     // Catch:{ Exception -> 0x0425 }
            goto L_0x0429
        L_0x0425:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0429:
            r3.close()     // Catch:{ Exception -> 0x042d }
            goto L_0x0431
        L_0x042d:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0431:
            if (r2 == 0) goto L_0x0436
            r2.disconnect()
        L_0x0436:
            throw r15
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.zcache.network.HttpConnector.dataConnect(com.taobao.zcache.network.HttpRequest):com.taobao.zcache.network.HttpResponse");
    }

    public static HttpHost getHttpsProxyInfo(Context context) {
        NetworkInfo networkInfo;
        if (Build.VERSION.SDK_INT < 11) {
            try {
                networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            } catch (Exception e) {
                e.printStackTrace();
                networkInfo = null;
            }
            if (networkInfo == null || !networkInfo.isAvailable() || networkInfo.getType() != 0) {
                return null;
            }
            String defaultHost = Proxy.getDefaultHost();
            int defaultPort = Proxy.getDefaultPort();
            if (defaultHost != null) {
                return new HttpHost(defaultHost, defaultPort);
            }
            return null;
        }
        String property = System.getProperty("https.proxyHost");
        String property2 = System.getProperty("https.proxyPort");
        if (!TextUtils.isEmpty(property)) {
            return new HttpHost(property, Integer.parseInt(property2));
        }
        return null;
    }

    private void setConnectProp(HttpURLConnection httpURLConnection, HttpRequest httpRequest) {
        int retryTime = httpRequest.getRetryTime() + 1;
        httpURLConnection.setConnectTimeout(httpRequest.getConnectTimeout() * retryTime);
        httpURLConnection.setReadTimeout(httpRequest.getReadTimeout() * retryTime);
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestProperty(HttpConstant.HOST, httpRequest.getUri().getHost());
        httpURLConnection.setRequestProperty("Connection", "close");
        httpURLConnection.setRequestProperty(HttpConstant.ACCEPT_ENCODING, "gzip");
        Map<String, String> headers = httpRequest.getHeaders();
        if (headers != null) {
            for (Map.Entry next : headers.entrySet()) {
                httpURLConnection.setRequestProperty((String) next.getKey(), (String) next.getValue());
            }
        }
        httpURLConnection.setUseCaches(false);
    }

    class HttpsErrorException extends Exception {
        public HttpsErrorException(String str) {
            super(str);
        }
    }

    class NetWorkErrorException extends Exception {
        public NetWorkErrorException(String str) {
            super(str);
        }
    }

    class HttpOverFlowException extends Exception {
        public HttpOverFlowException(String str) {
            super(str);
        }
    }

    class RedirectException extends Exception {
        public RedirectException(String str) {
            super(str);
        }
    }
}
