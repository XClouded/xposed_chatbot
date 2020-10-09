package com.uc.sandboxExport.helper;

import android.os.Process;
import com.taobao.onlinemonitor.OnLineMonitor;
import java.lang.reflect.Method;

/* compiled from: U4Source */
public final class a {
    private static boolean a = false;
    private static boolean b = false;
    private static final Object c = new Object();

    public static boolean a() {
        if (!b) {
            synchronized (c) {
                if (!b) {
                    a = b();
                    b = true;
                }
            }
        }
        return a;
    }

    private static boolean b() {
        try {
            Method declaredMethod = Process.class.getDeclaredMethod("isIsolated", new Class[0]);
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke((Object) null, new Object[0]);
                if (invoke != null && (invoke instanceof Boolean)) {
                    return ((Boolean) invoke).booleanValue();
                }
            }
        } catch (Throwable unused) {
        }
        int myUid = Process.myUid() % OnLineMonitor.TASK_TYPE_FROM_BOOT;
        return myUid >= 99000 && myUid <= 99999;
    }
}
