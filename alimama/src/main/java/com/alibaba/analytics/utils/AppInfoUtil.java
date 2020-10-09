package com.alibaba.analytics.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.taobao.android.tlog.protocol.model.joint.point.BackgroundJointPoint;
import com.taobao.android.tlog.protocol.model.joint.point.ForegroundJointPoint;
import java.util.HashMap;
import java.util.Map;

public class AppInfoUtil {
    private static final int IMPORTANCE_BACKGROUND = 400;
    private static final int IMPORTANCE_FOREGROUND_SERVICE = 125;
    private static final String TAG = "AppInfoUtil";
    private static String mAppkey = null;
    private static String mChannle = "";
    static Map<String, String> preInfoMap;

    @Deprecated
    public static String getUserid() {
        return "";
    }

    @Deprecated
    public static String getUsernick() {
        return "";
    }

    @Deprecated
    public static String getLongLoginUsernick() {
        if (Variables.getInstance().getContext() == null) {
            return "";
        }
        try {
            String string = Variables.getInstance().getContext().getSharedPreferences("UTCommon", 0).getString("_lun", "");
            if (!TextUtils.isEmpty(string)) {
                return new String(Base64.decode(string.getBytes(), 2), "UTF-8");
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    @Deprecated
    public static String getLongLoingUserid() {
        if (Variables.getInstance().getContext() == null) {
            return "";
        }
        try {
            String string = Variables.getInstance().getContext().getSharedPreferences("UTCommon", 0).getString("_luid", "");
            if (!TextUtils.isEmpty(string)) {
                return new String(Base64.decode(string.getBytes(), 2), "UTF-8");
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }

    public static String getChannel() {
        return Variables.getInstance().getChannel();
    }

    public static String getAppkey() {
        return Variables.getInstance().getAppkey();
    }

    public static boolean isAppOnForeground(Context context) {
        if (context == null) {
            Logger.i((String) null, ForegroundJointPoint.TYPE, false);
            return false;
        }
        try {
            PowerManager powerManager = (PowerManager) context.getSystemService("power");
            String packageName = context.getPackageName();
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (next.processName.equals(packageName)) {
                    if (next.importance != 400) {
                        if (next.importance != 125) {
                            if (!powerManager.isScreenOn()) {
                                return false;
                            }
                            Logger.i((String) null, ForegroundJointPoint.TYPE, true);
                            return true;
                        }
                    }
                    Logger.i((String) null, ForegroundJointPoint.TYPE, false);
                    return false;
                }
            }
        } catch (Throwable unused) {
        }
        Logger.i((String) null, BackgroundJointPoint.TYPE, false);
        return false;
    }

    public static String getString(Context context, String str) {
        int identifier;
        if (context == null) {
            return null;
        }
        try {
            Resources resources = context.getResources();
            if (resources == null || (identifier = resources.getIdentifier(str, "string", context.getPackageName())) == 0) {
                return null;
            }
            return context.getString(identifier);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static Map<String, String> getInfoForPreApk(Context context) {
        if (preInfoMap != null) {
            return preInfoMap;
        }
        if (context == null) {
            return null;
        }
        preInfoMap = new HashMap();
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences("manufacture_config", 0);
            boolean z = sharedPreferences.getBoolean("preLoad", false);
            String string = sharedPreferences.getString("preLoad_VersionName", "");
            String string2 = sharedPreferences.getString("preLoad_Channel1", "");
            String string3 = sharedPreferences.getString("preLoad_Channel2", "");
            if (z) {
                preInfoMap.put("preLoad", "true");
                preInfoMap.put("preLoad_VersionName", string);
                preInfoMap.put("preLoad_Channel1", string2);
                preInfoMap.put("preLoad_Channel2", string3);
            }
        } catch (Exception unused) {
        }
        return preInfoMap;
    }

    public static String getChannle2ForPreLoadApk(Context context) {
        Map<String, String> infoForPreApk = getInfoForPreApk(context);
        if (infoForPreApk != null) {
            return infoForPreApk.get("preLoad_Channel2");
        }
        return null;
    }

    public static boolean isMainProcess(Context context) {
        try {
            String str = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.processName;
            String curProcessName = getCurProcessName(context);
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(curProcessName)) {
                return true;
            }
            return curProcessName.equalsIgnoreCase(str);
        } catch (Throwable unused) {
            return true;
        }
    }

    public static String getCurProcessName(Context context) {
        if (context == null) {
            return "";
        }
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
            if (next.pid == myPid) {
                return next.processName;
            }
        }
        return null;
    }
}
