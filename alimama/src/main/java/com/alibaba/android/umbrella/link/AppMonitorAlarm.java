package com.alibaba.android.umbrella.link;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.android.umbrella.link.export.UmTypeKey;
import com.alibaba.android.umbrella.trace.UmbrellaInfo;
import com.alibaba.mtl.appmonitor.AppMonitor;
import java.util.HashMap;
import java.util.Map;

final class AppMonitorAlarm {
    static final String POINT_COMMIT_FAILURE = "exception_failure";
    static final String POINT_COMMIT_SUCCESS = "exception_success";
    private static final String POINT_INNER_EXCEPTION = "Monitor_Umbrella2_Service";
    static final String POINT_LOG = "exception_log";
    private static final String PURCHASE_MODULE = "Page_Trade_Govern";
    private static final String PURCHASE_POINT_POST = "_Service";
    private static final String PURCHASE_POINT_PRE = "Monitor_";

    AppMonitorAlarm() {
    }

    static void commitSuccessStability(String str, String str2, String str3, String str4, String str5, Map<String, String> map) {
        UmbrellaInfo.UmbrellaBuilder umbrellaBuilder = new UmbrellaInfo.UmbrellaBuilder(str2, str3, str, str4, str5);
        umbrellaBuilder.setVersion(str3).setParams(map);
        umbrellaBuilder.setUmbVersion("2.0");
        UmbrellaInfo build = umbrellaBuilder.build();
        AppMonitor.Alarm.commitSuccess("Page_Trade_Govern", "Monitor_" + build.mainBizName + "_Service", build.toJsonString());
    }

    static void commitFailureStability(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        UmbrellaInfo.UmbrellaBuilder umbrellaBuilder = new UmbrellaInfo.UmbrellaBuilder(str2, str3, str, str4, str5);
        if (!TextUtils.isEmpty(str7)) {
            if (map == null) {
                map = new HashMap<>();
            }
            map.put(ILocatable.ERROR_MSG, str7);
        }
        umbrellaBuilder.setVersion(str3).setParams(map);
        umbrellaBuilder.setUmbVersion("2.0");
        UmbrellaInfo build = umbrellaBuilder.build();
        if (build != null) {
            AppMonitor.Alarm.commitFail("Page_Trade_Govern", "Monitor_" + build.mainBizName + "_Service", build.toJsonString(), str6, str7);
        }
    }

    static void commitFeedback(String str, String str2, UmTypeKey umTypeKey, String str3, String str4) {
        UmbrellaInfo.UmbrellaBuilder umbrellaBuilder = new UmbrellaInfo.UmbrellaBuilder((String) null, (String) null, umTypeKey.getKey(), str, "feedback");
        HashMap hashMap = new HashMap();
        hashMap.put(ILocatable.ERROR_MSG, str4);
        hashMap.put("feedback", str2);
        hashMap.put("stacks", tail2KTread());
        umbrellaBuilder.setParams(hashMap);
        umbrellaBuilder.setUmbVersion("2.0");
        UmbrellaInfo build = umbrellaBuilder.build();
        if (build != null) {
            AppMonitor.Alarm.commitFail("Page_Trade_Govern", "Monitor_" + build.mainBizName + "_Service", build.toJsonString(), str3, str4);
        }
    }

    static void commitInnerException(@Nullable Throwable th, @Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5) {
        try {
            AppMonitor.Alarm.commitFail("Page_Trade_Govern", POINT_INNER_EXCEPTION, tail2KMessage(th), ensureNotNull(str), String.format("%s|%s|%s|%s", new Object[]{ensureNotNull(str2), ensureNotNull(str3), ensureNotNull(str4), ensureNotNull(str5)}));
        } catch (Throwable th2) {
            Log.e("umbrella.LinkLogWorker", "SafetyRunnable catch exception", th2);
        }
    }

    private static String ensureNotNull(@Nullable String str) {
        return UMStringUtils.isEmpty(str) ? "empty" : str;
    }

    private static String tail2KMessage(@Nullable Throwable th) {
        if (th == null) {
            return "empty_throwable";
        }
        String message = th.getMessage();
        if (!UMStringUtils.isEmpty(message)) {
            return message.substring(Math.max(0, message.length() - 2000));
        }
        Class<?> cls = th.getClass();
        if (cls == null) {
            return "empty_message";
        }
        String simpleName = cls.getSimpleName();
        return UMStringUtils.isNotEmpty(simpleName) ? simpleName : "empty_message";
    }

    private static String tail2KTread() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace == null || stackTrace.length == 0) {
            return "empty_stack";
        }
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            sb.append("\nat " + stackTraceElement);
        }
        if (UMStringUtils.isEmpty(sb.toString())) {
            return "empty_message";
        }
        return sb.substring(0, Math.min(2000, sb.length()));
    }
}
