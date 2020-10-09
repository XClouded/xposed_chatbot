package com.uc.crashsdk;

import android.util.SparseArray;
import android.util.SparseIntArray;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.el.parse.Operators;
import com.uc.crashsdk.a.a;
import com.uc.crashsdk.a.e;
import com.uc.crashsdk.a.g;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

/* compiled from: ProGuard */
public class f {
    static final /* synthetic */ boolean a = (!f.class.desiredAssertionStatus());
    private static final Map<String, SparseIntArray> b = new HashMap();
    private static final Object c = new Object();
    private static final SparseArray<String> d = new SparseArray<>();
    private static final Object e = new Object();
    private static boolean f = false;

    static void a(int i) {
        a(i, 1);
    }

    static void a(int i, int i2) {
        if (i2 == 0) {
            a.c("Add stat for type " + i + " with count 0!");
            return;
        }
        a(b.c(), new e(751, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)}));
    }

    private static boolean b(int i, int i2) {
        try {
            b.u();
        } catch (Throwable th) {
            g.a(th);
        }
        try {
            String c2 = c(i);
            if (c2 == null) {
                a.a("crashsdk", "Stat type not exists: " + i, (Throwable) null);
                return false;
            }
            File file = new File(b.c());
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (Throwable th2) {
                g.a(th2);
            }
            StringBuffer a2 = a(file);
            if (g.a(a2)) {
                if (a2 == null) {
                    a2 = new StringBuffer();
                }
                a2.append(Operators.ARRAY_START_STR);
                a2.append(e.g());
                a2.append("]\n");
            }
            a(a2, c2, a(a2, c2) + i2);
            return a(file, a2);
        } catch (Exception e2) {
            g.a((Throwable) e2);
            return false;
        }
    }

    private static StringBuffer a(File file) {
        FileReader fileReader = null;
        if (!file.exists()) {
            return null;
        }
        char[] e2 = e();
        if (e2 == null) {
            a.a("crashsdk", "readCrashStatData alloc buffer failed!", (Throwable) null);
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileReader fileReader2 = new FileReader(file);
            try {
                int read = fileReader2.read(e2);
                if (read > 0) {
                    stringBuffer.append(e2, 0, read);
                }
                g.a((Closeable) fileReader2);
            } catch (Exception e3) {
                e = e3;
                fileReader = fileReader2;
                try {
                    g.a((Throwable) e);
                    g.a((Closeable) fileReader);
                    return stringBuffer;
                } catch (Throwable th) {
                    th = th;
                    g.a((Closeable) fileReader);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileReader = fileReader2;
                g.a((Closeable) fileReader);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            g.a((Throwable) e);
            g.a((Closeable) fileReader);
            return stringBuffer;
        }
        return stringBuffer;
    }

    private static int a(StringBuffer stringBuffer, String str) {
        int indexOf = stringBuffer.indexOf(str);
        if (indexOf < 0) {
            return 0;
        }
        int indexOf2 = stringBuffer.indexOf(SymbolExpUtil.SYMBOL_EQUAL, indexOf + str.length());
        if (indexOf2 < 0) {
            a.c(str + " line not contain '='!");
            return 0;
        }
        int i = indexOf2 + 1;
        int indexOf3 = stringBuffer.indexOf("\n", i);
        if (indexOf3 < 0) {
            indexOf3 = stringBuffer.length();
        }
        try {
            int parseInt = Integer.parseInt(stringBuffer.substring(i, indexOf3));
            if (parseInt < 0) {
                return 0;
            }
            return parseInt;
        } catch (NumberFormatException e2) {
            g.a((Throwable) e2);
            return 0;
        }
    }

    private static void a(StringBuffer stringBuffer, String str, int i) {
        int indexOf = stringBuffer.indexOf(str);
        if (indexOf >= 0) {
            int indexOf2 = stringBuffer.indexOf("\n", indexOf);
            if (indexOf2 < 0) {
                indexOf2 = stringBuffer.length();
            }
            stringBuffer.replace(indexOf, indexOf2, str + SymbolExpUtil.SYMBOL_EQUAL + String.valueOf(i));
        } else if (i > 0) {
            stringBuffer.append(str);
            stringBuffer.append(SymbolExpUtil.SYMBOL_EQUAL);
            stringBuffer.append(i);
            stringBuffer.append("\n");
        }
    }

    private static boolean a(File file, StringBuffer stringBuffer) {
        FileWriter fileWriter = null;
        try {
            FileWriter fileWriter2 = new FileWriter(file);
            try {
                String stringBuffer2 = stringBuffer.toString();
                fileWriter2.write(stringBuffer2, 0, stringBuffer2.length());
                g.a((Closeable) fileWriter2);
                return true;
            } catch (Exception e2) {
                e = e2;
                fileWriter = fileWriter2;
                try {
                    g.a((Throwable) e);
                    g.a((Closeable) fileWriter);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    g.a((Closeable) fileWriter);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fileWriter = fileWriter2;
                g.a((Closeable) fileWriter);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            g.a((Throwable) e);
            g.a((Closeable) fileWriter);
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0093, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean c(java.lang.String r13) {
        /*
            f()
            android.util.SparseArray<java.lang.String> r0 = d
            monitor-enter(r0)
            java.io.File r1 = new java.io.File     // Catch:{ all -> 0x009b }
            r1.<init>(r13)     // Catch:{ all -> 0x009b }
            java.lang.StringBuffer r13 = a((java.io.File) r1)     // Catch:{ all -> 0x009b }
            boolean r2 = com.uc.crashsdk.a.g.a((java.lang.StringBuffer) r13)     // Catch:{ all -> 0x009b }
            r3 = 0
            if (r2 == 0) goto L_0x0018
            monitor-exit(r0)     // Catch:{ all -> 0x009b }
            return r3
        L_0x0018:
            java.lang.String r2 = "["
            int r2 = r13.indexOf(r2)     // Catch:{ all -> 0x009b }
            r4 = 0
            if (r2 >= 0) goto L_0x002a
            java.lang.String r13 = "Can not found process name start!"
            java.lang.String r1 = "crashsdk"
            com.uc.crashsdk.a.a.a(r1, r13, r4)     // Catch:{ all -> 0x009b }
            monitor-exit(r0)     // Catch:{ all -> 0x009b }
            return r3
        L_0x002a:
            r5 = 1
            int r2 = r2 + r5
            java.lang.String r6 = "]"
            int r6 = r13.indexOf(r6, r2)     // Catch:{ all -> 0x009b }
            if (r6 >= 0) goto L_0x003d
            java.lang.String r13 = "Can not found process name end!"
            java.lang.String r1 = "crashsdk"
            com.uc.crashsdk.a.a.a(r1, r13, r4)     // Catch:{ all -> 0x009b }
            monitor-exit(r0)     // Catch:{ all -> 0x009b }
            return r3
        L_0x003d:
            java.lang.String r2 = r13.substring(r2, r6)     // Catch:{ all -> 0x009b }
            r4 = 0
            r6 = 0
        L_0x0043:
            android.util.SparseArray<java.lang.String> r7 = d     // Catch:{ all -> 0x0094 }
            int r7 = r7.size()     // Catch:{ all -> 0x0094 }
            if (r4 >= r7) goto L_0x008d
            android.util.SparseArray<java.lang.String> r7 = d     // Catch:{ all -> 0x0094 }
            int r7 = r7.keyAt(r4)     // Catch:{ all -> 0x0094 }
            android.util.SparseArray<java.lang.String> r8 = d     // Catch:{ all -> 0x0094 }
            java.lang.Object r8 = r8.get(r7)     // Catch:{ all -> 0x0094 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ all -> 0x0094 }
            int r9 = a((java.lang.StringBuffer) r13, (java.lang.String) r8)     // Catch:{ all -> 0x0094 }
            if (r9 <= 0) goto L_0x008a
            com.uc.crashsdk.a.h.a((java.lang.String) r2, (int) r7, (int) r9)     // Catch:{ all -> 0x0094 }
            java.util.Map<java.lang.String, android.util.SparseIntArray> r10 = b     // Catch:{ all -> 0x0094 }
            monitor-enter(r10)     // Catch:{ all -> 0x0094 }
            java.util.Map<java.lang.String, android.util.SparseIntArray> r11 = b     // Catch:{ all -> 0x0087 }
            java.lang.Object r11 = r11.get(r2)     // Catch:{ all -> 0x0087 }
            android.util.SparseIntArray r11 = (android.util.SparseIntArray) r11     // Catch:{ all -> 0x0087 }
            if (r11 != 0) goto L_0x0079
            android.util.SparseIntArray r11 = new android.util.SparseIntArray     // Catch:{ all -> 0x0087 }
            r11.<init>()     // Catch:{ all -> 0x0087 }
            java.util.Map<java.lang.String, android.util.SparseIntArray> r12 = b     // Catch:{ all -> 0x0087 }
            r12.put(r2, r11)     // Catch:{ all -> 0x0087 }
        L_0x0079:
            int r12 = r11.get(r7, r3)     // Catch:{ all -> 0x0087 }
            int r12 = r12 + r9
            r11.put(r7, r12)     // Catch:{ all -> 0x0087 }
            monitor-exit(r10)     // Catch:{ all -> 0x0087 }
            a(r13, r8, r3)     // Catch:{ all -> 0x0094 }
            r6 = 1
            goto L_0x008a
        L_0x0087:
            r2 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x0087 }
            throw r2     // Catch:{ all -> 0x0094 }
        L_0x008a:
            int r4 = r4 + 1
            goto L_0x0043
        L_0x008d:
            if (r6 == 0) goto L_0x0092
            a((java.io.File) r1, (java.lang.StringBuffer) r13)     // Catch:{ all -> 0x009b }
        L_0x0092:
            monitor-exit(r0)     // Catch:{ all -> 0x009b }
            return r5
        L_0x0094:
            r2 = move-exception
            if (r6 == 0) goto L_0x009a
            a((java.io.File) r1, (java.lang.StringBuffer) r13)     // Catch:{ all -> 0x009b }
        L_0x009a:
            throw r2     // Catch:{ all -> 0x009b }
        L_0x009b:
            r13 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x009b }
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.f.c(java.lang.String):boolean");
    }

    static int a(boolean z) {
        int i;
        synchronized (b) {
            if (z) {
                try {
                    String g = e.g();
                    i = d(g);
                    b.remove(g);
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                i = 0;
                for (String d2 : b.keySet()) {
                    if (d(d2)) {
                        i++;
                    }
                }
                b.clear();
            }
        }
        return i;
    }

    private static boolean d(String str) {
        SparseIntArray sparseIntArray = b.get(str);
        if (sparseIntArray == null) {
            return false;
        }
        for (int i = 0; i < sparseIntArray.size(); i++) {
            int keyAt = sparseIntArray.keyAt(i);
            d.a(str, keyAt, sparseIntArray.get(keyAt));
        }
        return true;
    }

    static int b(boolean z) {
        int i;
        synchronized (b) {
            if (z) {
                try {
                    String g = e.g();
                    if (b.containsKey(g)) {
                        b.remove(g);
                        i = 1;
                    } else {
                        i = 0;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                i = b.size();
                b.clear();
            }
        }
        return i;
    }

    private static char[] e() {
        char[] cArr = null;
        int i = 1024;
        while (cArr == null && i > 0) {
            try {
                cArr = new char[i];
            } catch (Throwable unused) {
                i /= 2;
                if (i < 512) {
                    break;
                }
            }
        }
        return cArr;
    }

    private static boolean a(String str, e eVar) {
        return b.a(c, str, eVar);
    }

    static boolean a(String str) {
        return a(str, new e(752, new Object[]{str}));
    }

    private static String c(int i) {
        String str;
        f();
        synchronized (d) {
            str = d.get(i);
        }
        return str;
    }

    private static void f() {
        synchronized (d) {
            if (d.size() == 0) {
                d.put(100, "start_pv");
                d.put(1, "all_all");
                d.put(2, "all_fg");
                d.put(3, "java_fg");
                d.put(4, "java_bg");
                d.put(7, "native_fg");
                d.put(8, "native_bg");
                d.put(27, "native_anr_fg");
                d.put(28, "native_anr_bg");
                d.put(9, "native_ok");
                d.put(10, "unexp_anr");
                d.put(29, "unexp_lowmem");
                d.put(30, "unexp_killed");
                d.put(31, "unexp_exit");
                d.put(32, "unexp_restart");
                d.put(11, "unexp_fg");
                d.put(12, "unexp_bg");
                d.put(13, "log_up_succ");
                d.put(14, "log_up_fail");
                d.put(15, "log_empty");
                d.put(16, "log_abd_all");
                d.put(22, "log_abd_crash");
                d.put(23, "log_abd_custom");
                d.put(17, "log_large");
                d.put(18, "log_up_all");
                d.put(19, "log_up_bytes");
                d.put(20, "log_up_crash");
                d.put(21, "log_up_custom");
                d.put(24, "log_zipped");
                d.put(25, "log_renamed");
                d.put(26, "log_safe_skip");
            }
        }
    }

    private static File[] g() {
        return new File(h.S()).listFiles(new g());
    }

    static int a() {
        File[] g = g();
        if (g == null) {
            return 0;
        }
        int i = 0;
        for (File absolutePath : g) {
            if (a(absolutePath.getAbsolutePath())) {
                i++;
            }
        }
        return i;
    }

    static boolean b(String str) {
        return a(str, new e(753, new Object[]{str}));
    }

    static int b() {
        File[] g = g();
        if (g == null) {
            return 0;
        }
        int i = 0;
        for (File absolutePath : g) {
            if (b(absolutePath.getAbsolutePath())) {
                i++;
            }
        }
        return i;
    }

    static void c() {
        if (!f) {
            synchronized (e) {
                if (!f) {
                    f = true;
                    if (b.p() || b.q()) {
                        a(1, 1);
                        if (b.p()) {
                            a(2, 1);
                        }
                    }
                    a(100, 1);
                }
            }
        }
    }

    static void d() {
        com.uc.crashsdk.a.f.a(1, new e(700), TBToast.Duration.MEDIUM);
    }

    public static void b(int i) {
        if (i == 700) {
            c();
        }
    }

    public static boolean a(int i, Object[] objArr) {
        switch (i) {
            case 751:
                if (a || objArr != null) {
                    return b(objArr[0].intValue(), objArr[1].intValue());
                }
                throw new AssertionError();
            case 752:
                if (a || objArr != null) {
                    return c(objArr[0]);
                }
                throw new AssertionError();
            case 753:
                if (a || objArr != null) {
                    File file = new File(objArr[0]);
                    if (!file.exists()) {
                        return false;
                    }
                    file.delete();
                    return true;
                }
                throw new AssertionError();
            default:
                return false;
        }
    }
}
