package org.android.agoo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.utl.ALog;

public class Config {
    public static final String AGOO_CLEAR_TIME = "agoo_clear_time";
    public static final String AGOO_ENABLE_DAEMONSERVER = "agoo_enable_daemonserver";
    public static final String AGOO_UNREPORT_TIMES = "agoo_UnReport_times";
    private static String DEVICETOKEN = null;
    public static final String KEY_DEVICE_TOKEN = "deviceId";
    public static final String PREFERENCES = "Agoo_AppStore";
    public static final String PROPERTY_AGOO_SERVICE_MODE = "agoo_service_mode";
    public static final String PROPERTY_APP_KEY = "agoo_app_key";
    public static final String PROPERTY_APP_VERSION = "app_version";
    public static final String PROPERTY_DEVICE_TOKEN = "app_device_token";
    public static final String PROPERTY_PUSH_USER_TOKEN = "app_push_user_token";
    public static final String PROPERTY_TT_ID = "app_tt_id";
    public static final String TAG = "Config";
    public static String mAccsConfigTag;
    private static String mAgooAppkey;

    private static SharedPreferences getAgooPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, 4);
    }

    public static void clear(Context context) {
        try {
            SharedPreferences.Editor edit = getAgooPreferences(context).edit();
            edit.putInt("app_version", Integer.MIN_VALUE);
            edit.remove(PROPERTY_DEVICE_TOKEN);
            edit.remove(PROPERTY_APP_KEY);
            edit.remove(PROPERTY_TT_ID);
            edit.apply();
        } catch (Throwable unused) {
        }
    }

    public static void setAgooAppKey(Context context, String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                ALog.e(TAG, "setAgooAppKey appkey null", new Object[0]);
                return;
            }
            mAgooAppkey = str;
            SharedPreferences.Editor edit = getAgooPreferences(context).edit();
            edit.putString(PROPERTY_APP_KEY, str);
            edit.apply();
            ALog.d(TAG, "setAgooAppKey", "appkey", str);
        } catch (Throwable th) {
            ALog.e(TAG, "setAgooAppKey", th, new Object[0]);
        }
    }

    public static String getAgooAppKey(Context context) {
        String str;
        String str2 = mAgooAppkey;
        try {
            str = getAgooPreferences(context).getString(PROPERTY_APP_KEY, mAgooAppkey);
        } catch (Throwable th) {
            ALog.e(TAG, "getAgooAppKey", th, new Object[0]);
            str = str2;
        }
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getAgooAppKey null!!", new Object[0]);
        }
        ALog.d(TAG, "getAgooAppKey", "appkey", str);
        return str;
    }

    public static String getAccsConfigTag(Context context) {
        if (TextUtils.isEmpty(mAccsConfigTag)) {
            return ACCSManager.getDefaultConfig(context);
        }
        return mAccsConfigTag;
    }

    public static void setReportTimes(Context context, int i) {
        try {
            SharedPreferences agooPreferences = getAgooPreferences(context);
            SharedPreferences.Editor edit = agooPreferences.edit();
            edit.putInt(AGOO_UNREPORT_TIMES, agooPreferences.getInt(AGOO_UNREPORT_TIMES, 0) + i);
            edit.apply();
        } catch (Throwable unused) {
        }
    }

    public static boolean isReportCacheMsg(Context context) {
        try {
            if (getAgooPreferences(context).getInt(AGOO_UNREPORT_TIMES, 0) > 0) {
                return true;
            }
            return false;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void clearReportTimes(Context context) {
        try {
            SharedPreferences.Editor edit = getAgooPreferences(context).edit();
            edit.putInt(AGOO_UNREPORT_TIMES, 0);
            edit.apply();
        } catch (Throwable unused) {
        }
    }

    public static int getReportCacheMsg(Context context) {
        try {
            return getAgooPreferences(context).getInt(AGOO_UNREPORT_TIMES, 0);
        } catch (Throwable unused) {
            return 0;
        }
    }

    public static void setClearTimes(Context context, long j) {
        try {
            SharedPreferences.Editor edit = getAgooPreferences(context).edit();
            edit.putLong(AGOO_CLEAR_TIME, j);
            edit.apply();
        } catch (Throwable unused) {
        }
    }

    public static boolean isClearTime(Context context, long j) {
        try {
            long j2 = getAgooPreferences(context).getLong(AGOO_CLEAR_TIME, 0);
            StringBuilder sb = new StringBuilder();
            sb.append("now=");
            sb.append(j);
            sb.append(",now - lastTime=");
            long j3 = j - j2;
            sb.append(j3);
            sb.append(",istrue=");
            sb.append(j3 > 86400000);
            ALog.d("isClearTime", sb.toString(), new Object[0]);
            if (j == 0 || j3 <= 86400000) {
                return false;
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void setDaemonServerFlag(Context context, boolean z) {
        try {
            SharedPreferences.Editor edit = getAgooPreferences(context).edit();
            edit.putBoolean(AGOO_ENABLE_DAEMONSERVER, z);
            edit.apply();
        } catch (Throwable unused) {
        }
    }

    public static boolean isEnableDaemonServer(Context context) {
        try {
            return getAgooPreferences(context).getBoolean(AGOO_ENABLE_DAEMONSERVER, true);
        } catch (Throwable unused) {
            return false;
        }
    }

    public static void setDeviceToken(Context context, String str) {
        ALog.i(TAG, "setDeviceToken", "token", str);
        if (!TextUtils.isEmpty(str)) {
            DEVICETOKEN = str;
            try {
                SharedPreferences.Editor edit = getAgooPreferences(context).edit();
                edit.putString("deviceId", str);
                edit.apply();
            } catch (Throwable th) {
                ALog.e(TAG, "setDeviceToken", th, new Object[0]);
            }
        }
    }

    public static String getDeviceToken(Context context) {
        String str;
        String str2 = DEVICETOKEN;
        try {
            str = getAgooPreferences(context).getString("deviceId", DEVICETOKEN);
        } catch (Throwable th) {
            ALog.e(TAG, "getDeviceToken", th, new Object[0]);
            str = str2;
        }
        ALog.i(TAG, "getDeviceToken", "token", str);
        return str;
    }

    public static String getPushAliasToken(Context context) {
        try {
            return getAgooPreferences(context).getString(PROPERTY_PUSH_USER_TOKEN, "");
        } catch (Throwable unused) {
            return null;
        }
    }

    public static void setPushAliasToken(Context context, String str) {
        try {
            SharedPreferences.Editor edit = getAgooPreferences(context).edit();
            if (!TextUtils.isEmpty(str)) {
                edit.putString(PROPERTY_PUSH_USER_TOKEN, str);
            }
            edit.apply();
        } catch (Throwable unused) {
        }
    }
}
