package com.taobao.zcache.network;

import com.taobao.zcache.log.ZLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLTunnelSocketFactory extends SSLSocketFactory {
    private static final String TAG = "SSLTunnel";
    private SSLSocketFactory dfactory;
    private String tunnelHost;
    private int tunnelPort;
    private String useragent;

    public SSLTunnelSocketFactory(String str, int i, SSLSocketFactory sSLSocketFactory, String str2) {
        this.tunnelHost = str;
        this.tunnelPort = i;
        if (sSLSocketFactory != null) {
            this.dfactory = sSLSocketFactory;
        } else {
            this.dfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
        this.useragent = str2;
    }

    public Socket createSocket(String str, int i) throws IOException, UnknownHostException {
        return createSocket((Socket) null, str, i, true);
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException, UnknownHostException {
        return createSocket((Socket) null, str, i, true);
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return createSocket((Socket) null, inetAddress.getHostName(), i, true);
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return createSocket((Socket) null, inetAddress.getHostName(), i, true);
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException, UnknownHostException {
        Socket socket2 = new Socket(this.tunnelHost, this.tunnelPort);
        doTunnelHandshake(socket2, str, i);
        SSLSocket sSLSocket = (SSLSocket) this.dfactory.createSocket(socket2, str, i, z);
        sSLSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                ZLog.d(SSLTunnelSocketFactory.TAG, "Handshake finished!");
                ZLog.d(SSLTunnelSocketFactory.TAG, "\t CipherSuite:" + handshakeCompletedEvent.getCipherSuite());
                ZLog.d(SSLTunnelSocketFactory.TAG, "\t SessionId " + handshakeCompletedEvent.getSession());
                ZLog.d(SSLTunnelSocketFactory.TAG, "\t PeerHost " + handshakeCompletedEvent.getSession().getPeerHost());
            }
        });
        sSLSocket.startHandshake();
        return sSLSocket;
    }

    private void doTunnelHandshake(Socket socket, String str, int i) throws IOException {
        byte[] bArr;
        String str2;
        OutputStream outputStream = socket.getOutputStream();
        String str3 = "CONNECT " + str + ":" + i + " HTTP/1.1\n" + "User-Agent: " + this.useragent + "\n" + "Host:" + str + "\r\n\r\n";
        try {
            bArr = str3.getBytes("ASCII7");
        } catch (UnsupportedEncodingException unused) {
            bArr = str3.getBytes();
        }
        outputStream.write(bArr);
        outputStream.flush();
        byte[] bArr2 = new byte[200];
        InputStream inputStream = socket.getInputStream();
        int i2 = 0;
        int i3 = 0;
        boolean z = false;
        while (i2 < 2) {
            int read = inputStream.read();
            if (read < 0) {
                throw new IOException("Unexpected EOF from proxy");
            } else if (read == 10) {
                i2++;
                z = true;
            } else if (read != 13) {
                if (!z && i3 < bArr2.length) {
                    bArr2[i3] = (byte) read;
                    i3++;
                }
                i2 = 0;
            }
        }
        try {
            str2 = new String(bArr2, 0, i3, "ASCII7");
        } catch (UnsupportedEncodingException unused2) {
            str2 = new String(bArr2, 0, i3);
        }
        if (str2.toLowerCase().indexOf("200 connection established") == -1) {
            throw new IOException("Unable to tunnel through " + this.tunnelHost + ":" + this.tunnelPort + ".  Proxy returns \"" + str2 + "\"");
        }
    }

    public String[] getDefaultCipherSuites() {
        return this.dfactory.getDefaultCipherSuites();
    }

    public String[] getSupportedCipherSuites() {
        return this.dfactory.getSupportedCipherSuites();
    }
}
