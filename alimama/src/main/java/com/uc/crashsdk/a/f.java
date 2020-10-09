package com.uc.crashsdk.a;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import java.util.HashMap;

/* compiled from: ProGuard */
public class f {
    static final /* synthetic */ boolean a = (!f.class.desiredAssertionStatus());
    private static volatile HandlerThread b;
    private static volatile HandlerThread c;
    private static Handler d;
    private static Handler e;
    private static Handler f;
    private static final HashMap<Object, Object[]> g = new HashMap<>();

    public static boolean a(int i, Runnable runnable, long j) {
        Handler handler;
        if (runnable == null) {
            return false;
        }
        switch (i) {
            case 0:
                if (b == null) {
                    a();
                }
                handler = d;
                break;
            case 1:
                if (c == null) {
                    b();
                }
                handler = e;
                break;
            case 2:
                if (f == null) {
                    f = new Handler(Looper.getMainLooper());
                }
                handler = f;
                break;
            default:
                throw new RuntimeException("unknown thread type: " + i);
        }
        if (handler == null) {
            return false;
        }
        e eVar = new e(10, new Object[]{runnable});
        synchronized (g) {
            g.put(runnable, new Object[]{eVar, Integer.valueOf(i)});
        }
        return handler.postDelayed(eVar, j);
    }

    public static void a(int i, Object[] objArr) {
        if (i != 10) {
            if (!a) {
                throw new AssertionError();
            }
        } else if (a || objArr != null) {
            Runnable runnable = objArr[0];
            synchronized (g) {
                if (g.get(runnable) != null) {
                    g.remove(runnable);
                }
            }
            runnable.run();
        } else {
            throw new AssertionError();
        }
    }

    public static boolean a(int i, Runnable runnable) {
        return a(i, runnable, 0);
    }

    public static void a(Runnable runnable) {
        Object[] objArr;
        if (runnable != null) {
            synchronized (g) {
                objArr = g.get(runnable);
            }
            if (objArr != null) {
                Handler handler = null;
                switch (((Integer) objArr[1]).intValue()) {
                    case 0:
                        handler = d;
                        break;
                    case 1:
                        handler = e;
                        break;
                    case 2:
                        handler = f;
                        break;
                }
                if (handler != null) {
                    handler.removeCallbacks((Runnable) objArr[0]);
                }
                synchronized (g) {
                    if (g.get(runnable) != null) {
                        g.remove(runnable);
                    }
                }
            }
        }
    }

    public static boolean b(Runnable runnable) {
        Object[] objArr;
        if (runnable == null) {
            return false;
        }
        synchronized (g) {
            objArr = g.get(runnable);
        }
        if (objArr != null) {
            return true;
        }
        return false;
    }

    private static synchronized void a() {
        synchronized (f.class) {
            if (b == null) {
                HandlerThread handlerThread = new HandlerThread("CrashSDKBkgdHandler", 10);
                b = handlerThread;
                handlerThread.start();
                d = new Handler(b.getLooper());
            }
        }
    }

    private static synchronized void b() {
        synchronized (f.class) {
            if (c == null) {
                HandlerThread handlerThread = new HandlerThread("CrashSDKNormalHandler", 0);
                c = handlerThread;
                handlerThread.start();
                e = new Handler(c.getLooper());
            }
        }
    }
}
