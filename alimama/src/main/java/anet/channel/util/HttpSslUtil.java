package anet.channel.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpSslUtil {
    public static final HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
    public static final SSLSocketFactory TRUST_ALL_SSL_SOCKET_FACTORY = SSLTrustAllSocketFactory.getSocketFactory();
    static SSLSocketFactory sslSocketFactory;
    static HostnameVerifier verifier;

    public static SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    public static void setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
        sslSocketFactory = sSLSocketFactory;
    }

    public static HostnameVerifier getHostnameVerifier() {
        return verifier;
    }

    public static void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        verifier = hostnameVerifier;
    }

    private static class AllowAllHostnameVerifier implements HostnameVerifier {
        public boolean verify(String str, SSLSession sSLSession) {
            return true;
        }

        private AllowAllHostnameVerifier() {
        }
    }

    private static class SSLTrustAllSocketFactory {
        private SSLTrustAllSocketFactory() {
        }

        private static class SSLTrustAllManager implements X509TrustManager {
            public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            private SSLTrustAllManager() {
            }
        }

        public static SSLSocketFactory getSocketFactory() {
            try {
                SSLContext instance = SSLContext.getInstance("TLS");
                instance.init((KeyManager[]) null, new TrustManager[]{new SSLTrustAllManager()}, (SecureRandom) null);
                return instance.getSocketFactory();
            } catch (Throwable th) {
                ALog.w("awcn.SSLTrustAllSocketFactory", "getSocketFactory error :" + th.getMessage(), (String) null, new Object[0]);
                return null;
            }
        }
    }
}
