package com.alibaba.analytics.core.sync;

import android.annotation.TargetApi;
import com.alibaba.analytics.utils.Logger;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

@TargetApi(24)
class UtExtendTrustManager extends X509ExtendedTrustManager {
    private static final String TAG = "UtExtendTrustManager";
    private static TrustManager[] trustManagers;

    UtExtendTrustManager() {
    }

    static synchronized TrustManager[] getTrustManagers() {
        TrustManager[] trustManagerArr;
        synchronized (UtExtendTrustManager.class) {
            if (trustManagers == null) {
                trustManagers = new TrustManager[]{new UtExtendTrustManager()};
            }
            trustManagerArr = trustManagers;
        }
        return trustManagerArr;
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        Logger.d(TAG, "checkClientTrusted1");
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
        int i = 0;
        Logger.d(TAG, "checkServerTrusted1");
        if (x509CertificateArr == null || x509CertificateArr.length <= 0) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }
        try {
            TrustManagerFactory instance = TrustManagerFactory.getInstance("X509");
            instance.init((KeyStore) null);
            if (instance != null && instance.getTrustManagers() != null) {
                TrustManager[] trustManagers2 = instance.getTrustManagers();
                int length = trustManagers2.length;
                while (i < length) {
                    try {
                        ((X509TrustManager) trustManagers2[i]).checkServerTrusted(x509CertificateArr, str);
                        i++;
                    } catch (CertificateException e) {
                        Throwable th = e;
                        while (th != null) {
                            if (!(th instanceof CertificateExpiredException) && !(th instanceof CertificateNotYetValidException)) {
                                th = th.getCause();
                            } else {
                                return;
                            }
                        }
                        throw e;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new CertificateException(e2);
        } catch (KeyStoreException e3) {
            throw new CertificateException(e3);
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
        Logger.d(TAG, "checkClientTrusted2");
        if (x509CertificateArr == null || x509CertificateArr.length == 0) {
            throw new IllegalArgumentException("parameter is not used");
        } else if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("parameter is not used");
        } else {
            try {
                x509CertificateArr[0].checkValidity();
            } catch (Exception unused) {
                throw new CertificateException("Certificate not valid or trusted.");
            }
        }
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, Socket socket) throws CertificateException {
        Logger.d(TAG, "checkServerTrusted2");
    }

    public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
        Logger.d(TAG, "checkClientTrusted3");
    }

    public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str, SSLEngine sSLEngine) throws CertificateException {
        Logger.d(TAG, "checkServerTrusted3");
    }
}
