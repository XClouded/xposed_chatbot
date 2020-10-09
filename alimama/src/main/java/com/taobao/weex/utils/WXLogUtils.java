package com.taobao.weex.utils;

import android.text.TextUtils;
import android.util.Log;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.performance.WXStateRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WXLogUtils {
    private static final String CLAZZ_NAME_LOG_UTIL = "com.taobao.weex.devtools.common.LogUtil";
    public static final String WEEX_PERF_TAG = "weex_perf";
    public static final String WEEX_TAG = "weex";
    private static StringBuilder builder = new StringBuilder(50);
    private static HashMap<String, Class> clazzMaps = new HashMap<>(2);
    private static List<JsLogWatcher> jsLogWatcherList = new ArrayList();
    private static LogWatcher sLogWatcher;

    public interface JsLogWatcher {
        void onJsLog(int i, String str);
    }

    public interface LogWatcher {
        void onLog(String str, String str2, String str3);
    }

    public static void performance(String str, byte[] bArr) {
    }

    static {
        clazzMaps.put(CLAZZ_NAME_LOG_UTIL, loadClass(CLAZZ_NAME_LOG_UTIL));
    }

    private static Class loadClass(String str) {
        try {
            Class<?> cls = Class.forName(str);
            if (cls == null) {
                return cls;
            }
            try {
                clazzMaps.put(str, cls);
                return cls;
            } catch (ClassNotFoundException unused) {
                return cls;
            }
        } catch (ClassNotFoundException unused2) {
            return null;
        }
    }

    public static void renderPerformanceLog(String str, long j) {
        if (!WXEnvironment.isApkDebugable()) {
            WXEnvironment.isPerf();
        }
    }

    private static void log(String str, String str2, LogLevel logLevel) {
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str) && logLevel != null && !TextUtils.isEmpty(logLevel.getName())) {
            if (logLevel == LogLevel.ERROR && !TextUtils.isEmpty(str2) && str2.contains("IPCException")) {
                WXStateRecord.getInstance().recordIPCException("ipc", str2);
            }
            if (sLogWatcher != null) {
                sLogWatcher.onLog(logLevel.getName(), str, str2);
            }
            if (WXEnvironment.isApkDebugable()) {
                if (logLevel.getValue() - WXEnvironment.sLogLevel.getValue() >= 0) {
                    Log.println(logLevel.getPriority(), str, str2);
                    writeConsoleLog(logLevel.getName(), str2);
                }
            } else if (logLevel.getValue() - LogLevel.WARN.getValue() >= 0 && logLevel.getValue() - WXEnvironment.sLogLevel.getValue() >= 0) {
                Log.println(logLevel.getPriority(), str, str2);
            }
        }
    }

    public static void v(String str) {
        v("weex", str);
    }

    public static void d(String str) {
        d("weex", str);
    }

    public static void d(String str, byte[] bArr) {
        d(str, new String(bArr));
    }

    public static void i(String str) {
        i("weex", str);
    }

    public static void i(String str, byte[] bArr) {
        i(str, new String(bArr));
    }

    public static void info(String str) {
        i("weex", str);
    }

    public static void w(String str) {
        w("weex", str);
    }

    public static void w(String str, byte[] bArr) {
        w(str, new String(bArr));
    }

    public static void e(String str) {
        e("weex", str);
    }

    public static void e(String str, byte[] bArr) {
        e(str, new String(bArr));
    }

    public static void wtf(String str) {
        wtf("weex", str);
    }

    public static void d(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            log(str, str2, LogLevel.DEBUG);
            if (WXEnvironment.isApkDebugable() && "jsLog".equals(str) && jsLogWatcherList != null && jsLogWatcherList.size() > 0) {
                for (JsLogWatcher next : jsLogWatcherList) {
                    if (str2.endsWith("__DEBUG")) {
                        next.onJsLog(3, str2.replace("__DEBUG", ""));
                    } else if (str2.endsWith("__INFO")) {
                        next.onJsLog(3, str2.replace("__INFO", ""));
                    } else if (str2.endsWith("__WARN")) {
                        next.onJsLog(3, str2.replace("__WARN", ""));
                    } else if (str2.endsWith("__ERROR")) {
                        next.onJsLog(3, str2.replace("__ERROR", ""));
                    } else {
                        next.onJsLog(3, str2);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.taobao.weex.utils.LogLevel getLogLevel(java.lang.String r1) {
        /*
            java.lang.String r1 = r1.trim()
            int r0 = r1.hashCode()
            switch(r0) {
                case -1485211506: goto L_0x0034;
                case -1484806554: goto L_0x002a;
                case 90640196: goto L_0x0020;
                case 1198194259: goto L_0x0016;
                case 1199520264: goto L_0x000c;
                default: goto L_0x000b;
            }
        L_0x000b:
            goto L_0x003e
        L_0x000c:
            java.lang.String r0 = "__ERROR"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x003e
            r1 = 0
            goto L_0x003f
        L_0x0016:
            java.lang.String r0 = "__DEBUG"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x003e
            r1 = 4
            goto L_0x003f
        L_0x0020:
            java.lang.String r0 = "__LOG"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x003e
            r1 = 3
            goto L_0x003f
        L_0x002a:
            java.lang.String r0 = "__WARN"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x003e
            r1 = 1
            goto L_0x003f
        L_0x0034:
            java.lang.String r0 = "__INFO"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x003e
            r1 = 2
            goto L_0x003f
        L_0x003e:
            r1 = -1
        L_0x003f:
            switch(r1) {
                case 0: goto L_0x0051;
                case 1: goto L_0x004e;
                case 2: goto L_0x004b;
                case 3: goto L_0x0048;
                case 4: goto L_0x0045;
                default: goto L_0x0042;
            }
        L_0x0042:
            com.taobao.weex.utils.LogLevel r1 = com.taobao.weex.utils.LogLevel.DEBUG
            return r1
        L_0x0045:
            com.taobao.weex.utils.LogLevel r1 = com.taobao.weex.utils.LogLevel.DEBUG
            return r1
        L_0x0048:
            com.taobao.weex.utils.LogLevel r1 = com.taobao.weex.utils.LogLevel.INFO
            return r1
        L_0x004b:
            com.taobao.weex.utils.LogLevel r1 = com.taobao.weex.utils.LogLevel.INFO
            return r1
        L_0x004e:
            com.taobao.weex.utils.LogLevel r1 = com.taobao.weex.utils.LogLevel.WARN
            return r1
        L_0x0051:
            com.taobao.weex.utils.LogLevel r1 = com.taobao.weex.utils.LogLevel.ERROR
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXLogUtils.getLogLevel(java.lang.String):com.taobao.weex.utils.LogLevel");
    }

    public static void i(String str, String str2) {
        log(str, str2, LogLevel.INFO);
    }

    public static void v(String str, String str2) {
        log(str, str2, LogLevel.VERBOSE);
    }

    public static void w(String str, String str2) {
        log(str, str2, LogLevel.WARN);
    }

    public static void e(String str, String str2) {
        log(str, str2, LogLevel.ERROR);
    }

    public static void wtf(String str, String str2) {
        log(str, str2, LogLevel.WTF);
    }

    public static void p(String str) {
        d(WEEX_PERF_TAG, str);
    }

    public static void d(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            d(str + getStackTrace(th));
        }
    }

    public static void i(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            info(str + getStackTrace(th));
        }
    }

    public static void v(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            v(str + getStackTrace(th));
        }
    }

    public static void w(String str, Throwable th) {
        w(str + getStackTrace(th));
    }

    public static void e(String str, Throwable th) {
        e(str + getStackTrace(th));
    }

    public static void wtf(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            wtf(str + getStackTrace(th));
        }
    }

    public static void p(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            p(str + getStackTrace(th));
        }
    }

    public static void eTag(String str, Throwable th) {
        if (WXEnvironment.isApkDebugable()) {
            e(str, getStackTrace(th));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0032 A[SYNTHETIC, Splitter:B:21:0x0032] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x003c  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStackTrace(@androidx.annotation.Nullable java.lang.Throwable r3) {
        /*
            if (r3 != 0) goto L_0x0005
            java.lang.String r3 = ""
            return r3
        L_0x0005:
            r0 = 0
            java.io.StringWriter r1 = new java.io.StringWriter     // Catch:{ all -> 0x002e }
            r1.<init>()     // Catch:{ all -> 0x002e }
            java.io.PrintWriter r2 = new java.io.PrintWriter     // Catch:{ all -> 0x002c }
            r2.<init>(r1)     // Catch:{ all -> 0x002c }
            r3.printStackTrace(r2)     // Catch:{ all -> 0x0029 }
            r2.flush()     // Catch:{ all -> 0x0029 }
            r1.flush()     // Catch:{ all -> 0x0029 }
            r1.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0021
        L_0x001d:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0021:
            r2.close()
            java.lang.String r3 = r1.toString()
            return r3
        L_0x0029:
            r3 = move-exception
            r0 = r2
            goto L_0x0030
        L_0x002c:
            r3 = move-exception
            goto L_0x0030
        L_0x002e:
            r3 = move-exception
            r1 = r0
        L_0x0030:
            if (r1 == 0) goto L_0x003a
            r1.close()     // Catch:{ IOException -> 0x0036 }
            goto L_0x003a
        L_0x0036:
            r1 = move-exception
            r1.printStackTrace()
        L_0x003a:
            if (r0 == 0) goto L_0x003f
            r0.close()
        L_0x003f:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXLogUtils.getStackTrace(java.lang.Throwable):java.lang.String");
    }

    private static void writeConsoleLog(String str, String str2) {
        if (WXEnvironment.isApkDebugable()) {
            try {
                Class cls = clazzMaps.get(CLAZZ_NAME_LOG_UTIL);
                if (cls != null) {
                    cls.getMethod("log", new Class[]{String.class, String.class}).invoke(cls, new Object[]{str, str2});
                }
            } catch (Exception unused) {
                Log.d("weex", "LogUtil not found!");
            }
        }
    }

    public static void setJsLogWatcher(JsLogWatcher jsLogWatcher) {
        if (!jsLogWatcherList.contains(jsLogWatcher)) {
            jsLogWatcherList.add(jsLogWatcher);
        }
    }

    public static void setLogWatcher(LogWatcher logWatcher) {
        sLogWatcher = logWatcher;
    }
}
