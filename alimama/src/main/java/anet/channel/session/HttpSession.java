package anet.channel.session;

import android.content.Context;
import anet.channel.AwcnConfig;
import anet.channel.Session;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.Event;
import anet.channel.request.Request;
import anet.channel.session.HttpConnector;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.Inet64Util;
import anet.channel.util.TlsSniSocketFactory;
import anet.channel.util.Utils;
import javax.net.ssl.SSLSocketFactory;

public class HttpSession extends Session {
    private static final String TAG = "awcn.HttpSession";
    private SSLSocketFactory sslSocketFactory;

    /* access modifiers changed from: protected */
    public Runnable getRecvTimeOutRunnable() {
        return null;
    }

    public HttpSession(Context context, ConnInfo connInfo) {
        super(context, connInfo);
        if (this.mConnStrategy == null) {
            this.mConnType = (this.mHost == null || !this.mHost.startsWith("https")) ? ConnType.HTTP : ConnType.HTTPS;
        } else if (AwcnConfig.isHttpsSniEnable() && this.mConnType.equals(ConnType.HTTPS)) {
            this.sslSocketFactory = new TlsSniSocketFactory(this.mRealHost);
        }
    }

    public boolean isAvailable() {
        return this.mStatus == 4;
    }

    public void connect() {
        try {
            if (this.mConnStrategy == null || this.mConnStrategy.getIpSource() != 1) {
                Request.Builder redirectEnable = new Request.Builder().setUrl(this.mHost).setSeq(this.mSeq).setConnectTimeout((int) (((float) this.mConnTimeout) * Utils.getNetworkTimeFactor())).setReadTimeout((int) (((float) this.mReadTimeout) * Utils.getNetworkTimeFactor())).setRedirectEnable(false);
                if (this.sslSocketFactory != null) {
                    redirectEnable.setSslSocketFactory(this.sslSocketFactory);
                }
                if (this.mIpToHost) {
                    redirectEnable.addHeader(HttpConstant.HOST, this.mIp);
                }
                if (Inet64Util.isIPv6OnlyNetwork() && anet.channel.strategy.utils.Utils.isIPV4Address(this.mIp)) {
                    try {
                        this.mConnectIp = Inet64Util.convertToIPv6ThrowsException(this.mIp);
                    } catch (Exception unused) {
                    }
                }
                ALog.i(TAG, "HttpSession connect", (String) null, "host", this.mHost, "ip", this.mConnectIp, "port", Integer.valueOf(this.mPort));
                final Request build = redirectEnable.build();
                build.setDnsOptimize(this.mConnectIp, this.mPort);
                ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                    public void run() {
                        HttpConnector.Response connect = HttpConnector.connect(build);
                        if (connect.httpCode > 0) {
                            HttpSession.this.notifyStatus(4, new Event(1));
                        } else {
                            HttpSession.this.handleCallbacks(256, new Event(256, connect.httpCode, "Http connect fail"));
                        }
                    }
                }, ThreadPoolExecutorFactory.Priority.LOW);
                return;
            }
            notifyStatus(4, new Event(1));
        } catch (Throwable th) {
            ALog.e(TAG, "HTTP connect fail.", (String) null, th, new Object[0]);
        }
    }

    public void close() {
        notifyStatus(6, (Event) null);
    }

    public void close(boolean z) {
        this.autoReCreate = false;
        close();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:9|10|(1:14)|15|(2:(1:18)|19)|(1:21)|22|(2:24|(2:28|29))|30|31|(1:33)(1:34)|35|36|44) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:30:0x0073 */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0087 A[Catch:{ Throwable -> 0x00bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0099 A[Catch:{ Throwable -> 0x00bd }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public anet.channel.request.Cancelable request(final anet.channel.request.Request r9, final anet.channel.RequestCb r10) {
        /*
            r8 = this;
            anet.channel.request.FutureCancelable r0 = anet.channel.request.FutureCancelable.NULL
            r1 = 0
            if (r9 == 0) goto L_0x0008
            anet.channel.statist.RequestStatistic r2 = r9.rs
            goto L_0x000f
        L_0x0008:
            anet.channel.statist.RequestStatistic r2 = new anet.channel.statist.RequestStatistic
            java.lang.String r3 = r8.mRealHost
            r2.<init>(r3, r1)
        L_0x000f:
            anet.channel.entity.ConnType r3 = r8.mConnType
            r2.setConnType(r3)
            long r3 = r2.start
            r5 = 0
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 != 0) goto L_0x0024
            long r3 = java.lang.System.currentTimeMillis()
            r2.reqStart = r3
            r2.start = r3
        L_0x0024:
            if (r9 == 0) goto L_0x00ce
            if (r10 != 0) goto L_0x002a
            goto L_0x00ce
        L_0x002a:
            javax.net.ssl.SSLSocketFactory r3 = r9.getSslSocketFactory()     // Catch:{ Throwable -> 0x00bd }
            if (r3 != 0) goto L_0x003e
            javax.net.ssl.SSLSocketFactory r3 = r8.sslSocketFactory     // Catch:{ Throwable -> 0x00bd }
            if (r3 == 0) goto L_0x003e
            anet.channel.request.Request$Builder r1 = r9.newBuilder()     // Catch:{ Throwable -> 0x00bd }
            javax.net.ssl.SSLSocketFactory r3 = r8.sslSocketFactory     // Catch:{ Throwable -> 0x00bd }
            anet.channel.request.Request$Builder r1 = r1.setSslSocketFactory(r3)     // Catch:{ Throwable -> 0x00bd }
        L_0x003e:
            boolean r3 = r8.mIpToHost     // Catch:{ Throwable -> 0x00bd }
            if (r3 == 0) goto L_0x004f
            if (r1 != 0) goto L_0x0048
            anet.channel.request.Request$Builder r1 = r9.newBuilder()     // Catch:{ Throwable -> 0x00bd }
        L_0x0048:
            java.lang.String r3 = "Host"
            java.lang.String r4 = r8.mIp     // Catch:{ Throwable -> 0x00bd }
            r1.addHeader(r3, r4)     // Catch:{ Throwable -> 0x00bd }
        L_0x004f:
            if (r1 == 0) goto L_0x0055
            anet.channel.request.Request r9 = r1.build()     // Catch:{ Throwable -> 0x00bd }
        L_0x0055:
            java.lang.String r1 = r8.mConnectIp     // Catch:{ Throwable -> 0x00bd }
            if (r1 != 0) goto L_0x0073
            anet.channel.util.HttpUrl r1 = r9.getHttpUrl()     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r1 = r1.host()     // Catch:{ Throwable -> 0x00bd }
            boolean r3 = anet.channel.util.Inet64Util.isIPv6OnlyNetwork()     // Catch:{ Throwable -> 0x00bd }
            if (r3 == 0) goto L_0x0073
            boolean r3 = anet.channel.strategy.utils.Utils.isIPV4Address(r1)     // Catch:{ Throwable -> 0x00bd }
            if (r3 == 0) goto L_0x0073
            java.lang.String r1 = anet.channel.util.Inet64Util.convertToIPv6ThrowsException(r1)     // Catch:{ Exception -> 0x0073 }
            r8.mConnectIp = r1     // Catch:{ Exception -> 0x0073 }
        L_0x0073:
            java.lang.String r1 = r8.mConnectIp     // Catch:{ Throwable -> 0x00bd }
            int r3 = r8.mPort     // Catch:{ Throwable -> 0x00bd }
            r9.setDnsOptimize(r1, r3)     // Catch:{ Throwable -> 0x00bd }
            anet.channel.entity.ConnType r1 = r8.mConnType     // Catch:{ Throwable -> 0x00bd }
            boolean r1 = r1.isSSL()     // Catch:{ Throwable -> 0x00bd }
            r9.setUrlScheme(r1)     // Catch:{ Throwable -> 0x00bd }
            anet.channel.strategy.IConnStrategy r1 = r8.mConnStrategy     // Catch:{ Throwable -> 0x00bd }
            if (r1 == 0) goto L_0x0099
            anet.channel.statist.RequestStatistic r1 = r9.rs     // Catch:{ Throwable -> 0x00bd }
            anet.channel.strategy.IConnStrategy r3 = r8.mConnStrategy     // Catch:{ Throwable -> 0x00bd }
            int r3 = r3.getIpSource()     // Catch:{ Throwable -> 0x00bd }
            anet.channel.strategy.IConnStrategy r4 = r8.mConnStrategy     // Catch:{ Throwable -> 0x00bd }
            int r4 = r4.getIpType()     // Catch:{ Throwable -> 0x00bd }
            r1.setIpInfo(r3, r4)     // Catch:{ Throwable -> 0x00bd }
            goto L_0x009f
        L_0x0099:
            anet.channel.statist.RequestStatistic r1 = r9.rs     // Catch:{ Throwable -> 0x00bd }
            r3 = 1
            r1.setIpInfo(r3, r3)     // Catch:{ Throwable -> 0x00bd }
        L_0x009f:
            anet.channel.statist.RequestStatistic r1 = r9.rs     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r3 = r8.unit     // Catch:{ Throwable -> 0x00bd }
            r1.unit = r3     // Catch:{ Throwable -> 0x00bd }
            anet.channel.request.FutureCancelable r1 = new anet.channel.request.FutureCancelable     // Catch:{ Throwable -> 0x00bd }
            anet.channel.session.HttpSession$2 r3 = new anet.channel.session.HttpSession$2     // Catch:{ Throwable -> 0x00bd }
            r3.<init>(r9, r10, r2)     // Catch:{ Throwable -> 0x00bd }
            int r4 = anet.channel.util.RequestPriorityTable.lookup(r9)     // Catch:{ Throwable -> 0x00bd }
            java.util.concurrent.Future r3 = anet.channel.thread.ThreadPoolExecutorFactory.submitPriorityTask(r3, r4)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r9 = r9.getSeq()     // Catch:{ Throwable -> 0x00bd }
            r1.<init>(r3, r9)     // Catch:{ Throwable -> 0x00bd }
            r0 = r1
            goto L_0x00cd
        L_0x00bd:
            r9 = move-exception
            if (r10 == 0) goto L_0x00cd
            java.lang.String r9 = r9.toString()
            r1 = -101(0xffffffffffffff9b, float:NaN)
            java.lang.String r9 = anet.channel.util.ErrorConstant.formatMsg(r1, r9)
            r10.onFinish(r1, r9, r2)
        L_0x00cd:
            return r0
        L_0x00ce:
            if (r10 == 0) goto L_0x00d9
            r9 = -102(0xffffffffffffff9a, float:NaN)
            java.lang.String r1 = anet.channel.util.ErrorConstant.getErrMsg(r9)
            r10.onFinish(r9, r1, r2)
        L_0x00d9:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.HttpSession.request(anet.channel.request.Request, anet.channel.RequestCb):anet.channel.request.Cancelable");
    }
}
