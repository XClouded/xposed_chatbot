package com.huawei.hianalytics.h;

import android.os.Build;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.conn.ssl.X509HostnameVerifier;

final class b extends SSLSocketFactory {
    public static final X509HostnameVerifier a = new StrictHostnameVerifier();
    private static final String[] b = {"3DES", "DES", MessageDigestAlgorithms.MD5, "RC4", "aNULL", "eNULL", "TEA", "SHA0", MessageDigestAlgorithms.MD2, "MD4", "RIPEMD", "DESX", "DES40", "RC2", "ANON", "NULL", "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"};
    private static SocketFactory d;
    private final SSLContext c = SSLContext.getInstance("TLSv1.2");

    private b() {
        e eVar = new e();
        this.c.init((KeyManager[]) null, new X509TrustManager[]{eVar}, (SecureRandom) null);
    }

    public static SocketFactory a() {
        return b();
    }

    private void a(Socket socket) {
        if (socket instanceof SSLSocket) {
            SSLSocket sSLSocket = (SSLSocket) socket;
            a(sSLSocket);
            b(sSLSocket);
        }
    }

    private void a(SSLSocket sSLSocket) {
        if (sSLSocket != null && Build.VERSION.SDK_INT >= 16) {
            sSLSocket.setEnabledProtocols(new String[]{"TLSv1.2"});
        }
    }

    private static boolean a(String str) {
        for (String contains : b) {
            if (str.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    private static synchronized SocketFactory b() {
        String str;
        String str2;
        synchronized (b.class) {
            try {
                if (d == null) {
                    d = new b();
                }
                SocketFactory socketFactory = d;
                return socketFactory;
            } catch (KeyManagementException unused) {
                str = "Factory";
                str2 = "getLocalInstanceLock(): Failed to new SSLSocketFactory instance,Key Manage!";
                com.huawei.hianalytics.g.b.c(str, str2);
                return null;
            } catch (NoSuchAlgorithmException unused2) {
                str = "Factory";
                str2 = "getLocalInstanceLock(): Failed to new SSLSocketFactory instance,Algorithm Exception!";
                com.huawei.hianalytics.g.b.c(str, str2);
                return null;
            } catch (KeyStoreException unused3) {
                str = "Factory";
                str2 = "getLocalInstanceLock(): Failed to new SSLSocketFactory instance,Key Store!";
                com.huawei.hianalytics.g.b.c(str, str2);
                return null;
            } catch (GeneralSecurityException unused4) {
                str = "Factory";
                str2 = "getLocalInstanceLock(): GeneralSecurityException: Failed to new SSLSocketFactory instance";
                com.huawei.hianalytics.g.b.c(str, str2);
                return null;
            } catch (IOException unused5) {
                str = "Factory";
                str2 = "getLocalInstanceLock(): Failed to new SSLSocketFactory instance,IO!";
                com.huawei.hianalytics.g.b.c(str, str2);
                return null;
            }
        }
    }

    private static void b(SSLSocket sSLSocket) {
        String[] enabledCipherSuites = sSLSocket.getEnabledCipherSuites();
        if (enabledCipherSuites != null && enabledCipherSuites.length != 0) {
            ArrayList arrayList = new ArrayList();
            for (String str : enabledCipherSuites) {
                if (!a(str)) {
                    arrayList.add(str);
                }
            }
            sSLSocket.setEnabledCipherSuites((String[]) arrayList.toArray(new String[arrayList.size()]));
        }
    }

    public Socket createSocket(String str, int i) {
        Socket createSocket = this.c.getSocketFactory().createSocket(str, i);
        a(createSocket);
        return createSocket;
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) {
        return createSocket(str, i);
    }

    public Socket createSocket(InetAddress inetAddress, int i) {
        return createSocket(inetAddress.getHostAddress(), i);
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) {
        return createSocket(inetAddress.getHostAddress(), i);
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) {
        Socket createSocket = this.c.getSocketFactory().createSocket(socket, str, i, z);
        a(createSocket);
        return createSocket;
    }

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }
}
