package com.taobao.accs.net;

import android.content.Context;
import android.taobao.windvane.util.WVConstants;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.strategy.IConnStrategy;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.common.Constants;
import com.taobao.accs.common.ThreadPoolExecutorFactory;
import com.taobao.accs.data.Message;
import com.taobao.accs.ut.monitor.SessionMonitor;
import com.taobao.accs.ut.statistics.MonitorStatistic;
import com.taobao.accs.ut.statistics.ReceiveMsgStat;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.LoadSoFailUtil;
import com.taobao.accs.utl.OrangeAdapter;
import com.taobao.accs.utl.UTMini;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.extension.UCCore;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import kotlin.UByte;
import org.android.spdy.AccsSSLCallback;
import org.android.spdy.RequestPriority;
import org.android.spdy.SessionCb;
import org.android.spdy.SessionInfo;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdyByteArray;
import org.android.spdy.SpdyDataProvider;
import org.android.spdy.SpdyRequest;
import org.android.spdy.SpdySession;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.android.spdy.Spdycb;
import org.android.spdy.SuperviseConnectInfo;
import org.android.spdy.SuperviseData;

public class SpdyConnection extends BaseConnection implements Spdycb, SessionCb {
    private static final int ACCS_CONN_TIMEOUT = 120000;
    private static final int CONN_TIMEOUT = 40000;
    private static final int DEFAULT_RETRY_TIME = 5000;
    private static final String HTTP_STATUS = ":status";
    private static final int MAX_RETRY_TIMES = 4;
    protected static final int MAX_TIMEOUT_DATA = 3;
    private static final int REQ_TIMEOUT = 80000;
    private static final String TAG = "SilenceConn_";
    private static final long nanoToMs = 1000000;
    /* access modifiers changed from: private */
    public long lastPingTime;
    /* access modifiers changed from: private */
    public long lastPingTimeNano;
    private SpdyAgent mAgent = null;
    /* access modifiers changed from: private */
    public boolean mCanUserProxy = false;
    private Object mConnLock = new Object();
    private long mConnStartTime;
    private long mConnStartTimeNano;
    protected ScheduledFuture<?> mConnTimoutFuture;
    private String mFinalUrl;
    /* access modifiers changed from: private */
    public HttpDnsProvider mHttpDnsProvider = new HttpDnsProvider(getChannelHost());
    protected String mIp;
    /* access modifiers changed from: private */
    public boolean mLastConnectFail = false;
    /* access modifiers changed from: private */
    public LinkedList<Message> mMessageList = new LinkedList<>();
    private MonitorStatistic mMonitorInfo;
    protected int mPort;
    /* access modifiers changed from: private */
    public String mProxy = "";
    protected String mProxyIp;
    protected int mProxyPort;
    /* access modifiers changed from: private */
    public boolean mRunning = true;
    /* access modifiers changed from: private */
    public SpdySession mSession = null;
    /* access modifiers changed from: private */
    public String mSessionId;
    /* access modifiers changed from: private */
    public SessionMonitor mStatistic;
    /* access modifiers changed from: private */
    public int mStatus = 3;
    private NetworkThread mThread;
    private String mUrl;
    private int sessionConnectInterval = -1;
    private String testUrl = null;

    /* access modifiers changed from: protected */
    public int getMaxTimeOutData() {
        return 3;
    }

    /* access modifiers changed from: protected */
    public boolean isKeepAlive() {
        return false;
    }

    public SpdyConnection(Context context, int i, String str) {
        super(context, i, str);
        initClient();
    }

    public void start() {
        this.mRunning = true;
        ALog.d(getTag(), "start", new Object[0]);
        initAwcn(this.mContext);
        if (this.mThread == null) {
            ALog.i(getTag(), "start thread", new Object[0]);
            this.mThread = new NetworkThread("NetworkThread_" + this.mConfigTag);
            this.mThread.setPriority(2);
            this.mThread.start();
        }
        ping(false, false);
    }

    public void sendMessage(final Message message, final boolean z) {
        if (!this.mRunning || message == null) {
            String tag = getTag();
            ALog.e(tag, "not running or msg null! " + this.mRunning, new Object[0]);
            return;
        }
        try {
            if (ThreadPoolExecutorFactory.getScheduledExecutor().getQueue().size() <= 1000) {
                ScheduledFuture<?> schedule = ThreadPoolExecutorFactory.getScheduledExecutor().schedule(new Runnable() {
                    public void run() {
                        synchronized (SpdyConnection.this.mMessageList) {
                            SpdyConnection.this.clearRepeatControlCommand(message);
                            if (SpdyConnection.this.mMessageList.size() == 0) {
                                SpdyConnection.this.mMessageList.add(message);
                            } else {
                                Message message = (Message) SpdyConnection.this.mMessageList.getFirst();
                                if (message.getType() != 1) {
                                    if (message.getType() != 0) {
                                        if (message.getType() != 2 || message.getType() != 2) {
                                            SpdyConnection.this.mMessageList.addLast(message);
                                        } else if (!message.force && message.force) {
                                            SpdyConnection.this.mMessageList.removeFirst();
                                            SpdyConnection.this.mMessageList.addFirst(message);
                                        }
                                    }
                                }
                                SpdyConnection.this.mMessageList.addLast(message);
                                if (message.getType() == 2) {
                                    SpdyConnection.this.mMessageList.removeFirst();
                                }
                            }
                            if (z || SpdyConnection.this.mStatus == 3) {
                                try {
                                    SpdyConnection.this.mMessageList.notifyAll();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, message.delyTime, TimeUnit.MILLISECONDS);
                if (message.getType() == 1 && message.cunstomDataId != null) {
                    if (message.isControlFrame()) {
                        cancel(message.cunstomDataId);
                    }
                    this.mMessageHandler.reqTasks.put(message.cunstomDataId, schedule);
                }
                if (message.getNetPermanceMonitor() != null) {
                    message.getNetPermanceMonitor().setDeviceId(UtilityImpl.getDeviceId(this.mContext));
                    message.getNetPermanceMonitor().setConnType(this.mConnectionType);
                    message.getNetPermanceMonitor().onEnterQueueData();
                    return;
                }
                return;
            }
            throw new RejectedExecutionException("accs");
        } catch (RejectedExecutionException unused) {
            this.mMessageHandler.onResult(message, 70008);
            String tag2 = getTag();
            ALog.e(tag2, "send queue full count:" + ThreadPoolExecutorFactory.getScheduledExecutor().getQueue().size(), new Object[0]);
        } catch (Throwable th) {
            this.mMessageHandler.onResult(message, -8);
            ALog.e(getTag(), "send error", th, new Object[0]);
        }
    }

    public void shutdown() {
        super.shutdown();
        this.mRunning = false;
        ThreadPoolExecutorFactory.getScheduledExecutor().execute(new Runnable() {
            /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
            /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x002b */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r2 = this;
                    com.taobao.accs.net.SpdyConnection r0 = com.taobao.accs.net.SpdyConnection.this
                    r0.close()
                    com.taobao.accs.net.SpdyConnection r0 = com.taobao.accs.net.SpdyConnection.this
                    com.taobao.accs.ut.monitor.SessionMonitor r0 = r0.mStatistic
                    if (r0 == 0) goto L_0x0018
                    com.taobao.accs.net.SpdyConnection r0 = com.taobao.accs.net.SpdyConnection.this
                    com.taobao.accs.ut.monitor.SessionMonitor r0 = r0.mStatistic
                    java.lang.String r1 = "shut down"
                    r0.setCloseReason(r1)
                L_0x0018:
                    com.taobao.accs.net.SpdyConnection r0 = com.taobao.accs.net.SpdyConnection.this
                    java.util.LinkedList r0 = r0.mMessageList
                    monitor-enter(r0)
                    com.taobao.accs.net.SpdyConnection r1 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Exception -> 0x002b }
                    java.util.LinkedList r1 = r1.mMessageList     // Catch:{ Exception -> 0x002b }
                    r1.notifyAll()     // Catch:{ Exception -> 0x002b }
                    goto L_0x002b
                L_0x0029:
                    r1 = move-exception
                    goto L_0x002d
                L_0x002b:
                    monitor-exit(r0)     // Catch:{ all -> 0x0029 }
                    return
                L_0x002d:
                    monitor-exit(r0)     // Catch:{ all -> 0x0029 }
                    throw r1
                */
                throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.net.SpdyConnection.AnonymousClass2.run():void");
            }
        });
        ALog.e(getTag(), "shut down", new Object[0]);
    }

    public void ping(boolean z, boolean z2) {
        String tag = getTag();
        ALog.d(tag, "try ping, force:" + z, new Object[0]);
        if (this.mConnectionType == 1) {
            ALog.d(getTag(), "INAPP, skip", new Object[0]);
        } else {
            send(Message.BuildPing(z, (int) (z2 ? Math.random() * 10.0d * 1000.0d : 0.0d)), z);
        }
    }

    public int getChannelState() {
        return this.mStatus;
    }

    public void close() {
        ALog.e(getTag(), " force close!", new Object[0]);
        try {
            this.mSession.closeSession();
            this.mStatistic.setCloseType(1);
        } catch (Exception unused) {
        }
        notifyStatus(3);
    }

    public MonitorStatistic updateMonitorInfo() {
        if (this.mMonitorInfo == null) {
            this.mMonitorInfo = new MonitorStatistic();
        }
        this.mMonitorInfo.connType = this.mConnectionType;
        this.mMonitorInfo.messageNum = this.mMessageList.size();
        this.mMonitorInfo.networkAvailable = UtilityImpl.isNetworkConnected(this.mContext);
        this.mMonitorInfo.proxy = this.mProxy;
        this.mMonitorInfo.status = this.mStatus;
        int i = 0;
        this.mMonitorInfo.tcpConnected = this.mStatistic != null && this.mStatistic.getRet();
        this.mMonitorInfo.threadIsalive = isAlive();
        MonitorStatistic monitorStatistic = this.mMonitorInfo;
        if (this.mMessageHandler != null) {
            i = this.mMessageHandler.getUnhandledCount();
        }
        monitorStatistic.unHandleMessageNum = i;
        this.mMonitorInfo.url = this.mFinalUrl;
        return this.mMonitorInfo;
    }

    /* access modifiers changed from: private */
    public void clearRepeatControlCommand(Message message) {
        if (message.command != null && this.mMessageList.size() != 0) {
            for (int size = this.mMessageList.size() - 1; size >= 0; size--) {
                Message message2 = this.mMessageList.get(size);
                if (!(message2 == null || message2.command == null || !message2.getPackageName().equals(message.getPackageName()))) {
                    switch (message.command.intValue()) {
                        case 1:
                        case 2:
                            if (message2.command.intValue() == 1 || message2.command.intValue() == 2) {
                                this.mMessageList.remove(size);
                                break;
                            }
                        case 3:
                        case 4:
                            if (message2.command.intValue() == 3 || message2.command.intValue() == 4) {
                                this.mMessageList.remove(size);
                                break;
                            }
                        case 5:
                        case 6:
                            if (message2.command.intValue() == 5 || message2.command.intValue() == 6) {
                                this.mMessageList.remove(size);
                                break;
                            }
                    }
                    ALog.d(getTag(), "clearRepeatControlCommand message:" + message2.command + "/" + message2.getPackageName(), new Object[0]);
                }
            }
            if (this.mMessageHandler != null) {
                this.mMessageHandler.cancelControlMessage(message);
            }
        }
    }

    /* access modifiers changed from: private */
    public void connect(String str) {
        SessionInfo sessionInfo;
        String str2 = str;
        if (this.mStatus != 2 && this.mStatus != 1) {
            if (this.mHttpDnsProvider == null) {
                this.mHttpDnsProvider = new HttpDnsProvider(getChannelHost());
            }
            List<IConnStrategy> availableStrategy = this.mHttpDnsProvider.getAvailableStrategy(getChannelHost());
            int i = Constants.PORT;
            if (availableStrategy == null || availableStrategy.size() <= 0) {
                if (str2 != null) {
                    this.mIp = str2;
                } else {
                    this.mIp = getChannelHost();
                }
                if (System.currentTimeMillis() % 2 == 0) {
                    i = 80;
                }
                this.mPort = i;
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_DNS, "localdns", 0.0d);
                ALog.i(getTag(), "connect get ip from amdc fail!!", new Object[0]);
            } else {
                for (IConnStrategy next : availableStrategy) {
                    if (next != null) {
                        ALog.e(getTag(), "connect", "ip", next.getIp(), "port", Integer.valueOf(next.getPort()));
                    }
                }
                if (this.mLastConnectFail) {
                    this.mHttpDnsProvider.updateStrategyPos();
                    this.mLastConnectFail = false;
                }
                IConnStrategy strategy = this.mHttpDnsProvider.getStrategy();
                this.mIp = strategy == null ? getChannelHost() : strategy.getIp();
                if (strategy != null) {
                    i = strategy.getPort();
                }
                this.mPort = i;
                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_DNS, "httpdns", 0.0d);
                ALog.e(getTag(), "connect from amdc succ", "ip", this.mIp, "port", Integer.valueOf(this.mPort), "originPos", Integer.valueOf(this.mHttpDnsProvider.getStrategyPos()));
            }
            this.mUrl = "https://" + this.mIp + ":" + this.mPort + "/accs/";
            ALog.e(getTag(), "connect", WVConstants.INTENT_EXTRA_URL, this.mUrl);
            this.mSessionId = String.valueOf(System.currentTimeMillis());
            if (this.mStatistic != null) {
                AppMonitor.getInstance().commitStat(this.mStatistic);
            }
            this.mStatistic = new SessionMonitor();
            this.mStatistic.setConnectType(this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp");
            if (this.mAgent != null) {
                try {
                    this.mConnStartTime = System.currentTimeMillis();
                    this.mConnStartTimeNano = System.nanoTime();
                    this.mProxyIp = UtilityImpl.getProxyHost(this.mContext);
                    this.mProxyPort = UtilityImpl.getProxyPort(this.mContext);
                    this.lastPingTime = System.currentTimeMillis();
                    this.mStatistic.onStartConnect();
                    notifyStatus(2);
                    synchronized (this.mConnLock) {
                        try {
                            if (TextUtils.isEmpty(this.mProxyIp) || this.mProxyPort < 0 || !this.mCanUserProxy) {
                                ALog.e(getTag(), "connect normal", new Object[0]);
                                String str3 = this.mIp;
                                int i2 = this.mPort;
                                sessionInfo = new SessionInfo(str3, i2, getChannelHost() + "_" + this.mAppkey, (String) null, 0, this.mSessionId, this, 4226);
                                this.mProxy = "";
                            } else {
                                ALog.e(getTag(), "connect", "proxy", this.mProxyIp, "port", Integer.valueOf(this.mProxyPort));
                                String str4 = this.mIp;
                                int i3 = this.mPort;
                                sessionInfo = new SessionInfo(str4, i3, getChannelHost() + "_" + this.mAppkey, this.mProxyIp, this.mProxyPort, this.mSessionId, this, 4226);
                                this.mProxy = this.mProxyIp + ":" + this.mProxyPort;
                            }
                            sessionInfo.setPubKeySeqNum(getPublicKeyType());
                            sessionInfo.setConnectionTimeoutMs(40000);
                            this.mSession = this.mAgent.createSession(sessionInfo);
                            this.mStatistic.connection_stop_date = 0;
                            this.mConnLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            this.mCanUserProxy = false;
                        }
                    }
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    private int getPublicKeyType() {
        boolean isSecurityOff = isSecurityOff();
        if (AccsClientConfig.mEnv == 2) {
            return 0;
        }
        int channelPubKey = this.mConfig.getChannelPubKey();
        if (channelPubKey <= 0) {
            return isSecurityOff ? 4 : 3;
        }
        ALog.i(getTag(), "getPublicKeyType use custom pub key", "pubKey", Integer.valueOf(channelPubKey));
        return channelPubKey;
    }

    private void auth() {
        if (this.mSession == null) {
            notifyStatus(3);
            return;
        }
        try {
            String encode = URLEncoder.encode(UtilityImpl.getDeviceId(this.mContext));
            String appsign = UtilityImpl.getAppsign(this.mContext, getAppkey(), this.mConfig.getAppSecret(), UtilityImpl.getDeviceId(this.mContext), this.mConfigTag);
            String buildAuthUrl = buildAuthUrl(this.mUrl);
            ALog.e(getTag(), "auth", "url", buildAuthUrl);
            this.mFinalUrl = buildAuthUrl;
            if (!checkParam(encode, getAppkey(), appsign)) {
                ALog.e(getTag(), "auth param error!", new Object[0]);
                onAuthFail(-6);
                return;
            }
            new URL(buildAuthUrl);
            SpdyRequest spdyRequest = new SpdyRequest(new URL(buildAuthUrl), "GET", RequestPriority.DEFAULT_PRIORITY, (int) REQ_TIMEOUT, 40000);
            spdyRequest.setDomain(getChannelHost());
            this.mSession.submitRequest(spdyRequest, new SpdyDataProvider((byte[]) null), getChannelHost(), this);
        } catch (Throwable th) {
            ALog.e(getTag(), "auth exception ", th, new Object[0]);
            onAuthFail(-7);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0036, code lost:
        if (android.text.TextUtils.isEmpty(r14) != false) goto L_0x0038;
     */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0054  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkParam(java.lang.String r12, java.lang.String r13, java.lang.String r14) {
        /*
            r11 = this;
            android.content.Context r0 = r11.mContext
            int r0 = com.taobao.accs.utl.Utils.getMode(r0)
            r1 = 2
            r2 = 1
            if (r0 != r1) goto L_0x000b
            return r2
        L_0x000b:
            boolean r0 = android.text.TextUtils.isEmpty(r12)
            r3 = 0
            if (r0 != 0) goto L_0x001e
            boolean r0 = android.text.TextUtils.isEmpty(r13)
            if (r0 != 0) goto L_0x001e
            boolean r0 = android.text.TextUtils.isEmpty(r14)
            if (r0 == 0) goto L_0x00b4
        L_0x001e:
            r0 = 3
            r11.notifyStatus(r0)
            boolean r12 = android.text.TextUtils.isEmpty(r12)
            if (r12 == 0) goto L_0x002a
        L_0x0028:
            r0 = 1
            goto L_0x0038
        L_0x002a:
            boolean r12 = android.text.TextUtils.isEmpty(r13)
            if (r12 == 0) goto L_0x0032
            r0 = 2
            goto L_0x0038
        L_0x0032:
            boolean r12 = android.text.TextUtils.isEmpty(r14)
            if (r12 == 0) goto L_0x0028
        L_0x0038:
            com.taobao.accs.ut.monitor.SessionMonitor r12 = r11.mStatistic
            r12.setFailReason(r0)
            com.taobao.accs.ut.monitor.SessionMonitor r12 = r11.mStatistic
            r12.onConnectStop()
            int r12 = r11.mConnectionType
            if (r12 != 0) goto L_0x0049
            java.lang.String r12 = "service"
            goto L_0x004b
        L_0x0049:
            java.lang.String r12 = "inapp"
        L_0x004b:
            com.taobao.accs.net.SpdyConnection$NetworkThread r13 = r11.mThread
            if (r13 == 0) goto L_0x0054
            com.taobao.accs.net.SpdyConnection$NetworkThread r13 = r11.mThread
            int r13 = r13.failTimes
            goto L_0x0055
        L_0x0054:
            r13 = 0
        L_0x0055:
            com.taobao.accs.utl.UTMini r4 = com.taobao.accs.utl.UTMini.getInstance()
            r5 = 66001(0x101d1, float:9.2487E-41)
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r6 = "DISCONNECT "
            r14.append(r6)
            r14.append(r12)
            java.lang.String r6 = r14.toString()
            java.lang.Integer r7 = java.lang.Integer.valueOf(r0)
            java.lang.Integer r8 = java.lang.Integer.valueOf(r13)
            int r12 = com.taobao.accs.common.Constants.SDK_VERSION_CODE
            java.lang.Integer r9 = java.lang.Integer.valueOf(r12)
            java.lang.String[] r10 = new java.lang.String[r1]
            java.lang.String r12 = r11.mFinalUrl
            r10[r3] = r12
            java.lang.String r12 = r11.mProxy
            r10[r2] = r12
            r4.commitEvent((int) r5, (java.lang.String) r6, (java.lang.Object) r7, (java.lang.Object) r8, (java.lang.Object) r9, (java.lang.String[]) r10)
            java.lang.String r12 = "accs"
            java.lang.String r14 = "connect"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "retrytimes:"
            r1.append(r2)
            r1.append(r13)
            java.lang.String r13 = r1.toString()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            java.lang.String r0 = ""
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            java.lang.String r1 = ""
            com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r12, r14, r13, r0, r1)
            r2 = 0
        L_0x00b4:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.net.SpdyConnection.checkParam(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    /* access modifiers changed from: private */
    public synchronized void setHeartbeat(boolean z) {
        if (this.mConnectionType != 1) {
            this.lastPingTime = System.currentTimeMillis();
            this.lastPingTimeNano = System.nanoTime();
            HeartbeatManager.getInstance(this.mContext).set();
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(Unknown Source)
        	at java.util.ArrayList.get(Unknown Source)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0048 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:44:0x009f */
    private synchronized void notifyStatus(int r10) {
        /*
            r9 = this;
            monitor-enter(r9)
            java.lang.String r0 = r9.getTag()     // Catch:{ all -> 0x00bc }
            java.lang.String r1 = "notifyStatus start"
            r2 = 2
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = "status"
            r5 = 0
            r3[r5] = r4     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = r9.getStatus(r10)     // Catch:{ all -> 0x00bc }
            r6 = 1
            r3[r6] = r4     // Catch:{ all -> 0x00bc }
            com.taobao.accs.utl.ALog.e(r0, r1, r3)     // Catch:{ all -> 0x00bc }
            int r0 = r9.mStatus     // Catch:{ all -> 0x00bc }
            if (r10 != r0) goto L_0x002a
            java.lang.String r10 = r9.getTag()     // Catch:{ all -> 0x00bc }
            java.lang.String r0 = "ignore notifyStatus"
            java.lang.Object[] r1 = new java.lang.Object[r5]     // Catch:{ all -> 0x00bc }
            com.taobao.accs.utl.ALog.i(r10, r0, r1)     // Catch:{ all -> 0x00bc }
            monitor-exit(r9)
            return
        L_0x002a:
            r9.mStatus = r10     // Catch:{ all -> 0x00bc }
            switch(r10) {
                case 1: goto L_0x0073;
                case 2: goto L_0x0056;
                case 3: goto L_0x0031;
                case 4: goto L_0x00a5;
                default: goto L_0x002f;
            }     // Catch:{ all -> 0x00bc }
        L_0x002f:
            goto L_0x00a5
        L_0x0031:
            r9.setHeartbeat(r6)     // Catch:{ all -> 0x00bc }
            android.content.Context r0 = r9.mContext     // Catch:{ all -> 0x00bc }
            com.taobao.accs.net.HeartbeatManager r0 = com.taobao.accs.net.HeartbeatManager.getInstance(r0)     // Catch:{ all -> 0x00bc }
            r0.onNetworkFail()     // Catch:{ all -> 0x00bc }
            java.lang.Object r0 = r9.mConnLock     // Catch:{ all -> 0x00bc }
            monitor-enter(r0)     // Catch:{ all -> 0x00bc }
            java.lang.Object r1 = r9.mConnLock     // Catch:{ Exception -> 0x0048 }
            r1.notifyAll()     // Catch:{ Exception -> 0x0048 }
            goto L_0x0048
        L_0x0046:
            r10 = move-exception
            goto L_0x0054
        L_0x0048:
            monitor-exit(r0)     // Catch:{ all -> 0x0046 }
            com.taobao.accs.data.MessageHandler r0 = r9.mMessageHandler     // Catch:{ all -> 0x00bc }
            r1 = -10
            r0.onNetworkFail(r1)     // Catch:{ all -> 0x00bc }
            r9.ping(r5, r6)     // Catch:{ all -> 0x00bc }
            goto L_0x00a5
        L_0x0054:
            monitor-exit(r0)     // Catch:{ all -> 0x0046 }
            throw r10     // Catch:{ all -> 0x00bc }
        L_0x0056:
            java.util.concurrent.ScheduledFuture<?> r0 = r9.mConnTimoutFuture     // Catch:{ all -> 0x00bc }
            if (r0 == 0) goto L_0x005f
            java.util.concurrent.ScheduledFuture<?> r0 = r9.mConnTimoutFuture     // Catch:{ all -> 0x00bc }
            r0.cancel(r6)     // Catch:{ all -> 0x00bc }
        L_0x005f:
            java.lang.String r0 = r9.mSessionId     // Catch:{ all -> 0x00bc }
            java.util.concurrent.ScheduledThreadPoolExecutor r1 = com.taobao.accs.common.ThreadPoolExecutorFactory.getScheduledExecutor()     // Catch:{ all -> 0x00bc }
            com.taobao.accs.net.SpdyConnection$3 r3 = new com.taobao.accs.net.SpdyConnection$3     // Catch:{ all -> 0x00bc }
            r3.<init>(r0)     // Catch:{ all -> 0x00bc }
            r7 = 120000(0x1d4c0, double:5.9288E-319)
            java.util.concurrent.TimeUnit r0 = java.util.concurrent.TimeUnit.MILLISECONDS     // Catch:{ all -> 0x00bc }
            r1.schedule(r3, r7, r0)     // Catch:{ all -> 0x00bc }
            goto L_0x00a5
        L_0x0073:
            android.content.Context r0 = r9.mContext     // Catch:{ all -> 0x00bc }
            com.taobao.accs.net.HeartbeatManager r0 = com.taobao.accs.net.HeartbeatManager.getInstance(r0)     // Catch:{ all -> 0x00bc }
            r0.resetLevel()     // Catch:{ all -> 0x00bc }
            r9.setHeartbeat(r6)     // Catch:{ all -> 0x00bc }
            java.util.concurrent.ScheduledFuture<?> r0 = r9.mConnTimoutFuture     // Catch:{ all -> 0x00bc }
            if (r0 == 0) goto L_0x0088
            java.util.concurrent.ScheduledFuture<?> r0 = r9.mConnTimoutFuture     // Catch:{ all -> 0x00bc }
            r0.cancel(r6)     // Catch:{ all -> 0x00bc }
        L_0x0088:
            java.lang.Object r0 = r9.mConnLock     // Catch:{ all -> 0x00bc }
            monitor-enter(r0)     // Catch:{ all -> 0x00bc }
            java.lang.Object r1 = r9.mConnLock     // Catch:{ Exception -> 0x0093 }
            r1.notifyAll()     // Catch:{ Exception -> 0x0093 }
            goto L_0x0093
        L_0x0091:
            r10 = move-exception
            goto L_0x00a3
        L_0x0093:
            monitor-exit(r0)     // Catch:{ all -> 0x0091 }
            java.util.LinkedList<com.taobao.accs.data.Message> r0 = r9.mMessageList     // Catch:{ all -> 0x00bc }
            monitor-enter(r0)     // Catch:{ all -> 0x00bc }
            java.util.LinkedList<com.taobao.accs.data.Message> r1 = r9.mMessageList     // Catch:{ Exception -> 0x009f }
            r1.notifyAll()     // Catch:{ Exception -> 0x009f }
            goto L_0x009f
        L_0x009d:
            r10 = move-exception
            goto L_0x00a1
        L_0x009f:
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            goto L_0x00a5
        L_0x00a1:
            monitor-exit(r0)     // Catch:{ all -> 0x009d }
            throw r10     // Catch:{ all -> 0x00bc }
        L_0x00a3:
            monitor-exit(r0)     // Catch:{ all -> 0x0091 }
            throw r10     // Catch:{ all -> 0x00bc }
        L_0x00a5:
            java.lang.String r0 = r9.getTag()     // Catch:{ all -> 0x00bc }
            java.lang.String r1 = "notifyStatus end"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x00bc }
            java.lang.String r3 = "status"
            r2[r5] = r3     // Catch:{ all -> 0x00bc }
            java.lang.String r10 = r9.getStatus(r10)     // Catch:{ all -> 0x00bc }
            r2[r6] = r10     // Catch:{ all -> 0x00bc }
            com.taobao.accs.utl.ALog.i(r0, r1, r2)     // Catch:{ all -> 0x00bc }
            monitor-exit(r9)
            return
        L_0x00bc:
            r10 = move-exception
            monitor-exit(r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.net.SpdyConnection.notifyStatus(int):void");
    }

    public String getChannelHost() {
        String channelHost = this.mConfig.getChannelHost();
        ALog.i(getTag(), "getChannelHost", "host", channelHost);
        return channelHost == null ? "" : channelHost;
    }

    private void initClient() {
        try {
            SpdyAgent.enableDebug = true;
            this.mAgent = SpdyAgent.getInstance(this.mContext, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
            if (SpdyAgent.checkLoadSucc()) {
                LoadSoFailUtil.loadSoSuccess();
                if (!isSecurityOff()) {
                    this.mAgent.setAccsSslCallback(new AccsSSLCallback() {
                        public byte[] getSSLPublicKey(int i, byte[] bArr) {
                            return UtilityImpl.staticBinarySafeDecryptNoB64(SpdyConnection.this.mContext, SpdyConnection.this.mConfigTag, SpdyConnection.this.mAppkey, bArr);
                        }
                    });
                }
                if (!OrangeAdapter.isTnetLogOff(false)) {
                    String str = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
                    ALog.d(getTag(), "into--[setTnetLogPath]", new Object[0]);
                    String tnetLogFilePath = UtilityImpl.getTnetLogFilePath(this.mContext, str);
                    String tag = getTag();
                    ALog.d(tag, "config tnet log path:" + tnetLogFilePath, new Object[0]);
                    if (!TextUtils.isEmpty(tnetLogFilePath)) {
                        this.mAgent.configLogFile(tnetLogFilePath, 5242880, 5);
                        return;
                    }
                    return;
                }
                return;
            }
            ALog.e(getTag(), "initClient", new Object[0]);
            this.mAgent = null;
            LoadSoFailUtil.loadSoFail();
        } catch (Throwable th) {
            ALog.e(getTag(), "initClient", th, new Object[0]);
        }
    }

    private class NetworkThread extends Thread {
        private final String TAG = getName();
        public int failTimes = 0;
        long lastConnectTime;

        public NetworkThread(String str) {
            super(str);
        }

        private void tryConnect(boolean z) {
            if (SpdyConnection.this.mStatus != 1) {
                ALog.d(SpdyConnection.this.getTag(), "tryConnect", "force", Boolean.valueOf(z));
                if (!UtilityImpl.isNetworkConnected(SpdyConnection.this.mContext)) {
                    ALog.e(this.TAG, "Network not available", new Object[0]);
                    return;
                }
                if (z) {
                    this.failTimes = 0;
                }
                ALog.i(this.TAG, "tryConnect", "force", Boolean.valueOf(z), "failTimes", Integer.valueOf(this.failTimes));
                if (SpdyConnection.this.mStatus != 1 && this.failTimes >= 4) {
                    boolean unused = SpdyConnection.this.mCanUserProxy = true;
                    ALog.e(this.TAG, "tryConnect fail", "maxTimes", 4);
                } else if (SpdyConnection.this.mStatus != 1) {
                    if (SpdyConnection.this.mConnectionType == 1 && this.failTimes == 0) {
                        ALog.i(this.TAG, "tryConnect in app, no sleep", new Object[0]);
                    } else {
                        ALog.i(this.TAG, "tryConnect, need sleep", new Object[0]);
                        try {
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String unused2 = SpdyConnection.this.mProxy = "";
                    if (this.failTimes == 3) {
                        SpdyConnection.this.mHttpDnsProvider.forceUpdateStrategy(SpdyConnection.this.getChannelHost());
                    }
                    SpdyConnection.this.connect((String) null);
                    SpdyConnection.this.mStatistic.setRetryTimes(this.failTimes);
                    if (SpdyConnection.this.mStatus != 1) {
                        this.failTimes++;
                        ALog.e(this.TAG, "try connect fail, ready for reconnect", new Object[0]);
                        tryConnect(false);
                        return;
                    }
                    this.lastConnectTime = System.currentTimeMillis();
                }
            } else if (SpdyConnection.this.mStatus == 1 && System.currentTimeMillis() - this.lastConnectTime > 5000) {
                this.failTimes = 0;
            }
        }

        /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
            java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
            	at java.util.ArrayList.rangeCheck(Unknown Source)
            	at java.util.ArrayList.get(Unknown Source)
            	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
            	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:698)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:225)
            	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:106)
            	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
            	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
            */
        public void run() {
            /*
                r22 = this;
                r1 = r22
                java.lang.String r2 = r1.TAG
                java.lang.String r3 = "NetworkThread run"
                r4 = 0
                java.lang.Object[] r5 = new java.lang.Object[r4]
                com.taobao.accs.utl.ALog.i(r2, r3, r5)
                r1.failTimes = r4
                r2 = 0
            L_0x000f:
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this
                boolean r3 = r3.mRunning
                if (r3 == 0) goto L_0x050e
                java.lang.String r3 = r1.TAG
                java.lang.String r5 = "ready to get message"
                java.lang.Object[] r6 = new java.lang.Object[r4]
                com.taobao.accs.utl.ALog.d(r3, r5, r6)
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this
                java.util.LinkedList r3 = r3.mMessageList
                monitor-enter(r3)
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x050a }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ all -> 0x050a }
                int r5 = r5.size()     // Catch:{ all -> 0x050a }
                if (r5 != 0) goto L_0x004e
                java.lang.String r5 = r1.TAG     // Catch:{ InterruptedException -> 0x0046 }
                java.lang.String r6 = "no message, wait"
                java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ InterruptedException -> 0x0046 }
                com.taobao.accs.utl.ALog.d(r5, r6, r7)     // Catch:{ InterruptedException -> 0x0046 }
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ InterruptedException -> 0x0046 }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ InterruptedException -> 0x0046 }
                r5.wait()     // Catch:{ InterruptedException -> 0x0046 }
                goto L_0x004e
            L_0x0046:
                r0 = move-exception
                r2 = r0
                r2.printStackTrace()     // Catch:{ all -> 0x050a }
                monitor-exit(r3)     // Catch:{ all -> 0x050a }
                goto L_0x050e
            L_0x004e:
                java.lang.String r5 = r1.TAG     // Catch:{ all -> 0x050a }
                java.lang.String r6 = "try get message"
                java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ all -> 0x050a }
                com.taobao.accs.utl.ALog.d(r5, r6, r7)     // Catch:{ all -> 0x050a }
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x050a }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ all -> 0x050a }
                int r5 = r5.size()     // Catch:{ all -> 0x050a }
                if (r5 == 0) goto L_0x007c
                com.taobao.accs.net.SpdyConnection r2 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x050a }
                java.util.LinkedList r2 = r2.mMessageList     // Catch:{ all -> 0x050a }
                java.lang.Object r2 = r2.getFirst()     // Catch:{ all -> 0x050a }
                com.taobao.accs.data.Message r2 = (com.taobao.accs.data.Message) r2     // Catch:{ all -> 0x050a }
                com.taobao.accs.ut.monitor.NetPerformanceMonitor r5 = r2.getNetPermanceMonitor()     // Catch:{ all -> 0x050a }
                if (r5 == 0) goto L_0x007c
                com.taobao.accs.ut.monitor.NetPerformanceMonitor r5 = r2.getNetPermanceMonitor()     // Catch:{ all -> 0x050a }
                r5.onTakeFromQueue()     // Catch:{ all -> 0x050a }
            L_0x007c:
                monitor-exit(r3)     // Catch:{ all -> 0x050a }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this
                boolean r3 = r3.mRunning
                if (r3 != 0) goto L_0x0087
                goto L_0x050e
            L_0x0087:
                if (r2 == 0) goto L_0x000f
                java.lang.String r3 = r1.TAG
                java.lang.String r5 = "sendMessage not null"
                java.lang.Object[] r6 = new java.lang.Object[r4]
                com.taobao.accs.utl.ALog.d(r3, r5, r6)
                r3 = 201(0xc9, float:2.82E-43)
                r5 = 100
                r7 = 1
                int r8 = r2.getType()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r9 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r10 = "sendMessage"
                r11 = 4
                java.lang.Object[] r12 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r13 = "type"
                r12[r4] = r13     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r13 = com.taobao.accs.data.Message.MsgType.name(r8)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r12[r7] = r13     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r13 = "status"
                r14 = 2
                r12[r14] = r13     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r13 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r13 = r13.mStatus     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.Integer r13 = java.lang.Integer.valueOf(r13)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r15 = 3
                r12[r15] = r13     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.i(r9, r10, r12)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 != r14) goto L_0x01b6
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r8 = r8.mConnectionType     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 != r7) goto L_0x00f2
                java.lang.String r8 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r9 = "sendMessage INAPP ping, skip"
                java.lang.Object[] r10 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.d(r8, r9, r10)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r3 = r1.TAG     // Catch:{ Throwable -> 0x0430 }
                java.lang.String r5 = "send succ, remove it"
                java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.utl.ALog.d(r3, r5, r6)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ Throwable -> 0x0430 }
                monitor-enter(r3)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x00ee }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ all -> 0x00ee }
                r5.remove(r2)     // Catch:{ all -> 0x00ee }
                monitor-exit(r3)     // Catch:{ all -> 0x00ee }
                goto L_0x000f
            L_0x00ee:
                r0 = move-exception
                r5 = r0
                monitor-exit(r3)     // Catch:{ all -> 0x00ee }
                throw r5     // Catch:{ Throwable -> 0x0430 }
            L_0x00f2:
                long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r10 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r12 = r10.lastPingTime     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r10 = 0
                long r8 = r8 - r12
                com.taobao.accs.net.SpdyConnection r10 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                android.content.Context r10 = r10.mContext     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.HeartbeatManager r10 = com.taobao.accs.net.HeartbeatManager.getInstance(r10)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r10 = r10.getInterval()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r10 = r10 - r7
                int r10 = r10 * 1000
                long r12 = (long) r10     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r10 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
                if (r10 >= 0) goto L_0x011c
                boolean r8 = r2.force     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 == 0) goto L_0x0117
                goto L_0x011c
            L_0x0117:
                r1.tryConnect(r4)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                goto L_0x02d2
            L_0x011c:
                java.lang.String r8 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r9 = "sendMessage"
                java.lang.Object[] r10 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r11 = "force"
                r10[r4] = r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                boolean r11 = r2.force     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.Boolean r11 = java.lang.Boolean.valueOf(r11)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r10[r7] = r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r11 = "last ping"
                r10[r14] = r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r13 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r13 = r13.lastPingTime     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r16 = 0
                long r11 = r11 - r13
                java.lang.Long r11 = java.lang.Long.valueOf(r11)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r10[r15] = r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.d(r8, r9, r10)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r1.tryConnect(r7)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                org.android.spdy.SpdySession r8 = r8.mSession     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 == 0) goto L_0x02ba
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r8 = r8.mStatus     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 != r7) goto L_0x02ba
                long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r10 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r10 = r10.lastPingTime     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r12 = 0
                long r8 = r8 - r10
                com.taobao.accs.net.SpdyConnection r10 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                android.content.Context r10 = r10.mContext     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.HeartbeatManager r10 = com.taobao.accs.net.HeartbeatManager.getInstance(r10)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r10 = r10.getInterval()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r10 = r10 - r7
                int r10 = r10 * 1000
                long r10 = (long) r10     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r12 < 0) goto L_0x02d2
                java.lang.String r8 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r9 = "sendMessage onSendPing"
                java.lang.Object[] r10 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.i(r8, r9, r10)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.data.MessageHandler r8 = r8.mMessageHandler     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r8.onSendPing()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                org.android.spdy.SpdySession r8 = r8.mSession     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r8.submitPing()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.ut.monitor.SessionMonitor r8 = r8.mStatistic     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r8.onSendPing()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long unused = r8.lastPingTime = r9     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r9 = java.lang.System.nanoTime()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long unused = r8.lastPingTimeNano = r9     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r8.setPingTimeOut()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                goto L_0x02d2
            L_0x01b6:
                if (r8 != r7) goto L_0x02bc
                r1.tryConnect(r7)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r8 = r8.mStatus     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 != r7) goto L_0x02ba
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                org.android.spdy.SpdySession r8 = r8.mSession     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 == 0) goto L_0x02ba
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                android.content.Context r8 = r8.mContext     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r9 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r9 = r9.mConnectionType     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                byte[] r8 = r2.build(r8, r9)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r9 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r2.setSendTime(r9)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r9 = r8.length     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r10 = 16384(0x4000, float:2.2959E-41)
                if (r9 <= r10) goto L_0x01f7
                java.lang.Integer r9 = r2.command     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r9 = r9.intValue()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r10 = 102(0x66, float:1.43E-43)
                if (r9 == r10) goto L_0x01f7
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.data.MessageHandler r8 = r8.mMessageHandler     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r9 = -4
                r8.onResult(r2, r9)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                goto L_0x02d2
            L_0x01f7:
                boolean r9 = r2.isAck     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r9 == 0) goto L_0x0205
                com.taobao.accs.data.Message$Id r9 = r2.getMsgId()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r9 = r9.getId()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r9 = -r9
                goto L_0x020d
            L_0x0205:
                com.taobao.accs.data.Message$Id r9 = r2.getMsgId()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r9 = r9.getId()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
            L_0x020d:
                com.taobao.accs.net.SpdyConnection r10 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                org.android.spdy.SpdySession r16 = r10.mSession     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r18 = 200(0xc8, float:2.8E-43)
                r19 = 0
                if (r8 != 0) goto L_0x021c
                r20 = 0
                goto L_0x021f
            L_0x021c:
                int r10 = r8.length     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r20 = r10
            L_0x021f:
                r17 = r9
                r21 = r8
                r16.sendCustomControlFrame(r17, r18, r19, r20, r21)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r10 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r12 = "send data"
                r13 = 6
                java.lang.Object[] r13 = new java.lang.Object[r13]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r16 = "length"
                r13[r4] = r16     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r8 != 0) goto L_0x0235
                r6 = 0
                goto L_0x0236
            L_0x0235:
                int r6 = r8.length     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
            L_0x0236:
                java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r13[r7] = r6     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r6 = "dataId"
                r13[r14] = r6     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r6 = r2.getDataId()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r13[r15] = r6     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r6 = "utdid"
                r13[r11] = r6     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r6 = 5
                com.taobao.accs.net.SpdyConnection r11 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r11 = r11.mUtdid     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r13[r6] = r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.e(r10, r12, r13)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.data.MessageHandler r6 = r6.mMessageHandler     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r6.onSend(r2)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                boolean r6 = r2.isAck     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r6 == 0) goto L_0x027d
                java.lang.String r6 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r10 = "sendCFrame end ack"
                java.lang.Object[] r11 = new java.lang.Object[r14]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r12 = "dataId"
                r11[r4] = r12     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.Integer r12 = java.lang.Integer.valueOf(r9)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r11[r7] = r12     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.e(r6, r10, r11)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.util.LinkedHashMap r6 = r6.mAckMessage     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r6.put(r9, r2)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
            L_0x027d:
                com.taobao.accs.ut.monitor.NetPerformanceMonitor r6 = r2.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                if (r6 == 0) goto L_0x028a
                com.taobao.accs.ut.monitor.NetPerformanceMonitor r6 = r2.getNetPermanceMonitor()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r6.onSendData()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
            L_0x028a:
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r9 = r2.getDataId()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r10 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.AccsClientConfig r10 = r10.mConfig     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                boolean r10 = r10.isQuickReconnect()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r11 = r2.timeout     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r11 = (long) r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r6.setTimeOut(r9, r10, r11)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.data.MessageHandler r6 = r6.mMessageHandler     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo r15 = new com.taobao.accs.ut.monitor.TrafficsMonitor$TrafficInfo     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r10 = r2.serviceId     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                boolean r11 = anet.channel.GlobalAppRuntimeInfo.isAppBackground()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.net.SpdyConnection r9 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r12 = r9.getChannelHost()     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                int r8 = r8.length     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                long r13 = (long) r8     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r9 = r15
                r9.<init>(r10, r11, r12, r13)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r6.addTrafficsInfo(r15)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                goto L_0x02d2
            L_0x02ba:
                r6 = 0
                goto L_0x02d3
            L_0x02bc:
                r1.tryConnect(r4)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r6 = r1.TAG     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r9 = "skip msg"
                java.lang.Object[] r10 = new java.lang.Object[r14]     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.String r11 = "type"
                r10[r4] = r11     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                r10[r7] = r8     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
                com.taobao.accs.utl.ALog.e(r6, r9, r10)     // Catch:{ Throwable -> 0x0382, all -> 0x037d }
            L_0x02d2:
                r6 = 1
            L_0x02d3:
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0379, all -> 0x0375 }
                r8.setHeartbeat(r7)     // Catch:{ Throwable -> 0x0379, all -> 0x0375 }
                if (r6 != 0) goto L_0x0355
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                r6.close()     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.ut.monitor.SessionMonitor r6 = r6.mStatistic     // Catch:{ Throwable -> 0x0430 }
                if (r6 == 0) goto L_0x02f2
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.ut.monitor.SessionMonitor r6 = r6.mStatistic     // Catch:{ Throwable -> 0x0430 }
                java.lang.String r8 = "send fail"
                r6.setCloseReason(r8)     // Catch:{ Throwable -> 0x0430 }
            L_0x02f2:
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                java.util.LinkedList r6 = r6.mMessageList     // Catch:{ Throwable -> 0x0430 }
                monitor-enter(r6)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x0351 }
                java.util.LinkedList r8 = r8.mMessageList     // Catch:{ all -> 0x0351 }
                int r8 = r8.size()     // Catch:{ all -> 0x0351 }
                int r8 = r8 - r7
            L_0x0304:
                if (r8 < 0) goto L_0x033c
                com.taobao.accs.net.SpdyConnection r7 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x0351 }
                java.util.LinkedList r7 = r7.mMessageList     // Catch:{ all -> 0x0351 }
                java.lang.Object r7 = r7.get(r8)     // Catch:{ all -> 0x0351 }
                com.taobao.accs.data.Message r7 = (com.taobao.accs.data.Message) r7     // Catch:{ all -> 0x0351 }
                if (r7 == 0) goto L_0x0339
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x0351 }
                if (r9 == 0) goto L_0x0339
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x0351 }
                int r9 = r9.intValue()     // Catch:{ all -> 0x0351 }
                if (r9 == r5) goto L_0x0328
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x0351 }
                int r9 = r9.intValue()     // Catch:{ all -> 0x0351 }
                if (r9 != r3) goto L_0x0339
            L_0x0328:
                com.taobao.accs.net.SpdyConnection r9 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x0351 }
                com.taobao.accs.data.MessageHandler r9 = r9.mMessageHandler     // Catch:{ all -> 0x0351 }
                r10 = -1
                r9.onResult(r7, r10)     // Catch:{ all -> 0x0351 }
                com.taobao.accs.net.SpdyConnection r7 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x0351 }
                java.util.LinkedList r7 = r7.mMessageList     // Catch:{ all -> 0x0351 }
                r7.remove(r8)     // Catch:{ all -> 0x0351 }
            L_0x0339:
                int r8 = r8 + -1
                goto L_0x0304
            L_0x033c:
                java.lang.String r3 = r1.TAG     // Catch:{ all -> 0x0351 }
                java.lang.String r5 = "network disconnected, wait"
                java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ all -> 0x0351 }
                com.taobao.accs.utl.ALog.e(r3, r5, r7)     // Catch:{ all -> 0x0351 }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x0351 }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ all -> 0x0351 }
                r3.wait()     // Catch:{ all -> 0x0351 }
                monitor-exit(r6)     // Catch:{ all -> 0x0351 }
                goto L_0x000f
            L_0x0351:
                r0 = move-exception
                r3 = r0
                monitor-exit(r6)     // Catch:{ all -> 0x0351 }
                throw r3     // Catch:{ Throwable -> 0x0430 }
            L_0x0355:
                java.lang.String r3 = r1.TAG     // Catch:{ Throwable -> 0x0430 }
                java.lang.String r5 = "send succ, remove it"
                java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.utl.ALog.d(r3, r5, r6)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ Throwable -> 0x0430 }
                monitor-enter(r3)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x0371 }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ all -> 0x0371 }
                r5.remove(r2)     // Catch:{ all -> 0x0371 }
                monitor-exit(r3)     // Catch:{ all -> 0x0371 }
                goto L_0x000f
            L_0x0371:
                r0 = move-exception
                r5 = r0
                monitor-exit(r3)     // Catch:{ all -> 0x0371 }
                throw r5     // Catch:{ Throwable -> 0x0430 }
            L_0x0375:
                r0 = move-exception
                r8 = r6
                goto L_0x045f
            L_0x0379:
                r0 = move-exception
                r8 = r6
                r6 = r0
                goto L_0x0385
            L_0x037d:
                r0 = move-exception
                r6 = r0
                r8 = 1
                goto L_0x0460
            L_0x0382:
                r0 = move-exception
                r6 = r0
                r8 = 1
            L_0x0385:
                java.lang.String r9 = "accs"
                java.lang.String r10 = "send_fail"
                java.lang.String r11 = r2.serviceId     // Catch:{ all -> 0x045e }
                java.lang.String r12 = "1"
                java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ all -> 0x045e }
                r13.<init>()     // Catch:{ all -> 0x045e }
                com.taobao.accs.net.SpdyConnection r14 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x045e }
                int r14 = r14.mConnectionType     // Catch:{ all -> 0x045e }
                r13.append(r14)     // Catch:{ all -> 0x045e }
                java.lang.String r14 = r6.toString()     // Catch:{ all -> 0x045e }
                r13.append(r14)     // Catch:{ all -> 0x045e }
                java.lang.String r13 = r13.toString()     // Catch:{ all -> 0x045e }
                com.taobao.accs.utl.AppMonitorAdapter.commitAlarmFail(r9, r10, r11, r12, r13)     // Catch:{ all -> 0x045e }
                r6.printStackTrace()     // Catch:{ all -> 0x045e }
                java.lang.String r9 = r1.TAG     // Catch:{ all -> 0x045e }
                java.lang.String r10 = "service connection run"
                java.lang.Object[] r11 = new java.lang.Object[r4]     // Catch:{ all -> 0x045e }
                com.taobao.accs.utl.ALog.e(r9, r10, r6, r11)     // Catch:{ all -> 0x045e }
                if (r8 != 0) goto L_0x0433
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                r6.close()     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.ut.monitor.SessionMonitor r6 = r6.mStatistic     // Catch:{ Throwable -> 0x0430 }
                if (r6 == 0) goto L_0x03cd
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.ut.monitor.SessionMonitor r6 = r6.mStatistic     // Catch:{ Throwable -> 0x0430 }
                java.lang.String r8 = "send fail"
                r6.setCloseReason(r8)     // Catch:{ Throwable -> 0x0430 }
            L_0x03cd:
                com.taobao.accs.net.SpdyConnection r6 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                java.util.LinkedList r6 = r6.mMessageList     // Catch:{ Throwable -> 0x0430 }
                monitor-enter(r6)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x042c }
                java.util.LinkedList r8 = r8.mMessageList     // Catch:{ all -> 0x042c }
                int r8 = r8.size()     // Catch:{ all -> 0x042c }
                int r8 = r8 - r7
            L_0x03df:
                if (r8 < 0) goto L_0x0417
                com.taobao.accs.net.SpdyConnection r7 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x042c }
                java.util.LinkedList r7 = r7.mMessageList     // Catch:{ all -> 0x042c }
                java.lang.Object r7 = r7.get(r8)     // Catch:{ all -> 0x042c }
                com.taobao.accs.data.Message r7 = (com.taobao.accs.data.Message) r7     // Catch:{ all -> 0x042c }
                if (r7 == 0) goto L_0x0414
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x042c }
                if (r9 == 0) goto L_0x0414
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x042c }
                int r9 = r9.intValue()     // Catch:{ all -> 0x042c }
                if (r9 == r5) goto L_0x0403
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x042c }
                int r9 = r9.intValue()     // Catch:{ all -> 0x042c }
                if (r9 != r3) goto L_0x0414
            L_0x0403:
                com.taobao.accs.net.SpdyConnection r9 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x042c }
                com.taobao.accs.data.MessageHandler r9 = r9.mMessageHandler     // Catch:{ all -> 0x042c }
                r10 = -1
                r9.onResult(r7, r10)     // Catch:{ all -> 0x042c }
                com.taobao.accs.net.SpdyConnection r7 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x042c }
                java.util.LinkedList r7 = r7.mMessageList     // Catch:{ all -> 0x042c }
                r7.remove(r8)     // Catch:{ all -> 0x042c }
            L_0x0414:
                int r8 = r8 + -1
                goto L_0x03df
            L_0x0417:
                java.lang.String r3 = r1.TAG     // Catch:{ all -> 0x042c }
                java.lang.String r5 = "network disconnected, wait"
                java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ all -> 0x042c }
                com.taobao.accs.utl.ALog.e(r3, r5, r7)     // Catch:{ all -> 0x042c }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x042c }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ all -> 0x042c }
                r3.wait()     // Catch:{ all -> 0x042c }
                monitor-exit(r6)     // Catch:{ all -> 0x042c }
                goto L_0x000f
            L_0x042c:
                r0 = move-exception
                r3 = r0
                monitor-exit(r6)     // Catch:{ all -> 0x042c }
                throw r3     // Catch:{ Throwable -> 0x0430 }
            L_0x0430:
                r0 = move-exception
                r3 = r0
                goto L_0x0453
            L_0x0433:
                java.lang.String r3 = r1.TAG     // Catch:{ Throwable -> 0x0430 }
                java.lang.String r5 = "send succ, remove it"
                java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.utl.ALog.d(r3, r5, r6)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x0430 }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ Throwable -> 0x0430 }
                monitor-enter(r3)     // Catch:{ Throwable -> 0x0430 }
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x044f }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ all -> 0x044f }
                r5.remove(r2)     // Catch:{ all -> 0x044f }
                monitor-exit(r3)     // Catch:{ all -> 0x044f }
                goto L_0x000f
            L_0x044f:
                r0 = move-exception
                r5 = r0
                monitor-exit(r3)     // Catch:{ all -> 0x044f }
                throw r5     // Catch:{ Throwable -> 0x0430 }
            L_0x0453:
                java.lang.String r5 = r1.TAG
                java.lang.String r6 = " run finally error"
                java.lang.Object[] r7 = new java.lang.Object[r4]
                com.taobao.accs.utl.ALog.e(r5, r6, r3, r7)
                goto L_0x000f
            L_0x045e:
                r0 = move-exception
            L_0x045f:
                r6 = r0
            L_0x0460:
                if (r8 != 0) goto L_0x04e1
                com.taobao.accs.net.SpdyConnection r2 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x04de }
                r2.close()     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.net.SpdyConnection r2 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.ut.monitor.SessionMonitor r2 = r2.mStatistic     // Catch:{ Throwable -> 0x04de }
                if (r2 == 0) goto L_0x047a
                com.taobao.accs.net.SpdyConnection r2 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.ut.monitor.SessionMonitor r2 = r2.mStatistic     // Catch:{ Throwable -> 0x04de }
                java.lang.String r8 = "send fail"
                r2.setCloseReason(r8)     // Catch:{ Throwable -> 0x04de }
            L_0x047a:
                com.taobao.accs.net.SpdyConnection r2 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x04de }
                java.util.LinkedList r2 = r2.mMessageList     // Catch:{ Throwable -> 0x04de }
                monitor-enter(r2)     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.net.SpdyConnection r8 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x04da }
                java.util.LinkedList r8 = r8.mMessageList     // Catch:{ all -> 0x04da }
                int r8 = r8.size()     // Catch:{ all -> 0x04da }
                int r8 = r8 - r7
            L_0x048c:
                if (r8 < 0) goto L_0x04c6
                com.taobao.accs.net.SpdyConnection r7 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x04da }
                java.util.LinkedList r7 = r7.mMessageList     // Catch:{ all -> 0x04da }
                java.lang.Object r7 = r7.get(r8)     // Catch:{ all -> 0x04da }
                com.taobao.accs.data.Message r7 = (com.taobao.accs.data.Message) r7     // Catch:{ all -> 0x04da }
                if (r7 == 0) goto L_0x04c2
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x04da }
                if (r9 == 0) goto L_0x04c2
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x04da }
                int r9 = r9.intValue()     // Catch:{ all -> 0x04da }
                if (r9 == r5) goto L_0x04b0
                java.lang.Integer r9 = r7.command     // Catch:{ all -> 0x04da }
                int r9 = r9.intValue()     // Catch:{ all -> 0x04da }
                if (r9 != r3) goto L_0x04c2
            L_0x04b0:
                com.taobao.accs.net.SpdyConnection r9 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x04da }
                com.taobao.accs.data.MessageHandler r9 = r9.mMessageHandler     // Catch:{ all -> 0x04da }
                r10 = -1
                r9.onResult(r7, r10)     // Catch:{ all -> 0x04da }
                com.taobao.accs.net.SpdyConnection r7 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x04da }
                java.util.LinkedList r7 = r7.mMessageList     // Catch:{ all -> 0x04da }
                r7.remove(r8)     // Catch:{ all -> 0x04da }
                goto L_0x04c3
            L_0x04c2:
                r10 = -1
            L_0x04c3:
                int r8 = r8 + -1
                goto L_0x048c
            L_0x04c6:
                java.lang.String r3 = r1.TAG     // Catch:{ all -> 0x04da }
                java.lang.String r5 = "network disconnected, wait"
                java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ all -> 0x04da }
                com.taobao.accs.utl.ALog.e(r3, r5, r7)     // Catch:{ all -> 0x04da }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x04da }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ all -> 0x04da }
                r3.wait()     // Catch:{ all -> 0x04da }
                monitor-exit(r2)     // Catch:{ all -> 0x04da }
                goto L_0x0509
            L_0x04da:
                r0 = move-exception
                r3 = r0
                monitor-exit(r2)     // Catch:{ all -> 0x04da }
                throw r3     // Catch:{ Throwable -> 0x04de }
            L_0x04de:
                r0 = move-exception
                r2 = r0
                goto L_0x0500
            L_0x04e1:
                java.lang.String r3 = r1.TAG     // Catch:{ Throwable -> 0x04de }
                java.lang.String r5 = "send succ, remove it"
                java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.utl.ALog.d(r3, r5, r7)     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.net.SpdyConnection r3 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ Throwable -> 0x04de }
                java.util.LinkedList r3 = r3.mMessageList     // Catch:{ Throwable -> 0x04de }
                monitor-enter(r3)     // Catch:{ Throwable -> 0x04de }
                com.taobao.accs.net.SpdyConnection r5 = com.taobao.accs.net.SpdyConnection.this     // Catch:{ all -> 0x04fc }
                java.util.LinkedList r5 = r5.mMessageList     // Catch:{ all -> 0x04fc }
                r5.remove(r2)     // Catch:{ all -> 0x04fc }
                monitor-exit(r3)     // Catch:{ all -> 0x04fc }
                goto L_0x0509
            L_0x04fc:
                r0 = move-exception
                r2 = r0
                monitor-exit(r3)     // Catch:{ all -> 0x04fc }
                throw r2     // Catch:{ Throwable -> 0x04de }
            L_0x0500:
                java.lang.String r3 = r1.TAG
                java.lang.Object[] r4 = new java.lang.Object[r4]
                java.lang.String r5 = " run finally error"
                com.taobao.accs.utl.ALog.e(r3, r5, r2, r4)
            L_0x0509:
                throw r6
            L_0x050a:
                r0 = move-exception
                r2 = r0
                monitor-exit(r3)     // Catch:{ all -> 0x050a }
                throw r2
            L_0x050e:
                com.taobao.accs.net.SpdyConnection r2 = com.taobao.accs.net.SpdyConnection.this
                r2.close()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.net.SpdyConnection.NetworkThread.run():void");
        }
    }

    public boolean isAlive() {
        return this.mRunning;
    }

    public void spdySessionFailedError(SpdySession spdySession, int i, Object obj) {
        int i2 = i;
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                Exception exc = e;
                ALog.e(getTag(), "session cleanUp has exception: " + exc, new Object[0]);
            }
        }
        int i3 = this.mThread != null ? this.mThread.failTimes : 0;
        ALog.e(getTag(), "spdySessionFailedError", "retryTimes", Integer.valueOf(i3), "errorId", Integer.valueOf(i));
        this.mCanUserProxy = false;
        this.mLastConnectFail = true;
        notifyStatus(3);
        this.mStatistic.setFailReason(i2);
        this.mStatistic.onConnectStop();
        String str = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
        UTMini.getInstance().commitEvent(66001, "DISCONNECT " + str, (Object) Integer.valueOf(i), (Object) Integer.valueOf(i3), (Object) Integer.valueOf(Constants.SDK_VERSION_CODE), this.mFinalUrl, this.mProxy);
        AppMonitorAdapter.commitAlarmFail("accs", "connect", "retrytimes:" + i3, i2 + "", "");
    }

    public void spdySessionConnectCB(SpdySession spdySession, SuperviseConnectInfo superviseConnectInfo) {
        SuperviseConnectInfo superviseConnectInfo2 = superviseConnectInfo;
        this.sessionConnectInterval = superviseConnectInfo2.connectTime;
        int i = superviseConnectInfo2.handshakeTime;
        ALog.e(getTag(), "spdySessionConnectCB", "sessionConnectInterval", Integer.valueOf(this.sessionConnectInterval), "sslTime", Integer.valueOf(i), "reuse", Integer.valueOf(superviseConnectInfo2.sessionTicketReused));
        auth();
        this.mStatistic.setRet(true);
        this.mStatistic.onConnectStop();
        this.mStatistic.tcp_time = (long) this.sessionConnectInterval;
        this.mStatistic.ssl_time = (long) i;
        String str = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
        UTMini instance = UTMini.getInstance();
        instance.commitEvent(66001, "CONNECTED " + str + Operators.SPACE_STR + superviseConnectInfo2.sessionTicketReused, (Object) String.valueOf(this.sessionConnectInterval), (Object) String.valueOf(i), (Object) Integer.valueOf(Constants.SDK_VERSION_CODE), String.valueOf(superviseConnectInfo2.sessionTicketReused), this.mFinalUrl, this.mProxy);
        AppMonitorAdapter.commitAlarmSuccess("accs", "connect", "");
    }

    public void spdySessionCloseCallback(SpdySession spdySession, Object obj, SuperviseConnectInfo superviseConnectInfo, int i) {
        SuperviseConnectInfo superviseConnectInfo2 = superviseConnectInfo;
        ALog.e(getTag(), "spdySessionCloseCallback", "errorCode", Integer.valueOf(i));
        if (spdySession != null) {
            try {
                spdySession.cleanUp();
            } catch (Exception e) {
                Exception exc = e;
                String tag = getTag();
                ALog.e(tag, "session cleanUp has exception: " + exc, new Object[0]);
            }
        }
        notifyStatus(3);
        this.mStatistic.onCloseConnect();
        if (this.mStatistic.getConCloseDate() > 0 && this.mStatistic.getConStopDate() > 0) {
            int i2 = ((this.mStatistic.getConCloseDate() - this.mStatistic.getConStopDate()) > 0 ? 1 : ((this.mStatistic.getConCloseDate() - this.mStatistic.getConStopDate()) == 0 ? 0 : -1));
        }
        this.mStatistic.setCloseReason(this.mStatistic.getCloseReason() + "tnet error:" + i);
        if (superviseConnectInfo2 != null) {
            this.mStatistic.live_time = (long) superviseConnectInfo2.keepalive_period_second;
        }
        AppMonitor.getInstance().commitStat(this.mStatistic);
        for (Message next : this.mMessageHandler.getUnhandledMessages()) {
            if (next.getNetPermanceMonitor() != null) {
                next.getNetPermanceMonitor().setFailReason("session close");
                AppMonitor.getInstance().commitStat(next.getNetPermanceMonitor());
            }
        }
        String str = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
        String tag2 = getTag();
        ALog.d(tag2, "spdySessionCloseCallback, conKeepTime:" + this.mStatistic.live_time + " connectType:" + str, new Object[0]);
        UTMini instance = UTMini.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("DISCONNECT CLOSE ");
        sb.append(str);
        instance.commitEvent(66001, sb.toString(), (Object) Integer.valueOf(i), (Object) Long.valueOf(this.mStatistic.live_time), (Object) Integer.valueOf(Constants.SDK_VERSION_CODE), this.mFinalUrl, this.mProxy);
    }

    public void spdyPingRecvCallback(SpdySession spdySession, long j, Object obj) {
        String tag = getTag();
        ALog.d(tag, "spdyPingRecvCallback uniId:" + j, new Object[0]);
        if (j >= 0) {
            this.mMessageHandler.onRcvPing();
            HeartbeatManager.getInstance(this.mContext).onHeartbeatSucc();
            HeartbeatManager.getInstance(this.mContext).set();
            this.mStatistic.onPingCBReceive();
            if (this.mStatistic.ping_rec_times % 2 == 0) {
                UtilityImpl.setServiceTime(this.mContext, Constants.SP_KEY_SERVICE_END, System.currentTimeMillis());
            }
        }
    }

    public void spdyCustomControlFrameRecvCallback(SpdySession spdySession, Object obj, int i, int i2, int i3, int i4, byte[] bArr) {
        setHeartbeat(true);
        ALog.e(getTag(), "onFrame", "type", Integer.valueOf(i2), DinamicConstant.LENGTH_PREFIX, Integer.valueOf(bArr.length));
        StringBuilder sb = new StringBuilder();
        if (ALog.isPrintLog(ALog.Level.D) && bArr.length < 512) {
            long currentTimeMillis = System.currentTimeMillis();
            for (byte b : bArr) {
                sb.append(Integer.toHexString(b & UByte.MAX_VALUE));
                sb.append(Operators.SPACE_STR);
            }
            ALog.d(getTag(), sb + " log time:" + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
        }
        if (i2 == 200) {
            try {
                long currentTimeMillis2 = System.currentTimeMillis();
                this.mMessageHandler.onMessage(bArr);
                ReceiveMsgStat receiveMsgStat = this.mMessageHandler.getReceiveMsgStat();
                if (receiveMsgStat != null) {
                    receiveMsgStat.receiveDate = String.valueOf(currentTimeMillis2);
                    receiveMsgStat.messageType = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
                    receiveMsgStat.commitUT();
                }
            } catch (Throwable th) {
                ALog.e(getTag(), "onDataReceive ", th, new Object[0]);
                UTMini.getInstance().commitEvent(66001, "SERVICE_DATA_RECEIVE", UtilityImpl.getStackMsg(th));
            }
            ALog.d(getTag(), "try handle msg", new Object[0]);
            cancelPingTimeOut();
        } else {
            ALog.e(getTag(), "drop frame", DinamicConstant.LENGTH_PREFIX, Integer.valueOf(bArr.length));
        }
        ALog.d(getTag(), "spdyCustomControlFrameRecvCallback", new Object[0]);
    }

    public void spdyStreamCloseCallback(SpdySession spdySession, long j, int i, Object obj, SuperviseData superviseData) {
        ALog.d(getTag(), "spdyStreamCloseCallback", new Object[0]);
        if (i != 0) {
            ALog.e(getTag(), "spdyStreamCloseCallback", "statusCode", Integer.valueOf(i));
            onAuthFail(i);
        }
    }

    public void spdyRequestRecvCallback(SpdySession spdySession, long j, Object obj) {
        ALog.d(getTag(), "spdyRequestRecvCallback", new Object[0]);
    }

    public void spdyOnStreamResponse(SpdySession spdySession, long j, Map<String, List<String>> map, Object obj) {
        this.lastPingTime = System.currentTimeMillis();
        this.lastPingTimeNano = System.nanoTime();
        try {
            Map<String, String> header = UtilityImpl.getHeader(map);
            ALog.d(TAG, "spdyOnStreamResponse", "header", map);
            int parseInt = Integer.parseInt(header.get(":status"));
            ALog.e(getTag(), "spdyOnStreamResponse", "httpStatusCode", Integer.valueOf(parseInt));
            if (parseInt == 200) {
                notifyStatus(1);
                String str = header.get("x-at");
                if (!TextUtils.isEmpty(str)) {
                    this.mConnToken = str;
                }
                SessionMonitor sessionMonitor = this.mStatistic;
                long j2 = 0;
                if (this.mStatistic.connection_stop_date > 0) {
                    j2 = System.currentTimeMillis() - this.mStatistic.connection_stop_date;
                }
                sessionMonitor.auth_time = j2;
                String str2 = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
                UTMini instance = UTMini.getInstance();
                instance.commitEvent(66001, "CONNECTED 200 " + str2, (Object) this.mFinalUrl, (Object) this.mProxy, (Object) Integer.valueOf(Constants.SDK_VERSION_CODE), "0");
                AppMonitorAdapter.commitAlarmSuccess("accs", "auth", "");
            } else {
                onAuthFail(parseInt);
            }
        } catch (Exception e) {
            ALog.e(getTag(), e.toString(), new Object[0]);
            close();
            this.mStatistic.setCloseReason(UCCore.EVENT_EXCEPTION);
        }
        ALog.d(getTag(), "spdyOnStreamResponse", new Object[0]);
    }

    private void onAuthFail(int i) {
        this.mConnToken = null;
        close();
        int i2 = this.mThread != null ? this.mThread.failTimes : 0;
        SessionMonitor sessionMonitor = this.mStatistic;
        sessionMonitor.setCloseReason("code not 200 is" + i);
        this.mLastConnectFail = true;
        String str = this.mConnectionType == 0 ? NotificationCompat.CATEGORY_SERVICE : "inapp";
        UTMini instance = UTMini.getInstance();
        instance.commitEvent(66001, "CONNECTED NO 200 " + str, (Object) Integer.valueOf(i), (Object) Integer.valueOf(i2), (Object) Integer.valueOf(Constants.SDK_VERSION_CODE), this.mFinalUrl, this.mProxy);
        AppMonitorAdapter.commitAlarmFail("accs", "auth", "", i + "", "");
    }

    public void spdyDataSendCallback(SpdySession spdySession, boolean z, long j, int i, Object obj) {
        ALog.d(getTag(), "spdyDataSendCallback", new Object[0]);
    }

    public void spdyDataRecvCallback(SpdySession spdySession, boolean z, long j, int i, Object obj) {
        ALog.d(getTag(), "spdyDataRecvCallback", new Object[0]);
    }

    public void notifyNetWorkChange(String str) {
        this.mCanUserProxy = false;
        this.mTimeoutMsgNum = 0;
    }

    public void bioPingRecvCallback(SpdySession spdySession, int i) {
        String tag = getTag();
        ALog.w(tag, "bioPingRecvCallback uniId:" + i, new Object[0]);
    }

    public void onTimeOut(String str, boolean z, String str2) {
        try {
            notifyStatus(4);
            close();
            this.mStatistic.setCloseReason(str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean cancel(String str) {
        boolean z;
        synchronized (this.mMessageList) {
            z = true;
            int size = this.mMessageList.size() - 1;
            while (true) {
                if (size >= 0) {
                    Message message = this.mMessageList.get(size);
                    if (message != null && message.getType() == 1 && message.cunstomDataId != null && message.cunstomDataId.equals(str)) {
                        this.mMessageList.remove(size);
                        break;
                    }
                    size--;
                } else {
                    z = false;
                    break;
                }
            }
        }
        return z;
    }

    public byte[] getSSLMeta(SpdySession spdySession) {
        return UtilityImpl.SecurityGuardGetSslTicket2(this.mContext, this.mConfigTag, this.mAppkey, spdySession.getDomain());
    }

    public int putSSLMeta(SpdySession spdySession, byte[] bArr) {
        return UtilityImpl.SecurityGuardPutSslTicket2(this.mContext, this.mConfigTag, this.mAppkey, spdySession.getDomain(), bArr);
    }

    public void spdyDataChunkRecvCB(SpdySession spdySession, boolean z, long j, SpdyByteArray spdyByteArray, Object obj) {
        ALog.d(getTag(), "spdyDataChunkRecvCB", new Object[0]);
    }

    public String getTag() {
        return TAG + this.mConfigTag;
    }

    public void spdyCustomControlFrameFailCallback(SpdySession spdySession, Object obj, int i, int i2) {
        reSendAck(i);
    }

    /* access modifiers changed from: protected */
    public void initAwcn(Context context) {
        if (!this.mAwcnInited) {
            super.initAwcn(context);
            GlobalAppRuntimeInfo.setBackground(false);
            this.mAwcnInited = true;
            ALog.i(getTag(), "init awcn success!", new Object[0]);
        }
    }

    public String getHost(String str) {
        return "https://" + this.mConfig.getChannelHost();
    }
}
