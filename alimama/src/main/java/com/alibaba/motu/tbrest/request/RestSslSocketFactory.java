package com.alibaba.motu.tbrest.request;

import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import com.alibaba.motu.tbrest.utils.LogUtil;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class RestSslSocketFactory extends SSLSocketFactory {
    private String peerHost;
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

    public RestSslSocketFactory(String str) {
        this.peerHost = str;
    }

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        if (this.peerHost == null) {
            this.peerHost = str;
        }
        LogUtil.d("host" + this.peerHost + "port" + i + "autoClose" + z);
        InetAddress inetAddress = socket.getInetAddress();
        if (z) {
            socket.close();
        }
        SSLCertificateSocketFactory sSLCertificateSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(0);
        SSLSocket sSLSocket = (SSLSocket) sSLCertificateSocketFactory.createSocket(inetAddress, i);
        sSLSocket.setEnabledProtocols(sSLSocket.getSupportedProtocols());
        if (Build.VERSION.SDK_INT >= 17) {
            sSLCertificateSocketFactory.setHostname(sSLSocket, this.peerHost);
        } else {
            try {
                if (this.setHostNameMethod == null) {
                    this.setHostNameMethod = sSLSocket.getClass().getMethod("setHostname", new Class[]{String.class});
                    this.setHostNameMethod.setAccessible(true);
                }
                this.setHostNameMethod.invoke(sSLSocket, new Object[]{this.peerHost});
            } catch (Exception e) {
                LogUtil.w("SNI not useable", e);
            }
        }
        sSLSocket.getSession();
        return sSLSocket;
    }
}
