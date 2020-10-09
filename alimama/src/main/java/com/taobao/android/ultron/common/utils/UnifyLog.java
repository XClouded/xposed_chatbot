package com.taobao.android.ultron.common.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.taobao.android.AliLogInterface;
import com.taobao.android.AliLogServiceFetcher;
import com.taobao.weex.el.parse.Operators;

public class UnifyLog {
    public static final String KEY_USE_ANDROID_LOG = "useAndroidLog";
    private static AliLogInterface logInterface = AliLogServiceFetcher.getLogService();
    public static String sLogBizName = "[Page_DEFAULT]";
    public static boolean sUseAndroidLogForTest = false;

    public static void setBizName(String str) {
        sLogBizName = str;
    }

    public static void w(String str, String... strArr) {
        if (sUseAndroidLogForTest) {
            Log.w(sLogBizName, getLog(str, strArr));
        }
        if (logInterface != null) {
            logInterface.logw(sLogBizName, getLog(str, strArr));
        }
    }

    public static void d(String str, String... strArr) {
        if (sUseAndroidLogForTest) {
            Log.d(sLogBizName, getLog(str, strArr));
        }
        if (logInterface != null) {
            logInterface.logd(sLogBizName, getLog(str, strArr));
        }
    }

    public static void i(String str, String... strArr) {
        if (sUseAndroidLogForTest) {
            Log.i(sLogBizName, getLog(str, strArr));
        }
        if (logInterface != null) {
            logInterface.logi(sLogBizName, getLog(str, strArr));
        }
    }

    public static void e(String str, String... strArr) {
        if (sUseAndroidLogForTest) {
            Log.e(sLogBizName, getLog(str, strArr));
        }
        if (logInterface != null) {
            logInterface.loge(sLogBizName, getLog(str, strArr));
        }
    }

    private static String getLog(String str, String... strArr) {
        if (strArr == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strArr.length; i++) {
            if (i == 0) {
                sb.append(str);
                sb.append(".");
                sb.append(strArr[i]);
                if (strArr.length > 1) {
                    sb.append(" |");
                }
            } else {
                sb.append(Operators.SPACE_STR);
                sb.append(strArr[i]);
            }
        }
        return sb.toString();
    }

    public static String processSwitch(Context context, Uri uri) {
        String queryParameter;
        if (context == null || uri == null || (queryParameter = uri.getQueryParameter(KEY_USE_ANDROID_LOG)) == null) {
            return null;
        }
        sUseAndroidLogForTest = Boolean.TRUE.toString().equals(queryParameter);
        String str = "开关状态: useAndroidLog is " + sUseAndroidLogForTest;
        Toast.makeText(context.getApplicationContext(), str, 0).show();
        return str;
    }
}
