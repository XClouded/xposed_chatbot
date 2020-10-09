package anet.channel;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;
import anet.channel.Config;
import anet.channel.detect.NetworkDetector;
import anet.channel.entity.ConnType;
import anet.channel.entity.ENV;
import anet.channel.entity.SessionType;
import anet.channel.quic.QuicConnectionDetector;
import anet.channel.security.ISecurity;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.IAmdcSign;
import anet.channel.util.ALog;
import anet.channel.util.AppLifecycle;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.SessionSeq;
import anet.channel.util.StringUtils;
import anet.channel.util.Utils;
import anetwork.channel.util.RequestConstant;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

public class SessionCenter {
    public static final String TAG = "awcn.SessionCenter";
    static Map<Config, SessionCenter> instancesMap = new HashMap();
    /* access modifiers changed from: private */
    public static boolean mInit = false;
    final AccsSessionManager accsSessionManager;
    final SessionAttributeManager attributeManager = new SessionAttributeManager();
    Config config;
    Context context = GlobalAppRuntimeInfo.getContext();
    final InnerListener innerListener = new InnerListener();
    String seqNum;
    final SessionPool sessionPool = new SessionPool();
    final LruCache<String, SessionRequest> srCache = new LruCache<>(32);

    public static synchronized void init(Context context2) {
        synchronized (SessionCenter.class) {
            if (context2 != null) {
                GlobalAppRuntimeInfo.setContext(context2.getApplicationContext());
                if (!mInit) {
                    instancesMap.put(Config.DEFAULT_CONFIG, new SessionCenter(Config.DEFAULT_CONFIG));
                    AppLifecycle.initialize();
                    NetworkStatusHelper.startListener(context2);
                    if (!AwcnConfig.isTbNextLaunch()) {
                        StrategyCenter.getInstance().initialize(GlobalAppRuntimeInfo.getContext());
                    }
                    if (GlobalAppRuntimeInfo.isTargetProcess()) {
                        NetworkDetector.registerListener();
                        QuicConnectionDetector.registerListener();
                    }
                    mInit = true;
                }
            } else {
                ALog.e(TAG, "context is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. context is null");
            }
        }
    }

    @Deprecated
    public static synchronized void init(Context context2, String str) {
        synchronized (SessionCenter.class) {
            init(context2, str, GlobalAppRuntimeInfo.getEnv());
        }
    }

    public static synchronized void init(Context context2, String str, ENV env) {
        synchronized (SessionCenter.class) {
            if (context2 != null) {
                Config config2 = Config.getConfig(str, env);
                if (config2 == null) {
                    config2 = new Config.Builder().setAppkey(str).setEnv(env).build();
                }
                init(context2, config2);
            } else {
                ALog.e(TAG, "context is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. context is null");
            }
        }
    }

    public static synchronized void init(Context context2, Config config2) {
        synchronized (SessionCenter.class) {
            if (context2 == null) {
                ALog.e(TAG, "context is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. context is null");
            } else if (config2 != null) {
                init(context2);
                if (!instancesMap.containsKey(config2)) {
                    instancesMap.put(config2, new SessionCenter(config2));
                }
            } else {
                ALog.e(TAG, "paramter config is null!", (String) null, new Object[0]);
                throw new NullPointerException("init failed. config is null");
            }
        }
    }

    private SessionCenter(Config config2) {
        this.config = config2;
        this.seqNum = config2.getAppkey();
        this.innerListener.registerAll();
        this.accsSessionManager = new AccsSessionManager(this);
        if (!config2.getAppkey().equals("[default]")) {
            final ISecurity security = config2.getSecurity();
            final String appkey = config2.getAppkey();
            AmdcRuntimeInfo.setSign(new IAmdcSign() {
                public String getAppkey() {
                    return appkey;
                }

                public String sign(String str) {
                    return security.sign(SessionCenter.this.context, ISecurity.SIGN_ALGORITHM_HMAC_SHA1, getAppkey(), str);
                }

                public boolean useSecurityGuard() {
                    return !security.isSecOff();
                }
            });
        }
    }

    @Deprecated
    public synchronized void switchEnv(ENV env) {
        switchEnvironment(env);
    }

    public static synchronized void switchEnvironment(ENV env) {
        synchronized (SessionCenter.class) {
            try {
                if (GlobalAppRuntimeInfo.getEnv() != env) {
                    ALog.i(TAG, "switch env", (String) null, "old", GlobalAppRuntimeInfo.getEnv(), "new", env);
                    GlobalAppRuntimeInfo.setEnv(env);
                    StrategyCenter.getInstance().switchEnv();
                    SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION).switchAccsServer(env == ENV.TEST ? 0 : 1);
                }
                Iterator<Map.Entry<Config, SessionCenter>> it = instancesMap.entrySet().iterator();
                while (it.hasNext()) {
                    SessionCenter sessionCenter = (SessionCenter) it.next().getValue();
                    if (sessionCenter.config.getEnv() != env) {
                        ALog.i(TAG, "remove instance", sessionCenter.seqNum, RequestConstant.ENVIRONMENT, sessionCenter.config.getEnv());
                        sessionCenter.accsSessionManager.forceCloseSession(false);
                        sessionCenter.innerListener.unRegisterAll();
                        it.remove();
                    }
                }
            } catch (Throwable th) {
                ALog.e(TAG, "switch env error.", (String) null, th, new Object[0]);
            }
        }
        return;
    }

    public static synchronized SessionCenter getInstance(String str) {
        SessionCenter instance;
        synchronized (SessionCenter.class) {
            Config configByTag = Config.getConfigByTag(str);
            if (configByTag != null) {
                instance = getInstance(configByTag);
            } else {
                throw new RuntimeException("tag not exist!");
            }
        }
        return instance;
    }

    public static synchronized SessionCenter getInstance(Config config2) {
        SessionCenter sessionCenter;
        Context appContext;
        synchronized (SessionCenter.class) {
            if (config2 != null) {
                if (!mInit && (appContext = Utils.getAppContext()) != null) {
                    init(appContext);
                }
                sessionCenter = instancesMap.get(config2);
                if (sessionCenter == null) {
                    sessionCenter = new SessionCenter(config2);
                    instancesMap.put(config2, sessionCenter);
                }
            } else {
                throw new NullPointerException("config is null!");
            }
        }
        return sessionCenter;
    }

    @Deprecated
    public static synchronized SessionCenter getInstance() {
        Context appContext;
        synchronized (SessionCenter.class) {
            if (!mInit && (appContext = Utils.getAppContext()) != null) {
                init(appContext);
            }
            SessionCenter sessionCenter = null;
            for (Map.Entry next : instancesMap.entrySet()) {
                SessionCenter sessionCenter2 = (SessionCenter) next.getValue();
                if (next.getKey() != Config.DEFAULT_CONFIG) {
                    return sessionCenter2;
                }
                sessionCenter = sessionCenter2;
            }
            return sessionCenter;
        }
    }

    public Session getThrowsException(String str, long j) throws Exception {
        return getInternal(HttpUrl.parse(str), SessionType.ALL, j, (SessionGetCallback) null);
    }

    @Deprecated
    public Session getThrowsException(String str, ConnType.TypeLevel typeLevel, long j) throws Exception {
        return getInternal(HttpUrl.parse(str), typeLevel == ConnType.TypeLevel.SPDY ? SessionType.LONG_LINK : SessionType.SHORT_LINK, j, (SessionGetCallback) null);
    }

    public Session getThrowsException(HttpUrl httpUrl, int i, long j) throws Exception {
        return getInternal(httpUrl, i, j, (SessionGetCallback) null);
    }

    @Deprecated
    public Session getThrowsException(HttpUrl httpUrl, ConnType.TypeLevel typeLevel, long j) throws Exception {
        return getInternal(httpUrl, typeLevel == ConnType.TypeLevel.SPDY ? SessionType.LONG_LINK : SessionType.SHORT_LINK, j, (SessionGetCallback) null);
    }

    public Session get(String str, long j) {
        return get(HttpUrl.parse(str), SessionType.ALL, j);
    }

    @Deprecated
    public Session get(String str, ConnType.TypeLevel typeLevel, long j) {
        return get(HttpUrl.parse(str), typeLevel == ConnType.TypeLevel.SPDY ? SessionType.LONG_LINK : SessionType.SHORT_LINK, j);
    }

    @Deprecated
    public Session get(HttpUrl httpUrl, ConnType.TypeLevel typeLevel, long j) {
        return get(httpUrl, typeLevel == ConnType.TypeLevel.SPDY ? SessionType.LONG_LINK : SessionType.SHORT_LINK, j);
    }

    public Session get(HttpUrl httpUrl, int i, long j) {
        try {
            return getInternal(httpUrl, i, j, (SessionGetCallback) null);
        } catch (InvalidParameterException e) {
            ALog.e(TAG, "[Get]param url is invalid", this.seqNum, e, "url", httpUrl);
            return null;
        } catch (TimeoutException e2) {
            ALog.e(TAG, "[Get]timeout exception", this.seqNum, e2, "url", httpUrl.urlString());
            return null;
        } catch (ConnectException e3) {
            ALog.e(TAG, "[Get]connect exception", this.seqNum, IWXUserTrackAdapter.MONITOR_ERROR_MSG, e3.getMessage(), "url", httpUrl.urlString());
            return null;
        } catch (NoAvailStrategyException e4) {
            ALog.i(TAG, "[Get]" + e4.getMessage(), this.seqNum, null, "url", httpUrl.urlString());
            return null;
        } catch (Exception e5) {
            ALog.e(TAG, "[Get]" + e5.getMessage(), this.seqNum, (Throwable) null, "url", httpUrl.urlString());
            return null;
        }
    }

    public void asyncGet(HttpUrl httpUrl, int i, long j, SessionGetCallback sessionGetCallback) {
        if (sessionGetCallback == null) {
            throw new NullPointerException("cb is null");
        } else if (j > 0) {
            try {
                Session internal = getInternal(httpUrl, i, j, sessionGetCallback);
                if (internal != null) {
                    sessionGetCallback.onSessionGetSuccess(internal);
                }
            } catch (Exception unused) {
                sessionGetCallback.onSessionGetFail();
            }
        } else {
            throw new InvalidParameterException("timeout must > 0");
        }
    }

    public void registerSessionInfo(SessionInfo sessionInfo) {
        this.attributeManager.registerSessionInfo(sessionInfo);
        if (sessionInfo.isKeepAlive) {
            this.accsSessionManager.checkAndStartSession();
        }
    }

    public void unregisterSessionInfo(String str) {
        SessionInfo unregisterSessionInfo = this.attributeManager.unregisterSessionInfo(str);
        if (unregisterSessionInfo != null && unregisterSessionInfo.isKeepAlive) {
            this.accsSessionManager.checkAndStartSession();
        }
    }

    public void registerPublicKey(String str, int i) {
        this.attributeManager.registerPublicKey(str, i);
    }

    public static void checkAndStartAccsSession() {
        for (SessionCenter sessionCenter : instancesMap.values()) {
            sessionCenter.accsSessionManager.checkAndStartSession();
        }
    }

    public void forceRecreateAccsSession() {
        this.accsSessionManager.forceCloseSession(true);
    }

    private SessionRequest getSessionRequestByUrl(HttpUrl httpUrl) {
        String cNameByHost = StrategyCenter.getInstance().getCNameByHost(httpUrl.host());
        if (cNameByHost == null) {
            cNameByHost = httpUrl.host();
        }
        String scheme = httpUrl.scheme();
        if (!httpUrl.isSchemeLocked()) {
            scheme = StrategyCenter.getInstance().getSchemeByHost(cNameByHost, scheme);
        }
        return getSessionRequest(StringUtils.concatString(scheme, HttpConstant.SCHEME_SPLIT, cNameByHost));
    }

    /* access modifiers changed from: protected */
    public Session getInternal(HttpUrl httpUrl, int i, long j, SessionGetCallback sessionGetCallback) throws Exception {
        SessionInfo sessionInfo;
        int i2 = i;
        long j2 = j;
        if (!mInit) {
            ALog.e(TAG, "getInternal not inited!", this.seqNum, new Object[0]);
            throw new IllegalStateException("getInternal not inited");
        } else if (httpUrl != null) {
            String str = this.seqNum;
            Object[] objArr = new Object[6];
            objArr[0] = "u";
            objArr[1] = httpUrl.urlString();
            objArr[2] = "sessionType";
            objArr[3] = i2 == SessionType.LONG_LINK ? "LongLink" : "ShortLink";
            objArr[4] = "timeout";
            objArr[5] = Long.valueOf(j);
            ALog.d(TAG, "getInternal", str, objArr);
            SessionRequest sessionRequestByUrl = getSessionRequestByUrl(httpUrl);
            Session session = this.sessionPool.getSession(sessionRequestByUrl, i2);
            if (session != null) {
                ALog.d(TAG, "get internal hit cache session", this.seqNum, "session", session);
            } else if (this.config != Config.DEFAULT_CONFIG || i2 == SessionType.SHORT_LINK) {
                if (!GlobalAppRuntimeInfo.isAppBackground() || i2 != SessionType.LONG_LINK || !AwcnConfig.isAccsSessionCreateForbiddenInBg() || (sessionInfo = this.attributeManager.getSessionInfo(httpUrl.host())) == null || !sessionInfo.isAccs) {
                    sessionRequestByUrl.start(this.context, i, SessionSeq.createSequenceNo(this.seqNum), sessionGetCallback, j);
                    if (sessionGetCallback == null && j2 > 0 && (i2 == SessionType.ALL || sessionRequestByUrl.getConnectingType() == i2)) {
                        sessionRequestByUrl.await(j2);
                        session = this.sessionPool.getSession(sessionRequestByUrl, i2);
                        if (session == null) {
                            throw new ConnectException("session connecting failed or timeout");
                        }
                    }
                } else {
                    ALog.w(TAG, "app background, forbid to create accs session", this.seqNum, new Object[0]);
                    throw new ConnectException("accs session connecting forbidden in background");
                }
            } else if (sessionGetCallback == null) {
                return null;
            } else {
                sessionGetCallback.onSessionGetFail();
                return null;
            }
            return session;
        } else {
            throw new InvalidParameterException("httpUrl is null");
        }
    }

    @Deprecated
    public void enterBackground() {
        AppLifecycle.onBackground();
    }

    @Deprecated
    public void enterForeground() {
        AppLifecycle.onForeground();
    }

    /* access modifiers changed from: private */
    public void checkStrategy(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
        try {
            StrategyResultParser.DnsInfo[] dnsInfoArr = httpDnsResponse.dnsInfo;
            for (StrategyResultParser.DnsInfo dnsInfo : dnsInfoArr) {
                if (dnsInfo.effectNow) {
                    handleEffectNow(dnsInfo);
                }
                if (dnsInfo.unit != null) {
                    handleUnitChange(dnsInfo);
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "checkStrategy failed", this.seqNum, e, new Object[0]);
        }
    }

    private void handleUnitChange(StrategyResultParser.DnsInfo dnsInfo) {
        for (Session next : this.sessionPool.getSessions(getSessionRequest(StringUtils.buildKey(dnsInfo.safeAisles, dnsInfo.host)))) {
            if (!StringUtils.isStringEqual(next.unit, dnsInfo.unit)) {
                ALog.i(TAG, "unit change", next.mSeq, "session unit", next.unit, "unit", dnsInfo.unit);
                next.close(true);
            }
        }
    }

    private void handleEffectNow(StrategyResultParser.DnsInfo dnsInfo) {
        boolean z;
        boolean z2;
        ALog.i(TAG, "find effectNow", this.seqNum, "host", dnsInfo.host);
        StrategyResultParser.Aisles[] aislesArr = dnsInfo.aisleses;
        String[] strArr = dnsInfo.ips;
        for (Session next : this.sessionPool.getSessions(getSessionRequest(StringUtils.buildKey(dnsInfo.safeAisles, dnsInfo.host)))) {
            if (!next.getConnType().isHttpType()) {
                int i = 0;
                while (true) {
                    if (i >= strArr.length) {
                        z = false;
                        break;
                    } else if (next.getIp().equals(strArr[i])) {
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (!z) {
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "ip not match", next.mSeq, "session ip", next.getIp(), "ips", Arrays.toString(strArr));
                    }
                    next.close(true);
                } else {
                    int i2 = 0;
                    while (true) {
                        if (i2 < aislesArr.length) {
                            if (next.getPort() == aislesArr[i2].port && next.getConnType().equals(ConnType.valueOf(ConnProtocol.valueOf(aislesArr[i2])))) {
                                z2 = true;
                                break;
                            }
                            i2++;
                        } else {
                            z2 = false;
                            break;
                        }
                    }
                    if (!z2) {
                        if (ALog.isPrintLog(2)) {
                            ALog.i(TAG, "aisle not match", next.mSeq, "port", Integer.valueOf(next.getPort()), "connType", next.getConnType(), "aisle", Arrays.toString(aislesArr));
                        }
                        next.close(true);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public SessionRequest getSessionRequest(String str) {
        SessionRequest sessionRequest;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        synchronized (this.srCache) {
            sessionRequest = this.srCache.get(str);
            if (sessionRequest == null) {
                sessionRequest = new SessionRequest(str, this);
                this.srCache.put(str, sessionRequest);
            }
        }
        return sessionRequest;
    }

    private class InnerListener implements NetworkStatusHelper.INetworkStatusChangeListener, AppLifecycle.AppLifecycleListener, IStrategyListener {
        boolean foreGroundCheckRunning;

        private InnerListener() {
            this.foreGroundCheckRunning = false;
        }

        /* access modifiers changed from: package-private */
        public void registerAll() {
            AppLifecycle.registerLifecycleListener(this);
            NetworkStatusHelper.addStatusChangeListener(this);
            StrategyCenter.getInstance().registerListener(this);
        }

        /* access modifiers changed from: package-private */
        public void unRegisterAll() {
            StrategyCenter.getInstance().unregisterListener(this);
            AppLifecycle.unregisterLifecycleListener(this);
            NetworkStatusHelper.removeStatusChangeListener(this);
        }

        public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
            ALog.e(SessionCenter.TAG, "onNetworkStatusChanged.", SessionCenter.this.seqNum, "networkStatus", networkStatus);
            List<SessionRequest> infos = SessionCenter.this.sessionPool.getInfos();
            if (!infos.isEmpty()) {
                for (SessionRequest reCreateSession : infos) {
                    ALog.d(SessionCenter.TAG, "network change, try recreate session", SessionCenter.this.seqNum, new Object[0]);
                    reCreateSession.reCreateSession((String) null);
                }
            }
            SessionCenter.this.accsSessionManager.checkAndStartSession();
        }

        public void onStrategyUpdated(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
            SessionCenter.this.checkStrategy(httpDnsResponse);
            SessionCenter.this.accsSessionManager.checkAndStartSession();
        }

        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0057 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void forground() {
            /*
                r7 = this;
                java.lang.String r0 = "awcn.SessionCenter"
                java.lang.String r1 = "[forground]"
                anet.channel.SessionCenter r2 = anet.channel.SessionCenter.this
                java.lang.String r2 = r2.seqNum
                r3 = 0
                java.lang.Object[] r4 = new java.lang.Object[r3]
                anet.channel.util.ALog.i(r0, r1, r2, r4)
                anet.channel.SessionCenter r0 = anet.channel.SessionCenter.this
                android.content.Context r0 = r0.context
                if (r0 != 0) goto L_0x0015
                return
            L_0x0015:
                boolean r0 = r7.foreGroundCheckRunning
                if (r0 == 0) goto L_0x001a
                return
            L_0x001a:
                r0 = 1
                r7.foreGroundCheckRunning = r0
                boolean r1 = anet.channel.SessionCenter.mInit
                if (r1 != 0) goto L_0x0031
                java.lang.String r0 = "awcn.SessionCenter"
                java.lang.String r1 = "forground not inited!"
                anet.channel.SessionCenter r2 = anet.channel.SessionCenter.this
                java.lang.String r2 = r2.seqNum
                java.lang.Object[] r3 = new java.lang.Object[r3]
                anet.channel.util.ALog.e(r0, r1, r2, r3)
                return
            L_0x0031:
                long r1 = anet.channel.util.AppLifecycle.lastEnterBackgroundTime     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                r4 = 0
                int r6 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
                if (r6 == 0) goto L_0x0050
                long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                long r4 = anet.channel.util.AppLifecycle.lastEnterBackgroundTime     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                r6 = 0
                long r1 = r1 - r4
                r4 = 60000(0xea60, double:2.9644E-319)
                int r6 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
                if (r6 <= 0) goto L_0x0050
                anet.channel.SessionCenter r1 = anet.channel.SessionCenter.this     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                anet.channel.AccsSessionManager r1 = r1.accsSessionManager     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                r1.forceCloseSession(r0)     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                goto L_0x0057
            L_0x0050:
                anet.channel.SessionCenter r0 = anet.channel.SessionCenter.this     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                anet.channel.AccsSessionManager r0 = r0.accsSessionManager     // Catch:{ Exception -> 0x0057, all -> 0x005a }
                r0.checkAndStartSession()     // Catch:{ Exception -> 0x0057, all -> 0x005a }
            L_0x0057:
                r7.foreGroundCheckRunning = r3     // Catch:{ Exception -> 0x005e }
                goto L_0x005e
            L_0x005a:
                r0 = move-exception
                r7.foreGroundCheckRunning = r3     // Catch:{ Exception -> 0x005e }
                throw r0     // Catch:{ Exception -> 0x005e }
            L_0x005e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: anet.channel.SessionCenter.InnerListener.forground():void");
        }

        public void background() {
            ALog.i(SessionCenter.TAG, "[background]", SessionCenter.this.seqNum, new Object[0]);
            if (!SessionCenter.mInit) {
                ALog.e(SessionCenter.TAG, "background not inited!", SessionCenter.this.seqNum, new Object[0]);
                return;
            }
            try {
                StrategyCenter.getInstance().saveData();
                if (AwcnConfig.isAccsSessionCreateForbiddenInBg() && "OPPO".equalsIgnoreCase(Build.BRAND)) {
                    ALog.i(SessionCenter.TAG, "close session for OPPO", SessionCenter.this.seqNum, new Object[0]);
                    SessionCenter.this.accsSessionManager.forceCloseSession(false);
                }
            } catch (Exception unused) {
            }
        }
    }
}
