package com.uc.crashsdk;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Debug;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.StatFs;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.uc.crashsdk.a.f;
import com.uc.crashsdk.a.g;
import com.uc.crashsdk.a.h;
import com.uc.crashsdk.export.LogType;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.Thread;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.mtop.intf.MtopParamType;

/* compiled from: ProGuard */
public class e implements Thread.UncaughtExceptionHandler {
    private static Map<String, Integer> A = null;
    private static String B = null;
    /* access modifiers changed from: private */
    public static int C = -1;
    /* access modifiers changed from: private */
    public static int D = -1;
    /* access modifiers changed from: private */
    public static int E = -1;
    /* access modifiers changed from: private */
    public static int F = -1;
    /* access modifiers changed from: private */
    public static int G = -1;
    /* access modifiers changed from: private */
    public static int H = -1;
    /* access modifiers changed from: private */
    public static int I = -1;
    /* access modifiers changed from: private */
    public static String J = "?";
    /* access modifiers changed from: private */
    public static boolean K = false;
    private static boolean L = false;
    /* access modifiers changed from: private */
    public static int M = 0;
    private static com.uc.crashsdk.a.e N = new com.uc.crashsdk.a.e(405);
    private static a O = new a((byte) 0);
    private static boolean P = false;
    private static final com.uc.crashsdk.a.e Q = new com.uc.crashsdk.a.e(412);
    private static Thread.UncaughtExceptionHandler R = null;
    private static Throwable S = null;
    private static boolean T = false;
    private static boolean U = false;
    private static Runnable V = null;
    private static final Object W = new Object();
    private static Runnable X = new com.uc.crashsdk.a.e(407);
    private static final Object Y = new Object();
    private static boolean Z = false;
    static final /* synthetic */ boolean a = (!e.class.desiredAssertionStatus());
    private static boolean aa = false;
    private static ParcelFileDescriptor ab = null;
    private static boolean ac = false;
    private static boolean ad = false;
    private static long b;
    private static volatile boolean c = false;
    private static boolean d = false;
    private static long f = 0;
    /* access modifiers changed from: private */
    public static boolean g = true;
    private static String h;
    private static boolean i = false;
    private static String j = null;
    private static String k = null;
    private static String l = null;
    private static String m = null;
    private static final Object n = new Object();
    private static final Object o = new Object();
    private static final Object p = new Object();
    private static final Object q = new Object();
    private static final ArrayList<String> r = new ArrayList<>();
    private static int s = 0;
    private static String t = null;
    private static boolean u = false;
    private static String v = null;
    private static String w = null;
    private static String x = null;
    private static final Object y = new Object();
    private static final Object z = new Object();
    private final List<FileInputStream> e = new ArrayList();

    public static String d(boolean z2) {
        return z2 ? "https://up4-intl.ucweb.com/upload" : "https://up4.ucweb.com/upload";
    }

    static /* synthetic */ int A() {
        int i2 = M + 1;
        M = i2;
        return i2;
    }

    static /* synthetic */ void B() {
        StringBuilder K2;
        if (b.d && (K2 = K()) != null) {
            JNIBridge.nativeSyncStatus("bati", K2.toString(), 0);
        }
        L = true;
        L();
    }

    static /* synthetic */ boolean z() {
        if (h.L()) {
            return true;
        }
        return a();
    }

    public e() {
        try {
            D();
        } catch (Throwable th) {
            g.a(th);
        }
    }

    private void D() {
        int F2 = h.F();
        int i2 = 0;
        while (i2 < F2) {
            try {
                this.e.add(new FileInputStream("/dev/null"));
                i2++;
            } catch (Exception e2) {
                g.a((Throwable) e2);
                return;
            }
        }
    }

    public static boolean a() {
        if (f == 0) {
            f = 2;
            try {
                Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("getLong", new Class[]{String.class, Long.TYPE});
                if (declaredMethod != null) {
                    declaredMethod.setAccessible(true);
                    if (((Long) declaredMethod.invoke((Object) null, new Object[]{"debug.crs.logs", 0})).longValue() == 1) {
                        f = 1;
                    }
                }
            } catch (Throwable th) {
                g.a(th);
            }
        }
        return f == 1;
    }

    /* compiled from: ProGuard */
    private static class b extends OutputStream {
        private final long a;
        private final OutputStream b;
        private int c = 0;
        private int d = 0;
        private boolean e = false;

        b(long j, OutputStream outputStream) {
            this.a = j;
            this.b = outputStream;
        }

        private int a(byte[] bArr, int i, int i2) {
            this.d += i2;
            if (this.e) {
                return 0;
            }
            int y = h.y();
            int i3 = (y <= 0 || this.c + i2 <= y) ? i2 : y - this.c;
            this.c += i3;
            if (this.a != 0) {
                b(new String(bArr, i, i3));
            } else {
                this.b.write(bArr, i, i3);
            }
            if (i3 < i2) {
                this.e = true;
            }
            return i3;
        }

        /* access modifiers changed from: package-private */
        public final void a() {
            try {
                if (this.d - this.c > 0) {
                    a("\n");
                    a("--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n");
                }
                int y = h.y();
                a(String.format(Locale.US, "Full: %d bytes, write: %d bytes, limit: %d bytes, reject: %d bytes.\n", new Object[]{Integer.valueOf(this.d), Integer.valueOf(this.c), Integer.valueOf(y), Integer.valueOf(this.d - this.c)}));
            } catch (Throwable th) {
                g.a(th);
            }
        }

        /* access modifiers changed from: package-private */
        public final void a(String str) {
            if (e.g && e.z()) {
                com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, str);
            }
            if (this.a != 0) {
                b(str);
            } else {
                this.b.write(str.getBytes("UTF-8"));
            }
        }

        public final void write(int i) {
            if (e.g && e.z()) {
                com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, String.format(Locale.US, "%c", new Object[]{Integer.valueOf(i)}));
            }
            if (this.a != 0) {
                b(String.format(Locale.US, "%c", new Object[]{Integer.valueOf(i)}));
            } else {
                this.b.write(i);
            }
            this.c++;
            this.d++;
        }

        public final void write(byte[] bArr, int i, int i2) {
            if (e.g && e.z()) {
                byte[] bArr2 = new byte[i2];
                System.arraycopy(bArr, i, bArr2, 0, i2);
                if (!(i2 == 1 && bArr2[0] == 10)) {
                    try {
                        com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, new String(bArr2));
                    } catch (Throwable unused) {
                    }
                }
            }
            a(bArr, i, i2);
        }

        public final void write(byte[] bArr) {
            if (e.g && e.z() && !(bArr.length == 1 && bArr[0] == 10)) {
                try {
                    com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, new String(bArr));
                } catch (Throwable unused) {
                }
            }
            a(bArr, 0, bArr.length);
        }

        private void b(String str) {
            if (b.d) {
                JNIBridge.nativeClientWriteData(this.a, str);
            }
        }
    }

    private static String i(String str) {
        try {
            return str.replaceAll("[^0-9a-zA-Z-.]", "-");
        } catch (Throwable unused) {
            return "unknown";
        }
    }

    private static String E() {
        return h.f() + "_";
    }

    static void b() {
        h = null;
    }

    static String c() {
        if (h != null) {
            return h;
        }
        String j2 = j((String) null);
        h = j2;
        return j2;
    }

    private static String j(String str) {
        if (str == null) {
            str = String.valueOf(System.currentTimeMillis());
        }
        return String.format(Locale.US, "%s%s_%s_%s_%s_%s_", new Object[]{E(), h.P(), h.R(), i(Build.MODEL), i(Build.VERSION.RELEASE), str});
    }

    private static String F() {
        return b.y() ? "fg" : "bg";
    }

    private static byte[] G() {
        byte[] bArr = null;
        int i2 = 1024;
        while (bArr == null && i2 > 0) {
            try {
                bArr = new byte[i2];
            } catch (Throwable unused) {
                i2 /= 2;
                if (i2 < 16) {
                    break;
                }
            }
        }
        return bArr;
    }

    private static String k(String str) {
        return String.format(Locale.US, "%s%s_%s_%s%s.log", new Object[]{c(), k(), F(), str, a(h.M())});
    }

    static String a(String str) {
        if (g.a(str)) {
            return "";
        }
        if (str.length() > 128) {
            str = str.substring(0, 128);
        }
        String i2 = i(str);
        if (i2.startsWith(".")) {
            return i2;
        }
        return "." + i2;
    }

    static void b(String str) {
        f.a(0, (Runnable) new com.uc.crashsdk.a.e(404, new Object[]{str}));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0142, code lost:
        if (com.uc.crashsdk.a.d.d() != false) goto L_0x014e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x0144, code lost:
        com.uc.crashsdk.a.a.b(com.alibaba.android.umbrella.link.export.UMLLCons.FEATURE_TYPE_DEBUG, com.uc.crashsdk.a.d.b());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x014d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0154, code lost:
        if (d(com.uc.crashsdk.export.LogType.UNEXP_TYPE) != false) goto L_0x015e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0156, code lost:
        com.uc.crashsdk.a.a.c(com.alibaba.android.umbrella.link.export.UMLLCons.FEATURE_TYPE_DEBUG, "unexp sample miss");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x015d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x015e, code lost:
        r8 = com.uc.crashsdk.JNIBridge.nativeGenerateUnexpLog((long) com.uc.crashsdk.h.p(), com.uc.crashsdk.h.q());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x016b, code lost:
        if (r8 == 0) goto L_0x019b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:109:0x016d, code lost:
        com.uc.crashsdk.f.a(11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x0174, code lost:
        if ((r8 & 1280) == 0) goto L_0x017c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0176, code lost:
        r8 = 10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0178, code lost:
        com.uc.crashsdk.f.a(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x017e, code lost:
        if ((r8 & 2304) == 0) goto L_0x0183;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x0180, code lost:
        r8 = 29;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0185, code lost:
        if ((r8 & com.uc.crashsdk.export.LogType.UNEXP_KILL_PROCESS) == 0) goto L_0x018a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:0x0187, code lost:
        r8 = 30;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x018c, code lost:
        if ((r8 & com.uc.crashsdk.export.LogType.UNEXP_EXIT) == 0) goto L_0x0191;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:121:0x018e, code lost:
        r8 = 31;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:123:0x0193, code lost:
        if ((r8 & com.uc.crashsdk.export.LogType.UNEXP_RESTART) == 0) goto L_0x0198;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x0195, code lost:
        r8 = 32;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:125:0x0198, code lost:
        a(true);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x019b, code lost:
        r8 = W;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:127:0x019d, code lost:
        monitor-enter(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:?, code lost:
        V = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:130:0x01a0, code lost:
        monitor-exit(r8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:131:0x01a1, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:135:0x01a5, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:156:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:157:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0073, code lost:
        com.uc.crashsdk.b.p();
        com.uc.crashsdk.a.h.b();
        com.uc.crashsdk.f.d();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0080, code lost:
        if (com.uc.crashsdk.b.A() == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0082, code lost:
        com.uc.crashsdk.a.f.a(1, new com.uc.crashsdk.a.e(409), 7000);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x013c, code lost:
        if (com.uc.crashsdk.b.o() != false) goto L_0x01a5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(int r8, java.lang.Object[] r9) {
        /*
            r0 = 0
            r1 = 0
            r2 = 1
            switch(r8) {
                case 401: goto L_0x01a9;
                case 402: goto L_0x012c;
                case 403: goto L_0x0128;
                case 404: goto L_0x00dc;
                case 405: goto L_0x00c3;
                case 406: goto L_0x009d;
                case 407: goto L_0x0094;
                case 408: goto L_0x005c;
                case 409: goto L_0x0058;
                case 410: goto L_0x0054;
                case 411: goto L_0x0044;
                case 412: goto L_0x000b;
                default: goto L_0x0006;
            }
        L_0x0006:
            boolean r8 = a
            if (r8 == 0) goto L_0x01c4
            return
        L_0x000b:
            boolean r8 = P
            if (r8 != 0) goto L_0x0023
            boolean r8 = com.uc.crashsdk.b.y()
            if (r8 == 0) goto L_0x0023
            boolean r8 = com.uc.crashsdk.h.J()
            if (r8 == 0) goto L_0x0023
            android.content.Context r8 = com.uc.crashsdk.a.g.a()
            a((android.content.Context) r8)
            return
        L_0x0023:
            boolean r8 = P
            if (r8 == 0) goto L_0x0043
            boolean r8 = com.uc.crashsdk.b.y()
            if (r8 == 0) goto L_0x0033
            boolean r8 = com.uc.crashsdk.h.J()
            if (r8 != 0) goto L_0x0043
        L_0x0033:
            android.content.Context r8 = com.uc.crashsdk.a.g.a()
            com.uc.crashsdk.e$a r9 = O     // Catch:{ Throwable -> 0x003f }
            r8.unregisterReceiver(r9)     // Catch:{ Throwable -> 0x003f }
            P = r1     // Catch:{ Throwable -> 0x003f }
            return
        L_0x003f:
            r8 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r8)
        L_0x0043:
            return
        L_0x0044:
            boolean r8 = com.uc.crashsdk.b.d
            if (r8 == 0) goto L_0x0053
            java.lang.String r8 = "jni"
            boolean r8 = d((java.lang.String) r8)
            java.lang.String r9 = "jnisampl"
            com.uc.crashsdk.JNIBridge.nativeSyncStatus(r9, r0, r8)
        L_0x0053:
            return
        L_0x0054:
            a((boolean) r1, (boolean) r2)
            return
        L_0x0058:
            b((boolean) r1, (boolean) r1)
            return
        L_0x005c:
            java.lang.Object r8 = Y
            monitor-enter(r8)
            boolean r9 = Z     // Catch:{ all -> 0x0091 }
            if (r9 != 0) goto L_0x008f
            boolean r9 = com.uc.crashsdk.h.N()     // Catch:{ all -> 0x0091 }
            if (r9 == 0) goto L_0x008f
            boolean r9 = com.uc.crashsdk.b.w()     // Catch:{ all -> 0x0091 }
            if (r9 != 0) goto L_0x0070
            goto L_0x008f
        L_0x0070:
            Z = r2     // Catch:{ all -> 0x0091 }
            monitor-exit(r8)     // Catch:{ all -> 0x0091 }
            com.uc.crashsdk.b.p()
            com.uc.crashsdk.a.h.b()
            com.uc.crashsdk.f.d()
            boolean r8 = com.uc.crashsdk.b.A()
            if (r8 == 0) goto L_0x008e
            com.uc.crashsdk.a.e r8 = new com.uc.crashsdk.a.e
            r9 = 409(0x199, float:5.73E-43)
            r8.<init>(r9)
            r0 = 7000(0x1b58, double:3.4585E-320)
            com.uc.crashsdk.a.f.a(r2, r8, r0)
        L_0x008e:
            return
        L_0x008f:
            monitor-exit(r8)     // Catch:{ all -> 0x0091 }
            return
        L_0x0091:
            r9 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0091 }
            throw r9
        L_0x0094:
            com.uc.crashsdk.a.c()     // Catch:{ Throwable -> 0x0098 }
            return
        L_0x0098:
            r8 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r8)
            return
        L_0x009d:
            boolean r8 = a
            if (r8 != 0) goto L_0x00aa
            if (r9 == 0) goto L_0x00a4
            goto L_0x00aa
        L_0x00a4:
            java.lang.AssertionError r8 = new java.lang.AssertionError
            r8.<init>()
            throw r8
        L_0x00aa:
            r8 = r9[r1]
            java.lang.String r8 = (java.lang.String) r8
            r0 = r9[r2]
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            r1 = 2
            r9 = r9[r1]
            java.lang.Boolean r9 = (java.lang.Boolean) r9
            boolean r9 = r9.booleanValue()
            b((java.lang.String) r8, (boolean) r0, (boolean) r9)
            return
        L_0x00c3:
            L = r1
            java.lang.StringBuilder r8 = K()
            java.lang.String r9 = com.uc.crashsdk.b.f()
            if (r8 == 0) goto L_0x00db
            java.io.File r0 = new java.io.File
            r0.<init>(r9)
            java.lang.String r8 = r8.toString()
            com.uc.crashsdk.a.g.a((java.io.File) r0, (java.lang.String) r8)
        L_0x00db:
            return
        L_0x00dc:
            boolean r8 = a
            if (r8 != 0) goto L_0x00e9
            if (r9 == 0) goto L_0x00e3
            goto L_0x00e9
        L_0x00e3:
            java.lang.AssertionError r8 = new java.lang.AssertionError
            r8.<init>()
            throw r8
        L_0x00e9:
            r8 = r9[r1]
            java.lang.String r8 = (java.lang.String) r8
            java.lang.String r9 = com.uc.crashsdk.b.e()
            java.io.File r1 = new java.io.File
            r1.<init>(r9)
            boolean r9 = i
            if (r9 != 0) goto L_0x0116
            i = r2
            boolean r9 = r1.exists()
            if (r9 == 0) goto L_0x010c
            r9 = 128(0x80, float:1.794E-43)
            java.lang.String r9 = com.uc.crashsdk.a.g.a((java.io.File) r1, (int) r9)
            java.lang.String r0 = a((java.lang.String) r9)
        L_0x010c:
            r3 = r0
            java.lang.String r2 = "mLogTypeSuffix"
            r4 = 1
            r6 = 0
            com.uc.crashsdk.JNIBridge.nativeSyncInfo(r2, r3, r4, r6)
        L_0x0116:
            boolean r9 = com.uc.crashsdk.a.g.a((java.lang.String) r8)
            if (r9 == 0) goto L_0x0120
            com.uc.crashsdk.a.g.a((java.io.File) r1)
            return
        L_0x0120:
            byte[] r8 = r8.getBytes()
            com.uc.crashsdk.a.g.a((java.io.File) r1, (byte[]) r8)
            return
        L_0x0128:
            M()
            return
        L_0x012c:
            java.lang.Object r8 = W
            monitor-enter(r8)
            java.lang.Runnable r9 = V     // Catch:{ all -> 0x01a6 }
            if (r9 != 0) goto L_0x0135
            monitor-exit(r8)     // Catch:{ all -> 0x01a6 }
            return
        L_0x0135:
            U = r2     // Catch:{ all -> 0x01a6 }
            monitor-exit(r8)     // Catch:{ all -> 0x01a6 }
            boolean r8 = com.uc.crashsdk.b.o()
            if (r8 != 0) goto L_0x01a5
            boolean r8 = com.uc.crashsdk.a.d.d()
            if (r8 != 0) goto L_0x014e
            java.lang.String r8 = "DEBUG"
            java.lang.String r9 = com.uc.crashsdk.a.d.b()
            com.uc.crashsdk.a.a.b(r8, r9)
            return
        L_0x014e:
            java.lang.String r8 = "unexp"
            boolean r8 = d((java.lang.String) r8)
            if (r8 != 0) goto L_0x015e
            java.lang.String r8 = "DEBUG"
            java.lang.String r9 = "unexp sample miss"
            com.uc.crashsdk.a.a.c(r8, r9)
            return
        L_0x015e:
            int r8 = com.uc.crashsdk.h.p()
            long r8 = (long) r8
            int r1 = com.uc.crashsdk.h.q()
            int r8 = com.uc.crashsdk.JNIBridge.nativeGenerateUnexpLog(r8, r1)
            if (r8 == 0) goto L_0x019b
            r9 = 11
            com.uc.crashsdk.f.a((int) r9)
            r9 = r8 & 1280(0x500, float:1.794E-42)
            if (r9 == 0) goto L_0x017c
            r8 = 10
        L_0x0178:
            com.uc.crashsdk.f.a((int) r8)
            goto L_0x0198
        L_0x017c:
            r9 = r8 & 2304(0x900, float:3.229E-42)
            if (r9 == 0) goto L_0x0183
            r8 = 29
            goto L_0x0178
        L_0x0183:
            r9 = r8 & 4352(0x1100, float:6.098E-42)
            if (r9 == 0) goto L_0x018a
            r8 = 30
            goto L_0x0178
        L_0x018a:
            r9 = r8 & 8448(0x2100, float:1.1838E-41)
            if (r9 == 0) goto L_0x0191
            r8 = 31
            goto L_0x0178
        L_0x0191:
            r8 = r8 & 16640(0x4100, float:2.3318E-41)
            if (r8 == 0) goto L_0x0198
            r8 = 32
            goto L_0x0178
        L_0x0198:
            a((boolean) r2)
        L_0x019b:
            java.lang.Object r8 = W
            monitor-enter(r8)
            V = r0     // Catch:{ all -> 0x01a2 }
            monitor-exit(r8)     // Catch:{ all -> 0x01a2 }
            return
        L_0x01a2:
            r9 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x01a2 }
            throw r9
        L_0x01a5:
            return
        L_0x01a6:
            r9 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x01a6 }
            throw r9
        L_0x01a9:
            int r8 = com.uc.crashsdk.b.B()
            r9 = 5
            if (r8 != r9) goto L_0x01b2
            r8 = 1
            goto L_0x01b3
        L_0x01b2:
            r8 = 0
        L_0x01b3:
            com.uc.crashsdk.JNIBridge.nativePrepareUnexpInfos(r8)
            com.uc.crashsdk.a.c = r2
            com.uc.crashsdk.a.a((boolean) r1)
            L = r2
            L()
            s()
            return
        L_0x01c4:
            java.lang.AssertionError r8 = new java.lang.AssertionError
            r8.<init>()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.a(int, java.lang.Object[]):void");
    }

    public static boolean b(int i2, Object[] objArr) {
        switch (i2) {
            case 451:
                if (a || objArr != null) {
                    return a(objArr[0], objArr[1]);
                }
                throw new AssertionError();
            case 452:
                if (a || objArr != null) {
                    d dVar = objArr[1];
                    return g.a(new File(objArr[0]), String.format(Locale.US, "%d %d %d %d", new Object[]{Long.valueOf(dVar.a), Long.valueOf(dVar.b), Integer.valueOf(dVar.c), Integer.valueOf(dVar.d)}).getBytes());
                }
                throw new AssertionError();
            default:
                if (a) {
                    return false;
                }
                throw new AssertionError();
        }
    }

    private static String H() {
        return (!b.A() || d) ? "java" : "ucebujava";
    }

    static void a(boolean z2) {
        File[] listFiles;
        try {
            if (b.v() && (listFiles = new File(h.T()).listFiles()) != null) {
                int m2 = h.m();
                int n2 = h.n();
                if (listFiles.length >= Math.min(m2, n2)) {
                    int i2 = 0;
                    int i3 = 0;
                    for (File b2 : listFiles) {
                        if (b(b2)) {
                            i2++;
                        } else {
                            i3++;
                        }
                    }
                    int i4 = (!z2 || i2 < m2) ? 0 : (i2 - m2) + 1;
                    int i5 = (z2 || i3 < n2) ? 0 : (i3 - n2) + 1;
                    if (i4 != 0 || i5 != 0) {
                        Arrays.sort(listFiles, new c((byte) 0));
                        int i6 = i5;
                        int i7 = i4;
                        for (File file : listFiles) {
                            boolean b3 = b(file);
                            if (b3 && i7 > 0) {
                                com.uc.crashsdk.a.a.b("Delete oldest crash log: " + file.getPath());
                                file.delete();
                                i7 += -1;
                            } else if (!b3 && i6 > 0) {
                                com.uc.crashsdk.a.a.b("Delete oldest custom log: " + file.getPath());
                                file.delete();
                                i6 += -1;
                            }
                            if (i7 == 0 && i6 == 0) {
                                break;
                            }
                        }
                        f.a(16, i4 + i5);
                        if (i4 > 0) {
                            f.a(22, i4);
                        }
                        if (i5 > 0) {
                            f.a(23, i5);
                        }
                    }
                }
            }
        } catch (Throwable th) {
            g.a(th);
        }
    }

    /* compiled from: ProGuard */
    private static class c implements Comparator<File> {
        private c() {
        }

        /* synthetic */ c(byte b) {
            this();
        }

        public final /* synthetic */ int compare(Object obj, Object obj2) {
            File file = (File) obj;
            File file2 = (File) obj2;
            if (file.lastModified() > file2.lastModified()) {
                return 1;
            }
            return file.lastModified() < file2.lastModified() ? -1 : 0;
        }
    }

    private static void b(OutputStream outputStream, String str, String str2) {
        String str3;
        try {
            outputStream.write("*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***\n".getBytes("UTF-8"));
        } catch (Throwable th) {
            a(th, outputStream);
        }
        try {
            outputStream.write(String.format(Locale.US, "Basic Information: 'pid: %d/tid: %d/time: %s'\n", new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(Process.myTid()), k()}).getBytes("UTF-8"));
            Locale locale = Locale.US;
            Object[] objArr = new Object[3];
            objArr[0] = d();
            if (g.a(l)) {
                I();
            }
            objArr[1] = l;
            objArr[2] = e();
            outputStream.write(String.format(locale, "Cpu Information: 'abi: %s/processor: %s/hardware: %s'\n", objArr).getBytes("UTF-8"));
        } catch (Throwable th2) {
            a(th2, outputStream);
        }
        try {
            outputStream.write(String.format(Locale.US, "Mobile Information: 'model: %s/version: %s/sdk: %d'\n", new Object[]{Build.MODEL, Build.VERSION.RELEASE, Integer.valueOf(Build.VERSION.SDK_INT)}).getBytes("UTF-8"));
            outputStream.write(("Build fingerprint: '" + Build.FINGERPRINT + "'\n").getBytes("UTF-8"));
            outputStream.write(String.format(Locale.US, "Runtime Information: 'start: %s/maxheap: %s'\n", new Object[]{a(new Date(b)), Long.valueOf(Runtime.getRuntime().maxMemory())}).getBytes("UTF-8"));
        } catch (Throwable th3) {
            a(th3, outputStream);
        }
        try {
            outputStream.write(String.format(Locale.US, "Application Information: 'version: %s/subversion: %s/buildseq: %s'\n", new Object[]{h.P(), h.Q(), h.R()}).getBytes("UTF-8"));
            String str4 = "0";
            if (b.d) {
                str4 = JNIBridge.nativeGetNativeBuildseq();
            }
            outputStream.write(String.format(Locale.US, "CrashSDK Information: 'version: %s/nativeseq: %s/javaseq: %s/target: %s'\n", new Object[]{"2.3.1.0", str4, "190401175529", "release"}).getBytes("UTF-8"));
            if (str == null) {
                str = "";
            }
            outputStream.write(("Report Name: " + str.substring(str.lastIndexOf(47) + 1) + "\n").getBytes("UTF-8"));
        } catch (Throwable th4) {
            a(th4, outputStream);
        }
        try {
            if (ad) {
                str3 = s("UUID");
            } else {
                str3 = B;
            }
            outputStream.write(String.format("UUID: %s\n", new Object[]{str3}).getBytes("UTF-8"));
            outputStream.write(("Log Type: " + str2 + "\n").getBytes("UTF-8"));
        } catch (Throwable th5) {
            a(th5, outputStream);
        }
        a(outputStream);
        try {
            a.a(outputStream, "UTF-8");
            if (ad) {
                g = false;
                outputStream.write(s(MtopParamType.HEADER).getBytes("UTF-8"));
                g = true;
            }
        } catch (Throwable th6) {
            a(th6, outputStream);
        }
        a(outputStream);
    }

    public static String d() {
        if (g.a(j)) {
            if (Build.VERSION.SDK_INT >= 21) {
                try {
                    Field declaredField = Build.class.getDeclaredField("SUPPORTED_ABIS");
                    declaredField.setAccessible(true);
                    Object obj = declaredField.get((Object) null);
                    if (obj != null && (obj instanceof String[])) {
                        String[] strArr = (String[]) obj;
                        StringBuilder sb = new StringBuilder();
                        int length = strArr.length;
                        int i2 = 0;
                        boolean z2 = true;
                        while (i2 < length) {
                            String str = strArr[i2];
                            if (!z2) {
                                sb.append(",");
                            }
                            sb.append(str);
                            i2++;
                            z2 = false;
                        }
                        j = sb.toString();
                    }
                } catch (Throwable unused) {
                }
            }
            if (g.a(j)) {
                try {
                    j = Build.CPU_ABI;
                } catch (Throwable unused2) {
                }
            }
        }
        return j;
    }

    public static String e() {
        if (g.a(k)) {
            I();
        }
        return k;
    }

    private static void I() {
        BufferedReader bufferedReader;
        FileReader fileReader;
        Throwable th;
        String str = "-";
        String str2 = "-";
        try {
            str = Build.HARDWARE;
        } catch (Throwable th2) {
            g.a(th2);
        }
        try {
            fileReader = new FileReader(new File("/proc/cpuinfo"));
            try {
                bufferedReader = new BufferedReader(fileReader, 512);
                int i2 = 0;
                do {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        } else if (readLine.startsWith("Hardware")) {
                            i2++;
                            str = readLine.substring(readLine.indexOf(":") + 1).trim();
                        } else if (readLine.startsWith("Processor")) {
                            i2++;
                            str2 = readLine.substring(readLine.indexOf(":") + 1).trim();
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        try {
                            g.a(th);
                            g.a((Closeable) fileReader);
                            g.a((Closeable) bufferedReader);
                            k = str;
                            l = str2;
                        } catch (Throwable th4) {
                            th = th4;
                            g.a((Closeable) fileReader);
                            g.a((Closeable) bufferedReader);
                            throw th;
                        }
                    }
                } while (i2 < 2);
                g.a((Closeable) fileReader);
            } catch (Throwable th5) {
                th = th5;
                bufferedReader = null;
                g.a((Closeable) fileReader);
                g.a((Closeable) bufferedReader);
                throw th;
            }
        } catch (Throwable th6) {
            th = th6;
            fileReader = null;
            bufferedReader = null;
            g.a((Closeable) fileReader);
            g.a((Closeable) bufferedReader);
            throw th;
        }
        g.a((Closeable) bufferedReader);
        k = str;
        l = str2;
    }

    static String f() {
        StringBuilder sb;
        try {
            sb = new StringBuilder();
            sb.append("JavaMax:    ");
            sb.append(Runtime.getRuntime().maxMemory() / 1024);
            sb.append(" kB\n");
            sb.append("JavaTotal:  ");
            sb.append(Runtime.getRuntime().totalMemory() / 1024);
            sb.append(" kB\n");
            sb.append("JavaFree:   ");
            sb.append(Runtime.getRuntime().freeMemory() / 1024);
            sb.append(" kB\n");
            sb.append("NativeHeap: ");
            sb.append(Debug.getNativeHeapSize() / 1024);
            sb.append(" kB\n");
            sb.append("NativeAllocated: ");
            sb.append(Debug.getNativeHeapAllocatedSize() / 1024);
            sb.append(" kB\n");
            sb.append("NativeFree: ");
            sb.append(Debug.getNativeHeapFreeSize() / 1024);
            sb.append(" kB\n");
            ActivityManager activityManager = (ActivityManager) g.a().getSystemService("activity");
            if (activityManager != null) {
                ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                activityManager.getMemoryInfo(memoryInfo);
                sb.append("\n");
                sb.append("availMem:   ");
                sb.append(memoryInfo.availMem / 1024);
                sb.append(" kB\n");
                sb.append("threshold:  ");
                sb.append(memoryInfo.threshold / 1024);
                sb.append(" kB\n");
                sb.append("lowMemory:  ");
                sb.append(memoryInfo.lowMemory);
                sb.append("\n");
            }
        } catch (Throwable th) {
            g.a(th);
            return "exception exists.";
        }
        return sb.toString();
    }

    static String a(String str, String str2) {
        boolean z2;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(Build.VERSION.SDK_INT >= 26 ? new String[]{"ps", "-ef"} : new String[]{"ps"}).getInputStream()));
            boolean b2 = g.b(str);
            boolean b3 = g.b(str2);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return byteArrayOutputStream.toString("UTF-8");
                }
                if ((!b2 || !readLine.contains(str)) && ((!b3 || !readLine.contains(str2)) && (readLine.indexOf(47) >= 0 || readLine.indexOf(46) <= 0))) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (z2) {
                    byteArrayOutputStream.write(readLine.getBytes("UTF-8"));
                    byteArrayOutputStream.write("\n".getBytes("UTF-8"));
                }
            }
        } catch (Throwable th) {
            g.a(th);
            return "exception exists.";
        }
    }

    private static BufferedReader a(InputStreamReader inputStreamReader) {
        BufferedReader bufferedReader = null;
        int i2 = 8192;
        while (bufferedReader == null && i2 > 0) {
            try {
                bufferedReader = new BufferedReader(inputStreamReader, i2);
            } catch (Throwable unused) {
                i2 /= 2;
                if (i2 < 512) {
                    break;
                }
            }
        }
        return bufferedReader;
    }

    private static void a(OutputStream outputStream) {
        try {
            outputStream.write("--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n".getBytes("UTF-8"));
        } catch (Throwable th) {
            a(th, outputStream);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x008a, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x008b, code lost:
        r0 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x008e, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        a(r0, r9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00ee, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
        a(r0, r9);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:17:0x0082, B:39:0x00ce] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x008a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:17:0x0082] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void b(java.io.OutputStream r9) {
        /*
            r0 = 0
            r1 = 1
            java.lang.String r2 = "logcat: \n"
            java.lang.String r3 = "UTF-8"
            byte[] r2 = r2.getBytes(r3)     // Catch:{ Throwable -> 0x00fd }
            r9.write(r2)     // Catch:{ Throwable -> 0x00fd }
            int r2 = com.uc.crashsdk.h.o()     // Catch:{ Throwable -> 0x00fd }
            if (r2 > 0) goto L_0x002a
            java.lang.String r2 = "[DEBUG] custom java logcat lines count is 0!\n"
            java.lang.String r3 = "UTF-8"
            byte[] r2 = r2.getBytes(r3)     // Catch:{ Throwable -> 0x001f }
            r9.write(r2)     // Catch:{ Throwable -> 0x001f }
            goto L_0x0023
        L_0x001f:
            r2 = move-exception
            a((java.lang.Throwable) r2, (java.io.OutputStream) r9)     // Catch:{ Throwable -> 0x00fd }
        L_0x0023:
            a((java.io.OutputStream) r9)     // Catch:{ Throwable -> 0x00fd }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            return
        L_0x002a:
            int r2 = com.uc.crashsdk.h.o()     // Catch:{ Throwable -> 0x00fd }
            java.lang.Runtime r3 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x00fd }
            r4 = 10
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch:{ Throwable -> 0x00fd }
            java.lang.String r5 = "logcat"
            r6 = 0
            r4[r6] = r5     // Catch:{ Throwable -> 0x00fd }
            java.lang.String r5 = "-d"
            r4[r1] = r5     // Catch:{ Throwable -> 0x00fd }
            java.lang.String r5 = "-b"
            r7 = 2
            r4[r7] = r5     // Catch:{ Throwable -> 0x00fd }
            r5 = 3
            java.lang.String r8 = "events"
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            r5 = 4
            java.lang.String r8 = "-b"
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            r5 = 5
            java.lang.String r8 = "main"
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            r5 = 6
            java.lang.String r8 = "-v"
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            r5 = 7
            java.lang.String r8 = "threadtime"
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            r5 = 8
            java.lang.String r8 = "-t"
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            r5 = 9
            java.lang.String r8 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x00fd }
            r4[r5] = r8     // Catch:{ Throwable -> 0x00fd }
            java.lang.Process r3 = r3.exec(r4)     // Catch:{ Throwable -> 0x00fd }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x00fd }
            java.io.InputStream r3 = r3.getInputStream()     // Catch:{ Throwable -> 0x00fd }
            r4.<init>(r3)     // Catch:{ Throwable -> 0x00fd }
            java.io.BufferedReader r3 = a((java.io.InputStreamReader) r4)     // Catch:{ Throwable -> 0x00fd }
            if (r3 != 0) goto L_0x0099
            java.lang.String r0 = "[DEBUG] alloc buffer failed!\n"
            java.lang.String r2 = "UTF-8"
            byte[] r0 = r0.getBytes(r2)     // Catch:{ Throwable -> 0x008e, all -> 0x008a }
            r9.write(r0)     // Catch:{ Throwable -> 0x008e, all -> 0x008a }
            goto L_0x0092
        L_0x008a:
            r9 = move-exception
            r0 = r3
            goto L_0x010a
        L_0x008e:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r9)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
        L_0x0092:
            a((java.io.OutputStream) r9)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r3)
            return
        L_0x0099:
            g = r6     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            r0 = 0
            r4 = 0
        L_0x009d:
            java.lang.String r5 = r3.readLine()     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            if (r5 == 0) goto L_0x00ce
            int r0 = r0 + 1
            if (r4 >= r2) goto L_0x009d
            java.lang.String r8 = " I auditd "
            boolean r8 = r5.contains(r8)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            if (r8 != 0) goto L_0x009d
            java.lang.String r8 = " I liblog "
            boolean r8 = r5.contains(r8)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            if (r8 != 0) goto L_0x009d
            java.lang.String r8 = "UTF-8"
            byte[] r5 = r5.getBytes(r8)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            r9.write(r5)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            java.lang.String r5 = "\n"
            java.lang.String r8 = "UTF-8"
            byte[] r5 = r5.getBytes(r8)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            r9.write(r5)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            int r4 = r4 + 1
            goto L_0x009d
        L_0x00ce:
            java.util.Locale r2 = java.util.Locale.US     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            java.lang.String r5 = "[DEBUG] Read %d lines, wrote %d lines.\n"
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            r7[r6] = r0     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r4)     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            r7[r1] = r0     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            java.lang.String r0 = java.lang.String.format(r2, r5, r7)     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            java.lang.String r2 = "UTF-8"
            byte[] r0 = r0.getBytes(r2)     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            r9.write(r0)     // Catch:{ Throwable -> 0x00ee, all -> 0x008a }
            goto L_0x00f2
        L_0x00ee:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r9)     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
        L_0x00f2:
            g = r1     // Catch:{ Throwable -> 0x00f8, all -> 0x008a }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r3)
            goto L_0x0106
        L_0x00f8:
            r2 = move-exception
            r0 = r3
            goto L_0x00fe
        L_0x00fb:
            r9 = move-exception
            goto L_0x010a
        L_0x00fd:
            r2 = move-exception
        L_0x00fe:
            g = r1     // Catch:{ all -> 0x00fb }
            a((java.lang.Throwable) r2, (java.io.OutputStream) r9)     // Catch:{ all -> 0x00fb }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
        L_0x0106:
            a((java.io.OutputStream) r9)
            return
        L_0x010a:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.b(java.io.OutputStream):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0070 A[Catch:{ Throwable -> 0x0082 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void c(java.io.OutputStream r7) {
        /*
            java.lang.String r0 = "disk info:\n"
            java.lang.String r1 = "UTF-8"
            byte[] r0 = r0.getBytes(r1)     // Catch:{ Throwable -> 0x000c }
            r7.write(r0)     // Catch:{ Throwable -> 0x000c }
            goto L_0x0010
        L_0x000c:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r7)
        L_0x0010:
            boolean r0 = ad
            r1 = 0
            if (r0 == 0) goto L_0x002f
            g = r1
            java.lang.String r0 = "FSSTAT"
            java.lang.String r0 = s(r0)     // Catch:{ Throwable -> 0x0027 }
            java.lang.String r1 = "UTF-8"
            byte[] r0 = r0.getBytes(r1)     // Catch:{ Throwable -> 0x0027 }
            r7.write(r0)     // Catch:{ Throwable -> 0x0027 }
            goto L_0x002b
        L_0x0027:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r7)
        L_0x002b:
            r0 = 1
            g = r0
            goto L_0x0086
        L_0x002f:
            r0 = 0
            java.util.HashSet r2 = new java.util.HashSet     // Catch:{ Throwable -> 0x0048 }
            r2.<init>()     // Catch:{ Throwable -> 0x0048 }
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x0046 }
            java.lang.String r3 = com.uc.crashsdk.a.g.b()     // Catch:{ Throwable -> 0x0046 }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x0046 }
            java.lang.String r0 = a((java.io.File) r0)     // Catch:{ Throwable -> 0x0046 }
            a((java.io.OutputStream) r7, (java.lang.String) r0, (java.util.Set<java.lang.String>) r2)     // Catch:{ Throwable -> 0x0046 }
            goto L_0x004f
        L_0x0046:
            r0 = move-exception
            goto L_0x004c
        L_0x0048:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
        L_0x004c:
            a((java.lang.Throwable) r0, (java.io.OutputStream) r7)
        L_0x004f:
            java.io.File r0 = android.os.Environment.getExternalStorageDirectory()     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r0 = a((java.io.File) r0)     // Catch:{ Throwable -> 0x0082 }
            a((java.io.OutputStream) r7, (java.lang.String) r0, (java.util.Set<java.lang.String>) r2)     // Catch:{ Throwable -> 0x0082 }
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x0082 }
            java.lang.String r3 = "/storage"
            r0.<init>(r3)     // Catch:{ Throwable -> 0x0082 }
            boolean r3 = r0.exists()     // Catch:{ Throwable -> 0x0082 }
            if (r3 == 0) goto L_0x0086
            java.io.File[] r0 = r0.listFiles()     // Catch:{ Throwable -> 0x0082 }
            if (r0 == 0) goto L_0x0086
            int r3 = r0.length     // Catch:{ Throwable -> 0x0082 }
        L_0x006e:
            if (r1 >= r3) goto L_0x0086
            r4 = r0[r1]     // Catch:{ Throwable -> 0x0082 }
            boolean r5 = r4.isDirectory()     // Catch:{ Throwable -> 0x0082 }
            if (r5 == 0) goto L_0x007f
            java.lang.String r4 = a((java.io.File) r4)     // Catch:{ Throwable -> 0x0082 }
            a((java.io.OutputStream) r7, (java.lang.String) r4, (java.util.Set<java.lang.String>) r2)     // Catch:{ Throwable -> 0x0082 }
        L_0x007f:
            int r1 = r1 + 1
            goto L_0x006e
        L_0x0082:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r7)
        L_0x0086:
            a((java.io.OutputStream) r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.c(java.io.OutputStream):void");
    }

    private static String a(File file) {
        String str;
        try {
            str = file.getCanonicalPath();
        } catch (Throwable unused) {
            str = null;
        }
        return g.a(str) ? file.getPath() : str;
    }

    private static void a(OutputStream outputStream, String str, Set<String> set) {
        OutputStream outputStream2 = outputStream;
        String str2 = str;
        Set<String> set2 = set;
        if (!g.a(str) && !set2.contains(str2) && !str2.equals("/storage/emulated")) {
            set2.add(str2);
            try {
                StatFs statFs = new StatFs(str2);
                long a2 = a(statFs, "getBlockCountLong", "getBlockCount");
                long a3 = a(statFs, "getBlockSizeLong", "getBlockSize");
                if ((a2 / 1024) * a3 >= 10240) {
                    long a4 = a(statFs, "getAvailableBlocksLong", "getAvailableBlocks");
                    long a5 = a(statFs, "getFreeBlocksLong", "getFreeBlocks");
                    try {
                        outputStream2.write(String.format(Locale.US, "%s:\n", new Object[]{str2}).getBytes("UTF-8"));
                        Locale locale = Locale.US;
                        Object[] objArr = new Object[1];
                        double d2 = (double) a2;
                        Double.isNaN(d2);
                        double d3 = (double) a3;
                        Double.isNaN(d3);
                        objArr[0] = Long.valueOf((long) (((d2 * 1.0d) * d3) / 1024.0d));
                        outputStream2.write(String.format(locale, "  total:      %d kB\n", objArr).getBytes("UTF-8"));
                        Locale locale2 = Locale.US;
                        Object[] objArr2 = new Object[1];
                        double d4 = (double) a4;
                        Double.isNaN(d4);
                        Double.isNaN(d3);
                        objArr2[0] = Long.valueOf((long) (((d4 * 1.0d) * d3) / 1024.0d));
                        outputStream2.write(String.format(locale2, "  available:  %d kB\n", objArr2).getBytes("UTF-8"));
                        Locale locale3 = Locale.US;
                        Object[] objArr3 = new Object[1];
                        double d5 = (double) a5;
                        Double.isNaN(d5);
                        Double.isNaN(d3);
                        objArr3[0] = Long.valueOf((long) (((d5 * 1.0d) * d3) / 1024.0d));
                        outputStream2.write(String.format(locale3, "  free:       %d kB\n", objArr3).getBytes("UTF-8"));
                        outputStream2.write(String.format(Locale.US, "  block size: %d B\n\n", new Object[]{Long.valueOf(a3)}).getBytes("UTF-8"));
                    } catch (Throwable th) {
                        a(th, outputStream2);
                    }
                }
            } catch (Throwable unused) {
            }
        }
    }

    private static long a(StatFs statFs, String str, String str2) {
        try {
            if (Build.VERSION.SDK_INT >= 18) {
                Method declaredMethod = StatFs.class.getDeclaredMethod(str, new Class[0]);
                declaredMethod.setAccessible(true);
                Object invoke = declaredMethod.invoke(statFs, new Object[0]);
                if (invoke != null && (invoke instanceof Long)) {
                    return ((Long) invoke).longValue();
                }
            }
        } catch (Throwable unused) {
        }
        try {
            Method declaredMethod2 = StatFs.class.getDeclaredMethod(str2, new Class[0]);
            declaredMethod2.setAccessible(true);
            Object invoke2 = declaredMethod2.invoke(statFs, new Object[0]);
            if (invoke2 == null || !(invoke2 instanceof Integer)) {
                return 0;
            }
            return (long) ((Integer) invoke2).intValue();
        } catch (Throwable th) {
            g.a(th);
            return 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0055 A[SYNTHETIC, Splitter:B:17:0x0055] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void d(java.io.OutputStream r9) {
        /*
            r0 = 0
            r1 = 0
            int r2 = com.uc.crashsdk.h.G()     // Catch:{ Throwable -> 0x0049 }
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0044 }
            java.lang.String r4 = "/proc/self/fd"
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0044 }
            java.io.File[] r3 = r3.listFiles()     // Catch:{ Throwable -> 0x0044 }
            if (r3 == 0) goto L_0x0038
            java.util.Locale r1 = java.util.Locale.US     // Catch:{ Throwable -> 0x0036 }
            java.lang.String r4 = "opened file count: %d, write limit: %d.\n"
            r5 = 2
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0036 }
            int r6 = r3.length     // Catch:{ Throwable -> 0x0036 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ Throwable -> 0x0036 }
            r5[r0] = r6     // Catch:{ Throwable -> 0x0036 }
            java.lang.Integer r6 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x0036 }
            r7 = 1
            r5[r7] = r6     // Catch:{ Throwable -> 0x0036 }
            java.lang.String r1 = java.lang.String.format(r1, r4, r5)     // Catch:{ Throwable -> 0x0036 }
            java.lang.String r4 = "UTF-8"
            byte[] r1 = r1.getBytes(r4)     // Catch:{ Throwable -> 0x0036 }
            r9.write(r1)     // Catch:{ Throwable -> 0x0036 }
            goto L_0x0053
        L_0x0036:
            r1 = move-exception
            goto L_0x0050
        L_0x0038:
            java.lang.String r1 = "[DEBUG] listFiles failed!\n"
            java.lang.String r4 = "UTF-8"
            byte[] r1 = r1.getBytes(r4)     // Catch:{ Throwable -> 0x0036 }
            r9.write(r1)     // Catch:{ Throwable -> 0x0036 }
            goto L_0x0053
        L_0x0044:
            r3 = move-exception
            r8 = r3
            r3 = r1
            r1 = r8
            goto L_0x0050
        L_0x0049:
            r2 = move-exception
            r3 = 900(0x384, float:1.261E-42)
            r3 = r1
            r1 = r2
            r2 = 900(0x384, float:1.261E-42)
        L_0x0050:
            a((java.lang.Throwable) r1, (java.io.OutputStream) r9)
        L_0x0053:
            if (r3 == 0) goto L_0x009e
            int r1 = r3.length     // Catch:{ Throwable -> 0x009a }
            if (r1 < r2) goto L_0x009e
            java.lang.String r1 = "opened files:\n"
            java.lang.String r2 = "UTF-8"
            byte[] r1 = r1.getBytes(r2)     // Catch:{ Throwable -> 0x009a }
            r9.write(r1)     // Catch:{ Throwable -> 0x009a }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009a }
            r1.<init>()     // Catch:{ Throwable -> 0x009a }
            int r2 = r3.length     // Catch:{ Throwable -> 0x0088 }
        L_0x0069:
            if (r0 >= r2) goto L_0x008c
            r4 = r3[r0]     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r5 = r4.getName()     // Catch:{ Throwable -> 0x0088 }
            r1.append(r5)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r5 = " -> "
            r1.append(r5)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r4 = r4.getCanonicalPath()     // Catch:{ Throwable -> 0x0088 }
            r1.append(r4)     // Catch:{ Throwable -> 0x0088 }
            java.lang.String r4 = "\n"
            r1.append(r4)     // Catch:{ Throwable -> 0x0088 }
            int r0 = r0 + 1
            goto L_0x0069
        L_0x0088:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r9)     // Catch:{ Throwable -> 0x009a }
        L_0x008c:
            java.lang.String r0 = r1.toString()     // Catch:{ Throwable -> 0x009a }
            java.lang.String r1 = "UTF-8"
            byte[] r0 = r0.getBytes(r1)     // Catch:{ Throwable -> 0x009a }
            r9.write(r0)     // Catch:{ Throwable -> 0x009a }
            goto L_0x009e
        L_0x009a:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r9)
        L_0x009e:
            a((java.io.OutputStream) r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.d(java.io.OutputStream):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0031 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0032  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void e(java.io.OutputStream r12) {
        /*
            r0 = 0
            r1 = 0
            int r2 = com.uc.crashsdk.h.H()     // Catch:{ Throwable -> 0x0024 }
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0022 }
            java.lang.String r4 = "/proc/self/task"
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0022 }
            java.io.File[] r3 = r3.listFiles()     // Catch:{ Throwable -> 0x0022 }
            if (r3 != 0) goto L_0x0014
            return
        L_0x0014:
            int r1 = r3.length     // Catch:{ Throwable -> 0x001d }
            if (r1 >= r2) goto L_0x0018
            return
        L_0x0018:
            r11 = r2
            r2 = r1
            r1 = r3
            r3 = r11
            goto L_0x002f
        L_0x001d:
            r1 = move-exception
            r11 = r3
            r3 = r1
            r1 = r11
            goto L_0x002a
        L_0x0022:
            r3 = move-exception
            goto L_0x002a
        L_0x0024:
            r2 = move-exception
            r3 = 300(0x12c, float:4.2E-43)
            r3 = r2
            r2 = 300(0x12c, float:4.2E-43)
        L_0x002a:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r3)
            r3 = r2
            r2 = 0
        L_0x002f:
            if (r1 != 0) goto L_0x0032
            return
        L_0x0032:
            java.lang.String r4 = "threads info:\n"
            java.lang.String r5 = "UTF-8"
            byte[] r4 = r4.getBytes(r5)     // Catch:{ Throwable -> 0x00a2 }
            r12.write(r4)     // Catch:{ Throwable -> 0x00a2 }
            java.util.Locale r4 = java.util.Locale.US     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r5 = "threads count: %d, dump limit: %d.\n"
            r6 = 2
            java.lang.Object[] r7 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00a2 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x00a2 }
            r7[r0] = r2     // Catch:{ Throwable -> 0x00a2 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x00a2 }
            r3 = 1
            r7[r3] = r2     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r2 = java.lang.String.format(r4, r5, r7)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r4 = "UTF-8"
            byte[] r2 = r2.getBytes(r4)     // Catch:{ Throwable -> 0x00a2 }
            r12.write(r2)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r2 = " tid     name\n"
            java.lang.String r4 = "UTF-8"
            byte[] r2 = r2.getBytes(r4)     // Catch:{ Throwable -> 0x00a2 }
            r12.write(r2)     // Catch:{ Throwable -> 0x00a2 }
            int r2 = r1.length     // Catch:{ Throwable -> 0x00a2 }
            r4 = 0
        L_0x006b:
            if (r4 >= r2) goto L_0x00a6
            r5 = r1[r4]     // Catch:{ Throwable -> 0x00a2 }
            java.io.File r7 = new java.io.File     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r8 = r5.getPath()     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r9 = "comm"
            r7.<init>(r8, r9)     // Catch:{ Throwable -> 0x00a2 }
            r8 = 128(0x80, float:1.794E-43)
            java.lang.String r7 = com.uc.crashsdk.a.g.a((java.io.File) r7, (int) r8)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r7 = l(r7)     // Catch:{ Throwable -> 0x00a2 }
            java.util.Locale r8 = java.util.Locale.US     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r9 = "%5s %s\n"
            java.lang.Object[] r10 = new java.lang.Object[r6]     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r5 = r5.getName()     // Catch:{ Throwable -> 0x00a2 }
            r10[r0] = r5     // Catch:{ Throwable -> 0x00a2 }
            r10[r3] = r7     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r5 = java.lang.String.format(r8, r9, r10)     // Catch:{ Throwable -> 0x00a2 }
            java.lang.String r7 = "UTF-8"
            byte[] r5 = r5.getBytes(r7)     // Catch:{ Throwable -> 0x00a2 }
            r12.write(r5)     // Catch:{ Throwable -> 0x00a2 }
            int r4 = r4 + 1
            goto L_0x006b
        L_0x00a2:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r12)
        L_0x00a6:
            a((java.io.OutputStream) r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.e(java.io.OutputStream):void");
    }

    private static void a(b bVar) {
        try {
            bVar.a(String.format(Locale.US, "log end: %s\n", new Object[]{k()}));
        } catch (Throwable th) {
            a(th, (OutputStream) bVar);
        }
    }

    private static void b(b bVar) {
        g = false;
        try {
            bVar.write((s("LOG_END") + "\n").getBytes("UTF-8"));
        } catch (Throwable th) {
            g.a(th);
        }
        g = true;
    }

    static int a(OutputStream outputStream, String str, int i2) {
        int i3;
        Throwable th;
        if (str == null) {
            a(outputStream);
            return 0;
        }
        try {
            String a2 = com.uc.crashsdk.a.b.a(str);
            if (a2 == null) {
                a2 = "file: '" + str + "' not found or decode failed!";
            }
            i3 = a2.length();
            if (i3 > i2 + 32) {
                i3 = i2;
            }
            if (i3 > 0) {
                try {
                    outputStream.write(a2.getBytes("UTF-8"), 0, i3);
                    outputStream.write("\n".getBytes("UTF-8"));
                } catch (Throwable th2) {
                    th = th2;
                    a(th, outputStream);
                    a(outputStream);
                    return i3;
                }
            }
            if (i3 < a2.length()) {
                outputStream.write(String.format(Locale.US, "(truncated %d bytes)\n", new Object[]{Integer.valueOf(a2.length() - i3)}).getBytes("UTF-8"));
            }
        } catch (Throwable th3) {
            th = th3;
            i3 = 0;
            a(th, outputStream);
            a(outputStream);
            return i3;
        }
        a(outputStream);
        return i3;
    }

    static int b(OutputStream outputStream, String str, int i2) {
        int i3;
        DataInputStream dataInputStream;
        int i4;
        DataInputStream dataInputStream2 = null;
        try {
            File file = new File(str);
            if (file.exists()) {
                byte[] G2 = G();
                if (G2 == null) {
                    outputStream.write("(alloc buffer failed!)\n".getBytes("UTF-8"));
                    g.a((Closeable) null);
                    return 0;
                }
                dataInputStream = new DataInputStream(new FileInputStream(file));
                i4 = 0;
                i3 = 0;
                loop0:
                while (true) {
                    boolean z2 = false;
                    while (true) {
                        try {
                            int read = dataInputStream.read(G2);
                            if (read == -1) {
                                break loop0;
                            }
                            i4 += read;
                            int i5 = i2 - i3;
                            if (read <= i5 + 32) {
                                i5 = read;
                            }
                            if (i5 > 0 && !z2) {
                                outputStream.write(G2, 0, i5);
                                i3 += i5;
                            }
                            if (!z2) {
                                if (i5 < read || i3 >= i2) {
                                    z2 = true;
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            dataInputStream2 = dataInputStream;
                            g.a((Closeable) dataInputStream2);
                            throw th;
                        }
                    }
                }
            } else {
                outputStream.write(("file: '" + str + "' not exists!\n").getBytes("UTF-8"));
                dataInputStream = null;
                i4 = 0;
                i3 = 0;
            }
            if (i3 > 0) {
                outputStream.write("\n".getBytes("UTF-8"));
            }
            if (i3 < i4) {
                outputStream.write(String.format(Locale.US, "(truncated %d bytes)\n", new Object[]{Integer.valueOf(i4 - i3)}).getBytes("UTF-8"));
            }
            g.a((Closeable) dataInputStream);
        } catch (Throwable th2) {
            th = th2;
            i3 = 0;
            a(th, outputStream);
            g.a((Closeable) dataInputStream2);
            a(outputStream);
            return i3;
        }
        a(outputStream);
        return i3;
    }

    static String a(int i2) {
        try {
            String a2 = g.a(new File(String.format(Locale.US, "/proc/%d/cmdline", new Object[]{Integer.valueOf(i2)})), 128);
            if (g.b(a2)) {
                return l(a2);
            }
            return "unknown";
        } catch (Throwable th) {
            g.a(th);
            return "unknown";
        }
    }

    private static String l(String str) {
        if (!g.b(str)) {
            return "";
        }
        int indexOf = str.indexOf(0);
        if (indexOf >= 0) {
            str = str.substring(0, indexOf);
        }
        return str.trim();
    }

    public static String g() {
        if (m != null) {
            return m;
        }
        String a2 = a(Process.myPid());
        m = a2;
        return a2;
    }

    private static boolean a(String str, String str2, int i2) {
        if (b.d) {
            return JNIBridge.nativeSyncStatus(str, str2, i2);
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:101:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x01d6, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x01e0, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:116:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:0x01eb, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:126:0x01fb, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:136:0x022b, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:143:0x0241, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:145:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:150:0x025e, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002b, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:168:0x0305, code lost:
        r10 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:174:0x0319, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:176:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:183:0x0333, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:185:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:189:0x033d, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:191:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:195:0x0349, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:197:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:204:0x0363, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:206:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:211:0x0373, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:213:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:243:0x03b6, code lost:
        b(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0088, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        com.uc.crashsdk.a.g.a(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b6, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:?, code lost:
        com.uc.crashsdk.a.g.a(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00e7, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        com.uc.crashsdk.a.g.a(r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x0104, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x010f, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x011b, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0125, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:?, code lost:
        com.uc.crashsdk.a.g.a(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x013c, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:?, code lost:
        a(r14, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0163, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x01ac, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:?, code lost:
        a(r10, (java.io.OutputStream) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:99:0x01bc, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002b A[ExcHandler: all (th java.lang.Throwable), Splitter:B:26:0x0043] */
    /* JADX WARNING: Removed duplicated region for block: B:179:0x0321 A[Catch:{ Throwable -> 0x0382, all -> 0x002b }] */
    /* JADX WARNING: Removed duplicated region for block: B:200:0x0351 A[Catch:{ Throwable -> 0x0382, all -> 0x002b }] */
    /* JADX WARNING: Removed duplicated region for block: B:216:0x037b  */
    /* JADX WARNING: Removed duplicated region for block: B:231:0x039a  */
    /* JADX WARNING: Removed duplicated region for block: B:235:0x03a1 A[Catch:{ Throwable -> 0x03ad }] */
    /* JADX WARNING: Removed duplicated region for block: B:236:0x03a6 A[Catch:{ Throwable -> 0x03ad }] */
    /* JADX WARNING: Removed duplicated region for block: B:243:0x03b6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String a(java.lang.Throwable r10, java.lang.String r11, long r12, boolean r14) {
        /*
            r0 = 0
            r2 = 0
            boolean r3 = com.uc.crashsdk.b.D()     // Catch:{ Throwable -> 0x0388 }
            r4 = 1
            if (r3 != 0) goto L_0x0010
            com.uc.crashsdk.h.b()     // Catch:{ Throwable -> 0x0388 }
            a((boolean) r4)     // Catch:{ Throwable -> 0x0388 }
        L_0x0010:
            int r3 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r3 != 0) goto L_0x001a
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0388 }
            r3.<init>(r11)     // Catch:{ Throwable -> 0x0388 }
            goto L_0x001b
        L_0x001a:
            r3 = r2
        L_0x001b:
            com.uc.crashsdk.e$b r5 = new com.uc.crashsdk.e$b     // Catch:{ Throwable -> 0x0388 }
            r5.<init>(r12, r3)     // Catch:{ Throwable -> 0x0388 }
            r3 = 0
            boolean r6 = com.uc.crashsdk.b.d     // Catch:{ Throwable -> 0x002e, all -> 0x002b }
            if (r6 == 0) goto L_0x0032
            java.lang.String r6 = "logj"
            a((java.lang.String) r6, (java.lang.String) r11, (int) r3)     // Catch:{ Throwable -> 0x002e, all -> 0x002b }
            goto L_0x0032
        L_0x002b:
            r10 = move-exception
            goto L_0x03b2
        L_0x002e:
            r6 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r6)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0032:
            java.lang.String r6 = H()     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            b((java.io.OutputStream) r5, (java.lang.String) r11, (java.lang.String) r6)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            if (r14 == 0) goto L_0x0043
            r5.flush()     // Catch:{ Throwable -> 0x003f, all -> 0x002b }
            goto L_0x0043
        L_0x003f:
            r6 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r6)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0043:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = "Process Name: '"
            r6.<init>(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = g()     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            r6.append(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = "'\n"
            r6.append(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            r5.write(r6)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = "Thread Name: '"
            r6.<init>(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.Thread r7 = java.lang.Thread.currentThread()     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = r7.getName()     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            r6.append(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = "'\n"
            r6.append(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            r5.write(r6)     // Catch:{ Throwable -> 0x0088, all -> 0x002b }
            goto L_0x008c
        L_0x0088:
            r6 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r6)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x008c:
            java.lang.String r6 = "Back traces starts.\n"
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            r5.write((byte[]) r6)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            java.lang.Class<java.lang.Throwable> r6 = java.lang.Throwable.class
            java.lang.String r7 = "detailMessage"
            java.lang.reflect.Field r6 = r6.getDeclaredField(r7)     // Catch:{ Throwable -> 0x00b6, all -> 0x002b }
            r6.setAccessible(r4)     // Catch:{ Throwable -> 0x00b6, all -> 0x002b }
            java.lang.Object r7 = r6.get(r10)     // Catch:{ Throwable -> 0x00b6, all -> 0x002b }
            if (r7 == 0) goto L_0x00ba
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x00b6, all -> 0x002b }
            java.lang.String r8 = "\n\t"
            java.lang.String r9 = "\n->  "
            java.lang.String r7 = r7.replaceAll(r8, r9)     // Catch:{ Throwable -> 0x00b6, all -> 0x002b }
            r6.set(r10, r7)     // Catch:{ Throwable -> 0x00b6, all -> 0x002b }
            goto L_0x00ba
        L_0x00b6:
            r6 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r6)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
        L_0x00ba:
            java.lang.String r6 = r10.getMessage()     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            if (r6 == 0) goto L_0x00eb
            java.lang.String r7 = r10.getLocalizedMessage()     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            boolean r7 = r6.equals(r7)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            if (r7 != 0) goto L_0x00eb
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            java.lang.String r8 = "Message: "
            r7.<init>(r8)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            r7.append(r6)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            java.lang.String r6 = "\n"
            r7.append(r6)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            java.lang.String r6 = r7.toString()     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            r5.write((byte[]) r6)     // Catch:{ Throwable -> 0x00e7, all -> 0x002b }
            goto L_0x00eb
        L_0x00e7:
            r6 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r6)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x00eb:
            java.io.PrintStream r6 = new java.io.PrintStream     // Catch:{ Throwable -> 0x00f4, all -> 0x002b }
            r6.<init>(r5)     // Catch:{ Throwable -> 0x00f4, all -> 0x002b }
            r10.printStackTrace(r6)     // Catch:{ Throwable -> 0x00f4, all -> 0x002b }
            goto L_0x00f8
        L_0x00f4:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x00f8:
            java.lang.String r10 = "Back traces ends.\n"
            java.lang.String r6 = "UTF-8"
            byte[] r10 = r10.getBytes(r6)     // Catch:{ Throwable -> 0x0104, all -> 0x002b }
            r5.write((byte[]) r10)     // Catch:{ Throwable -> 0x0104, all -> 0x002b }
            goto L_0x0108
        L_0x0104:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0108:
            a((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            r5.flush()     // Catch:{ Throwable -> 0x010f, all -> 0x002b }
            goto L_0x0113
        L_0x010f:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0113:
            java.lang.String r10 = "UTF-8"
            java.lang.String r6 = "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n"
            com.uc.crashsdk.a.a((java.io.OutputStream) r5, (java.lang.String) r10, (java.lang.String) r6)     // Catch:{ Throwable -> 0x011b, all -> 0x002b }
            goto L_0x011f
        L_0x011b:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x011f:
            if (r14 == 0) goto L_0x0129
            r5.flush()     // Catch:{ Throwable -> 0x0125, all -> 0x002b }
            goto L_0x0129
        L_0x0125:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0129:
            r10 = 10240(0x2800, float:1.4349E-41)
            java.lang.String r14 = "/proc/meminfo"
            java.lang.String r6 = "meminfo:\n"
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ Throwable -> 0x013c, all -> 0x002b }
            r5.write(r6)     // Catch:{ Throwable -> 0x013c, all -> 0x002b }
            b((java.io.OutputStream) r5, (java.lang.String) r14, (int) r10)     // Catch:{ Throwable -> 0x013c, all -> 0x002b }
            goto L_0x0140
        L_0x013c:
            r14 = move-exception
            a((java.lang.Throwable) r14, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0140:
            java.util.Locale r14 = java.util.Locale.US     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            java.lang.String r6 = "/proc/%d/status"
            java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            int r8 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            r7[r3] = r8     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            java.lang.String r14 = java.lang.String.format(r14, r6, r7)     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            java.lang.String r6 = "status:\n"
            java.lang.String r7 = "UTF-8"
            byte[] r6 = r6.getBytes(r7)     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            r5.write(r6)     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            b((java.io.OutputStream) r5, (java.lang.String) r14, (int) r10)     // Catch:{ Throwable -> 0x0163, all -> 0x002b }
            goto L_0x0167
        L_0x0163:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0167:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.String r14 = "Dalvik:\nMaxMemory: "
            r10.<init>(r14)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.Runtime r14 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            long r6 = r14.maxMemory()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            r10.append(r6)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.String r14 = " TotalMemory: "
            r10.append(r14)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.Runtime r14 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            long r6 = r14.totalMemory()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            r10.append(r6)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.String r14 = " FreeMemory: "
            r10.append(r14)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.Runtime r14 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            long r6 = r14.freeMemory()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            r10.append(r6)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.String r14 = "\n"
            r10.append(r14)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x01ac, all -> 0x002b }
            goto L_0x01b0
        L_0x01ac:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01b0:
            a((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            f((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            java.lang.String r10 = "UTF-8"
            com.uc.crashsdk.a.a((java.io.OutputStream) r5, (java.lang.String) r10, (java.util.ArrayList<java.lang.String>) r2)     // Catch:{ Throwable -> 0x01bc, all -> 0x002b }
            goto L_0x01c0
        L_0x01bc:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01c0:
            boolean r10 = ad     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            if (r10 == 0) goto L_0x01dc
            g = r3     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            java.lang.String r10 = "JAVADUMPFILES"
            java.lang.String r10 = s(r10)     // Catch:{ Throwable -> 0x01d6, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x01d6, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x01d6, all -> 0x002b }
            goto L_0x01da
        L_0x01d6:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01da:
            g = r4     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01dc:
            r5.flush()     // Catch:{ Throwable -> 0x01e0, all -> 0x002b }
            goto L_0x01e4
        L_0x01e0:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01e4:
            b((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            r5.flush()     // Catch:{ Throwable -> 0x01eb, all -> 0x002b }
            goto L_0x01ef
        L_0x01eb:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01ef:
            java.lang.String r10 = "battery info:\n"
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x01fb, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x01fb, all -> 0x002b }
            goto L_0x01ff
        L_0x01fb:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x01ff:
            boolean r10 = ad     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            if (r10 == 0) goto L_0x0217
            g = r3     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            java.lang.String r10 = "BATTERYINFO"
            java.lang.String r10 = s(r10)     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            g = r4     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            goto L_0x022f
        L_0x0217:
            java.lang.StringBuilder r10 = K()     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            if (r10 == 0) goto L_0x022f
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x022b, all -> 0x002b }
            goto L_0x022f
        L_0x022b:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x022f:
            a((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            c((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            java.lang.String r10 = "device status:\n"
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0241, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0241, all -> 0x002b }
            goto L_0x0245
        L_0x0241:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0245:
            boolean r10 = ad     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            if (r10 == 0) goto L_0x0264
            g = r3     // Catch:{ Throwable -> 0x025e, all -> 0x002b }
            java.lang.String r10 = "DEVICESTATUS"
            java.lang.String r10 = s(r10)     // Catch:{ Throwable -> 0x025e, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x025e, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x025e, all -> 0x002b }
            g = r4     // Catch:{ Throwable -> 0x025e, all -> 0x002b }
            goto L_0x0308
        L_0x025e:
            r10 = move-exception
        L_0x025f:
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            goto L_0x0308
        L_0x0264:
            java.util.Locale r10 = java.util.Locale.US     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = "has root: %s\n"
            java.lang.Object[] r6 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            boolean r7 = com.uc.crashsdk.a.g.d()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r6[r3] = r7     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r10 = java.lang.String.format(r10, r14, r6)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r10 = ""
            java.lang.String r14 = android.os.Build.TAGS     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            if (r14 == 0) goto L_0x0289
            java.lang.String r10 = android.os.Build.TAGS     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
        L_0x0289:
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r14.<init>()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r6 = "build tags: "
            r14.append(r6)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r14.append(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            boolean r10 = com.uc.crashsdk.a.g.e()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            if (r10 == 0) goto L_0x02a1
            java.lang.String r10 = " (default root)"
            r14.append(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
        L_0x02a1:
            java.lang.String r10 = "\n"
            r14.append(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r10 = r14.toString()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r10 = com.uc.crashsdk.a.g.g()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            boolean r14 = com.uc.crashsdk.a.g.b((java.lang.String) r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            if (r14 == 0) goto L_0x0308
            java.util.Locale r14 = java.util.Locale.US     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r6 = "su binary: %s\n"
            java.lang.Object[] r7 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r7[r3] = r10     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r10 = java.lang.String.format(r14, r6, r7)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r10.<init>()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = "su permission: "
            r10.append(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            boolean r14 = com.uc.crashsdk.a.g.f()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            if (r14 == 0) goto L_0x02e8
            java.lang.String r14 = "valid ("
        L_0x02e4:
            r10.append(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            goto L_0x02eb
        L_0x02e8:
            java.lang.String r14 = "invalid ("
            goto L_0x02e4
        L_0x02eb:
            java.lang.String r14 = com.uc.crashsdk.a.g.h()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r10.append(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = ")\n"
            r10.append(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0305, all -> 0x002b }
            goto L_0x0308
        L_0x0305:
            r10 = move-exception
            goto L_0x025f
        L_0x0308:
            a((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            d((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            e((java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            java.lang.String r10 = "UTF-8"
            java.lang.String r14 = "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n"
            com.uc.crashsdk.a.b(r5, r10, r14, r2)     // Catch:{ Throwable -> 0x0319, all -> 0x002b }
            goto L_0x031d
        L_0x0319:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x031d:
            boolean r10 = ad     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            if (r10 == 0) goto L_0x0339
            g = r3     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            java.lang.String r10 = "JAVACACHEDINFOS"
            java.lang.String r10 = s(r10)     // Catch:{ Throwable -> 0x0333, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0333, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0333, all -> 0x002b }
            goto L_0x0337
        L_0x0333:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0337:
            g = r4     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0339:
            r5.flush()     // Catch:{ Throwable -> 0x033d, all -> 0x002b }
            goto L_0x0341
        L_0x033d:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0341:
            java.lang.String r10 = "UTF-8"
            java.lang.String r14 = "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n"
            com.uc.crashsdk.a.a(r5, r10, r14, r2)     // Catch:{ Throwable -> 0x0349, all -> 0x002b }
            goto L_0x034d
        L_0x0349:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x034d:
            boolean r10 = ad     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            if (r10 == 0) goto L_0x0369
            g = r3     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            java.lang.String r10 = "JAVACALLBACKINFOS"
            java.lang.String r10 = s(r10)     // Catch:{ Throwable -> 0x0363, all -> 0x002b }
            java.lang.String r14 = "UTF-8"
            byte[] r10 = r10.getBytes(r14)     // Catch:{ Throwable -> 0x0363, all -> 0x002b }
            r5.write(r10)     // Catch:{ Throwable -> 0x0363, all -> 0x002b }
            goto L_0x0367
        L_0x0363:
            r10 = move-exception
            a((java.lang.Throwable) r10, (java.io.OutputStream) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0367:
            g = r4     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0369:
            r5.a()     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            a((com.uc.crashsdk.e.b) r5)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
            r5.flush()     // Catch:{ Throwable -> 0x0373, all -> 0x002b }
            goto L_0x0377
        L_0x0373:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)     // Catch:{ Throwable -> 0x0382, all -> 0x002b }
        L_0x0377:
            int r10 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r10 == 0) goto L_0x037e
            b((com.uc.crashsdk.e.b) r5)
        L_0x037e:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r5)
            goto L_0x0396
        L_0x0382:
            r10 = move-exception
            r2 = r5
            goto L_0x0389
        L_0x0385:
            r10 = move-exception
            r5 = r2
            goto L_0x03b2
        L_0x0388:
            r10 = move-exception
        L_0x0389:
            a((java.lang.Throwable) r10, (java.io.OutputStream) r2)     // Catch:{ all -> 0x0385 }
            int r10 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r10 == 0) goto L_0x0393
            b((com.uc.crashsdk.e.b) r2)
        L_0x0393:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r2)
        L_0x0396:
            boolean r10 = ad
            if (r10 != 0) goto L_0x039d
            r(r11)
        L_0x039d:
            boolean r10 = ad     // Catch:{ Throwable -> 0x03ad }
            if (r10 != 0) goto L_0x03a6
            java.lang.String r10 = m(r11)     // Catch:{ Throwable -> 0x03ad }
            goto L_0x03a7
        L_0x03a6:
            r10 = r11
        L_0x03a7:
            java.lang.String r12 = "java"
            b((java.lang.String) r10, (java.lang.String) r12)     // Catch:{ Throwable -> 0x03ad }
            goto L_0x03b1
        L_0x03ad:
            r10 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r10)
        L_0x03b1:
            return r11
        L_0x03b2:
            int r11 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
            if (r11 == 0) goto L_0x03b9
            b((com.uc.crashsdk.e.b) r5)
        L_0x03b9:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r5)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.a(java.lang.Throwable, java.lang.String, long, boolean):java.lang.String");
    }

    private static void f(OutputStream outputStream) {
        String str;
        try {
            outputStream.write("Recent Status:\n".getBytes("UTF-8"));
        } catch (Throwable th) {
            a(th, outputStream);
        }
        try {
            if (ad) {
                str = s("LASTVER");
            } else {
                str = a.l();
            }
            outputStream.write(String.format(Locale.US, "last version: '%s'\n", new Object[]{str}).getBytes("UTF-8"));
        } catch (Throwable th2) {
            a(th2, outputStream);
        }
        try {
            synchronized (r) {
                if (t != null) {
                    outputStream.write(String.format(Locale.US, "generating log: %s\n", new Object[]{t}).getBytes("UTF-8"));
                }
                if (s > 0 || r.size() > 0) {
                    outputStream.write(String.format(Locale.US, "generated %d logs, recent are:\n", new Object[]{Integer.valueOf(s)}).getBytes("UTF-8"));
                    Iterator<String> it = r.iterator();
                    while (it.hasNext()) {
                        outputStream.write(String.format(Locale.US, "* %s\n", new Object[]{it.next()}).getBytes("UTF-8"));
                    }
                }
            }
            outputStream.write(String.format(Locale.US, "dumping all threads: %s\n", new Object[]{Boolean.valueOf(u)}).getBytes("UTF-8"));
            if (v != null) {
                outputStream.write(String.format(Locale.US, "dumping threads: %s\n", new Object[]{v}).getBytes("UTF-8"));
            }
        } catch (Throwable th3) {
            a(th3, outputStream);
        }
        a(outputStream);
    }

    private static String m(String str) {
        String a2 = com.uc.crashsdk.a.b.a(str, h.x(), h.w());
        if (!str.equals(a2)) {
            File file = new File(str);
            if (file.exists()) {
                file.delete();
            }
        }
        return a2;
    }

    static void a(Throwable th, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.write("[DEBUG] CrashHandler occurred new exception:\n".getBytes("UTF-8"));
                th.printStackTrace(new PrintStream(outputStream));
                outputStream.write("\n\n".getBytes("UTF-8"));
            } catch (Throwable th2) {
                g.a(th2);
            }
        }
        g.a(th);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x002d, code lost:
        if (r4 != false) goto L_0x002f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002f, code lost:
        n.notify();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0035, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0037, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0039, code lost:
        throw r2;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:8:0x001a, B:13:0x0023] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void b(java.lang.String r2, boolean r3, boolean r4) {
        /*
            java.lang.String r0 = "crashsdk uploading logs"
            com.uc.crashsdk.a.a.b(r0)
            java.lang.Object r0 = n
            monitor-enter(r0)
            boolean r1 = com.uc.crashsdk.a.g.b((java.lang.String) r2)     // Catch:{ Throwable -> 0x0022 }
            if (r1 == 0) goto L_0x0013
            r1 = 0
            a((java.lang.String) r2, (boolean) r3, (boolean) r1)     // Catch:{ Throwable -> 0x0022 }
            goto L_0x0018
        L_0x0013:
            java.lang.String r2 = "upload url is empty!"
            com.uc.crashsdk.a.a.b(r2)     // Catch:{ Throwable -> 0x0022 }
        L_0x0018:
            if (r4 == 0) goto L_0x002b
            java.lang.Object r2 = n     // Catch:{ all -> 0x0035 }
        L_0x001c:
            r2.notify()     // Catch:{ all -> 0x0035 }
            goto L_0x002b
        L_0x0020:
            r2 = move-exception
            goto L_0x002d
        L_0x0022:
            r2 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r2)     // Catch:{ all -> 0x0020 }
            if (r4 == 0) goto L_0x002b
            java.lang.Object r2 = n     // Catch:{ all -> 0x0035 }
            goto L_0x001c
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x0035 }
            return
        L_0x002d:
            if (r4 == 0) goto L_0x0037
            java.lang.Object r3 = n     // Catch:{ all -> 0x0035 }
            r3.notify()     // Catch:{ all -> 0x0035 }
            goto L_0x0037
        L_0x0035:
            r2 = move-exception
            goto L_0x0038
        L_0x0037:
            throw r2     // Catch:{ all -> 0x0035 }
        L_0x0038:
            monitor-exit(r0)     // Catch:{ all -> 0x0035 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.b(java.lang.String, boolean, boolean):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x02a5  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x02e2  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x02e4  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x02f1  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0364  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x0357 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0186  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x019a  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x01a7  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x01a9 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x01c4  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x01db  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x0264  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x029a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void a(java.lang.String r28, boolean r29, boolean r30) {
        /*
            java.lang.String r0 = com.uc.crashsdk.h.T()
            java.io.File r1 = new java.io.File
            r1.<init>(r0)
            boolean r2 = r1.exists()
            if (r2 != 0) goto L_0x0021
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Folder not exist: "
            r1.<init>(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.uc.crashsdk.a.a.b(r0)
            return
        L_0x0021:
            java.io.File[] r1 = r1.listFiles()
            if (r1 != 0) goto L_0x0039
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "List folder failed: "
            r1.<init>(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.uc.crashsdk.a.a.c(r0)
            return
        L_0x0039:
            int r2 = r1.length
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r11 = 0
            r12 = 0
            r13 = 0
        L_0x0044:
            if (r4 >= r2) goto L_0x0370
            r14 = r1[r4]
            boolean r0 = r14.isFile()
            if (r0 != 0) goto L_0x0052
            com.uc.crashsdk.a.g.a((java.io.File) r14)
            goto L_0x0061
        L_0x0052:
            if (r30 == 0) goto L_0x006d
            java.lang.String r0 = r14.getName()
            java.lang.String r15 = "ucebu"
            boolean r0 = r0.contains(r15)
            if (r0 == 0) goto L_0x0061
            goto L_0x006d
        L_0x0061:
            r24 = r1
            r25 = r2
            r19 = r4
        L_0x0067:
            r18 = 0
            r4 = r28
            goto L_0x0367
        L_0x006d:
            long r17 = r14.length()
            r19 = r4
            r3 = 0
            int r0 = (r17 > r3 ? 1 : (r17 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0088
            int r5 = r5 + 1
            com.uc.crashsdk.a.g.a((java.io.File) r14)
            r4 = r28
            r24 = r1
            r25 = r2
            r18 = 0
            goto L_0x0367
        L_0x0088:
            if (r29 == 0) goto L_0x00ec
            long r20 = java.lang.System.currentTimeMillis()
            long r22 = r14.lastModified()
            long r20 = r20 - r22
            r22 = 1000(0x3e8, double:4.94E-321)
            long r20 = r20 / r22
            int r0 = (r20 > r3 ? 1 : (r20 == r3 ? 0 : -1))
            if (r0 < 0) goto L_0x00b7
            r22 = 2
            int r0 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1))
            if (r0 >= 0) goto L_0x00a4
        L_0x00a2:
            r0 = 0
            goto L_0x00b8
        L_0x00a4:
            r22 = 5
            int r0 = (r20 > r22 ? 1 : (r20 == r22 ? 0 : -1))
            if (r0 >= 0) goto L_0x00b7
            java.lang.String r0 = r14.getName()
            java.lang.String r3 = ".log"
            boolean r0 = r0.endsWith(r3)
            if (r0 == 0) goto L_0x00b7
            goto L_0x00a2
        L_0x00b7:
            r0 = 1
        L_0x00b8:
            java.util.Locale r3 = java.util.Locale.US
            java.lang.String r4 = "file: %s, modify interval: %d s, safe upload: %s"
            r24 = r1
            r15 = 3
            java.lang.Object[] r1 = new java.lang.Object[r15]
            java.lang.String r15 = r14.getName()
            r18 = 0
            r1[r18] = r15
            java.lang.Long r15 = java.lang.Long.valueOf(r20)
            r16 = 1
            r1[r16] = r15
            java.lang.Boolean r15 = java.lang.Boolean.valueOf(r0)
            r18 = 2
            r1[r18] = r15
            java.lang.String r1 = java.lang.String.format(r3, r4, r1)
            com.uc.crashsdk.a.a.a(r1)
            if (r0 != 0) goto L_0x00ee
            int r6 = r6 + 1
            r18 = 0
            r4 = r28
            r25 = r2
            goto L_0x0367
        L_0x00ec:
            r24 = r1
        L_0x00ee:
            boolean r0 = com.uc.crashsdk.h.l()     // Catch:{ Throwable -> 0x018a }
            if (r0 == 0) goto L_0x0181
            java.lang.String r0 = r14.getName()     // Catch:{ Throwable -> 0x018a }
            java.lang.String r1 = "([^_]+)_([^_]+)_([^_]+)\\.crashsdk"
            java.util.regex.Pattern r1 = java.util.regex.Pattern.compile(r1)     // Catch:{ Throwable -> 0x018a }
            java.util.regex.Matcher r0 = r1.matcher(r0)     // Catch:{ Throwable -> 0x018a }
            boolean r1 = r0.matches()     // Catch:{ Throwable -> 0x018a }
            if (r1 == 0) goto L_0x0181
            r1 = 1
            java.lang.String r3 = r0.group(r1)     // Catch:{ Throwable -> 0x018a }
            r1 = 2
            java.lang.String r4 = r0.group(r1)     // Catch:{ Throwable -> 0x018a }
            r1 = 3
            java.lang.String r0 = r0.group(r1)     // Catch:{ Throwable -> 0x018a }
            java.util.Locale r1 = java.util.Locale.US     // Catch:{ Throwable -> 0x018a }
            java.lang.String r15 = "%s%s_%s_%s.%s"
            r25 = r2
            r2 = 5
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x017f }
            java.lang.String r4 = j(r4)     // Catch:{ Throwable -> 0x017f }
            r18 = 0
            r2[r18] = r4     // Catch:{ Throwable -> 0x017f }
            java.lang.String r4 = k()     // Catch:{ Throwable -> 0x017f }
            r16 = 1
            r2[r16] = r4     // Catch:{ Throwable -> 0x017f }
            java.lang.String r4 = F()     // Catch:{ Throwable -> 0x017f }
            r18 = 2
            r2[r18] = r4     // Catch:{ Throwable -> 0x017f }
            r4 = 3
            r2[r4] = r3     // Catch:{ Throwable -> 0x017f }
            r3 = 4
            r2[r3] = r0     // Catch:{ Throwable -> 0x017f }
            java.lang.String r0 = java.lang.String.format(r1, r15, r2)     // Catch:{ Throwable -> 0x017f }
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x017f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x017f }
            r2.<init>()     // Catch:{ Throwable -> 0x017f }
            java.lang.String r3 = com.uc.crashsdk.h.T()     // Catch:{ Throwable -> 0x017f }
            r2.append(r3)     // Catch:{ Throwable -> 0x017f }
            r2.append(r0)     // Catch:{ Throwable -> 0x017f }
            java.lang.String r0 = r2.toString()     // Catch:{ Throwable -> 0x017f }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x017f }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x017f }
            java.lang.String r2 = "File "
            r0.<init>(r2)     // Catch:{ Throwable -> 0x017f }
            java.lang.String r2 = r14.getPath()     // Catch:{ Throwable -> 0x017f }
            r0.append(r2)     // Catch:{ Throwable -> 0x017f }
            java.lang.String r2 = " matches, rename to "
            r0.append(r2)     // Catch:{ Throwable -> 0x017f }
            java.lang.String r2 = r1.getPath()     // Catch:{ Throwable -> 0x017f }
            r0.append(r2)     // Catch:{ Throwable -> 0x017f }
            java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x017f }
            com.uc.crashsdk.a.a.b(r0)     // Catch:{ Throwable -> 0x017f }
            r14.renameTo(r1)     // Catch:{ Throwable -> 0x017f }
            goto L_0x0184
        L_0x017f:
            r0 = move-exception
            goto L_0x018d
        L_0x0181:
            r25 = r2
            r1 = r14
        L_0x0184:
            if (r1 == r14) goto L_0x0188
            int r9 = r9 + 1
        L_0x0188:
            r14 = r1
            goto L_0x0190
        L_0x018a:
            r0 = move-exception
            r25 = r2
        L_0x018d:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x0190:
            java.lang.String r0 = r14.getPath()
            java.lang.String r1 = n(r0)
            if (r0 == r1) goto L_0x01a1
            int r10 = r10 + 1
            java.io.File r14 = new java.io.File
            r14.<init>(r1)
        L_0x01a1:
            java.io.File r0 = com.uc.crashsdk.d.a((java.io.File) r14)
            if (r0 != 0) goto L_0x01a9
            r0 = 0
            goto L_0x01c2
        L_0x01a9:
            if (r14 == r0) goto L_0x01c2
            java.lang.String r2 = r14.getName()
            java.lang.String r3 = r0.getName()
            boolean r2 = r2.equals(r3)
            if (r2 != 0) goto L_0x01c2
            boolean r2 = r14.exists()
            if (r2 == 0) goto L_0x01c2
            r14.delete()
        L_0x01c2:
            if (r0 != 0) goto L_0x01db
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "onBeforeUploadLog return null, skip upload: "
            r0.<init>(r1)
            java.lang.String r1 = r14.getAbsolutePath()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.uc.crashsdk.a.a.c(r0)
            goto L_0x0067
        L_0x01db:
            int r2 = com.uc.crashsdk.h.z()
            if (r2 <= 0) goto L_0x01f1
            long r3 = r0.length()
            long r14 = (long) r2
            int r2 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1))
            if (r2 < 0) goto L_0x01f1
            int r8 = r8 + 1
            com.uc.crashsdk.a.g.a((java.io.File) r0)
            goto L_0x0067
        L_0x01f1:
            com.uc.crashsdk.e$d r2 = new com.uc.crashsdk.e$d
            r3 = 0
            r2.<init>(r3)
            r3 = 0
            r2.b = r3
            long r3 = java.lang.System.currentTimeMillis()
            r2.a = r3
            java.lang.String r3 = J()
            java.io.File r4 = new java.io.File
            r4.<init>(r3)
            boolean r4 = r4.exists()
            if (r4 == 0) goto L_0x0223
            com.uc.crashsdk.a.e r4 = new com.uc.crashsdk.a.e
            r14 = 451(0x1c3, float:6.32E-43)
            r15 = 2
            java.lang.Object[] r1 = new java.lang.Object[r15]
            r15 = 0
            r1[r15] = r3
            r15 = 1
            r1[r15] = r2
            r4.<init>(r14, r1)
            a((java.lang.String) r3, (com.uc.crashsdk.a.e) r4)
        L_0x0223:
            long r3 = com.uc.crashsdk.h.A()
            int r1 = com.uc.crashsdk.h.B()
            int r14 = com.uc.crashsdk.h.C()
            r20 = 0
            int r15 = (r3 > r20 ? 1 : (r3 == r20 ? 0 : -1))
            if (r15 < 0) goto L_0x025a
            r26 = r9
            r27 = r10
            long r9 = r2.b
            long r20 = r0.length()
            long r9 = r9 + r20
            int r15 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1))
            if (r15 <= 0) goto L_0x025e
            r9 = 1
            r2.e = r9
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r9 = "Reach max upload bytes: "
            r1.<init>(r9)
            r1.append(r3)
        L_0x0252:
            java.lang.String r1 = r1.toString()
        L_0x0256:
            com.uc.crashsdk.a.a.c(r1)
            goto L_0x0296
        L_0x025a:
            r26 = r9
            r27 = r10
        L_0x025e:
            boolean r3 = com.uc.crashsdk.h.g()
            if (r3 != 0) goto L_0x0296
            boolean r3 = b((java.io.File) r0)
            if (r3 == 0) goto L_0x0282
            if (r1 < 0) goto L_0x0296
            int r3 = r2.c
            if (r3 < r1) goto L_0x0296
            r3 = 1
            r2.g = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "Reach max upload crash log count: "
            r3.<init>(r4)
            r3.append(r1)
            java.lang.String r1 = r3.toString()
            goto L_0x0256
        L_0x0282:
            if (r14 < 0) goto L_0x0296
            int r1 = r2.d
            if (r1 < r14) goto L_0x0296
            r1 = 1
            r2.f = r1
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r3 = "Reach max upload custom log count: "
            r1.<init>(r3)
            r1.append(r14)
            goto L_0x0252
        L_0x0296:
            boolean r1 = r2.e
            if (r1 == 0) goto L_0x02a5
            r11 = 1
        L_0x029b:
            r18 = 0
            r4 = r28
        L_0x029f:
            r9 = r26
            r10 = r27
            goto L_0x0367
        L_0x02a5:
            boolean r1 = r2.g
            if (r1 == 0) goto L_0x02ab
            r12 = 1
            goto L_0x029b
        L_0x02ab:
            boolean r1 = r2.f
            if (r1 == 0) goto L_0x02b6
            r9 = r26
            r10 = r27
            r7 = 1
            goto L_0x0067
        L_0x02b6:
            java.lang.String r1 = r0.getName()
            java.lang.String r3 = E()
            boolean r3 = r1.startsWith(r3)
            if (r3 == 0) goto L_0x02d5
            java.lang.String r3 = "_"
            r4 = 10
            java.lang.String[] r1 = r1.split(r3, r4)
            int r3 = r1.length
            r4 = 9
            if (r3 != r4) goto L_0x02d5
            r3 = 1
            r1 = r1[r3]
            goto L_0x02d6
        L_0x02d5:
            r1 = 0
        L_0x02d6:
            if (r1 == 0) goto L_0x02e4
            java.lang.String r3 = com.uc.crashsdk.h.P()
            boolean r1 = r1.equals(r3)
            if (r1 == 0) goto L_0x02e4
            r1 = 1
            goto L_0x02e5
        L_0x02e4:
            r1 = 0
        L_0x02e5:
            java.lang.String r3 = r0.getName()
            r4 = r28
            boolean r3 = com.uc.crashsdk.a.c.a((java.io.File) r0, (java.lang.String) r3, (java.lang.String) r4)
            if (r3 == 0) goto L_0x0349
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r9 = "Uploaded log: "
            r3.<init>(r9)
            java.lang.String r9 = r0.getName()
            r3.append(r9)
            java.lang.String r3 = r3.toString()
            java.lang.String r9 = "crashsdk"
            r10 = 0
            com.uc.crashsdk.a.a.a(r9, r3, r10)
            if (r1 == 0) goto L_0x0310
            r1 = 13
            com.uc.crashsdk.f.a((int) r1)
        L_0x0310:
            long r9 = r2.b
            long r13 = r0.length()
            long r9 = r9 + r13
            r2.b = r9
            boolean r1 = b((java.io.File) r0)
            if (r1 == 0) goto L_0x0326
            int r1 = r2.c
            r3 = 1
            int r1 = r1 + r3
            r2.c = r1
            goto L_0x032c
        L_0x0326:
            r3 = 1
            int r1 = r2.d
            int r1 = r1 + r3
            r2.d = r1
        L_0x032c:
            java.lang.String r1 = J()
            com.uc.crashsdk.a.e r9 = new com.uc.crashsdk.a.e
            r10 = 452(0x1c4, float:6.33E-43)
            r13 = 2
            java.lang.Object[] r13 = new java.lang.Object[r13]
            r18 = 0
            r13[r18] = r1
            r13[r3] = r2
            r9.<init>(r10, r13)
            a((java.lang.String) r1, (com.uc.crashsdk.a.e) r9)
            r0.delete()
            r1 = 3
            r3 = 0
            goto L_0x0355
        L_0x0349:
            r18 = 0
            int r3 = r13 + 1
            if (r1 == 0) goto L_0x0354
            r0 = 14
            com.uc.crashsdk.f.a((int) r0)
        L_0x0354:
            r1 = 3
        L_0x0355:
            if (r3 < r1) goto L_0x0364
            java.lang.String r0 = "Upload failed 3 times continuously, abort upload!"
            java.lang.String r1 = "crashsdk"
            r2 = 0
            com.uc.crashsdk.a.a.a(r1, r0, r2)
            r9 = r26
            r10 = r27
            goto L_0x0370
        L_0x0364:
            r13 = r3
            goto L_0x029f
        L_0x0367:
            int r0 = r19 + 1
            r4 = r0
            r1 = r24
            r2 = r25
            goto L_0x0044
        L_0x0370:
            if (r5 <= 0) goto L_0x0377
            r0 = 15
            com.uc.crashsdk.f.a((int) r0, (int) r5)
        L_0x0377:
            if (r8 <= 0) goto L_0x037e
            r0 = 17
            com.uc.crashsdk.f.a((int) r0, (int) r8)
        L_0x037e:
            if (r11 == 0) goto L_0x0385
            r0 = 19
            com.uc.crashsdk.f.a((int) r0)
        L_0x0385:
            if (r12 == 0) goto L_0x038c
            r0 = 20
            com.uc.crashsdk.f.a((int) r0)
        L_0x038c:
            if (r7 == 0) goto L_0x0393
            r0 = 21
            com.uc.crashsdk.f.a((int) r0)
        L_0x0393:
            if (r11 != 0) goto L_0x0399
            if (r12 != 0) goto L_0x0399
            if (r7 == 0) goto L_0x039e
        L_0x0399:
            r0 = 18
            com.uc.crashsdk.f.a((int) r0)
        L_0x039e:
            if (r10 <= 0) goto L_0x03a5
            r0 = 24
            com.uc.crashsdk.f.a((int) r0, (int) r10)
        L_0x03a5:
            if (r9 <= 0) goto L_0x03ac
            r0 = 25
            com.uc.crashsdk.f.a((int) r0, (int) r9)
        L_0x03ac:
            if (r6 <= 0) goto L_0x03b3
            r0 = 26
            com.uc.crashsdk.f.a((int) r0, (int) r6)
        L_0x03b3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.a(java.lang.String, boolean, boolean):void");
    }

    static boolean h() {
        return d;
    }

    public static boolean a(boolean z2, boolean z3) {
        if (!d) {
            if (b.d) {
                JNIBridge.nativeSetCrashLogFilesUploaded();
            }
            d = true;
        }
        try {
            String i2 = i();
            if (g.a(i2)) {
                com.uc.crashsdk.a.a.b("CrashHandler url is empty!");
                return false;
            }
            synchronized (n) {
                if (f.a(z2 ? 1 : 0, (Runnable) new com.uc.crashsdk.a.e(406, new Object[]{i2, Boolean.valueOf(z3), Boolean.valueOf(z2)})) && z2) {
                    try {
                        n.wait();
                    } catch (InterruptedException e2) {
                        g.a((Throwable) e2);
                    }
                }
            }
            return true;
        } catch (Throwable th) {
            g.a(th);
            return false;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void b(boolean r3) {
        /*
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x0040 }
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            boolean r0 = com.uc.crashsdk.b.A()     // Catch:{ Throwable -> 0x0040 }
            if (r0 == 0) goto L_0x003f
            boolean r0 = d     // Catch:{ Throwable -> 0x0040 }
            if (r0 != 0) goto L_0x003f
            r0 = 0
            if (r3 == 0) goto L_0x003b
            java.lang.String r3 = i()     // Catch:{ Throwable -> 0x0040 }
            boolean r1 = com.uc.crashsdk.a.g.a((java.lang.String) r3)     // Catch:{ Throwable -> 0x0040 }
            if (r1 != 0) goto L_0x003a
            android.os.StrictMode$ThreadPolicy r1 = android.os.StrictMode.getThreadPolicy()     // Catch:{ Throwable -> 0x0033 }
            android.os.StrictMode$ThreadPolicy$Builder r2 = new android.os.StrictMode$ThreadPolicy$Builder     // Catch:{ Throwable -> 0x0033 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x0033 }
            android.os.StrictMode$ThreadPolicy$Builder r1 = r2.permitNetwork()     // Catch:{ Throwable -> 0x0033 }
            android.os.StrictMode$ThreadPolicy r1 = r1.build()     // Catch:{ Throwable -> 0x0033 }
            android.os.StrictMode.setThreadPolicy(r1)     // Catch:{ Throwable -> 0x0033 }
            goto L_0x0037
        L_0x0033:
            r1 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r1)     // Catch:{ Throwable -> 0x0040 }
        L_0x0037:
            b((java.lang.String) r3, (boolean) r0, (boolean) r0)     // Catch:{ Throwable -> 0x0040 }
        L_0x003a:
            return
        L_0x003b:
            r3 = 1
            a((boolean) r3, (boolean) r0)     // Catch:{ Throwable -> 0x0040 }
        L_0x003f:
            return
        L_0x0040:
            r3 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.b(boolean):void");
    }

    private static String J() {
        return h.S() + "bytes";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0025, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x005d, code lost:
        r1 = null;
        r5 = r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x005f, code lost:
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        com.uc.crashsdk.a.g.a((java.lang.Throwable) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0066, code lost:
        r6 = th;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0024 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:11:0x0018] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(java.lang.String r5, com.uc.crashsdk.a.e r6) {
        /*
            java.lang.Object r0 = o
            monitor-enter(r0)
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x006b }
            r1.<init>(r5)     // Catch:{ all -> 0x006b }
            boolean r5 = r1.exists()     // Catch:{ all -> 0x006b }
            if (r5 != 0) goto L_0x0016
            r1.createNewFile()     // Catch:{ Exception -> 0x0012 }
            goto L_0x0016
        L_0x0012:
            r5 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x006b }
        L_0x0016:
            r5 = 0
            r2 = 0
            java.io.RandomAccessFile r3 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0027, all -> 0x0024 }
            java.lang.String r4 = "rw"
            r3.<init>(r1, r4)     // Catch:{ Exception -> 0x0027, all -> 0x0024 }
            java.nio.channels.FileChannel r1 = r3.getChannel()     // Catch:{ Exception -> 0x0027, all -> 0x0024 }
            goto L_0x002c
        L_0x0024:
            r6 = move-exception
            r1 = r5
            goto L_0x0067
        L_0x0027:
            r1 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r1)     // Catch:{ Exception -> 0x005c, all -> 0x0024 }
            r1 = r5
        L_0x002c:
            if (r1 == 0) goto L_0x003b
            java.nio.channels.FileLock r3 = r1.lock()     // Catch:{ Exception -> 0x0034 }
            r5 = r3
            goto L_0x003b
        L_0x0034:
            r3 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r3)     // Catch:{ Exception -> 0x0039 }
            goto L_0x003b
        L_0x0039:
            r5 = move-exception
            goto L_0x005f
        L_0x003b:
            boolean r6 = r6.a()     // Catch:{ all -> 0x0050 }
            if (r5 == 0) goto L_0x004c
            r5.release()     // Catch:{ Exception -> 0x0045 }
            goto L_0x004c
        L_0x0045:
            r5 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r5)     // Catch:{ Exception -> 0x004a }
            goto L_0x004c
        L_0x004a:
            r5 = move-exception
            goto L_0x0060
        L_0x004c:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)     // Catch:{ all -> 0x006b }
            goto L_0x0064
        L_0x0050:
            r6 = move-exception
            if (r5 == 0) goto L_0x005b
            r5.release()     // Catch:{ Exception -> 0x0057 }
            goto L_0x005b
        L_0x0057:
            r5 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r5)     // Catch:{ Exception -> 0x0039 }
        L_0x005b:
            throw r6     // Catch:{ Exception -> 0x0039 }
        L_0x005c:
            r6 = move-exception
            r1 = r5
            r5 = r6
        L_0x005f:
            r6 = 0
        L_0x0060:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r5)     // Catch:{ all -> 0x0066 }
            goto L_0x004c
        L_0x0064:
            monitor-exit(r0)     // Catch:{ all -> 0x006b }
            return r6
        L_0x0066:
            r6 = move-exception
        L_0x0067:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)     // Catch:{ all -> 0x006b }
            throw r6     // Catch:{ all -> 0x006b }
        L_0x006b:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x006b }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.a(java.lang.String, com.uc.crashsdk.a.e):boolean");
    }

    /* compiled from: ProGuard */
    private static class d {
        long a;
        long b;
        int c;
        int d;
        boolean e;
        boolean f;
        boolean g;

        private d() {
            this.a = 0;
            this.b = 0;
            this.c = 0;
            this.d = 0;
            this.e = false;
            this.f = false;
            this.g = false;
        }

        /* synthetic */ d(byte b2) {
            this();
        }
    }

    private static boolean a(String str, d dVar) {
        String a2 = g.a(new File(str), 64);
        if (a2 == null) {
            return false;
        }
        try {
            Matcher matcher = Pattern.compile("(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)").matcher(a2);
            if (matcher.find()) {
                long parseLong = Long.parseLong(matcher.group(1));
                if (System.currentTimeMillis() - parseLong < 86400000) {
                    dVar.b = Long.parseLong(matcher.group(2));
                    dVar.c = Integer.parseInt(matcher.group(3));
                    dVar.d = Integer.parseInt(matcher.group(4));
                    dVar.a = parseLong;
                }
            }
        } catch (Throwable th) {
            g.a(th);
        }
        return true;
    }

    private static boolean b(File file) {
        int indexOf;
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(95);
        if (lastIndexOf <= 0 || (indexOf = name.indexOf(46, lastIndexOf)) <= 0) {
            return false;
        }
        String substring = name.substring(lastIndexOf + 1, indexOf);
        if ("java".equals(substring) || "ucebujava".equals(substring) || LogType.NATIVE_TYPE.equals(substring) || "ucebujni".equals(substring) || LogType.UNEXP_TYPE.equals(substring)) {
            return true;
        }
        return false;
    }

    private static String n(String str) {
        int lastIndexOf;
        int i2;
        if (!h.w() || g.a(h.x()) || (lastIndexOf = str.lastIndexOf(".log")) <= 0 || lastIndexOf + 4 != str.length()) {
            return str;
        }
        int lastIndexOf2 = str.lastIndexOf(File.separatorChar);
        int i3 = 0;
        if (lastIndexOf2 < 0) {
            lastIndexOf2 = 0;
        }
        do {
            i2 = str.indexOf(95, i2);
            if (i2 >= 0) {
                i3++;
                i2++;
                continue;
            }
        } while (i2 >= 0);
        if (i3 != 8) {
            return str;
        }
        int lastIndexOf3 = str.lastIndexOf(95);
        if (!a && lastIndexOf3 <= 0) {
            throw new AssertionError();
        } else if (str.indexOf(".log", lastIndexOf3) != lastIndexOf) {
            return str;
        } else {
            try {
                return m(str);
            } catch (Throwable th) {
                g.a(th);
                return str;
            }
        }
    }

    public static boolean a(StringBuffer stringBuffer, String str, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, String str2) {
        long j2;
        String str3;
        boolean a2;
        String str4 = str;
        boolean z7 = z6;
        if (c) {
            com.uc.crashsdk.a.a.c("Processing java crash, skip generate custom log: " + str4);
            return false;
        }
        boolean z8 = ad || b.D();
        if (!z8 && !com.uc.crashsdk.a.d.d()) {
            com.uc.crashsdk.a.a.b(UMLLCons.FEATURE_TYPE_DEBUG, com.uc.crashsdk.a.d.b());
            return false;
        } else if (!d(str)) {
            com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, "custom log sample miss: " + str4);
            return false;
        } else if (b.d && JNIBridge.nativeIsCrashing()) {
            com.uc.crashsdk.a.a.c("Processing native crash, skip generate custom log: " + str4);
            return false;
        } else if (stringBuffer == null || str4 == null) {
            return false;
        } else {
            String str5 = h.T() + k(str);
            if (z8) {
                long nativeClientCreateConnection = b.d ? JNIBridge.nativeClientCreateConnection(str5, "custom", str4, z7 ? 1 : 0) : 0;
                if (nativeClientCreateConnection == 0) {
                    com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, "skip custom log: " + str4);
                    return false;
                }
                j2 = nativeClientCreateConnection;
            } else if (a(g(), str4, z7)) {
                return false;
            } else {
                h.b();
                a(false);
                j2 = 0;
            }
            synchronized (p) {
                str3 = str5;
                a2 = a(str5, j2, stringBuffer, str, z2, z3, z4, z5, arrayList, arrayList2, arrayList3, str2);
            }
            if (a2 && !z8) {
                b(g(), str4, z7);
            }
            if (j2 != 0) {
                JNIBridge.nativeClientCloseConnection(j2);
            }
            if (!a2) {
                return false;
            }
            if (!z8) {
                r(str3);
            }
            b(!z8 ? m(str3) : str3, str4);
            if (!z7 || z8) {
                return true;
            }
            try {
                a(true, false);
                return true;
            } catch (Throwable th) {
                g.a(th);
                return true;
            }
        }
    }

    static boolean a(String str, String str2, boolean z2) {
        if (!o(str2)) {
            return false;
        }
        h.a(str, str2, true, z2);
        com.uc.crashsdk.a.a.c(String.format(Locale.US, "Custom log '%s' has reach max count!", new Object[]{str2}));
        return true;
    }

    static void b(String str, String str2, boolean z2) {
        h.a(str, str2, false, z2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0056, code lost:
        r11 = java.lang.Long.parseLong(r10.group(2));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0067, code lost:
        if ((r4 - r11) >= 86400000) goto L_0x0077;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r0 = java.lang.Integer.parseInt(r10.group(3));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0072, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        com.uc.crashsdk.a.g.a((java.lang.Throwable) r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0077, code lost:
        r11 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0078, code lost:
        r0 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0079, code lost:
        r13 = com.uc.crashsdk.h.D();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x007d, code lost:
        if (r13 < 0) goto L_0x0083;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0081, code lost:
        r13 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0083, code lost:
        r13 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        r6.replace(r10.start(), r10.end(), java.lang.String.format(java.util.Locale.US, "%s %d %d", new java.lang.Object[]{r1, java.lang.Long.valueOf(r11), java.lang.Integer.valueOf(r0 + 1)}));
        r0 = true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00af A[Catch:{ Exception -> 0x0072 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean o(java.lang.String r17) {
        /*
            r1 = r17
            java.lang.Object r2 = q
            monitor-enter(r2)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ef }
            r0.<init>()     // Catch:{ all -> 0x00ef }
            java.lang.String r3 = com.uc.crashsdk.h.S()     // Catch:{ all -> 0x00ef }
            r0.append(r3)     // Catch:{ all -> 0x00ef }
            java.lang.String r3 = "customlog"
            r0.append(r3)     // Catch:{ all -> 0x00ef }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x00ef }
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x00ef }
            r3.<init>(r0)     // Catch:{ all -> 0x00ef }
            r0 = 1024(0x400, float:1.435E-42)
            java.lang.String r0 = com.uc.crashsdk.a.g.a((java.io.File) r3, (int) r0)     // Catch:{ all -> 0x00ef }
            long r4 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x00ef }
            java.lang.StringBuffer r6 = new java.lang.StringBuffer     // Catch:{ all -> 0x00ef }
            r6.<init>()     // Catch:{ all -> 0x00ef }
            r7 = 2
            r8 = 1
            r9 = 0
            if (r0 == 0) goto L_0x00ab
            r6.append(r0)     // Catch:{ all -> 0x00ef }
            java.lang.String r0 = "([^\\n\\r\\t\\s]+) (\\d+) (\\d+)"
            java.util.regex.Pattern r0 = java.util.regex.Pattern.compile(r0)     // Catch:{ all -> 0x00ef }
            java.util.regex.Matcher r10 = r0.matcher(r6)     // Catch:{ all -> 0x00ef }
            r0 = 0
        L_0x0041:
            boolean r0 = r10.find(r0)     // Catch:{ all -> 0x00ef }
            if (r0 == 0) goto L_0x00ab
            java.lang.String r0 = r10.group(r8)     // Catch:{ all -> 0x00ef }
            boolean r0 = r1.equals(r0)     // Catch:{ all -> 0x00ef }
            if (r0 != 0) goto L_0x0056
            int r0 = r10.end()     // Catch:{ all -> 0x00ef }
            goto L_0x0041
        L_0x0056:
            java.lang.String r0 = r10.group(r7)     // Catch:{ all -> 0x00ef }
            long r11 = java.lang.Long.parseLong(r0)     // Catch:{ all -> 0x00ef }
            r0 = 0
            long r13 = r4 - r11
            r15 = 86400000(0x5265c00, double:4.2687272E-316)
            r7 = 3
            int r0 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r0 >= 0) goto L_0x0077
            java.lang.String r0 = r10.group(r7)     // Catch:{ Exception -> 0x0072 }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ Exception -> 0x0072 }
            goto L_0x0079
        L_0x0072:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ all -> 0x00ef }
            goto L_0x0078
        L_0x0077:
            r11 = r4
        L_0x0078:
            r0 = 0
        L_0x0079:
            int r13 = com.uc.crashsdk.h.D()     // Catch:{ all -> 0x00ef }
            if (r13 < 0) goto L_0x0083
            if (r0 < r13) goto L_0x0083
            r13 = 1
            goto L_0x0084
        L_0x0083:
            r13 = 0
        L_0x0084:
            int r0 = r0 + r8
            java.util.Locale r14 = java.util.Locale.US     // Catch:{ all -> 0x00ef }
            java.lang.String r15 = "%s %d %d"
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x00ef }
            r7[r9] = r1     // Catch:{ all -> 0x00ef }
            java.lang.Long r11 = java.lang.Long.valueOf(r11)     // Catch:{ all -> 0x00ef }
            r7[r8] = r11     // Catch:{ all -> 0x00ef }
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ all -> 0x00ef }
            r11 = 2
            r7[r11] = r0     // Catch:{ all -> 0x00ef }
            java.lang.String r0 = java.lang.String.format(r14, r15, r7)     // Catch:{ all -> 0x00ef }
            int r7 = r10.start()     // Catch:{ all -> 0x00ef }
            int r10 = r10.end()     // Catch:{ all -> 0x00ef }
            r6.replace(r7, r10, r0)     // Catch:{ all -> 0x00ef }
            r0 = 1
            goto L_0x00ad
        L_0x00ab:
            r0 = 0
            r13 = 0
        L_0x00ad:
            if (r0 != 0) goto L_0x00c5
            java.util.Locale r0 = java.util.Locale.US     // Catch:{ all -> 0x00ef }
            java.lang.String r7 = "%s %d 1\n"
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ all -> 0x00ef }
            r10[r9] = r1     // Catch:{ all -> 0x00ef }
            java.lang.Long r1 = java.lang.Long.valueOf(r4)     // Catch:{ all -> 0x00ef }
            r10[r8] = r1     // Catch:{ all -> 0x00ef }
            java.lang.String r0 = java.lang.String.format(r0, r7, r10)     // Catch:{ all -> 0x00ef }
            r6.append(r0)     // Catch:{ all -> 0x00ef }
        L_0x00c5:
            r1 = 0
            java.io.FileWriter r4 = new java.io.FileWriter     // Catch:{ Exception -> 0x00e2 }
            r4.<init>(r3)     // Catch:{ Exception -> 0x00e2 }
            java.lang.String r0 = r6.toString()     // Catch:{ Exception -> 0x00dd, all -> 0x00da }
            int r1 = r0.length()     // Catch:{ Exception -> 0x00dd, all -> 0x00da }
            r4.write(r0, r9, r1)     // Catch:{ Exception -> 0x00dd, all -> 0x00da }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r4)     // Catch:{ all -> 0x00ef }
            goto L_0x00e9
        L_0x00da:
            r0 = move-exception
            r1 = r4
            goto L_0x00eb
        L_0x00dd:
            r0 = move-exception
            r1 = r4
            goto L_0x00e3
        L_0x00e0:
            r0 = move-exception
            goto L_0x00eb
        L_0x00e2:
            r0 = move-exception
        L_0x00e3:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ all -> 0x00e0 }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)     // Catch:{ all -> 0x00ef }
        L_0x00e9:
            monitor-exit(r2)     // Catch:{ all -> 0x00ef }
            return r13
        L_0x00eb:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r1)     // Catch:{ all -> 0x00ef }
            throw r0     // Catch:{ all -> 0x00ef }
        L_0x00ef:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00ef }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.o(java.lang.String):boolean");
    }

    private static void a(b bVar, String str, long j2) {
        String str2;
        String str3 = null;
        if (b.d) {
            try {
                bVar.flush();
            } catch (Throwable th) {
                g.a(th);
            }
            str2 = JNIBridge.nativeDumpThreads(str, j2);
            if (ad || str2 == null || str2.length() >= 512 || !str2.startsWith("/") || str2.indexOf(10) >= 0) {
                str3 = str2;
            } else {
                if (!new File(str2).exists()) {
                    str3 = "Can not found " + str2;
                }
                String str4 = str3;
                str3 = str2;
                str2 = str4;
            }
        } else {
            str2 = "Native not initialized, skip dump!";
        }
        if (str2 != null) {
            try {
                bVar.write(str2.getBytes("UTF-8"));
                bVar.write("\n".getBytes("UTF-8"));
            } catch (Throwable th2) {
                g.a(th2);
            }
            a((OutputStream) bVar);
        } else if (str3 != null && !ad) {
            b((OutputStream) bVar, str3, 1048576);
            File file = new File(str3);
            if (file.exists()) {
                file.delete();
            }
        }
        try {
            bVar.flush();
        } catch (Throwable th3) {
            g.a(th3);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x003f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0040  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean a(java.lang.String r16, long r17, java.lang.StringBuffer r19, java.lang.String r20, boolean r21, boolean r22, boolean r23, boolean r24, java.util.ArrayList<java.lang.String> r25, java.util.ArrayList<java.lang.String> r26, java.util.ArrayList<java.lang.String> r27, java.lang.String r28) {
        /*
            r1 = r16
            r2 = r17
            r4 = r25
            r5 = r26
            r6 = r27
            r7 = r28
            r8 = 0
            r10 = 0
            r11 = 0
            int r0 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r0 != 0) goto L_0x001f
            java.io.FileOutputStream r0 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x001b }
            r0.<init>(r1)     // Catch:{ Throwable -> 0x001b }
            r12 = r0
            goto L_0x0020
        L_0x001b:
            r0 = move-exception
            r12 = r10
            r13 = r12
            goto L_0x003a
        L_0x001f:
            r12 = r10
        L_0x0020:
            com.uc.crashsdk.e$b r13 = new com.uc.crashsdk.e$b     // Catch:{ Throwable -> 0x0038 }
            r13.<init>(r2, r12)     // Catch:{ Throwable -> 0x0038 }
            java.util.ArrayList<java.lang.String> r14 = r     // Catch:{ Throwable -> 0x0036 }
            monitor-enter(r14)     // Catch:{ Throwable -> 0x0036 }
            t = r1     // Catch:{ all -> 0x0033 }
            java.lang.String r0 = "logb"
            java.lang.String r15 = t     // Catch:{ all -> 0x0033 }
            a((java.lang.String) r0, (java.lang.String) r15, (int) r11)     // Catch:{ all -> 0x0033 }
            monitor-exit(r14)     // Catch:{ all -> 0x0033 }
            goto L_0x003d
        L_0x0033:
            r0 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x0033 }
            throw r0     // Catch:{ Throwable -> 0x0036 }
        L_0x0036:
            r0 = move-exception
            goto L_0x003a
        L_0x0038:
            r0 = move-exception
            r13 = r10
        L_0x003a:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x003d:
            if (r13 != 0) goto L_0x0040
            return r11
        L_0x0040:
            r14 = 1
            if (r21 == 0) goto L_0x004f
            r15 = r20
            b((java.io.OutputStream) r13, (java.lang.String) r1, (java.lang.String) r15)     // Catch:{ Throwable -> 0x004c }
            goto L_0x004f
        L_0x0049:
            r0 = move-exception
            goto L_0x0156
        L_0x004c:
            r0 = move-exception
            goto L_0x0112
        L_0x004f:
            java.lang.String r0 = r19.toString()     // Catch:{ Throwable -> 0x0069 }
            byte[] r0 = r0.getBytes()     // Catch:{ Throwable -> 0x0069 }
            r13.write((byte[]) r0)     // Catch:{ Throwable -> 0x0069 }
            java.lang.String r0 = "\n"
            java.lang.String r1 = "UTF-8"
            byte[] r0 = r0.getBytes(r1)     // Catch:{ Throwable -> 0x0069 }
            r13.write((byte[]) r0)     // Catch:{ Throwable -> 0x0069 }
            r13.flush()     // Catch:{ Throwable -> 0x0069 }
            goto L_0x006d
        L_0x0069:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x006d:
            a((java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
            if (r23 == 0) goto L_0x007e
            b((java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
            r13.flush()     // Catch:{ Throwable -> 0x0079 }
            goto L_0x007e
        L_0x0079:
            r0 = move-exception
            r1 = r0
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r1)     // Catch:{ Throwable -> 0x004c }
        L_0x007e:
            if (r4 == 0) goto L_0x008b
            int r0 = r25.size()     // Catch:{ Throwable -> 0x004c }
            if (r0 <= 0) goto L_0x008b
            java.lang.String r0 = "UTF-8"
            com.uc.crashsdk.a.a((java.io.OutputStream) r13, (java.lang.String) r0, (java.util.ArrayList<java.lang.String>) r4)     // Catch:{ Throwable -> 0x004c }
        L_0x008b:
            if (r5 == 0) goto L_0x009a
            int r0 = r26.size()     // Catch:{ Throwable -> 0x004c }
            if (r0 <= 0) goto L_0x009a
            java.lang.String r0 = "UTF-8"
            java.lang.String r1 = "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n"
            com.uc.crashsdk.a.a(r13, r0, r1, r5)     // Catch:{ Throwable -> 0x004c }
        L_0x009a:
            if (r6 == 0) goto L_0x00a9
            int r0 = r27.size()     // Catch:{ Throwable -> 0x004c }
            if (r0 <= 0) goto L_0x00a9
            java.lang.String r0 = "UTF-8"
            java.lang.String r1 = "--- --- --- --- --- --- --- --- --- --- --- --- --- --- --- ---\n"
            com.uc.crashsdk.a.b(r13, r0, r1, r6)     // Catch:{ Throwable -> 0x004c }
        L_0x00a9:
            if (r7 == 0) goto L_0x00d5
            r13.flush()     // Catch:{ Throwable -> 0x00af }
            goto L_0x00b4
        L_0x00af:
            r0 = move-exception
            r1 = r0
            a((java.lang.Throwable) r1, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x00b4:
            java.lang.String r0 = "threads dump:\n"
            java.lang.String r1 = "UTF-8"
            byte[] r0 = r0.getBytes(r1)     // Catch:{ Throwable -> 0x00c0 }
            r13.write((byte[]) r0)     // Catch:{ Throwable -> 0x00c0 }
            goto L_0x00c4
        L_0x00c0:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x00c4:
            g = r11     // Catch:{ Throwable -> 0x004c }
            v = r7     // Catch:{ Throwable -> 0x004c }
            a((com.uc.crashsdk.e.b) r13, (java.lang.String) r7, (long) r2)     // Catch:{ Throwable -> 0x00cc }
            goto L_0x00d1
        L_0x00cc:
            r0 = move-exception
            r1 = r0
            a((java.lang.Throwable) r1, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x00d1:
            v = r10     // Catch:{ Throwable -> 0x004c }
            g = r14     // Catch:{ Throwable -> 0x004c }
        L_0x00d5:
            if (r24 == 0) goto L_0x0102
            int r0 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r0 != 0) goto L_0x0102
            r13.flush()     // Catch:{ Throwable -> 0x00df }
            goto L_0x00e4
        L_0x00df:
            r0 = move-exception
            r1 = r0
            a((java.lang.Throwable) r1, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x00e4:
            java.lang.String r0 = "all threads dump:\n"
            java.lang.String r1 = "UTF-8"
            byte[] r0 = r0.getBytes(r1)     // Catch:{ Throwable -> 0x00f0 }
            r13.write((byte[]) r0)     // Catch:{ Throwable -> 0x00f0 }
            goto L_0x00f4
        L_0x00f0:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x00f4:
            u = r14     // Catch:{ Throwable -> 0x004c }
            java.lang.String r0 = "all"
            a((com.uc.crashsdk.e.b) r13, (java.lang.String) r0, (long) r8)     // Catch:{ Throwable -> 0x00fc }
            goto L_0x0100
        L_0x00fc:
            r0 = move-exception
            a((java.lang.Throwable) r0, (java.io.OutputStream) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x0100:
            u = r11     // Catch:{ Throwable -> 0x004c }
        L_0x0102:
            if (r22 == 0) goto L_0x010a
            r13.a()     // Catch:{ Throwable -> 0x004c }
            a((com.uc.crashsdk.e.b) r13)     // Catch:{ Throwable -> 0x004c }
        L_0x010a:
            int r0 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
            if (r0 == 0) goto L_0x0115
            b((com.uc.crashsdk.e.b) r13)     // Catch:{ Throwable -> 0x004c }
            goto L_0x0115
        L_0x0112:
            a((java.lang.Throwable) r0, (java.io.OutputStream) r13)     // Catch:{ all -> 0x0049 }
        L_0x0115:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r13)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r12)
            java.util.ArrayList<java.lang.String> r1 = r     // Catch:{ Throwable -> 0x0151 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0151 }
            int r0 = s     // Catch:{ all -> 0x014e }
            int r0 = r0 + r14
            s = r0     // Catch:{ all -> 0x014e }
            java.lang.String r0 = t     // Catch:{ all -> 0x014e }
            if (r0 == 0) goto L_0x0145
            java.util.ArrayList<java.lang.String> r0 = r     // Catch:{ all -> 0x014e }
            java.lang.String r2 = t     // Catch:{ all -> 0x014e }
            r0.add(r2)     // Catch:{ all -> 0x014e }
            java.util.ArrayList<java.lang.String> r0 = r     // Catch:{ all -> 0x014e }
            int r0 = r0.size()     // Catch:{ all -> 0x014e }
            r2 = 3
            if (r0 <= r2) goto L_0x013c
            java.util.ArrayList<java.lang.String> r0 = r     // Catch:{ all -> 0x014e }
            r0.remove(r11)     // Catch:{ all -> 0x014e }
        L_0x013c:
            java.lang.String r0 = "loge"
            java.lang.String r2 = t     // Catch:{ all -> 0x014e }
            a((java.lang.String) r0, (java.lang.String) r2, (int) r11)     // Catch:{ all -> 0x014e }
            t = r10     // Catch:{ all -> 0x014e }
        L_0x0145:
            java.lang.String r0 = "logct"
            int r2 = s     // Catch:{ all -> 0x014e }
            a((java.lang.String) r0, (java.lang.String) r10, (int) r2)     // Catch:{ all -> 0x014e }
            monitor-exit(r1)     // Catch:{ all -> 0x014e }
            goto L_0x0155
        L_0x014e:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x014e }
            throw r0     // Catch:{ Throwable -> 0x0151 }
        L_0x0151:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x0155:
            return r14
        L_0x0156:
            com.uc.crashsdk.a.g.a((java.io.Closeable) r13)
            com.uc.crashsdk.a.g.a((java.io.Closeable) r12)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.a(java.lang.String, long, java.lang.StringBuffer, java.lang.String, boolean, boolean, boolean, boolean, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, java.lang.String):boolean");
    }

    public static String i() {
        if (g.a(w)) {
            synchronized (y) {
                w = g.a(b.h(), x, true);
            }
        }
        return w;
    }

    public static void a(String str, boolean z2) {
        if (z2) {
            x = str;
            return;
        }
        synchronized (y) {
            w = str;
            com.uc.crashsdk.a.b.a(b.h(), str + "\n");
        }
    }

    public static void c(String str) {
        synchronized (z) {
            String k2 = b.k();
            com.uc.crashsdk.a.b.a(k2, str + "\n");
        }
    }

    static boolean d(String str) {
        if (ad) {
            return true;
        }
        try {
            return p(str);
        } catch (Throwable th) {
            g.a(th);
            return true;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x006c  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0077  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean p(java.lang.String r14) {
        /*
            java.lang.Object r0 = z
            monitor-enter(r0)
            java.util.Map<java.lang.String, java.lang.Integer> r1 = A     // Catch:{ all -> 0x0100 }
            r2 = 0
            if (r1 != 0) goto L_0x0018
            java.lang.String r1 = com.uc.crashsdk.b.k()     // Catch:{ all -> 0x0100 }
            java.lang.String r3 = "all:1"
            java.lang.String r1 = com.uc.crashsdk.a.g.a(r1, r3, r2)     // Catch:{ all -> 0x0100 }
            java.util.Map r1 = q(r1)     // Catch:{ all -> 0x0100 }
            A = r1     // Catch:{ all -> 0x0100 }
        L_0x0018:
            java.util.Map<java.lang.String, java.lang.Integer> r1 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r3 = "all"
            boolean r1 = r1.containsKey(r3)     // Catch:{ all -> 0x0100 }
            r3 = 1
            if (r1 == 0) goto L_0x0033
            java.util.Map<java.lang.String, java.lang.Integer> r14 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "all"
            java.lang.Object r14 = r14.get(r1)     // Catch:{ all -> 0x0100 }
            java.lang.Integer r14 = (java.lang.Integer) r14     // Catch:{ all -> 0x0100 }
        L_0x002d:
            int r14 = r14.intValue()     // Catch:{ all -> 0x0100 }
            goto L_0x00a4
        L_0x0033:
            java.util.Map<java.lang.String, java.lang.Integer> r1 = A     // Catch:{ all -> 0x0100 }
            boolean r1 = r1.containsKey(r14)     // Catch:{ all -> 0x0100 }
            if (r1 == 0) goto L_0x0044
            java.util.Map<java.lang.String, java.lang.Integer> r1 = A     // Catch:{ all -> 0x0100 }
            java.lang.Object r14 = r1.get(r14)     // Catch:{ all -> 0x0100 }
            java.lang.Integer r14 = (java.lang.Integer) r14     // Catch:{ all -> 0x0100 }
            goto L_0x002d
        L_0x0044:
            java.lang.String r1 = "java"
            boolean r1 = r1.equals(r14)     // Catch:{ all -> 0x0100 }
            if (r1 != 0) goto L_0x005f
            java.lang.String r1 = "jni"
            boolean r1 = r1.equals(r14)     // Catch:{ all -> 0x0100 }
            if (r1 != 0) goto L_0x005f
            java.lang.String r1 = "unexp"
            boolean r14 = r1.equals(r14)     // Catch:{ all -> 0x0100 }
            if (r14 == 0) goto L_0x005d
            goto L_0x005f
        L_0x005d:
            r14 = 0
            goto L_0x0060
        L_0x005f:
            r14 = 1
        L_0x0060:
            if (r14 == 0) goto L_0x0077
            java.util.Map<java.lang.String, java.lang.Integer> r1 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r4 = "crash"
            boolean r1 = r1.containsKey(r4)     // Catch:{ all -> 0x0100 }
            if (r1 == 0) goto L_0x0077
            java.util.Map<java.lang.String, java.lang.Integer> r14 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "crash"
            java.lang.Object r14 = r14.get(r1)     // Catch:{ all -> 0x0100 }
            java.lang.Integer r14 = (java.lang.Integer) r14     // Catch:{ all -> 0x0100 }
            goto L_0x002d
        L_0x0077:
            if (r14 != 0) goto L_0x008e
            java.util.Map<java.lang.String, java.lang.Integer> r14 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "nocrash"
            boolean r14 = r14.containsKey(r1)     // Catch:{ all -> 0x0100 }
            if (r14 == 0) goto L_0x008e
            java.util.Map<java.lang.String, java.lang.Integer> r14 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "nocrash"
            java.lang.Object r14 = r14.get(r1)     // Catch:{ all -> 0x0100 }
            java.lang.Integer r14 = (java.lang.Integer) r14     // Catch:{ all -> 0x0100 }
            goto L_0x002d
        L_0x008e:
            java.util.Map<java.lang.String, java.lang.Integer> r14 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "other"
            boolean r14 = r14.containsKey(r1)     // Catch:{ all -> 0x0100 }
            if (r14 == 0) goto L_0x00a3
            java.util.Map<java.lang.String, java.lang.Integer> r14 = A     // Catch:{ all -> 0x0100 }
            java.lang.String r1 = "other"
            java.lang.Object r14 = r14.get(r1)     // Catch:{ all -> 0x0100 }
            java.lang.Integer r14 = (java.lang.Integer) r14     // Catch:{ all -> 0x0100 }
            goto L_0x002d
        L_0x00a3:
            r14 = 1
        L_0x00a4:
            if (r14 == 0) goto L_0x00fd
            long r4 = (long) r14     // Catch:{ all -> 0x0100 }
            r6 = 1
            r8 = 0
            int r14 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r14 >= 0) goto L_0x00e9
            r10 = 30
            r12 = -2
            int r14 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r14 != 0) goto L_0x00ba
            r10 = 7
            goto L_0x00cb
        L_0x00ba:
            r12 = -3
            int r14 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r14 != 0) goto L_0x00c3
            r10 = 15
            goto L_0x00cb
        L_0x00c3:
            r12 = -4
            int r14 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r14 != 0) goto L_0x00cb
            r10 = 60
        L_0x00cb:
            long r4 = com.uc.crashsdk.a.b()     // Catch:{ all -> 0x0100 }
            int r14 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r14 != 0) goto L_0x00d6
            r4 = -1
            goto L_0x00e1
        L_0x00d6:
            long r12 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0100 }
            r14 = 0
            long r12 = r12 - r4
            r4 = 86400000(0x5265c00, double:4.2687272E-316)
            long r4 = r12 / r4
        L_0x00e1:
            int r14 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1))
            if (r14 > 0) goto L_0x00e7
            r4 = r6
            goto L_0x00e9
        L_0x00e7:
            r14 = 0
            long r4 = r4 - r10
        L_0x00e9:
            int r14 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r14 != 0) goto L_0x00ee
            goto L_0x00fe
        L_0x00ee:
            int r14 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r14 > 0) goto L_0x00f3
            goto L_0x00fe
        L_0x00f3:
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0100 }
            long r6 = r6 % r4
            int r14 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r14 != 0) goto L_0x00fd
            goto L_0x00fe
        L_0x00fd:
            r3 = 0
        L_0x00fe:
            monitor-exit(r0)     // Catch:{ all -> 0x0100 }
            return r3
        L_0x0100:
            r14 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0100 }
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.p(java.lang.String):boolean");
    }

    private static Map<String, Integer> q(String str) {
        int i2;
        HashMap hashMap = new HashMap();
        for (String split : str.split(SymbolExpUtil.SYMBOL_VERTICALBAR, 30)) {
            String[] split2 = split.split(":", 3);
            if (split2.length == 2) {
                String trim = split2[0].trim();
                if (!g.a(trim)) {
                    try {
                        i2 = Integer.parseInt(split2[1].trim(), 10);
                    } catch (Throwable th) {
                        g.a(th);
                        i2 = 1;
                    }
                    hashMap.put(trim, Integer.valueOf(i2));
                }
            }
        }
        return hashMap;
    }

    public static void j() {
        if (!ad) {
            f.a(1, new com.uc.crashsdk.a.e(411), 1000);
        }
    }

    static String k() {
        return a(new Date());
    }

    private static String a(Date date) {
        return String.format(Locale.US, "%d%02d%02d%02d%02d%02d", new Object[]{Integer.valueOf(date.getYear() + SecExceptionCode.SEC_ERROR_AVMP), Integer.valueOf(date.getMonth() + 1), Integer.valueOf(date.getDate()), Integer.valueOf(date.getHours()), Integer.valueOf(date.getMinutes()), Integer.valueOf(date.getSeconds())});
    }

    public static void l() {
        b = System.currentTimeMillis();
    }

    private static void r(String str) {
        if (h.r()) {
            try {
                M();
            } catch (Throwable th) {
                g.a(th);
            }
            if (str != null && !"".equals(str)) {
                try {
                    File file = new File(h.U());
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    com.uc.crashsdk.a.a.b("copy log to: " + file);
                    g.a(new File(str), file);
                } catch (Throwable th2) {
                    g.a(th2);
                }
            }
        }
    }

    private static void b(String str, String str2) {
        try {
            d.a(str, g(), str2);
        } catch (Throwable th) {
            g.a(th);
        }
    }

    public static void m() {
        String uuid;
        if (g.a(B)) {
            String str = null;
            try {
                File file = new File(h.S() + "unique");
                if (file.exists()) {
                    uuid = g.a(file, 48);
                    if (uuid != null) {
                        try {
                            if (uuid.length() == 36) {
                                str = uuid.replaceAll("[^0-9a-zA-Z-]", "-");
                            }
                        } catch (Exception e2) {
                            g.a((Throwable) e2);
                        } catch (Throwable th) {
                            th = th;
                            str = uuid;
                            g.a(th);
                            B = str;
                        }
                    }
                    str = uuid;
                }
                if (g.a(str)) {
                    uuid = UUID.randomUUID().toString();
                    if (!g.a(uuid)) {
                        g.a(file, uuid.getBytes());
                    }
                    str = uuid;
                }
            } catch (Throwable th2) {
                th = th2;
                g.a(th);
                B = str;
            }
            B = str;
        }
    }

    public static String n() {
        return B;
    }

    private static String s(String str) {
        return String.format("$^%s^$", new Object[]{str});
    }

    static void a(OutputStream outputStream, String str, String str2, int i2, boolean z2) {
        g = false;
        try {
            outputStream.write(String.format(Locale.US, "$^%s`%s`%d`%d^$", new Object[]{str, str2, Integer.valueOf(i2), Integer.valueOf(z2 ? 1 : 0)}).getBytes("UTF-8"));
        } catch (Throwable th) {
            g.a(th);
        }
        g = true;
        a(outputStream);
    }

    static void a(OutputStream outputStream, String str, String str2) {
        g = false;
        try {
            outputStream.write(String.format(Locale.US, "$^%s`%s^$", new Object[]{str, str2}).getBytes("UTF-8"));
        } catch (Throwable th) {
            g.a(th);
        }
        g = true;
    }

    /* compiled from: ProGuard */
    private static class a extends BroadcastReceiver {
        private a() {
        }

        /* synthetic */ a(byte b) {
            this();
        }

        public final void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                int unused = e.C = intent.getIntExtra("level", -1);
                int unused2 = e.D = intent.getIntExtra("scale", -1);
                int unused3 = e.E = intent.getIntExtra("voltage", -1);
                int unused4 = e.F = intent.getIntExtra("health", -1);
                int unused5 = e.G = intent.getIntExtra("plugged", -1);
                int unused6 = e.H = intent.getIntExtra("status", -1);
                int unused7 = e.I = intent.getIntExtra("temperature", -1);
                String unused8 = e.J = intent.getStringExtra("technology");
                if (e.A() >= 2) {
                    e.B();
                    int unused9 = e.M = 0;
                }
            } else if ("android.intent.action.BATTERY_LOW".equals(action) || "android.intent.action.BATTERY_OKAY".equals(action)) {
                boolean unused10 = e.K = "android.intent.action.BATTERY_LOW".equals(action);
                e.B();
            }
        }
    }

    private static StringBuilder K() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("level: ");
            sb.append(C);
            sb.append("\n");
            sb.append("scale: ");
            sb.append(D);
            sb.append("\n");
            String str = " (?)";
            switch (F) {
                case 1:
                    str = " (Unknown)";
                    break;
                case 2:
                    str = " (Good)";
                    break;
                case 3:
                    str = " (Overheat)";
                    break;
                case 4:
                    str = " (Dead)";
                    break;
                case 5:
                    str = " (Over voltage)";
                    break;
                case 6:
                    str = " (Unspecified failure)";
                    break;
                case 7:
                    str = " (Cold)";
                    break;
            }
            sb.append("health: ");
            sb.append(F);
            sb.append(str);
            sb.append("\n");
            String str2 = " (?)";
            int i2 = G;
            if (i2 != 4) {
                switch (i2) {
                    case 0:
                        str2 = " (None)";
                        break;
                    case 1:
                        str2 = " (AC charger)";
                        break;
                    case 2:
                        str2 = " (USB port)";
                        break;
                }
            } else {
                str2 = " (Wireless)";
            }
            sb.append("pluged: ");
            sb.append(G);
            sb.append(str2);
            sb.append("\n");
            String str3 = " (?)";
            switch (H) {
                case 1:
                    str3 = " (Unknown)";
                    break;
                case 2:
                    str3 = " (Charging)";
                    break;
                case 3:
                    str3 = " (Discharging)";
                    break;
                case 4:
                    str3 = " (Not charging)";
                    break;
                case 5:
                    str3 = " (Full)";
                    break;
            }
            sb.append("status: ");
            sb.append(H);
            sb.append(str3);
            sb.append("\n");
            sb.append("voltage: ");
            sb.append(E);
            sb.append("\n");
            sb.append("temperature: ");
            sb.append(I);
            sb.append("\n");
            sb.append("technology: ");
            sb.append(J);
            sb.append("\n");
            sb.append("battery low: ");
            sb.append(K);
            sb.append("\n");
            return sb;
        } catch (Throwable th) {
            g.a(th);
            return null;
        }
    }

    private static void L() {
        if (b.c && L && a.c) {
            L = false;
            if (!f.b(N)) {
                f.a(0, N, 2000);
            }
        }
    }

    public static void a(Context context) {
        if (h.J()) {
            try {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
                intentFilter.addAction("android.intent.action.BATTERY_LOW");
                intentFilter.addAction("android.intent.action.BATTERY_OKAY");
                context.registerReceiver(O, intentFilter);
                P = true;
            } catch (Throwable th) {
                g.a(th);
            }
        }
    }

    static void c(boolean z2) {
        boolean z3 = true;
        if (!P ? !z2 || !h.J() : z2 && h.J()) {
            z3 = false;
        }
        if (z3) {
            if (f.b(Q)) {
                f.a(Q);
            }
            f.a(0, Q, TBToast.Duration.MEDIUM);
        }
    }

    public static void o() {
        R = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new e());
    }

    public static void p() {
        Thread.setDefaultUncaughtExceptionHandler(R);
    }

    public void uncaughtException(Thread thread, Throwable th) {
        a(thread, th, false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:118:0x0167, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:120:?, code lost:
        com.uc.crashsdk.a.g.a(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x019c, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:133:0x019e, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:134:0x019f, code lost:
        r12 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:?, code lost:
        com.uc.crashsdk.a.a.c(com.alibaba.android.umbrella.link.export.UMLLCons.FEATURE_TYPE_DEBUG, "get java log name failed: " + r0);
        a(r0);
        com.uc.crashsdk.a.a.c(com.alibaba.android.umbrella.link.export.UMLLCons.FEATURE_TYPE_DEBUG, "original exception is: " + r3);
        a(r20);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:138:0x01cc, code lost:
        r13 = null;
        r14 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:147:0x01e2, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:149:?, code lost:
        com.uc.crashsdk.a.g.a(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:151:0x01f3, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:153:?, code lost:
        com.uc.crashsdk.a.g.a(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:236:0x02f4, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:395:0x0506, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:396:0x0509, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:397:0x050a, code lost:
        r8 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:400:0x0511, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:401:0x0512, code lost:
        r8 = r0;
        r10 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:410:0x0535, code lost:
        com.uc.crashsdk.a.a.c(com.alibaba.android.umbrella.link.export.UMLLCons.FEATURE_TYPE_DEBUG, "original exception is: " + r3);
        a(r20);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:422:?, code lost:
        a(true, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:423:0x0565, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:424:0x0567, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:425:0x0568, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:426:0x056a, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:427:0x056b, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:428:0x056c, code lost:
        com.uc.crashsdk.a.g.a(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:433:0x057b, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:451:0x05bd, code lost:
        android.os.Process.killProcess(android.os.Process.myPid());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:466:?, code lost:
        a(true, false);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:467:0x05e2, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:468:0x05e4, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:469:0x05e5, code lost:
        r4 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:470:0x05e7, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:471:0x05e8, code lost:
        r4 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:472:0x05e9, code lost:
        com.uc.crashsdk.a.g.a(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:477:0x05f8, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:495:0x063a, code lost:
        android.os.Process.killProcess(android.os.Process.myPid());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:514:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:515:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00bc, code lost:
        r0 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00bd, code lost:
        r10 = 0;
        r12 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00c1, code lost:
        r0 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:108:0x013c  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x01d0 A[SYNTHETIC, Splitter:B:140:0x01d0] */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x0223 A[Catch:{ Throwable -> 0x0250 }] */
    /* JADX WARNING: Removed duplicated region for block: B:190:0x0263  */
    /* JADX WARNING: Removed duplicated region for block: B:213:0x029d A[Catch:{ Throwable -> 0x02ca }] */
    /* JADX WARNING: Removed duplicated region for block: B:230:0x02dd  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x02e8  */
    /* JADX WARNING: Removed duplicated region for block: B:272:0x0340 A[Catch:{ Throwable -> 0x036d }] */
    /* JADX WARNING: Removed duplicated region for block: B:289:0x0380  */
    /* JADX WARNING: Removed duplicated region for block: B:317:0x03c0 A[Catch:{ Throwable -> 0x03ed }] */
    /* JADX WARNING: Removed duplicated region for block: B:334:0x0400  */
    /* JADX WARNING: Removed duplicated region for block: B:345:0x043a A[Catch:{ Throwable -> 0x0509, all -> 0x0506 }] */
    /* JADX WARNING: Removed duplicated region for block: B:360:0x049b A[SYNTHETIC, Splitter:B:360:0x049b] */
    /* JADX WARNING: Removed duplicated region for block: B:376:0x04bc A[Catch:{ Throwable -> 0x04e9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:394:0x04fe  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x0506 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:337:0x040a] */
    /* JADX WARNING: Removed duplicated region for block: B:400:0x0511 A[ExcHandler: all (r0v39 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:67:0x00c4] */
    /* JADX WARNING: Removed duplicated region for block: B:410:0x0535 A[Catch:{ all -> 0x05c5 }] */
    /* JADX WARNING: Removed duplicated region for block: B:417:0x055a A[SYNTHETIC, Splitter:B:417:0x055a] */
    /* JADX WARNING: Removed duplicated region for block: B:433:0x057b A[Catch:{ Throwable -> 0x05a8 }] */
    /* JADX WARNING: Removed duplicated region for block: B:451:0x05bd  */
    /* JADX WARNING: Removed duplicated region for block: B:461:0x05d7 A[SYNTHETIC, Splitter:B:461:0x05d7] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x0074 A[Catch:{ Throwable -> 0x00a1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:477:0x05f8 A[Catch:{ Throwable -> 0x0625 }] */
    /* JADX WARNING: Removed duplicated region for block: B:495:0x063a  */
    /* JADX WARNING: Removed duplicated region for block: B:501:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:503:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:505:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:507:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:509:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:511:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:513:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:515:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00bc A[ExcHandler: all (th java.lang.Throwable), Splitter:B:13:0x0020] */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x00fc A[Catch:{ Throwable -> 0x0129 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(java.lang.Thread r19, java.lang.Throwable r20, boolean r21) {
        /*
            r18 = this;
            r1 = r18
            r2 = r19
            r3 = r20
            boolean r0 = ad
            r5 = 0
            r6 = 1
            if (r0 == 0) goto L_0x0010
            boolean r0 = com.uc.crashsdk.b.d
            if (r0 != 0) goto L_0x0016
        L_0x0010:
            boolean r0 = com.uc.crashsdk.b.D()
            if (r0 == 0) goto L_0x0018
        L_0x0016:
            r7 = 1
            goto L_0x0019
        L_0x0018:
            r7 = 0
        L_0x0019:
            r8 = 0
            boolean r0 = c     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            r10 = 4
            if (r0 == 0) goto L_0x00c4
            int r0 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            if (r0 <= 0) goto L_0x00c4
            java.lang.String r0 = "DEBUG"
            java.lang.String r11 = "another thread is generating java report!"
            com.uc.crashsdk.a.a.c(r0, r11)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            java.lang.String r0 = "DEBUG"
            java.lang.String r11 = "current thread exception is:"
            com.uc.crashsdk.a.a.c(r0, r11)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            a((java.lang.Throwable) r20)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            r11 = 0
        L_0x0038:
            boolean r0 = T     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            if (r0 != 0) goto L_0x004a
            r12 = 1000(0x3e8, double:4.94E-321)
            java.lang.Thread.sleep(r12)     // Catch:{ Throwable -> 0x0042, all -> 0x00bc }
            goto L_0x0047
        L_0x0042:
            r0 = move-exception
            r12 = r0
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r12)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
        L_0x0047:
            int r11 = r11 + r6
            if (r11 < r10) goto L_0x0038
        L_0x004a:
            int r0 = android.os.Process.myPid()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            android.os.Process.killProcess(r0)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            if (r21 == 0) goto L_0x0069
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x0063 }
            if (r0 == 0) goto L_0x0069
            if (r7 != 0) goto L_0x0069
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x0060 }
            r4 = 1
            goto L_0x006a
        L_0x0060:
            r0 = move-exception
            r4 = 1
            goto L_0x0065
        L_0x0063:
            r0 = move-exception
            r4 = 0
        L_0x0065:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x006a
        L_0x0069:
            r4 = 0
        L_0x006a:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x00a1 }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x00a1 }
            if (r8 != 0) goto L_0x0075
            r0 = 1
        L_0x0075:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00a1 }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x00a1 }
            r8.append(r0)     // Catch:{ Throwable -> 0x00a1 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x00a1 }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x00a1 }
            if (r0 == 0) goto L_0x0091
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x00a1 }
            if (r0 == 0) goto L_0x0091
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x00a1 }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x00a1 }
        L_0x0091:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x00a1 }
            if (r0 == 0) goto L_0x00a5
            if (r7 != 0) goto L_0x00a5
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x00a1 }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x00a1 }
            goto L_0x00a5
        L_0x00a1:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x00a5:
            if (r4 != 0) goto L_0x00ac
            if (r7 != 0) goto L_0x00ac
            b((boolean) r5)
        L_0x00ac:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x00bb
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x00bb:
            return
        L_0x00bc:
            r0 = move-exception
            r10 = r8
            r12 = 0
            goto L_0x05c7
        L_0x00c1:
            r0 = move-exception
            goto L_0x051b
        L_0x00c4:
            c = r6     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            S = r3     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            if (r7 != 0) goto L_0x0144
            boolean r0 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            if (r0 != 0) goto L_0x0144
            java.lang.String r0 = "DEBUG"
            java.lang.String r10 = com.uc.crashsdk.a.d.b()     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            com.uc.crashsdk.a.a.b(r0, r10)     // Catch:{ Throwable -> 0x00c1, all -> 0x00bc }
            if (r21 == 0) goto L_0x00f1
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x00eb }
            if (r0 == 0) goto L_0x00f1
            if (r7 != 0) goto L_0x00f1
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x00e8 }
            r4 = 1
            goto L_0x00f2
        L_0x00e8:
            r0 = move-exception
            r4 = 1
            goto L_0x00ed
        L_0x00eb:
            r0 = move-exception
            r4 = 0
        L_0x00ed:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x00f2
        L_0x00f1:
            r4 = 0
        L_0x00f2:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x0129 }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x0129 }
            if (r8 != 0) goto L_0x00fd
            r0 = 1
        L_0x00fd:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0129 }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0129 }
            r8.append(r0)     // Catch:{ Throwable -> 0x0129 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0129 }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x0129 }
            if (r0 == 0) goto L_0x0119
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x0129 }
            if (r0 == 0) goto L_0x0119
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x0129 }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x0129 }
        L_0x0119:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x0129 }
            if (r0 == 0) goto L_0x012d
            if (r7 != 0) goto L_0x012d
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x0129 }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x0129 }
            goto L_0x012d
        L_0x0129:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x012d:
            if (r4 != 0) goto L_0x0134
            if (r7 != 0) goto L_0x0134
            b((boolean) r5)
        L_0x0134:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x0143
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x0143:
            return
        L_0x0144:
            java.lang.String r0 = "DEBUG"
            java.lang.String r11 = "begin to generate java report"
            com.uc.crashsdk.a.a.c(r0, r11)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            java.util.List<java.io.FileInputStream> r0 = r1.e     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
        L_0x0151:
            boolean r11 = r0.hasNext()     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            if (r11 == 0) goto L_0x0161
            java.lang.Object r11 = r0.next()     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            java.io.FileInputStream r11 = (java.io.FileInputStream) r11     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            com.uc.crashsdk.a.g.a((java.io.Closeable) r11)     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            goto L_0x0151
        L_0x0161:
            java.util.List<java.io.FileInputStream> r0 = r1.e     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            r0.clear()     // Catch:{ Throwable -> 0x0167, all -> 0x00bc }
            goto L_0x016b
        L_0x0167:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
        L_0x016b:
            r11 = 0
            boolean r12 = com.uc.crashsdk.h.u()     // Catch:{ Throwable -> 0x019e, all -> 0x00bc }
            java.lang.String r0 = com.uc.crashsdk.h.h()     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            if (r0 == 0) goto L_0x017e
            java.lang.String r13 = ""
            boolean r13 = r0.equals(r13)     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            if (r13 == 0) goto L_0x0186
        L_0x017e:
            java.lang.String r0 = H()     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            java.lang.String r0 = k(r0)     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
        L_0x0186:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            r13.<init>()     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            java.lang.String r14 = com.uc.crashsdk.h.T()     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            r13.append(r14)     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            r13.append(r0)     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            java.lang.String r0 = r13.toString()     // Catch:{ Throwable -> 0x019c, all -> 0x00bc }
            r13 = r0
            r14 = 0
            goto L_0x01ce
        L_0x019c:
            r0 = move-exception
            goto L_0x01a0
        L_0x019e:
            r0 = move-exception
            r12 = 0
        L_0x01a0:
            java.lang.String r13 = "DEBUG"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            java.lang.String r15 = "get java log name failed: "
            r14.<init>(r15)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            r14.append(r0)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            java.lang.String r14 = r14.toString()     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            com.uc.crashsdk.a.a.c(r13, r14)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            a((java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            java.lang.String r0 = "DEBUG"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            java.lang.String r14 = "original exception is: "
            r13.<init>(r14)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            r13.append(r3)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            java.lang.String r13 = r13.toString()     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            com.uc.crashsdk.a.a.c(r0, r13)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            a((java.lang.Throwable) r20)     // Catch:{ Throwable -> 0x0518, all -> 0x0511 }
            r13 = r11
            r14 = 1
        L_0x01ce:
            if (r7 != 0) goto L_0x02e8
            com.uc.crashsdk.h.b()     // Catch:{ Throwable -> 0x01f3, all -> 0x00bc }
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x01e2, all -> 0x00bc }
            if (r0 == 0) goto L_0x01de
            r0 = 3
            com.uc.crashsdk.f.a((int) r0)     // Catch:{ Throwable -> 0x01e2, all -> 0x00bc }
            goto L_0x01e6
        L_0x01de:
            com.uc.crashsdk.f.a((int) r10)     // Catch:{ Throwable -> 0x01e2, all -> 0x00bc }
            goto L_0x01e6
        L_0x01e2:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x01f3, all -> 0x00bc }
        L_0x01e6:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x01f3, all -> 0x00bc }
            java.lang.String r10 = com.uc.crashsdk.b.b()     // Catch:{ Throwable -> 0x01f3, all -> 0x00bc }
            r0.<init>(r10)     // Catch:{ Throwable -> 0x01f3, all -> 0x00bc }
            r0.createNewFile()     // Catch:{ Throwable -> 0x01f3, all -> 0x00bc }
            goto L_0x01f7
        L_0x01f3:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)     // Catch:{ Throwable -> 0x02f4, all -> 0x00bc }
        L_0x01f7:
            if (r12 == 0) goto L_0x026b
            java.lang.String r0 = "DEBUG"
            java.lang.String r10 = "omit java crash"
            com.uc.crashsdk.a.a.c(r0, r10)     // Catch:{ Throwable -> 0x02f4, all -> 0x00bc }
            if (r21 == 0) goto L_0x0218
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x0212 }
            if (r0 == 0) goto L_0x0218
            if (r7 != 0) goto L_0x0218
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x020f }
            r4 = 1
            goto L_0x0219
        L_0x020f:
            r0 = move-exception
            r4 = 1
            goto L_0x0214
        L_0x0212:
            r0 = move-exception
            r4 = 0
        L_0x0214:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x0219
        L_0x0218:
            r4 = 0
        L_0x0219:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x0250 }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x0250 }
            if (r8 != 0) goto L_0x0224
            r0 = 1
        L_0x0224:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0250 }
            r8.append(r0)     // Catch:{ Throwable -> 0x0250 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x0250 }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x0250 }
            if (r0 == 0) goto L_0x0240
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x0250 }
            if (r0 == 0) goto L_0x0240
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x0250 }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x0250 }
        L_0x0240:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x0250 }
            if (r0 == 0) goto L_0x0254
            if (r7 != 0) goto L_0x0254
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x0250 }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x0250 }
            goto L_0x0254
        L_0x0250:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x0254:
            if (r4 != 0) goto L_0x025b
            if (r7 != 0) goto L_0x025b
            b((boolean) r5)
        L_0x025b:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x026a
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x026a:
            return
        L_0x026b:
            java.lang.String r0 = "java"
            boolean r0 = d((java.lang.String) r0)     // Catch:{ Throwable -> 0x02f4, all -> 0x00bc }
            if (r0 != 0) goto L_0x02e5
            java.lang.String r0 = "DEBUG"
            java.lang.String r10 = "java log sample miss"
            com.uc.crashsdk.a.a.c(r0, r10)     // Catch:{ Throwable -> 0x02f4, all -> 0x00bc }
            if (r21 == 0) goto L_0x0292
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x028c }
            if (r0 == 0) goto L_0x0292
            if (r7 != 0) goto L_0x0292
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x0289 }
            r4 = 1
            goto L_0x0293
        L_0x0289:
            r0 = move-exception
            r4 = 1
            goto L_0x028e
        L_0x028c:
            r0 = move-exception
            r4 = 0
        L_0x028e:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x0293
        L_0x0292:
            r4 = 0
        L_0x0293:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x02ca }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x02ca }
            if (r8 != 0) goto L_0x029e
            r0 = 1
        L_0x029e:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02ca }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x02ca }
            r8.append(r0)     // Catch:{ Throwable -> 0x02ca }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x02ca }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x02ca }
            if (r0 == 0) goto L_0x02ba
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x02ca }
            if (r0 == 0) goto L_0x02ba
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x02ca }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x02ca }
        L_0x02ba:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x02ca }
            if (r0 == 0) goto L_0x02ce
            if (r7 != 0) goto L_0x02ce
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x02ca }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x02ca }
            goto L_0x02ce
        L_0x02ca:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x02ce:
            if (r4 != 0) goto L_0x02d5
            if (r7 != 0) goto L_0x02d5
            b((boolean) r5)
        L_0x02d5:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x02e4
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x02e4:
            return
        L_0x02e5:
            r10 = r8
            goto L_0x0408
        L_0x02e8:
            if (r12 == 0) goto L_0x02f8
            java.lang.String r13 = "omit"
            java.lang.String r0 = "DEBUG"
            java.lang.String r10 = "omit java crash"
            com.uc.crashsdk.a.a.c(r0, r10)     // Catch:{ Throwable -> 0x02f4, all -> 0x00bc }
            goto L_0x02f8
        L_0x02f4:
            r0 = move-exception
        L_0x02f5:
            r12 = 0
            goto L_0x051d
        L_0x02f8:
            boolean r0 = com.uc.crashsdk.b.d     // Catch:{ Throwable -> 0x050c, all -> 0x0511 }
            if (r0 == 0) goto L_0x0303
            java.lang.String r0 = "java"
            long r10 = com.uc.crashsdk.JNIBridge.nativeClientCreateConnection(r13, r0, r11, r5)     // Catch:{ Throwable -> 0x02f4, all -> 0x00bc }
            goto L_0x0304
        L_0x0303:
            r10 = r8
        L_0x0304:
            int r0 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r0 != 0) goto L_0x0390
            java.lang.String r0 = "DEBUG"
            java.lang.String r12 = "skip java crash:"
            com.uc.crashsdk.a.a.c(r0, r12)     // Catch:{ Throwable -> 0x038c, all -> 0x0388 }
            a((java.lang.Throwable) r20)     // Catch:{ Throwable -> 0x038c, all -> 0x0388 }
            int r0 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r0 == 0) goto L_0x031d
            boolean r0 = com.uc.crashsdk.b.d
            if (r0 == 0) goto L_0x031d
            com.uc.crashsdk.JNIBridge.nativeClientCloseConnection(r10)
        L_0x031d:
            if (r21 == 0) goto L_0x0335
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x032f }
            if (r0 == 0) goto L_0x0335
            if (r7 != 0) goto L_0x0335
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x032c }
            r4 = 1
            goto L_0x0336
        L_0x032c:
            r0 = move-exception
            r4 = 1
            goto L_0x0331
        L_0x032f:
            r0 = move-exception
            r4 = 0
        L_0x0331:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x0336
        L_0x0335:
            r4 = 0
        L_0x0336:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x036d }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x036d }
            if (r8 != 0) goto L_0x0341
            r0 = 1
        L_0x0341:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x036d }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x036d }
            r8.append(r0)     // Catch:{ Throwable -> 0x036d }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x036d }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x036d }
            if (r0 == 0) goto L_0x035d
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x036d }
            if (r0 == 0) goto L_0x035d
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x036d }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x036d }
        L_0x035d:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x036d }
            if (r0 == 0) goto L_0x0371
            if (r7 != 0) goto L_0x0371
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x036d }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x036d }
            goto L_0x0371
        L_0x036d:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x0371:
            if (r4 != 0) goto L_0x0378
            if (r7 != 0) goto L_0x0378
            b((boolean) r5)
        L_0x0378:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x0387
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x0387:
            return
        L_0x0388:
            r0 = move-exception
            r8 = r0
            goto L_0x0515
        L_0x038c:
            r0 = move-exception
            r8 = r10
            goto L_0x02f5
        L_0x0390:
            if (r12 == 0) goto L_0x0408
            int r0 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r0 == 0) goto L_0x039d
            boolean r0 = com.uc.crashsdk.b.d
            if (r0 == 0) goto L_0x039d
            com.uc.crashsdk.JNIBridge.nativeClientCloseConnection(r10)
        L_0x039d:
            if (r21 == 0) goto L_0x03b5
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x03af }
            if (r0 == 0) goto L_0x03b5
            if (r7 != 0) goto L_0x03b5
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x03ac }
            r4 = 1
            goto L_0x03b6
        L_0x03ac:
            r0 = move-exception
            r4 = 1
            goto L_0x03b1
        L_0x03af:
            r0 = move-exception
            r4 = 0
        L_0x03b1:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x03b6
        L_0x03b5:
            r4 = 0
        L_0x03b6:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x03ed }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x03ed }
            if (r8 != 0) goto L_0x03c1
            r0 = 1
        L_0x03c1:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03ed }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x03ed }
            r8.append(r0)     // Catch:{ Throwable -> 0x03ed }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x03ed }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x03ed }
            if (r0 == 0) goto L_0x03dd
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x03ed }
            if (r0 == 0) goto L_0x03dd
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x03ed }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x03ed }
        L_0x03dd:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x03ed }
            if (r0 == 0) goto L_0x03f1
            if (r7 != 0) goto L_0x03f1
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x03ed }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x03ed }
            goto L_0x03f1
        L_0x03ed:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x03f1:
            if (r4 != 0) goto L_0x03f8
            if (r7 != 0) goto L_0x03f8
            b((boolean) r5)
        L_0x03f8:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x0407
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x0407:
            return
        L_0x0408:
            boolean r12 = r3 instanceof java.lang.OutOfMemoryError     // Catch:{ Throwable -> 0x038c, all -> 0x0388 }
            a(r3, r13, r10, r12)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r0 = "DEBUG"
            java.lang.String r15 = "generate java report finished"
            com.uc.crashsdk.a.a.c(r0, r15)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            boolean r0 = com.uc.crashsdk.b.D()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            if (r0 != 0) goto L_0x048c
            if (r12 == 0) goto L_0x048c
            boolean r0 = com.uc.crashsdk.h.k()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            if (r0 == 0) goto L_0x048c
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            r0.<init>(r13)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r0 = r0.getName()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r13 = com.uc.crashsdk.h.U()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.io.File r15 = new java.io.File     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            r15.<init>(r13)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            boolean r16 = r15.exists()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            if (r16 != 0) goto L_0x043d
            r15.mkdirs()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
        L_0x043d:
            java.util.Locale r15 = java.util.Locale.US     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r8 = "%s%s.hprof"
            r9 = 2
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            r9[r5] = r13     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            r9[r6] = r0     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r0 = java.lang.String.format(r15, r8, r9)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r8 = "DEBUG"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r13 = "begin dump hprof: "
            r9.<init>(r13)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            r9.append(r0)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            com.uc.crashsdk.a.a.c(r8, r9)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            android.os.Debug.dumpHprofData(r0)     // Catch:{ Throwable -> 0x0467, all -> 0x0506 }
            goto L_0x046c
        L_0x0467:
            r0 = move-exception
            r13 = r0
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r13)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
        L_0x046c:
            java.lang.String r0 = "DEBUG"
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r15 = "end dump hprof, use "
            r13.<init>(r15)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            long r15 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            r17 = 0
            long r8 = r15 - r8
            r13.append(r8)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r8 = " ms"
            r13.append(r8)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            java.lang.String r8 = r13.toString()     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
            com.uc.crashsdk.a.a.c(r0, r8)     // Catch:{ Throwable -> 0x0509, all -> 0x0506 }
        L_0x048c:
            r8 = 0
            int r0 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1))
            if (r0 == 0) goto L_0x0499
            boolean r0 = com.uc.crashsdk.b.d
            if (r0 == 0) goto L_0x0499
            com.uc.crashsdk.JNIBridge.nativeClientCloseConnection(r10)
        L_0x0499:
            if (r21 == 0) goto L_0x04b1
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x04ab }
            if (r0 == 0) goto L_0x04b1
            if (r7 != 0) goto L_0x04b1
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x04a8 }
            r4 = 1
            goto L_0x04b2
        L_0x04a8:
            r0 = move-exception
            r4 = 1
            goto L_0x04ad
        L_0x04ab:
            r0 = move-exception
            r4 = 0
        L_0x04ad:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x04b2
        L_0x04b1:
            r4 = 0
        L_0x04b2:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x04e9 }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x04e9 }
            if (r8 != 0) goto L_0x04bd
            r0 = 1
        L_0x04bd:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04e9 }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x04e9 }
            r8.append(r0)     // Catch:{ Throwable -> 0x04e9 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x04e9 }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x04e9 }
            if (r0 == 0) goto L_0x04d9
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x04e9 }
            if (r0 == 0) goto L_0x04d9
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x04e9 }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x04e9 }
        L_0x04d9:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x04e9 }
            if (r0 == 0) goto L_0x04ed
            if (r7 != 0) goto L_0x04ed
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x04e9 }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x04e9 }
            goto L_0x04ed
        L_0x04e9:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x04ed:
            if (r4 != 0) goto L_0x04f6
            if (r7 != 0) goto L_0x04f6
            if (r12 != 0) goto L_0x04f6
            b((boolean) r5)
        L_0x04f6:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x0505
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x0505:
            return
        L_0x0506:
            r0 = move-exception
            goto L_0x05c7
        L_0x0509:
            r0 = move-exception
            r8 = r10
            goto L_0x051d
        L_0x050c:
            r0 = move-exception
            r8 = 0
            goto L_0x02f5
        L_0x0511:
            r0 = move-exception
            r8 = r0
            r10 = 0
        L_0x0515:
            r12 = 0
            goto L_0x05c8
        L_0x0518:
            r0 = move-exception
            r8 = 0
        L_0x051b:
            r12 = 0
            r14 = 0
        L_0x051d:
            java.lang.String r10 = "DEBUG"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c5 }
            java.lang.String r13 = "exception occurs while java log: "
            r11.<init>(r13)     // Catch:{ all -> 0x05c5 }
            r11.append(r0)     // Catch:{ all -> 0x05c5 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x05c5 }
            com.uc.crashsdk.a.a.c(r10, r11)     // Catch:{ all -> 0x05c5 }
            a((java.lang.Throwable) r0)     // Catch:{ all -> 0x05c5 }
            if (r14 != 0) goto L_0x054b
            java.lang.String r0 = "DEBUG"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x05c5 }
            java.lang.String r11 = "original exception is: "
            r10.<init>(r11)     // Catch:{ all -> 0x05c5 }
            r10.append(r3)     // Catch:{ all -> 0x05c5 }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x05c5 }
            com.uc.crashsdk.a.a.c(r0, r10)     // Catch:{ all -> 0x05c5 }
            a((java.lang.Throwable) r20)     // Catch:{ all -> 0x05c5 }
        L_0x054b:
            r10 = 0
            int r0 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
            if (r0 == 0) goto L_0x0558
            boolean r0 = com.uc.crashsdk.b.d
            if (r0 == 0) goto L_0x0558
            com.uc.crashsdk.JNIBridge.nativeClientCloseConnection(r8)
        L_0x0558:
            if (r21 == 0) goto L_0x0570
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x056a }
            if (r0 == 0) goto L_0x0570
            if (r7 != 0) goto L_0x0570
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x0567 }
            r4 = 1
            goto L_0x0571
        L_0x0567:
            r0 = move-exception
            r4 = 1
            goto L_0x056c
        L_0x056a:
            r0 = move-exception
            r4 = 0
        L_0x056c:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x0571
        L_0x0570:
            r4 = 0
        L_0x0571:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x05a8 }
            boolean r8 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x05a8 }
            if (r8 != 0) goto L_0x057c
            r0 = 1
        L_0x057c:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x05a8 }
            java.lang.String r9 = "Call java default handler: "
            r8.<init>(r9)     // Catch:{ Throwable -> 0x05a8 }
            r8.append(r0)     // Catch:{ Throwable -> 0x05a8 }
            java.lang.String r8 = r8.toString()     // Catch:{ Throwable -> 0x05a8 }
            com.uc.crashsdk.a.a.b(r8)     // Catch:{ Throwable -> 0x05a8 }
            if (r0 == 0) goto L_0x0598
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x05a8 }
            if (r0 == 0) goto L_0x0598
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x05a8 }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x05a8 }
        L_0x0598:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x05a8 }
            if (r0 == 0) goto L_0x05ac
            if (r7 != 0) goto L_0x05ac
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x05a8 }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x05a8 }
            goto L_0x05ac
        L_0x05a8:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x05ac:
            if (r4 != 0) goto L_0x05b5
            if (r7 != 0) goto L_0x05b5
            if (r12 != 0) goto L_0x05b5
            b((boolean) r5)
        L_0x05b5:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x05c4
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x05c4:
            return
        L_0x05c5:
            r0 = move-exception
            r10 = r8
        L_0x05c7:
            r8 = r0
        L_0x05c8:
            r13 = 0
            int r0 = (r10 > r13 ? 1 : (r10 == r13 ? 0 : -1))
            if (r0 == 0) goto L_0x05d5
            boolean r0 = com.uc.crashsdk.b.d
            if (r0 == 0) goto L_0x05d5
            com.uc.crashsdk.JNIBridge.nativeClientCloseConnection(r10)
        L_0x05d5:
            if (r21 == 0) goto L_0x05ed
            boolean r0 = com.uc.crashsdk.h.t()     // Catch:{ Throwable -> 0x05e7 }
            if (r0 == 0) goto L_0x05ed
            if (r7 != 0) goto L_0x05ed
            a((boolean) r6, (boolean) r5)     // Catch:{ Throwable -> 0x05e4 }
            r4 = 1
            goto L_0x05ee
        L_0x05e4:
            r0 = move-exception
            r4 = 1
            goto L_0x05e9
        L_0x05e7:
            r0 = move-exception
            r4 = 0
        L_0x05e9:
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
            goto L_0x05ee
        L_0x05ed:
            r4 = 0
        L_0x05ee:
            boolean r0 = com.uc.crashsdk.h.j()     // Catch:{ Throwable -> 0x0625 }
            boolean r9 = com.uc.crashsdk.a.d.d()     // Catch:{ Throwable -> 0x0625 }
            if (r9 != 0) goto L_0x05f9
            r0 = 1
        L_0x05f9:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0625 }
            java.lang.String r10 = "Call java default handler: "
            r9.<init>(r10)     // Catch:{ Throwable -> 0x0625 }
            r9.append(r0)     // Catch:{ Throwable -> 0x0625 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0625 }
            com.uc.crashsdk.a.a.b(r9)     // Catch:{ Throwable -> 0x0625 }
            if (r0 == 0) goto L_0x0615
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x0625 }
            if (r0 == 0) goto L_0x0615
            java.lang.Thread$UncaughtExceptionHandler r0 = R     // Catch:{ Throwable -> 0x0625 }
            r0.uncaughtException(r2, r3)     // Catch:{ Throwable -> 0x0625 }
        L_0x0615:
            boolean r0 = com.uc.crashsdk.b.y()     // Catch:{ Throwable -> 0x0625 }
            if (r0 == 0) goto L_0x0629
            if (r7 != 0) goto L_0x0629
            android.content.Context r0 = com.uc.crashsdk.a.g.a()     // Catch:{ Throwable -> 0x0625 }
            com.uc.crashsdk.i.a(r0)     // Catch:{ Throwable -> 0x0625 }
            goto L_0x0629
        L_0x0625:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x0629:
            if (r4 != 0) goto L_0x0632
            if (r7 != 0) goto L_0x0632
            if (r12 != 0) goto L_0x0632
            b((boolean) r5)
        L_0x0632:
            T = r6
            int r0 = android.os.Process.myPid()
            if (r0 <= 0) goto L_0x0641
            int r0 = android.os.Process.myPid()
            android.os.Process.killProcess(r0)
        L_0x0641:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.a(java.lang.Thread, java.lang.Throwable, boolean):void");
    }

    private static void a(Throwable th) {
        try {
            com.uc.crashsdk.a.a.c(UMLLCons.FEATURE_TYPE_DEBUG, a(th.getStackTrace(), (String) null).toString());
        } catch (Throwable unused) {
        }
    }

    public static Throwable q() {
        return S;
    }

    public static void r() {
        long p2 = (long) h.p();
        if (p2 >= 0) {
            boolean z2 = b.B() == 5;
            f.a(0, (Runnable) new com.uc.crashsdk.a.e(401));
            if (z2) {
                V = new com.uc.crashsdk.a.e(402);
                f.a(0, V, p2);
            }
        }
    }

    static void s() {
        if (b.c && a.c && !f.b(X)) {
            f.a(0, X, 1000);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0017, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean t() {
        /*
            java.lang.Object r0 = W
            monitor-enter(r0)
            java.lang.Runnable r1 = V     // Catch:{ all -> 0x0019 }
            if (r1 == 0) goto L_0x0016
            boolean r1 = U     // Catch:{ all -> 0x0019 }
            if (r1 != 0) goto L_0x0016
            java.lang.Runnable r1 = V     // Catch:{ all -> 0x0019 }
            com.uc.crashsdk.a.f.a(r1)     // Catch:{ all -> 0x0019 }
            r1 = 0
            V = r1     // Catch:{ all -> 0x0019 }
            r1 = 1
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            return r1
        L_0x0016:
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            r0 = 0
            return r0
        L_0x0019:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.e.t():boolean");
    }

    public static boolean e(String str) {
        File file = new File(str);
        boolean z2 = false;
        try {
            if (file.exists()) {
                System.load(file.getAbsolutePath());
                z2 = true;
            }
        } catch (Throwable th) {
            g.a(th);
        }
        if (z2) {
            return z2;
        }
        try {
            if (f(file.getName())) {
                return true;
            }
            return z2;
        } catch (Throwable th2) {
            g.a(th2);
            return z2;
        }
    }

    public static boolean f(String str) {
        try {
            if (!g.b(str) || !str.startsWith("lib") || !str.endsWith(".so")) {
                return false;
            }
            System.loadLibrary(str.substring(3, str.length() - 3));
            return true;
        } catch (Throwable th) {
            g.a(th);
            return false;
        }
    }

    public static void b(int i2) {
        f.a(0, new com.uc.crashsdk.a.e(410), (long) (i2 * 1000));
    }

    private static void M() {
        int i2;
        String U2 = h.U();
        File file = new File(U2);
        if (file.isDirectory()) {
            try {
                File[] listFiles = file.listFiles();
                if (listFiles != null && listFiles.length > 150) {
                    Arrays.sort(listFiles, new c((byte) 0));
                    int length = listFiles.length - 150;
                    int i3 = length < 0 ? 0 : length;
                    long currentTimeMillis = System.currentTimeMillis();
                    int i4 = 0;
                    int i5 = 0;
                    i2 = 0;
                    while (i4 < listFiles.length) {
                        File file2 = listFiles[i4];
                        boolean z2 = i4 < i3;
                        if (!z2 && currentTimeMillis - file2.lastModified() >= 432000000) {
                            z2 = true;
                        }
                        if (!z2) {
                            break;
                        }
                        file2.delete();
                        i5++;
                        i2 = 0;
                        if (i2 >= 3) {
                            break;
                        }
                        i4++;
                    }
                    com.uc.crashsdk.a.a.a("Removed " + i5 + " logs in " + U2);
                }
            } catch (Throwable th) {
                g.a(th);
            }
        }
    }

    public static void u() {
        if (h.r()) {
            f.a(0, new com.uc.crashsdk.a.e(403), 10000);
        }
    }

    public static void v() {
        if (!Z) {
            f.a(1, new com.uc.crashsdk.a.e(408), 1000);
        }
    }

    public static int b(boolean z2, boolean z3) {
        int i2;
        if (aa && !z3) {
            return 0;
        }
        if (z3) {
            aa = true;
            if (!Z) {
                f.c();
            }
        }
        if (z2) {
            i2 = f.a(b.c());
            h.c();
        } else {
            i2 = f.a();
            h.c();
        }
        return z3 ? f.a(z2) : i2;
    }

    public static int e(boolean z2) {
        int i2;
        if (z2) {
            i2 = f.b(b.c()) ? 1 : 0;
        } else {
            i2 = f.b();
        }
        int b2 = f.b(z2);
        return b2 > i2 ? b2 : i2;
    }

    static StringBuilder a(StackTraceElement[] stackTraceElementArr, String str) {
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        if (stackTraceElementArr != null && stackTraceElementArr.length > 0) {
            boolean z2 = str == null;
            boolean z3 = z2;
            int i3 = 0;
            for (StackTraceElement stackTraceElement : stackTraceElementArr) {
                i3++;
                sb.append("  at ");
                sb.append(stackTraceElement.toString());
                sb.append("\n");
                if (!z3 && stackTraceElement.getMethodName().contains(str)) {
                    sb.delete(0, sb.length());
                    i3 = 0;
                    z3 = true;
                }
            }
            i2 = i3;
        }
        if (i2 == 0) {
            sb.append("  (no java stack)\n");
        }
        return sb;
    }

    static StringBuilder g(String str) {
        return a(Thread.currentThread().getStackTrace(), str);
    }

    public static ParcelFileDescriptor w() {
        if (!b.d) {
            com.uc.crashsdk.a.a.c("crashsdk", "Crash so is not loaded!");
            return null;
        } else if (ab != null) {
            return ab;
        } else {
            int nativeGetOrSetHostFd = JNIBridge.nativeGetOrSetHostFd(2, -1);
            if (nativeGetOrSetHostFd == -1) {
                return null;
            }
            ab = ParcelFileDescriptor.adoptFd(nativeGetOrSetHostFd);
            ac = true;
            return ab;
        }
    }

    public static boolean a(ParcelFileDescriptor parcelFileDescriptor) {
        if (ac) {
            com.uc.crashsdk.a.a.c("crashsdk", "Can not call setHostFd and getHostFd in the same process!");
            return false;
        } else if (!b.d) {
            com.uc.crashsdk.a.a.c("crashsdk", "Crash so is not loaded!");
            return false;
        } else {
            if (ab != null) {
                com.uc.crashsdk.a.a.b("crashsdk", "Has already set host fd!");
            }
            ab = parcelFileDescriptor;
            int fd = parcelFileDescriptor.getFd();
            int nativeGetOrSetHostFd = JNIBridge.nativeGetOrSetHostFd(1, fd);
            ad = nativeGetOrSetHostFd != -1;
            if (fd == -1 || nativeGetOrSetHostFd != -1) {
                return true;
            }
            return false;
        }
    }

    public static boolean x() {
        return ad;
    }
}
