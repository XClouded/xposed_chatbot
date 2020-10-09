package com.uc.webview.export.internal.setup;

import android.content.Context;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.wireless.security.SecExceptionCode;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.uc.startup.a;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.i;
import com.uc.webview.export.internal.utility.k;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* compiled from: U4Source */
public class af {
    public static Context a = null;
    public static boolean b = true;
    public static br c;
    public static ConcurrentHashMap<String, Object> d = new ConcurrentHashMap<>();
    public static boolean e = true;
    private static ClassLoader f = af.class.getClassLoader();
    private static int g = 0;
    private static int h = 0;
    private static AtomicBoolean i = new AtomicBoolean(false);
    private static final AtomicBoolean j = new AtomicBoolean(false);
    private static HashSet<Integer> k = new HashSet<>();
    private static final AtomicBoolean l = new AtomicBoolean(false);

    public static void a(ConcurrentHashMap<String, Object> concurrentHashMap) {
        long d2 = b.d();
        d.putAll(concurrentHashMap);
        a = (Context) d.get("CONTEXT");
        b = !k.b((Boolean) d.get(UCCore.OPTION_USE_SDK_SETUP));
        e = j.a(k.a(concurrentHashMap, UCCore.OPTION_HARDWARE_ACCELERATED));
        Integer num = (Integer) d.get(UCCore.OPTION_STARTUP_POLICY);
        if (num != null) {
            g = num.intValue();
        }
        if (g == 0) {
            g = 128;
        }
        b.a(15, String.valueOf(g));
        b.a((int) SecExceptionCode.SEC_ERROR_STA_INVALID_ENCRYPTED_DATA, b.d() - d2);
        Log.i("SetupController", "initSdkSetup sSetupPolicy:" + g + ", sUseSdkSetup:" + b + ", sIsHardwareAccleration:" + e);
    }

    public static final Object a(String str) {
        return d.get(str);
    }

    public static int a() {
        return g;
    }

    public static int c() {
        return h;
    }

    public static void d() {
        h = 0;
    }

    public static void a(int i2, Object... objArr) {
        Log.i("SetupController", "onReveiveSetupStatus status:" + i2);
        h = i2;
        if (i2 == 1) {
            ClassLoader classLoader = objArr[0];
            if (classLoader != null) {
                a(classLoader);
            }
            j.a();
        } else if (i2 == 3) {
        } else {
            if (i2 != 5) {
                switch (i2) {
                    case 7:
                        a(4);
                        return;
                    case 8:
                        a(3);
                        return;
                    case 9:
                        b.a();
                        ak.a(objArr[0]);
                        return;
                    case 10:
                        b.b();
                        return;
                    case 11:
                        return;
                    default:
                        return;
                }
            } else {
                if (SDKFactory.d() != 2) {
                    i.set(true);
                    a.a(ILocatable.ErrorCode.LOCATION_ERROR, (Object[]) null);
                    synchronized (l) {
                        if (l.get()) {
                            a.a(9010, (Object[]) null);
                        }
                    }
                }
                b.a(42);
                a(2);
            }
        }
    }

    public static void a(int i2) {
        synchronized (k) {
            if (!k.contains(Integer.valueOf(i2))) {
                k.add(Integer.valueOf(i2));
                Log.d("SetupController", "doCoreStatusStat status:" + i2);
                i.a((Runnable) new ag(i2));
            }
        }
    }

    public static void a(ClassLoader classLoader) {
        synchronized (j) {
            f = classLoader;
        }
    }

    public static ClassLoader e() {
        ClassLoader classLoader;
        synchronized (j) {
            classLoader = f;
        }
        return classLoader;
    }

    static ClassLoader a(String str, String str2, String str3) {
        b.a(532);
        synchronized (j) {
            if (j.compareAndSet(false, true)) {
                m mVar = new m(str, str2, str3);
                mVar.run();
                a(mVar.c);
            }
        }
        b.a(533);
        return e();
    }

    public static void f() {
        synchronized (l) {
            if (i.get()) {
                a.a(9010, (Object[]) null);
            } else {
                l.set(true);
            }
        }
    }

    public static boolean b() {
        return !((g & 128) != 0);
    }
}
