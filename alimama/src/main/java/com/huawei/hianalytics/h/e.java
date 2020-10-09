package com.huawei.hianalytics.h;

import com.huawei.hianalytics.g.b;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class e implements X509TrustManager {
    protected List<X509TrustManager> a = new ArrayList();

    public e() {
        TrustManagerFactory instance = TrustManagerFactory.getInstance("X509");
        KeyStore instance2 = KeyStore.getInstance("bks");
        instance2.load((InputStream) null, (char[]) null);
        String[] strArr = a.a;
        for (int i = 0; i < strArr.length; i++) {
            instance2.setCertificateEntry("Certificate" + i, a(strArr[i]));
        }
        instance.init(instance2);
        TrustManager[] trustManagers = instance.getTrustManagers();
        for (int i2 = 0; i2 < trustManagers.length; i2++) {
            if (trustManagers[i2] instanceof X509TrustManager) {
                this.a.add((X509TrustManager) trustManagers[i2]);
            }
        }
        if (this.a.isEmpty()) {
            throw new CertificateException("X509TrustManager is empty");
        }
    }

    private static X509Certificate a(String str) {
        try {
            return (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException unused) {
            b.c("SecureX509TrustManager", "generateCertificate: Exception has happened!Encoding is utf-8!");
            throw new CertificateException("generateCertificate(): UnsupportedEncodingException");
        }
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) {
        if (!this.a.isEmpty()) {
            this.a.get(0).checkClientTrusted(x509CertificateArr, str);
            return;
        }
        throw new CertificateException("checkClientTrusted CertificateException");
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) {
        if (!this.a.isEmpty()) {
            this.a.get(0).checkServerTrusted(x509CertificateArr, str);
            return;
        }
        throw new CertificateException("checkServerTrusted CertificateException");
    }

    public X509Certificate[] getAcceptedIssuers() {
        try {
            ArrayList arrayList = new ArrayList();
            for (X509TrustManager acceptedIssuers : this.a) {
                arrayList.addAll(Arrays.asList(acceptedIssuers.getAcceptedIssuers()));
            }
            return (X509Certificate[]) arrayList.toArray(new X509Certificate[arrayList.size()]);
        } catch (Exception e) {
            b.d("SecureX509TrustManager", "getAcceptedIssuers exception : " + e.getMessage());
            return new X509Certificate[0];
        }
    }
}
