package com.uc.crashsdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.uc.crashsdk.a.a;
import com.uc.crashsdk.a.e;
import com.uc.crashsdk.a.f;
import com.uc.crashsdk.a.g;
import java.io.Closeable;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

/* compiled from: ProGuard */
public class b {
    private static final Object A = new Object();
    private static String B = null;
    private static int C = 0;
    private static RandomAccessFile D = null;
    private static boolean E = false;
    private static final Object F = new Object();
    private static String G = null;
    private static boolean H = false;
    private static volatile Object[] I = null;
    private static Runnable J = new e(101);
    private static boolean K = false;
    private static long L = 0;
    private static final Object M = new Object();
    private static long N = 0;
    private static boolean O = false;
    private static boolean P = false;
    private static boolean Q = false;
    /* access modifiers changed from: private */
    public static final WeakHashMap<Activity, Integer> R = new WeakHashMap<>();
    /* access modifiers changed from: private */
    public static boolean S = false;
    private static boolean T = false;
    private static boolean U = false;
    private static boolean V = false;
    private static boolean W = false;
    private static final Object X = new Object();
    public static boolean a = false;
    public static boolean b = false;
    public static boolean c = false;
    public static boolean d = false;
    public static final Object e = new Object();
    public static boolean f = false;
    public static boolean g = false;
    static final /* synthetic */ boolean h = (!b.class.desiredAssertionStatus());
    private static String i = null;
    private static String j = null;
    private static String k = null;
    private static String l = null;
    private static String m = null;
    private static String n = null;
    private static String o = null;
    private static String p = null;
    private static String q = null;
    private static String r = null;
    private static String s = null;
    private static String t = null;
    private static boolean u = false;
    private static boolean v = false;
    private static volatile boolean w = false;
    private static boolean x = false;
    private static boolean y = false;
    private static boolean z = false;

    static String a() {
        int i2;
        if (B != null) {
            return B;
        }
        String g2 = e.g();
        if (g.a(g2)) {
            B = "LLUN";
        } else {
            if (g2.length() > 48) {
                i2 = g2.length() - 48;
                g2 = g2.substring(0, 48);
            } else {
                i2 = 0;
            }
            StringBuilder sb = new StringBuilder();
            byte[] bytes = g2.getBytes();
            for (int length = bytes.length - 1; length >= 0; length--) {
                byte b2 = bytes[length];
                if (b2 == 46) {
                    sb.append('0');
                } else if (b2 == 58) {
                    sb.append('1');
                } else if (b2 >= 97 && b2 <= 122) {
                    sb.append((char) ((b2 + 65) - 97));
                } else if (b2 >= 65 && b2 <= 90) {
                    sb.append((char) b2);
                } else if (b2 < 48 || b2 > 57) {
                    sb.append('2');
                } else {
                    sb.append((char) b2);
                }
            }
            if (i2 > 0) {
                sb.append(String.valueOf(i2));
            }
            B = sb.toString();
        }
        return B;
    }

    private static String a(String str) {
        return h.S() + a() + "." + str;
    }

    private static String G() {
        if (i == null) {
            i = a("ss");
        }
        return i;
    }

    static String b() {
        if (j == null) {
            j = a("ct");
        }
        return j;
    }

    static String c() {
        if (k == null) {
            k = a("st");
        }
        return k;
    }

    static String d() {
        if (l == null) {
            l = a("rt");
        }
        return l;
    }

    static String e() {
        if (r == null) {
            r = a("lgsuf");
        }
        return r;
    }

    static String f() {
        if (s == null) {
            s = a("bati");
        }
        return s;
    }

    static String g() {
        if (t == null) {
            t = a("hdr");
        }
        return t;
    }

    static String h() {
        if (m == null) {
            m = h.S() + "up";
        }
        return m;
    }

    public static String i() {
        if (n == null) {
            n = h.S() + "authu";
        }
        return n;
    }

    public static String j() {
        if (o == null) {
            o = h.S() + "statu";
        }
        return o;
    }

    static String k() {
        if (p == null) {
            p = h.S() + "poli";
        }
        return p;
    }

    static String l() {
        if (q == null) {
            q = h.S() + "ver";
        }
        return q;
    }

    public static String m() {
        return h.S() + "bvu";
    }

    static String n() {
        return g.a(new File(G()), 8);
    }

    static boolean o() {
        return v;
    }

    private static void H() {
        if (!w && !v) {
            synchronized (A) {
                if (!w) {
                    b(h.S());
                    String n2 = n();
                    File file = new File(b());
                    x = "f".equals(n2);
                    y = "b".equals(n2);
                    z = file.exists();
                    try {
                        if (w()) {
                            K();
                        }
                    } catch (Throwable th) {
                        g.a(th);
                    }
                    w = true;
                }
            }
        }
    }

    static boolean p() {
        H();
        return x;
    }

    static boolean q() {
        H();
        return y;
    }

    private static boolean I() {
        H();
        return z;
    }

    static boolean r() {
        return u;
    }

    public static void s() {
        boolean z2;
        b(h.S());
        v = true;
        x = false;
        y = false;
        z = false;
        String[] strArr = {".st", ".wa", ".callback", ".ct", ".signal"};
        String[] strArr2 = {"up", "authu", "statu", "poli"};
        File[] listFiles = new File(h.S()).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                String name = file.getName();
                int i2 = 0;
                while (true) {
                    if (i2 >= 5) {
                        z2 = false;
                        break;
                    } else if (name.endsWith(strArr[i2])) {
                        z2 = true;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (!z2) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= 4) {
                            break;
                        } else if (name.equals(strArr2[i3])) {
                            z2 = true;
                            break;
                        } else {
                            i3++;
                        }
                    }
                }
                if (z2) {
                    a.b("delete file: " + file.getPath());
                    g.a(file);
                }
            }
        }
        K();
    }

    public static void t() {
        if (!u) {
            u = true;
            if (!D()) {
                b(h.S());
                J();
                K();
            }
        }
    }

    private static void J() {
        if (d) {
            JNIBridge.nativeSyncStatus("exit", (String) null, u ? 1 : 0);
        }
    }

    private static void K() {
        if (!K) {
            K = true;
            try {
                new File(b()).delete();
            } catch (Throwable th) {
                g.a(th);
            }
        }
        Object[] M2 = M();
        if (M2[0].equals(G) || I != null) {
            H = true;
            L();
            return;
        }
        a(M2);
    }

    private static void L() {
        if (!f.b(J)) {
            f.a(1, J);
            return;
        }
        Object[] objArr = I;
        if (objArr == null || !M()[0].equals(objArr[0])) {
            f.a(J);
            f.a(1, J);
        }
    }

    private static void a(Object[] objArr) {
        I = objArr;
        synchronized (F) {
            String str = objArr[0];
            long longValue = objArr[1].longValue();
            if (longValue < L) {
                a.b("crashsdk", String.format(Locale.US, "Update state generation %d, last is: %d", new Object[]{Long.valueOf(longValue), Long.valueOf(L)}));
                return;
            }
            L = longValue;
            String G2 = G();
            if (d) {
                if (D != null) {
                    g.a((Closeable) D);
                    D = null;
                }
                boolean nativeChangeState = JNIBridge.nativeChangeState(G2, str, E);
                E = false;
                if (!nativeChangeState) {
                    a.c("write state failed: " + str);
                }
            } else {
                if (D == null || E) {
                    if (D != null) {
                        g.a((Closeable) D);
                        D = null;
                    }
                    try {
                        RandomAccessFile randomAccessFile = new RandomAccessFile(G2, "rw");
                        D = randomAccessFile;
                        randomAccessFile.seek(0);
                        E = false;
                    } catch (Exception e2) {
                        g.a((Throwable) e2);
                    }
                }
                try {
                    D.write(str.getBytes());
                    D.seek(0);
                } catch (Exception e3) {
                    g.a((Throwable) e3);
                }
            }
            G = str;
            I = null;
        }
    }

    private static Object[] M() {
        synchronized (M) {
            N++;
            if (u) {
                Object[] objArr = {"e", Long.valueOf(N)};
                return objArr;
            } else if (y()) {
                Object[] objArr2 = {"f", Long.valueOf(N)};
                return objArr2;
            } else {
                Object[] objArr3 = {"b", Long.valueOf(N)};
                return objArr3;
            }
        }
    }

    static boolean u() {
        return b(h.S());
    }

    static boolean v() {
        return b(h.T());
    }

    private static boolean b(String str) {
        File file = new File(str);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (file.isDirectory()) {
            return true;
        }
        a.a("crashsdk", "Crash log directory was placed by a file!", (Throwable) null);
        if (!file.delete()) {
            return false;
        }
        file.mkdirs();
        return true;
    }

    static boolean w() {
        return O || !O();
    }

    public static boolean x() {
        return P || !O();
    }

    public static void a(boolean z2) {
        if (z2 && u) {
            if (h.K()) {
                Log.v("crashsdk", "setForeground, reset sExited to false!!!");
            }
            u = false;
            J();
        }
        if (!u) {
            boolean z3 = e.x() || D();
            if (!z3) {
                H();
            }
            P = z2;
            if (z2) {
                O = true;
            }
            if (d) {
                JNIBridge.nativeSetForeground(z2);
            }
            if (!z3) {
                K();
                if (z2) {
                    a.a(false);
                    if (!Q) {
                        e.v();
                        Q = true;
                    }
                }
                if (!E) {
                    L();
                }
                e.c(z2);
            }
        }
    }

    static boolean y() {
        return P && !u;
    }

    public static boolean a(Context context) {
        try {
            ((Application) context).registerActivityLifecycleCallbacks(new c());
            if (!h.I()) {
                return true;
            }
            z();
            return true;
        } catch (Throwable th) {
            g.a(th);
            return false;
        }
    }

    static void z() {
        f.a(2, (Runnable) new e(100));
    }

    public static void a(int i2) {
        Object a2;
        Activity activity;
        int i3;
        boolean z2 = false;
        switch (i2) {
            case 100:
                Object N2 = N();
                if (N2 != null && (a2 = a(N2, (Class<?>) null, "mActivities")) != null) {
                    try {
                        boolean z3 = false;
                        for (Map.Entry value : ((Map) a2).entrySet()) {
                            Object value2 = value.getValue();
                            if (!(value2 == null || (activity = (Activity) a(value2, (Class<?>) null, "activity")) == null)) {
                                boolean booleanValue = ((Boolean) a(value2, (Class<?>) null, IWXAudio.KEY_PAUSED)).booleanValue();
                                boolean booleanValue2 = ((Boolean) a(value2, (Class<?>) null, "stopped")).booleanValue();
                                synchronized (R) {
                                    if (booleanValue || booleanValue2) {
                                        i3 = 2;
                                    } else {
                                        i3 = 1;
                                        z3 = true;
                                    }
                                    R.put(activity, Integer.valueOf(i3));
                                }
                            }
                            z2 = true;
                        }
                        if (z2) {
                            a(z3);
                            return;
                        }
                        return;
                    } catch (Throwable th) {
                        g.a(th);
                        return;
                    }
                } else {
                    return;
                }
            case 101:
                try {
                    boolean z4 = !new File(G()).exists();
                    E = z4;
                    if (z4 || H) {
                        a(M());
                        H = false;
                        return;
                    }
                    return;
                } catch (Throwable th2) {
                    g.a(th2);
                    return;
                }
            default:
                if (!h) {
                    throw new AssertionError();
                }
                return;
        }
    }

    private static Object N() {
        Method declaredMethod;
        Object a2;
        Object a3 = a((Object) (Application) g.a(), (Class<?>) Application.class, "mLoadedApk");
        if (a3 != null && (a2 = a(a3, (Class<?>) null, "mActivityThread")) != null) {
            return a2;
        }
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            if (!(cls == null || (declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0])) == null)) {
                declaredMethod.setAccessible(true);
                return declaredMethod.invoke((Object) null, new Object[0]);
            }
        } catch (Throwable th) {
            g.a(th);
        }
        return null;
    }

    private static Object a(Object obj, Class<?> cls, String str) {
        if (obj == null) {
            return null;
        }
        if (cls == null) {
            cls = obj.getClass();
        }
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField.get(obj);
        } catch (Throwable th) {
            g.a(th);
            return null;
        }
    }

    public static boolean A() {
        if (!T) {
            if (!g.a(a.a) && a.a.equals(e.g())) {
                U = true;
                if (d) {
                    JNIBridge.nativeSetProcessType(true);
                }
            }
            T = true;
        }
        return U;
    }

    public static int B() {
        boolean I2 = I();
        return q() ? I2 ? 3 : 6 : p() ? I2 ? 2 : 5 : I2 ? 4 : 1;
    }

    public static void b(int i2) {
        C = i2;
        C();
    }

    static void C() {
        if (d) {
            JNIBridge.nativeSyncStatus("logType", "12", C);
        }
    }

    public static boolean c(int i2) {
        return (i2 & C) != 0;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.nio.channels.FileLock} */
    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v4 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0071, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0078, code lost:
        r6 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0079, code lost:
        r1 = r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0085, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        com.uc.crashsdk.a.g.a((java.lang.Throwable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x008a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x008b, code lost:
        r1 = r5;
        r2 = r6;
        r6 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0093, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0094, code lost:
        if (r1 != 0) goto L_0x0096;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
        r1.release();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x009a, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        com.uc.crashsdk.a.g.a((java.lang.Throwable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x009e, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0071 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:44:0x006b] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:17:0x0030=Splitter:B:17:0x0030, B:63:0x008f=Splitter:B:63:0x008f} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.lang.Object r4, java.lang.String r5, com.uc.crashsdk.a.e r6) {
        /*
            monitor-enter(r4)
            boolean r0 = d     // Catch:{ all -> 0x00ad }
            r1 = 0
            r2 = 0
            if (r0 == 0) goto L_0x0041
            int r0 = com.uc.crashsdk.JNIBridge.nativeOpenFile(r5)     // Catch:{ all -> 0x00ad }
            if (r0 >= 0) goto L_0x0022
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ad }
            java.lang.String r0 = "Can not open file: "
            r6.<init>(r0)     // Catch:{ all -> 0x00ad }
            r6.append(r5)     // Catch:{ all -> 0x00ad }
            java.lang.String r5 = r6.toString()     // Catch:{ all -> 0x00ad }
            java.lang.String r6 = "crashsdk"
            com.uc.crashsdk.a.a.a(r6, r5, r1)     // Catch:{ all -> 0x00ad }
            monitor-exit(r4)     // Catch:{ all -> 0x00ad }
            return r2
        L_0x0022:
            r5 = 1
            boolean r5 = com.uc.crashsdk.JNIBridge.nativeLockFile(r0, r5)     // Catch:{ all -> 0x003c }
            boolean r6 = r6.a()     // Catch:{ all -> 0x0035 }
            if (r5 == 0) goto L_0x0030
            com.uc.crashsdk.JNIBridge.nativeLockFile(r0, r2)     // Catch:{ all -> 0x003c }
        L_0x0030:
            com.uc.crashsdk.JNIBridge.nativeCloseFile(r0)     // Catch:{ all -> 0x00ad }
            goto L_0x00a7
        L_0x0035:
            r6 = move-exception
            if (r5 == 0) goto L_0x003b
            com.uc.crashsdk.JNIBridge.nativeLockFile(r0, r2)     // Catch:{ all -> 0x003c }
        L_0x003b:
            throw r6     // Catch:{ all -> 0x003c }
        L_0x003c:
            r5 = move-exception
            com.uc.crashsdk.JNIBridge.nativeCloseFile(r0)     // Catch:{ all -> 0x00ad }
            throw r5     // Catch:{ all -> 0x00ad }
        L_0x0041:
            java.io.File r0 = new java.io.File     // Catch:{ all -> 0x00ad }
            r0.<init>(r5)     // Catch:{ all -> 0x00ad }
            boolean r5 = r0.exists()     // Catch:{ all -> 0x00ad }
            if (r5 != 0) goto L_0x0054
            r0.createNewFile()     // Catch:{ Exception -> 0x0050 }
            goto L_0x0054
        L_0x0050:
            r5 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x00ad }
        L_0x0054:
            java.io.RandomAccessFile r5 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0064 }
            java.lang.String r3 = "rw"
            r5.<init>(r0, r3)     // Catch:{ Exception -> 0x0064 }
            java.nio.channels.FileChannel r5 = r5.getChannel()     // Catch:{ Exception -> 0x0064 }
            goto L_0x0069
        L_0x0060:
            r5 = move-exception
            r6 = r5
            r5 = r1
            goto L_0x00a9
        L_0x0064:
            r5 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r5)     // Catch:{ Exception -> 0x009f }
            r5 = r1
        L_0x0069:
            if (r5 == 0) goto L_0x007b
            java.nio.channels.FileLock r0 = r5.lock()     // Catch:{ Exception -> 0x0073, all -> 0x0071 }
            r1 = r0
            goto L_0x007b
        L_0x0071:
            r6 = move-exception
            goto L_0x00a9
        L_0x0073:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ Exception -> 0x0078, all -> 0x0071 }
            goto L_0x007b
        L_0x0078:
            r6 = move-exception
            r1 = r5
            goto L_0x00a0
        L_0x007b:
            boolean r6 = r6.a()     // Catch:{ all -> 0x0093 }
            if (r1 == 0) goto L_0x008f
            r1.release()     // Catch:{ Exception -> 0x0085, all -> 0x0071 }
            goto L_0x008f
        L_0x0085:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ Exception -> 0x008a, all -> 0x0071 }
            goto L_0x008f
        L_0x008a:
            r0 = move-exception
            r1 = r5
            r2 = r6
            r6 = r0
            goto L_0x00a0
        L_0x008f:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r5)     // Catch:{ all -> 0x00ad }
            goto L_0x00a7
        L_0x0093:
            r6 = move-exception
            if (r1 == 0) goto L_0x009e
            r1.release()     // Catch:{ Exception -> 0x009a, all -> 0x0071 }
            goto L_0x009e
        L_0x009a:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ Exception -> 0x0078, all -> 0x0071 }
        L_0x009e:
            throw r6     // Catch:{ Exception -> 0x0078, all -> 0x0071 }
        L_0x009f:
            r6 = move-exception
        L_0x00a0:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r6)     // Catch:{ all -> 0x0060 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)     // Catch:{ all -> 0x00ad }
            r6 = r2
        L_0x00a7:
            monitor-exit(r4)     // Catch:{ all -> 0x00ad }
            return r6
        L_0x00a9:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r5)     // Catch:{ all -> 0x00ad }
            throw r6     // Catch:{ all -> 0x00ad }
        L_0x00ad:
            r5 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x00ad }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.b.a(java.lang.Object, java.lang.String, com.uc.crashsdk.a.e):boolean");
    }

    private static boolean O() {
        String a2 = g.a(new File("/proc/self/cgroup"), 512);
        if (g.a(a2)) {
            return false;
        }
        if (a2.contains("/bg_non_interactive") || a2.contains("/background")) {
            return true;
        }
        return false;
    }

    public static boolean D() {
        if (!W) {
            synchronized (X) {
                if (!W) {
                    V = P();
                    W = true;
                }
            }
        }
        return V;
    }

    private static boolean P() {
        try {
            Method declaredMethod = Process.class.getDeclaredMethod("isIsolated", new Class[0]);
            if (declaredMethod != null) {
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke((Object) null, new Object[0]);
                if (invoke != null && (invoke instanceof Boolean)) {
                    return ((Boolean) invoke).booleanValue();
                }
            }
        } catch (Throwable th) {
            g.a(th);
        }
        int myUid = Process.myUid() % OnLineMonitor.TASK_TYPE_FROM_BOOT;
        return myUid >= 99000 && myUid <= 99999;
    }
}
