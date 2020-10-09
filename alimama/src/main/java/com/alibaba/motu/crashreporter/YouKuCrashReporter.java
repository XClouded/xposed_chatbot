package com.alibaba.motu.crashreporter;

import android.content.Context;
import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class YouKuCrashReporter {
    public static void initYouKuCrashReporter(final Context context) {
        try {
            ReporterConfigure reporterConfigure = new ReporterConfigure();
            reporterConfigure.setEnableDumpSysLog(true);
            reporterConfigure.setEnableDumpRadioLog(true);
            reporterConfigure.setEnableDumpEventsLog(true);
            reporterConfigure.setEnableCatchANRException(true);
            reporterConfigure.setEnableANRMainThreadOnly(true);
            reporterConfigure.setEnableDumpAllThread(true);
            String contextAppVersion = Utils.getContextAppVersion(context);
            if (contextAppVersion == null) {
                contextAppVersion = "defaultVersion";
            }
            String str = contextAppVersion;
            String defaultAppkey = getDefaultAppkey();
            if (MotuCrashReporter.getInstance().enable(context, defaultAppkey + "@android", defaultAppkey, str, "channel", (String) null, reporterConfigure)) {
                LogUtil.d("crashreporter enable success");
            } else {
                LogUtil.d("crashreporter enable failure");
            }
            MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) new IUTCrashCaughtListener() {
                public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
                    HashMap hashMap = new HashMap();
                    try {
                        Utils.getMTLMetaData(hashMap, context);
                    } catch (Exception unused) {
                    }
                    return hashMap;
                }
            });
        } catch (Exception e) {
            LogUtil.e("enable", e);
        }
    }

    private static String getDefaultAppkey() {
        try {
            Class<?> cls = Class.forName("com.youku.phone.keycenter.YkKeyCenterConstant");
            Method declaredMethod = cls.getDeclaredMethod("getAppkeyRelease", new Class[0]);
            declaredMethod.setAccessible(true);
            String valueOf = String.valueOf(declaredMethod.invoke(cls, new Object[0]));
            return TextUtils.isEmpty(valueOf) ? "23570660" : valueOf;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "23570660";
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return "23570660";
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return "23570660";
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return "23570660";
        }
    }
}
