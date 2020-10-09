package anet.channel;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import anet.channel.entity.ENV;
import anet.channel.fulltrace.AnalysisFactory;
import anet.channel.fulltrace.SceneInfo;
import anet.channel.strategy.StrategyCenter;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.DispatchConstants;
import anet.channel.util.ALog;
import anet.channel.util.Utils;
import com.taobao.android.dinamic.DinamicConstant;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlobalAppRuntimeInfo {
    private static final String TAG = "awcn.GlobalAppRuntimeInfo";
    private static final String USER_ID = "UserId";
    private static String appVersion;
    private static volatile CopyOnWriteArrayList<String> bucketInfos = null;
    private static Context context;
    private static String currentProcess = "";
    private static ENV env = ENV.ONLINE;
    private static volatile long initTime;
    private static volatile boolean isBackground = true;
    private static SharedPreferences sp = null;
    private static String targetProcess = "";
    private static String ttid;
    private static String userId;
    private static String utdid;

    public static void setContext(Context context2) {
        context = context2;
        if (context2 != null) {
            if (TextUtils.isEmpty(currentProcess)) {
                currentProcess = Utils.getProcessName(context2, Process.myPid());
            }
            if (TextUtils.isEmpty(targetProcess)) {
                targetProcess = Utils.getMainProcessName(context2);
            }
            if (sp == null) {
                sp = PreferenceManager.getDefaultSharedPreferences(context2);
                userId = sp.getString(USER_ID, (String) null);
            }
            ALog.e(TAG, "", (String) null, "CurrentProcess", currentProcess, "TargetProcess", targetProcess);
        }
    }

    public static Context getContext() {
        return context;
    }

    public static void setTargetProcess(String str) {
        targetProcess = str;
    }

    public static boolean isTargetProcess() {
        if (TextUtils.isEmpty(targetProcess) || TextUtils.isEmpty(currentProcess)) {
            return true;
        }
        return targetProcess.equalsIgnoreCase(currentProcess);
    }

    public static String getCurrentProcess() {
        return currentProcess;
    }

    public static void setCurrentProcess(String str) {
        currentProcess = str;
    }

    public static void setEnv(ENV env2) {
        env = env2;
    }

    public static ENV getEnv() {
        return env;
    }

    public static void setTtid(String str) {
        ttid = str;
        try {
            if (!TextUtils.isEmpty(str)) {
                int indexOf = str.indexOf(DinamicConstant.DINAMIC_PREFIX_AT);
                String str2 = null;
                String substring = indexOf != -1 ? str.substring(0, indexOf) : null;
                String substring2 = str.substring(indexOf + 1);
                int lastIndexOf = substring2.lastIndexOf("_");
                if (lastIndexOf != -1) {
                    String substring3 = substring2.substring(0, lastIndexOf);
                    str2 = substring2.substring(lastIndexOf + 1);
                    substring2 = substring3;
                }
                appVersion = str2;
                AmdcRuntimeInfo.setAppInfo(substring2, str2, substring);
            }
        } catch (Exception unused) {
        }
    }

    public static String getTtid() {
        return ttid;
    }

    public static void setUserId(String str) {
        if (userId == null || !userId.equals(str)) {
            userId = str;
            StrategyCenter.getInstance().forceRefreshStrategy(DispatchConstants.getAmdcServerDomain());
            if (sp != null) {
                sp.edit().putString(USER_ID, str).apply();
            }
        }
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUtdid(String str) {
        if (utdid == null || !utdid.equals(str)) {
            utdid = str;
        }
    }

    public static String getUtdid() {
        if (utdid == null && context != null) {
            utdid = Utils.getDeviceId(context);
        }
        return utdid;
    }

    public static void setBackground(boolean z) {
        isBackground = z;
    }

    public static boolean isAppBackground() {
        if (context == null) {
            return true;
        }
        return isBackground;
    }

    public static void addBucketInfo(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && str.length() <= 32 && str2.length() <= 32) {
            synchronized (GlobalAppRuntimeInfo.class) {
                if (bucketInfos == null) {
                    bucketInfos = new CopyOnWriteArrayList<>();
                }
                bucketInfos.add(str);
                bucketInfos.add(str2);
            }
        }
    }

    public static CopyOnWriteArrayList<String> getBucketInfo() {
        return bucketInfos;
    }

    @Deprecated
    public static void setInitTime(long j) {
        initTime = j;
    }

    @Deprecated
    public static long getInitTime() {
        return initTime;
    }

    @Deprecated
    public static int getStartType() {
        SceneInfo sceneInfo = AnalysisFactory.getInstance().getSceneInfo();
        if (sceneInfo != null) {
            return sceneInfo.startType;
        }
        return -1;
    }
}
