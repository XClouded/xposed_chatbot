package com.taobao.monitor;

import alimama.com.unwetaologger.base.UNWLogger;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.monitor.procedure.Header;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.monitor.procedure.ProcedureManagerProxy;
import com.taobao.monitor.util.ProcessUtils;
import com.taobao.orange.OConstant;
import java.lang.reflect.Method;
import java.util.Map;

public class ProcedureLauncher {
    private static boolean init = false;

    private interface Delay<T> {
        T call();
    }

    private ProcedureLauncher() {
    }

    public static void init(Context context, Map<String, Object> map) {
        if (!init) {
            init = true;
            ProcedureGlobal.instance().setContext(context);
            initHeader(context, map);
            ProcedureManagerProxy.PROXY.setReal(ProcedureGlobal.PROCEDURE_MANAGER);
            ProcedureFactoryProxy.PROXY.setReal(ProcedureGlobal.PROCEDURE_FACTORY);
        }
    }

    private static void initHeader(Context context, Map<String, Object> map) {
        Header.appId = context.getPackageName();
        Header.appKey = safeString(map.get(OConstant.LAUNCH_ONLINEAPPKEY), "12278902");
        Header.appBuild = safeString(map.get(Constants.KEY_APP_BUILD), "");
        Header.appVersion = safeString(map.get("appVersion"), (Delay<String>) new Delay<String>() {
            public String call() {
                Context context = ProcedureGlobal.instance().context();
                try {
                    return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "unknown";
                }
            }
        });
        Header.appPatch = safeString(map.get("appPatch"), "");
        Header.channel = safeString(map.get("channel"), "");
        Header.utdid = safeString(map.get("deviceId"), "");
        Header.brand = Build.BRAND;
        Header.deviceModel = Build.MODEL;
        String[] OS = OS();
        if (!TextUtils.isEmpty(OS[0])) {
            Header.osVersion = OS[0];
            Header.os = OS[1];
        } else {
            Header.osVersion = Build.VERSION.RELEASE;
            Header.os = "android";
        }
        Header.processName = safeString(map.get(UNWLogger.LOG_VALUE_TYPE_PROCESS), (Delay<String>) new Delay<String>() {
            public String call() {
                return ProcessUtils.getCurrProcessName();
            }
        });
        Header.session = String.valueOf(System.currentTimeMillis());
        Header.ttid = safeString(map.get("ttid"), "");
    }

    private static String safeString(Object obj, String str) {
        if (obj instanceof String) {
            String str2 = (String) obj;
            if (!TextUtils.isEmpty(str2)) {
                return str2;
            }
        }
        return str;
    }

    private static String safeString(Object obj, Delay<String> delay) {
        if (obj instanceof String) {
            String str = (String) obj;
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return delay.call();
    }

    @SuppressLint({"DefaultLocale"})
    private static String[] OS() {
        String str;
        String str2 = null;
        try {
            Method method = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
            str = (String) method.invoke((Object) null, new Object[]{"ro.yunos.version"});
            try {
                str2 = (String) method.invoke((Object) null, new Object[]{"java.vm.name"});
            } catch (Exception e) {
                e = e;
                e.printStackTrace();
                return new String[]{str, str2};
            }
        } catch (Exception e2) {
            e = e2;
            str = null;
            e.printStackTrace();
            return new String[]{str, str2};
        }
        return new String[]{str, str2};
    }
}
