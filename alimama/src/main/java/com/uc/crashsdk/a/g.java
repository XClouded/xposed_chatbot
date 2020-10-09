package com.uc.crashsdk.a;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import com.taobao.weex.common.Constants;
import com.uc.crashsdk.JNIBridge;
import com.uc.crashsdk.b;
import com.uc.crashsdk.h;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/* compiled from: ProGuard */
public class g {
    static final /* synthetic */ boolean a = (!g.class.desiredAssertionStatus());
    private static Context b;
    private static String c = null;
    private static String d = null;
    private static String e = null;
    private static String f = null;
    private static boolean g = false;
    private static final Object h = new Object();

    public static void a(File file, File file2) {
        FileInputStream fileInputStream;
        byte[] bArr = new byte[524288];
        File parentFile = file2.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (file2.isDirectory()) {
            file2 = new File(file2, file.getName());
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file2);
                while (true) {
                    try {
                        int read = fileInputStream.read(bArr);
                        if (read != -1) {
                            fileOutputStream2.write(bArr, 0, read);
                        } else {
                            a((Closeable) fileInputStream);
                            a((Closeable) fileOutputStream2);
                            return;
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        a((Closeable) fileInputStream);
                        a((Closeable) fileOutputStream);
                        throw th;
                    }
                }
            } catch (Throwable th2) {
                th = th2;
                a((Closeable) fileInputStream);
                a((Closeable) fileOutputStream);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            a((Closeable) fileInputStream);
            a((Closeable) fileOutputStream);
            throw th;
        }
    }

    public static boolean a(File file) {
        String[] list;
        if (file.isDirectory() && (list = file.list()) != null) {
            for (String file2 : list) {
                if (!a(new File(file, file2))) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static String b(File file) {
        if (!file.exists()) {
            return "";
        }
        FileInputStream fileInputStream = null;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(file);
            try {
                byte[] bArr = new byte[256];
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int read = fileInputStream2.read(bArr);
                    if (read > 0) {
                        sb.append(new String(bArr, 0, read));
                    } else {
                        String sb2 = sb.toString();
                        a((Closeable) fileInputStream2);
                        return sb2;
                    }
                }
            } catch (Throwable th) {
                th = th;
                a((Closeable) fileInputStream2);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            a(th, false);
            a((Closeable) fileInputStream);
            return "";
        }
    }

    public static String a(File file, int i) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                byte[] bArr = new byte[i];
                int read = fileInputStream.read(bArr);
                if (read > 0) {
                    String str = new String(bArr, 0, read);
                    a((Closeable) fileInputStream);
                    return str;
                }
            } catch (Throwable th) {
                th = th;
                a((Closeable) fileInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            a((Closeable) fileInputStream);
            throw th;
        }
        a((Closeable) fileInputStream);
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v1, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v5, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v7, resolved type: java.io.BufferedReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v8, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v9, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v11, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v12, resolved type: java.io.FileReader} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v13, resolved type: java.io.FileReader} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<java.lang.String> b(java.io.File r5, int r6) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            r2 = 0
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ Throwable -> 0x0037, all -> 0x0034 }
            r3.<init>(r5)     // Catch:{ Throwable -> 0x0037, all -> 0x0034 }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x0030, all -> 0x002e }
            r4 = 512(0x200, float:7.175E-43)
            r5.<init>(r3, r4)     // Catch:{ Throwable -> 0x0030, all -> 0x002e }
            r2 = 0
        L_0x0014:
            java.lang.String r4 = r5.readLine()     // Catch:{ Throwable -> 0x002c, all -> 0x002a }
            if (r4 == 0) goto L_0x0023
            r0.add(r4)     // Catch:{ Throwable -> 0x002c, all -> 0x002a }
            int r2 = r2 + 1
            if (r6 <= 0) goto L_0x0014
            if (r2 < r6) goto L_0x0014
        L_0x0023:
            a((java.io.Closeable) r3)
        L_0x0026:
            a((java.io.Closeable) r5)
            goto L_0x0040
        L_0x002a:
            r6 = move-exception
            goto L_0x0043
        L_0x002c:
            r6 = move-exception
            goto L_0x0032
        L_0x002e:
            r6 = move-exception
            goto L_0x0044
        L_0x0030:
            r6 = move-exception
            r5 = r2
        L_0x0032:
            r2 = r3
            goto L_0x0039
        L_0x0034:
            r6 = move-exception
            r3 = r2
            goto L_0x0044
        L_0x0037:
            r6 = move-exception
            r5 = r2
        L_0x0039:
            a((java.lang.Throwable) r6, (boolean) r1)     // Catch:{ all -> 0x0041 }
            a((java.io.Closeable) r2)
            goto L_0x0026
        L_0x0040:
            return r0
        L_0x0041:
            r6 = move-exception
            r3 = r2
        L_0x0043:
            r2 = r5
        L_0x0044:
            a((java.io.Closeable) r3)
            a((java.io.Closeable) r2)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.g.b(java.io.File, int):java.util.ArrayList");
    }

    public static boolean a(File file, byte[] bArr) {
        FileOutputStream fileOutputStream = null;
        try {
            FileOutputStream fileOutputStream2 = new FileOutputStream(file);
            try {
                fileOutputStream2.write(bArr);
                fileOutputStream2.flush();
                a((Closeable) fileOutputStream2);
                return true;
            } catch (Throwable th) {
                th = th;
                fileOutputStream = fileOutputStream2;
                a((Closeable) fileOutputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            a(th, false);
            a((Closeable) fileOutputStream);
            return false;
        }
    }

    public static boolean a(File file, String str) {
        FileWriter fileWriter = null;
        try {
            FileWriter fileWriter2 = new FileWriter(file);
            try {
                fileWriter2.write(str, 0, str.length());
                a((Closeable) fileWriter2);
                return true;
            } catch (Throwable th) {
                th = th;
                fileWriter = fileWriter2;
                a((Closeable) fileWriter);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            a(th, false);
            a((Closeable) fileWriter);
            return false;
        }
    }

    public static void a(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable th) {
                a(th, true);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0027, code lost:
        if (r2.toLowerCase().startsWith("http") != false) goto L_0x0029;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String a(java.lang.String r2, java.lang.String r3, boolean r4) {
        /*
            java.io.File r0 = new java.io.File
            r0.<init>(r2)
            boolean r0 = r0.exists()
            r1 = 0
            if (r0 == 0) goto L_0x002a
            java.lang.String r2 = com.uc.crashsdk.a.b.a((java.lang.String) r2)
            boolean r0 = a((java.lang.String) r2)
            if (r0 == 0) goto L_0x0017
            goto L_0x002a
        L_0x0017:
            if (r4 == 0) goto L_0x0029
            java.lang.String r2 = r2.trim()
            java.lang.String r4 = r2.toLowerCase()
            java.lang.String r0 = "http"
            boolean r4 = r4.startsWith(r0)
            if (r4 == 0) goto L_0x002a
        L_0x0029:
            r1 = r2
        L_0x002a:
            if (r1 != 0) goto L_0x002d
            goto L_0x002e
        L_0x002d:
            r3 = r1
        L_0x002e:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.g.a(java.lang.String, java.lang.String, boolean):java.lang.String");
    }

    public static boolean a(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean a(StringBuffer stringBuffer) {
        return stringBuffer == null || stringBuffer.length() == 0;
    }

    public static boolean b(String str) {
        return !a(str);
    }

    public static long c(String str) {
        if (!a(str)) {
            try {
                long parseLong = Long.parseLong(str.trim());
                if (parseLong < 0) {
                    return 0;
                }
                return parseLong;
            } catch (NumberFormatException unused) {
            }
        }
        return 0;
    }

    public static void a(Throwable th) {
        a(th, false);
    }

    public static void b(Throwable th) {
        a(th, true);
    }

    private static void a(Throwable th, boolean z) {
        if (!z) {
            try {
                if (!h.K()) {
                    return;
                }
            } catch (Throwable unused) {
                return;
            }
        }
        th.printStackTrace();
    }

    public static void a(Context context) {
        if (b != null) {
            a.c("mContext is existed");
        }
        b = context;
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        c = applicationInfo.dataDir;
        d = applicationInfo.sourceDir;
    }

    public static Context a() {
        return b;
    }

    public static String b() {
        return c;
    }

    public static String c() {
        return d;
    }

    public static Object d(String str) {
        return b.getSystemService(str);
    }

    public static boolean d() {
        if (f()) {
            return true;
        }
        return e();
    }

    public static boolean e() {
        return Build.TAGS != null && Build.TAGS.contains("test-keys");
    }

    public static boolean f() {
        int indexOf;
        String h2 = h();
        if (!a(h2) && (indexOf = h2.indexOf(" root ")) > 0) {
            String substring = h2.substring(0, indexOf);
            if (substring.contains(Constants.Name.X) || substring.contains("s")) {
                return true;
            }
        }
        return false;
    }

    public static String g() {
        k();
        if (a(e)) {
            return "";
        }
        return e;
    }

    public static String h() {
        k();
        if (a(f)) {
            return "";
        }
        return f;
    }

    public static void i() {
        f.a(0, new e(800), 15000);
    }

    public static void a(int i) {
        if (i == 800) {
            k();
        } else if (!a) {
            throw new AssertionError();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002a, code lost:
        r2 = r2.trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void k() {
        /*
            boolean r0 = g
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.Object r0 = h
            monitor-enter(r0)
            boolean r1 = g     // Catch:{ all -> 0x0072 }
            if (r1 == 0) goto L_0x000e
            monitor-exit(r0)     // Catch:{ all -> 0x0072 }
            return
        L_0x000e:
            r1 = 3
            java.lang.String[] r2 = new java.lang.String[r1]     // Catch:{ all -> 0x0072 }
            java.lang.String r3 = "sh"
            r4 = 0
            r2[r4] = r3     // Catch:{ all -> 0x0072 }
            java.lang.String r3 = "-c"
            r5 = 1
            r2[r5] = r3     // Catch:{ all -> 0x0072 }
            java.lang.String r3 = "type su"
            r6 = 2
            r2[r6] = r3     // Catch:{ all -> 0x0072 }
            java.lang.String r2 = a((java.lang.String[]) r2)     // Catch:{ all -> 0x0072 }
            boolean r3 = a((java.lang.String) r2)     // Catch:{ all -> 0x0072 }
            if (r3 != 0) goto L_0x006b
            java.lang.String r2 = r2.trim()     // Catch:{ all -> 0x0072 }
            r3 = 32
            int r3 = r2.indexOf(r3)     // Catch:{ all -> 0x0072 }
            if (r3 <= 0) goto L_0x006b
            java.lang.String r7 = "/su"
            boolean r7 = r2.contains(r7)     // Catch:{ all -> 0x0072 }
            if (r7 == 0) goto L_0x006b
            r7 = 47
            int r3 = r3 + r5
            int r3 = r2.indexOf(r7, r3)     // Catch:{ all -> 0x0072 }
            if (r3 <= 0) goto L_0x006b
            java.lang.String r2 = r2.substring(r3)     // Catch:{ all -> 0x0072 }
            e = r2     // Catch:{ all -> 0x0072 }
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ all -> 0x0072 }
            java.lang.String r2 = "ls"
            r1[r4] = r2     // Catch:{ all -> 0x0072 }
            java.lang.String r2 = "-l"
            r1[r5] = r2     // Catch:{ all -> 0x0072 }
            java.lang.String r2 = e     // Catch:{ all -> 0x0072 }
            r1[r6] = r2     // Catch:{ all -> 0x0072 }
            java.lang.String r1 = a((java.lang.String[]) r1)     // Catch:{ all -> 0x0072 }
            boolean r2 = a((java.lang.String) r1)     // Catch:{ all -> 0x0072 }
            if (r2 != 0) goto L_0x006b
            java.lang.String r1 = r1.trim()     // Catch:{ all -> 0x0072 }
            f = r1     // Catch:{ all -> 0x0072 }
        L_0x006b:
            g = r5     // Catch:{ all -> 0x0072 }
            monitor-exit(r0)     // Catch:{ all -> 0x0072 }
            j()
            return
        L_0x0072:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0072 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.crashsdk.a.g.k():void");
    }

    public static void j() {
        if (b.d && g) {
            JNIBridge.nativeSyncInfo("subin", e, 0, 0);
            JNIBridge.nativeSyncInfo("supmi", f, 0, 0);
        }
    }

    private static String a(String[] strArr) {
        Throwable th;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            inputStreamReader = new InputStreamReader(Runtime.getRuntime().exec(strArr).getInputStream());
            try {
                bufferedReader = new BufferedReader(inputStreamReader, 512);
            } catch (Throwable th2) {
                th = th2;
                a((Closeable) bufferedReader2);
                a((Closeable) inputStreamReader);
                throw th;
            }
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                    } else {
                        String trim = sb.toString().trim();
                        a((Closeable) bufferedReader);
                        a((Closeable) inputStreamReader);
                        return trim;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                a(th, false);
                a((Closeable) bufferedReader);
                a((Closeable) inputStreamReader);
                return null;
            }
        } catch (Throwable th4) {
            th = th4;
            inputStreamReader = null;
            a((Closeable) bufferedReader2);
            a((Closeable) inputStreamReader);
            throw th;
        }
    }
}
