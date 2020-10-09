package com.taobao.android.modular;

import android.util.Log;

public class MLog {
    private static final LogProvider DEFAULT_LOGGER = new DefaultLogger();
    private static volatile LogProvider mLogger = DEFAULT_LOGGER;

    public static void set(LogProvider logProvider) {
        mLogger = logProvider;
    }

    public static void e(String str, String str2) {
        mLogger.e(str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        mLogger.e(str, str2, th);
    }

    public static void w(String str, String str2, Throwable th) {
        mLogger.w(str, str2, th);
    }

    public static void w(String str, String str2) {
        mLogger.w(str, str2);
    }

    public static void d(String str, String str2) {
        mLogger.d(str, str2);
    }

    private static final class DefaultLogger implements LogProvider {
        private DefaultLogger() {
        }

        public void e(String str, String str2) {
            Log.e(str, str2);
        }

        public void e(String str, String str2, Throwable th) {
            Log.e(str, str2, th);
        }

        public void w(String str, String str2) {
            Log.w(str, str2);
        }

        public void w(String str, String str2, Throwable th) {
            Log.w(str, str2, th);
        }

        public void d(String str, String str2) {
            Log.d(str, str2);
        }
    }
}
