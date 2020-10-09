package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.internal.utility.i;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/* compiled from: U4Source */
public class ae {
    private static final String b = "ae";
    private static ae c;
    protected ConcurrentHashMap<b, c> a;

    /* compiled from: U4Source */
    enum b {
        SETUP_GLOBAL,
        CHECK_OLD_KERNEL,
        VERIFY_SDK_SHELL,
        VERIFY_CORE_JAR,
        LOAD_SDK_SHELL,
        CHECK_VERSION,
        CHECK_SO,
        CHECK_PAK,
        INIT_SDK_SETTINGS,
        SETUP_CORE_FACTORY,
        INIT_UCMOBILE_WEBKIT,
        SETUP_PRINT_LOG
    }

    /* compiled from: U4Source */
    public static class d {
        public static int a = 0;
        public static int b = 1;
    }

    /* compiled from: U4Source */
    public static class e {
        public static int a = -2;
        public static int b = -1;
        public static int c;
    }

    /* compiled from: U4Source */
    public static class f {
        public static int a = -1;
        public static int b = 1;
        public static int c = 2;
    }

    /* compiled from: U4Source */
    public class c {
        public b a;
        public int b;
        public volatile int c;
        public volatile Object d;
        public Future<?> e = null;
        public ValueCallback<Object> f;

        /* JADX WARNING: type inference failed for: r3v0, types: [com.uc.webview.export.internal.setup.UCAsyncTask, com.uc.webview.export.internal.setup.ae$b] */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.util.concurrent.Future<?>, android.webkit.ValueCallback<java.lang.Object>] */
        /* JADX WARNING: Unknown variable types count: 2 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public c(int r2, com.uc.webview.export.internal.setup.UCAsyncTask r3, java.util.concurrent.Future<?> r4) {
            /*
                r0 = this;
                com.uc.webview.export.internal.setup.ae.this = r1
                r0.<init>()
                r0.b = r2
                r0.a = r3
                r1 = 0
                r0.e = r1
                r0.f = r4
                int r1 = com.uc.webview.export.internal.setup.ae.f.a
                r0.c = r1
                int r1 = com.uc.webview.export.internal.setup.ae.e.a
                java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
                r0.d = r1
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.ae.c.<init>(com.uc.webview.export.internal.setup.ae, int, com.uc.webview.export.internal.setup.ae$b, android.webkit.ValueCallback):void");
        }

        public final String toString() {
            Object[] objArr = new Object[5];
            objArr[0] = this.a.toString();
            objArr[1] = Integer.valueOf(this.b);
            objArr[2] = Integer.valueOf(this.c);
            objArr[3] = this.d;
            objArr[4] = this.e != null ? this.e.toString() : "";
            return String.format("Task name: %s, policy: %d, status: %d, result: %s, future: %s", objArr);
        }
    }

    public static ae a() {
        if (c == null) {
            synchronized (ae.class) {
                if (c == null) {
                    c = new ae();
                }
            }
        }
        return c;
    }

    private ae() {
        if (this.a == null) {
            this.a = new ConcurrentHashMap<>();
        }
    }

    public final void a(b bVar) {
        this.a.remove(bVar);
    }

    private static Object a(c cVar) {
        try {
            return cVar.e.get();
        } catch (Exception e2) {
            throw new UCSetupException(4032, (Throwable) e2);
        }
    }

    public final void a(b[] bVarArr) throws UCSetupException {
        for (int i = 0; i <= 0; i++) {
            b(bVarArr[0]);
        }
    }

    private c c(Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        for (Map.Entry next : this.a.entrySet()) {
            if (((c) next.getValue()).e.equals(runnable)) {
                return (c) next.getValue();
            }
        }
        return null;
    }

    public final void a(Runnable runnable) {
        c c2 = c(runnable);
        if (c2 != null) {
            try {
                c2.c = f.b;
                if (c2.f != null) {
                    c2.f.onReceiveValue(c2);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public final void b(Runnable runnable) {
        c c2 = c(runnable);
        if (c2 != null) {
            try {
                c2.c = f.c;
                c2.d = c2.e.get();
                if (c2.f != null) {
                    c2.f.onReceiveValue(c2);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* compiled from: U4Source */
    public class a<V, T> implements Callable<V> {
        ValueCallback<T> a;
        Callable<V> b;

        public a(Callable<V> callable, ValueCallback<T> valueCallback) {
            this.a = valueCallback;
            this.b = callable;
        }

        public final V call() throws Exception {
            V v;
            UCSetupException uCSetupException = null;
            try {
                v = this.b.call();
            } catch (UCSetupException e) {
                UCSetupException uCSetupException2 = e;
                v = null;
                uCSetupException = uCSetupException2;
            } catch (Throwable th) {
                UCSetupException uCSetupException3 = new UCSetupException(3003, th);
                v = null;
                uCSetupException = uCSetupException3;
            }
            if (uCSetupException == null) {
                return v;
            }
            if (this.a != null) {
                this.a.onReceiveValue(uCSetupException);
            }
            return Integer.valueOf(e.b);
        }
    }

    public final c a(int i, b bVar, Callable<?> callable, ValueCallback<Object> valueCallback) {
        com.uc.webview.export.internal.uc.startup.b.a(bVar.ordinal() + 400);
        c cVar = new c(i, bVar, valueCallback);
        if ((i & d.b) != 0) {
            try {
                cVar.c = f.b;
                callable.call();
                cVar.c = f.c;
                valueCallback.onReceiveValue(cVar);
                return null;
            } catch (Exception e2) {
                throw new UCSetupException((Throwable) e2);
            }
        } else {
            cVar.e = i.a(callable);
            this.a.put(cVar.a, cVar);
            return cVar;
        }
    }

    public final Object b(b bVar) {
        c cVar = this.a.get(bVar);
        if (cVar != null) {
            Integer num = (Integer) cVar.d;
            if (num.intValue() == e.a) {
                num = (Integer) a(cVar);
            }
            if (num.equals(Integer.valueOf(e.c))) {
                return num;
            }
            throw new UCSetupException(4032, String.format("task %s failed. result: %d", new Object[]{bVar.toString(), cVar.d}));
        }
        throw new UCSetupException(4033, String.format("没有找到%s任务", new Object[]{bVar.toString()}));
    }
}
