package com.alimama.moon.emas.crashreporter;

import android.content.Context;
import androidx.annotation.NonNull;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.alibaba.motu.crashreporter.ReporterConfigure;
import com.alimama.moon.BuildConfig;
import com.alimama.union.app.logger.NewMonitorLogger;
import java.util.HashMap;
import java.util.Map;

public class CrashReporter {
    public static void init(@NonNull Context context, @NonNull String str) {
        ReporterConfigure reporterConfigure = new ReporterConfigure();
        reporterConfigure.setEnableDebug(false);
        reporterConfigure.setEnableDumpSysLog(true);
        reporterConfigure.setEnableDumpRadioLog(false);
        reporterConfigure.setEnableDumpEventsLog(true);
        reporterConfigure.setEnableCatchANRException(true);
        reporterConfigure.setEnableANRMainThreadOnly(true);
        reporterConfigure.setEnableDumpAllThread(true);
        reporterConfigure.enableDeduplication = false;
        MotuCrashReporter instance = MotuCrashReporter.getInstance();
        instance.enable(context, str + "@android", str, BuildConfig.VERSION_NAME, BuildConfig.TTID, (String) null, reporterConfigure);
        MotuCrashReporter.getInstance().registerLifeCallbacks(context);
        MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new UTCrashCaughtListner());
    }

    public static class UTCrashCaughtListner implements IUTCrashCaughtListener {
        public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
            HashMap hashMap = new HashMap();
            if (!(th == null || thread == null)) {
                try {
                    NewMonitorLogger.Crash.crash(th.toString(), thread.toString());
                } catch (Exception unused) {
                }
            }
            return hashMap;
        }
    }
}
