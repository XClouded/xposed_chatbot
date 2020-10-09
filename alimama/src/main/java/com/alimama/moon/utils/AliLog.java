package com.alimama.moon.utils;

import android.util.Log;

public class AliLog {
    public static final int ASSERT = 6;
    public static final int DEBUG = 2;
    public static final int ERROR = 5;
    public static final int INFO = 3;
    private static final String TAG = "Moon";
    public static final int VERBOSE = 1;
    public static final int WARN = 4;
    private static int mLevel = 2;

    public static boolean isLoggable(int i) {
        return Log.isLoggable(TAG, i);
    }

    public static void setLogLevel(int i) {
        if (i >= 1 && i <= 6) {
            mLevel = i;
        }
    }

    public static void LogV(String str) {
        if (mLevel <= 1) {
            Log.v("", str);
        }
    }

    public static void LogV(String str, String str2) {
        if (mLevel <= 1) {
            Log.v(str, str2);
        }
    }

    public static void LogD(String str) {
        if (mLevel <= 2) {
            Log.d("", str);
        }
    }

    public static void LogD(String str, String str2) {
        if (mLevel <= 2) {
            Log.d(str, str2);
        }
    }

    public static void LogE(String str) {
        if (mLevel <= 5) {
            Log.e("", str);
        }
    }

    public static void LogE(String str, String str2) {
        if (mLevel <= 5) {
            Log.e(str, str2);
        }
    }
}
