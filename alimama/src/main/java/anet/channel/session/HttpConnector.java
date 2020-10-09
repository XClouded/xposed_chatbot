package anet.channel.session;

import android.os.Build;
import android.util.Pair;
import anet.channel.RequestCb;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpSslUtil;
import anet.channel.util.ProxySetting;
import anet.channel.util.StringUtils;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpConnector {
    private static final String TAG = "awcn.HttpConnector";

    public static class Response {
        public int contentLength;
        public Map<String, List<String>> header;
        public int httpCode;
        public boolean isGZip;
        public byte[] out;
    }

    private HttpConnector() {
    }

    public static Response connect(Request request) {
        return connectImpl(request, (RequestCb) null);
    }

    public static void connect(Request request, RequestCb requestCb) {
        connectImpl(request, requestCb);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        anet.channel.util.ALog.e(TAG, "redirect url is invalid!", r4.getSeq(), "redirect url", r5);
     */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x02f4 A[SYNTHETIC, Splitter:B:107:0x02f4] */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x0311 A[SYNTHETIC, Splitter:B:116:0x0311] */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0350 A[SYNTHETIC, Splitter:B:127:0x0350] */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x0382 A[SYNTHETIC, Splitter:B:136:0x0382] */
    /* JADX WARNING: Removed duplicated region for block: B:145:0x03a2 A[SYNTHETIC, Splitter:B:145:0x03a2] */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x03c2 A[SYNTHETIC, Splitter:B:154:0x03c2] */
    /* JADX WARNING: Removed duplicated region for block: B:163:0x03e1 A[SYNTHETIC, Splitter:B:163:0x03e1] */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x040c A[SYNTHETIC, Splitter:B:172:0x040c] */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x0412 A[SYNTHETIC, Splitter:B:176:0x0412] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x023e A[Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0258 A[Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }] */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0263 A[SYNTHETIC, Splitter:B:68:0x0263] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x0294 A[Catch:{ all -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0299 A[Catch:{ all -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x02a3 A[Catch:{ all -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x02b4 A[Catch:{ all -> 0x0268 }] */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x02c8 A[SYNTHETIC, Splitter:B:98:0x02c8] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static anet.channel.session.HttpConnector.Response connectImpl(anet.channel.request.Request r17, anet.channel.RequestCb r18) {
        /*
            r1 = r18
            anet.channel.session.HttpConnector$Response r2 = new anet.channel.session.HttpConnector$Response
            r2.<init>()
            r3 = 0
            if (r17 == 0) goto L_0x0422
            java.net.URL r5 = r17.getUrl()
            if (r5 != 0) goto L_0x0012
            goto L_0x0422
        L_0x0012:
            r4 = r17
            r5 = r3
        L_0x0015:
            boolean r6 = anet.channel.status.NetworkStatusHelper.isConnected()
            if (r6 != 0) goto L_0x0022
            r5 = -200(0xffffffffffffff38, float:NaN)
            onException(r4, r2, r1, r5, r3)
            goto L_0x040f
        L_0x0022:
            r6 = -402(0xfffffffffffffe6e, float:NaN)
            r7 = 3
            r8 = 2
            r9 = 1
            r10 = 0
            java.net.HttpURLConnection r11 = getConnection(r4)     // Catch:{ UnknownHostException -> 0x03e5, SocketTimeoutException -> 0x03c6, ConnectTimeoutException -> 0x03a7, ConnectException -> 0x0387, SSLHandshakeException -> 0x0355, SSLException -> 0x0323, CancellationException -> 0x02f9, IOException -> 0x02cd, Exception -> 0x028a, all -> 0x0285 }
            boolean r5 = anet.channel.util.ALog.isPrintLog(r8)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r5 == 0) goto L_0x007f
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r12 = ""
            java.lang.String r13 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r14 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = "request URL"
            r14[r10] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.net.URL r15 = r11.getURL()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = r15.toString()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r14[r9] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.i(r5, r12, r13, r14)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r12 = ""
            java.lang.String r13 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r14 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = "request Method"
            r14[r10] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = r11.getRequestMethod()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r14[r9] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.i(r5, r12, r13, r14)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r12 = ""
            java.lang.String r13 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r14 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = "request headers"
            r14[r10] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map r15 = r11.getRequestProperties()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = r15.toString()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r14[r9] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.i(r5, r12, r13, r14)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x007f:
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.sendStart = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r12 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = r12.sendStart     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r14 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r14 = r14.start     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r16 = 0
            long r12 = r12 - r14
            r5.processTime = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r11.connect()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            postData(r11, r4)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.sendEnd = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r12 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = r12.sendEnd     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r14 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r14 = r14.sendStart     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r16 = 0
            long r12 = r12 - r14
            r5.sendDataTime = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r5 = r11.getResponseCode()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r2.httpCode = r5     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map r5 = r11.getHeaderFields()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map r5 = anet.channel.util.HttpHelper.cloneMap(r5)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r2.header = r5     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r12 = ""
            java.lang.String r13 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r14 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = "response code"
            r14[r10] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r15 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Integer r15 = java.lang.Integer.valueOf(r15)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r14[r9] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.i(r5, r12, r13, r14)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r12 = ""
            java.lang.String r13 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r14 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r15 = "response headers"
            r14[r10] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r15 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r14[r9] = r15     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.i(r5, r12, r13, r14)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            boolean r5 = anet.channel.util.HttpHelper.checkRedirect(r4, r5)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r5 == 0) goto L_0x0195
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r5 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r12 = "Location"
            java.lang.String r5 = anet.channel.util.HttpHelper.getSingleHeaderFieldByKey(r5, r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r5 == 0) goto L_0x0195
            anet.channel.util.HttpUrl r12 = anet.channel.util.HttpUrl.parse(r5)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r12 == 0) goto L_0x0182
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r13 = "redirect"
            java.lang.String r14 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r15 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r16 = "to url"
            r15[r10] = r16     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r16 = r12.toString()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r15[r9] = r16     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.i(r5, r13, r14, r15)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.request.Request$Builder r5 = r4.newBuilder()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r13 = "GET"
            anet.channel.request.Request$Builder r5 = r5.setMethod(r13)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.request.Request$Builder r5 = r5.setBody(r3)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.request.Request$Builder r5 = r5.setUrl((anet.channel.util.HttpUrl) r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r13 = r4.getRedirectTimes()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r13 = r13 + r9
            anet.channel.request.Request$Builder r5 = r5.setRedirectTimes(r13)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.request.Request$Builder r5 = r5.setSslSocketFactory(r3)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.request.Request$Builder r5 = r5.setHostnameVerifier(r3)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.request.Request r5 = r5.build()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r4 = r5.rs     // Catch:{ UnknownHostException -> 0x017f, SocketTimeoutException -> 0x017c, ConnectTimeoutException -> 0x0179, ConnectException -> 0x0176, SSLHandshakeException -> 0x0173, SSLException -> 0x0170, CancellationException -> 0x016d, IOException -> 0x016a, Exception -> 0x0167 }
            int r13 = r2.httpCode     // Catch:{ UnknownHostException -> 0x017f, SocketTimeoutException -> 0x017c, ConnectTimeoutException -> 0x0179, ConnectException -> 0x0176, SSLHandshakeException -> 0x0173, SSLException -> 0x0170, CancellationException -> 0x016d, IOException -> 0x016a, Exception -> 0x0167 }
            java.lang.String r12 = r12.simpleUrlString()     // Catch:{ UnknownHostException -> 0x017f, SocketTimeoutException -> 0x017c, ConnectTimeoutException -> 0x0179, ConnectException -> 0x0176, SSLHandshakeException -> 0x0173, SSLException -> 0x0170, CancellationException -> 0x016d, IOException -> 0x016a, Exception -> 0x0167 }
            r4.recordRedirect(r13, r12)     // Catch:{ UnknownHostException -> 0x017f, SocketTimeoutException -> 0x017c, ConnectTimeoutException -> 0x0179, ConnectException -> 0x0176, SSLHandshakeException -> 0x0173, SSLException -> 0x0170, CancellationException -> 0x016d, IOException -> 0x016a, Exception -> 0x0167 }
            if (r11 == 0) goto L_0x0163
            r11.disconnect()     // Catch:{ Exception -> 0x0158 }
            goto L_0x0163
        L_0x0158:
            r0 = move-exception
            r4 = r0
            java.lang.String r6 = "awcn.HttpConnector"
            java.lang.String r7 = "http disconnect"
            java.lang.Object[] r8 = new java.lang.Object[r10]
            anet.channel.util.ALog.e(r6, r7, r3, r4, r8)
        L_0x0163:
            r4 = r5
            r5 = r11
            goto L_0x0015
        L_0x0167:
            r0 = move-exception
            goto L_0x028d
        L_0x016a:
            r0 = move-exception
            goto L_0x02d0
        L_0x016d:
            r0 = move-exception
            goto L_0x02fc
        L_0x0170:
            r0 = move-exception
            goto L_0x0326
        L_0x0173:
            r0 = move-exception
            goto L_0x0358
        L_0x0176:
            r0 = move-exception
            goto L_0x038a
        L_0x0179:
            r0 = move-exception
            goto L_0x03aa
        L_0x017c:
            r0 = move-exception
            goto L_0x03c9
        L_0x017f:
            r0 = move-exception
            goto L_0x03e8
        L_0x0182:
            java.lang.String r12 = "awcn.HttpConnector"
            java.lang.String r13 = "redirect url is invalid!"
            java.lang.String r14 = r4.getSeq()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.Object[] r15 = new java.lang.Object[r8]     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r16 = "redirect url"
            r15[r10] = r16     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r15[r9] = r5     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.util.ALog.e(r12, r13, r14, r15)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x0195:
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r12 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r13 = "Content-Encoding"
            java.lang.String r12 = anet.channel.util.HttpHelper.getSingleHeaderFieldByKey(r12, r13)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.contentEncoding = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r12 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r13 = "Content-Type"
            java.lang.String r12 = anet.channel.util.HttpHelper.getSingleHeaderFieldByKey(r12, r13)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.contentType = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r5 = "HEAD"
            java.lang.String r12 = r4.getMethod()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            boolean r5 = r5.equals(r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r5 != 0) goto L_0x0214
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r12 = 304(0x130, float:4.26E-43)
            if (r5 == r12) goto L_0x0214
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r12 = 204(0xcc, float:2.86E-43)
            if (r5 == r12) goto L_0x0214
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r12 = 100
            if (r5 < r12) goto L_0x01d2
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r12 = 200(0xc8, float:2.8E-43)
            if (r5 >= r12) goto L_0x01d2
            goto L_0x0214
        L_0x01d2:
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r5 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r5 = anet.channel.util.HttpHelper.parseContentLength(r5)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r2.contentLength = r5     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r12 = r2.contentLength     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = (long) r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.contentLength = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r5 = "gzip"
            anet.channel.statist.RequestStatistic r12 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r12 = r12.contentEncoding     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            boolean r5 = r5.equalsIgnoreCase(r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r2.isGZip = r5     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            boolean r5 = r2.isGZip     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r5 == 0) goto L_0x01ff
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r5 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r12 = "Content-Encoding"
            r5.remove(r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r5 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r12 = "Content-Length"
            r5.remove(r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x01ff:
            if (r1 == 0) goto L_0x0208
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r12 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r1.onResponseCode(r5, r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x0208:
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.rspStart = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            parseBody(r11, r4, r2, r1)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            goto L_0x0225
        L_0x0214:
            if (r1 == 0) goto L_0x021d
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.Map<java.lang.String, java.util.List<java.lang.String>> r12 = r2.header     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r1.onResponseCode(r5, r12)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x021d:
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.rspStart = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x0225:
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r12 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = r12.rspStart     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r14 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r14 = r14.sendEnd     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r16 = 0
            long r12 = r12 - r14
            r5.firstDataTime = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.util.concurrent.atomic.AtomicBoolean r5 = r5.isDone     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            boolean r5 = r5.get()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            if (r5 != 0) goto L_0x0256
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.ret = r9     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            int r12 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.statusCode = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r12 = "SUCCESS"
            r5.msg = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            anet.channel.statist.RequestStatistic r5 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r5.rspEnd = r12     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x0256:
            if (r1 == 0) goto L_0x0261
            int r5 = r2.httpCode     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            java.lang.String r12 = "SUCCESS"
            anet.channel.statist.RequestStatistic r13 = r4.rs     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
            r1.onFinish(r5, r12, r13)     // Catch:{ UnknownHostException -> 0x0282, SocketTimeoutException -> 0x027f, ConnectTimeoutException -> 0x027c, ConnectException -> 0x0279, SSLHandshakeException -> 0x0276, SSLException -> 0x0273, CancellationException -> 0x0270, IOException -> 0x026e, Exception -> 0x026c }
        L_0x0261:
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x0268:
            r0 = move-exception
            r1 = r0
            goto L_0x0410
        L_0x026c:
            r0 = move-exception
            goto L_0x028c
        L_0x026e:
            r0 = move-exception
            goto L_0x02cf
        L_0x0270:
            r0 = move-exception
            goto L_0x02fb
        L_0x0273:
            r0 = move-exception
            goto L_0x0325
        L_0x0276:
            r0 = move-exception
            goto L_0x0357
        L_0x0279:
            r0 = move-exception
            goto L_0x0389
        L_0x027c:
            r0 = move-exception
            goto L_0x03a9
        L_0x027f:
            r0 = move-exception
            goto L_0x03c8
        L_0x0282:
            r0 = move-exception
            goto L_0x03e7
        L_0x0285:
            r0 = move-exception
            r1 = r0
            r11 = r5
            goto L_0x0410
        L_0x028a:
            r0 = move-exception
            r11 = r5
        L_0x028c:
            r5 = r4
        L_0x028d:
            r4 = r0
            java.lang.String r6 = r4.getMessage()     // Catch:{ all -> 0x0268 }
            if (r6 == 0) goto L_0x0299
            java.lang.String r6 = r4.getMessage()     // Catch:{ all -> 0x0268 }
            goto L_0x029b
        L_0x0299:
            java.lang.String r6 = ""
        L_0x029b:
            java.lang.String r7 = "not verified"
            boolean r6 = r6.contains(r7)     // Catch:{ all -> 0x0268 }
            if (r6 == 0) goto L_0x02b4
            anet.channel.strategy.SchemeGuesser r6 = anet.channel.strategy.SchemeGuesser.getInstance()     // Catch:{ all -> 0x0268 }
            java.lang.String r7 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r6.onSslFail(r7)     // Catch:{ all -> 0x0268 }
            r6 = -403(0xfffffffffffffe6d, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            goto L_0x02b9
        L_0x02b4:
            r6 = -101(0xffffffffffffff9b, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
        L_0x02b9:
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "HTTP Exception"
            java.lang.String r5 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r5, r4, r7)     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x02cd:
            r0 = move-exception
            r11 = r5
        L_0x02cf:
            r5 = r4
        L_0x02d0:
            r4 = r0
            r6 = -404(0xfffffffffffffe6c, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "IO Exception"
            java.lang.String r12 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0268 }
            java.lang.String r13 = "host"
            r7[r10] = r13     // Catch:{ all -> 0x0268 }
            java.lang.String r5 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r7[r9] = r5     // Catch:{ all -> 0x0268 }
            r7[r8] = r4     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r12, r7)     // Catch:{ all -> 0x0268 }
            anet.channel.status.NetworkStatusHelper.printNetworkDetail()     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x02f9:
            r0 = move-exception
            r11 = r5
        L_0x02fb:
            r5 = r4
        L_0x02fc:
            r4 = r0
            r6 = -204(0xffffffffffffff34, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "HTTP Request Cancel"
            java.lang.String r5 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r5, r4, r7)     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x0316:
            r0 = move-exception
            r1 = r0
            java.lang.String r4 = "awcn.HttpConnector"
            java.lang.String r5 = "http disconnect"
            java.lang.Object[] r6 = new java.lang.Object[r10]
            anet.channel.util.ALog.e(r4, r5, r3, r1, r6)
            goto L_0x040f
        L_0x0323:
            r0 = move-exception
            r11 = r5
        L_0x0325:
            r5 = r4
        L_0x0326:
            r4 = r0
            anet.channel.strategy.SchemeGuesser r12 = anet.channel.strategy.SchemeGuesser.getInstance()     // Catch:{ all -> 0x0268 }
            java.lang.String r13 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r12.onSslFail(r13)     // Catch:{ all -> 0x0268 }
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "connect SSLException"
            java.lang.String r12 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0268 }
            java.lang.String r13 = "host"
            r7[r10] = r13     // Catch:{ all -> 0x0268 }
            java.lang.String r5 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r7[r9] = r5     // Catch:{ all -> 0x0268 }
            r7[r8] = r4     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r12, r7)     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x0355:
            r0 = move-exception
            r11 = r5
        L_0x0357:
            r5 = r4
        L_0x0358:
            r4 = r0
            anet.channel.strategy.SchemeGuesser r12 = anet.channel.strategy.SchemeGuesser.getInstance()     // Catch:{ all -> 0x0268 }
            java.lang.String r13 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r12.onSslFail(r13)     // Catch:{ all -> 0x0268 }
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "HTTP Connect SSLHandshakeException"
            java.lang.String r12 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0268 }
            java.lang.String r13 = "host"
            r7[r10] = r13     // Catch:{ all -> 0x0268 }
            java.lang.String r5 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r7[r9] = r5     // Catch:{ all -> 0x0268 }
            r7[r8] = r4     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r12, r7)     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x0387:
            r0 = move-exception
            r11 = r5
        L_0x0389:
            r5 = r4
        L_0x038a:
            r4 = r0
            r6 = -406(0xfffffffffffffe6a, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "HTTP Connect Exception"
            java.lang.String r5 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r5, r4, r7)     // Catch:{ all -> 0x0268 }
            anet.channel.status.NetworkStatusHelper.printNetworkDetail()     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x03a7:
            r0 = move-exception
            r11 = r5
        L_0x03a9:
            r5 = r4
        L_0x03aa:
            r4 = r0
            r6 = -400(0xfffffffffffffe70, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "HTTP Connect Timeout"
            java.lang.String r5 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r5, r4, r7)     // Catch:{ all -> 0x0268 }
            anet.channel.status.NetworkStatusHelper.printNetworkDetail()     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x03c6:
            r0 = move-exception
            r11 = r5
        L_0x03c8:
            r5 = r4
        L_0x03c9:
            r4 = r0
            r6 = -401(0xfffffffffffffe6f, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "HTTP Socket Timeout"
            java.lang.String r5 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r10]     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r5, r4, r7)     // Catch:{ all -> 0x0268 }
            anet.channel.status.NetworkStatusHelper.printNetworkDetail()     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
            goto L_0x040f
        L_0x03e5:
            r0 = move-exception
            r11 = r5
        L_0x03e7:
            r5 = r4
        L_0x03e8:
            r4 = r0
            r6 = -405(0xfffffffffffffe6b, float:NaN)
            onException(r5, r2, r1, r6, r4)     // Catch:{ all -> 0x0268 }
            java.lang.String r1 = "awcn.HttpConnector"
            java.lang.String r6 = "Unknown Host Exception"
            java.lang.String r12 = r5.getSeq()     // Catch:{ all -> 0x0268 }
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0268 }
            java.lang.String r13 = "host"
            r7[r10] = r13     // Catch:{ all -> 0x0268 }
            java.lang.String r5 = r5.getHost()     // Catch:{ all -> 0x0268 }
            r7[r9] = r5     // Catch:{ all -> 0x0268 }
            r7[r8] = r4     // Catch:{ all -> 0x0268 }
            anet.channel.util.ALog.e(r1, r6, r12, r7)     // Catch:{ all -> 0x0268 }
            anet.channel.status.NetworkStatusHelper.printNetworkDetail()     // Catch:{ all -> 0x0268 }
            if (r11 == 0) goto L_0x040f
            r11.disconnect()     // Catch:{ Exception -> 0x0316 }
        L_0x040f:
            return r2
        L_0x0410:
            if (r11 == 0) goto L_0x0421
            r11.disconnect()     // Catch:{ Exception -> 0x0416 }
            goto L_0x0421
        L_0x0416:
            r0 = move-exception
            r2 = r0
            java.lang.Object[] r4 = new java.lang.Object[r10]
            java.lang.String r5 = "awcn.HttpConnector"
            java.lang.String r6 = "http disconnect"
            anet.channel.util.ALog.e(r5, r6, r3, r2, r4)
        L_0x0421:
            throw r1
        L_0x0422:
            if (r1 == 0) goto L_0x0432
            r4 = -102(0xffffffffffffff9a, float:NaN)
            java.lang.String r5 = anet.channel.util.ErrorConstant.getErrMsg(r4)
            anet.channel.statist.RequestStatistic r6 = new anet.channel.statist.RequestStatistic
            r6.<init>(r3, r3)
            r1.onFinish(r4, r5, r6)
        L_0x0432:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.HttpConnector.connectImpl(anet.channel.request.Request, anet.channel.RequestCb):anet.channel.session.HttpConnector$Response");
    }

    private static void onException(Request request, Response response, RequestCb requestCb, int i, Throwable th) {
        String errMsg = ErrorConstant.getErrMsg(i);
        ALog.e(TAG, "onException", request.getSeq(), "errorCode", Integer.valueOf(i), IWXUserTrackAdapter.MONITOR_ERROR_MSG, errMsg, "url", request.getUrlString(), "host", request.getHost());
        if (response != null) {
            response.httpCode = i;
        }
        if (!request.rs.isDone.get()) {
            request.rs.statusCode = i;
            request.rs.msg = errMsg;
            request.rs.rspEnd = System.currentTimeMillis();
            if (i != -204) {
                AppMonitor.getInstance().commitStat(new ExceptionStatistic(i, errMsg, request.rs, th));
            }
        }
        if (requestCb != null) {
            requestCb.onFinish(i, errMsg, request.rs);
        }
    }

    private static HttpURLConnection getConnection(Request request) throws IOException {
        HttpURLConnection httpURLConnection;
        Pair<String, Integer> wifiProxy = NetworkStatusHelper.getWifiProxy();
        Proxy proxy = wifiProxy != null ? new Proxy(Proxy.Type.HTTP, new InetSocketAddress((String) wifiProxy.first, ((Integer) wifiProxy.second).intValue())) : null;
        ProxySetting proxySetting = ProxySetting.get();
        if (NetworkStatusHelper.getStatus().isMobile() && proxySetting != null) {
            proxy = proxySetting.getProxy();
        }
        URL url = request.getUrl();
        if (proxy != null) {
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpURLConnection.setConnectTimeout(request.getConnectTimeout());
        httpURLConnection.setReadTimeout(request.getReadTimeout());
        httpURLConnection.setRequestMethod(request.getMethod());
        if (request.containsBody()) {
            httpURLConnection.setDoOutput(true);
        }
        Map<String, String> headers = request.getHeaders();
        for (Map.Entry next : headers.entrySet()) {
            httpURLConnection.addRequestProperty((String) next.getKey(), (String) next.getValue());
        }
        String str = headers.get(HttpConstant.HOST);
        if (str == null) {
            str = request.getHost();
        }
        String concatString = request.getHttpUrl().containsNonDefaultPort() ? StringUtils.concatString(str, ":", String.valueOf(request.getHttpUrl().getPort())) : str;
        httpURLConnection.setRequestProperty(HttpConstant.HOST, concatString);
        if (NetworkStatusHelper.getApn().equals("cmwap")) {
            httpURLConnection.setRequestProperty(HttpConstant.X_ONLINE_HOST, concatString);
        }
        if (!headers.containsKey(HttpConstant.ACCEPT_ENCODING)) {
            httpURLConnection.setRequestProperty(HttpConstant.ACCEPT_ENCODING, "gzip");
        }
        if (proxySetting != null) {
            httpURLConnection.setRequestProperty(HttpConstant.AUTHORIZATION, proxySetting.getBasicAuthorization());
        }
        if (url.getProtocol().equalsIgnoreCase("https")) {
            supportHttps(httpURLConnection, request, str);
        }
        httpURLConnection.setInstanceFollowRedirects(false);
        return httpURLConnection;
    }

    private static void supportHttps(HttpURLConnection httpURLConnection, Request request, final String str) {
        if (Integer.parseInt(Build.VERSION.SDK) < 8) {
            ALog.e(TAG, "supportHttps", "[supportHttps]Froyo 以下版本不支持https", new Object[0]);
            return;
        }
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
        if (request.getSslSocketFactory() != null) {
            httpsURLConnection.setSSLSocketFactory(request.getSslSocketFactory());
        } else if (HttpSslUtil.getSSLSocketFactory() != null) {
            httpsURLConnection.setSSLSocketFactory(HttpSslUtil.getSSLSocketFactory());
        }
        if (request.getHostnameVerifier() != null) {
            httpsURLConnection.setHostnameVerifier(request.getHostnameVerifier());
        } else if (HttpSslUtil.getHostnameVerifier() != null) {
            httpsURLConnection.setHostnameVerifier(HttpSslUtil.getHostnameVerifier());
        } else {
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String str, SSLSession sSLSession) {
                    return HttpsURLConnection.getDefaultHostnameVerifier().verify(str, sSLSession);
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0045 A[SYNTHETIC, Splitter:B:23:0x0045] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006a A[SYNTHETIC, Splitter:B:29:0x006a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int postData(java.net.HttpURLConnection r7, anet.channel.request.Request r8) {
        /*
            boolean r0 = r8.containsBody()
            r1 = 0
            if (r0 == 0) goto L_0x0080
            r0 = 0
            java.io.OutputStream r7 = r7.getOutputStream()     // Catch:{ Exception -> 0x0035 }
            int r0 = r8.postBody(r7)     // Catch:{ Exception -> 0x002e, all -> 0x0029 }
            if (r7 == 0) goto L_0x0027
            r7.flush()     // Catch:{ IOException -> 0x0019 }
            r7.close()     // Catch:{ IOException -> 0x0019 }
            goto L_0x0027
        L_0x0019:
            r7 = move-exception
            java.lang.String r2 = "awcn.HttpConnector"
            java.lang.String r3 = "postData"
            java.lang.String r4 = r8.getSeq()
            java.lang.Object[] r1 = new java.lang.Object[r1]
            anet.channel.util.ALog.e(r2, r3, r4, r7, r1)
        L_0x0027:
            r1 = r0
            goto L_0x005a
        L_0x0029:
            r0 = move-exception
            r6 = r0
            r0 = r7
            r7 = r6
            goto L_0x0068
        L_0x002e:
            r0 = move-exception
            r6 = r0
            r0 = r7
            r7 = r6
            goto L_0x0036
        L_0x0033:
            r7 = move-exception
            goto L_0x0068
        L_0x0035:
            r7 = move-exception
        L_0x0036:
            java.lang.String r2 = "awcn.HttpConnector"
            java.lang.String r3 = "postData error"
            java.lang.String r4 = r8.getSeq()     // Catch:{ all -> 0x0033 }
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ all -> 0x0033 }
            anet.channel.util.ALog.e(r2, r3, r4, r7, r5)     // Catch:{ all -> 0x0033 }
            if (r0 == 0) goto L_0x005a
            r0.flush()     // Catch:{ IOException -> 0x004c }
            r0.close()     // Catch:{ IOException -> 0x004c }
            goto L_0x005a
        L_0x004c:
            r7 = move-exception
            java.lang.String r0 = "awcn.HttpConnector"
            java.lang.String r2 = "postData"
            java.lang.String r3 = r8.getSeq()
            java.lang.Object[] r4 = new java.lang.Object[r1]
            anet.channel.util.ALog.e(r0, r2, r3, r7, r4)
        L_0x005a:
            anet.channel.statist.RequestStatistic r7 = r8.rs
            long r2 = (long) r1
            r7.reqBodyInflateSize = r2
            anet.channel.statist.RequestStatistic r7 = r8.rs
            r7.reqBodyDeflateSize = r2
            anet.channel.statist.RequestStatistic r7 = r8.rs
            r7.sendDataSize = r2
            goto L_0x0080
        L_0x0068:
            if (r0 == 0) goto L_0x007f
            r0.flush()     // Catch:{ IOException -> 0x0071 }
            r0.close()     // Catch:{ IOException -> 0x0071 }
            goto L_0x007f
        L_0x0071:
            r0 = move-exception
            java.lang.String r8 = r8.getSeq()
            java.lang.Object[] r1 = new java.lang.Object[r1]
            java.lang.String r2 = "awcn.HttpConnector"
            java.lang.String r3 = "postData"
            anet.channel.util.ALog.e(r2, r3, r8, r0, r1)
        L_0x007f:
            throw r7
        L_0x0080:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.HttpConnector.postData(java.net.HttpURLConnection, anet.channel.request.Request):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:60:0x00fb A[SYNTHETIC, Splitter:B:60:0x00fb] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void parseBody(java.net.HttpURLConnection r12, anet.channel.request.Request r13, anet.channel.session.HttpConnector.Response r14, anet.channel.RequestCb r15) throws java.io.IOException, java.util.concurrent.CancellationException {
        /*
            java.net.URL r0 = r12.getURL()
            r0.toString()
            r0 = 1
            r1 = 2
            r2 = 0
            r3 = 0
            java.io.InputStream r4 = r12.getInputStream()     // Catch:{ IOException -> 0x0011 }
            r12 = r4
            goto L_0x0041
        L_0x0011:
            r4 = move-exception
            boolean r4 = r4 instanceof java.io.FileNotFoundException
            if (r4 == 0) goto L_0x002d
            java.lang.String r4 = "awcn.HttpConnector"
            java.lang.String r5 = "File not found"
            java.lang.String r6 = r13.getSeq()
            java.lang.Object[] r7 = new java.lang.Object[r1]
            java.lang.String r8 = "url"
            r7[r2] = r8
            java.lang.String r8 = r13.getUrlString()
            r7[r0] = r8
            anet.channel.util.ALog.w(r4, r5, r6, r7)
        L_0x002d:
            java.io.InputStream r12 = r12.getErrorStream()     // Catch:{ Exception -> 0x0032 }
            goto L_0x0041
        L_0x0032:
            r12 = move-exception
            java.lang.String r4 = "awcn.HttpConnector"
            java.lang.String r5 = "get error stream failed."
            java.lang.String r6 = r13.getSeq()
            java.lang.Object[] r7 = new java.lang.Object[r2]
            anet.channel.util.ALog.e(r4, r5, r6, r12, r7)
            r12 = r3
        L_0x0041:
            if (r12 != 0) goto L_0x0049
            r12 = -404(0xfffffffffffffe6c, float:NaN)
            onException(r13, r14, r15, r12, r3)
            return
        L_0x0049:
            if (r15 != 0) goto L_0x0063
            int r4 = r14.contentLength
            if (r4 > 0) goto L_0x0052
            r1 = 1024(0x400, float:1.435E-42)
            goto L_0x005d
        L_0x0052:
            boolean r4 = r14.isGZip
            if (r4 == 0) goto L_0x005b
            int r4 = r14.contentLength
            int r1 = r4 * 2
            goto L_0x005d
        L_0x005b:
            int r1 = r14.contentLength
        L_0x005d:
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream
            r4.<init>(r1)
            goto L_0x0064
        L_0x0063:
            r4 = r3
        L_0x0064:
            anet.channel.util.ByteCounterInputStream r1 = new anet.channel.util.ByteCounterInputStream     // Catch:{ all -> 0x00e2 }
            r1.<init>(r12)     // Catch:{ all -> 0x00e2 }
            boolean r5 = r14.isGZip     // Catch:{ all -> 0x00e0 }
            if (r5 == 0) goto L_0x0074
            java.util.zip.GZIPInputStream r5 = new java.util.zip.GZIPInputStream     // Catch:{ all -> 0x00e0 }
            r5.<init>(r1)     // Catch:{ all -> 0x00e0 }
            r12 = r5
            goto L_0x0075
        L_0x0074:
            r12 = r1
        L_0x0075:
            r5 = r3
        L_0x0076:
            java.lang.Thread r6 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x00e0 }
            boolean r6 = r6.isInterrupted()     // Catch:{ all -> 0x00e0 }
            if (r6 != 0) goto L_0x00d8
            if (r5 != 0) goto L_0x008c
            anet.channel.bytes.ByteArrayPool r5 = anet.channel.bytes.ByteArrayPool.getInstance()     // Catch:{ all -> 0x00e0 }
            r6 = 2048(0x800, float:2.87E-42)
            anet.channel.bytes.ByteArray r5 = r5.retrieve(r6)     // Catch:{ all -> 0x00e0 }
        L_0x008c:
            int r6 = r5.readFrom(r12)     // Catch:{ all -> 0x00e0 }
            r7 = -1
            if (r6 == r7) goto L_0x00ae
            if (r4 == 0) goto L_0x0099
            r5.writeTo(r4)     // Catch:{ all -> 0x00e0 }
            goto L_0x009d
        L_0x0099:
            r15.onDataReceive(r5, r2)     // Catch:{ all -> 0x00e0 }
            r5 = r3
        L_0x009d:
            anet.channel.statist.RequestStatistic r7 = r13.rs     // Catch:{ all -> 0x00e0 }
            long r8 = r7.recDataSize     // Catch:{ all -> 0x00e0 }
            long r10 = (long) r6     // Catch:{ all -> 0x00e0 }
            long r8 = r8 + r10
            r7.recDataSize = r8     // Catch:{ all -> 0x00e0 }
            anet.channel.statist.RequestStatistic r6 = r13.rs     // Catch:{ all -> 0x00e0 }
            long r7 = r6.rspBodyInflateSize     // Catch:{ all -> 0x00e0 }
            r9 = 0
            long r7 = r7 + r10
            r6.rspBodyInflateSize = r7     // Catch:{ all -> 0x00e0 }
            goto L_0x0076
        L_0x00ae:
            if (r4 == 0) goto L_0x00b4
            r5.recycle()     // Catch:{ all -> 0x00e0 }
            goto L_0x00b7
        L_0x00b4:
            r15.onDataReceive(r5, r0)     // Catch:{ all -> 0x00e0 }
        L_0x00b7:
            if (r4 == 0) goto L_0x00bf
            byte[] r15 = r4.toByteArray()     // Catch:{ all -> 0x00e0 }
            r14.out = r15     // Catch:{ all -> 0x00e0 }
        L_0x00bf:
            anet.channel.statist.RequestStatistic r14 = r13.rs
            long r2 = java.lang.System.currentTimeMillis()
            anet.channel.statist.RequestStatistic r15 = r13.rs
            long r4 = r15.rspStart
            long r2 = r2 - r4
            r14.recDataTime = r2
            anet.channel.statist.RequestStatistic r13 = r13.rs
            long r14 = r1.getReadByteCount()
            r13.rspBodyDeflateSize = r14
            r12.close()     // Catch:{ IOException -> 0x00d7 }
        L_0x00d7:
            return
        L_0x00d8:
            java.util.concurrent.CancellationException r14 = new java.util.concurrent.CancellationException     // Catch:{ all -> 0x00e0 }
            java.lang.String r15 = "task cancelled"
            r14.<init>(r15)     // Catch:{ all -> 0x00e0 }
            throw r14     // Catch:{ all -> 0x00e0 }
        L_0x00e0:
            r14 = move-exception
            goto L_0x00e4
        L_0x00e2:
            r14 = move-exception
            r1 = r3
        L_0x00e4:
            anet.channel.statist.RequestStatistic r15 = r13.rs
            long r2 = java.lang.System.currentTimeMillis()
            anet.channel.statist.RequestStatistic r0 = r13.rs
            long r4 = r0.rspStart
            long r2 = r2 - r4
            r15.recDataTime = r2
            anet.channel.statist.RequestStatistic r13 = r13.rs
            long r0 = r1.getReadByteCount()
            r13.rspBodyDeflateSize = r0
            if (r12 == 0) goto L_0x00fe
            r12.close()     // Catch:{ IOException -> 0x00fe }
        L_0x00fe:
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.HttpConnector.parseBody(java.net.HttpURLConnection, anet.channel.request.Request, anet.channel.session.HttpConnector$Response, anet.channel.RequestCb):void");
    }
}
