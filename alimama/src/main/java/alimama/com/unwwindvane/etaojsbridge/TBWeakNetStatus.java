package alimama.com.unwwindvane.etaojsbridge;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.WVResult;
import anetwork.channel.monitor.Monitor;
import anetwork.channel.monitor.speed.NetworkSpeed;

public class TBWeakNetStatus extends WVApiPlugin {
    private final int NETWORK_TYPE_1xRTT = 7;
    private final int NETWORK_TYPE_CDMA = 4;
    private final int NETWORK_TYPE_EDGE = 2;
    private final int NETWORK_TYPE_EHRPD = 14;
    private final int NETWORK_TYPE_EVDO_0 = 5;
    private final int NETWORK_TYPE_EVDO_A = 6;
    private final int NETWORK_TYPE_EVDO_B = 12;
    private final int NETWORK_TYPE_GPRS = 1;
    private final int NETWORK_TYPE_HSDPA = 8;
    private final int NETWORK_TYPE_HSPA = 10;
    private final int NETWORK_TYPE_HSPAP = 15;
    private final int NETWORK_TYPE_HSUPA = 9;
    private final int NETWORK_TYPE_IDEN = 11;
    private final int NETWORK_TYPE_LTE = 13;
    private final int NETWORK_TYPE_UMTS = 3;

    public static void register() {
        WVPluginManager.registerPlugin("TBWeakNetStatus", (Class<? extends WVApiPlugin>) TBWeakNetStatus.class);
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getStatus".equals(str)) {
            getStatus(wVCallBackContext);
            return true;
        } else if (!"getNetworkType".equals(str)) {
            return false;
        } else {
            getNetworkType(wVCallBackContext);
            return true;
        }
    }

    public final void getStatus(WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        NetworkSpeed networkSpeed = Monitor.getNetworkSpeed();
        if (networkSpeed == null || !networkSpeed.name().equals("Fast")) {
            wVResult.addData("WeakNetStatus", "true");
        } else {
            wVResult.addData("WeakNetStatus", "false");
        }
        wVCallBackContext.success(wVResult);
    }

    public final void getNetworkType(WVCallBackContext wVCallBackContext) {
        WVResult wVResult = new WVResult();
        wVResult.addData("networkType", networkType());
        wVCallBackContext.success(wVResult);
    }

    public final String networkType() {
        Context context = this.mContext;
        Context context2 = this.mContext;
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return "NONE";
        }
        if (activeNetworkInfo.getType() == 1) {
            return "WIFI";
        }
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 13:
                return "4G";
            default:
                return "UNKNOWN";
        }
    }
}
