package anet.channel.strategy.dispatch;

import android.content.Context;
import anet.channel.util.ALog;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AmdcRuntimeInfo {
    private static final String TAG = "awcn.AmdcRuntimeInfo";
    private static volatile int amdcLimitLevel;
    private static volatile long amdcLimitTime;
    public static volatile String appChannel;
    public static volatile String appName;
    public static volatile String appVersion;
    private static volatile Context context;
    private static IAmdcSign iSign;
    public static volatile double latitude;
    public static volatile double longitude;
    private static Map<String, String> params;

    public static void updateAmdcLimit(int i, int i2) {
        ALog.i(TAG, "set amdc limit", (String) null, "level", Integer.valueOf(i), "time", Integer.valueOf(i2));
        if (i >= 0 && i <= 3) {
            amdcLimitLevel = i;
            amdcLimitTime = System.currentTimeMillis() + (((long) i2) * 1000);
        }
    }

    public static int getAmdcLimitLevel() {
        if (amdcLimitLevel > 0 && System.currentTimeMillis() - amdcLimitTime > 0) {
            amdcLimitTime = 0;
            amdcLimitLevel = 0;
        }
        return amdcLimitLevel;
    }

    public static void setContext(Context context2) {
        context = context2;
    }

    public static Context getContext() {
        return context;
    }

    public static void setSign(IAmdcSign iAmdcSign) {
        iSign = iAmdcSign;
    }

    public static IAmdcSign getSign() {
        return iSign;
    }

    public static void updateLocation(double d, double d2) {
        latitude = d;
        longitude = d2;
    }

    public static void setAppInfo(String str, String str2, String str3) {
        appName = str;
        appVersion = str2;
        appChannel = str3;
    }

    public static synchronized void setParam(String str, String str2) {
        synchronized (AmdcRuntimeInfo.class) {
            if (params == null) {
                params = new HashMap();
            }
            params.put(str, str2);
        }
    }

    public static synchronized Map<String, String> getParams() {
        synchronized (AmdcRuntimeInfo.class) {
            if (params == null) {
                Map<String, String> map = Collections.EMPTY_MAP;
                return map;
            }
            HashMap hashMap = new HashMap(params);
            return hashMap;
        }
    }
}
