package com.alipay.sdk.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;

public final class a {
    private static final String a = "msp";
    private static final String b = "application/octet-stream;binary/octet-stream";
    private static final CookieManager c = new CookieManager();

    /* renamed from: com.alipay.sdk.net.a$a  reason: collision with other inner class name */
    public static final class C0002a {
        public final String a;
        public final byte[] b;
        public final Map<String, String> c;

        public C0002a(String str, Map<String, String> map, byte[] bArr) {
            this.a = str;
            this.b = bArr;
            this.c = map;
        }

        public String toString() {
            return String.format("<UrlConnectionConfigure url=%s requestBody=%s headers=%s>", new Object[]{this.a, this.b, this.c});
        }
    }

    public static final class b {
        public final Map<String, List<String>> a;
        public final String b;
        public final byte[] c;

        public b(Map<String, List<String>> map, String str, byte[] bArr) {
            this.a = map;
            this.b = str;
            this.c = bArr;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v27, resolved type: java.io.BufferedOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v29, resolved type: java.io.BufferedOutputStream} */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.io.OutputStream] */
    /* JADX WARNING: type inference failed for: r2v2 */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:64:0x0191 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01d4 A[SYNTHETIC, Splitter:B:106:0x01d4] */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01db A[SYNTHETIC, Splitter:B:110:0x01db] */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x01e2 A[SYNTHETIC, Splitter:B:114:0x01e2] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0196 A[SYNTHETIC, Splitter:B:68:0x0196] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01be A[SYNTHETIC, Splitter:B:91:0x01be] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x01c5 A[SYNTHETIC, Splitter:B:95:0x01c5] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x01cc A[SYNTHETIC, Splitter:B:99:0x01cc] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.alipay.sdk.net.a.b a(android.content.Context r12, com.alipay.sdk.net.a.C0002a r13) {
        /*
            r0 = 0
            if (r12 != 0) goto L_0x0004
            return r0
        L_0x0004:
            java.lang.String r1 = "msp"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            r2.<init>()     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.lang.String r3 = "config : "
            r2.append(r3)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            r2.append(r13)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            com.alipay.sdk.util.c.c(r1, r2)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.net.URL r1 = new java.net.URL     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.lang.String r2 = r13.a     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.net.Proxy r12 = a((android.content.Context) r12)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.lang.String r2 = "msp"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            r3.<init>()     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.lang.String r4 = "proxy: "
            r3.append(r4)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            r3.append(r12)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            com.alipay.sdk.util.c.c(r2, r3)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            if (r12 == 0) goto L_0x0044
            java.net.URLConnection r12 = r1.openConnection(r12)     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.net.HttpURLConnection r12 = (java.net.HttpURLConnection) r12     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            goto L_0x004a
        L_0x0044:
            java.net.URLConnection r12 = r1.openConnection()     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
            java.net.HttpURLConnection r12 = (java.net.HttpURLConnection) r12     // Catch:{ Throwable -> 0x01b4, all -> 0x01af }
        L_0x004a:
            java.lang.String r2 = "http.keepAlive"
            java.lang.String r3 = "false"
            java.lang.System.setProperty(r2, r3)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            boolean r2 = r12 instanceof javax.net.ssl.HttpsURLConnection     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r2 == 0) goto L_0x0058
            r2 = r12
            javax.net.ssl.HttpsURLConnection r2 = (javax.net.ssl.HttpsURLConnection) r2     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
        L_0x0058:
            java.net.CookieManager r2 = c     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.net.CookieStore r2 = r2.getCookieStore()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.util.List r2 = r2.getCookies()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            int r2 = r2.size()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r2 <= 0) goto L_0x007d
            java.lang.String r2 = "Cookie"
            java.lang.String r3 = ";"
            java.net.CookieManager r4 = c     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.net.CookieStore r4 = r4.getCookieStore()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.util.List r4 = r4.getCookies()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = android.text.TextUtils.join(r3, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            r12.setRequestProperty(r2, r3)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
        L_0x007d:
            r2 = 20000(0x4e20, float:2.8026E-41)
            r12.setConnectTimeout(r2)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            r2 = 30000(0x7530, float:4.2039E-41)
            r12.setReadTimeout(r2)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            r2 = 1
            r12.setInstanceFollowRedirects(r2)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = "User-Agent"
            java.lang.String r4 = "msp"
            r12.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            byte[] r3 = r13.b     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r3 == 0) goto L_0x00bd
            byte[] r3 = r13.b     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            int r3 = r3.length     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r3 <= 0) goto L_0x00bd
            java.lang.String r3 = "POST"
            r12.setRequestMethod(r3)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = "Content-Type"
            java.lang.String r4 = "application/octet-stream;binary/octet-stream"
            r12.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = "Accept-Charset"
            java.lang.String r4 = "UTF-8"
            r12.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = "Connection"
            java.lang.String r4 = "Keep-Alive"
            r12.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = "Keep-Alive"
            java.lang.String r4 = "timeout=180, max=100"
            r12.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            goto L_0x00c2
        L_0x00bd:
            java.lang.String r3 = "GET"
            r12.setRequestMethod(r3)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
        L_0x00c2:
            java.util.Map<java.lang.String, java.lang.String> r3 = r13.c     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r3 == 0) goto L_0x00f3
            java.util.Map<java.lang.String, java.lang.String> r3 = r13.c     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.util.Set r3 = r3.entrySet()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
        L_0x00d0:
            boolean r4 = r3.hasNext()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r4 == 0) goto L_0x00f3
            java.lang.Object r4 = r3.next()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.Object r5 = r4.getKey()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r5 != 0) goto L_0x00e3
            goto L_0x00d0
        L_0x00e3:
            java.lang.Object r5 = r4.getKey()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.Object r4 = r4.getValue()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            r12.setRequestProperty(r5, r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            goto L_0x00d0
        L_0x00f3:
            r12.setDoInput(r2)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.lang.String r3 = "POST"
            java.lang.String r4 = r12.getRequestMethod()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            boolean r3 = r3.equals(r4)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r3 == 0) goto L_0x0105
            r12.setDoOutput(r2)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
        L_0x0105:
            java.lang.String r2 = "POST"
            java.lang.String r3 = r12.getRequestMethod()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            boolean r2 = r2.equals(r3)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            if (r2 == 0) goto L_0x0123
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            java.io.OutputStream r3 = r12.getOutputStream()     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x01ac, all -> 0x01a9 }
            byte[] r13 = r13.b     // Catch:{ Throwable -> 0x01a6, all -> 0x01a4 }
            r2.write(r13)     // Catch:{ Throwable -> 0x01a6, all -> 0x01a4 }
            r2.flush()     // Catch:{ Throwable -> 0x01a6, all -> 0x01a4 }
            goto L_0x0124
        L_0x0123:
            r2 = r0
        L_0x0124:
            java.io.BufferedInputStream r13 = new java.io.BufferedInputStream     // Catch:{ Throwable -> 0x01a6, all -> 0x01a4 }
            java.io.InputStream r3 = r12.getInputStream()     // Catch:{ Throwable -> 0x01a6, all -> 0x01a4 }
            r13.<init>(r3)     // Catch:{ Throwable -> 0x01a6, all -> 0x01a4 }
            byte[] r3 = a((java.io.InputStream) r13)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.util.Map r4 = r12.getHeaderFields()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r4 == 0) goto L_0x014a
            java.lang.Object r5 = r4.get(r0)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r5 == 0) goto L_0x014a
            java.lang.String r5 = ","
            java.lang.Object r6 = r4.get(r0)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.lang.Iterable r6 = (java.lang.Iterable) r6     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.lang.String r5 = android.text.TextUtils.join(r5, r6)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            goto L_0x014b
        L_0x014a:
            r5 = r0
        L_0x014b:
            java.lang.String r6 = "Set-Cookie"
            java.lang.Object r6 = r4.get(r6)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.util.List r6 = (java.util.List) r6     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r6 == 0) goto L_0x0187
            java.util.Iterator r6 = r6.iterator()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
        L_0x0159:
            boolean r7 = r6.hasNext()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r7 == 0) goto L_0x0187
            java.lang.Object r7 = r6.next()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.util.List r7 = java.net.HttpCookie.parse(r7)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r7 == 0) goto L_0x0159
            boolean r8 = r7.isEmpty()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r8 == 0) goto L_0x0172
            goto L_0x0159
        L_0x0172:
            java.net.CookieManager r8 = c     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.net.CookieStore r8 = r8.getCookieStore()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.net.URI r9 = r1.toURI()     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            r10 = 0
            java.lang.Object r7 = r7.get(r10)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            java.net.HttpCookie r7 = (java.net.HttpCookie) r7     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            r8.add(r9, r7)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            goto L_0x0159
        L_0x0187:
            com.alipay.sdk.net.a$b r1 = new com.alipay.sdk.net.a$b     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            r1.<init>(r4, r5, r3)     // Catch:{ Throwable -> 0x019f, all -> 0x019a }
            if (r12 == 0) goto L_0x0191
            r12.disconnect()     // Catch:{ Throwable -> 0x0191 }
        L_0x0191:
            r13.close()     // Catch:{ Throwable -> 0x0194 }
        L_0x0194:
            if (r2 == 0) goto L_0x0199
            r2.close()     // Catch:{ Throwable -> 0x0199 }
        L_0x0199:
            return r1
        L_0x019a:
            r0 = move-exception
            r11 = r0
            r0 = r13
            r13 = r11
            goto L_0x01d2
        L_0x019f:
            r1 = move-exception
            r11 = r1
            r1 = r13
            r13 = r11
            goto L_0x01b9
        L_0x01a4:
            r13 = move-exception
            goto L_0x01d2
        L_0x01a6:
            r13 = move-exception
            r1 = r0
            goto L_0x01b9
        L_0x01a9:
            r13 = move-exception
            r2 = r0
            goto L_0x01d2
        L_0x01ac:
            r13 = move-exception
            r1 = r0
            goto L_0x01b8
        L_0x01af:
            r12 = move-exception
            r13 = r12
            r12 = r0
            r2 = r12
            goto L_0x01d2
        L_0x01b4:
            r12 = move-exception
            r13 = r12
            r12 = r0
            r1 = r12
        L_0x01b8:
            r2 = r1
        L_0x01b9:
            com.alipay.sdk.util.c.a(r13)     // Catch:{ all -> 0x01d0 }
            if (r12 == 0) goto L_0x01c3
            r12.disconnect()     // Catch:{ Throwable -> 0x01c2 }
            goto L_0x01c3
        L_0x01c2:
        L_0x01c3:
            if (r1 == 0) goto L_0x01ca
            r1.close()     // Catch:{ Throwable -> 0x01c9 }
            goto L_0x01ca
        L_0x01c9:
        L_0x01ca:
            if (r2 == 0) goto L_0x01cf
            r2.close()     // Catch:{ Throwable -> 0x01cf }
        L_0x01cf:
            return r0
        L_0x01d0:
            r13 = move-exception
            r0 = r1
        L_0x01d2:
            if (r12 == 0) goto L_0x01d9
            r12.disconnect()     // Catch:{ Throwable -> 0x01d8 }
            goto L_0x01d9
        L_0x01d8:
        L_0x01d9:
            if (r0 == 0) goto L_0x01e0
            r0.close()     // Catch:{ Throwable -> 0x01df }
            goto L_0x01e0
        L_0x01df:
        L_0x01e0:
            if (r2 == 0) goto L_0x01e5
            r2.close()     // Catch:{ Throwable -> 0x01e5 }
        L_0x01e5:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.net.a.a(android.content.Context, com.alipay.sdk.net.a$a):com.alipay.sdk.net.a$b");
    }

    private static Proxy a(Context context) {
        String c2 = c(context);
        if (c2 != null && !c2.contains("wap")) {
            return null;
        }
        try {
            String property = System.getProperty("https.proxyHost");
            String property2 = System.getProperty("https.proxyPort");
            if (!TextUtils.isEmpty(property)) {
                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(property, Integer.parseInt(property2)));
            }
            return null;
        } catch (Throwable unused) {
            return null;
        }
    }

    private static NetworkInfo b(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception unused) {
            return null;
        }
    }

    private static String c(Context context) {
        try {
            NetworkInfo b2 = b(context);
            if (b2 == null || !b2.isAvailable()) {
                return "none";
            }
            if (b2.getType() == 1) {
                return "wifi";
            }
            return b2.getExtraInfo().toLowerCase();
        } catch (Exception unused) {
            return "none";
        }
    }

    private static byte[] a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr, 0, bArr.length);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
        }
    }
}
