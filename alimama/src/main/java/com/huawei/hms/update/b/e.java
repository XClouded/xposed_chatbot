package com.huawei.hms.update.b;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.X509TrustManager;

/* compiled from: SecureX509TrustManager */
public class e implements X509TrustManager {
    protected List<X509TrustManager> a = new ArrayList();

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0072 A[SYNTHETIC, Splitter:B:27:0x0072] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public e(android.content.Context r6) throws java.io.IOException, java.security.NoSuchAlgorithmException, java.security.cert.CertificateException, java.security.KeyStoreException, java.lang.IllegalArgumentException {
        /*
            r5 = this;
            r5.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r5.a = r0
            if (r6 == 0) goto L_0x007e
            r0 = 0
            java.lang.String r1 = "X509"
            javax.net.ssl.TrustManagerFactory r1 = javax.net.ssl.TrustManagerFactory.getInstance(r1)     // Catch:{ all -> 0x006c }
            java.lang.String r2 = "bks"
            java.security.KeyStore r2 = java.security.KeyStore.getInstance(r2)     // Catch:{ all -> 0x006c }
            android.content.res.AssetManager r6 = r6.getAssets()     // Catch:{ all -> 0x006c }
            java.lang.String r3 = "updatesdkcas.bks"
            java.io.InputStream r6 = r6.open(r3)     // Catch:{ all -> 0x006c }
            r6.reset()     // Catch:{ all -> 0x006a }
            java.lang.String r0 = ""
            char[] r0 = r0.toCharArray()     // Catch:{ all -> 0x006a }
            r2.load(r6, r0)     // Catch:{ all -> 0x006a }
            r1.init(r2)     // Catch:{ all -> 0x006a }
            javax.net.ssl.TrustManager[] r0 = r1.getTrustManagers()     // Catch:{ all -> 0x006a }
            r1 = 0
        L_0x0037:
            int r2 = r0.length     // Catch:{ all -> 0x006a }
            if (r1 >= r2) goto L_0x004c
            r2 = r0[r1]     // Catch:{ all -> 0x006a }
            boolean r2 = r2 instanceof javax.net.ssl.X509TrustManager     // Catch:{ all -> 0x006a }
            if (r2 == 0) goto L_0x0049
            java.util.List<javax.net.ssl.X509TrustManager> r2 = r5.a     // Catch:{ all -> 0x006a }
            r3 = r0[r1]     // Catch:{ all -> 0x006a }
            javax.net.ssl.X509TrustManager r3 = (javax.net.ssl.X509TrustManager) r3     // Catch:{ all -> 0x006a }
            r2.add(r3)     // Catch:{ all -> 0x006a }
        L_0x0049:
            int r1 = r1 + 1
            goto L_0x0037
        L_0x004c:
            java.util.List<javax.net.ssl.X509TrustManager> r0 = r5.a     // Catch:{ all -> 0x006a }
            boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x006a }
            if (r0 != 0) goto L_0x0062
            if (r6 == 0) goto L_0x0061
            r6.close()     // Catch:{ IOException -> 0x005a }
            goto L_0x0061
        L_0x005a:
            java.lang.String r6 = "SecureX509TrustManager"
            java.lang.String r0 = "close bks exception"
            android.util.Log.e(r6, r0)
        L_0x0061:
            return
        L_0x0062:
            java.security.cert.CertificateException r0 = new java.security.cert.CertificateException     // Catch:{ all -> 0x006a }
            java.lang.String r1 = "X509TrustManager is empty"
            r0.<init>(r1)     // Catch:{ all -> 0x006a }
            throw r0     // Catch:{ all -> 0x006a }
        L_0x006a:
            r0 = move-exception
            goto L_0x0070
        L_0x006c:
            r6 = move-exception
            r4 = r0
            r0 = r6
            r6 = r4
        L_0x0070:
            if (r6 == 0) goto L_0x007d
            r6.close()     // Catch:{ IOException -> 0x0076 }
            goto L_0x007d
        L_0x0076:
            java.lang.String r6 = "SecureX509TrustManager"
            java.lang.String r1 = "close bks exception"
            android.util.Log.e(r6, r1)
        L_0x007d:
            throw r0
        L_0x007e:
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r0 = "context is null"
            r6.<init>(r0)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hms.update.b.e.<init>(android.content.Context):void");
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        if (!this.a.isEmpty()) {
            this.a.get(0).checkClientTrusted(x509CertificateArr, str);
            return;
        }
        throw new CertificateException("checkClientTrusted CertificateException");
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        if (!this.a.isEmpty()) {
            this.a.get(0).checkServerTrusted(x509CertificateArr, str);
            return;
        }
        throw new CertificateException("checkServerTrusted CertificateException");
    }

    public X509Certificate[] getAcceptedIssuers() {
        ArrayList arrayList = new ArrayList();
        for (X509TrustManager acceptedIssuers : this.a) {
            arrayList.addAll(Arrays.asList(acceptedIssuers.getAcceptedIssuers()));
        }
        return (X509Certificate[]) arrayList.toArray(new X509Certificate[arrayList.size()]);
    }
}
