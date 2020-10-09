package com.uc.webview.export.internal.utility;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.Build;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.cyclone.UCKnownException;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.UCMRunningInfo;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.br;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class k {
    public static final String[] a = {"armeabi-v7a", "arm64-v8a", "armeabi", "x86"};
    public static String b = "3032";
    private static final Long c = 10000L;
    private static final HashMap<String, Pair<Long, Object>> d = new HashMap<>();
    private static a e = a.NOT_INITED;
    private static a f = a.NOT_INITED;
    private static final Map<String, String> g = new l();
    private static volatile Method h = null;
    private static final String[] i = {b};
    private static boolean j = false;
    private static boolean k = false;
    private static volatile String l = null;
    private static String m;

    /* compiled from: U4Source */
    enum a {
        NOT_INITED,
        ENABLE,
        DISABLE
    }

    public static boolean a(Object obj) {
        return obj == null;
    }

    public static boolean a(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean b(String str) {
        return !a(str) && "true".equalsIgnoreCase(str);
    }

    public static boolean a(Boolean bool) {
        return bool != null && bool.booleanValue();
    }

    public static boolean b(Boolean bool) {
        return bool == null || !bool.booleanValue();
    }

    public static boolean b(Object obj) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof Integer) {
            return 1 == ((Integer) obj).intValue();
        }
        if (!(obj instanceof String)) {
            return false;
        }
        String lowerCase = ((String) obj).trim().toLowerCase(Locale.getDefault());
        return "1".equals(lowerCase) || "yes".equals(lowerCase) || "true".equals(lowerCase);
    }

    public static String a() {
        try {
            File[] listFiles = new File("/sys/devices/system/cpu/").listFiles(new m());
            Log.d("Utils", "CPU Count: " + listFiles.length);
            return String.valueOf(listFiles.length);
        } catch (Throwable th) {
            Log.d("Utils", "CPU Count: Failed.");
            th.printStackTrace();
            return "1";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0044  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String h(java.lang.String r5) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            java.io.FileReader r2 = new java.io.FileReader     // Catch:{ Throwable -> 0x0036, all -> 0x0032 }
            r2.<init>(r5)     // Catch:{ Throwable -> 0x0036, all -> 0x0032 }
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x002d, all -> 0x002a }
            r5.<init>(r2)     // Catch:{ Throwable -> 0x002d, all -> 0x002a }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028 }
            r1.<init>()     // Catch:{ Throwable -> 0x0028 }
        L_0x0012:
            java.lang.String r3 = r5.readLine()     // Catch:{ Throwable -> 0x0028 }
            if (r3 == 0) goto L_0x001c
            r1.append(r3)     // Catch:{ Throwable -> 0x0028 }
            goto L_0x0012
        L_0x001c:
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0028 }
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
            r0 = r1
            goto L_0x0047
        L_0x0028:
            r1 = move-exception
            goto L_0x003a
        L_0x002a:
            r0 = move-exception
            r5 = r1
            goto L_0x0049
        L_0x002d:
            r5 = move-exception
            r4 = r1
            r1 = r5
            r5 = r4
            goto L_0x003a
        L_0x0032:
            r0 = move-exception
            r5 = r1
            r2 = r5
            goto L_0x0049
        L_0x0036:
            r5 = move-exception
            r2 = r1
            r1 = r5
            r5 = r2
        L_0x003a:
            r1.printStackTrace()     // Catch:{ all -> 0x0048 }
            if (r2 == 0) goto L_0x0042
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
        L_0x0042:
            if (r5 == 0) goto L_0x0047
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
        L_0x0047:
            return r0
        L_0x0048:
            r0 = move-exception
        L_0x0049:
            if (r2 == 0) goto L_0x004e
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
        L_0x004e:
            if (r5 == 0) goto L_0x0053
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
        L_0x0053:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.h(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x0049 A[SYNTHETIC, Splitter:B:28:0x0049] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x004e A[SYNTHETIC, Splitter:B:32:0x004e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(java.io.File r9, java.io.File r10) throws java.io.IOException {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x0044 }
            r1.<init>(r9)     // Catch:{ all -> 0x0044 }
            java.nio.channels.FileChannel r1 = r1.getChannel()     // Catch:{ all -> 0x0044 }
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ all -> 0x0041 }
            r2.<init>(r10)     // Catch:{ all -> 0x0041 }
            java.nio.channels.FileChannel r8 = r2.getChannel()     // Catch:{ all -> 0x0041 }
            r4 = 0
            long r6 = r1.size()     // Catch:{ all -> 0x003f }
            r2 = r8
            r3 = r1
            long r2 = r2.transferFrom(r3, r4, r6)     // Catch:{ all -> 0x003f }
            long r4 = r9.length()     // Catch:{ all -> 0x003f }
            int r9 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r9 != 0) goto L_0x0034
            if (r1 == 0) goto L_0x002c
            r1.close()     // Catch:{ Throwable -> 0x002c }
        L_0x002c:
            if (r8 == 0) goto L_0x0033
            r8.close()     // Catch:{ Throwable -> 0x0032 }
            goto L_0x0033
        L_0x0032:
            return
        L_0x0033:
            return
        L_0x0034:
            r10.delete()     // Catch:{ all -> 0x003f }
            java.lang.RuntimeException r9 = new java.lang.RuntimeException     // Catch:{ all -> 0x003f }
            java.lang.String r10 = "Size mismatch."
            r9.<init>(r10)     // Catch:{ all -> 0x003f }
            throw r9     // Catch:{ all -> 0x003f }
        L_0x003f:
            r9 = move-exception
            goto L_0x0047
        L_0x0041:
            r9 = move-exception
            r8 = r0
            goto L_0x0047
        L_0x0044:
            r9 = move-exception
            r1 = r0
            r8 = r1
        L_0x0047:
            if (r1 == 0) goto L_0x004c
            r1.close()     // Catch:{ Throwable -> 0x004c }
        L_0x004c:
            if (r8 == 0) goto L_0x0051
            r8.close()     // Catch:{ Throwable -> 0x0051 }
        L_0x0051:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.a(java.io.File, java.io.File):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(12:10|(3:12|(2:14|(1:16))|17)|18|19|20|(3:22|23|24)|25|26|(3:27|29|(3:31|32|(1:34)(2:35|36)))|42|43|(2:45|(2:47|48)(1:49))(2:50|51)) */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c8, code lost:
        if (r8.getName().equals("libar_pak_kr_uc.so") == false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ca, code lost:
        android.util.Log.i("ThinEnvTask", "linkOrCopyFile Time:" + r2.getMilis());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00e1, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x013d, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0145, code lost:
        throw new com.uc.webview.export.internal.setup.UCSetupException(1007, r8);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x007c */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x00e2 */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b1 A[SYNTHETIC, Splitter:B:31:0x00b1] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x010b A[Catch:{ Throwable -> 0x013d }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0123 A[Catch:{ Throwable -> 0x013d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.io.File a(java.io.File r8, java.io.File r9, java.io.File r10) {
        /*
            boolean r0 = r9.exists()
            if (r0 == 0) goto L_0x0007
            return r9
        L_0x0007:
            boolean r0 = r10.exists()
            if (r0 == 0) goto L_0x0026
            long r0 = r8.length()
            long r2 = r10.length()
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0026
            long r0 = r8.lastModified()
            long r2 = r10.lastModified()
            int r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r4 != 0) goto L_0x0026
            return r10
        L_0x0026:
            java.io.File r0 = new java.io.File
            java.lang.String r1 = r10.getParent()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "bak_"
            r2.<init>(r3)
            java.lang.String r3 = r10.getName()
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r1, r2)
            boolean r1 = r0.exists()
            if (r1 == 0) goto L_0x006a
            long r1 = r8.length()
            long r3 = r0.length()
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 != 0) goto L_0x0067
            long r1 = r8.lastModified()
            r0.setLastModified(r1)
            long r1 = r0.lastModified()
            long r3 = r8.lastModified()
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 == 0) goto L_0x0067
            return r0
        L_0x0067:
            r0.delete()
        L_0x006a:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00e2 }
            r2 = 21
            if (r1 < r2) goto L_0x007c
            java.lang.String r1 = r8.getAbsolutePath()     // Catch:{ Exception -> 0x007c }
            java.lang.String r2 = r9.getAbsolutePath()     // Catch:{ Exception -> 0x007c }
            android.system.Os.symlink(r1, r2)     // Catch:{ Exception -> 0x007c }
            return r9
        L_0x007c:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r2 = "ln -s "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r2 = r8.getAbsolutePath()     // Catch:{ Throwable -> 0x00e2 }
            r1.append(r2)     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r2 = " "
            r1.append(r2)     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r2 = r9.getAbsolutePath()     // Catch:{ Throwable -> 0x00e2 }
            r1.append(r2)     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00e2 }
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x00e2 }
            java.lang.Process r1 = r2.exec(r1)     // Catch:{ Throwable -> 0x00e2 }
            com.uc.webview.export.cyclone.UCElapseTime r2 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ Throwable -> 0x00e2 }
            r2.<init>()     // Catch:{ Throwable -> 0x00e2 }
        L_0x00a7:
            long r3 = r2.getMilis()     // Catch:{ Throwable -> 0x00e2 }
            r5 = 500(0x1f4, double:2.47E-321)
            int r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r7 >= 0) goto L_0x00be
            int r3 = r1.exitValue()     // Catch:{ IllegalThreadStateException -> 0x00a7 }
            if (r3 != 0) goto L_0x00b8
            goto L_0x00be
        L_0x00b8:
            java.lang.Throwable r3 = new java.lang.Throwable     // Catch:{ IllegalThreadStateException -> 0x00a7 }
            r3.<init>()     // Catch:{ IllegalThreadStateException -> 0x00a7 }
            throw r3     // Catch:{ IllegalThreadStateException -> 0x00a7 }
        L_0x00be:
            java.lang.String r1 = r8.getName()     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r3 = "libar_pak_kr_uc.so"
            boolean r1 = r1.equals(r3)     // Catch:{ Throwable -> 0x00e2 }
            if (r1 == 0) goto L_0x00e1
            java.lang.String r1 = "ThinEnvTask"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r4 = "linkOrCopyFile Time:"
            r3.<init>(r4)     // Catch:{ Throwable -> 0x00e2 }
            long r4 = r2.getMilis()     // Catch:{ Throwable -> 0x00e2 }
            r3.append(r4)     // Catch:{ Throwable -> 0x00e2 }
            java.lang.String r2 = r3.toString()     // Catch:{ Throwable -> 0x00e2 }
            android.util.Log.i(r1, r2)     // Catch:{ Throwable -> 0x00e2 }
        L_0x00e1:
            return r9
        L_0x00e2:
            r10.delete()     // Catch:{ Throwable -> 0x013d }
            java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x013d }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013d }
            r1.<init>()     // Catch:{ Throwable -> 0x013d }
            java.lang.String r2 = r10.getAbsolutePath()     // Catch:{ Throwable -> 0x013d }
            r1.append(r2)     // Catch:{ Throwable -> 0x013d }
            java.lang.String r2 = ".tmp"
            r1.append(r2)     // Catch:{ Throwable -> 0x013d }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x013d }
            r9.<init>(r1)     // Catch:{ Throwable -> 0x013d }
            r9.createNewFile()     // Catch:{ Throwable -> 0x013d }
            a((java.io.File) r8, (java.io.File) r9)     // Catch:{ Throwable -> 0x013d }
            boolean r1 = r9.renameTo(r10)     // Catch:{ Throwable -> 0x013d }
            if (r1 == 0) goto L_0x0123
            long r1 = r8.lastModified()     // Catch:{ Throwable -> 0x013d }
            r10.setLastModified(r1)     // Catch:{ Throwable -> 0x013d }
            long r1 = r10.lastModified()     // Catch:{ Throwable -> 0x013d }
            long r8 = r8.lastModified()     // Catch:{ Throwable -> 0x013d }
            int r3 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r3 == 0) goto L_0x0122
            r10.renameTo(r0)     // Catch:{ Throwable -> 0x013d }
            return r0
        L_0x0122:
            return r10
        L_0x0123:
            r9.delete()     // Catch:{ Throwable -> 0x013d }
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Throwable -> 0x013d }
            r0 = 1005(0x3ed, float:1.408E-42)
            java.lang.String r1 = "Rename [%s] to [%s] failed."
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x013d }
            r3 = 0
            r2[r3] = r9     // Catch:{ Throwable -> 0x013d }
            r9 = 1
            r2[r9] = r10     // Catch:{ Throwable -> 0x013d }
            java.lang.String r9 = java.lang.String.format(r1, r2)     // Catch:{ Throwable -> 0x013d }
            r8.<init>((int) r0, (java.lang.String) r9)     // Catch:{ Throwable -> 0x013d }
            throw r8     // Catch:{ Throwable -> 0x013d }
        L_0x013d:
            r8 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r9 = new com.uc.webview.export.internal.setup.UCSetupException
            r10 = 1007(0x3ef, float:1.411E-42)
            r9.<init>((int) r10, (java.lang.Throwable) r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.a(java.io.File, java.io.File, java.io.File):java.io.File");
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x00bd A[SYNTHETIC, Splitter:B:41:0x00bd] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00c3 A[Catch:{ Throwable -> 0x00e7, Throwable -> 0x013c }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00db A[Catch:{ Throwable -> 0x00e7, Throwable -> 0x013c }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00e3 A[Catch:{ Throwable -> 0x00e7, Throwable -> 0x013c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File[] a(java.io.File[] r11, java.io.File[] r12, java.io.File[] r13) {
        /*
            int r0 = r11.length
            java.io.File[] r0 = new java.io.File[r0]
            r1 = 2
            r2 = 1
            r3 = 0
            int r4 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00e7 }
            r5 = 21
            if (r4 < r5) goto L_0x0034
            r4 = 0
        L_0x000d:
            int r5 = r11.length     // Catch:{ Throwable -> 0x00e7 }
            if (r4 >= r5) goto L_0x0033
            r5 = r12[r4]     // Catch:{ Throwable -> 0x00e7 }
            boolean r5 = r5.exists()     // Catch:{ Throwable -> 0x00e7 }
            if (r5 == 0) goto L_0x001d
            r5 = r12[r4]     // Catch:{ Throwable -> 0x00e7 }
            r0[r4] = r5     // Catch:{ Throwable -> 0x00e7 }
            goto L_0x0030
        L_0x001d:
            r5 = r11[r4]     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r5 = r5.getAbsolutePath()     // Catch:{ Throwable -> 0x00e7 }
            r6 = r12[r4]     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r6 = r6.getAbsolutePath()     // Catch:{ Throwable -> 0x00e7 }
            android.system.Os.symlink(r5, r6)     // Catch:{ Throwable -> 0x00e7 }
            r5 = r12[r4]     // Catch:{ Throwable -> 0x00e7 }
            r0[r4] = r5     // Catch:{ Throwable -> 0x00e7 }
        L_0x0030:
            int r4 = r4 + 1
            goto L_0x000d
        L_0x0033:
            return r0
        L_0x0034:
            com.uc.webview.export.cyclone.UCElapseTime r4 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ Throwable -> 0x00e7 }
            r4.<init>()     // Catch:{ Throwable -> 0x00e7 }
            r5 = 0
            java.lang.Runtime r6 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x00b3 }
            java.lang.String r7 = "sh"
            java.lang.Process r6 = r6.exec(r7)     // Catch:{ Exception -> 0x00b3 }
            java.io.DataOutputStream r5 = new java.io.DataOutputStream     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.io.OutputStream r7 = r6.getOutputStream()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r5.<init>(r7)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.io.BufferedReader r7 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.io.InputStreamReader r8 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.io.InputStream r9 = r6.getInputStream()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r8.<init>(r9)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r7.<init>(r8)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r7 = 0
        L_0x005c:
            int r8 = r11.length     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            if (r7 >= r8) goto L_0x0096
            r8 = r12[r7]     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            boolean r8 = r8.exists()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            if (r8 == 0) goto L_0x006c
            r8 = r12[r7]     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r0[r7] = r8     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            goto L_0x0093
        L_0x006c:
            java.lang.String r8 = "ln -s %s %s"
            java.lang.Object[] r9 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r10 = r11[r7]     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r9[r3] = r10     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r10 = r12[r7]     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.lang.String r10 = r10.getAbsolutePath()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r9[r2] = r10     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.lang.String r8 = java.lang.String.format(r8, r9)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r5.writeBytes(r8)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            java.lang.String r8 = "\n"
            r5.writeBytes(r8)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r5.flush()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r8 = r12[r7]     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r0[r7] = r8     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
        L_0x0093:
            int r7 = r7 + 1
            goto L_0x005c
        L_0x0096:
            java.lang.String r12 = "exit\n"
            r5.writeBytes(r12)     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r5.flush()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r6.waitFor()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            r5.close()     // Catch:{ Exception -> 0x00ad, all -> 0x00ab }
            if (r6 == 0) goto L_0x00a9
            r6.destroy()     // Catch:{ Throwable -> 0x00e7 }
        L_0x00a9:
            r12 = 1
            goto L_0x00c1
        L_0x00ab:
            r12 = move-exception
            goto L_0x00e1
        L_0x00ad:
            r12 = move-exception
            r5 = r6
            goto L_0x00b4
        L_0x00b0:
            r12 = move-exception
            r6 = r5
            goto L_0x00e1
        L_0x00b3:
            r12 = move-exception
        L_0x00b4:
            java.lang.String r6 = "Utils"
            java.lang.String r7 = "symlink exception."
            com.uc.webview.export.internal.utility.Log.e(r6, r7, r12)     // Catch:{ all -> 0x00b0 }
            if (r5 == 0) goto L_0x00c0
            r5.destroy()     // Catch:{ Throwable -> 0x00e7 }
        L_0x00c0:
            r12 = 0
        L_0x00c1:
            if (r12 == 0) goto L_0x00db
            java.lang.String r12 = "Utils"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r6 = "link success! Time:"
            r5.<init>(r6)     // Catch:{ Throwable -> 0x00e7 }
            long r6 = r4.getMilis()     // Catch:{ Throwable -> 0x00e7 }
            r5.append(r6)     // Catch:{ Throwable -> 0x00e7 }
            java.lang.String r4 = r5.toString()     // Catch:{ Throwable -> 0x00e7 }
            com.uc.webview.export.internal.utility.Log.e(r12, r4)     // Catch:{ Throwable -> 0x00e7 }
            return r0
        L_0x00db:
            java.lang.Throwable r12 = new java.lang.Throwable     // Catch:{ Throwable -> 0x00e7 }
            r12.<init>()     // Catch:{ Throwable -> 0x00e7 }
            throw r12     // Catch:{ Throwable -> 0x00e7 }
        L_0x00e1:
            if (r6 == 0) goto L_0x00e6
            r6.destroy()     // Catch:{ Throwable -> 0x00e7 }
        L_0x00e6:
            throw r12     // Catch:{ Throwable -> 0x00e7 }
        L_0x00e7:
            r12 = 0
        L_0x00e8:
            int r4 = r11.length     // Catch:{ Throwable -> 0x013c }
            if (r12 >= r4) goto L_0x013b
            r4 = r13[r12]     // Catch:{ Throwable -> 0x013c }
            r5 = r11[r12]     // Catch:{ Throwable -> 0x013c }
            r4.delete()     // Catch:{ Throwable -> 0x013c }
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x013c }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x013c }
            r7.<init>()     // Catch:{ Throwable -> 0x013c }
            java.lang.String r8 = r4.getAbsolutePath()     // Catch:{ Throwable -> 0x013c }
            r7.append(r8)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r8 = ".tmp"
            r7.append(r8)     // Catch:{ Throwable -> 0x013c }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x013c }
            r6.<init>(r7)     // Catch:{ Throwable -> 0x013c }
            r6.createNewFile()     // Catch:{ Throwable -> 0x013c }
            a((java.io.File) r5, (java.io.File) r6)     // Catch:{ Throwable -> 0x013c }
            boolean r7 = r6.renameTo(r4)     // Catch:{ Throwable -> 0x013c }
            if (r7 == 0) goto L_0x0124
            long r5 = r5.lastModified()     // Catch:{ Throwable -> 0x013c }
            r4.setLastModified(r5)     // Catch:{ Throwable -> 0x013c }
            r0[r12] = r4     // Catch:{ Throwable -> 0x013c }
            int r12 = r12 + 1
            goto L_0x00e8
        L_0x0124:
            r6.delete()     // Catch:{ Throwable -> 0x013c }
            com.uc.webview.export.internal.setup.UCSetupException r11 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Throwable -> 0x013c }
            r12 = 1005(0x3ed, float:1.408E-42)
            java.lang.String r13 = "Rename [%s] to [%s] failed."
            java.lang.Object[] r0 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x013c }
            r0[r3] = r6     // Catch:{ Throwable -> 0x013c }
            r0[r2] = r4     // Catch:{ Throwable -> 0x013c }
            java.lang.String r13 = java.lang.String.format(r13, r0)     // Catch:{ Throwable -> 0x013c }
            r11.<init>((int) r12, (java.lang.String) r13)     // Catch:{ Throwable -> 0x013c }
            throw r11     // Catch:{ Throwable -> 0x013c }
        L_0x013b:
            return r0
        L_0x013c:
            r11 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r12 = new com.uc.webview.export.internal.setup.UCSetupException
            r13 = 1007(0x3ef, float:1.411E-42)
            r12.<init>((int) r13, (java.lang.Throwable) r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.a(java.io.File[], java.io.File[], java.io.File[]):java.io.File[]");
    }

    public static int c() {
        Integer num = (Integer) d.a().a(UCCore.OPTION_CONNECTION_CONNECT_TIMEOUT);
        if (num != null) {
            return num.intValue();
        }
        return 5000;
    }

    public static int d() {
        Integer num = (Integer) d.a().a(UCCore.OPTION_CONNECTION_READ_TIMEOUT);
        if (num != null) {
            return num.intValue();
        }
        return 5000;
    }

    public static Boolean a(ConcurrentHashMap<String, Object> concurrentHashMap, String str) {
        Object obj = concurrentHashMap.get(str);
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.valueOf(Boolean.parseBoolean((String) obj));
        }
        throw new UCSetupException(3012, String.format("\"true\" or \"false\" or boolean expected with key:[%s], now is [%s]", new Object[]{str, obj}));
    }

    private static boolean d(File file) {
        try {
            if (file.getCanonicalPath().contains("/data/app/") || file.getCanonicalPath().contains("/system/")) {
                return true;
            }
            return false;
        } catch (IOException e2) {
            Log.e("Utils", "isSystemFile", e2);
            return false;
        }
    }

    public static boolean a(File file) {
        if (file != null) {
            try {
                if (!file.exists() || d(file)) {
                    return true;
                }
                if (Build.VERSION.SDK_INT >= 9) {
                    return file.setReadable(true, false);
                }
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 644 " + file.getAbsolutePath());
            } catch (Exception e2) {
                Log.e("Utils", "setReadable", e2);
                return false;
            }
        }
        return true;
    }

    public static boolean b(File file) {
        if (file != null) {
            try {
                if (!file.exists() || d(file)) {
                    return true;
                }
                if (Build.VERSION.SDK_INT >= 9) {
                    return file.setExecutable(true, false);
                }
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("chmod 711 " + file.getAbsolutePath());
            } catch (Exception e2) {
                Log.e("Utils", "setExecutable", e2);
                return false;
            }
        }
        return true;
    }

    public static int c(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception unused) {
            return 0;
        }
    }

    public static long d(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception unused) {
            return 0;
        }
    }

    private static String i(String str) {
        try {
            if (h == null) {
                Method declaredMethod = Class.forName("android.os.SystemProperties").getDeclaredMethod("get", new Class[]{String.class});
                h = declaredMethod;
                declaredMethod.setAccessible(true);
            }
            Object invoke = h.invoke((Object) null, new Object[]{str});
            if (invoke == null) {
                return null;
            }
            return invoke.toString();
        } catch (Throwable th) {
            Log.i("Utils", "getSystemProperty " + th.getMessage());
            return null;
        }
    }

    public static String e() {
        for (Map.Entry next : g.entrySet()) {
            String i2 = i((String) next.getKey());
            if (i2 != null && i2.length() > 0) {
                return (String) next.getValue();
            }
        }
        return "UNKNOWN";
    }

    public static boolean f() {
        if (k) {
            return j;
        }
        String[] strArr = i;
        int length = strArr.length;
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            if (com.uc.webview.export.Build.SDK_PROFILE_ID.equals(strArr[i2])) {
                z = true;
                break;
            }
            i2++;
        }
        j = z;
        k = true;
        return j;
    }

    public static String[] a(ClassLoader classLoader) {
        try {
            Class<?> cls = Class.forName("com.uc.webview.browser.shell.NativeLibraries", true, classLoader);
            if (cls != null) {
                Field declaredField = cls.getDeclaredField("LIBRARIES");
                declaredField.setAccessible(true);
                String[][] strArr = (String[][]) declaredField.get((Object) null);
                ArrayList arrayList = new ArrayList();
                for (String[] strArr2 : strArr) {
                    if (!(strArr2 == null || strArr2[0] == null)) {
                        arrayList.add(strArr2[0]);
                    }
                }
                return (String[]) arrayList.toArray(new String[arrayList.size()]);
            }
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (NoSuchFieldException e3) {
            e3.printStackTrace();
        } catch (ClassNotFoundException e4) {
            e4.printStackTrace();
        }
        return null;
    }

    /* compiled from: U4Source */
    public static class b {
        private static String a;

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v7, resolved type: java.lang.Object} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v4, resolved type: java.lang.String} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static java.lang.String a(android.content.Context r9) {
            /*
                java.lang.String r0 = a
                boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
                if (r0 != 0) goto L_0x000b
                java.lang.String r9 = a
                return r9
            L_0x000b:
                java.lang.ClassLoader r0 = com.uc.webview.export.internal.SDKFactory.c
                r1 = 0
                if (r0 == 0) goto L_0x00a1
                if (r9 != 0) goto L_0x0014
                goto L_0x00a1
            L_0x0014:
                long r2 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0088 }
                r0 = 0
                r4 = 1
                java.lang.String r5 = "com.ta.utdid2.device.UTDevice"
                java.lang.Class r5 = java.lang.Class.forName(r5)     // Catch:{ Throwable -> 0x0033 }
                java.lang.String r6 = "getUtdid"
                java.lang.Class[] r7 = new java.lang.Class[r4]     // Catch:{ Throwable -> 0x0033 }
                java.lang.Class<android.content.Context> r8 = android.content.Context.class
                r7[r0] = r8     // Catch:{ Throwable -> 0x0033 }
                java.lang.Object[] r8 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0033 }
                r8[r0] = r9     // Catch:{ Throwable -> 0x0033 }
                java.lang.Object r5 = com.uc.webview.export.internal.utility.ReflectionUtil.invoke((java.lang.Class<?>) r5, (java.lang.String) r6, (java.lang.Class[]) r7, (java.lang.Object[]) r8)     // Catch:{ Throwable -> 0x0033 }
                java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x0033 }
                goto L_0x004e
            L_0x0033:
                java.lang.String r5 = "com.ta.utdid2.device.UTDevice"
                java.lang.ClassLoader r6 = com.uc.webview.export.internal.SDKFactory.c     // Catch:{ Throwable -> 0x0088 }
                java.lang.Class r5 = java.lang.Class.forName(r5, r4, r6)     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r6 = "getUtdid"
                java.lang.Class[] r7 = new java.lang.Class[r4]     // Catch:{ Throwable -> 0x0088 }
                java.lang.Class<android.content.Context> r8 = android.content.Context.class
                r7[r0] = r8     // Catch:{ Throwable -> 0x0088 }
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Throwable -> 0x0088 }
                r4[r0] = r9     // Catch:{ Throwable -> 0x0088 }
                java.lang.Object r9 = com.uc.webview.export.internal.utility.ReflectionUtil.invoke((java.lang.Class<?>) r5, (java.lang.String) r6, (java.lang.Class[]) r7, (java.lang.Object[]) r4)     // Catch:{ Throwable -> 0x0088 }
                r5 = r9
                java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x0088 }
            L_0x004e:
                java.lang.String r9 = "Utils"
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r4 = "getUtdidBySdk time: "
                r0.<init>(r4)     // Catch:{ Throwable -> 0x0088 }
                long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0088 }
                r4 = 0
                long r6 = r6 - r2
                r0.append(r6)     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r2 = " milliseconds"
                r0.append(r2)     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0088 }
                com.uc.webview.export.internal.utility.Log.d(r9, r0)     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r9 = "Utils"
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r2 = "getUtdidBySdk utdid: "
                r0.<init>(r2)     // Catch:{ Throwable -> 0x0088 }
                r0.append(r5)     // Catch:{ Throwable -> 0x0088 }
                java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x0088 }
                com.uc.webview.export.internal.utility.Log.d(r9, r0)     // Catch:{ Throwable -> 0x0088 }
                boolean r9 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r5)     // Catch:{ Throwable -> 0x0088 }
                if (r9 != 0) goto L_0x0087
                a = r5     // Catch:{ Throwable -> 0x0088 }
            L_0x0087:
                return r5
            L_0x0088:
                r9 = move-exception
                java.lang.String r0 = "Utils"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "getUtdidBySdk cd exception : "
                r2.<init>(r3)
                java.lang.String r9 = r9.getMessage()
                r2.append(r9)
                java.lang.String r9 = r2.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r9)
                return r1
            L_0x00a1:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.b.a(android.content.Context):java.lang.String");
        }
    }

    public static String b(ClassLoader classLoader) {
        if (l == null) {
            l = a(classLoader, "NAME");
        }
        return l;
    }

    public static String c(ClassLoader classLoader) {
        return a(classLoader, "SUPPORT_SDK_MIN");
    }

    private static String a(ClassLoader classLoader, String str) {
        if (classLoader == null) {
            return null;
        }
        try {
            String str2 = (String) Class.forName("com.uc.webview.browser.shell.Build$Version", false, classLoader).getField(str).get((Object) null);
            if (str2 == null || str2.length() <= 0) {
                return null;
            }
            return str2;
        } catch (Exception e2) {
            Log.d("Utils", ".getVersionFieldFromSdkShellDexLoader", e2);
        }
    }

    public static void a(Map<String, String> map) {
        try {
            Log.d("Utils", "addHeaderInfoToCrashSdk headerInfos: " + map);
            Object invoke = ReflectionUtil.invoke("com.uc.crashsdk.export.CrashApi", "getInstance");
            if (invoke != null && map.size() > 0) {
                for (Map.Entry next : map.entrySet()) {
                    ReflectionUtil.invoke(invoke, "addHeaderInfo", new Class[]{String.class, String.class}, new Object[]{next.getKey(), next.getValue()});
                }
            }
        } catch (Throwable th) {
            Log.i("Utils", "addHeaderInfoToCrashSdk " + th.getMessage());
        }
    }

    public static int a(boolean z) {
        if (g()) {
            return 1;
        }
        return z ? 4 : 2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v1, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v11, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v13, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v15, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v16, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v17, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v13, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v14, resolved type: java.io.FileInputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v19, resolved type: java.io.FileOutputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r8) {
        /*
            r0 = 0
            java.lang.String r1 = "flags"
            java.io.File r8 = a((android.content.Context) r8, (java.lang.String) r1)     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            java.lang.String r2 = "fpathhash"
            r1.<init>(r8, r2)     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            java.lang.String r8 = r1.getAbsolutePath()     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            java.lang.String r8 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r8)     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            boolean r2 = r1.exists()     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            if (r2 == 0) goto L_0x0080
            long r2 = r1.length()     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            r4 = 0
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 <= 0) goto L_0x0080
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x007c, all -> 0x0079 }
            r1.<init>()     // Catch:{ Throwable -> 0x007c, all -> 0x0079 }
            r3 = 32
            byte[] r3 = new byte[r3]     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
        L_0x0034:
            int r4 = r2.read(r3)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            if (r4 <= 0) goto L_0x003f
            r5 = 0
            r1.write(r3, r5, r4)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            goto L_0x0034
        L_0x003f:
            java.lang.String r3 = new java.lang.String     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            byte[] r4 = r1.toByteArray()     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            java.lang.String r4 = "Utils"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            java.lang.String r6 = "curHash:"
            r5.<init>(r6)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            r5.append(r8)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            java.lang.String r6 = ", preHash:"
            r5.append(r6)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            r5.append(r3)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            com.uc.webview.export.internal.utility.Log.d(r4, r5)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            boolean r8 = r8.equals(r3)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
            if (r8 != 0) goto L_0x006e
            java.lang.String r8 = "mpfpc"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r8)     // Catch:{ Throwable -> 0x0074, all -> 0x0072 }
        L_0x006e:
            r7 = r2
            r2 = r0
            r0 = r7
            goto L_0x008d
        L_0x0072:
            r8 = move-exception
            goto L_0x00ad
        L_0x0074:
            r8 = move-exception
            r7 = r2
            r2 = r0
            r0 = r7
            goto L_0x00a5
        L_0x0079:
            r8 = move-exception
            r1 = r0
            goto L_0x00ad
        L_0x007c:
            r8 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x00a4
        L_0x0080:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x00a2, all -> 0x009e }
            byte[] r8 = r8.getBytes()     // Catch:{ Throwable -> 0x009b, all -> 0x0097 }
            r2.write(r8)     // Catch:{ Throwable -> 0x009b, all -> 0x0097 }
            r1 = r0
        L_0x008d:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            return
        L_0x0097:
            r8 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x00a0
        L_0x009b:
            r8 = move-exception
            r1 = r0
            goto L_0x00a5
        L_0x009e:
            r8 = move-exception
            r1 = r0
        L_0x00a0:
            r2 = r1
            goto L_0x00ad
        L_0x00a2:
            r8 = move-exception
            r1 = r0
        L_0x00a4:
            r2 = r1
        L_0x00a5:
            r8.printStackTrace()     // Catch:{ all -> 0x00a9 }
            goto L_0x008d
        L_0x00a9:
            r8 = move-exception
            r7 = r2
            r2 = r0
            r0 = r7
        L_0x00ad:
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.a(android.content.Context):void");
    }

    public static boolean a(UCMRunningInfo uCMRunningInfo) {
        if (uCMRunningInfo != null) {
            return uCMRunningInfo.coreType == 2 && d.a().b(UCCore.OPTION_MULTI_CORE_TYPE);
        }
        return true;
    }

    public static String a(File file, String str) {
        return a(file, str, true);
    }

    public static String a(File file, String str, boolean z) {
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) {
            return null;
        }
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                if (z) {
                    String a2 = a(file2, str, true);
                    if (!a(a2)) {
                        return a2;
                    }
                } else {
                    continue;
                }
            }
            if (file2.getName().contains(str)) {
                return file2.getAbsolutePath();
            }
        }
        return null;
    }

    public static boolean b(File file, File file2) {
        try {
            if (!file.getCanonicalPath().startsWith(file2.getCanonicalPath())) {
                return false;
            }
            while (file.getCanonicalPath().contains(file2.getCanonicalPath()) && !file.getCanonicalPath().equals(file2.getCanonicalPath())) {
                if (!b(file)) {
                    return false;
                }
                file = file.getParentFile();
            }
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static synchronized List<br> a(Context context, ConcurrentHashMap<String, Object> concurrentHashMap) {
        List<br> arrayList;
        br a2;
        synchronized (k.class) {
            arrayList = new ArrayList<>();
            com.uc.webview.export.internal.uc.startup.b.a(283);
            if (!a((String) concurrentHashMap.get(UCCore.OPTION_SO_FILE_PATH)) && (a2 = a(context, (String) null, (String) concurrentHashMap.get(UCCore.OPTION_SO_FILE_PATH), (String) concurrentHashMap.get(UCCore.OPTION_RES_FILE_PATH), (String) null)) != null) {
                arrayList.add(a2);
            }
            String str = (String) concurrentHashMap.get(UCCore.OPTION_UCM_KRL_DIR);
            if (!a(str)) {
                arrayList = a(context, new File(str), arrayList);
            }
            com.uc.webview.export.internal.uc.startup.b.a(284);
        }
        return arrayList;
    }

    public static synchronized List<br> b(Context context, ConcurrentHashMap<String, Object> concurrentHashMap) {
        synchronized (k.class) {
            List<br> arrayList = new ArrayList<>();
            com.uc.webview.export.internal.uc.startup.b.a(285);
            String str = (String) concurrentHashMap.get(UCCore.OPTION_DEX_FILE_PATH);
            Log.i("Utils", " listFromOptions dexPath:" + str);
            if (!a(str)) {
                arrayList = a(context, new File(str), arrayList);
            }
            String str2 = (String) concurrentHashMap.get(UCCore.OPTION_SET_ODEX_ROOT_PATH);
            if (str2 == null) {
                str2 = a(context, "odexs").getAbsolutePath();
            }
            if (arrayList.size() == 0) {
                br a2 = a(context, (String) concurrentHashMap.get(UCCore.OPTION_DEX_FILE_PATH), (String) concurrentHashMap.get(UCCore.OPTION_SO_FILE_PATH), (String) concurrentHashMap.get(UCCore.OPTION_RES_FILE_PATH), str2);
                if (a2 != null) {
                    arrayList.add(a2);
                }
                if (g()) {
                    com.uc.webview.export.internal.uc.startup.b.a(286);
                    return arrayList;
                }
            }
            String str3 = (String) concurrentHashMap.get(UCCore.OPTION_UCM_KRL_DIR);
            if (!a(str3)) {
                arrayList = a(context, new File(str3), arrayList);
            }
            String str4 = (String) concurrentHashMap.get(UCCore.OPTION_UCM_LIB_DIR);
            if (!a(str4)) {
                arrayList.add(d(context, str4, str2));
            }
            if (arrayList != null && arrayList.size() > 1) {
                Collections.sort(arrayList, new p());
            }
            com.uc.webview.export.internal.uc.startup.b.a(286);
            return arrayList;
        }
    }

    public static File a(Context context, String str) throws UCSetupException {
        File dir = context.getDir("ucmsdk", 0);
        if (str == null) {
            return dir;
        }
        return UCCyclone.expectCreateDirFile(new File(dir, str));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x003d, code lost:
        if (r2 != null) goto L_0x0041;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r5, java.io.File r6, java.io.File r7) throws com.uc.webview.export.internal.setup.UCSetupException {
        /*
            boolean r0 = r6.isFile()
            r1 = 1
            r2 = 0
            r3 = 0
            if (r0 == 0) goto L_0x000a
            goto L_0x0041
        L_0x000a:
            boolean r0 = r6.isDirectory()
            if (r0 == 0) goto L_0x0040
            java.lang.String r0 = "flags"
            java.io.File r5 = a((android.content.Context) r5, (java.lang.String) r0)
            java.io.File r0 = new java.io.File
            java.lang.String r4 = "setup_delete"
            r0.<init>(r5, r4)
            java.io.File r5 = com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r0)
            java.lang.String r0 = r6.getAbsolutePath()
            java.lang.String r0 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r0)
            java.io.File r4 = new java.io.File
            r4.<init>(r5, r0)
            java.io.File r5 = com.uc.webview.export.cyclone.UCCyclone.expectCreateDirFile(r4)
            java.lang.String[] r0 = r5.list()
            int r0 = r0.length
            r4 = 12
            if (r0 < r4) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r2 = r5
        L_0x003d:
            if (r2 == 0) goto L_0x0040
            goto L_0x0041
        L_0x0040:
            r1 = 0
        L_0x0041:
            if (r1 == 0) goto L_0x0073
            com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r6, r3, r7)
            boolean r5 = r6.exists()
            if (r5 != 0) goto L_0x0073
            if (r2 == 0) goto L_0x0073
            long r5 = java.lang.System.currentTimeMillis()
            java.lang.String r5 = java.lang.String.valueOf(r5)
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x006a }
            r6.<init>(r2, r5)     // Catch:{ Throwable -> 0x006a }
            boolean r5 = r6.createNewFile()     // Catch:{ Throwable -> 0x006a }
            if (r5 == 0) goto L_0x0062
            return
        L_0x0062:
            java.lang.Exception r5 = new java.lang.Exception     // Catch:{ Throwable -> 0x006a }
            java.lang.String r6 = "createNewFile return false"
            r5.<init>(r6)     // Catch:{ Throwable -> 0x006a }
            throw r5     // Catch:{ Throwable -> 0x006a }
        L_0x006a:
            r5 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r6 = new com.uc.webview.export.cyclone.UCKnownException
            r7 = 1006(0x3ee, float:1.41E-42)
            r6.<init>((int) r7, (java.lang.Throwable) r5)
            throw r6
        L_0x0073:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.a(android.content.Context, java.io.File, java.io.File):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:7|8|9|10|11) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0019 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean g() {
        /*
            com.uc.webview.export.internal.utility.k$a r0 = e
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.NOT_INITED
            if (r0 != r1) goto L_0x0022
            com.uc.webview.export.internal.utility.k$a r0 = e
            monitor-enter(r0)
            com.uc.webview.export.internal.utility.k$a r1 = e     // Catch:{ all -> 0x001f }
            com.uc.webview.export.internal.utility.k$a r2 = com.uc.webview.export.internal.utility.k.a.NOT_INITED     // Catch:{ all -> 0x001f }
            if (r1 != r2) goto L_0x001d
            java.lang.String r1 = "com.uc.webkit.sdk.CoreFactoryImpl"
            java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x0019 }
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.ENABLE     // Catch:{ ClassNotFoundException -> 0x0019 }
            e = r1     // Catch:{ ClassNotFoundException -> 0x0019 }
            goto L_0x001d
        L_0x0019:
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.DISABLE     // Catch:{ all -> 0x001f }
            e = r1     // Catch:{ all -> 0x001f }
        L_0x001d:
            monitor-exit(r0)     // Catch:{ all -> 0x001f }
            goto L_0x0022
        L_0x001f:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x001f }
            throw r1
        L_0x0022:
            com.uc.webview.export.internal.utility.k$a r0 = e
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.ENABLE
            if (r0 != r1) goto L_0x002a
            r0 = 1
            return r0
        L_0x002a:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.g():boolean");
    }

    public static boolean a(ConcurrentHashMap<String, Object> concurrentHashMap) {
        Boolean bool;
        a aVar;
        if (e != a.NOT_INITED || concurrentHashMap == null || (bool = (Boolean) concurrentHashMap.get(UCCore.OPTION_THICK_INTEGRATION)) == null) {
            return g();
        }
        boolean a2 = a(bool);
        synchronized (e) {
            if (a2) {
                try {
                    aVar = a.ENABLE;
                } catch (Throwable th) {
                    while (true) {
                        throw th;
                    }
                }
            } else {
                aVar = a.DISABLE;
            }
            e = aVar;
        }
        return e == a.ENABLE;
    }

    public static String e(String str) {
        return String.valueOf(str.hashCode()).replace('-', '_');
    }

    public static String b(Context context, String str) {
        return (context == null || a(str) || str.indexOf(context.getPackageName()) <= 0) ? str : str.substring(str.indexOf(context.getPackageName()), str.length());
    }

    public static String f(String str) {
        if (!a(str)) {
            File file = new File(str);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
        }
        return str;
    }

    public static String h() {
        return "/unexists/" + System.currentTimeMillis();
    }

    public static File g(String str) throws UCSetupException {
        File[] listFiles;
        if ((com.uc.webview.export.Build.PACK_TYPE == 34 || com.uc.webview.export.Build.PACK_TYPE == 43) && (listFiles = j(str).listFiles(new n())) != null && listFiles.length > 0) {
            return listFiles[0];
        }
        return null;
    }

    private static File j(String str) throws UCSetupException {
        File file = new File(str);
        if (file.exists()) {
            return file;
        }
        throw new UCSetupException(1002, String.format("Directory [%s] not exists.", new Object[]{file.getAbsolutePath()}));
    }

    public static File b(File file, String str) throws UCSetupException {
        return UCCyclone.expectCreateDirFile(new File(file, str));
    }

    public static void d(ClassLoader classLoader) {
        Class<?> cls;
        Class<?> cls2;
        if (classLoader == null) {
            try {
                cls2 = Class.forName("com.uc.webview.browser.shell.Build$Version");
            } catch (Exception e2) {
                Log.d("Utils", UCCore.EVENT_EXCEPTION, e2);
                Build.Version.API_LEVEL = 1;
            }
        } else {
            cls2 = Class.forName("com.uc.webview.browser.shell.Build$Version", false, classLoader);
        }
        String obj = cls2.getField("NAME").get((Object) null).toString();
        String obj2 = cls2.getField("SUPPORT_SDK_MIN").get((Object) null).toString();
        com.uc.webview.export.Build.UCM_VERSION = obj;
        com.uc.webview.export.Build.UCM_SUPPORT_SDK_MIN = obj2;
        Build.Version.API_LEVEL = cls2.getField("API_LEVEL").getInt((Object) null);
        if (classLoader == null) {
            try {
                cls = Class.forName("com.uc.webview.browser.shell.Build");
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            cls = Class.forName("com.uc.webview.browser.shell.Build", false, classLoader);
        }
        com.uc.webview.export.Build.CORE_VERSION = cls.getField("CORE_VERSION").get((Object) null).toString();
        com.uc.webview.export.Build.CORE_TIME = cls.getField("TIME").get((Object) null).toString();
        a((Map<String, String>) new q());
    }

    public static void b(Context context) {
        File[] listFiles = context.getCacheDir().listFiles(new r());
        if (listFiles != null && listFiles.length > 0) {
            for (File recursiveDelete : listFiles) {
                UCCyclone.recursiveDelete(recursiveDelete, false, (Object) null);
            }
        }
    }

    public static String a(Context context, String str, String str2) {
        File j2;
        String[] list;
        Context context2 = context;
        if (a(str2) || (list = j2.list(new o())) == null || list.length == 0) {
            return null;
        }
        String e2 = e(str);
        File b2 = b(a(context2, "krlinks"), e2);
        File b3 = b(a(context2, "krcopies"), e2);
        File b4 = b(b2, br.RES_PAKS_DIR_NAME);
        File b5 = b(b3, br.RES_PAKS_DIR_NAME);
        File[] fileArr = new File[list.length];
        File[] fileArr2 = new File[list.length];
        File[] fileArr3 = new File[list.length];
        UCElapseTime uCElapseTime = new UCElapseTime();
        int i2 = 0;
        while (i2 < list.length) {
            String str3 = list[i2];
            boolean endsWith = str3.endsWith("_pak_kr_uc.so");
            String substring = str3.substring(3, str3.length() - 9);
            int lastIndexOf = substring.lastIndexOf(95);
            File file = b4;
            StringBuilder sb = new StringBuilder();
            File file2 = b5;
            sb.append(substring.substring(0, lastIndexOf));
            sb.append('.');
            sb.append(substring.substring(lastIndexOf + 1));
            String sb2 = sb.toString();
            File file3 = new File((j2 = j(str2)), str3);
            File file4 = new File(endsWith ? file : b2, sb2);
            File file5 = new File(endsWith ? file2 : b3, sb2);
            fileArr[i2] = file3;
            fileArr2[i2] = file4;
            fileArr3[i2] = file5;
            i2++;
            b4 = file;
            b5 = file2;
        }
        boolean z = true;
        File[] a2 = a(fileArr, fileArr2, fileArr3);
        Log.i("Utils", "getLnkOrCpyResDirFromSO: file count:" + list.length + " time:" + uCElapseTime.getMilis());
        if (a2[0] != fileArr2[0]) {
            z = false;
        }
        if (!z) {
            return b3.getAbsolutePath();
        }
        return b2.getAbsolutePath();
    }

    public static String b(Context context, String str, String str2) {
        if (a(str2)) {
            return null;
        }
        File file = new File(str2);
        String name = file.getName();
        if (!name.startsWith("lib") || !name.endsWith("_jar_kj_uc.so")) {
            return str2;
        }
        String str3 = name.substring(3, name.length() - 13) + ".jar";
        String e2 = e(str);
        return a(file, new File(b(a(context, "kjlinks"), e2), str3), new File(b(a(context, "kjcopies"), e2), str3)).getAbsolutePath();
    }

    private static br d(Context context, String str, String str2) throws UCSetupException {
        String str3;
        String str4;
        loop0:
        while (true) {
            File j2 = j(str);
            try {
                String absolutePath = UCCyclone.expectFile(j2, br.CORE_IMPL_DEX_FILE_USING_SO_SUFFIX).getAbsolutePath();
                try {
                    str3 = UCCyclone.expectFile(j2, br.SDK_SHELL_DEX_FILE_USING_SO_SUFFIX).getAbsolutePath();
                } catch (Throwable unused) {
                    str3 = null;
                }
                try {
                    str4 = UCCyclone.expectFile(j2, br.BROWSER_IF_DEX_FILE_USING_SO_SUFFIX).getAbsolutePath();
                } catch (Throwable unused2) {
                    str4 = null;
                }
                return new UCMPackageInfo(context, "specified", j2.getAbsolutePath(), j2.getAbsolutePath(), j2.getAbsolutePath(), str3, str4, absolutePath, str2, true, false);
            } catch (UCKnownException e2) {
                File[] listFiles = j2.listFiles();
                if (listFiles == null) {
                    break;
                }
                for (String str5 : a) {
                    int length = listFiles.length;
                    int i2 = 0;
                    while (i2 < length) {
                        File file = listFiles[i2];
                        if (!str5.equals(file.getName()) || !file.isDirectory()) {
                            i2++;
                        } else {
                            str = file.getAbsolutePath();
                        }
                    }
                }
                break loop0;
                throw e2;
            }
        }
        throw e2;
    }

    private static br a(Context context, String str, String str2, String str3, String str4) throws UCSetupException {
        String str5;
        String str6;
        String str7;
        String str8;
        boolean a2 = a(str);
        boolean a3 = a(str2);
        boolean a4 = a(str3);
        if (!a2 || g()) {
            if (!a2) {
                File j2 = j(str);
                String absolutePath = UCCyclone.expectFile(j2, "core.jar").getAbsolutePath();
                try {
                    str8 = UCCyclone.expectFile(j2, br.SDK_SHELL_DEX_FILE).getAbsolutePath();
                } catch (Throwable unused) {
                    str8 = null;
                }
                try {
                    str6 = UCCyclone.expectFile(j2, "browser_if.jar").getAbsolutePath();
                    str5 = absolutePath;
                    str7 = str8;
                } catch (Throwable unused2) {
                    str5 = absolutePath;
                    str7 = str8;
                    str6 = null;
                }
            } else {
                str7 = null;
                str6 = null;
                str5 = null;
            }
            return new UCMPackageInfo(context, "specified", str2, str3, str, str7, str6, str5, str4, false, false);
        } else if (a3 && a4) {
            return null;
        } else {
            throw new UCSetupException(3002, "No ucm dex file specified.");
        }
    }

    private static String e(File file) {
        return file.exists() ? file.getAbsolutePath() : "";
    }

    private static List<br> a(Context context, File file, List<br> list) throws UCSetupException {
        boolean z;
        File file2 = file;
        List<br> arrayList = list != null ? list : new ArrayList<>();
        Log.i("Utils", " listUninstalls ucmDirFile :" + file.getAbsolutePath());
        if (file.exists() && file.isDirectory()) {
            File file3 = new File(file2, br.SDK_SHELL_DEX_FILE);
            File file4 = new File(file2, "browser_if.jar");
            File file5 = new File(file2, "core.jar");
            File file6 = new File(file2, "lib");
            d.a().b(UCCore.OPTION_USE_SDK_SETUP);
            if (g()) {
                z = file6.isDirectory();
            } else {
                z = file5.exists() && (!d.a().b(UCCore.OPTION_USE_SDK_SETUP) || file3.exists()) && file6.isDirectory();
            }
            if (z) {
                String[] strArr = a;
                int length = strArr.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    File file7 = new File(file6, strArr[i2]);
                    if (file7.isDirectory()) {
                        file6 = file7;
                        break;
                    }
                    i2++;
                }
                File file8 = new File(file2, br.ASSETS_DIR);
                Log.i("Utils", " listUninstalls resDirFile :" + file8.getAbsolutePath());
                if (z) {
                    arrayList.add(new UCMPackageInfo(context, "specified", e(file6), e(file8), e(file), e(file3), e(file4), e(file5), (String) null, false, false));
                }
            }
            Log.i("Utils", " listUninstalls retUCMpis size :" + arrayList.size());
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file9 : listFiles) {
                    if (file9.isDirectory()) {
                        a(context, file9, arrayList);
                    } else {
                        Context context2 = context;
                    }
                }
            }
        }
        return arrayList;
    }

    public static boolean c(Context context, String str, String str2) {
        File a2 = a(context, "decompresses2");
        if (!str.startsWith(a2.getAbsolutePath())) {
            return false;
        }
        File file = new File(str2);
        return !str.startsWith(new File(new File(a2, UCCyclone.getSourceHash(file.getAbsolutePath())), UCCyclone.getSourceHash(file.length(), file.lastModified())).getAbsolutePath());
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:11|12|13|14|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0028 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean i() {
        /*
            java.lang.String r0 = "go_is_bw_rt"
            java.lang.Object r0 = com.uc.webview.export.extension.UCCore.getGlobalOption(r0)
            if (r0 == 0) goto L_0x000f
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            return r0
        L_0x000f:
            com.uc.webview.export.internal.utility.k$a r0 = f
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.NOT_INITED
            if (r0 != r1) goto L_0x0031
            com.uc.webview.export.internal.utility.k$a r0 = f
            monitor-enter(r0)
            com.uc.webview.export.internal.utility.k$a r1 = f     // Catch:{ all -> 0x002e }
            com.uc.webview.export.internal.utility.k$a r2 = com.uc.webview.export.internal.utility.k.a.NOT_INITED     // Catch:{ all -> 0x002e }
            if (r1 != r2) goto L_0x002c
            java.lang.String r1 = "com.uc.webview.browser.BrowserCore"
            java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x0028 }
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.ENABLE     // Catch:{ ClassNotFoundException -> 0x0028 }
            f = r1     // Catch:{ ClassNotFoundException -> 0x0028 }
            goto L_0x002c
        L_0x0028:
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.DISABLE     // Catch:{ all -> 0x002e }
            f = r1     // Catch:{ all -> 0x002e }
        L_0x002c:
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            goto L_0x0031
        L_0x002e:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002e }
            throw r1
        L_0x0031:
            com.uc.webview.export.internal.utility.k$a r0 = f
            com.uc.webview.export.internal.utility.k$a r1 = com.uc.webview.export.internal.utility.k.a.ENABLE
            if (r0 != r1) goto L_0x0039
            r0 = 1
            return r0
        L_0x0039:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.i():boolean");
    }

    public static final String a(Throwable th) {
        if (th == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(th.getMessage());
        StackTraceElement[] stackTrace = th.getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            int min = Math.min(8, stackTrace.length);
            for (int i2 = 0; i2 < min; i2++) {
                StackTraceElement stackTraceElement = stackTrace[i2];
                String str = "";
                if (i2 < 2) {
                    str = stackTraceElement.getClassName();
                }
                int lineNumber = stackTraceElement.getLineNumber();
                sb.append(Operators.SPACE_STR);
                sb.append(str);
                sb.append(":");
                sb.append(lineNumber);
            }
        }
        return sb.toString();
    }

    public static File c(File file) {
        for (String file2 : a) {
            File file3 = new File(file, file2);
            if (file3.isDirectory()) {
                return file3;
            }
        }
        return file;
    }

    public static String c(Context context) {
        if (m != null) {
            return m;
        }
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            m = (String) cls.getMethod("getProcessName", new Class[0]).invoke(cls.getMethod("currentActivityThread", new Class[0]).invoke((Object) null, new Object[0]), new Object[0]);
        } catch (Exception unused) {
            int myPid = Process.myPid();
            Iterator<ActivityManager.RunningAppProcessInfo> it = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ActivityManager.RunningAppProcessInfo next = it.next();
                if (next.pid == myPid) {
                    m = next.processName;
                    break;
                }
            }
        }
        if (TextUtils.isEmpty(m)) {
            return "";
        }
        return m;
    }

    public static boolean d(Context context) {
        return !c(context).contains(":");
    }

    public static String b() {
        return String.valueOf(c(h("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").trim()));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00e9, code lost:
        r1 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x012b, code lost:
        r7 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0130, code lost:
        r7 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x0131, code lost:
        r1 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:?, code lost:
        r8.disconnect();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x0102 A[SYNTHETIC, Splitter:B:53:0x0102] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x012b A[ExcHandler: all (th java.lang.Throwable), Splitter:B:21:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0130 A[ExcHandler: UCKnownException (e com.uc.webview.export.cyclone.UCKnownException), Splitter:B:21:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x0143 A[SYNTHETIC, Splitter:B:76:0x0143] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.Pair<java.lang.Long, java.lang.Long> a(java.lang.String r7, java.net.URL r8) throws com.uc.webview.export.internal.setup.UCSetupException {
        /*
            java.util.HashMap<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Object>> r0 = d
            java.lang.Object r0 = r0.get(r7)
            android.util.Pair r0 = (android.util.Pair) r0
            r1 = 0
            if (r0 == 0) goto L_0x002d
            java.lang.Object r2 = r0.first
            java.lang.Long r2 = (java.lang.Long) r2
            long r3 = android.os.SystemClock.uptimeMillis()
            long r5 = r2.longValue()
            long r3 = r3 - r5
            java.lang.Long r2 = java.lang.Long.valueOf(r3)
            long r2 = r2.longValue()
            java.lang.Long r4 = c
            long r4 = r4.longValue()
            int r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r6 >= 0) goto L_0x002d
            java.lang.Object r0 = r0.second
            goto L_0x002e
        L_0x002d:
            r0 = r1
        L_0x002e:
            android.util.Pair r0 = (android.util.Pair) r0
            if (r0 == 0) goto L_0x0033
            return r0
        L_0x0033:
            boolean r0 = com.uc.webview.export.internal.SDKFactory.f     // Catch:{ Throwable -> 0x0050 }
            if (r0 != 0) goto L_0x0054
            java.lang.String r0 = "traffic_stat"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ Throwable -> 0x0050 }
            boolean r0 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Throwable -> 0x0050 }
            if (r0 == 0) goto L_0x0054
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x0050 }
            r2 = 14
            if (r0 < r2) goto L_0x0054
            r0 = 40962(0xa002, float:5.74E-41)
            android.net.TrafficStats.setThreadStatsTag(r0)     // Catch:{ Throwable -> 0x0050 }
            goto L_0x0054
        L_0x0050:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0054:
            java.net.URL r0 = new java.net.URL     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            r0.<init>(r8, r7)     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            java.net.URLConnection r8 = r0.openConnection()     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            int r2 = c()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r8.setConnectTimeout(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            int r2 = d()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r8.setReadTimeout(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r2 = 0
            r8.setInstanceFollowRedirects(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r2 = "HEAD"
            r8.setRequestMethod(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r8.connect()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            int r2 = r8.getResponseCode()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r3 = 200(0xc8, float:2.8E-43)
            if (r2 < r3) goto L_0x0110
            r3 = 303(0x12f, float:4.25E-43)
            if (r2 > r3) goto L_0x0110
            r4 = 300(0x12c, float:4.2E-43)
            if (r2 == r4) goto L_0x00d9
            r4 = 301(0x12d, float:4.22E-43)
            if (r2 == r4) goto L_0x00d9
            r4 = 302(0x12e, float:4.23E-43)
            if (r2 == r4) goto L_0x00d9
            if (r2 != r3) goto L_0x0094
            goto L_0x00d9
        L_0x0094:
            com.uc.webview.export.internal.utility.d r0 = com.uc.webview.export.internal.utility.d.a()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r1 = "exact_mod"
            boolean r0 = r0.b(r1)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r1 = 0
            if (r0 == 0) goto L_0x00a7
            long r3 = r8.getLastModified()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            goto L_0x00a8
        L_0x00a7:
            r3 = r1
        L_0x00a8:
            int r0 = r8.getContentLength()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            long r5 = (long) r0     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            int r0 = (r5 > r1 ? 1 : (r5 == r1 ? 0 : -1))
            if (r0 <= 0) goto L_0x00c3
            r8.disconnect()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            android.util.Pair r0 = new android.util.Pair     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.Long r1 = java.lang.Long.valueOf(r5)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.Long r2 = java.lang.Long.valueOf(r3)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r0.<init>(r1, r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r1 = r8
            goto L_0x00ee
        L_0x00c3:
            com.uc.webview.export.cyclone.UCKnownException r7 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r0 = 4023(0xfb7, float:5.637E-42)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r2 = "Total size is not correct:"
            r1.<init>(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r1.append(r5)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r1 = r1.toString()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r7.<init>((int) r0, (java.lang.String) r1)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            throw r7     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
        L_0x00d9:
            java.lang.String r2 = "Location"
            java.lang.String r2 = r8.getHeaderField(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            boolean r3 = a((java.lang.String) r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            if (r3 != 0) goto L_0x0106
            r8.disconnect()     // Catch:{ Throwable -> 0x00e9, UCKnownException -> 0x0130, all -> 0x012b }
            goto L_0x00ea
        L_0x00e9:
            r1 = r8
        L_0x00ea:
            android.util.Pair r0 = a((java.lang.String) r2, (java.net.URL) r0)     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
        L_0x00ee:
            java.util.HashMap<java.lang.String, android.util.Pair<java.lang.Long, java.lang.Object>> r8 = d     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            android.util.Pair r2 = new android.util.Pair     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            long r3 = android.os.SystemClock.uptimeMillis()     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            r2.<init>(r3, r0)     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            r8.put(r7, r2)     // Catch:{ UCKnownException -> 0x013f, Throwable -> 0x0136 }
            if (r1 == 0) goto L_0x0105
            r1.disconnect()     // Catch:{ Throwable -> 0x0105 }
        L_0x0105:
            return r0
        L_0x0106:
            com.uc.webview.export.cyclone.UCKnownException r7 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r0 = 4022(0xfb6, float:5.636E-42)
            java.lang.String r1 = "Redirect location is null."
            r7.<init>((int) r0, (java.lang.String) r1)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            throw r7     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
        L_0x0110:
            com.uc.webview.export.cyclone.UCKnownException r7 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r0 = 4021(0xfb5, float:5.635E-42)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r3 = "httpcode:"
            r1.<init>(r3)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r1.append(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r2 = " not correct."
            r1.append(r2)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            java.lang.String r1 = r1.toString()     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            r7.<init>((int) r0, (java.lang.String) r1)     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
            throw r7     // Catch:{ UCKnownException -> 0x0130, Throwable -> 0x012d, all -> 0x012b }
        L_0x012b:
            r7 = move-exception
            goto L_0x0141
        L_0x012d:
            r7 = move-exception
            r1 = r8
            goto L_0x0137
        L_0x0130:
            r7 = move-exception
            r1 = r8
            goto L_0x0140
        L_0x0133:
            r7 = move-exception
            r8 = r1
            goto L_0x0141
        L_0x0136:
            r7 = move-exception
        L_0x0137:
            com.uc.webview.export.internal.setup.UCSetupException r8 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ all -> 0x0133 }
            r0 = 2009(0x7d9, float:2.815E-42)
            r8.<init>((int) r0, (java.lang.Throwable) r7)     // Catch:{ all -> 0x0133 }
            throw r8     // Catch:{ all -> 0x0133 }
        L_0x013f:
            r7 = move-exception
        L_0x0140:
            throw r7     // Catch:{ all -> 0x0133 }
        L_0x0141:
            if (r8 == 0) goto L_0x0146
            r8.disconnect()     // Catch:{ Throwable -> 0x0146 }
        L_0x0146:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.k.a(java.lang.String, java.net.URL):android.util.Pair");
    }
}
