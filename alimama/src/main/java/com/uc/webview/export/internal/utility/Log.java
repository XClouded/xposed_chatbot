package com.uc.webview.export.internal.utility;

import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.weex.ui.component.WXComponent;
import com.uc.webview.export.annotations.Interface;
import com.uc.webview.export.cyclone.UCLogger;
import java.util.concurrent.ConcurrentHashMap;

@Interface
/* compiled from: U4Source */
public class Log {
    private static boolean a = false;
    private static final ConcurrentHashMap<String, UCLogger> b = new ConcurrentHashMap<>();

    public static boolean enableUCLogger() {
        return a;
    }

    private static UCLogger a(String str, String str2) {
        if (!UCLogger.enable(str, str2)) {
            return null;
        }
        if (b.containsKey(str)) {
            return b.get(str);
        }
        UCLogger create = UCLogger.create(str);
        b.put(str, create);
        return create;
    }

    private Log() {
    }

    public static void d(String str, String str2) {
        UCLogger a2 = a("d", str);
        if (a2 != null) {
            a2.print(str, str2, new Throwable[0]);
        }
    }

    public static void d(String str, String str2, Throwable th) {
        UCLogger a2 = a("d", str);
        if (a2 != null) {
            a2.print(str, str2, th);
        }
    }

    public static void e(String str, String str2) {
        UCLogger a2 = a("e", str);
        if (a2 != null) {
            a2.print(str, str2, new Throwable[0]);
        }
    }

    public static void e(String str, String str2, Throwable th) {
        UCLogger a2 = a("e", str);
        if (a2 != null) {
            a2.print(str, str2, th);
        }
    }

    public static void i(String str, String str2) {
        UCLogger a2 = a(UploadQueueMgr.MSGTYPE_INTERVAL, str);
        if (a2 != null) {
            a2.print(str, str2, new Throwable[0]);
        }
    }

    public static void i(String str, String str2, Throwable th) {
        UCLogger a2 = a(UploadQueueMgr.MSGTYPE_INTERVAL, str);
        if (a2 != null) {
            a2.print(str, str2, th);
        }
    }

    public static void w(String str, String str2) {
        UCLogger a2 = a(WXComponent.PROP_FS_WRAP_CONTENT, str);
        if (a2 != null) {
            a2.print(str, str2, new Throwable[0]);
        }
    }

    public static void w(String str, String str2, Throwable th) {
        UCLogger a2 = a(WXComponent.PROP_FS_WRAP_CONTENT, str);
        if (a2 != null) {
            a2.print(str, str2, th);
        }
    }

    public static void setPrintLog(boolean z, Object[] objArr) {
        a = z;
        if (objArr != null && objArr.length == 5) {
            objArr[0] = Boolean.valueOf(z);
            UCLogger.setup(objArr);
        }
    }
}
