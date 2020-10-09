package anet.channel.detect;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.statist.MtuDetectStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import org.android.netutil.PingEntry;
import org.android.netutil.PingResponse;
import org.android.netutil.PingTask;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

class MTUDetector {
    private static final String MTOP_HOST = "guide-acs.m.taobao.com";
    private static final long MTU_EXPIRED_TIME = 432000000;
    private static final int MTU_MAX_TIME = 3;
    private static final String SP_MTU_DETECT = "sp_mtu_";
    private static final String TAG = "anet.MTUDetector";
    private static HashMap<String, Long> mtuDetectHistory = new HashMap<>();

    MTUDetector() {
    }

    public void register() {
        NetworkStatusHelper.addStatusChangeListener(new NetworkStatusHelper.INetworkStatusChangeListener() {
            public void onNetworkStatusChanged(final NetworkStatusHelper.NetworkStatus networkStatus) {
                ThreadPoolExecutorFactory.submitDetectTask(new Runnable() {
                    public void run() {
                        try {
                            if (networkStatus == NetworkStatusHelper.NetworkStatus.NO) {
                                return;
                            }
                            if (networkStatus != NetworkStatusHelper.NetworkStatus.NONE) {
                                MTUDetector.this.mtuDetectTask(NetworkStatusHelper.getUniqueId(networkStatus));
                            }
                        } catch (Throwable th) {
                            ALog.e(MTUDetector.TAG, "MTU detecet fail.", (String) null, th, new Object[0]);
                        }
                    }
                });
            }
        });
    }

    /* access modifiers changed from: private */
    public void mtuDetectTask(String str) {
        PingResponse pingResponse;
        String str2 = str;
        if (!AwcnConfig.isNetworkDetectEnable()) {
            ALog.e(TAG, "network detect closed.", (String) null, new Object[0]);
            return;
        }
        ALog.i(TAG, "mtuDetectTask start", (String) null, new Object[0]);
        SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        if (!TextUtils.isEmpty(str)) {
            long currentTimeMillis = System.currentTimeMillis();
            Long l = mtuDetectHistory.get(str2);
            if (l == null || currentTimeMillis >= l.longValue()) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(GlobalAppRuntimeInfo.getContext());
                long j = defaultSharedPreferences.getLong(SP_MTU_DETECT + str2, 0);
                if (currentTimeMillis < j) {
                    mtuDetectHistory.put(str2, Long.valueOf(j));
                    ALog.i(TAG, "mtuDetectTask in period of validity", (String) null, new Object[0]);
                    return;
                }
                List<IConnStrategy> connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost("guide-acs.m.taobao.com");
                String ip = (connStrategyListByHost == null || connStrategyListByHost.isEmpty()) ? null : connStrategyListByHost.get(0).getIp();
                if (!TextUtils.isEmpty(ip)) {
                    String str3 = ip;
                    Future<PingResponse> launch = new PingTask(str3, 1000, 3, 0, 0).launch();
                    Future<PingResponse> launch2 = new PingTask(str3, 1000, 3, 1172, 0).launch();
                    Future<PingResponse> launch3 = new PingTask(str3, 1000, 3, 1272, 0).launch();
                    Future<PingResponse> launch4 = new PingTask(str3, 1000, 3, 1372, 0).launch();
                    Future<PingResponse> launch5 = new PingTask(str3, 1000, 3, 1432, 0).launch();
                    try {
                        pingResponse = launch.get();
                    } catch (Exception unused) {
                        pingResponse = null;
                    }
                    if (pingResponse != null) {
                        if (pingResponse.getSuccessCnt() < 2) {
                            ALog.e(TAG, "MTU detect preTask error", (String) null, IWXUserTrackAdapter.MONITOR_ERROR_CODE, Integer.valueOf(pingResponse.getErrcode()), "successCount", Integer.valueOf(pingResponse.getSuccessCnt()));
                            return;
                        }
                        reportMtuDetectStat(1200, launch2);
                        reportMtuDetectStat(SecExceptionCode.SEC_ERROR_MALDETECT, launch3);
                        reportMtuDetectStat(SecExceptionCode.SEC_ERROR_SECURITYBODY, launch4);
                        reportMtuDetectStat(1460, launch5);
                        long j2 = currentTimeMillis + MTU_EXPIRED_TIME;
                        mtuDetectHistory.put(str2, Long.valueOf(j2));
                        defaultSharedPreferences.edit().putLong(SP_MTU_DETECT + str2, j2).apply();
                    }
                }
            }
        }
    }

    private void reportMtuDetectStat(int i, Future<PingResponse> future) {
        PingResponse pingResponse;
        try {
            pingResponse = future.get();
        } catch (Exception unused) {
            pingResponse = null;
        }
        if (pingResponse != null) {
            int successCnt = pingResponse.getSuccessCnt();
            int i2 = 3 - successCnt;
            StringBuilder sb = new StringBuilder();
            PingEntry[] results = pingResponse.getResults();
            int length = results.length;
            for (int i3 = 0; i3 < length; i3++) {
                sb.append(results[i3].rtt);
                if (i3 != length - 1) {
                    sb.append(",");
                }
            }
            ALog.i(TAG, "MTU detect result", (String) null, "mtu", Integer.valueOf(i), "successCount", Integer.valueOf(successCnt), "timeoutCount", Integer.valueOf(i2));
            MtuDetectStat mtuDetectStat = new MtuDetectStat();
            mtuDetectStat.mtu = i;
            mtuDetectStat.pingSuccessCount = successCnt;
            mtuDetectStat.pingTimeoutCount = i2;
            mtuDetectStat.rtt = sb.toString();
            mtuDetectStat.errCode = pingResponse.getErrcode();
            AppMonitor.getInstance().commitStat(mtuDetectStat);
        }
    }
}
