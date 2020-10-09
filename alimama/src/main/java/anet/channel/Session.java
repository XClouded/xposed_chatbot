package anet.channel;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.Event;
import anet.channel.entity.EventCb;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.statist.SessionStatistic;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.StringUtils;
import com.taobao.weex.el.parse.Operators;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

public abstract class Session implements Comparable<Session> {
    private static final String TAG = "awcn.Session";
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    protected boolean autoReCreate;
    private List<Long> errorTimeList;
    private long lastAmdcRequestSend;
    protected IConnStrategy mConnStrategy;
    protected int mConnTimeout;
    protected ConnType mConnType;
    protected String mConnectIp;
    protected Context mContext;
    Map<EventCb, Integer> mEventCallBacks = new LinkedHashMap();
    protected String mHost;
    protected String mIp;
    protected boolean mIpToHost;
    private boolean mIsConnTimeOut;
    protected int mPort;
    protected String mProxyIp;
    protected int mProxyPort;
    protected int mReadTimeout;
    protected String mRealHost;
    protected Runnable mRecvTimeOutRunnable;
    public final String mSeq;
    public final SessionStatistic mSessionStat;
    protected int mStatus;
    private Future<?> timeoutTaskFuture;
    protected boolean tryNextWhenFail;
    protected String unit;

    public abstract void close();

    public void connect() {
    }

    /* access modifiers changed from: protected */
    public abstract Runnable getRecvTimeOutRunnable();

    public abstract boolean isAvailable();

    /* access modifiers changed from: protected */
    public void onDisconnect() {
    }

    public void ping(boolean z) {
    }

    public void ping(boolean z, int i) {
    }

    public abstract Cancelable request(Request request, RequestCb requestCb);

    public void sendCustomFrame(int i, byte[] bArr, int i2) {
    }

    public static class Status {
        public static final int AUTHING = 3;
        public static final int AUTH_FAIL = 5;
        public static final int AUTH_SUCC = 4;
        public static final int CONNECTED = 0;
        public static final int CONNECTING = 1;
        public static final int CONNETFAIL = 2;
        public static final int DISCONNECTED = 6;
        public static final int DISCONNECTING = 7;
        static final String[] names = {"CONNECTED", "CONNECTING", "CONNETFAIL", "AUTHING", "AUTH_SUCC", "AUTH_FAIL", "DISCONNECTED", "DISCONNECTING"};

        static String getName(int i) {
            return names[i];
        }
    }

    public int compareTo(Session session) {
        return ConnType.compare(this.mConnType, session.mConnType);
    }

    public Session(Context context, ConnInfo connInfo) {
        boolean z = false;
        this.mIsConnTimeOut = false;
        this.unit = null;
        this.mIpToHost = false;
        this.mStatus = 6;
        this.autoReCreate = false;
        this.tryNextWhenFail = true;
        this.errorTimeList = null;
        this.lastAmdcRequestSend = 0;
        this.mContext = context;
        this.mIp = connInfo.getIp();
        this.mConnectIp = this.mIp;
        this.mPort = connInfo.getPort();
        this.mConnType = connInfo.getConnType();
        this.mHost = connInfo.getHost();
        this.mRealHost = this.mHost.substring(this.mHost.indexOf(HttpConstant.SCHEME_SPLIT) + 3);
        this.mReadTimeout = connInfo.getReadTimeout();
        this.mConnTimeout = connInfo.getConnectionTimeout();
        this.mConnStrategy = connInfo.strategy;
        if (this.mConnStrategy != null && this.mConnStrategy.getIpType() == -1) {
            z = true;
        }
        this.mIpToHost = z;
        this.mSeq = connInfo.getSeq();
        this.mSessionStat = new SessionStatistic(connInfo);
        this.mSessionStat.host = this.mRealHost;
    }

    public void checkAvailable() {
        ping(true);
    }

    public static void configTnetALog(Context context, String str, int i, int i2) {
        SpdyAgent instance = SpdyAgent.getInstance(context, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        if (instance == null || !SpdyAgent.checkLoadSucc()) {
            ALog.e("agent null or configTnetALog load so fail!!!", (String) null, "loadso", Boolean.valueOf(SpdyAgent.checkLoadSucc()));
            return;
        }
        instance.configLogFile(str, i, i2);
    }

    public void close(boolean z) {
        this.autoReCreate = z;
        close();
    }

    public void registerEventcb(int i, EventCb eventCb) {
        if (this.mEventCallBacks != null) {
            this.mEventCallBacks.put(eventCb, Integer.valueOf(i));
        }
    }

    /* access modifiers changed from: protected */
    public void unReceiveEventCb(EventCb eventCb) {
        if (this.mEventCallBacks != null) {
            this.mEventCallBacks.remove(eventCb);
        }
    }

    public String getIp() {
        return this.mIp;
    }

    public int getPort() {
        return this.mPort;
    }

    public ConnType getConnType() {
        return this.mConnType;
    }

    public String getHost() {
        return this.mHost;
    }

    public String getRealHost() {
        return this.mRealHost;
    }

    public IConnStrategy getConnStrategy() {
        return this.mConnStrategy;
    }

    public String getUnit() {
        return this.unit;
    }

    /* access modifiers changed from: protected */
    public void handleCallbacks(final int i, final Event event) {
        executorService.submit(new Runnable() {
            public void run() {
                try {
                    if (Session.this.mEventCallBacks != null) {
                        for (EventCb next : Session.this.mEventCallBacks.keySet()) {
                            if (!(next == null || (Session.this.mEventCallBacks.get(next).intValue() & i) == 0)) {
                                try {
                                    next.onEvent(Session.this, i, event);
                                } catch (Exception e) {
                                    ALog.e(Session.TAG, e.toString(), Session.this.mSeq, new Object[0]);
                                }
                            }
                        }
                    }
                } catch (Exception e2) {
                    ALog.e(Session.TAG, "handleCallbacks", Session.this.mSeq, e2, new Object[0]);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005f, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void notifyStatus(int r9, anet.channel.entity.Event r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.lang.String r0 = "awcn.Session"
            java.lang.String r1 = "notifyStatus"
            java.lang.String r2 = r8.mSeq     // Catch:{ all -> 0x0060 }
            r3 = 2
            java.lang.Object[] r4 = new java.lang.Object[r3]     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = "status"
            r6 = 0
            r4[r6] = r5     // Catch:{ all -> 0x0060 }
            java.lang.String r5 = anet.channel.Session.Status.getName(r9)     // Catch:{ all -> 0x0060 }
            r7 = 1
            r4[r7] = r5     // Catch:{ all -> 0x0060 }
            anet.channel.util.ALog.e(r0, r1, r2, r4)     // Catch:{ all -> 0x0060 }
            int r0 = r8.mStatus     // Catch:{ all -> 0x0060 }
            if (r9 != r0) goto L_0x002a
            java.lang.String r9 = "awcn.Session"
            java.lang.String r10 = "ignore notifyStatus"
            java.lang.String r0 = r8.mSeq     // Catch:{ all -> 0x0060 }
            java.lang.Object[] r1 = new java.lang.Object[r6]     // Catch:{ all -> 0x0060 }
            anet.channel.util.ALog.i(r9, r10, r0, r1)     // Catch:{ all -> 0x0060 }
            monitor-exit(r8)
            return
        L_0x002a:
            r8.mStatus = r9     // Catch:{ all -> 0x0060 }
            int r9 = r8.mStatus     // Catch:{ all -> 0x0060 }
            switch(r9) {
                case 0: goto L_0x005b;
                case 1: goto L_0x005e;
                case 2: goto L_0x0055;
                case 3: goto L_0x005e;
                case 4: goto L_0x0043;
                case 5: goto L_0x003d;
                case 6: goto L_0x0032;
                case 7: goto L_0x005e;
                default: goto L_0x0031;
            }     // Catch:{ all -> 0x0060 }
        L_0x0031:
            goto L_0x005e
        L_0x0032:
            r8.onDisconnect()     // Catch:{ all -> 0x0060 }
            boolean r9 = r8.mIsConnTimeOut     // Catch:{ all -> 0x0060 }
            if (r9 != 0) goto L_0x005e
            r8.handleCallbacks(r3, r10)     // Catch:{ all -> 0x0060 }
            goto L_0x005e
        L_0x003d:
            r9 = 1024(0x400, float:1.435E-42)
            r8.handleCallbacks(r9, r10)     // Catch:{ all -> 0x0060 }
            goto L_0x005e
        L_0x0043:
            anet.channel.strategy.IStrategyInstance r9 = anet.channel.strategy.StrategyCenter.getInstance()     // Catch:{ all -> 0x0060 }
            java.lang.String r0 = r8.mRealHost     // Catch:{ all -> 0x0060 }
            java.lang.String r9 = r9.getUnitByHost(r0)     // Catch:{ all -> 0x0060 }
            r8.unit = r9     // Catch:{ all -> 0x0060 }
            r9 = 512(0x200, float:7.175E-43)
            r8.handleCallbacks(r9, r10)     // Catch:{ all -> 0x0060 }
            goto L_0x005e
        L_0x0055:
            r9 = 256(0x100, float:3.59E-43)
            r8.handleCallbacks(r9, r10)     // Catch:{ all -> 0x0060 }
            goto L_0x005e
        L_0x005b:
            r8.handleCallbacks(r7, r10)     // Catch:{ all -> 0x0060 }
        L_0x005e:
            monitor-exit(r8)
            return
        L_0x0060:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.Session.notifyStatus(int, anet.channel.entity.Event):void");
    }

    /* access modifiers changed from: protected */
    public void setPingTimeout(int i) {
        if (this.mRecvTimeOutRunnable == null) {
            this.mRecvTimeOutRunnable = getRecvTimeOutRunnable();
        }
        cancelTimeout();
        if (this.mRecvTimeOutRunnable != null) {
            this.timeoutTaskFuture = ThreadPoolExecutorFactory.submitScheduledTask(this.mRecvTimeOutRunnable, (long) i, TimeUnit.MILLISECONDS);
        }
    }

    /* access modifiers changed from: protected */
    public void cancelTimeout() {
        if (this.mRecvTimeOutRunnable != null && this.timeoutTaskFuture != null) {
            this.timeoutTaskFuture.cancel(true);
        }
    }

    public String toString() {
        return "Session@[" + this.mSeq + '|' + this.mConnType + Operators.ARRAY_END;
    }

    /* access modifiers changed from: protected */
    public void handleResponseCode(Request request, int i) {
        if (request.getHeaders().containsKey("x-pv") && i >= 500 && i < 600) {
            synchronized (this) {
                if (this.errorTimeList == null) {
                    this.errorTimeList = new LinkedList();
                }
                if (this.errorTimeList.size() < 5) {
                    this.errorTimeList.add(Long.valueOf(System.currentTimeMillis()));
                } else {
                    long longValue = this.errorTimeList.remove(0).longValue();
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - longValue <= 60000) {
                        StrategyCenter.getInstance().forceRefreshStrategy(request.getHost());
                        this.errorTimeList.clear();
                    } else {
                        this.errorTimeList.add(Long.valueOf(currentTimeMillis));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleResponseHeaders(Request request, Map<String, List<String>> map) {
        try {
            if (map.containsKey(HttpConstant.X_SWITCH_UNIT)) {
                String singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map, HttpConstant.X_SWITCH_UNIT);
                if (TextUtils.isEmpty(singleHeaderFieldByKey)) {
                    singleHeaderFieldByKey = null;
                }
                if (!StringUtils.isStringEqual(this.unit, singleHeaderFieldByKey)) {
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.lastAmdcRequestSend > 60000) {
                        StrategyCenter.getInstance().forceRefreshStrategy(request.getHost());
                        this.lastAmdcRequestSend = currentTimeMillis;
                    }
                }
            }
        } catch (Exception unused) {
        }
    }
}
