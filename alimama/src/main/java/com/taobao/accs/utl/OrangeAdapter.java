package com.taobao.accs.utl;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.ChannelService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListenerV1;
import java.util.HashMap;
import java.util.Map;

public class OrangeAdapter {
    private static final String ACCS_ENABLE_KEY = "main_function_enable";
    private static final String BIND_CHANEEL_KEY = "channel_mode_enable";
    private static final String BIND_SERVICE_KEY = "bind_service_enable";
    private static final String HEARTBEAT_KEY = "heartbeat_smart_enable";
    private static final String KEEP_ALIVE_KEY = "keep_alive_enable";
    public static final String NAMESPACE = "accs";
    private static final String PULLUP = "pullup";
    private static final String PULL_UP_KEY = "pull_up_enable";
    private static final String TAG = "OrangeAdapter";
    private static final String TNET_ELECTION_KEY = "election_enable";
    private static final String TNET_LOG_KEY = "tnet_log_off";
    private static boolean isReset = false;
    public static boolean mOrangeValid = false;
    private static Boolean resultOfChannel = null;

    static {
        try {
            Class.forName("com.taobao.orange.OrangeConfig");
            mOrangeValid = true;
        } catch (Exception unused) {
            mOrangeValid = false;
        }
    }

    public static void registerListener(String[] strArr, OrangeConfigListenerV1 orangeConfigListenerV1) {
        if (mOrangeValid) {
            OrangeConfig.getInstance().registerListener(strArr, orangeConfigListenerV1);
        } else {
            ALog.w(TAG, "no orange sdk", new Object[0]);
        }
    }

    public static String getConfig(String str, String str2, String str3) {
        if (mOrangeValid) {
            return OrangeConfig.getInstance().getConfig(str, str2, str3);
        }
        ALog.w(TAG, "no orange sdk", new Object[0]);
        return str3;
    }

    public static boolean isAccsEnabled() {
        boolean z;
        try {
            z = Boolean.valueOf(getConfig("accs", ACCS_ENABLE_KEY, "true")).booleanValue();
        } catch (Throwable th) {
            ALog.e(TAG, "isAccsEnabled", th, new Object[0]);
            z = true;
        }
        ALog.i(TAG, "isAccsEnabled", "enable", Boolean.valueOf(z));
        return z;
    }

    public static boolean isSmartHb() {
        boolean z;
        try {
            z = getConfigFromSP(GlobalClientInfo.getContext(), Constants.SP_KEY_HB_SMART_ENABLE, true);
        } catch (Throwable th) {
            ALog.e(TAG, "isSmartHb", th, new Object[0]);
            z = true;
        }
        ALog.d(TAG, "isSmartHb", "result", Boolean.valueOf(z));
        return z;
    }

    public static boolean isBindService() {
        boolean z;
        try {
            z = getConfigFromSP(GlobalClientInfo.getContext(), "bind_service_enable", true);
        } catch (Throwable th) {
            ALog.e(TAG, "isBindService", th, new Object[0]);
            z = true;
        }
        ALog.d(TAG, "isBindService", "result", Boolean.valueOf(z));
        return z;
    }

    public static boolean isKeepAlive() {
        boolean z;
        try {
            z = getConfigFromSP(GlobalClientInfo.getContext(), "keep_alive_enable", true);
        } catch (Throwable th) {
            ALog.e(TAG, "isKeepAlive", th, new Object[0]);
            z = true;
        }
        ALog.d(TAG, "isKeepAlive", "result", Boolean.valueOf(z));
        return z;
    }

    public static boolean isPullUp() {
        boolean z;
        try {
            z = getConfigFromSP(GlobalClientInfo.getContext(), "pull_up_enable", true);
        } catch (Throwable th) {
            ALog.e(TAG, "isPullUp", th, new Object[0]);
            z = true;
        }
        ALog.d(TAG, "isPullUp", "result", Boolean.valueOf(z));
        return z;
    }

    public static long getLastLaunchTime(Context context) {
        long j = 0;
        try {
            j = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getLong(Constants.SP_KEY_LAST_LAUNCH_TIME, 0);
        } catch (Throwable th) {
            ALog.e(TAG, "getLastLaunchTime", th, new Object[0]);
        }
        ALog.d(TAG, "getLastLaunchTime", "result", Long.valueOf(j));
        return j;
    }

    public static void saveLastLaunchTime(Context context, long j) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            edit.putLong(Constants.SP_KEY_LAST_LAUNCH_TIME, j);
            edit.apply();
        } catch (Throwable th) {
            ALog.e(TAG, "saveLastLaunchTime fail:", th, "lastLaunchTime", Long.valueOf(j));
        }
    }

    public static boolean isTnetLogOff(boolean z) {
        boolean z2;
        Throwable th;
        boolean z3;
        String str = "default";
        if (z) {
            try {
                str = getConfig("accs", "tnet_log_off", "default");
            } catch (Throwable th2) {
                th = th2;
                z3 = true;
                ALog.e(TAG, "isTnetLogOff", th, new Object[0]);
                z2 = z3;
                ALog.i(TAG, "isTnetLogOff", "result", Boolean.valueOf(z2));
                return z2;
            }
        }
        if (str.equals("default")) {
            z2 = getConfigFromSP(GlobalClientInfo.getContext(), "tnet_log_off", true);
        } else {
            z2 = Boolean.valueOf(str).booleanValue();
            try {
                saveConfigToSP(GlobalClientInfo.getContext(), "tnet_log_off", z2);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                z3 = z2;
                th = th4;
            }
        }
        ALog.i(TAG, "isTnetLogOff", "result", Boolean.valueOf(z2));
        return z2;
    }

    public static void resetChannelModeEnable() {
        isReset = true;
        resultOfChannel = false;
        saveConfigToSP(GlobalClientInfo.getContext(), "channel_mode_enable", false);
    }

    public static boolean isChannelModeEnable() {
        if (resultOfChannel == null) {
            try {
                resultOfChannel = Boolean.valueOf(getConfigFromSP(GlobalClientInfo.getContext(), "channel_mode_enable", false));
            } catch (Throwable th) {
                ALog.e(TAG, "isChannelModeEnable", th, new Object[0]);
            }
            ALog.d(TAG, "isChannelModeEnable", "result", resultOfChannel);
        }
        return resultOfChannel.booleanValue();
    }

    private static boolean getConfigFromSP(Context context, String str, boolean z) {
        try {
            return context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getBoolean(str, z);
        } catch (Exception e) {
            ALog.e(TAG, "getConfigFromSP fail:", e, "key", str);
            return z;
        }
    }

    private static void saveConfigToSP(Context context, String str, boolean z) {
        if (context == null) {
            try {
                ALog.e(TAG, "saveTLogOffToSP context null", new Object[0]);
            } catch (Exception e) {
                ALog.e(TAG, "saveConfigToSP fail:", e, "key", str, "value", Boolean.valueOf(z));
            }
        } else {
            SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            edit.putBoolean(str, z);
            edit.apply();
            ALog.i(TAG, "saveConfigToSP", "key", str, "value", Boolean.valueOf(z));
        }
    }

    public static void saveConfigToSP(Context context, String str, int i) {
        if (context == null) {
            try {
                ALog.e(TAG, "saveTLogOffToSP context null", new Object[0]);
            } catch (Exception e) {
                ALog.e(TAG, "saveConfigToSP fail:", e, "key", str, "value", Integer.valueOf(i));
            }
        } else {
            SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            edit.putInt(str, i);
            edit.apply();
            ALog.i(TAG, "saveConfigToSP", "key", str, "value", Integer.valueOf(i));
        }
    }

    private static void saveConfigsToSP(Context context, Map<String, Boolean> map) {
        if (map != null) {
            try {
                if (map.size() != 0) {
                    SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                    for (Map.Entry next : map.entrySet()) {
                        edit.putBoolean((String) next.getKey(), ((Boolean) next.getValue()).booleanValue());
                    }
                    edit.apply();
                    ALog.i(TAG, "saveConfigsToSP", "configs", map.toString());
                }
            } catch (Exception e) {
                ALog.e(TAG, "saveConfigsToSP fail:", e, "configs", map.toString());
            }
        }
    }

    private static void savePullupInfo(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                SharedPreferences.Editor edit = GlobalClientInfo.getContext().getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.putString(PULLUP, str);
                edit.apply();
            } catch (Throwable th) {
                ALog.e(TAG, "savePullupInfo fail:", th, PULLUP, str);
            }
            ALog.i(TAG, "savePullupInfo", PULLUP, str);
        }
    }

    public static String getPullupInfo() {
        try {
            return GlobalClientInfo.getContext().getSharedPreferences(Constants.SP_FILE_NAME, 0).getString(PULLUP, (String) null);
        } catch (Throwable th) {
            ALog.e(TAG, "getPullupInfo fail:", th, new Object[0]);
            return null;
        }
    }

    public static void getConfigForAccs() {
        HashMap hashMap = new HashMap();
        hashMap.put("tnet_log_off", Boolean.valueOf(getConfig("accs", "tnet_log_off", "false")));
        hashMap.put("election_enable", Boolean.valueOf(getConfig("accs", "election_enable", String.valueOf(GlobalClientInfo.mSupprotElection))));
        hashMap.put(Constants.SP_KEY_HB_SMART_ENABLE, Boolean.valueOf(getConfig("accs", HEARTBEAT_KEY, "true")));
        hashMap.put("bind_service_enable", Boolean.valueOf(getConfig("accs", "bind_service_enable", "true")));
        hashMap.put("channel_mode_enable", Boolean.valueOf(isReset ? "false" : getConfig("accs", "channel_mode_enable", "false")));
        hashMap.put("keep_alive_enable", Boolean.valueOf(getConfig("accs", "keep_alive_enable", "true")));
        hashMap.put("pull_up_enable", Boolean.valueOf(getConfig("accs", "pull_up_enable", "true")));
        saveConfigsToSP(GlobalClientInfo.getContext(), hashMap);
        saveConfigToSP(GlobalClientInfo.getContext(), ChannelService.SUPPORT_FOREGROUND_VERSION_KEY, UtilityImpl.String2Int(getConfig("accs", ChannelService.SUPPORT_FOREGROUND_VERSION_KEY, String.valueOf(21))));
        savePullupInfo(getConfig("accs", PULLUP, (String) null));
    }

    public static class OrangeListener implements OrangeConfigListenerV1 {
        public void onConfigUpdate(String str, boolean z) {
            if (GlobalClientInfo.getContext() == null) {
                ALog.e(OrangeAdapter.TAG, "onConfigUpdate context null", new Object[0]);
                return;
            }
            try {
                ALog.i(OrangeAdapter.TAG, "onConfigUpdate", "namespace", str);
                if ("accs".equals(str)) {
                    OrangeAdapter.checkAccsEnabled();
                    OrangeAdapter.getConfigForAccs();
                }
            } catch (Throwable th) {
                ALog.e(OrangeAdapter.TAG, "onConfigUpdate", th, new Object[0]);
            }
        }
    }

    public static void checkAccsEnabled() {
        if (!isAccsEnabled()) {
            ALog.e(TAG, "force disable service", new Object[0]);
            ACCSManager.forceDisableService(GlobalClientInfo.getContext());
        } else if (UtilityImpl.getFocusDisableStatus(GlobalClientInfo.getContext())) {
            ALog.i(TAG, "force enable service", new Object[0]);
            ACCSManager.forceEnableService(GlobalClientInfo.getContext());
        }
    }
}
