package com.uploader.implement.b.a;

import android.text.TextUtils;
import com.uploader.implement.b.f;
import com.uploader.implement.c;
import com.uploader.implement.e.b;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

/* compiled from: ShortLivedConnection */
public class e extends a {
    static volatile SSLSocketFactory f;
    HttpURLConnection e = null;
    boolean g;

    public boolean b() {
        return true;
    }

    public boolean c() {
        return true;
    }

    public boolean d() {
        return false;
    }

    e(c cVar, g gVar) {
        super(cVar, gVar);
        this.g = gVar.f.startsWith("https://");
    }

    public void a(final f fVar, final int i) {
        b.a(new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:41:0x0102 A[Catch:{ all -> 0x00f9 }] */
            /* JADX WARNING: Removed duplicated region for block: B:44:0x0130 A[Catch:{ all -> 0x00f9 }] */
            /* JADX WARNING: Removed duplicated region for block: B:46:0x0137 A[SYNTHETIC, Splitter:B:46:0x0137] */
            /* JADX WARNING: Removed duplicated region for block: B:53:0x014e A[SYNTHETIC, Splitter:B:53:0x014e] */
            /* JADX WARNING: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r9 = this;
                    com.uploader.implement.b.a.e r0 = com.uploader.implement.b.a.e.this
                    com.uploader.implement.b.b r0 = r0.e()
                    r1 = 1
                    r2 = 8
                    com.uploader.implement.b.a.e r3 = com.uploader.implement.b.a.e.this     // Catch:{ Exception -> 0x0197 }
                    r3.g()     // Catch:{ Exception -> 0x0197 }
                    com.uploader.implement.b.f r3 = r2
                    java.util.Map<java.lang.String, java.lang.String> r3 = r3.a
                    if (r3 == 0) goto L_0x0040
                    com.uploader.implement.b.f r3 = r2
                    java.util.Map<java.lang.String, java.lang.String> r3 = r3.a
                    java.util.Set r3 = r3.entrySet()
                    java.util.Iterator r3 = r3.iterator()
                L_0x0020:
                    boolean r4 = r3.hasNext()
                    if (r4 == 0) goto L_0x0040
                    java.lang.Object r4 = r3.next()
                    java.util.Map$Entry r4 = (java.util.Map.Entry) r4
                    com.uploader.implement.b.a.e r5 = com.uploader.implement.b.a.e.this
                    java.net.HttpURLConnection r5 = r5.e
                    java.lang.Object r6 = r4.getKey()
                    java.lang.String r6 = (java.lang.String) r6
                    java.lang.Object r4 = r4.getValue()
                    java.lang.String r4 = (java.lang.String) r4
                    r5.addRequestProperty(r6, r4)
                    goto L_0x0020
                L_0x0040:
                    r3 = 4
                    boolean r4 = com.uploader.implement.a.a((int) r3)
                    if (r4 == 0) goto L_0x0080
                    java.lang.String r4 = "ShortLivedConnection"
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder
                    r5.<init>()
                    com.uploader.implement.b.a.e r6 = com.uploader.implement.b.a.e.this
                    int r6 = r6.c
                    r5.append(r6)
                    java.lang.String r6 = " URL:"
                    r5.append(r6)
                    com.uploader.implement.b.a.e r6 = com.uploader.implement.b.a.e.this
                    java.net.HttpURLConnection r6 = r6.e
                    java.net.URL r6 = r6.getURL()
                    java.lang.String r6 = r6.toString()
                    r5.append(r6)
                    java.lang.String r6 = " RequestHeaders:"
                    r5.append(r6)
                    com.uploader.implement.b.a.e r6 = com.uploader.implement.b.a.e.this
                    java.net.HttpURLConnection r6 = r6.e
                    java.util.Map r6 = r6.getRequestProperties()
                    r5.append(r6)
                    java.lang.String r5 = r5.toString()
                    com.uploader.implement.a.a(r3, r4, r5)
                L_0x0080:
                    com.uploader.implement.b.a.e r4 = com.uploader.implement.b.a.e.this     // Catch:{ Exception -> 0x0176 }
                    java.net.HttpURLConnection r4 = r4.e     // Catch:{ Exception -> 0x0176 }
                    r4.connect()     // Catch:{ Exception -> 0x0176 }
                    if (r0 == 0) goto L_0x0090
                    com.uploader.implement.b.a.e r4 = com.uploader.implement.b.a.e.this
                    int r5 = r3
                    r0.a((com.uploader.implement.b.e) r4, (int) r5)
                L_0x0090:
                    com.uploader.implement.b.f r4 = r2
                    byte[] r4 = r4.b
                    if (r4 == 0) goto L_0x0163
                    r4 = 0
                    com.uploader.implement.b.a.e r5 = com.uploader.implement.b.a.e.this     // Catch:{ Exception -> 0x00fb }
                    java.net.HttpURLConnection r5 = r5.e     // Catch:{ Exception -> 0x00fb }
                    java.io.OutputStream r5 = r5.getOutputStream()     // Catch:{ Exception -> 0x00fb }
                    com.uploader.implement.b.f r4 = r2     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    byte[] r4 = r4.b     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    com.uploader.implement.b.f r6 = r2     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    int r6 = r6.c     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    com.uploader.implement.b.f r7 = r2     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    int r7 = r7.d     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    r5.write(r4, r6, r7)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    boolean r4 = com.uploader.implement.a.a((int) r3)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    if (r4 == 0) goto L_0x00da
                    java.lang.String r4 = "ShortLivedConnection"
                    java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    r6.<init>()     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    com.uploader.implement.b.a.e r7 = com.uploader.implement.b.a.e.this     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    int r7 = r7.c     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    r6.append(r7)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    java.lang.String r7 = " BODY:"
                    r6.append(r7)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    com.uploader.implement.b.f r8 = r2     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    byte[] r8 = r8.b     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    r7.<init>(r8)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    r6.append(r7)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                    com.uploader.implement.a.a(r3, r4, r6)     // Catch:{ Exception -> 0x00f6, all -> 0x00f3 }
                L_0x00da:
                    if (r5 == 0) goto L_0x0163
                    r5.close()     // Catch:{ IOException -> 0x00e1 }
                    goto L_0x0163
                L_0x00e1:
                    r1 = move-exception
                    boolean r3 = com.uploader.implement.a.a((int) r2)
                    if (r3 == 0) goto L_0x0163
                    java.lang.String r3 = "ShortLivedConnection"
                    java.lang.String r1 = r1.toString()
                    com.uploader.implement.a.a(r2, r3, r1)
                    goto L_0x0163
                L_0x00f3:
                    r0 = move-exception
                    r4 = r5
                    goto L_0x014c
                L_0x00f6:
                    r3 = move-exception
                    r4 = r5
                    goto L_0x00fc
                L_0x00f9:
                    r0 = move-exception
                    goto L_0x014c
                L_0x00fb:
                    r3 = move-exception
                L_0x00fc:
                    boolean r5 = com.uploader.implement.a.a((int) r2)     // Catch:{ all -> 0x00f9 }
                    if (r5 == 0) goto L_0x011c
                    java.lang.String r5 = "ShortLivedConnection"
                    java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f9 }
                    r6.<init>()     // Catch:{ all -> 0x00f9 }
                    com.uploader.implement.b.a.e r7 = com.uploader.implement.b.a.e.this     // Catch:{ all -> 0x00f9 }
                    int r7 = r7.c     // Catch:{ all -> 0x00f9 }
                    r6.append(r7)     // Catch:{ all -> 0x00f9 }
                    java.lang.String r7 = " send data error."
                    r6.append(r7)     // Catch:{ all -> 0x00f9 }
                    java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00f9 }
                    com.uploader.implement.a.a(r2, r5, r6, r3)     // Catch:{ all -> 0x00f9 }
                L_0x011c:
                    com.uploader.implement.b.a.e r3 = com.uploader.implement.b.a.e.this     // Catch:{ all -> 0x00f9 }
                    java.net.HttpURLConnection r3 = r3.e     // Catch:{ all -> 0x00f9 }
                    r3.disconnect()     // Catch:{ all -> 0x00f9 }
                    com.uploader.implement.c.a r3 = new com.uploader.implement.c.a     // Catch:{ all -> 0x00f9 }
                    java.lang.String r5 = "100"
                    java.lang.String r6 = "5"
                    java.lang.String r7 = "send data error"
                    r3.<init>(r5, r6, r7, r1)     // Catch:{ all -> 0x00f9 }
                    if (r0 == 0) goto L_0x0135
                    com.uploader.implement.b.a.e r1 = com.uploader.implement.b.a.e.this     // Catch:{ all -> 0x00f9 }
                    r0.a((com.uploader.implement.b.e) r1, (com.uploader.implement.c.a) r3)     // Catch:{ all -> 0x00f9 }
                L_0x0135:
                    if (r4 == 0) goto L_0x014b
                    r4.close()     // Catch:{ IOException -> 0x013b }
                    goto L_0x014b
                L_0x013b:
                    r0 = move-exception
                    boolean r1 = com.uploader.implement.a.a((int) r2)
                    if (r1 == 0) goto L_0x014b
                    java.lang.String r1 = "ShortLivedConnection"
                    java.lang.String r0 = r0.toString()
                    com.uploader.implement.a.a(r2, r1, r0)
                L_0x014b:
                    return
                L_0x014c:
                    if (r4 == 0) goto L_0x0162
                    r4.close()     // Catch:{ IOException -> 0x0152 }
                    goto L_0x0162
                L_0x0152:
                    r1 = move-exception
                    boolean r3 = com.uploader.implement.a.a((int) r2)
                    if (r3 == 0) goto L_0x0162
                    java.lang.String r1 = r1.toString()
                    java.lang.String r3 = "ShortLivedConnection"
                    com.uploader.implement.a.a(r2, r3, r1)
                L_0x0162:
                    throw r0
                L_0x0163:
                    if (r0 == 0) goto L_0x016c
                    com.uploader.implement.b.a.e r1 = com.uploader.implement.b.a.e.this
                    int r2 = r3
                    r0.b(r1, r2)
                L_0x016c:
                    com.uploader.implement.b.a.e r1 = com.uploader.implement.b.a.e.this
                    com.uploader.implement.b.a.e r2 = com.uploader.implement.b.a.e.this
                    java.net.HttpURLConnection r2 = r2.e
                    r1.a((java.net.HttpURLConnection) r2, (com.uploader.implement.b.b) r0)
                    return
                L_0x0176:
                    r3 = move-exception
                    boolean r4 = com.uploader.implement.a.a((int) r2)
                    if (r4 == 0) goto L_0x0184
                    java.lang.String r4 = "ShortLivedConnection"
                    java.lang.String r5 = "connect error."
                    com.uploader.implement.a.a(r2, r4, r5, r3)
                L_0x0184:
                    com.uploader.implement.c.a r2 = new com.uploader.implement.c.a
                    java.lang.String r3 = "100"
                    java.lang.String r4 = "4"
                    java.lang.String r5 = "connect error"
                    r2.<init>(r3, r4, r5, r1)
                    if (r0 == 0) goto L_0x0196
                    com.uploader.implement.b.a.e r1 = com.uploader.implement.b.a.e.this
                    r0.a((com.uploader.implement.b.e) r1, (com.uploader.implement.c.a) r2)
                L_0x0196:
                    return
                L_0x0197:
                    r3 = move-exception
                    boolean r4 = com.uploader.implement.a.a((int) r2)
                    if (r4 == 0) goto L_0x01b8
                    java.lang.String r4 = "ShortLivedConnection"
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder
                    r5.<init>()
                    com.uploader.implement.b.a.e r6 = com.uploader.implement.b.a.e.this
                    int r6 = r6.c
                    r5.append(r6)
                    java.lang.String r6 = " open connection error, "
                    r5.append(r6)
                    java.lang.String r5 = r5.toString()
                    com.uploader.implement.a.a(r2, r4, r5, r3)
                L_0x01b8:
                    com.uploader.implement.c.a r2 = new com.uploader.implement.c.a
                    java.lang.String r3 = "100"
                    java.lang.String r4 = "3"
                    java.lang.String r5 = "open connection error"
                    r2.<init>(r3, r4, r5, r1)
                    if (r0 == 0) goto L_0x01ca
                    com.uploader.implement.b.a.e r1 = com.uploader.implement.b.a.e.this
                    r0.a((com.uploader.implement.b.e) r1, (com.uploader.implement.c.a) r2)
                L_0x01ca:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.b.a.e.AnonymousClass1.run():void");
            }
        });
    }

    /* access modifiers changed from: private */
    public void g() throws Exception {
        if (this.e == null) {
            g gVar = (g) this.a;
            URL url = new URL(gVar.f);
            if (this.g) {
                this.e = (HttpURLConnection) url.openConnection();
                a(this.e, gVar.g);
            } else {
                Proxy proxy = null;
                if (!TextUtils.isEmpty(gVar.c) && gVar.d > 0) {
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(gVar.c, gVar.d));
                }
                if (proxy != null) {
                    this.e = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    this.e = (HttpURLConnection) url.openConnection();
                }
            }
            this.e.setConnectTimeout(10000);
            this.e.setConnectTimeout(10000);
            this.e.setRequestMethod("POST");
            this.e.setDoOutput(true);
            this.e.setDoInput(true);
        }
    }

    private void a(HttpURLConnection httpURLConnection, final String str) {
        ((HttpsURLConnection) httpURLConnection).setHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String str, SSLSession sSLSession) {
                return HttpsURLConnection.getDefaultHostnameVerifier().verify(str, sSLSession);
            }
        });
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(12:35|36|(2:37|(1:39)(1:103))|40|(1:42)|43|44|45|46|(1:49)|50|51) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x0115 */
    /* JADX WARNING: Removed duplicated region for block: B:106:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x011a  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013c A[Catch:{ all -> 0x0174 }] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0161 A[Catch:{ all -> 0x0174 }] */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0169 A[SYNTHETIC, Splitter:B:73:0x0169] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x0170 A[SYNTHETIC, Splitter:B:77:0x0170] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0177 A[SYNTHETIC, Splitter:B:82:0x0177] */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x017e A[SYNTHETIC, Splitter:B:86:0x017e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(java.net.HttpURLConnection r12, com.uploader.implement.b.b r13) {
        /*
            r11 = this;
            com.uploader.implement.b.f r0 = new com.uploader.implement.b.f
            r0.<init>()
            r1 = 1
            r2 = 8
            int r3 = r12.getResponseCode()     // Catch:{ Exception -> 0x0182 }
            java.lang.String r4 = r12.getResponseMessage()     // Catch:{ Exception -> 0x0182 }
            java.util.Map r5 = r12.getHeaderFields()     // Catch:{ Exception -> 0x0182 }
            r6 = 4
            boolean r7 = com.uploader.implement.a.a((int) r6)     // Catch:{ Exception -> 0x0182 }
            if (r7 == 0) goto L_0x0041
            java.lang.String r7 = "ShortLivedConnection"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0182 }
            r8.<init>()     // Catch:{ Exception -> 0x0182 }
            java.lang.String r9 = "code="
            r8.append(r9)     // Catch:{ Exception -> 0x0182 }
            r8.append(r3)     // Catch:{ Exception -> 0x0182 }
            java.lang.String r9 = ",msg="
            r8.append(r9)     // Catch:{ Exception -> 0x0182 }
            r8.append(r4)     // Catch:{ Exception -> 0x0182 }
            java.lang.String r9 = ",headers="
            r8.append(r9)     // Catch:{ Exception -> 0x0182 }
            r8.append(r5)     // Catch:{ Exception -> 0x0182 }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x0182 }
            com.uploader.implement.a.a(r6, r7, r8)     // Catch:{ Exception -> 0x0182 }
        L_0x0041:
            java.io.InputStream r6 = r12.getInputStream()     // Catch:{ Exception -> 0x0182 }
            r7 = 0
            if (r5 == 0) goto L_0x0087
            boolean r8 = r5.isEmpty()
            if (r8 != 0) goto L_0x0087
            java.util.HashMap r8 = new java.util.HashMap
            int r9 = r5.size()
            r8.<init>(r9)
            r0.a = r8
            java.util.Set r5 = r5.entrySet()
            java.util.Iterator r5 = r5.iterator()
        L_0x0061:
            boolean r8 = r5.hasNext()
            if (r8 == 0) goto L_0x0087
            java.lang.Object r8 = r5.next()
            java.util.Map$Entry r8 = (java.util.Map.Entry) r8
            java.lang.Object r9 = r8.getKey()
            if (r9 == 0) goto L_0x0061
            java.util.Map<java.lang.String, java.lang.String> r9 = r0.a
            java.lang.Object r10 = r8.getKey()
            java.lang.Object r8 = r8.getValue()
            java.util.List r8 = (java.util.List) r8
            java.lang.Object r8 = r8.get(r7)
            r9.put(r10, r8)
            goto L_0x0061
        L_0x0087:
            java.util.Map<java.lang.String, java.lang.String> r5 = r0.a
            if (r5 != 0) goto L_0x0092
            java.util.HashMap r5 = new java.util.HashMap
            r5.<init>()
            r0.a = r5
        L_0x0092:
            java.util.Map<java.lang.String, java.lang.String> r5 = r0.a
            java.lang.String r8 = "response_code"
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r5.put(r8, r3)
            java.util.Map<java.lang.String, java.lang.String> r3 = r0.a
            java.lang.String r5 = "response_msg"
            r3.put(r5, r4)
            if (r6 != 0) goto L_0x00af
            if (r13 == 0) goto L_0x00ab
            r13.a((com.uploader.implement.b.e) r11, (com.uploader.implement.b.f) r0)
        L_0x00ab:
            r11.f()
            return
        L_0x00af:
            r3 = 0
            java.lang.String r4 = "Content-Encoding"
            java.lang.String r12 = r12.getHeaderField(r4)     // Catch:{ Exception -> 0x0133, all -> 0x012f }
            java.lang.String r4 = "gzip"
            boolean r12 = r4.equalsIgnoreCase(r12)     // Catch:{ Exception -> 0x0133, all -> 0x012f }
            if (r12 == 0) goto L_0x00c4
            java.util.zip.GZIPInputStream r12 = new java.util.zip.GZIPInputStream     // Catch:{ Exception -> 0x0133, all -> 0x012f }
            r12.<init>(r6)     // Catch:{ Exception -> 0x0133, all -> 0x012f }
            goto L_0x00c5
        L_0x00c4:
            r12 = r6
        L_0x00c5:
            java.io.DataInputStream r4 = new java.io.DataInputStream     // Catch:{ Exception -> 0x0133, all -> 0x012f }
            r4.<init>(r12)     // Catch:{ Exception -> 0x0133, all -> 0x012f }
            java.io.ByteArrayOutputStream r12 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x012a, all -> 0x0125 }
            r12.<init>()     // Catch:{ Exception -> 0x012a, all -> 0x0125 }
            r3 = 1024(0x400, float:1.435E-42)
            byte[] r3 = new byte[r3]     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
        L_0x00d3:
            int r5 = r4.read(r3)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r6 = -1
            if (r5 == r6) goto L_0x00de
            r12.write(r3, r7, r5)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            goto L_0x00d3
        L_0x00de:
            byte[] r3 = r12.toByteArray()     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r0.b = r3     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            byte[] r3 = r0.b     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            int r3 = r3.length     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r0.d = r3     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r3 = 2
            boolean r5 = com.uploader.implement.a.a((int) r3)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            if (r5 == 0) goto L_0x0112
            java.lang.String r5 = "ShortLivedConnection"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r6.<init>()     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            int r7 = r11.c     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r6.append(r7)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            java.lang.String r7 = " response body:"
            r6.append(r7)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            byte[] r8 = r0.b     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r7.<init>(r8)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            r6.append(r7)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
            com.uploader.implement.a.a(r3, r5, r6)     // Catch:{ Exception -> 0x0123, all -> 0x0121 }
        L_0x0112:
            r4.close()     // Catch:{ IOException -> 0x0115 }
        L_0x0115:
            r12.close()     // Catch:{ IOException -> 0x0118 }
        L_0x0118:
            if (r13 == 0) goto L_0x011d
            r13.a((com.uploader.implement.b.e) r11, (com.uploader.implement.b.f) r0)
        L_0x011d:
            r11.f()
            return
        L_0x0121:
            r13 = move-exception
            goto L_0x0128
        L_0x0123:
            r0 = move-exception
            goto L_0x012d
        L_0x0125:
            r12 = move-exception
            r13 = r12
            r12 = r3
        L_0x0128:
            r3 = r4
            goto L_0x0175
        L_0x012a:
            r12 = move-exception
            r0 = r12
            r12 = r3
        L_0x012d:
            r3 = r4
            goto L_0x0136
        L_0x012f:
            r12 = move-exception
            r13 = r12
            r12 = r3
            goto L_0x0175
        L_0x0133:
            r12 = move-exception
            r0 = r12
            r12 = r3
        L_0x0136:
            boolean r4 = com.uploader.implement.a.a((int) r2)     // Catch:{ all -> 0x0174 }
            if (r4 == 0) goto L_0x0154
            java.lang.String r4 = "ShortLivedConnection"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0174 }
            r5.<init>()     // Catch:{ all -> 0x0174 }
            int r6 = r11.c     // Catch:{ all -> 0x0174 }
            r5.append(r6)     // Catch:{ all -> 0x0174 }
            java.lang.String r6 = " parseResponse, read Stream error"
            r5.append(r6)     // Catch:{ all -> 0x0174 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0174 }
            com.uploader.implement.a.a(r2, r4, r5, r0)     // Catch:{ all -> 0x0174 }
        L_0x0154:
            com.uploader.implement.c.a r0 = new com.uploader.implement.c.a     // Catch:{ all -> 0x0174 }
            java.lang.String r2 = "100"
            java.lang.String r4 = "6"
            java.lang.String r5 = "parseResponse read"
            r0.<init>(r2, r4, r5, r1)     // Catch:{ all -> 0x0174 }
            if (r13 == 0) goto L_0x0164
            r13.a((com.uploader.implement.b.e) r11, (com.uploader.implement.c.a) r0)     // Catch:{ all -> 0x0174 }
        L_0x0164:
            r11.f()     // Catch:{ all -> 0x0174 }
            if (r3 == 0) goto L_0x016e
            r3.close()     // Catch:{ IOException -> 0x016d }
            goto L_0x016e
        L_0x016d:
        L_0x016e:
            if (r12 == 0) goto L_0x0173
            r12.close()     // Catch:{ IOException -> 0x0173 }
        L_0x0173:
            return
        L_0x0174:
            r13 = move-exception
        L_0x0175:
            if (r3 == 0) goto L_0x017c
            r3.close()     // Catch:{ IOException -> 0x017b }
            goto L_0x017c
        L_0x017b:
        L_0x017c:
            if (r12 == 0) goto L_0x0181
            r12.close()     // Catch:{ IOException -> 0x0181 }
        L_0x0181:
            throw r13
        L_0x0182:
            r12 = move-exception
            boolean r0 = com.uploader.implement.a.a((int) r2)
            if (r0 == 0) goto L_0x01a8
            java.lang.String r0 = "ShortLivedConnection"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            int r4 = r11.c
            r3.append(r4)
            java.lang.String r4 = " parseResponse:"
            r3.append(r4)
            java.lang.String r12 = r12.toString()
            r3.append(r12)
            java.lang.String r12 = r3.toString()
            com.uploader.implement.a.a(r2, r0, r12)
        L_0x01a8:
            com.uploader.implement.c.a r12 = new com.uploader.implement.c.a
            java.lang.String r0 = "100"
            java.lang.String r2 = "6"
            java.lang.String r3 = "parseResponse getStream"
            r12.<init>(r0, r2, r3, r1)
            if (r13 == 0) goto L_0x01b8
            r13.a((com.uploader.implement.b.e) r11, (com.uploader.implement.c.a) r12)
        L_0x01b8:
            r11.f()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uploader.implement.b.a.e.a(java.net.HttpURLConnection, com.uploader.implement.b.b):void");
    }

    /* access modifiers changed from: package-private */
    public void f() {
        try {
            this.e.disconnect();
        } catch (Exception unused) {
        }
    }
}
