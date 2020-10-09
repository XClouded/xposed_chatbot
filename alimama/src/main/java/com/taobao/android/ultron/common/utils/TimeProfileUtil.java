package com.taobao.android.ultron.common.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import java.util.HashMap;

public class TimeProfileUtil {
    public static final String KEY_ULTRON_PROFILE = "ultronProfile";
    public static final String TAG = "UltronPerformance";
    public static boolean sLogTimeProfile = false;
    private static HashMap<String, Long> sStageTraceIdMap = new HashMap<>();
    private static HashMap<String, Long> sStartTraceIdMap = new HashMap<>();

    public static void start(String str, String str2) {
        if (sLogTimeProfile) {
            UnifyLog.e(TAG, "开始-" + str + ": " + str2);
            sStartTraceIdMap.put(str, Long.valueOf(System.currentTimeMillis()));
        }
    }

    public static void stage(String str, String str2) {
        if (sLogTimeProfile) {
            long currentTimeMillis = System.currentTimeMillis();
            Long l = sStageTraceIdMap.get(str);
            if (l == null) {
                l = sStartTraceIdMap.get(str);
            }
            if (l == null || l.longValue() <= 0) {
                UnifyLog.e(TAG, "过程-" + str + ": " + str2 + ", no time");
            } else {
                UnifyLog.e(TAG, "过程-" + str + ": " + str2 + ", 耗时: " + (currentTimeMillis - l.longValue()));
            }
            sStageTraceIdMap.put(str, Long.valueOf(currentTimeMillis));
        }
    }

    public static void stageFromStart(String str, String str2) {
        if (sLogTimeProfile) {
            long currentTimeMillis = System.currentTimeMillis();
            Long l = sStartTraceIdMap.get(str);
            if (l == null) {
                UnifyLog.e(TAG, "开始至过程-" + str + ": " + str2 + ", no time");
                return;
            }
            if (l.longValue() > 0) {
                UnifyLog.e(TAG, "开始至过程-" + str + ": " + str2 + ", 耗时: " + (currentTimeMillis - l.longValue()));
            }
            sStageTraceIdMap.put(str, Long.valueOf(currentTimeMillis));
        }
    }

    public static void end(String str, String str2) {
        if (sLogTimeProfile) {
            long currentTimeMillis = System.currentTimeMillis();
            Long l = sStageTraceIdMap.get(str);
            Long l2 = sStartTraceIdMap.get(str);
            if (l != null && l.longValue() > 0) {
                UnifyLog.e(TAG, "结束-" + str + ": " + str2 + ", 耗时: " + (currentTimeMillis - l.longValue()));
            }
            if (l2 != null && l2.longValue() > 0) {
                UnifyLog.e(TAG, "从开始到结束" + str + ": " + str2 + ", 总耗时: " + (currentTimeMillis - l2.longValue()));
            }
            sStageTraceIdMap.remove(str);
            sStartTraceIdMap.remove(str);
        }
    }

    public static String processSwitch(Context context, Uri uri) {
        String queryParameter;
        if (context == null || uri == null || (queryParameter = uri.getQueryParameter(KEY_ULTRON_PROFILE)) == null) {
            return null;
        }
        sLogTimeProfile = Boolean.TRUE.toString().equals(queryParameter);
        String str = "更新开关: ultronProfile is " + sLogTimeProfile;
        Toast.makeText(context.getApplicationContext(), str, 0).show();
        return str;
    }
}
