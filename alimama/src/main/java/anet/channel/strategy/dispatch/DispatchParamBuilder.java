package anet.channel.strategy.dispatch;

import android.os.Build;
import android.text.TextUtils;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.utils.Utils;
import anet.channel.util.ALog;
import anet.channel.util.Inet64Util;
import com.alipay.sdk.a;
import com.taobao.accs.antibrush.CookieMgr;
import java.util.Map;
import java.util.Set;

class DispatchParamBuilder {
    public static final String TAG = "amdc.DispatchParamBuilder";

    DispatchParamBuilder() {
    }

    public static Map buildParamMap(Map<String, Object> map) {
        IAmdcSign sign = AmdcRuntimeInfo.getSign();
        if (sign == null || TextUtils.isEmpty(sign.getAppkey())) {
            ALog.e(TAG, "amdc sign is null or appkey is empty", (String) null, new Object[0]);
            return null;
        }
        NetworkStatusHelper.NetworkStatus status = NetworkStatusHelper.getStatus();
        if (!NetworkStatusHelper.isConnected()) {
            ALog.e(TAG, "no network, don't send amdc request", (String) null, new Object[0]);
            return null;
        }
        map.put("appkey", sign.getAppkey());
        map.put("v", DispatchConstants.VER_CODE);
        map.put("platform", "android");
        map.put(DispatchConstants.PLATFORM_VERSION, Build.VERSION.RELEASE);
        if (!TextUtils.isEmpty(GlobalAppRuntimeInfo.getUserId())) {
            map.put("sid", GlobalAppRuntimeInfo.getUserId());
        }
        if (!TextUtils.isEmpty(GlobalAppRuntimeInfo.getUtdid())) {
            map.put("deviceId", GlobalAppRuntimeInfo.getUtdid());
        }
        map.put("netType", status.toString());
        if (status.isWifi()) {
            map.put(DispatchConstants.BSSID, NetworkStatusHelper.getWifiBSSID());
        }
        map.put(DispatchConstants.CARRIER, NetworkStatusHelper.getCarrier());
        map.put(DispatchConstants.MNC, NetworkStatusHelper.getSimOp());
        map.put("lat", String.valueOf(AmdcRuntimeInfo.latitude));
        map.put("lng", String.valueOf(AmdcRuntimeInfo.longitude));
        map.putAll(AmdcRuntimeInfo.getParams());
        map.put("channel", AmdcRuntimeInfo.appChannel);
        map.put("appName", AmdcRuntimeInfo.appName);
        map.put("appVersion", AmdcRuntimeInfo.appVersion);
        map.put(DispatchConstants.STACK_TYPE, Integer.toString(getStackType()));
        map.put("domain", formatDomains(map));
        map.put(DispatchConstants.SIGNTYPE, sign.useSecurityGuard() ? CookieMgr.KEY_SEC : a.h);
        map.put("t", String.valueOf(System.currentTimeMillis()));
        String sign2 = getSign(sign, map);
        if (TextUtils.isEmpty(sign2)) {
            return null;
        }
        map.put("sign", sign2);
        return map;
    }

    private static int getStackType() {
        switch (Inet64Util.getStackType()) {
            case 1:
                return 4;
            case 2:
                return 2;
            case 3:
                return 1;
            default:
                return 4;
        }
    }

    private static String formatDomains(Map map) {
        StringBuilder sb = new StringBuilder();
        for (String append : (Set) map.remove("hosts")) {
            sb.append(append);
            sb.append(' ');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    static String getSign(IAmdcSign iAmdcSign, Map<String, String> map) {
        StringBuilder sb = new StringBuilder(128);
        sb.append(Utils.stringNull2Empty(map.get("appkey")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("domain")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("appName")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("appVersion")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get(DispatchConstants.BSSID)));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("channel")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("deviceId")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("lat")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("lng")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get(DispatchConstants.MACHINE)));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("netType")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("other")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("platform")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get(DispatchConstants.PLATFORM_VERSION)));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get(DispatchConstants.PRE_IP)));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("sid")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("t")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get("v")));
        sb.append("&");
        sb.append(Utils.stringNull2Empty(map.get(DispatchConstants.SIGNTYPE)));
        try {
            return iAmdcSign.sign(sb.toString());
        } catch (Exception e) {
            ALog.e(TAG, "get sign failed", (String) null, e, new Object[0]);
            return null;
        }
    }
}
