package com.huawei.hianalytics.h;

import android.os.Build;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.huawei.hianalytics.a.b;
import com.taobao.login4android.video.AudioFileFunc;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public abstract class c {

    private static class a extends Exception {
        a(String str) {
            super(str);
        }
    }

    public static d a(String str, String str2) {
        return a(str, str2, "POST", (Map<String, String>) null);
    }

    public static d a(String str, String str2, String str3) {
        HashMap hashMap = new HashMap();
        hashMap.put("App-Id", b.f());
        hashMap.put("App-Ver", b.g());
        hashMap.put("Sdk-Name", "hianalytics");
        hashMap.put("Sdk-Ver", "2.1.4.301");
        hashMap.put("Device-Type", Build.MODEL);
        hashMap.put("Package-Name", b.e());
        hashMap.put(HttpConstant.AUTHORIZATION, str3);
        hashMap.put("Charset", "UTF-8");
        hashMap.put("Content-Encoding", "gzip");
        hashMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        return a(str, str2, "POST", (Map<String, String>) hashMap);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0097, code lost:
        r0 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0099, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x009a, code lost:
        r2 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
        r6.getInputStream().close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00fb, code lost:
        com.huawei.hianalytics.g.b.c("HttpClient", "event PostRequest(String): connHttp.getInputStream() close exception !");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0099 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:26:0x0062] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00d0 A[SYNTHETIC, Splitter:B:60:0x00d0] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00f3 A[SYNTHETIC, Splitter:B:69:0x00f3] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.huawei.hianalytics.h.d a(java.lang.String r6, java.lang.String r7, java.lang.String r8, java.util.Map<java.lang.String, java.lang.String> r9) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            if (r0 == 0) goto L_0x0010
            com.huawei.hianalytics.h.d r6 = new com.huawei.hianalytics.h.d
            r7 = -100
            java.lang.String r8 = ""
            r6.<init>(r7, r8)
            return r6
        L_0x0010:
            r0 = -102(0xffffffffffffff9a, float:NaN)
            r1 = -101(0xffffffffffffff9b, float:NaN)
            r2 = 0
            r3 = 6
            r4 = 9
            int r5 = r7.length()     // Catch:{ a -> 0x00a5 }
            java.net.HttpURLConnection r6 = a((java.lang.String) r6, (int) r5, (java.lang.String) r8, (java.util.Map<java.lang.String, java.lang.String>) r9)     // Catch:{ a -> 0x00a5 }
            if (r6 != 0) goto L_0x0052
            com.huawei.hianalytics.h.d r7 = new com.huawei.hianalytics.h.d     // Catch:{ IOException -> 0x004f, all -> 0x004b }
            java.lang.String r8 = ""
            r7.<init>(r1, r8)     // Catch:{ IOException -> 0x004f, all -> 0x004b }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r2)
            if (r6 == 0) goto L_0x004a
            java.io.InputStream r8 = r6.getInputStream()     // Catch:{ IOException -> 0x0039 }
            r8.close()     // Catch:{ IOException -> 0x0039 }
            goto L_0x0040
        L_0x0039:
            java.lang.String r8 = "HttpClient"
            java.lang.String r9 = "event PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r8, r9)
        L_0x0040:
            r6.disconnect()
            java.lang.String r6 = "HttpClient"
            java.lang.String r8 = "disconnect"
            com.huawei.hianalytics.g.b.a(r6, r8)
        L_0x004a:
            return r7
        L_0x004b:
            r7 = move-exception
            r8 = r2
            goto L_0x00eb
        L_0x004f:
            r8 = r2
            goto L_0x00ba
        L_0x0052:
            java.io.OutputStream r8 = r6.getOutputStream()     // Catch:{ IOException -> 0x004f, all -> 0x004b }
            java.io.BufferedWriter r9 = new java.io.BufferedWriter     // Catch:{ IOException -> 0x00ba }
            java.io.OutputStreamWriter r1 = new java.io.OutputStreamWriter     // Catch:{ IOException -> 0x00ba }
            java.lang.String r5 = "UTF-8"
            r1.<init>(r8, r5)     // Catch:{ IOException -> 0x00ba }
            r9.<init>(r1)     // Catch:{ IOException -> 0x00ba }
            r9.append(r7)     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            r9.flush()     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            int r7 = r6.getResponseCode()     // Catch:{ IOException -> 0x009c, all -> 0x0099 }
            java.lang.String r0 = b(r6)     // Catch:{ IOException -> 0x0097, all -> 0x0099 }
            com.huawei.hianalytics.h.d r1 = new com.huawei.hianalytics.h.d     // Catch:{ IOException -> 0x0097, all -> 0x0099 }
            r1.<init>(r7, r0)     // Catch:{ IOException -> 0x0097, all -> 0x0099 }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r9)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r8)
            if (r6 == 0) goto L_0x0096
            java.io.InputStream r7 = r6.getInputStream()     // Catch:{ IOException -> 0x0085 }
            r7.close()     // Catch:{ IOException -> 0x0085 }
            goto L_0x008c
        L_0x0085:
            java.lang.String r7 = "HttpClient"
            java.lang.String r8 = "event PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r7, r8)
        L_0x008c:
            r6.disconnect()
            java.lang.String r6 = "HttpClient"
            java.lang.String r7 = "disconnect"
            com.huawei.hianalytics.g.b.a(r6, r7)
        L_0x0096:
            return r1
        L_0x0097:
            r0 = r7
            goto L_0x009c
        L_0x0099:
            r7 = move-exception
            r2 = r9
            goto L_0x00eb
        L_0x009c:
            r2 = r9
            goto L_0x00ba
        L_0x009e:
            r7 = move-exception
            r6 = r2
            r8 = r6
            goto L_0x00eb
        L_0x00a2:
            r6 = r2
            r8 = r6
            goto L_0x00ba
        L_0x00a5:
            java.lang.String r6 = "HttpClient"
            java.lang.String r7 = "sendPostRequest(String): No ssl socket factory set!"
            com.huawei.hianalytics.g.b.c(r6, r7)     // Catch:{ IOException -> 0x00a2, all -> 0x009e }
            com.huawei.hianalytics.h.d r6 = new com.huawei.hianalytics.h.d     // Catch:{ IOException -> 0x00a2, all -> 0x009e }
            java.lang.String r7 = ""
            r6.<init>(r1, r7)     // Catch:{ IOException -> 0x00a2, all -> 0x009e }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r2)
            return r6
        L_0x00ba:
            java.lang.String r7 = "HttpClient"
            java.lang.String r9 = "log file PostRequest(String): IO operation exception !"
            com.huawei.hianalytics.g.b.c(r7, r9)     // Catch:{ all -> 0x00ea }
            com.huawei.hianalytics.h.d r7 = new com.huawei.hianalytics.h.d     // Catch:{ all -> 0x00ea }
            java.lang.String r9 = ""
            r7.<init>(r0, r9)     // Catch:{ all -> 0x00ea }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r8)
            if (r6 == 0) goto L_0x00e9
            java.io.InputStream r8 = r6.getInputStream()     // Catch:{ IOException -> 0x00d8 }
            r8.close()     // Catch:{ IOException -> 0x00d8 }
            goto L_0x00df
        L_0x00d8:
            java.lang.String r8 = "HttpClient"
            java.lang.String r9 = "event PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r8, r9)
        L_0x00df:
            r6.disconnect()
            java.lang.String r6 = "HttpClient"
            java.lang.String r8 = "disconnect"
            com.huawei.hianalytics.g.b.a(r6, r8)
        L_0x00e9:
            return r7
        L_0x00ea:
            r7 = move-exception
        L_0x00eb:
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r8)
            if (r6 == 0) goto L_0x010c
            java.io.InputStream r8 = r6.getInputStream()     // Catch:{ IOException -> 0x00fb }
            r8.close()     // Catch:{ IOException -> 0x00fb }
            goto L_0x0102
        L_0x00fb:
            java.lang.String r8 = "HttpClient"
            java.lang.String r9 = "event PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r8, r9)
        L_0x0102:
            r6.disconnect()
            java.lang.String r6 = "HttpClient"
            java.lang.String r8 = "disconnect"
            com.huawei.hianalytics.g.b.a(r6, r8)
        L_0x010c:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.h.c.a(java.lang.String, java.lang.String, java.lang.String, java.util.Map):com.huawei.hianalytics.h.d");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0099, code lost:
        r8 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x009a, code lost:
        r2 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x009f, code lost:
        r8 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00a0, code lost:
        r2 = r10;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x0165 A[SYNTHETIC, Splitter:B:108:0x0165] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0099 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:28:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00db A[SYNTHETIC, Splitter:B:69:0x00db] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00f9 A[Catch:{ all -> 0x015c }] */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0101 A[Catch:{ all -> 0x015c }] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0142 A[SYNTHETIC, Splitter:B:99:0x0142] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.huawei.hianalytics.h.d a(java.lang.String r7, byte[] r8, java.lang.String r9, java.util.Map<java.lang.String, java.lang.String> r10) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r7)
            if (r0 == 0) goto L_0x0010
            com.huawei.hianalytics.h.d r7 = new com.huawei.hianalytics.h.d
            r8 = -100
            java.lang.String r9 = ""
            r7.<init>(r8, r9)
            return r7
        L_0x0010:
            r0 = -102(0xffffffffffffff9a, float:NaN)
            r1 = -101(0xffffffffffffff9b, float:NaN)
            r2 = 0
            r3 = 6
            r4 = 3
            int r5 = r8.length     // Catch:{ a -> 0x00b0 }
            java.net.HttpURLConnection r7 = a((java.lang.String) r7, (int) r5, (java.lang.String) r9, (java.util.Map<java.lang.String, java.lang.String>) r10)     // Catch:{ a -> 0x00b0 }
            if (r7 != 0) goto L_0x0052
            com.huawei.hianalytics.h.d r8 = new com.huawei.hianalytics.h.d     // Catch:{ IOException -> 0x004e, SecurityException -> 0x004b, all -> 0x0047 }
            java.lang.String r9 = ""
            r8.<init>(r1, r9)     // Catch:{ IOException -> 0x004e, SecurityException -> 0x004b, all -> 0x0047 }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r2)
            if (r7 == 0) goto L_0x0046
            java.io.InputStream r9 = r7.getInputStream()     // Catch:{ Exception -> 0x0035 }
            r9.close()     // Catch:{ Exception -> 0x0035 }
            goto L_0x003c
        L_0x0035:
            java.lang.String r9 = "HttpClient"
            java.lang.String r10 = "PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r9, r10)
        L_0x003c:
            r7.disconnect()
            java.lang.String r7 = "HttpClient"
            java.lang.String r9 = " connHttp disconnect"
            com.huawei.hianalytics.g.b.a(r7, r9)
        L_0x0046:
            return r8
        L_0x0047:
            r8 = move-exception
            r9 = r2
            goto L_0x015d
        L_0x004b:
            r9 = r2
            goto L_0x00c5
        L_0x004e:
            r8 = move-exception
            r9 = r2
            goto L_0x00f5
        L_0x0052:
            java.io.OutputStream r9 = r7.getOutputStream()     // Catch:{ IOException -> 0x004e, SecurityException -> 0x004b, all -> 0x0047 }
            java.io.BufferedOutputStream r10 = new java.io.BufferedOutputStream     // Catch:{ IOException -> 0x00a2, SecurityException -> 0x00c5 }
            r10.<init>(r9)     // Catch:{ IOException -> 0x00a2, SecurityException -> 0x00c5 }
            r10.write(r8)     // Catch:{ IOException -> 0x009f, SecurityException -> 0x009d, all -> 0x0099 }
            r10.flush()     // Catch:{ IOException -> 0x009f, SecurityException -> 0x009d, all -> 0x0099 }
            int r8 = r7.getResponseCode()     // Catch:{ IOException -> 0x009f, SecurityException -> 0x009d, all -> 0x0099 }
            java.lang.String r0 = b(r7)     // Catch:{ IOException -> 0x0092, SecurityException -> 0x0090, all -> 0x0099 }
            com.huawei.hianalytics.h.d r1 = new com.huawei.hianalytics.h.d     // Catch:{ IOException -> 0x0092, SecurityException -> 0x0090, all -> 0x0099 }
            r1.<init>(r8, r0)     // Catch:{ IOException -> 0x0092, SecurityException -> 0x0090, all -> 0x0099 }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r10)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r9)
            if (r7 == 0) goto L_0x008f
            java.io.InputStream r8 = r7.getInputStream()     // Catch:{ Exception -> 0x007e }
            r8.close()     // Catch:{ Exception -> 0x007e }
            goto L_0x0085
        L_0x007e:
            java.lang.String r8 = "HttpClient"
            java.lang.String r9 = "PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r8, r9)
        L_0x0085:
            r7.disconnect()
            java.lang.String r7 = "HttpClient"
            java.lang.String r8 = " connHttp disconnect"
            com.huawei.hianalytics.g.b.a(r7, r8)
        L_0x008f:
            return r1
        L_0x0090:
            r0 = r8
            goto L_0x009d
        L_0x0092:
            r0 = move-exception
            r2 = r10
            r6 = r0
            r0 = r8
            r8 = r6
            goto L_0x00f5
        L_0x0099:
            r8 = move-exception
            r2 = r10
            goto L_0x015d
        L_0x009d:
            r2 = r10
            goto L_0x00c5
        L_0x009f:
            r8 = move-exception
            r2 = r10
            goto L_0x00f5
        L_0x00a2:
            r8 = move-exception
            goto L_0x00f5
        L_0x00a4:
            r8 = move-exception
            r7 = r2
            r9 = r7
            goto L_0x015d
        L_0x00a9:
            r7 = r2
            r9 = r7
            goto L_0x00c5
        L_0x00ac:
            r8 = move-exception
            r7 = r2
            r9 = r7
            goto L_0x00f5
        L_0x00b0:
            java.lang.String r7 = "HttpClient"
            java.lang.String r8 = "PostRequest(byte[]): No ssl socket factory set!"
            com.huawei.hianalytics.g.b.c(r7, r8)     // Catch:{ IOException -> 0x00ac, SecurityException -> 0x00a9, all -> 0x00a4 }
            com.huawei.hianalytics.h.d r7 = new com.huawei.hianalytics.h.d     // Catch:{ IOException -> 0x00ac, SecurityException -> 0x00a9, all -> 0x00a4 }
            java.lang.String r8 = ""
            r7.<init>(r1, r8)     // Catch:{ IOException -> 0x00ac, SecurityException -> 0x00a9, all -> 0x00a4 }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r2)
            return r7
        L_0x00c5:
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "SecurityException with HttpClient. Please check INTERNET permission."
            com.huawei.hianalytics.g.b.c(r8, r10)     // Catch:{ all -> 0x015c }
            com.huawei.hianalytics.h.d r8 = new com.huawei.hianalytics.h.d     // Catch:{ all -> 0x015c }
            java.lang.String r10 = ""
            r8.<init>(r0, r10)     // Catch:{ all -> 0x015c }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r9)
            if (r7 == 0) goto L_0x00f4
            java.io.InputStream r9 = r7.getInputStream()     // Catch:{ Exception -> 0x00e3 }
            r9.close()     // Catch:{ Exception -> 0x00e3 }
            goto L_0x00ea
        L_0x00e3:
            java.lang.String r9 = "HttpClient"
            java.lang.String r10 = "PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r9, r10)
        L_0x00ea:
            r7.disconnect()
            java.lang.String r7 = "HttpClient"
            java.lang.String r9 = " connHttp disconnect"
            com.huawei.hianalytics.g.b.a(r7, r9)
        L_0x00f4:
            return r8
        L_0x00f5:
            boolean r10 = r8 instanceof javax.net.ssl.SSLPeerUnverifiedException     // Catch:{ all -> 0x015c }
            if (r10 == 0) goto L_0x0101
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "Certificate has not been verified,Request is restricted!"
        L_0x00fd:
            com.huawei.hianalytics.g.b.c(r8, r10)     // Catch:{ all -> 0x015c }
            goto L_0x0133
        L_0x0101:
            boolean r10 = r8 instanceof javax.net.ssl.SSLHandshakeException     // Catch:{ all -> 0x015c }
            if (r10 == 0) goto L_0x010a
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "Chain validation failed,Certificate expired"
            goto L_0x00fd
        L_0x010a:
            boolean r10 = r8 instanceof java.net.ConnectException     // Catch:{ all -> 0x015c }
            if (r10 == 0) goto L_0x0113
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "Network is unreachable or Connection refused"
            goto L_0x00fd
        L_0x0113:
            boolean r10 = r8 instanceof java.net.UnknownHostException     // Catch:{ all -> 0x015c }
            if (r10 == 0) goto L_0x011c
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "No address associated with hostname"
            goto L_0x00fd
        L_0x011c:
            boolean r10 = r8 instanceof java.net.SocketTimeoutException     // Catch:{ all -> 0x015c }
            if (r10 == 0) goto L_0x0125
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "failed to connect to this address,pls check url"
            goto L_0x00fd
        L_0x0125:
            boolean r8 = r8 instanceof java.net.MalformedURLException     // Catch:{ all -> 0x015c }
            if (r8 == 0) goto L_0x012e
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "Unknown protocol,pls check url"
            goto L_0x00fd
        L_0x012e:
            java.lang.String r8 = "HttpClient"
            java.lang.String r10 = "events PostRequest(byte[]): Exception or something"
            goto L_0x00fd
        L_0x0133:
            com.huawei.hianalytics.h.d r8 = new com.huawei.hianalytics.h.d     // Catch:{ all -> 0x015c }
            java.lang.String r10 = ""
            r8.<init>(r0, r10)     // Catch:{ all -> 0x015c }
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r9)
            if (r7 == 0) goto L_0x015b
            java.io.InputStream r9 = r7.getInputStream()     // Catch:{ Exception -> 0x014a }
            r9.close()     // Catch:{ Exception -> 0x014a }
            goto L_0x0151
        L_0x014a:
            java.lang.String r9 = "HttpClient"
            java.lang.String r10 = "PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r9, r10)
        L_0x0151:
            r7.disconnect()
            java.lang.String r7 = "HttpClient"
            java.lang.String r9 = " connHttp disconnect"
            com.huawei.hianalytics.g.b.a(r7, r9)
        L_0x015b:
            return r8
        L_0x015c:
            r8 = move-exception
        L_0x015d:
            com.huawei.hianalytics.util.e.a((int) r4, (java.io.Closeable) r2)
            com.huawei.hianalytics.util.e.a((int) r3, (java.io.Closeable) r9)
            if (r7 == 0) goto L_0x017e
            java.io.InputStream r9 = r7.getInputStream()     // Catch:{ Exception -> 0x016d }
            r9.close()     // Catch:{ Exception -> 0x016d }
            goto L_0x0174
        L_0x016d:
            java.lang.String r9 = "HttpClient"
            java.lang.String r10 = "PostRequest(String): connHttp.getInputStream() close exception !"
            com.huawei.hianalytics.g.b.c(r9, r10)
        L_0x0174:
            r7.disconnect()
            java.lang.String r7 = "HttpClient"
            java.lang.String r9 = " connHttp disconnect"
            com.huawei.hianalytics.g.b.a(r7, r9)
        L_0x017e:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.h.c.a(java.lang.String, byte[], java.lang.String, java.util.Map):com.huawei.hianalytics.h.d");
    }

    public static d a(String str, byte[] bArr, Map<String, String> map) {
        return a(str, bArr, "POST", map);
    }

    private static HttpURLConnection a(String str, int i, String str2, Map<String, String> map) {
        if (TextUtils.isEmpty(str)) {
            com.huawei.hianalytics.g.b.d("HttpClient", "CreateConnection: invalid urlPath.");
            return null;
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        a(httpURLConnection);
        httpURLConnection.setRequestMethod(str2);
        httpURLConnection.setConnectTimeout(AudioFileFunc.AUDIO_SAMPLE_RATE);
        httpURLConnection.setReadTimeout(AudioFileFunc.AUDIO_SAMPLE_RATE);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(i));
        httpURLConnection.setRequestProperty("App-Ver", "2.1.4.301");
        httpURLConnection.setRequestProperty("Connection", "close");
        if (map != null && map.size() >= 1) {
            for (Map.Entry next : map.entrySet()) {
                String str3 = (String) next.getKey();
                if (str3 != null && !TextUtils.isEmpty(str3)) {
                    httpURLConnection.setRequestProperty(str3, (String) next.getValue());
                }
            }
        }
        return httpURLConnection;
    }

    private static void a(HttpURLConnection httpURLConnection) {
        if (httpURLConnection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            SocketFactory a2 = b.a();
            if (a2 == null || !(a2 instanceof SSLSocketFactory)) {
                throw new a("No ssl socket factory set");
            }
            httpsURLConnection.setSSLSocketFactory((SSLSocketFactory) a2);
            httpsURLConnection.setHostnameVerifier(b.a);
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0017 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String b(java.net.HttpURLConnection r6) {
        /*
            r0 = 7
            r1 = 0
            java.io.InputStream r2 = r6.getInputStream()     // Catch:{ IOException -> 0x0017 }
            r1 = 2048(0x800, float:2.87E-42)
            java.lang.String r1 = com.huawei.hianalytics.util.e.a((java.io.InputStream) r2, (int) r1)     // Catch:{ IOException -> 0x0013, all -> 0x0010 }
            com.huawei.hianalytics.util.e.a((int) r0, (java.io.Closeable) r2)
            return r1
        L_0x0010:
            r6 = move-exception
            r1 = r2
            goto L_0x0032
        L_0x0013:
            r1 = r2
            goto L_0x0017
        L_0x0015:
            r6 = move-exception
            goto L_0x0032
        L_0x0017:
            int r6 = r6.getResponseCode()     // Catch:{ all -> 0x0015 }
            java.lang.String r2 = "HttpClient"
            java.lang.String r3 = "When Response Content From Connection inputStream operation exception!"
            r4 = 1
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0015 }
            r5 = 0
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ all -> 0x0015 }
            r4[r5] = r6     // Catch:{ all -> 0x0015 }
            com.huawei.hianalytics.g.b.c(r2, r3, r4)     // Catch:{ all -> 0x0015 }
            java.lang.String r6 = ""
            com.huawei.hianalytics.util.e.a((int) r0, (java.io.Closeable) r1)
            return r6
        L_0x0032:
            com.huawei.hianalytics.util.e.a((int) r0, (java.io.Closeable) r1)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.h.c.b(java.net.HttpURLConnection):java.lang.String");
    }
}
