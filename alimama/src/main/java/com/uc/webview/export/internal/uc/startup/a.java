package com.uc.webview.export.internal.uc.startup;

import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.setup.af;
import com.uc.webview.export.internal.uc.CoreFactory;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.i;

/* compiled from: U4Source */
public final class a implements InvokeObject {
    private static final a a = new a();
    private static InvokeObject b = null;

    public static synchronized void a() {
        synchronized (a.class) {
            if (b == null) {
                try {
                    b = (InvokeObject) ReflectionUtil.invoke(Class.forName("com.uc.sdk_glue.StartupBridge", true, af.e()), "doBridge", new Class[]{InvokeObject.class}, new Object[]{a});
                } catch (Throwable th) {
                    a("sdk.StartupBridge", "setupBridge fail.", th);
                }
            }
        }
    }

    public static boolean b() {
        return b != null;
    }

    public static Object a(int i, Object[] objArr) {
        a("sdk.StartupBridge", "StartupBridge call methodID:" + i, (Throwable) null);
        if (b()) {
            return b.invoke(i, objArr);
        }
        a("sdk.StartupBridge", "StartupBridge not enable. fallback to corefactroy methodID:" + i, (Throwable) null);
        return b(i, objArr);
    }

    private static Object b(int i, Object[] objArr) {
        if (i != 9002) {
            try {
                a("sdk.StartupBridge", "fallback error no fallback methodID:" + i, (Throwable) null);
            } catch (Throwable unused) {
                a("sdk.StartupBridge", "fallback to corefactroy error. methodID:" + i, (Throwable) null);
            }
        } else {
            a("sdk.StartupBridge", "fallback to corefactroy methodID:" + i, (Throwable) null);
            CoreFactory.initUCMobileWebkitCoreSoEnv(objArr[0], objArr[1]);
        }
        return null;
    }

    public final Object invoke(int i, Object[] objArr) {
        switch (i) {
            case 9100:
                i.a(objArr[0]);
                break;
            case 9101:
                a(objArr[0], objArr[1], objArr[2]);
                break;
            case 9102:
                Integer num = objArr[0];
                if (objArr.length != 1) {
                    b.a(num.intValue(), objArr[1]);
                    break;
                } else {
                    b.a(num.intValue());
                    break;
                }
            case 9103:
                return b.c();
            case 9104:
                try {
                    Integer num2 = objArr[0];
                    if (num2 != null) {
                        af.a(num2.intValue());
                        break;
                    }
                } catch (Throwable th) {
                    a("sdk.StartupBridge", "onCalled failed", th);
                    break;
                }
                break;
            default:
                a("sdk.StartupBridge", "error methodID:" + i, (Throwable) null);
                break;
        }
        return null;
    }

    private static void a(String str, String str2, Throwable th) {
        Thread currentThread = Thread.currentThread();
        Log.i("sdk.StartupBridge", Operators.BRACKET_START_STR + currentThread.getId() + Operators.SPACE_STR + currentThread.getName() + ") " + str + "." + str2, th);
    }
}
