package anet.channel;

import android.text.TextUtils;
import anet.channel.heartbeat.IHeartbeat;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyTemplate;
import anet.channel.strategy.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

public class AwcnConfig {
    public static final String NEXT_LAUNCH_FORBID = "NEXT_LAUNCH_FORBID";
    private static volatile int accsReconnectionDelayPeriod = 10000;
    private static volatile boolean ipv6BlackListEnable = true;
    private static volatile long ipv6BlackListTtl = 43200000;
    private static volatile boolean ipv6Enable = true;
    private static volatile boolean isAccsSessionCreateForbiddenInBg = false;
    private static boolean isAppLifeCycleListenerEnable = true;
    private static boolean isAsyncLoadStrategyEnable = false;
    private static volatile boolean isHorseRaceEnable = true;
    private static volatile boolean isHttpsSniEnable = true;
    private static volatile boolean isIdleSessionCloseEnable = false;
    private static volatile boolean isNetworkDetectEnable = true;
    private static volatile boolean isPing6Enable = true;
    private static volatile boolean isQuicEnable = false;
    private static volatile boolean isTbNextLaunch = false;
    private static volatile boolean isTnetHeaderCacheEnable = true;

    public static boolean isAccsSessionCreateForbiddenInBg() {
        return isAccsSessionCreateForbiddenInBg;
    }

    public static void setAccsSessionCreateForbiddenInBg(boolean z) {
        isAccsSessionCreateForbiddenInBg = z;
    }

    public static void setHttpsSniEnable(boolean z) {
        isHttpsSniEnable = z;
    }

    public static boolean isHttpsSniEnable() {
        return isHttpsSniEnable;
    }

    public static boolean isHorseRaceEnable() {
        return isHorseRaceEnable;
    }

    public static void setHorseRaceEnable(boolean z) {
        isHorseRaceEnable = z;
    }

    public static boolean isTnetHeaderCacheEnable() {
        return isTnetHeaderCacheEnable;
    }

    public static void setTnetHeaderCacheEnable(boolean z) {
        isTnetHeaderCacheEnable = z;
    }

    public static void setQuicEnable(boolean z) {
        isQuicEnable = z;
    }

    public static boolean isQuicEnable() {
        return isQuicEnable;
    }

    public static void setIdleSessionCloseEnable(boolean z) {
        isIdleSessionCloseEnable = z;
    }

    public static boolean isIdleSessionCloseEnable() {
        return isIdleSessionCloseEnable;
    }

    public static void registerPresetSessions(String str) {
        if (GlobalAppRuntimeInfo.isTargetProcess() && !TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                int length = jSONArray.length();
                int i = 0;
                while (i < length) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    String string = jSONObject.getString("host");
                    if (Utils.checkHostValidAndNotIp(string)) {
                        StrategyTemplate.getInstance().registerConnProtocol(string, ConnProtocol.valueOf(jSONObject.getString("protocol"), jSONObject.getString("rtt"), jSONObject.getString("publicKey")));
                        if (jSONObject.getBoolean("isKeepAlive")) {
                            SessionCenter.getInstance().registerSessionInfo(SessionInfo.create(string, true, false, (IAuth) null, (IHeartbeat) null, (DataFrameCb) null));
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            } catch (Exception unused) {
            }
        }
    }

    public static boolean isIpv6Enable() {
        return ipv6Enable;
    }

    public static void setIpv6Enable(boolean z) {
        ipv6Enable = z;
    }

    public static boolean isIpv6BlackListEnable() {
        return ipv6BlackListEnable;
    }

    public static void setIpv6BlackListEnable(boolean z) {
        ipv6BlackListEnable = z;
    }

    public static long getIpv6BlackListTtl() {
        return ipv6BlackListTtl;
    }

    public static void setIpv6BlackListTtl(long j) {
        ipv6BlackListTtl = j;
    }

    public static boolean isAppLifeCycleListenerEnable() {
        return isAppLifeCycleListenerEnable;
    }

    public static void setAppLifeCycleListenerEnable(boolean z) {
        isAppLifeCycleListenerEnable = z;
    }

    public static boolean isAsyncLoadStrategyEnable() {
        return isAsyncLoadStrategyEnable;
    }

    public static void setAsyncLoadStrategyEnable(boolean z) {
        isAsyncLoadStrategyEnable = z;
    }

    public static boolean isTbNextLaunch() {
        return isTbNextLaunch;
    }

    public static void setTbNextLaunch(boolean z) {
        isTbNextLaunch = z;
    }

    public static boolean isPing6Enable() {
        return isPing6Enable;
    }

    public static void setPing6Enable(boolean z) {
        isPing6Enable = z;
    }

    public static boolean isNetworkDetectEnable() {
        return isNetworkDetectEnable;
    }

    public static void setNetworkDetectEnable(boolean z) {
        isNetworkDetectEnable = z;
    }

    public static int getAccsReconnectionDelayPeriod() {
        return accsReconnectionDelayPeriod;
    }

    public static void setAccsReconnectionDelayPeriod(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 10000) {
            i = 10000;
        }
        accsReconnectionDelayPeriod = i;
    }
}
