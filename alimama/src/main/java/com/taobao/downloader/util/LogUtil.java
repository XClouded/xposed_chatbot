package com.taobao.downloader.util;

import android.util.Log;
import com.taobao.downloader.Configuration;
import com.taobao.downloader.adpater.Logger;
import java.util.StringTokenizer;

public class LogUtil {
    private static String PREFIX = "downloader.";

    public static void debug(String str, String str2, Object... objArr) {
        if (Configuration.logDebugEnabled) {
            if (Configuration.logger != null) {
                Logger logger = Configuration.logger;
                logger.debug(PREFIX + str, appendLogMessage(str2, objArr));
                return;
            }
            Log.d(PREFIX + str + ":" + Thread.currentThread().getId(), appendLogMessage(str2, objArr));
        }
    }

    private static String appendLogMessage(String str, Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            return str;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str, "{}");
        StringBuilder sb = new StringBuilder();
        boolean z = str.indexOf("{}") == 0;
        for (Object string : objArr) {
            String string2 = getString(string);
            if (stringTokenizer.hasMoreTokens()) {
                String nextToken = stringTokenizer.nextToken();
                if (z) {
                    sb.append(string2);
                    sb.append(nextToken);
                } else {
                    sb.append(nextToken);
                    sb.append(string2);
                }
            } else {
                sb.append("|");
                sb.append(string2);
            }
        }
        return sb.toString();
    }

    private static String getString(Object obj) {
        if (obj instanceof byte[]) {
            return new String((byte[]) obj);
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return obj.toString();
        } catch (Throwable unused) {
            return "";
        }
    }

    public static void warn(String str, String str2, Object... objArr) {
        if (Configuration.logger != null) {
            Logger logger = Configuration.logger;
            logger.error(PREFIX + str, appendLogMessage(str2, objArr));
            return;
        }
        Log.w(PREFIX + str, appendLogMessage(str2, objArr));
    }

    public static void error(String str, String str2, Object... objArr) {
        if (Configuration.logger != null) {
            Logger logger = Configuration.logger;
            logger.error(PREFIX + str, appendLogMessage(str2, objArr));
            return;
        }
        Log.e(PREFIX + str, appendLogMessage(str2, objArr));
    }

    public static void error(String str, String str2, Throwable th) {
        if (Configuration.logger != null) {
            Logger logger = Configuration.logger;
            logger.error(PREFIX + str, str2, th);
            return;
        }
        Log.e(PREFIX + str, str2, th);
    }
}
