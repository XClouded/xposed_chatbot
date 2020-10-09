package com.huawei.hianalytics.log.e;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;
import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import mtopsdk.common.util.SymbolExpUtil;

public class f {
    public static String a() {
        String c = c();
        int myPid = Process.myPid();
        return myPid + "_" + c + ".log";
    }

    public static void a(Context context, String str) {
        float f;
        try {
            float parseFloat = Float.parseFloat(str.split(SymbolExpUtil.SYMBOL_EQUAL)[1].trim());
            f = 0.0f;
            if (parseFloat > 3.0f) {
                f = 3.0f;
            } else if (parseFloat >= 0.0f) {
                f = parseFloat;
            }
        } catch (NumberFormatException unused) {
            b.d("AppLogApi/LogUtil", "The cycle of the server returns : " + str);
            f = 1.0f;
        }
        b.b("AppLogApi/LogUtil", "setpolicy cycle : " + f);
        c.a(c.b(context), "autocheck_policy", Float.valueOf(1.0f));
    }

    public static boolean a(Context context) {
        if (context == null) {
            b.c("AppLogApi/LogUtil", "No init of logServer!");
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        SharedPreferences b = c.b(context);
        long longValue = ((Long) c.b(b, "autocheck_starttime", -1L)).longValue();
        b.b("AppLogApi/LogUtil", "checkPolicyOver beforeTimeMillis : " + longValue);
        boolean z = true;
        if (-1 == longValue) {
            b.c("AppLogApi/LogUtil", "checkPolicyOver beforeTimeMillis is first!");
        } else {
            float floatValue = ((Float) c.b(b, "autocheck_policy", Float.valueOf(-1.0f))).floatValue();
            long j = (long) (8.64E7f * floatValue);
            b.b("AppLogApi/LogUtil", "checkPolicyOver policy : " + floatValue);
            if (-1.0f == floatValue) {
                c.a(b, "autocheck_policy", Float.valueOf(1.0f));
            } else if (currentTimeMillis - longValue <= j) {
                z = false;
            }
        }
        b.b("AppLogApi/LogUtil", "checkPolicyOver() No upload cycle :  " + z);
        return z;
    }

    public static boolean a(String str) {
        File file = new File(str);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static boolean a(File[] fileArr) {
        int length = fileArr.length;
        int i = 0;
        while (i < length) {
            File file = fileArr[i];
            if (file.getName().contains("Crash")) {
                i++;
            } else if (!file.delete()) {
                return true;
            } else {
                b.c("AppLogApi/LogUtil", "Logzips folder is larger than 1.8M, and the first file is deleted. ");
                return true;
            }
        }
        return false;
    }

    public static boolean a(File[] fileArr, File file) {
        return b(fileArr, file);
    }

    public static int b(File[] fileArr) {
        String str;
        String str2;
        int length = fileArr.length;
        int i = 0;
        for (File file : fileArr) {
            String path = file.getPath();
            if (!path.endsWith(".log") && !path.endsWith(".zip")) {
                if (file.delete()) {
                    i++;
                    str = "HiAnalytics/logServer";
                    str2 = "del filter file :";
                } else {
                    str = "HiAnalytics/logServer";
                    str2 = "del file failed.";
                }
                b.b(str, str2);
            }
        }
        return length - i;
    }

    public static String b() {
        return new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public static void b(String str) {
        if (!TextUtils.isEmpty(str) && !new File(str).delete()) {
            b.d("AppLogApi/LogUtil", "delete file fail");
        }
    }

    public static boolean b(Context context) {
        boolean z = false;
        if (context == null) {
            b.d("AppLogApi/LogUtil", "No init of logServer!");
            return false;
        }
        SharedPreferences b = c.b(context);
        long currentTimeMillis = System.currentTimeMillis();
        long longValue = ((Long) c.b(b, "autocheck_tenminstarttime", -1L)).longValue();
        b.b("AppLogApi/LogUtil", "checkTimeOver beforeTimeMillis : " + longValue);
        if (-1 == longValue || (currentTimeMillis - longValue > 600000 && c(context))) {
            c.a(b, "autocheck_tenminstarttime", Long.valueOf(currentTimeMillis));
            b.b("AppLogApi/LogUtil", "setTenMinAutoCheckTime!");
            z = true;
        }
        b.c("AppLogApi/LogUtil", "checkTimeOver " + z);
        return z;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:46:0x0087=Splitter:B:46:0x0087, B:64:0x00a8=Splitter:B:64:0x00a8} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized boolean b(java.io.File[] r20, java.io.File r21) {
        /*
            r0 = r20
            java.lang.Class<com.huawei.hianalytics.log.e.f> r1 = com.huawei.hianalytics.log.e.f.class
            monitor-enter(r1)
            r2 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r2]     // Catch:{ all -> 0x00b0 }
            r3 = 0
            r5 = 5
            r6 = 0
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ FileNotFoundException -> 0x0095, all -> 0x0091 }
            r8 = r21
            r7.<init>(r8)     // Catch:{ FileNotFoundException -> 0x0095, all -> 0x0091 }
            java.util.zip.ZipOutputStream r8 = new java.util.zip.ZipOutputStream     // Catch:{ FileNotFoundException -> 0x0096 }
            r8.<init>(r7)     // Catch:{ FileNotFoundException -> 0x0096 }
            if (r0 == 0) goto L_0x0087
            int r9 = r0.length     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            if (r9 <= 0) goto L_0x0087
            int r9 = r0.length     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            r10 = 0
            r11 = 0
        L_0x0020:
            if (r10 >= r9) goto L_0x0087
            r12 = r0[r10]     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            long r13 = r12.length()     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            double r13 = (double) r13     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            r15 = 4700857291049323725(0x413ccccccccccccd, double:1887436.8)
            int r17 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1))
            if (r17 <= 0) goto L_0x0040
            boolean r12 = r12.delete()     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            if (r12 == 0) goto L_0x007c
            java.lang.String r12 = "AppLogApi/LogUtil"
            java.lang.String r13 = "Delete a file larger than 1.8M"
            com.huawei.hianalytics.g.b.b(r12, r13)     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            goto L_0x007c
        L_0x0040:
            java.io.FileInputStream r13 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            r13.<init>(r12)     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            java.util.zip.ZipEntry r14 = new java.util.zip.ZipEntry     // Catch:{ IOException -> 0x0073 }
            java.lang.String r4 = r12.getName()     // Catch:{ IOException -> 0x0073 }
            r14.<init>(r4)     // Catch:{ IOException -> 0x0073 }
            r8.putNextEntry(r14)     // Catch:{ IOException -> 0x0073 }
            long r18 = r12.length()     // Catch:{ IOException -> 0x0073 }
            long r3 = (long) r11     // Catch:{ IOException -> 0x0073 }
            long r3 = r3 + r18
            double r3 = (double) r3     // Catch:{ IOException -> 0x0073 }
            int r12 = (r3 > r15 ? 1 : (r3 == r15 ? 0 : -1))
            if (r12 > 0) goto L_0x0068
        L_0x005d:
            int r3 = r13.read(r2)     // Catch:{ IOException -> 0x0073 }
            if (r3 <= 0) goto L_0x0068
            int r11 = r11 + r3
            r8.write(r2, r6, r3)     // Catch:{ IOException -> 0x0073 }
            goto L_0x005d
        L_0x0068:
            r8.flush()     // Catch:{ IOException -> 0x0073 }
            r3 = 1
        L_0x006c:
            com.huawei.hianalytics.log.e.d.a((int) r3, (java.io.Closeable) r13)     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            goto L_0x007c
        L_0x0070:
            r0 = move-exception
            r2 = 1
            goto L_0x007f
        L_0x0073:
            java.lang.String r3 = "AppLogApi/LogUtil"
            java.lang.String r4 = "createLogZipWithLock() Stream Exception!"
            com.huawei.hianalytics.g.b.d(r3, r4)     // Catch:{ all -> 0x0070 }
            r3 = 1
            goto L_0x006c
        L_0x007c:
            int r10 = r10 + 1
            goto L_0x0020
        L_0x007f:
            com.huawei.hianalytics.log.e.d.a((int) r2, (java.io.Closeable) r13)     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
            throw r0     // Catch:{ FileNotFoundException -> 0x0085, all -> 0x0083 }
        L_0x0083:
            r0 = move-exception
            goto L_0x00a8
        L_0x0085:
            r3 = r8
            goto L_0x0096
        L_0x0087:
            com.huawei.hianalytics.log.e.d.a((int) r5, (java.io.Closeable) r8)     // Catch:{ all -> 0x00b0 }
            r2 = 6
            com.huawei.hianalytics.log.e.d.a((int) r2, (java.io.Closeable) r7)     // Catch:{ all -> 0x00b0 }
            monitor-exit(r1)
            r1 = 1
            return r1
        L_0x0091:
            r0 = move-exception
            r7 = r3
            r8 = r7
            goto L_0x00a8
        L_0x0095:
            r7 = r3
        L_0x0096:
            java.lang.String r0 = "HiAnalytics/logServer"
            java.lang.String r2 = "checkUploadLog,file not found !"
            com.huawei.hianalytics.g.b.c(r0, r2)     // Catch:{ all -> 0x00a6 }
            com.huawei.hianalytics.log.e.d.a((int) r5, (java.io.Closeable) r3)     // Catch:{ all -> 0x00b0 }
            r2 = 6
            com.huawei.hianalytics.log.e.d.a((int) r2, (java.io.Closeable) r7)     // Catch:{ all -> 0x00b0 }
            monitor-exit(r1)
            return r6
        L_0x00a6:
            r0 = move-exception
            r8 = r3
        L_0x00a8:
            com.huawei.hianalytics.log.e.d.a((int) r5, (java.io.Closeable) r8)     // Catch:{ all -> 0x00b0 }
            r2 = 6
            com.huawei.hianalytics.log.e.d.a((int) r2, (java.io.Closeable) r7)     // Catch:{ all -> 0x00b0 }
            throw r0     // Catch:{ all -> 0x00b0 }
        L_0x00b0:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.log.e.f.b(java.io.File[], java.io.File):boolean");
    }

    private static String c() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001f, code lost:
        r4 = r4.getActiveNetworkInfo();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean c(android.content.Context r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L_0x0033
            android.content.pm.PackageManager r1 = r4.getPackageManager()
            java.lang.String r2 = "android.permission.ACCESS_NETWORK_STATE"
            java.lang.String r3 = r4.getPackageName()
            int r1 = r1.checkPermission(r2, r3)
            if (r1 == 0) goto L_0x0014
            goto L_0x0033
        L_0x0014:
            java.lang.String r1 = "connectivity"
            java.lang.Object r4 = r4.getSystemService(r1)
            android.net.ConnectivityManager r4 = (android.net.ConnectivityManager) r4
            if (r4 != 0) goto L_0x001f
            return r0
        L_0x001f:
            android.net.NetworkInfo r4 = r4.getActiveNetworkInfo()
            if (r4 == 0) goto L_0x0032
            boolean r1 = r4.isConnected()
            if (r1 == 0) goto L_0x0032
            boolean r4 = r4.isAvailable()
            if (r4 == 0) goto L_0x0032
            r0 = 1
        L_0x0032:
            return r0
        L_0x0033:
            java.lang.String r4 = "HiAnalytics/logServer"
            java.lang.String r1 = "not have network state phone permission!"
            com.huawei.hianalytics.g.b.c(r4, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hianalytics.log.e.f.c(android.content.Context):boolean");
    }
}
