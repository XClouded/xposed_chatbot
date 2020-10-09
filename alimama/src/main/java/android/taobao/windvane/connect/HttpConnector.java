package android.taobao.windvane.connect;

import android.taobao.windvane.WVCookieManager;
import android.taobao.windvane.util.TaoLog;

import java.net.HttpURLConnection;
import java.util.Map;

import anet.channel.util.HttpConstant;

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
            String str = null;
            this.mListener = httpConnectListener;
            int i = 0;
            this.redirectTime = 0;
            int retryTime = httpRequest.getRetryTime();
            while (i < retryTime) {
                try {
                    return dataConnect(httpRequest);
                } catch (NetWorkErrorException e) {
                    e.printStackTrace();
                    str = e.toString();
                    i++;
                    try {
                        Thread.sleep((long) (i * 2 * 1000));
                    } catch (InterruptedException unused) {
                        TaoLog.e(TAG, "HttpConnector retry Sleep has been interrupted, go ahead");
                    }
                } catch (HttpOverFlowException e2) {
                    e2.printStackTrace();
                    str = e2.toString();
                } catch (RedirectException e3) {
                    e3.printStackTrace();
                    str = e3.toString();
                } catch (HttpsErrorException e4) {
                    e4.printStackTrace();
                    str = e4.toString();
                }
            }
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setErrorMsg(str);
            return httpResponse;
        }
        throw new NullPointerException("Http connect error, request is null");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v1, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v3, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v3, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v3, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v6, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v4, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v5, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v5, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v6, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v7, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v7, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v9, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v10, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v12, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v15, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v16, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v11, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v17, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v12, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v15, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v19, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v16, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v19, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v10, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v17, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v20, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v16, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v21, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v18, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v17, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v18, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v24, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v19, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v24, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v11, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v20, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v25, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v26, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v21, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v27, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v21, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v28, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v23, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v29, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v24, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v15, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v29, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v30, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v31, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v32, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v33, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v33, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v37, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v38, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v34, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v39, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v41, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v36, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v43, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v0, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v44, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v17, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v45, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v18, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v37, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v47, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v19, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v20, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v48, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v50, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v21, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v51, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v45, resolved type: android.net.Uri} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v22, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v47, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v23, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v48, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v24, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v25, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v26, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v58, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v27, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v52, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v59, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v44, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v60, resolved type: android.taobao.windvane.connect.HttpResponse} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v48, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v64, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v65, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v82, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v85, resolved type: java.net.HttpURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v61, resolved type: java.util.zip.GZIPInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v28, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v29, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v30, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v31, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v32, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v33, resolved type: java.io.DataInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v63, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v68, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v73, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v114, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v115, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v116, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v117, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v118, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v119, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v120, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v121, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v122, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v123, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v124, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v125, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v126, resolved type: javax.net.ssl.HttpsURLConnection} */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r6v4 */
    /* JADX WARNING: type inference failed for: r6v8 */
    /* JADX WARNING: type inference failed for: r6v12 */
    /* JADX WARNING: type inference failed for: r6v13 */
    /* JADX WARNING: type inference failed for: r7v27 */
    /* JADX WARNING: type inference failed for: r6v14 */
    /* JADX WARNING: type inference failed for: r6v16 */
    /* JADX WARNING: type inference failed for: r0v46 */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x0340, code lost:
        if (r2 != null) goto L_0x0381;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:264:0x037f, code lost:
        if (r2 == null) goto L_0x0384;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:265:0x0381, code lost:
        r2.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:267:0x0386, code lost:
        if (r14.mListener == null) goto L_0x0392;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:268:0x0388, code lost:
        r14.mListener.onFinish(new android.taobao.windvane.connect.HttpResponse(), 0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:270:0x0397, code lost:
        return new android.taobao.windvane.connect.HttpResponse();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:273:0x0399, code lost:
        r15 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:274:0x039a, code lost:
        r6 = r6;
        r2 = r2;
        r0 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:276:0x03a3, code lost:
        throw new android.taobao.windvane.connect.HttpConnector.HttpsErrorException(r14, r15.getMessage());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:277:0x03a4, code lost:
        r15 = th;
        r6 = r6;
        r2 = r2;
        r0 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:295:?, code lost:
        r6.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:296:0x03d0, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:297:0x03d1, code lost:
        r1.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:300:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:301:0x03da, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:302:0x03db, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:305:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:306:0x03e4, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:307:0x03e5, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:313:0x03f2, code lost:
        r2.disconnect();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:212:0x0308, B:271:0x0398] */
    /* JADX WARNING: Failed to insert additional move for type inference */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:207:0x02e1 A[Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf, all -> 0x03c7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:215:0x030c A[Catch:{ Throwable -> 0x0399, all -> 0x03a4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:218:0x031c A[SYNTHETIC, Splitter:B:218:0x031c] */
    /* JADX WARNING: Removed duplicated region for block: B:223:0x0326 A[SYNTHETIC, Splitter:B:223:0x0326] */
    /* JADX WARNING: Removed duplicated region for block: B:228:0x0330 A[SYNTHETIC, Splitter:B:228:0x0330] */
    /* JADX WARNING: Removed duplicated region for block: B:242:0x034b A[Catch:{ Throwable -> 0x0399, all -> 0x03a4 }] */
    /* JADX WARNING: Removed duplicated region for block: B:245:0x0359  */
    /* JADX WARNING: Removed duplicated region for block: B:271:0x0398 A[SYNTHETIC, Splitter:B:271:0x0398] */
    /* JADX WARNING: Removed duplicated region for block: B:283:0x03ae A[Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf, all -> 0x03c7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:289:0x03bf A[Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf, all -> 0x03c7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x03cc A[SYNTHETIC, Splitter:B:294:0x03cc] */
    /* JADX WARNING: Removed duplicated region for block: B:299:0x03d6 A[SYNTHETIC, Splitter:B:299:0x03d6] */
    /* JADX WARNING: Removed duplicated region for block: B:304:0x03e0 A[SYNTHETIC, Splitter:B:304:0x03e0] */
    /* JADX WARNING: Removed duplicated region for block: B:313:0x03f2  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:204:0x02d9=Splitter:B:204:0x02d9, B:280:0x03aa=Splitter:B:280:0x03aa} */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:239:0x0347=Splitter:B:239:0x0347, B:212:0x0308=Splitter:B:212:0x0308} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.taobao.windvane.connect.HttpResponse dataConnect(android.taobao.windvane.connect.HttpRequest r15) throws android.taobao.windvane.connect.HttpConnector.NetWorkErrorException, android.taobao.windvane.connect.HttpConnector.HttpOverFlowException, android.taobao.windvane.connect.HttpConnector.RedirectException, android.taobao.windvane.connect.HttpConnector.HttpsErrorException {
        /*
            r14 = this;
            android.net.Uri r0 = r15.getUri()
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r1 = r14.mListener
            if (r1 == 0) goto L_0x000d
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r1 = r14.mListener
            r1.onStart()
        L_0x000d:
            android.taobao.windvane.connect.HttpResponse r1 = new android.taobao.windvane.connect.HttpResponse
            r1.<init>()
            java.lang.String r2 = "https"
            java.lang.String r3 = r0.getScheme()
            boolean r2 = r2.equalsIgnoreCase(r3)
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream
            r4 = 128(0x80, float:1.794E-43)
            r3.<init>(r4)
            r4 = -1
            r5 = 0
            r6 = 0
            java.net.URL r7 = new java.net.URL     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r8 = r0.toString()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            r7.<init>(r8)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r8 = r7.getHost()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            if (r2 == 0) goto L_0x0077
            java.lang.String r2 = TAG     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r9 = "proxy or https"
            android.taobao.windvane.util.TaoLog.i(r2, r9)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            android.app.Application r2 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            org.apache.http.HttpHost r2 = android.taobao.windvane.util.NetWork.getHttpsProxyInfo(r2)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            if (r2 == 0) goto L_0x0054
            android.taobao.windvane.security.SSLTunnelSocketFactory r9 = new android.taobao.windvane.security.SSLTunnelSocketFactory     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r10 = r2.getHostName()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            int r2 = r2.getPort()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r11 = "taobao_hybrid_8.5.0"
            r9.<init>(r10, r2, r6, r11)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            goto L_0x005c
        L_0x0054:
            java.lang.String r2 = TAG     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r9 = "https:proxy: none"
            android.taobao.windvane.util.TaoLog.d(r2, r9)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            r9 = r6
        L_0x005c:
            java.net.URLConnection r2 = r7.openConnection()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            if (r9 == 0) goto L_0x0067
            r2.setSSLSocketFactory(r9)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
        L_0x0067:
            org.apache.http.conn.ssl.StrictHostnameVerifier r7 = new org.apache.http.conn.ssl.StrictHostnameVerifier     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            r7.<init>()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            r2.setHostnameVerifier(r7)     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.lang.String r7 = "Connection"
            java.lang.String r9 = "Keep-Alive"
            r2.setRequestProperty(r7, r9)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            goto L_0x007d
        L_0x0077:
            java.net.URLConnection r2 = r7.openConnection()     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
            java.net.HttpURLConnection r2 = (java.net.HttpURLConnection) r2     // Catch:{ RedirectException -> 0x03b7, HttpOverFlowException -> 0x03a6, SSLHandshakeException -> 0x0343, OutOfMemoryError -> 0x0304, Throwable -> 0x02d5, all -> 0x02cf }
        L_0x007d:
            r14.setConnectProp(r2, r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r7 = r14.mListener     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            if (r7 == 0) goto L_0x0089
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r7 = r14.mListener     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r7.onProcess(r5)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
        L_0x0089:
            java.lang.String r7 = "post"
            java.lang.String r9 = r15.getMethod()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            boolean r7 = r7.equalsIgnoreCase(r9)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r9 = 1
            if (r7 == 0) goto L_0x00d5
            java.lang.String r7 = TAG     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r10.<init>()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r11 = "post data: "
            r10.append(r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r11 = new java.lang.String     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            byte[] r12 = r15.getPostData()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r11.<init>(r12)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r10.append(r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r10 = r10.toString()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            android.taobao.windvane.util.TaoLog.d(r7, r10)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r2.setDoOutput(r9)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r2.setDoInput(r9)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r7 = "POST"
            r2.setRequestMethod(r7)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r2.connect()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.io.OutputStream r7 = r2.getOutputStream()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            byte[] r10 = r15.getPostData()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r7.write(r10)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r7.flush()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r7.close()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            goto L_0x00d8
        L_0x00d5:
            r2.connect()     // Catch:{ AssertionError -> 0x02a2 }
        L_0x00d8:
            int r7 = r2.getResponseCode()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r10 = TAG     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r11.<init>()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r12 = "responeCode:"
            r11.append(r12)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r11.append(r7)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r11 = r11.toString()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            android.taobao.windvane.util.TaoLog.d(r10, r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r10 = 300(0x12c, float:4.2E-43)
            if (r7 < r10) goto L_0x0169
            r11 = 400(0x190, float:5.6E-43)
            if (r7 >= r11) goto L_0x0169
            r11 = 304(0x130, float:4.26E-43)
            if (r7 == r11) goto L_0x0169
            boolean r11 = r15.isRedirect()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            if (r11 == 0) goto L_0x0169
            int r11 = r14.redirectTime     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r12 = 5
            if (r11 > r12) goto L_0x0161
            int r11 = r14.redirectTime     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            int r11 = r11 + r9
            r14.redirectTime = r11     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r11 = "location"
            java.lang.String r11 = r2.getHeaderField(r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            if (r11 == 0) goto L_0x0169
            java.lang.String r0 = r11.toLowerCase()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r1 = "http"
            boolean r0 = r0.startsWith(r1)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            if (r0 != 0) goto L_0x012d
            java.net.URL r0 = new java.net.URL     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r1 = "http"
            r0.<init>(r1, r8, r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r11 = r0.toString()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
        L_0x012d:
            r0 = 305(0x131, float:4.27E-43)
            if (r7 == r0) goto L_0x014a
            android.net.Uri r0 = android.net.Uri.parse(r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r15.setUri(r0)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            android.taobao.windvane.connect.HttpResponse r15 = r14.dataConnect(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r3.close()     // Catch:{ Exception -> 0x0140 }
            goto L_0x0144
        L_0x0140:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0144:
            if (r2 == 0) goto L_0x0149
            r2.disconnect()
        L_0x0149:
            return r15
        L_0x014a:
            android.taobao.windvane.connect.HttpRequest r15 = new android.taobao.windvane.connect.HttpRequest     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r15.<init>(r11)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            android.taobao.windvane.connect.HttpResponse r15 = r14.dataConnect(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r3.close()     // Catch:{ Exception -> 0x0157 }
            goto L_0x015b
        L_0x0157:
            r0 = move-exception
            r0.printStackTrace()
        L_0x015b:
            if (r2 == 0) goto L_0x0160
            r2.disconnect()
        L_0x0160:
            return r15
        L_0x0161:
            android.taobao.windvane.connect.HttpConnector$RedirectException r15 = new android.taobao.windvane.connect.HttpConnector$RedirectException     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r0 = "too many redirect"
            r15.<init>(r0)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            throw r15     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
        L_0x0169:
            r1.setHttpCode(r7)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
        L_0x016c:
            java.lang.String r15 = r2.getHeaderFieldKey(r9)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            if (r15 != 0) goto L_0x0288
            r15 = 200(0xc8, float:2.8E-43)
            if (r7 < r15) goto L_0x0233
            if (r7 >= r10) goto L_0x0233
            int r15 = r2.getContentLength()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r0 = 5242880(0x500000, float:7.34684E-39)
            if (r15 > r0) goto L_0x021c
            java.io.InputStream r0 = r2.getInputStream()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r7 = r2.getContentEncoding()     // Catch:{ RedirectException -> 0x0218, HttpOverFlowException -> 0x0214, SSLHandshakeException -> 0x0210, OutOfMemoryError -> 0x020c, Throwable -> 0x0208, all -> 0x0204 }
            if (r7 == 0) goto L_0x01ae
            java.lang.String r8 = "gzip"
            boolean r7 = r8.equals(r7)     // Catch:{ RedirectException -> 0x0218, HttpOverFlowException -> 0x0214, SSLHandshakeException -> 0x0210, OutOfMemoryError -> 0x020c, Throwable -> 0x0208, all -> 0x0204 }
            if (r7 == 0) goto L_0x01ae
            java.util.zip.GZIPInputStream r7 = new java.util.zip.GZIPInputStream     // Catch:{ RedirectException -> 0x0218, HttpOverFlowException -> 0x0214, SSLHandshakeException -> 0x0210, OutOfMemoryError -> 0x020c, Throwable -> 0x0208, all -> 0x0204 }
            r7.<init>(r0)     // Catch:{ RedirectException -> 0x0218, HttpOverFlowException -> 0x0214, SSLHandshakeException -> 0x0210, OutOfMemoryError -> 0x020c, Throwable -> 0x0208, all -> 0x0204 }
            java.io.DataInputStream r8 = new java.io.DataInputStream     // Catch:{ RedirectException -> 0x01ab, HttpOverFlowException -> 0x01a8, SSLHandshakeException -> 0x01a5, OutOfMemoryError -> 0x01a2, Throwable -> 0x019f }
            r8.<init>(r7)     // Catch:{ RedirectException -> 0x01ab, HttpOverFlowException -> 0x01a8, SSLHandshakeException -> 0x01a5, OutOfMemoryError -> 0x01a2, Throwable -> 0x019f }
            r6 = r7
            r7 = r8
            goto L_0x01b3
        L_0x019f:
            r15 = move-exception
            goto L_0x0272
        L_0x01a2:
            r15 = move-exception
            goto L_0x0308
        L_0x01a5:
            r15 = move-exception
            goto L_0x0347
        L_0x01a8:
            r15 = move-exception
            goto L_0x0281
        L_0x01ab:
            r15 = move-exception
            goto L_0x0286
        L_0x01ae:
            java.io.DataInputStream r7 = new java.io.DataInputStream     // Catch:{ RedirectException -> 0x0218, HttpOverFlowException -> 0x0214, SSLHandshakeException -> 0x0210, OutOfMemoryError -> 0x020c, Throwable -> 0x0208, all -> 0x0204 }
            r7.<init>(r0)     // Catch:{ RedirectException -> 0x0218, HttpOverFlowException -> 0x0214, SSLHandshakeException -> 0x0210, OutOfMemoryError -> 0x020c, Throwable -> 0x0208, all -> 0x0204 }
        L_0x01b3:
            r8 = 2048(0x800, float:2.87E-42)
            byte[] r9 = new byte[r8]     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            r10 = r15
            r15 = 0
        L_0x01b9:
            int r11 = r7.read(r9, r5, r8)     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            if (r11 == r4) goto L_0x01d8
            r3.write(r9, r5, r11)     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r12 = r14.mListener     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            if (r12 == 0) goto L_0x01b9
            int r15 = r15 + r11
            if (r15 <= r10) goto L_0x01ca
            r10 = r15
        L_0x01ca:
            float r11 = (float) r15     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            float r12 = (float) r10     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            float r11 = r11 / r12
            r12 = 1120403456(0x42c80000, float:100.0)
            float r11 = r11 * r12
            int r11 = (int) r11     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r12 = r14.mListener     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            r12.onProcess(r11)     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            goto L_0x01b9
        L_0x01d8:
            byte[] r15 = r3.toByteArray()     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            r1.setData(r15)     // Catch:{ RedirectException -> 0x01ff, HttpOverFlowException -> 0x01fa, SSLHandshakeException -> 0x01f4, OutOfMemoryError -> 0x01ee, Throwable -> 0x01e9, all -> 0x01e3 }
            r15 = r6
            r6 = r7
            goto L_0x0235
        L_0x01e3:
            r15 = move-exception
            r13 = r7
            r7 = r6
            r6 = r13
            goto L_0x03ca
        L_0x01e9:
            r15 = move-exception
            r1 = r7
            r7 = r6
            goto L_0x02b6
        L_0x01ee:
            r15 = move-exception
            r13 = r7
            r7 = r6
            r6 = r13
            goto L_0x0308
        L_0x01f4:
            r15 = move-exception
            r13 = r7
            r7 = r6
            r6 = r13
            goto L_0x0347
        L_0x01fa:
            r15 = move-exception
            r1 = r7
            r7 = r6
            goto L_0x02c5
        L_0x01ff:
            r15 = move-exception
            r1 = r7
            r7 = r6
            goto L_0x02cc
        L_0x0204:
            r15 = move-exception
            r7 = r6
            goto L_0x03ca
        L_0x0208:
            r15 = move-exception
            r1 = r6
            goto L_0x02b5
        L_0x020c:
            r15 = move-exception
            r7 = r6
            goto L_0x0308
        L_0x0210:
            r15 = move-exception
            r7 = r6
            goto L_0x0347
        L_0x0214:
            r15 = move-exception
            r1 = r6
            goto L_0x02c4
        L_0x0218:
            r15 = move-exception
            r1 = r6
            goto L_0x02cb
        L_0x021c:
            android.taobao.windvane.connect.HttpConnector$HttpOverFlowException r0 = new android.taobao.windvane.connect.HttpConnector$HttpOverFlowException     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r1.<init>()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r7 = "The Content-Length is too large:"
            r1.append(r7)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r1.append(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r15 = r1.toString()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r0.<init>(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            throw r0     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
        L_0x0233:
            r15 = r6
            r0 = r15
        L_0x0235:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r7 = r14.mListener     // Catch:{ RedirectException -> 0x0283, HttpOverFlowException -> 0x027e, SSLHandshakeException -> 0x0279, OutOfMemoryError -> 0x0274, Throwable -> 0x026f, all -> 0x026a }
            if (r7 == 0) goto L_0x023e
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r7 = r14.mListener     // Catch:{ RedirectException -> 0x0283, HttpOverFlowException -> 0x027e, SSLHandshakeException -> 0x0279, OutOfMemoryError -> 0x0274, Throwable -> 0x026f, all -> 0x026a }
            r7.onFinish(r1, r5)     // Catch:{ RedirectException -> 0x0283, HttpOverFlowException -> 0x027e, SSLHandshakeException -> 0x0279, OutOfMemoryError -> 0x0274, Throwable -> 0x026f, all -> 0x026a }
        L_0x023e:
            if (r6 == 0) goto L_0x0248
            r6.close()     // Catch:{ Exception -> 0x0244 }
            goto L_0x0248
        L_0x0244:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0248:
            if (r0 == 0) goto L_0x0252
            r0.close()     // Catch:{ Exception -> 0x024e }
            goto L_0x0252
        L_0x024e:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0252:
            if (r15 == 0) goto L_0x025c
            r15.close()     // Catch:{ Exception -> 0x0258 }
            goto L_0x025c
        L_0x0258:
            r15 = move-exception
            r15.printStackTrace()
        L_0x025c:
            r3.close()     // Catch:{ Exception -> 0x0260 }
            goto L_0x0264
        L_0x0260:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0264:
            if (r2 == 0) goto L_0x0269
            r2.disconnect()
        L_0x0269:
            return r1
        L_0x026a:
            r1 = move-exception
            r7 = r15
            r15 = r1
            goto L_0x03ca
        L_0x026f:
            r1 = move-exception
            r7 = r15
            r15 = r1
        L_0x0272:
            r1 = r6
            goto L_0x02b6
        L_0x0274:
            r1 = move-exception
            r7 = r15
            r15 = r1
            goto L_0x0308
        L_0x0279:
            r1 = move-exception
            r7 = r15
            r15 = r1
            goto L_0x0347
        L_0x027e:
            r1 = move-exception
            r7 = r15
            r15 = r1
        L_0x0281:
            r1 = r6
            goto L_0x02c5
        L_0x0283:
            r1 = move-exception
            r7 = r15
            r15 = r1
        L_0x0286:
            r1 = r6
            goto L_0x02cc
        L_0x0288:
            int r9 = r9 + 1
            java.lang.String r8 = r2.getHeaderField(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r1.addHeader(r15, r8)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r11 = "Set-Cookie"
            boolean r15 = r11.equals(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            if (r15 == 0) goto L_0x016c
            java.lang.String r15 = r0.toString()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            android.taobao.windvane.WVCookieManager.setCookie(r15, r8)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            goto L_0x016c
        L_0x02a2:
            r15 = move-exception
            android.taobao.windvane.connect.HttpConnector$NetWorkErrorException r0 = new android.taobao.windvane.connect.HttpConnector$NetWorkErrorException     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            java.lang.String r15 = r15.getMessage()     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            r0.<init>(r15)     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
            throw r0     // Catch:{ RedirectException -> 0x02c8, HttpOverFlowException -> 0x02c1, SSLHandshakeException -> 0x02bc, OutOfMemoryError -> 0x02b8, Throwable -> 0x02b2, all -> 0x02ad }
        L_0x02ad:
            r15 = move-exception
            r0 = r6
            r7 = r0
            goto L_0x03ca
        L_0x02b2:
            r15 = move-exception
            r0 = r6
            r1 = r0
        L_0x02b5:
            r7 = r1
        L_0x02b6:
            r6 = r2
            goto L_0x02d9
        L_0x02b8:
            r15 = move-exception
            r0 = r6
            r7 = r0
            goto L_0x0308
        L_0x02bc:
            r15 = move-exception
            r0 = r6
            r7 = r0
            goto L_0x0347
        L_0x02c1:
            r15 = move-exception
            r0 = r6
            r1 = r0
        L_0x02c4:
            r7 = r1
        L_0x02c5:
            r6 = r2
            goto L_0x03aa
        L_0x02c8:
            r15 = move-exception
            r0 = r6
            r1 = r0
        L_0x02cb:
            r7 = r1
        L_0x02cc:
            r6 = r2
            goto L_0x03bb
        L_0x02cf:
            r15 = move-exception
            r0 = r6
            r2 = r0
            r7 = r2
            goto L_0x03ca
        L_0x02d5:
            r15 = move-exception
            r0 = r6
            r1 = r0
            r7 = r1
        L_0x02d9:
            java.lang.String r2 = r15.getMessage()     // Catch:{ all -> 0x03c7 }
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r4 = r14.mListener     // Catch:{ all -> 0x03c7 }
            if (r4 == 0) goto L_0x02f8
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r4 = r14.mListener     // Catch:{ all -> 0x03c7 }
            r5 = -4
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x03c7 }
            r8.<init>()     // Catch:{ all -> 0x03c7 }
            java.lang.String r9 = "network exception: "
            r8.append(r9)     // Catch:{ all -> 0x03c7 }
            r8.append(r2)     // Catch:{ all -> 0x03c7 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x03c7 }
            r4.onError(r5, r8)     // Catch:{ all -> 0x03c7 }
        L_0x02f8:
            r15.printStackTrace()     // Catch:{ all -> 0x03c7 }
            r3.reset()     // Catch:{ all -> 0x03c7 }
            android.taobao.windvane.connect.HttpConnector$NetWorkErrorException r15 = new android.taobao.windvane.connect.HttpConnector$NetWorkErrorException     // Catch:{ all -> 0x03c7 }
            r15.<init>(r2)     // Catch:{ all -> 0x03c7 }
            throw r15     // Catch:{ all -> 0x03c7 }
        L_0x0304:
            r15 = move-exception
            r0 = r6
            r2 = r0
            r7 = r2
        L_0x0308:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x03a4 }
            if (r1 == 0) goto L_0x0314
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x03a4 }
            r4 = -5
            java.lang.String r8 = "out of memory error"
            r1.onError(r4, r8)     // Catch:{ all -> 0x03a4 }
        L_0x0314:
            r15.printStackTrace()     // Catch:{ all -> 0x03a4 }
            r3.reset()     // Catch:{ all -> 0x03a4 }
            if (r6 == 0) goto L_0x0324
            r6.close()     // Catch:{ Exception -> 0x0320 }
            goto L_0x0324
        L_0x0320:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0324:
            if (r0 == 0) goto L_0x032e
            r0.close()     // Catch:{ Exception -> 0x032a }
            goto L_0x032e
        L_0x032a:
            r15 = move-exception
            r15.printStackTrace()
        L_0x032e:
            if (r7 == 0) goto L_0x0338
            r7.close()     // Catch:{ Exception -> 0x0334 }
            goto L_0x0338
        L_0x0334:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0338:
            r3.close()     // Catch:{ Exception -> 0x033c }
            goto L_0x0340
        L_0x033c:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0340:
            if (r2 == 0) goto L_0x0384
            goto L_0x0381
        L_0x0343:
            r15 = move-exception
            r0 = r6
            r2 = r0
            r7 = r2
        L_0x0347:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x03a4 }
            if (r1 == 0) goto L_0x0353
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r1 = r14.mListener     // Catch:{ all -> 0x03a4 }
            r4 = -3
            java.lang.String r8 = "ssl handshake exception"
            r1.onError(r4, r8)     // Catch:{ all -> 0x03a4 }
        L_0x0353:
            java.lang.Throwable r15 = r15.getCause()     // Catch:{ all -> 0x03a4 }
            if (r15 != 0) goto L_0x0398
            if (r6 == 0) goto L_0x0363
            r6.close()     // Catch:{ Exception -> 0x035f }
            goto L_0x0363
        L_0x035f:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0363:
            if (r0 == 0) goto L_0x036d
            r0.close()     // Catch:{ Exception -> 0x0369 }
            goto L_0x036d
        L_0x0369:
            r15 = move-exception
            r15.printStackTrace()
        L_0x036d:
            if (r7 == 0) goto L_0x0377
            r7.close()     // Catch:{ Exception -> 0x0373 }
            goto L_0x0377
        L_0x0373:
            r15 = move-exception
            r15.printStackTrace()
        L_0x0377:
            r3.close()     // Catch:{ Exception -> 0x037b }
            goto L_0x037f
        L_0x037b:
            r15 = move-exception
            r15.printStackTrace()
        L_0x037f:
            if (r2 == 0) goto L_0x0384
        L_0x0381:
            r2.disconnect()
        L_0x0384:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r15 = r14.mListener
            if (r15 == 0) goto L_0x0392
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r15 = r14.mListener
            android.taobao.windvane.connect.HttpResponse r0 = new android.taobao.windvane.connect.HttpResponse
            r0.<init>()
            r15.onFinish(r0, r5)
        L_0x0392:
            android.taobao.windvane.connect.HttpResponse r15 = new android.taobao.windvane.connect.HttpResponse
            r15.<init>()
            return r15
        L_0x0398:
            throw r15     // Catch:{ Throwable -> 0x0399 }
        L_0x0399:
            r15 = move-exception
            android.taobao.windvane.connect.HttpConnector$HttpsErrorException r1 = new android.taobao.windvane.connect.HttpConnector$HttpsErrorException     // Catch:{ all -> 0x03a4 }
            java.lang.String r15 = r15.getMessage()     // Catch:{ all -> 0x03a4 }
            r1.<init>(r15)     // Catch:{ all -> 0x03a4 }
            throw r1     // Catch:{ all -> 0x03a4 }
        L_0x03a4:
            r15 = move-exception
            goto L_0x03ca
        L_0x03a6:
            r15 = move-exception
            r0 = r6
            r1 = r0
            r7 = r1
        L_0x03aa:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r2 = r14.mListener     // Catch:{ all -> 0x03c7 }
            if (r2 == 0) goto L_0x03b6
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r2 = r14.mListener     // Catch:{ all -> 0x03c7 }
            r4 = -2
            java.lang.String r5 = "connect file is too large"
            r2.onError(r4, r5)     // Catch:{ all -> 0x03c7 }
        L_0x03b6:
            throw r15     // Catch:{ all -> 0x03c7 }
        L_0x03b7:
            r15 = move-exception
            r0 = r6
            r1 = r0
            r7 = r1
        L_0x03bb:
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r2 = r14.mListener     // Catch:{ all -> 0x03c7 }
            if (r2 == 0) goto L_0x03c6
            android.taobao.windvane.connect.HttpConnectListener<android.taobao.windvane.connect.HttpResponse> r2 = r14.mListener     // Catch:{ all -> 0x03c7 }
            java.lang.String r5 = "too many redirect"
            r2.onError(r4, r5)     // Catch:{ all -> 0x03c7 }
        L_0x03c6:
            throw r15     // Catch:{ all -> 0x03c7 }
        L_0x03c7:
            r15 = move-exception
            r2 = r6
            r6 = r1
        L_0x03ca:
            if (r6 == 0) goto L_0x03d4
            r6.close()     // Catch:{ Exception -> 0x03d0 }
            goto L_0x03d4
        L_0x03d0:
            r1 = move-exception
            r1.printStackTrace()
        L_0x03d4:
            if (r0 == 0) goto L_0x03de
            r0.close()     // Catch:{ Exception -> 0x03da }
            goto L_0x03de
        L_0x03da:
            r0 = move-exception
            r0.printStackTrace()
        L_0x03de:
            if (r7 == 0) goto L_0x03e8
            r7.close()     // Catch:{ Exception -> 0x03e4 }
            goto L_0x03e8
        L_0x03e4:
            r0 = move-exception
            r0.printStackTrace()
        L_0x03e8:
            r3.close()     // Catch:{ Exception -> 0x03ec }
            goto L_0x03f0
        L_0x03ec:
            r0 = move-exception
            r0.printStackTrace()
        L_0x03f0:
            if (r2 == 0) goto L_0x03f5
            r2.disconnect()
        L_0x03f5:
            throw r15
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.connect.HttpConnector.dataConnect(android.taobao.windvane.connect.HttpRequest):android.taobao.windvane.connect.HttpResponse");
    }

    private void setConnectProp(HttpURLConnection httpURLConnection, HttpRequest httpRequest) {
        int retryTime = httpRequest.getRetryTime() + 1;
        httpURLConnection.setConnectTimeout(httpRequest.getConnectTimeout() * retryTime);
        httpURLConnection.setReadTimeout(httpRequest.getReadTimeout() * retryTime);
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestProperty(HttpConstant.HOST, httpRequest.getUri().getHost());
        httpURLConnection.setRequestProperty("Connection", "close");
        httpURLConnection.setRequestProperty(HttpConstant.ACCEPT_ENCODING, "gzip");
        String cookie = WVCookieManager.getCookie(httpURLConnection.getURL().toString());
        if (cookie != null) {
            httpURLConnection.setRequestProperty(HttpConstant.COOKIE, cookie);
        }
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
