package alimama.com.unwetaologger.base;

import android.util.Log;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.accs.utl.UTMini;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import com.ut.mini.UTTracker;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import java.util.Map;

public class UNWLogger {
    public static final int DEBUG = 1;
    public static final int ERROR = 4;
    public static final int INFO = 2;
    public static final String LOG_KEY_MODULE = "module";
    public static final String LOG_VALUE_SUBTYPE_H5 = "h5";
    public static final String LOG_VALUE_SUBTYPE_MTOP = "mtop";
    public static final String LOG_VALUE_SUBTYPE_NATIVE = "native";
    public static final String LOG_VALUE_TYPE_PERFORMANCE = "performance";
    public static final String LOG_VALUE_TYPE_PROCESS = "process";
    public static final String LOG_VALUE_TYPE_USER = "user";
    public static final String UNWLOG_LOCAL_TAG = "UNWLOG";
    public static final String UNWLOG_TAG = "/all-path.event.log";
    public static final int WARN = 3;
    private String mModule;

    public UNWLogger(String str) {
        this.mModule = str;
    }

    private boolean isEnable() {
        return UNWLoggerManager.getInstance().isEnable();
    }

    private boolean isLocalLogEnable() {
        return UNWLoggerManager.getInstance().getLocalEnable();
    }

    private int getLocalLogLevel() {
        return UNWLoggerManager.getInstance().getLevel();
    }

    public void debug(String str, String str2) {
        if (isLocalLogEnable() && getLocalLogLevel() <= 1) {
            Log.d("UNWLOG_" + this.mModule + "_" + str, "debug: " + str2);
        }
    }

    public void debug(LogContent logContent) {
        if (logContent != null) {
            makeLocalLog(logContent, 1);
        }
    }

    public void info(LogContent logContent) {
        if (logContent != null) {
            makeLocalLog(logContent, 2);
            logContent.processInfoMap();
            makeUTLog(logContent);
        }
    }

    public void error(ErrorContent errorContent) {
        if (errorContent != null) {
            makeLocalLog(errorContent, 4);
            errorContent.addInfoItem("error", "1");
            errorContent.addInfoItem("errorCode", errorContent.errorCode);
            errorContent.processInfoMap();
            makeUTLog(errorContent);
            AppMonitor.Alarm.commitFail(this.mModule, errorContent.getPoint(), errorContent.getInfoMapStr(), errorContent.getErrorCode(), errorContent.getMsg());
        }
    }

    public void succeed(SuccessContent successContent) {
        AppMonitor.Alarm.commitSuccess(this.mModule, successContent.getPoint(), successContent.getInfoMapStr());
    }

    private static void sendCustomUT(String str, String str2, Map<String, String> map) {
        UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str2);
        uTCustomHitBuilder.setEventPage(str);
        if (map != null) {
            uTCustomHitBuilder.setProperties(map);
        }
        UTTracker defaultTracker = UTAnalytics.getInstance().getDefaultTracker();
        if (defaultTracker != null) {
            defaultTracker.send(uTCustomHitBuilder.build());
        }
    }

    private static void setCustomUTLog(String str, String str2, String str3, String str4, Map<String, String> map) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder(str, UTMini.EVENTID_AGOO, str2, str3, str4, map).build());
    }

    private void makeLocalLog(LogContent logContent, int i) {
        if (isLocalLogEnable() && getLocalLogLevel() <= i) {
            String str = "UNWLOG_" + this.mModule + "_" + logContent.getName() + "_" + logContent.getPoint();
            String str2 = "type: " + logContent.getType() + ", subType: " + logContent.getSubType() + ", args: " + logContent.getAllInfoMapStr();
            if (i != 4) {
                switch (i) {
                    case 1:
                        Log.d(str, str2);
                        return;
                    case 2:
                        Log.i(str, str2);
                        return;
                    default:
                        return;
                }
            } else {
                Log.e(str, str2);
            }
        }
    }

    private void makeUTLog(LogContent logContent) {
        Map<String, String> newAllInfoMap = logContent.getNewAllInfoMap();
        newAllInfoMap.put("module", this.mModule);
        setCustomUTLog(this.mModule, UNWLOG_TAG, "", "", newAllInfoMap);
    }
}
