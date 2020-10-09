package com.huawei.android.hms.agent.common;

import android.util.Log;
import com.taobao.weex.el.parse.Operators;

public final class HMSAgentLog {
    private static final int PRINT_STACK_COUTN = 2;
    private static final int START_STACK_INDEX = 4;
    private static IHMSAgentLogCallback logCallback;

    public interface IHMSAgentLogCallback {
        void logD(String str, String str2);

        void logE(String str, String str2);

        void logI(String str, String str2);

        void logV(String str, String str2);

        void logW(String str, String str2);
    }

    public static void setHMSAgentLogCallback(IHMSAgentLogCallback iHMSAgentLogCallback) {
        logCallback = iHMSAgentLogCallback;
    }

    public static void d(String str) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(str);
        if (logCallback != null) {
            logCallback.logD("HMSAgent", sb.toString());
        } else {
            Log.d("HMSAgent", sb.toString());
        }
    }

    public static void v(String str) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(str);
        if (logCallback != null) {
            logCallback.logV("HMSAgent", sb.toString());
        } else {
            Log.v("HMSAgent", sb.toString());
        }
    }

    public static void i(String str) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(str);
        if (logCallback != null) {
            logCallback.logI("HMSAgent", sb.toString());
        } else {
            Log.i("HMSAgent", sb.toString());
        }
    }

    public static void w(String str) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(str);
        if (logCallback != null) {
            logCallback.logW("HMSAgent", sb.toString());
        } else {
            Log.w("HMSAgent", sb.toString());
        }
    }

    public static void e(String str) {
        StringBuilder sb = new StringBuilder();
        appendStack(sb);
        sb.append(str);
        if (logCallback != null) {
            logCallback.logE("HMSAgent", sb.toString());
        } else {
            Log.e("HMSAgent", sb.toString());
        }
    }

    private static void appendStack(StringBuilder sb) {
        int indexOf;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null && stackTrace.length > 4) {
            for (int min = Math.min(stackTrace.length - 1, 6); min >= 4; min--) {
                if (stackTrace[min] != null) {
                    String fileName = stackTrace[min].getFileName();
                    if (fileName != null && (indexOf = fileName.indexOf(46)) > 0) {
                        fileName = fileName.substring(0, indexOf);
                    }
                    sb.append(fileName);
                    sb.append('(');
                    sb.append(stackTrace[min].getLineNumber());
                    sb.append(Operators.BRACKET_END_STR);
                    sb.append("->");
                }
            }
            sb.append(stackTrace[4].getMethodName());
        }
        sb.append(10);
    }
}
