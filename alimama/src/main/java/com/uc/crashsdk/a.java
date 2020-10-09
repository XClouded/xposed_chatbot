package com.uc.crashsdk;

import android.content.pm.PackageInfo;
import android.util.SparseArray;
import com.uc.crashsdk.a.b;
import com.uc.crashsdk.a.e;
import com.uc.crashsdk.a.f;
import com.uc.crashsdk.a.g;
import com.uc.crashsdk.export.LogType;
import java.io.File;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/* compiled from: ProGuard */
public class a {
    private static int A = 0;
    private static Runnable B = new e(201);
    private static boolean C = false;
    private static boolean D = false;
    public static String a = "";
    public static String b = "";
    static boolean c = false;
    static final /* synthetic */ boolean d = (!a.class.desiredAssertionStatus());
    private static final Map<String, String> e = new HashMap();
    private static final List<String> f = new ArrayList();
    private static String g = "";
    private static String h = null;
    private static long i = 0;
    private static final HashMap<String, Object[]> j = new HashMap<>();
    private static final List<String> k = new ArrayList();
    private static int l = 0;
    private static int m = 0;
    private static int n = 0;
    private static final HashMap<String, Object[]> o = new HashMap<>();
    private static final List<String> p = new ArrayList();
    private static int q = 0;
    private static int r = 0;
    private static int s = 0;
    private static int t = 0;
    private static int u = 0;
    private static final SparseArray<Object[]> v = new SparseArray<>();
    private static final List<Integer> w = new ArrayList();
    private static final HashMap<String, Object[]> x = new HashMap<>();
    private static final List<String> y = new ArrayList();
    private static int z = 0;

    public static String a() {
        if (h != null) {
            return h;
        }
        try {
            PackageInfo packageInfo = g.a().getPackageManager().getPackageInfo(a, 0);
            h = packageInfo.versionName;
            i = packageInfo.lastUpdateTime;
            return h;
        } catch (Throwable th) {
            g.b(th);
            return "";
        }
    }

    static long b() {
        return i;
    }

    public static void a(String str, String str2) {
        synchronized (e) {
            if (!e.containsKey(str)) {
                f.add(str);
            }
            e.put(str, str2);
            if (b.d) {
                JNIBridge.nativeAddHeaderInfo(str, str2);
            }
            e.s();
        }
    }

    static void c() {
        StringBuilder sb = new StringBuilder();
        synchronized (e) {
            for (String next : f) {
                String str = e.get(next);
                sb.append(next);
                sb.append(": ");
                if (str != null) {
                    sb.append(str);
                }
                sb.append("\n");
            }
        }
        sb.append(String.format(Locale.US, "(saved at %s)\n", new Object[]{e.k()}));
        b.a(b.g(), sb.toString());
    }

    static void d() {
        if (d || b.d) {
            synchronized (e) {
                for (String next : f) {
                    JNIBridge.nativeAddHeaderInfo(next, e.get(next));
                }
            }
            return;
        }
        throw new AssertionError();
    }

    static void a(OutputStream outputStream, String str) {
        synchronized (e) {
            for (String next : f) {
                try {
                    StringBuilder sb = new StringBuilder(11);
                    sb.append(next);
                    sb.append(": ");
                    String str2 = e.get(next);
                    if (str2 != null) {
                        sb.append(str2);
                    }
                    sb.append("\n");
                    outputStream.write(sb.toString().getBytes(str));
                } catch (Throwable th) {
                    e.a(th, outputStream);
                }
            }
        }
    }

    public static byte[] e() {
        return new byte[]{24, 99, 121, 60};
    }

    public static int a(String str, String str2, boolean z2, boolean z3, int i2, boolean z4) {
        int i3;
        int i4;
        int i5;
        boolean z5;
        if (str == null || str2 == null) {
            return 0;
        }
        if (str.length() > 256) {
            com.uc.crashsdk.a.a.a("crashsdk", "addDumpFile: description is too long!", (Throwable) null);
            return 0;
        }
        synchronized (j) {
            if (j.containsKey(str)) {
                i4 = ((Integer) j.get(str)[0]).intValue();
                i3 = LogType.addType(i4, i2);
            } else {
                i3 = i2;
                i4 = 0;
            }
            if (LogType.isForJava(i3) && !LogType.isForJava(i4)) {
                if (l >= 10) {
                    i3 = LogType.removeType(i3, 16);
                } else {
                    l++;
                }
            }
            if (LogType.isForNative(i3) && !LogType.isForNative(i4)) {
                if (m >= 10) {
                    i3 = LogType.removeType(i3, 1);
                } else {
                    m++;
                }
            }
            if (LogType.isForUnexp(i3) && !LogType.isForUnexp(i4)) {
                if (n >= 10) {
                    i3 = LogType.removeType(i3, 256);
                } else {
                    n++;
                }
            }
            i5 = i3;
            if ((i5 & 273) == 0) {
                z5 = false;
            } else {
                if (i4 == 0) {
                    k.add(str);
                }
                z5 = true;
            }
            if (z5) {
                if (b.d && (LogType.isForNative(i2) || LogType.isForUnexp(i2))) {
                    int nativeAddDumpFile = JNIBridge.nativeAddDumpFile(str, str2, z2, z3, LogType.isForNative(i2), LogType.isForUnexp(i2), z4);
                    if (!LogType.isForNative(nativeAddDumpFile)) {
                        i5 = LogType.removeType(i5, 1);
                    }
                    if (!LogType.isForUnexp(nativeAddDumpFile)) {
                        i5 = LogType.removeType(i5, 256);
                    }
                }
                j.put(str, new Object[]{Integer.valueOf(i5), str2, Boolean.valueOf(z2), Boolean.valueOf(z3), Boolean.valueOf(z4)});
            }
        }
        return i5;
    }

    static void f() {
        if (d || b.d) {
            synchronized (j) {
                for (String next : k) {
                    Object[] objArr = j.get(next);
                    int intValue = ((Integer) objArr[0]).intValue();
                    if (LogType.isForNative(intValue) || LogType.isForUnexp(intValue)) {
                        boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                        boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
                        boolean booleanValue3 = ((Boolean) objArr[4]).booleanValue();
                        JNIBridge.nativeAddDumpFile(next, (String) objArr[1], booleanValue, booleanValue2, LogType.isForNative(intValue), LogType.isForUnexp(intValue), booleanValue3);
                    }
                }
            }
            return;
        }
        throw new AssertionError();
    }

    static void a(OutputStream outputStream, String str, ArrayList<String> arrayList) {
        boolean z2 = arrayList == null;
        boolean x2 = e.x();
        synchronized (j) {
            int i2 = 0;
            for (String next : k) {
                try {
                    Object[] objArr = j.get(next);
                    if (arrayList == null) {
                        if (!LogType.isForJava(((Integer) objArr[0]).intValue())) {
                        }
                    } else if (!a((List<String>) arrayList, next)) {
                    }
                    if (((Boolean) objArr[3]).booleanValue()) {
                        outputStream.write((next + "\n").getBytes(str));
                    }
                    if (i2 > 153600) {
                        i2 = 153600;
                    }
                    int min = Math.min(20480, 153600 - i2);
                    boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
                    String str2 = (String) objArr[1];
                    if (x2 && !str2.startsWith("/proc/")) {
                        e.a(outputStream, "FILE", str2, min, booleanValue);
                    } else if (booleanValue) {
                        i2 += e.a(outputStream, str2, min);
                    } else {
                        i2 += e.b(outputStream, str2, min);
                    }
                    if (((Boolean) objArr[4]).booleanValue() && z2 && !x2) {
                        File file = new File(str2);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                } catch (Throwable th) {
                    e.a(th, outputStream);
                }
            }
            if (arrayList != null && x2) {
                Iterator<String> it = arrayList.iterator();
                while (it.hasNext()) {
                    String next2 = it.next();
                    if (!a(k, next2)) {
                        e.a(outputStream, "CUSTOMDUMPFILE", next2);
                    }
                }
            }
        }
    }

    static String g() {
        StringBuilder sb = new StringBuilder();
        synchronized (j) {
            boolean z2 = true;
            for (String next : k) {
                if (LogType.isForJava(((Integer) j.get(next)[0]).intValue())) {
                    if (!z2) {
                        sb.append("`");
                    }
                    sb.append(next);
                    z2 = false;
                }
            }
        }
        return sb.toString();
    }

    static String a(String str) {
        synchronized (j) {
            Object[] objArr = j.get(str);
            if (objArr == null) {
                return null;
            }
            boolean booleanValue = ((Boolean) objArr[2]).booleanValue();
            boolean booleanValue2 = ((Boolean) objArr[3]).booleanValue();
            String format = String.format(Locale.US, "%s%s%d%d", new Object[]{(String) objArr[1], "`", Integer.valueOf(booleanValue ? 1 : 0), Integer.valueOf(booleanValue2 ? 1 : 0)});
            return format;
        }
    }

    private static boolean a(List<String> list, String str) {
        if (g.a(str)) {
            return false;
        }
        for (String equals : list) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0151, code lost:
        return r9;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00d7  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00d9  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00e3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int a(java.lang.String r15, int r16, java.util.concurrent.Callable<java.lang.String> r17, long r18, int r20) {
        /*
            r0 = r15
            r7 = 0
            if (r0 != 0) goto L_0x0005
            return r7
        L_0x0005:
            java.util.HashMap<java.lang.String, java.lang.Object[]> r8 = o
            monitor-enter(r8)
            java.util.HashMap<java.lang.String, java.lang.Object[]> r1 = o     // Catch:{ all -> 0x0152 }
            boolean r1 = r1.containsKey(r15)     // Catch:{ all -> 0x0152 }
            if (r1 == 0) goto L_0x002a
            java.util.HashMap<java.lang.String, java.lang.Object[]> r1 = o     // Catch:{ all -> 0x0152 }
            java.lang.Object r1 = r1.get(r15)     // Catch:{ all -> 0x0152 }
            java.lang.Object[] r1 = (java.lang.Object[]) r1     // Catch:{ all -> 0x0152 }
            r1 = r1[r7]     // Catch:{ all -> 0x0152 }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ all -> 0x0152 }
            int r1 = r1.intValue()     // Catch:{ all -> 0x0152 }
            r2 = r16
            int r3 = com.uc.crashsdk.export.LogType.addType(r1, r2)     // Catch:{ all -> 0x0152 }
            if (r1 != r3) goto L_0x002e
            monitor-exit(r8)     // Catch:{ all -> 0x0152 }
            return r1
        L_0x002a:
            r2 = r16
            r3 = r2
            r1 = 0
        L_0x002e:
            boolean r4 = com.uc.crashsdk.export.LogType.isForJava(r3)     // Catch:{ all -> 0x0152 }
            r5 = 0
            r9 = 6
            r10 = 1
            if (r4 == 0) goto L_0x006b
            boolean r4 = com.uc.crashsdk.export.LogType.isForJava(r1)     // Catch:{ all -> 0x0152 }
            if (r4 != 0) goto L_0x006b
            int r4 = q     // Catch:{ all -> 0x0152 }
            r11 = 8
            if (r4 < r11) goto L_0x0046
        L_0x0044:
            r4 = 1
            goto L_0x0063
        L_0x0046:
            int r4 = (r18 > r5 ? 1 : (r18 == r5 ? 0 : -1))
            if (r4 == 0) goto L_0x0055
            int r4 = t     // Catch:{ all -> 0x0152 }
            if (r4 < r9) goto L_0x004f
            goto L_0x0044
        L_0x004f:
            int r4 = t     // Catch:{ all -> 0x0152 }
            int r4 = r4 + r10
            t = r4     // Catch:{ all -> 0x0152 }
            goto L_0x005d
        L_0x0055:
            int r4 = q     // Catch:{ all -> 0x0152 }
            int r11 = t     // Catch:{ all -> 0x0152 }
            int r4 = r4 - r11
            if (r4 < r9) goto L_0x005d
            goto L_0x0044
        L_0x005d:
            int r4 = q     // Catch:{ all -> 0x0152 }
            int r4 = r4 + r10
            q = r4     // Catch:{ all -> 0x0152 }
            r4 = 0
        L_0x0063:
            if (r4 == 0) goto L_0x006b
            r4 = 16
            int r3 = com.uc.crashsdk.export.LogType.removeType(r3, r4)     // Catch:{ all -> 0x0152 }
        L_0x006b:
            boolean r4 = com.uc.crashsdk.export.LogType.isForNative(r3)     // Catch:{ all -> 0x0152 }
            r11 = 4
            if (r4 == 0) goto L_0x00b0
            boolean r4 = com.uc.crashsdk.export.LogType.isForNative(r1)     // Catch:{ all -> 0x0152 }
            if (r4 != 0) goto L_0x00b0
            int r4 = r     // Catch:{ all -> 0x0152 }
            if (r4 < r9) goto L_0x0080
        L_0x007c:
            r4 = 1
            r5 = 0
        L_0x007e:
            r6 = 0
            goto L_0x00a7
        L_0x0080:
            int r4 = (r18 > r5 ? 1 : (r18 == r5 ? 0 : -1))
            if (r4 == 0) goto L_0x0097
            int r4 = u     // Catch:{ all -> 0x0152 }
            if (r4 < r11) goto L_0x0089
            goto L_0x007c
        L_0x0089:
            int r4 = u     // Catch:{ all -> 0x0152 }
            int r4 = r4 + r10
            u = r4     // Catch:{ all -> 0x0152 }
            int r4 = r     // Catch:{ all -> 0x0152 }
            int r4 = r4 + r10
            r = r4     // Catch:{ all -> 0x0152 }
            r4 = 0
            r5 = 1
            r6 = 1
            goto L_0x00a7
        L_0x0097:
            int r4 = r     // Catch:{ all -> 0x0152 }
            int r5 = u     // Catch:{ all -> 0x0152 }
            int r4 = r4 - r5
            if (r4 < r11) goto L_0x009f
            goto L_0x007c
        L_0x009f:
            int r4 = r     // Catch:{ all -> 0x0152 }
            int r4 = r4 + r10
            r = r4     // Catch:{ all -> 0x0152 }
            r4 = 0
            r5 = 1
            goto L_0x007e
        L_0x00a7:
            if (r4 == 0) goto L_0x00ad
            int r3 = com.uc.crashsdk.export.LogType.removeType(r3, r10)     // Catch:{ all -> 0x0152 }
        L_0x00ad:
            r12 = r5
            r13 = r6
            goto L_0x00b2
        L_0x00b0:
            r12 = 0
            r13 = 0
        L_0x00b2:
            boolean r4 = com.uc.crashsdk.export.LogType.isForUnexp(r3)     // Catch:{ all -> 0x0152 }
            r6 = 256(0x100, float:3.59E-43)
            if (r4 == 0) goto L_0x00d1
            boolean r4 = com.uc.crashsdk.export.LogType.isForUnexp(r1)     // Catch:{ all -> 0x0152 }
            if (r4 != 0) goto L_0x00d1
            int r4 = s     // Catch:{ all -> 0x0152 }
            if (r4 < r9) goto L_0x00c9
            int r3 = com.uc.crashsdk.export.LogType.removeType(r3, r6)     // Catch:{ all -> 0x0152 }
            goto L_0x00d1
        L_0x00c9:
            int r4 = s     // Catch:{ all -> 0x0152 }
            int r4 = r4 + r10
            s = r4     // Catch:{ all -> 0x0152 }
            r9 = r3
            r14 = 1
            goto L_0x00d3
        L_0x00d1:
            r9 = r3
            r14 = 0
        L_0x00d3:
            r3 = r9 & 273(0x111, float:3.83E-43)
            if (r3 != 0) goto L_0x00d9
            r1 = 0
            goto L_0x00e1
        L_0x00d9:
            if (r1 != 0) goto L_0x00e0
            java.util.List<java.lang.String> r1 = p     // Catch:{ all -> 0x0152 }
            r1.add(r15)     // Catch:{ all -> 0x0152 }
        L_0x00e0:
            r1 = 1
        L_0x00e1:
            if (r1 == 0) goto L_0x0150
            boolean r1 = com.uc.crashsdk.b.d     // Catch:{ all -> 0x0152 }
            if (r1 == 0) goto L_0x0132
            boolean r1 = com.uc.crashsdk.export.LogType.isForNative(r16)     // Catch:{ all -> 0x0152 }
            if (r1 != 0) goto L_0x00f3
            boolean r1 = com.uc.crashsdk.export.LogType.isForUnexp(r16)     // Catch:{ all -> 0x0152 }
            if (r1 == 0) goto L_0x0132
        L_0x00f3:
            boolean r3 = com.uc.crashsdk.export.LogType.isForNative(r16)     // Catch:{ all -> 0x0152 }
            boolean r4 = com.uc.crashsdk.export.LogType.isForUnexp(r16)     // Catch:{ all -> 0x0152 }
            r1 = r15
            r2 = r3
            r3 = r4
            r4 = r18
            r7 = 256(0x100, float:3.59E-43)
            r6 = r20
            int r1 = com.uc.crashsdk.JNIBridge.nativeAddCallbackInfo(r1, r2, r3, r4, r6)     // Catch:{ all -> 0x0152 }
            boolean r2 = com.uc.crashsdk.export.LogType.isForNative(r1)     // Catch:{ all -> 0x0152 }
            if (r2 != 0) goto L_0x0120
            int r9 = com.uc.crashsdk.export.LogType.removeType(r9, r10)     // Catch:{ all -> 0x0152 }
            if (r12 == 0) goto L_0x0119
            int r2 = r     // Catch:{ all -> 0x0152 }
            int r2 = r2 - r10
            r = r2     // Catch:{ all -> 0x0152 }
        L_0x0119:
            if (r13 == 0) goto L_0x0120
            int r2 = u     // Catch:{ all -> 0x0152 }
            int r2 = r2 - r10
            u = r2     // Catch:{ all -> 0x0152 }
        L_0x0120:
            boolean r1 = com.uc.crashsdk.export.LogType.isForUnexp(r1)     // Catch:{ all -> 0x0152 }
            if (r1 != 0) goto L_0x0132
            int r1 = com.uc.crashsdk.export.LogType.removeType(r9, r7)     // Catch:{ all -> 0x0152 }
            if (r14 == 0) goto L_0x0131
            int r2 = s     // Catch:{ all -> 0x0152 }
            int r2 = r2 - r10
            s = r2     // Catch:{ all -> 0x0152 }
        L_0x0131:
            r9 = r1
        L_0x0132:
            java.util.HashMap<java.lang.String, java.lang.Object[]> r1 = o     // Catch:{ all -> 0x0152 }
            java.lang.Object[] r2 = new java.lang.Object[r11]     // Catch:{ all -> 0x0152 }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r9)     // Catch:{ all -> 0x0152 }
            r4 = 0
            r2[r4] = r3     // Catch:{ all -> 0x0152 }
            r2[r10] = r17     // Catch:{ all -> 0x0152 }
            r3 = 2
            java.lang.Long r4 = java.lang.Long.valueOf(r18)     // Catch:{ all -> 0x0152 }
            r2[r3] = r4     // Catch:{ all -> 0x0152 }
            r3 = 3
            java.lang.Integer r4 = java.lang.Integer.valueOf(r20)     // Catch:{ all -> 0x0152 }
            r2[r3] = r4     // Catch:{ all -> 0x0152 }
            r1.put(r15, r2)     // Catch:{ all -> 0x0152 }
        L_0x0150:
            monitor-exit(r8)     // Catch:{ all -> 0x0152 }
            return r9
        L_0x0152:
            r0 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x0152 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.a(java.lang.String, int, java.util.concurrent.Callable, long, int):int");
    }

    static void h() {
        if (d || b.d) {
            synchronized (o) {
                for (String next : p) {
                    Object[] objArr = o.get(next);
                    int intValue = ((Integer) objArr[0]).intValue();
                    if (LogType.isForNative(intValue) || LogType.isForUnexp(intValue)) {
                        long longValue = ((Long) objArr[2]).longValue();
                        int intValue2 = ((Integer) objArr[3]).intValue();
                        JNIBridge.nativeAddCallbackInfo(next, LogType.isForNative(intValue), LogType.isForUnexp(intValue), longValue, intValue2);
                    }
                }
            }
            return;
        }
        throw new AssertionError();
    }

    static void a(OutputStream outputStream, String str, String str2, ArrayList<String> arrayList) {
        String str3;
        synchronized (o) {
            for (String next : p) {
                try {
                    Object[] objArr = o.get(next);
                    int intValue = ((Integer) objArr[0]).intValue();
                    if (arrayList == null) {
                        if (!LogType.isForJava(intValue)) {
                        }
                    } else if (!a((List<String>) arrayList, next)) {
                    }
                    outputStream.write((next + "\n").getBytes(str));
                    long longValue = ((Long) objArr[2]).longValue();
                    if (longValue != 0) {
                        str3 = JNIBridge.nativeGetCallbackInfo(next, longValue, ((Integer) objArr[3]).intValue(), false);
                    } else {
                        str3 = b(next, false).toString();
                    }
                    if (str3 == null || str3.length() <= 0) {
                        outputStream.write("(data is null)\n".getBytes(str));
                    } else {
                        outputStream.write(str3.getBytes(str));
                    }
                } catch (Throwable th) {
                    e.a(th, outputStream);
                }
                try {
                    outputStream.write("\n".getBytes(str));
                    outputStream.write(str2.getBytes(str));
                } catch (Throwable th2) {
                    e.a(th2, outputStream);
                }
            }
            if (arrayList != null && e.x()) {
                Iterator<String> it = arrayList.iterator();
                while (it.hasNext()) {
                    String next2 = it.next();
                    if (!a(p, next2)) {
                        e.a(outputStream, "CUSTOMCALLBACKINFO", next2);
                    }
                }
            }
        }
        return;
    }

    static String a(String str, boolean z2) {
        String str2;
        synchronized (o) {
            Object[] objArr = o.get(str);
            long longValue = ((Long) objArr[2]).longValue();
            if (longValue != 0) {
                str2 = JNIBridge.nativeGetCallbackInfo(str, longValue, ((Integer) objArr[3]).intValue(), z2);
            } else {
                str2 = b(str, z2).toString();
            }
        }
        return str2;
    }

    private static StringBuilder b(String str, boolean z2) {
        String str2;
        StringBuilder sb = new StringBuilder();
        try {
            Object[] objArr = o.get(str);
            if (objArr == null) {
                str2 = "Unknown callback: " + str;
            } else {
                Callable callable = (Callable) objArr[1];
                if (callable != null) {
                    str2 = (String) callable.call();
                } else {
                    str2 = d.a(str, z2);
                }
            }
            if (str2 != null) {
                sb.append(str2);
            }
        } catch (Throwable th) {
            g.a(th);
        }
        try {
            if (sb.length() == 0) {
                sb.append("(data is null)\n");
            }
        } catch (Throwable th2) {
            g.a(th2);
        }
        return sb;
    }

    static String i() {
        String sb;
        synchronized (o) {
            StringBuilder sb2 = new StringBuilder();
            synchronized (p) {
                boolean z2 = true;
                for (String next : p) {
                    if (LogType.isForJava(((Integer) o.get(next)[0]).intValue())) {
                        if (!z2) {
                            sb2.append("`");
                        }
                        sb2.append(next);
                        z2 = false;
                    }
                }
            }
            sb = sb2.toString();
        }
        return sb;
    }

    private static boolean a(String str, Thread thread) {
        if (thread == null) {
            return false;
        }
        synchronized (v) {
            int id = (int) thread.getId();
            if (v.get(id) == null) {
                w.add(Integer.valueOf(id));
            }
            WeakReference weakReference = new WeakReference(thread);
            v.put(id, new Object[]{weakReference, str});
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0084 A[Catch:{ Throwable -> 0x00c1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0087 A[Catch:{ Throwable -> 0x00c1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00cc A[Catch:{ Throwable -> 0x0102 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00da A[Catch:{ Throwable -> 0x0102 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void a(java.io.OutputStream r13, java.lang.String r14, java.lang.String r15) {
        /*
            android.util.SparseArray<java.lang.Object[]> r0 = v
            monitor-enter(r0)
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0120 }
            java.util.List<java.lang.Integer> r2 = w     // Catch:{ all -> 0x0120 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0120 }
        L_0x000d:
            boolean r3 = r2.hasNext()     // Catch:{ all -> 0x0120 }
            if (r3 == 0) goto L_0x011e
            java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x0120 }
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ all -> 0x0120 }
            int r3 = r3.intValue()     // Catch:{ all -> 0x0120 }
            r4 = 0
            r5 = 1
            r6 = 0
            android.util.SparseArray<java.lang.Object[]> r7 = v     // Catch:{ Throwable -> 0x0064 }
            java.lang.Object r7 = r7.get(r3)     // Catch:{ Throwable -> 0x0064 }
            java.lang.Object[] r7 = (java.lang.Object[]) r7     // Catch:{ Throwable -> 0x0064 }
            if (r7 != 0) goto L_0x002b
            goto L_0x000d
        L_0x002b:
            r8 = r7[r6]     // Catch:{ Throwable -> 0x0064 }
            java.lang.ref.WeakReference r8 = (java.lang.ref.WeakReference) r8     // Catch:{ Throwable -> 0x0064 }
            java.lang.Object r8 = r8.get()     // Catch:{ Throwable -> 0x0064 }
            java.lang.Thread r8 = (java.lang.Thread) r8     // Catch:{ Throwable -> 0x0064 }
            r7 = r7[r5]     // Catch:{ Throwable -> 0x005f }
            java.lang.String r7 = (java.lang.String) r7     // Catch:{ Throwable -> 0x005f }
            if (r8 != 0) goto L_0x005c
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x005a }
            java.lang.String r9 = "Thread ("
            r4.<init>(r9)     // Catch:{ Throwable -> 0x005a }
            r4.append(r7)     // Catch:{ Throwable -> 0x005a }
            java.lang.String r9 = ", "
            r4.append(r9)     // Catch:{ Throwable -> 0x005a }
            r4.append(r3)     // Catch:{ Throwable -> 0x005a }
            java.lang.String r9 = ") has exited!"
            r4.append(r9)     // Catch:{ Throwable -> 0x005a }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x005a }
            com.uc.crashsdk.a.a.c(r4)     // Catch:{ Throwable -> 0x005a }
            goto L_0x000d
        L_0x005a:
            r4 = move-exception
            goto L_0x0068
        L_0x005c:
            if (r1 == r8) goto L_0x000d
            goto L_0x006b
        L_0x005f:
            r7 = move-exception
            r12 = r7
            r7 = r4
            r4 = r12
            goto L_0x0068
        L_0x0064:
            r7 = move-exception
            r8 = r4
            r4 = r7
            r7 = r8
        L_0x0068:
            com.uc.crashsdk.e.a((java.lang.Throwable) r4, (java.io.OutputStream) r13)     // Catch:{ all -> 0x0120 }
        L_0x006b:
            java.util.Locale r4 = java.util.Locale.US     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r9 = "Thread Name: '%s'\n"
            java.lang.Object[] r10 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00c1 }
            r10[r6] = r7     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r4 = java.lang.String.format(r4, r9, r10)     // Catch:{ Throwable -> 0x00c1 }
            byte[] r4 = r4.getBytes(r14)     // Catch:{ Throwable -> 0x00c1 }
            r13.write(r4)     // Catch:{ Throwable -> 0x00c1 }
            boolean r4 = r8.isDaemon()     // Catch:{ Throwable -> 0x00c1 }
            if (r4 == 0) goto L_0x0087
            java.lang.String r4 = " daemon"
            goto L_0x0089
        L_0x0087:
            java.lang.String r4 = ""
        L_0x0089:
            java.util.Locale r7 = java.util.Locale.US     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r9 = "\"%s\"%s prio=%d tid=%d %s\n"
            r10 = 5
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r11 = r8.getName()     // Catch:{ Throwable -> 0x00c1 }
            r10[r6] = r11     // Catch:{ Throwable -> 0x00c1 }
            r10[r5] = r4     // Catch:{ Throwable -> 0x00c1 }
            r4 = 2
            int r11 = r8.getPriority()     // Catch:{ Throwable -> 0x00c1 }
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)     // Catch:{ Throwable -> 0x00c1 }
            r10[r4] = r11     // Catch:{ Throwable -> 0x00c1 }
            r4 = 3
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x00c1 }
            r10[r4] = r3     // Catch:{ Throwable -> 0x00c1 }
            r3 = 4
            java.lang.Thread$State r4 = r8.getState()     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r4 = r4.toString()     // Catch:{ Throwable -> 0x00c1 }
            r10[r3] = r4     // Catch:{ Throwable -> 0x00c1 }
            java.lang.String r3 = java.lang.String.format(r7, r9, r10)     // Catch:{ Throwable -> 0x00c1 }
            byte[] r3 = r3.getBytes(r14)     // Catch:{ Throwable -> 0x00c1 }
            r13.write(r3)     // Catch:{ Throwable -> 0x00c1 }
            goto L_0x00c5
        L_0x00c1:
            r3 = move-exception
            com.uc.crashsdk.e.a((java.lang.Throwable) r3, (java.io.OutputStream) r13)     // Catch:{ all -> 0x0120 }
        L_0x00c5:
            java.lang.StackTraceElement[] r3 = r8.getStackTrace()     // Catch:{ Throwable -> 0x0102 }
            int r4 = r3.length     // Catch:{ Throwable -> 0x0102 }
            if (r4 != 0) goto L_0x00d5
            java.lang.String r4 = "  (no stack frames)"
            byte[] r4 = r4.getBytes(r14)     // Catch:{ Throwable -> 0x0102 }
            r13.write(r4)     // Catch:{ Throwable -> 0x0102 }
        L_0x00d5:
            int r4 = r3.length     // Catch:{ Throwable -> 0x0102 }
            r7 = 0
            r8 = 1
        L_0x00d8:
            if (r7 >= r4) goto L_0x0106
            r9 = r3[r7]     // Catch:{ Throwable -> 0x0102 }
            if (r8 != 0) goto L_0x00e7
            java.lang.String r8 = "\n"
            byte[] r8 = r8.getBytes(r14)     // Catch:{ Throwable -> 0x0102 }
            r13.write(r8)     // Catch:{ Throwable -> 0x0102 }
        L_0x00e7:
            java.util.Locale r8 = java.util.Locale.US     // Catch:{ Throwable -> 0x0102 }
            java.lang.String r10 = "  at %s"
            java.lang.Object[] r11 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x0102 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0102 }
            r11[r6] = r9     // Catch:{ Throwable -> 0x0102 }
            java.lang.String r8 = java.lang.String.format(r8, r10, r11)     // Catch:{ Throwable -> 0x0102 }
            byte[] r8 = r8.getBytes(r14)     // Catch:{ Throwable -> 0x0102 }
            r13.write(r8)     // Catch:{ Throwable -> 0x0102 }
            int r7 = r7 + 1
            r8 = 0
            goto L_0x00d8
        L_0x0102:
            r3 = move-exception
            com.uc.crashsdk.e.a((java.lang.Throwable) r3, (java.io.OutputStream) r13)     // Catch:{ all -> 0x0120 }
        L_0x0106:
            java.lang.String r3 = "\n"
            byte[] r3 = r3.getBytes(r14)     // Catch:{ Throwable -> 0x0118 }
            r13.write(r3)     // Catch:{ Throwable -> 0x0118 }
            byte[] r3 = r15.getBytes(r14)     // Catch:{ Throwable -> 0x0118 }
            r13.write(r3)     // Catch:{ Throwable -> 0x0118 }
            goto L_0x000d
        L_0x0118:
            r3 = move-exception
            com.uc.crashsdk.e.a((java.lang.Throwable) r3, (java.io.OutputStream) r13)     // Catch:{ all -> 0x0120 }
            goto L_0x000d
        L_0x011e:
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            return
        L_0x0120:
            r13 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0120 }
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.a(java.io.OutputStream, java.lang.String, java.lang.String):void");
    }

    public static int a(String str, int i2, int i3) {
        int i4;
        int i5;
        boolean z2;
        if (str == null || i2 <= 0) {
            return 0;
        }
        if (i2 > 1500) {
            com.uc.crashsdk.a.a.a("crashsdk", "createCachedInfo: capacity is too large!", (Throwable) null);
            return 0;
        }
        synchronized (x) {
            if (x.containsKey(str)) {
                i5 = ((Integer) x.get(str)[1]).intValue();
                i4 = LogType.addType(i5, i3);
            } else {
                i4 = i3;
                i5 = 0;
            }
            if (LogType.isForJava(i4) && !LogType.isForJava(i5)) {
                if (z >= 8) {
                    i4 = LogType.removeType(i4, 16);
                } else {
                    z++;
                }
            }
            if (LogType.isForNative(i4) && !LogType.isForNative(i5)) {
                if (A >= 8) {
                    i4 = LogType.removeType(i4, 1);
                } else {
                    A++;
                }
            }
            if ((i4 & 273) == 0) {
                z2 = false;
            } else {
                if (i5 == 0) {
                    y.add(str);
                }
                z2 = true;
            }
            if (z2) {
                if (b.d && LogType.isForNative(i3) && !JNIBridge.nativeCreateCachedInfo(str, i2)) {
                    i4 = LogType.removeType(i4, 1);
                }
                x.put(str, new Object[]{Integer.valueOf(i2), Integer.valueOf(i4), new ArrayList()});
            }
        }
        return i4;
    }

    public static int b(String str, String str2) {
        int i2 = 0;
        if (str == null || str2 == null) {
            return 0;
        }
        if (str2.length() > 2048) {
            str2 = str2.substring(0, 2048);
        }
        synchronized (x) {
            Object[] objArr = x.get(str);
            if (objArr != null) {
                int intValue = ((Integer) objArr[0]).intValue();
                int intValue2 = ((Integer) objArr[1]).intValue();
                List list = (List) objArr[2];
                if (list.size() >= intValue) {
                    list.remove(0);
                }
                list.add(str2);
                i2 = LogType.addType(0, 16);
                if (!b.d && LogType.isForNative(intValue2)) {
                    i2 = LogType.addType(i2, 1);
                }
            }
            if (b.d && JNIBridge.nativeAddCachedInfo(str, str2)) {
                i2 = LogType.addType(i2, 1);
            }
        }
        return i2;
    }

    static void j() {
        if (d || b.d) {
            synchronized (x) {
                for (String next : y) {
                    Object[] objArr = x.get(next);
                    int intValue = ((Integer) objArr[0]).intValue();
                    int intValue2 = ((Integer) objArr[1]).intValue();
                    List list = (List) objArr[2];
                    if (LogType.isForNative(intValue2) && JNIBridge.nativeCreateCachedInfo(next, intValue)) {
                        Iterator it = list.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                if (!JNIBridge.nativeAddCachedInfo(next, (String) it.next())) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            return;
        }
        throw new AssertionError();
    }

    static void b(OutputStream outputStream, String str, String str2, ArrayList<String> arrayList) {
        synchronized (x) {
            for (String next : y) {
                Object[] objArr = x.get(next);
                int intValue = ((Integer) objArr[0]).intValue();
                int intValue2 = ((Integer) objArr[1]).intValue();
                List<String> list = (List) objArr[2];
                if (arrayList == null) {
                    if (!LogType.isForJava(intValue2)) {
                    }
                } else if (!a((List<String>) arrayList, next)) {
                    continue;
                }
                try {
                    outputStream.write(String.format(Locale.US, "%s (%d/%d)\n", new Object[]{next, Integer.valueOf(list.size()), Integer.valueOf(intValue)}).getBytes(str));
                } catch (Throwable th) {
                    e.a(th, outputStream);
                }
                try {
                    for (String bytes : list) {
                        outputStream.write(bytes.getBytes(str));
                        outputStream.write("\n".getBytes(str));
                    }
                } catch (Throwable th2) {
                    e.a(th2, outputStream);
                }
                try {
                    outputStream.write("\n".getBytes(str));
                    outputStream.write(str2.getBytes(str));
                } catch (Throwable th3) {
                    e.a(th3, outputStream);
                }
            }
            if (arrayList != null && e.x()) {
                Iterator<String> it = arrayList.iterator();
                while (it.hasNext()) {
                    String next2 = it.next();
                    if (!a(y, next2)) {
                        e.a(outputStream, "CUSTOMCACHEDINFO", next2);
                    }
                }
            }
        }
        return;
    }

    static String k() {
        StringBuilder sb = new StringBuilder();
        synchronized (x) {
            boolean z2 = true;
            for (String next : y) {
                if (LogType.isForJava(((Integer) x.get(next)[1]).intValue())) {
                    if (!z2) {
                        sb.append("`");
                    }
                    sb.append(next);
                    z2 = false;
                }
            }
        }
        return sb.toString();
    }

    static String b(String str) {
        StringBuilder sb = new StringBuilder();
        synchronized (x) {
            Object[] objArr = x.get(str);
            int intValue = ((Integer) objArr[0]).intValue();
            List<String> list = (List) objArr[2];
            sb.append(String.format(Locale.US, "%s (%d/%d)\n", new Object[]{str, Integer.valueOf(list.size()), Integer.valueOf(intValue)}));
            for (String append : list) {
                sb.append(append);
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public static int a(int i2, String str) {
        if (g.a(str)) {
            str = Thread.currentThread().getName();
        }
        int i3 = 0;
        if (LogType.isForNative(i2)) {
            if (b.d) {
                synchronized (v) {
                    JNIBridge.nativeRegisterCurrentThread(str);
                }
                i3 = 1;
            } else {
                com.uc.crashsdk.a.a.a("crashsdk", "crashsdk so has not loaded!", (Throwable) null);
            }
        }
        if (!LogType.isForJava(i2)) {
            return i3;
        }
        a(str, Thread.currentThread());
        return i3 | 16;
    }

    public static boolean a(boolean z2) {
        int i2;
        if (!b.c) {
            com.uc.crashsdk.a.a.b("Unexp log not enabled, skip update unexp info!");
            return false;
        } else if (e.x() || b.D()) {
            return false;
        } else {
            if (z2) {
                f.a(B);
                i2 = 0;
            } else if (!b.y()) {
                com.uc.crashsdk.a.a.b("Stop update unexp info in background!");
                return false;
            } else if (h.E() <= 0) {
                return false;
            } else {
                if (f.b(B)) {
                    return true;
                }
                i2 = h.E() * 1000;
            }
            f.a(0, B, (long) i2);
            return true;
        }
    }

    static ArrayList<String> c(String str) {
        if (g.a(str)) {
            return null;
        }
        String[] split = str.split(";", 20);
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str2 : split) {
            if (!g.a(str2)) {
                arrayList.add(str2);
            }
        }
        return arrayList;
    }

    static String l() {
        if (!C) {
            g = b.a(b.l());
            C = true;
            if (g == null) {
                g = "";
            }
        }
        return g;
    }

    public static void m() {
        if (!D) {
            D = true;
            f.a(0, (Runnable) new e(202));
        } else if (b.d) {
            JNIBridge.nativeSyncStatus("ver", g, 0);
        }
    }

    public static void a(int i2) {
        switch (i2) {
            case 201:
                com.uc.crashsdk.a.a.b("Begin update unexp info ...");
                long currentTimeMillis = System.currentTimeMillis();
                if (b.d && c) {
                    JNIBridge.nativeUpdateUnexpInfo(h.E());
                }
                com.uc.crashsdk.a.a.b("Update unexp info took " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
                a(false);
                return;
            case 202:
                String format = String.format(Locale.US, "%s/%s/%s", new Object[]{h.P(), h.Q(), h.R()});
                if (e.a()) {
                    com.uc.crashsdk.a.a.a("crashsdk", "UUID: " + e.n());
                    com.uc.crashsdk.a.a.a("crashsdk", "Version: " + format);
                    com.uc.crashsdk.a.a.a("crashsdk", "Process Name: " + e.g());
                }
                g = l();
                if (b.d) {
                    JNIBridge.nativeSyncStatus("ver", g, 0);
                }
                boolean z2 = !format.equals(g);
                if (z2) {
                    b.a(b.l(), format);
                }
                if (z2 && h.v()) {
                    com.uc.crashsdk.a.a.b(String.format(Locale.US, "Is new version ('%s' -> '%s'), deleting old stats data!", new Object[]{g, format}));
                    b.s();
                    return;
                }
                return;
            default:
                if (!d) {
                    throw new AssertionError();
                }
                return;
        }
    }
}
