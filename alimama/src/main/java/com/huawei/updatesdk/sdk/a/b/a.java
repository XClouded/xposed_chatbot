package com.huawei.updatesdk.sdk.a.b;

import com.huawei.updatesdk.support.e.b;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class a {
    private HttpURLConnection a = null;

    /* renamed from: com.huawei.updatesdk.sdk.a.b.a$a  reason: collision with other inner class name */
    public static class C0014a {
        /* access modifiers changed from: private */
        public String a;
        /* access modifiers changed from: private */
        public int b;

        public String a() {
            return (String) b.a(this.a);
        }
    }

    private HttpURLConnection a(String str) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IllegalAccessException {
        HttpsURLConnection httpsURLConnection;
        URL url = new URL(str);
        Proxy e = com.huawei.updatesdk.sdk.a.d.c.b.e(com.huawei.updatesdk.sdk.service.a.a.a().b());
        if ("https".equals(url.getProtocol())) {
            httpsURLConnection = (HttpsURLConnection) (e == null ? url.openConnection() : url.openConnection(e));
            HttpsURLConnection httpsURLConnection2 = httpsURLConnection;
            httpsURLConnection2.setSSLSocketFactory(b.a(com.huawei.updatesdk.sdk.service.a.a.a().b()));
            httpsURLConnection2.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
        } else {
            httpsURLConnection = null;
        }
        return "http".equals(url.getProtocol()) ? (HttpURLConnection) url.openConnection() : httpsURLConnection;
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x003b A[SYNTHETIC, Splitter:B:24:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0045 A[SYNTHETIC, Splitter:B:29:0x0045] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] a(byte[] r6) {
        /*
            r5 = this;
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0030 }
            r1.<init>()     // Catch:{ IOException -> 0x0030 }
            java.io.DataOutputStream r2 = new java.io.DataOutputStream     // Catch:{ IOException -> 0x002c }
            java.util.zip.GZIPOutputStream r3 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x002c }
            int r4 = r6.length     // Catch:{ IOException -> 0x002c }
            r3.<init>(r1, r4)     // Catch:{ IOException -> 0x002c }
            r2.<init>(r3)     // Catch:{ IOException -> 0x002c }
            r0 = 0
            int r3 = r6.length     // Catch:{ IOException -> 0x0029, all -> 0x0026 }
            r2.write(r6, r0, r3)     // Catch:{ IOException -> 0x0029, all -> 0x0026 }
            r2.flush()     // Catch:{ IOException -> 0x0029, all -> 0x0026 }
            r2.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x003e
        L_0x001d:
            r6 = move-exception
            java.lang.String r0 = "HttpsUtil"
            java.lang.String r2 = "gzip error!"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r0, r2, r6)
            goto L_0x003e
        L_0x0026:
            r6 = move-exception
            r0 = r2
            goto L_0x0043
        L_0x0029:
            r6 = move-exception
            r0 = r2
            goto L_0x0032
        L_0x002c:
            r6 = move-exception
            goto L_0x0032
        L_0x002e:
            r6 = move-exception
            goto L_0x0043
        L_0x0030:
            r6 = move-exception
            r1 = r0
        L_0x0032:
            java.lang.String r2 = "HttpsUtil"
            java.lang.String r3 = "gzip error!"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r2, r3, r6)     // Catch:{ all -> 0x002e }
            if (r0 == 0) goto L_0x003e
            r0.close()     // Catch:{ IOException -> 0x001d }
        L_0x003e:
            byte[] r6 = r1.toByteArray()
            return r6
        L_0x0043:
            if (r0 == 0) goto L_0x0051
            r0.close()     // Catch:{ IOException -> 0x0049 }
            goto L_0x0051
        L_0x0049:
            r0 = move-exception
            java.lang.String r1 = "HttpsUtil"
            java.lang.String r2 = "gzip error!"
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(r1, r2, r0)
        L_0x0051:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.a.b.a.a(byte[]):byte[]");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.huawei.updatesdk.sdk.a.b.a.C0014a a(java.lang.String r5, java.lang.String r6, java.lang.String r7, java.lang.String r8) throws java.io.IOException, java.security.cert.CertificateException, java.lang.IllegalAccessException, java.security.NoSuchAlgorithmException, java.security.KeyStoreException, java.security.KeyManagementException {
        /*
            r4 = this;
            com.huawei.updatesdk.sdk.a.b.a$a r0 = new com.huawei.updatesdk.sdk.a.b.a$a
            r0.<init>()
            r1 = 0
            java.net.HttpURLConnection r5 = r4.a((java.lang.String) r5)     // Catch:{ all -> 0x00be }
            if (r5 != 0) goto L_0x0018
            if (r5 == 0) goto L_0x0011
            r5.disconnect()
        L_0x0011:
            com.huawei.updatesdk.sdk.a.d.c.a(r1)
            com.huawei.updatesdk.sdk.a.d.c.a(r1)
            return r0
        L_0x0018:
            r4.a = r5     // Catch:{ all -> 0x00bb }
            r2 = 1
            r5.setDoInput(r2)     // Catch:{ all -> 0x00bb }
            r5.setDoOutput(r2)     // Catch:{ all -> 0x00bb }
            r2 = 0
            r5.setUseCaches(r2)     // Catch:{ all -> 0x00bb }
            r2 = 5000(0x1388, float:7.006E-42)
            r5.setConnectTimeout(r2)     // Catch:{ all -> 0x00bb }
            r2 = 10000(0x2710, float:1.4013E-41)
            r5.setReadTimeout(r2)     // Catch:{ all -> 0x00bb }
            java.lang.String r2 = "POST"
            r5.setRequestMethod(r2)     // Catch:{ all -> 0x00bb }
            java.lang.String r2 = "Content-Type"
            java.lang.String r3 = "application/x-gzip"
            r5.setRequestProperty(r2, r3)     // Catch:{ all -> 0x00bb }
            java.lang.String r2 = "Content-Encoding"
            java.lang.String r3 = "gzip"
            r5.setRequestProperty(r2, r3)     // Catch:{ all -> 0x00bb }
            java.lang.String r2 = "Connection"
            java.lang.String r3 = "Keep-Alive"
            r5.setRequestProperty(r2, r3)     // Catch:{ all -> 0x00bb }
            java.lang.String r2 = "User-Agent"
            r5.setRequestProperty(r2, r8)     // Catch:{ all -> 0x00bb }
            java.io.DataOutputStream r8 = new java.io.DataOutputStream     // Catch:{ all -> 0x00bb }
            java.io.OutputStream r2 = r5.getOutputStream()     // Catch:{ all -> 0x00bb }
            r8.<init>(r2)     // Catch:{ all -> 0x00bb }
            byte[] r6 = r6.getBytes(r7)     // Catch:{ all -> 0x00b7 }
            byte[] r6 = r4.a((byte[]) r6)     // Catch:{ all -> 0x00b7 }
            r8.write(r6)     // Catch:{ all -> 0x00b7 }
            r8.flush()     // Catch:{ all -> 0x00b7 }
            int r6 = r5.getResponseCode()     // Catch:{ all -> 0x00b7 }
            int unused = r0.b = r6     // Catch:{ all -> 0x00b7 }
            r7 = 200(0xc8, float:2.8E-43)
            if (r6 != r7) goto L_0x007b
            java.io.BufferedInputStream r6 = new java.io.BufferedInputStream     // Catch:{ all -> 0x00b7 }
            java.io.InputStream r7 = r5.getInputStream()     // Catch:{ all -> 0x00b7 }
            r6.<init>(r7)     // Catch:{ all -> 0x00b7 }
        L_0x0079:
            r1 = r6
            goto L_0x0085
        L_0x007b:
            java.io.BufferedInputStream r6 = new java.io.BufferedInputStream     // Catch:{ all -> 0x00b7 }
            java.io.InputStream r7 = r5.getErrorStream()     // Catch:{ all -> 0x00b7 }
            r6.<init>(r7)     // Catch:{ all -> 0x00b7 }
            goto L_0x0079
        L_0x0085:
            com.huawei.updatesdk.sdk.a.d.b r6 = new com.huawei.updatesdk.sdk.a.d.b     // Catch:{ all -> 0x00b7 }
            r6.<init>()     // Catch:{ all -> 0x00b7 }
            com.huawei.updatesdk.sdk.a.a.a.a r7 = com.huawei.updatesdk.sdk.a.a.a.a.a()     // Catch:{ all -> 0x00b7 }
            byte[] r7 = r7.b()     // Catch:{ all -> 0x00b7 }
        L_0x0092:
            int r2 = r1.read(r7)     // Catch:{ all -> 0x00b7 }
            r3 = -1
            if (r2 == r3) goto L_0x009d
            r6.a(r7, r2)     // Catch:{ all -> 0x00b7 }
            goto L_0x0092
        L_0x009d:
            com.huawei.updatesdk.sdk.a.a.a.a r2 = com.huawei.updatesdk.sdk.a.a.a.a.a()     // Catch:{ all -> 0x00b7 }
            r2.a(r7)     // Catch:{ all -> 0x00b7 }
            java.lang.String r6 = r6.a()     // Catch:{ all -> 0x00b7 }
            java.lang.String unused = r0.a = r6     // Catch:{ all -> 0x00b7 }
            if (r5 == 0) goto L_0x00b0
            r5.disconnect()
        L_0x00b0:
            com.huawei.updatesdk.sdk.a.d.c.a(r8)
            com.huawei.updatesdk.sdk.a.d.c.a(r1)
            return r0
        L_0x00b7:
            r6 = move-exception
            r7 = r1
            r1 = r8
            goto L_0x00c1
        L_0x00bb:
            r6 = move-exception
            r7 = r1
            goto L_0x00c1
        L_0x00be:
            r6 = move-exception
            r5 = r1
            r7 = r5
        L_0x00c1:
            if (r5 == 0) goto L_0x00c6
            r5.disconnect()
        L_0x00c6:
            com.huawei.updatesdk.sdk.a.d.c.a(r1)
            com.huawei.updatesdk.sdk.a.d.c.a(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.updatesdk.sdk.a.b.a.a(java.lang.String, java.lang.String, java.lang.String, java.lang.String):com.huawei.updatesdk.sdk.a.b.a$a");
    }
}
