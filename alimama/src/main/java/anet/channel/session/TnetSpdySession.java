package anet.channel.session;

import android.content.Context;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.Config;
import anet.channel.DataFrameCb;
import anet.channel.IAuth;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.SessionInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.ByteArrayPool;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.Event;
import anet.channel.heartbeat.HeartbeatManager;
import anet.channel.heartbeat.IHeartbeat;
import anet.channel.heartbeat.SelfKillHeartbeatImpl;
import anet.channel.request.Request;
import anet.channel.security.ISecurity;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.SessionMonitor;
import anet.channel.statist.SessionStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import com.taobao.accs.common.Constants;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Map;
import kotlin.UByte;
import org.android.spdy.AccsSSLCallback;
import org.android.spdy.SessionCb;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdyByteArray;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdyProtocol;
import org.android.spdy.SpdySession;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.android.spdy.SuperviseConnectInfo;
import org.android.spdy.SuperviseData;
import org.json.JSONException;
import org.json.JSONObject;

public class TnetSpdySession extends Session implements SessionCb {
    private static final String SSL_TIKET_KEY2 = "accs_ssl_key2_";
    private static final String TAG = "awcn.TnetSpdySession";
    protected IAuth auth = null;
    protected DataFrameCb dataFrameCb = null;
    protected IHeartbeat heartbeat = null;
    protected ISecurity iSecurity = null;
    /* access modifiers changed from: private */
    public boolean isAccs = false;
    protected SpdyAgent mAgent;
    protected String mAppkey = null;
    protected long mConnectedTime = 0;
    protected volatile boolean mHasUnrevPing = false;
    protected long mLastPingTime;
    protected SpdySession mSession;
    /* access modifiers changed from: private */
    public int requestTimeoutCount = 0;
    protected int tnetPublicKey = -1;

    public void bioPingRecvCallback(SpdySession spdySession, int i) {
    }

    static /* synthetic */ int access$804(TnetSpdySession tnetSpdySession) {
        int i = tnetSpdySession.requestTimeoutCount + 1;
        tnetSpdySession.requestTimeoutCount = i;
        return i;
    }

    public TnetSpdySession(Context context, ConnInfo connInfo) {
        super(context, connInfo);
    }

    public void initConfig(Config config) {
        if (config != null) {
            this.mAppkey = config.getAppkey();
            this.iSecurity = config.getSecurity();
        }
    }

    public void initSessionInfo(SessionInfo sessionInfo) {
        if (sessionInfo != null) {
            this.dataFrameCb = sessionInfo.dataFrameCb;
            this.auth = sessionInfo.auth;
            if (sessionInfo.isKeepAlive) {
                this.mSessionStat.isKL = 1;
                this.autoReCreate = true;
                this.heartbeat = sessionInfo.heartbeat;
                this.isAccs = sessionInfo.isAccs;
                if (this.heartbeat == null) {
                    if (!sessionInfo.isAccs || AwcnConfig.isAccsSessionCreateForbiddenInBg()) {
                        this.heartbeat = HeartbeatManager.getDefaultHeartbeat();
                    } else {
                        this.heartbeat = HeartbeatManager.getDefaultBackgroundAccsHeartbeat();
                    }
                }
            }
        }
        if (AwcnConfig.isIdleSessionCloseEnable() && this.heartbeat == null) {
            this.heartbeat = new SelfKillHeartbeatImpl();
        }
    }

    public void setTnetPublicKey(int i) {
        this.tnetPublicKey = i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0111 A[Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0125 A[Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0174 A[Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x01b1 A[Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x01be A[Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public anet.channel.request.Cancelable request(anet.channel.request.Request r21, anet.channel.RequestCb r22) {
        /*
            r20 = this;
            r1 = r20
            r0 = r21
            r2 = r22
            anet.channel.request.TnetCancelable r3 = anet.channel.request.TnetCancelable.NULL
            if (r0 == 0) goto L_0x000d
            anet.channel.statist.RequestStatistic r4 = r0.rs
            goto L_0x0015
        L_0x000d:
            anet.channel.statist.RequestStatistic r4 = new anet.channel.statist.RequestStatistic
            java.lang.String r5 = r1.mRealHost
            r6 = 0
            r4.<init>(r5, r6)
        L_0x0015:
            anet.channel.entity.ConnType r5 = r1.mConnType
            r4.setConnType(r5)
            long r5 = r4.start
            r7 = 0
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 != 0) goto L_0x002a
            long r5 = java.lang.System.currentTimeMillis()
            r4.reqStart = r5
            r4.start = r5
        L_0x002a:
            java.lang.String r5 = r1.mConnectIp
            int r6 = r1.mPort
            r4.setIPAndPort(r5, r6)
            anet.channel.strategy.IConnStrategy r5 = r1.mConnStrategy
            int r5 = r5.getIpSource()
            r4.ipRefer = r5
            anet.channel.strategy.IConnStrategy r5 = r1.mConnStrategy
            int r5 = r5.getIpType()
            r4.ipType = r5
            java.lang.String r5 = r1.unit
            r4.unit = r5
            if (r0 == 0) goto L_0x0229
            if (r2 != 0) goto L_0x004b
            goto L_0x0229
        L_0x004b:
            r5 = 0
            r6 = 2
            org.android.spdy.SpdySession r7 = r1.mSession     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r7 == 0) goto L_0x01dc
            int r7 = r1.mStatus     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r7 == 0) goto L_0x005a
            int r7 = r1.mStatus     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r8 = 4
            if (r7 != r8) goto L_0x01dc
        L_0x005a:
            boolean r7 = r1.mIpToHost     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r7 == 0) goto L_0x0065
            java.lang.String r7 = r1.mIp     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            int r8 = r1.mPort     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r0.setDnsOptimize(r7, r8)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x0065:
            anet.channel.entity.ConnType r7 = r1.mConnType     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            boolean r7 = r7.isSSL()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r0.setUrlScheme(r7)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.net.URL r9 = r21.getUrl()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            boolean r7 = anet.channel.util.ALog.isPrintLog(r6)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r19 = 1
            if (r7 == 0) goto L_0x00bf
            java.lang.String r7 = "awcn.TnetSpdySession"
            java.lang.String r8 = ""
            java.lang.String r10 = r21.getSeq()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.Object[] r11 = new java.lang.Object[r6]     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = "request URL"
            r11[r5] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = r9.toString()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r11[r19] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.util.ALog.i(r7, r8, r10, r11)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r7 = "awcn.TnetSpdySession"
            java.lang.String r8 = ""
            java.lang.String r10 = r21.getSeq()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.Object[] r11 = new java.lang.Object[r6]     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = "request Method"
            r11[r5] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = r21.getMethod()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r11[r19] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.util.ALog.i(r7, r8, r10, r11)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r7 = "awcn.TnetSpdySession"
            java.lang.String r8 = ""
            java.lang.String r10 = r21.getSeq()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.Object[] r11 = new java.lang.Object[r6]     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = "request headers"
            r11[r5] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.util.Map r12 = r21.getHeaders()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r11[r19] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.util.ALog.i(r7, r8, r10, r11)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x00bf:
            java.lang.String r7 = r1.mProxyIp     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            boolean r7 = android.text.TextUtils.isEmpty(r7)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r7 != 0) goto L_0x00ed
            int r7 = r1.mProxyPort     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r7 > 0) goto L_0x00cc
            goto L_0x00ed
        L_0x00cc:
            org.android.spdy.SpdyRequest r7 = new org.android.spdy.SpdyRequest     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r10 = r9.getHost()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            int r11 = r9.getPort()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = r1.mProxyIp     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            int r13 = r1.mProxyPort     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r14 = r21.getMethod()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            org.android.spdy.RequestPriority r15 = org.android.spdy.RequestPriority.DEFAULT_PRIORITY     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r16 = -1
            int r17 = r21.getConnectTimeout()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r18 = 0
            r8 = r7
            r8.<init>(r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            goto L_0x00fe
        L_0x00ed:
            org.android.spdy.SpdyRequest r7 = new org.android.spdy.SpdyRequest     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r10 = r21.getMethod()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            org.android.spdy.RequestPriority r11 = org.android.spdy.RequestPriority.DEFAULT_PRIORITY     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r12 = -1
            int r13 = r21.getConnectTimeout()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r8 = r7
            r8.<init>((java.net.URL) r9, (java.lang.String) r10, (org.android.spdy.RequestPriority) r11, (int) r12, (int) r13)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x00fe:
            int r8 = r21.getReadTimeout()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r7.setRequestRdTimeoutMs(r8)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.util.Map r8 = r21.getHeaders()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r9 = "Host"
            boolean r9 = r8.containsKey(r9)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r9 != 0) goto L_0x0125
            r7.addHeaders(r8)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r8 = ":host"
            boolean r9 = r1.mIpToHost     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r9 == 0) goto L_0x011d
            java.lang.String r9 = r1.mIp     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            goto L_0x0121
        L_0x011d:
            java.lang.String r9 = r21.getHost()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x0121:
            r7.addHeader(r8, r9)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            goto L_0x0144
        L_0x0125:
            java.util.HashMap r8 = new java.util.HashMap     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.util.Map r9 = r21.getHeaders()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r8.<init>(r9)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r9 = "Host"
            java.lang.Object r9 = r8.remove(r9)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r9 = (java.lang.String) r9     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r10 = ":host"
            boolean r11 = r1.mIpToHost     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r11 == 0) goto L_0x013e
            java.lang.String r9 = r1.mIp     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x013e:
            r8.put(r10, r9)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r7.addHeaders(r8)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x0144:
            byte[] r8 = r21.getBodyBytes()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            org.android.spdy.SpdyDataProvider r9 = new org.android.spdy.SpdyDataProvider     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r9.<init>((byte[]) r8)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.statist.RequestStatistic r8 = r0.rs     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            long r10 = java.lang.System.currentTimeMillis()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r8.sendStart = r10     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.statist.RequestStatistic r8 = r0.rs     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.statist.RequestStatistic r10 = r0.rs     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            long r10 = r10.sendStart     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.statist.RequestStatistic r12 = r0.rs     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            long r12 = r12.start     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r14 = 0
            long r10 = r10 - r12
            r8.processTime = r10     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            org.android.spdy.SpdySession r8 = r1.mSession     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.session.TnetSpdySession$RequestCallback r10 = new anet.channel.session.TnetSpdySession$RequestCallback     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r10.<init>(r0, r2)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            int r7 = r8.submitRequest(r7, r9, r1, r10)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            boolean r8 = anet.channel.util.ALog.isPrintLog(r19)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            if (r8 == 0) goto L_0x018b
            java.lang.String r8 = "awcn.TnetSpdySession"
            java.lang.String r9 = ""
            java.lang.String r10 = r21.getSeq()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.Object[] r11 = new java.lang.Object[r6]     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r12 = "streamId"
            r11[r5] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r7)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r11[r19] = r12     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.util.ALog.d(r8, r9, r10, r11)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
        L_0x018b:
            anet.channel.request.TnetCancelable r8 = new anet.channel.request.TnetCancelable     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            org.android.spdy.SpdySession r9 = r1.mSession     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            java.lang.String r10 = r21.getSeq()     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r8.<init>(r9, r7, r10)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.statist.SessionStatistic r3 = r1.mSessionStat     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            long r9 = r3.requestCount     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            r11 = 1
            long r9 = r9 + r11
            r3.requestCount = r9     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            anet.channel.statist.SessionStatistic r3 = r1.mSessionStat     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            long r9 = r3.stdRCount     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            r7 = 0
            long r9 = r9 + r11
            r3.stdRCount = r9     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            long r9 = java.lang.System.currentTimeMillis()     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            r1.mLastPingTime = r9     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            anet.channel.heartbeat.IHeartbeat r3 = r1.heartbeat     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            if (r3 == 0) goto L_0x01b6
            anet.channel.heartbeat.IHeartbeat r3 = r1.heartbeat     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            r3.reSchedule()     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
        L_0x01b6:
            anet.channel.entity.ConnType r3 = r1.mConnType     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            boolean r3 = r3.isQuic()     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            if (r3 == 0) goto L_0x01d5
            anet.channel.statist.RequestStatistic r3 = r0.rs     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            java.lang.String r7 = "QuicConnectionID"
            org.android.spdy.SpdySession r9 = r1.mSession     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            java.lang.String r9 = r9.getQuicConnectionID()     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            r3.putExtra(r7, r9)     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            anet.channel.statist.RequestStatistic r0 = r0.rs     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            org.android.spdy.SpdySession r3 = r1.mSession     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            java.lang.String r3 = r3.getQuicConnectionID()     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
            r0.quicConnectionID = r3     // Catch:{ SpdyErrorException -> 0x01d9, Exception -> 0x01d7 }
        L_0x01d5:
            r3 = r8
            goto L_0x0228
        L_0x01d7:
            r3 = r8
            goto L_0x01e8
        L_0x01d9:
            r0 = move-exception
            r3 = r8
            goto L_0x01f3
        L_0x01dc:
            r7 = -301(0xfffffffffffffed3, float:NaN)
            java.lang.String r8 = anet.channel.util.ErrorConstant.getErrMsg(r7)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            anet.channel.statist.RequestStatistic r0 = r0.rs     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            r2.onFinish(r7, r8, r0)     // Catch:{ SpdyErrorException -> 0x01f2, Exception -> 0x01e8 }
            goto L_0x0228
        L_0x01e8:
            r0 = -101(0xffffffffffffff9b, float:NaN)
            java.lang.String r5 = anet.channel.util.ErrorConstant.getErrMsg(r0)
            r2.onFinish(r0, r5, r4)
            goto L_0x0228
        L_0x01f2:
            r0 = move-exception
        L_0x01f3:
            int r7 = r0.SpdyErrorGetCode()
            r8 = -1104(0xfffffffffffffbb0, float:NaN)
            if (r7 == r8) goto L_0x0203
            int r7 = r0.SpdyErrorGetCode()
            r8 = -1103(0xfffffffffffffbb1, float:NaN)
            if (r7 != r8) goto L_0x0217
        L_0x0203:
            java.lang.String r7 = "awcn.TnetSpdySession"
            java.lang.String r8 = "Send request on closed session!!!"
            java.lang.String r9 = r1.mSeq
            java.lang.Object[] r5 = new java.lang.Object[r5]
            anet.channel.util.ALog.e(r7, r8, r9, r5)
            r5 = 6
            anet.channel.entity.Event r7 = new anet.channel.entity.Event
            r7.<init>(r6)
            r1.notifyStatus(r5, r7)
        L_0x0217:
            int r0 = r0.SpdyErrorGetCode()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r5 = -300(0xfffffffffffffed4, float:NaN)
            java.lang.String r0 = anet.channel.util.ErrorConstant.formatMsg(r5, r0)
            r2.onFinish(r5, r0, r4)
        L_0x0228:
            return r3
        L_0x0229:
            if (r2 == 0) goto L_0x0234
            r0 = -102(0xffffffffffffff9a, float:NaN)
            java.lang.String r5 = anet.channel.util.ErrorConstant.getErrMsg(r0)
            r2.onFinish(r0, r5, r4)
        L_0x0234:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.TnetSpdySession.request(anet.channel.request.Request, anet.channel.RequestCb):anet.channel.request.Cancelable");
    }

    public void sendCustomFrame(int i, byte[] bArr, int i2) {
        try {
            if (this.dataFrameCb != null) {
                ALog.e(TAG, "sendCustomFrame", this.mSeq, Constants.KEY_DATA_ID, Integer.valueOf(i), "type", Integer.valueOf(i2));
                if (this.mStatus != 4 || this.mSession == null) {
                    ALog.e(TAG, "sendCustomFrame", this.mSeq, "sendCustomFrame con invalid mStatus:" + this.mStatus);
                    onDataFrameException(i, ErrorConstant.ERROR_SESSION_INVALID, true, "session invalid");
                } else if (bArr == null || bArr.length <= 16384) {
                    this.mSession.sendCustomControlFrame(i, i2, 0, bArr == null ? 0 : bArr.length, bArr);
                    this.mSessionStat.requestCount++;
                    this.mSessionStat.cfRCount++;
                    this.mLastPingTime = System.currentTimeMillis();
                    if (this.heartbeat != null) {
                        this.heartbeat.reSchedule();
                    }
                } else {
                    onDataFrameException(i, ErrorConstant.ERROR_DATA_TOO_LARGE, false, (String) null);
                }
            }
        } catch (SpdyErrorException e) {
            ALog.e(TAG, "sendCustomFrame error", this.mSeq, e, new Object[0]);
            onDataFrameException(i, ErrorConstant.ERROR_TNET_EXCEPTION, true, "SpdyErrorException: " + e.toString());
        } catch (Exception e2) {
            ALog.e(TAG, "sendCustomFrame error", this.mSeq, e2, new Object[0]);
            onDataFrameException(i, -101, true, e2.toString());
        }
    }

    private void onDataFrameException(int i, int i2, boolean z, String str) {
        if (this.dataFrameCb != null) {
            this.dataFrameCb.onException(i, i2, z, str);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(14:6|7|8|(1:10)|11|(2:15|16)|17|18|(2:22|(1:24)(4:25|(1:27)(3:28|(1:30)(1:31)|32)|38|(2:40|41)(2:42|46)))|33|(1:35)(1:36)|37|38|(0)(0)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0030 */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00e5 A[Catch:{ Throwable -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00e6 A[Catch:{ Throwable -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0126 A[Catch:{ Throwable -> 0x0164 }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x013d A[Catch:{ Throwable -> 0x0164 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect() {
        /*
            r14 = this;
            int r0 = r14.mStatus
            r10 = 1
            if (r0 == r10) goto L_0x0174
            int r0 = r14.mStatus
            if (r0 == 0) goto L_0x0174
            int r0 = r14.mStatus
            r1 = 4
            if (r0 != r1) goto L_0x0010
            goto L_0x0174
        L_0x0010:
            r11 = 0
            r12 = 2
            r13 = 0
            org.android.spdy.SpdyAgent r0 = r14.mAgent     // Catch:{ Throwable -> 0x0164 }
            if (r0 != 0) goto L_0x001a
            r14.initSpdyAgent()     // Catch:{ Throwable -> 0x0164 }
        L_0x001a:
            boolean r0 = anet.channel.util.Inet64Util.isIPv6OnlyNetwork()     // Catch:{ Throwable -> 0x0164 }
            if (r0 == 0) goto L_0x0030
            java.lang.String r0 = r14.mIp     // Catch:{ Throwable -> 0x0164 }
            boolean r0 = anet.channel.strategy.utils.Utils.isIPV4Address(r0)     // Catch:{ Throwable -> 0x0164 }
            if (r0 == 0) goto L_0x0030
            java.lang.String r0 = r14.mIp     // Catch:{ Exception -> 0x0030 }
            java.lang.String r0 = anet.channel.util.Inet64Util.convertToIPv6ThrowsException(r0)     // Catch:{ Exception -> 0x0030 }
            r14.mConnectIp = r0     // Catch:{ Exception -> 0x0030 }
        L_0x0030:
            long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r7 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r0 = "awcn.TnetSpdySession"
            java.lang.String r2 = "connect"
            java.lang.String r3 = r14.mSeq     // Catch:{ Throwable -> 0x0164 }
            r4 = 14
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = "host"
            r4[r13] = r5     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = r14.mHost     // Catch:{ Throwable -> 0x0164 }
            r4[r10] = r5     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = "ip"
            r4[r12] = r5     // Catch:{ Throwable -> 0x0164 }
            r5 = 3
            java.lang.String r6 = r14.mConnectIp     // Catch:{ Throwable -> 0x0164 }
            r4[r5] = r6     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = "port"
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 5
            int r5 = r14.mPort     // Catch:{ Throwable -> 0x0164 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0164 }
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 6
            java.lang.String r5 = "sessionId"
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 7
            r4[r1] = r7     // Catch:{ Throwable -> 0x0164 }
            r1 = 8
            java.lang.String r5 = "SpdyProtocol,"
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 9
            anet.channel.entity.ConnType r5 = r14.mConnType     // Catch:{ Throwable -> 0x0164 }
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 10
            java.lang.String r5 = "proxyIp,"
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 11
            java.lang.String r5 = r14.mProxyIp     // Catch:{ Throwable -> 0x0164 }
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 12
            java.lang.String r5 = "proxyPort,"
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            r1 = 13
            int r5 = r14.mProxyPort     // Catch:{ Throwable -> 0x0164 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0164 }
            r4[r1] = r5     // Catch:{ Throwable -> 0x0164 }
            anet.channel.util.ALog.e(r0, r2, r3, r4)     // Catch:{ Throwable -> 0x0164 }
            org.android.spdy.SessionInfo r0 = new org.android.spdy.SessionInfo     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r2 = r14.mConnectIp     // Catch:{ Throwable -> 0x0164 }
            int r3 = r14.mPort     // Catch:{ Throwable -> 0x0164 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0164 }
            r1.<init>()     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = r14.mHost     // Catch:{ Throwable -> 0x0164 }
            r1.append(r4)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = "_"
            r1.append(r4)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = r14.mAppkey     // Catch:{ Throwable -> 0x0164 }
            r1.append(r4)     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r4 = r1.toString()     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r5 = r14.mProxyIp     // Catch:{ Throwable -> 0x0164 }
            int r6 = r14.mProxyPort     // Catch:{ Throwable -> 0x0164 }
            anet.channel.entity.ConnType r1 = r14.mConnType     // Catch:{ Throwable -> 0x0164 }
            int r9 = r1.getTnetConType()     // Catch:{ Throwable -> 0x0164 }
            r1 = r0
            r8 = r14
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)     // Catch:{ Throwable -> 0x0164 }
            int r1 = r14.mConnTimeout     // Catch:{ Throwable -> 0x0164 }
            float r1 = (float) r1     // Catch:{ Throwable -> 0x0164 }
            float r2 = anet.channel.util.Utils.getNetworkTimeFactor()     // Catch:{ Throwable -> 0x0164 }
            float r1 = r1 * r2
            int r1 = (int) r1     // Catch:{ Throwable -> 0x0164 }
            r0.setConnectionTimeoutMs(r1)     // Catch:{ Throwable -> 0x0164 }
            anet.channel.entity.ConnType r1 = r14.mConnType     // Catch:{ Throwable -> 0x0164 }
            boolean r1 = r1.isPublicKeyAuto()     // Catch:{ Throwable -> 0x0164 }
            if (r1 != 0) goto L_0x010a
            anet.channel.entity.ConnType r1 = r14.mConnType     // Catch:{ Throwable -> 0x0164 }
            boolean r1 = r1.isQuic()     // Catch:{ Throwable -> 0x0164 }
            if (r1 != 0) goto L_0x010a
            anet.channel.entity.ConnType r1 = r14.mConnType     // Catch:{ Throwable -> 0x0164 }
            boolean r1 = r1.isH2S()     // Catch:{ Throwable -> 0x0164 }
            if (r1 == 0) goto L_0x00e6
            goto L_0x010a
        L_0x00e6:
            int r1 = r14.tnetPublicKey     // Catch:{ Throwable -> 0x0164 }
            if (r1 < 0) goto L_0x00f0
            int r1 = r14.tnetPublicKey     // Catch:{ Throwable -> 0x0164 }
            r0.setPubKeySeqNum(r1)     // Catch:{ Throwable -> 0x0164 }
            goto L_0x0116
        L_0x00f0:
            anet.channel.entity.ConnType r1 = r14.mConnType     // Catch:{ Throwable -> 0x0164 }
            anet.channel.security.ISecurity r2 = r14.iSecurity     // Catch:{ Throwable -> 0x0164 }
            if (r2 == 0) goto L_0x00fd
            anet.channel.security.ISecurity r2 = r14.iSecurity     // Catch:{ Throwable -> 0x0164 }
            boolean r2 = r2.isSecOff()     // Catch:{ Throwable -> 0x0164 }
            goto L_0x00fe
        L_0x00fd:
            r2 = 1
        L_0x00fe:
            int r1 = r1.getTnetPublicKey(r2)     // Catch:{ Throwable -> 0x0164 }
            r14.tnetPublicKey = r1     // Catch:{ Throwable -> 0x0164 }
            int r1 = r14.tnetPublicKey     // Catch:{ Throwable -> 0x0164 }
            r0.setPubKeySeqNum(r1)     // Catch:{ Throwable -> 0x0164 }
            goto L_0x0116
        L_0x010a:
            boolean r1 = r14.mIpToHost     // Catch:{ Throwable -> 0x0164 }
            if (r1 == 0) goto L_0x0111
            java.lang.String r1 = r14.mIp     // Catch:{ Throwable -> 0x0164 }
            goto L_0x0113
        L_0x0111:
            java.lang.String r1 = r14.mRealHost     // Catch:{ Throwable -> 0x0164 }
        L_0x0113:
            r0.setCertHost(r1)     // Catch:{ Throwable -> 0x0164 }
        L_0x0116:
            org.android.spdy.SpdyAgent r1 = r14.mAgent     // Catch:{ Throwable -> 0x0164 }
            org.android.spdy.SpdySession r0 = r1.createSession(r0)     // Catch:{ Throwable -> 0x0164 }
            r14.mSession = r0     // Catch:{ Throwable -> 0x0164 }
            org.android.spdy.SpdySession r0 = r14.mSession     // Catch:{ Throwable -> 0x0164 }
            int r0 = r0.getRefCount()     // Catch:{ Throwable -> 0x0164 }
            if (r0 <= r10) goto L_0x013d
            java.lang.String r0 = "awcn.TnetSpdySession"
            java.lang.String r1 = "get session ref count > 1!!!"
            java.lang.String r2 = r14.mSeq     // Catch:{ Throwable -> 0x0164 }
            java.lang.Object[] r3 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0164 }
            anet.channel.util.ALog.e(r0, r1, r2, r3)     // Catch:{ Throwable -> 0x0164 }
            anet.channel.entity.Event r0 = new anet.channel.entity.Event     // Catch:{ Throwable -> 0x0164 }
            r0.<init>(r10)     // Catch:{ Throwable -> 0x0164 }
            r14.notifyStatus(r13, r0)     // Catch:{ Throwable -> 0x0164 }
            r14.auth()     // Catch:{ Throwable -> 0x0164 }
            return
        L_0x013d:
            r14.notifyStatus(r10, r11)     // Catch:{ Throwable -> 0x0164 }
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0164 }
            r14.mLastPingTime = r0     // Catch:{ Throwable -> 0x0164 }
            anet.channel.statist.SessionStatistic r0 = r14.mSessionStat     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r1 = r14.mProxyIp     // Catch:{ Throwable -> 0x0164 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0164 }
            r1 = r1 ^ r10
            r0.isProxy = r1     // Catch:{ Throwable -> 0x0164 }
            anet.channel.statist.SessionStatistic r0 = r14.mSessionStat     // Catch:{ Throwable -> 0x0164 }
            java.lang.String r1 = "false"
            r0.isTunnel = r1     // Catch:{ Throwable -> 0x0164 }
            anet.channel.statist.SessionStatistic r0 = r14.mSessionStat     // Catch:{ Throwable -> 0x0164 }
            boolean r1 = anet.channel.GlobalAppRuntimeInfo.isAppBackground()     // Catch:{ Throwable -> 0x0164 }
            r0.isBackground = r1     // Catch:{ Throwable -> 0x0164 }
            r0 = 0
            r14.mConnectedTime = r0     // Catch:{ Throwable -> 0x0164 }
            goto L_0x0173
        L_0x0164:
            r0 = move-exception
            r14.notifyStatus(r12, r11)
            java.lang.String r1 = "awcn.TnetSpdySession"
            java.lang.String r2 = "connect exception "
            java.lang.String r3 = r14.mSeq
            java.lang.Object[] r4 = new java.lang.Object[r13]
            anet.channel.util.ALog.e(r1, r2, r3, r0, r4)
        L_0x0173:
            return
        L_0x0174:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.TnetSpdySession.connect():void");
    }

    public void close() {
        ALog.e(TAG, "force close!", this.mSeq, "session", this);
        notifyStatus(7, (Event) null);
        try {
            if (this.heartbeat != null) {
                this.heartbeat.stop();
                this.heartbeat = null;
            }
            if (this.mSession != null) {
                this.mSession.closeSession();
            }
        } catch (Exception unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void onDisconnect() {
        this.mHasUnrevPing = false;
    }

    /* access modifiers changed from: protected */
    public Runnable getRecvTimeOutRunnable() {
        return new Runnable() {
            public void run() {
                if (TnetSpdySession.this.mHasUnrevPing) {
                    ALog.e(TnetSpdySession.TAG, "send msg time out!", TnetSpdySession.this.mSeq, "pingUnRcv:", Boolean.valueOf(TnetSpdySession.this.mHasUnrevPing));
                    try {
                        TnetSpdySession.this.handleCallbacks(2048, (Event) null);
                        if (TnetSpdySession.this.mSessionStat != null) {
                            TnetSpdySession.this.mSessionStat.closeReason = "ping time out";
                        }
                        ConnEvent connEvent = new ConnEvent();
                        connEvent.isSuccess = false;
                        connEvent.isAccs = TnetSpdySession.this.isAccs;
                        StrategyCenter.getInstance().notifyConnEvent(TnetSpdySession.this.mRealHost, TnetSpdySession.this.mConnStrategy, connEvent);
                        TnetSpdySession.this.close(true);
                    } catch (Exception unused) {
                    }
                }
            }
        };
    }

    public void ping(boolean z) {
        ping(z, this.mReadTimeout);
    }

    public void ping(boolean z, int i) {
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "ping", this.mSeq, "host", this.mHost, "thread", Thread.currentThread().getName());
        }
        if (z) {
            try {
                if (this.mSession == null) {
                    if (this.mSessionStat != null) {
                        this.mSessionStat.closeReason = "session null";
                    }
                    ALog.e(TAG, this.mHost + " session null", this.mSeq, new Object[0]);
                    close();
                } else if (this.mStatus == 0 || this.mStatus == 4) {
                    handleCallbacks(64, (Event) null);
                    if (!this.mHasUnrevPing) {
                        this.mHasUnrevPing = true;
                        this.mSessionStat.ppkgCount++;
                        this.mSession.submitPing();
                        if (ALog.isPrintLog(1)) {
                            ALog.d(TAG, this.mHost + " submit ping ms:" + (System.currentTimeMillis() - this.mLastPingTime) + " force:" + z, this.mSeq, new Object[0]);
                        }
                        setPingTimeout(i);
                        this.mLastPingTime = System.currentTimeMillis();
                        if (this.heartbeat != null) {
                            this.heartbeat.reSchedule();
                        }
                    }
                }
            } catch (SpdyErrorException e) {
                if (e.SpdyErrorGetCode() == -1104 || e.SpdyErrorGetCode() == -1103) {
                    ALog.e(TAG, "Send request on closed session!!!", this.mSeq, new Object[0]);
                    notifyStatus(6, new Event(2));
                }
                ALog.e(TAG, "ping", this.mSeq, e, new Object[0]);
            } catch (Exception e2) {
                ALog.e(TAG, "ping", this.mSeq, e2, new Object[0]);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void auth() {
        if (this.auth != null) {
            this.auth.auth(this, new IAuth.AuthCallback() {
                public void onAuthSuccess() {
                    TnetSpdySession.this.notifyStatus(4, (Event) null);
                    TnetSpdySession.this.mLastPingTime = System.currentTimeMillis();
                    if (TnetSpdySession.this.heartbeat != null) {
                        TnetSpdySession.this.heartbeat.start(TnetSpdySession.this);
                    }
                    TnetSpdySession.this.mSessionStat.ret = 1;
                    ALog.d(TnetSpdySession.TAG, "spdyOnStreamResponse", TnetSpdySession.this.mSeq, "authTime", Long.valueOf(TnetSpdySession.this.mSessionStat.authTime));
                    if (TnetSpdySession.this.mConnectedTime > 0) {
                        TnetSpdySession.this.mSessionStat.authTime = System.currentTimeMillis() - TnetSpdySession.this.mConnectedTime;
                    }
                }

                public void onAuthFail(int i, String str) {
                    TnetSpdySession.this.notifyStatus(5, (Event) null);
                    if (TnetSpdySession.this.mSessionStat != null) {
                        SessionStatistic sessionStatistic = TnetSpdySession.this.mSessionStat;
                        sessionStatistic.closeReason = "Accs_Auth_Fail:" + i;
                        TnetSpdySession.this.mSessionStat.errorCode = (long) i;
                    }
                    TnetSpdySession.this.close();
                }
            });
            return;
        }
        notifyStatus(4, (Event) null);
        this.mSessionStat.ret = 1;
        if (this.heartbeat != null) {
            this.heartbeat.start(this);
        }
    }

    public boolean isAvailable() {
        return this.mStatus == 4;
    }

    private void initSpdyAgent() {
        SpdyAgent.enableDebug = false;
        this.mAgent = SpdyAgent.getInstance(this.mContext, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        if (this.iSecurity != null && !this.iSecurity.isSecOff()) {
            this.mAgent.setAccsSslCallback(new AccsSSLCallback() {
                public byte[] getSSLPublicKey(int i, byte[] bArr) {
                    byte[] bArr2;
                    try {
                        bArr2 = TnetSpdySession.this.iSecurity.decrypt(TnetSpdySession.this.mContext, ISecurity.CIPHER_ALGORITHM_AES128, SpdyProtocol.TNET_PUBKEY_SG_KEY, bArr);
                        if (bArr2 != null) {
                            try {
                                if (ALog.isPrintLog(2)) {
                                    ALog.i("getSSLPublicKey", (String) null, "decrypt", new String(bArr2));
                                }
                            } catch (Throwable th) {
                                th = th;
                                ALog.e(TnetSpdySession.TAG, "getSSLPublicKey", (String) null, th, new Object[0]);
                                return bArr2;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        bArr2 = null;
                        ALog.e(TnetSpdySession.TAG, "getSSLPublicKey", (String) null, th, new Object[0]);
                        return bArr2;
                    }
                    return bArr2;
                }
            });
        }
        if (!AwcnConfig.isTnetHeaderCacheEnable()) {
            try {
                this.mAgent.getClass().getDeclaredMethod("disableHeaderCache", new Class[0]).invoke(this.mAgent, new Object[0]);
                ALog.i(TAG, "tnet disableHeaderCache", (String) null, new Object[0]);
            } catch (Exception e) {
                ALog.e(TAG, "tnet disableHeaderCache", (String) null, e, new Object[0]);
            }
        }
    }

    public void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
        this.mSessionStat.connectionTime = (long) superviseConnectInfo.connectTime;
        this.mSessionStat.sslTime = (long) superviseConnectInfo.handshakeTime;
        this.mSessionStat.sslCalTime = (long) superviseConnectInfo.doHandshakeTime;
        this.mSessionStat.netType = NetworkStatusHelper.getNetworkSubType();
        this.mConnectedTime = System.currentTimeMillis();
        notifyStatus(0, new Event(1));
        auth();
        ALog.e(TAG, "spdySessionConnectCB connect", this.mSeq, "connectTime", Integer.valueOf(superviseConnectInfo.connectTime), "sslTime", Integer.valueOf(superviseConnectInfo.handshakeTime));
    }

    public void spdyPingRecvCallback(SpdySession spdySession, long j, Object obj) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "ping receive", this.mSeq, HttpConstant.HOST, this.mHost, "id", Long.valueOf(j));
        }
        if (j >= 0) {
            this.mHasUnrevPing = false;
            this.requestTimeoutCount = 0;
            if (this.heartbeat != null) {
                this.heartbeat.reSchedule();
            }
            handleCallbacks(128, (Event) null);
        }
    }

    public void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i, int i2, int i3, int i4, byte[] bArr) {
        ALog.e(TAG, "[spdyCustomControlFrameRecvCallback]", this.mSeq, DinamicConstant.LENGTH_PREFIX, Integer.valueOf(i4), "frameCb", this.dataFrameCb);
        if (ALog.isPrintLog(1) && i4 < 512) {
            String str = "";
            for (int i5 = 0; i5 < bArr.length; i5++) {
                str = str + Integer.toHexString(bArr[i5] & UByte.MAX_VALUE) + Operators.SPACE_STR;
            }
            ALog.e(TAG, (String) null, this.mSeq, "str", str);
        }
        if (this.dataFrameCb != null) {
            this.dataFrameCb.onDataReceive(this, bArr, i, i2);
        } else {
            ALog.e(TAG, "AccsFrameCb is null", this.mSeq, new Object[0]);
            AppMonitor.getInstance().commitStat(new ExceptionStatistic(-105, (String) null, "rt"));
        }
        this.mSessionStat.inceptCount++;
        if (this.heartbeat != null) {
            this.heartbeat.reSchedule();
        }
    }

    public void spdySessionFailedError(SpdySession spdySession, int i, Object obj) {
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                ALog.e(TAG, "[spdySessionFailedError]session clean up failed!", (String) null, e, new Object[0]);
            }
        }
        notifyStatus(2, new Event(256, i, "tnet connect fail"));
        ALog.e(TAG, (String) null, this.mSeq, " errorId:", Integer.valueOf(i));
        this.mSessionStat.errorCode = (long) i;
        this.mSessionStat.ret = 0;
        this.mSessionStat.netType = NetworkStatusHelper.getNetworkSubType();
        AppMonitor.getInstance().commitStat(this.mSessionStat);
        if (Utils.isIPV6Address(this.mSessionStat.ip)) {
            AppMonitor.getInstance().commitStat(new SessionMonitor(this.mSessionStat));
        }
        AppMonitor.getInstance().commitAlarm(this.mSessionStat.getAlarmObject());
    }

    public void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i) {
        ALog.e(TAG, "spdySessionCloseCallback", this.mSeq, " errorCode:", Integer.valueOf(i));
        if (this.heartbeat != null) {
            this.heartbeat.stop();
            this.heartbeat = null;
        }
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                ALog.e(TAG, "session clean up failed!", (String) null, e, new Object[0]);
            }
        }
        if (i == -3516) {
            ConnEvent connEvent = new ConnEvent();
            connEvent.isSuccess = false;
            StrategyCenter.getInstance().notifyConnEvent(this.mRealHost, this.mConnStrategy, connEvent);
        }
        notifyStatus(6, new Event(2));
        if (superviseConnectInfo != null) {
            this.mSessionStat.requestCount = (long) superviseConnectInfo.reused_counter;
            this.mSessionStat.liveTime = (long) superviseConnectInfo.keepalive_period_second;
            try {
                if (this.mConnType.isQuic()) {
                    this.mSessionStat.extra = new JSONObject();
                    this.mSessionStat.extra.put("QuicConnectionID", this.mSession.getQuicConnectionID());
                    this.mSessionStat.extra.put("retransmissionRate", superviseConnectInfo.retransmissionRate);
                    this.mSessionStat.extra.put("lossRate", superviseConnectInfo.lossRate);
                    this.mSessionStat.extra.put("tlpCount", superviseConnectInfo.tlpCount);
                    this.mSessionStat.extra.put("rtoCount", superviseConnectInfo.rtoCount);
                }
            } catch (JSONException unused) {
            }
        }
        if (this.mSessionStat.errorCode == 0) {
            this.mSessionStat.errorCode = (long) i;
        }
        this.mSessionStat.lastPingInterval = (int) (System.currentTimeMillis() - this.mLastPingTime);
        AppMonitor.getInstance().commitStat(this.mSessionStat);
        if (Utils.isIPV6Address(this.mSessionStat.ip)) {
            AppMonitor.getInstance().commitStat(new SessionMonitor(this.mSessionStat));
        }
        AppMonitor.getInstance().commitAlarm(this.mSessionStat.getAlarmObject());
    }

    public void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i, int i2) {
        ALog.e(TAG, "spdyCustomControlFrameFailCallback", this.mSeq, Constants.KEY_DATA_ID, Integer.valueOf(i));
        onDataFrameException(i, i2, true, "tnet error");
    }

    public byte[] getSSLMeta(SpdySession spdySession) {
        String domain = spdySession.getDomain();
        if (TextUtils.isEmpty(domain)) {
            ALog.i(TAG, "get sslticket host is null", (String) null, new Object[0]);
            return null;
        }
        try {
            if (this.iSecurity == null) {
                return null;
            }
            ISecurity iSecurity2 = this.iSecurity;
            Context context = this.mContext;
            return iSecurity2.getBytes(context, SSL_TIKET_KEY2 + domain);
        } catch (Throwable th) {
            ALog.e(TAG, "getSSLMeta", (String) null, th, new Object[0]);
            return null;
        }
    }

    public int putSSLMeta(SpdySession spdySession, byte[] bArr) {
        String domain = spdySession.getDomain();
        if (TextUtils.isEmpty(domain)) {
            return -1;
        }
        int i = 0;
        try {
            if (this.iSecurity == null) {
                return -1;
            }
            ISecurity iSecurity2 = this.iSecurity;
            Context context = this.mContext;
            if (!iSecurity2.saveBytes(context, SSL_TIKET_KEY2 + domain, bArr)) {
                i = -1;
            }
            return i;
        } catch (Throwable th) {
            ALog.e(TAG, "putSSLMeta", (String) null, th, new Object[0]);
            return -1;
        }
    }

    private class RequestCallback extends DftSpdyCb {
        private RequestCb callback;
        private long recDataCount = 0;
        private Request request;
        private int statusCode = 0;

        public RequestCallback(Request request2, RequestCb requestCb) {
            this.request = request2;
            this.callback = requestCb;
        }

        public void spdyDataChunkRecvCB(SpdySession spdySession, boolean z, long j, SpdyByteArray spdyByteArray, Object obj) {
            if (ALog.isPrintLog(1)) {
                ALog.d(TnetSpdySession.TAG, "spdyDataChunkRecvCB", this.request.getSeq(), DinamicConstant.LENGTH_PREFIX, Integer.valueOf(spdyByteArray.getDataLength()), "fin", Boolean.valueOf(z));
            }
            this.recDataCount += (long) spdyByteArray.getDataLength();
            this.request.rs.recDataSize += (long) spdyByteArray.getDataLength();
            if (TnetSpdySession.this.heartbeat != null) {
                TnetSpdySession.this.heartbeat.reSchedule();
            }
            if (this.callback != null) {
                ByteArray retrieveAndCopy = ByteArrayPool.getInstance().retrieveAndCopy(spdyByteArray.getByteArray(), spdyByteArray.getDataLength());
                spdyByteArray.recycle();
                this.callback.onDataReceive(retrieveAndCopy, z);
            }
            TnetSpdySession.this.handleCallbacks(32, (Event) null);
        }

        public void spdyStreamCloseCallback(SpdySession spdySession, long j, int i, Object obj, SuperviseData superviseData) {
            if (ALog.isPrintLog(1)) {
                ALog.d(TnetSpdySession.TAG, "spdyStreamCloseCallback", this.request.getSeq(), "streamId", Long.valueOf(j), "errorCode", Integer.valueOf(i));
            }
            String str = "SUCCESS";
            if (i != 0) {
                this.statusCode = ErrorConstant.ERROR_TNET_REQUEST_FAIL;
                str = ErrorConstant.formatMsg(ErrorConstant.ERROR_TNET_REQUEST_FAIL, String.valueOf(i));
                if (i != -2005) {
                    AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_TNET_EXCEPTION, str, this.request.rs, (Throwable) null));
                }
                ALog.e(TnetSpdySession.TAG, "spdyStreamCloseCallback error", this.request.getSeq(), "session", TnetSpdySession.this.mSeq, "status code", Integer.valueOf(i), WVConstants.INTENT_EXTRA_URL, this.request.getHttpUrl().simpleUrlString());
            }
            this.request.rs.tnetErrorCode = i;
            setStatisticData(superviseData, this.statusCode, str);
            if (this.callback != null) {
                this.callback.onFinish(this.statusCode, str, this.request.rs);
            }
            if (i == -2004) {
                if (!TnetSpdySession.this.mHasUnrevPing) {
                    TnetSpdySession.this.ping(true);
                }
                if (TnetSpdySession.access$804(TnetSpdySession.this) >= 2) {
                    ConnEvent connEvent = new ConnEvent();
                    connEvent.isSuccess = false;
                    connEvent.isAccs = TnetSpdySession.this.isAccs;
                    StrategyCenter.getInstance().notifyConnEvent(TnetSpdySession.this.mRealHost, TnetSpdySession.this.mConnStrategy, connEvent);
                    TnetSpdySession.this.close(true);
                }
            }
        }

        private void setStatisticData(SuperviseData superviseData, int i, String str) {
            try {
                this.request.rs.rspEnd = System.currentTimeMillis();
                if (!this.request.rs.isDone.get()) {
                    if (i > 0) {
                        this.request.rs.ret = 1;
                    }
                    this.request.rs.statusCode = i;
                    this.request.rs.msg = str;
                    if (superviseData != null) {
                        this.request.rs.rspEnd = superviseData.responseEnd;
                        this.request.rs.sendBeforeTime = superviseData.sendStart - superviseData.requestStart;
                        this.request.rs.sendDataTime = superviseData.sendEnd - this.request.rs.sendStart;
                        this.request.rs.firstDataTime = superviseData.responseStart - superviseData.sendEnd;
                        this.request.rs.recDataTime = superviseData.responseEnd - superviseData.responseStart;
                        this.request.rs.sendDataSize = (long) (superviseData.bodySize + superviseData.compressSize);
                        this.request.rs.recDataSize = this.recDataCount + ((long) superviseData.recvUncompressSize);
                        this.request.rs.reqHeadInflateSize = (long) superviseData.uncompressSize;
                        this.request.rs.reqHeadDeflateSize = (long) superviseData.compressSize;
                        this.request.rs.reqBodyInflateSize = (long) superviseData.bodySize;
                        this.request.rs.reqBodyDeflateSize = (long) superviseData.bodySize;
                        this.request.rs.rspHeadDeflateSize = (long) superviseData.recvCompressSize;
                        this.request.rs.rspHeadInflateSize = (long) superviseData.recvUncompressSize;
                        this.request.rs.rspBodyDeflateSize = (long) superviseData.recvBodySize;
                        this.request.rs.rspBodyInflateSize = this.recDataCount;
                        if (this.request.rs.contentLength == 0) {
                            this.request.rs.contentLength = (long) superviseData.originContentLength;
                        }
                        TnetSpdySession.this.mSessionStat.recvSizeCount += (long) (superviseData.recvBodySize + superviseData.recvCompressSize);
                        TnetSpdySession.this.mSessionStat.sendSizeCount += (long) (superviseData.bodySize + superviseData.compressSize);
                    }
                }
            } catch (Exception unused) {
            }
        }

        public void spdyOnStreamResponse(SpdySession spdySession, long j, Map<String, List<String>> map, Object obj) {
            this.request.rs.firstDataTime = System.currentTimeMillis() - this.request.rs.sendStart;
            this.statusCode = HttpHelper.parseStatusCode(map);
            int unused = TnetSpdySession.this.requestTimeoutCount = 0;
            ALog.i(TnetSpdySession.TAG, "", this.request.getSeq(), "statusCode", Integer.valueOf(this.statusCode));
            ALog.i(TnetSpdySession.TAG, "", this.request.getSeq(), "response headers", map);
            if (this.callback != null) {
                this.callback.onResponseCode(this.statusCode, HttpHelper.cloneMap(map));
            }
            TnetSpdySession.this.handleCallbacks(16, (Event) null);
            this.request.rs.contentEncoding = HttpHelper.getSingleHeaderFieldByKey(map, "Content-Encoding");
            this.request.rs.contentType = HttpHelper.getSingleHeaderFieldByKey(map, "Content-Type");
            this.request.rs.contentLength = (long) HttpHelper.parseContentLength(map);
            this.request.rs.serverRT = HttpHelper.parseServerRT(map);
            TnetSpdySession.this.handleResponseCode(this.request, this.statusCode);
            TnetSpdySession.this.handleResponseHeaders(this.request, map);
            if (TnetSpdySession.this.heartbeat != null) {
                TnetSpdySession.this.heartbeat.reSchedule();
            }
        }
    }
}
