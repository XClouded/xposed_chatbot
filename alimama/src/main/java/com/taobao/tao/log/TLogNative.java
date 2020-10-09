package com.taobao.tao.log;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.android.tlog.protocol.TLogSecret;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TLogNative {
    public static final int AppednerModeAsync = 0;
    public static final int AppednerModeSync = 1;
    private static final int MAX_CACHE_CAPACITY = 100;
    private static final List<XLoggerInfo> sInitCache = new CopyOnWriteArrayList();
    private static volatile boolean sOpenSoSuccess = false;
    private String TAG = "TLOG.TLogNative";

    public static native void addModuleFilter(String str, int i);

    public static native void appenderClose();

    public static native void appenderFlush(boolean z);

    public static native boolean appenderOpen(int i, int i2, String str, String str2, String str3, String str4);

    public static native void cleanModuleFilter();

    public static native int getLogLevel();

    private static native void logWrite(XLoggerInfo xLoggerInfo, String str);

    private static native void logWrite2(int i, String str, String str2, String str3, String str4, String str5, String str6);

    public static native void setAppenderMode(int i);

    public static native void setConsoleLogOpen(boolean z);

    public static native void setLogLevel(int i);

    static class XLoggerInfo {
        public String clientID;
        public String filename;
        public String funcname;
        public int level;
        public int line;
        public String log;
        public long maintid;
        public String module;
        public long pid;
        public String serverID;
        public String tag;
        public long tid;
        public String type;

        XLoggerInfo() {
        }
    }

    public static void appenderOpen(int i, String str, String str2, String str3, String str4) {
        try {
            System.loadLibrary("stlport_shared");
            System.loadLibrary("marsxlog");
            sOpenSoSuccess = appenderOpen(i, 0, str, str2, str3, str4);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void appenderFlushData(boolean z) {
        try {
            appenderFlush(z);
        } catch (Exception e) {
            Log.e("TLogNative", "appenderFlushData failure", e);
        } catch (UnsatisfiedLinkError e2) {
            Log.e("TLogNative", "appenderFlushData failure, unsatisfied link error", e2);
        }
    }

    static boolean isSoOpen() {
        return sOpenSoSuccess;
    }

    public static String getSecurityKey() {
        String securityKey = TLogInitializer.getInstance().getSecurityKey();
        if (TextUtils.isEmpty(securityKey)) {
            securityKey = "t_remote_debugger";
        }
        if (TLogInitializer.getInstance().isDebugable()) {
            Log.d("SecurityKey", securityKey);
        }
        return securityKey;
    }

    public static String getRc4EncryptSecretyKeyValue() {
        try {
            return TLogSecret.getInstance().getRc4EncryptSecretValue(TLogInitializer.getInstance().getSecurityKey());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getRsaPublicKeyMd5Value() {
        return TLogSecret.getInstance().getRsaMd5Value();
    }

    private static void logWrite(int i, String str, String str2, String str3, String str4, String str5, String str6) {
        if (sOpenSoSuccess) {
            if (TextUtils.isEmpty(str) && str2.contains(".")) {
                str = str2.substring(0, str2.indexOf("."));
                str2 = str2.substring(str2.indexOf(".") + 1, str2.length());
            }
            if (TextUtils.isEmpty(str)) {
                str = "module";
            }
            String str7 = str;
            if (TextUtils.isEmpty(str2)) {
                str2 = "tag";
            }
            try {
                logWrite2(i, str7, str2, str3, str4, str5, str6);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void dispatch(int i, String str, String str2, String str3, String str4, String str5, String str6) {
        if (TLogInitializer.getInstance().getInitState() == 2) {
            if (sInitCache.size() > 0) {
                Log.e("tlog", "flush log in asyncInit Mode");
                for (XLoggerInfo next : sInitCache) {
                    logWrite(next.level, next.module, next.tag, next.type, next.clientID, next.serverID, next.log);
                }
                sInitCache.clear();
            }
            logWrite(i, str, str2, str3, str4, str5, str6);
        } else if (TLogInitializer.getInstance().isInitSync()) {
            Log.w("tlog", "tlog isn't init,please call init() ,or initSync(bool) method !");
        } else {
            if (sInitCache.size() >= 100) {
                sInitCache.remove(0);
            }
            XLoggerInfo xLoggerInfo = new XLoggerInfo();
            xLoggerInfo.level = i;
            xLoggerInfo.module = str;
            xLoggerInfo.tag = str2;
            xLoggerInfo.type = str3;
            xLoggerInfo.clientID = str4;
            xLoggerInfo.serverID = str5;
            xLoggerInfo.log = str6;
            sInitCache.add(xLoggerInfo);
        }
    }
}
