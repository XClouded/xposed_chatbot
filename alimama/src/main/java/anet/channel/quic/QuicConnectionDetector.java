package anet.channel.quic;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.Session;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.Event;
import anet.channel.entity.EventCb;
import anet.channel.session.TnetSpdySession;
import anet.channel.statist.QuicDetectStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.IStrategyFilter;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.StrategyResultParser;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.android.spdy.SpdyAgent;

public class QuicConnectionDetector {
    private static final long DETECT_VALID_TIME = 1800000;
    private static final String SP_QUIC_DETECTOR_HOST = "quic_detector_host";
    private static final String TAG = "awcn.QuicConnDetector";
    private static HashMap<String, Long> detectedNetworks = new HashMap<>();
    /* access modifiers changed from: private */
    public static String host;
    /* access modifiers changed from: private */
    public static AtomicBoolean isCertInit = new AtomicBoolean(false);
    private static IStrategyFilter quicFilter = new IStrategyFilter() {
        public boolean accept(IConnStrategy iConnStrategy) {
            String str = iConnStrategy.getProtocol().protocol;
            return ConnType.QUIC.equals(str) || ConnType.QUIC_PLAIN.equals(str);
        }
    };
    /* access modifiers changed from: private */
    public static AtomicInteger seq = new AtomicInteger(1);

    public static void startDetect(NetworkStatusHelper.NetworkStatus networkStatus) {
        if (!AwcnConfig.isQuicEnable()) {
            ALog.i(TAG, "startDetect", (String) null, "quic global config close.");
        } else if (NetworkStatusHelper.isConnected()) {
            if (TextUtils.isEmpty(host)) {
                ALog.e(TAG, "startDetect", (String) null, "host is null");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            Long l = detectedNetworks.get(networkStatus.getType());
            if (l == null || l.longValue() + 1800000 <= currentTimeMillis) {
                final List<IConnStrategy> connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(host, quicFilter);
                if (connStrategyListByHost.isEmpty()) {
                    ALog.e(TAG, "startDetect", (String) null, "quic strategy is null.");
                    return;
                }
                detectedNetworks.put(networkStatus.getType(), Long.valueOf(currentTimeMillis));
                ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                    public void run() {
                        if (QuicConnectionDetector.isCertInit.compareAndSet(false, true)) {
                            SpdyAgent.InitializeCerts();
                        }
                        final IConnStrategy iConnStrategy = (IConnStrategy) connStrategyListByHost.get(0);
                        TnetSpdySession tnetSpdySession = new TnetSpdySession(GlobalAppRuntimeInfo.getContext(), new ConnInfo("https://" + QuicConnectionDetector.host, "QuicDetect" + QuicConnectionDetector.seq.getAndIncrement(), iConnStrategy));
                        tnetSpdySession.registerEventcb(257, new EventCb() {
                            public void onEvent(Session session, int i, Event event) {
                                ConnEvent connEvent = new ConnEvent();
                                if (i == 1) {
                                    connEvent.isSuccess = true;
                                }
                                StrategyCenter.getInstance().notifyConnEvent(QuicConnectionDetector.host, iConnStrategy, connEvent);
                                session.close(false);
                                QuicDetectStat quicDetectStat = new QuicDetectStat(QuicConnectionDetector.host, iConnStrategy);
                                quicDetectStat.ret = connEvent.isSuccess ? 1 : 0;
                                AppMonitor.getInstance().commitStat(quicDetectStat);
                            }
                        });
                        tnetSpdySession.mSessionStat.isCommitted = true;
                        tnetSpdySession.connect();
                    }
                }, ThreadPoolExecutorFactory.Priority.LOW);
            }
        }
    }

    public static void registerListener() {
        ALog.e(TAG, "registerListener", (String) null, new Object[0]);
        final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(GlobalAppRuntimeInfo.getContext());
        host = defaultSharedPreferences.getString(SP_QUIC_DETECTOR_HOST, "");
        NetworkStatusHelper.addStatusChangeListener(new NetworkStatusHelper.INetworkStatusChangeListener() {
            public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
                QuicConnectionDetector.startDetect(networkStatus);
            }
        });
        StrategyCenter.getInstance().registerListener(new IStrategyListener() {
            public void onStrategyUpdated(StrategyResultParser.HttpDnsResponse httpDnsResponse) {
                if (httpDnsResponse != null && httpDnsResponse.dnsInfo != null) {
                    for (int i = 0; i < httpDnsResponse.dnsInfo.length; i++) {
                        String str = httpDnsResponse.dnsInfo[i].host;
                        StrategyResultParser.Aisles[] aislesArr = httpDnsResponse.dnsInfo[i].aisleses;
                        if (aislesArr != null && aislesArr.length > 0) {
                            for (StrategyResultParser.Aisles aisles : aislesArr) {
                                String str2 = aisles.protocol;
                                if (ConnType.QUIC.equals(str2) || ConnType.QUIC_PLAIN.equals(str2)) {
                                    if (!str.equals(QuicConnectionDetector.host)) {
                                        String unused = QuicConnectionDetector.host = str;
                                        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                                        edit.putString(QuicConnectionDetector.SP_QUIC_DETECTOR_HOST, QuicConnectionDetector.host);
                                        edit.apply();
                                    }
                                    QuicConnectionDetector.startDetect(NetworkStatusHelper.getStatus());
                                    return;
                                }
                            }
                            continue;
                        }
                    }
                }
            }
        });
    }
}
