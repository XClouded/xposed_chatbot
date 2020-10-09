package android.taobao.windvane.util;

import android.app.Application;
import android.content.res.Resources;
import android.taobao.windvane.config.GlobalConfig;

import org.android.agoo.common.AgooConstants;

public class EnvUtil {
    private static boolean DEBUG = false;
    private static boolean inited = false;
    private static boolean openSpdyforDebug = false;

    public static boolean isDebug() {
        return TaoLog.getLogStatus() && isAppDebug();
    }

    public static boolean isAppDebug() {
        if (!inited) {
            init();
        }
        return DEBUG;
    }

    private static synchronized void init() {
        synchronized (EnvUtil.class) {
            if (!inited) {
                try {
                    Application application = GlobalConfig.context;
                    if (application != null) {
                        DEBUG = (application.getApplicationInfo().flags & 2) != 0;
                        inited = true;
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    public static boolean isOpenSpdyforDebug() {
        return openSpdyforDebug;
    }

    public static void setOpenSpdyforDebug(boolean z) {
        openSpdyforDebug = z;
    }

    public static boolean isCN() {
        try {
            return Resources.getSystem().getConfiguration().locale.getLanguage().contains("zh");
        } catch (Throwable unused) {
            return false;
        }
    }

    public static boolean isTaobao() {
        if (GlobalConfig.context != null) {
            return AgooConstants.TAOBAO_PACKAGE.equals(GlobalConfig.context.getPackageName());
        }
        return false;
    }
}
