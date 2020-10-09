package anet.channel.detect;

import android.text.TextUtils;
import android.util.Pair;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import org.android.netutil.NetUtils;
import org.android.netutil.PingEntry;
import org.android.netutil.PingResponse;
import org.android.netutil.PingTask;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ExceptionDetector {
    private static final String ACCS_HOST = "msgacs.m.taobao.com";
    private static final String CDN_HOST = "gw.alicdn.com";
    private static final long EXCEPTION_EXPIRED_TIME = 1800000;
    private static final int MAX_LENGTH = 10;
    private static final String MTOP_HOST = "guide-acs.m.taobao.com";
    private static final int PING_MAX_TIME = 3;
    private static final String TAG = "anet.ExceptionDetector";
    /* access modifiers changed from: private */
    public String accsCurrentIp;
    /* access modifiers changed from: private */
    public String cdnCurrentIp;
    /* access modifiers changed from: private */
    public long exceptionDetectExpiredTime;
    /* access modifiers changed from: private */
    public String mtopCurrentIp;
    /* access modifiers changed from: private */
    public LimitedQueue<Pair<String, Integer>> recentRequestHistory = new LimitedQueue<>(10);

    ExceptionDetector() {
    }

    public void register() {
        NetworkStatusHelper.addStatusChangeListener(new NetworkStatusHelper.INetworkStatusChangeListener() {
            public void onNetworkStatusChanged(NetworkStatusHelper.NetworkStatus networkStatus) {
                ThreadPoolExecutorFactory.submitDetectTask(new Runnable() {
                    public void run() {
                        ExceptionDetector.this.recentRequestHistory.clear();
                        long unused = ExceptionDetector.this.exceptionDetectExpiredTime = 0;
                    }
                });
            }
        });
    }

    public void commitDetect(final RequestStatistic requestStatistic) {
        if (!AwcnConfig.isNetworkDetectEnable()) {
            ALog.e(TAG, "network detect closed.", (String) null, new Object[0]);
        } else {
            ThreadPoolExecutorFactory.submitDetectTask(new Runnable() {
                public void run() {
                    try {
                        if (requestStatistic != null) {
                            if (!TextUtils.isEmpty(requestStatistic.ip) && requestStatistic.ret == 0) {
                                if ("guide-acs.m.taobao.com".equalsIgnoreCase(requestStatistic.host)) {
                                    String unused = ExceptionDetector.this.mtopCurrentIp = requestStatistic.ip;
                                } else if (ExceptionDetector.ACCS_HOST.equalsIgnoreCase(requestStatistic.host)) {
                                    String unused2 = ExceptionDetector.this.accsCurrentIp = requestStatistic.ip;
                                } else if (ExceptionDetector.CDN_HOST.equalsIgnoreCase(requestStatistic.host)) {
                                    String unused3 = ExceptionDetector.this.cdnCurrentIp = requestStatistic.ip;
                                }
                            }
                            if (!TextUtils.isEmpty(requestStatistic.url)) {
                                ExceptionDetector.this.recentRequestHistory.add(Pair.create(requestStatistic.url, Integer.valueOf(requestStatistic.statusCode)));
                            }
                            if (ExceptionDetector.this.isNeedExceptionDetect()) {
                                ExceptionDetector.this.exceptionDetectTask();
                            }
                        }
                    } catch (Throwable th) {
                        ALog.e(ExceptionDetector.TAG, "network detect fail.", (String) null, th, new Object[0]);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void exceptionDetectTask() throws JSONException {
        ALog.e(TAG, "network detect start.", (String) null, new Object[0]);
        SpdyAgent.getInstance(GlobalAppRuntimeInfo.getContext(), SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.getStatus();
        jSONObject2.put("status", status.getType());
        jSONObject2.put("subType", NetworkStatusHelper.getNetworkSubType());
        if (status != NetworkStatusHelper.NetworkStatus.NO) {
            if (status.isMobile()) {
                jSONObject2.put("apn", NetworkStatusHelper.getApn());
                jSONObject2.put(DispatchConstants.CARRIER, NetworkStatusHelper.getCarrier());
            } else {
                jSONObject2.put(DispatchConstants.BSSID, NetworkStatusHelper.getWifiBSSID());
                jSONObject2.put("ssid", NetworkStatusHelper.getWifiSSID());
            }
            jSONObject2.put("proxy", NetworkStatusHelper.getProxyType());
        }
        jSONObject.put("NetworkInfo", jSONObject2);
        String defaultGateway = status.isWifi() ? NetUtils.getDefaultGateway("114.114.114.114") : NetUtils.getPreferNextHop("114.114.114.114", 2);
        Future<PingResponse> launch = !TextUtils.isEmpty(defaultGateway) ? new PingTask(defaultGateway, 1000, 3, 0, 0).launch() : null;
        DetectInfo buildPingInfo = buildPingInfo("guide-acs.m.taobao.com", this.mtopCurrentIp);
        DetectInfo buildPingInfo2 = buildPingInfo(CDN_HOST, this.cdnCurrentIp);
        DetectInfo buildPingInfo3 = buildPingInfo(ACCS_HOST, this.accsCurrentIp);
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("nextHop", defaultGateway);
        jSONObject3.put("ping", getPingResponse(launch));
        jSONObject.put("LocalDetect", jSONObject3);
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(getPingResult(buildPingInfo));
        jSONArray.put(getPingResult(buildPingInfo2));
        jSONArray.put(getPingResult(buildPingInfo3));
        jSONObject.put("InternetDetect", jSONArray);
        JSONObject jSONObject4 = new JSONObject();
        Iterator it = this.recentRequestHistory.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            jSONObject4.put((String) pair.first, pair.second);
        }
        jSONObject.put("BizDetect", jSONObject4);
        this.recentRequestHistory.clear();
        ALog.e(TAG, "network detect result: " + jSONObject.toString(), (String) null, new Object[0]);
    }

    /* access modifiers changed from: private */
    public boolean isNeedExceptionDetect() {
        boolean z = false;
        if (this.recentRequestHistory.size() != 10) {
            return false;
        }
        if (NetworkStatusHelper.getStatus() == NetworkStatusHelper.NetworkStatus.NO) {
            ALog.e(TAG, "no network", (String) null, new Object[0]);
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis < this.exceptionDetectExpiredTime) {
            return false;
        }
        Iterator it = this.recentRequestHistory.iterator();
        int i = 0;
        while (it.hasNext()) {
            int intValue = ((Integer) ((Pair) it.next()).second).intValue();
            if (intValue == -202 || intValue == -400 || intValue == -401 || intValue == -405 || intValue == -406) {
                i++;
            }
        }
        if (i * 2 > 10) {
            z = true;
        }
        if (z) {
            this.exceptionDetectExpiredTime = currentTimeMillis + 1800000;
        }
        return z;
    }

    private ArrayList<String> traceRoute(String str, int i) {
        PingResponse pingResponse;
        ArrayList<String> arrayList = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return arrayList;
        }
        int i2 = 0;
        while (i2 < i) {
            i2++;
            try {
                pingResponse = new PingTask(str, 0, 1, 0, i2).launch().get();
            } catch (Exception unused) {
                pingResponse = null;
            }
            StringBuilder sb = new StringBuilder();
            if (pingResponse != null) {
                String lastHopIPStr = pingResponse.getLastHopIPStr();
                double d = pingResponse.getResults()[0].rtt;
                int errcode = pingResponse.getErrcode();
                if (TextUtils.isEmpty(lastHopIPStr)) {
                    lastHopIPStr = "*";
                }
                sb.append("hop=");
                sb.append(lastHopIPStr);
                sb.append(",rtt=");
                sb.append(d);
                sb.append(",errCode=");
                sb.append(errcode);
            }
            arrayList.add(sb.toString());
        }
        return arrayList;
    }

    private DetectInfo buildPingInfo(String str, String str2) {
        DetectInfo detectInfo = new DetectInfo();
        detectInfo.host = str;
        try {
            detectInfo.localIp = InetAddress.getByName(str).getHostAddress();
        } catch (UnknownHostException unused) {
        }
        if (!TextUtils.isEmpty(str2)) {
            detectInfo.currentIp = str2;
        } else {
            List<IConnStrategy> connStrategyListByHost = StrategyCenter.getInstance().getConnStrategyListByHost(str);
            if (connStrategyListByHost != null && !connStrategyListByHost.isEmpty()) {
                detectInfo.currentIp = connStrategyListByHost.get(0).getIp();
            }
        }
        String str3 = !TextUtils.isEmpty(detectInfo.currentIp) ? detectInfo.currentIp : detectInfo.localIp;
        if (!TextUtils.isEmpty(str3)) {
            String str4 = str3;
            detectInfo.defaultFuture = new PingTask(str4, 1000, 3, 0, 0).launch();
            detectInfo.mtu1200Future = new PingTask(str4, 1000, 3, 1172, 0).launch();
            detectInfo.mtu1460Future = new PingTask(str4, 1000, 3, 1432, 0).launch();
        }
        return detectInfo;
    }

    private JSONObject getPingResult(DetectInfo detectInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (detectInfo == null || detectInfo.defaultFuture == null) {
            return jSONObject;
        }
        jSONObject.put("host", detectInfo.host);
        jSONObject.put("currentIp", detectInfo.currentIp);
        jSONObject.put("localIp", detectInfo.localIp);
        jSONObject.put("ping", getPingResponse(detectInfo.defaultFuture));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("1200", getPingResponse(detectInfo.mtu1200Future));
        jSONObject2.put("1460", getPingResponse(detectInfo.mtu1460Future));
        jSONObject.put("MTU", jSONObject2);
        if ("guide-acs.m.taobao.com".equals(detectInfo.host)) {
            ArrayList<String> traceRoute = traceRoute(!TextUtils.isEmpty(detectInfo.currentIp) ? detectInfo.currentIp : detectInfo.localIp, 5);
            JSONObject jSONObject3 = new JSONObject();
            int i = 0;
            while (i < traceRoute.size()) {
                int i2 = i + 1;
                jSONObject3.put(String.valueOf(i2), traceRoute.get(i));
                i = i2;
            }
            jSONObject.put("traceRoute", jSONObject3);
        }
        return jSONObject;
    }

    private JSONObject getPingResponse(Future<PingResponse> future) throws JSONException {
        PingResponse pingResponse;
        JSONObject jSONObject = new JSONObject();
        if (future == null) {
            return jSONObject;
        }
        try {
            pingResponse = future.get();
        } catch (Exception unused) {
            pingResponse = null;
        }
        if (pingResponse == null) {
            return jSONObject;
        }
        jSONObject.put(IWXUserTrackAdapter.MONITOR_ERROR_CODE, pingResponse.getErrcode());
        JSONArray jSONArray = new JSONArray();
        for (PingEntry pingEntry : pingResponse.getResults()) {
            jSONArray.put("seq=" + pingEntry.seq + ",hop=" + pingEntry.hop + ",rtt=" + pingEntry.rtt);
        }
        jSONObject.put(NetworkEventSender.TYPE_RESPONSE, jSONArray);
        return jSONObject;
    }

    private class LimitedQueue<E> extends LinkedList<E> {
        private int limit;

        public LimitedQueue(int i) {
            this.limit = i;
        }

        public boolean add(E e) {
            boolean add = super.add(e);
            while (add && size() > this.limit) {
                super.remove();
            }
            return add;
        }
    }

    private class DetectInfo {
        String currentIp;
        Future<PingResponse> defaultFuture;
        String host;
        String localIp;
        Future<PingResponse> mtu1200Future;
        Future<PingResponse> mtu1460Future;

        private DetectInfo() {
        }
    }
}
