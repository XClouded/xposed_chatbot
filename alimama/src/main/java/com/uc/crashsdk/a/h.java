package com.uc.crashsdk.a;

import android.os.Build;
import android.os.Process;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.SparseArray;
import anetwork.channel.NetworkListenerState;
import anetwork.channel.util.RequestConstant;
import com.alibaba.taffy.core.util.codec.MessageDigestAlgorithms;
import com.taobao.accs.common.Constants;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.weex.analyzer.Config;
import com.uc.crashsdk.b;
import com.uc.crashsdk.e;
import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import kotlin.jvm.internal.ByteCompanionObject;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.xstate.util.XStateConstants;

/* compiled from: ProGuard */
public class h {
    static final /* synthetic */ boolean a = (!h.class.desiredAssertionStatus());
    private static final Object b = new Object();
    private static final Map<String, String> c = new HashMap();
    private static int d = 0;
    private static final Map<String, a> e = new HashMap();
    private static final Object f = new Object();
    private static final Object g = new Object();
    private static final SparseArray<String> h = new SparseArray<>();
    private static boolean i = false;
    private static boolean j = false;
    private static final char[] k = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final Object l = new Object();
    private static String m = null;

    static /* synthetic */ String a(long j2) {
        if (j2 < PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED) {
            return "512M";
        }
        return String.format(Locale.US, "%dG", new Object[]{Long.valueOf(((j2 / 1024) + 512) / 1024)});
    }

    static /* synthetic */ void a(StringBuilder sb, Map map) {
        for (String str : map.keySet()) {
            b(sb, str, (String) map.get(str));
        }
    }

    static /* synthetic */ Map c(String str) {
        HashMap hashMap = new HashMap();
        for (String str2 : str.split("`")) {
            if (str2.length() > 1) {
                String[] split = str2.split(SymbolExpUtil.SYMBOL_EQUAL, 3);
                if (split.length == 2) {
                    hashMap.put(split[0], split[1]);
                }
            }
        }
        return hashMap;
    }

    /* compiled from: ProGuard */
    private static class a {
        long a = 0;
        int b = 0;
        Map<String, String> c = new HashMap();
        private String d;
        private boolean e = false;
        private boolean f = false;

        a(String str, boolean z, boolean z2) {
            this.d = str;
            this.e = z;
            this.f = z2;
        }

        /* access modifiers changed from: package-private */
        public final void a(String str, String str2) {
            this.c.put(str, str2);
        }

        /* access modifiers changed from: package-private */
        public final void a(String str, long j) {
            long d2 = d(str) + j;
            if (d2 <= 100) {
                j = d2 < 0 ? 0 : d2;
            }
            a(str, String.valueOf(j));
        }

        /* access modifiers changed from: package-private */
        public final boolean a(a aVar) {
            if (!this.f) {
                a.a("crashsdk", String.format(Locale.US, "WaItem '%s' is not mergable!", new Object[]{this.d}), (Throwable) null);
                return false;
            }
            for (String next : aVar.c.keySet()) {
                if (next.startsWith("c_")) {
                    a(next, aVar.a(next));
                } else {
                    long d2 = aVar.d(next);
                    if (d2 == 0) {
                        a(next, aVar.a(next));
                    } else if (d2 < 100) {
                        a(next, d2);
                    }
                }
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public final String a(String str) {
            return this.c.get(str);
        }

        /* access modifiers changed from: package-private */
        public final String b(String str) {
            String a2 = a(str);
            return a2 == null ? "" : a2;
        }

        private long d(String str) {
            return g.c(a(str));
        }

        /* access modifiers changed from: package-private */
        public final String a(boolean z, boolean z2, boolean z3) {
            if (this.d == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            if (z) {
                h.b(sb, "lt", DinamicConstant.UPPER_PREFIX);
                h.b(sb, RequestConstant.ENV_PRE, com.uc.crashsdk.h.f());
                h.b(sb, "pkg", com.uc.crashsdk.a.a);
                h.b(sb, "rom", Build.VERSION.RELEASE);
                h.b(sb, "brd", Build.BRAND);
                h.b(sb, Constants.KEY_MODEL, Build.MODEL);
                h.b(sb, "sdk", String.valueOf((long) Build.VERSION.SDK_INT));
                h.b(sb, Config.TYPE_CPU, e.d());
                h.b(sb, "hdw", e.e());
                long e2 = h.h();
                h.b(sb, "ram", String.valueOf(e2));
                h.b(sb, "aram", h.a(e2));
                h.b(sb, "nett", c.a());
                h.b(sb, "cver", "2.3.1.0");
                h.b(sb, "cseq", "190401175529");
                h.b(sb, "aver", com.uc.crashsdk.a.a());
                h.b(sb, "ver", com.uc.crashsdk.h.P());
                h.b(sb, "sver", com.uc.crashsdk.h.Q());
                h.b(sb, "seq", com.uc.crashsdk.h.R());
                h.b(sb, "grd", b.x() ? "fg" : "bg");
                h.b(sb, "os", "android");
                sb.append("\n");
            }
            h.b(sb, "lt", this.d);
            h.a(sb, (Map) this.c);
            if (this.e && !z2) {
                if (this.a != 0) {
                    h.b(sb, "up", String.valueOf(this.a));
                }
                if (z3) {
                    h.b(sb, "pid", String.format(Locale.US, "%d", new Object[]{Integer.valueOf(Process.myPid())}));
                } else if (this.b != 0) {
                    h.b(sb, "pid", String.format(Locale.US, "%d", new Object[]{Integer.valueOf(this.b)}));
                }
            }
            sb.append("\n");
            return sb.toString();
        }

        /* access modifiers changed from: package-private */
        public final boolean c(String str) {
            if (g.a(str)) {
                return false;
            }
            String str2 = null;
            long j = 0;
            HashMap hashMap = new HashMap();
            Map c2 = h.c(str);
            int i = 0;
            for (String str3 : c2.keySet()) {
                String str4 = (String) c2.get(str3);
                if (str3.equals("lt")) {
                    str2 = str4;
                } else if (this.e && str3.equals("up")) {
                    j = g.c(str4);
                } else if (!this.e || !str3.equals("pid")) {
                    hashMap.put(str3, str4);
                } else {
                    i = (int) g.c(str4);
                }
            }
            if (this.d != null && !this.d.equals(str2)) {
                return false;
            }
            this.a = j;
            this.b = i;
            this.d = str2;
            this.c = hashMap;
            return true;
        }
    }

    /* access modifiers changed from: private */
    public static void b(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(str2);
        sb.append("`");
    }

    static void a(String str) {
        synchronized (b) {
            File file = new File(f());
            a aVar = new a(XStateConstants.KEY_PV, true, true);
            String b2 = g.b(file);
            if (!g.a(b2)) {
                aVar.c(b2);
            }
            aVar.a(str, 1);
            aVar.a("aujv", 1);
            g.a(file, aVar.a(false, false, false));
        }
    }

    public static void a() {
        if (b.A()) {
            f.a(0, new e(302), 70000);
        }
    }

    public static boolean a(String str, String str2) {
        String str3;
        try {
            String str4 = "c_" + str.replaceAll("[^0-9a-zA-Z-_]", "-");
            if (g.a(str2)) {
                str3 = "";
            } else {
                str3 = str2.replaceAll("[`=]", "-");
            }
            synchronized (c) {
                if (c.get(str4) == null) {
                    if (d >= 20) {
                        return false;
                    }
                    d++;
                }
                c.put(str4, str3);
                return true;
            }
        } catch (Throwable th) {
            g.a(th);
            return false;
        }
    }

    private static void a(a aVar) {
        synchronized (c) {
            for (String next : c.keySet()) {
                aVar.a(next, c.get(next));
            }
        }
    }

    public static void b() {
        if (com.uc.crashsdk.h.N()) {
            f.a(1, new e(301), 2000);
        }
    }

    public static void a(int i2) {
        switch (i2) {
            case 301:
                String str = com.uc.crashsdk.h.S() + "cr.wa";
                b.a(b, str, new e(351, new Object[]{str}));
                return;
            case 302:
                synchronized (b) {
                    if (!i) {
                        i = true;
                        if (!com.uc.crashsdk.a.b.equals("2.0") || !b.c(268435456)) {
                            File file = new File(f());
                            String b2 = g.b(file);
                            a aVar = new a(XStateConstants.KEY_PV, true, true);
                            if (!g.a(b2)) {
                                aVar.c(b2);
                            }
                            if (!(aVar.b == Process.myPid())) {
                                aVar.a(XStateConstants.KEY_PV, 1);
                                aVar.a("fjv", 1);
                                String n = e.n();
                                long j2 = aVar.a;
                                if ((j2 == 0 || System.currentTimeMillis() - j2 >= 28800000) ? b(n, aVar.a(true, true, false)) : false) {
                                    aVar.c = new HashMap();
                                    aVar.a = System.currentTimeMillis();
                                    aVar.b = Process.myPid();
                                }
                                g.a(file, aVar.a(false, false, true));
                            }
                        }
                    }
                }
                return;
            case 303:
                String str2 = com.uc.crashsdk.h.S() + "dt.wa";
                b.a(f, str2, new e(352, new Object[]{str2}));
                String g2 = g();
                b.a(g, g2, new e(354, new Object[]{g2}));
                return;
            default:
                if (!a) {
                    throw new AssertionError();
                }
                return;
        }
    }

    static boolean a(int i2, Object[] objArr) {
        switch (i2) {
            case 351:
                if (a || objArr != null) {
                    String str = objArr[0];
                    if (j) {
                        return false;
                    }
                    j = true;
                    File file = new File(str);
                    ArrayList<a> a2 = a(file, "crp", 100);
                    a aVar = new a("crp", false, false);
                    aVar.a("et", String.valueOf(b.B()));
                    aVar.a("prc", e.g());
                    aVar.a("imp", b.A() ? "1" : "0");
                    a(aVar);
                    a2.add(0, aVar);
                    boolean b2 = b(e.n(), a((Iterable<a>) a2, true, false).toString());
                    g.a(file);
                    if (!b2) {
                        g.a(file, a((Iterable<a>) a2, false, true).toString());
                    }
                    return true;
                }
                throw new AssertionError();
            case 352:
                if (a || objArr != null) {
                    return d(objArr[0]);
                }
                throw new AssertionError();
            case 353:
                if (a || objArr != null) {
                    return b(objArr[0], objArr[1], objArr[2].booleanValue(), objArr[3].booleanValue());
                }
                throw new AssertionError();
            case 354:
                if (a || objArr != null) {
                    File file2 = new File(objArr[0]);
                    boolean b3 = b(e.n(), a((Iterable<a>) a(file2, "cst", 30), true, false).toString());
                    if (b3) {
                        g.a(file2);
                    }
                    return b3;
                }
                throw new AssertionError();
            default:
                return false;
        }
    }

    private static StringBuilder a(Iterable<a> iterable, boolean z, boolean z2) {
        StringBuilder sb = new StringBuilder();
        boolean z3 = true;
        for (a next : iterable) {
            if (z3) {
                sb.append(next.a(z, z, z2));
                z3 = false;
            } else {
                sb.append(next.a(false, z, z2));
            }
        }
        return sb;
    }

    public static void a(String str, int i2, int i3) {
        if (com.uc.crashsdk.h.N()) {
            synchronized (f) {
                a aVar = e.get(str);
                if (aVar == null) {
                    aVar = new a("cst", false, true);
                    e.put(str, aVar);
                    a(aVar);
                }
                synchronized (h) {
                    if (h.size() == 0) {
                        a(100, XStateConstants.KEY_PV);
                        a(1, "all");
                        a(2, "afg");
                        a(3, "jfg");
                        a(4, "jbg");
                        a(7, "nfg");
                        a(8, "nbg");
                        a(27, "nafg");
                        a(28, "nabg");
                        a(9, "nho");
                        a(10, "uar");
                        a(29, "ulm");
                        a(30, "ukt");
                        a(31, "uet");
                        a(32, "urs");
                        a(11, "ufg");
                        a(12, "ubg");
                        a(13, "lup");
                        a(14, "luf");
                        a(15, "lef");
                        a(16, "laf");
                        a(22, "lac");
                        a(23, "lau");
                        a(17, "llf");
                        a(18, "lul");
                        a(19, "lub");
                        a(20, "luc");
                        a(21, "luu");
                        a(24, "lzc");
                        a(25, "lrc");
                        a(26, "lss");
                    }
                }
                String str2 = h.get(i2);
                if (str2 == null) {
                    a.a("crashsdk", "map key is not set with: " + i2, (Throwable) null);
                }
                aVar.a("prc", str);
                if (str2 != null) {
                    aVar.a(str2, String.valueOf(i3));
                }
            }
        }
    }

    public static void c() {
        if (com.uc.crashsdk.h.N()) {
            f.a(1, (Runnable) new e(303));
        }
    }

    private static boolean d(String str) {
        File file = new File(str);
        Iterator<a> it = a(file, "cst", 30).iterator();
        while (it.hasNext()) {
            a next = it.next();
            String a2 = next.a("prc");
            if (!g.a(a2)) {
                a aVar = e.get(a2);
                if (aVar != null) {
                    aVar.a(next);
                } else {
                    e.put(a2, next);
                }
            }
        }
        boolean b2 = b(e.n(), a((Iterable<a>) e.values(), true, false).toString());
        g.a(file);
        if (b2 || g.a(file, a((Iterable<a>) e.values(), false, true).toString())) {
            e.clear();
        }
        return true;
    }

    public static boolean a(String str, String str2, boolean z, boolean z2) {
        if (!com.uc.crashsdk.h.N()) {
            return false;
        }
        String g2 = g();
        return b.a(g, g2, new e(353, new Object[]{str, str2, Boolean.valueOf(z), Boolean.valueOf(z2)}));
    }

    private static boolean b(String str, String str2, boolean z, boolean z2) {
        a aVar;
        File file = new File(g());
        ArrayList<a> a2 = a(file, "cst", 30);
        String str3 = str + str2;
        Iterator<a> it = a2.iterator();
        while (true) {
            if (!it.hasNext()) {
                aVar = null;
                break;
            }
            aVar = it.next();
            if (str3.equals(aVar.b("prc") + aVar.b("typ"))) {
                break;
            }
        }
        if (aVar == null) {
            aVar = new a("cst", false, true);
            aVar.a("prc", str);
            aVar.a("typ", str2);
            a(aVar);
            a2.add(aVar);
        }
        aVar.a("cnt", 1);
        if (z) {
            aVar.a("lim", 1);
        }
        if (z2) {
            aVar.a("syu", 1);
        }
        return g.a(file, a((Iterable<a>) a2, false, false).toString());
    }

    private static void a(int i2, String str) {
        h.put(i2, str);
    }

    private static ArrayList<a> a(File file, String str, int i2) {
        ArrayList<String> b2 = g.b(file, i2);
        ArrayList<a> arrayList = new ArrayList<>();
        Iterator<String> it = b2.iterator();
        while (it.hasNext()) {
            a aVar = new a(str, false, false);
            if (aVar.c(it.next())) {
                arrayList.add(aVar);
            }
        }
        return arrayList;
    }

    private static String f() {
        return com.uc.crashsdk.h.S() + "pv.wa";
    }

    private static String g() {
        return com.uc.crashsdk.h.S() + "cdt.wa";
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00c9 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x00ca A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean b(java.lang.String r9, java.lang.String r10) {
        /*
            boolean r0 = com.uc.crashsdk.a.g.a((java.lang.String) r10)
            r1 = 1
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            byte[] r10 = r10.getBytes()
            r0 = 16
            r2 = 8
            r3 = 0
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x003a }
            byte[] r4 = com.uc.crashsdk.a.c.b()     // Catch:{ Throwable -> 0x003a }
            com.uc.crashsdk.a.c.a((byte[]) r0, (int) r3, (byte[]) r4)     // Catch:{ Throwable -> 0x003a }
            r4 = 4
            byte[] r5 = d()     // Catch:{ Throwable -> 0x003a }
            com.uc.crashsdk.a.c.a((byte[]) r0, (int) r4, (byte[]) r5)     // Catch:{ Throwable -> 0x003a }
            byte[] r4 = com.uc.crashsdk.a.e()     // Catch:{ Throwable -> 0x003a }
            com.uc.crashsdk.a.c.a((byte[]) r0, (int) r2, (byte[]) r4)     // Catch:{ Throwable -> 0x003a }
            r4 = 12
            byte[] r5 = com.uc.crashsdk.a.d.c()     // Catch:{ Throwable -> 0x003a }
            com.uc.crashsdk.a.c.a((byte[]) r0, (int) r4, (byte[]) r5)     // Catch:{ Throwable -> 0x003a }
            byte[] r0 = com.uc.crashsdk.a.c.a((byte[]) r10, (byte[]) r0)     // Catch:{ Throwable -> 0x003a }
            if (r0 == 0) goto L_0x003e
            r10 = 1
            goto L_0x0040
        L_0x003a:
            r0 = move-exception
            com.uc.crashsdk.a.g.a((java.lang.Throwable) r0)
        L_0x003e:
            r0 = r10
            r10 = 0
        L_0x0040:
            if (r9 != 0) goto L_0x0044
            java.lang.String r9 = "unknown"
        L_0x0044:
            java.lang.String r4 = "28ef1713347d"
            boolean r5 = com.uc.crashsdk.h.O()
            if (r5 == 0) goto L_0x004e
            java.lang.String r4 = "4ea4e41a3993"
        L_0x004e:
            long r5 = java.lang.System.currentTimeMillis()
            java.lang.String r5 = java.lang.String.valueOf(r5)
            java.lang.String r6 = "AppChk#2014"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r7.append(r4)
            r7.append(r9)
            r7.append(r5)
            r7.append(r6)
            java.lang.String r6 = r7.toString()
            java.lang.String r6 = e(r6)
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = i()
            r7.append(r8)
            java.lang.String r8 = "?chk="
            r7.append(r8)
            int r8 = r6.length()
            int r8 = r8 - r2
            int r2 = r6.length()
            java.lang.String r2 = r6.substring(r8, r2)
            r7.append(r2)
            java.lang.String r2 = "&vno="
            r7.append(r2)
            r7.append(r5)
            java.lang.String r2 = "&uuid="
            r7.append(r2)
            r7.append(r9)
            java.lang.String r9 = "&app="
            r7.append(r9)
            r7.append(r4)
            if (r10 == 0) goto L_0x00b1
            java.lang.String r9 = "&enc=aes"
            r7.append(r9)
        L_0x00b1:
            java.lang.String r9 = r7.toString()
            byte[] r9 = com.uc.crashsdk.a.c.a((java.lang.String) r9, (byte[]) r0)
            if (r9 != 0) goto L_0x00bc
            return r3
        L_0x00bc:
            java.lang.String r10 = new java.lang.String
            r10.<init>(r9)
            java.lang.String r9 = "retcode=0"
            boolean r9 = r10.contains(r9)
            if (r9 == 0) goto L_0x00ca
            return r1
        L_0x00ca:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.h.b(java.lang.String, java.lang.String):boolean");
    }

    /* access modifiers changed from: private */
    public static long h() {
        Iterator<String> it = g.b(new File("/proc/meminfo"), 2).iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next.contains("MemTotal:")) {
                try {
                    return Long.parseLong(next.replaceAll("\\D+", ""));
                } catch (NumberFormatException e2) {
                    g.a((Throwable) e2);
                }
            }
        }
        return 0;
    }

    static byte[] d() {
        return new byte[]{ByteCompanionObject.MAX_VALUE, 100, 110, NetworkListenerState.ALL};
    }

    private static String e(String str) {
        try {
            byte[] bytes = str.getBytes("utf-8");
            MessageDigest instance = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            instance.update(bytes);
            byte[] digest = instance.digest();
            int length = digest.length;
            StringBuilder sb = new StringBuilder(length * 2);
            int i2 = length + 0;
            for (int i3 = 0; i3 < i2; i3++) {
                byte b2 = digest[i3];
                char c2 = k[(b2 & 240) >> 4];
                char c3 = k[b2 & 15];
                sb.append(c2);
                sb.append(c3);
            }
            return sb.toString();
        } catch (Exception e2) {
            a.a("crashsdk", "getMD5: ", e2);
            return null;
        }
    }

    static void b(String str) {
        synchronized (l) {
            m = str;
            String j2 = b.j();
            b.a(j2, m + "\n");
        }
    }

    private static String i() {
        if (g.a(m)) {
            synchronized (l) {
                String str = "https://applog.uc.cn/collect";
                if (com.uc.crashsdk.h.O()) {
                    str = "https://gjapplog.ucweb.com/collect";
                }
                m = g.a(b.j(), str, true);
            }
        }
        return m;
    }
}
