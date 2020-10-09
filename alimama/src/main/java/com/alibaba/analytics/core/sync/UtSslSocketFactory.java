package com.alibaba.analytics.core.sync;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

class UtSslSocketFactory extends SSLSocketFactory {
    private static final String TAG = "UtSslSocketFactory";
    private String bizHost;
    private Method setHostNameMethod = null;

    public Socket createSocket() throws IOException {
        return null;
    }

    public Socket createSocket(String str, int i) throws IOException {
        return null;
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return null;
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return null;
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return null;
    }

    public UtSslSocketFactory(String str) {
        this.bizHost = str;
    }

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        SSLSocket sSLSocket;
        Logger.d(TAG, "bizHost", this.bizHost, "host", str, "port", Integer.valueOf(i), "autoClose", Boolean.valueOf(z));
        if (!TextUtils.isEmpty(this.bizHost)) {
            Logger.d(TAG, "customized createSocket. host: " + this.bizHost);
            try {
                SSLCertificateSocketFactory sSLCertificateSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(10000, new SSLSessionCache(Variables.getInstance().getContext()));
                if (Build.VERSION.SDK_INT < 24) {
                    sSLCertificateSocketFactory.setTrustManagers(UtTrustManager.getTrustManagers());
                } else {
                    sSLCertificateSocketFactory.setTrustManagers(UtExtendTrustManager.getTrustManagers());
                }
                sSLSocket = (SSLSocket) sSLCertificateSocketFactory.createSocket(socket, this.bizHost, i, z);
                sSLSocket.setEnabledProtocols(sSLSocket.getSupportedProtocols());
                if (Build.VERSION.SDK_INT < 17) {
                    if (this.setHostNameMethod == null) {
                        this.setHostNameMethod = sSLSocket.getClass().getMethod("setHostname", new Class[]{String.class});
                        this.setHostNameMethod.setAccessible(true);
                    }
                    this.setHostNameMethod.invoke(sSLSocket, new Object[]{this.bizHost});
                } else {
                    sSLCertificateSocketFactory.setUseSessionTickets(sSLSocket, true);
                    sSLCertificateSocketFactory.setHostname(sSLSocket, this.bizHost);
                }
            } catch (Exception e) {
                Logger.d("", "SNI not useable", e);
            } catch (Throwable th) {
                throw new IOException("createSocket exception: " + th);
            }
            sSLSocket.startHandshake();
            Logger.d(TAG, "customized createSocket end");
            return sSLSocket;
        }
        throw new IOException("SDK set empty bizHost");
    }

    public boolean equals(Object obj) {
        if (TextUtils.isEmpty(this.bizHost) || !(obj instanceof UtSslSocketFactory)) {
            return false;
        }
        String str = ((UtSslSocketFactory) obj).bizHost;
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return this.bizHost.equals(str);
    }

    public String getHost() {
        return this.bizHost;
    }
}
