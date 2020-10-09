package org.android.spdy;

import java.security.cert.CertificateFactory;
import java.security.cert.TrustAnchor;
import java.util.HashSet;
import java.util.Set;

class AndroidTrustAnchors {
    private static String Android_CA_PATH = "/system/etc/security/cacerts/";
    public static volatile boolean init = false;
    private static AndroidTrustAnchors singleton = new AndroidTrustAnchors();
    private CertificateFactory certificateFactory;
    private Set<TrustAnchor> trustAnchors;

    public static AndroidTrustAnchors getInstance() {
        return singleton;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x006d A[SYNTHETIC, Splitter:B:40:0x006d] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0079 A[SYNTHETIC, Splitter:B:47:0x0079] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x0084 A[SYNTHETIC, Splitter:B:52:0x0084] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x007f A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x007f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void Initialize() {
        /*
            r9 = this;
            monitor-enter(r9)
            boolean r0 = init     // Catch:{ all -> 0x0092 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r9)
            return
        L_0x0007:
            java.lang.String r0 = "X.509"
            java.security.cert.CertificateFactory r0 = java.security.cert.CertificateFactory.getInstance(r0)     // Catch:{ CertificateException -> 0x0010 }
            r9.certificateFactory = r0     // Catch:{ CertificateException -> 0x0010 }
            goto L_0x0014
        L_0x0010:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0092 }
        L_0x0014:
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x0092 }
            java.lang.String r1 = Android_CA_PATH     // Catch:{ all -> 0x0092 }
            r0.<init>(r1)     // Catch:{ all -> 0x0092 }
            java.io.File[] r0 = r0.listFiles()     // Catch:{ all -> 0x0092 }
            int r1 = r0.length     // Catch:{ all -> 0x0092 }
            r2 = 0
        L_0x0021:
            if (r2 >= r1) goto L_0x008d
            r3 = r0[r2]     // Catch:{ all -> 0x0092 }
            boolean r4 = r3.isDirectory()     // Catch:{ all -> 0x0092 }
            if (r4 == 0) goto L_0x002c
            goto L_0x007f
        L_0x002c:
            r4 = 0
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0073, CertificateException -> 0x0067 }
            r5.<init>(r3)     // Catch:{ FileNotFoundException -> 0x0073, CertificateException -> 0x0067 }
            java.security.cert.CertificateFactory r3 = r9.certificateFactory     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            java.util.Collection r3 = r3.generateCertificates(r5)     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
        L_0x003c:
            boolean r6 = r3.hasNext()     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            if (r6 == 0) goto L_0x0053
            java.util.Set<java.security.cert.TrustAnchor> r6 = r9.trustAnchors     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            java.security.cert.TrustAnchor r7 = new java.security.cert.TrustAnchor     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            java.lang.Object r8 = r3.next()     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            java.security.cert.X509Certificate r8 = (java.security.cert.X509Certificate) r8     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            r7.<init>(r8, r4)     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            r6.add(r7)     // Catch:{ FileNotFoundException -> 0x0061, CertificateException -> 0x005e, all -> 0x005c }
            goto L_0x003c
        L_0x0053:
            r5.close()     // Catch:{ IOException -> 0x0057 }
            goto L_0x007f
        L_0x0057:
            r3 = move-exception
        L_0x0058:
            r3.printStackTrace()     // Catch:{ all -> 0x0092 }
            goto L_0x007f
        L_0x005c:
            r0 = move-exception
            goto L_0x0082
        L_0x005e:
            r3 = move-exception
            r4 = r5
            goto L_0x0068
        L_0x0061:
            r3 = move-exception
            r4 = r5
            goto L_0x0074
        L_0x0064:
            r0 = move-exception
            r5 = r4
            goto L_0x0082
        L_0x0067:
            r3 = move-exception
        L_0x0068:
            r3.printStackTrace()     // Catch:{ all -> 0x0064 }
            if (r4 == 0) goto L_0x007f
            r4.close()     // Catch:{ IOException -> 0x0071 }
            goto L_0x007f
        L_0x0071:
            r3 = move-exception
            goto L_0x0058
        L_0x0073:
            r3 = move-exception
        L_0x0074:
            r3.printStackTrace()     // Catch:{ all -> 0x0064 }
            if (r4 == 0) goto L_0x007f
            r4.close()     // Catch:{ IOException -> 0x007d }
            goto L_0x007f
        L_0x007d:
            r3 = move-exception
            goto L_0x0058
        L_0x007f:
            int r2 = r2 + 1
            goto L_0x0021
        L_0x0082:
            if (r5 == 0) goto L_0x008c
            r5.close()     // Catch:{ IOException -> 0x0088 }
            goto L_0x008c
        L_0x0088:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ all -> 0x0092 }
        L_0x008c:
            throw r0     // Catch:{ all -> 0x0092 }
        L_0x008d:
            r0 = 1
            init = r0     // Catch:{ all -> 0x0092 }
            monitor-exit(r9)
            return
        L_0x0092:
            r0 = move-exception
            monitor-exit(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.spdy.AndroidTrustAnchors.Initialize():void");
    }

    private AndroidTrustAnchors() {
        this.trustAnchors = null;
        this.certificateFactory = null;
        this.trustAnchors = new HashSet();
    }

    public Set<TrustAnchor> getTrustAnchors() {
        return this.trustAnchors;
    }

    public CertificateFactory getCertificateFactory() {
        return this.certificateFactory;
    }
}
